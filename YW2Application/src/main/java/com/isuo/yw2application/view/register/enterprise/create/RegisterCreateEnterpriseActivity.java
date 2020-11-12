package com.isuo.yw2application.view.register.enterprise.create;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.isuo.yw2application.R;
import com.isuo.yw2application.app.Yw2Application;
import com.isuo.yw2application.view.base.BaseActivity;

public class RegisterCreateEnterpriseActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutAndToolbar(R.layout.activity_register_create_enterprise, "注册企业");
        final EditText editText = findViewById(R.id.edit);
        findViewById(R.id.create).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editText.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    Yw2Application.getInstance().showToast("请输入企业名称");
                    return;
                }
                Yw2Application.getInstance().showToast("创建成功");
            }
        });
    }
}
