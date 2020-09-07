package com.isuo.yw2application.view.splash;

import android.content.Intent;
import android.os.Bundle;

import com.isuo.yw2application.R;
import com.isuo.yw2application.app.Yw2Application;
import com.isuo.yw2application.common.ConstantStr;
import com.isuo.yw2application.view.base.BaseActivity;
import com.isuo.yw2application.view.MainActivity;
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
        new SplashPresenter(Yw2Application.getInstance().getRepositoryComponent().getRepository(), this);
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
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void showWelcome() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void openHome() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
