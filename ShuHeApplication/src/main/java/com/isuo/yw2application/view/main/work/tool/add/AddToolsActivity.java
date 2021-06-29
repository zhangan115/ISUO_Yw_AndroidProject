package com.isuo.yw2application.view.main.work.tool.add;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.isuo.yw2application.R;
import com.isuo.yw2application.app.Yw2Application;
import com.isuo.yw2application.common.ConstantStr;
import com.isuo.yw2application.mode.tools.ToolsRepository;
import com.isuo.yw2application.mode.tools.bean.Tools;
import com.isuo.yw2application.utils.PhotoUtils;
import com.isuo.yw2application.view.base.BaseActivity;
import com.isuo.yw2application.view.main.task.increment.submit.IncrementActivity;
import com.isuo.yw2application.view.photo.ViewPagePhotoActivity;
import com.qw.soul.permission.SoulPermission;
import com.qw.soul.permission.bean.Permission;
import com.qw.soul.permission.bean.Permissions;
import com.qw.soul.permission.callbcak.CheckRequestPermissionsListener;
import com.sito.library.utils.ActivityUtils;
import com.sito.library.utils.DataUtil;
import com.sito.library.utils.FileFromUri;
import com.sito.library.utils.GlideUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import pub.devrel.easypermissions.AppSettingsDialog;

/**
 * 新增tools
 * Created by zhangan on 2018/4/4.
 */

public class AddToolsActivity extends BaseActivity implements AddToolsContract.View {

    private Tools tools;
    private File photoFile, takePhoto;
    private static final int ACTION_START_CAMERA = 200;
    private static final int ACTION_START_PHOTO = 203;
    private AddToolsContract.Presenter mPresenter;
    //view
    private TextView tvToolsBuyTime, tvToolState, tvIsQualified, tvDetectionTime;
    private EditText editToolsName, editToolsModel, editToolsNum;
    private EditText editToolsManufacturer, editToolsCount, editToolsUnitPrice;
    private EditText editToolsDepositaryPlace, editToolsDes;
    private EditText editToolCertificateNo, editToolDetectionType;
    private EditText editToolDetectionSite, etValidityTerm;
    private ImageView ivToolsPhoto;
    private ProgressBar progressBar;
    private Calendar mCreateCalender, mDetectionTime;
    private String time, detectionTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tools = getIntent().getParcelableExtra(ConstantStr.KEY_BUNDLE_OBJECT);
        boolean isAdd = tools == null;
        setLayoutAndToolbar(R.layout.tools_add_activity, isAdd ? "新增" : "编辑");
        new AddToolsPresenter(ToolsRepository.getRepository(this), this);
        mPresenter.subscribe();
        if (tools == null) {
            tools = new Tools();
        }
        initView();
    }

    private void initView() {
        findViewById(R.id.btnAdd).setOnClickListener(this);
        findViewById(R.id.llChooseTime).setOnClickListener(this);

        findViewById(R.id.llToolState).setOnClickListener(this);
        findViewById(R.id.llIsQualified).setOnClickListener(this);
        findViewById(R.id.llDetectionTime).setOnClickListener(this);

        tvToolsBuyTime = findViewById(R.id.tvToolsBuyTime);
        tvToolState = findViewById(R.id.tvToolState);
        tvIsQualified = findViewById(R.id.tvIsQualified);
        tvDetectionTime = findViewById(R.id.tvDetectionTime);

        editToolsName = findViewById(R.id.editToolsName);
        editToolsModel = findViewById(R.id.editToolsModel);
        editToolsNum = findViewById(R.id.editToolsNum);
        editToolsManufacturer = findViewById(R.id.editToolsManufacturer);
        editToolsCount = findViewById(R.id.editToolsCount);
        editToolsUnitPrice = findViewById(R.id.editToolsUnitPrice);
        editToolsDepositaryPlace = findViewById(R.id.editToolsDepositaryPlace);
        editToolsDes = findViewById(R.id.editToolsDes);

        editToolCertificateNo = findViewById(R.id.editToolCertificateNo);
        editToolDetectionType = findViewById(R.id.editToolDetectionType);
        editToolDetectionSite = findViewById(R.id.editToolDetectionSite);
        etValidityTerm = findViewById(R.id.etValidityTerm);

        ivToolsPhoto = findViewById(R.id.ivToolsPhoto);
        ivToolsPhoto.setOnClickListener(this);
        ivToolsPhoto.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new MaterialDialog.Builder(AddToolsActivity.this)
                        .items(R.array.choose_condition_2)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                                switch (position) {
                                    case 0://删除照片
                                        takePhoto = null;
                                        tools.setToolPic(null);
                                        GlideUtils.ShowImage(AddToolsActivity.this, tools.getToolPic(), ivToolsPhoto, R.drawable.photograph);
                                        break;
                                    default://重新拍照
                                        takePhoto = null;
                                        tools.setToolPic(null);
                                        GlideUtils.ShowImage(AddToolsActivity.this, tools.getToolPic(), ivToolsPhoto, R.drawable.photograph);
                                        takePhoto();
                                        break;
                                }
                            }
                        })
                        .show();
                return takePhoto != null;
            }
        });
        mCreateCalender = Calendar.getInstance(Locale.CHINA);
        mDetectionTime = Calendar.getInstance(Locale.CHINA);
        progressBar = findViewById(R.id.progressBar);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAdd:
                if (TextUtils.isEmpty(editToolsName.getText().toString())) {
                    Yw2Application.getInstance().showToast("请输入工具名称");
                    return;
                }
                tools.setToolName(editToolsName.getText().toString());
                tools.setDepositaryPlace(editToolsDepositaryPlace.getText().toString());
                tools.setManufacturer(editToolsManufacturer.getText().toString());
                try {
                    int count = Integer.valueOf(editToolsCount.getText().toString());
                    tools.setToolCount(count);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                tools.setToolDesc(editToolsDes.getText().toString());
                if (!TextUtils.isEmpty(time)) {
                    tools.setBuyTime(mCreateCalender.getTimeInMillis());
                }
                if (!TextUtils.isEmpty(detectionTime)) {
                    tools.setDetectionTime(mDetectionTime.getTimeInMillis());
                }
                try {
                    float price = Float.valueOf(editToolsUnitPrice.getText().toString());
                    tools.setUnitPrice(price);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (!TextUtils.isEmpty(editToolCertificateNo.getText())) {
                    tools.setToolCertificateNo(editToolCertificateNo.getText().toString());
                }
                if (!TextUtils.isEmpty(editToolDetectionType.getText())) {
                    tools.setToolDetectionType(editToolDetectionType.getText().toString());
                }
                if (!TextUtils.isEmpty(editToolDetectionSite.getText())) {
                    tools.setToolDetectionSite(editToolDetectionSite.getText().toString());
                }
                if (!TextUtils.isEmpty(etValidityTerm.getText())) {
                    try {
                        int year = Integer.valueOf(etValidityTerm.getText().toString());
                        tools.setValidityTerm(year);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                tools.setToolNumber(editToolsNum.getText().toString());
                tools.setToolModel(editToolsModel.getText().toString());
                mPresenter.addTools(tools);
                break;
            case R.id.llChooseTime:
                new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, final int year, final int month, final int dayOfMonth) {
                        mCreateCalender.set(year, month, dayOfMonth);
                        time = getDataStr(mCreateCalender);
                        tvToolsBuyTime.setText(time);
                    }
                }, mCreateCalender.get(Calendar.YEAR)
                        , mCreateCalender.get(Calendar.MONTH)
                        , mCreateCalender.get(Calendar.DAY_OF_MONTH))
                        .show();
                break;
            case R.id.ivToolsPhoto:
                if (!TextUtils.isEmpty(tools.getToolPic())) {
                    ViewPagePhotoActivity.startActivity(this, new String[]{takePhoto.getAbsolutePath()}, 0);
                    return;
                }
                takePhoto();
                break;
            case R.id.llToolState:
                ArrayList<String> items = new ArrayList<>();
                items.add("消耗品");
                items.add("公用品");
                new MaterialDialog.Builder(this).items(items).itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        tvToolState.setText(text);
                        tools.setToolType(String.valueOf(position + 1));
                    }
                }).show();
                break;
            case R.id.llIsQualified:
                ArrayList<String> items1 = new ArrayList<>();
                items1.add("合格");
                items1.add("不合格");
                new MaterialDialog.Builder(this).items(items1).itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        tvIsQualified.setText(text);
                        tools.setIsQualified(position);
                    }
                }).show();
                break;
            case R.id.llDetectionTime:
                new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, final int year, final int month, final int dayOfMonth) {
                        mDetectionTime.set(year, month, dayOfMonth);
                        detectionTime = getDataStr(mDetectionTime);
                        tvDetectionTime.setText(detectionTime);
                    }
                }, mDetectionTime.get(Calendar.YEAR)
                        , mDetectionTime.get(Calendar.MONTH)
                        , mDetectionTime.get(Calendar.DAY_OF_MONTH))
                        .show();
                break;
        }
    }

    private void takePhoto() {
        Permissions permissions = Permissions.build(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA);
        SoulPermission.getInstance().checkAndRequestPermissions(permissions,
                new CheckRequestPermissionsListener() {
                    @Override
                    public void onAllPermissionOk(Permission[] allPermissions) {
                        new MaterialDialog.Builder(AddToolsActivity.this)
                                .items(R.array.choose_photo)
                                .itemsCallback(new MaterialDialog.ListCallback() {
                                    @Override
                                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                                        if (position == 0) {
                                            photoFile = new File(Yw2Application.getInstance().imageCacheFile(), System.currentTimeMillis() + ".jpg");
                                            ActivityUtils.startCameraToPhoto(AddToolsActivity.this, photoFile, ACTION_START_CAMERA);
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
                        new AppSettingsDialog.Builder(AddToolsActivity.this)
                                .setRationale(getString(R.string.need_save_setting))
                                .setTitle(getString(R.string.request_permissions))
                                .setPositiveButton(getString(R.string.sure))
                                .setNegativeButton(getString(R.string.cancel))
                                .build()
                                .show();
                    }
                });
    }

    private String getDataStr(Calendar calendar) {
        return DataUtil.timeFormat(calendar.getTimeInMillis(), "yyyy-MM-dd");
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTION_START_CAMERA && resultCode == RESULT_OK) {
            PhotoUtils.cropPhoto(this, photoFile, new PhotoUtils.PhotoListener() {
                @Override
                public void onSuccess(File file) {
                    uploadImage(file);
                }

            });
        } else if (requestCode == ACTION_START_PHOTO && resultCode == RESULT_OK) {
            if (data.getData() == null) {
                Yw2Application.getInstance().showToast("图片选择失败!");
                return;
            }
            try {
                File photo = FileFromUri.from(this.getApplicationContext(), data.getData());
                PhotoUtils.cropPhoto(this, photo, new PhotoUtils.PhotoListener() {
                    @Override
                    public void onSuccess(File file) {
                        uploadImage(file);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage(File file) {
        takePhoto = file;
        GlideUtils.ShowImage(AddToolsActivity.this, file, ivToolsPhoto, R.drawable.picture_default);
        progressBar.setVisibility(View.VISIBLE);
        mPresenter.uploadImage(file);
    }

    @Override
    public void uploadToolsSuccess() {
        Yw2Application.getInstance().showToast("成功");
        setResult(Activity.RESULT_OK);
        finish();
    }

    @Override
    public void uploadToolsFail() {
        Yw2Application.getInstance().showToast("失败");
    }

    @Override
    public void uploadImageSuccess(String url) {
        progressBar.setVisibility(View.GONE);
        tools.setToolPic(url);
    }

    @Override
    public void uploadImageFail() {
        progressBar.setVisibility(View.GONE);
        takePhoto = null;
        tools.setToolPic(null);
        GlideUtils.ShowImage(AddToolsActivity.this, tools.getToolPic(), ivToolsPhoto, R.drawable.picture_default);
    }

    @Override
    public void setPresenter(AddToolsContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
