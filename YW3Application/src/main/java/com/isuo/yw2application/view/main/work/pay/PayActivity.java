package com.isuo.yw2application.view.main.work.pay;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.isuo.yw2application.R;
import com.isuo.yw2application.app.Yw2Application;
import com.isuo.yw2application.mode.bean.PayMenuBean;
import com.isuo.yw2application.utils.WXPayUtils;
import com.isuo.yw2application.view.base.BaseActivity;
import com.isuo.yw2application.view.main.about.AboutActivity;
import com.isuo.yw2application.widget.PayBottomView;
import com.isuo.yw2application.widget.PayContentView;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.sito.library.utils.DisplayUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PayActivity extends BaseActivity implements PayContract.View {

    PayContract.Presenter mPresenter;
    private PayMenuBean currentMenu;
    private Button btn_buy;
    private LinearLayout layoutContent;
    private WeiXinSuccessBr weiXinSuccessBr;
    private WeiXinFailBr weiXinFailBr;
    private String weiXinOutTradeNo = "";
    private int AL_PAY_FLAG = 100;
    private PayMenuBean customerSetMenu;
    private BasePopupView popupView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutAndToolbar(R.layout.activity_pay, "套餐选择");
        new PayPresenter(Yw2Application.getInstance().getRepositoryComponent().getRepository(), this);
        btn_buy = findViewById(R.id.btn_buy);
//        btn_buy.setText("未选择套餐");
//        btn_buy.setBackground(findDrawById(R.drawable.bg_btn_gray));
        layoutContent = findViewById(R.id.layoutContent);
        btn_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPayView();
            }
        });
        weiXinSuccessBr = new WeiXinSuccessBr();
        registerReceiver(weiXinSuccessBr, new IntentFilter("pay_wei_xin_success"));
        weiXinFailBr = new WeiXinFailBr();
        registerReceiver(weiXinFailBr, new IntentFilter("pay_wei_xin_fail"));
        customerSetMenu = Yw2Application.getInstance().getCurrentUser().getCustomerSetMenu();
        mPresenter.getPayList(new JSONObject());
    }

    @Override
    public void setPresenter(PayContract.Presenter presenter) {
        mPresenter = presenter;
    }

    private void showPayView() {
        if (currentMenu == null) return;
        startActivity(new Intent(PayActivity.this, AboutActivity.class));
//        if (currentMenu.getMenuName().equals("定制版")) {
//            startActivity(new Intent(PayActivity.this, AboutActivity.class));
//            return;
//        }
//        popupView = new XPopup.Builder(PayActivity.this)
//                .atView(btn_buy)
//                .asCustom(new PayBottomView(PayActivity.this
//                        , "支付信息:" + currentMenu.getMenuName(), "支付金额:" + String.format("%.0f", currentMenu.getPrice()) + "元"
//                        , new PayBottomView.PayClickListener() {
//                    @Override
//                    public void onPay(int type) {
//                        JSONObject jsonObject = new JSONObject();
//                        try {
//                            jsonObject.put("setMenuId", PayActivity.this.currentMenu.getId());
//                            jsonObject.put("payType", type);
//                            jsonObject.put("year", 1);
//                            jsonObject.put("appType", "android");
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        if (type == 0) {
//                            mPresenter.getWeiXinPayInfo(jsonObject);
//                        } else {
//                            mPresenter.getAlPayInfo(jsonObject);
//                        }
//                    }
//                }))
//                .show();
    }

    private PayContentView[] views = null;
    private List<PayMenuBean> payList = new ArrayList<>();

    @Override
    public void showPayList(List<PayMenuBean> list) {
        this.payList = list;
        views = new PayContentView[list.size()];
        layoutContent.removeAllViews();
        for (int i = 0; i < list.size(); i++) {
            final PayContentView view = new PayContentView(this);
            view.setLayoutParams(new LinearLayout.LayoutParams(DisplayUtil.dip2px(this, 160), LinearLayout.LayoutParams.WRAP_CONTENT));
            view.setTag(R.id.tag_position, i);
            view.setData(list.get(i));
            view.currentTv.setVisibility(list.get(i).getId() == customerSetMenu.getMenuId() ? View.VISIBLE : View.GONE);
            view.chooseListener = new PayContentView.OnChooseListener() {
                @Override
                public void chooseMenu(PayMenuBean bean) {
                    currentMenu = bean;
                    int position = (int) view.getTag(R.id.tag_position);
                    for (int j = 0; j < views.length; j++) {
                        views[j].setChooseState(j == position);
                    }
//                    if (currentMenu.getMenuName().equals("定制版")) {
//                        btn_buy.setText("请联系客服");
//                        btn_buy.setBackground(findDrawById(R.drawable.bg_btn_report));
//                        return;
//                    }
//                    if (customerSetMenu != null && customerSetMenu.getMenuId() == currentMenu.getId()) {
//                        btn_buy.setText("无法购买");
//                        btn_buy.setBackground(findDrawById(R.drawable.bg_btn_gray));
//                    } else {
//                        btn_buy.setText(MessageFormat.format("升级(已选{0}元{1})", String.format("%.0f", currentMenu.getPrice()), currentMenu.getMenuName()));
//                        btn_buy.setBackground(findDrawById(R.drawable.bg_btn_report));
//                    }
                }
            };
            layoutContent.addView(view);
            views[i] = view;
        }
        btn_buy.setText("请联系客服");
        btn_buy.setBackground(findDrawById(R.drawable.bg_btn_report));
    }

    @Override
    public void payAli(final String payMessage) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                PayTask alPay = new PayTask(PayActivity.this);
                Map<String, String> result = alPay.payV2(payMessage, true);
                Message msg = new Message();
                msg.what = AL_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == AL_PAY_FLAG) {
                PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                String resultStatus = payResult.getResultStatus();
                if (TextUtils.equals(resultStatus, "9000")) {
                    JSONObject json;
                    try {
                        json = new JSONObject(resultInfo);
                        AlPaySuccessCallBack callBack = new Gson().fromJson(json.toString(), AlPaySuccessCallBack.class);
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("outTradeNo", callBack.getAlipay_trade_app_pay_response().getOut_trade_no());
                        jsonObject.put("trade_no", callBack.getAlipay_trade_app_pay_response().getTrade_no());
                        mPresenter.paySuccess(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Yw2Application.getInstance().showToast("支付失败");
                }
            }
        }
    };

    @Override
    public void payWeiXin(WeiXinPayBean payMessage) {
        weiXinOutTradeNo = payMessage.getOutTradeNo();
        WXPayUtils.WXPayBuilder builder = new WXPayUtils.WXPayBuilder();
        builder.setAppId(payMessage.getAppid())
                .setPartnerId(payMessage.getPartnerid())
                .setPrepayId(payMessage.getPrepayid())
                .setPackageValue(payMessage.getPackages())
                .setNonceStr(payMessage.getNoncestr())
                .setTimeStamp(payMessage.getTimestamp())
                .setSign(payMessage.getSign())
                .build().toWXPayNotSign(this);
    }

    @Override
    public void paySuccess() {
        customerSetMenu = Yw2Application.getInstance().getCurrentUser().getCustomerSetMenu();
        currentMenu = null;
        showPayList(this.payList);
    }

    @Override
    public void hidePopView() {
        popupView.doDismissAnimation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (weiXinFailBr != null) {
                unregisterReceiver(weiXinFailBr);
            }
            if (weiXinSuccessBr != null) {
                unregisterReceiver(weiXinSuccessBr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class WeiXinSuccessBr extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //微信支付成功
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("outTradeNo", weiXinOutTradeNo);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mPresenter.paySuccess(jsonObject);
        }
    }

    class WeiXinFailBr extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //微信支付失败
        }
    }
}
