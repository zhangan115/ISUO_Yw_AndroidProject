package com.isuo.yw2application.view.main.work.pay;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.isuo.yw2application.R;
import com.isuo.yw2application.view.base.BaseActivity;
import com.isuo.yw2application.widget.PayBottomView;
import com.isuo.yw2application.widget.PayContentView;
import com.lxj.xpopup.XPopup;
import com.sito.library.utils.DisplayUtil;

import org.json.JSONObject;


public class PayActivity extends BaseActivity implements PayContract.View {

    PayContract.Presenter mPresenter;

    private Button btn_buy;
    private LinearLayout layoutContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutAndToolbar(R.layout.activity_pay, "套餐选择");
        btn_buy = findViewById(R.id.btn_buy);
        layoutContent = findViewById(R.id.layoutContent);
        btn_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPayView();
            }
        });
        for (int i = 0; i < 5; i++) {
            PayContentView view = new PayContentView(this);
            view.setLayoutParams(new LinearLayout.LayoutParams(DisplayUtil.dip2px(this, 160), LinearLayout.LayoutParams.WRAP_CONTENT));
            layoutContent.addView(view);
        }
    }

    private void showPayView() {
        new XPopup.Builder(PayActivity.this)
                .atView(btn_buy)
                .asCustom(new PayBottomView(PayActivity.this
                        , "支付信息:小梭优维免费版0元套餐", "支付金额:0元"
                        , new PayBottomView.PayClickListener() {
                    @Override
                    public void onPay(int type) {
                        mPresenter.pay(new JSONObject());
                    }
                }))
                .show();
    }

    @Override
    public void setPresenter(PayContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showPayList() {

    }

    @Override
    public void paySuccess() {

    }

    @Override
    public void payFail() {

    }
}
