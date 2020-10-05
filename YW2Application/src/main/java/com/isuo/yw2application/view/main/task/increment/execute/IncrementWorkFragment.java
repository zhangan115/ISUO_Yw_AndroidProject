package com.isuo.yw2application.view.main.task.increment.execute;


import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechRecognizer;
import com.isuo.yw2application.R;
import com.isuo.yw2application.app.Yw2Application;
import com.isuo.yw2application.common.ConstantInt;
import com.isuo.yw2application.common.ConstantStr;
import com.isuo.yw2application.mode.bean.db.Image;
import com.isuo.yw2application.mode.bean.db.Voice;
import com.isuo.yw2application.mode.bean.work.IncrementBean;
import com.isuo.yw2application.mode.increment.IncrementRepository;
import com.isuo.yw2application.utils.CountDownTimerUtils;
import com.isuo.yw2application.utils.MediaPlayerManager;
import com.isuo.yw2application.utils.PhotoUtils;
import com.isuo.yw2application.view.base.MvpFragment;
import com.isuo.yw2application.widget.ShowImageLayout;
import com.isuo.yw2application.widget.SpeechDialog;
import com.isuo.yw2application.widget.TakePhotoView;
import com.sito.library.utils.ActivityUtils;
import com.sito.library.utils.DataUtil;
import com.za.aacrecordlibrary.RecordManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 专项工作详情
 * Created by zhangan on 2017/10/10.
 */

public class IncrementWorkFragment extends MvpFragment<IncrementWorkContract.Presenter> implements IncrementWorkContract.View, View.OnClickListener {

    private TextView mIncrementTime;
    private EditText mWorkSoundET;
    private TakePhotoView takePhotoView;

    private IncrementBean data;
    private SpeechRecognizer mRecognizer;
    private RecordManager mRecordManager;
    private SpeechDialog speechDialog;
    private CountDownTimerUtils mIncrementTimer, mWorkTimer;
    private AnimationDrawable incrementAnim, workAnim;
    private String soundUrl;
    @Nullable
    private Image mImage;
    private List<Image> images;
    private Voice mVoice;
    private File photoFile;
    private int soundTime;
    private long mWorkId;
    private boolean isIncrementPlay, isWorkPlay;
    private JSONObject jsonStr;
    private static final int ACTION_START_CAMERA = 100;
    private TextView workSoundTimeTv;
    private TextView tv_increment_state;

    public static IncrementWorkFragment newInstance(IncrementBean bean) {
        Bundle args = new Bundle();
        args.putParcelable(ConstantStr.KEY_BUNDLE_OBJECT, bean);
        IncrementWorkFragment fragment = new IncrementWorkFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new IncrementWorkPresenter(IncrementRepository.getRepository(getActivity()), this);
        data = getArguments().getParcelable(ConstantStr.KEY_BUNDLE_OBJECT);
        mWorkId = data != null ? data.getWorkId() : 0;
        images = new ArrayList<>();
        initSpeech();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fmg_increment_work, container, false);
        takePhotoView = rootView.findViewById(R.id.take_photo_view);
        TextView tv_increment_name = rootView.findViewById(R.id.tv_increment_name);
        tv_increment_state = rootView.findViewById(R.id.tv_increment_state);
        TextView tv_increment_play_text = rootView.findViewById(R.id.tv_increment_play_text);
        mIncrementTime = rootView.findViewById(R.id.id_increment_time);
        TextView tv_equip_name = rootView.findViewById(R.id.tv_equip_name);
        TextView tv_plan_start_time = rootView.findViewById(R.id.tv_plan_start_time);
        TextView tv_plan_end_time = rootView.findViewById(R.id.tv_plan_end_time);

        rootView.findViewById(R.id.ll_play_task_sound).setOnClickListener(this);
        rootView.findViewById(R.id.iv_record).setOnClickListener(this);
        rootView.findViewById(R.id.ll_play_work_sound).setOnClickListener(this);
        rootView.findViewById(R.id.tv_upload).setOnClickListener(this);

        mWorkSoundET = rootView.findViewById(R.id.id_work_content);
        workSoundTimeTv = rootView.findViewById(R.id.id_work_sound_time);

        ShowImageLayout ll_increment_image = rootView.findViewById(R.id.ll_increment_image);
        String[] images;
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
        if (data.getWorkIssued() == 0) {
            images = data.getWorkImages().split(",");
            soundTime = data.getSoundTimescale();
            tv_increment_play_text.setText(data.getWorkContent());
            soundUrl = data.getWorkSound();
            tv_plan_start_time.setVisibility(View.GONE);
            tv_plan_end_time.setVisibility(View.GONE);
            tv_increment_state.setVisibility(View.GONE);
        } else {
            images = data.getXworkImages().split(",");
            soundTime = data.getXsoundTimescale();
            tv_increment_play_text.setText(data.getXworkContent());
            soundUrl = data.getXworkSound();
            tv_plan_start_time.setVisibility(View.VISIBLE);
            tv_plan_start_time.setText(MessageFormat.format("计划开始时间:{0}", DataUtil.timeFormat(data.getStartTime(), null)));
            tv_plan_end_time.setVisibility(View.VISIBLE);
            tv_plan_end_time.setText(MessageFormat.format("计划截至时间:{0}", DataUtil.timeFormat(data.getEndTime(), null)));
            tv_increment_state.setVisibility(View.VISIBLE);
        }
        mIncrementTime.setText(soundTime + "''");
        tv_increment_name.setText(Yw2Application.getInstance().getMapOption().get("1").get(String.valueOf(data.getWorkType())));
        String state;
        switch (data.getWorkState()) {
            case 0:
                tv_increment_state.setVisibility(View.VISIBLE);
                tv_increment_state.setTextColor(findColorById(R.color.color_not_start));
                state = "待开始";
                break;
            case 1:
                tv_increment_state.setVisibility(View.VISIBLE);
                tv_increment_state.setTextColor(findColorById(R.color.color_start));
                state = "进行中";
                break;
            default:
                tv_increment_state.setVisibility(View.GONE);
                tv_increment_state.setTextColor(findColorById(R.color.color_finish));
                state = "已完成";
                break;
        }
        tv_increment_state.setText(state);
        ll_increment_image.showImage(images);
        if (TextUtils.isEmpty(soundUrl) || soundTime == 0) {
            mIncrementTime.setVisibility(View.GONE);
        } else {
            mIncrementTime.setVisibility(View.VISIBLE);
        }
        takePhotoView.setTakePhotoListener(new TakePhotoView.TakePhotoListener() {
            @Override
            public void onTakePhoto() {
                mImage = null;
                photoFile = new File(Yw2Application.getInstance().imageCacheFile(), System.currentTimeMillis() + ".jpg");
                ActivityUtils.startCameraToPhoto(IncrementWorkFragment.this, photoFile, ACTION_START_CAMERA);
            }

            @Override
            public void onDelete(int position, Image image) {
                mImage = null;
                IncrementWorkFragment.this.images.remove(position);
                Yw2Application.getInstance().getDaoSession().getImageDao().deleteInTx(image);
                takePhotoView.setImages(IncrementWorkFragment.this.images);
            }

            @Override
            public void onTakePhoto(int position, Image image) {
                mImage = image;
                photoFile = new File(Yw2Application.getInstance().imageCacheFile(), System.currentTimeMillis() + ".jpg");
                ActivityUtils.startCameraToPhoto(IncrementWorkFragment.this, photoFile, ACTION_START_CAMERA);
            }
        });
        mRecordManager = new RecordManager()
                .setFileName(Yw2Application.getInstance().voiceCacheFile())
                .setSpeechRecognizer(mRecognizer)
                .setRecordListener(new RecordManager.RecordListener() {

                    @Override
                    public void onSpeechRecognizerError(String errorMessage) {

                    }

                    @Override
                    public void onSpeechFinish(int time, String content, String voiceFile) {
                        mWorkSoundET.setText(content);
                        mWorkSoundET.setSelection(mWorkSoundET.getText().toString().length());
                        workSoundTimeTv.setText(String.valueOf(time) + "''");
                        if (mVoice == null) {
                            mVoice = new Voice();
                        }
                        mVoice.setMContent(content);
                        mVoice.setSaveTime(System.currentTimeMillis());
                        mVoice.setWorkType(ConstantInt.INCREMENT_WORK);
                        mVoice.setIsUpload(false);
                        mVoice.setItemId(mWorkId);
                        mVoice.setVoiceTime(String.valueOf(time));
                        mVoice.setVoiceLocal(voiceFile);
                        if (mPresenter != null) {
                            mPresenter.saveVoiceToDb(mVoice);
                        }
                    }

                });
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mPresenter != null) {
            mPresenter.loadDataFromDb(mWorkId);
            if (data.getWorkState() == 0) {
                mPresenter.startIncrementWork(data.getWorkId());
            }
        }
    }

    private void initSpeech() {
        mRecognizer = SpeechRecognizer.createRecognizer(getActivity(), mInitListener);
        mRecognizer.setParameter(SpeechConstant.DOMAIN, "iat");
        mRecognizer.setParameter(SpeechConstant.LANGUAGE, "zh_cn");//中文
        mRecognizer.setParameter(SpeechConstant.ACCENT, "mandarin ");//普通话
    }

    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            if (code != ErrorCode.SUCCESS) {
                Yw2Application.getInstance().showToast("初始化失败，错误码：" + code);
            }
        }
    };

    public void setParam() {
        mRecognizer.setParameter(SpeechConstant.PARAMS, null);
        mRecognizer.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        mRecognizer.setParameter(SpeechConstant.RESULT_TYPE, "json");
        mRecognizer.setParameter(SpeechConstant.VAD_BOS, "60000");
        mRecognizer.setParameter(SpeechConstant.VAD_EOS, "60000");
        mRecognizer.setParameter(SpeechConstant.ASR_PTT, "1");
        mRecognizer.setParameter(SpeechConstant.AUDIO_SOURCE, "-1");
    }


    @Override
    public void setPresenter(IncrementWorkContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_play_task_sound:
                if (TextUtils.isEmpty(soundUrl) || soundTime == 0) {
                    return;
                }
                if (!isIncrementPlay) {
                    if (isWorkPlay) {
                        workSoundStopPlay();
                    }
                    isIncrementPlay = true;
                    mIncrementTime.setBackgroundResource(R.drawable.play_anim);
                    incrementAnim = (AnimationDrawable) mIncrementTime.getBackground();
                    mIncrementTimer = new CountDownTimerUtils(mIncrementTime, soundTime * 1000
                            , 1000, soundTime + "''", "#ffffff");
                    MediaPlayerManager.playSound(soundUrl, new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            isIncrementPlay = false;
                            mIncrementTime.setBackgroundResource(R.drawable.record_play_3);
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
            case R.id.ll_play_work_sound:
                if (mVoice == null || TextUtils.isEmpty(mVoice.getVoiceLocal())) {
                    return;
                }
                if (!isWorkPlay) {
                    if (isIncrementPlay) {
                        incrementSoundStopPlay();
                    }
                    isWorkPlay = true;
                    workSoundTimeTv.setBackgroundResource(R.drawable.play_anim);
                    workAnim = (AnimationDrawable) workSoundTimeTv.getBackground();
                    mWorkTimer = new CountDownTimerUtils(workSoundTimeTv, Integer.valueOf(mVoice.getVoiceTime()) * 1000, 1000, Integer.valueOf(mVoice.getVoiceTime()) + "''", "#ffffff");
                    MediaPlayerManager.playSound(mVoice.getVoiceLocal(), new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            isWorkPlay = false;
                            workSoundTimeTv.setBackgroundResource(R.drawable.record_play_3);
                        }
                    });
                    mWorkTimer.start();
                    workAnim.start();
                } else {
                    workSoundStopPlay();
                }
                break;
            case R.id.iv_record:
                setParam();
                speechDialog = new SpeechDialog(getActivity()) {
                    @Override
                    public void result(int time) {
                        mRecordManager.stop();
                    }

                    @Override
                    public void noResult() {
                        mRecordManager.onCancel();
                    }
                };
                speechDialog.show();
                mRecordManager.start();
                break;
            case R.id.tv_upload:
                uploadAllData();
                break;
        }
    }


    private void incrementSoundStopPlay() {
        isIncrementPlay = false;
        MediaPlayerManager.release();
        mIncrementTimer.cancel();
        mIncrementTime.clearAnimation();
        mIncrementTime.setText(soundTime + "''");
        mIncrementTime.setBackgroundResource(R.drawable.record_play_3);
    }

    private void workSoundStopPlay() {
        isWorkPlay = false;
        MediaPlayerManager.release();
        mWorkTimer.cancel();
        workSoundTimeTv.clearAnimation();
        workSoundTimeTv.setText(mVoice.getVoiceTime() + "''");
        workSoundTimeTv.setBackgroundResource(R.drawable.record_play_3);
    }

    private void uploadAllData() {
        if (mPresenter == null) {
            return;
        }
        for (int i = 0; i < takePhotoView.getImages().size(); i++) {
            if (!takePhotoView.getImages().get(i).getIsUpload()) {
                Yw2Application.getInstance().showToast("正在上传照片,请稍等...");
                return;
            }
        }
        images = takePhotoView.getImages();
        jsonStr = new JSONObject();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < images.size(); i++) {
            if (!TextUtils.isEmpty(images.get(i).getBackUrl())) {
                if (i != images.size() - 1) {
                    sb.append(images.get(i).getBackUrl()).append(",");
                } else {
                    sb.append(images.get(i).getBackUrl());
                }
            }
        }
        try {
            if (TextUtils.isEmpty(sb.toString())) {
                getApp().showToast("请拍照");
                return;
            }
            jsonStr.put("workImages", sb.toString());
            jsonStr.put("workId", mWorkId);
            String editSoundStr = mWorkSoundET.getText().toString();
            if (TextUtils.isEmpty(editSoundStr)) {
                getApp().showToast("请输入工作结果");
                return;
            }
            jsonStr.put("workContent", editSoundStr);
            if (mVoice != null && !TextUtils.isEmpty(mVoice.getVoiceTime())) {
                mVoice.setMContent(editSoundStr);
                jsonStr.put("soundTimescale", mVoice.getVoiceTime());
                mPresenter.postVoiceFile("increment", mVoice);
            } else {
                mPresenter.uploadIncrementInfo(jsonStr.toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void uploadVoiceSuccess(String url) {
        try {
            mVoice.setBackUrl(url);
            jsonStr.put("workSound", mVoice.getBackUrl());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (mPresenter != null) {
            mPresenter.saveVoiceToDb(mVoice);
            mPresenter.uploadIncrementInfo(jsonStr.toString());
        }
    }

    @Override
    public void showMessage(String message) {
        if (!TextUtils.isEmpty(message)) {
            getApp().showToast(message);
        }
    }

    @Override
    public void uploadImageSuccess(Image image) {
        images.add(image);
        takePhotoView.setImages(images);
    }

    @Override
    public void uploadImageSuccess(int position, Image image) {
        images.remove(position);
        images.add(position, image);
        takePhotoView.setImages(images);
    }

    @Override
    public void uploadAllDataSuccess() {
        if (mPresenter != null) {
            mPresenter.cleanAllData(images, mVoice);
        }
        getApp().showToast("提交成功");
        getActivity().finish();
    }

    @Override
    public void showDataFromDb(List<Image> imageList, Voice voice) {
        this.images = imageList;
        this.mVoice = voice;
        takePhotoView.setImages(imageList);
        if (mVoice != null) {
            mWorkSoundET.setText(mVoice.getMContent());
            mWorkSoundET.setSelection(mWorkSoundET.getText().toString().length());
            workSoundTimeTv.setText(mVoice.getVoiceTime() + "''");
        }
    }

    @Override
    public void uploadImageSuccess() {
        takePhotoView.setImages(images);
    }

    @Override
    public void uploadImageFail(Image image) {
        Yw2Application.getInstance().showToast("照片上传失败");
        if (images.contains(image)) {
            images.remove(image);
            takePhotoView.setImages(images);
        }
    }

    @Override
    public void startSuccess() {
        tv_increment_state.setVisibility(View.VISIBLE);
        tv_increment_state.setTextColor(findColorById(R.color.color_start));
        tv_increment_state.setText("进行中");
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTION_START_CAMERA && resultCode == Activity.RESULT_OK) {
            PhotoUtils.cropPhoto(getActivity(), photoFile, new PhotoUtils.PhotoListener() {
                @Override
                public void onSuccess(File file) {
                    if (mPresenter != null) {
                        if (mImage != null) {
                            mImage.setIsUpload(false);
                            mImage.setImageLocal(file.getAbsolutePath());
                            mImage.setBackUrl(null);
                            mPresenter.uploadImage(ConstantInt.INCREMENT_WORK, mWorkId, "increment", mImage);
                        } else {
                            Image image = new Image();
                            images.add(image);
                            image.setImageLocal(file.getAbsolutePath());
                            mPresenter.uploadImage(ConstantInt.INCREMENT_WORK, mWorkId, "increment", image);
                        }
                    }
                    takePhotoView.setImages(images);
                }
            });
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mIncrementTimer != null && incrementAnim != null && incrementAnim.isRunning()) {
            incrementSoundStopPlay();
        }
        if (mWorkTimer != null && workAnim != null && workAnim.isRunning()) {
            workSoundStopPlay();
        }

    }
}
