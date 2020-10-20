package com.isuo.yw2application.view.main.task.overhaul.execute;

import android.Manifest;
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
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
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
import com.isuo.yw2application.mode.bean.option.OptionBean;
import com.isuo.yw2application.mode.bean.overhaul.OverhaulBean;
import com.isuo.yw2application.mode.bean.overhaul.WorkBean;
import com.isuo.yw2application.mode.overhaul.OverhaulRepository;
import com.isuo.yw2application.utils.CountDownTimerUtils;
import com.isuo.yw2application.utils.MediaPlayerManager;
import com.isuo.yw2application.utils.PhotoUtils;
import com.isuo.yw2application.view.base.MvpFragment;
import com.isuo.yw2application.view.main.generate.repair.GenerateRepairFragment;
import com.isuo.yw2application.view.main.task.increment.submit.IncrementActivity;
import com.isuo.yw2application.widget.ShowImageLayout;
import com.isuo.yw2application.widget.SpeechDialog;
import com.isuo.yw2application.widget.TakePhotoView;
import com.qw.soul.permission.SoulPermission;
import com.qw.soul.permission.bean.Permission;
import com.qw.soul.permission.callbcak.CheckRequestPermissionListener;
import com.sito.library.utils.ActivityUtils;
import com.sito.library.utils.DataUtil;
import com.za.aacrecordlibrary.RecordManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;

import static android.app.Activity.RESULT_OK;

/**
 * 检修工作展示录入界面
 * Created by zhangan on 2017-06-26.
 */

public class OverhaulExecuteFragment extends MvpFragment<OverhaulExecuteContract.Presenter> implements OverhaulExecuteContract.View, View.OnClickListener {

    private String repairId;
    private OverhaulBean mRepairWorkBean;
    private List<Image> images;
    private WorkBean mWorkBean;
    private File photoFile;
    private List<OptionBean.ItemListBean> mItemListBeen;
    private ScrollView mScrollView;
    private RelativeLayout noDataLayout;
    private SpeechRecognizer mIat;
    private TextView mFaultPlayTv;
    private boolean isPlayFault, isPlayRep;
    private CountDownTimerUtils mFaultTimer, mRepairTimer;
    private TextView mWorkResultTv, mWorkSoundTimeTv;
    private EditText mWorkSoundTv;
    private TakePhotoView takePhotoView;
    private RecordManager mRecordManager;
    private SpeechDialog speechDialog;
    private JSONObject jsonObject;

    public static OverhaulExecuteFragment newInstance(long repairId) {
        Bundle args = new Bundle();
        args.putString(ConstantStr.KEY_BUNDLE_STR, String.valueOf(repairId));
        OverhaulExecuteFragment fragment = new OverhaulExecuteFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new OverhaulExecutePresenter(OverhaulRepository.getRepository(getActivity()), this);
        mItemListBeen = new ArrayList<>();
        images = new ArrayList<>();
        mWorkBean = new WorkBean();
        repairId = getArguments().getString(ConstantStr.KEY_BUNDLE_STR);
        initSpeech();
    }

    private void initSpeech() {
        mIat = SpeechRecognizer.createRecognizer(getActivity(), mInitListener);
        mIat.setParameter(SpeechConstant.DOMAIN, "iat");
        mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");//中文
        mIat.setParameter(SpeechConstant.ACCENT, "mandarin ");//普通话
    }

    /**
     * 初始化监听器。
     */
    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            if (code != ErrorCode.SUCCESS) {
                Yw2Application.getInstance().showToast("初始化失败，错误码：" + code);
            }
        }
    };

    public void setParam() {
        mIat.setParameter(SpeechConstant.PARAMS, null);
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");
        mIat.setParameter(SpeechConstant.VAD_BOS, "60000");
        mIat.setParameter(SpeechConstant.VAD_EOS, "60000");
        mIat.setParameter(SpeechConstant.ASR_PTT, "1");
        mIat.setParameter(SpeechConstant.AUDIO_SOURCE, "-1");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fmg_overhaul_work, container, false);
        rootView.findViewById(R.id.ll_play_task_sound).setOnClickListener(this);
        rootView.findViewById(R.id.ll_play_report_sound).setOnClickListener(this);
        rootView.findViewById(R.id.iv_record).setOnClickListener(this);
        rootView.findViewById(R.id.tv_report).setOnClickListener(this);
        rootView.findViewById(R.id.ll_choose_result).setOnClickListener(this);
        mScrollView = rootView.findViewById(R.id.scroll_view);
        mWorkSoundTv = rootView.findViewById(R.id.id_fault_content);
        noDataLayout = rootView.findViewById(R.id.layout_no_data);
        mWorkResultTv = rootView.findViewById(R.id.tv_work_result);
        mWorkSoundTimeTv = rootView.findViewById(R.id.id_work_sound_time);
        mScrollView.setVisibility(View.GONE);
        takePhotoView = rootView.findViewById(R.id.take_photo_view);
        takePhotoView.setTakePhotoListener(new TakePhotoView.TakePhotoListener() {
            @Override
            public void onTakePhoto() {
                SoulPermission.getInstance().checkAndRequestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        new CheckRequestPermissionListener() {
                            @Override
                            public void onPermissionOk(Permission permission) {
                                mImage = null;
                                photoFile = new File(Yw2Application.getInstance().imageCacheFile(), System.currentTimeMillis() + ".jpg");
                                ActivityUtils.startCameraToPhoto(OverhaulExecuteFragment.this, photoFile, ACTION_START_CAMERA);
                            }

                            @Override
                            public void onPermissionDenied(Permission permission) {
                                new AppSettingsDialog.Builder(getActivity())
                                        .setRationale(getString(R.string.need_save_setting))
                                        .setTitle(getString(R.string.request_permissions))
                                        .setPositiveButton(getString(R.string.sure))
                                        .setNegativeButton(getString(R.string.cancel))
                                        .build()
                                        .show();
                            }
                        });
            }

            @Override
            public void onDelete(int position, Image image) {
                mImage = null;
                images.remove(position);
                Yw2Application.getInstance().getDaoSession().getImageDao().deleteInTx(image);
                mWorkBean.setImages(images);
                takePhotoView.setImages(images);
            }

            @Override
            public void onTakePhoto(int position, Image image) {
                mImage = image;
                photoFile = new File(Yw2Application.getInstance().imageCacheFile(), System.currentTimeMillis() + ".jpg");
                ActivityUtils.startCameraToPhoto(OverhaulExecuteFragment.this, photoFile, ACTION_START_CAMERA);
            }
        });
        mRecordManager = new RecordManager().setFileName(Yw2Application.getInstance().voiceCacheFile())
                .setSpeechRecognizer(mIat)
                .setRecordListener(new RecordManager.RecordListener() {

                    @Override
                    public void onSpeechRecognizerError(String errorMessage) {

                    }

                    @Override
                    public void onSpeechFinish(int time, String content, String voiceFile) {
                        mWorkSoundTv.setText(content);
                        mWorkSoundTv.setSelection(mWorkSoundTv.getText().toString().length());
                        saveVoiceInDb(voiceFile, content, String.valueOf(time));
                    }

                });
        if (mPresenter != null) {
            mPresenter.loadRepairWorkFromDb(repairId);
            mPresenter.getRepairWork(repairId);
        }
        return rootView;
    }

    @Nullable
    private Image mImage;

    @Override
    public void setPresenter(OverhaulExecuteContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_report:
                if (mPresenter != null) {
                    jsonObject = new JSONObject();
                    try {
                        for (int i = 0; i < takePhotoView.getImages().size(); i++) {
                            if (!takePhotoView.getImages().get(i).getIsUpload()) {
                                Yw2Application.getInstance().showToast("正在上传照片,请稍等...");
                                return;
                            }
                        }
                        images = takePhotoView.getImages();
                        mWorkBean.setImages(images);
                        mWorkBean.setRepairId(Long.valueOf(repairId));
                        jsonObject.put("repairId", mWorkBean.getRepairId());
                        if (mWorkBean.getImages() == null || mWorkBean.getImages().size() == 0) {
                            getApp().showToast("请拍照");
                            return;
                        }
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < mWorkBean.getImages().size(); i++) {
                            sb.append(mWorkBean.getImages().get(i).getBackUrl());
                            if (i != mWorkBean.getImages().size() - 1) {
                                sb.append(",");
                            }
                        }
                        jsonObject.put("picsUrl", sb.toString());
                        if (TextUtils.isEmpty(mWorkSoundTv.getText().toString().trim())) {
                            getApp().showToast("请输入检修内容");
                            return;
                        }
                        if (mWorkBean.getVoice() != null) {
                            mWorkBean.getVoice().setMContent(mWorkSoundTv.getText().toString().trim());
                        }
                        jsonObject.put("repairRemark", mWorkSoundTv.getText().toString().trim());

                        if (TextUtils.isEmpty(mWorkBean.getRepairResult())) {
                            getApp().showToast("请选择检修结果");
                            return;
                        }
                        jsonObject.put("repairResult", mWorkBean.getRepairResult());
                        if (mWorkBean.getVoice() == null || TextUtils.isEmpty(mWorkBean.getVoice().getVoiceLocal())
                                || TextUtils.isEmpty(mWorkBean.getVoice().getVoiceTime())) {
                            mPresenter.uploadAllData(jsonObject);
                        } else {
                            jsonObject.put("soundTimescale", mWorkBean.getVoice().getVoiceTime());
                            mPresenter.uploadVoiceFile(mWorkBean.getVoice().getVoiceLocal(), "repair", "voice");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.ll_choose_result:
                if (mItemListBeen.size() == 0) {
                    for (int i = 0; i < Yw2Application.getInstance().getOptionInfo().size(); i++) {
                        if (Yw2Application.getInstance().getOptionInfo().get(i).getOptionId() == ConstantInt.REPAIRRESULT) {
                            mItemListBeen = Yw2Application.getInstance().getOptionInfo().get(i).getItemList();
                        }
                    }
                }
                final List<String> mTypeItem = new ArrayList<>();
                for (int i = 0; i < mItemListBeen.size(); i++) {
                    mTypeItem.add(mItemListBeen.get(i).getItemName());
                }
                new MaterialDialog.Builder(getActivity())
                        .items(mTypeItem)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                                mWorkResultTv.setText(text);
                                mWorkBean.setRepairResult(mItemListBeen.get(position).getItemCode());
                            }
                        })
                        .show();
                break;
            case R.id.iv_record:
                SoulPermission.getInstance().checkAndRequestPermission(Manifest.permission.RECORD_AUDIO,
                        new CheckRequestPermissionListener() {
                            @Override
                            public void onPermissionOk(Permission permission) {
                                //设置参数
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
                            }

                            @Override
                            public void onPermissionDenied(Permission permission) {
                                new AppSettingsDialog.Builder(getActivity())
                                        .setRationale(getString(R.string.need_voice_setting))
                                        .setTitle(getString(R.string.request_permissions))
                                        .setPositiveButton(getString(R.string.sure))
                                        .setNegativeButton(getString(R.string.cancel))
                                        .build()
                                        .show();
                            }
                        });
                break;
            case R.id.ll_play_task_sound:
                if (mFaultPlayTv == null) {
                    return;
                }
                isPlayRep = false;
                int timeScale = mRepairWorkBean.getAddType() == 0 ? mRepairWorkBean.getFault().getSoundTimescale() : mRepairWorkBean.getSoundTimescaleAdd();
                String url = mRepairWorkBean.getAddType() == 0 ? mRepairWorkBean.getFault().getVoiceUrl() : mRepairWorkBean.getVoiceUrlAdd();
                mFaultPlayTv.setText(timeScale + "''");
                mFaultPlayTv.setBackgroundResource(R.drawable.record_play_3);
                if (!isPlayFault) {
                    isPlayFault = true;
                    //开始动画
                    mFaultPlayTv.setBackgroundResource(R.drawable.play_anim);
                    final AnimationDrawable animation = (AnimationDrawable) mFaultPlayTv.getBackground();

                    //播放故障语音
                    mFaultTimer = new CountDownTimerUtils(mFaultPlayTv, timeScale * 1000, 1000
                            , timeScale + "''", "#ffffff");
                    MediaPlayerManager.playSound(url, new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            MediaPlayerManager.release();
                            mFaultTimer.cancel();
                            mFaultPlayTv.clearAnimation();
                            mFaultPlayTv.setBackgroundResource(R.drawable.record_play_3);
                            isPlayFault = false;
                        }
                    }, new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mp.start();
                            animation.start();
                            mFaultTimer.start();
                        }
                    });
                } else {
                    MediaPlayerManager.release();
                    mFaultTimer.cancel();
                    mFaultPlayTv.clearAnimation();
                    mFaultPlayTv.setBackgroundResource(R.drawable.record_play_3);
                    isPlayFault = false;
                }
                break;
            case R.id.ll_play_report_sound:
                if (mWorkSoundTimeTv == null || mWorkBean.getVoice() == null) {
                    return;
                }
                isPlayFault = false;
                mWorkSoundTimeTv.setText(mWorkBean.getVoice().getVoiceTime() + "''");
                mWorkSoundTimeTv.setBackgroundResource(R.drawable.record_play_3);
                MediaPlayerManager.release();
                if (!isPlayRep) {
                    isPlayRep = true;
                    //开始动画
                    mWorkSoundTimeTv.setBackgroundResource(R.drawable.play_anim);
                    AnimationDrawable animation = (AnimationDrawable) mWorkSoundTimeTv.getBackground();
                    animation.start();
                    //播放故障语音
                    mRepairTimer = new CountDownTimerUtils(mWorkSoundTimeTv, Integer.valueOf(mWorkBean.getVoice().getVoiceTime()) * 1000, 1000
                            , Integer.valueOf(mWorkBean.getVoice().getVoiceTime()) + "''", "#ffffff");
                    mRepairTimer.start();
                    MediaPlayerManager.playSound(mWorkBean.getVoice().getVoiceLocal(), new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            mRepairTimer.cancel();
                            mWorkSoundTimeTv.clearAnimation();
                            mWorkSoundTimeTv.setBackgroundResource(R.drawable.record_play_3);
                            isPlayRep = false;
                        }
                    });
                } else {
                    mRepairTimer.cancel();
                    mWorkSoundTimeTv.clearAnimation();
                    mWorkSoundTimeTv.setBackgroundResource(R.drawable.record_play_3);
                    isPlayRep = false;
                }
                break;
        }
    }

    @Override
    public void showRepairWork(OverhaulBean repairWorkBean) {
        if (getView() == null) {
            return;
        }
        mRepairWorkBean = repairWorkBean;
        if (mPresenter != null && mRepairWorkBean.getRepairState() == 1) {
            mPresenter.startRepair(repairId);
        }
        mScrollView.setVisibility(View.VISIBLE);
        ((TextView) getView().findViewById(R.id.tv_overhaul_state))
                .setText(Yw2Application.getInstance().getMapOption().get("7").get(mRepairWorkBean.getRepairState() + ""));
        String equipmentName;
        if (TextUtils.isEmpty(mRepairWorkBean.getEquipment().getEquipmentSn())) {
            equipmentName = mRepairWorkBean.getEquipment().getEquipmentName();
        } else {
            equipmentName = mRepairWorkBean.getEquipment().getEquipmentName() + "(" + mRepairWorkBean.getEquipment().getEquipmentSn() + ")";
        }
        ((TextView) getView().findViewById(R.id.tv_equip_name)).setText(equipmentName);
        String equipmentAlias;
        if (TextUtils.isEmpty(mRepairWorkBean.getEquipment().getEquipmentAlias())) {
            equipmentAlias = "";
        } else {
            equipmentAlias = mRepairWorkBean.getEquipment().getEquipmentAlias();
        }
        ((TextView) getView().findViewById(R.id.tv_equip_alias)).setText(String.format("设备别名:%s", equipmentAlias));
        ((TextView) getView().findViewById(R.id.tv_plan_start_time))
                .setText(MessageFormat.format("计划开始时间:{0}", DataUtil.timeFormat(mRepairWorkBean.getStartTime(), null)));
        ((TextView) getView().findViewById(R.id.tv_plan_end_time))
                .setText(MessageFormat.format("计划截至时间:{0}", DataUtil.timeFormat(mRepairWorkBean.getEndTime(), null)));
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mRepairWorkBean.getRepairUsers().size(); i++) {
            sb.append(mRepairWorkBean.getRepairUsers().get(i).getUser().getRealName());
            if (i != mRepairWorkBean.getRepairUsers().size() - 1) {
                sb.append("、");
            }
        }
        ((TextView) getView().findViewById(R.id.tv_repair_name)).setText(MessageFormat.format("检修人:{0}", sb.toString()));
        mFaultPlayTv = getView().findViewById(R.id.id_fault_time);
        ShowImageLayout showImageLayout = getView().findViewById(R.id.alarm_show_image);
        String[] faultPicUrl;
        if (mRepairWorkBean.getAddType() == 0) {
            faultPicUrl = new String[mRepairWorkBean.getFault().getFaultPics().size()];
            for (int i = 0; i < mRepairWorkBean.getFault().getFaultPics().size(); i++) {
                faultPicUrl[i] = mRepairWorkBean.getFault().getFaultPics().get(i).getPicUrl();
            }
            ((TextView) getView().findViewById(R.id.tv_play_text)).setText(mRepairWorkBean.getFault().getFaultDescript());
            mFaultPlayTv.setText(mRepairWorkBean.getFault().getSoundTimescale() + "''");
            if (mRepairWorkBean.getFault().getSoundTimescale() == 0 || TextUtils.isEmpty(mRepairWorkBean.getFault().getVoiceUrl())) {
                mFaultPlayTv.setVisibility(View.GONE);
            } else {
                mFaultPlayTv.setVisibility(View.VISIBLE);
            }
        } else {
            faultPicUrl = new String[mRepairWorkBean.getRepairPicsAdd().size()];
            for (int i = 0; i < mRepairWorkBean.getRepairPicsAdd().size(); i++) {
                faultPicUrl[i] = mRepairWorkBean.getRepairPicsAdd().get(i).getPicUrl();
            }
            ((TextView) getView().findViewById(R.id.tv_play_text)).setText(mRepairWorkBean.getRepairIntro());
            mFaultPlayTv.setText(mRepairWorkBean.getSoundTimescaleAdd() + "''");
            if (mRepairWorkBean.getSoundTimescaleAdd() == 0 || TextUtils.isEmpty(mRepairWorkBean.getVoiceUrlAdd())) {
                mFaultPlayTv.setVisibility(View.GONE);
            } else {
                mFaultPlayTv.setVisibility(View.VISIBLE);
            }
        }
        showImageLayout.showImage(faultPicUrl);
    }

    @Override
    public void showWorkData(WorkBean workBean) {
        mWorkBean = workBean;
        images.addAll(workBean.getImages());
        takePhotoView.setImages(images);
        if (!TextUtils.isEmpty(mWorkBean.getRepairResult())) {
            mWorkResultTv.setText(Yw2Application.getInstance().getMapOption().get("4").get(mWorkBean.getRepairResult()));
        }
        if (mWorkBean.getVoice() != null) {
            mWorkSoundTimeTv.setText(mWorkBean.getVoice().getVoiceTime() + "''");
            mWorkSoundTv.setText(mWorkBean.getVoice().getMContent());
            mWorkSoundTv.setSelection(mWorkSoundTv.getText().toString().length());
        }
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
        noDataLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void uploadVoiceFail() {
        getApp().showToast("上传音频失败");
        hideProgressDialog();
    }

    @Override
    public void uploadVoiceSuccess(String url) {
        if (mPresenter == null) return;
        try {
            jsonObject.put("voiceUrl", url);
            mPresenter.uploadAllData(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void uploadAllDataSuccess() {
        hideProgressDialog();
        getActivity().setResult(RESULT_OK);
        getActivity().finish();
    }

    @Override
    public void uploadAllDataFail() {
        hideProgressDialog();
        getApp().showToast("数据上传失败");
    }

    @Override
    public void uploadProgress() {
        showProgressDialog("上传中...");
    }

    @Override
    public void uploadImageSuccess() {
        takePhotoView.setImages(images);
        mWorkBean.setImages(images);
    }

    @Override
    public void uploadImageFail(Image image) {
        Yw2Application.getInstance().showToast("照片上传失败");
        if (images.contains(image)) {
            images.remove(image);
            takePhotoView.setImages(images);
            mWorkBean.setImages(images);
        }
    }

    private static final int ACTION_START_CAMERA = 100;

    private void saveVoiceInDb(String path, String result, String time) {
        if (TextUtils.isEmpty(result) || TextUtils.isEmpty(time)) {
            Yw2Application.getInstance().showToast("录音失败,请重新录音");
            return;
        }
        Voice voice;
        if (mWorkBean.getVoice() == null) {
            voice = new Voice();
        } else {
            voice = mWorkBean.getVoice();
        }
        voice.setBackUrl("");
        voice.setVoiceLocal(path);
        voice.setUpload(false);
        voice.setMContent(result);
        voice.setItemId(Long.valueOf(repairId));
        voice.setVoiceTime(time);
        voice.setSaveTime(System.currentTimeMillis());
        voice.setWorkType(ConstantInt.CHECKPAIR);
        mWorkBean.setVoice(voice);
        mWorkSoundTimeTv.setText(time + "''");
        Yw2Application.getInstance().getDaoSession().getVoiceDao().insertOrReplaceInTx(voice);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ACTION_START_CAMERA && resultCode == RESULT_OK) {
            PhotoUtils.cropPhoto(getActivity(), photoFile, new PhotoUtils.PhotoListener() {

                @Override
                public void onSuccess(File file) {
                    if (mPresenter != null) {
                        if (mImage != null) {
                            mImage.setIsUpload(false);
                            mImage.setImageLocal(file.getAbsolutePath());
                            mImage.setBackUrl(null);
                            mPresenter.uploadImage(ConstantInt.CHECKPAIR, Long.valueOf(repairId), "fault", mImage);
                        } else {
                            Image image = new Image();
                            images.add(image);
                            image.setImageLocal(file.getAbsolutePath());
                            mPresenter.uploadImage(ConstantInt.CHECKPAIR, Long.valueOf(repairId), "fault", image);
                        }
                    }
                    takePhotoView.setImages(images);
                }
            });
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MediaPlayerManager.release();
    }

    @Override
    public void onResume() {
        super.onResume();
        MediaPlayerManager.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        MediaPlayerManager.pause();
        if (mWorkBean.getVoice() != null && !TextUtils.isEmpty(mWorkSoundTv.getText().toString())) {
            saveVoiceInDb(mWorkBean.getVoice().getVoiceLocal(), mWorkSoundTv.getText().toString(), mWorkBean.getVoice().getVoiceTime());
        }
    }

}
