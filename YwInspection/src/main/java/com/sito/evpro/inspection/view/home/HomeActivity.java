package com.sito.evpro.inspection.view.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.sito.evpro.inspection.R;
import com.sito.evpro.inspection.app.AppStatusConstant;
import com.sito.evpro.inspection.app.InspectionApp;
import com.sito.evpro.inspection.common.ConstantStr;
import com.sito.evpro.inspection.mode.bean.NewVersion;
import com.sito.evpro.inspection.mode.bean.equip.EquipBean;
import com.sito.evpro.inspection.mode.bean.option.OptionBean;
import com.sito.evpro.inspection.utils.UpdateManager;
import com.sito.evpro.inspection.view.BaseActivity;
import com.sito.evpro.inspection.view.contact.ContactActivity;
import com.sito.evpro.inspection.view.create.equipment.CreateEquipmentActivity;
import com.sito.evpro.inspection.view.equipment.equiplist.EquipListActivity;
import com.sito.evpro.inspection.view.fault.FaultActivity;
import com.sito.evpro.inspection.view.greasing.GreasingListActivity;
import com.sito.evpro.inspection.view.increment.IncrementActivity;
import com.sito.evpro.inspection.view.login.LoginActivity;
import com.sito.evpro.inspection.view.repair.RepairActivity;
import com.sito.evpro.inspection.view.setting.SettingActivity;
import com.sito.evpro.inspection.view.sos.SOSActivity;
import com.sito.evpro.inspection.view.splash.SplashActivity;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class HomeActivity extends BaseActivity implements HomeContract.View, EasyPermissions.PermissionCallbacks, UpdateManager.DownLoadCancelListener {

    @Inject
    HomePresenter homePresenter;
    @Nullable
    HomeContract.Presenter mPresenter;
    private NewVersion mVersion;
    private MaterialDialog exitDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        transparentStatusBar();
        setDarkStatusIcon(true);
        DaggerHomeComponent.builder().inspectionRepositoryComponent(InspectionApp.getInstance().getRepositoryComponent())
                .homeModule(new HomeModule(this)).build()
                .inject(this);
        if (mPresenter != null) {
            mPresenter.subscribe();
        }
        findViewById(R.id.tv_setting).setOnClickListener(this);
        findViewById(R.id.tv_account).setOnClickListener(this);
        findViewById(R.id.tv_repair).setOnClickListener(this);
        findViewById(R.id.tv_users).setOnClickListener(this);
        findViewById(R.id.tv_greasing).setOnClickListener(this);
        findViewById(R.id.tv_increment).setOnClickListener(this);
        findViewById(R.id.tv_fault).setOnClickListener(this);
        findViewById(R.id.tv_sos).setOnClickListener(this);
        findViewById(R.id.tv_create_equip).setOnClickListener(this);
        findViewById(R.id.tv_equip_info).setOnClickListener(this);
        permList.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permList.add(android.Manifest.permission.RECORD_AUDIO);
        checkPermission();
    }

    @Override
    public void onClick(View v) {
        if (!EasyPermissions.hasPermissions(this.getApplicationContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                , android.Manifest.permission.RECORD_AUDIO)) {
            new AppSettingsDialog.Builder(this)
                    .setTitle(getString(R.string.request_permissions))
                    .setRationale(getString(R.string.need_save_state_setting))
                    .setPositiveButton(getString(R.string.sure))
                    .setNegativeButton(getString(R.string.cancel))
                    .setRequestCode(REQUEST_EXTERNAL)
                    .build()
                    .show();
            return;
        }
        switch (v.getId()) {
            case R.id.tv_setting://设置
                startActivity(new Intent(this, SettingActivity.class));
                overridePendingTransition(R.anim.slideinright, R.anim.slideoutleft);
                break;
            case R.id.tv_account://更换账号
                View view = LayoutInflater.from(this).inflate(R.layout.dialog_home_exit, null);
                view.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (exitDialog != null) {
                            exitDialog.dismiss();
                        }
                    }
                });
                view.findViewById(R.id.tv_sure).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (exitDialog != null) {
                            exitDialog.dismiss();
                            if (mPresenter != null) {
                                mPresenter.exitApp();
                            }
                            InspectionApp.getInstance().exitCurrentUser();
                            //友盟用户统计
                            MobclickAgent.onProfileSignOff();
                            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                            overridePendingTransition(R.anim.slideinright, R.anim.slideoutleft);
                            finish();
                        }
                    }
                });
                exitDialog = new MaterialDialog.Builder(this)
                        .customView(view, false)
                        .build();
                exitDialog.show();
                break;
            case R.id.tv_repair://检修工作
                startActivity(new Intent(this, RepairActivity.class));
                overridePendingTransition(R.anim.slideinright, R.anim.slideoutleft);
                break;
            case R.id.tv_users://联系人
                startActivity(new Intent(this, ContactActivity.class));
                overridePendingTransition(R.anim.slideinright, R.anim.slideoutleft);
                break;
            case R.id.tv_greasing://注油管理
                startActivity(new Intent(this, GreasingListActivity.class));
                overridePendingTransition(R.anim.slideinright, R.anim.slideoutleft);
                break;
            case R.id.tv_increment://增值工作
                startActivity(new Intent(this, IncrementActivity.class));
                overridePendingTransition(R.anim.slideinright, R.anim.slideoutleft);
                break;
            case R.id.tv_fault://故障上报
                startActivity(new Intent(this, FaultActivity.class));
                overridePendingTransition(R.anim.slideinright, R.anim.slideoutleft);
                break;
            case R.id.tv_sos://SoS
                startActivity(new Intent(this, SOSActivity.class));
                overridePendingTransition(R.anim.slideinright, R.anim.slideoutleft);
                break;
            case R.id.tv_create_equip://创建设备
                startActivity(new Intent(this, CreateEquipmentActivity.class));
                overridePendingTransition(R.anim.slideinright, R.anim.slideoutleft);
                break;
            case R.id.tv_equip_info://
                Intent intent = new Intent(this, EquipListActivity.class);
                intent.putExtra(ConstantStr.KEY_BUNDLE_BOOLEAN_1, true);
                startActivity(intent);
                overridePendingTransition(R.anim.slideinright, R.anim.slideoutleft);
                break;
        }
    }

    @Override
    public void saveEquipData(List<EquipBean> list) {

    }

    @Override
    public void saveOptionData(List<OptionBean> list) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void saveSuccess() {

    }

    @Override
    public void saveFail() {

    }

    @Override
    public void showNewVersion(NewVersion version) {
        mVersion = version;
        new MaterialDialog.Builder(this)
                .content(version.getNote())
                .negativeText("取消")
                .positiveText("确定")
                .cancelable(false)
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
                                        }
                                    }).onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    new UpdateManager(HomeActivity.this, mVersion.getUrl(), HomeActivity.this).updateApp();
                                }
                            }).show();
                        }
                    }
                })
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        new UpdateManager(HomeActivity.this, mVersion.getUrl(), HomeActivity.this).updateApp();
                    }
                })
                .show();
    }


    @Override
    public void setPresenter(HomeContract.Presenter presenter) {
        mPresenter = presenter;
    }

    private long mCurrentTime = 0;

    @Override
    public void onBackPressed() {
        long time = System.currentTimeMillis();
        if (time - this.mCurrentTime < 3000) {
            InspectionApp.getInstance().exitApp();
        } else {
            this.mCurrentTime = time;
            InspectionApp.getInstance().showToast(getString(R.string.toast_exit_app));
        }
    }


    public static final int REQUEST_EXTERNAL = 10;//内存卡权限

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    List<String> permList = new ArrayList<>();

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (requestCode == REQUEST_EXTERNAL) {
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

    @AfterPermissionGranted(REQUEST_EXTERNAL)
    public void checkPermission() {
        if (!EasyPermissions.hasPermissions(this.getApplicationContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                , android.Manifest.permission.RECORD_AUDIO)) {
            EasyPermissions.requestPermissions(this, "当前app 需要请求存储权限以及麦克风权限",
                    REQUEST_EXTERNAL, android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    , android.Manifest.permission.RECORD_AUDIO);
        }
    }

    @Override
    protected void restartApp() {
        startActivity(new Intent(this, SplashActivity.class));
        finish();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        int action = intent.getIntExtra(AppStatusConstant.KEY_HOME_ACTION, AppStatusConstant.ACTION_BACK_TO_HOME);
        if (action == AppStatusConstant.ACTION_RESTART_APP) {
            restartApp();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mPresenter != null) {
            mPresenter.unSubscribe();
        }
    }

    @Override
    public void onCancelUpdate() {
        if (mVersion.getFlag() == 1) {
            finish();
        }
    }
}
