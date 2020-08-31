package com.sito.evpro.inspection.view.repair.inspection;

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
import com.sito.evpro.inspection.common.ConstantInt;
import com.sito.evpro.inspection.common.ConstantStr;
import com.sito.evpro.inspection.mode.bean.inspection.InspectionBean;
import com.sito.evpro.inspection.view.MvpFragmentV4;
import com.sito.evpro.inspection.view.repair.IRepairDataChangeListener;
import com.sito.evpro.inspection.view.repair.RepairActivity;
import com.sito.evpro.inspection.view.repair.inspection.inspectdetial.InspectionDetailActivity;
import com.sito.library.adapter.RVAdapter;
import com.sito.library.utils.DataUtil;
import com.sito.library.widget.RecycleRefreshLoadLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 今日巡检
 * Created by zhangan on 2017-06-22.
 */

public class InspectionFragment extends MvpFragmentV4<InspectionContract.Presenter> implements InspectionContract.View, IRepairDataChangeListener, SwipeRefreshLayout.OnRefreshListener, RecycleRefreshLoadLayout.OnLoadListener {

    //view
    private RecyclerView mRecyclerView;
    private RelativeLayout noDataLayout;
    private RecycleRefreshLoadLayout mRecycleRefreshLoadLayout;
    //data
    private List<InspectionBean> mList;
    private int mPosition;
    private String mDate;
    private boolean isLoad;
    private boolean isRefresh;

    public static InspectionFragment newInstance(int position, String date) {
        Bundle args = new Bundle();
        args.putInt(ConstantStr.KEY_BUNDLE_INT, position);
        args.putString(ConstantStr.KEY_BUNDLE_STR, date);
        InspectionFragment fragment = new InspectionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        RepairActivity repairActivity = (RepairActivity) activity;
        repairActivity.addChangeListeners(this);
    }

    private int[] icons = new int[]{R.drawable.work_day_icon
            , R.drawable.work_week_icon
            , R.drawable.work_month_icon
            , R.drawable.work_special_icon};
    private String[] strs = new String[]{"日检", "周检", "月检", "特检"};


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
        mRecyclerView = rootView.findViewById(R.id.recycleViewId);
        noDataLayout = rootView.findViewById(R.id.layout_no_data);
        mRecycleRefreshLoadLayout = rootView.findViewById(R.id.refreshLoadLayoutId);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        mRecycleRefreshLoadLayout.setColorSchemeColors(findColorById(R.color.colorPrimary));
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecycleRefreshLoadLayout.setOnRefreshListener(this);
        RVAdapter<InspectionBean> adapter = new RVAdapter<InspectionBean>(mRecyclerView, mList, R.layout.item_day_inspection) {
            @Override
            public void showData(RVAdapter.ViewHolder vHolder, InspectionBean data, int position) {
                TextView tv_belong_place = (TextView) vHolder.getView(R.id.tv_belong_place);
                TextView tv_task_name = (TextView) vHolder.getView(R.id.tv_task_name);
                TextView tv_equip_num = (TextView) vHolder.getView(R.id.tv_equip_num);
                TextView tv_time_plan = (TextView) vHolder.getView(R.id.tv_time_plan_start);
                TextView tv_time_actual = (TextView) vHolder.getView(R.id.tv_time_actual_start);
                TextView tv_executor_user_type = (TextView) vHolder.getView(R.id.tv_executor_user_type);
                TextView tv_time_plan_end = (TextView) vHolder.getView(R.id.tv_time_plan_end);
                TextView tv_time_actual_end = (TextView) vHolder.getView(R.id.tv_time_actual_end);
                ImageView iv_state = (ImageView) vHolder.getView(R.id.iv_state);
                LinearLayout ll_inspection_type = (LinearLayout) vHolder.getView(R.id.ll_inspection_type);
                LinearLayout ll_actual_time = (LinearLayout) vHolder.getView(R.id.ll_actual_time);
                ImageView iv_inspection_type = (ImageView) vHolder.getView(R.id.iv_inspection_type);
                TextView tv_inspection_type = (TextView) vHolder.getView(R.id.tv_inspection_type);
                LinearLayout startTaskLayout = (LinearLayout) vHolder.getView(R.id.ll_start_task);
                TextView tv_executor_inspection_user = (TextView) vHolder.getView(R.id.tv_executor_inspection_user);
                if (data.getIsManualCreated() == 0) {
                    if (data.getPlanPeriodType() == 0) {
                        ll_inspection_type.setVisibility(View.GONE);
                    } else {
                        ll_inspection_type.setVisibility(View.VISIBLE);
                        iv_inspection_type.setImageDrawable(findDrawById(icons[data.getPlanPeriodType() - 1]));
                        tv_inspection_type.setText(strs[data.getPlanPeriodType() - 1]);
                    }
                } else {
                    ll_inspection_type.setVisibility(View.VISIBLE);
                    iv_inspection_type.setImageDrawable(findDrawById(icons[3]));
                    tv_inspection_type.setText(strs[3]);
                }
                tv_task_name.setText(data.getTaskName());
                if (data.getTaskState() == ConstantInt.TASK_STATE_1) {
                    ll_actual_time.setVisibility(View.GONE);
                    iv_state.setTag(R.id.tag_object, data);
                    iv_state.setTag(R.id.tag_position, position);
                    iv_state.setImageDrawable(findDrawById(R.drawable.home_bg_get_task));
                    startTaskLayout.setVisibility(View.GONE);
                    if (data.getExecutorUserList() != null && data.getExecutorUserList().size() > 0) {
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < data.getExecutorUserList().size(); i++) {
                            if (data.getExecutorUserList().get(i).getUser() == null) {
                                continue;
                            }
                            if (!TextUtils.isEmpty(data.getExecutorUserList().get(i).getUser().getRealName())) {
                                sb.append(data.getExecutorUserList().get(i).getUser().getRealName());
                                if (i != data.getExecutorUserList().size() - 1) {
                                    sb.append("、");
                                }
                            }
                        }
                        tv_executor_inspection_user.setText(sb.toString());
                    }
                    tv_executor_user_type.setText("被指派人:");
                } else if (data.getTaskState() == ConstantInt.TASK_STATE_2) {
                    ll_actual_time.setVisibility(View.GONE);
                    iv_state.setImageDrawable(findDrawById(R.drawable.work_receive_tag));
                    tv_executor_user_type.setText("领 取 人：");
                    tv_executor_inspection_user.setText(data.getReceiveUser().getRealName());
                    startTaskLayout.setVisibility(View.VISIBLE);
                } else if (data.getTaskState() == ConstantInt.TASK_STATE_3) {
                    ll_actual_time.setVisibility(View.GONE);
                    iv_state.setImageDrawable(findDrawById(R.drawable.work_on_tag));
                    tv_executor_user_type.setText("领 取 人：");
                    tv_executor_inspection_user.setText(data.getReceiveUser().getRealName());
                    startTaskLayout.setVisibility(View.VISIBLE);
                } else if (data.getTaskState() == ConstantInt.TASK_STATE_4) {
                    ll_actual_time.setVisibility(View.VISIBLE);
                    iv_state.setImageDrawable(findDrawById(R.drawable.work_complete_tag));
                    tv_time_actual.setText("巡检截至时间:" + DataUtil.timeFormat(data.getStartTime(), "yyyy-MM-dd HH:mm"));
                    tv_time_actual_end.setText(DataUtil.timeFormat(data.getEndTime(), "yyyy-MM-dd HH:mm"));
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < data.getUsers().size(); i++) {
                        if (data.getUsers().get(i) == null) {
                            continue;
                        }
                        sb.append(data.getUsers().get(i).getRealName());
                        if (i != data.getUsers().size() - 1) {
                            sb.append("、");
                        }
                    }
                    tv_executor_inspection_user.setText(sb.toString());
                    tv_executor_user_type.setText("执 行 人：");
                    startTaskLayout.setVisibility(View.GONE);
                }
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < data.getRooms().size(); i++) {
                    sb.append(data.getRooms().get(i));
                    if (i != data.getRooms().size() - 1) {
                        sb.append("、");
                    }
                }
                tv_belong_place.setText(sb.toString());
                tv_equip_num.setText(String.valueOf(data.getUploadCount()) + "/" + String.valueOf(data.getCount()));
                if (data.getPlanStartTime() != 0) {
                    tv_time_plan.setVisibility(View.VISIBLE);
                    tv_time_plan.setText("计划起止时间:" + DataUtil.timeFormat(data.getPlanStartTime(), "yyyy-MM-dd HH:mm"));
                } else {
                    tv_time_plan.setVisibility(View.GONE);
                }
                if (data.getPlanEndTime() != 0) {
                    tv_time_plan_end.setVisibility(View.VISIBLE);
                    tv_time_plan_end.setText(DataUtil.timeFormat(data.getPlanEndTime(), "yyyy-MM-dd HH:mm"));
                } else {
                    tv_time_plan_end.setVisibility(View.GONE);
                }
                iv_state.setOnClickListener(unReceiveListener);
                startTaskLayout.setTag(R.id.tag_task, String.valueOf(data.getTaskId()));
                startTaskLayout.setTag(R.id.tag_position_1, data.getSecurityPackage() == null ? -1 : data.getSecurityPackage().getSecurityId());
                startTaskLayout.setOnClickListener(startTaskListener);
            }
        };
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new RVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), InspectionDetailActivity.class);
                intent.putExtra(ConstantStr.KEY_BUNDLE_LONG, mList.get(position).getTaskId());
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
            mPresenter.getInspectionList(mDate);
        }
    }

    @Override
    public void setPresenter(InspectionContract.Presenter presenter) {
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
    public void showData(List<InspectionBean> been) {
        mList.clear();
        isRefresh = false;
        mList.addAll(been);
        mRecyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void showMoreData(List<InspectionBean> inspectionBeen) {
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
        isRefresh = false;
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
    public void operationSuccess(int position) {
        onRefresh();
    }

    @Override
    public void onRefresh() {
        if (mPresenter != null) {
            isRefresh = true;
            noDataLayout.setVisibility(View.GONE);
            mRecycleRefreshLoadLayout.setNoMoreData(false);
            mPresenter.getInspectionList(mDate);
        }
    }

    private boolean isClick;
    private View.OnClickListener startTaskListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (isClick) {
                return;
            }
            Intent intent = new Intent(getActivity(), com.sito.evpro.inspection.view.repair.inspection.detail.InspectionDetailActivity.class);
            String taskId = (String) v.getTag(R.id.tag_task);
            long securityId = (long) v.getTag(R.id.tag_position_1);
            if (TextUtils.isEmpty(taskId)) {
                return;
            }
            intent.putExtra(ConstantStr.KEY_BUNDLE_STR, taskId);
            intent.putExtra(ConstantStr.KEY_BUNDLE_LONG, securityId);
            isClick = true;
            startActivity(intent);
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

    private View.OnClickListener unReceiveListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getTag(R.id.tag_object) != null) {
                int position = (int) v.getTag(R.id.tag_position);
                if (mList.get(position).getTaskState() == ConstantInt.TASK_STATE_1 && mPresenter != null) {
                    mPresenter.operationTask(String.valueOf(mList.get(position).getTaskId()), position);
                }
            }
        }
    };

    @Override
    public void onLoadMore() {
        if (isRefresh || mList.size() == 0) {
            return;
        }
        if (mPresenter != null) {
            mPresenter.getInspectionListMore(mDate, String.valueOf(mList.get(mList.size() - 1).getTaskId()));
        }
    }
}
