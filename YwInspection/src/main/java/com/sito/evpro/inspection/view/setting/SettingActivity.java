package com.sito.evpro.inspection.view.setting;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.sito.evpro.inspection.BuildConfig;
import com.sito.evpro.inspection.R;
import com.sito.evpro.inspection.app.InspectionApp;
import com.sito.evpro.inspection.common.ConstantStr;
import com.sito.evpro.inspection.mode.bean.NewVersion;
import com.sito.evpro.inspection.mode.bean.db.EquipmentDataDb;
import com.sito.evpro.inspection.mode.bean.db.EquipmentDb;
import com.sito.evpro.inspection.mode.bean.db.Image;
import com.sito.evpro.inspection.mode.bean.db.RoomDb;
import com.sito.evpro.inspection.mode.bean.db.TaskDb;
import com.sito.evpro.inspection.mode.bean.db.UserInfo;
import com.sito.evpro.inspection.mode.bean.db.Voice;
import com.sito.evpro.inspection.utils.PhotoUtils;
import com.sito.evpro.inspection.utils.UpdateManager;
import com.sito.evpro.inspection.view.BaseActivity;
import com.sito.evpro.inspection.view.setting.feedback.QuestionActivity;
import com.sito.evpro.inspection.view.setting.forgepassword.ForgePassWordActivity;
import com.sito.evpro.inspection.view.share.ShareActivity;
import com.sito.evpro.inspection.view.speech.voice.MediaPlayerManager;
import com.sito.library.utils.GlideUtils;
import com.sito.library.utils.SPHelper;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class SettingActivity extends BaseActivity implements View.OnClickListener, SettingContract.View {
    private NewVersion mVersion;
    private ImageView mUserPhoto;

    @Inject
    SettingPresenter mSettingPresenter;
    SettingContract.Presenter mPresenter;

    private File photoFile;
    private static final int ACTION_START_CAMERA = 100;
    private static final int ACTION_START_PHOTO = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutAndToolbar(R.layout.activity_setting, "设置");
        DaggerSettingComponent.builder().inspectionRepositoryComponent(InspectionApp.getInstance().getRepositoryComponent())
                .settingModule(new SettingModule(this)).build()
                .inject(this);
        initView();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slideinleft, R.anim.slideoutright);
    }

    private void initView() {
        TextView mUser = (TextView) findViewById(R.id.id_setting_user);
        TextView mUserPosition = (TextView) findViewById(R.id.id_setting_user_position);
        LinearLayout itemLLs = (LinearLayout) findViewById(R.id.ll_items);
        for (int i = 0; i < itemLLs.getChildCount(); i++) {
            itemLLs.getChildAt(i).setOnClickListener(this);
        }
        mUserPhoto = (ImageView) findViewById(R.id.iv_user_photo);
        mUserPhoto.setOnClickListener(this);
        if (InspectionApp.getInstance().getCurrentUser() != null) {
            mUser.setText(InspectionApp.getInstance().getCurrentUser().getRealName());
            if (InspectionApp.getInstance().getCurrentUser().getCustomer() == null) {
                mUserPosition.setText(InspectionApp.getInstance().getCurrentUser().getUserRoleNames());
            } else {
                mUserPosition.setText(InspectionApp.getInstance().getCurrentUser().getCustomer().getCustomerName() + "-"
                        + InspectionApp.getInstance().getCurrentUser().getUserRoleNames());
            }
            GlideUtils.ShowCircleImage(this, InspectionApp.getInstance().getCurrentUser().getPortraitUrl()
                    , mUserPhoto, R.drawable.mine_head_default);
        }
        TextView version_name = (TextView) findViewById(R.id.version_name);
        version_name.setText("V" + BuildConfig.VERSION_NAME);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_setting_about:
                startActivity(new Intent(this, AboutActivity.class));
                break;
            case R.id.ll_setting_clear:
                showProgressDialog("清理中...");
                Observable.just(1)
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io())
                        .doOnNext(new Action1<Integer>() {
                            @Override
                            public void call(Integer integer) {
                                SPHelper.clean(getApplicationContext(), ConstantStr.USER_DATA);
                                SPHelper.clean(getApplicationContext(), ConstantStr.SECURIT_INFO);
                                GlideUtils.cleanGlideDishCache(getApplicationContext());
                                File voiceFile = new File(InspectionApp.getInstance().voiceCacheFile());
                                File result = new File(getApplicationContext().getExternalCacheDir(), "luban_disk_cache");
                                deleteDirWithFile(voiceFile);//语音文件
                                deleteDirWithFile(result);//图片文件
                                List<Voice> voices = InspectionApp.getInstance().getDaoSession().getVoiceDao()
                                        .queryBuilder()
                                        .list();
                                InspectionApp.getInstance().getDaoSession().getVoiceDao()
                                        .deleteInTx(voices);
                                List<Image> images = InspectionApp.getInstance().getDaoSession().getImageDao()
                                        .queryBuilder()
                                        .list();
                                InspectionApp.getInstance().getDaoSession().getImageDao()
                                        .deleteInTx(images);
                                List<EquipmentDb> equipmentDbs = InspectionApp.getInstance().getDaoSession().getEquipmentDbDao()
                                        .queryBuilder()
                                        .list();
                                InspectionApp.getInstance().getDaoSession().getEquipmentDbDao()
                                        .deleteInTx(equipmentDbs);
                                List<EquipmentDataDb> equipmentDataDbs = InspectionApp.getInstance().getDaoSession().getEquipmentDataDbDao()
                                        .queryBuilder()
                                        .list();
                                InspectionApp.getInstance().getDaoSession().getEquipmentDataDbDao()
                                        .deleteInTx(equipmentDataDbs);
                                List<TaskDb> taskDbs = InspectionApp.getInstance().getDaoSession().getTaskDbDao()
                                        .queryBuilder()
                                        .list();
                                InspectionApp.getInstance().getDaoSession().getTaskDbDao()
                                        .deleteInTx(taskDbs);
                                List<UserInfo> userInfos = InspectionApp.getInstance().getDaoSession().getUserInfoDao()
                                        .queryBuilder()
                                        .list();
                                InspectionApp.getInstance().getDaoSession().getUserInfoDao()
                                        .deleteInTx(userInfos);
                                List<RoomDb> roomDbs = InspectionApp.getInstance().getDaoSession().getRoomDbDao()
                                        .queryBuilder()
                                        .list();
                                InspectionApp.getInstance().getDaoSession().getRoomDbDao()
                                        .deleteInTx(roomDbs);
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<Integer>() {
                            @Override
                            public void call(Integer integer) {
                                hideProgressDialog();
                                InspectionApp.getInstance().showToast("清除缓存成功");
                            }
                        });
                break;
            case R.id.ll_setting_sugback:
                startActivity(new Intent(this, QuestionActivity.class));
                break;
            case R.id.ll_setting_version:
                mPresenter.getNewVersion();
                break;
            case R.id.iv_user_photo:
                new MaterialDialog.Builder(this)
                        .items(R.array.choose_photo)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                                if (position == 0) {
                                    photoFile = new File(InspectionApp.getInstance().imageCacheFile(), System.currentTimeMillis() + ".jpg");
                                    Intent intent = new Intent();
                                    intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                                    ContentValues contentValues = new ContentValues(1);
                                    contentValues.put(MediaStore.Images.Media.DATA, photoFile.getAbsolutePath());
                                    Uri uri = getApplicationContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                                    startActivityForResult(intent, ACTION_START_CAMERA);
                                } else {
                                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                                    intent.setType("image/*");
                                    startActivityForResult(intent, ACTION_START_PHOTO);
                                }
                            }
                        })
                        .show();
                break;
            case R.id.ll_update_password:
                startActivity(new Intent(this, ForgePassWordActivity.class));
                break;
            case R.id.ll_setting_share:
                startActivity(new Intent(this, ShareActivity.class));
                break;
        }
    }

    @Override
    public void newVersionDialog(@NonNull NewVersion version) {
        mVersion = version;
        new MaterialDialog.Builder(this)
                .content(version.getNote())
                .negativeText("取消")
                .positiveText("确定")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        downLoadApp();
                    }
                }).show();
    }

    @Override
    public void currentVersion() {
        InspectionApp.getInstance().showToast("当前为最新版本");
    }

    @Override
    public void downLoadApp() {
        new UpdateManager(this, mVersion.getUrl(), null).updateApp();
    }

    @Override
    public void uploadUserPhotoSuccess() {
        GlideUtils.ShowCircleImage(this, userPhoto.getAbsolutePath(), mUserPhoto, R.drawable.mine_head_default);
    }

    @Override
    public void uploadUserPhotoFail() {
        InspectionApp.getInstance().showToast("图片上传失败");
    }

    @Override
    public void setPresenter(SettingContract.Presenter presenter) {
        mPresenter = presenter;
    }

    private File userPhoto;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTION_START_CAMERA && resultCode == RESULT_OK) {
            Uri photoIn = Uri.fromFile(photoFile);
            beginCrop(photoIn);
        }
        if (requestCode == ACTION_START_PHOTO && resultCode == RESULT_OK) {
            beginCrop(data.getData());
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, data);
        }
    }

    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(this.getCacheDir(), "user.jpg"));
        Crop.of(source, destination).asSquare().start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            Uri uri = Crop.getOutput(result);
            PhotoUtils.cropPhoto(this, new File(uri.getEncodedPath()), new PhotoUtils.PhotoListener() {
                @Override
                public void onSuccess(File file) {
                    if (mPresenter != null) {
                        userPhoto = file;
                        mPresenter.uploadUserPhoto(file);
                    }
                }
            });
        } else if (resultCode == Crop.RESULT_ERROR) {
            InspectionApp.getInstance().showToast(Crop.getError(result).getMessage());
        }
    }

    private static void deleteDirWithFile(File dir) {
        if (dir == null || !dir.exists() || !dir.isDirectory())
            return;
        for (File file : dir.listFiles()) {
            if (file.isFile())
                file.delete(); // 删除所有文件
            else if (file.isDirectory())
                deleteDirWithFile(file); // 递规的方式删除文件夹
        }
        dir.delete();// 删除目录本身
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mPresenter != null) {
            mPresenter.unSubscribe();
        }
    }
}
