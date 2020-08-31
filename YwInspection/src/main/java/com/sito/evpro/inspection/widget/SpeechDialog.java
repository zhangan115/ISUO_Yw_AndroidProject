package com.sito.evpro.inspection.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.sito.evpro.inspection.R;
import com.sito.evpro.inspection.utils.CountDownTimerUtils;

import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by Administrator on 2017/6/14.
 */

public abstract class SpeechDialog extends Dialog implements View.OnClickListener, Runnable {
    private Activity activity;
    private TextView mTime;
    private ImageView mFinish;
    private ImageView mCancel;
    private int recLen = 59;
    private Timer timer;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                timer.cancel();
                int time = 60;
                result(time);
                stop();
            } else if (msg.what == 1) {
                stop();
            }
        }
    };

    public SpeechDialog(Activity activity) {
        super(activity, R.style.dialog);
        this.activity = activity;
    }


    public void stop() {
        this.cancel();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.speech_dialog);
        mTime = (TextView) findViewById(R.id.id_speech_time);
        mFinish = (ImageView) findViewById(R.id.id_speech_finish);
        mCancel = (ImageView) findViewById(R.id.id_speech_cancle);
        mFinish.setOnClickListener(this);
        mCancel.setOnClickListener(this);
        timer = new Timer();
        timer.schedule(timerTask, 1000, 1000);
        CountDownTimerUtils mCountDownTimerUtils = new CountDownTimerUtils(mTime, 60000, 1000);
        mCountDownTimerUtils.start();
        setViewLocation();
        setCanceledOnTouchOutside(true);//外部点击取消
    }

    private TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            recLen--;
            if (recLen < 0) {
                handler.sendEmptyMessage(0);
            }
        }
    };

    /**
     * 设置dialog位于屏幕中间
     */
    private void setViewLocation() {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int height = dm.heightPixels;
        int width = dm.widthPixels;
        Window window = this.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        window.setGravity(Gravity.CENTER);
        lp.x = (int) (width * 0.8);
        lp.y = (int) (height * 0.8);
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
        onWindowAttributesChanged(lp);
    }

    @Override
    public void run() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_speech_finish:
                int time = 60;
                if (!mTime.getText().toString().equals("录音结束")) {
                    time = 60 - Integer.valueOf(mTime.getText().toString().substring(0, mTime.getText().toString().length() - 2));
                }
                result(time);
                timer.cancel();
                this.cancel();
                break;
            case R.id.id_speech_cancle:
                noResult();
                timer.cancel();
                this.cancel();
                break;
        }
    }

    public abstract void result(int time);

    public abstract void noResult();
}
