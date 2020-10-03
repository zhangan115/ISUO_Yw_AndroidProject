package com.isuo.yw2application.view.main.equip.time;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.isuo.yw2application.R;
import com.isuo.yw2application.app.Yw2Application;
import com.isuo.yw2application.common.ConstantStr;
import com.isuo.yw2application.mode.bean.equip.TimeLineBean;
import com.isuo.yw2application.view.base.MvpFragment;
import com.isuo.yw2application.view.main.equip.time.detail.EquipmentRecordDetailActivity;
import com.sito.library.adapter.RVAdapter;
import com.sito.library.utils.DataUtil;
import com.sito.library.widget.ExpendRecycleView;
import com.sito.library.widget.RecycleRefreshLoadLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 大修记录，带电检测，实验数据
 * Created by zhangan on 2017/10/13.
 */

public class EquipmentTimeLineFragment extends MvpFragment<EquipmentTimeLineContract.Presenter> implements EquipmentTimeLineContract.View
        , SwipeRefreshLayout.OnRefreshListener {

    private int showType;
    private long equipmentId;
    private List<TimeLineBean> mList;

    private RelativeLayout mNoDataLayout;
    private ExpendRecycleView mExpendRecycleView;
    private RecycleRefreshLoadLayout mRecycleRefreshLoadLayout;

    public static EquipmentTimeLineFragment newInstance(int showType, long equipmentId) {

        Bundle args = new Bundle();
        args.putLong(ConstantStr.KEY_BUNDLE_LONG, equipmentId);
        args.putInt(ConstantStr.KEY_BUNDLE_INT, showType);
        EquipmentTimeLineFragment fragment = new EquipmentTimeLineFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new TimeLinePresenter(Yw2Application.getInstance().getEquipmentRepositoryComponent().getRepository(), this);
        showType = getArguments().getInt(ConstantStr.KEY_BUNDLE_INT);
        equipmentId = getArguments().getLong(ConstantStr.KEY_BUNDLE_LONG);
        mList = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fmg_recycle_with_bg, container, false);
        mExpendRecycleView = (ExpendRecycleView) rootView.findViewById(R.id.recycleViewId);
        mNoDataLayout = (RelativeLayout) rootView.findViewById(R.id.layout_no_data);
        mRecycleRefreshLoadLayout = (RecycleRefreshLoadLayout) rootView.findViewById(R.id.refreshLoadLayoutId);
        mExpendRecycleView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        mRecycleRefreshLoadLayout.setColorSchemeColors(findColorById(R.color.colorPrimary));
        mRecycleRefreshLoadLayout.setOnRefreshListener(this);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RVAdapter<TimeLineBean> adapter = new RVAdapter<TimeLineBean>(mExpendRecycleView, mList, R.layout.item_equip_time_line) {
            @Override
            public void showData(ViewHolder vHolder, TimeLineBean data, int position) {
                View view1 = vHolder.getView(R.id.view_1);
                View view2 = vHolder.getView(R.id.view_2);
                View view3 = vHolder.getView(R.id.view_3);
                TextView time = (TextView) vHolder.getView(R.id.tv_time);
                TextView name = (TextView) vHolder.getView(R.id.tv_name);
                if (position == 0) {
                    view1.setVisibility(View.INVISIBLE);
                } else {
                    view1.setVisibility(View.VISIBLE);
                }
                if (position == mList.size() - 1) {
                    view2.setVisibility(View.INVISIBLE);
                    view3.setVisibility(View.INVISIBLE);
                } else {
                    view2.setVisibility(View.VISIBLE);
                    view3.setVisibility(View.VISIBLE);
                }
                if (mList.size() == 1) {
                    view2.setVisibility(View.VISIBLE);
                    view3.setVisibility(View.VISIBLE);
                }
                time.setText(DataUtil.timeFormat(data.getCreateTime(), null));
                name.setText(data.getRecordName());
            }
        };
        mExpendRecycleView.setAdapter(adapter);
        adapter.setOnItemClickListener(new RVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), EquipmentRecordDetailActivity.class);
                intent.putExtra(ConstantStr.KEY_BUNDLE_OBJECT, mList.get(position));
                startActivity(intent);
            }
        });
        onRefresh();
    }

    @Override
    public void setPresenter(EquipmentTimeLineContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showLoading() {
        showEvLoading();
    }

    @Override
    public void hideLoading() {
        hideEvLoading();
        mRecycleRefreshLoadLayout.setRefreshing(false);
    }

    @Override
    public void showData(@NonNull List<TimeLineBean> lineBeen) {
        mNoDataLayout.setVisibility(View.GONE);
        mList.clear();
        mList.addAll(lineBeen);
        mExpendRecycleView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void noData() {
        mList.clear();
        mExpendRecycleView.getAdapter().notifyDataSetChanged();
        mNoDataLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void showMessage(String message) {
        if (!TextUtils.isEmpty(message)) {
            getApp().showToast(message);
        }
    }

    @Override
    public void onRefresh() {
        if (mPresenter != null) {
            getData();
        }
    }

    private void getData() {
        if (mPresenter == null) {
            return;
        }
        if (showType == 1) {
            mPresenter.getEquipRepairData(equipmentId);
        } else if (showType == 2) {
            mPresenter.getEquipCheckData(equipmentId);
        } else {
            mPresenter.getEquipExperimentData(equipmentId);
        }

    }
}
