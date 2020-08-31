package com.sito.customer.view.home.work.tool;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sito.customer.R;
import com.sito.customer.app.CustomerApp;
import com.sito.customer.common.ConstantStr;
import com.sito.customer.mode.tools.ToolsRepository;
import com.sito.customer.mode.tools.bean.Tools;
import com.sito.customer.view.BaseActivity;
import com.sito.customer.view.home.work.tool.add.AddToolsActivity;
import com.sito.customer.view.home.work.tool.borrow.BorrowToolsActivity;
import com.sito.customer.view.home.work.tool.detail.ToolsDetailActivity;
import com.sito.customer.view.home.work.tool.return_tools.ReturnToolsActivity;
import com.sito.library.adapter.RVAdapter;
import com.sito.library.utils.GlideUtils;
import com.sito.library.widget.ExpendRecycleView;
import com.sito.library.widget.RecycleRefreshLoadLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 工具
 * Created by zhangan on 2018/4/2.
 */

public class ToolsActivity extends BaseActivity implements ToolsContract.View, SwipeRefreshLayout.OnRefreshListener
        , RecycleRefreshLoadLayout.OnLoadListener {

    //view
    private ToolsContract.Presenter mPresenter;
    private EditText edit_search;
    private ExpendRecycleView recycleView;
    private RecycleRefreshLoadLayout recycleRefreshLoadLayout;
    private RelativeLayout noDataLayout;
    //data
    private List<Tools> datas;
    private Map<String, String> requestMap;
    private boolean isRefresh;
    private boolean isCustomer = false;
    private static final int REQUEST_BORROW = 100;
    private static final int REQUEST_RETURN = 101;
    private static final int REQUEST_DETAIL = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutAndToolbar(R.layout.tools_activity, "工具管理");
        new ToolsPresenter(ToolsRepository.getRepository(this), this);
        mPresenter.subscribe();
        initData();
        initView();
    }

    private void initData() {
        datas = new ArrayList<>();
        requestMap = new ArrayMap<>();
        if (CustomerApp.getInstance().getCurrentUser().getResTreeList() != null) {
            for (int i = 0; i < CustomerApp.getInstance().getCurrentUser().getResTreeList().size(); i++) {
                if (CustomerApp.getInstance().getCurrentUser().getResTreeList().get(i).getResource().getResourceUrl().equals("header.toolManagement")) {
                    isCustomer = true;
                    break;
                }
            }
        }
    }

    private void initView() {
        findViewById(R.id.tvSearch).setOnClickListener(this);
        edit_search = (EditText) findViewById(R.id.edit_search);
        edit_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    if (requestMap.containsKey("toolName")) {
                        requestMap.remove("toolName");
                    }
                    if (requestMap.containsKey("lastId")) {
                        requestMap.remove("lastId");
                    }
                    mPresenter.getToolsList(requestMap);
                }
            }
        });
        noDataLayout = (RelativeLayout) findViewById(R.id.layout_no_data);
        recycleRefreshLoadLayout = (RecycleRefreshLoadLayout) findViewById(R.id.refreshLoadLayoutId);
        recycleRefreshLoadLayout.setColorSchemeColors(findColorById(R.color.colorPrimary));
        recycleRefreshLoadLayout.setOnRefreshListener(this);
        recycleRefreshLoadLayout.setOnLoadListener(this);
        @SuppressLint("InflateParams")
        View loadFooterView = LayoutInflater.from(this).inflate(R.layout.view_load_more_inspection, null);
        recycleRefreshLoadLayout.setViewFooter(loadFooterView);
        recycleView = (ExpendRecycleView) findViewById(R.id.recycleViewId);
        recycleView.setLayoutManager(new GridLayoutManager(this, 1));
        RVAdapter<Tools> userAdapter = new RVAdapter<Tools>(recycleView, datas, R.layout.tools_use_item) {
            @Override
            public void showData(ViewHolder vHolder, Tools data, int position) {
                ImageView ivImage = (ImageView) vHolder.getView(R.id.ivTools);
                TextView tvToolsName = (TextView) vHolder.getView(R.id.tvToolsName);
                TextView tvToolsNum = (TextView) vHolder.getView(R.id.tvToolsNum);
                TextView tvToolsState = (TextView) vHolder.getView(R.id.tvToolsState);
                TextView tvToolsLocal = (TextView) vHolder.getView(R.id.tvToolsLocal);
                TextView tvToolsDes = (TextView) vHolder.getView(R.id.tvToolsDes);
                GlideUtils.ShowImage(ToolsActivity.this, data.getToolPic(), ivImage, R.drawable.picture_default);
                tvToolsDes.setText(data.getToolDesc());
                tvToolsName.setText(data.getToolName());
                tvToolsNum.setText(String.format("设备编号:%s", data.getToolNumber()));
                if (data.getIsUse().equals("0")) {
                    tvToolsLocal.setTextColor(findColorById(R.color.colorTextBlue));
                    tvToolsLocal.setText("在库");
                } else {
                    tvToolsLocal.setTextColor(findColorById(R.color.colorBtnYellowNormal));
                    tvToolsLocal.setText(String.format("借用人:%s", data.getUseUser()));
                }
                switch (data.getToolStatus()) {
                    case "1":
                        tvToolsState.setText("状态:正常");
                        tvToolsState.setTextColor(findColorById(R.color.colorTextGreen));
                        break;
                    case "2":
                        tvToolsState.setText("状态:遗失");
                        tvToolsState.setTextColor(findColorById(R.color.colorTextYellow));
                        break;
                    default:
                        tvToolsState.setText("状态:损坏");
                        tvToolsState.setTextColor(findColorById(R.color.colorTextRed));
                        break;
                }
            }
        };
        userAdapter.setOnItemClickListener(new RVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(ToolsActivity.this, ToolsDetailActivity.class);
                intent.putExtra(ConstantStr.KEY_BUNDLE_OBJECT, datas.get(position));
                startActivityForResult(intent,REQUEST_DETAIL);
            }
        });
        RVAdapter<Tools> customerAdapter = new RVAdapter<Tools>(recycleView, datas, R.layout.tools_customer_item) {
            @Override
            public void showData(ViewHolder vHolder, Tools data, int position) {
                ImageView ivImage = (ImageView) vHolder.getView(R.id.ivTools);
                TextView tvToolsName = (TextView) vHolder.getView(R.id.tvToolsName);
                TextView tvToolsNum = (TextView) vHolder.getView(R.id.tvToolsNum);
                TextView tvToolsState = (TextView) vHolder.getView(R.id.tvToolsState);
                TextView tvToolsBrowUser = (TextView) vHolder.getView(R.id.tvToolsBrowUser);
                TextView tvToolsLocal = (TextView) vHolder.getView(R.id.tvToolsLocal);
                TextView tvToolsDes = (TextView) vHolder.getView(R.id.tvToolsDes);

                TextView tvToolsBrow = (TextView) vHolder.getView(R.id.tvToolsBrow);
                TextView tvToolsReturn = (TextView) vHolder.getView(R.id.tvToolsReturn);
                TextView tvToolsAsk = (TextView) vHolder.getView(R.id.tvToolsAsk);

                GlideUtils.ShowImage(ToolsActivity.this, data.getToolPic(), ivImage, R.drawable.picture_default);
                tvToolsDes.setText(data.getToolDesc());
                tvToolsName.setText(data.getToolName());
                tvToolsNum.setText(String.format("设备编号:%s", data.getToolNumber()));
                if (data.getIsUse().equals("0")) {
                    tvToolsLocal.setTextColor(findColorById(R.color.colorTextBlue));
                    tvToolsLocal.setText("在库");
                    tvToolsBrowUser.setVisibility(View.GONE);
                } else {
                    tvToolsLocal.setTextColor(findColorById(R.color.colorBtnYellowNormal));
                    tvToolsLocal.setText("外借");
                    tvToolsBrowUser.setVisibility(View.VISIBLE);
                    tvToolsBrowUser.setText(String.format("借用人:%s", data.getUseUser()));
                }
                switch (data.getToolStatus()) {
                    case "1":
                        tvToolsState.setText("状态:正常");
                        tvToolsState.setTextColor(findColorById(R.color.colorTextGreen));
                        if (data.getIsUse().equals("0")) {
                            tvToolsBrow.setVisibility(View.VISIBLE);
                            tvToolsBrow.setTextColor(findColorById(R.color.colorTextBlue));
                            tvToolsBrow.setBackground(findDrawById(R.drawable.shape_tools_return));
                            tvToolsReturn.setVisibility(View.GONE);
                            tvToolsAsk.setVisibility(View.GONE);
                        } else {
                            tvToolsBrow.setVisibility(View.GONE);
                            tvToolsReturn.setVisibility(View.VISIBLE);
                            tvToolsAsk.setVisibility(View.VISIBLE);
                            if (data.getToolsLog() != null && data.getToolsLog().getPreReturnTime() < System.currentTimeMillis()) {
                                tvToolsAsk.setVisibility(View.VISIBLE);
                            } else {
                                tvToolsAsk.setVisibility(View.GONE);
                            }
                        }
                        break;
                    case "2":
                        tvToolsState.setText("状态:遗失");
                        tvToolsState.setTextColor(findColorById(R.color.colorTextYellow));
                        if (data.getIsUse().equals("0")) {
                            tvToolsBrow.setVisibility(View.VISIBLE);
                            tvToolsBrow.setTextColor(findColorById(R.color.colorTextBlue));
                            tvToolsBrow.setBackground(findDrawById(R.drawable.shape_tools_return));
                            tvToolsReturn.setVisibility(View.GONE);
                            tvToolsAsk.setVisibility(View.GONE);
                        } else {
                            tvToolsBrow.setVisibility(View.GONE);
                            tvToolsReturn.setVisibility(View.VISIBLE);
                            tvToolsAsk.setVisibility(View.VISIBLE);
                        }
                        break;
                    default:
                        tvToolsState.setText("状态:损坏");
                        tvToolsState.setTextColor(findColorById(R.color.colorTextRed));
                        if (data.getIsUse().equals("0")) {
                            tvToolsBrow.setVisibility(View.VISIBLE);
                            tvToolsBrow.setTextColor(findColorById(R.color.colorTextGray));
                            tvToolsBrow.setBackground(findDrawById(R.drawable.shape_tools_return));
                            tvToolsReturn.setVisibility(View.GONE);
                            tvToolsAsk.setVisibility(View.GONE);
                        } else {
                            tvToolsBrow.setVisibility(View.GONE);
                            tvToolsReturn.setVisibility(View.VISIBLE);
                            tvToolsAsk.setVisibility(View.VISIBLE);
                        }
                        break;
                }
                tvToolsBrow.setTag(R.id.tag_object, datas.get(position));
                tvToolsBrow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Tools tools = (Tools) v.getTag(R.id.tag_object);
                        Intent intent = new Intent(ToolsActivity.this, BorrowToolsActivity.class);
                        intent.putExtra(ConstantStr.KEY_BUNDLE_OBJECT, tools);
                        startActivityForResult(intent, REQUEST_BORROW);
                    }
                });
                tvToolsReturn.setTag(R.id.tag_object, datas.get(position));
                tvToolsReturn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Tools tools = (Tools) v.getTag(R.id.tag_object);
                        Intent intent = new Intent(ToolsActivity.this, ReturnToolsActivity.class);
                        intent.putExtra(ConstantStr.KEY_BUNDLE_OBJECT, tools);
                        startActivityForResult(intent, REQUEST_RETURN);
                    }
                });
            }
        };
        customerAdapter.setOnItemClickListener(new RVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(ToolsActivity.this, ToolsDetailActivity.class);
                intent.putExtra(ConstantStr.KEY_BUNDLE_OBJECT, datas.get(position));
                intent.putExtra(ConstantStr.KEY_BUNDLE_BOOLEAN, true);
                startActivityForResult(intent,REQUEST_DETAIL);
            }
        });
        requestMap.put("count", "20");
        if (isCustomer) {
            recycleView.setAdapter(customerAdapter);
            requestMap.put("isManager", "1");
        } else {
            recycleView.setAdapter(userAdapter);
            requestMap.put("isManager", "0");
        }
        mPresenter.getToolsList(requestMap);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_tools, menu);
        return isCustomer;
    }

    private int REQUEST_ADD = 100;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.tools_add) {
            startActivityForResult(new Intent(this, AddToolsActivity.class), REQUEST_ADD);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvSearch:
                String searchStr = edit_search.getText().toString();
                if (TextUtils.isEmpty(searchStr)) {
                    return;
                }
                if (requestMap.containsKey("lastId")) {
                    requestMap.remove("lastId");
                }
                requestMap.put("toolName", searchStr);
                mPresenter.getToolsList(requestMap);
                break;
        }
    }

    @Override
    public void setPresenter(ToolsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showTools(List<Tools> tools) {
        recycleRefreshLoadLayout.setNoMoreData(false);
        noDataLayout.setVisibility(View.GONE);
        datas.clear();
        datas.addAll(tools);
        recycleView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void showMoreTools(List<Tools> tools) {
        datas.addAll(tools);
        recycleView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void noData() {
        datas.clear();
        recycleRefreshLoadLayout.setNoMoreData(false);
        recycleView.getAdapter().notifyDataSetChanged();
        noDataLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void showMessage(String message) {
        CustomerApp.getInstance().showToast(message);
    }

    @Override
    public void showLoading() {
        recycleRefreshLoadLayout.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        isRefresh = false;
        recycleRefreshLoadLayout.setRefreshing(false);
    }

    @Override
    public void noMoreData() {
        recycleRefreshLoadLayout.setNoMoreData(true);
    }

    @Override
    public void hideLoadingMore() {
        recycleRefreshLoadLayout.loadFinish();
    }

    @Override
    public void onRefresh() {
        isRefresh = true;
        if (requestMap.containsKey("lastId")) {
            requestMap.remove("lastId");
        }
        mPresenter.getToolsList(requestMap);
    }

    @Override
    public void onLoadMore() {
        if (datas.size() == 0 || isRefresh) {
            return;
        }
        requestMap.put("lastId", String.valueOf(datas.get(datas.size() - 1).getToolId()));
        mPresenter.getToolsListMore(requestMap);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_BORROW && resultCode == Activity.RESULT_OK) {
            onRefresh();
        } else if (requestCode == REQUEST_ADD && resultCode == Activity.RESULT_OK) {
            onRefresh();
        } else if (requestCode == REQUEST_RETURN && resultCode == Activity.RESULT_OK) {
            onRefresh();
        } else if (requestCode == REQUEST_DETAIL && resultCode == Activity.RESULT_OK) {
            onRefresh();
        }
    }
}
