package com.sito.customer.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CalendarView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;


import com.sito.customer.BuildConfig;
import com.sito.customer.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DatePick extends FrameLayout implements View.OnClickListener {

    private CalendarView calendarView;
    private LinearLayout ll_choose_hour_minuet;
    private TextView tv_year_month_day;
    private TextView tv_time;
    private View view_year_month_day, view_choose;
    private String yearMonthDayStr, hourStr = "00", minStr = "00";
    private int MODE = 0;
    private Calendar calendar;
    private NumberPicker hour_picker;
    private NumberPicker minuet_picker;
    private String[] hourPickArrays;
    private String[] minPickArrays;

    public DatePick(Context context) {
        super(context);
        init(context);
    }

    public DatePick(Context context, AttributeSet set) {
        super(context, set);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.layout_choose_calendar_view, this);
        calendarView = findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                if (calendar != null) {
                    calendar.set(year, month, dayOfMonth);
                    yearMonthDayStr = timeFormat(calendar.getTimeInMillis(), "yyyy年MM月dd日");
                }
                setYearMonthDayStr();
                MODE = 1;
                switchMode();
            }
        });
        ll_choose_hour_minuet = findViewById(R.id.ll_choose_hour_minuet);
        hour_picker = findViewById(R.id.hour_picker);
        minuet_picker = findViewById(R.id.minuet_picker);
        tv_year_month_day = findViewById(R.id.tv_year_month_day);
        tv_time = findViewById(R.id.tv_time);
        view_year_month_day = findViewById(R.id.view_year_month_day);
        view_choose = findViewById(R.id.view_choose);
        hour_picker.setMaxValue(23);
        hour_picker.setMinValue(0);
        hourPickArrays = new String[24];
        for (int i = 0; i < 24; i++) {
            if (i < 10) {
                hourPickArrays[i] = "0" + i;
            } else {
                hourPickArrays[i] = String.valueOf(i);
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            hour_picker.setDefaultFocusHighlightEnabled(false);
        }
        hour_picker.setDisplayedValues(hourPickArrays);
        hour_picker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        hour_picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                hourStr = hourPickArrays[newVal];
                setHourMinStr();
                if (calendar != null) {
                    calendar.set(Calendar.HOUR_OF_DAY, newVal);
                }
            }
        });
        minuet_picker.setMaxValue(59);
        minuet_picker.setMinValue(0);
        minPickArrays = new String[60];
        for (int i = 0; i < 60; i++) {
            if (i < 10) {
                minPickArrays[i] = "0" + i;
            } else {
                minPickArrays[i] = String.valueOf(i);
            }
        }
        minuet_picker.setDisplayedValues(minPickArrays);
        minuet_picker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        minuet_picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                minStr = minPickArrays[newVal];
                setHourMinStr();
                if (calendar != null) {
                    calendar.set(Calendar.MINUTE, newVal);
                }
            }
        });
        setHourMinStr();
        tv_year_month_day.setOnClickListener(this);
        tv_time.setOnClickListener(this);
        findViewById(R.id.tv_sure).setOnClickListener(this);
        findViewById(R.id.view_background).setOnClickListener(this);
        calendarView.setVisibility(VISIBLE);
        ll_choose_hour_minuet.setVisibility(GONE);
        view_choose.setVisibility(INVISIBLE);
        view_year_month_day.setVisibility(VISIBLE);
    }

    private void setYearMonthDayStr() {
        tv_year_month_day.setText(yearMonthDayStr);
    }

    private void setHourMinStr() {
        tv_time.setText(hourStr + ":" + minStr);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_background:
                hide();
                break;
            case R.id.tv_time:
                if (MODE == 1) {
                    return;
                }
                MODE = 1;
                switchMode();
                break;
            case R.id.tv_sure:
                confirmDate();
                break;
            case R.id.tv_year_month_day:
                if (MODE == 0) {
                    return;
                }
                MODE = 0;
                switchMode();
                break;
        }
    }

    public void hide() {
        Animation hideAnim = AnimationUtils.loadAnimation(getContext(), R.anim.background_hide);
        startAnimation(hideAnim);
        hideAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setVisibility(GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void confirmDate() {
        if (chooseTimeListener != null) {
            chooseTimeListener.onDataChange(calendar);
        }
        hide();
        if (BuildConfig.DEBUG) {
            Log.d("za", timeFormat(calendar.getTimeInMillis(), null));
        }
    }

    public void show(Calendar calendar, IChooseTimeListener listener) {
        this.calendar = calendar;
        chooseTimeListener = listener;
        calendarView.setDate(calendar.getTimeInMillis());
        yearMonthDayStr = timeFormat(calendar.getTimeInMillis(), "yyyy年MM月dd日");
        setYearMonthDayStr();
        Animation showAnim = AnimationUtils.loadAnimation(getContext(), R.anim.show_date_pick);
        Animation backAnim = AnimationUtils.loadAnimation(getContext(), R.anim.background_show);
        findViewById(R.id.view_background).startAnimation(backAnim);
        findViewById(R.id.constraintLayout).startAnimation(showAnim);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        hourStr = hourPickArrays[hour];
        setHourMinStr();
        minStr = minPickArrays[minute];
        setHourMinStr();
        hour_picker.setValue(hour);
        minuet_picker.setValue(minute);
        setVisibility(VISIBLE);
    }

    private IChooseTimeListener chooseTimeListener;

    public interface IChooseTimeListener {

        void onDataChange(Calendar calendar);

    }

    private void switchMode() {
        if (MODE == 0) {
            calendarView.setVisibility(VISIBLE);
            Animation showDayOver = AnimationUtils.loadAnimation(getContext(), R.anim.date_pick_day_over);
            Animation showTimeOver = AnimationUtils.loadAnimation(getContext(), R.anim.date_pick_time_over);
            showDayOver.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    view_choose.setVisibility(INVISIBLE);
                    view_year_month_day.setVisibility(VISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            showTimeOver.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    ll_choose_hour_minuet.setVisibility(GONE);
                    view_choose.setVisibility(INVISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            calendarView.startAnimation(showDayOver);
            ll_choose_hour_minuet.startAnimation(showTimeOver);
        } else {
            ll_choose_hour_minuet.setVisibility(VISIBLE);
            Animation showDay = AnimationUtils.loadAnimation(getContext(), R.anim.date_pick_day);
            Animation showTime = AnimationUtils.loadAnimation(getContext(), R.anim.date_pick_time);
            showDay.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    calendarView.setVisibility(GONE);
                    view_year_month_day.setVisibility(INVISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            showTime.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    view_choose.setVisibility(VISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            calendarView.startAnimation(showDay);
            ll_choose_hour_minuet.startAnimation(showTime);
        }
    }

    public static String timeFormat(long time, @Nullable String format) {
        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd HH:mm";
        }
        Date d = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        return sdf.format(d);
    }
}
