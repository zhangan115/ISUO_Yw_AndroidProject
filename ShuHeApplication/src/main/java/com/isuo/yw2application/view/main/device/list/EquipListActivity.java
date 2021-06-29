package com.isuo.yw2application.view.main.device.list;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.isuo.yw2application.R;
import com.isuo.yw2application.app.Yw2Application;
import com.isuo.yw2application.common.ConstantInt;
import com.isuo.yw2application.common.ConstantStr;
import com.isuo.yw2application.mode.bean.equip.EquipBean;
import com.isuo.yw2application.mode.bean.equip.EquipmentBean;
import com.isuo.yw2application.view.base.BaseActivity;
import com.isuo.yw2application.view.main.device.equipment.CreateEquipmentActivity;
import com.isuo.yw2application.view.main.device.search.EquipSearchActivity;
import com.isuo.yw2application.view.main.equip.archives.EquipmentArchivesActivity;
import com.sito.library.adapter.RVAdapter;
import com.sito.library.widget.ExpendRecycleView;
import com.sito.library.widget.PinnedHeaderExpandableListView1;
import com.sito.library.widget.RecycleRefreshLoadLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class EquipListActivity extends BaseActivity implements EquipListContract.View, SwipeRefreshLayout.OnRefreshListener
        , RecycleRefreshLoadLayout.OnLoadListener {
    //view
    private PinnedHeaderExpandableListView1 mListView;
    private EquipListAdapter equipAdapter;

    private RelativeLayout mNoDataLayout;
    private ExpendRecycleView mExpendRecycleView;
    private RecycleRefreshLoadLayout mRecycleRefreshLoadLayout;
    private LinearLayout mRoom;
    private LinearLayout mSearch;
    private boolean isRefresh;
    //data
    private List<EquipBean> mEquipBeen;
    private List<EquipmentBean> mEquipmentBeen;
    public static final String NAME = "NAME";
    public static final String TYPE = "TYPE";
    private int typeId;
    private int roomId;
    @Inject
    EquipListPresenter mEquipListPresenter;
    EquipListContract.Presenter mPresenter;
    private String name, roomName;
    private long mTaskId;
    private boolean isSearch;
    private boolean isShowEquipment;
    private boolean isShowEquipmentInfo;
    private boolean isFocusNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTaskId = getIntent().getLongExtra(ConstantStr.KEY_BUNDLE_LONG, 0);
        isSearch = getIntent().getBooleanExtra(ConstantStr.KEY_BUNDLE_BOOLEAN_3, false);
        isShowEquipment = getIntent().getBooleanExtra(ConstantStr.KEY_BUNDLE_BOOLEAN, false);
        isShowEquipmentInfo = getIntent().getBooleanExtra(ConstantStr.KEY_BUNDLE_BOOLEAN_1, false);
        isFocusNow = getIntent().getBooleanExtra(ConstantStr.KEY_BUNDLE_BOOLEAN_2, false);
        setLayoutAndToolbar(R.layout.activity_equip_list, isFocusNow ? "关注设备" : "设备列表");
        DaggerEquipListComponent.builder().customerRepositoryComponent(Yw2Application.getInstance().getRepositoryComponent())
                .equipListModule(new EquipListModule(this)).build()
                .inject(this);
        initView();
        initData();
    }

    private void initData() {
        mEquipBeen = new ArrayList<>();
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
                intent.putExtra(TYPE, String.valueOf(equipName.getEquipmentId()));
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
                        line.setVisibility(View.VISIBLE);
                    } else if (position == mEquipmentBeen.size() - 1) {
                        line.setVisibility(View.GONE);
                    } else {
                        line.setVisibility(View.VISIBLE);
                    }
                } else {
                    line.setVisibility(View.GONE);
                }
                TextView nameTv = (TextView) vHolder.getView(R.id.id_item_equip_name);
                String nameStr = data.getEquipmentName() + (TextUtils.isEmpty(data.getEquipmentSn()) ? "" : "(" + data.getEquipmentSn() + ")");
                nameTv.setText(nameStr);
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
        if (isSearch) {
            searchEquipment(getIntent());
            return;
        }
        if (mTaskId == 0) {
            mPresenter.getEquipInfo(isFocusNow);
        } else {
            mPresenter.getEquipByTaskId(mTaskId);
        }
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
    public void showMoreEquip(List<EquipmentBean> list) {
        mEquipmentBeen.addAll(list);
        mExpendRecycleView.getAdapter().notifyDataSetChanged();
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

    @Override
    public void onRefresh() {
        isRefresh = true;
        mNoDataLayout.setVisibility(View.GONE);
        mRecycleRefreshLoadLayout.setNoMoreData(false);
        if (typeId != 0 || roomId != 0 || !TextUtils.isEmpty(name)) {
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
            if (mTaskId != 0) {
                map.put("taskId", mTaskId);
            }
            map.put("lastId", mEquipmentBeen.get(mEquipmentBeen.size() - 1).getEquipmentId());
            mPresenter.getMoreEquipList(map);
        }
    }

    private void initView() {
        mListView = findViewById(R.id.expandableListView);
        mSearch = findViewById(R.id.id_equip_search);
        mRoom = findViewById(R.id.id_equip_room);
        mRecycleRefreshLoadLayout = findViewById(R.id.refreshLoadLayoutId);
        mRecycleRefreshLoadLayout.setColorSchemeColors(findColorById(R.color.colorPrimary));
        mExpendRecycleView = findViewById(R.id.recycleViewId);
        mNoDataLayout = findViewById(R.id.layout_no_data);
        mExpendRecycleView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
        @SuppressLint("InflateParams")
        View loadFooterView = LayoutInflater.from(this).inflate(R.layout.view_load_more_inspection, null);
        mRecycleRefreshLoadLayout.setViewFooter(loadFooterView);
        mRecycleRefreshLoadLayout.setOnRefreshListener(this);
        mRecycleRefreshLoadLayout.setOnLoadListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_equip, menu);
        return false;
    }

    private final static int REQUEST_CODE = 1;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu) {
            //搜索
            startActivityForResult(new Intent(this, EquipSearchActivity.class), REQUEST_CODE);
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            searchEquipment(data);
        }
    }

    private void searchEquipment(Intent data) {
        mEquipmentBeen.clear();
        Bundle bundle = data.getExtras();
        if (bundle == null) return;
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
