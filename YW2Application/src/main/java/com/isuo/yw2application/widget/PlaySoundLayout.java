package com.isuo.yw2application.widget;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.isuo.yw2application.R;
import com.isuo.yw2application.utils.CountDownTimerUtils;
import com.isuo.yw2application.utils.MediaPlayerManager;

/**
 * Created by zhangan on 2017-08-01.
 */

public class PlaySoundLayout extends LinearLayout implements View.OnClickListener {

    //view
    private TextView titleNameTv;
    private TextView soundTimeTv;
    private TextView soundContentTv;

    //data
    private boolean isPlaying;
    private String voiceUrl;
    private int soundTime;
    private CountDownTimerUtils mTimer;
    private AnimationDrawable animation;

    public PlaySoundLayout(Context context) {
        super(context);
        init(context);
    }

    public PlaySoundLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.layout_play_sound, this);
        titleNameTv = (TextView) findViewById(R.id.tv_title);
        LinearLayout playSoundLL = (LinearLayout) findViewById(R.id.ll_play_sound);
        soundTimeTv = (TextView) findViewById(R.id.id_sound_time);
        soundContentTv = (TextView) findViewById(R.id.tv_play_text);
        playSoundLL.setOnClickListener(this);
    }

    public void setContent(String voiceUrl, int soundTime, String title, String content) {
        this.voiceUrl = voiceUrl;
        this.soundTime = soundTime;
        titleNameTv.setText(title + ":");
        soundTimeTv.setText(soundTime + "''");
        soundTimeTv.setBackgroundResource(R.drawable.play_anim);
        soundContentTv.setText(content);
        if (soundTime == 0 || TextUtils.isEmpty(voiceUrl)) {
            soundTimeTv.setVisibility(GONE);
        } else {
            soundTimeTv.setVisibility(VISIBLE);
        }
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ll_play_sound) {
            if (TextUtils.isEmpty(voiceUrl)) {
                return;
            }
            if (onPlaySoundListener != null) {
                onPlaySoundListener.onPlaySound();
            }
            if (!isPlaying) {
                isPlaying = true;
                //开始动画
                animation = (AnimationDrawable) soundTimeTv.getBackground();
                //播放故障语音
                mTimer = new CountDownTimerUtils(soundTimeTv, soundTime * 1000, 1000
                        , soundTime + "''", "#ffffff");
                MediaPlayerManager.playSound(voiceUrl, new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        cancelPlay();
                    }
                }, new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mp.start();
                        mTimer.start();
                        animation.start();
                    }
                });
            } else {
                cancelPlay();
            }

        }
    }

    public void cancelPlay() {
        if (!isPlaying) {
            return;
        }
        mTimer.cancel();
        isPlaying = false;
        MediaPlayerManager.release();
        animation.selectDrawable(0);
        animation.stop();
        soundTimeTv.clearAnimation();
        soundTimeTv.setText(soundTime + "''");
    }

    public void setOnPlaySoundListener(OnPlaySoundListener onPlaySoundListener) {
        this.onPlaySoundListener = onPlaySoundListener;
    }

    private OnPlaySoundListener onPlaySoundListener;

    public interface OnPlaySoundListener {
        void onPlaySound();
    }
}
