package com.sito.evpro.inspection.view.login;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.orhanobut.logger.Logger;
import com.sito.evpro.inspection.R;
import com.sito.evpro.inspection.app.AppStatusConstant;
import com.sito.evpro.inspection.app.AppStatusManager;
import com.sito.evpro.inspection.app.InspectionApp;
import com.sito.evpro.inspection.common.ConstantStr;
import com.sito.evpro.inspection.view.BaseActivity;
import com.sito.library.utils.ActivityUtils;

import javax.inject.Inject;

/**
 * 登陆Act
 * Created by zhangan on 2017-06-22.
 */

public class LoginActivity extends BaseActivity {

    @Inject
    LoginPresenter loginPresenter;
    private boolean isNen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppStatusManager.getInstance().setAppStatus(AppStatusConstant.STATUS_NORMAL);//进入应用初始化设置成未登录状态
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);
        transparentStatusBar();
        setDarkStatusIcon(false);
        Intent intent = getIntent();
        if (intent != null) {
            if (!TextUtils.isEmpty(intent.getAction())) {
                if (intent.getAction().equals(ConstantStr.NEED_LOGIN)) {
                    isNen = true;
                }
            }
        }
        LoginFragment fragment = (LoginFragment) getFragmentManager().findFragmentById(R.id.frame_container);
        if (fragment == null) {
            fragment = LoginFragment.newInstance(isNen);
        }
        ActivityUtils.addFragmentToActivity(getFragmentManager(), fragment, R.id.frame_container);
        DaggerLoginComponent.builder().loginModule(new LoginModule(fragment))
                .inspectionRepositoryComponent(InspectionApp.getInstance().getRepositoryComponent())
                .build().inject(this);
    }

//    @Override
//    public void onBackPressed() {
//        new MaterialDialog.Builder(this).content("退出后将关机!")
//                .positiveText("确定")
//                .negativeText("取消")
//                .onPositive(new MaterialDialog.SingleButtonCallback() {
//                    @Override
//                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//
//                    }
//                }).show();
//    }
}
