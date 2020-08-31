package com.sito.evpro.inspection.view.splash;

import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;

import com.sito.evpro.inspection.BuildConfig;
import com.sito.evpro.inspection.R;
import com.sito.evpro.inspection.app.AppStatusConstant;
import com.sito.evpro.inspection.app.AppStatusManager;
import com.sito.evpro.inspection.app.InspectionApp;
import com.sito.evpro.inspection.common.ConstantStr;
import com.sito.evpro.inspection.view.BaseActivity;
import com.sito.evpro.inspection.view.fault.FaultActivity;
import com.sito.evpro.inspection.view.home.HomeActivity;
import com.sito.evpro.inspection.view.increment.IncrementActivity;
import com.sito.evpro.inspection.view.login.LoginActivity;
import com.sito.evpro.inspection.view.shortcuts.ShortcutsActivity;
import com.sito.evpro.inspection.view.sos.SOSActivity;
import com.sito.library.utils.SystemUtil;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;


/**
 * 启动屏幕
 * Created by zhangan on 2017-07-24.
 */

public class SplashActivity extends BaseActivity implements SplashContract.View {

    private SplashContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppStatusManager.getInstance().setAppStatus(AppStatusConstant.STATUS_NORMAL);//进入应用初始化设置成未登录状态
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        transparentStatusBar();
        new SplashPresenter(InspectionApp.getInstance().getRepositoryComponent().getRepository(), this);
        if (mPresenter != null && !ConstantStr.ALPS.equals(SystemUtil.getDeviceBrand())) {
            mPresenter.subscribe();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
                initDynamicShortcuts();
            }
        } else {
            needLogin();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    private void initDynamicShortcuts() {
        ShortcutManager scManager = getSystemService(ShortcutManager.class);
        Intent intent1 = new Intent(Intent.ACTION_MAIN, Uri.EMPTY, this, ShortcutsActivity.class);
        intent1.setAction(FaultActivity.ACTION);
        ShortcutInfo scInfo1 = new ShortcutInfo.Builder(this, "fault_id")
                .setShortLabel(getString(R.string.fault_short_name))
                .setLongLabel(getString(R.string.fault_long_name))
                .setIcon(Icon.createWithResource(this, R.drawable.fault_call_icon_selected))
                .setIntent(intent1)
                .build();
        Intent intent2 = new Intent(Intent.ACTION_MAIN, Uri.EMPTY, this, ShortcutsActivity.class);
        intent2.setAction(IncrementActivity.ACTION);
        ShortcutInfo scInfo2 = new ShortcutInfo.Builder(this, "increment_id")
                .setShortLabel(getString(R.string.increment_short_name))
                .setLongLabel(getString(R.string.increment_long_name))
                .setIcon(Icon.createWithResource(this, R.drawable.fault_call_icon_selected))
                .setIntent(intent2)
                .build();
        Intent intent3 = new Intent(Intent.ACTION_MAIN, Uri.EMPTY, this, ShortcutsActivity.class);
        intent3.setAction(SOSActivity.ACTION);
        ShortcutInfo scInfo3 = new ShortcutInfo.Builder(this, "sos_id")
                .setShortLabel(getString(R.string.sos_short_name))
                .setLongLabel(getString(R.string.sos_long_name))
                .setIcon(Icon.createWithResource(this, R.drawable.fault_call_icon_selected))
                .setIntent(intent3)
                .build();
        scManager.setDynamicShortcuts(Arrays.asList(scInfo1, scInfo2, scInfo3));
        ShortcutInfo dynamicActivityShortcut1 = new ShortcutInfo.Builder(this, "fault_id")
                .setRank(0)
                .build();
        ShortcutInfo dynamicActivityShortcut2 = new ShortcutInfo.Builder(this, "increment_id")
                .setRank(1)
                .build();
        ShortcutInfo dynamicActivityShortcut3 = new ShortcutInfo.Builder(this, "sos_id")
                .setRank(1)
                .build();
        scManager.updateShortcuts(Arrays.asList(dynamicActivityShortcut1, dynamicActivityShortcut2, dynamicActivityShortcut3));
    }

    @Override
    public void setPresenter(SplashContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void needLogin() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    public void openHome() {
        startActivity(new Intent(this, HomeActivity.class));
        overridePendingTransition(R.anim.slideinright, R.anim.slideoutleft);
        finish();
    }
}
