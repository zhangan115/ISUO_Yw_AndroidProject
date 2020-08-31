package com.sito.customer.widget;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TabHost;

import com.sito.customer.R;

public class DateRangePickerFragment extends DialogFragment implements View.OnClickListener {

    private OnDateRangeSelectedListener onDateRangeSelectedListener;

    private DatePicker startDatePicker;
    private DatePicker endDatePicker;
    boolean is24HourMode;

    public DateRangePickerFragment() {
    }

    public static DateRangePickerFragment newInstance(OnDateRangeSelectedListener callback, boolean is24HourMode) {
        DateRangePickerFragment dateRangePickerFragment = new DateRangePickerFragment();
        dateRangePickerFragment.initialize(callback, is24HourMode);
        return dateRangePickerFragment;
    }

    public void initialize(OnDateRangeSelectedListener callback,
                           boolean is24HourMode) {
        onDateRangeSelectedListener = callback;
        this.is24HourMode = is24HourMode;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.date_range_picker, container, true);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        TabHost tabHost = root.findViewById(R.id.tabHost);
        Button butSetDateRange = root.findViewById(R.id.but_set_time_range);
        startDatePicker = root.findViewById(R.id.start_date_picker);
        endDatePicker = root.findViewById(R.id.end_date_picker);
        butSetDateRange.setOnClickListener(this);
        root.findViewById(R.id.but_cancel).setOnClickListener(this);
        tabHost.findViewById(R.id.tabHost);
        tabHost.setup();
        TabHost.TabSpec startDatePage = tabHost.newTabSpec("start");
        startDatePage.setContent(R.id.start_date_group);
        startDatePage.setIndicator(getString(R.string.title_tab_start_date));

        TabHost.TabSpec endDatePage = tabHost.newTabSpec("end");
        endDatePage.setContent(R.id.end_date_group);
        endDatePage.setIndicator(getString(R.string.ttile_tab_end_date));

        tabHost.addTab(startDatePage);
        tabHost.addTab(endDatePage);
        return root;

    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() == null)
            return;
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onClick(View v) {
        dismiss();
        switch (v.getId()) {
            case R.id.but_cancel:
                break;
            case R.id.but_set_time_range:
                onDateRangeSelectedListener.onDateRangeSelected(startDatePicker.getDayOfMonth(), startDatePicker.getMonth(), startDatePicker.getYear(),
                        endDatePicker.getDayOfMonth(), endDatePicker.getMonth(), endDatePicker.getYear());
                break;
        }
    }

    public interface OnDateRangeSelectedListener {
        void onDateRangeSelected(int startDay, int startMonth, int startYear, int endDay, int endMonth, int endYear);
    }

}
