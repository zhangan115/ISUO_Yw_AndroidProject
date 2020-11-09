package com.isuo.yw2application.view.main.equip.record;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.isuo.yw2application.R;
import com.isuo.yw2application.app.Yw2Application;
import com.isuo.yw2application.common.ConstantStr;
import com.isuo.yw2application.mode.bean.overhaul.OverhaulBean;
import com.isuo.yw2application.view.base.MvpFragment;
import com.isuo.yw2application.view.main.task.overhaul.detail.OverhaulDetailActivity;
import com.sito.library.adapter.RVAdapter;
import com.sito.library.utils.DataUtil;
import com.sito.library.widget.ExpendRecycleView;
import com.sito.library.widget.RecycleRefreshLoadLayout;
import com.sito.library.widget.TextViewVertical;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangan on 2017/7/3.
 */

public class EquipmentRecordFragment extends MvpFragment<EquipmentRecordContact.Presenter> implements EquipmentRecordContact.View, SwipeRefreshLayout.OnRefreshListener, RecycleRefreshLoadLayout.OnLoadListener {

    private long mEquipId;
    private RelativeLayout mNoDataLayout;
    private ExpendRecycleView mExpendRecycleView;
    private RecycleRefreshLoadLayout mRecycleRefreshLoadLayout;

    private boolean isRefresh;
    private List<OverhaulBean> mList;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new EquipmentRecordPresenter(Yw2Application.getInstance().getEquipmentRepositoryComponent().getRepository(), this);
        mEquipId = getArguments().getLong(ConstantStr.KEY_BUNDLE_LONG);
    }


    public static EquipmentRecordFragment newInstance(long equipId) {
        Bundle args = new Bundle();
        args.putLong(ConstantStr.KEY_BUNDLE_LONG, equipId);
        EquipmentRecordFragment fragment = new EquipmentRecordFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fmg_equip_data, container, false);
        mExpendRecycleView = rootView.findViewById(R.id.recycleViewId);
        mNoDataLayout = rootView.findViewById(R.id.layout_no_data);
        mRecycleRefreshLoadLayout = rootView.findViewById(R.id.refreshLoadLayoutId);
        mExpendRecycleView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        mRecycleRefreshLoadLayout.setColorSchemeColors(findColorById(R.color.colorPrimary));
        mRecycleRefreshLoadLayout.setOnRefreshListener(this);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mList = new ArrayList<>();
        RVAdapter<OverhaulBean> adapter = new RVAdapter<OverhaulBean>(mExpendRecycleView, mList, R.layout.item_overhaul) {
            @Override
            public void showData(ViewHolder vHolder, OverhaulBean data, int position) {
                TextView tv_repair_name = (TextView) vHolder.getView(R.id.tv_repair_name);
                TextView tv_belong_place = (TextView) vHolder.getView(R.id.tv_belong_place);
                TextView tv_state = (TextView) vHolder.getView(R.id.tv_state);
                TextView tv_equip_name = (TextView) vHolder.getView(R.id.tv_equip_name);
                TextView tv_user = (TextView) vHolder.getView(R.id.tv_user);
                TextView tv_start_time = (TextView) vHolder.getView(R.id.tv_start_time);
                TextView tv_repair_result = (TextView) vHolder.getView(R.id.tv_repair_result);
                TextView tv_alarm = (TextView) vHolder.getView(R.id.tv_alarm);
                ImageView iv_state = (ImageView) vHolder.getView(R.id.iv_state);
                vHolder.getView(R.id.ll_start_task).setVisibility(View.GONE);
                if (data.getEquipment() != null) {
                    tv_equip_name.setText(data.getEquipment().getEquipmentName());
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
                            sb.append(" ");
                        }
                    }
                    tv_user.setText(sb.toString());
                }
                tv_repair_name.setText(data.getRepairName());
                String state;
                switch (data.getRepairState()) {
                    case 1:
                        state = "待开始";
                        tv_state.setTextColor(findColorById(R.color.color_not_start));
                        break;
                    case 2:
                        tv_state.setTextColor(findColorById(R.color.color_start));
                        state = "进行中";
                        break;
                    default:
                        tv_state.setTextColor(findColorById(R.color.color_finish));
                        state = "已完成";
                        break;
                }
                tv_state.setText(state);
                if (!TextUtils.isEmpty(Yw2Application.getInstance().getMapOption().get("4").get(String.valueOf(data.getRepairResult())))) {
                    tv_repair_result.setVisibility(View.VISIBLE);
                    tv_repair_result.setText(MessageFormat.format("检修结果:{0}"
                            , Yw2Application.getInstance().getMapOption().get("4").get(String.valueOf(data.getRepairResult()))));
                } else {
                    tv_repair_result.setVisibility(View.GONE);
                }
            }
        };
        mExpendRecycleView.setAdapter(adapter);
        adapter.setOnItemClickListener(new RVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                long repairId = mList.get(position).getRepairId();
                Intent intent = new Intent(getActivity(), OverhaulDetailActivity.class);
                intent.putExtra(ConstantStr.KEY_BUNDLE_LONG, repairId);
                startActivity(intent);
            }
        });
        if (mPresenter != null) {
            mPresenter.getOverByEId(mEquipId);
        }
    }

    @Override
    public void setPresenter(EquipmentRecordContact.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onRefresh() {
        if (mPresenter != null) {
            isRefresh = true;
            mNoDataLayout.setVisibility(View.GONE);
            mRecycleRefreshLoadLayout.setNoMoreData(false);
            mPresenter.getOverByEId(mEquipId);
        }
    }

    @Override
    public void onLoadMore() {
        if (isRefresh || mList.size() == 0) {
            return;
        }
        if (mPresenter != null) {
            mPresenter.getMoreOverByEId(mEquipId, (int) mList.get(mList.size() - 1).getRepairId());
        }
    }

    @Override
    public void showData(List<OverhaulBean> list) {
        if (isRefresh) {
            mList.clear();
            isRefresh = false;
        }
        mList.addAll(list);
        if (list.size() > 0) {
            mNoDataLayout.setVisibility(View.GONE);
        } else {
            noData();
        }
        mExpendRecycleView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void showMoreData(List<OverhaulBean> list) {
        mList.addAll(list);
        mExpendRecycleView.getAdapter().notifyDataSetChanged();
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
        mExpendRecycleView.getAdapter().notifyDataSetChanged();
        mNoDataLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void noMoreData() {
        mRecycleRefreshLoadLayout.setNoMoreData(true);
    }

    @Override
    public void hideLoadingMore() {
        mRecycleRefreshLoadLayout.loadFinish();
    }
}
