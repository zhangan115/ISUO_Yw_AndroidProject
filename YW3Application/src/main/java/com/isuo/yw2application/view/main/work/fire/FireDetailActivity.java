package com.isuo.yw2application.view.main.work.fire;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.isuo.yw2application.R;
import com.isuo.yw2application.common.ConstantStr;
import com.isuo.yw2application.mode.fire.FireBean;
import com.isuo.yw2application.view.base.BaseActivity;
import com.sito.library.utils.DataUtil;

public class FireDetailActivity extends BaseActivity {

    TextView[] textViews = new TextView[16];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        FireBean fireBean = (FireBean) intent.getSerializableExtra(ConstantStr.KEY_BUNDLE_OBJECT);
        setLayoutAndToolbar(R.layout.activity_fire_detail, "小空间智能灭火装置管理系统");
        textViews[0] = findViewById(R.id.tv_1);
        textViews[1] = findViewById(R.id.tv_2);
        textViews[2] = findViewById(R.id.tv_3);
        textViews[3] = findViewById(R.id.tv_4);
        textViews[4] = findViewById(R.id.tv_5);
        textViews[5] = findViewById(R.id.tv_6);
        textViews[6] = findViewById(R.id.tv_7);
        textViews[7] = findViewById(R.id.tv_8);
        textViews[8] = findViewById(R.id.tv_9);
        textViews[9] = findViewById(R.id.tv_10);
        textViews[10] = findViewById(R.id.tv_11);
        textViews[11] = findViewById(R.id.tv_12);
        textViews[12] = findViewById(R.id.tv_13);
        textViews[13] = findViewById(R.id.tv_14);
        textViews[14] = findViewById(R.id.tv_15);
        textViews[15] = findViewById(R.id.tv_16);

        TextView equipmentTv = findViewById(R.id.tv_equipment_name);
        equipmentTv.setText("小空间智能灭火装置");
        textViews[0].setText(fireBean.getRoomName());
        textViews[1].setText(fireBean.getTwoRegion());
        textViews[2].setText(fireBean.getThreeRegion());
        textViews[3].setText(fireBean.getEquipmentName());
        textViews[4].setText(fireBean.getEquipmentNumber());
//        textViews[5].setText(fireBean.getEquipmentModel());
        textViews[6].setText(fireBean.getEquipmentModel());
        textViews[7].setText(String.valueOf(fireBean.getCount()));
        textViews[8].setText(fireBean.getCompanyNumber());
        if (fireBean.getPattern() == 2) {
            textViews[9].setText("线下");
        } else if (fireBean.getPattern() == 1){
            textViews[9].setText("线上");
        }
        if (fireBean.getState() == 1) {
            textViews[10].setText("正常");
        } else if (fireBean.getState() == 2){
            textViews[10].setText("触发");
        }
        textViews[11].setText(DataUtil.timeFormat(fireBean.getManufactureTime(), "yyyy-MM"));
        textViews[12].setText(DataUtil.timeFormat(fireBean.getRemindTime(), "yyyy-MM"));
        textViews[13].setText(DataUtil.timeFormat(fireBean.getExpireTime(), "yyyy-MM"));

        textViews[14].setText(fireBean.getRemark());
        textViews[15].setText(fireBean.getEquipmentPerson());
    }
}
