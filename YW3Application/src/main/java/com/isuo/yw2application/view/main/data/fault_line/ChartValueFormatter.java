package com.isuo.yw2application.view.main.data.fault_line;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

/**
 * x轴显示
 * Created by zhangan on 2017-05-19.
 */

class ChartValueFormatter implements IValueFormatter {

    @Override
    public String getFormattedValue(float v, Entry entry, int i, ViewPortHandler viewPortHandler) {
        int value = Float.valueOf(v).intValue();
        return String.valueOf(value);
    }
}
