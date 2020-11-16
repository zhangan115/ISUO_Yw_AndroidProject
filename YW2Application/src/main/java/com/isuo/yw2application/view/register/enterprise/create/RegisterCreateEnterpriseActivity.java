package com.isuo.yw2application.view.register.enterprise.create;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.isuo.yw2application.R;
import com.isuo.yw2application.app.Yw2Application;
import com.isuo.yw2application.view.base.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterCreateEnterpriseActivity extends BaseActivity implements RegisterCreateEnterpriseContract.View {
    private RegisterCreateEnterpriseContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutAndToolbar(R.layout.activity_register_create_enterprise, "注册企业");
        new RegisterCreateEnterprisePresenter(Yw2Application.getInstance().getRepositoryComponent().getRepository(), this);
        final EditText editText = findViewById(R.id.edit);
        findViewById(R.id.create).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name = editText.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    Yw2Application.getInstance().showToast("请输入企业名称");
                    return;
                }
                new MaterialDialog.Builder(RegisterCreateEnterpriseActivity.this)
                        .content("确实使用该名称来创建新的企业？")
                        .negativeText("取消")
                        .positiveText("确定")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                JSONObject jsonObject = new JSONObject();
                                try {
                                    jsonObject.put("customerName", name);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                mPresenter.createEnterpriseCustomer(jsonObject);
                            }
                        })
                        .show();
            }
        });
    }

    @Override
    public void createSuccess() {
        Yw2Application.getInstance().showToast("创建成功");

    }

    @Override
    public void createError() {

    }

    @Override
    public void setPresenter(RegisterCreateEnterpriseContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
