package com.sito.customer.view.home.work.fault;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.sito.customer.R;
import com.sito.customer.app.CustomerApp;
import com.sito.customer.common.ConstantInt;
import com.sito.customer.common.ConstantStr;
import com.sito.customer.mode.UploadResult;
import com.sito.customer.mode.bean.User;
import com.sito.customer.mode.bean.db.Image;
import com.sito.customer.mode.bean.db.ImageDao;
import com.sito.customer.mode.bean.db.Voice;
import com.sito.customer.mode.bean.db.VoiceDao;
import com.sito.customer.mode.bean.employee.EmployeeBean;
import com.sito.customer.mode.bean.fault.DefaultFlowBean;
import com.sito.customer.mode.bean.option.OptionBean;
import com.sito.customer.utils.CountDownTimerUtils;
import com.sito.customer.utils.MediaPlayerManager;
import com.sito.customer.utils.PhotoUtils;
import com.sito.customer.view.SpeechActivity;
import com.sito.customer.view.alarm.adduser.EmployeeActivity;
import com.sito.customer.view.home.discover.equiplist.EquipListActivity;
import com.sito.customer.view.home.discover.generate.increment.GenerateIncrementFragment;
import com.sito.customer.view.home.work.fault.history.FaultHistoryActivity;
import com.sito.customer.widget.FlowLayout;
import com.sito.customer.widget.SpeechDialog;
import com.sito.customer.widget.TakePhotoView;
import com.sito.library.utils.ActivityUtils;
import com.sito.library.utils.DisplayUtil;
import com.sito.library.utils.FileFromUri;
import com.sito.library.widget.ShowUserLayout;
import com.za.aacrecordlibrary.RecordManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FaultActivity extends SpeechActivity implements View.OnClickListener, FaultContract.View {

    private FaultContract.Presenter mPresenter;
    private TextView mDeviceName;
    private long equipId = -1;
    private TextView mFaultType;
    private ImageView mSpeech;
    private EditText mContent;
    private String mVoiceStr;
    private TextView mVoiceTime;

    private long mTime;
    private File photoFile;
    private String mPath;
    private LinearLayout mLLDevice;
    private LinearLayout mLLType;

    private List<OptionBean.ItemListBean> typeList;
    private int mFaultCode = -1;
    private String mVTime;
    private String mImageUrl;
    private String mNextUserId = "";
    private LinearLayout addEmployeeLayout;
    private ArrayList<EmployeeBean> chooseEmployeeBeen;
    private boolean isChooseEquip = true;
    private List<Image> images;
    private long mTaskId = -1;
    private TextView chooseTitleType1, chooseTitleType2;
    private LinearLayout mFlowLayout;
    private HorizontalScrollView mHSView;
    private List<FlowLayout> mFlowLayoutList = new ArrayList<>();
    private TakePhotoView takePhotoView;
    @Nullable
    private Image mImage;
    private final static int REQUEST_CODE_USER = 2;
    private RecordManager mRecordManager;
    private SpeechDialog speechDialog;
    private JSONObject jsonObject;

    private static final int request_equipment = 201;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutAndToolbar(R.layout.activity_fault, "故障上报");
        new FaultPresenter(CustomerApp.getInstance().getFaultRepositoryComponent().getRepository(), this);
        mTaskId = getIntent().getLongExtra(ConstantStr.KEY_BUNDLE_LONG, -1);
        initView();
        initEvent();
        initData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_fault, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu) {
            startActivity(new Intent(this, FaultHistoryActivity.class));
        }
        return true;
    }

    private void initView() {
        mDeviceName = (TextView) findViewById(R.id.id_fault_devicename);
        mFaultType = (TextView) findViewById(R.id.id_fault_type);
        mSpeech = (ImageView) findViewById(R.id.id_fault_speech);
        mContent = (EditText) findViewById(R.id.id_fault_content);
        mVoiceTime = (TextView) findViewById(R.id.id_fault_time);
        mFlowLayout = (LinearLayout) findViewById(R.id.ll_flow_layout);
        mLLDevice = (LinearLayout) findViewById(R.id.id_fault_ll_device);
        mLLType = (LinearLayout) findViewById(R.id.id_fault_ll_type);
        String equipmentName = getIntent().getStringExtra(ConstantStr.KEY_BUNDLE_STR);
        long equipmentId = getIntent().getLongExtra(ConstantStr.KEY_BUNDLE_LONG_1, -1);
        if (equipmentId != -1 && !TextUtils.isEmpty(equipmentName)) {
            equipId = equipmentId;
            mDeviceName.setText(equipmentName);
            isChooseEquip = false;//无法选择其他设备
            mDeviceName.setCompoundDrawables(null, null, null, null);
        }
        addEmployeeLayout = (LinearLayout) findViewById(R.id.ll_employee_add);
        mHSView = (HorizontalScrollView) findViewById(R.id.id_hs_employee);
        chooseTitleType1 = (TextView) findViewById(R.id.tv_type_1);
        chooseTitleType2 = (TextView) findViewById(R.id.tv_type_2);
        takePhotoView = (TakePhotoView) findViewById(R.id.take_photo_view);
        takePhotoView.setTakePhotoListener(new TakePhotoView.TakePhotoListener() {
            @Override
            public void onTakePhoto() {
                /*mImage = null;
                photoFile = new File(CustomerApp.getInstance().imageCacheFile(), System.currentTimeMillis() + ".jpg");
                ActivityUtils.startCameraToPhoto(FaultActivity.this, photoFile, ACTION_START_CAMERA);*/

                mImage = null;
                new MaterialDialog.Builder(FaultActivity.this)
                        .items(R.array.choose_photo)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                                if (position == 0) {
                                    photoFile = new File(CustomerApp.getInstance().imageCacheFile(), System.currentTimeMillis() + ".jpg");
                                    ActivityUtils.startCameraToPhoto(FaultActivity.this, photoFile, ACTION_START_CAMERA);
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
                CustomerApp.getInstance().getDaoSession().getImageDao().deleteInTx(image);
                takePhotoView.setImages(images);
            }

            @Override
            public void onTakePhoto(int position, final Image image) {
                /*mImage = image;
                photoFile = new File(CustomerApp.getInstance().imageCacheFile(), System.currentTimeMillis() + ".jpg");
                ActivityUtils.startCameraToPhoto(FaultActivity.this, photoFile, ACTION_START_CAMERA);*/

                new MaterialDialog.Builder(FaultActivity.this)
                        .items(R.array.choose_photo)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                                mImage = image;
                                if (position == 0) {
                                    photoFile = new File(CustomerApp.getInstance().imageCacheFile(), System.currentTimeMillis() + ".jpg");
                                    ActivityUtils.startCameraToPhoto(FaultActivity.this, photoFile, ACTION_START_CAMERA);
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
    }

    private void initEvent() {
        mSpeech.setOnClickListener(this);
        findViewById(R.id.id_fault_commit).setOnClickListener(this);
        mVoiceTime.setOnClickListener(this);
        mLLType.setOnClickListener(this);
        mLLDevice.setOnClickListener(this);
    }

    private void initData() {
        voiceName = CustomerApp.getInstance().voiceCacheFile();
        images = new ArrayList<>();
        List<OptionBean> list = CustomerApp.getInstance().getOptionInfo();
        if (list != null && list.size() > 0) {
            typeList = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getOptionId() == ConstantInt.FAULTTYPE) {
                    typeList.addAll(list.get(i).getItemList());
                }
            }
        }
        chooseEmployeeBeen = new ArrayList<>();
        if (isChooseEquip) {
            loadImageDataFromDb();
            loadVoiceDataFromDb();
        }
        addEmployee();
        if (CustomerApp.getInstance().getCurrentUser().getCustomer().getIsOpen() == 1) {
            chooseTitleType1.setVisibility(View.GONE);
            chooseTitleType2.setVisibility(View.VISIBLE);
            mHSView.setVisibility(View.GONE);
            mFlowLayout.setVisibility(View.VISIBLE);
            mPresenter.getUserFlowList();
        }
        mRecordManager = new RecordManager().setFileName(voiceName)
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
                        String timeStr = mVTime + "''";
                        mVoiceTime.setText(timeStr);
                        mTime = time * 1000;
                        saveVoiceInDb(mPath);
                    }

                });
    }

    private void loadImageDataFromDb() {
        images = CustomerApp.getInstance().getDaoSession().getImageDao().queryBuilder().where(ImageDao.Properties.WorkType.eq(ConstantInt.FAULT)
                , ImageDao.Properties.CurrentUserId.eq(CustomerApp.getInstance().getCurrentUser().getUserId())).list();
        takePhotoView.setImages(images);
    }

    private void loadVoiceDataFromDb() {
        List<Voice> voiceList = CustomerApp.getInstance().getDaoSession().getVoiceDao().queryBuilder()
                .where(VoiceDao.Properties.WorkType.eq(ConstantInt.FAULT), VoiceDao.Properties.IsUpload.eq(false)
                        , VoiceDao.Properties.CurrentUserId.eq(CustomerApp.getInstance().getCurrentUser().getUserId())).list();
        if (voiceList != null && voiceList.size() > 0) {
            if (!TextUtils.isEmpty(voiceList.get(0).getVoiceLocal())) {
                mPath = voiceList.get(0).getVoiceLocal();
                mContent.setText(voiceList.get(0).getMContent());
                mContent.setSelection(mContent.getText().toString().length());
                mVoiceStr = voiceList.get(0).getMContent();
                String voiceStr = voiceList.get(0).getVoiceTime() + "''";
                mVoiceTime.setText(voiceStr);
                mTime = Long.parseLong(voiceList.get(0).getVoiceTime()) * 1000;
                mVTime = voiceList.get(0).getVoiceTime();
                if (!TextUtils.isEmpty(mVoiceStr)) {
                    mVoiceTime.setEnabled(true);
                }
            }
        }
    }

    AnimationDrawable animation;
    CountDownTimerUtils mCountDownTimerUtils;

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.id_fault_speech:
                setParam();
                speechDialog = new SpeechDialog(FaultActivity.this) {

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
            case R.id.id_fault_time:
                //开始动画
                mVoiceTime.setBackgroundResource(R.drawable.play_anim);
                animation = (AnimationDrawable) mVoiceTime.getBackground();
                animation.start();
                //开始计时
                mCountDownTimerUtils = new CountDownTimerUtils(mVoiceTime, mTime, 1000, mVTime + "''", "#FFFFFF");
                mCountDownTimerUtils.start();
                //播放录音
                MediaPlayerManager.playSound(mPath, new MediaPlayer.OnCompletionListener() {

                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mVoiceTime.setBackgroundResource(R.drawable.record_play_3);
                    }
                });
                break;
            case R.id.id_fault_ll_device:
                if (isChooseEquip) {
                    Intent intent = new Intent(this, EquipListActivity.class);
                    startActivityForResult(intent, request_equipment);
                }
                break;
            case R.id.id_fault_ll_type:
                final List<String> mTypeItem = new ArrayList<>();
                for (int i = 0; i < typeList.size(); i++) {
                    mTypeItem.add(typeList.get(i).getItemName());
                }
                new MaterialDialog.Builder(FaultActivity.this)
                        .items(mTypeItem)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                                mFaultType.setText(typeList.get(position).getItemName());
                                mFaultCode = Integer.valueOf(typeList.get(position).getItemCode());
                            }
                        })
                        .show();
                break;
            case R.id.id_fault_commit://提交
                jsonObject = new JSONObject();
                try {
                    for (int i = 0; i < takePhotoView.getImages().size(); i++) {
                        if (!takePhotoView.getImages().get(i).getIsUpload()) {
                            CustomerApp.getInstance().showToast("正在上传照片,请稍等...");
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
                    if (equipId == -1) {
                        CustomerApp.getInstance().showToast("请选择设备");
                        return;
                    }
                    jsonObject.put("equipmentId", equipId);
                    if (mFaultCode == -1) {
                        CustomerApp.getInstance().showToast("请选择故障类型");
                        return;
                    }
                    jsonObject.put("faultType", mFaultCode);
                    if (TextUtils.isEmpty(mContent.getText().toString())) {
                        CustomerApp.getInstance().showToast("请输入故障内容");
                        return;
                    }
                    jsonObject.put("faultDescript", mContent.getText().toString());
                    if (TextUtils.isEmpty(mImageUrl)) {
                        CustomerApp.getInstance().showToast("请拍照");
                        return;
                    }
                    jsonObject.put("picsUrl", mImageUrl);
                    if (TextUtils.isEmpty(mNextUserId) && defaultFlowId == null) {
                        CustomerApp.getInstance().showToast("请选择指派人");
                        return;
                    }
                    if (defaultFlowId != null) {
                        jsonObject.put("defaultFlowId", defaultFlowId);
                        jsonObject.put("usersNext", "-");
                    } else {
                        jsonObject.put("usersNext", mNextUserId);
                    }
                    if (mTaskId != -1) {
                        jsonObject.put("taskId", mTaskId);
                    }
                    if (!TextUtils.isEmpty(mVTime)) {
                        jsonObject.put("soundTimescale", mVTime);
                        mPresenter.postVoiceFile(ConstantInt.FAULT, "fault");
                    } else {
                        mPresenter.postFaultInfo(jsonObject);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void saveVoiceInDb(String path) {
        List<Voice> voices = CustomerApp.getInstance().getDaoSession().getVoiceDao().queryBuilder()
                .where(VoiceDao.Properties.WorkType.eq(ConstantInt.FAULT)).list();
        if (voices != null && voices.size() > 0) {
            for (int i = 0; i < voices.size(); i++) {
                voices.get(i).setMContent(mVoiceStr);
                voices.get(i).setBackUrl("");
                voices.get(i).setVoiceLocal(path);
                voices.get(i).setUpload(false);
                voices.get(i).setSaveTime(System.currentTimeMillis());
                voices.get(i).setWorkType(ConstantInt.FAULT);
                voices.get(i).setVoiceTime(mVTime);
                CustomerApp.getInstance().getDaoSession().getVoiceDao().insertOrReplace(voices.get(i));
            }
        } else {
            Voice voice = new Voice();
            voice.setMContent(mVoiceStr);
            voice.setBackUrl("");
            voice.setVoiceLocal(path);
            voice.setUpload(false);
            voice.setSaveTime(System.currentTimeMillis());
            voice.setWorkType(ConstantInt.FAULT);
            voice.setVoiceTime(mVTime);
            CustomerApp.getInstance().getDaoSession().getVoiceDao().insertOrReplace(voice);
        }
    }

    private void uploadPhoto(File file) {
        if (mPresenter != null) {
            if (mImage != null) {
                mImage.setIsUpload(false);
                mImage.setImageLocal(file.getAbsolutePath());
                mImage.setBackUrl(null);
                mPresenter.uploadImage(ConstantInt.FAULT, "fault", mImage);
            } else {
                Image image = new Image();
                images.add(image);
                image.setImageLocal(file.getAbsolutePath());
                mPresenter.uploadImage(ConstantInt.FAULT, "fault", image);
            }
        }
        takePhotoView.setImages(images);
    }

    private static final int ACTION_START_CAMERA = 100;
    private static final int ACTION_START_PHOTO = 101;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTION_START_CAMERA && resultCode == RESULT_OK) {
            PhotoUtils.cropPhoto(this, photoFile, new PhotoUtils.PhotoListener() {
                @Override
                public void onSuccess(File file) {
                    uploadPhoto(file);
                }
            });
        } else if (requestCode == ACTION_START_PHOTO && resultCode == RESULT_OK) {
            try {
                File photo = FileFromUri.from(this, data.getData());
                PhotoUtils.cropPhoto(this, photo, new PhotoUtils.PhotoListener() {
                    @Override
                    public void onSuccess(File file) {
                        uploadPhoto(file);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == request_equipment && resultCode == RESULT_OK) {
            String equipmentId = data.getStringExtra(EquipListActivity.TYPE);
            String equipmentName = data.getStringExtra(EquipListActivity.NAME);
            mDeviceName.setText(equipmentName);
            equipId = Long.valueOf(equipmentId);
        } else if (requestCode == REQUEST_CODE_USER && resultCode == RESULT_OK) {
            ArrayList<EmployeeBean> employeeBeen = data.getParcelableArrayListExtra(ConstantStr.KEY_BUNDLE_LIST);
            chooseEmployeeBeen.clear();
            mNextUserId = "";
            if (employeeBeen != null && employeeBeen.size() > 0) {
                chooseEmployeeBeen.addAll(employeeBeen);
            }
            for (int i = 0; i < chooseEmployeeBeen.size(); i++) {
                mNextUserId = mNextUserId + chooseEmployeeBeen.get(i).getUser().getUserId() + ",";
            }
            if (!TextUtils.isEmpty(mNextUserId)) {
                mNextUserId = mNextUserId.substring(0, mNextUserId.length() - 1);
            }
            addEmployee();
        }
    }


    private void addEmployee() {
        addEmployeeLayout.removeAllViews();
        for (int i = 0; i < chooseEmployeeBeen.size(); i++) {
            addEmployeeLayout.addView(addUser(chooseEmployeeBeen.get(i).getUser()));
        }
        addEmployeeLayout.addView(getAddUserBtn());
    }

    private View addUser(User user) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, DisplayUtil.dip2px(this, 10), 0);
        ShowUserLayout layout = new ShowUserLayout(this, user.getRealName(), user.getPortraitUrl(), findColorById(R.color.colorPrimary));
        layout.setLayoutParams(params);
        return layout;
    }

    private ImageButton getAddUserBtn() {
        ImageButton imageButton = new ImageButton(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DisplayUtil.dip2px(this, 45)
                , DisplayUtil.dip2px(this, 45));
        imageButton.setBackground(findDrawById(R.drawable.bg_add_user));
        imageButton.setLayoutParams(params);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FaultActivity.this, EmployeeActivity.class);
                intent.putParcelableArrayListExtra(ConstantStr.KEY_BUNDLE_LIST, chooseEmployeeBeen);
                startActivityForResult(intent, REQUEST_CODE_USER);
            }
        });
        return imageButton;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (speechDialog != null) {
            speechDialog.cancel();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MediaPlayerManager.release();
        if (animation != null && animation.isRunning()) {
            animation.stop();
            mVoiceTime.setBackgroundResource(R.drawable.record_play_3);
            String timeStr = mVTime + "''";
            mVoiceTime.setText(timeStr);
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
    public void postSuccess(UploadResult result) {
        mPresenter.uploadSuccess(defaultFlowId);
        CustomerApp.getInstance().showToast("提交成功");
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void postVoiceSuccess(List<String> fileBeen) {
        if (fileBeen != null && fileBeen.size() > 0) {
            String mVoiceUrl = fileBeen.get(0);
            try {
                jsonObject.put("voiceUrl", mVoiceUrl);
                mPresenter.postFaultInfo(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void postFail(String message) {
        if (!TextUtils.isEmpty(message)) {
            CustomerApp.getInstance().showToast(message);
        }
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
        hideProgressDialog();
    }

    private List<DefaultFlowBean> mDefaultFlowBeen;
    private Long defaultFlowId;

    @Override
    public void showDefaultFlowList(@NonNull List<DefaultFlowBean> defaultFlowBeen) {
        mFlowLayoutList.clear();
        mDefaultFlowBeen = defaultFlowBeen;
        chooseTitleType2.setText("故障审批人已由管理员预设");
        defaultFlowId = defaultFlowBeen.get(0).getDefaultFlowId();
        if (defaultFlowBeen.size() == 1) {
            FlowLayout flowLayout = new FlowLayout(this);
            flowLayout.setContent(defaultFlowBeen.get(0).getDefaultFlowName(), defaultFlowBeen.get(0).getUsersN());
            mFlowLayout.addView(flowLayout);
        } else {
            for (int i = 0; i < defaultFlowBeen.size(); i++) {
                FlowLayout flowLayout = new FlowLayout(this);
                mFlowLayoutList.add(flowLayout);
                flowLayout.setContent(defaultFlowBeen.get(i).getDefaultFlowName(), defaultFlowBeen.get(i).getUsersN(), i == 0);
                flowLayout.setTag(R.id.tag_position, i);
                flowLayout.setOnClickListener(flowClickListener);
                mFlowLayout.addView(flowLayout);
            }
        }
    }

    private View.OnClickListener flowClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            int position = (int) v.getTag(R.id.tag_position);
            for (int i = 0; i < mFlowLayoutList.size(); i++) {
                mFlowLayoutList.get(i).setChooseState(i == position);
                if (i == position) {
                    defaultFlowId = mDefaultFlowBeen.get(i).getDefaultFlowId();
                }
            }
        }
    };


    @Override
    public void showDefaultFlowError() {
        chooseTitleType2.setText("未设置故障审批人，请去后台设置");
    }

    @Override
    public void uploadImageSuccess() {
        takePhotoView.setImages(images);
    }

    @Override
    public void uploadImageFail(Image image) {
        CustomerApp.getInstance().showToast("照片上传失败");
        if (images.contains(image)) {
            images.remove(image);
            takePhotoView.setImages(images);
        }
    }

    @Override
    public void setPresenter(FaultContract.Presenter presenter) {
        mPresenter = presenter;
    }

}
