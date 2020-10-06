package com.isuo.yw2application.view.main.task.increment.detail;

import android.annotation.SuppressLint;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.isuo.yw2application.R;
import com.isuo.yw2application.app.Yw2Application;
import com.isuo.yw2application.common.ConstantStr;
import com.isuo.yw2application.mode.bean.work.IncrementBean;
import com.isuo.yw2application.utils.CountDownTimerUtils;
import com.isuo.yw2application.utils.MediaPlayerManager;
import com.isuo.yw2application.view.base.BaseActivity;
import com.isuo.yw2application.widget.ShowImageLayout;
import com.sito.library.utils.DataUtil;

import java.text.MessageFormat;

/**
 * 展示专项工作
 * Created by zhangan on 2017/10/11.
 */

public class IncrementDetailActivity extends BaseActivity implements IncrementDetailContract.View {


    private IncrementBean data;
    private boolean isIncrementPlay, isWorkPlay;
    private IncrementDetailContract.Presenter mPresenter;
    private TextView incrementTimeTv;
    private CountDownTimerUtils mIncrementTimer, mWorkTimer;
    private AnimationDrawable incrementAnim, workAnim;
    private String incrementSoundUrl, workSoundUrl;
    private int incrementSoundTime, workSoundTime;
    private TextView workSoundTimeTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data = getIntent().getParcelableExtra(ConstantStr.KEY_BUNDLE_OBJECT);
        long workId = getIntent().getLongExtra(ConstantStr.KEY_BUNDLE_LONG, -1);
        if (data == null && workId == -1) {
            finish();
            return;
        }
        setLayoutAndToolbar(R.layout.activity_increment_detail, "专项工作详情");
        new IncrementDetailPresenter(Yw2Application.getInstance().getWorkRepositoryComponent().getRepository(), this);
        if (data == null) {
            mPresenter.getIncrementData(workId);
        } else {
            initData();
        }
    }

    private void initData() {
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
                .setText(Yw2Application.getInstance().getMapOption().get("1").get(String.valueOf(data.getWorkType())));
        TextView tv_increment_state = findViewById(R.id.tv_increment_state);
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
        TextView tv_equip_name = findViewById(R.id.tv_equip_name);
        if (data.getEquipment() == null) {
            tv_equip_name.setVisibility(View.GONE);
        } else {
            if (TextUtils.isEmpty(data.getEquipment().getEquipmentSn())) {
                tv_equip_name.setText(data.getEquipment().getEquipmentName());
            } else {
                tv_equip_name.setText(MessageFormat.format("{0}({1})", data.getEquipment().getEquipmentName(), data.getEquipment().getEquipmentSn()));
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
        incrementTimeTv = findViewById(R.id.id_increment_time);
        incrementTimeTv.setText(MessageFormat.format("{0}s", incrementSoundTime));
        ShowImageLayout ll_increment_image = findViewById(R.id.ll_increment_image);
        ll_increment_image.showImage(data.getXworkImages().split(","));
        if (incrementSoundTime == 0 || TextUtils.isEmpty(incrementSoundUrl)) {
            incrementTimeTv.setVisibility(View.GONE);
        } else {
            incrementTimeTv.setVisibility(View.VISIBLE);
        }
    }

    private void setLayout1() {
        ((TextView) findViewById(R.id.tv_increment_name_1))
                .setText(Yw2Application.getInstance().getMapOption().get("1").get(String.valueOf(data.getWorkType())));
        TextView tv_increment_state = findViewById(R.id.tv_increment_state_1);
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
        TextView tv_equip_name = findViewById(R.id.tv_equip_name_1);
        if (data.getEquipment() == null) {
            tv_equip_name.setVisibility(View.GONE);
        } else {
            if (TextUtils.isEmpty(data.getEquipment().getEquipmentSn())) {
                tv_equip_name.setText(data.getEquipment().getEquipmentName());
            } else {
                tv_equip_name.setText(String.format("%s(%s)", data.getEquipment().getEquipmentName(), data.getEquipment().getEquipmentSn()));
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
        workSoundTimeTv = findViewById(R.id.id_result_increment_time);
        workSoundTimeTv.setText(MessageFormat.format("{0}s", workSoundTime));
        findViewById(R.id.ll_result_sound).setOnClickListener(this);
        ShowImageLayout showImageLayout = findViewById(R.id.ll_result_image);
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
                if (!isIncrementPlay) {
                    if (isWorkPlay) {
                        workSoundStopPlay();
                    }
                    isIncrementPlay = true;
                    incrementTimeTv.setBackgroundResource(R.drawable.play_anim);
                    incrementAnim = (AnimationDrawable) incrementTimeTv.getBackground();
                    mIncrementTimer = new CountDownTimerUtils(incrementTimeTv, incrementSoundTime * 1000
                            , 1000, incrementSoundTime + "s", "#999999");
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
                if (!isWorkPlay) {
                    if (isIncrementPlay) {
                        incrementSoundStopPlay();
                    }
                    isWorkPlay = true;
                    workSoundTimeTv.setBackgroundResource(R.drawable.play_anim);
                    workAnim = (AnimationDrawable) workSoundTimeTv.getBackground();
                    mWorkTimer = new CountDownTimerUtils(workSoundTimeTv, workSoundTime * 1000, 1000
                            , workSoundTime + "s", "#ffffff");
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
        incrementTimeTv.setText(MessageFormat.format("{0}s", incrementSoundTime));
        incrementTimeTv.setBackgroundResource(R.drawable.voice_three);
    }

    private void workSoundStopPlay() {
        isWorkPlay = false;
        MediaPlayerManager.release();
        mWorkTimer.cancel();
        workSoundTimeTv.clearAnimation();
        workSoundTimeTv.setText(MessageFormat.format("{0}s", workSoundTime));
        workSoundTimeTv.setBackgroundResource(R.drawable.voice_three);
    }

    @Override
    public void showIncrement(IncrementBean incrementBean) {
        data = incrementBean;
        initData();
    }

    @Override
    public void showLoading() {
        showEvLoading();
    }

    @Override
    public void hideLoading() {
        hideEvLoading();
    }

    @Override
    public void noData() {

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mPresenter != null) {
            mPresenter.unSubscribe();
        }
        MediaPlayerManager.release();
        if (mWorkTimer != null) {
            mWorkTimer.cancel();
            workSoundTimeTv.setText(MessageFormat.format("{0}s", workSoundTime));
            workSoundTimeTv.setBackgroundResource(R.drawable.voice_three);
        }
        if (mIncrementTimer != null) {
            mIncrementTimer.cancel();
            incrementTimeTv.setText(MessageFormat.format("{0}s", incrementSoundTime));
            incrementTimeTv.setBackgroundResource(R.drawable.voice_three);
        }
        if (workAnim != null && workAnim.isRunning()) {
            workAnim.stop();
        }
        if (incrementAnim != null && incrementAnim.isRunning()) {
            incrementAnim.stop();
        }
    }

    @Override
    public void setPresenter(IncrementDetailContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
