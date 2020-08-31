package com.sito.evpro.inspection.view.repair.increment.detail;

import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.sito.evpro.inspection.R;
import com.sito.evpro.inspection.app.InspectionApp;
import com.sito.evpro.inspection.common.ConstantStr;
import com.sito.evpro.inspection.mode.bean.increment.IncrementBean;
import com.sito.evpro.inspection.utils.CountDownTimerUtils;
import com.sito.evpro.inspection.view.BaseActivity;
import com.sito.evpro.inspection.view.speech.voice.MediaPlayerManager;
import com.sito.evpro.inspection.widget.ShowImageLayout;
import com.sito.library.utils.DataUtil;

import java.text.MessageFormat;

/**
 * 展示专项工作
 * Created by zhangan on 2017/10/11.
 */

public class IncrementDetailActivity extends BaseActivity {

    private TextView incrementTimeTv;
    private IncrementBean data;
    private boolean isIncrementPlay, isWorkPlay;
    private CountDownTimerUtils mIncrementTimer, mWorkTimer;
    private AnimationDrawable incrementAnim, workAnim;
    private String incrementSoundUrl, workSoundUrl;
    private int incrementSoundTime, workSoundTime;
    private TextView workSoundTimeTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data = getIntent().getParcelableExtra(ConstantStr.KEY_BUNDLE_OBJECT);
        if (data == null) {
            finish();
            return;
        }
        setLayoutAndToolbar(R.layout.activity_increment_detail, "专项工作详情");
        if (data.getWorkIssued() == 0) {
            findViewById(R.id.layout_1).setVisibility(View.VISIBLE);
            setLayout1();
        } else {
            findViewById(R.id.layout).setVisibility(View.VISIBLE);
            incrementSoundUrl = data.getXworkSound();
            incrementSoundTime = data.getXsoundTimescale();
            setLayout();
        }
        if (data.getWorkState() == 2) {
            findViewById(R.id.layout_result).setVisibility(View.VISIBLE);
            workSoundTime = data.getSoundTimescale();
            workSoundUrl = data.getWorkSound();
            setResult();
        }
    }

    private void setLayout() {
        ((TextView) findViewById(R.id.tv_increment_name))
                .setText(InspectionApp.getInstance().getMapOption().get("1").get(String.valueOf(data.getWorkType())));
        TextView tv_increment_state = (TextView) findViewById(R.id.tv_increment_state);
        String state;
        switch (data.getWorkState()) {
            case 0:
                tv_increment_state.setTextColor(findColorById(R.color.color_not_start));
                state = "待开始";
                break;
            case 1:
                tv_increment_state.setTextColor(findColorById(R.color.color_start));
                state = "进行中";
                break;
            default:
                tv_increment_state.setTextColor(findColorById(R.color.color_finish));
                state = "已完成";
                break;
        }
        tv_increment_state.setText(state);
        TextView tv_equip_name = (TextView) findViewById(R.id.tv_equip_name);
        if (data.getEquipment() == null) {
            tv_equip_name.setVisibility(View.GONE);
        } else {
            if (TextUtils.isEmpty(data.getEquipment().getEquipmentSn())) {
                tv_equip_name.setText(data.getEquipment().getEquipmentName());
            } else {
                tv_equip_name.setText(data.getEquipment().getEquipmentName() + "(" + data.getEquipment().getEquipmentSn() + ")");
            }
            tv_equip_name.setVisibility(View.VISIBLE);
        }
        if (data.getStartTime() != 0)
            ((TextView) findViewById(R.id.tv_plan_start_time)).setText(DataUtil.timeFormat(data.getStartTime(), null));
        if (data.getEndTime() != 0)
            ((TextView) findViewById(R.id.tv_plan_end_time)).setText(DataUtil.timeFormat(data.getEndTime(), null));
        if (!TextUtils.isEmpty(data.getUserNames())) {
            ((TextView) findViewById(R.id.tv_plan_user)).setText(data.getUserNames());
        }
        if (data.getIssuedUser() != null) {
            ((TextView) findViewById(R.id.tv_from)).setText(data.getIssuedUser().getRealName());
        }
        findViewById(R.id.ll_play_task_sound).setOnClickListener(this);
        ((TextView) findViewById(R.id.tv_increment_play_text)).setText(data.getXworkContent());
        incrementTimeTv = (TextView) findViewById(R.id.id_increment_time);
        incrementTimeTv.setText(MessageFormat.format("{0}''''", String.valueOf(data.getXsoundTimescale())));
        ShowImageLayout ll_increment_image = (ShowImageLayout) findViewById(R.id.ll_increment_image);
        ll_increment_image.showImage(data.getXworkImages().split(","));
        if (incrementSoundTime == 0 || TextUtils.isEmpty(incrementSoundUrl)) {
            incrementTimeTv.setVisibility(View.GONE);
        } else {
            incrementTimeTv.setVisibility(View.VISIBLE);
        }
    }

    private void setLayout1() {
        ((TextView) findViewById(R.id.tv_increment_name_1))
                .setText(InspectionApp.getInstance().getMapOption().get("1").get(String.valueOf(data.getWorkType())));
        TextView tv_increment_state = (TextView) findViewById(R.id.tv_increment_state_1);
        String state;
        switch (data.getWorkState()) {
            case 0:
                tv_increment_state.setTextColor(findColorById(R.color.color_not_start));
                state = "待开始";
                break;
            case 1:
                tv_increment_state.setTextColor(findColorById(R.color.color_start));
                state = "进行中";
                break;
            default:
                tv_increment_state.setTextColor(findColorById(R.color.color_finish));
                state = "已完成";
                break;
        }
        tv_increment_state.setText(state);
        TextView tv_equip_name = (TextView) findViewById(R.id.tv_equip_name_1);
        if (data.getEquipment() == null) {
            tv_equip_name.setVisibility(View.GONE);
        } else {
            if (TextUtils.isEmpty(data.getEquipment().getEquipmentSn())) {
                tv_equip_name.setText(data.getEquipment().getEquipmentName());
            } else {
                tv_equip_name.setText(data.getEquipment().getEquipmentName() + "(" + data.getEquipment().getEquipmentSn() + ")");
            }
            tv_equip_name.setVisibility(View.VISIBLE);
        }
        if (!TextUtils.isEmpty(data.getUserNames())) {
            ((TextView) findViewById(R.id.tv_plan_user_1)).setText(data.getUserNames());
        }
        if (data.getIssuedUser() != null) {
            ((TextView) findViewById(R.id.tv_from_1)).setText(data.getIssuedUser().getRealName());
        }
    }

    private void setResult() {
        ((TextView) findViewById(R.id.tv_result_play_text)).setText(data.getWorkContent());
        if (data.getWorkIssued() == 0) {
            ((TextView) findViewById(R.id.textView_time)).setText(DataUtil.timeFormat(data.getCreateTime(), null));
        } else {
            ((TextView) findViewById(R.id.textView_time)).setText(DataUtil.timeFormat(data.getCommitTime(), null));
        }
        workSoundTimeTv = (TextView) findViewById(R.id.id_result_increment_time);
        workSoundTimeTv.setText(String.valueOf(workSoundTime) + "''");
        findViewById(R.id.ll_result_sound).setOnClickListener(this);
        ShowImageLayout showImageLayout = (ShowImageLayout) findViewById(R.id.ll_result_image);
        showImageLayout.showImage(data.getWorkImages().split(","));
        if (workSoundTime == 0 || TextUtils.isEmpty(workSoundUrl)) {
            workSoundTimeTv.setVisibility(View.GONE);
        } else {
            workSoundTimeTv.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.ll_play_task_sound:
                if (TextUtils.isEmpty(incrementSoundUrl) || incrementSoundTime == 0) {
                    return;
                }
                if (!isIncrementPlay) {
                    if (isWorkPlay) {
                        workSoundStopPlay();
                    }
                    isIncrementPlay = true;
                    incrementTimeTv.setBackgroundResource(R.drawable.play_anim);
                    incrementAnim = (AnimationDrawable) incrementTimeTv.getBackground();
                    mIncrementTimer = new CountDownTimerUtils(incrementTimeTv, incrementSoundTime * 1000, 1000, incrementSoundTime + "''", "#ffffff");
                    MediaPlayerManager.playSound(incrementSoundUrl, new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            incrementSoundStopPlay();
                        }
                    }, new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mp.start();
                            mIncrementTimer.start();
                            incrementAnim.start();
                        }
                    });

                } else {
                    incrementSoundStopPlay();
                }
                break;
            case R.id.ll_result_sound:
                if (TextUtils.isEmpty(workSoundUrl) || workSoundTime == 0) {
                    return;
                }
                if (!isWorkPlay) {
                    if (isIncrementPlay) {
                        incrementSoundStopPlay();
                    }
                    isWorkPlay = true;
                    workSoundTimeTv.setBackgroundResource(R.drawable.play_anim);
                    workAnim = (AnimationDrawable) workSoundTimeTv.getBackground();
                    mWorkTimer = new CountDownTimerUtils(workSoundTimeTv, workSoundTime * 1000, 1000
                            , workSoundTime + "''", "#ffffff");
                    MediaPlayerManager.playSound(workSoundUrl, new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            workSoundStopPlay();
                        }
                    }, new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mp.start();
                            mWorkTimer.start();
                            workAnim.start();
                        }
                    });
                } else {
                    workSoundStopPlay();
                }
                break;
        }
    }

    private void incrementSoundStopPlay() {
        isIncrementPlay = false;
        MediaPlayerManager.release();
        mIncrementTimer.cancel();
        incrementTimeTv.clearAnimation();
        incrementTimeTv.setText(incrementSoundTime + "''");
        incrementTimeTv.setBackgroundResource(R.drawable.record_play_3);
    }

    private void workSoundStopPlay() {
        isWorkPlay = false;
        MediaPlayerManager.release();
        mWorkTimer.cancel();
        workSoundTimeTv.clearAnimation();
        workSoundTimeTv.setText(workSoundTime + "''");
        workSoundTimeTv.setBackgroundResource(R.drawable.record_play_3);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MediaPlayerManager.release();
        if (mWorkTimer != null) {
            mWorkTimer.cancel();
            workSoundTimeTv.setText(workSoundTime + "''");
            workSoundTimeTv.setBackgroundResource(R.drawable.record_play_3);
        }
        if (mIncrementTimer != null) {
            mIncrementTimer.cancel();
            incrementTimeTv.setText(incrementSoundTime + "''");
            incrementTimeTv.setBackgroundResource(R.drawable.record_play_3);
        }
        if (workAnim != null && workAnim.isRunning()) {
            workAnim.stop();
        }
        if (incrementAnim != null && incrementAnim.isRunning()) {
            incrementAnim.stop();
        }
    }
}
