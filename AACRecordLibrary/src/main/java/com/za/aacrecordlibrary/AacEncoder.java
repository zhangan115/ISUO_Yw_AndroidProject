package com.za.aacrecordlibrary;

import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

/**
 * AAC 编码器（纯 Java 实现，替代原来的 FAAC JNI 库 libFaacEncoder.so）。
 *
 * <p>对外暴露的方法签名与旧实现保持一致，{@link AACRecord} 无需改动：
 * {@link #AACEncoderOpen(int, int)} / {@link #AACEncoderEncode(int, byte[], int)} /
 * {@link #AACEncoderClose(int)}。
 *
 * <p>输出格式与原 FAAC 配置保持一致：ADTS 裸流（{@code .aac} 可直接播放/上传），
 * 默认 16 kHz、单声道、32 kbps、AAC-LC。每帧手工补 7 字节 ADTS 头，
 * 因为 MediaCodec 输出的是不带头的 raw AAC 帧。
 */
public class AacEncoder {

    private static final String TAG = "AacEncoder";

    /** AAC-LC 每帧固定 1024 个采样，{@link AACRecord} 用它计算一次喂入的字节数 */
    public int inputSamples = 1024;
    public int bitRate = 32000;

    /** ADTS 头 7 字节 */
    private static final int ADTS_HEADER_SIZE = 7;

    private MediaCodec codec;
    private MediaCodec.BufferInfo bufferInfo;
    /** 尚未凑够一帧的 PCM 数据 */
    private byte[] pending = new byte[0];
    private int sampleRate;
    private int channels;
    private final ByteArrayOutputStream encoded = new ByteArrayOutputStream();

    static {
        // 旧实现这里 System.loadLibrary("FaacEncoder")，现在改为系统 MediaCodec，不再需要任何 .so
    }

    /**
     * 创建并启动编码器。
     *
     * @return 0 表示成功，-1 表示该采样率不支持（{@link AACRecord} 会换下一个采样率重试）
     */
    public int AACEncoderOpen(int sampleRate, int channels) {
        try {
            this.sampleRate = sampleRate;
            this.channels = channels;

            int frameBytes = inputSamples * 2 * channels;
            MediaFormat format = MediaFormat.createAudioFormat(
                    MediaFormat.MIMETYPE_AUDIO_AAC, sampleRate, channels);
            format.setInteger(MediaFormat.KEY_AAC_PROFILE,
                    MediaCodecInfo.CodecProfileLevel.AACObjectLC);
            format.setInteger(MediaFormat.KEY_BIT_RATE, bitRate);
            format.setInteger(MediaFormat.KEY_MAX_INPUT_SIZE, frameBytes * 4);
            format.setInteger(MediaFormat.KEY_CHANNEL_MASK,
                    channels == 1 ? android.media.AudioFormat.CHANNEL_IN_MONO
                            : android.media.AudioFormat.CHANNEL_IN_STEREO);

            codec = MediaCodec.createEncoderByType(MediaFormat.MIMETYPE_AUDIO_AAC);
            codec.configure(format, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
            codec.start();
            bufferInfo = new MediaCodec.BufferInfo();
            pending = new byte[0];
            encoded.reset();
            return 0;
        } catch (Throwable e) {
            Log.e(TAG, "AAC 编码器初始化失败: " + sampleRate + "Hz/" + channels + "ch", e);
            releaseQuietly();
            return -1;
        }
    }

    /**
     * 编码一段 PCM（16 bit 小端）。
     *
     * @return 本次产生的 ADTS 帧数据，可能为空数组
     */
    public synchronized byte[] AACEncoderEncode(int hEncoder, byte[] inputBuffer, int inputBufferSize) {
        encoded.reset();
        if (codec == null || inputBuffer == null || inputBufferSize <= 0) {
            return new byte[0];
        }
        try {
            // 追加到待编码缓冲区
            byte[] merged = new byte[pending.length + inputBufferSize];
            System.arraycopy(pending, 0, merged, 0, pending.length);
            System.arraycopy(inputBuffer, 0, merged, pending.length, inputBufferSize);
            pending = merged;

            int frameBytes = inputSamples * 2 * channels;
            int offset = 0;
            while (pending.length - offset >= frameBytes) {
                feedOneFrame(pending, offset, frameBytes);
                offset += frameBytes;
                drainOutput();
            }
            if (offset > 0) {
                byte[] rest = new byte[pending.length - offset];
                System.arraycopy(pending, offset, rest, 0, rest.length);
                pending = rest;
            }
            return encoded.toByteArray();
        } catch (Throwable e) {
            Log.e(TAG, "AAC 编码失败", e);
            return new byte[0];
        }
    }

    public int AACEncoderClose(int hEncoder) {
        if (codec == null) {
            return -1;
        }
        try {
            // 送 EOS，把编码器里剩余的帧冲出来（不足一帧的尾部会被丢弃，约 64ms）
            int inIndex = codec.dequeueInputBuffer(10000);
            if (inIndex >= 0) {
                codec.queueInputBuffer(inIndex, 0, 0, 0, MediaCodec.BUFFER_FLAG_END_OF_STREAM);
            }
            drainOutput();
        } catch (Throwable e) {
            Log.e(TAG, "AAC 编码器收尾异常", e);
        } finally {
            releaseQuietly();
        }
        return 0;
    }

    private void feedOneFrame(byte[] data, int offset, int size) {
        int inIndex = codec.dequeueInputBuffer(10000);
        if (inIndex < 0) {
            return;
        }
        ByteBuffer inBuf = codec.getInputBuffer(inIndex);
        if (inBuf == null) {
            return;
        }
        inBuf.clear();
        inBuf.put(data, offset, size);
        codec.queueInputBuffer(inIndex, 0, size, System.nanoTime() / 1000, 0);
    }

    private void drainOutput() {
        while (true) {
            int outIndex = codec.dequeueOutputBuffer(bufferInfo, 0);
            if (outIndex == MediaCodec.INFO_TRY_AGAIN_LATER) {
                return;
            }
            if (outIndex == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                MediaFormat outFormat = codec.getOutputFormat();
                // 以编码器实际输出为准，保证 ADTS 头里的采样率/声道正确
                sampleRate = outFormat.getInteger(MediaFormat.KEY_SAMPLE_RATE);
                channels = outFormat.getInteger(MediaFormat.KEY_CHANNEL_COUNT);
                continue;
            }
            if (outIndex < 0) {
                return;
            }
            ByteBuffer outBuf = codec.getOutputBuffer(outIndex);
            if (outBuf != null && bufferInfo.size > 0
                    && (bufferInfo.flags & MediaCodec.BUFFER_FLAG_CODEC_CONFIG) == 0
                    && (bufferInfo.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) == 0) {
                byte[] frame = new byte[bufferInfo.size];
                outBuf.position(bufferInfo.offset);
                outBuf.get(frame, 0, bufferInfo.size);
                encoded.write(addAdtsHeader(frame), 0, frame.length + ADTS_HEADER_SIZE);
            }
            codec.releaseOutputBuffer(outIndex, false);
        }
    }

    /** 给 raw AAC 帧补 7 字节 ADTS 头 */
    private byte[] addAdtsHeader(byte[] rawFrame) {
        int frameLength = rawFrame.length + ADTS_HEADER_SIZE;
        byte[] out = new byte[frameLength];

        int profile = 2; // AAC-LC => object type - 1
        int freqIndex = frequencyIndex(sampleRate);
        int channelConfig = channels;

        out[0] = (byte) 0xFF;
        out[1] = (byte) 0xF1; // MPEG-4, Layer 0, 无 CRC
        out[2] = (byte) (((profile << 6) & 0xC0)
                | ((freqIndex << 2) & 0x3C)
                | ((channelConfig >> 2) & 0x01));
        out[3] = (byte) (((channelConfig & 0x03) << 6)
                | ((frameLength >> 11) & 0x03));
        out[4] = (byte) ((frameLength >> 3) & 0xFF);
        out[5] = (byte) (((frameLength & 0x07) << 5) | 0x1F);
        out[6] = (byte) 0xFC;

        System.arraycopy(rawFrame, 0, out, ADTS_HEADER_SIZE, rawFrame.length);
        return out;
    }

    private static int frequencyIndex(int rate) {
        switch (rate) {
            case 96000: return 0;
            case 88200: return 1;
            case 64000: return 2;
            case 48000: return 3;
            case 44100: return 4;
            case 32000: return 5;
            case 24000: return 6;
            case 22050: return 7;
            case 16000: return 8;
            case 12000: return 9;
            case 11025: return 10;
            case 8000: return 11;
            case 7350: return 12;
            default: return 8; // 兜底按 16 kHz
        }
    }

    private void releaseQuietly() {
        if (codec != null) {
            try {
                codec.stop();
            } catch (Throwable ignore) {
            }
            try {
                codec.release();
            } catch (Throwable ignore) {
            }
            codec = null;
        }
    }
}
