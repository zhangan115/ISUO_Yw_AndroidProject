package com.isuo.yw2application.view.main.work.pay;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.isuo.yw2application.R;
import com.isuo.yw2application.app.Yw2Application;
import com.isuo.yw2application.mode.bean.PayMenuBean;
import com.isuo.yw2application.utils.WXPayUtils;
import com.isuo.yw2application.view.base.BaseActivity;
import com.isuo.yw2application.widget.PayBottomView;
import com.isuo.yw2application.widget.PayContentView;
import com.lxj.xpopup.XPopup;
import com.orhanobut.logger.Logger;
import com.sito.library.utils.DisplayUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.MessageFormat;
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
        weiXinSuccessBr = new WeiXinSuccessBr();
        registerReceiver(weiXinSuccessBr, new IntentFilter("pay_wei_xin_success"));
        weiXinFailBr = new WeiXinFailBr();
        registerReceiver(weiXinFailBr, new IntentFilter("pay_wei_xin_fail"));
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
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("setMenuId",PayActivity.this.currentMenu.getId());
                            jsonObject.put("payType",type);
                            jsonObject.put("year",1);
                            jsonObject.put("appType","android");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        mPresenter.getPayInfo(jsonObject);
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
                    btn_buy.setText(MessageFormat.format("升级(已选{0}元{1})", currentMenu.getPrice(), currentMenu.getMenuName()));
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

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == AL_PAY_FLAG) {
                PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                String resultStatus = payResult.getResultStatus();
                // 判断resultStatus 为9000则代表支付成功
                if (TextUtils.equals(resultStatus, "9000")) {
                    // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
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
                    // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                    Logger.d(resultInfo);
                    Logger.d(resultStatus);
                    Yw2Application.getInstance().showToast("支付失败");
                }
            }
        }
    };

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
