package com.isuo.yw2application.utils;

import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.isuo.yw2application.app.Yw2Application;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * @param
 * @author ldm
 * @description 播放声音工具类
 * @time 2016/6/25 11:29
 */
public class MediaPlayerManager {
//    public static final String recordPath = "/data/data/com.sito.customer/files/";
    public static final String recordPath = Yw2Application.getInstance().voiceCacheFile();
    //播放音频API类：MediaPlayer
    private static MediaPlayer mMediaPlayer;
    //是否暂停
    private static boolean isPause;
    public static String newFilename;

    /**
     * @param filePath：文件路径 onCompletionListener：播放完成监听
     * @description 播放声音
     * @author ldm
     * @time 2016/6/25 11:30
     */
    public static void playSound(String filePath, MediaPlayer.OnCompletionListener onCompletionListener) {
        if (TextUtils.isEmpty(filePath)) {
            return;
        }
        try {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setAudioStreamType(android.media.AudioManager.STREAM_MUSIC);
            mMediaPlayer.setOnCompletionListener(onCompletionListener);
            mMediaPlayer.setDataSource(filePath);
            mMediaPlayer.prepareAsync();
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                }
            });
            //设置一个error监听器
            mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {

                public boolean onError(MediaPlayer arg0, int arg1, int arg2) {
                    Yw2Application.getInstance().showToast("播放错误");
                    return false;
                }
            });
        } catch (Exception e) {
            Yw2Application.getInstance().showToast("播放错误");
        }
    }

    // Thread是一个类，必须继承
    public static class myThread extends Thread {
        private String filename;
        private String filePath;

        private myThread(String filename, String filepath) {
            this.filename = filename;
            this.filePath = filepath;
        }

        @Override
        public void run() {
            super.run();
            // 写子线程中的操作
            try {
                File file = new File(filename);
                file.createNewFile();
                if (file.exists()) {
                    URL url = new URL(filePath);
                    // 打开连接
                    URLConnection con = url.openConnection();
                    //获得文件的长度
                    int contentLength = con.getContentLength();
                    // 输入流
                    InputStream is = con.getInputStream();
                    // 1K的数据缓冲
                    byte[] bs = new byte[1024];
                    // 读取到的数据长度
                    int len;
                    // 输出的文件流
                    OutputStream os = new FileOutputStream(filename);
                    // 开始读取
                    while ((len = is.read(bs)) != -1) {
                        os.write(bs, 0, len);
                    }
                    // 完毕，关闭所有链接
                    os.close();
                    is.close();
                    handler.sendEmptyMessage(0);
                }
                // 构造URL


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                //下载成功
            }
        }
    };

    public static void playSound(String filePath, MediaPlayer.OnCompletionListener onCompletionListener, MediaPlayer.OnPreparedListener onPreparedListener) {
        if (TextUtils.isEmpty(filePath)) {
            return;
        }
        newFilename = filePath.substring(filePath.lastIndexOf("/") + 1);
        newFilename = recordPath + newFilename;
        if (!new File(newFilename).exists()) {//变下载边播放 //此时比较耗费流量 下载一次 播放一次 下载后播放本地 后续优化
            //文件不存在 去下载
            new myThread(newFilename, filePath).start();
            try {
                mMediaPlayer = new MediaPlayer();
                mMediaPlayer.setAudioStreamType(android.media.AudioManager.STREAM_MUSIC);
                mMediaPlayer.setOnCompletionListener(onCompletionListener);
                mMediaPlayer.setOnPreparedListener(onPreparedListener);
                mMediaPlayer.setDataSource(filePath);
                mMediaPlayer.prepareAsync();
                //设置一个error监听器
                mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {

                    public boolean onError(MediaPlayer arg0, int arg1, int arg2) {
//                    mMediaPlayer.reset();
                        Yw2Application.getInstance().showToast("播放错误");
                        return false;
                    }
                });
//            mMediaPlayer.start();
            } catch (Exception e) {
                Yw2Application.getInstance().showToast("播放错误");
            }
        } else {
            try {
                mMediaPlayer = new MediaPlayer();
                mMediaPlayer.setAudioStreamType(android.media.AudioManager.STREAM_MUSIC);
                mMediaPlayer.setOnCompletionListener(onCompletionListener);
                mMediaPlayer.setOnPreparedListener(onPreparedListener);
                mMediaPlayer.setDataSource(newFilename);
                mMediaPlayer.prepareAsync();
                //设置一个error监听器
                mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {

                    public boolean onError(MediaPlayer arg0, int arg1, int arg2) {
                        Yw2Application.getInstance().showToast("播放错误");
                        return false;
                    }
                });
            } catch (Exception e) {
                Yw2Application.getInstance().showToast("播放错误");
            }
        }
    }

    /**
     * @param
     * @description 暂停播放
     * @author ldm
     * @time 2016/6/25 11:31
     */
    public static void pause() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) { //正在播放的时候
            mMediaPlayer.pause();
            isPause = true;
        }
    }

    /**
     * @param
     * @description 重新播放
     * @author ldm
     * @time 2016/6/25 11:31
     */
    public static void resume() {
        if (mMediaPlayer != null && isPause) {
            mMediaPlayer.start();
            isPause = false;
        }
    }

    /**
     * @param
     * @description 释放操作
     * @author ldm
     * @time 2016/6/25 11:32
     */
    public static void release() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }
}