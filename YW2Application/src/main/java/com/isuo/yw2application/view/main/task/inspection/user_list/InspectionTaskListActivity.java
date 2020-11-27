package com.isuo.yw2application.view.main.task.inspection.user_list;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.isuo.yw2application.R;
import com.isuo.yw2application.app.Yw2Application;
import com.isuo.yw2application.common.ConstantInt;
import com.isuo.yw2application.common.ConstantStr;
import com.isuo.yw2application.mode.bean.work.InspectionBean;
import com.isuo.yw2application.view.base.BaseActivity;
import com.isuo.yw2application.view.main.task.inspection.detial.InspectDetailActivity;
import com.sito.library.adapter.RVAdapter;
import com.sito.library.utils.DataUtil;
import com.sito.library.widget.ExpendRecycleView;
import com.sito.library.widget.RecycleRefreshLoadLayout;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class InspectionTaskListActivity extends BaseActivity implements InspectionTaskContract.View, SwipeRefreshLayout.OnRefreshListener, RecycleRefreshLoadLayout.OnLoadListener {

    InspectionTaskContract.Presenter mPresenter;
    private int userId;
    //view
    private RelativeLayout mNoDataLayout;
    private ExpendRecycleView mExpendRecycleView;
    private RecycleRefreshLoadLayout mRecycleRefreshLoadLayout;
    private List<InspectionBean> mList;

    private int[] icons = new int[]{R.drawable.work_day_icon
            , R.drawable.work_week_icon
            , R.drawable.work_month_icon
            , R.drawable.work_special_icon};
    private String[] inspectionTypeStr = new String[]{"日检", "周检", "月检", "特检"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutAndToolbar(R.layout.activity_fault_history, "巡检数据");
        new InspectionTaskPresenter(Yw2Application.getInstance().getWorkRepositoryComponent().getRepository(), this);
        userId = getIntent().getIntExtra(ConstantStr.KEY_BUNDLE_INT, -1);
        mExpendRecycleView = findViewById(R.id.recycleViewId);
        mRecycleRefreshLoadLayout = findViewById(R.id.refreshLoadLayoutId);
        mRecycleRefreshLoadLayout.setColorSchemeColors(findColorById(R.color.colorPrimary));
        mRecycleRefreshLoadLayout.setOnRefreshListener(this);
        mRecycleRefreshLoadLayout.setOnLoadListener(this);
        mList = new ArrayList<>();
        mNoDataLayout = findViewById(R.id.layout_no_data);
        initRecycleView();
        mPresenter.getTaskList(userId, ConstantInt.PAGE_SIZE);
    }

    private void initRecycleView() {
        mExpendRecycleView.setLayoutManager(new GridLayoutManager(this, 1));
        RVAdapter<InspectionBean> adapter = new RVAdapter<InspectionBean>(mExpendRecycleView, mList, R.layout.item_day_inspection) {
            @Override
            public void showData(ViewHolder vHolder, InspectionBean data, int position) {
                TextView tv_belong_place = (TextView) vHolder.getView(R.id.tv_belong_place);
                TextView tv_task_name = (TextView) vHolder.getView(R.id.tv_task_name);
                TextView tv_equip_num = (TextView) vHolder.getView(R.id.tv_equip_num);
                TextView tv_time_plan = (TextView) vHolder.getView(R.id.tv_time_plan_start);
                TextView tv_time_actual = (TextView) vHolder.getView(R.id.tv_time_actual_start);
                TextView tv_executor_user_type = (TextView) vHolder.getView(R.id.tv_executor_user_type);
                TextView tv_time_plan_end = (TextView) vHolder.getView(R.id.tv_time_plan_end);
                TextView tv_time_actual_end = (TextView) vHolder.getView(R.id.tv_time_actual_end);
                TextView planStartTimeTv = (TextView) vHolder.getView(R.id.planStartTimeTv);
                TextView actualStartTimeTv = (TextView) vHolder.getView(R.id.actualStartTimeTv);
                TextView startTaskTv = (TextView) vHolder.getView(R.id.startTaskTv);
                LinearLayout ll_inspection_type = (LinearLayout) vHolder.getView(R.id.ll_inspection_type);
                LinearLayout ll_actual_time = (LinearLayout) vHolder.getView(R.id.ll_actual_time);
                ImageView iv_inspection_type = (ImageView) vHolder.getView(R.id.iv_inspection_type);
                TextView tv_inspection_type = (TextView) vHolder.getView(R.id.tv_inspection_type);
                TextView tv_executor_inspection_user = (TextView) vHolder.getView(R.id.tv_executor_inspection_user);
                LinearLayout startTaskLayout = (LinearLayout) vHolder.getView(R.id.ll_start_task);
                if (data.getIsManualCreated() == 0) {
                    if (data.getPlanPeriodType() == 0) {
                        ll_inspection_type.setVisibility(View.GONE);
                    } else {
                        ll_inspection_type.setVisibility(View.VISIBLE);
                        iv_inspection_type.setImageDrawable(findDrawById(icons[data.getPlanPeriodType() - 1]));
                        tv_inspection_type.setText(inspectionTypeStr[data.getPlanPeriodType() - 1]);
                    }
                } else {
                    ll_inspection_type.setVisibility(View.VISIBLE);
                    iv_inspection_type.setImageDrawable(findDrawById(icons[3]));
                    tv_inspection_type.setText(inspectionTypeStr[3]);
                }
                tv_task_name.setText(data.getTaskName());
                if (data.getTaskState() == ConstantInt.TASK_STATE_1) {
                    ll_actual_time.setVisibility(View.GONE);
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
                    actualStartTimeTv.setText("实际开始:");
                    startTaskTv.setText("领取任务");
                } else if (data.getTaskState() == ConstantInt.TASK_STATE_2) {
                    ll_actual_time.setVisibility(View.GONE);
                    tv_executor_user_type.setText("领 取 人:");
                    tv_executor_inspection_user.setText(data.getReceiveUser().getRealName());
                    actualStartTimeTv.setText("实际开始:");
                    startTaskTv.setText("开始任务");
                } else if (data.getTaskState() == ConstantInt.TASK_STATE_3) {
                    ll_actual_time.setVisibility(View.GONE);
                    tv_executor_user_type.setText("领 取 人:");
                    tv_executor_inspection_user.setText(data.getReceiveUser().getRealName());
                    actualStartTimeTv.setText("实际开始:");
                    startTaskTv.setText("开始任务");
                } else if (data.getTaskState() == ConstantInt.TASK_STATE_4) {
                    actualStartTimeTv.setText("巡检截至:");
                    ll_actual_time.setVisibility(View.VISIBLE);
                    tv_time_actual.setText(DataUtil.timeFormat(data.getStartTime(), "yyyy-MM-dd HH:mm"));
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
                    tv_executor_user_type.setText("执 行 人:");
                    startTaskTv.setText("开始任务");
                }
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < data.getRooms().size(); i++) {
                    sb.append(data.getRooms().get(i));
                    if (i != data.getRooms().size() - 1) {
                        sb.append("、");
                    }
                }
                tv_belong_place.setText(sb.toString());
                String str = data.getUploadCount() + "/" + data.getCount();
                tv_equip_num.setText(str);
                if (data.getPlanStartTime() != 0) {
                    tv_time_plan.setVisibility(View.VISIBLE);
                    planStartTimeTv.setText("计划起止:");
                    tv_time_plan.setText(MessageFormat.format("{0}"
                            , DataUtil.timeFormat(data.getPlanStartTime(), "yyyy-MM-dd HH:mm")));
                } else {
                    planStartTimeTv.setVisibility(View.GONE);
                    tv_time_plan.setVisibility(View.GONE);
                }
                if (data.getPlanEndTime() != 0) {
                    tv_time_plan_end.setVisibility(View.VISIBLE);
                    tv_time_plan_end.setText(DataUtil.timeFormat(data.getPlanEndTime()
                            , "yyyy-MM-dd HH:mm"));
                } else {
                    tv_time_plan_end.setVisibility(View.GONE);
                }
                startTaskLayout.setVisibility(View.GONE);
            }
        };
        mExpendRecycleView.setAdapter(adapter);
        adapter.setOnItemClickListener(new RVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (mList == null || mList.size() == 0) {
                    return;
                }
                Intent intent = new Intent(InspectionTaskListActivity.this, InspectDetailActivity.class);
                intent.putExtra(ConstantStr.KEY_BUNDLE_LONG, mList.get(position).getTaskId());
                intent.putExtra(ConstantStr.KEY_BUNDLE_STR, mList.get(position).getTaskName());
                startActivity(intent);
            }
        });
    }

    @Override
    public void setPresenter(InspectionTaskContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onRefresh() {
        mNoDataLayout.setVisibility(View.GONE);
        mRecycleRefreshLoadLayout.setNoMoreData(false);
        mPresenter.getTaskList(userId, ConstantInt.PAGE_SIZE);
    }

    @Override
    public void showData(List<InspectionBean> lists) {
        mList.clear();
        mExpendRecycleView.getAdapter().notifyDataSetChanged();
        mNoDataLayout.setVisibility(View.GONE);
    }

    @Override
    public void showMoreData(List<InspectionBean> lists) {
        mList.addAll(lists);
        mExpendRecycleView.getAdapter().notifyDataSetChanged();
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
    public void showLoading() {
        mRecycleRefreshLoadLayout.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        mRecycleRefreshLoadLayout.setRefreshing(false);
    }

    @Override
    public void noData() {
        mList.clear();
        if (mExpendRecycleView.getAdapter() == null) {
            return;
        }
        mExpendRecycleView.getAdapter().notifyDataSetChanged();
        mNoDataLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoadMore() {
        if (mList.size() > 1) {
            mPresenter.getMoreTaskList(userId, ConstantInt.PAGE_SIZE, mList.get(mList.size() - 1).getTaskId());
        }
    }
}
