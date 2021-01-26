package com.isuo.yw2application.view.main.work.tool.borrow;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
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
import com.isuo.yw2application.mode.bean.employee.EmployeeBean;
import com.isuo.yw2application.mode.tools.ToolsRepository;
import com.isuo.yw2application.mode.tools.bean.CheckListBean;
import com.isuo.yw2application.mode.tools.bean.Tools;
import com.isuo.yw2application.utils.PhotoUtils;
import com.isuo.yw2application.view.base.BaseActivity;
import com.isuo.yw2application.view.main.adduser.EmployeeActivity;
import com.isuo.yw2application.view.main.work.tool.return_tools.ReturnToolsActivity;
import com.isuo.yw2application.view.photo.ViewPagePhotoActivity;
import com.isuo.yw2application.widget.CheckListItemLayout;
import com.isuo.yw2application.widget.DatePick;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import pub.devrel.easypermissions.AppSettingsDialog;

/**
 * 外借
 * Created by zhangan on 2018/4/8.
 */

public class BorrowToolsActivity extends BaseActivity implements BorrowContract.View {
    //data
    private BorrowContract.Presenter mPresenter;
    private JSONObject jsonObject;
    private Calendar mCreateCalender;
    private final static int REQUEST_CODE_USER = 2;
    private ArrayList<EmployeeBean> chooseEmployeeBeen;
    //view
    private EditText editToolsUse;
    private LinearLayout llCheckList, llAllCheckList;
    private TextView tvToolsBorrowUser, tvBorrowToolsTime, tvToolsReturnTime;
    private List<CheckListBean> checkListBeans;
    private Button borrowBtn;
    private DatePick datePick;
    private ImageView ivToolsPhoto;
    private File photoFile;
    private ProgressBar progressBar;
    private String photoUrl = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutAndToolbar(R.layout.borrow_tools_activity, "外借");
        new BorrowPresenter(ToolsRepository.getRepository(this), this);
        mPresenter.subscribe();
        Tools tools = getIntent().getParcelableExtra(ConstantStr.KEY_BUNDLE_OBJECT);
        if (tools == null) {
            finish();
            return;
        }
        jsonObject = new JSONObject();
        mCreateCalender = Calendar.getInstance(Locale.CHINA);
        chooseEmployeeBeen = new ArrayList<>();
        try {
            jsonObject.put("toolId", String.valueOf(tools.getToolId()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        TextView tvToolsName = findViewById(R.id.tvToolsName);
        tvToolsBorrowUser = findViewById(R.id.tvToolsBorrowUser);
        editToolsUse = findViewById(R.id.editToolsUse);
        tvBorrowToolsTime = findViewById(R.id.tvBorrowToolsTime);
        tvToolsReturnTime = findViewById(R.id.tvToolsReturnTime);
        llCheckList = findViewById(R.id.llCheckList);
        llAllCheckList = findViewById(R.id.llAllCheckList);
        datePick = findViewById(R.id.date_pick);
        tvToolsName.setText(tools.getToolName());
        findViewById(R.id.llBorrowUser).setOnClickListener(this);
        findViewById(R.id.llBorrowTime).setOnClickListener(this);
        findViewById(R.id.llReturnTime).setOnClickListener(this);
        borrowBtn = findViewById(R.id.btnBorrow);
        borrowBtn.setOnClickListener(this);
        borrowBtn.setVisibility(View.GONE);
        ivToolsPhoto = findViewById(R.id.ivToolsPhoto);
        progressBar = findViewById(R.id.progressBar);
        ivToolsPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(BorrowToolsActivity.this.photoUrl)) {
                    ViewPagePhotoActivity.startActivity(BorrowToolsActivity.this, new String[]{BorrowToolsActivity.this.photoUrl}, 0);
                    return;
                }
                takePhoto();
            }
        });
        ivToolsPhoto.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new MaterialDialog.Builder(BorrowToolsActivity.this)
                        .items(R.array.choose_condition_2)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                                switch (position) {
                                    case 0://删除照片
                                        photoFile = null;
                                        BorrowToolsActivity.this.photoUrl = null;
                                        ivToolsPhoto.setImageDrawable(findDrawById(R.drawable.photograph));
                                        break;
                                    default://重新拍照
                                        photoFile = null;
                                        BorrowToolsActivity.this.photoUrl = null;
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
        mPresenter.getCheckList(tools.getToolId());
        mPresenter.getToolsState(tools.getToolId());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llBorrowUser:
                Intent intentUser = new Intent(this, EmployeeActivity.class);
                intentUser.putParcelableArrayListExtra(ConstantStr.KEY_BUNDLE_LIST, chooseEmployeeBeen);
                intentUser.putExtra(ConstantStr.KEY_BUNDLE_BOOLEAN_1, true);
                startActivityForResult(intentUser, REQUEST_CODE_USER);
                break;
            case R.id.llBorrowTime:
                datePick.show(mCreateCalender, new DatePick.IChooseTimeListener() {
                    @Override
                    public void onDataChange(Calendar calendar) {
                        mCreateCalender = calendar;
                        String time = getDataStr(mCreateCalender);
                        tvBorrowToolsTime.setText(time);
                        try {
                            jsonObject.put("useTime", time);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                break;
            case R.id.llReturnTime:
                datePick.show(mCreateCalender, new DatePick.IChooseTimeListener() {
                    @Override
                    public void onDataChange(Calendar calendar) {
                        mCreateCalender = calendar;
                        String time = getDataStr(mCreateCalender);
                        try {
                            jsonObject.put("preReturnTime", time);
                            tvToolsReturnTime.setText(time);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                break;
            case R.id.btnBorrow:
                if (this.progressBar.getVisibility() == View.VISIBLE) {
                    Toast.makeText(this, "图片上传中...请稍等", Toast.LENGTH_SHORT).show();
                    return;
                }
                String useStr = editToolsUse.getText().toString();
                if (TextUtils.isEmpty(useStr)) {
                    Yw2Application.getInstance().showToast("请输入用途");
                    return;
                } else {
                    try {
                        jsonObject.put("use", useStr);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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
                if (!TextUtils.isEmpty(this.photoUrl)) {
                    try {
                        jsonObject.put("picUrl", this.photoUrl);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (jsonObject.has("picUrl")) {
                    jsonObject.remove("picUrl");
                }
                mPresenter.borrowTools(jsonObject);
                break;
        }
    }

    @Override
    public void setPresenter(BorrowContract.Presenter presenter) {
        mPresenter = presenter;
    }

    private String getDataStr(Calendar calendar) {
        return DataUtil.timeFormat(calendar.getTimeInMillis(), "yyyy-MM-dd HH:mm");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_USER && resultCode == RESULT_OK) {
            ArrayList<EmployeeBean> employeeBeen = data.getParcelableArrayListExtra(ConstantStr.KEY_BUNDLE_LIST);
            chooseEmployeeBeen.clear();
            if (employeeBeen != null && employeeBeen.size() > 0) {
                chooseEmployeeBeen.addAll(employeeBeen);
            }
            if (chooseEmployeeBeen.size() == 1) {
                try {
                    jsonObject.put("useUser", String.valueOf(chooseEmployeeBeen.get(0).getUser().getUserId()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                tvToolsBorrowUser.setText(chooseEmployeeBeen.get(0).getUser().getRealName());
            }
        } else if (requestCode == ACTION_START_CAMERA && resultCode == RESULT_OK) {
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

    @Override
    public void borrowSuccess() {
        Intent intent = new Intent();
        intent.putExtra(ConstantStr.KEY_BUNDLE_STR, chooseEmployeeBeen.get(0).getUser().getRealName());
        setResult(Activity.RESULT_OK, intent);
        finish();
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
    public void toolsCanBorrow() {
        borrowBtn.setVisibility(View.VISIBLE);
    }

    private static final int ACTION_START_CAMERA = 200;
    private static final int ACTION_START_PHOTO = 203;

    private void takePhoto() {
        Permissions permissions = Permissions.build(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA);
        SoulPermission.getInstance().checkAndRequestPermissions(permissions,
                new CheckRequestPermissionsListener() {
                    @Override
                    public void onAllPermissionOk(Permission[] allPermissions) {
                        new MaterialDialog.Builder(BorrowToolsActivity.this)
                                .items(R.array.choose_photo)
                                .itemsCallback(new MaterialDialog.ListCallback() {
                                    @Override
                                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                                        if (position == 0) {
                                            photoFile = new File(Yw2Application.getInstance().imageCacheFile(), System.currentTimeMillis() + ".jpg");
                                            ActivityUtils.startCameraToPhoto(BorrowToolsActivity.this, photoFile, ACTION_START_CAMERA);
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
                        new AppSettingsDialog.Builder(BorrowToolsActivity.this)
                                .setRationale(getString(R.string.need_save_setting))
                                .setTitle(getString(R.string.request_permissions))
                                .setPositiveButton(getString(R.string.sure))
                                .setNegativeButton(getString(R.string.cancel))
                                .build()
                                .show();
                    }
                });
    }

    private void uploadImage(File file) {
        photoFile = file;
        progressBar.setVisibility(View.VISIBLE);
        mPresenter.uploadImage(file);
    }

    @Override
    public void toolsCantBorrow() {
        borrowBtn.setVisibility(View.VISIBLE);
        borrowBtn.setText("工具已经借出");
        borrowBtn.setTextColor(findColorById(R.color.color_bg_nav_normal));
        borrowBtn.setBackgroundColor(findColorById(R.color.news_time_gray));
    }

    @Override
    public void uploadImageSuccess(String url) {
        progressBar.setVisibility(View.GONE);
        this.photoUrl = url;
        GlideUtils.ShowImage(BorrowToolsActivity.this, this.photoUrl, ivToolsPhoto, R.drawable.picture_default);
    }

    @Override
    public void uploadImageFail() {
        progressBar.setVisibility(View.GONE);
        photoFile = null;
        ivToolsPhoto.setImageDrawable(findDrawById(R.drawable.photograph));
    }
}
