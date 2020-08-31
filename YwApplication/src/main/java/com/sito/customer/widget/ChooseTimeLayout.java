package com.sito.customer.widget;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.sito.customer.R;
import com.sito.library.utils.CalendarUtil;
import com.sito.library.utils.DataUtil;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * 选择日期
 * Created by zhangan on 2018/4/17.
 */

public class ChooseTimeLayout extends LinearLayout implements View.OnClickListener {

    private Context context;
    private TextView[] textViews;
    private int choosePosition;
    private IChooseTime iChooseTime;
    private Calendar calendar;
    private FragmentManager fragmentManager;
    String startTime = null, endTime = null, title;

    public ChooseTimeLayout(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.layout_choose_time, this);
        this.context = context;
        textViews = new TextView[7];
        textViews[0] = findViewById(R.id.tvToday);
        textViews[1] = findViewById(R.id.tvYesterday);
        textViews[2] = findViewById(R.id.tvThisWeek);
        textViews[3] = findViewById(R.id.tvLastWeek);
        textViews[4] = findViewById(R.id.tvThisMonth);
        textViews[5] = findViewById(R.id.tvLastMonth);
        textViews[6] = findViewById(R.id.tvChooseTime);

        textViews[0].setTag(0);
        textViews[1].setTag(1);
        textViews[2].setTag(2);
        textViews[3].setTag(3);
        textViews[4].setTag(4);
        textViews[5].setTag(5);
        textViews[6].setTag(6);

        calendar = Calendar.getInstance(TimeZone.getDefault());
        startTime = getDataStr(calendar);
        endTime = getDataStr(calendar);
        title = "今日";
        for (TextView textView : textViews) {
            textView.setOnClickListener(this);
        }
    }

    public void setChooseTimeListener(IChooseTime chooseTime, FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
        this.iChooseTime = chooseTime;
        iChooseTime.onTimeChange(startTime, endTime, title);
    }

    @Override
    public void onClick(View v) {
        if (iChooseTime == null) {
            return;
        }
        choosePosition = (int) v.getTag();
        calendar = Calendar.getInstance(TimeZone.getDefault());
        switch (v.getId()) {
            case R.id.tvToday:
                startTime = getDataStr(calendar);
                endTime = getDataStr(calendar);
                title = "今日";
                break;
            case R.id.tvYesterday:
                calendar.add(Calendar.DATE, -1);
                startTime = getDataStr(calendar);
                endTime = getDataStr(calendar);
                title = "昨日";
                break;
            case R.id.tvThisWeek:
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                startTime = getDataStr(calendar);
                calendar = Calendar.getInstance(TimeZone.getDefault());
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
                endTime = getDataStr(calendar);
                title = "本周";
                break;
            case R.id.tvLastWeek:
                int week = calendar.get(Calendar.DAY_OF_WEEK);
                calendar.add(Calendar.DAY_OF_WEEK, -week);
                endTime = getDataStr(calendar);
                calendar.add(Calendar.DAY_OF_WEEK, -6);
                startTime = getDataStr(calendar);
                title = "上周";
                break;
            case R.id.tvThisMonth:
                calendar.add(Calendar.MONTH, 0);
                calendar.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天
                startTime = getDataStr(calendar);
                calendar = Calendar.getInstance(TimeZone.getDefault());
                calendar.add(Calendar.MONTH, 1);
                calendar.set(Calendar.DAY_OF_MONTH, 0);
                endTime = getDataStr(calendar);
                title = "本月";
                break;
            case R.id.tvLastMonth:
                calendar.add(Calendar.MONTH, -1);
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                startTime = getDataStr(calendar);
                calendar = Calendar.getInstance(TimeZone.getDefault());
                calendar.add(Calendar.MONTH, 0);
                calendar.set(Calendar.DAY_OF_MONTH, 0);
                endTime = getDataStr(calendar);
                title = "上月";
                break;
            case R.id.tvChooseTime:
                calendar = Calendar.getInstance(TimeZone.getDefault());
                if (fragmentManager == null) {
                    return;
                }
                DateRangePickerFragment dateRangePickerFragment = DateRangePickerFragment.newInstance(new DateRangePickerFragment.OnDateRangeSelectedListener() {
                    @Override
                    public void onDateRangeSelected(int startDay, int startMonth, int startYear, int endDay, int endMonth, int endYear) {
                        calendar.set(startYear, startMonth, startDay);
                        startTime = getDataStr(calendar);
                        calendar.set(endYear, endMonth, endDay);
                        endTime = getDataStr(calendar);
                        textViews[6].setText(startTime + "-" + endTime);
                        title = startTime + "至" + endTime;
                        iChooseTime.onTimeChange(startTime, endTime, title);
                        refreshUi();
                    }
                }, false);
                dateRangePickerFragment.show(fragmentManager, "datePicker");
                break;
        }
        if (choosePosition != 6) {
            iChooseTime.onTimeChange(startTime, endTime, title);
            refreshUi();
        }
    }

    private void refreshUi() {
        for (int i = 0; i < textViews.length; i++) {
            if (i == choosePosition) {
                textViews[i].setTextColor(this.context.getResources().getColor(R.color.colorWhite));
                textViews[i].setBackground(this.context.getResources().getDrawable(R.drawable.bg_bt_blue));
            } else {
                textViews[i].setTextColor(this.context.getResources().getColor(R.color.color2B2B2B));
                textViews[i].setBackground(this.context.getResources().getDrawable(R.drawable.bg_btn_gray));
            }
        }
    }

    public interface IChooseTime {

        void onTimeChange(String startTime, String endTime, String title);
    }

    private String getDataStr(Calendar calendar) {
        return DataUtil.timeFormat(calendar.getTimeInMillis(), "yyyy-MM-dd");
    }
}
