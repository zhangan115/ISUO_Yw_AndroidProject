package com.sito.customer.view.equip.archives;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sito.customer.R;
import com.sito.customer.app.CustomerApp;
import com.sito.customer.common.ConstantStr;
import com.sito.customer.mode.bean.equip.EquipmentBean;
import com.sito.customer.mode.bean.equip.FocusBean;
import com.sito.customer.view.MvpFragment;
import com.sito.customer.view.equip.alarm.EquipmentAlarmActivity;
import com.sito.customer.view.equip.data.EquipmentDataActivity;
import com.sito.customer.view.equip.record.EquipmentRecordActivity;
import com.sito.customer.view.equip.time.EquipmentTimeLineActivity;
import com.sito.customer.view.photo.ViewPagePhotoActivity;
import com.sito.customer.widget.EquipExpandItemLayout;
import com.sito.library.utils.DataUtil;
import com.sito.library.utils.GlideUtils;

/**
 * 设备档案
 * Created by zhangan on 2017/10/12.
 */

public class EquipmentArchivesFragment extends MvpFragment<EquipmentArchivesContract.Presenter>
        implements EquipmentArchivesContract.View, View.OnClickListener {

    private EquipmentBean bean;

    public static EquipmentArchivesFragment newInstance(EquipmentBean bean) {
        Bundle args = new Bundle();
        args.putParcelable(ConstantStr.KEY_BUNDLE_OBJECT, bean);
        EquipmentArchivesFragment fragment = new EquipmentArchivesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bean = getArguments().getParcelable(ConstantStr.KEY_BUNDLE_OBJECT);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fmg_equipment_archives, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (bean != null && mPresenter != null) {
            mPresenter.getEquipmentDetail(bean.getEquipmentId());
        }
    }

    private void setData(View rootView, final EquipmentBean bean) {
        rootView.findViewById(R.id.ll_1).setOnClickListener(this);
        rootView.findViewById(R.id.ll_2).setOnClickListener(this);
        rootView.findViewById(R.id.ll_3).setOnClickListener(this);
        rootView.findViewById(R.id.ll_4).setOnClickListener(this);
        rootView.findViewById(R.id.ll_5).setOnClickListener(this);
        rootView.findViewById(R.id.ll_6).setOnClickListener(this);
        if (bean.getRoom() != null) {
            ((TextView) rootView.findViewById(R.id.tv_equipment_room)).setText(bean.getRoom().getRoomName());
        }
        ((TextView) rootView.findViewById(R.id.tv_equipment_name)).setText(bean.getEquipmentName());
        if (!TextUtils.isEmpty(bean.getEquipmentSn())) {
            ((TextView) rootView.findViewById(R.id.tv_1)).setText(bean.getEquipmentSn());
        }
        if (bean.getEquipmentType() != null) {
            ((TextView) rootView.findViewById(R.id.tv_2)).setText(bean.getEquipmentType().getEquipmentTypeName());
        }
        if (!TextUtils.isEmpty(bean.getEquipmentFsn())) {
            ((TextView) rootView.findViewById(R.id.tv_3)).setText(bean.getEquipmentFsn());
        }
        if (!TextUtils.isEmpty(bean.getManufacturer())) {
            ((TextView) rootView.findViewById(R.id.tv_4)).setText(bean.getManufacturer());
        }
        if (!TextUtils.isEmpty(bean.getSupplier())) {
            ((TextView) rootView.findViewById(R.id.tv_5)).setText(bean.getSupplier());
        }
        if (bean.getWorkingState() != 0) {
            ((TextView) rootView.findViewById(R.id.tv_6)).setText(CustomerApp.getInstance()
                    .getMapOption().get("13").get(String.valueOf(bean.getWorkingState())));
        }
        if (bean.getEquipmentState() != 0) {
            ((TextView) rootView.findViewById(R.id.tv_7)).setText(CustomerApp.getInstance()
                    .getMapOption().get("14").get(String.valueOf(bean.getEquipmentState())));
        }
        if (bean.getInstallTime() != 0) {
            ((TextView) rootView.findViewById(R.id.tv_8)).setText(DataUtil.timeFormat(bean.getInstallTime(), null));
        }
        if (bean.getManufactureTime() != 0) {
            ((TextView) rootView.findViewById(R.id.tv_9)).setText(DataUtil.timeFormat(bean.getManufactureTime(), null));
        }
        if (bean.getStartTime() != 0) {
            ((TextView) rootView.findViewById(R.id.tv_10)).setText(DataUtil.timeFormat(bean.getStartTime(), null));
        }
        if (!TextUtils.isEmpty(bean.getEquipmentAlias())) {
            ((TextView) rootView.findViewById(R.id.tv_11)).setText(bean.getEquipmentAlias());
        }
        if (!TextUtils.isEmpty(bean.getNameplatePicUrl())) {
            rootView.findViewById(R.id.iv_1).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v.getId() == R.id.iv_1) {
                        ViewPagePhotoActivity.startActivity(getActivity(), new String[]{bean.getNameplatePicUrl()}, 0);
                    }
                }
            });
        } else {
            rootView.findViewById(R.id.iv_1).setVisibility(View.GONE);
        }
        GlideUtils.ShowImage(getActivity(), bean.getNameplatePicUrl(), ((ImageView) rootView.findViewById(R.id.iv_1)), R.drawable.picture_default);
        LinearLayout layout = rootView.findViewById(R.id.ll_expand_list);
        if (bean.getExpandList() != null && bean.getExpandList().size() > 0) {
            for (int i = 0; i < bean.getExpandList().size(); i++) {
                layout.addView(new EquipExpandItemLayout(getActivity()).setExpand(bean.getExpandList().get(i)));
            }
        }
    }

    @Override
    public void setPresenter(EquipmentArchivesContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.ll_1:
                intent = new Intent(getActivity(), EquipmentAlarmActivity.class);
                intent.putExtra(ConstantStr.KEY_BUNDLE_LONG, bean.getEquipmentId());
                break;
            case R.id.ll_2:
                intent = new Intent(getActivity(), EquipmentRecordActivity.class);
                intent.putExtra(ConstantStr.KEY_BUNDLE_LONG, bean.getEquipmentId());
                break;
            case R.id.ll_3:
                intent = new Intent(getActivity(), EquipmentDataActivity.class);
                intent.putExtra(ConstantStr.KEY_BUNDLE_LONG, bean.getEquipmentId());
                break;
            case R.id.ll_4:
                intent = new Intent(getActivity(), EquipmentTimeLineActivity.class);
                intent.putExtra(ConstantStr.KEY_BUNDLE_LONG, bean.getEquipmentId());
                intent.putExtra(ConstantStr.KEY_BUNDLE_INT, 1);
                break;
            case R.id.ll_5:
                intent = new Intent(getActivity(), EquipmentTimeLineActivity.class);
                intent.putExtra(ConstantStr.KEY_BUNDLE_LONG, bean.getEquipmentId());
                intent.putExtra(ConstantStr.KEY_BUNDLE_INT, 2);
                break;
            case R.id.ll_6:
                intent = new Intent(getActivity(), EquipmentTimeLineActivity.class);
                intent.putExtra(ConstantStr.KEY_BUNDLE_LONG, bean.getEquipmentId());
                intent.putExtra(ConstantStr.KEY_BUNDLE_INT, 3);
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }

    @Override
    public void showEquipmentDetail(EquipmentBean bean) {
        if (bean.getIsOnFocus() == 1) {
            mPresenter.getEquipmentCare(bean.getEquipmentId());
        }
        setData(getView(), bean);
    }

    @Override
    public void showCareData(FocusBean f) {
        if (getView() == null) return;
        getView().findViewById(R.id.ll_care).setVisibility(View.VISIBLE);
        TextView care_time = getView().findViewById(R.id.care_time);
        TextView care_des = getView().findViewById(R.id.care_des);
        care_time.setText("结束日期:" + DataUtil.timeFormat(f.getEndTime(), "yyy-MM-dd"));
        if (!TextUtils.isEmpty(f.getDescription())) {
            care_des.setText("内容描述:" + f.getDescription());
        } else {
            care_des.setVisibility(View.GONE);
        }
    }

    @Override
    public void showCareDataFail() {
        if (getView() == null) return;
        getView().findViewById(R.id.ll_care).setVisibility(View.GONE);
    }
}
