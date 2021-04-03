package com.isuo.yw2application.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.isuo.yw2application.R;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;

public class PayBottomView extends BottomPopupView {

    ImageView image1,image2;
    TextView text1,text2;
    String content1,content2;

    PayClickListener listener;
    int payType = 0; // 0 微信 1 支付宝

    public PayBottomView(@NonNull Context context) {
        super(context);
    }
    public PayBottomView(@NonNull Context context,String content1, String content2,PayClickListener listener) {
        super(context);
        this.listener = listener;
        this.content1 = content1;
        this.content2 = content2;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        image1 = findViewById(R.id.image1);
        image2 = findViewById(R.id.image2);
        text1 = findViewById(R.id.payText1);
        text2 = findViewById(R.id.payText2);
        text1.setText(content1);
        text2.setText(content2);
        findViewById(R.id.payBtn).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onPay(payType);
                }
            }
        });
        findViewById(R.id.wxItem).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                payType = 0;
                image1.setImageDrawable(getResources().getDrawable(R.drawable.sos_choose_selected));
                image2.setImageDrawable(getResources().getDrawable(R.drawable.sos_choose_normal));
            }
        });
        findViewById(R.id.aLItem).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                payType = 1;
                image1.setImageDrawable(getResources().getDrawable(R.drawable.sos_choose_normal));
                image2.setImageDrawable(getResources().getDrawable(R.drawable.sos_choose_selected));
            }
        });
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.bottom_layout_pay;
    }

    @Override
    protected int getMaxHeight() {
        return (int) (XPopupUtils.getWindowHeight(getContext()) * .85f);
    }

    public interface PayClickListener {
        void onPay(int type);
    }
}
