package com.sito.customer.view.home.news;


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
import android.util.TypedValue;
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
import com.sito.customer.view.home.news.message.MessageActivity;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.badgeview.BGABadgeView;

/**
 * 消息
 */
public class NewsFragmentN extends MvpFragmentV4<NewsContract.Presenter> implements NewsContract.View, View.OnClickListener {

    private List<BGABadgeView> bgaBadgeTextViews;
    private int currentItem;

    public static NewsFragmentN newInstance() {
        Bundle args = new Bundle();
        NewsFragmentN fragment = new NewsFragmentN();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new NewsPresenter(CustomerApp.getInstance().getRepositoryComponent().getRepository(), this);
        CustomerApp.isNewsMeOpen = true;
        bgaBadgeTextViews = new ArrayList<>();
        IntentFilter filter = new IntentFilter();
        filter.addAction(BroadcastAction.MESSAGE_UN_READ_STATE);
        filter.addAction(BroadcastAction.CLEAN_ALL_DATA);
        broadcastReceiver = new UpdateUIBroadcastReceiver();
        getActivity().registerReceiver(broadcastReceiver, filter);
    }

    private UpdateUIBroadcastReceiver broadcastReceiver;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_news_me, container, false);
        ViewPager mViewPager = rootView.findViewById(R.id.viewpager);
        mViewPager.setOffscreenPageLimit(4);
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = rootView.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            View view = mSectionsPagerAdapter.getTabView(i);
            BGABadgeView bgTv = view.findViewById(R.id.tv_tab);
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
                    TextView textView = view.findViewById(R.id.tvName);
                    textView.setTextColor(findColorById(R.color.colorWhite));
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View view = tab.getCustomView();
                if (view != null) {
                    TextView textView = view.findViewById(R.id.tvName);
                    textView.setTextColor(findColorById(R.color.new_text_un_select));
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        TabLayout.Tab tab = tabLayout.getTabAt(currentItem);
        if (tab != null) {
            View view = tab.getCustomView();
            if (view != null) {
                TextView textView = view.findViewById(R.id.tvName);
                textView.setTextColor(findColorById(R.color.colorWhite));
                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
            }
        }
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentItem = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        return rootView;
    }

    @Override
    public void setPresenter(NewsContract.Presenter presenter) {
        mPresenter = presenter;
    }


    @Override
    public void onResume() {
        super.onResume();
        if (mPresenter != null) {
            mPresenter.getUnReadCount();
        }
    }

    @Override
    public void showUnReadCount(int[] unReadState) {
        for (int i = 0; i < unReadState.length; i++) {
            if (unReadState[i] == 0) {
                bgaBadgeTextViews.get(i).hiddenBadge();
            } else {
                bgaBadgeTextViews.get(i).showTextBadge(String.valueOf(unReadState[i]));
            }
        }
    }

    @Override
    public void onClick(View v) {
        String tag = (String) v.getTag();
        Intent intent = new Intent(getActivity(), MessageActivity.class);
        intent.putExtra(ConstantStr.KEY_BUNDLE_INT, Integer.valueOf(tag));
        startActivity(intent);
    }

    /**
     * 定义广播接收器（内部类）
     */
    private class UpdateUIBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (mPresenter != null) {
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
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.new_work);
                case 1:
                    return getString(R.string.new_alarm);
                case 2:
                    return getString(R.string.new_notify);
                default:
                    return getString(R.string.new_me);
            }
        }

        View getTabView(int position) {

            View tabView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_news_tab, null);
            TextView tvName = tabView.findViewById(R.id.tvName);
            tvName.setText(getPageTitle(position));
            return tabView;
        }

    }
}
