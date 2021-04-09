package com.isuo.yw2application.view.main.work.fire;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.isuo.yw2application.R;
import com.isuo.yw2application.common.ConstantStr;
import com.isuo.yw2application.mode.bean.equip.RoomBean;
import com.isuo.yw2application.mode.fire.FireBean;
import com.isuo.yw2application.mode.fire.FireListBean;
import com.isuo.yw2application.view.base.BaseActivity;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.sito.library.widget.PinnedHeaderExpandableListView;

import java.util.ArrayList;
import java.util.List;

public class FireActivity extends BaseActivity {

    private SwipeRefreshLayout swipeRefreshLayout;
    private PinnedHeaderExpandableListView expandableListView;
    private FireListAdapter fireAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutAndToolbar(R.layout.activity_fire, "小空间智能灭火装置管理系统");
        expandableListView = findViewById(R.id.expandableListView);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorScheme(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        findViewById(R.id.countTv).setOnClickListener(this);
        findViewById(R.id.typeTv).setOnClickListener(this);
        findViewById(R.id.roomTv).setOnClickListener(this);
        findViewById(R.id.stateTv).setOnClickListener(this);
        findViewById(R.id.timeTv).setOnClickListener(this);
        fireAdapter = new FireListAdapter(this, expandableListView
                , R.layout.item_equip_group
                , R.layout.item_equip_child);
        fireAdapter.setItemListener(new FireListAdapter.ItemClickListener() {
            @Override
            public void onItemClick(FireListBean equipBean, FireBean equipName) {
                Intent intent = new Intent(FireActivity.this, FireDetailActivity.class);
                intent.putExtra(ConstantStr.KEY_BUNDLE_STR, equipName.getEquipmentName());
                intent.putExtra(ConstantStr.KEY_BUNDLE_OBJECT, equipName);
                startActivity(intent);
            }
        });
        expandableListView.setAdapter(fireAdapter);
        String[] list = new String[]{"法士特办公楼1#", "法士特一分厂", "法士特二分厂", "法士特三分厂"};
        List<FireListBean> listBeanList = new ArrayList<>();
        for (int i = 0; i < list.length; i++) {
            FireListBean listBean = new FireListBean();
            ArrayList<FireBean> beans = new ArrayList<>();
            for (int j = 0; j < 4; j++) {
                FireBean fireBean = new FireBean();
                fireBean.setCreateTime(System.currentTimeMillis());
                fireBean.setDeleteState(0);
                fireBean.setEquipmentFsn("SN2020-" + "x" + (i + 1) + "-" + (j + 1));
                fireBean.setEquipmentId(j);
                fireBean.setEquipmentName("配电柜");
                fireBean.setEquipmentSn("PDG AB-0" + (j + 1));
                fireBean.setManufacturer("西安中微安控");
                fireBean.setManufactureTime(1586448000000L);
                fireBean.setNearTime(1617984000000L);
                fireBean.setExpireTime(1625846400000L);
                fireBean.setRoom1("A座");
                fireBean.setRoom2("负一层");
                fireBean.setRoom3("1#配电室");
                if (i == 0) {
                    fireBean.setItemNumber("ZMM-20A");
                }
                if (i == 1 || i == 2) {
                    fireBean.setItemNumber("ZMM-20B");
                }
                if (i == 3) {
                    fireBean.setItemNumber("ZMM-20C");
                }
                fireBean.setCount(4);
                fireBean.setEquipmentState(0);
                beans.add(fireBean);
            }
            listBean.setCreateTime(System.currentTimeMillis());
            listBean.setRoomId(i);
            listBean.setRoomName(list[i]);
            listBean.setEquipments(beans);
            listBeanList.add(listBean);
        }
        roomList.clear();
        equipmentTypeList.clear();
        roomList.add("全部");
        equipmentTypeList.add("全部");
        for (FireListBean bean : listBeanList) {
            roomList.add(bean.getRoomName());
            for (FireBean fb : bean.getEquipments()) {
                boolean canAdd = true;
                for (String type : equipmentTypeList) {
                    if (TextUtils.equals(type, fb.getItemNumber())) {
                        canAdd = false;
                    }
                }
                if (canAdd) {
                    equipmentTypeList.add(fb.getItemNumber());
                }
            }
        }
        fireAdapter.setData(listBeanList);

        TextView fire2Tv = findViewById(R.id.alarmTv2);
        TextView fire3Tv = findViewById(R.id.alarmTv3);
        TextView fire4Tv = findViewById(R.id.alarmTv4);
        TextView fire5Tv = findViewById(R.id.alarmTv5);
        fire2Tv.setText("16");
        fire3Tv.setText("12");
        fire4Tv.setText("4");
        fire5Tv.setText("0");

        TextView stateTv1 = findViewById(R.id.stateTv1);
        TextView stateTv2 = findViewById(R.id.stateTv2);
        TextView stateTv3 = findViewById(R.id.stateTv3);

        stateTv1.setText("16");
        stateTv2.setText("0");
        stateTv3.setText("0");
    }

    ArrayList<String> equipmentTypeList = new ArrayList<>();
    ArrayList<String> roomList = new ArrayList<>();

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.countTv:
                String[] list = new String[]{"默认", "从少到多", "从多到少"};
                new XPopup.Builder(this).atView(v).asBottomList("", list,
                        new OnSelectListener() {
                            @Override
                            public void onSelect(int position, String text) {
                                if (position == 0) {

                                } else if (position == 1) {

                                } else {

                                }
                            }
                        }).show();
                break;
            case R.id.typeTv:
                new XPopup.Builder(this).atView(v).asBottomList("", equipmentTypeList.toArray(new String[]{}),
                        new OnSelectListener() {
                            @Override
                            public void onSelect(int position, String text) {
                                if (position == 0) {

                                } else {

                                }
                            }
                        }).show();
                break;
            case R.id.roomTv:
                new XPopup.Builder(this).atView(v).asBottomList("", roomList.toArray(new String[]{}),
                        new OnSelectListener() {
                            @Override
                            public void onSelect(int position, String text) {
                                if (position == 0) {

                                } else {

                                }
                            }
                        }).show();
                break;
            case R.id.stateTv:
                String[] list1 = new String[]{"默认", "线上", "线下", "触发"};
                new XPopup.Builder(this).atView(v).asBottomList("", list1,
                        new OnSelectListener() {
                            @Override
                            public void onSelect(int position, String text) {
                                if (position == 0) {

                                } else if (position == 1) {

                                } else if (position == 2) {

                                } else {

                                }
                            }
                        }).show();
                break;
            case R.id.timeTv:
                String[] list2 = new String[]{"默认", "正常", "预警", "过期"};
                new XPopup.Builder(this).atView(v).asBottomList("", list2,
                        new OnSelectListener() {
                            @Override
                            public void onSelect(int position, String text) {
                                if (position == 0) {

                                } else if (position == 1) {

                                } else if (position == 2) {

                                } else {

                                }
                            }
                        }).show();
                break;
        }
    }
}
