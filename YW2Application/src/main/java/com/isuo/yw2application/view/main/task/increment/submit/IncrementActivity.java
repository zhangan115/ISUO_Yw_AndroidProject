package com.isuo.yw2application.view.main.task.increment.submit;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.isuo.yw2application.R;
import com.isuo.yw2application.app.Yw2Application;
import com.isuo.yw2application.common.ConstantInt;
import com.isuo.yw2application.mode.UploadResult;
import com.isuo.yw2application.mode.bean.db.Image;
import com.isuo.yw2application.mode.bean.db.ImageDao;
import com.isuo.yw2application.mode.bean.db.Voice;
import com.isuo.yw2application.mode.bean.db.VoiceDao;
import com.isuo.yw2application.mode.bean.option.OptionBean;
import com.isuo.yw2application.mode.increment.IncrementRepository;
import com.isuo.yw2application.utils.CountDownTimerUtils;
import com.isuo.yw2application.utils.MediaPlayerManager;
import com.isuo.yw2application.utils.PhotoUtils;
import com.isuo.yw2application.view.base.SpeechActivity;
import com.isuo.yw2application.view.main.alarm.fault.FaultActivity;
import com.isuo.yw2application.view.main.device.equipment.CreateEquipFragment;
import com.isuo.yw2application.view.main.device.list.EquipListActivity;
import com.isuo.yw2application.widget.SpeechDialog;
import com.isuo.yw2application.widget.SwitchButton;
import com.isuo.yw2application.widget.TakePhotoView;
import com.qw.soul.permission.SoulPermission;
import com.qw.soul.permission.bean.Permission;
import com.qw.soul.permission.bean.Permissions;
import com.qw.soul.permission.callbcak.CheckRequestPermissionListener;
import com.qw.soul.permission.callbcak.CheckRequestPermissionsListener;
import com.sito.library.utils.ActivityUtils;
import com.za.aacrecordlibrary.RecordManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;

public class IncrementActivity extends SpeechActivity implements View.OnClickListener, IncrementContract.View {
    //view
    private TextView mWorkType, mWorkSource, mCommit, mVoiceTime;
    private TextView mIncrementStart, mIncrementStop, mDeviceName;
    private ImageView mSpeech;
    private EditText mContent;
    private LinearLayout mLLSource, mLLType;
    private LinearLayout mLLChooseQuarantine;
    private LinearLayout mIncrementState, mIncrementDevice;
    private TakePhotoView takePhotoView;
    //data
    private int operation = 1;
    private long mTime;
    private String equipId;
    private long mTypeCode = -1;
    private File photoFile;
    private List<Image> images;
    private String mVoiceStr, mPath, mVTime;
    private String mImageUrl;
    private List<OptionBean.ItemListBean> typeList, sourceList;
    private final static int REQUEST_CODE = 1;


    IncrementContract.Presenter mPresenter;
    private RecordManager mRecordManager;
    private SpeechDialog speechDialog;
    private JSONObject jsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutAndToolbar(R.layout.activity_increment, "发布专项工作");
        new IncrementPresenter(IncrementRepository.getRepository(this), this);
        initView();
        initEvent();
        initData();
    }

    private void initData() {
        List<OptionBean> list = Yw2Application.getInstance().getOptionInfo();
        if (list != null && list.size() > 0) {
            typeList = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getOptionId() == ConstantInt.INCREMENTTYPE) {
                    typeList.addAll(list.get(i).getItemList());
                }
            }
            sourceList = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getOptionId() == ConstantInt.SOURCE) {
                    sourceList.addAll(list.get(i).getItemList());
                }
            }
        }
        images = Yw2Application.getInstance().getDaoSession().getImageDao().queryBuilder()
                .where(ImageDao.Properties.WorkType.eq(ConstantInt.INCREMENT)
                        , ImageDao.Properties.CurrentUserId.eq(Yw2Application.getInstance().getCurrentUser().getUserId())).list();
        takePhotoView.setImages(images);
        List<Voice> voices = Yw2Application.getInstance().getDaoSession().getVoiceDao().queryBuilder()
                .where(VoiceDao.Properties.WorkType.eq(ConstantInt.INCREMENT), VoiceDao.Properties.IsUpload.eq(false)
                        , VoiceDao.Properties.CurrentUserId.eq(Yw2Application.getInstance().getCurrentUser().getUserId())).list();
        if (voices != null && voices.size() > 0) {
            if (!TextUtils.isEmpty(voices.get(0).getVoiceLocal())) {
                mPath = voices.get(0).getVoiceLocal();
                mContent.setText(voices.get(0).getMContent());
                mContent.setSelection(mContent.getText().toString().length());
                mVoiceStr = voices.get(0).getMContent();
                mVoiceTime.setText(MessageFormat.format("{0}s", voices.get(0).getVoiceTime()));
                mTime = Long.parseLong(voices.get(0).getVoiceTime()) * 1000;
                mVTime = voices.get(0).getVoiceTime();
                if (!TextUtils.isEmpty(mVoiceStr)) {
                    mVoiceTime.setEnabled(true);
                }
            }
        }
    }

    private void initEvent() {
        mSpeech.setOnClickListener(this);
        mCommit.setOnClickListener(this);
        mVoiceTime.setOnClickListener(this);
        mLLSource.setOnClickListener(this);
        mLLType.setOnClickListener(this);
        mIncrementDevice.setOnClickListener(this);
        mIncrementStart.setOnClickListener(this);
        mIncrementStop.setOnClickListener(this);
    }

    private void initView() {
        mWorkType = findViewById(R.id.id_increment_type);
        mSpeech = findViewById(R.id.id_increment_speech);
        mWorkSource = findViewById(R.id.id_increment_source);
        mCommit = findViewById(R.id.id_increment_commit);
        mContent = findViewById(R.id.id_increment_content);
        mVoiceTime = findViewById(R.id.id_increment_time);
        mLLType = findViewById(R.id.id_increment_ll_type);
        mLLSource = findViewById(R.id.id_increment_ll_source);
        mLLChooseQuarantine = findViewById(R.id.ll_choose_quarantine);
        mIncrementState = findViewById(R.id.id_ll_incre_state);
        mIncrementDevice = findViewById(R.id.id_incre_device);
        mIncrementStart = findViewById(R.id.id_incre_start);
        mIncrementStop = findViewById(R.id.id_incre_stop);
        mDeviceName = findViewById(R.id.id_incre_devicename);
        SwitchButton switchButton = findViewById(R.id.switchButton);
        switchButton.setChecked(false);//默认开启
        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    operation = 0;
                } else {
                    operation = 1;
                }
            }
        });
        takePhotoView = findViewById(R.id.take_photo_view);
        takePhotoView.setTakePhotoListener(new TakePhotoView.TakePhotoListener() {
            @Override
            public void onTakePhoto() {
                Permissions permissions = Permissions.build(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA);
                SoulPermission.getInstance().checkAndRequestPermissions(permissions,
                        new CheckRequestPermissionsListener() {
                            @Override
                            public void onAllPermissionOk(Permission[] allPermissions) {
                                mImage = null;
                                photoFile = new File(Yw2Application.getInstance().imageCacheFile(), System.currentTimeMillis() + ".jpg");
                                ActivityUtils.startCameraToPhoto(IncrementActivity.this, photoFile, ACTION_START_CAMERA);
                            }

                            @Override
                            public void onPermissionDenied(Permission[] refusedPermissions) {
                                new AppSettingsDialog.Builder(IncrementActivity.this)
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
                takePhotoView.setImages(images);
            }

            @Override
            public void onTakePhoto(final int position, final Image image) {
                Permissions permissions = Permissions.build(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA);
                SoulPermission.getInstance().checkAndRequestPermissions(permissions,
                        new CheckRequestPermissionsListener() {
                            @Override
                            public void onAllPermissionOk(Permission[] allPermissions) {
                                mImage = image;
                                takePhotoPosition = position;
                                photoFile = new File(Yw2Application.getInstance().imageCacheFile(), System.currentTimeMillis() + ".jpg");
                                ActivityUtils.startCameraToPhoto(IncrementActivity.this, photoFile, ACTION_START_CAMERA);
                            }

                            @Override
                            public void onPermissionDenied(Permission[] refusedPermissions) {
                                new AppSettingsDialog.Builder(IncrementActivity.this)
                                        .setRationale(getString(R.string.need_save_setting))
                                        .setTitle(getString(R.string.request_permissions))
                                        .setPositiveButton(getString(R.string.sure))
                                        .setNegativeButton(getString(R.string.cancel))
                                        .build()
                                        .show();
                            }
                        });
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
                        mVoiceStr = content;
                        mContent.setText(mVoiceStr);
                        mContent.setSelection(mContent.getText().toString().length());
                        mPath = voiceFile;
                        mVTime = String.valueOf(time);
                        mVoiceTime.setEnabled(true);
                        mVoiceTime.setText(MessageFormat.format("{0}s", mVTime));
                        mTime = time * 1000;
                        saveVoiceInDb(mPath);
                    }
                });
    }

    @Nullable
    private Image mImage;
    private int takePhotoPosition;

    AnimationDrawable animation;
    CountDownTimerUtils mCountDownTimerUtils;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_increment_ll_type://工作类型
                final List<String> mTypeItem = new ArrayList<>();
                for (int i = 0; i < typeList.size(); i++) {
                    mTypeItem.add(typeList.get(i).getItemName());
                }
                new MaterialDialog.Builder(IncrementActivity.this)
                        .items(mTypeItem)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                                mWorkType.setText(typeList.get(position).getItemName());
                                mTypeCode = Long.valueOf(typeList.get(position).getItemCode());
                                if (mTypeCode == 1) {
                                    mIncrementState.setVisibility(View.VISIBLE);
                                    mLLChooseQuarantine.setVisibility(View.VISIBLE);
                                } else if (mTypeCode == 4) {
                                    mIncrementState.setVisibility(View.VISIBLE);
                                    mLLChooseQuarantine.setVisibility(View.GONE);
                                } else {
                                    mIncrementState.setVisibility(View.GONE);
                                    mDeviceName.setText("");
                                    equipId = "";
                                }
                            }
                        })
                        .show();
                break;
            case R.id.id_increment_ll_source://工作来源
                final List<String> mSourceItem = new ArrayList<>();
                for (int i = 0; i < sourceList.size(); i++) {
                    mSourceItem.add(sourceList.get(i).getItemName());
                }
                new MaterialDialog.Builder(IncrementActivity.this)
                        .items(mSourceItem)
                        .itemsCallback(new MaterialDialog.ListCallback() {

                            @Override
                            public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                                mWorkSource.setText(sourceList.get(position).getItemName());
                            }
                        })
                        .show();
                break;
            case R.id.id_increment_speech:
                SoulPermission.getInstance().checkAndRequestPermission(Manifest.permission.RECORD_AUDIO,
                        new CheckRequestPermissionListener() {
                            @Override
                            public void onPermissionOk(Permission permission) {
                                speechDialog = new SpeechDialog(IncrementActivity.this) {

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
                                new AppSettingsDialog.Builder(IncrementActivity.this)
                                        .setRationale(getString(R.string.need_voice_setting))
                                        .setTitle(getString(R.string.request_permissions))
                                        .setPositiveButton(getString(R.string.sure))
                                        .setNegativeButton(getString(R.string.cancel))
                                        .build()
                                        .show();
                            }
                        });
                break;
            case R.id.id_increment_time:
                //开始动画
                mVoiceTime.setBackgroundResource(R.drawable.play_anim);
                animation = (AnimationDrawable) mVoiceTime.getBackground();
                animation.start();
                //开始计时
                mCountDownTimerUtils = new CountDownTimerUtils(mVoiceTime, mTime, 1000
                        , mVTime + "s", "#999999");
                mCountDownTimerUtils.start();
                //播放录音
                MediaPlayerManager.playSound(mPath, new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mVoiceTime.setBackgroundResource(R.drawable.voice_three);
                    }
                });
                break;
            case R.id.id_increment_commit:
                jsonObject = new JSONObject();
                try {
                    for (int i = 0; i < takePhotoView.getImages().size(); i++) {
                        if (!takePhotoView.getImages().get(i).getIsUpload()) {
                            Yw2Application.getInstance().showToast("正在上传照片,请稍等...");
                            return;
                        }
                    }
                    images = takePhotoView.getImages();
                    if (images != null && images.size() > 0) {
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
                        mImageUrl = sb.toString();
                    }
                    if (mTypeCode == -1) {
                        Yw2Application.getInstance().showToast("请选择工作类型");
                        return;
                    }
                    jsonObject.put("workType", mTypeCode);
                    if (TextUtils.isEmpty(mContent.getText().toString())) {
                        Yw2Application.getInstance().showToast("请输入工作内容");
                        return;
                    }
                    if (mTypeCode == 1) {
                        if (TextUtils.isEmpty(equipId)) {
                            Yw2Application.getInstance().showToast("请选择对象");
                            return;
                        }
                        jsonObject.put("equipmentId", equipId);
                        jsonObject.put("operation", operation);
                    } else if (mTypeCode == 4) {
                        if (TextUtils.isEmpty(equipId)) {
                            Yw2Application.getInstance().showToast("请选择对象");
                            return;
                        }
                        jsonObject.put("equipmentId", equipId);
                    }
                    jsonObject.put("workContent", mContent.getText().toString());
                    if (TextUtils.isEmpty(mImageUrl)) {
                        Yw2Application.getInstance().showToast("请拍照");
                        return;
                    }
                    jsonObject.put("workImages", mImageUrl);
                    jsonObject.put("workIssued", 0);
                    if (!TextUtils.isEmpty(mVTime) && !TextUtils.isEmpty(mPath)) {
                        jsonObject.put("soundTimescale", mVTime);
                        mPresenter.postVoiceFile(ConstantInt.INCREMENT, "increment");
                    } else {
                        mPresenter.postIncrementInfo(jsonObject);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;
            case R.id.id_incre_device:
                Intent intent = new Intent(this, EquipListActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
                break;
            case R.id.id_incre_start:
                mIncrementStart.setBackground(findDrawById(R.drawable.staff_bg_blue_trans));
                mIncrementStart.setTextColor(findColorById(R.color.colorWhite));
                mIncrementStop.setBackground(findDrawById(R.drawable.staff_bg_gray_trans));
                mIncrementStop.setTextColor(findColorById(R.color.color_bg_staff_gray));
                break;
            case R.id.id_incre_stop:
                mIncrementStart.setBackground(findDrawById(R.drawable.staff_bg_gray));
                mIncrementStart.setTextColor(findColorById(R.color.color_bg_staff_gray));
                mIncrementStop.setBackground(findDrawById(R.drawable.staff_bg_blue));
                mIncrementStop.setTextColor(findColorById(R.color.colorWhite));
                break;
        }
    }

    private void saveVoiceInDb(String path) {
        List<Voice> voices = Yw2Application.getInstance().getDaoSession().getVoiceDao().queryBuilder()
                .where(VoiceDao.Properties.WorkType.eq(ConstantInt.INCREMENT)).list();
        if (voices != null && voices.size() > 0) {
            for (int i = 0; i < voices.size(); i++) {
                voices.get(i).setMContent(mVoiceStr);
                voices.get(i).setBackUrl("");
                voices.get(i).setVoiceLocal(path);
                voices.get(i).setUpload(false);
                voices.get(i).setSaveTime(System.currentTimeMillis());
                voices.get(i).setWorkType(ConstantInt.INCREMENT);
                voices.get(i).setVoiceTime(mVTime);
                Yw2Application.getInstance().getDaoSession().getVoiceDao().insertOrReplace(voices.get(i));
            }
        } else {
            Voice voice = new Voice();
            voice.setMContent(mVoiceStr);
            voice.setBackUrl("");
            voice.setVoiceLocal(path);
            voice.setUpload(false);
            voice.setSaveTime(System.currentTimeMillis());
            voice.setWorkType(ConstantInt.INCREMENT);
            voice.setVoiceTime(mVTime);
            Yw2Application.getInstance().getDaoSession().getVoiceDao().insertOrReplace(voice);
        }
    }

    private static final int ACTION_START_CAMERA = 100;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTION_START_CAMERA && resultCode == RESULT_OK) {
            PhotoUtils.cropPhoto(this, photoFile, "", new PhotoUtils.PhotoListener() {
                @Override
                public void onSuccess(File file) {
                    if (mPresenter != null) {
                        if (mImage != null) {
                            mImage.setIsUpload(false);
                            mImage.setImageLocal(file.getAbsolutePath());
                            mImage.setBackUrl(null);
                            mPresenter.uploadImage(ConstantInt.INCREMENT, "increment", mImage);
                        } else {
                            Image image = new Image();
                            images.add(image);
                            image.setImageLocal(file.getAbsolutePath());
                            mPresenter.uploadImage(ConstantInt.INCREMENT, "increment", image);
                        }
                    }
                    takePhotoView.setImages(images);
                }
            });
        } else if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            String equipmentId = data.getStringExtra(EquipListActivity.TYPE);
            String equipmentName = data.getStringExtra(EquipListActivity.NAME);
            mDeviceName.setText(equipmentName);
            equipId = equipmentId;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (speechDialog != null) {
            speechDialog.stop();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MediaPlayerManager.release();
        if (animation != null && animation.isRunning()) {
            animation.stop();
            mVoiceTime.setBackgroundResource(R.drawable.voice_three);
            mVoiceTime.setText(MessageFormat.format("{0}s", mVTime));
        }
        if (mCountDownTimerUtils != null) {
            mCountDownTimerUtils.cancel();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MediaPlayerManager.resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MediaPlayerManager.release();
        if (mPresenter != null) {
            mPresenter.unSubscribe();
        }

    }

    @Override
    public void setPresenter(IncrementContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void postSuccess(UploadResult result) {
        mPresenter.uploadSuccess();
        Yw2Application.getInstance().showToast("提交成功");
        setResult(Activity.RESULT_OK);
        finish();
    }

    @Override
    public void postImageSuccess(List<String> fileBeen) {
        if (fileBeen != null && fileBeen.size() > 0) {
            mPresenter.postVoiceFile(ConstantInt.INCREMENT, "increment");
            mImageUrl = fileBeen.get(0) + "," + fileBeen.get(1) + "," + fileBeen.get(2);
        }
    }

    @Override
    public void postVoiceSuccess(List<String> fileBeen) {
        //提交参数
        if (fileBeen != null && fileBeen.size() > 0) {
            String voiceUrl = fileBeen.get(0);
            try {
                jsonObject.put("workSound", voiceUrl);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mPresenter.postIncrementInfo(jsonObject);
        }
    }

    @Override
    public void postFail() {

    }

    @Override
    public void postFinish() {
        hideProgressDialog();
    }

    @Override
    public void showLoading() {
        showProgressDialog("正在提交...");
    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void postSingleImgSuccess(Image image) {
        if (mImage == null) {
            images.add(image);
        } else {
            Yw2Application.getInstance().getDaoSession().getImageDao().deleteInTx(images.get(takePhotoPosition));
            images.remove(takePhotoPosition);
            images.add(takePhotoPosition, image);
        }
        takePhotoView.setImages(images);
    }

    @Override
    public void postSingleVocSuccess(List<String> lists) {

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
}
