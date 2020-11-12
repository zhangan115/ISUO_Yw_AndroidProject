package com.isuo.yw2application.view.register.enterprise;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.isuo.yw2application.R;
import com.isuo.yw2application.common.ConstantStr;
import com.isuo.yw2application.view.base.BaseActivity;
import com.isuo.yw2application.view.register.enterprise.add.RegisterAddEnterpriseActivity;
import com.isuo.yw2application.view.register.enterprise.create.RegisterCreateEnterpriseActivity;

public class RegisterEnterpriseActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutAndToolbar(R.layout.activity_register_enterprise, "注册提示");
        String name = getIntent().getStringExtra(ConstantStr.KEY_BUNDLE_STR);
        String pass = getIntent().getStringExtra(ConstantStr.KEY_BUNDLE_STR_1);
        String phoneNum = getIntent().getStringExtra(ConstantStr.KEY_BUNDLE_STR_2);
        String codeStr = getIntent().getStringExtra(ConstantStr.KEY_BUNDLE_STR_3);

        findViewById(R.id.tv_add).setOnClickListener(this);
        findViewById(R.id.tv_create).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.tv_add:
                startActivityForResult(new Intent(this, RegisterAddEnterpriseActivity.class),100);
                break;
            case R.id.tv_create:
                startActivityForResult(new Intent(this, RegisterCreateEnterpriseActivity.class),101);
                break;
        }
    }
}
