package com.isuo.yw2application.view.main.work.sos;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechRecognizer;
import com.isuo.yw2application.R;
import com.isuo.yw2application.app.Yw2Application;
import com.isuo.yw2application.mode.bean.sos.EmergencyCall;
import com.isuo.yw2application.mode.sos.SOSRepository;
import com.isuo.yw2application.utils.CountDownTimerUtils;
import com.isuo.yw2application.utils.MediaPlayerManager;
import com.isuo.yw2application.view.base.BaseActivity;
import com.isuo.yw2application.widget.SpeechDialog;
import com.sito.library.adapter.RVAdapter;
import com.sito.library.widget.ExpendRecycleView;
import com.za.aacrecordlibrary.RecordManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * SOS报警
 * Created by zhangan on 2017/8/23.
 */

public class SOSActivity extends BaseActivity implements SOSContract.View {
    private SOSContract.Presenter mPresenter;
    private List<EmergencyCall> list;
    private ExpendRecycleView recyclerView;
    private CountDownTimerUtils mTimerUtils;
    private EditText mWorkSoundTv;
    private EditText mLocalEt;
    private TextView mWorkSoundTimeTv;
    private SpeechRecognizer mIat;
    private String voiceTime;
    private String voicePath;
    private String voiceStr;
    private boolean isPlaying;
    private int chooseType = 0;
    private LinearLayout headerLayout;
    private View bottomView;
    private RecordManager mRecordManager;
    private boolean uploading;
    private JSONObject jsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutAndToolbar(R.layout.activity_sos, "紧急电话");
        new SOSPresenter(SOSRepository.getRepository(), this);
        recyclerView = findViewById(R.id.recycleViewId);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        initSpeech();
        RVAdapter<EmergencyCall> adapter = new RVAdapter<EmergencyCall>(recyclerView, list, R.layout.item_sos) {
            @Override
            public void showData(ViewHolder vHolder, EmergencyCall data, int position) {
                TextView textView = (TextView) vHolder.getView(R.id.tv_item);
                TextView numberTv = (TextView) vHolder.getView(R.id.tv_number);
                textView.setText(data.getEmergencyName());
                numberTv.setText(data.getEmergencyCall());
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new RVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                call(list.get(position).getEmergencyCall());
            }
        });
        @SuppressLint("InflateParams")
        View headerView = LayoutInflater.from(this).inflate(R.layout.layout_header_sos, null);
        headerLayout = headerView.findViewById(R.id.ll_header);
        headerLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        bottomView = LayoutInflater.from(this).inflate(R.layout.layout_sos_buttom, null);
        recyclerView.addHeaderView(headerView);
        recyclerView.addFootView(bottomView);
        bottomView.findViewById(R.id.iv_record).setOnClickListener(this);
        bottomView.findViewById(R.id.tv_report).setOnClickListener(this);
        bottomView.findViewById(R.id.ll_fire).setOnClickListener(this);
        bottomView.findViewById(R.id.ll_help).setOnClickListener(this);
        mWorkSoundTv = bottomView.findViewById(R.id.id_fault_content);
        mLocalEt = bottomView.findViewById(R.id.edit_local);
        mWorkSoundTimeTv = bottomView.findViewById(R.id.id_work_sound_time);
        mWorkSoundTimeTv.setOnClickListener(this);
        mPresenter.emergencyCalls();
        mRecordManager = new RecordManager().setFileName(Yw2Application.getInstance().voiceCacheFile())
                .setSpeechRecognizer(mIat)
                .setRecordListener(new RecordManager.RecordListener() {

                    @Override
                    public void onSpeechRecognizerError(String errorMessage) {

                    }

                    @Override
                    public void onSpeechFinish(int time, String content, String voiceFile) {
                        voiceTime = String.valueOf(time);
                        voiceStr = content;
                        voicePath = voiceFile;
                        String str = voiceTime + "s";
                        mWorkSoundTimeTv.setText(str);
                        mWorkSoundTimeTv.setBackgroundResource(R.drawable.voice_three);
                        mWorkSoundTv.setText(voiceStr);
                        mWorkSoundTv.setSelection(mWorkSoundTv.getText().toString().length());
                    }

                });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slideinleft, R.anim.slideoutright);
    }

    private void initSpeech() {
        mIat = SpeechRecognizer.createRecognizer(this, mInitListener);
        mIat.setParameter(SpeechConstant.DOMAIN, "iat");
        mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");//中文
        mIat.setParameter(SpeechConstant.ACCENT, "mandarin ");//普通话
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_record:
                //设置参数
                setParam();
                new SpeechDialog(this) {
                    @Override
                    public void result(int time) {
                        mRecordManager.stop();
                    }

                    @Override
                    public void noResult() {
                        mRecordManager.onCancel();
                    }
                }.show();
                mRecordManager.start();
                break;
            case R.id.id_work_sound_time:
                if (mWorkSoundTimeTv == null || TextUtils.isEmpty(voicePath)) {
                    return;
                }
                isPlaying = false;
                String voiceTimeStr = voiceTime + "s";
                mWorkSoundTimeTv.setText(voiceTimeStr);
                mWorkSoundTimeTv.setBackgroundResource(R.drawable.voice_three);
                MediaPlayerManager.release();
                if (!isPlaying) {
                    isPlaying = true;
                    //开始动画
                    mWorkSoundTimeTv.setBackgroundResource(R.drawable.play_anim);
                    AnimationDrawable animation = (AnimationDrawable) mWorkSoundTimeTv.getBackground();
                    animation.start();
                    //播放故障语音
                    mTimerUtils = new CountDownTimerUtils(mWorkSoundTimeTv, Integer.parseInt(voiceTime) * 1000, 1000
                            , Integer.valueOf(voiceTime) + "s", "#999999");
                    mTimerUtils.start();
                    MediaPlayerManager.playSound(voicePath, new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            isPlaying = false;
                            mTimerUtils.cancel();
                            mWorkSoundTimeTv.clearAnimation();
                            mWorkSoundTimeTv.setBackgroundResource(R.drawable.voice_three);
                            String voiceTimeStr = voiceTime + "s";
                            mWorkSoundTimeTv.setText(voiceTimeStr);
                        }
                    });
                } else {
                    mTimerUtils.cancel();
                    mWorkSoundTimeTv.clearAnimation();
                    mWorkSoundTimeTv.setBackgroundResource(R.drawable.voice_three);
                    isPlaying = false;
                }
                break;
            case R.id.tv_report:
                if (uploading) {
                    return;
                }
                voiceStr = mWorkSoundTv.getText().toString();
                try {
                    jsonObject = new JSONObject();
                    if (TextUtils.isEmpty(voiceStr)) {
                        Yw2Application.getInstance().showToast("请输入情况说明");
                        return;
                    }
                    jsonObject.put("type", chooseType);
                    jsonObject.put("suggest", voiceStr);
                    if (!TextUtils.isEmpty(mLocalEt.getText().toString())) {
                        jsonObject.put("place", mLocalEt.getText().toString());
                    }
                    if (!TextUtils.isEmpty(voicePath) && !TextUtils.isEmpty(voiceTime)) {
                        jsonObject.put("soundTimescale", voiceTime);
                        mPresenter.uploadVoiceFile(voicePath, "sos", "voice");
                    } else {
                        mPresenter.uploadEmergencyData(jsonObject);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.ll_fire:
                if (chooseType != 0) {
                    bottomView.findViewById(R.id.ib_fire).setBackground(findDrawById(R.drawable.sos_choose_selected));
                    bottomView.findViewById(R.id.ib_help).setBackground(findDrawById(R.drawable.sos_choose_normal));
                }
                chooseType = 0;
                break;
            case R.id.ll_help:
                if (chooseType != 1) {
                    bottomView.findViewById(R.id.ib_fire).setBackground(findDrawById(R.drawable.sos_choose_normal));
                    bottomView.findViewById(R.id.ib_help).setBackground(findDrawById(R.drawable.sos_choose_selected));
                }
                chooseType = 1;
                break;
        }

    }

    public void setParam() {
        mIat.setParameter(SpeechConstant.PARAMS, null);
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");
        mIat.setParameter(SpeechConstant.VAD_BOS, "60000");
        mIat.setParameter(SpeechConstant.VAD_EOS, "60000");
        mIat.setParameter(SpeechConstant.ASR_PTT, "1");
        mIat.setParameter(SpeechConstant.AUDIO_SOURCE, "-1");
    }

    @Override
    public void showEmergencyCalls(final List<EmergencyCall> emergencyCalls) {
        list.clear();
        list.addAll(emergencyCalls);
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void emergencyCallError() {
        headerLayout.setBackground(findDrawById(R.drawable.white_bg_with_shape));
    }

    @Override
    public void uploadDataSuccess() {
        Yw2Application.getInstance().showToast("提交成功");
        finish();
    }

    @Override
    public void uploadDataFail(String message) {
        Yw2Application.getInstance().showToast("提交失败");
    }

    @Override
    public void showUploadProgress() {
        showProgressDialog("提交中...");
        uploading = true;
    }

    @Override
    public void hideUploadProgress() {
        hideProgressDialog();
        uploading = false;
    }

    @Override
    public void uploadVoiceSuccess(String url) {
        try {
            jsonObject.put("voiceUrl", url);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mPresenter.uploadEmergencyData(jsonObject);
    }

    @Override
    public void uploadVoiceFail() {

    }

    @Override
    public void setPresenter(SOSContract.Presenter presenter) {
        mPresenter = presenter;
    }

    private void call(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            if (code != ErrorCode.SUCCESS) {
                Yw2Application.getInstance().showToast("初始化失败，错误码：" + code);
            }
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        if (mPresenter != null) {
            mPresenter.unSubscribe();
        }
    }
}
