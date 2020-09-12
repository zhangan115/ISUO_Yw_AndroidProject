package com.sito.customer.view.home.mine;


import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.sito.customer.BuildConfig;
import com.sito.customer.R;
import com.sito.customer.app.CustomerApp;
import com.sito.customer.common.BroadcastAction;
import com.sito.customer.common.ConstantStr;
import com.sito.customer.mode.bean.NewVersion;
import com.sito.customer.mode.bean.db.CreateEquipmentDb;
import com.sito.customer.mode.bean.db.CreateRoomDb;
import com.sito.customer.mode.bean.db.EquipmentDataDb;
import com.sito.customer.mode.bean.db.EquipmentDb;
import com.sito.customer.mode.bean.db.Image;
import com.sito.customer.mode.bean.db.RoomDb;
import com.sito.customer.mode.bean.db.ShareDataDb;
import com.sito.customer.mode.bean.db.TaskDb;
import com.sito.customer.mode.bean.db.UserInfo;
import com.sito.customer.mode.bean.db.Voice;
import com.sito.customer.mode.bean.db.NewsBean;
import com.sito.customer.utils.PhotoUtils;
import com.sito.customer.utils.UpdateManager;
import com.sito.customer.view.MvpFragmentV4;
import com.sito.customer.view.contact.ContactActivity;
import com.sito.customer.view.home.mine.feedback.QuestionActivity;
import com.sito.customer.view.home.mine.forgepassword.ForgePassWordActivity;
import com.sito.customer.view.share.ShareActivity;
import com.sito.library.utils.GlideUtils;
import com.sito.library.utils.SPHelper;
import com.sito.library.utils.SystemUtil;
import com.soundcloud.android.crop.Crop;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static android.app.Activity.RESULT_OK;

/**
 * 我的
 */
public class MineFragment extends MvpFragmentV4<MineContract.Presenter> implements MineContract.View, View.OnClickListener, EasyPermissions.PermissionCallbacks {
    private NewVersion mVersion;
    private ImageView mUserPhoto;
    private File userPhoto;

    public static MineFragment newInstance() {
        Bundle args = new Bundle();
        MineFragment fragment = new MineFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initData();
        View rootView = inflater.inflate(R.layout.fragment_mine, container, false);
        initView(rootView);
        return rootView;
    }

    private void initData() {
        new MinePresenter(CustomerApp.getInstance().getRepositoryComponent().getRepository(), this);
    }

    private void initView(View rootView) {
        TextView mUser = rootView.findViewById(R.id.id_setting_user);
        TextView mUserPosition = rootView.findViewById(R.id.id_setting_user_position);
        LinearLayout itemLLs = rootView.findViewById(R.id.ll_items);
        for (int i = 0; i < itemLLs.getChildCount(); i++) {
            itemLLs.getChildAt(i).setOnClickListener(this);
        }
        mUserPhoto = rootView.findViewById(R.id.iv_user_photo);
        mUserPhoto.setOnClickListener(this);
        if (CustomerApp.getInstance().getCurrentUser() != null) {
            mUser.setText(CustomerApp.getInstance().getCurrentUser().getRealName());
            if (CustomerApp.getInstance().getCurrentUser().getCustomer() == null) {
                mUserPosition.setText(CustomerApp.getInstance().getCurrentUser().getUserRoleNames());
            } else {
                String str = CustomerApp.getInstance().getCurrentUser().getCustomer().getCustomerName() + "-"
                        + CustomerApp.getInstance().getCurrentUser().getUserRoleNames();
                mUserPosition.setText(str);
            }
            GlideUtils.ShowCircleImage(getActivity(), CustomerApp.getInstance().getCurrentUser().getPortraitUrl(), mUserPhoto, R.drawable.mine_head_default);
        }
        TextView version_name = rootView.findViewById(R.id.version_name);
        version_name.setText("V" + BuildConfig.VERSION_NAME);
    }

    @Override
    public void setPresenter(MineContract.Presenter presenter) {
        mPresenter = presenter;
    }

    private File photoFile;
    private static final int ACTION_START_CAMERA = 100;
    private static final int ACTION_START_PHOTO = 101;

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.ll_setting_about:
                startActivity(new Intent(getContext(), AboutActivity.class));
                break;
            case R.id.ll_setting_clear:
                showProgressDialog("清理中...");
                SPHelper.write(getActivity(), ConstantStr.USER_INFO, CustomerApp.getInstance().getCurrentUser().getUserId() + ConstantStr.NEWS_ALARM_STATE, 0);
                SPHelper.write(getActivity(), ConstantStr.USER_INFO, CustomerApp.getInstance().getCurrentUser().getUserId() + ConstantStr.NEWS_WORK_STATE, 0);
                SPHelper.write(getActivity(), ConstantStr.USER_INFO, CustomerApp.getInstance().getCurrentUser().getUserId() + ConstantStr.NEWS_NOTIFY_STATE, 0);
                SPHelper.write(getActivity(), ConstantStr.USER_INFO, CustomerApp.getInstance().getCurrentUser().getUserId() + ConstantStr.NEWS_ME_STATE, 0);
                SPHelper.clean(getActivity(), ConstantStr.INSPECTION_CACHE_DATA);
                Observable.just(1)
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io())
                        .doOnNext(new Action1<Integer>() {
                            @Override
                            public void call(Integer integer) {
                                GlideUtils.cleanGlideDishCache(getActivity());
                                deleteDirWithFile(new File(CustomerApp.getInstance().voiceCacheFile()));
                                CustomerApp.getInstance().getDaoSession().deleteAll(RoomDb.class);
                                CustomerApp.getInstance().getDaoSession().deleteAll(NewsBean.class);
                                CustomerApp.getInstance().getDaoSession().deleteAll(Image.class);
                                CustomerApp.getInstance().getDaoSession().deleteAll(Voice.class);
                                CustomerApp.getInstance().getDaoSession().deleteAll(EquipmentDb.class);
                                CustomerApp.getInstance().getDaoSession().deleteAll(EquipmentDataDb.class);
                                CustomerApp.getInstance().getDaoSession().deleteAll(TaskDb.class);
                                CustomerApp.getInstance().getDaoSession().deleteAll(CreateEquipmentDb.class);
                                CustomerApp.getInstance().getDaoSession().deleteAll(CreateRoomDb.class);
                                CustomerApp.getInstance().getDaoSession().deleteAll(UserInfo.class);
                                CustomerApp.getInstance().getDaoSession().deleteAll(ShareDataDb.class);
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<Integer>() {
                            @Override
                            public void call(Integer integer) {
                                hideProgressDialog();
                                CustomerApp.getInstance().showToast("清除缓存成功");
                                //更新ui
                                Intent intent = new Intent();
                                intent.setAction(BroadcastAction.CLEAN_ALL_DATA);
                                getActivity().sendBroadcast(intent);
                            }
                        });
                break;
            case R.id.ll_setting_sugback:
                startActivity(new Intent(getActivity(), QuestionActivity.class));
                break;
            case R.id.ll_setting_version:
                if (mPresenter != null) {
                    mPresenter.getNewVersion();
                }
                break;
            case R.id.ll_setting_quit:
                if (mPresenter != null) {
                    mPresenter.exitApp();
                    MobclickAgent.onProfileSignOff();
                }
                CustomerApp.getInstance().needLogin();
                //退出登录
                break;
            case R.id.iv_user_photo:
                new MaterialDialog.Builder(getActivity())
                        .items(R.array.choose_photo)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                                if (position == 0) {
                                    checkPermissionPhoto();
                                } else {
                                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                                    intent.setType("image/*");
                                    startActivityForResult(intent, ACTION_START_PHOTO);
                                }
                            }
                        })
                        .show();
                break;
            case R.id.ll_change_password:
                startActivity(new Intent(getActivity(), ForgePassWordActivity.class));
                break;
            case R.id.ll_setting_share:
                startActivity(new Intent(getActivity(), ShareActivity.class));
                break;
            case R.id.ll_setting_users:
                startActivity(new Intent(getActivity(), ContactActivity.class));
                break;
        }
    }

    @Override
    public void newVersionDialog(@NonNull NewVersion version) {
        mVersion = version;
        new MaterialDialog.Builder(getActivity())
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
        getApp().showToast("当前为最新版本");
    }

    @Override
    public void downLoadApp() {
        checkPermission();
    }

    @Override
    public void showUploadPhotoLoading() {
        showProgressDialog("上传中...");
    }

    @Override
    public void hideUploadPhotoLoading() {
        hideProgressDialog();
    }

    @Override
    public void uploadUserPhotoSuccess() {
        GlideUtils.ShowCircleImage(getActivity(), userPhoto.getAbsolutePath(), mUserPhoto, R.drawable.mine_head_default);
    }

    @Override
    public void uploadUserPhotoFail() {
        getApp().showToast("图片上传失败");
    }

    public static final int REQUEST_EXTERNAL = 10;
    public static final int REQUEST_EXTERNAL_PHOTO = 11;

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (requestCode == REQUEST_EXTERNAL || requestCode == REQUEST_EXTERNAL_PHOTO) {
            if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
                new AppSettingsDialog.Builder(this)
                        .setRationale(getString(R.string.need_save_setting))
                        .setTitle(getString(R.string.request_permissions))
                        .setPositiveButton(getString(R.string.sure))
                        .setNegativeButton(getString(R.string.cancel))
                        .setRequestCode(REQUEST_EXTERNAL)
                        .build()
                        .show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(REQUEST_EXTERNAL)
    public void checkPermission() {
        if (EasyPermissions.hasPermissions(getActivity().getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            if (ConstantStr.ALPS.equals(SystemUtil.getDeviceBrand())) {
                new UpdateManager(getActivity(), mVersion.getSpecialUrl(), null).updateApp();
            } else {
                new UpdateManager(getActivity(), mVersion.getUrl(), null).updateApp();
            }
        } else {
            EasyPermissions.requestPermissions(this, "需要请求内存卡权限",
                    REQUEST_EXTERNAL, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }

    @AfterPermissionGranted(REQUEST_EXTERNAL_PHOTO)
    public void checkPermissionPhoto() {
        if (EasyPermissions.hasPermissions(getActivity().getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            photoFile = new File(CustomerApp.getInstance().imageCacheFile(), System.currentTimeMillis() + ".jpg");
            Intent intent = new Intent();
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            ContentValues contentValues = new ContentValues(1);
            contentValues.put(MediaStore.Images.Media.DATA, photoFile.getAbsolutePath());
            Uri uri = getActivity().getApplicationContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(intent, ACTION_START_CAMERA);
        } else {
            EasyPermissions.requestPermissions(this, "需要请求内存卡权限",
                    REQUEST_EXTERNAL_PHOTO, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }

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
        Uri destination = Uri.fromFile(new File(getActivity().getCacheDir(), "user.jpg"));
        Crop.of(source, destination).asSquare().start(getActivity(), this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            Uri uri = Crop.getOutput(result);
            PhotoUtils.cropPhoto(getActivity(), new File(uri.getEncodedPath()), new PhotoUtils.PhotoListener() {
                @Override
                public void onSuccess(File file) {
                    if (mPresenter != null) {
                        userPhoto = file;
                        mPresenter.uploadUserPhoto(file);
                    }
                }
            });
        } else if (resultCode == Crop.RESULT_ERROR) {
            getApp().showToast(Crop.getError(result).getMessage());
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
}
