package com.isuo.yw2application.view.main.alarm;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.isuo.yw2application.R;
import com.isuo.yw2application.app.Yw2Application;
import com.isuo.yw2application.mode.bean.check.FaultList;
import com.isuo.yw2application.view.base.MvpFragmentV4;
import com.sito.library.adapter.RVAdapter;

import java.util.ArrayList;
import java.util.List;

public class AlarmFragment extends MvpFragmentV4<AlarmContract.Presenter> implements View.OnClickListener, AlarmContract.View {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private List<FaultList> alarmList;

    public static AlarmFragment newInstance() {
        Bundle args = new Bundle();
        AlarmFragment fragment = new AlarmFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        alarmList = new ArrayList<>();
        new AlarmPresenter(Yw2Application.getInstance().getRepositoryComponent().getRepository(),
                Yw2Application.getInstance().getFaultRepositoryComponent().getRepository(), this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_alarm, container, false);
        rootView.findViewById(R.id.reportAlarmIv).setOnClickListener(this);
        swipeRefreshLayout = rootView.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorScheme(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mPresenter != null) {
                    mPresenter.getAlarmList();
                    mPresenter.getAlarmCount();
                }
            }
        });
        recyclerView = rootView.findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        RVAdapter<FaultList> adapter = new RVAdapter<FaultList>(recyclerView, this.alarmList, R.layout.item_alarm) {
            @Override
            public void showData(ViewHolder vHolder, FaultList data, int position) {

            }
        };
        recyclerView.setAdapter(adapter);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mPresenter != null) {
            mPresenter.getAlarmList();
            mPresenter.getAlarmCount();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.reportAlarmIv:
                break;
        }
    }

    @Override
    public void showLoading() {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showAlarmCount() {

    }

    @Override
    public void showFaultList(List<FaultList> list) {
        this.alarmList.clear();
        this.alarmList.addAll(list);
        recyclerView.getAdapter().notifyDataSetChanged();
    }


    @Override
    public void setPresenter(AlarmContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
