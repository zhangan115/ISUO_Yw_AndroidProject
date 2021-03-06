package com.sito.customer.view.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatMultiAutoCompleteTextView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.sito.customer.R;
import com.sito.customer.app.CustomerApp;
import com.sito.customer.view.MvpFragment;
import com.sito.customer.view.home.HomeActivity;
import com.sito.customer.view.pass.ForgePassActivity;
import com.sito.customer.view.regist.RegisterActivity;
import com.sito.customer.view.test.TestActivity;

/**
 * 登陆界面
 * Created by zhangan on 2017-06-22.
 */

public class LoginFragment extends MvpFragment<LoginContract.Presenter> implements LoginContract.View, View.OnClickListener {

    private AppCompatMultiAutoCompleteTextView mNameEt;
    private EditText mPassEt;

    public static LoginFragment newInstance() {
        Bundle args = new Bundle();
        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fmg_login, container, false);
        mNameEt = rootView.findViewById(R.id.edit_username);
        mPassEt = rootView.findViewById(R.id.edit_password);
        rootView.findViewById(R.id.tv_login).setOnClickListener(this);
        rootView.findViewById(R.id.tv_forget_password).setOnClickListener(this);
        rootView.findViewById(R.id.tv_test).setOnClickListener(this);
        rootView.findViewById(R.id.tv_reg).setOnClickListener(this);
        return rootView;
    }

    @Override
    public void loginSuccess() {
        startHomeActivity();
    }

    @Override
    public void loginFail() {

    }

    @Override
    public void loginLoading() {
        showProgressDialog("登陆中...");
    }

    @Override
    public void loginHideLoading() {
        hideProgressDialog();
    }


    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_login:
                checkUserInfo();
                break;
            case R.id.tv_forget_password:
                startActivity(new Intent(getActivity(), ForgePassActivity.class));
                break;
            case R.id.tv_test:
                startTestTimes++;
                if (startTestTimes > 6) {
                    startActivity(new Intent(getActivity(), TestActivity.class));
                    startTestTimes = 0;
                }
                break;
            case R.id.tv_reg:
                startActivity(new Intent(getActivity(), RegisterActivity.class));
                break;
        }
    }

    int startTestTimes;

    private void checkUserInfo() {
        String userName = mNameEt.getEditableText().toString().trim();
        String userPass = mPassEt.getEditableText().toString().trim();
        if (TextUtils.isEmpty(userName)) {
            CustomerApp.getInstance().showToast("请输入账号");
            return;
        }
        if (TextUtils.isEmpty(userPass)) {
            CustomerApp.getInstance().showToast("请输入密码");
            return;
        }
        if (mPresenter != null) {
            mPresenter.login(userName, userPass);
        }
    }

    private void startHomeActivity() {
        startActivity(new Intent(getActivity(), HomeActivity.class));
        getActivity().finish();
    }
}
