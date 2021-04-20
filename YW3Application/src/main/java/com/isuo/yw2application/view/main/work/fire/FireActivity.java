package com.isuo.yw2application.view.main.work.fire;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.sito.library.adapter.RVAdapter;
import com.sito.library.widget.PinnedHeaderExpandableListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class FireActivity extends BaseActivity {

    private SwipeRefreshLayout swipeRefreshLayout;
    private PinnedHeaderExpandableListView expandableListView;
    private FireListAdapter fireAdapter;
    private RecyclerView recyclerView;
    List<FireListBean> listBeanList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutAndToolbar(R.layout.activity_fire, "小空间智能灭火装置管理系统");
        expandableListView = findViewById(R.id.expandableListView);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        swipeRefreshLayout.setColorScheme(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                request();
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

        RVAdapter<FireBean> adapter = new RVAdapter<FireBean>(recyclerView, fireBeans, R.layout.item_equip_child) {
            @Override
            public void showData(ViewHolder vHolder, FireBean data, int position) {
                TextView equipmentNameTv = (TextView) vHolder.getView(R.id.id_item_equip_name);
                equipmentNameTv.setText(String.format("%s%s", data.getEquipmentName()
                        , TextUtils.isEmpty(data.getEquipmentModel()) ? ""
                                : "(" + data.getEquipmentModel() + ")"));
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new RVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                FireBean fireBean = fireBeans.get(position);
                Intent intent = new Intent(FireActivity.this, FireDetailActivity.class);
                intent.putExtra(ConstantStr.KEY_BUNDLE_STR, fireBean.getEquipmentName());
                intent.putExtra(ConstantStr.KEY_BUNDLE_OBJECT, fireBean);
                startActivity(intent);
            }
        });
        request();
    }

    private void request() {
        recyclerView.setVisibility(View.GONE);
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
                        swipeRefreshLayout.setRefreshing(false);
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
                        FireActivity.this.listBeanList.clear();
                        FireActivity.this.listBeanList.addAll(list);
                        FireActivity.this.fireAdapter.setData(list);
                    }

                    @Override
                    public void onError(String message) {

                    }

                    @Override
                    public void onFinish() {
                        swipeRefreshLayout.setRefreshing(false);
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
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
    }

    ArrayList<String> equipmentTypeList = new ArrayList<>();
    ArrayList<String> roomList = new ArrayList<>();
    ArrayList<FireBean> fireBeans = new ArrayList<>();

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.countTv:
                String[] list = new String[]{"默认", "从少到多", "从多到少"};
                new XPopup.Builder(this).atView(v).asBottomList("", list,
                        new OnSelectListener() {
                            @Override
                            public void onSelect(int position, String text) {
                                recyclerView.setVisibility(View.GONE);
                                if (position == 0) {
                                    FireActivity.this.fireAdapter.setData(FireActivity.this.listBeanList);
                                } else if (position == 1) {
                                    List<FireListBean> beanList = new ArrayList<>(FireActivity.this.listBeanList);
                                    Collections.sort(beanList);
                                    FireActivity.this.fireAdapter.setData(beanList);
                                } else {
                                    List<FireListBean> beanList = new ArrayList<>(FireActivity.this.listBeanList);
                                    Collections.sort(beanList);
                                    Collections.reverse(beanList);
                                    FireActivity.this.fireAdapter.setData(beanList);
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
                                    FireActivity.this.fireAdapter.setData(FireActivity.this.listBeanList);
                                    recyclerView.setVisibility(View.GONE);
                                } else {
                                    List<FireListBean> beanList = new ArrayList<>();
                                    for (FireListBean bean : FireActivity.this.listBeanList) {
                                        ArrayList<FireBean> fireBeans = new ArrayList<>();
                                        for (FireBean fb : bean.getEquipments()) {
                                            if (TextUtils.equals(fb.getEquipmentType(), text)) {
                                                fireBeans.add(fb);
                                            }
                                        }
                                        if (fireBeans.size() > 0) {
                                            FireListBean bean1 = new FireListBean();
                                            bean1.setRoomId(bean.getRoomId());
                                            bean1.setRoomName(bean.getRoomName());
                                            bean1.setEquipments(fireBeans);
                                            int count = 0;
                                            for (int i = 0; i < fireBeans.size(); i++) {
                                                count += fireBeans.get(i).getCount();
                                            }
                                            bean1.setCount(count);
                                            beanList.add(bean1);
                                        }
                                    }
                                    FireActivity.this.fireAdapter.setData(beanList);
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
                                    FireActivity.this.fireAdapter.setData(FireActivity.this.listBeanList);
                                    recyclerView.setVisibility(View.GONE);
                                } else {
                                    List<FireListBean> beanList = new ArrayList<>();
                                    for (FireListBean bean : FireActivity.this.listBeanList) {
                                        if (TextUtils.equals(bean.getRoomName(), text)) {
                                            beanList.add(bean);
                                        }
                                    }
                                    FireActivity.this.fireAdapter.setData(beanList);
                                }
                            }
                        }).show();
                break;
            case R.id.stateTv:
                String[] list1 = new String[]{"全部", "线上", "线下", "触发"};
                new XPopup.Builder(this).atView(v).asBottomList("", list1,
                        new OnSelectListener() {
                            @Override
                            public void onSelect(int position, String text) {
                                if (position == 0) {
                                    FireActivity.this.fireAdapter.setData(FireActivity.this.listBeanList);
                                    recyclerView.setVisibility(View.GONE);
                                } else if (position == 1 || position == 2) {
                                    List<FireListBean> beanList = new ArrayList<>();
                                    for (FireListBean bean : FireActivity.this.listBeanList) {
                                        ArrayList<FireBean> fireBeans = new ArrayList<>();
                                        for (FireBean fb : bean.getEquipments()) {
                                            if (fb.getPattern() == position) {
                                                fireBeans.add(fb);
                                            }
                                        }
                                        if (fireBeans.size() > 0) {
                                            FireListBean bean1 = new FireListBean();
                                            bean1.setRoomId(bean.getRoomId());
                                            bean1.setRoomName(bean.getRoomName());
                                            bean1.setEquipments(fireBeans);
                                            int count = 0;
                                            for (int i = 0; i < fireBeans.size(); i++) {
                                                count += fireBeans.get(i).getCount();
                                            }
                                            bean1.setCount(count);
                                            beanList.add(bean1);
                                        }
                                    }
                                    FireActivity.this.fireAdapter.setData(beanList);
                                } else {
                                    List<FireListBean> beanList = new ArrayList<>();
                                    for (FireListBean bean : FireActivity.this.listBeanList) {
                                        ArrayList<FireBean> fireBeans = new ArrayList<>();
                                        for (FireBean fb : bean.getEquipments()) {
                                            if (fb.getState() == 2) {
                                                fireBeans.add(fb);
                                            }
                                        }
                                        if (fireBeans.size() > 0) {
                                            FireListBean bean1 = new FireListBean();
                                            bean1.setRoomId(bean.getRoomId());
                                            bean1.setRoomName(bean.getRoomName());
                                            bean1.setEquipments(fireBeans);
                                            int count = 0;
                                            for (int i = 0; i < fireBeans.size(); i++) {
                                                count += fireBeans.get(i).getCount();
                                            }
                                            bean1.setCount(count);
                                            beanList.add(bean1);
                                        }
                                    }
                                    FireActivity.this.fireAdapter.setData(beanList);
                                }
                            }
                        }).show();
                break;
            case R.id.timeTv:
                String[] list2 = new String[]{"全部", "正常", "预警", "过期"};
                new XPopup.Builder(this).atView(v).asBottomList("", list2,
                        new OnSelectListener() {
                            @Override
                            public void onSelect(int position, String text) {
                                if (position == 0) {
                                    FireActivity.this.fireAdapter.setData(FireActivity.this.listBeanList);
                                } else {
                                    List<FireListBean> beanList = new ArrayList<>();
                                    for (FireListBean bean : FireActivity.this.listBeanList) {
                                        ArrayList<FireBean> fireBeans = new ArrayList<>();
                                        for (FireBean fb : bean.getEquipments()) {
                                            long current = System.currentTimeMillis();
                                            if (position == 1) {
                                                if (current < fb.getRemindTime()) {
                                                    fireBeans.add(fb);
                                                }
                                            } else if (position == 2) {
                                                if (current >= fb.getRemindTime() && current < fb.getExpireTime()) {
                                                    fireBeans.add(fb);
                                                }
                                            } else if (position == 3) {
                                                if (current >= fb.getExpireTime()) {
                                                    fireBeans.add(fb);
                                                }
                                            }
                                        }
                                        if (fireBeans.size() > 0) {
                                            bean.setEquipments(fireBeans);
                                            int count = 0;
                                            for (int i = 0; i < fireBeans.size(); i++) {
                                                count += fireBeans.get(i).getCount();
                                            }
                                            bean.setCount(count);
                                            beanList.add(bean);
                                        }
                                    }
                                    FireActivity.this.fireAdapter.setData(beanList);
                                }
                            }
                        }).show();
                break;
        }
    }
}
