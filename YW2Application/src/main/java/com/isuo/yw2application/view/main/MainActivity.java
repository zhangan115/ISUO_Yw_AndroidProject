package com.isuo.yw2application.view.main;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.isuo.yw2application.R;
import com.isuo.yw2application.app.Yw2Application;
import com.isuo.yw2application.common.BroadcastAction;
import com.isuo.yw2application.common.ConstantStr;
import com.isuo.yw2application.mode.bean.NewVersion;
import com.isuo.yw2application.mode.bean.User;
import com.isuo.yw2application.mode.bean.count.PartPersonStatistics;
import com.isuo.yw2application.mode.bean.db.CreateEquipmentDb;
import com.isuo.yw2application.mode.bean.db.CreateRoomDb;
import com.isuo.yw2application.mode.bean.db.EquipmentDataDb;
import com.isuo.yw2application.mode.bean.db.EquipmentDb;
import com.isuo.yw2application.mode.bean.db.Image;
import com.isuo.yw2application.mode.bean.db.NewsBean;
import com.isuo.yw2application.mode.bean.db.RoomDb;
import com.isuo.yw2application.mode.bean.db.ShareDataDb;
import com.isuo.yw2application.mode.bean.db.TaskDb;
import com.isuo.yw2application.mode.bean.db.UserInfo;
import com.isuo.yw2application.mode.bean.db.Voice;
import com.isuo.yw2application.utils.PhotoUtils;
import com.isuo.yw2application.utils.UpdateManager;
import com.isuo.yw2application.view.base.BaseActivity;
import com.isuo.yw2application.view.contact.ContactActivity;
import com.isuo.yw2application.view.main.about.AboutActivity;
import com.isuo.yw2application.view.main.alarm.AlarmFragment;
import com.isuo.yw2application.view.main.data.DataFragment;
import com.isuo.yw2application.view.main.device.DeviceFragment;
import com.isuo.yw2application.view.main.feedback.QuestionActivity;
import com.isuo.yw2application.view.main.forgepassword.ForgePassWordActivity;
import com.isuo.yw2application.view.main.task.TaskFragment;
import com.isuo.yw2application.view.main.work.WorkFragment;
import com.isuo.yw2application.view.share.ShareActivity;
import com.sito.library.utils.GlideUtils;
import com.sito.library.utils.SPHelper;
import com.sito.library.utils.SystemUtil;
import com.soundcloud.android.crop.Crop;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends BaseActivity implements WorkFragment.DrawClickCallBack, MainContract.View, EasyPermissions.PermissionCallbacks {

    private ArrayList<Fragment> mFragments;
    private AHBottomNavigation bottomNavigation;
    private int selectPosition = 0;
    private long mCurrentTime = 0;
    private DrawerLayout drawer;
    private MainContract.Presenter mainPresenter;
    private NewVersion mVersion;
    private ImageView mUserPhoto;
    private File userPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawer = findViewById(R.id.drawer_layout);
        mFragments = getFragments();
        bottomNavigation = findViewById(R.id.bottom_navigation);
        AHBottomNavigationItem item1 = new AHBottomNavigationItem(R.string.str_first_nav_1, R.drawable.work, R.color.colorPrimary);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(R.string.str_first_nav_2, R.drawable.drive, R.color.colorPrimary);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(R.string.str_first_nav_3, R.drawable.task_g, R.color.colorPrimary);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem(R.string.str_first_nav_4, R.drawable.fault_bottom, R.color.colorPrimary);
        AHBottomNavigationItem item5 = new AHBottomNavigationItem(R.string.str_first_nav_5, R.drawable.board, R.color.colorPrimary);
        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);
        bottomNavigation.addItem(item4);
        bottomNavigation.addItem(item5);
        bottomNavigation.setTitleTextSizeInSp(12f, 12f);
        bottomNavigation.setBackgroundColor(findColorById(R.color.colorWhite));
        bottomNavigation.setDefaultBackgroundColor(findColorById(R.color.colorWhite));
        bottomNavigation.setForceTint(false);
        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        bottomNavigation.setAccentColor(findColorById(R.color.colorPrimary));
        bottomNavigation.setInactiveColor(findColorById(R.color.color_bg_nav_normal));
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        if (savedInstanceState != null) {
            selectPosition = savedInstanceState.getInt(ConstantStr.KEY_BUNDLE_INT);
            if (mFragments.get(selectPosition).isAdded()) {
                transaction.show(mFragments.get(selectPosition));
            } else {
                transaction.add(R.id.frame_container, mFragments.get(selectPosition), "tag_" + selectPosition);
            }
        } else {
            transaction.add(R.id.frame_container, mFragments.get(selectPosition), "tag_" + selectPosition);
        }
        bottomNavigation.setCurrentItem(selectPosition);
        transaction.commit();
        initView();
        new MainPresenter(Yw2Application.getInstance().getRepositoryComponent().getRepository(), this);
    }

    private void initView() {
        findViewById(R.id.layout_1).setOnClickListener(this);
        findViewById(R.id.layout_2).setOnClickListener(this);
        findViewById(R.id.layout_3).setOnClickListener(this);
        findViewById(R.id.layout_4).setOnClickListener(this);
        findViewById(R.id.layout_5).setOnClickListener(this);
        findViewById(R.id.layout_6).setOnClickListener(this);
        findViewById(R.id.layout_7).setOnClickListener(this);
        findViewById(R.id.exitApp).setOnClickListener(this);
        User user = Yw2Application.getInstance().getCurrentUser();
        mUserPhoto = findViewById(R.id.userImage);
        mUserPhoto.setOnClickListener(this);
        GlideUtils.ShowCircleImage(this, user.getPortraitUrl(), mUserPhoto, R.drawable.mine_head_default);
        TextView userNameTv = findViewById(R.id.textUserName);
        userNameTv.setText(user.getRealName());
        TextView userInfoTv = findViewById(R.id.textUserInfo);
        if (user.getCustomer() != null) {
            userInfoTv.setText(String.format("%s-%s", user.getCustomer().getCustomerName(), user.getUserRoleNames()));
        } else {
            userInfoTv.setText(user.getUserRoleNames());
        }
        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {

            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                if (selectPosition != position) {
                    commitFragment(position);
                    return true;
                }
                return false;
            }
        });
    }

    private static final int REQUEST_PERMISSION = 0;

    public ArrayList<Fragment> getFragments() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        WorkFragment workFragment = (WorkFragment) getSupportFragmentManager().findFragmentByTag("tag_0");
        if (workFragment == null) {
            workFragment = WorkFragment.newInstance();
        }
        DeviceFragment deviceFragment = (DeviceFragment) getSupportFragmentManager().findFragmentByTag("tag_1");
        if (deviceFragment == null) {
            deviceFragment = DeviceFragment.newInstance();
        }
        TaskFragment taskFragment = (TaskFragment) getSupportFragmentManager().findFragmentByTag("tag_2");
        if (taskFragment == null) {
            taskFragment = TaskFragment.newInstance();
        }
        AlarmFragment alarmFragment = (AlarmFragment) getSupportFragmentManager().findFragmentByTag("tag_3");
        if (alarmFragment == null) {
            alarmFragment = AlarmFragment.newInstance();
        }
        DataFragment dataFragment = (DataFragment) getSupportFragmentManager().findFragmentByTag("tag_4");
        if (dataFragment == null) {
            dataFragment = DataFragment.newInstance();
        }
        fragments.add(workFragment);
        fragments.add(deviceFragment);
        fragments.add(taskFragment);
        fragments.add(alarmFragment);
        fragments.add(dataFragment);
        return fragments;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_1:
                startActivity(new Intent(this, ContactActivity.class));
                break;
            case R.id.layout_2:
                this.cleanCache();
                break;
            case R.id.layout_3:
                startActivity(new Intent(this, ShareActivity.class));
                break;
            case R.id.layout_4:
                startActivity(new Intent(this, QuestionActivity.class));
                break;
            case R.id.layout_5:
                if (mainPresenter != null) {
                    mainPresenter.getNewVersion();
                }
                break;
            case R.id.layout_6:
                startActivity(new Intent(this, AboutActivity.class));
                break;
            case R.id.layout_7:
                startActivity(new Intent(this, ForgePassWordActivity.class));
                break;
            case R.id.exitApp:
                MobclickAgent.onProfileSignOff();
                Yw2Application.getInstance().needLogin();
                break;
            case R.id.userImage:
                new MaterialDialog.Builder(this)
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
        }
    }

    private void cleanCache() {
        showProgressDialog("清理中...");
        SPHelper.write(this, ConstantStr.USER_INFO, Yw2Application.getInstance().getCurrentUser().getUserId() + ConstantStr.NEWS_ALARM_STATE, 0);
        SPHelper.write(this, ConstantStr.USER_INFO, Yw2Application.getInstance().getCurrentUser().getUserId() + ConstantStr.NEWS_WORK_STATE, 0);
        SPHelper.write(this, ConstantStr.USER_INFO, Yw2Application.getInstance().getCurrentUser().getUserId() + ConstantStr.NEWS_NOTIFY_STATE, 0);
        SPHelper.write(this, ConstantStr.USER_INFO, Yw2Application.getInstance().getCurrentUser().getUserId() + ConstantStr.NEWS_ME_STATE, 0);
        SPHelper.clean(this, ConstantStr.INSPECTION_CACHE_DATA);
        Observable.just(1)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .doOnNext(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        GlideUtils.cleanGlideDishCache(MainActivity.this);
                        deleteDirWithFile(new File(Yw2Application.getInstance().voiceCacheFile()));
                        Yw2Application.getInstance().getDaoSession().deleteAll(RoomDb.class);
                        Yw2Application.getInstance().getDaoSession().deleteAll(NewsBean.class);
                        Yw2Application.getInstance().getDaoSession().deleteAll(Image.class);
                        Yw2Application.getInstance().getDaoSession().deleteAll(Voice.class);
                        Yw2Application.getInstance().getDaoSession().deleteAll(EquipmentDb.class);
                        Yw2Application.getInstance().getDaoSession().deleteAll(EquipmentDataDb.class);
                        Yw2Application.getInstance().getDaoSession().deleteAll(TaskDb.class);
                        Yw2Application.getInstance().getDaoSession().deleteAll(CreateEquipmentDb.class);
                        Yw2Application.getInstance().getDaoSession().deleteAll(CreateRoomDb.class);
                        Yw2Application.getInstance().getDaoSession().deleteAll(UserInfo.class);
                        Yw2Application.getInstance().getDaoSession().deleteAll(ShareDataDb.class);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        hideProgressDialog();
                        Yw2Application.getInstance().showToast("清除缓存成功");
                        //更新ui
                        Intent intent = new Intent();
                        intent.setAction(BroadcastAction.CLEAN_ALL_DATA);
                        sendBroadcast(intent);
                    }
                });
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

    private void commitFragment(int position) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (mFragments.get(selectPosition).isAdded()) {
            ft.hide(mFragments.get(selectPosition));
        }
        if (mFragments.get(position).isAdded()) {
            ft.show(mFragments.get(position));
        } else {
            ft.add(R.id.frame_container, mFragments.get(position), "tag_" + position);
        }
        selectPosition = position;
        ft.commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (outState != null) {
            outState.putInt(ConstantStr.KEY_BUNDLE_INT, selectPosition);
        }
    }

    @Override
    public void onBackPressed() {
        if (this.drawer.isDrawerOpen(Gravity.START)) {
            this.drawer.closeDrawer(Gravity.START);
            return;
        }
        long time = System.currentTimeMillis();
        if (time - this.mCurrentTime < 2000) {
            Yw2Application.getInstance().exitApp();
        } else {
            this.mCurrentTime = time;
            Yw2Application.getInstance().showToast(getString(R.string.toast_exit_app));
        }
    }

    @Override
    public void onCallBack() {
        drawer.openDrawer(Gravity.START);
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
        Yw2Application.getInstance().showToast("当前为最新版本");
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
    public void uploadUserPhotoSuccess(String url) {
        GlideUtils.ShowCircleImage(this, url, mUserPhoto, R.drawable.mine_head_default);
        Intent userPhotoUpdate = new Intent(BroadcastAction.USER_PHOTO_UPDATE);
        sendBroadcast(userPhotoUpdate);
    }

    @Override
    public void uploadUserPhotoFail() {
        Yw2Application.getInstance().showToast("图片上传失败");
    }
    private File photoFile;
    public static final int REQUEST_EXTERNAL = 10;
    public static final int REQUEST_EXTERNAL_PHOTO = 11;

    private static final int ACTION_START_CAMERA = 100;
    private static final int ACTION_START_PHOTO = 101;

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
        if (EasyPermissions.hasPermissions(this.getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            if (ConstantStr.ALPS.equals(SystemUtil.getDeviceBrand())) {
                new UpdateManager(this, mVersion.getSpecialUrl(), null).updateApp();
            } else {
                new UpdateManager(this, mVersion.getUrl(), null).updateApp();
            }
        } else {
            EasyPermissions.requestPermissions(this, "需要请求内存卡权限",
                    REQUEST_EXTERNAL, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }

    @AfterPermissionGranted(REQUEST_EXTERNAL_PHOTO)
    public void checkPermissionPhoto() {
        if (EasyPermissions.hasPermissions(this.getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            photoFile = new File(Yw2Application.getInstance().imageCacheFile(), System.currentTimeMillis() + ".jpg");
            Intent intent = new Intent();
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            ContentValues contentValues = new ContentValues(1);
            contentValues.put(MediaStore.Images.Media.DATA, photoFile.getAbsolutePath());
            Uri uri = this.getApplicationContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
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
        Uri destination = Uri.fromFile(new File(this.getCacheDir(), "user.jpg"));
        Crop.of(source, destination).asSquare().start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            Uri uri = Crop.getOutput(result);
            PhotoUtils.cropPhoto(this, new File(uri.getEncodedPath()), new PhotoUtils.PhotoListener() {
                @Override
                public void onSuccess(File file) {
                    if (mainPresenter != null) {
                        userPhoto = file;
                        mainPresenter.uploadUserPhoto(file);
                    }
                }
            });
        } else if (resultCode == Crop.RESULT_ERROR) {
            Yw2Application.getInstance().showToast(Crop.getError(result).getMessage());
        }
    }

    @Override
    public void setPresenter(MainContract.Presenter presenter) {
        this.mainPresenter = presenter;
    }
}
