package com.sito.evpro.inspection.view.repair;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sito.evpro.inspection.R;
import com.sito.evpro.inspection.app.InspectionApp;
import com.sito.evpro.inspection.view.BaseActivity;
import com.sito.evpro.inspection.view.repair.increment.IncrementFragment;
import com.sito.evpro.inspection.view.repair.increment.IncrementPresenter;
import com.sito.evpro.inspection.view.repair.inspection.InspectionFragment;
import com.sito.evpro.inspection.view.repair.inspection.InspectionPresenter;
import com.sito.evpro.inspection.view.repair.overhaul.OverhaulFragment;
import com.sito.evpro.inspection.view.repair.overhaul.OverhaulPresenter;
import com.sito.library.utils.CalendarUtil;
import com.wdullaer.materialdatetimepicker.date.DatePickerView;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 日常检修
 * Created by zhangan on 2017-06-22.
 */

public class RepairActivity extends BaseActivity implements DatePickerView.OnDateSetListener {

    private List<IRepairDataChangeListener> listeners = new ArrayList<>();
    private String mDate;
    private LinearLayout mChooseDayLayout, mAddDatePickLayout;
    private DatePickerView mDatePickerView;
    private TextView mYearTv, mMonthTv, mDayTv;
    private TextView[] dayTvs = new TextView[7];
    private LinearLayout[] dayOfWeekLayout = new LinearLayout[7];
    private List<Date> dateList = new ArrayList<>();
    private Calendar mCurrentDay;

    public void addChangeListeners(IRepairDataChangeListener listener) {
        if (listeners.contains(listener)) {
            return;
        }
        listeners.add(listener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar == null) {
            return;
        }
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setDisplayShowTitleEnabled(false);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolBarClick();
            }
        });
        findViewById(R.id.ll_choose_month_day).setOnClickListener(this);
        mYearTv = (TextView) findViewById(R.id.tv_year);
        mMonthTv = (TextView) findViewById(R.id.tv_month);
        mDayTv = (TextView) findViewById(R.id.tv_day);
        dayTvs[0] = (TextView) findViewById(R.id.tv_1);
        dayTvs[1] = (TextView) findViewById(R.id.tv_2);
        dayTvs[2] = (TextView) findViewById(R.id.tv_3);
        dayTvs[3] = (TextView) findViewById(R.id.tv_4);
        dayTvs[4] = (TextView) findViewById(R.id.tv_5);
        dayTvs[5] = (TextView) findViewById(R.id.tv_6);
        dayTvs[6] = (TextView) findViewById(R.id.tv_7);
        dayOfWeekLayout[0] = (LinearLayout) findViewById(R.id.ll_monday);
        dayOfWeekLayout[1] = (LinearLayout) findViewById(R.id.ll_tuesday);
        dayOfWeekLayout[2] = (LinearLayout) findViewById(R.id.ll_wednesday);
        dayOfWeekLayout[3] = (LinearLayout) findViewById(R.id.ll_thursday);
        dayOfWeekLayout[4] = (LinearLayout) findViewById(R.id.ll_friday);
        dayOfWeekLayout[5] = (LinearLayout) findViewById(R.id.ll_saturday);
        dayOfWeekLayout[6] = (LinearLayout) findViewById(R.id.ll_sunday);
        for (LinearLayout layout : dayOfWeekLayout) {
            layout.setOnClickListener(this);
        }
        mCurrentDay = Calendar.getInstance(Locale.CHINA);
        getDate(mCurrentDay.get(Calendar.YEAR), mCurrentDay.get(Calendar.MONTH) + 1, mCurrentDay.get(Calendar.DAY_OF_MONTH));
        dateList = CalendarUtil.getDaysOfWeek(mCurrentDay.getTime());
        mChooseDayLayout = (LinearLayout) findViewById(R.id.ll_choose_day);
        mAddDatePickLayout = (LinearLayout) findViewById(R.id.ll_choose_day_content);
        mDatePickerView = DatePickerView.newInstance(this, this);
        mAddDatePickLayout.addView(mDatePickerView);
        setDayToView(false);
        findViewById(R.id.ll_choose_day_empty).setOnClickListener(this);
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        ViewPager mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(4);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        mViewPager.addOnPageChangeListener(pageChangeListener);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slideinleft, R.anim.slideoutright);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_choose_month_day:
                if (mChooseDayLayout.getVisibility() == View.GONE) {
                    mChooseDayLayout.setVisibility(View.VISIBLE);
                    Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_calendar_open);
                    mDatePickerView.startAnimation(animation);
                } else {
                    Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_calendar_close);
                    mDatePickerView.startAnimation(animation);
                    animation.setAnimationListener(animationListener);
                }
                break;
            case R.id.ll_monday:
                mCurrentDay.setTime(dateList.get(0));
                setDayToView();
                break;
            case R.id.ll_tuesday:
                mCurrentDay.setTime(dateList.get(1));
                setDayToView();
                break;
            case R.id.ll_wednesday:
                mCurrentDay.setTime(dateList.get(2));
                setDayToView();
                break;
            case R.id.ll_thursday:
                mCurrentDay.setTime(dateList.get(3));
                setDayToView();
                break;
            case R.id.ll_friday:
                mCurrentDay.setTime(dateList.get(4));
                setDayToView();
                break;
            case R.id.ll_saturday:
                mCurrentDay.setTime(dateList.get(5));
                setDayToView();
                break;
            case R.id.ll_sunday:
                mCurrentDay.setTime(dateList.get(6));
                setDayToView();
                break;
            case R.id.ll_choose_day_empty:
                Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_calendar_close);
                mDatePickerView.startAnimation(animation);
                animation.setAnimationListener(animationListener);
                break;
        }
    }

    @Override
    public void onDateSet(DatePickerView view, int year, int monthOfYear, int dayOfMonth) {
        mCurrentDay.set(year, monthOfYear, dayOfMonth);
        dateList = CalendarUtil.getDaysOfWeek(mCurrentDay.getTime());
        setDayToView(true);
        monthOfYear = monthOfYear + 1;
        getDate(year, monthOfYear, dayOfMonth);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_calendar_close);
        mDatePickerView.startAnimation(animation);
        animation.setAnimationListener(animationListener);
    }

    private void getDate(int year, int monthOfYear, int dayOfMonth) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.valueOf(year)).append("-");
        mYearTv.setText(String.valueOf(year));
        if (monthOfYear < 10) {
            sb.append("0").append(String.valueOf(monthOfYear)).append("-");
            mMonthTv.setText("0" + String.valueOf(monthOfYear) + "月");
        } else {
            sb.append(String.valueOf(monthOfYear)).append("-");
            mMonthTv.setText(MessageFormat.format("{0}月", String.valueOf(monthOfYear)));
        }
        if (dayOfMonth < 10) {
            sb.append("0").append(String.valueOf(dayOfMonth));
            mDayTv.setText(String.format("0%s日", String.valueOf(dayOfMonth)));
        } else {
            sb.append(MessageFormat.format("{0}", String.valueOf(dayOfMonth)));
            mDayTv.setText(MessageFormat.format("{0}日", String.valueOf(dayOfMonth)));
        }
        mDate = sb.toString();
    }

    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                InspectionFragment fragment = InspectionFragment.newInstance(position, mDate);
                new InspectionPresenter(InspectionApp.getInstance().getRepositoryComponent().getRepository(), fragment);
                return fragment;
            } else if (position == 1) {
                OverhaulFragment fragment = OverhaulFragment.newInstance(position, mDate);
                new OverhaulPresenter(InspectionApp.getInstance().getRepositoryComponent().getRepository(), fragment);
                return fragment;
            } else if (position == 2) {
                IncrementFragment fragment = IncrementFragment.newInstance(position, mDate);
                new IncrementPresenter(InspectionApp.getInstance().getRepositoryComponent().getRepository(), fragment);
                return fragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.tab_inspection);
                case 1:
                    return getString(R.string.tab_overhaul);
                case 2:
                    return getString(R.string.tab_increment);
                case 3:
            }
            return null;
        }
    }

    private void setDayToView() {
        setDayToView(false);
    }

    private void setDayToView(boolean isCalendar) {
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        for (int i = 0; i < dayTvs.length; i++) {
            calendar.setTime(dateList.get(i));
            if (calendar.get(Calendar.DAY_OF_WEEK) == mCurrentDay.get(Calendar.DAY_OF_WEEK)) {
                dayOfWeekLayout[i].setBackgroundColor(findColorById(R.color.color2261b0));
            } else {
                dayOfWeekLayout[i].setBackgroundColor(findColorById(R.color.colorPrimary));
            }
            dayTvs[i].setText(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
        }
        getDate(mCurrentDay.get(Calendar.YEAR), mCurrentDay.get(Calendar.MONTH) + 1, mCurrentDay.get(Calendar.DAY_OF_MONTH));
        if (!isCalendar) {
            onDataChange(p);
        }
    }

    private Animation.AnimationListener animationListener = new Animation.AnimationListener() {

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            mChooseDayLayout.setVisibility(View.GONE);
            onDataChange(p);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };

    private void onDataChange(int position) {
        for (IRepairDataChangeListener l : listeners) {
            l.onDataChange(mDate, position);
        }
    }

    int p = -1;

    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (p != position) {
                p = position;
            } else {
                return;
            }
            for (IRepairDataChangeListener l : listeners) {
                l.onPageChanged(position);
            }
        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}
