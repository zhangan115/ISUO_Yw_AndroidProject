package com.isuo.yw2application.view.main.work.tool.return_tools;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.isuo.yw2application.R;
import com.isuo.yw2application.app.Yw2Application;
import com.isuo.yw2application.common.ConstantStr;
import com.isuo.yw2application.mode.tools.ToolsRepository;
import com.isuo.yw2application.mode.tools.bean.CheckListBean;
import com.isuo.yw2application.mode.tools.bean.Tools;
import com.isuo.yw2application.mode.tools.bean.ToolsLog;
import com.isuo.yw2application.utils.ChooseDateDialog;
import com.isuo.yw2application.utils.PhotoUtils;
import com.isuo.yw2application.view.base.BaseActivity;
import com.isuo.yw2application.view.main.work.tool.add.AddToolsActivity;
import com.isuo.yw2application.view.photo.ViewPagePhotoActivity;
import com.isuo.yw2application.widget.CheckListItemLayout;
import com.qw.soul.permission.SoulPermission;
import com.qw.soul.permission.bean.Permission;
import com.qw.soul.permission.bean.Permissions;
import com.qw.soul.permission.callbcak.CheckRequestPermissionsListener;
import com.sito.library.utils.ActivityUtils;
import com.sito.library.utils.DataUtil;
import com.sito.library.utils.FileFromUri;
import com.sito.library.utils.GlideUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import pub.devrel.easypermissions.AppSettingsDialog;

/**
 * 归还
 * Created by zhangan on 2018/4/8.
 */

public class ReturnToolsActivity extends BaseActivity implements ReturnToolsContract.View {

    private ReturnToolsContract.Presenter mPresenter;
    private JSONObject jsonObject;
    private Calendar mCreateCalender;
    //view
    private TextView tvToolsReturnTime;
    private LinearLayout llCheckList, llAllCheckList;
    private List<CheckListBean> checkListBeans;
    private ImageView ivToolsPhoto;
    private File photoFile;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutAndToolbar(R.layout.return_tools_activity, "归还");
        Tools tools = getIntent().getParcelableExtra(ConstantStr.KEY_BUNDLE_OBJECT);
        if (tools == null) {
            finish();
            return;
        }
        jsonObject = new JSONObject();
        new ReturnToolsPresenter(ToolsRepository.getRepository(this), this);
        mPresenter.subscribe();
        mCreateCalender = Calendar.getInstance(Locale.CHINA);
        ((TextView) findViewById(R.id.tvToolsName)).setText(tools.getToolName());
        tvToolsReturnTime = findViewById(R.id.tvToolsReturnTime);
        findViewById(R.id.llReturnTime).setOnClickListener(this);
        findViewById(R.id.btnReturn).setOnClickListener(this);
        llCheckList = findViewById(R.id.llCheckList);
        llAllCheckList = findViewById(R.id.llAllCheckList);
        ivToolsPhoto = findViewById(R.id.ivToolsPhoto);
        progressBar = findViewById(R.id.progressBar);
        ivToolsPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(ReturnToolsActivity.this.photoUrl)) {
                    ViewPagePhotoActivity.startActivity(ReturnToolsActivity.this, new String[]{ReturnToolsActivity.this.photoUrl}, 0);
                    return;
                }
                takePhoto();
            }
        });
        ivToolsPhoto.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new MaterialDialog.Builder(ReturnToolsActivity.this)
                        .items(R.array.choose_condition_2)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                                switch (position) {
                                    case 0://删除照片
                                        photoFile = null;
                                        ReturnToolsActivity.this.photoUrl = null;
                                        ivToolsPhoto.setImageDrawable(findDrawById(R.drawable.photograph));
                                        break;
                                    default://重新拍照
                                        photoFile = null;
                                        ReturnToolsActivity.this.photoUrl = null;
                                        ivToolsPhoto.setImageDrawable(findDrawById(R.drawable.photograph));
                                        takePhoto();
                                        break;
                                }
                            }
                        })
                        .show();
                return photoFile != null;
            }
        });
        mPresenter.getToolsLog(tools.getToolId());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnReturn:
                if (this.progressBar.getVisibility()==View.VISIBLE){
                    Toast.makeText(this,"图片上传中...请稍等",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!jsonObject.has("returnTime")) {
                    Yw2Application.getInstance().showToast("请选择归还时间");
                    return;
                }
                if (checkListBeans != null) {
                    for (int i = 0; i < checkListBeans.size(); i++) {
                        if (TextUtils.isEmpty(checkListBeans.get(i).getValue())) {
                            Yw2Application.getInstance().showToast("请完成检测项");
                            return;
                        }
                    }
                    try {
                        jsonObject.put("checkListValues", new Gson().toJson(checkListBeans));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                EditText noteEt = findViewById(R.id.et_content);
                String note = noteEt.getText().toString();
                if (!TextUtils.isEmpty(note)) {
                    try {
                        jsonObject.put("view", note);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (jsonObject.has("view")) {
                    jsonObject.remove("view");
                }
                if (!TextUtils.isEmpty(this.photoUrl)){
                    try {
                        jsonObject.put("picUrl",this.photoUrl);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (jsonObject.has("picUrl")) {
                    jsonObject.remove("picUrl");
                }
                mPresenter.returnTools(jsonObject);
                break;
            case R.id.llReturnTime:
                new ChooseDateDialog(this, R.style.MyDateDialog)
                        .setCurrent(mCreateCalender)
                        .setResultListener(new ChooseDateDialog.OnDateChooseListener() {
                            @Override
                            public void onDate(Calendar calendar) {
                                mCreateCalender = calendar;
                                String time = getDataStr(mCreateCalender);
                                tvToolsReturnTime.setText(time);
                                try {
                                    jsonObject.put("returnTime", time);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).show();
                break;
        }
    }

    private static final int ACTION_START_CAMERA = 200;
    private static final int ACTION_START_PHOTO = 203;

    private void takePhoto() {
        Permissions permissions = Permissions.build(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA);
        SoulPermission.getInstance().checkAndRequestPermissions(permissions,
                new CheckRequestPermissionsListener() {
                    @Override
                    public void onAllPermissionOk(Permission[] allPermissions) {
                        new MaterialDialog.Builder(ReturnToolsActivity.this)
                                .items(R.array.choose_photo)
                                .itemsCallback(new MaterialDialog.ListCallback() {
                                    @Override
                                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                                        if (position == 0) {
                                            photoFile = new File(Yw2Application.getInstance().imageCacheFile(), System.currentTimeMillis() + ".jpg");
                                            ActivityUtils.startCameraToPhoto(ReturnToolsActivity.this, photoFile, ACTION_START_CAMERA);
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
                        new AppSettingsDialog.Builder(ReturnToolsActivity.this)
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
        photoFile = file;
        progressBar.setVisibility(View.VISIBLE);
        mPresenter.uploadImage(file);
    }

    private String photoUrl = null;

    @Override
    public void uploadImageSuccess(String url) {
        progressBar.setVisibility(View.GONE);
        this.photoUrl = url;
        GlideUtils.ShowImage(ReturnToolsActivity.this, this.photoUrl, ivToolsPhoto, R.drawable.picture_default);
    }

    @Override
    public void uploadImageFail() {
        progressBar.setVisibility(View.GONE);
        photoFile = null;
        ivToolsPhoto.setImageDrawable(findDrawById(R.drawable.photograph));
    }

    @Override
    public void returnSuccess() {
        setResult(Activity.RESULT_OK);
        finish();
    }

    @Override
    public void showToolsLog(ToolsLog toolsLog) {
        ((TextView) findViewById(R.id.tvToolsBorrowUser)).setText(toolsLog.getUseUser().getRealName());
        ((TextView) findViewById(R.id.tvToolsUse)).setText(toolsLog.getUse());
        ((TextView) findViewById(R.id.tvBorrowToolsTime)).setText(DataUtil.timeFormat(toolsLog.getUseTime(), "yyyy-MM-dd HH:mm"));
        try {
            jsonObject.put("logId", toolsLog.getLogId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (toolsLog.getCheckList() != null && toolsLog.getCheckList().size() > 0) {
            showCheckList(toolsLog.getCheckList());
        } else {
            noCheckList();
        }
    }

    @Override
    public void toolsLogError() {

    }

    @Override
    public void showCheckList(List<CheckListBean> checkListBeans) {
        this.checkListBeans = checkListBeans;
        llAllCheckList.setVisibility(View.VISIBLE);
        for (int i = 0; i < checkListBeans.size(); i++) {
            CheckListItemLayout layout = new CheckListItemLayout(this);
            layout.setCheckListBean(checkListBeans.get(i));
            llCheckList.addView(layout);
        }
    }

    @Override
    public void noCheckList() {
        llAllCheckList.setVisibility(View.GONE);
    }

    @Override
    public void setPresenter(ReturnToolsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    private String getDataStr(Calendar calendar) {
        return DataUtil.timeFormat(calendar.getTimeInMillis(), "yyyy-MM-dd HH:mm");
    }

}
