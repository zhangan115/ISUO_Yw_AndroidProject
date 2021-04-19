package com.isuo.yw2application.view.main.work.fire;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.isuo.yw2application.R;
import com.isuo.yw2application.app.Yw2Application;
import com.isuo.yw2application.common.ConstantStr;
import com.isuo.yw2application.mode.IListCallBack;
import com.isuo.yw2application.mode.IObjectCallBack;
import com.isuo.yw2application.mode.bean.equip.RoomBean;
import com.isuo.yw2application.mode.fire.FireBean;
import com.isuo.yw2application.mode.fire.FireCountBean;
import com.isuo.yw2application.mode.fire.FireListBean;
import com.isuo.yw2application.view.base.BaseActivity;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.sito.library.widget.PinnedHeaderExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
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
        List<FireListBean> listBeanList = new ArrayList<>();
        roomList.clear();
        equipmentTypeList.clear();
        roomList.add("全部");
        equipmentTypeList.add("全部");
        Yw2Application.getInstance().getWorkRepositoryComponent().getRepository()
                .getFireCount(new HashMap<String, String>(), new IObjectCallBack<FireCountBean>() {
                    @Override
                    public void onSuccess(@NonNull FireCountBean bean) {
                        TextView fire2Tv = findViewById(R.id.alarmTv2);
                        TextView fire3Tv = findViewById(R.id.alarmTv3);
                        TextView fire4Tv = findViewById(R.id.alarmTv4);
                        TextView fire5Tv = findViewById(R.id.alarmTv5);
                        fire2Tv.setText(String.valueOf(bean.getAllCount()));
                        fire3Tv.setText(String.valueOf(bean.getOnLineCount()));
                        fire4Tv.setText(String.valueOf(bean.getLeaveCount()));
                        fire5Tv.setText(String.valueOf(bean.getTriggerCount()));
                    }

                    @Override
                    public void onError(String message) {

                    }

                    @Override
                    public void onFinish() {

                    }
                });
        Yw2Application.getInstance().getWorkRepositoryComponent().getRepository()
                .getFireRoomList(new HashMap<String, String>(), new IListCallBack<FireListBean>() {
                    @Override
                    public void onSuccess(@NonNull List<FireListBean> list) {
                        for (FireListBean bean : list) {
                            roomList.add(bean.getRoomName());
                            for (FireBean fb : bean.getEquipments()) {
                                fb.setRoomName(bean.getRoomName());
                                boolean canAdd = true;
                                for (String type : equipmentTypeList) {
                                    if (TextUtils.equals(type, fb.getEquipmentType())) {
                                        canAdd = false;
                                    }
                                }
                                if (canAdd) {
                                    equipmentTypeList.add(fb.getEquipmentType());
                                }
                            }
                        }
                        FireActivity.this.fireAdapter.setData(list);
                    }

                    @Override
                    public void onError(String message) {

                    }

                    @Override
                    public void onFinish() {

                    }
                });
        Yw2Application.getInstance().getWorkRepositoryComponent().getRepository()
                .getFireStateCount(new HashMap<String, String>(), new IObjectCallBack<FireCountBean>() {
                    @Override
                    public void onSuccess(@NonNull FireCountBean bean) {
                        TextView stateTv1 = findViewById(R.id.stateTv1);
                        TextView stateTv2 = findViewById(R.id.stateTv2);
                        TextView stateTv3 = findViewById(R.id.stateTv3);

                        stateTv1.setText(String.valueOf(bean.getNormalCount()));
                        stateTv2.setText(String.valueOf(bean.getWarningCount()));
                        stateTv3.setText(String.valueOf(bean.getExpireCount()));

                    }

                    @Override
                    public void onError(String message) {

                    }

                    @Override
                    public void onFinish() {

                    }
                });
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
