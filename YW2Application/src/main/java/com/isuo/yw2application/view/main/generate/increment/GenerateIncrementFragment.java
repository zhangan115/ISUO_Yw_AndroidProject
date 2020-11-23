package com.isuo.yw2application.view.main.generate.increment;

import android.Manifest;
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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
import com.isuo.yw2application.mode.bean.User;
import com.isuo.yw2application.mode.bean.db.Image;
import com.isuo.yw2application.mode.bean.employee.EmployeeBean;
import com.isuo.yw2application.mode.bean.option.OptionBean;
import com.isuo.yw2application.utils.ChooseDateDialog;
import com.isuo.yw2application.utils.CountDownTimerUtils;
import com.isuo.yw2application.utils.MediaPlayerManager;
import com.isuo.yw2application.utils.PhotoUtils;
import com.isuo.yw2application.view.base.MvpFragment;
import com.isuo.yw2application.view.main.adduser.EmployeeActivity;
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
import com.sito.library.utils.DataUtil;
import com.sito.library.utils.DisplayUtil;
import com.sito.library.utils.FileFromUri;
import com.sito.library.widget.ShowUserLayout;
import com.za.aacrecordlibrary.RecordManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import pub.devrel.easypermissions.AppSettingsDialog;

import static android.app.Activity.RESULT_OK;

/**
 * 生成增值工作
 * Created by zhangan on 2017/9/29.
 */

public class GenerateIncrementFragment extends MvpFragment<GenerateIncrementContract.Presenter> implements
        GenerateIncrementContract.View, View.OnClickListener {

    public SpeechRecognizer mSpeechRecognizer;
    private TextView incrementTimeTv, deviceNameTv, mWorkType;
    private TextView startTimeTv, endTimeTv;
    private EditText incrementContentTv;
    private LinearLayout mAddUserLayout, mIncrementState;
    private LinearLayout mLLChooseQuarantine;
    private TakePhotoView take_photo_view;

    private RecordManager mRecordManager;
    private SpeechDialog speechDialog;
    private int voiceTime;
    private int operation = 1;
    private long mTypeCode = -1;
    private File photoFile;
    private Image mImage;
    private List<Image> images;
    private String voiceContent, voiceFile;
    private String equipmentName, equipmentId;
    private static final int ACTION_START_CAMERA = 200;
    private static final int ACTION_START_PHOTO = 203;

    private static final int request_equipment = 201;
    private static final int request_add_user = 202;
    private ArrayList<EmployeeBean> employeeBeen;
    private Calendar mStartCalender;
    private Calendar mEndCalender;
    private String startTime = null;
    private String endTime = null;
    private List<OptionBean.ItemListBean> typeList, sourceList;
    private JSONObject uploadJson;
    private CountDownTimerUtils mCountDownTimerUtils;
    private AnimationDrawable animation;
    private boolean isPlaying;

    public static GenerateIncrementFragment newInstance() {
        Bundle args = new Bundle();
        GenerateIncrementFragment fragment = new GenerateIncrementFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new GenerateIncrementPresenter(Yw2Application.getInstance().getGenerateRepositoryComponent().getRepository(), this);
        images = new ArrayList<>();
        mStartCalender = Calendar.getInstance(Locale.CHINA);
        mEndCalender = Calendar.getInstance(Locale.CHINA);
        initSpeech();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_generate_increment, container, false);
        rootView.findViewById(R.id.id_increment_ll_type).setOnClickListener(this);
        rootView.findViewById(R.id.ll_increment_start_time).setOnClickListener(this);
        rootView.findViewById(R.id.ll_increment_end_time).setOnClickListener(this);
        rootView.findViewById(R.id.id_increment_speech).setOnClickListener(this);
        rootView.findViewById(R.id.id_increment_device).setOnClickListener(this);
        rootView.findViewById(R.id.id_increment_time).setOnClickListener(this);
        rootView.findViewById(R.id.id_increment_commit).setOnClickListener(this);
        rootView.findViewById(R.id.ib_add_user).setOnClickListener(this);
        initView(rootView);
        initData();
        return rootView;
    }

    private void initView(View rootView) {
        incrementTimeTv = rootView.findViewById(R.id.id_increment_time);
        mWorkType = rootView.findViewById(R.id.id_increment_type);
        incrementContentTv = rootView.findViewById(R.id.id_increment_content);
        deviceNameTv = rootView.findViewById(R.id.id_increment_device_name);
        startTimeTv = rootView.findViewById(R.id.tv_increment_start_time);
        endTimeTv = rootView.findViewById(R.id.tv_increment_end_time);
        mAddUserLayout = rootView.findViewById(R.id.ll_employee_add);
        mIncrementState = rootView.findViewById(R.id.id_ll_increment_state);
        mLLChooseQuarantine = rootView.findViewById(R.id.ll_choose_quarantine);
        SwitchButton switchButton = rootView.findViewById(R.id.switchButton);
        switchButton.setChecked(false);
        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                operation = isChecked ? 0 : 1;
            }
        });
        take_photo_view = rootView.findViewById(R.id.take_photo_view);
        take_photo_view.setTakePhotoListener(new TakePhotoView.TakePhotoListener() {

            @Override
            public void onTakePhoto() {
                Permissions permissions = Permissions.build(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA);
                SoulPermission.getInstance().checkAndRequestPermissions(permissions,
                        new CheckRequestPermissionsListener() {
                            @Override
                            public void onAllPermissionOk(Permission[] allPermissions) {
                                new MaterialDialog.Builder(getActivity())
                                        .items(R.array.choose_photo)
                                        .itemsCallback(new MaterialDialog.ListCallback() {
                                            @Override
                                            public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                                                mImage = null;
                                                if (position == 0) {
                                                    photoFile = new File(Yw2Application.getInstance().imageCacheFile(), System.currentTimeMillis() + ".jpg");
                                                    ActivityUtils.startCameraToPhoto(GenerateIncrementFragment.this, photoFile, ACTION_START_CAMERA);
                                                } else {
                                                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                                                    intent.setType("image/*");
                                                    startActivityForResult(intent, ACTION_START_PHOTO);
                                                }
                                            }
                                        })
                                        .show();
                            }

                            @Override
                            public void onPermissionDenied(Permission[] refusedPermissions) {
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
                take_photo_view.setImages(images);
            }

            @Override
            public void onTakePhoto(int position, final Image image) {
                new MaterialDialog.Builder(getActivity())
                        .items(R.array.choose_photo)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                                mImage = image;
                                if (position == 0) {
                                    photoFile = new File(Yw2Application.getInstance().imageCacheFile(), System.currentTimeMillis() + ".jpg");
                                    ActivityUtils.startCameraToPhoto(GenerateIncrementFragment.this, photoFile, ACTION_START_CAMERA);
                                } else {
                                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                                    intent.setType("image/*");
                                    startActivityForResult(intent, ACTION_START_PHOTO);
                                }
                            }
                        })
                        .show();
            }
        });
        mRecordManager = new RecordManager().setFileName(Yw2Application.getInstance().voiceCacheFile())
                .setSpeechRecognizer(mSpeechRecognizer)
                .setRecordListener(new RecordManager.RecordListener() {

                    @Override
                    public void onSpeechRecognizerError(String errorMessage) {
                        getApp().showToast(errorMessage);
                    }

                    @Override
                    public void onSpeechFinish(int time, String content, String voiceFile) {
                        GenerateIncrementFragment.this.voiceTime = time;
                        GenerateIncrementFragment.this.voiceContent = content;
                        GenerateIncrementFragment.this.voiceFile = voiceFile;
                        updateVoiceUi();
                    }

                });
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
    }


    @Override
    public void setPresenter(GenerateIncrementContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_increment_ll_type:
                final List<String> mTypeItem = new ArrayList<>();
                for (int i = 0; i < typeList.size(); i++) {
                    mTypeItem.add(typeList.get(i).getItemName());
                }
                new MaterialDialog.Builder(getActivity())
                        .items(mTypeItem)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                                mWorkType.setText(typeList.get(position).getItemName());
                                mTypeCode = Long.parseLong(typeList.get(position).getItemCode());
                                if (mTypeCode == 1) {
                                    mIncrementState.setVisibility(View.VISIBLE);
                                    mLLChooseQuarantine.setVisibility(View.VISIBLE);
                                } else if (mTypeCode == 4) {
                                    mIncrementState.setVisibility(View.VISIBLE);
                                    mLLChooseQuarantine.setVisibility(View.GONE);
                                } else {
                                    mIncrementState.setVisibility(View.GONE);
                                }
                            }
                        })
                        .show();
                break;
            case R.id.ll_increment_start_time:
                new ChooseDateDialog(getActivity(),R.style.MyDateDialog)
                        .setCurrent(mStartCalender)
                        .setResultListener(new ChooseDateDialog.OnDateChooseListener() {
                            @Override
                            public void onDate(Calendar calendar) {
                                mStartCalender = calendar;
                                startTime = getDataStr(mStartCalender);
                                startTimeTv.setText(startTime);
                            }
                        })
                        .show();
                break;
            case R.id.ll_increment_end_time:
                new ChooseDateDialog(getActivity(),R.style.MyDateDialog)
                        .setCurrent(mEndCalender)
                        .setResultListener(new ChooseDateDialog.OnDateChooseListener() {
                            @Override
                            public void onDate(Calendar calendar) {
                                mEndCalender = calendar;
                                endTime = getDataStr(mEndCalender);
                                endTimeTv.setText(endTime);
                            }
                        })
                        .show();
                break;
            case R.id.id_increment_speech:
                SoulPermission.getInstance().checkAndRequestPermission(Manifest.permission.RECORD_AUDIO,
                        new CheckRequestPermissionListener() {
                            @Override
                            public void onPermissionOk(Permission permission) {
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
            case R.id.id_increment_device:
                Intent intent = new Intent(getActivity(), EquipListActivity.class);
                startActivityForResult(intent, request_equipment);
                break;
            case R.id.id_increment_time:
                if (TextUtils.isEmpty(voiceFile)) {
                    getApp().showToast("请录音");
                    return;
                }
                if (!isPlaying) {
                    incrementTimeTv.setBackgroundResource(R.drawable.play_anim);
                    animation = (AnimationDrawable) incrementTimeTv.getBackground();
                    animation.start();
                    mCountDownTimerUtils = new CountDownTimerUtils(incrementTimeTv, voiceTime * 1000, 1000, voiceTime + "s", "#999999");
                    mCountDownTimerUtils.start();
                    isPlaying = true;
                    MediaPlayerManager.playSound(voiceFile, new MediaPlayer.OnCompletionListener() {

                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            cancelPlay();
                        }
                    });
                } else {
                    cancelPlay();
                }
                break;
            case R.id.ib_add_user:
                Intent userIntent = new Intent(getActivity(), EmployeeActivity.class);
                userIntent.putParcelableArrayListExtra(ConstantStr.KEY_BUNDLE_LIST, employeeBeen);
                startActivityForResult(userIntent, request_add_user);
                break;
            case R.id.id_increment_commit:
                if (mPresenter == null) return;
                for (int i = 0; i < take_photo_view.getImages().size(); i++) {
                    if (!take_photo_view.getImages().get(i).isUpload()) {
                        showErrorMessage("正在上传照片,请稍等...");
                        return;
                    }
                }
                images = take_photo_view.getImages();
                uploadJson = new JSONObject();
                try {
                    uploadJson.put("workIssued", 1);
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
                    String imageUrl = sb.toString();
                    if (!TextUtils.isEmpty(imageUrl)) {
                        uploadJson.put("xworkImages", imageUrl);
                    }
                    if (mTypeCode == -1) {
                        getApp().showToast("请选择工作类型");
                        return;
                    }
                    uploadJson.put("workType", mTypeCode);
                    if (mTypeCode == 1) {
                        if (!TextUtils.isEmpty(equipmentId)) {
                            uploadJson.put("equipmentId", equipmentId);
                            uploadJson.put("operation", operation);
                        } else {
                            getApp().showToast("请选择设备");
                        }
                    } else if (mTypeCode == 4) {
                        if (!TextUtils.isEmpty(equipmentId)) {
                            uploadJson.put("equipmentId", equipmentId);
                        } else {
                            getApp().showToast("请选择设备");
                            return;
                        }
                    }
                    voiceContent = incrementContentTv.getText().toString();
                    if (TextUtils.isEmpty(voiceContent)) {
                        getApp().showToast("请输入工作内容");
                        return;
                    }
                    uploadJson.put("xworkContent", String.valueOf(voiceContent));
                    if (TextUtils.isEmpty(getUserIds())) {
                        getApp().showToast("请选择指派人");
                        return;
                    }
                    uploadJson.put("userIds", String.valueOf(getUserIds()));
                    if (TextUtils.isEmpty(startTime)) {
                        getApp().showToast("选择开始时间");
                    }
                    uploadJson.put("startTime", startTime);
                    if (TextUtils.isEmpty(endTime)) {
                        getApp().showToast("选择结束时间");
                    }
                    uploadJson.put("endTime", endTime);
                    if (voiceTime != 0 && !TextUtils.isEmpty(voiceFile)) {
                        uploadJson.put("xsoundTimescale", String.valueOf(voiceTime));
                        mPresenter.uploadVoiceFile("increment", voiceFile);
                    } else {
                        mPresenter.uploadAllData(uploadJson);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

        }
    }

    private void cancelPlay() {
        if (!isPlaying) {
            return;
        }
        mCountDownTimerUtils.cancel();
        isPlaying = false;
        MediaPlayerManager.release();
        animation.selectDrawable(0);
        animation.stop();
        incrementTimeTv.clearAnimation();
        incrementTimeTv.setText(MessageFormat.format("{0}s", voiceTime));
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTION_START_CAMERA && resultCode == RESULT_OK) {
            PhotoUtils.cropPhoto(getActivity(), photoFile,"", new PhotoUtils.PhotoListener() {
                @Override
                public void onSuccess(File file) {
                    uploadPhoto(file);
                }
            });
        } else if (requestCode == request_equipment && resultCode == RESULT_OK) {
            equipmentId = data.getStringExtra(EquipListActivity.TYPE);
            equipmentName = data.getStringExtra(EquipListActivity.NAME);
            deviceNameTv.setText(equipmentName);
        } else if (requestCode == request_add_user && resultCode == RESULT_OK) {
            employeeBeen = data.getParcelableArrayListExtra(ConstantStr.KEY_BUNDLE_LIST);
            if (employeeBeen != null) {
                mAddUserLayout.removeAllViews();
                for (int i = 0; i < employeeBeen.size(); i++) {
                    mAddUserLayout.addView(addUser(employeeBeen.get(i).getUser()));
                }
                ImageButton imageButton = new ImageButton(getActivity());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DisplayUtil.dip2px(getActivity(), 50)
                        , DisplayUtil.dip2px(getActivity(), 50));
                imageButton.setBackground(findDrawById(R.drawable.add_btn));
                imageButton.setLayoutParams(params);
                imageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), EmployeeActivity.class);
                        intent.putParcelableArrayListExtra(ConstantStr.KEY_BUNDLE_LIST, employeeBeen);
                        startActivityForResult(intent, request_add_user);
                    }
                });
                mAddUserLayout.addView(imageButton);
                final HorizontalScrollView scrollView = Objects.requireNonNull(getView()).findViewById(R.id.id_hs_employee);
                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
                    }
                });
            }
        } else if (requestCode == ACTION_START_PHOTO && resultCode == RESULT_OK) {
            if (data.getData() == null) {
                showErrorMessage("图片选择失败!");
                return;
            }
            try {
                File photo = FileFromUri.from(getActivity().getApplicationContext(), data.getData());
                PhotoUtils.cropPhoto(getActivity(), photo, "",new PhotoUtils.PhotoListener() {
                    @Override
                    public void onSuccess(File file) {
                        uploadPhoto(file);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadPhoto(File file) {
        if (mPresenter != null) {
            if (mImage != null) {
                mImage.setUpload(false);
                mImage.setImageLocal(file.getAbsolutePath());
                mImage.setBackUrl(null);
                mPresenter.uploadImage("increment", mImage);
            } else {
                Image image = new Image();
                images.add(image);
                image.setImageLocal(file.getAbsolutePath());
                mPresenter.uploadImage("increment", image);
            }
        }
        take_photo_view.setImages(images);
    }


    private void updateVoiceUi() {
        incrementTimeTv.setText(MessageFormat.format("{0}s", String.valueOf(voiceTime)));
        incrementContentTv.setText(voiceContent);
        incrementContentTv.setSelection(voiceContent.length());
    }

    private void initSpeech() {
        mSpeechRecognizer = SpeechRecognizer.createRecognizer(getActivity(), mInitListener);
        mSpeechRecognizer.setParameter(SpeechConstant.DOMAIN, "iat");
        mSpeechRecognizer.setParameter(SpeechConstant.LANGUAGE, "zh_cn");//中文
        mSpeechRecognizer.setParameter(SpeechConstant.ACCENT, "mandarin ");//普通话
        mSpeechRecognizer.setParameter(SpeechConstant.AUDIO_SOURCE, "-1");
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
        mSpeechRecognizer.setParameter(SpeechConstant.PARAMS, null);
        mSpeechRecognizer.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        mSpeechRecognizer.setParameter(SpeechConstant.RESULT_TYPE, "json");
        mSpeechRecognizer.setParameter(SpeechConstant.VAD_BOS, "60000");
        mSpeechRecognizer.setParameter(SpeechConstant.VAD_EOS, "60000");
        mSpeechRecognizer.setParameter(SpeechConstant.ASR_PTT, "1");
        mSpeechRecognizer.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mSpeechRecognizer.setParameter(SpeechConstant.AUDIO_SOURCE, "-1");
    }

    private View addUser(User user) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, DisplayUtil.dip2px(getActivity(), 10), 0);
        ShowUserLayout showUserLayout = new ShowUserLayout(getActivity(), user.getRealName(), user.getPortraitUrl(), findColorById(R.color.colorPrimary));
        showUserLayout.setLayoutParams(params);
        return showUserLayout;
    }

    private String getUserIds() {
        StringBuilder sb = new StringBuilder();
        if (employeeBeen != null) {
            employeeBeen.size();
            for (int i = 0; i < employeeBeen.size(); i++) {
                sb.append(String.valueOf(employeeBeen.get(i).getUser().getUserId()));
                if (i != employeeBeen.size() - 1) {
                    sb.append(",");
                }
            }
        }
        return sb.toString();
    }

    private String getDataStr(Calendar calendar) {
        calendar.getTimeInMillis();
        return DataUtil.timeFormat(calendar.getTimeInMillis(), "yyyy-MM-dd HH:mm:00");
    }

    @Override
    public void uploadImgSuccess() {
        take_photo_view.setImages(images);
    }

    @Override
    public void uploadImgFail(Image image) {
        if (images.contains(image)) {
            images.remove(image);
            take_photo_view.setImages(images);
        }
    }

    @Override
    public void uploadImgSuccess(int position, Image image) {
        images.remove(position);
        images.add(position, image);
        take_photo_view.setImages(images);
    }

    @Override
    public void showErrorMessage(String message) {
        if (!TextUtils.isEmpty(message)) {
            getApp().showToast(message);
        }
    }

    @Override
    public void uploadVoiceSuccess(String voiceUrl) {
        if (uploadJson != null && mPresenter != null) {
            try {
                uploadJson.put("xworkSound", voiceUrl);
                mPresenter.uploadAllData(uploadJson);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void uploadDataSuccess() {
        if (mPresenter != null) {
            mPresenter.deleteVoiceFile(voiceFile);
        }
        getApp().showToast("发布成功");
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    @Override
    public void showUploadProgress() {
        showProgressDialog("发布中...");
    }

    @Override
    public void hideUploadProgress() {
        hideProgressDialog();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (speechDialog != null) {
            speechDialog.cancel();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        MediaPlayerManager.release();
        cancelPlay();
    }

    @Override
    public void onResume() {
        super.onResume();
        MediaPlayerManager.resume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MediaPlayerManager.release();
    }

}
