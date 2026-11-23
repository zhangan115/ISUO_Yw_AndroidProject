package com.za.aacrecordlibrary;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Handler;
import android.util.Log;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class AACRecord implements Runnable {

    /**
     * 从默认的16000开始，若失败则会用其余的采样率
     */
    private static int[] sampleRates = {16000, 44100, 22050, 11025, 8000, 4000};

    private byte[] tempBuffer;
    private int hAac;
    private int bufferSize, minBufferSize;
    private boolean isStart = false;

    private AacEncoder aacEncoder;
    private AudioRecord recordInstance;

    private FileOutputStream fos;
    private IRecordListener iRecordListener;

    public AACRecord(String fileName) {
        try {
            fos = new FileOutputStream(fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置默认采样率，默认16000
     */
    public AACRecord sampleRateInHz(int sampleRateInHz) {
        sampleRates[0] = sampleRateInHz;
        return this;
    }

    public void start() {
        isStart = true;
        Thread t = new Thread(this);
        t.start();
    }

    public void start(IRecordListener iRecordListener) {
        isStart = true;
        this.iRecordListener = iRecordListener;
        Thread t = new Thread(this);
        t.start();
    }

    public void stop() {
        isStart = false;
    }

    @Override
    public void run() {
        for (int sampleRate : sampleRates) {
            try {
                aacEncoder = new AacEncoder();
                hAac = aacEncoder.AACEncoderOpen(sampleRate, 1);
                if (hAac != 0) {
                    // 该采样率创建编码器失败，换下一个采样率重试（沿用原有降级策略）
                    continue;
                }
                minBufferSize = AudioRecord.getMinBufferSize(sampleRate, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
                if (minBufferSize < 2048)
                    minBufferSize = 2048;
                bufferSize = aacEncoder.inputSamples * 16 / 8;
                tempBuffer = new byte[minBufferSize];
                recordInstance = new AudioRecord(MediaRecorder.AudioSource.MIC, sampleRate, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, minBufferSize);//bufferSize);
                recordInstance.startRecording();
                break;
            } catch (Exception e) {
                e.printStackTrace();
                // 本次尝试失败，释放已创建的编码器，避免泄漏
                if (aacEncoder != null) {
                    aacEncoder.AACEncoderClose(hAac);
                }
            }
        }
        if (recordInstance == null) {
            // 所有采样率都没能创建录音/编码器，直接结束，避免下面空指针，并把已打开的资源收掉
            isStart = false;
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException ignore) {
            }
            if (aacEncoder != null) {
                aacEncoder.AACEncoderClose(hAac);
            }
            return;
        }
        while (isStart) {
            int bufferRead = recordInstance.read(tempBuffer, 0, minBufferSize);
            if (bufferRead > 0) {
                int n = minBufferSize / bufferSize;//算出需要循环几次取值
                for (int i = 0; i < n; i++) {
                    byte[] temp = new byte[bufferSize];
                    System.arraycopy(tempBuffer, i * bufferSize, temp, 0, bufferSize);
                    byte[] ret = aacEncoder.AACEncoderEncode(hAac, temp, temp.length);
                    try {
                        if (ret != null && ret.length > 0) {
                            fos.write(ret);
                        }
                        // 注意：语音识别（讯飞 writeAudio）依赖每一段 PCM，必须无条件回调。
                        // MediaCodec 是异步编码，前几帧会返回空数组，若把回调挂在 ret.length != 0
                        // 上就会丢音频，导致识别结果缺失。
                        if (iRecordListener != null) {
                            iRecordListener.onRecord(temp, 0, temp.length);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            //计算出音量分贝值
            if (bufferRead > 0) {
                long v = 0;
                for (byte aTempBuffer : tempBuffer) {
                    v += aTempBuffer * aTempBuffer;//计算平方和
                }
                double mean = v / (double) bufferRead;//平方和/总长度，得到音量大小
                volume = 10 * Math.log10(mean);//转换公式
            }
        }
        try {
            recordInstance.stop();
            recordInstance.release();
            recordInstance = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        aacEncoder.AACEncoderClose(hAac);
        try {
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private double volume = 0;

    /**
     * 实时获取音量大小
     */
    public double getVolume() {
        return volume;
    }

    public interface IRecordListener {
        void onRecord(byte[] bytes, int offset, int size);
    }

}
