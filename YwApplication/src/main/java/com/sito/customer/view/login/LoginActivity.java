package com.sito.customer.view.login;

import android.os.Bundle;

import com.sito.customer.R;
import com.sito.customer.app.CustomerApp;
import com.sito.customer.common.ConstantStr;
import com.sito.customer.view.BaseActivity;
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
        LoginFragment fragment = (LoginFragment) getFragmentManager().findFragmentById(R.id.frame_container);
        if (fragment == null) {
            fragment = LoginFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getFragmentManager(), fragment, R.id.frame_container);
        }
        DaggerLoginComponent.builder().loginModule(new LoginModule(fragment))
                .customerRepositoryComponent(CustomerApp.getInstance().getRepositoryComponent())
                .build().inject(this);
    }

    @Override
    public void onBackPressed() {
        if (!ConstantStr.ALPS.equals(SystemUtil.getDeviceBrand())) {
            super.onBackPressed();
        }
    }
}
