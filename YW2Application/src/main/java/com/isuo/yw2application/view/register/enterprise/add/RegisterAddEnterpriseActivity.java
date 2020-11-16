package com.isuo.yw2application.view.register.enterprise.add;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.isuo.yw2application.R;
import com.isuo.yw2application.app.Yw2Application;
import com.isuo.yw2application.common.ConstantInt;
import com.isuo.yw2application.mode.bean.User;
import com.isuo.yw2application.view.base.BaseActivity;
import com.sito.library.adapter.RVAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RegisterAddEnterpriseActivity extends BaseActivity implements RegisterAddEnterpriseContract.View {

    private EditText enterpriseNameEt;
    private ImageView cleanIv;
    private RecyclerView recyclerView;
    private List<User.CustomerBean> data = new ArrayList<>();
    private RegisterAddEnterpriseContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutAndToolbar(R.layout.activity_register_add_enterprise, "申请注册企业");
        enterpriseNameEt = findViewById(R.id.enterpriseNameEt);
        cleanIv = findViewById(R.id.clean_name_iv);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RVAdapter<User.CustomerBean> adapter = new RVAdapter<User.CustomerBean>(recyclerView, data, R.layout.item_enterprise_name) {
            @Override
            public void showData(ViewHolder vHolder, User.CustomerBean data, int position) {
                TextView nameTv = (TextView) vHolder.getView(R.id.nameTv);
                nameTv.setText(String.format("%s%s", String.valueOf(position + 1), data.getCustomerName()));
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new RVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                String text = RegisterAddEnterpriseActivity.this.data.get(position).getCustomerName();
                text = "是否申请加入" + text + "?";
                new MaterialDialog.Builder(RegisterAddEnterpriseActivity.this)
                        .content(text)
                        .negativeText("取消")
                        .positiveText("确定")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                JSONObject jsonObject = new JSONObject();
                                try {
                                    jsonObject.put("", RegisterAddEnterpriseActivity.this.data.get(position).getCustomerId());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                mPresenter.askCustomer(jsonObject);
                            }
                        }).show();
            }
        });
        etListener();
        new RegisterAddEnterprisePresenter(Yw2Application.getInstance().getRepositoryComponent().getRepository(), this);
    }

    private void etListener() {
        enterpriseNameEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String name = enterpriseNameEt.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    RegisterAddEnterpriseActivity.this.data.clear();
                    recyclerView.getAdapter().notifyDataSetChanged();
                    cleanIv.setVisibility(View.GONE);
                } else {
                    JSONObject jo = new JSONObject();
                    try {
                        jo.put("customerName", name);
                        jo.put("count", ConstantInt.PAGE_SIZE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    cleanIv.setVisibility(View.VISIBLE);
                    mPresenter.getEnterpriseCustomerList(jo);
                }
            }
        });
    }

    @Override
    public void showData(List<User.CustomerBean> list) {
        this.data.clear();
        this.data.addAll(list);
        this.recyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void joinAskSuccess() {
        Yw2Application.getInstance().showToast("申请成功");
        setResult(Activity.RESULT_OK);
        finish();
    }

    @Override
    public void joinAskError() {
        Yw2Application.getInstance().showToast("申请失败");
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void noData() {

    }

    @Override
    public void setPresenter(RegisterAddEnterpriseContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
