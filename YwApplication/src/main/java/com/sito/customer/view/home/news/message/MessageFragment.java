package com.sito.customer.view.home.news.message;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sito.customer.R;
import com.sito.customer.app.CustomerApp;
import com.sito.customer.common.BroadcastAction;
import com.sito.customer.common.ConstantStr;
import com.sito.customer.view.MvpFragmentV4;
import com.sito.customer.view.home.news.list.NewsListFragment;
import com.sito.library.utils.SPHelper;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.badgeview.BGABadgeTextView;

/**
 * 消息
 */
public class MessageFragment extends MvpFragmentV4<MessageContract.Presenter> implements MessageContract.View {

    private List<BGABadgeTextView> bgaBadgeTextViews;
    private boolean[] readState = null;
    private int currentItem;
    private UpdateUIBroadcastReceiver broadcastReceiver;

    public static MessageFragment newInstance(int position) {
        Bundle args = new Bundle();
        args.putInt(ConstantStr.KEY_BUNDLE_INT, position);
        MessageFragment fragment = new MessageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new MessagePresenter(CustomerApp.getInstance().getRepositoryComponent().getRepository(), this);
        bgaBadgeTextViews = new ArrayList<>();
        currentItem = getArguments().getInt(ConstantStr.KEY_BUNDLE_INT);
        IntentFilter filter = new IntentFilter();
        filter.addAction(BroadcastAction.MESSAGE_UN_READ_STATE);
        broadcastReceiver = new UpdateUIBroadcastReceiver();
        getActivity().registerReceiver(broadcastReceiver, filter);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_news_n, container, false);
        ViewPager mViewPager = rootView.findViewById(R.id.viewpager);
        mViewPager.setOffscreenPageLimit(3);
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = rootView.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            View view = mSectionsPagerAdapter.getTabView(i);
            BGABadgeTextView bgTv = view.findViewById(R.id.tv_tab);
            bgaBadgeTextViews.add(bgTv);
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(view);
            }
        }
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                View view = tab.getCustomView();
                if (view != null) {
                    BGABadgeTextView textView = view.findViewById(R.id.tv_tab);
                    textView.setTextColor(findColorById(R.color.colorPrimary));
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View view = tab.getCustomView();
                if (view != null) {
                    BGABadgeTextView textView = view.findViewById(R.id.tv_tab);
                    textView.setTextColor(findColorById(R.color.color_text_table_normal));
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        mViewPager.setCurrentItem(currentItem);
        setNewsStateRead(currentItem);
        TabLayout.Tab tab = tabLayout.getTabAt(currentItem);
        if (tab != null) {
            View view = tab.getCustomView();
            if (view != null) {
                BGABadgeTextView textView = view.findViewById(R.id.tv_tab);
                textView.setTextColor(findColorById(R.color.colorPrimary));
            }
        }
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentItem = position;
                setNewsStateRead(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        if (mPresenter != null) {
            mPresenter.getUnReadCount();
        }
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        CustomerApp.isNewsOtherOpen = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        CustomerApp.isNewsOtherOpen = false;
    }

    private void setNewsStateRead(int currentItem) {
        String keyName;
        if (CustomerApp.getInstance().getCurrentUser() == null) {
            return;
        }
        if (currentItem == 0) {
            keyName = CustomerApp.getInstance().getCurrentUser().getUserId() + ConstantStr.NEWS_ALARM_STATE;
        } else if (currentItem == 1) {
            keyName = CustomerApp.getInstance().getCurrentUser().getUserId() + ConstantStr.NEWS_WORK_STATE;
        } else {
            keyName = CustomerApp.getInstance().getCurrentUser().getUserId() + ConstantStr.NEWS_NOTIFY_STATE;
        }
        SPHelper.write(getActivity(), ConstantStr.USER_INFO, keyName, false);
        if (readState != null) {
            readState[currentItem] = false;
            uploadReadState(readState);
        }
    }

    @Override
    public void setPresenter(MessageContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showUnReadCount(int[] unReadState) {
//        readState = unReadState;
//        uploadReadState(unReadState);
    }

    private void uploadReadState(boolean[] unReadState) {
        for (int i = 0; i < unReadState.length; i++) {
            if (unReadState[i]) {
                bgaBadgeTextViews.get(i).showCirclePointBadge();
            } else {
                bgaBadgeTextViews.get(i).hiddenBadge();
            }
        }
    }


    private class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return NewsListFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.new_alarm);
                case 1:
                    return getString(R.string.new_work);
                default:
                    return getString(R.string.new_notify);
            }
        }

        View getTabView(int position) {
            View tabView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_news_tab, null);
            TextView tabTitle = tabView.findViewById(R.id.tv_tab);
            tabTitle.setText(getPageTitle(position));
            return tabView;
        }

    }

    /**
     * 定义广播接收器（内部类）
     */
    private class UpdateUIBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (mPresenter != null) {
                setNewsStateRead(currentItem);
                mPresenter.getUnReadCount();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            getActivity().unregisterReceiver(broadcastReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
