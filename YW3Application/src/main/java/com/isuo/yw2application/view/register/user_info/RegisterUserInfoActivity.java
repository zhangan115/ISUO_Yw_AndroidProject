package com.isuo.yw2application.view.register.user_info;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.isuo.yw2application.R;
import com.isuo.yw2application.app.Yw2Application;
import com.isuo.yw2application.common.ConstantStr;
import com.isuo.yw2application.view.base.BaseActivity;
import com.isuo.yw2application.view.register.enterprise.RegisterEnterpriseActivity;

public class RegisterUserInfoActivity extends BaseActivity {

    private EditText mUserNameEt, mUserPassEt;
    private String phoneNum,codeStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutAndToolbar(R.layout.activity_register_user_info, "完善个人信息");
        mUserNameEt = findViewById(R.id.user_phone_et);
        mUserPassEt = findViewById(R.id.code_et);
        phoneNum = getIntent().getStringExtra(ConstantStr.KEY_BUNDLE_STR);
        codeStr = getIntent().getStringExtra(ConstantStr.KEY_BUNDLE_STR_1);
        findViewById(R.id.tv_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                next();
            }
        });
    }

    public void next() {
        String name = mUserNameEt.getText().toString();
        String pass = mUserPassEt.getText().toString();
        if (TextUtils.isEmpty(name)) {
            Yw2Application.getInstance().showToast("请输入姓名");
            return;
        }
        if (TextUtils.isEmpty(pass)) {
            Yw2Application.getInstance().showToast("请输入密码");
            return;
        }
        Intent intent = new Intent(this, RegisterEnterpriseActivity.class);
        intent.putExtra(ConstantStr.KEY_BUNDLE_STR, name);
        intent.putExtra(ConstantStr.KEY_BUNDLE_STR_1, pass);
        intent.putExtra(ConstantStr.KEY_BUNDLE_STR_2,phoneNum);
        intent.putExtra(ConstantStr.KEY_BUNDLE_STR_3,codeStr);
        startActivity(intent);
    }
}
