package com.isuo.yw2application.view.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatMultiAutoCompleteTextView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.isuo.yw2application.R;
import com.isuo.yw2application.app.Yw2Application;
import com.isuo.yw2application.common.ConstantStr;
import com.isuo.yw2application.view.base.MvpFragment;
import com.isuo.yw2application.view.main.MainActivity;
import com.isuo.yw2application.view.main.data.StandInfoActivity;
import com.isuo.yw2application.view.pass.ForgePassActivity;
import com.isuo.yw2application.view.register.RegisterActivity;
import com.isuo.yw2application.view.register.enterprise.RegisterEnterpriseActivity;
import com.isuo.yw2application.view.test.TestActivity;

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
        rootView.findViewById(R.id.setting).setOnClickListener(this);
        rootView.findViewById(R.id.tv_login).setOnClickListener(this);
        rootView.findViewById(R.id.tv_forget_password).setOnClickListener(this);
        rootView.findViewById(R.id.tv_test).setOnClickListener(this);
        rootView.findViewById(R.id.tv_register).setOnClickListener(this);
        rootView.findViewById(R.id.agree).setOnClickListener(this);
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
//        showProgressDialog("登陆中...");
    }

    @Override
    public void loginHideLoading() {
//        hideProgressDialog();
    }

    @Override
    public void showDialog(String message) {
        new MaterialDialog.Builder(getActivity()).content(message)
                .positiveText("确定")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        startHomeActivity();
                    }
                }).show();
    }

    @Override
    public void showFreezeDialog(String message) {
        new MaterialDialog.Builder(getActivity()).content(message)
                .positiveText("确定").show();
    }

    @Override
    public void showJoinDialog(String message) {
        new MaterialDialog.Builder(getActivity()).content(message)
                .positiveText("确定")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        startActivity(new Intent(getActivity(), RegisterEnterpriseActivity.class));
                    }
                }).show();
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
            case R.id.tv_register:
                startActivity(new Intent(getActivity(), RegisterActivity.class));
                break;
            case R.id.setting:
                startTestTimes++;
                if (startTestTimes > 6) {
                    startActivity(new Intent(getActivity(), TestActivity.class));
                    startTestTimes = 0;
                }
                break;
            case R.id.agree:
                Intent intent = new Intent(getActivity(), StandInfoActivity.class);
                intent.putExtra(ConstantStr.KEY_TITLE, "平台使用协议及隐私条款");
                String url = Yw2Application.getInstance().AppHost();
                int lastPosition = url.lastIndexOf("/");
                String newUrl = url.substring(0, lastPosition) + "/info.html";
                intent.putExtra(ConstantStr.KEY_BUNDLE_STR, newUrl);
                startActivity(intent);
                break;
        }
    }

    int startTestTimes;

    private void checkUserInfo() {
        String userName = mNameEt.getEditableText().toString().trim();
        String userPass = mPassEt.getEditableText().toString().trim();
        if (TextUtils.isEmpty(userName)) {
            Yw2Application.getInstance().showToast("请输入账号");
            return;
        }
        if (TextUtils.isEmpty(userPass)) {
            Yw2Application.getInstance().showToast("请输入密码");
            return;
        }
        if (mPresenter != null) {
            mPresenter.login(userName, userPass);
        }
    }

    private void startHomeActivity() {
        startActivity(new Intent(getActivity(), MainActivity.class));
        getActivity().finish();
    }
}
