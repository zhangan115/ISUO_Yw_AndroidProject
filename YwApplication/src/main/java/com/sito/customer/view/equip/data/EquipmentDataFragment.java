package com.sito.customer.view.equip.data;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sito.customer.R;
import com.sito.customer.common.ConstantStr;
import com.sito.customer.mode.bean.ChartData;
import com.sito.customer.mode.bean.check.CheckData;
import com.sito.customer.mode.bean.check.CheckValue;
import com.sito.customer.mode.bean.check.InspectionData;
import com.sito.customer.view.MvpFragment;
import com.sito.customer.view.equip.photo.EquipmentPhotoListActivity;
import com.sito.customer.view.photo.ViewPagePhotoActivity;
import com.sito.customer.widget.ChartDialog;
import com.sito.customer.widget.CheckDialog;
import com.sito.library.adapter.RVAdapter;
import com.sito.library.utils.GlideUtils;
import com.sito.library.widget.ExpendRecycleView;
import com.sito.library.widget.RecycleRefreshLoadLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 巡检数据
 * Created by zhangan on 2017/7/3.
 */

public class EquipmentDataFragment extends MvpFragment<EquipmentDataContact.Presenter> implements EquipmentDataContact.View, SwipeRefreshLayout.OnRefreshListener {

    private long mEquipId;
    private int mInsType;
    private ExpendRecycleView mExpendRecycleView;
    private RecycleRefreshLoadLayout mRecycleRefreshLoadLayout;
    private RelativeLayout mNoDataLayout;
    private List<CheckData> mCheckDatas;
    private boolean isRefresh;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mEquipId = getArguments().getLong(ConstantStr.KEY_BUNDLE_LONG);
    }


    public static EquipmentDataFragment newInstance(long equipId) {
        Bundle args = new Bundle();
        args.putLong(ConstantStr.KEY_BUNDLE_LONG, equipId);
        EquipmentDataFragment fragment = new EquipmentDataFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setPresenter(EquipmentDataContact.Presenter presenter) {
        mPresenter = presenter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fmg_recycle_with_bg, container, false);
        mExpendRecycleView = (ExpendRecycleView) rootView.findViewById(R.id.recycleViewId);
        mExpendRecycleView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        mRecycleRefreshLoadLayout = (RecycleRefreshLoadLayout) rootView.findViewById(R.id.refreshLoadLayoutId);
        mRecycleRefreshLoadLayout.setColorSchemeColors(findColorById(R.color.colorPrimary));
        mRecycleRefreshLoadLayout.setOnRefreshListener(this);
        mNoDataLayout = (RelativeLayout) rootView.findViewById(R.id.layout_no_data);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCheckDatas = new ArrayList<>();
        RVAdapter<CheckData> adapter = new RVAdapter<CheckData>(mExpendRecycleView, mCheckDatas, R.layout.item_equip_data) {
            @Override
            public void showData(ViewHolder vHolder, CheckData data, int position) {
                TextView mName = (TextView) vHolder.getView(R.id.tv_equip_check_name);
                TextView mValue = (TextView) vHolder.getView(R.id.tv_equip_check_value);
                TextView mHistory = (TextView) vHolder.getView(R.id.tv_equip_check_history);
                ImageView mImg = (ImageView) vHolder.getView(R.id.tv_equip_check_img);
                if (data.getInspectionType() == 2) {
                    mName.setVisibility(View.VISIBLE);
                    mName.setText(data.getInspectionName());
                    mValue.setText("巡检结果" + "：" + data.getValue() + data.getQuantityUnit());
                    mHistory.setVisibility(View.VISIBLE);
                    mImg.setVisibility(View.GONE);
                } else if (data.getInspectionType() == 1) {
                    mName.setVisibility(View.VISIBLE);
                    mName.setText(data.getInspectionName());
                    mValue.setText("巡检结果" + "：" + data.getValue());
                    mHistory.setVisibility(View.VISIBLE);
                    mImg.setVisibility(View.GONE);
                } else if (data.getInspectionType() == 3) {
                    mName.setVisibility(View.GONE);
                    mValue.setText(data.getInspectionName());
                    mHistory.setVisibility(View.GONE);
                    mImg.setVisibility(View.VISIBLE);
                    GlideUtils.ShowImage(getActivity(), data.getValue(), mImg, R.drawable.picture_default);
                } else if (data.getInspectionType() == 4) {
                    mName.setVisibility(View.VISIBLE);
                    mName.setText(data.getInspectionName());
                    mValue.setText("巡检结果" + "：" + data.getValue());
                    mHistory.setVisibility(View.VISIBLE);
                    mImg.setVisibility(View.GONE);
                } else if (data.getInspectionType() == 5) {
                    mName.setVisibility(View.GONE);
                    mValue.setText(data.getInspectionName());
                    mHistory.setVisibility(View.GONE);
                    mImg.setVisibility(View.VISIBLE);
                    GlideUtils.ShowImage(getActivity(), data.getValue(), mImg, R.drawable.picture_default);
                }
            }
        };
        mExpendRecycleView.setAdapter(adapter);
        adapter.setOnItemClickListener(new RVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (mCheckDatas.get(position).getInspectionType() == 3) {
                    String[] urls = new String[1];
                    urls[0] = mCheckDatas.get(position).getValue();
                    ViewPagePhotoActivity.startActivity(getActivity(), urls, 0);
                } else if (mCheckDatas.get(position).getInspectionType() == 1
                        || mCheckDatas.get(position).getInspectionType() == 2
                        || mCheckDatas.get(position).getInspectionType() == 4) {
                    mInsType = mCheckDatas.get(position).getInspectionType();
                    if (mPresenter != null) {
                        mPresenter.getCheckValue(mEquipId, mCheckDatas.get(position).getInspectionId());
                    }
                } else if (mCheckDatas.get(position).getInspectionType() == 5) {
                    Intent intent = new Intent(getActivity(),EquipmentPhotoListActivity.class);
                    intent.putExtra(ConstantStr.KEY_BUNDLE_LIST,mCheckDatas.get(position).getPhotos());
//                    ViewPagePhotoActivity.startActivity(getActivity(), mCheckDatas.get(position).getPhotos(), 0);
                    startActivity(intent);
                }
            }
        });
        if (mPresenter != null) {
            mPresenter.getCheckData(mEquipId);
        }
    }

    @Override
    public void showCheckData(InspectionData inspectionData) {
        mCheckDatas.clear();
        if (inspectionData.getPicList() != null && inspectionData.getPicList().size() > 0) {
            CheckData checkData = new CheckData();
            checkData.setInspectionName("巡检自拍");

            checkData.setInspectionType(5);
            ArrayList<String>  photoUrls = new ArrayList<>();
            for (int i = 0; i < inspectionData.getPicList().size(); i++) {
                photoUrls.add(inspectionData.getPicList().get(i).getInplacePicUrl());
            }
            String[] list = new String[inspectionData.getPicList().size()];
            Collections.reverse(photoUrls);
            if (!photoUrls.isEmpty()){
                checkData.setValue(photoUrls.get(0));
            }
            checkData.setPhotos(photoUrls.toArray(list));
            mCheckDatas.add(0, checkData);
        }
        mCheckDatas.addAll(inspectionData.getDataList());
        if (mCheckDatas.size() > 0) {
            mNoDataLayout.setVisibility(View.GONE);
        } else {
            noData();
        }
        mExpendRecycleView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void showCheckValue(CheckValue checkValue) {
        if (mInsType == 1 || mInsType == 4) {
            //列表
            new CheckDialog(getActivity(), checkValue).show();
        } else {
            //图表
            List<ChartData> chartDatas = new ArrayList<>();
            List<ChartData.Data> mDatas = new ArrayList<>();
            ChartData mData = new ChartData();
            for (int i = 0; i < checkValue.getValueList().size(); i++) {
                ChartData.Data data = new ChartData.Data();
                data.setDataValue(Float.valueOf(checkValue.getValueList().get(i).getValue()));
                mDatas.add(data);
            }
            mData.setData(mDatas);
            chartDatas.add(mData);
            new ChartDialog(getActivity(), chartDatas).show();
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
        mRecycleRefreshLoadLayout.setRefreshing(false);
        hideEvLoading();
    }

    @Override
    public void noData() {
        mNoDataLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onRefresh() {
        isRefresh = true;
        mNoDataLayout.setVisibility(View.GONE);
        mRecycleRefreshLoadLayout.setNoMoreData(false);
        if (mPresenter != null) {
            mPresenter.getCheckData(mEquipId);
        }
    }

}
