package com.sito.evpro.inspection.view.repair.overhaul;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sito.evpro.inspection.R;
import com.sito.evpro.inspection.common.ConstantStr;
import com.sito.evpro.inspection.mode.bean.overhaul.OverhaulBean;
import com.sito.evpro.inspection.view.MvpFragmentV4;
import com.sito.evpro.inspection.view.repair.IRepairDataChangeListener;
import com.sito.evpro.inspection.view.repair.RepairActivity;
import com.sito.evpro.inspection.view.repair.overhaul.detail.OverhaulDetailActivity;
import com.sito.evpro.inspection.view.repair.overhaul.note.OverhaulNoteActivity;
import com.sito.library.adapter.RVAdapter;
import com.sito.library.utils.DataUtil;
import com.sito.library.widget.RecycleRefreshLoadLayout;
import com.sito.library.widget.TextViewVertical;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 检修工作
 * Created by zhangan on 2017-06-22.
 */

public class OverhaulFragment extends MvpFragmentV4<OverhaulContract.Presenter> implements OverhaulContract.View, IRepairDataChangeListener, SwipeRefreshLayout.OnRefreshListener, RecycleRefreshLoadLayout.OnLoadListener {

    //view
    private RecyclerView mRecyclerView;
    private RecycleRefreshLoadLayout mRecycleRefreshLoadLayout;
    private RelativeLayout noDataLayout;
    //data
    private int mPosition;
    private String mDate;
    private boolean isLoad, isRefresh;
    private List<OverhaulBean> mList;

    public static OverhaulFragment newInstance(int position, String date) {
        Bundle args = new Bundle();
        args.putInt(ConstantStr.KEY_BUNDLE_INT, position);
        args.putString(ConstantStr.KEY_BUNDLE_STR, date);
        OverhaulFragment fragment = new OverhaulFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        RepairActivity repairActivity = (RepairActivity) activity;
        repairActivity.addChangeListeners(this);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mList = new ArrayList<>();
        mPosition = getArguments().getInt(ConstantStr.KEY_BUNDLE_INT);
        mDate = getArguments().getString(ConstantStr.KEY_BUNDLE_STR);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fmg_recycle_view_load_data, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycleViewId);
        noDataLayout = (RelativeLayout) rootView.findViewById(R.id.layout_no_data);
        mRecycleRefreshLoadLayout = (RecycleRefreshLoadLayout) rootView.findViewById(R.id.refreshLoadLayoutId);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        mRecycleRefreshLoadLayout.setColorSchemeColors(findColorById(R.color.colorPrimary));
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecycleRefreshLoadLayout.setOnRefreshListener(this);
        RVAdapter<OverhaulBean> adapter = new RVAdapter<OverhaulBean>(mRecyclerView, mList, R.layout.item_overhaul) {

            @Override
            public void showData(ViewHolder vHolder, OverhaulBean data, int position) {
                TextView tv_repair_name = (TextView) vHolder.getView(R.id.tv_repair_name);
                TextView tv_belong_place = (TextView) vHolder.getView(R.id.tv_belong_place);
                TextView tv_state = (TextView) vHolder.getView(R.id.tv_state);
                TextView tv_equip_name = (TextView) vHolder.getView(R.id.tv_equip_name);
                TextView tv_user = (TextView) vHolder.getView(R.id.tv_user);
                TextView tv_start_time = (TextView) vHolder.getView(R.id.tv_start_time);
                TextViewVertical tv_alarm = (TextViewVertical) vHolder.getView(R.id.tv_alarm);
                ImageView iv_state = (ImageView) vHolder.getView(R.id.iv_state);
                if (data.getEquipment() != null) {
                    if (TextUtils.isEmpty(data.getEquipment().getEquipmentSn())) {
                        tv_equip_name.setText(data.getEquipment().getEquipmentName());
                    } else {
                        tv_equip_name.setText(data.getEquipment().getEquipmentName() + "(" + data.getEquipment().getEquipmentSn() + ")");
                    }
                    tv_belong_place.setText(data.getEquipment().getRoom().getRoomName());
                }
                tv_start_time.setText(MessageFormat.format("计划开始时间:{0}", DataUtil.timeFormat(data.getStartTime(), "yyyy-MM-dd HH:mm")));
                if (data.getAddType() == 1) {
                    tv_alarm.setVisibility(View.GONE);
                    iv_state.setVisibility(View.GONE);
                } else {
                    if (data.getFault() != null) {
                        iv_state.setVisibility(View.VISIBLE);
                        tv_alarm.setVisibility(View.VISIBLE);
                        if (data.getFault().getFaultType() == 1) {
                            tv_alarm.setText("A类故障");
                            iv_state.setImageDrawable(findDrawById(R.drawable.work_a));
                        } else if (data.getFault().getFaultType() == 2) {
                            tv_alarm.setText("B类故障");
                            iv_state.setImageDrawable(findDrawById(R.drawable.work_b));
                        } else {
                            tv_alarm.setText("C类故障");
                            iv_state.setImageDrawable(findDrawById(R.drawable.work_c));
                        }
                    }
                }
                if (data.getRepairUsers() != null && data.getRepairUsers().size() > 0) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < data.getRepairUsers().size(); i++) {
                        sb.append(data.getRepairUsers().get(i).getUser().getRealName());
                        if (i != data.getRepairUsers().size() - 1) {
                            sb.append("、");
                        }
                    }
                    tv_user.setText(sb.toString());
                }
                tv_repair_name.setText(data.getRepairName());

                LinearLayout startLayout = (LinearLayout) vHolder.getView(R.id.ll_start_task);
                String state;
                switch (data.getRepairState()) {
                    case 1:
                        state = "待开始";
                        tv_state.setTextColor(findColorById(R.color.color_not_start));
                        startLayout.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        startLayout.setVisibility(View.VISIBLE);
                        tv_state.setTextColor(findColorById(R.color.color_start));
                        state = "进行中";
                        break;
                    default:
                        startLayout.setVisibility(View.GONE);
                        tv_state.setTextColor(findColorById(R.color.color_finish));
                        state = "已完成";
                        break;
                }
                tv_state.setText(state);
                startLayout.setTag(R.id.tag_position, position);
                if (data.getJobPackage() != null) {
                    startLayout.setTag(R.id.tag_position_1, String.valueOf(data.getJobPackage().getJobId()));
                }
                startLayout.setOnClickListener(onClickListener);
            }
        };
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new RVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                long repairId = mList.get(position).getRepairId();
                Intent intent = new Intent(getActivity(), OverhaulDetailActivity.class);
                intent.putExtra(ConstantStr.KEY_BUNDLE_LONG, repairId);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onPageChanged(int p) {
        if (p != mPosition) {
            return;
        }
        if (isLoad) {
            return;
        }
        if (mPresenter != null) {
            isLoad = true;
            mList.clear();
            mRecyclerView.getAdapter().notifyDataSetChanged();
            noDataLayout.setVisibility(View.GONE);
            mPresenter.getOverhaulList(mDate);
        }
    }

    @Override
    public void setPresenter(OverhaulContract.Presenter presenter) {
        mPresenter = presenter;
    }


    @Override
    public void onDataChange(String data, int position) {
        mDate = data;
        isLoad = false;
        mList.clear();
        mRecyclerView.getAdapter().notifyDataSetChanged();
        onRefresh();
    }

    @Override
    public void onRefresh() {
        if (mPresenter != null) {
            isRefresh = true;
            noDataLayout.setVisibility(View.GONE);
            mRecycleRefreshLoadLayout.setNoMoreData(false);
            mPresenter.getOverhaulList(mDate);
        }
    }

    @Override
    public void onLoadMore() {
        if (isRefresh || mList.size() == 0) {
            return;
        }
        if (mPresenter != null) {
            mPresenter.getOverhaulListMore(mDate, String.valueOf(mList.get(mList.size() - 1).getRepairId()));
        }
    }

    @Override
    public void showData(List<OverhaulBean> inspectionBeen) {
        mList.clear();
        isRefresh = false;
        mList.addAll(inspectionBeen);
        mRecyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void showMoreData(List<OverhaulBean> inspectionBeen) {
        mList.addAll(inspectionBeen);
        mRecyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void showLoading() {
        if (!isRefresh) {
            showEvLoading();
        } else {
            mRecycleRefreshLoadLayout.setRefreshing(true);
        }
    }

    @Override
    public void hideLoading() {
        hideEvLoading();
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
    public void startSuccess(int position) {
        if (intent == null) {
            intent = new Intent(getActivity(), OverhaulNoteActivity.class);
        }
        intent.putExtra(ConstantStr.KEY_BUNDLE_STR, String.valueOf(mList.get(position).getRepairId()));
        if (mList.get(position).getJobPackage() != null) {
            intent.putExtra(ConstantStr.KEY_BUNDLE_STR_1, String.valueOf(mList.get(position).getJobPackage().getJobId()));
        }
        isClick = true;
        startActivity(intent);
    }

    @Override
    public void startFail() {

    }

    private Intent intent;

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (int) v.getTag(R.id.tag_position);
            if (mList.get(position).getRepairState() == 1) {
                if (mPresenter != null) {
                    mPresenter.startOverhaul(position, mList.get(position).getRepairId());
                }
            } else {
                startSuccess(position);
            }
        }
    };


    @Override
    public void onResume() {
        super.onResume();
        if (isClick) {
            onRefresh();
        }
        isClick = false;
    }

    boolean isClick;
}
