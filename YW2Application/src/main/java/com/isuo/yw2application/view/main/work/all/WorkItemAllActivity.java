package com.isuo.yw2application.view.main.work.all;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huxq17.handygridview.HandyGridView;
import com.isuo.yw2application.R;
import com.isuo.yw2application.app.Yw2Application;
import com.isuo.yw2application.common.ConstantStr;
import com.isuo.yw2application.mode.bean.PayMenuBean;
import com.isuo.yw2application.mode.bean.work.WorkItem;
import com.isuo.yw2application.view.base.BaseActivity;
import com.isuo.yw2application.view.main.work.all.widget.ShowWorkItemView;
import com.isuo.yw2application.view.main.work.all.widget.WorkItemGridView;
import com.isuo.yw2application.view.main.work.pay.PayActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 展示所有的工作
 * Created by zhangan on 2018/3/26.
 */

public class WorkItemAllActivity extends BaseActivity implements WorkItemAllContract.View, AllGridViewAdapter.IStateChange, GridViewAdapter.IShowWorkStateChange, PayGridViewAdapter.IStateChange {

    private WorkItemAllContract.Presenter mPresenter;
    private ArrayList<WorkItem> workAllItems;
    private ArrayList<WorkItem> showWorkItems;
    private ArrayList<WorkItem> payWorkItems;
    private ShowWorkItemView mHandyGridView;
    private WorkItemGridView allGridView;
    private WorkItemGridView payGridView;
    private GridViewAdapter adapter;
    private TextView tvNote, tvEdit, tvFinish;
    private boolean isEditState;
    private AllGridViewAdapter allGridViewAdapter;
    private PayGridViewAdapter payGridViewAdapter;
    private LinearLayout canBuyItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutAndToolbar(R.layout.activity_work_item_list, "功能选择");
        new WorkItemAllPresenter(Yw2Application.getInstance().getWorkRepositoryComponent().getRepository(), this);
        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_equipment, menu);
        PayMenuBean bean = Yw2Application.getInstance().getCurrentUser().getCustomerSetMenu();
        MenuItem menuItem = menu.getItem(0);
        if (menuItem != null) {
            menuItem.setTitle("当前为" + bean.getMenuName());
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        startActivity(new Intent(WorkItemAllActivity.this, PayActivity.class));
        return true;

    }

    private void init() {
        showWorkItems = new ArrayList<>();
        workAllItems = new ArrayList<>();
        payWorkItems = new ArrayList<>();
        mHandyGridView = findViewById(R.id.grid_tips);
        canBuyItem = findViewById(R.id.canBuyItem);
        allGridView = findViewById(R.id.gridView);
        payGridView = findViewById(R.id.payGridView);
        tvNote = findViewById(R.id.tvNote);
        tvEdit = findViewById(R.id.tvEdit);
        tvFinish = findViewById(R.id.finish);
        tvEdit.setOnClickListener(this);
        tvFinish.setOnClickListener(this);
        adapter = new GridViewAdapter(this, showWorkItems, R.layout.work_item, this);
        mHandyGridView.setAdapter(adapter);
        mHandyGridView.setAutoOptimize(false);
        mHandyGridView.setScrollSpeed(750);
        mHandyGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!isEditState && showWorkItems.get(position).getId() != -1) {
                    startActivity(WorkItemAllIntent.startWorkItem(WorkItemAllActivity.this, showWorkItems.get(position)));
                }
            }
        });
        allGridViewAdapter = new AllGridViewAdapter(this, workAllItems, R.layout.work_item, this);
        allGridView.setAdapter(allGridViewAdapter);
        allGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!isEditState) {
                    startActivity(WorkItemAllIntent.startWorkItem(WorkItemAllActivity.this, workAllItems.get(position)));
                }
            }
        });
        payGridViewAdapter = new PayGridViewAdapter(this, payWorkItems, R.layout.work_item, this);
        payGridView.setAdapter(payGridViewAdapter);
        payGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(WorkItemAllActivity.this, PayActivity.class));
            }
        });
        if (mPresenter != null) {
            mPresenter.getWorkItem();
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tvEdit:
                isEditState = true;
                setModeChange();
                break;
            case R.id.finish:
                if (mHandyGridView == null) break;
                isEditState = false;
                setModeChange();
                break;
        }
    }

    private void setModeChange() {
        for (int i = 0; i < workAllItems.size(); i++) {
            workAllItems.get(i).setEdit(isEditState);
        }
        for (int i = 0; i < showWorkItems.size(); i++) {
            showWorkItems.get(i).setEdit(isEditState);
        }
//        for (int i = 0; i < payWorkItems.size(); i++) {
//            payWorkItems.get(i).setEdit(isEditState);
//        }
        tvNote.setVisibility(isEditState ? View.VISIBLE : View.GONE);
        tvEdit.setVisibility(!isEditState ? View.VISIBLE : View.GONE);
        tvFinish.setVisibility(!isEditState ? View.GONE : View.VISIBLE);
        allGridViewAdapter.notifyDataSetChanged();
        payGridViewAdapter.notifyDataSetChanged();
        setMode(isEditState ? HandyGridView.MODE.TOUCH : HandyGridView.MODE.NONE);
    }


    private void setMode(HandyGridView.MODE mode) {
        mHandyGridView.setMode(mode);
        adapter.setInEditMode();
    }

    @Override
    public void toolBarClick() {
        if (sendEditWorkItems()) {
            super.toolBarClick();
        }
    }

    @Override
    public void onBackPressed() {
        if (sendEditWorkItems()) {
            super.onBackPressed();
        }
    }

    private boolean sendEditWorkItems() {
        if (isEditState) {
            isEditState = false;
            setModeChange();
            return false;
        }
        Intent intent = new Intent();
        if (mPresenter != null) {
            mPresenter.saveWorkItem(this.showWorkItems);
        }
        setResult(Activity.RESULT_OK, intent);
        return true;
    }

    @Override
    public void onItemMoved(int from, int to) {
        WorkItem s = showWorkItems.remove(from);
        showWorkItems.add(to, s);
    }

    @Override
    public void showMyWorkItemList(List<WorkItem> workItems) {
        int position = -1;
        for (int i = 0; i < workItems.size(); i++) {
            if (workItems.get(i).getId() == -1) {
                position = i;
            }
        }
        if (position != -1) {
            workItems.remove(position);
        }
        this.showWorkItems.clear();
        this.showWorkItems.addAll(workItems);
        this.adapter.setData(this.showWorkItems);
    }

    @Override
    public void showAllWorkItemList(List<WorkItem> workItems) {
        this.workAllItems.clear();
        this.workAllItems.addAll(workItems);
        for (int i = 0; i < showWorkItems.size(); i++) {
            showWorkItems.get(i).setAdd(true);
            for (int j = 0; j < workAllItems.size(); j++) {
                if (workAllItems.get(j).getId() == showWorkItems.get(i).getId()) {
                    workAllItems.get(j).setAdd(true);
                    break;
                }
            }
        }
        allGridViewAdapter.setData(this.workAllItems);
    }

    @Override
    public void showPayWorkItemList(List<WorkItem> workItems) {
        if (workItems.size() == 0) {
            return;
        }
        this.canBuyItem.setVisibility(View.VISIBLE);
        this.payWorkItems.clear();
        this.payWorkItems.addAll(workItems);
        for (int i = 0; i < showWorkItems.size(); i++) {
            showWorkItems.get(i).setAdd(true);
            for (int j = 0; j < payWorkItems.size(); j++) {
                if (payWorkItems.get(j).getId() == showWorkItems.get(i).getId()) {
                    payWorkItems.get(j).setAdd(true);
                    break;
                }
            }
        }
        payGridViewAdapter.setData(this.payWorkItems);
    }

    @Override
    public void setPresenter(WorkItemAllContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onShowWorkStateChange(int position) {
        if (showWorkItems.size() == 3) {
            Yw2Application.getInstance().showToast("首页最少保留3个工作");
        } else {
            for (int i = 0; i < workAllItems.size(); i++) {
                if (workAllItems.get(i).getId() == showWorkItems.get(position).getId()) {
                    workAllItems.get(i).setAdd(false);
                    break;
                }
            }
            showWorkItems.remove(position);
            adapter.setData(showWorkItems);
            allGridViewAdapter.setData(workAllItems);
        }
    }

    @Override
    public void onStateChange(int position) {
        if (!isEditState) {
            return;
        }
        if (workAllItems.get(position).isAdd()) {
            if (showWorkItems.size() == 3) {
                Yw2Application.getInstance().showToast("首页最少保留3个工作");
                return;
            }
            workAllItems.get(position).setAdd(false);
            for (int i = 0; i < showWorkItems.size(); i++) {
                if (showWorkItems.get(i).getId() == workAllItems.get(position).getId()) {
                    showWorkItems.remove(i);
                    break;
                }
            }
            adapter.setData(showWorkItems);
            allGridViewAdapter.setData(workAllItems);
        } else {
            if (showWorkItems.size() == 7) {
                Yw2Application.getInstance().showToast("首页最多添加7个工作");
            } else {
                workAllItems.get(position).setAdd(true);
                showWorkItems.add(workAllItems.get(position));
                adapter.setData(showWorkItems);
                allGridViewAdapter.setData(workAllItems);
            }
        }
    }

    @Override
    public void onPayStateChange(int position) {
        if (!isEditState) {
            return;
        }
        if (payWorkItems.get(position).isAdd()) {
            if (showWorkItems.size() == 3) {
                Yw2Application.getInstance().showToast("首页最少保留3个工作");
                return;
            }
            payWorkItems.get(position).setAdd(false);
            for (int i = 0; i < showWorkItems.size(); i++) {
                if (showWorkItems.get(i).getId() == payWorkItems.get(position).getId()) {
                    showWorkItems.remove(i);
                    break;
                }
            }
            adapter.setData(showWorkItems);
            payGridViewAdapter.setData(payWorkItems);
        } else {
            if (showWorkItems.size() == 7) {
                Yw2Application.getInstance().showToast("首页最多添加7个工作");
            } else {
                payWorkItems.get(position).setAdd(true);
                showWorkItems.add(payWorkItems.get(position));
                adapter.setData(showWorkItems);
                payGridViewAdapter.setData(payWorkItems);
            }
        }
    }
}
