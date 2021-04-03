package com.isuo.yw2application.view.main.device;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.zxing.client.android.CaptureActivity;
import com.isuo.yw2application.R;
import com.isuo.yw2application.app.Yw2Application;
import com.isuo.yw2application.common.ConstantStr;
import com.isuo.yw2application.mode.bean.PayMenuBean;
import com.isuo.yw2application.mode.bean.equip.EquipBean;
import com.isuo.yw2application.mode.bean.equip.EquipmentBean;
import com.isuo.yw2application.mode.bean.work.WorkItem;
import com.isuo.yw2application.view.base.MvpFragmentV4;
import com.isuo.yw2application.view.main.device.equipment.CreateEquipmentActivity;
import com.isuo.yw2application.view.main.device.list.EquipListActivity;
import com.isuo.yw2application.view.main.device.search.EquipSearchActivity;
import com.isuo.yw2application.view.main.equip.archives.EquipmentArchivesActivity;
import com.isuo.yw2application.view.main.work.pay.PayActivity;
import com.isuo.yw2application.widget.WorkItemLayout;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.qw.soul.permission.SoulPermission;
import com.qw.soul.permission.bean.Permission;
import com.qw.soul.permission.callbcak.CheckRequestPermissionListener;
import com.sito.library.widget.PinnedHeaderExpandableListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;

public class DeviceFragment extends MvpFragmentV4<DeviceContract.Presenter> implements DeviceContract.View, View.OnClickListener {

    private SwipeRefreshLayout swipeRefreshLayout;
    private PinnedHeaderExpandableListView expandableListView;
    private TextView deviceAllCountTv, deviceNormalCountTv;
    private EquipListAdapter equipAdapter;
    private List<EquipBean> mEquipBeen, mAllEquipmentBean;

    private final int SCANNER_CODE = 200;

    private String[] deviceCountItems = new String[]{"数量最多", "数量最少", "默认数量"};
    private ArrayList<String> deviceTypeItems;
    private ArrayList<String> deviceRoomItems;
    private ArrayList<String> deviceStateItems;

    private String roomName, typeName, stateName;
    private int deviceCountSort;

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
        deviceTypeItems = new ArrayList<>();
        mAllEquipmentBean = new ArrayList<>();
        deviceRoomItems = new ArrayList<>();
        deviceStateItems = new ArrayList<>();
        new DevicePresenter(Yw2Application.getInstance().getRepositoryComponent().getRepository(), this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_device, container, false);
        rootView.findViewById(R.id.searchDeviceLayout).setOnClickListener(this);
        rootView.findViewById(R.id.deviceCountTv).setOnClickListener(this);
        rootView.findViewById(R.id.type).setOnClickListener(this);
        rootView.findViewById(R.id.room).setOnClickListener(this);
        rootView.findViewById(R.id.state).setOnClickListener(this);
        rootView.findViewById(R.id.payTitle).setOnClickListener(this);
        TextView payTitle = rootView.findViewById(R.id.payTitle);
        PayMenuBean bean = Yw2Application.getInstance().getCurrentUser().getCustomerSetMenu();
        payTitle.setText(MessageFormat.format("当前为{0}", bean.getMenuName()));
        payTitle.setOnClickListener(this);
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
            ((WorkItemLayout) getView().findViewById(R.id.workItem1)).setContent(new WorkItem(0, "台账录入", R.drawable.drive_input));
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

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.searchDeviceLayout:
                Intent searchIntent = new Intent(getActivity(), EquipSearchActivity.class);
                startActivity(searchIntent);
                break;
            case R.id.payTitle:
                startActivity(new Intent(getActivity(), PayActivity.class));
                break;
            case R.id.deviceCountTv:
                new XPopup.Builder(getContext()).atView(view).asAttachList(deviceCountItems, new int[]{},
                        new OnSelectListener() {
                            @Override
                            public void onSelect(int position, String text) {
                                deviceCountSort = position;
                                sortDevice();
                            }
                        }).show();
                break;
            case R.id.type:
                if (deviceTypeItems.size() > 1) {
                    new XPopup.Builder(getContext()).atView(view).asBottomList("", deviceTypeItems.toArray(new String[deviceTypeItems.size()]),
                            new OnSelectListener() {
                                @Override
                                public void onSelect(int position, String text) {
                                    if (position == 0) {
                                        typeName = null;
                                    } else {
                                        typeName = text;
                                    }
                                    sortDevice();
                                }
                            }).show();
                }
                break;
            case R.id.room:
                if (deviceRoomItems.size() > 1) {
                    new XPopup.Builder(getContext()).atView(view).asBottomList("", deviceRoomItems.toArray(new String[deviceRoomItems.size()]),
                            new OnSelectListener() {
                                @Override
                                public void onSelect(int position, String text) {
                                    if (position == 0) {
                                        roomName = null;
                                    } else {
                                        roomName = text;
                                    }
                                    sortDevice();
                                }
                            }).show();
                }
                break;
            case R.id.state:
                if (deviceStateItems.size() > 1) {
                    new XPopup.Builder(getContext()).atView(view).asAttachList(deviceStateItems.toArray(new String[deviceStateItems.size()]), new int[]{},
                            new OnSelectListener() {
                                @Override
                                public void onSelect(int position, String text) {
                                    if (position == 0) {
                                        stateName = null;
                                    } else {
                                        stateName = text;
                                    }
                                    sortDevice();
                                }
                            }).show();
                }
                break;
            case R.id.workItem1:
                Intent createEquipIntent = new Intent(getActivity(), CreateEquipmentActivity.class);
                startActivity(createEquipIntent);
                break;
            case R.id.workItem2:
                SoulPermission.getInstance().checkAndRequestPermission(Manifest.permission.CAMERA,
                        new CheckRequestPermissionListener() {
                            @Override
                            public void onPermissionOk(Permission permission) {
                                startActivityForResult(new Intent(getActivity(), CaptureActivity.class), SCANNER_CODE);
                            }

                            @Override
                            public void onPermissionDenied(Permission permission) {
                                new AppSettingsDialog.Builder(getActivity())
                                        .setRationale(getString(R.string.need_camera_setting))
                                        .setTitle(getString(R.string.request_permissions))
                                        .setPositiveButton(getString(R.string.sure))
                                        .setNegativeButton(getString(R.string.cancel))
                                        .build()
                                        .show();
                            }
                        });
                break;
            case R.id.workItem3:
                Intent importIntent = new Intent(getActivity(), EquipListActivity.class);
                importIntent.putExtra(ConstantStr.KEY_BUNDLE_BOOLEAN_1, true);
                importIntent.putExtra(ConstantStr.KEY_BUNDLE_BOOLEAN_2, true);
                startActivity(importIntent);
                break;
        }
    }

    private void sortDevice() {
        mEquipBeen.clear();
        ArrayList<EquipBean> allEquip1 = new ArrayList<>();
        if (!TextUtils.isEmpty(roomName)) {
            for (EquipBean bean : mAllEquipmentBean) {
                if (TextUtils.equals(bean.getRoomName(), roomName)) {
                    allEquip1.add(bean);
                    break;
                }
            }
        } else {
            allEquip1.addAll(mAllEquipmentBean);
        }
        ArrayList<EquipBean> allEquip2 = new ArrayList<>();
        if (!TextUtils.isEmpty(typeName)) {
            for (EquipBean equipBean : allEquip1) {
                List<EquipmentBean> equipments = new ArrayList<>();
                for (EquipmentBean bean : equipBean.getEquipments()) {
                    String name = bean.getEquipmentType().getEquipmentTypeName();
                    if (TextUtils.equals(name, typeName)) {
                        equipments.add(bean);
                    }
                }
                if (equipments.size() > 0) {
                    equipBean.setEquipments(equipments);
                    allEquip2.add(equipBean);
                }
            }
        } else {
            allEquip2.addAll(allEquip1);
        }

        ArrayList<EquipBean> allEquip3 = new ArrayList<>();
        if (!TextUtils.isEmpty(stateName)) {
            for (EquipBean equipBean : allEquip2) {
                List<EquipmentBean> equipments = new ArrayList<>();
                for (EquipmentBean bean : equipBean.getEquipments()) {
                    String name = Yw2Application.getInstance()
                            .getMapOption().get("14").get(String.valueOf(bean.getEquipmentState()));

                    if (TextUtils.equals(name, stateName)) {
                        equipments.add(bean);
                    }
                }
                if (equipments.size() > 0) {
                    equipBean.setEquipments(equipments);
                    allEquip3.add(equipBean);
                }
            }
        } else {
            allEquip3.addAll(allEquip2);
        }
        mEquipBeen.addAll(allEquip3);
        if (deviceCountSort == 0) {
            Collections.sort(mEquipBeen, new Comparator<EquipBean>() {
                @Override
                public int compare(EquipBean bean1, EquipBean bean2) {
                    if (bean1.getEquipments() != null && bean2.getEquipments() != null) {
                        if (bean1.getEquipments().size() < bean2.getEquipments().size()) {
                            return 1;
                        } else {
                            return -1;
                        }
                    }
                    return 0;
                }
            });
        } else if (deviceCountSort == 1) {
            Collections.sort(mEquipBeen, new Comparator<EquipBean>() {
                @Override
                public int compare(EquipBean bean1, EquipBean bean2) {
                    if (bean1.getEquipments() != null && bean2.getEquipments() != null) {
                        if (bean1.getEquipments().size() < bean2.getEquipments().size()) {
                            return -1;
                        } else {
                            return 1;
                        }
                    }
                    return 0;
                }
            });
        }
        equipAdapter.setData(mEquipBeen);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == SCANNER_CODE) {
            if (data != null) {
                String result = data.getStringExtra(CaptureActivity.RESULT);
                if (TextUtils.isEmpty(result)){
                    Yw2Application.getInstance().showToast("未找到数据,请从新扫码");
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String type = jsonObject.getString("type");
                    long id = jsonObject.getLong("id");
                    if (TextUtils.equals("room", type)) {
                        Intent intent = new Intent(getActivity(), EquipListActivity.class);
                        intent.putExtra(ConstantStr.KEY_BUNDLE_BOOLEAN_3, true);
                        intent.putExtra(ConstantStr.KEY_BUNDLE_INT_2, id);
                        startActivity(intent);
                    } else if (TextUtils.equals("equipment", type)) {
                        Intent intent = new Intent(getActivity(), EquipmentArchivesActivity.class);
                        intent.putExtra(ConstantStr.KEY_BUNDLE_STR, String.valueOf(id));
                        startActivity(intent);
                    }else {
                        Yw2Application.getInstance().showToast("二维码不符合规范");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Yw2Application.getInstance().showToast("扫码失败");
                }
            }else {
                Yw2Application.getInstance().showToast("扫码失败");
            }
        }
    }

    @Override
    public void showData(List<EquipBean> list) {
        mEquipBeen.clear();
        mAllEquipmentBean.clear();
        mAllEquipmentBean.addAll(list);
        mEquipBeen.addAll(mAllEquipmentBean);
        this.deviceTypeItems.clear();
        this.deviceTypeItems.add("全部");
        this.deviceRoomItems.clear();
        this.deviceRoomItems.add("全部");
        this.deviceStateItems.clear();
        this.deviceStateItems.add("全部");

        int allDeviceCount = 0;
        int normalDeviceCount = 0;
        for (EquipBean bean : mAllEquipmentBean) {
            for (EquipmentBean equipment : bean.getEquipments()) {
                String equipmentTypeName = equipment.getEquipmentType().getEquipmentTypeName();
                String equipmentState = Yw2Application.getInstance()
                        .getMapOption().get("14").get(String.valueOf(equipment.getEquipmentState()));
                if (equipment.getEquipmentState() == 1 || TextUtils.isEmpty(equipmentState)) {
                    normalDeviceCount++;
                }
                if (!TextUtils.isEmpty(equipmentTypeName)) {
                    boolean haveName = false;
                    for (String name : deviceTypeItems) {
                        if (TextUtils.equals(name, equipmentTypeName)) {
                            haveName = true;
                            break;
                        }
                    }
                    if (!haveName) {
                        this.deviceTypeItems.add(equipmentTypeName);
                    }
                }
                if (!TextUtils.isEmpty(equipmentState)) {
                    boolean haveState = false;
                    for (String name : deviceStateItems) {
                        if (TextUtils.equals(name, equipmentState)) {
                            haveState = true;
                            break;
                        }
                    }
                    if (!haveState) {
                        this.deviceStateItems.add(equipmentState);
                    }
                }
            }
            String roomName = bean.getRoomName();
            if (!TextUtils.isEmpty(roomName)) {
                boolean haveName = false;
                for (String name : deviceRoomItems) {
                    if (TextUtils.equals(name, roomName)) {
                        haveName = true;
                        break;
                    }
                }
                if (!haveName) {
                    this.deviceRoomItems.add(roomName);
                }
            }

            allDeviceCount = allDeviceCount + bean.getEquipments().size();

        }
        this.deviceAllCountTv.setText(String.valueOf(allDeviceCount));
        this.deviceNormalCountTv.setText(String.valueOf(normalDeviceCount));
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
