package com.isuo.yw2application.utils;


import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.isuo.yw2application.R;
import com.isuo.yw2application.widget.MyNumberPicker;
import com.sito.library.utils.DataUtil;
import com.sito.library.utils.DisplayUtil;

import java.util.Calendar;
import java.util.TimeZone;

public class ChooseDateDialog extends Dialog {

    private Context context;
    private int style;

    private MyNumberPicker np1, np2, np3, np4, np5;
    private boolean isPickYear, isPickMonth, isPickDay;
    private int year, month, day, hour, minute;
    private Calendar minDate, maxDate, currentDate;
    private TextView dateText, titleText;

    public ChooseDateDialog(Context context) {
        super(context);
        this.context = context;
    }

    public ChooseDateDialog(Context context, int style) {
        super(context);
        this.context = context;
        this.style = style;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.choose_date_dialog);
        if (currentDate == null) {
            currentDate = Calendar.getInstance(TimeZone.getDefault());
        }
        titleText = findViewById(R.id.titleText);
        dateText = findViewById(R.id.dateText);
        Resources resources = this.getContext().getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        int width = dm.widthPixels - DisplayUtil.dip2px(getContext(), 30);
        LinearLayout dialog_content = findViewById(R.id.dialog_content);
        dialog_content.setMinimumWidth(width);
        np1 = findViewById(R.id.np1);
        np2 = findViewById(R.id.np2);
        np3 = findViewById(R.id.np3);
        np4 = findViewById(R.id.np4);
        np5 = findViewById(R.id.np5);
        if (isPickYear) {
            np2.setVisibility(View.GONE);
            np3.setVisibility(View.GONE);
            np4.setVisibility(View.GONE);
            np5.setVisibility(View.GONE);
        }
        if (isPickMonth) {
            np3.setVisibility(View.GONE);
            np4.setVisibility(View.GONE);
            np5.setVisibility(View.GONE);
        }
        if (isPickDay) {
            np4.setVisibility(View.GONE);
            np5.setVisibility(View.GONE);
        }

        year = currentDate.get(Calendar.YEAR);
        month = currentDate.get(Calendar.MONTH);
        day = currentDate.get(Calendar.DAY_OF_MONTH);
        hour = currentDate.get(Calendar.HOUR_OF_DAY);
        minute = currentDate.get(Calendar.MINUTE);
        if (!TextUtils.isEmpty(titleTextStr)) {
            titleText.setVisibility(View.VISIBLE);
            titleText.setText(titleTextStr);
        }
        np1.getChildAt(0).setFocusable(false);
        np1.getChildAt(0).setFocusableInTouchMode(false);
        np2.getChildAt(0).setFocusable(false);
        np2.getChildAt(0).setFocusableInTouchMode(false);
        np3.getChildAt(0).setFocusable(false);
        np3.getChildAt(0).setFocusableInTouchMode(false);
        np4.getChildAt(0).setFocusable(false);
        np4.getChildAt(0).setFocusableInTouchMode(false);
        np5.getChildAt(0).setFocusable(false);
        np5.getChildAt(0).setFocusableInTouchMode(false);

        if (minDate == null) {
            np1.setMinValue(currentDate.get(Calendar.YEAR) - 20);
        } else {
            np1.setMinValue(minDate.get(Calendar.YEAR));
        }
        if (maxDate == null) {
            np1.setMaxValue(currentDate.get(Calendar.YEAR) + 20);
        } else {
            np1.setMaxValue(maxDate.get(Calendar.YEAR));
        }
        np1.setValue(year);
        String[] disNp1Value = new String[np1.getMaxValue() - np1.getMinValue() + 1];
        for (int i = np1.getMinValue(); i <= np1.getMaxValue(); i++) {
            disNp1Value[i - np1.getMinValue()] = i + "年";
        }
        np1.setDisplayedValues(disNp1Value);
        np1.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

            @Override
            public void onValueChange(NumberPicker arg0, int arg1, int arg2) {
                if (year % 4 == 0
                        && year % 100 != 0
                        || year % 400 == 0) {
                    switch (month) {
                        case 0:
                        case 2:
                        case 4:
                        case 6:
                        case 7:
                        case 9:
                        case 11:
                            np3.setMaxValue(31);
                            np3.setMinValue(1);
                            break;
                        case 3:
                        case 5:
                        case 8:
                        case 10:
                            np3.setMaxValue(30);
                            np3.setMinValue(1);
                            break;
                        default:
                            np3.setMaxValue(29);
                            np3.setMinValue(1);
                            break;
                    }

                } else {
                    if (month == 0 || month == 2 || month == 4 || month == 6 || month == 7 || month == 9 || month == 11) {
                        np3.setMaxValue(31);
                        np3.setMinValue(1);
                    } else if (month == 3 || month == 5 || month == 8 || month == 10) {
                        np3.setMaxValue(30);
                        np3.setMinValue(1);
                    } else {
                        np3.setMaxValue(28);
                        np3.setMinValue(1);
                    }
                }
                dateChange();
            }
        });
        np2.setMinValue(1);
        np2.setMaxValue(12);
        String[] disNp2Value = new String[np2.getMaxValue() - np2.getMinValue() + 1];
        for (int i = np2.getMinValue(); i <= np2.getMaxValue(); i++) {
            disNp2Value[i - np2.getMinValue()] = i + "月";
        }
        np2.setDisplayedValues(disNp2Value);
        np2.setValue(month + 1);
        np2.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

            @Override
            public void onValueChange(NumberPicker arg0, int arg1, int arg2) {
                month = np2.getValue() - 1;
                switch (month) {
                    case 0:
                    case 2:
                    case 4:
                    case 6:
                    case 7:
                    case 9:
                    case 11:
                        np3.setMaxValue(31);
                        np3.setMinValue(1);
                        break;
                    case 3:
                    case 5:
                    case 8:
                    case 10:
                        np3.setMaxValue(30);
                        np3.setMinValue(1);
                        break;
                    default:
                        if (year % 4 == 0
                                && year % 100 != 0
                                || year % 400 == 0) {
                            np3.setMaxValue(29);
                            np3.setMinValue(1);
                        } else {
                            np3.setMaxValue(28);
                            np3.setMinValue(1);
                        }
                        break;
                }
                dateChange();
            }
        });
        np3.setMaxValue(31);
        np3.setMinValue(1);
        np3.setValue(day);
        String[] disNp3Value = new String[np3.getMaxValue() - np3.getMinValue() + 1];
        for (int i = np3.getMinValue(); i <= np3.getMaxValue(); i++) {
            if (i < 10) {
                disNp3Value[i - np3.getMinValue()] = "0" + i + "日";
            } else {
                disNp3Value[i - np3.getMinValue()] = i + "日";
            }
        }
        np3.setDisplayedValues(disNp3Value);
        np3.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

            @Override
            public void onValueChange(NumberPicker arg0, int arg1, int arg2) {
                dateChange();
            }
        });
        np4.setMaxValue(23);
        np4.setMinValue(0);
        np4.setValue(hour);
        String[] disNp4Value = new String[np4.getMaxValue() - np4.getMinValue() + 1];
        for (int i = np4.getMinValue(); i <= np4.getMaxValue(); i++) {
            if (i < 10) {
                disNp4Value[i - np4.getMinValue()] = "0" + i + "时";
            } else {
                disNp4Value[i - np4.getMinValue()] = i + "时";
            }
        }
        np4.setDisplayedValues(disNp4Value);
        np4.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

            @Override
            public void onValueChange(NumberPicker arg0, int arg1, int arg2) {
                dateChange();
            }
        });
        np5.setMaxValue(59);
        np5.setMinValue(0);
        np5.setValue(minute);
        String[] disNp5Value = new String[np5.getMaxValue() - np5.getMinValue() + 1];
        for (int i = np5.getMinValue(); i <= np5.getMaxValue(); i++) {
            if (i < 10) {
                disNp5Value[i - np5.getMinValue()] = "0" + i + "分";
            } else {
                disNp5Value[i - np5.getMinValue()] = i + "分";
            }
        }
        np5.setDisplayedValues(disNp5Value);
        np5.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

            @Override
            public void onValueChange(NumberPicker arg0, int arg1, int arg2) {
                dateChange();
            }
        });
        findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dismiss();
            }
        });
        findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (currentDate != null && listener != null) {
                    listener.onDate(currentDate);
                }
                dismiss();
            }
        });
        dateChange();
    }

    private void dateChange() {
        year = np1.getValue();
        month = np2.getValue() - 1;
        day = np3.getValue();
        hour = np4.getValue();
        minute = np5.getValue();
        if (currentDate != null) {
            currentDate.set(year, month, day, hour, minute);
            String showTime;
            if (isPickYear) {
                showTime = DataUtil.timeFormat(currentDate.getTimeInMillis(), "yyyy");
            } else if (isPickMonth) {
                showTime = DataUtil.timeFormat(currentDate.getTimeInMillis(), "yyyy年MM月");
            } else if (isPickDay) {
                showTime = DataUtil.timeFormat(currentDate.getTimeInMillis(), "yyyy年MM月dd日");
            } else {
                showTime = DataUtil.timeFormat(currentDate.getTimeInMillis(), "yyyy年MM月dd日 HH:mm");
            }
            dateText.setText(showTime);
        }
    }

    public ChooseDateDialog pickYear() {
        isPickYear = true;
        return this;
    }

    public ChooseDateDialog pickMonth() {
        isPickMonth = true;
        return this;
    }

    public ChooseDateDialog pickDay() {
        isPickDay = true;
        return this;
    }

    public ChooseDateDialog setCurrent(Calendar currentDate) {
        this.currentDate = currentDate;
        return this;
    }

    public ChooseDateDialog setMinDate(Calendar minDate) {
        this.minDate = minDate;
        return this;
    }

    public ChooseDateDialog setMaxDate(Calendar maxDate) {
        this.maxDate = maxDate;
        return this;
    }

    private String titleTextStr;

    public ChooseDateDialog setTitleText(String text) {
        this.titleTextStr = text;
        return this;
    }

    // 对话框按钮监听事件
    private OnDateChooseListener listener;

    public ChooseDateDialog setResultListener(OnDateChooseListener listener) {
        this.listener = listener;
        return this;
    }

    public interface OnDateChooseListener {
        void onDate(Calendar calendar);
    }

    @Override
    public void show() {
        super.show();
    }
}
