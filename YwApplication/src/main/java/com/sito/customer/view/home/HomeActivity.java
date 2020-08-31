package com.sito.customer.view.home;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.igexin.sdk.PushManager;
import com.sito.customer.R;
import com.sito.customer.app.CustomerApp;
import com.sito.customer.common.BroadcastAction;
import com.sito.customer.common.ConstantStr;
import com.sito.customer.mode.bean.NewVersion;
import com.sito.customer.push.CustPushService;
import com.sito.customer.utils.UpdateManager;
import com.sito.customer.view.BaseActivity;
import com.sito.customer.view.home.alarm.AlarmFragment;
import com.sito.customer.view.home.discover.DiscoverFragment;
import com.sito.customer.view.home.mine.MineFragment;
import com.sito.customer.view.home.news.NewsFragmentN;
import com.sito.customer.view.home.work.WorkFragment;
import com.sito.customer.view.login.LoginActivity;
import com.sito.customer.view.splash.SplashActivity;
import com.sito.library.utils.SystemUtil;

import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class HomeActivity extends BaseActivity implements HomeContract.View, EasyPermissions.PermissionCallbacks
        , UpdateManager.DownLoadCancelListener {
    public static final String HOME_ACTION_OPEN_NEWS = "action.open.news_HOME";
    private NewVersion mVersion;
    private HomeContract.Presenter mPresenter;
    private ArrayList<Fragment> mFragments;
    private AHBottomNavigation bottomNavigation;
    private UpdateUIBroadcastReceiver broadcastReceiver;
    private int selectPosition = 0;
    private long mCurrentTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        transparentStatusBar();
        setContentView(R.layout.activity_home);
        // 动态注册广播
        IntentFilter filter = new IntentFilter();
        filter.addAction(BroadcastAction.CLEAN_ALL_DATA);
        broadcastReceiver = new UpdateUIBroadcastReceiver();
        registerReceiver(broadcastReceiver, filter);
        mFragments = getFragments();
        new HomePresenter(CustomerApp.getInstance().getRepositoryComponent().getRepository(), this);
        if (!TextUtils.isEmpty(CustomerApp.getInstance().getCid())) {
            mPresenter.postCidInfo(CustomerApp.getInstance().getCid());
        }
        cleanAllNotify();
        bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);
        AHBottomNavigationItem item1 = new AHBottomNavigationItem(R.string.str_first_nav_2, R.drawable.nav_work_icon_normal, R.color.colorPrimary);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(R.string.str_first_nav_1, R.drawable.nav_message_icon_normal, R.color.colorPrimary);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(R.string.str_first_nav_3, R.drawable.nav_call_icon_normal, R.color.colorPrimary);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem(R.string.str_first_nav_4, R.drawable.nav_found_icon_normal, R.color.colorPrimary);
        AHBottomNavigationItem item5 = new AHBottomNavigationItem(R.string.str_first_nav_5, R.drawable.nav_mine_icon_normal, R.color.colorPrimary);
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
        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {

            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                CustomerApp.isNewsMeOpen = position == 1;
                if (position == 1) {
                    cleanAllNotify();
                }
                if (selectPosition != position) {
                    commitFragment(position);
                    return true;
                }
                return false;
            }
        });
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
        mPresenter.getUnReadCount();
        checkPermission();
    }

    private void commitFragment(int position) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.hide(mFragments.get(selectPosition));
        if (mFragments.get(position).isAdded()) {
            ft.show(mFragments.get(position));
        } else {
            ft.add(R.id.frame_container, mFragments.get(position), "tag_" + position);
        }
        selectPosition = position;
        showUnReadCount(unReadNewsCount);
        ft.commit();
    }

    private static final int REQUEST_PERMISSION = 0;

    public ArrayList<Fragment> getFragments() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        WorkFragment workFragment = (WorkFragment) getSupportFragmentManager().findFragmentByTag("tag_0");
        if (workFragment == null) {
            workFragment = WorkFragment.newInstance();
        }
        NewsFragmentN newsFragment = (NewsFragmentN) getSupportFragmentManager().findFragmentByTag("tag_1");
        if (newsFragment == null) {
            newsFragment = NewsFragmentN.newInstance();
        }
        AlarmFragment alarmFragment = (AlarmFragment) getSupportFragmentManager().findFragmentByTag("tag_2");
        if (alarmFragment == null) {
            alarmFragment = AlarmFragment.newInstance();
        }
        DiscoverFragment discoverFragment = (DiscoverFragment) getSupportFragmentManager().findFragmentByTag("tag_3");
        if (discoverFragment == null) {
            discoverFragment = DiscoverFragment.newInstance();
        }
        MineFragment mineFragment = (MineFragment) getSupportFragmentManager().findFragmentByTag("tag_4");
        if (mineFragment == null) {
            mineFragment = MineFragment.newInstance();
        }
        fragments.add(workFragment);
        fragments.add(newsFragment);
        fragments.add(alarmFragment);
        fragments.add(discoverFragment);
        fragments.add(mineFragment);
        return fragments;
    }

    @Override
    protected void onResume() {
        super.onResume();
        CustomerApp.isNewsMeOpen = selectPosition == 1;
    }

    @Override
    protected void onPause() {
        super.onPause();
        CustomerApp.isNewsMeOpen = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
        if (mPresenter != null) {
            mPresenter.unSubscribe();
        }
    }

    @Override
    public void onBackPressed() {
        long time = System.currentTimeMillis();
        if (time - this.mCurrentTime < 3000) {
            if (!ConstantStr.ALPS.equals(SystemUtil.getDeviceBrand())) {
                CustomerApp.getInstance().exitApp();
            } else {
                finish();
                startActivity(new Intent(this, LoginActivity.class));
            }
        } else {
            this.mCurrentTime = time;
            CustomerApp.getInstance().showToast(getString(R.string.toast_exit_app));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (outState != null) {
            outState.putInt(ConstantStr.KEY_BUNDLE_INT, selectPosition);
        }
    }

    @Override
    public void showNewVersion(NewVersion version) {
        mVersion = version;
        new MaterialDialog.Builder(this)
                .content(version.getNote())
                .cancelable(false)
                .negativeText("取消")
                .positiveText("确定")
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        //强制升级，不升级不让用
                        if (mVersion.getFlag() == 1) {
                            new MaterialDialog.Builder(HomeActivity.this)
                                    .cancelable(false)
                                    .content("当前版本必须升级，否则将无法使用app!")
                                    .negativeText("取消")
                                    .positiveText("确定")
                                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                            HomeActivity.this.finish();
                                            if (ConstantStr.ALPS.equals(SystemUtil.getDeviceBrand())) {
                                                startActivity(new Intent(HomeActivity.this, SplashActivity.class));
                                            }
                                        }
                                    }).onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    downLoadApp();
                                }
                            }).show();
                        }
                    }
                })
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        downLoadApp();
                    }
                })
                .show();
    }

    private void downLoadApp() {
        if (ConstantStr.ALPS.equals(SystemUtil.getDeviceBrand())) {
            new UpdateManager(HomeActivity.this, mVersion.getSpecialUrl(), HomeActivity.this).updateApp();
        } else {
            new UpdateManager(HomeActivity.this, mVersion.getUrl(), HomeActivity.this).updateApp();
        }
    }

    private long unReadNewsCount;

    @Override
    public void showUnReadCount(long count) {
        unReadNewsCount = count;
        if (bottomNavigation == null) {
            return;
        }
        if (selectPosition != 1) {
            if (unReadNewsCount == 0) {
                bottomNavigation.setNotification("", 1);
            } else if (unReadNewsCount > 99) {
                bottomNavigation.setNotificationBackgroundColor(findColorById(R.color.news_color));
                bottomNavigation.setNotification("99+" + "", 1);
            } else {
                bottomNavigation.setNotificationBackgroundColor(findColorById(R.color.news_color));
                bottomNavigation.setNotification(unReadNewsCount + "", 1);
            }
        } else {
            bottomNavigation.setNotification("", 1);
        }
    }

    @Override
    public void setPresenter(HomeContract.Presenter presenter) {
        mPresenter = presenter;
    }

    public static final int REQUEST_EXTERNAL = 10;


    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this)
                    .setTitle(getString(R.string.request_permissions))
                    .setPositiveButton(getString(R.string.sure))
                    .setNegativeButton(getString(R.string.cancel))
                    .setRequestCode(REQUEST_EXTERNAL)
                    .build()
                    .show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
        if (requestCode == REQUEST_PERMISSION) {
            if ((grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                PushManager.getInstance().initialize(this.getApplicationContext(), CustPushService.class);
            } else {
                PushManager.getInstance().initialize(this.getApplicationContext(), CustPushService.class);
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onCancelUpload() {
        if (mVersion.getFlag() == 1) {
            if (ConstantStr.ALPS.equals(SystemUtil.getDeviceBrand())) {
                startActivity(new Intent(HomeActivity.this, SplashActivity.class));
            }
            finish();
        }
    }

    /**
     * 定义广播接收器（内部类）
     */
    private class UpdateUIBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (bottomNavigation == null) {
                return;
            }
            if (mPresenter == null) {
                return;
            }
            mPresenter.getUnReadCount();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (TextUtils.equals(intent.getAction(), HOME_ACTION_OPEN_NEWS)) {
            if (selectPosition != 1 && bottomNavigation != null) {
                bottomNavigation.setCurrentItem(1);
            }
        }
    }

    private void cleanAllNotify() {
        NotificationManager notificationManager = (NotificationManager) getApplication().getSystemService(NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.cancelAll();
        }
    }

    @AfterPermissionGranted(REQUEST_EXTERNAL)
    public void checkPermission() {
        if (!EasyPermissions.hasPermissions(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                , android.Manifest.permission.RECORD_AUDIO)) {
            EasyPermissions.requestPermissions(this, "当前app 需要请求存储权限以及麦克风权限",
                    REQUEST_EXTERNAL, android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    , android.Manifest.permission.RECORD_AUDIO);
        } else {
            mPresenter.getNewVersion();
        }
    }
}
