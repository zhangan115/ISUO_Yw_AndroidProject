package com.isuo.yw2application.view.main.data.fault_report;

import android.support.annotation.Nullable;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.isuo.yw2application.mode.bean.ChartData;

import java.util.List;

/**
 * x轴显示
 * Created by zhangan on 2017-05-19.
 */

class ChartYFormatter implements IAxisValueFormatter {

    @Nullable
    private List<ChartData> dataValues;

    ChartYFormatter(@Nullable List<ChartData> dataValues) {
        this.dataValues = dataValues;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return String.valueOf((int)(value));
    }
}
