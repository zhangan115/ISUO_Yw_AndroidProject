package com.isuo.yw2application.view.register;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.isuo.yw2application.R;
import com.isuo.yw2application.app.Yw2Application;
import com.isuo.yw2application.common.ConstantStr;
import com.isuo.yw2application.utils.CountDownTimerUtils;
import com.isuo.yw2application.view.base.BaseActivity;
import com.isuo.yw2application.view.register.enterprise.RegisterEnterpriseActivity;
import com.sito.library.utils.CheckInput;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import javax.inject.Inject;

public class RegisterActivity extends BaseActivity implements View.OnClickListener, RegisterContract.View {

    private EditText mUserPhoneNumEt, mCodeEt, mRealNameEt, mUSerPwsEt;
    private TextView mGetCode;
    private ImageView mCleanPsIv, mCleanPhoneNumIv, mCleanUserNameIv;
    private String mPhoneNumStr, mCodeStr, mRealNameStr, mUserPws;
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
        mCodeEt = findViewById(R.id.code_et);
        mRealNameEt = findViewById(R.id.realName_et);
        mUSerPwsEt = findViewById(R.id.userPws_et);

        mCleanUserNameIv = findViewById(R.id.username_iv);
        mCleanPhoneNumIv = findViewById(R.id.clean_phone_iv);
        mCleanPsIv = findViewById(R.id.password_iv);
        mGetCode = findViewById(R.id.get_code_tv);
        timerUtils = new CountDownTimerUtils(mGetCode, 60000, 1000, "重新获取", "");
    }


    private void initEvents() {
        mGetCode.setOnClickListener(this);
        mCleanUserNameIv.setOnClickListener(this);
        mCleanPsIv.setOnClickListener(this);
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
                        Yw2Application.getInstance().showToast(Objects.requireNonNull(CheckInput.checkPhoneNum(mPhoneNumStr)));
                        return;
                    }
                    mPresenter.getVerificationCode(mPhoneNumStr);
                }
                break;
            case R.id.tv_sure:
                mPhoneNumStr = mUserPhoneNumEt.getText().toString();
                mCodeStr = mCodeEt.getText().toString();
                mRealNameStr = mRealNameEt.getText().toString();
                mUserPws = mUSerPwsEt.getText().toString();
                if (CheckInput.checkIdeCode(mCodeStr) != null) {
                    Yw2Application.getInstance().showToast(Objects.requireNonNull(CheckInput.checkIdeCode(mCodeStr)));
                    return;
                }
                if (TextUtils.isEmpty(mRealNameStr)) {
                    Yw2Application.getInstance().showToast("请输入真实姓名");
                    return;
                }
                if (TextUtils.isEmpty(mPhoneNumStr)) {
                    Yw2Application.getInstance().showToast("请输入手机号码");
                    return;
                }
                if (TextUtils.isEmpty(mUserPws)) {
                    Yw2Application.getInstance().showToast("请输入密码");
                    return;
                }
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("realName", mRealNameStr);
                    jsonObject.put("userPhone", mPhoneNumStr);
                    jsonObject.put("userPwd", mUserPws);
                    jsonObject.put("code", mCodeStr);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mPresenter.toRegister(jsonObject);
                break;
            case R.id.clean_phone_iv:
                mUserPhoneNumEt.setText("");
                break;
            case R.id.password_iv:
                mUSerPwsEt.setText("");
                break;
            case R.id.username_iv:
                mRealNameEt.setText("");
                break;
        }
    }

    @Override
    public void getSuccess(String code) {
        Yw2Application.getInstance().showToast("发送成功");
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
        Yw2Application.getInstance().showToast("注册成功");
        Intent intent = new Intent(this, RegisterEnterpriseActivity.class);
        startActivityForResult(intent, 100);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            useAnimToBack = false;
            setResult(Activity.RESULT_OK);
            finish();
            overridePendingTransition(0, 0);
        }
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
                    mCleanUserNameIv.setVisibility(View.GONE);
                } else {
                    mCleanUserNameIv.setVisibility(View.VISIBLE);
                }
            }
        });
        mUSerPwsEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mUSerPwsEt.getText().toString().isEmpty()) {
                    mCleanPsIv.setVisibility(View.GONE);
                } else {
                    mCleanPsIv.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}
