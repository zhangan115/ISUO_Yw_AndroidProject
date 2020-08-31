package com.sito.customer.view.home.work;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sito.customer.R;
import com.sito.customer.app.CustomerApp;
import com.sito.customer.common.ConstantStr;
import com.sito.customer.mode.bean.work.WorkItem;
import com.sito.customer.view.MvpFragmentV4;
import com.sito.customer.view.home.discover.equiplist.EquipListActivity;
import com.sito.customer.view.home.work.await.AwaitActivity;
import com.sito.customer.view.home.work.increment.WorkIncrementActivity;
import com.sito.customer.view.home.work.inspection.WorkInspectionActivity;
import com.sito.customer.view.home.work.item_list.WorkItemListActivity;
import com.sito.customer.view.home.work.today.doing.TodayDoingActivity;
import com.sito.customer.view.home.work.today.fault.TodayFaultActivity;
import com.sito.customer.widget.CountWorkLayout;
import com.sito.library.adapter.RVAdapter;
import com.sito.library.utils.DataUtil;
import com.sito.library.utils.DisplayUtil;
import com.sito.library.widget.ExpendRecycleView;

import java.util.ArrayList;
import java.util.List;

/**
 * 工作
 */
public class WorkFragment extends MvpFragmentV4<WorkContract.Presenter> implements WorkContract.View, CountWorkLayout.ItemClickListener {

    private ExpendRecycleView mRecyclerView;
    private ArrayList<WorkItem> workItemList;
    private ArrayList<WorkItem> showWorkItemList;
    private int WORK_ITEM_CODE = 100;
    private LinearLayout llWorkCount;
    private SwipeRefreshLayout swipeRefreshLayout;

    public static WorkFragment newInstance() {
        Bundle args = new Bundle();
        WorkFragment fragment = new WorkFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        workItemList = new ArrayList<>();
        showWorkItemList = new ArrayList<>();
        new WorkPresenter(CustomerApp.getInstance().getWorkRepositoryComponent().getRepository(), this);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_work, container, false);
        TextView tvUserCustomerName = rootView.findViewById(R.id.titleId);
        //tvUserCustomerName.setText(CustomerApp.getInstance().getCurrentUser().getCustomer().getCustomerName());
        tvUserCustomerName.setText("优维+");
        mRecyclerView = rootView.findViewById(R.id.recycleViewId);
        llWorkCount = rootView.findViewById(R.id.llWorkCount);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        RVAdapter<WorkItem> adapter = new RVAdapter<WorkItem>(mRecyclerView, showWorkItemList, R.layout.work_item) {
            @Override
            public void showData(ViewHolder vHolder, WorkItem data, int position) {
                TextView workItemName = (TextView) vHolder.getView(R.id.tvWorkName);
                ImageView workItemIcon = (ImageView) vHolder.getView(R.id.ivWorkIcon);
                workItemIcon.setImageDrawable(findDrawById(data.getIcon()));
                workItemName.setText(data.getName());
            }
        };
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new RVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (showWorkItemList.get(position).getId() == -1) {
                    Intent workItemListInt = new Intent(getActivity(), WorkItemListActivity.class);
                    workItemListInt.putParcelableArrayListExtra(ConstantStr.KEY_BUNDLE_LIST, showWorkItemList);
                    workItemListInt.putParcelableArrayListExtra(ConstantStr.KEY_BUNDLE_LIST_1, workItemList);
                    startActivityForResult(workItemListInt, WORK_ITEM_CODE);
                } else {
                    startActivity(WorkItemIntent.startWorkItem(getActivity(), showWorkItemList.get(position)));
                }
            }
        });
        swipeRefreshLayout = rootView.findViewById(R.id.refreshLoadLayoutId);
        swipeRefreshLayout.setColorSchemeColors(findColorById(R.color.colorPrimary));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getWorkCount();
            }
        });
        mPresenter.getWorkItem();
        mPresenter.getWorkCount();
        return rootView;
    }

    @Override
    public void setPresenter(WorkContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showWorkCount(List<CountWorkLayout.CountWorkBean> countWorkBeans) {
        llWorkCount.removeAllViews();
        for (int i = 0; i < countWorkBeans.size(); i++) {
            CountWorkLayout layout = new CountWorkLayout(getActivity().getApplicationContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT
                    , LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 0, 0, DisplayUtil.dip2px(getActivity(), 7));
            layout.setLayoutParams(params);
            layout.setData(countWorkBeans.get(i), this);
            llWorkCount.addView(layout);
        }
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showWorkItemList(List<WorkItem> workItems) {
        showWorkItemList.clear();
        showWorkItemList.addAll(workItems);
        mRecyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        swipeRefreshLayout.setRefreshing(true);
        mPresenter.getWorkCount();
    }

    @Override
    public void showAllWorkItemList(List<WorkItem> items) {
        workItemList.clear();
        workItemList.addAll(items);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == WORK_ITEM_CODE && resultCode == Activity.RESULT_OK && data != null) {
            ArrayList<WorkItem> workItems = data.getParcelableArrayListExtra(ConstantStr.KEY_BUNDLE_LIST);
            showWorkItemList(workItems);
            if (workItems != null) {
                mPresenter.saveWorkItem(workItems);
            }
        }
    }

    @Override
    public void onItem(String type) {
        Intent intent = new Intent();
        switch (type) {
            case "1":
                intent.putExtra(ConstantStr.KEY_BUNDLE_STR, "巡检任务");
                intent.putExtra(ConstantStr.KEY_BUNDLE_INT, 3);
                intent.setClass(getActivity(), WorkInspectionActivity.class);
                break;
            case "2":
                intent.putExtra(ConstantStr.KEY_BUNDLE_STR, "巡检任务");
                intent.putExtra(ConstantStr.KEY_BUNDLE_INT, 2);
                intent.setClass(getActivity(), WorkInspectionActivity.class);
                break;
            case "3":
                intent.putExtra(ConstantStr.KEY_BUNDLE_STR, "巡检任务");
                intent.putExtra(ConstantStr.KEY_BUNDLE_INT, 1);
                intent.setClass(getActivity(), WorkInspectionActivity.class);
                break;
            case "4":
                intent.setClass(getActivity(), TodayDoingActivity.class);
                break;
            case "5":
                intent.setClass(getActivity(), TodayFaultActivity.class);
                intent.putExtra(ConstantStr.KEY_BUNDLE_STR, DataUtil.timeFormat(System.currentTimeMillis(), "yyyy-MM-dd"));
                break;
            case "6":
                intent.setClass(getActivity(), WorkIncrementActivity.class);
                intent.putExtra(ConstantStr.KEY_BUNDLE_STR, DataUtil.timeFormat(System.currentTimeMillis(), "yyyy-MM-dd"));
                break;
            case "7":
                intent.setClass(getActivity(), TodayFaultActivity.class);
                break;
            case "8":
                intent.setClass(getActivity(), EquipListActivity.class);
                intent.putExtra(ConstantStr.KEY_BUNDLE_BOOLEAN_1, true);
                intent.putExtra(ConstantStr.KEY_BUNDLE_BOOLEAN_2, true);
                break;
            case "9":
                intent.putExtra(ConstantStr.KEY_BUNDLE_BOOLEAN, true);
                intent.setClass(getActivity(), WorkIncrementActivity.class);
                break;
        }
        startActivity(intent);
    }
}
