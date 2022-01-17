package com.isuo.yw2application.view.main.work.await;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.isuo.yw2application.R;
import com.isuo.yw2application.app.Yw2Application;
import com.isuo.yw2application.common.ConstantStr;
import com.isuo.yw2application.mode.bean.work.AwaitWorkBean;
import com.isuo.yw2application.view.base.LazyLoadFragmentV4;
import com.isuo.yw2application.view.main.alarm.detail.AlarmDetailActivity;
import com.sito.library.adapter.RVAdapter;
import com.sito.library.utils.DataUtil;
import com.sito.library.widget.RecycleRefreshLoadLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 代办任务
 * Created by zhangan on 2017-06-29.
 */

public class AwaitFragment extends LazyLoadFragmentV4<AwaitContract.Presenter> implements AwaitContract.View
        , SwipeRefreshLayout.OnRefreshListener, RecycleRefreshLoadLayout.OnLoadListener {

    private boolean isRefresh, isLoad;
    private List<AwaitWorkBean> mList;
    private String date;
    //view
    private RecyclerView mRecyclerView;
    private RecycleRefreshLoadLayout mRecycleRefreshLoadLayout;
    private RelativeLayout noDataLayout;

    public static AwaitFragment newInstance(String date) {
        Bundle args = new Bundle();
        args.putString(ConstantStr.KEY_BUNDLE_STR, date);
        AwaitFragment fragment = new AwaitFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new AwaitPresenter(Yw2Application.getInstance().getWorkRepositoryComponent().getRepository(), this);
        date = getArguments().getString(ConstantStr.KEY_BUNDLE_STR);
        mList = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fmg_recycle_view_load_data_await, container, false);
        mRecyclerView = rootView.findViewById(R.id.recycleViewId);
        noDataLayout = rootView.findViewById(R.id.layout_no_data);
        mRecycleRefreshLoadLayout = rootView.findViewById(R.id.refreshLoadLayoutId);
        mRecycleRefreshLoadLayout.setColorSchemeColors(findColorById(R.color.colorPrimary));
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        @SuppressLint("InflateParams")
        View loadFooterView = LayoutInflater.from(getActivity()).inflate(R.layout.view_load_more_inspection, null);
        mRecycleRefreshLoadLayout.setViewFooter(loadFooterView);
        mRecycleRefreshLoadLayout.setOnRefreshListener(this);
        mRecycleRefreshLoadLayout.setOnLoadListener(this);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RVAdapter<AwaitWorkBean> adapter = new RVAdapter<AwaitWorkBean>(mRecyclerView, mList, R.layout.item_work_await) {
            @Override
            public void showData(ViewHolder vHolder, AwaitWorkBean data, int position) {
                TextView tv_equip_name = (TextView) vHolder.getView(R.id.tv_equip_name);
                TextView tv_alarm_name = (TextView) vHolder.getView(R.id.tv_alarm_name);
                TextView tv_time = (TextView) vHolder.getView(R.id.tv_time);
                if (data.getEquipment() != null) {
                    if (TextUtils.isEmpty(data.getEquipment().getEquipmentSn())) {
                        tv_equip_name.setText(data.getEquipment().getEquipmentName());
                    } else {
                        String str = data.getEquipment().getEquipmentName() +
                                "(" + data.getEquipment().getEquipmentSn() + ")";
                        tv_equip_name.setText(str);
                    }
                }
                if (data.getFaultType() == 1) {
                    tv_alarm_name.setText("A类事件");
                    tv_alarm_name.setTextColor(findColorById(R.color.colorFA5B58));
                } else if (data.getFaultType() == 2) {
                    tv_alarm_name.setText("B类事件");
                    tv_alarm_name.setTextColor(findColorById(R.color.colorF0A326));
                } else {
                    tv_alarm_name.setText("C类事件");
                    tv_alarm_name.setTextColor(findColorById(R.color.color3A87EE));
                }
                tv_time.setText(DataUtil.timeFormat(data.getCreateTime(), null));
            }
        };
        adapter.setOnItemClickListener(new RVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), AlarmDetailActivity.class);
                intent.putExtra(ConstantStr.KEY_BUNDLE_BOOLEAN, true);
                intent.putExtra(ConstantStr.KEY_BUNDLE_STR, String.valueOf(mList.get(position).getFaultId()));
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void requestData() {
        if (isLoad) {
            return;
        }
        if (mPresenter != null) {
            isLoad = true;
            mList.clear();
            mRecyclerView.getAdapter().notifyDataSetChanged();
            noDataLayout.setVisibility(View.GONE);
            mPresenter.getData(date);
        }
    }

    private int REQUEST_CODE = 200;


    @Override
    public void setPresenter(AwaitContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onRefresh() {
        if (mPresenter != null) {
            isRefresh = true;
            noDataLayout.setVisibility(View.GONE);
            mRecycleRefreshLoadLayout.setNoMoreData(false);
            mPresenter.getData(date);
        }
    }

    @Override
    public void onLoadMore() {
        if (isRefresh || mList.size() == 0) {
            return;
        }
        if (mPresenter != null) {
            mPresenter.getDataMore(date, String.valueOf(mList.get(mList.size() - 1).getFaultId()));
        }
    }

    @Override
    public void showData(List<AwaitWorkBean> awaitWorkBeen) {
        mList.clear();
        isRefresh = false;
        mList.addAll(awaitWorkBeen);
        mRecyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void showMoreData(List<AwaitWorkBean> awaitWorkBeen) {
        mList.addAll(awaitWorkBeen);
        mRecyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void showLoading() {
        if (!isRefresh) {
//            showEvLoading();
        } else {
            mRecycleRefreshLoadLayout.setRefreshing(true);
        }
    }

    @Override
    public void hideLoading() {
//        hideEvLoading();
        mRecycleRefreshLoadLayout.setRefreshing(false);
    }

    @Override
    public void noData() {
        mList.clear();
        mRecyclerView.getAdapter().notifyDataSetChanged();
        noDataLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void noMoreData() {
        mRecycleRefreshLoadLayout.setNoMoreData(true);
    }

    @Override
    public void hideLoadingMore() {
        mRecycleRefreshLoadLayout.loadFinish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            onRefresh();
        }
    }
}
