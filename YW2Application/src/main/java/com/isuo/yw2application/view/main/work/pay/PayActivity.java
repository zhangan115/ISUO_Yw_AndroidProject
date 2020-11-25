package com.isuo.yw2application.view.main.work.pay;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.isuo.yw2application.R;
import com.isuo.yw2application.view.base.BaseActivity;
import com.isuo.yw2application.widget.PayContentView;
import com.sito.library.utils.DisplayUtil;


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

            }
        });
        for (int i = 0; i < 5; i++) {
            PayContentView view = new PayContentView(this);
            view.setLayoutParams(new LinearLayout.LayoutParams(DisplayUtil.dip2px(this, 160), LinearLayout.LayoutParams.WRAP_CONTENT));
            layoutContent.addView(view);
        }
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
