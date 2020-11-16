package com.isuo.yw2application.view.register.enterprise;

import android.app.Activity;
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
        setLayoutAndToolbar(R.layout.activity_register_enterprise, "加入企业");


        findViewById(R.id.tv_add).setOnClickListener(this);
        findViewById(R.id.tv_create).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_add:
                Intent intent1 = new Intent(this, RegisterAddEnterpriseActivity.class);
                startActivityForResult(intent1, 100);
                break;
            case R.id.tv_create:
                Intent intent2 = new Intent(this, RegisterCreateEnterpriseActivity.class);
                startActivityForResult(intent2, 101);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 100) {
                useAnimToBack = false;
                setResult(Activity.RESULT_OK);
                finish();
                overridePendingTransition(0, 0);
            } else if (requestCode == 101) {
                useAnimToBack = false;
                setResult(Activity.RESULT_OK);
                finish();
                overridePendingTransition(0, 0);
            }
        }
    }
}
