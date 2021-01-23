package com.isuo.yw2application.view.main.alarm.equipalarm;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;

import com.isuo.yw2application.R;
import com.isuo.yw2application.common.ConstantStr;
import com.isuo.yw2application.utils.MediaPlayerManager;
import com.isuo.yw2application.view.base.BaseActivity;
import com.isuo.yw2application.view.base.IRepairDataChangeListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yangzb
 * E-mail：yangzongbin@si-top.com
 * Created on 2017/7/4
 */
public class EquipAlarmActivity extends BaseActivity {

    @Nullable
    private String mCalendar;
    @Nullable
    private String alarmState;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String titleStr = getIntent().getStringExtra(ConstantStr.KEY_BUNDLE_STR);
        mCalendar = getIntent().getStringExtra(ConstantStr.KEY_BUNDLE_OBJECT);
        alarmState = getIntent().getStringExtra(ConstantStr.KEY_BUNDLE_OBJECT_1);
        setLayoutAndToolbar(R.layout.activity_work, titleStr);
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            //避免多次调用fmg中的方法
            int p = -1;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (p != position) {
                    p = position;
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    private class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            EquipmentAlarmFragment fragment = (EquipmentAlarmFragment) getSupportFragmentManager()
                    .findFragmentByTag(makeFragmentName(mViewPager.getId(), position));
            if (fragment == null) {
                fragment = EquipmentAlarmFragment.newInstance(mCalendar, alarmState,position);
            }
            addChangeListeners(fragment);
            return fragment;
        }

        @Override
        public int getCount() {
            if (TextUtils.isEmpty(mCalendar)) {
                return 3;
            }
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    if (TextUtils.isEmpty(mCalendar)) {
                        return getString(R.string.str_a_count);
                    }
                    return getString(R.string.str_pending_count);
                case 1:
                    if (TextUtils.isEmpty(mCalendar)) {
                        return getString(R.string.str_b_count);
                    }
                    return getString(R.string.str_flowing_count);
                case 2:
                    if (TextUtils.isEmpty(mCalendar)) {
                        return getString(R.string.str_c_count);
                    }
                    return getString(R.string.str_close_count);
                case 3:
                    return getString(R.string.str_repair_count);
            }
            return null;
        }

        private String makeFragmentName(int viewId, long id) {
            return "android:switcher:" + viewId + ":" + id;
        }
    }

    private List<IRepairDataChangeListener> listeners = new ArrayList<>();

    public void addChangeListeners(IRepairDataChangeListener listener) {
        if (listeners.contains(listener)) {
            return;
        }
        listeners.add(listener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MediaPlayerManager.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MediaPlayerManager.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MediaPlayerManager.pause();
    }
}
