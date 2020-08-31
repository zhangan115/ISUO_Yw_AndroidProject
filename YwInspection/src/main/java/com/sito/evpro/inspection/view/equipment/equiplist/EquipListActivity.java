package com.sito.evpro.inspection.view.equipment.equiplist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sito.evpro.inspection.R;
import com.sito.evpro.inspection.app.InspectionApp;
import com.sito.evpro.inspection.common.ConstantInt;
import com.sito.evpro.inspection.common.ConstantStr;
import com.sito.evpro.inspection.mode.bean.equip.EquipBean;
import com.sito.evpro.inspection.mode.bean.inspection.EquipmentBean;
import com.sito.evpro.inspection.view.BaseActivity;
import com.sito.evpro.inspection.view.create.equipment.CreateEquipmentActivity;
import com.sito.evpro.inspection.view.equipment.archives.EquipmentArchivesActivity;
import com.sito.evpro.inspection.view.equipment.equiplist.search.EquipSearchActivity;
import com.sito.library.adapter.RVAdapter;
import com.sito.library.widget.ExpendRecycleView;
import com.sito.library.widget.PinnedHeaderExpandableListView;
import com.sito.library.widget.RecycleRefreshLoadLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/**
 * 设备界面
 */
public class EquipListActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, RecycleRefreshLoadLayout.OnLoadListener, EquipListContract.View {

    //view
    private PinnedHeaderExpandableListView mListView;
    private ExpendRecycleView mExpendRecycleView;
    private RecycleRefreshLoadLayout mRecycleRefreshLoadLayout;
    private LinearLayout mRoom;
    private LinearLayout mSearch;
    //data
    public static final String NAME = "NAME";
    public static final String TYPE = "TYPE";
    private RelativeLayout mNoDataLayout;
    private boolean isRefresh, isShowEquipment, isShowEquipmentInfo, editEquip;
    private int typeId;
    private int roomId;
    private String name, roomName;
    private List<EquipmentBean> mEquipmentBeen;
    private List<EquipBean> mEquipBeen;
    private long mTaskId;
    private EquipListAdapter equipAdapter;
    @Inject
    EquipListPresenter mEquipListPresenter;
    EquipListContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutAndToolbar(R.layout.activity_equip_list, "设备列表");
        DaggerEquipListComponent.builder().inspectionRepositoryComponent(InspectionApp.getInstance().getRepositoryComponent())
                .equipListModule(new EquipListModule(this)).build()
                .inject(this);
        mTaskId = getIntent().getLongExtra(ConstantStr.KEY_BUNDLE_LONG, 0);
        isShowEquipment = getIntent().getBooleanExtra(ConstantStr.KEY_BUNDLE_BOOLEAN, false);
        isShowEquipmentInfo = getIntent().getBooleanExtra(ConstantStr.KEY_BUNDLE_BOOLEAN_1, false);
        initView();
        initData();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slideinleft, R.anim.slideoutright);
    }

    private void initData() {
        mEquipBeen = new ArrayList<>();
        if (mTaskId == 0) {
            mPresenter.getEquipList();
        } else {
            mPresenter.getEquipByTaskId(mTaskId);
        }
        equipAdapter = new EquipListAdapter(this, mListView, R.layout.item_equip_group, R.layout.item_equip_child);
        equipAdapter.setData(mEquipBeen);
        mListView.setAdapter(equipAdapter);
        equipAdapter.setItemListener(new EquipListAdapter.ItemClickListener() {
            @Override
            public void onItemClick(EquipBean equipBean, EquipmentBean equipName) {
                if (isShowEquipment) {
                    Intent intent = new Intent(EquipListActivity.this, CreateEquipmentActivity.class);
                    intent.putExtra(ConstantStr.KEY_BUNDLE_OBJECT, equipName);
                    intent.putExtra(ConstantStr.KEY_BUNDLE_LONG, Long.valueOf(equipBean.getRoomId()));
                    intent.putExtra(ConstantStr.KEY_BUNDLE_STR, equipBean.getRoomName());
                    startActivity(intent);
                    editEquip = true;
                    return;
                }
                if (isShowEquipmentInfo) {
                    Intent intent = new Intent(EquipListActivity.this, EquipmentArchivesActivity.class);
                    intent.putExtra(ConstantStr.KEY_BUNDLE_OBJECT, equipName);
                    startActivity(intent);
                    return;
                }
                Intent intent = new Intent();
                if (TextUtils.isEmpty(equipName.getEquipmentSn())) {
                    intent.putExtra(NAME, equipName.getEquipmentName());
                } else {
                    intent.putExtra(NAME, equipName.getEquipmentName() + "(" + equipName.getEquipmentSn() + ")");
                }
                intent.putExtra(TYPE, equipName.getEquipmentId());
                setResult(RESULT_OK, intent);
                finish();
            }

        });
        mEquipmentBeen = new ArrayList<>();
        RVAdapter<EquipmentBean> adapter = new RVAdapter<EquipmentBean>(mExpendRecycleView, mEquipmentBeen, R.layout.item_equip_list) {
            @Override
            public void showData(ViewHolder vHolder, EquipmentBean data, int position) {
                LinearLayout ll = (LinearLayout) vHolder.getView(R.id.id_ll_bg);
                View line = vHolder.getView(R.id.id_item_line);
                if (mEquipmentBeen.size() > 1) {
                    if (position == 0) {
                        ll.setBackground(findDrawById(R.drawable.bg_up));
                        line.setVisibility(View.VISIBLE);
                    } else if (position == mEquipmentBeen.size() - 1) {
                        ll.setBackground(findDrawById(R.drawable.bg_down));
                        line.setVisibility(View.GONE);
                    } else {
                        ll.setBackground(findDrawById(R.drawable.bg_centre));
                        line.setVisibility(View.VISIBLE);
                    }
                } else {
                    ll.setBackground(findDrawById(R.drawable.bg));
                    line.setVisibility(View.GONE);
                }
                TextView name = (TextView) vHolder.getView(R.id.id_item_equip_name);
                name.setText(data.getEquipmentName() + (TextUtils.isEmpty(data.getEquipmentSn()) ? "" : "(" + data.getEquipmentSn() + ")"));
            }
        };
        mExpendRecycleView.setAdapter(adapter);
        adapter.setOnItemClickListener(new RVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (isShowEquipment) {
                    Intent intent = new Intent(EquipListActivity.this, CreateEquipmentActivity.class);
                    intent.putExtra(ConstantStr.KEY_BUNDLE_OBJECT, mEquipmentBeen.get(position));
                    intent.putExtra(ConstantStr.KEY_BUNDLE_LONG, Long.valueOf(roomId));
                    intent.putExtra(ConstantStr.KEY_BUNDLE_STR, roomName);
                    startActivity(intent);
                    editEquip = true;
                    return;
                }
                if (isShowEquipmentInfo) {
                    Intent intent = new Intent(EquipListActivity.this, EquipmentArchivesActivity.class);
                    intent.putExtra(ConstantStr.KEY_BUNDLE_OBJECT, mEquipmentBeen.get(position));
                    startActivity(intent);
                    return;
                }
                Intent intent = new Intent();
                if (TextUtils.isEmpty(mEquipmentBeen.get(position).getEquipmentSn())) {
                    intent.putExtra(NAME, mEquipmentBeen.get(position).getEquipmentName());
                } else {
                    intent.putExtra(NAME, mEquipmentBeen.get(position).getEquipmentName() + "(" + mEquipmentBeen.get(position).getEquipmentSn() + ")");
                }
                intent.putExtra(TYPE, mEquipmentBeen.get(position).getEquipmentId());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    public void onRefresh() {
        isRefresh = true;
        mNoDataLayout.setVisibility(View.GONE);
        mRecycleRefreshLoadLayout.setNoMoreData(false);
        if (mTaskId == 0) {
            Map<String, Object> map = new HashMap<>();
            map.put("count", ConstantInt.PAGE_SIZE);
            if (typeId != 0) {
                map.put("equipmentTypeId", typeId);
            }
            if (roomId != 0) {
                map.put("roomId", roomId);
            }
            if (!TextUtils.isEmpty(name)) {
                map.put("equipmentName", name);
            }
            mPresenter.getEquipList(map);
        } else {
            mPresenter.getEquipByTaskId(mTaskId);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (editEquip) {
            mPresenter.getEquipList();
        }
    }

    @Override
    public void onLoadMore() {
        if (mEquipmentBeen.size() > 1 && !isRefresh && mTaskId == 0) {
            Map<String, Object> map = new HashMap<>();
            map.put("count", ConstantInt.PAGE_SIZE);
            if (typeId != 0) {
                map.put("equipmentTypeId", typeId);
            }
            if (roomId != 0) {
                map.put("roomId", roomId);
            }
            if (!TextUtils.isEmpty(name)) {
                map.put("equipmentName", name);
            }
            map.put("lastId", mEquipmentBeen.get(mEquipmentBeen.size() - 1).getEquipmentId());
            mPresenter.getMoreEquipList(map);
        }
    }

    @Override
    public void showEquip(List<EquipmentBean> list) {
        mRoom.setVisibility(View.GONE);
        if (isRefresh) {
            mEquipmentBeen.clear();
            isRefresh = false;
        }
        mEquipmentBeen.addAll(list);
        if (list.size() > 0) {
            mSearch.setVisibility(View.VISIBLE);
            mNoDataLayout.setVisibility(View.GONE);
        } else {
            mSearch.setVisibility(View.GONE);
            noData();
        }
        mExpendRecycleView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void showMoreEquipment(List<EquipmentBean> list) {
        mEquipmentBeen.addAll(list);
        mExpendRecycleView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void showData(List<EquipBean> list) {
        mSearch.setVisibility(View.GONE);
        mEquipBeen.clear();
        mEquipBeen.addAll(list);
        equipAdapter.notifyDataSetChanged();
        if (list.size() > 0) {
            mNoDataLayout.setVisibility(View.GONE);
        } else {
            mRoom.setVisibility(View.GONE);
            noData();
        }
    }

    @Override
    public void showLoading() {
        if (!isRefresh) {
            showEvLoading();
        } else {
            mRecycleRefreshLoadLayout.setRefreshing(true);
        }
    }

    @Override
    public void hideLoading() {
        hideEvLoading();
        mRecycleRefreshLoadLayout.setRefreshing(false);
    }

    @Override
    public void noData() {
        mNoDataLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void noMoreData() {
        mRecycleRefreshLoadLayout.setNoMoreData(true);
    }

    @Override
    public void hideLoadingMore() {
        mRecycleRefreshLoadLayout.loadFinish();
    }

    @Override
    public void setPresenter(EquipListContract.Presenter presenter) {
        mPresenter = presenter;
    }


    private void initView() {
        mListView = (PinnedHeaderExpandableListView) findViewById(R.id.expandableListView);
        mSearch = (LinearLayout) findViewById(R.id.id_equip_search);
        mRoom = (LinearLayout) findViewById(R.id.id_equip_room);
        mRecycleRefreshLoadLayout = (RecycleRefreshLoadLayout) findViewById(R.id.refreshLoadLayoutId);
        mRecycleRefreshLoadLayout.setColorSchemeColors(findColorById(R.color.colorPrimary));
        mExpendRecycleView = (ExpendRecycleView) findViewById(R.id.recycleViewId);
        mNoDataLayout = (RelativeLayout) findViewById(R.id.layout_no_data);
        mExpendRecycleView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
        mRecycleRefreshLoadLayout.setOnLoadListener(this);
        mRecycleRefreshLoadLayout.setOnRefreshListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_equip, menu);
        return !isShowEquipment;
    }

    private final static int REQUEST_CODE = 1;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu) {
            Intent intent = new Intent(this, EquipSearchActivity.class);
            startActivityForResult(intent, REQUEST_CODE);
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            mEquipmentBeen.clear();
            Bundle bundle = data.getExtras();
            typeId = bundle.getInt(ConstantStr.KEY_BUNDLE_INT_1);
            roomId = bundle.getInt(ConstantStr.KEY_BUNDLE_INT_2);
            name = bundle.getString(ConstantStr.KEY_BUNDLE_STR);
            roomName = bundle.getString(ConstantStr.KEY_BUNDLE_STR_1);
            Map<String, Object> map = new HashMap<>();
            map.put("count", ConstantInt.PAGE_SIZE);
            if (typeId != 0) {
                map.put("equipmentTypeId", typeId);
            }
            if (roomId != 0) {
                map.put("roomId", roomId);
            }
            if (!TextUtils.isEmpty(name)) {
                map.put("equipmentName", name);
            }
            mPresenter.getEquipList(map);
        }
    }
}
