package com.isuo.yw2application.view.main.work.inject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.isuo.yw2application.R;
import com.isuo.yw2application.app.Yw2Application;
import com.isuo.yw2application.common.ConstantStr;
import com.isuo.yw2application.mode.inject.InjectOilRepository;
import com.isuo.yw2application.mode.inject.bean.EquipmentInjectionOilBean;
import com.isuo.yw2application.mode.inject.bean.InjectEquipment;
import com.isuo.yw2application.view.base.MvpFragment;
import com.isuo.yw2application.view.main.work.inject.detail.InjectDetailActivity;
import com.isuo.yw2application.view.main.work.inject.filter.InjectFilterActivity;
import com.isuo.yw2application.view.main.work.inject.oil.InjectOilActivity;
import com.sito.library.adapter.RVAdapter;
import com.sito.library.utils.DataUtil;
import com.sito.library.widget.ExpendRecycleView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 润滑管理
 * Created by zhangan on 2017/9/21.
 */

public class InjectFragment extends MvpFragment<InjectContract.Presenter> implements InjectContract.View, View.OnClickListener
        , SwipeRefreshLayout.OnRefreshListener {

    private ExpendRecycleView mExpendRecycleView;
    private EditText mSearchEditText;
    private ImageView cleanEditIv;
    private RelativeLayout noDataLayout;
    private SwipeRefreshLayout swipeRefreshLayout;
    //data
    private List<InjectEquipment> mEquipment;
    private List<InjectEquipment> mAllEquipment;
    private Map<String, String> map;
    private long roomId;
    private int defaultNeedInjectionDays = 3;

    public static InjectFragment newInstance(long roomId) {
        Bundle args = new Bundle();
        InjectFragment fragment = new InjectFragment();
        args.putLong(ConstantStr.KEY_BUNDLE_LONG, roomId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        map = new HashMap<>();
        new InjectPresenter(InjectOilRepository.getRepository(getActivity()), this);
        roomId = getArguments().getLong(ConstantStr.KEY_BUNDLE_LONG, -1);
        setHasOptionsMenu(roomId == -1);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_filter, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_filter) {
            Intent intent = new Intent(getActivity(), InjectFilterActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_inject, container, false);
        mExpendRecycleView = rootView.findViewById(R.id.recycleViewId);
        swipeRefreshLayout = rootView.findViewById(R.id.swipeLayout);
        mExpendRecycleView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        cleanEditIv = rootView.findViewById(R.id.iv_clean_edit);
        noDataLayout = rootView.findViewById(R.id.layout_no_data);
        cleanEditIv.setOnClickListener(this);
        mSearchEditText = rootView.findViewById(R.id.edit_search);
        mSearchEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mPresenter != null) {
                    mPresenter.searchEquipment(mAllEquipment, s.toString());
                }
                if (s.length() > 0) {
                    cleanEditIv.setVisibility(View.VISIBLE);
                } else {
                    cleanEditIv.setVisibility(View.GONE);
                }
            }
        });
        swipeRefreshLayout.setColorSchemeColors(findColorById(R.color.colorPrimary));
        swipeRefreshLayout.setOnRefreshListener(this);
        initData();
        if (roomId == -1) {
            map.put("needInjection", String.valueOf(1));
        } else {
            map.put("roomId", String.valueOf(roomId));
            map.put("needInjection", String.valueOf(0));
        }
        mPresenter.getRoomEquipmentList(map);
        return rootView;
    }


    private void initData() {
        mEquipment = new ArrayList<>();
        mAllEquipment = new ArrayList<>();
        RVAdapter<InjectEquipment> adapter = new RVAdapter<InjectEquipment>(mExpendRecycleView, mEquipment, R.layout.item_greas_list) {
            @Override
            public void showData(ViewHolder vHolder, InjectEquipment data, int position) {
                TextView equipmentName = (TextView) vHolder.getView(R.id.tvEquipmentName);
                TextView tvEquipmentSn = (TextView) vHolder.getView(R.id.tvEquipmentSn);
                TextView tvInjectState = (TextView) vHolder.getView(R.id.tvInjectState);
                TextView tvInjectTime = (TextView) vHolder.getView(R.id.tvInjectTime);
                TextView tvBackInject = (TextView) vHolder.getView(R.id.tvBackInject);
                TextView tvBeforeInject = (TextView) vHolder.getView(R.id.tvBeforeInject);
                View division_id = vHolder.getView(R.id.division_id);
                if (position == 0) {
                    division_id.setVisibility(View.GONE);
                } else {
                    division_id.setVisibility(View.GONE);
                }
                equipmentName.setText(data.getEquipmentName());
                if (TextUtils.isEmpty(data.getEquipmentSn())) {
                    tvEquipmentSn.setVisibility(View.INVISIBLE);
                } else {
                    tvEquipmentSn.setVisibility(View.VISIBLE);
                }
                tvEquipmentSn.setText(data.getEquipmentSn());
                tvBackInject.setTag(mEquipment.get(position));
                tvBackInject.setTag(R.id.tag_object, 2);
                tvBeforeInject.setTag(mEquipment.get(position));
                tvBeforeInject.setTag(R.id.tag_object, 1);
                tvBackInject.setOnClickListener(injectListener);
                tvBeforeInject.setOnClickListener(injectListener);
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
            }
        };
        mExpendRecycleView.setAdapter(adapter);
        adapter.setOnItemClickListener(new RVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), InjectDetailActivity.class);
                intent.putExtra(ConstantStr.KEY_BUNDLE_OBJECT, mEquipment.get(position));
                startActivity(intent);
            }
        });
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

    private int REQUEST_INJECT = 100;

    View.OnClickListener injectListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            InjectEquipment equipment = (InjectEquipment) v.getTag();
            int injectType = (int) v.getTag(R.id.tag_object);
            Intent intent = new Intent(getActivity(), InjectOilActivity.class);
            intent.putExtra(ConstantStr.KEY_BUNDLE_OBJECT, equipment);
            intent.putExtra(ConstantStr.KEY_BUNDLE_INT, injectType);
            startActivityForResult(intent, REQUEST_INJECT);
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_INJECT && resultCode == Activity.RESULT_OK) {
            mPresenter.getRoomEquipmentList(map);
        }
    }

    @Override
    public void setPresenter(InjectContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showLoading() {
        showEvLoading();
    }

    @Override
    public void hideLoading() {
        hideEvLoading();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void noData() {
        if (mEquipment.size() > 0) {
            mEquipment.clear();
            mExpendRecycleView.getAdapter().notifyDataSetChanged();
        }
        noDataLayout.setVisibility(View.VISIBLE);
        mExpendRecycleView.setVisibility(View.GONE);
    }

    @Override
    public void showRoomEquipment(List<InjectEquipment> injectEquipments) {
        mExpendRecycleView.setVisibility(View.VISIBLE);
        noDataLayout.setVisibility(View.GONE);
        mAllEquipment.clear();
        mAllEquipment.addAll(injectEquipments);
        mEquipment.clear();
        mEquipment.addAll(injectEquipments);
        mExpendRecycleView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void getEquipmentError() {

    }

    @Override
    public void showSearchInjectEqu(List<InjectEquipment> injectEquipments) {
        mEquipment.clear();
        noDataLayout.setVisibility(View.GONE);
        mExpendRecycleView.setVisibility(View.VISIBLE);
        mEquipment.addAll(injectEquipments);
        mExpendRecycleView.getAdapter().notifyDataSetChanged();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_clean_edit:
                mSearchEditText.setText("");
                mSearchEditText.setSelection(0);
                break;
        }
    }


    @Override
    public void onRefresh() {
        noDataLayout.setVisibility(View.GONE);
        mEquipment.clear();
        mExpendRecycleView.getAdapter().notifyDataSetChanged();
        mPresenter.getRoomEquipmentList(map);
    }
}
