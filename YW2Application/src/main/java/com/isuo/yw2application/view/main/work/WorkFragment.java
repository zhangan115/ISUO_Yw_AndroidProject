package com.isuo.yw2application.view.main.work;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.isuo.yw2application.R;
import com.isuo.yw2application.app.Yw2Application;
import com.isuo.yw2application.mode.bean.work.WorkItem;
import com.isuo.yw2application.view.base.MvpFragmentV4;
import com.isuo.yw2application.widget.WorkItemLayout;

import java.util.ArrayList;
import java.util.List;

public class WorkFragment extends MvpFragmentV4<WorkContract.Presenter> implements WorkContract.View {

    private ArrayList<WorkItem> workItemList;
    private ArrayList<WorkItem> showWorkItemList;
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
        new WorkPresenter(Yw2Application.getInstance().getWorkRepositoryComponent().getRepository(), this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_work, container, false);
        swipeRefreshLayout = rootView.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeColors(findColorById(R.color.colorPrimary));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getWorkItem();
            }
        });
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter.getWorkItem();

    }

    @Override
    public void showWorkItemList(List<WorkItem> workItems) {
        if (workItems.size() == 8 && getView() != null) {
            ((WorkItemLayout) getView().findViewById(R.id.workItem1)).setContent(workItems.get(0));
            ((WorkItemLayout) getView().findViewById(R.id.workItem2)).setContent(workItems.get(1));
            ((WorkItemLayout) getView().findViewById(R.id.workItem3)).setContent(workItems.get(2));
            ((WorkItemLayout) getView().findViewById(R.id.workItem4)).setContent(workItems.get(3));
            ((WorkItemLayout) getView().findViewById(R.id.workItem5)).setContent(workItems.get(4));
            ((WorkItemLayout) getView().findViewById(R.id.workItem6)).setContent(workItems.get(5));
            ((WorkItemLayout) getView().findViewById(R.id.workItem7)).setContent(workItems.get(6));
            ((WorkItemLayout) getView().findViewById(R.id.workItem8)).setContent(workItems.get(7));
            WorkItem workItem9 = new WorkItem(9, "紧急电话", R.drawable.emergency_call);
            WorkItem workItem10 = new WorkItem(10, "安全制度管理", R.drawable.security);
            ((WorkItemLayout) getView().findViewById(R.id.workItem9)).setContent(workItem9);
            ((WorkItemLayout) getView().findViewById(R.id.workItem10)).setContent(workItem10);
        }
    }

    @Override
    public void showNews() {

    }

    @Override
    public void setPresenter(WorkContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
