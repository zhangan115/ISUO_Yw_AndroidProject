package com.isuo.yw2application.view.main.work.pay;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.isuo.yw2application.R;
import com.isuo.yw2application.app.Yw2Application;
import com.isuo.yw2application.mode.bean.PayMenuBean;
import com.isuo.yw2application.view.base.BaseActivity;
import com.isuo.yw2application.widget.PayBottomView;
import com.isuo.yw2application.widget.PayContentView;
import com.lxj.xpopup.XPopup;
import com.sito.library.utils.DisplayUtil;

import org.json.JSONObject;

import java.text.MessageFormat;
import java.util.List;


public class PayActivity extends BaseActivity implements PayContract.View {

    PayContract.Presenter mPresenter;
    private PayMenuBean currentMenu;
    private Button btn_buy;
    private LinearLayout layoutContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutAndToolbar(R.layout.activity_pay, "套餐选择");
        new PayPresenter(Yw2Application.getInstance().getRepositoryComponent().getRepository(), this);
        btn_buy = findViewById(R.id.btn_buy);
        btn_buy.setText("未选择套餐");
        btn_buy.setBackground(findDrawById(R.drawable.bg_btn_gray));
        layoutContent = findViewById(R.id.layoutContent);
        btn_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPayView();
            }
        });
        mPresenter.getPayList(new JSONObject());
    }

    private void showPayView() {
        if (currentMenu == null) return;
        new XPopup.Builder(PayActivity.this)
                .atView(btn_buy)
                .asCustom(new PayBottomView(PayActivity.this
                        , "支付信息:" + currentMenu.getMenuName(), "支付金额:" + currentMenu.getPrice() + "元"
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

    private PayContentView[] views = null;

    @Override
    public void showPayList(List<PayMenuBean> list) {
        views = new PayContentView[list.size()];
        layoutContent.removeAllViews();
        for (int i = 0; i < list.size(); i++) {
            final PayContentView view = new PayContentView(this);
            view.setLayoutParams(new LinearLayout.LayoutParams(DisplayUtil.dip2px(this, 160), LinearLayout.LayoutParams.WRAP_CONTENT));
            view.setTag(R.id.tag_position, i);
            view.setData(list.get(i));
            view.chooseListener = new PayContentView.OnChooseListener() {
                @Override
                public void chooseMenu(PayMenuBean bean) {
                    currentMenu = bean;
                    int position = (int) view.getTag(R.id.tag_position);
                    for (int j = 0; j < views.length; j++) {
                        views[j].setChooseState(j == position);
                    }
                    btn_buy.setText(MessageFormat.format("购买(已选{0}元{1})", currentMenu.getPrice(), currentMenu.getMenuName()));
                    btn_buy.setBackground(findDrawById(R.drawable.bg_btn_report));
                }
            };
            layoutContent.addView(view);
            views[i] = view;
        }
    }

    @Override
    public void paySuccess() {

    }

    @Override
    public void payFail() {

    }
}
