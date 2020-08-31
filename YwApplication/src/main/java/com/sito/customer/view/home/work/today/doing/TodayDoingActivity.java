package com.sito.customer.view.home.work.today.doing;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sito.customer.R;
import com.sito.customer.app.CustomerApp;
import com.sito.customer.common.ConstantStr;
import com.sito.customer.mode.bean.today.TodayToDoBean;
import com.sito.customer.view.BaseActivity;
import com.sito.customer.view.alarm.AlarmDetailActivity;
import com.sito.customer.view.home.work.overhaul.WorkOverhaulActivity;
import com.sito.customer.view.home.work.overhaul.detail.OverhaulDetailActivity;
import com.sito.customer.view.home.work.overhaul.execute.OverhaulExecuteActivity;
import com.sito.library.adapter.RVAdapter;
import com.sito.library.utils.DataUtil;

import java.util.ArrayList;
import java.util.List;

public class TodayDoingActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, TodayDoingContact.View {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private RelativeLayout noDataLayout;
    private List<TodayToDoBean> datas;

    private TodayDoingContact.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutAndToolbar(R.layout.activity_today_doing, "今日代办");
        new TodayDoingPresenter(CustomerApp.getInstance().getWorkRepositoryComponent().getRepository(), this);
        datas = new ArrayList<>();
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLayout);
        refreshLayout.setColorSchemeColors(findColorById(R.color.colorPrimary));
        refreshLayout.setOnRefreshListener(this);
        recyclerView = (RecyclerView) findViewById(R.id.recycleViewId);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        noDataLayout = (RelativeLayout) findViewById(R.id.layout_no_data);
        noDataLayout.setVisibility(View.VISIBLE);
        RVAdapter<TodayToDoBean> adapter = new RVAdapter<TodayToDoBean>(recyclerView, datas, R.layout.item_today_todo) {
            @Override
            public void showData(ViewHolder vHolder, TodayToDoBean data, int position) {
                TextView name = (TextView) vHolder.getView(R.id.tv_name);
                TextView state = (TextView) vHolder.getView(R.id.tv_state);
                TextView type = (TextView) vHolder.getView(R.id.tv_type);
                TextView from = (TextView) vHolder.getView(R.id.tv_from);
                TextView time = (TextView) vHolder.getView(R.id.tv_time);
                name.setText(data.getEquipmentName());
                if (TextUtils.equals(data.getType(), "fault")) {
                    type.setText("故障");
                    type.setBackground(findDrawById(R.drawable.bg_shape_inject_red));
                    type.setTextColor(Color.parseColor("#f48181"));
                    state.setTextColor(findColorById(R.color.color90A5BF));
                    state.setText(CustomerApp.getInstance().getMapOption().get("9").get(String.valueOf(data.getState())));
                } else {
                    type.setText("检修");
                    type.setBackground(findDrawById(R.drawable.bg_shape_inject_blue));
                    type.setTextColor(Color.parseColor("#488DE5"));
                    state.setTextColor(findColorById(R.color.color_bg_bt_blue));
                    state.setText(CustomerApp.getInstance().getMapOption().get("7").get(String.valueOf(data.getState())));
                }
                from.setText("指派人:" + data.getUsers().getRealName());
                time.setText("创建时间:" + DataUtil.timeFormat(data.getTime(), null));
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new RVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (TextUtils.equals(datas.get(position).getType(), "fault")) {
                    //故障流转
                    Intent intent = new Intent(TodayDoingActivity.this, AlarmDetailActivity.class);
                    intent.putExtra(ConstantStr.KEY_BUNDLE_STR, String.valueOf(datas.get(position).getId()));
                    intent.putExtra(ConstantStr.KEY_BUNDLE_BOOLEAN, true);
                    startActivity(intent);
                } else {
                    //检修任务
                    if (datas.get(position).getState() == 3) {
                        Intent intent = new Intent(TodayDoingActivity.this, OverhaulDetailActivity.class);
                        intent.putExtra(ConstantStr.KEY_BUNDLE_LONG, datas.get(position).getId());
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(TodayDoingActivity.this, OverhaulExecuteActivity.class);
                        intent.putExtra(ConstantStr.KEY_BUNDLE_LONG, datas.get(position).getId());
                        startActivity(intent);
                    }
                }
            }
        });
        mPresenter.getData();
    }

    @Override
    public void onRefresh() {
        noDataLayout.setVisibility(View.GONE);
        datas.clear();
        recyclerView.getAdapter().notifyDataSetChanged();
        mPresenter.getData();
    }

    @Override
    public void setPresenter(TodayDoingContact.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.unSubscribe();
        }
    }

    @Override
    public void showLoading() {
        refreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void showData(List<TodayToDoBean> datas) {
        noDataLayout.setVisibility(View.GONE);
        this.datas.addAll(datas);
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void noData() {
        noDataLayout.setVisibility(View.VISIBLE);
    }
}
