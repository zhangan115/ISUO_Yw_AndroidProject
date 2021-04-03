package com.isuo.yw2application.view.main.work.inject.detail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.isuo.yw2application.R;
import com.isuo.yw2application.app.Yw2Application;
import com.isuo.yw2application.common.ConstantInt;
import com.isuo.yw2application.common.ConstantStr;
import com.isuo.yw2application.mode.inject.InjectOilRepository;
import com.isuo.yw2application.mode.inject.bean.EquipmentInjectionOilBean;
import com.isuo.yw2application.mode.inject.bean.InjectEquipment;
import com.isuo.yw2application.mode.inject.bean.InjectEquipmentLog;
import com.isuo.yw2application.view.base.BaseActivity;
import com.isuo.yw2application.view.main.work.inject.oil.InjectOilActivity;
import com.sito.library.adapter.RVAdapter;
import com.sito.library.utils.DataUtil;
import com.sito.library.utils.DisplayUtil;
import com.sito.library.widget.ExpendRecycleView;
import com.sito.library.widget.RecycleRefreshLoadLayout;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 注油详情
 * Created by zhangan on 2018/4/12.
 */

public class InjectDetailActivity extends BaseActivity implements InjectDetailContract.View
        , SwipeRefreshLayout.OnRefreshListener, RecycleRefreshLoadLayout.OnLoadListener {

    private InjectDetailContract.Presenter mPresenter;
    private InjectEquipment data;
    private RelativeLayout noDataLayout;
    private ExpendRecycleView expendRecycleView;
    private RecycleRefreshLoadLayout loadLayout;
    private Map<String, String> map;
    private List<InjectEquipmentLog.ItemList> logList;
    private boolean isRefresh;
    private int defaultNeedInjectionDays = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutAndToolbar(R.layout.inject_detail_activitiy, "详情");
        new InjectDetailPresenter(InjectOilRepository.getRepository(this), this);
        mPresenter.subscribe();
        data = getIntent().getParcelableExtra(ConstantStr.KEY_BUNDLE_OBJECT);
        if (data == null) {
            finish();
            return;
        }
        if (Yw2Application.getInstance().getCurrentUser().getCustomer().getCustomerConfig() != null
                && Yw2Application.getInstance().getCurrentUser().getCustomer().getCustomerConfig().size() > 0) {
            for (int i = 0; i < Yw2Application.getInstance().getCurrentUser().getCustomer().getCustomerConfig().size(); i++) {
                if (Yw2Application.getInstance().getCurrentUser().getCustomer().getCustomerConfig().get(i).getConfigCode().equals("defaultNeedInjectionDays")) {
                    String time = Yw2Application.getInstance().getCurrentUser().getCustomer().getCustomerConfig().get(i).getConfigValue();
                    try {
                        defaultNeedInjectionDays = Integer.parseInt(time);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }
        loadLayout = findViewById(R.id.refreshLoadLayoutId);
        loadLayout.setColorSchemeColors(findColorById(R.color.colorPrimary));
        loadLayout.setOnRefreshListener(this);
        loadLayout.setOnLoadListener(this);
        noDataLayout = findViewById(R.id.layout_no_data);
        expendRecycleView = findViewById(R.id.recycleViewId);
        expendRecycleView.setLayoutManager(new GridLayoutManager(this, 1));
        expendRecycleView.setNestedScrollingEnabled(false);
        TextView equipmentName = findViewById(R.id.tvEquipmentName);
        TextView tvEquipmentSn = findViewById(R.id.tvEquipmentSn);
        TextView tvInjectState = findViewById(R.id.tvInjectState);
        TextView tvInjectTime = findViewById(R.id.tvInjectTime);
        TextView tvBackInject = findViewById(R.id.tvBackInject);
        TextView tvBeforeInject = findViewById(R.id.tvBeforeInject);
        equipmentName.setText(data.getEquipmentName());
        if (TextUtils.isEmpty(data.getEquipmentSn())) {
            tvEquipmentSn.setVisibility(View.INVISIBLE);
        } else {
            tvEquipmentSn.setVisibility(View.VISIBLE);
        }
        tvEquipmentSn.setText(data.getEquipmentSn());
        tvBeforeInject.setOnClickListener(this);
        tvBackInject.setOnClickListener(this);

        setInjectState(tvBeforeInject, data.getBeforeInjectionOil());
        setInjectState(tvBackInject, data.getBackInjectionOil());
        if (data.getInjectionOil() != null) {
            try {
                long time = System.currentTimeMillis();
                long aa = DataUtil.getDistanceDays(DataUtil.timeFormat(data.getInjectionOil().getNextTime(), null)
                        , DataUtil.timeFormat(time, null));
                if (time > data.getInjectionOil().getNextTime()) {
                    tvEquipmentSn.setTextColor(findColorById(R.color.colorTextRed));
                    tvInjectTime.setVisibility(View.VISIBLE);
                    tvInjectTime.setBackground(findDrawById(R.drawable.bg_shape_inject_warring_red));
                    tvInjectTime.setText("延期" + aa + "天");
                    tvInjectState.setTextColor(findColorById(R.color.colorTextRed));
                    tvInjectState.setText("急需注油");
                } else {
                    if (aa > defaultNeedInjectionDays) {
                        tvEquipmentSn.setTextColor(findColorById(R.color.color6F88A9));
                        tvInjectTime.setVisibility(View.GONE);
                        tvInjectState.setTextColor(findColorById(R.color.color6F88A9));
                        tvInjectState.setText("油量正常");
                    } else {
                        tvEquipmentSn.setTextColor(findColorById(R.color.colorTextYellow));
                        tvInjectTime.setVisibility(View.VISIBLE);
                        tvInjectTime.setBackground(findDrawById(R.drawable.bg_shape_inject_warring_yellow));
                        tvInjectTime.setText("剩余" + aa + "天");
                        tvInjectState.setTextColor(findColorById(R.color.colorTextYellow));
                        tvInjectState.setText("急需注油");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            tvInjectTime.setVisibility(View.GONE);
            tvInjectState.setTextColor(findColorById(R.color.color6F88A9));
            tvInjectState.setText("没有记录");
        }
        map = new HashMap<>();
        map.put("equipmentId", String.valueOf(data.getEquipmentId()));
        map.put("count", String.valueOf(ConstantInt.PAGE_SIZE));
        logList = new ArrayList<>();
        RVAdapter<InjectEquipmentLog.ItemList> adapter = new RVAdapter<InjectEquipmentLog.ItemList>(expendRecycleView
                , logList, R.layout.inject_log_item) {
            @Override
            public void showData(ViewHolder vHolder, InjectEquipmentLog.ItemList data, int position) {
                TextView injectName = (TextView) vHolder.getView(R.id.tvInjectTime);
                TextView tvUser = (TextView) vHolder.getView(R.id.tvUser);
                LinearLayout llItems = (LinearLayout) vHolder.getView(R.id.llItems);
                injectName.setText(MessageFormat.format("{0}{1}", DataUtil.timeFormat(data.getCreateTime(), "yyyy-MM-dd")
                        , data.getBeforeOrBack() == 1 ? "前轴注油时间" : "后轴注油时间"));
                tvUser.setText(data.getUser().getRealName());
                llItems.removeAllViews();
                if (data.getInjectionOilDataList() == null || data.getInjectionOilDataList().size() == 0) {
                    return;
                }
                for (int i = 0; i < data.getInjectionOilDataList().size(); i++) {
                    TextView textView = new TextView(InjectDetailActivity.this);
                    textView.setTextColor(findColorById(R.color.color6F88A9));
                    String content = null;
                    String unit = "";
                    if (!TextUtils.isEmpty(data.getInjectionOilDataList().get(i).getInjectionOilItem().getValueUnit())) {
                        unit = "(" + data.getInjectionOilDataList().get(i).getInjectionOilItem().getValueUnit() + ")";
                    }
                    switch (data.getInjectionOilDataList().get(i).getInjectionOilItem().getItemType()) {
                        case 1:
                            content = data.getInjectionOilDataList().get(i).getInjectionOilItem().getItemName() + ":"
                                    + data.getInjectionOilDataList().get(i).getTextValue();
                            break;
                        case 2:
                            content = data.getInjectionOilDataList().get(i).getInjectionOilItem().getItemName() + ":"
                                    + data.getInjectionOilDataList().get(i).getRadioContent();
                            break;
                        case 3:
                            content = data.getInjectionOilDataList().get(i).getInjectionOilItem().getItemName() + ":"
                                    + data.getInjectionOilDataList().get(i).getSelectContent();
                            break;
                        case 4:
                            content = data.getInjectionOilDataList().get(i).getInjectionOilItem().getItemName() + ":"
                                    + data.getInjectionOilDataList().get(i).getNumberValue() + unit;
                            break;
                        case 5:
                            content = data.getInjectionOilDataList().get(i).getInjectionOilItem().getItemName() + ":"
                                    + DataUtil.timeFormat(data.getInjectionOilDataList().get(i).getDateValue(), "yyyy-MM-dd");
                            break;
                    }
                    if (!TextUtils.isEmpty(content)) {
                        textView.setText(content);
                        llItems.addView(textView);
                    }
                }
            }
        };
        expendRecycleView.setAdapter(adapter);
        View headerView = LayoutInflater.from(this).inflate(R.layout.header_inject_log, null);
        expendRecycleView.addHeaderView(headerView);
        mPresenter.getInjectDetailList(map);
    }

    private int REQUEST_INJECT = 100;

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, InjectOilActivity.class);
        intent.putExtra(ConstantStr.KEY_BUNDLE_OBJECT, data);
        switch (v.getId()) {
            case R.id.tvBackInject:
                intent.putExtra(ConstantStr.KEY_BUNDLE_INT, 2);
                startActivityForResult(intent, REQUEST_INJECT);
                break;
            case R.id.tvBeforeInject:
                intent.putExtra(ConstantStr.KEY_BUNDLE_INT, 1);
                startActivityForResult(intent, REQUEST_INJECT);
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_INJECT && resultCode == Activity.RESULT_OK) {
            onRefresh();
        }
    }

    @Override
    public void showLoading() {
        showEvLoading();
        isRefresh = true;
    }

    @Override
    public void hideLoading() {
        hideEvLoading();
        loadLayout.setRefreshing(false);
        isRefresh = false;
    }

    @Override
    public void showInjectDetailData(List<InjectEquipmentLog.ItemList> list) {
        noDataLayout.setVisibility(View.GONE);
        logList.clear();
        logList.addAll(list);
        expendRecycleView.setVisibility(View.VISIBLE);
        expendRecycleView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void noData() {
        noDataLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void showMoreInjectDetailData(List<InjectEquipmentLog.ItemList> list) {
        logList.addAll(list);
        expendRecycleView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void noMoreData() {
        loadLayout.setNoMoreData(true);
    }

    @Override
    public void hideLoadingMore() {
        loadLayout.loadFinish();
    }

    @Override
    public void setPresenter(InjectDetailContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onRefresh() {
        logList.clear();
        expendRecycleView.getAdapter().notifyDataSetChanged();
        noDataLayout.setVisibility(View.GONE);
        map.remove("lastId");
        loadLayout.setNoMoreData(false);
        mPresenter.getInjectDetailList(map);
    }

    @Override
    public void onLoadMore() {
        if (isRefresh || logList.size() == 0) {
            return;
        }
        map.put("lastId", String.valueOf(logList.get(logList.size() - 1).getId()));
        mPresenter.getInjectDetailListMore(map);
    }

    private void setInjectState(TextView tvInject, EquipmentInjectionOilBean data) {
        if (data != null) {
            try {
                long time = System.currentTimeMillis();
                long aa = DataUtil.getDistanceDays(DataUtil.timeFormat(data.getNextTime(), null)
                        , DataUtil.timeFormat(time, null));
                if (time > data.getNextTime()) {
                    tvInject.setTextColor(findColorById(R.color.colorTextRed));
                    tvInject.setBackground(findDrawById(R.drawable.bg_shape_inject_red));
                } else {
                    if (aa > defaultNeedInjectionDays) {
                        tvInject.setTextColor(findColorById(R.color.color6F88A9));
                        tvInject.setBackground(findDrawById(R.drawable.bg_shape_inject_blue));
                    } else {
                        tvInject.setTextColor(findColorById(R.color.colorTextYellow));
                        tvInject.setBackground(findDrawById(R.drawable.bg_shape_inject_yellow));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            tvInject.setBackground(findDrawById(R.drawable.bg_shape_inject_blue));
            tvInject.setTextColor(findColorById(R.color.colorTextBlue));
        }
    }
}
