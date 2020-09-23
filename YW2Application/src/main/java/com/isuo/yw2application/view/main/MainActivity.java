package com.isuo.yw2application.view.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.iflytek.cloud.thirdparty.V;
import com.isuo.yw2application.R;
import com.isuo.yw2application.app.Yw2Application;
import com.isuo.yw2application.common.ConstantStr;
import com.isuo.yw2application.mode.bean.User;
import com.isuo.yw2application.view.base.BaseActivity;
import com.isuo.yw2application.view.main.alarm.AlarmFragment;
import com.isuo.yw2application.view.main.data.DataFragment;
import com.isuo.yw2application.view.main.device.DeviceFragment;
import com.isuo.yw2application.view.main.task.TaskFragment;
import com.isuo.yw2application.view.main.work.WorkFragment;
import com.isuo.yw2application.view.share.ShareActivity;
import com.sito.library.utils.DisplayUtil;
import com.sito.library.utils.GlideUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {

    private ArrayList<Fragment> mFragments;
    private AHBottomNavigation bottomNavigation;
    private int selectPosition = 0;
    private long mCurrentTime = 0;
    private DrawerLayout drawer;
    private LinearLayout frameLayout1;
    private ImageView leftImage, rightImage;
    private TextView frameTv, rightTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawer = findViewById(R.id.drawer_layout);
        mFragments = getFragments();
        bottomNavigation = findViewById(R.id.bottom_navigation);
        leftImage = findViewById(R.id.leftImage);
        rightImage = findViewById(R.id.rightImage);
        frameTv = findViewById(R.id.frameTitleTv);
        rightTv = findViewById(R.id.rightTv);
        frameLayout1 = findViewById(R.id.titleLayout1);
        AHBottomNavigationItem item1 = new AHBottomNavigationItem(R.string.str_first_nav_1, R.drawable.work, R.color.colorPrimary);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(R.string.str_first_nav_2, R.drawable.drive, R.color.colorPrimary);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(R.string.str_first_nav_3, R.drawable.task_g, R.color.colorPrimary);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem(R.string.str_first_nav_4, R.drawable.fault_bottom, R.color.colorPrimary);
        AHBottomNavigationItem item5 = new AHBottomNavigationItem(R.string.str_first_nav_5, R.drawable.board, R.color.colorPrimary);
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
        initView();
        showTitleLayout();
    }

    private void initView() {
        findViewById(R.id.layout_1).setOnClickListener(this);
        findViewById(R.id.layout_2).setOnClickListener(this);
        findViewById(R.id.layout_3).setOnClickListener(this);
        findViewById(R.id.layout_4).setOnClickListener(this);
        findViewById(R.id.layout_5).setOnClickListener(this);
        findViewById(R.id.layout_6).setOnClickListener(this);
        findViewById(R.id.layout_7).setOnClickListener(this);
        findViewById(R.id.exitApp).setOnClickListener(this);
        User user = Yw2Application.getInstance().getCurrentUser();
        ImageView userImage = findViewById(R.id.userImage);
        ImageView leftImage = findViewById(R.id.leftImage);
        GlideUtils.ShowCircleImage(this, user.getPortraitUrl(), userImage, R.drawable.mine_head_default);
        GlideUtils.ShowCircleImage(this, user.getPortraitUrl(), leftImage, R.drawable.mine_head_default);
        TextView userNameTv = findViewById(R.id.textUserName);
        userNameTv.setText(user.getRealName());
        TextView userInfoTv = findViewById(R.id.textUserInfo);
        if (user.getCustomer() != null) {
            userInfoTv.setText(String.format("%s-%s", user.getCustomer().getCustomerName(), user.getUserRoleNames()));
        } else {
            userInfoTv.setText(user.getUserRoleNames());
        }
        findViewById(R.id.leftImage).setOnClickListener(this);
        findViewById(R.id.rightImage).setOnClickListener(this);
        findViewById(R.id.rightTv).setOnClickListener(this);
        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {

            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                if (selectPosition != position) {
                    commitFragment(position);
                    return true;
                }
                return false;
            }
        });
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_1:
                break;
            case R.id.layout_2:
                break;
            case R.id.layout_3:
                startActivity(new Intent(this, ShareActivity.class));
                break;
            case R.id.layout_4:
                break;
            case R.id.layout_5:
                break;
            case R.id.layout_6:
                break;
            case R.id.layout_7:
                break;
            case R.id.exitApp:
                MobclickAgent.onProfileSignOff();
                Yw2Application.getInstance().needLogin();
                break;
            case R.id.userImage:
                break;
            case R.id.leftImage:
                drawer.openDrawer(Gravity.START);
                break;
            case R.id.rightImage:
                // TODO: 2020/9/22
                //scan code
                break;
            case R.id.rightTv:
                // TODO: 2020/9/22
                if (selectPosition == 3) {
                } else if (selectPosition == 4) {

                }
                break;
        }
    }

    private void commitFragment(int position) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (mFragments.get(selectPosition).isAdded()) {
            ft.hide(mFragments.get(selectPosition));
        }
        if (mFragments.get(position).isAdded()) {
            ft.show(mFragments.get(position));
        } else {
            ft.add(R.id.frame_container, mFragments.get(position), "tag_" + position);
        }
        selectPosition = position;
        showTitleLayout();
        ft.commit();
    }

    private void showTitleLayout() {
        LinearLayout layout = findViewById(R.id.layout2);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT
                , FrameLayout.LayoutParams.MATCH_PARENT);
        if (selectPosition == 2) {
            params.setMargins(0,0,0,0);
        } else {
            params.setMargins(0, DisplayUtil.dip2px(this,54),0,0);
        }
        layout.setLayoutParams(params);
        frameLayout1.setVisibility(selectPosition != 2 ? View.VISIBLE : View.GONE);
        leftImage.setVisibility(selectPosition == 0 ? View.VISIBLE : View.GONE);
        rightTv.setVisibility(selectPosition == 0 ? View.VISIBLE : View.GONE);
        switch (selectPosition) {
            case 0:
                rightTv.setVisibility(View.GONE);
                User user = Yw2Application.getInstance().getCurrentUser();
                frameTv.setText(String.format("欢迎，%s", user.getRealName()));
                break;
            case 1:
                rightTv.setVisibility(View.GONE);
                frameTv.setText("设备");
                break;
            case 3:
                rightTv.setVisibility(View.VISIBLE);
                frameTv.setText("故障");
                rightTv.setText("故障列表");
                break;
            case 4:
                rightTv.setVisibility(View.VISIBLE);
                frameTv.setText("数据");
                rightTv.setText("其他服务");
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (outState != null) {
            outState.putInt(ConstantStr.KEY_BUNDLE_INT, selectPosition);
        }
    }

    @Override
    public void onBackPressed() {
        if (this.drawer.isDrawerOpen(Gravity.START)) {
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
