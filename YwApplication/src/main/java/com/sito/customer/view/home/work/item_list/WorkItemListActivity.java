package com.sito.customer.view.home.work.item_list;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.huxq17.handygridview.HandyGridView;
import com.sito.customer.R;
import com.sito.customer.app.CustomerApp;
import com.sito.customer.common.ConstantStr;
import com.sito.customer.mode.bean.work.WorkItem;
import com.sito.customer.view.BaseActivity;
import com.sito.customer.view.home.work.WorkItemIntent;

import java.util.ArrayList;

/**
 * 展示所有的工作
 * Created by zhangan on 2018/3/26.
 */

public class WorkItemListActivity extends BaseActivity implements AllGridViewAdapter.IStateChange, GridViewAdapter.IShowWorkStateChange {

    private ArrayList<WorkItem> workItems;
    private ArrayList<WorkItem> showWorkItems;
    private HandyGridView mHandyGridView;
    private GridView gridView;
    private GridViewAdapter adapter;
    private TextView tvNote, tvEdit, tvFinish;
    private boolean isEditState;
    private AllGridViewAdapter allGridViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showWorkItems = getIntent().getParcelableArrayListExtra(ConstantStr.KEY_BUNDLE_LIST);
        workItems = getIntent().getParcelableArrayListExtra(ConstantStr.KEY_BUNDLE_LIST_1);
        for (int i = 0; i < showWorkItems.size(); i++) {
            showWorkItems.get(i).setAdd(true);
            for (int j = 0; j < workItems.size(); j++) {
                if (workItems.get(j).getId() == showWorkItems.get(i).getId()) {
                    workItems.get(j).setAdd(true);
                    break;
                }
            }
        }
        setLayoutAndToolbar(R.layout.activity_work_item_list, "所有工作");
        mHandyGridView = (HandyGridView) findViewById(R.id.grid_tips);
        gridView = (GridView) findViewById(R.id.gridView);
        tvNote = (TextView) findViewById(R.id.tvNote);
        tvEdit = (TextView) findViewById(R.id.tvEdit);
        tvFinish = (TextView) findViewById(R.id.finish);
        tvEdit.setOnClickListener(this);
        tvFinish.setOnClickListener(this);
        initView();
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
        for (int i = 0; i < workItems.size(); i++) {
            workItems.get(i).setEdit(isEditState);
        }
        for (int i = 0; i < showWorkItems.size(); i++) {
            showWorkItems.get(i).setEdit(isEditState);
        }
        tvNote.setVisibility(isEditState ? View.VISIBLE : View.GONE);
        tvEdit.setVisibility(!isEditState ? View.VISIBLE : View.GONE);
        tvFinish.setVisibility(!isEditState ? View.GONE : View.VISIBLE);
        allGridViewAdapter.notifyDataSetChanged();
        setMode(isEditState ? HandyGridView.MODE.TOUCH : HandyGridView.MODE.NONE);
    }

    private void initView() {
        adapter = new GridViewAdapter(this, showWorkItems, R.layout.work_item, this);
        mHandyGridView.setAdapter(adapter);
        mHandyGridView.setAutoOptimize(false);
        mHandyGridView.setScrollSpeed(750);
        mHandyGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!isEditState && showWorkItems.get(position).getId() != -1) {
                    startActivity(WorkItemIntent.startWorkItem(WorkItemListActivity.this, showWorkItems.get(position)));
                }
            }
        });
        allGridViewAdapter = new AllGridViewAdapter(this, workItems, R.layout.work_item, this);
        gridView.setAdapter(allGridViewAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!isEditState) {
                    startActivity(WorkItemIntent.startWorkItem(WorkItemListActivity.this, workItems.get(position)));
                }
            }
        });
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
        intent.putParcelableArrayListExtra(ConstantStr.KEY_BUNDLE_LIST, showWorkItems);
        setResult(Activity.RESULT_OK, intent);
        return true;
    }

    @Override
    public void onStateChange(int position) {
        if (!isEditState) {
            return;
        }
        if (workItems.get(position).isAdd()) {
            workItems.get(position).setAdd(false);
            for (int i = 0; i < showWorkItems.size(); i++) {
                if (showWorkItems.get(i).getId() == workItems.get(position).getId()) {
                    showWorkItems.remove(i);
                }
            }
            adapter.setData(showWorkItems);
            allGridViewAdapter.setData(workItems);
        } else {
            if (showWorkItems.size() == 8) {
                CustomerApp.getInstance().showToast("首页最多添加7个工作");
            } else {
                workItems.get(position).setAdd(true);
                showWorkItems.add(showWorkItems.size() - 1, workItems.get(position));
                adapter.setData(showWorkItems);
                allGridViewAdapter.setData(workItems);
            }
        }
    }

    @Override
    public void onShowWorkStateChange(int position) {
        if (showWorkItems.size() == 4) {
            CustomerApp.getInstance().showToast("首页最少保留3个工作");
        } else {
            for (int i = 0; i < workItems.size(); i++) {
                if (workItems.get(i).getId() == showWorkItems.get(position).getId()) {
                    workItems.get(i).setAdd(false);
                    break;
                }
            }
            showWorkItems.remove(position);
            adapter.setData(showWorkItems);
            allGridViewAdapter.setData(workItems);
        }
    }

    @Override
    public void onItemMoved(int from, int to) {
        WorkItem s = showWorkItems.remove(from);
        showWorkItems.add(to, s);
    }
}
