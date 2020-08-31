package com.sito.customer.view.home.discover.generate.repair;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.afollestad.materialdialogs.MaterialDialog;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechRecognizer;
import com.sito.customer.R;
import com.sito.customer.app.CustomerApp;
import com.sito.customer.common.ConstantStr;
import com.sito.customer.mode.bean.User;
import com.sito.customer.mode.bean.db.Image;
import com.sito.customer.mode.bean.employee.EmployeeBean;
import com.sito.customer.utils.CountDownTimerUtils;
import com.sito.customer.utils.MediaPlayerManager;
import com.sito.customer.utils.PhotoUtils;
import com.sito.customer.view.MvpFragment;
import com.sito.customer.view.alarm.adduser.EmployeeActivity;
import com.sito.customer.view.home.discover.equiplist.EquipListActivity;
import com.sito.customer.widget.SpeechDialog;
import com.sito.customer.widget.TakePhotoView;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

/**
 * 生成检修任务
 * Created by zhangan on 2017/9/29.
 */

public class GenerateRepairFragment extends MvpFragment<GenerateRepairContract.Presenter>
        implements GenerateRepairContract.View, View.OnClickListener {

    public SpeechRecognizer mSpeechRecognizer;
    private TextView faultTimeTv, deviceNameTv;
    private TextView startTimeTv, endTimeTv;
    private EditText id_fault_content;
    private LinearLayout mAddUserLayout;
    private TakePhotoView take_photo_view;

    private RecordManager mRecordManager;
    private SpeechDialog speechDialog;
    private int voiceTime;
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
    private JSONObject uploadJson;

    public static GenerateRepairFragment newInstance() {
        Bundle args = new Bundle();
        GenerateRepairFragment fragment = new GenerateRepairFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new GenerateRepairPresenter(CustomerApp.getInstance().getGenerateRepositoryComponent().getRepository(), this);
        images = new ArrayList<>();
        mStartCalender = Calendar.getInstance(Locale.CHINA);
        mEndCalender = Calendar.getInstance(Locale.CHINA);
        initSpeech();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_generate_repair, container, false);
        rootView.findViewById(R.id.id_fault_ll_device).setOnClickListener(this);
        rootView.findViewById(R.id.id_fault_speech).setOnClickListener(this);
        rootView.findViewById(R.id.id_fault_commit).setOnClickListener(this);
        rootView.findViewById(R.id.ll_voice_time).setOnClickListener(this);
        rootView.findViewById(R.id.ib_add_user).setOnClickListener(this);
        rootView.findViewById(R.id.ll_repair_start_time).setOnClickListener(this);
        rootView.findViewById(R.id.ll_repair_end_time).setOnClickListener(this);
        faultTimeTv = rootView.findViewById(R.id.id_fault_time);
        deviceNameTv = rootView.findViewById(R.id.id_fault_device_name);
        startTimeTv = rootView.findViewById(R.id.tv_repair_start_time);
        endTimeTv = rootView.findViewById(R.id.tv_repair_end_time);
        mAddUserLayout = rootView.findViewById(R.id.ll_employee_add);
        id_fault_content = rootView.findViewById(R.id.id_fault_content);
        take_photo_view = rootView.findViewById(R.id.take_photo_view);
        take_photo_view.setTakePhotoListener(new TakePhotoView.TakePhotoListener() {

            @Override
            public void onTakePhoto() {
                new MaterialDialog.Builder(getActivity())
                        .items(R.array.choose_photo)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                                mImage = null;
                                if (position == 0) {
                                    photoFile = new File(CustomerApp.getInstance().imageCacheFile(), System.currentTimeMillis() + ".jpg");
                                    ActivityUtils.startCameraToPhoto(GenerateRepairFragment.this, photoFile, ACTION_START_CAMERA);
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
                                    photoFile = new File(CustomerApp.getInstance().imageCacheFile(), System.currentTimeMillis() + ".jpg");
                                    ActivityUtils.startCameraToPhoto(GenerateRepairFragment.this, photoFile, ACTION_START_CAMERA);
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
        mRecordManager = new RecordManager().setFileName(CustomerApp.getInstance().voiceCacheFile())
                .setSpeechRecognizer(mSpeechRecognizer)
                .setRecordListener(new RecordManager.RecordListener() {

                    @Override
                    public void onSpeechRecognizerError(String errorMessage) {
                        getApp().showToast(errorMessage);
                    }

                    @Override
                    public void onSpeechFinish(int time, String content, String voiceFile) {
                        GenerateRepairFragment.this.voiceTime = time;
                        GenerateRepairFragment.this.voiceContent = content;
                        GenerateRepairFragment.this.voiceFile = voiceFile;
                        updateVoiceUi();
                    }

                });
        return rootView;
    }

    private void updateVoiceUi() {
        String timeStr = String.valueOf(voiceTime) + "''";
        faultTimeTv.setText(timeStr);
        id_fault_content.setText(voiceContent);
        id_fault_content.setSelection(voiceContent.length());
    }

    @Override
    public void setPresenter(GenerateRepairContract.Presenter presenter) {
        mPresenter = presenter;
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
                CustomerApp.getInstance().showToast("初始化失败，错误码：" + code);
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

    AnimationDrawable animation;
    CountDownTimerUtils mCountDownTimerUtils;

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.id_fault_ll_device:
                Intent intent = new Intent(getActivity(), EquipListActivity.class);
                startActivityForResult(intent, request_equipment);
                break;
            case R.id.id_fault_speech:
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
            case R.id.ll_voice_time:
                if (TextUtils.isEmpty(voiceFile)) {
                    getApp().showToast("请录音");
                    return;
                }
                faultTimeTv.setBackgroundResource(R.drawable.play_anim);
                animation = (AnimationDrawable) faultTimeTv.getBackground();
                animation.start();
                mCountDownTimerUtils = new CountDownTimerUtils(faultTimeTv, voiceTime * 1000
                        , 1000, voiceTime + "''", "#FFFFFF");
                mCountDownTimerUtils.start();
                MediaPlayerManager.playSound(voiceFile, new MediaPlayer.OnCompletionListener() {

                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mCountDownTimerUtils.cancel();
                        MediaPlayerManager.release();
                        animation.selectDrawable(0);
                        animation.stop();
                        faultTimeTv.clearAnimation();
                        String voiceTimeStr = voiceTime + "''";
                        faultTimeTv.setText(voiceTimeStr);
                    }
                });
                break;
            case R.id.ib_add_user:
                Intent userIntent = new Intent(getActivity(), EmployeeActivity.class);
                userIntent.putParcelableArrayListExtra(ConstantStr.KEY_BUNDLE_LIST, employeeBeen);
                startActivityForResult(userIntent, request_add_user);
                break;
            case R.id.ll_repair_start_time:
                new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, final int year, final int month, final int dayOfMonth) {
                        new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                mStartCalender.set(year, month, dayOfMonth, hourOfDay, minute);
                                startTime = getDataStr(mStartCalender);
                                startTimeTv.setText(startTime);
                            }
                        }, 0, 0, true).show();
                    }
                }, mStartCalender.get(Calendar.YEAR), mStartCalender.get(Calendar.MONTH), mStartCalender.get(Calendar.DAY_OF_MONTH))
                        .show();
                break;
            case R.id.ll_repair_end_time:
                new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, final int year, final int month, final int dayOfMonth) {
                        new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                mEndCalender.set(year, month, dayOfMonth, hourOfDay, minute);
                                endTime = getDataStr(mEndCalender);
                                endTimeTv.setText(endTime);
                            }
                        }, 0, 0, true).show();
                    }
                }, mEndCalender.get(Calendar.YEAR), mEndCalender.get(Calendar.MONTH), mEndCalender.get(Calendar.DAY_OF_MONTH))
                        .show();
                break;
            case R.id.id_fault_commit:
                if (mPresenter == null) {
                    return;
                }
                for (int i = 0; i < take_photo_view.getImages().size(); i++) {
                    if (!take_photo_view.getImages().get(i).isUpload()) {
                        showErrorMessage("正在上传照片,请稍等...");
                        return;
                    }
                }
                images = take_photo_view.getImages();
                uploadJson = new JSONObject();
                try {
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
                        uploadJson.put("picsUrlAdd", imageUrl);
                    }
                    if (TextUtils.isEmpty(equipmentId)) {
                        getApp().showToast("请选择设备");
                        return;
                    }
                    uploadJson.put("equipmentId", equipmentId);
                    voiceContent = id_fault_content.getText().toString();
                    if (TextUtils.isEmpty(voiceContent)) {
                        getApp().showToast("请输入检修内容");
                        return;
                    }
                    uploadJson.put("repairIntro", String.valueOf(voiceContent));
                    if (TextUtils.isEmpty(getUserIds())) {
                        getApp().showToast("请选择指派人");
                        return;
                    }
                    uploadJson.put("usersNext", String.valueOf(getUserIds()));
                    if (TextUtils.isEmpty(startTime)) {
                        getApp().showToast("选择开始时间");
                    }
                    uploadJson.put("startTime", startTime);
                    if (TextUtils.isEmpty(endTime)) {
                        getApp().showToast("选择结束时间");
                    }
                    uploadJson.put("endTime", endTime);
                    if (!TextUtils.isEmpty(voiceFile) && voiceTime != 0) {
                        uploadJson.put("soundTimescaleAdd", String.valueOf(voiceTime));
                        mPresenter.uploadVoiceFile("fault", voiceFile);
                    } else {
                        mPresenter.uploadAllData(uploadJson);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (speechDialog != null) {
            speechDialog.cancel();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTION_START_CAMERA && resultCode == RESULT_OK) {
            PhotoUtils.cropPhoto(getActivity(), photoFile, new PhotoUtils.PhotoListener() {
                @Override
                public void onSuccess(File file) {
                    uploadPhoto(file);
                }
            });
        } else if (requestCode == request_equipment && resultCode == RESULT_OK) {
            equipmentId = data.getStringExtra(EquipListActivity.TYPE);
            equipmentName = data.getStringExtra(EquipListActivity.NAME);
            updateEquipmentUi();
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
                imageButton.setBackground(findDrawById(R.drawable.bg_add_user));
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
                final HorizontalScrollView scrollView = getView().findViewById(R.id.id_hs_employee);
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
                PhotoUtils.cropPhoto(getActivity(), photo, new PhotoUtils.PhotoListener() {
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
                mPresenter.uploadImage("fault", mImage);
            } else {
                Image image = new Image();
                images.add(image);
                image.setImageLocal(file.getAbsolutePath());
                mPresenter.uploadImage("fault", image);
            }
        }
        take_photo_view.setImages(images);
    }


    private void updateEquipmentUi() {
        deviceNameTv.setText(equipmentName);
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
        if (uploadJson != null) {
            try {
                uploadJson.put("voiceUrlAdd", voiceUrl);
                if (mPresenter != null) {
                    mPresenter.uploadAllData(uploadJson);
                }
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
        getActivity().finish();
        getApp().showToast("发布成功");
    }

    @Override
    public void showUploadProgress() {
        showProgressDialog("发布中...");
    }

    @Override
    public void hideUploadProgress() {
        hideProgressDialog();
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
        if (employeeBeen != null && employeeBeen.size() >= 0) {
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
    public void onPause() {
        super.onPause();
        MediaPlayerManager.release();
        if (mCountDownTimerUtils != null) {
            mCountDownTimerUtils.cancel();
        }
        if (animation != null && animation.isRunning()) {
            animation.selectDrawable(0);
            animation.stop();
            faultTimeTv.clearAnimation();
            faultTimeTv.setText(voiceTime + "''");
        }
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
