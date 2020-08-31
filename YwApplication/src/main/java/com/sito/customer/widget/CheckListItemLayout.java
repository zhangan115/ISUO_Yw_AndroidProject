package com.sito.customer.widget;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sito.customer.R;
import com.sito.customer.mode.tools.bean.CheckListBean;

/**
 * check list
 * Created by zhangan on 2018/4/9.
 */

public class CheckListItemLayout extends LinearLayout implements View.OnClickListener {

    private CheckListBean checkListBean;
    private TextView tvName;
    private ImageView ivYes, ivNo;
    private boolean isYes, isNo;
    private Context context;

    public CheckListItemLayout(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        inflate(context, R.layout.item_check_list, this);
        tvName = findViewById(R.id.tvName);
        ivYes = findViewById(R.id.ivYes);
        ivNo = findViewById(R.id.ivNo);
        findViewById(R.id.llYes).setOnClickListener(this);
        findViewById(R.id.llNo).setOnClickListener(this);
    }

    public void setCheckListBean(CheckListBean checkListBean) {
        this.checkListBean = checkListBean;
        tvName.setText(checkListBean.getCheckName());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llNo:
                isNo = !isNo;
                isYes = false;
                changeState();
                break;
            case R.id.llYes:
                isYes = !isYes;
                isNo = false;
                changeState();
                break;
        }
    }

    private void changeState() {
        if (isYes) {
            ivYes.setImageDrawable(context.getResources().getDrawable(R.drawable.choose_select));
        } else {
            ivYes.setImageDrawable(context.getResources().getDrawable(R.drawable.choose_normal));
        }
        if (isNo) {
            ivNo.setImageDrawable(context.getResources().getDrawable(R.drawable.choose_select));
        } else {
            ivNo.setImageDrawable(context.getResources().getDrawable(R.drawable.choose_normal));
        }
        if (isYes) {
            checkListBean.setValue("1");
        } else if (isNo) {
            checkListBean.setValue("0");
        } else {
            checkListBean.setValue(null);
        }
    }
}
