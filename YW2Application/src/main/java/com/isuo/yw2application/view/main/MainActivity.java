package com.isuo.yw2application.view.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.isuo.yw2application.R;
import com.isuo.yw2application.app.Yw2Application;
import com.isuo.yw2application.common.ConstantStr;
import com.isuo.yw2application.view.base.BaseActivity;
import com.isuo.yw2application.view.main.alarm.AlarmFragment;
import com.isuo.yw2application.view.main.data.DataFragment;
import com.isuo.yw2application.view.main.device.DeviceFragment;
import com.isuo.yw2application.view.main.task.TaskFragment;
import com.isuo.yw2application.view.main.work.WorkFragment;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {

    private ArrayList<Fragment> mFragments;
    private AHBottomNavigation bottomNavigation;
    private int selectPosition = 0;
    private long mCurrentTime = 0;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawer = findViewById(R.id.drawer_layout);
        mFragments = getFragments();
        bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);
        AHBottomNavigationItem item1 = new AHBottomNavigationItem(R.string.str_first_nav_2, R.drawable.nav_work_icon_normal, R.color.colorPrimary);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(R.string.str_first_nav_1, R.drawable.nav_message_icon_normal, R.color.colorPrimary);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(R.string.str_first_nav_3, R.drawable.nav_call_icon_normal, R.color.colorPrimary);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem(R.string.str_first_nav_4, R.drawable.nav_found_icon_normal, R.color.colorPrimary);
        AHBottomNavigationItem item5 = new AHBottomNavigationItem(R.string.str_first_nav_5, R.drawable.nav_mine_icon_normal, R.color.colorPrimary);
        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);
        bottomNavigation.addItem(item4);
        bottomNavigation.addItem(item5);
        bottomNavigation.setTitleTextSizeInSp(12f, 12f);
        bottomNavigation.setBackgroundColor(findColorById(R.color.colorWhite));
        bottomNavigation.setDefaultBackgroundColor(findColorById(R.color.colorWhite));
        bottomNavigation.setForceTint(false);
        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        bottomNavigation.setAccentColor(findColorById(R.color.colorPrimary));
        bottomNavigation.setInactiveColor(findColorById(R.color.color_bg_nav_normal));
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        if (savedInstanceState != null) {
            selectPosition = savedInstanceState.getInt(ConstantStr.KEY_BUNDLE_INT);
            if (mFragments.get(selectPosition).isAdded()) {
                transaction.show(mFragments.get(selectPosition));
            } else {
                transaction.add(R.id.frame_container, mFragments.get(selectPosition), "tag_" + selectPosition);
            }
        } else {
            transaction.add(R.id.frame_container, mFragments.get(selectPosition), "tag_" + selectPosition);
        }
        bottomNavigation.setCurrentItem(selectPosition);
        transaction.commit();
    }

    private static final int REQUEST_PERMISSION = 0;

    public ArrayList<Fragment> getFragments() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        WorkFragment workFragment = (WorkFragment) getSupportFragmentManager().findFragmentByTag("tag_0");
        if (workFragment == null) {
            workFragment = WorkFragment.newInstance();
        }
        DeviceFragment deviceFragment = (DeviceFragment) getSupportFragmentManager().findFragmentByTag("tag_1");
        if (deviceFragment == null) {
            deviceFragment = DeviceFragment.newInstance();
        }
        TaskFragment taskFragment = (TaskFragment) getSupportFragmentManager().findFragmentByTag("tag_2");
        if (taskFragment == null) {
            taskFragment = TaskFragment.newInstance();
        }
        AlarmFragment alarmFragment = (AlarmFragment) getSupportFragmentManager().findFragmentByTag("tag_3");
        if (alarmFragment == null) {
            alarmFragment = AlarmFragment.newInstance();
        }
        DataFragment dataFragment = (DataFragment) getSupportFragmentManager().findFragmentByTag("tag_4");
        if (dataFragment == null) {
            dataFragment = DataFragment.newInstance();
        }
        fragments.add(workFragment);
        fragments.add(deviceFragment);
        fragments.add(taskFragment);
        fragments.add(alarmFragment);
        fragments.add(dataFragment);
        return fragments;
    }

    @Override
    public void onBackPressed() {
        if (this.drawer.isDrawerOpen(Gravity.START)){
            this.drawer.closeDrawer(Gravity.START);
            return;
        }
        long time = System.currentTimeMillis();
        if (time - this.mCurrentTime < 2000) {
            Yw2Application.getInstance().exitApp();
        } else {
            this.mCurrentTime = time;
            Yw2Application.getInstance().showToast(getString(R.string.toast_exit_app));
        }
    }
}
