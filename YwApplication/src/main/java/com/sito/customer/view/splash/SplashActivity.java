package com.sito.customer.view.splash;

import android.content.Intent;
import android.os.Bundle;

import com.sito.customer.R;
import com.sito.customer.app.CustomerApp;
import com.sito.customer.common.ConstantStr;
import com.sito.customer.view.BaseActivity;
import com.sito.customer.view.home.HomeActivity;
import com.sito.customer.view.login.LoginActivity;
import com.sito.customer.view.welcome.WelcomeActivity;
import com.sito.library.utils.SystemUtil;

/**
 * 启动屏幕
 * Created by zhangan on 2017-07-24.
 */

public class SplashActivity extends BaseActivity implements SplashContract.View {

    private SplashContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        transparentStatusBar();
        new SplashPresenter(CustomerApp.getInstance().getRepositoryComponent().getRepository(), this);
        if (mPresenter != null && !ConstantStr.ALPS.equals(SystemUtil.getDeviceBrand())) {
            mPresenter.subscribe();
        } else {
            needLogin();
        }
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
    public void showWelcome() {
        startActivity(new Intent(this, WelcomeActivity.class));
        finish();
    }

    @Override
    public void openHome() {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }
}
