package com.isuo.yw2application.view.login;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.isuo.yw2application.R;
import com.isuo.yw2application.app.Yw2Application;
import com.isuo.yw2application.common.ConstantStr;
import com.isuo.yw2application.view.base.BaseActivity;
import com.sito.library.utils.ActivityUtils;
import com.sito.library.utils.SystemUtil;

import javax.inject.Inject;

/**
 * 登陆Act
 * Created by zhangan on 2017-06-22.
 */

public class LoginActivity extends BaseActivity {

    @Inject
    LoginPresenter loginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutAndToolbar(R.layout.activity_container);
        transparentStatusBar();
        setDarkStatusIcon(true);
        FrameLayout fl = findViewById(R.id.frame_container);
        fl.setPadding(0,getStatusHeight(),0,0);
        LoginFragment fragment = (LoginFragment) getFragmentManager().findFragmentById(R.id.frame_container);
        if (fragment == null) {
            fragment = LoginFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getFragmentManager(), fragment, R.id.frame_container);
        }
        DaggerLoginComponent.builder().loginModule(new LoginModule(fragment))
                .customerRepositoryComponent(Yw2Application.getInstance().getRepositoryComponent())
                .build().inject(this);
    }

    @Override
    public void onBackPressed() {
        if (!ConstantStr.ALPS.equals(SystemUtil.getDeviceBrand())) {
            super.onBackPressed();
        }
    }
}
