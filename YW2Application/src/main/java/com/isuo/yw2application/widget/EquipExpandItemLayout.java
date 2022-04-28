package com.isuo.yw2application.widget;

import android.content.Context;
import android.text.TextUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.isuo.yw2application.R;
import com.isuo.yw2application.mode.bean.equip.ExpandListBean;


/**
 * 对象详情
 * Created by zhangan on 2018/1/9.
 */

public class EquipExpandItemLayout extends LinearLayout {

    public EquipExpandItemLayout(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.layout_equip_expand_item, this);
    }

    public EquipExpandItemLayout setExpand(ExpandListBean bean) {
        ((TextView) (findViewById(R.id.tv_name))).setText(bean.getFieldName());
        StringBuilder sb = new StringBuilder();
        if (!TextUtils.isEmpty(bean.getValue())) {
            sb.append(bean.getValue());
            if (!TextUtils.isEmpty(bean.getFieldUnit())) {
                sb.append("(").append(bean.getFieldUnit()).append(")");
            }
        }
        if (!TextUtils.isEmpty(sb.toString())) {
            ((TextView) (findViewById(R.id.tv_value))).setText(sb.toString());
        }
        return this;
    }
}
