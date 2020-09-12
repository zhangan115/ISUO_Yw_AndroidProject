package com.isuo.yw2application.view.register;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import  com.isuo.yw2application.R;
import  com.isuo.yw2application.app.Yw2Application;
import  com.isuo.yw2application.utils.CountDownTimerUtils;
import  com.isuo.yw2application.view.base.BaseActivity;
import com.sito.library.utils.CheckInput;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

public class RegisterActivity extends BaseActivity implements View.OnClickListener, RegisterContract.View {

    private EditText mUserPhoneNumEt, mRealNameEt, mUnitNameEt, mCodeEt;
    private TextView mGetCode;
    private ImageView mCleanPs1Iv, mCleanPs2Iv, mCleanPhoneNumIv;
    private String mPhoneNumStr, mRealNameStr, mUnitNameStr, mCodeStr;
    @Inject
    RegisterPresenter registerPresenter;
    RegisterContract.Presenter mPresenter;
    private CountDownTimerUtils timerUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutAndToolbar(R.layout.activity_register_user, "免费注册");
        DaggerRegisterComponent.builder().customerRepositoryComponent(Yw2Application.getInstance().getRepositoryComponent())
                .registerModule(new RegisterModule(this)).build().inject(this);
        initViews();
        initEvents();
        showCleanText();
    }


    private void initViews() {
        mUserPhoneNumEt = findViewById(R.id.user_phone_et);
        mRealNameEt = findViewById(R.id.realName_et);
        mUnitNameEt = findViewById(R.id.unitName_et);
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
        mUnitNameEt.setOnClickListener(this);
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
                mRealNameStr = mRealNameEt.getText().toString();
                mUnitNameStr = mUnitNameEt.getText().toString();
                mCodeStr = mCodeEt.getText().toString();
                if (CheckInput.checkIdeCode(mCodeStr) != null) {
                    Yw2Application.getInstance().showToast(CheckInput.checkIdeCode(mCodeStr));
                    return;
                }
                if (TextUtils.isEmpty(mRealNameStr)) {
                    Yw2Application.getInstance().showToast("请输入真实姓名");
                    return;
                }
                if (TextUtils.isEmpty(mUnitNameStr)) {
                    Yw2Application.getInstance().showToast("请输入公司名称");
                    return;
                }
                if (TextUtils.isEmpty(mPhoneNumStr)) {
                    Yw2Application.getInstance().showToast("请输入手机号码");
                    return;
                }
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("realName", mRealNameStr);
                    jsonObject.put("unitName", mUnitNameStr);
                    jsonObject.put("phone", mPhoneNumStr);
                    jsonObject.put("validateCode", mCodeStr);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mPresenter.toRegister(jsonObject);
                break;
            case R.id.clean_phone_iv:
                mUserPhoneNumEt.setText("");
                break;
            case R.id.password_iv:
                mRealNameEt.setText("");
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
        Yw2Application.getInstance().showToast("注册失败");
    }

    @Override
    public void registerSuccess() {
        Intent intent = new Intent();
        Yw2Application.getInstance().showToast("注册成功");
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
    public void setPresenter(RegisterContract.Presenter presenter) {
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
        mRealNameEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mRealNameEt.getText().toString().isEmpty()) {
                    mCleanPs1Iv.setVisibility(View.GONE);
                } else {
                    mCleanPs1Iv.setVisibility(View.VISIBLE);
                }
            }
        });
        mUnitNameEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mUnitNameEt.getText().toString().isEmpty()) {
                    mCleanPs2Iv.setVisibility(View.GONE);
                } else {
                    mCleanPs2Iv.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}
