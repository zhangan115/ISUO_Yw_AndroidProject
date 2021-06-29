package com.isuo.yw2application.view.pass;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import  com.isuo.yw2application.R;
import  com.isuo.yw2application.app.Yw2Application;
import  com.isuo.yw2application.mode.bean.Register;
import  com.isuo.yw2application.utils.CountDownTimerUtils;
import  com.isuo.yw2application.view.base.BaseActivity;
import com.sito.library.utils.CheckInput;

import javax.inject.Inject;

public class ForgePassActivity extends BaseActivity implements View.OnClickListener, ForgePassContract.View {

    private EditText mUserPhoneNumEt, mPasswordEt, mPasswordAgainEt, mCodeEt;
    private TextView mGetCode;
    private ImageView mCleanPs1Iv, mCleanPs2Iv, mCleanPhoneNumIv;
    private String mPhoneNumStr, mPassStr, mPassStrAgainStr, mCodeStr;
    public static final String NAME = "NAME";
    public static final String PASSWORD = "PASSWORD";

    @Inject
    ForgePassPresenter registerPresenter;
    ForgePassContract.Presenter mPresenter;
    private CountDownTimerUtils timerUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutAndToolbar(R.layout.activity_register, "找回密码");
        DaggerForgePassComponent.builder().customerRepositoryComponent(Yw2Application.getInstance().getRepositoryComponent())
                .forgePassModule(new ForgePassModule(this)).build().inject(this);
        initViews();
        initEvents();
        showCleanText();
    }


    private void initViews() {
        mUserPhoneNumEt = findViewById(R.id.user_phone_et);
        mPasswordEt = findViewById(R.id.password_et);
        mPasswordAgainEt = findViewById(R.id.password_2_et);
        mCodeEt = findViewById(R.id.code_et);
        mCleanPhoneNumIv = findViewById(R.id.clean_phone_iv);
        mCleanPs1Iv = findViewById(R.id.password_iv);
        mCleanPs2Iv = findViewById(R.id.clean_password_2_iv);
        mGetCode = findViewById(R.id.get_code_tv);
        timerUtils = new CountDownTimerUtils(mGetCode, 60000, 1000, "重新获取", "");
    }


    private void initEvents() {
        mGetCode.setOnClickListener(this);
        mCleanPs1Iv.setOnClickListener(this);
        mCleanPs2Iv.setOnClickListener(this);
        mPasswordAgainEt.setOnClickListener(this);
        mCleanPhoneNumIv.setOnClickListener(this);
        findViewById(R.id.tv_sure).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.get_code_tv:
                if (!timerUtils.getTimer()) {//防止重复点击
                    mPhoneNumStr = mUserPhoneNumEt.getText().toString();
                    //获取验证码
                    if (CheckInput.checkPhoneNum(mPhoneNumStr) != null) {
                        Yw2Application.getInstance().showToast(CheckInput.checkPhoneNum(mPhoneNumStr));
                        return;
                    }
                    mPresenter.getVerificationCode(mPhoneNumStr);
                }
                break;
            case R.id.tv_sure:
                mPassStr = mPasswordEt.getText().toString();
                mPassStrAgainStr = mPasswordAgainEt.getText().toString();
                mCodeStr = mCodeEt.getText().toString();
                if (!mPassStrAgainStr.equals(mPassStr)) {
                    Yw2Application.getInstance().showToast("两次密码输入不一致");
                    return;
                }
                if (CheckInput.checkIdeCode(mCodeStr) != null) {
                    Yw2Application.getInstance().showToast(CheckInput.checkIdeCode(mCodeStr));
                    return;
                }
                //重置密码
                Register mRegister = new Register();
                mRegister.setPhoneNum(mPhoneNumStr);
                mRegister.setPassword(mPassStr);
                mRegister.setVerificationCode(mCodeStr);
                mPresenter.toRegister(mRegister);
                break;
            case R.id.clean_phone_iv:
                mUserPhoneNumEt.setText("");
                break;
            case R.id.password_iv:
                mPasswordEt.setText("");
                break;
            case R.id.password_2_et:
                mPasswordAgainEt.setText("");
                break;
        }
    }

    @Override
    public void getSuccess() {
        timerUtils.start();
    }

    @Override
    public void getFail() {
        Yw2Application.getInstance().showToast("验证码发送失败");
    }

    @Override
    public void registerFail() {
        Yw2Application.getInstance().showToast("密码重置失败");
    }

    @Override
    public void registerSuccess() {
        Yw2Application.getInstance().showToast("密码重置成功");
        Intent intent = new Intent();
        intent.putExtra(NAME, mPhoneNumStr);
        intent.putExtra(PASSWORD, mPassStr);
        setResult(RESULT_OK, intent);//发送返回码
        finish();
    }

    @Override
    public void showLoading() {
        showProgressDialog("请稍等...");
    }

    @Override
    public void hideLoading() {
        hideProgressDialog();
    }

    @Override
    public void setPresenter(ForgePassContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    protected void onCancel() {
        super.onCancel();
        mPresenter.unSubscribe();
    }

    /**
     * 监听文本框输入状态
     */
    private void showCleanText() {
        mUserPhoneNumEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mUserPhoneNumEt.getText().toString().isEmpty()) {
                    mCleanPhoneNumIv.setVisibility(View.GONE);
                } else {
                    mCleanPhoneNumIv.setVisibility(View.VISIBLE);
                }
            }
        });
        mPasswordEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mPasswordEt.getText().toString().isEmpty()) {
                    mCleanPs1Iv.setVisibility(View.GONE);
                } else {
                    mCleanPs1Iv.setVisibility(View.VISIBLE);
                }
            }
        });
        mPasswordAgainEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mPasswordAgainEt.getText().toString().isEmpty()) {
                    mCleanPs2Iv.setVisibility(View.GONE);
                } else {
                    mCleanPs2Iv.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}
