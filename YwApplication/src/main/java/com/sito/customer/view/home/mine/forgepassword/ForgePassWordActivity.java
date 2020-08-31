package com.sito.customer.view.home.mine.forgepassword;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.sito.customer.R;
import com.sito.customer.app.CustomerApp;
import com.sito.customer.view.BaseActivity;

/**
 * Created by zhangan on 2017-07-26.
 */

public class ForgePassWordActivity extends BaseActivity implements ForgePassWordContract.View {

    private EditText mOldPasswordEt, mPasswordEt, mPasswordAgainEt;
    private ImageView mCleanPs1Iv, mCleanPs2Iv, mOldPasswordIv;
    private String mOldPasswordStr, mPassStr, mPassStrAgainStr;

    ForgePassWordContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutAndToolbar(R.layout.activity_forgepassword, "修改密码");
        new ForgePassWordPresenter(CustomerApp.getInstance().getRepositoryComponent().getRepository(), this);
        initViews();
        initEvents();
        showCleanText();
    }

    private void initViews() {
        mOldPasswordEt = (EditText) findViewById(R.id.user_phone_et);
        mPasswordEt = (EditText) findViewById(R.id.password_et);
        mPasswordAgainEt = (EditText) findViewById(R.id.password_2_et);

        mOldPasswordIv = (ImageView) findViewById(R.id.clean_phone_iv);
        mCleanPs1Iv = (ImageView) findViewById(R.id.password_iv);
        mCleanPs2Iv = (ImageView) findViewById(R.id.clean_password_2_iv);
    }

    private void initEvents() {
        mCleanPs1Iv.setOnClickListener(this);
        mCleanPs2Iv.setOnClickListener(this);
        mOldPasswordIv.setOnClickListener(this);
        findViewById(R.id.tv_sure).setOnClickListener(this);
    }

    /**
     * 监听文本框输入状态
     */
    private void showCleanText() {
        mOldPasswordEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mOldPasswordEt.getText().toString().isEmpty()) {
                    mOldPasswordIv.setVisibility(View.GONE);
                } else {
                    mOldPasswordIv.setVisibility(View.VISIBLE);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_sure:
                mOldPasswordStr = mOldPasswordEt.getText().toString();
                mPassStr = mPasswordEt.getText().toString();
                mPassStrAgainStr = mPasswordAgainEt.getText().toString();
                if (!mPassStrAgainStr.equals(mPassStr)) {
                    CustomerApp.getInstance().showToast("两次密码输入不一致");
                    return;
                }
                if (TextUtils.isEmpty(mOldPasswordStr)) {
                    CustomerApp.getInstance().showToast("请输入旧密码");
                    return;
                }
                mPresenter.updatePassWord(mOldPasswordStr, mPassStrAgainStr);
                break;
            case R.id.clean_phone_iv:
                mOldPasswordEt.setText("");
                break;
            case R.id.password_iv:
                mPasswordEt.setText("");
                break;
            case R.id.clean_password_2_iv:
                mPasswordAgainEt.setText("");
                break;
        }
    }

    @Override
    public void updateSuccess() {
        CustomerApp.getInstance().showToast("密码修改成功");
        finish();
    }

    @Override
    public void updateFail() {
        CustomerApp.getInstance().showToast("修改密码失败");
    }

    @Override
    public void setPresenter(ForgePassWordContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
