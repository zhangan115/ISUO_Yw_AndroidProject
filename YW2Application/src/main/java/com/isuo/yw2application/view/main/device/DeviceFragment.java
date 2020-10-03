package com.isuo.yw2application.view.main.device;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.isuo.yw2application.R;
import com.isuo.yw2application.app.Yw2Application;
import com.isuo.yw2application.common.ConstantStr;
import com.isuo.yw2application.mode.bean.equip.EquipBean;
import com.isuo.yw2application.mode.bean.equip.EquipmentBean;
import com.isuo.yw2application.mode.bean.work.WorkItem;
import com.isuo.yw2application.view.base.MvpFragmentV4;
import com.isuo.yw2application.view.main.device.list.EquipListActivity;
import com.isuo.yw2application.view.main.device.search.EquipSearchActivity;
import com.isuo.yw2application.view.main.equip.archives.EquipmentArchivesActivity;
import com.isuo.yw2application.widget.WorkItemLayout;
import com.sito.library.widget.PinnedHeaderExpandableListView;

import java.util.ArrayList;
import java.util.List;

public class DeviceFragment extends MvpFragmentV4<DeviceContract.Presenter> implements DeviceContract.View, View.OnClickListener {

    private SwipeRefreshLayout swipeRefreshLayout;
    private PinnedHeaderExpandableListView expandableListView;
    private TextView deviceAllCountTv, deviceNormalCountTv;
    private EquipListAdapter equipAdapter;
    private List<EquipBean> mEquipBeen;

    public static DeviceFragment newInstance() {
        Bundle args = new Bundle();
        DeviceFragment fragment = new DeviceFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mEquipBeen = new ArrayList<>();
        new DevicePresenter(Yw2Application.getInstance().getRepositoryComponent().getRepository(), this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_device, container, false);
        rootView.findViewById(R.id.searchDeviceLayout).setOnClickListener(this);
        rootView.findViewById(R.id.deviceCountTv).setOnClickListener(this);
        expandableListView = rootView.findViewById(R.id.expandableListView);
        swipeRefreshLayout = rootView.findViewById(R.id.swipeRefreshLayout);
        deviceAllCountTv = rootView.findViewById(R.id.deviceAllCountTv);
        deviceNormalCountTv = rootView.findViewById(R.id.deviceNormalCountTv);
        swipeRefreshLayout.setColorScheme(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getEquipInfo();
            }
        });
        return rootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getView() != null) {
            ((WorkItemLayout) getView().findViewById(R.id.workItem1)).setContent(new WorkItem(0, "设备录入", R.drawable.drive_input));
            getView().findViewById(R.id.workItem1).setOnClickListener(this);
            ((WorkItemLayout) getView().findViewById(R.id.workItem2)).setContent(new WorkItem(1, "设备扫码", R.drawable.drive_scan));
            getView().findViewById(R.id.workItem2).setOnClickListener(this);
            ((WorkItemLayout) getView().findViewById(R.id.workItem3)).setContent(new WorkItem(2, "重点关注", R.drawable.focus_on));
            getView().findViewById(R.id.workItem3).setOnClickListener(this);
            equipAdapter = new EquipListAdapter(getActivity(), expandableListView
                    , R.layout.item_equip_group
                    , R.layout.item_equip_child);
            equipAdapter.setItemListener(new EquipListAdapter.ItemClickListener() {
                @Override
                public void onItemClick(EquipBean equipBean, EquipmentBean equipName) {
                    Intent intent = new Intent(getActivity(), EquipmentArchivesActivity.class);
                    intent.putExtra(ConstantStr.KEY_BUNDLE_OBJECT, equipName);
                    startActivity(intent);
                }
            });
            expandableListView.setAdapter(equipAdapter);
            mPresenter.getEquipInfo();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.searchDeviceLayout:
                Intent searchIntent = new Intent(getActivity(), EquipSearchActivity.class);
                startActivity(searchIntent);
                break;
            case R.id.deviceCountTv:
                break;
            case R.id.workItem1:
                break;
            case R.id.workItem2:
                break;
            case R.id.workItem3:
                break;
        }
    }

    @Override
    public void showData(List<EquipBean> list) {
        mEquipBeen.clear();
        mEquipBeen.addAll(list);
        equipAdapter.setData(mEquipBeen);
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
    public void noData() {

    }

    @Override
    public void setPresenter(DeviceContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
