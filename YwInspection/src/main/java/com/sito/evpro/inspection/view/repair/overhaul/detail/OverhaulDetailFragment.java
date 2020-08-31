package com.sito.evpro.inspection.view.repair.overhaul.detail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.sito.evpro.inspection.R;
import com.sito.evpro.inspection.app.InspectionApp;
import com.sito.evpro.inspection.common.ConstantStr;
import com.sito.evpro.inspection.mode.bean.overhaul.OverhaulBean;
import com.sito.evpro.inspection.view.MvpFragment;
import com.sito.evpro.inspection.view.speech.voice.MediaPlayerManager;
import com.sito.evpro.inspection.widget.PlaySoundLayout;
import com.sito.evpro.inspection.widget.ShowImageLayout;
import com.sito.library.utils.DataUtil;

import java.text.MessageFormat;

/**
 * 检修详情
 * Created by zhangan on 2017-07-17.
 */

public class OverhaulDetailFragment extends MvpFragment<OverhaulDetailContract.Presenter> implements OverhaulDetailContract.View {

    private long repairId;
    private OverhaulBean overhaulBean;
    private RelativeLayout noDataLayout;
    private ScrollView mScrollView;
    private LinearLayout card_view_repair;
    private PlaySoundLayout repairSoundLayout;
    private PlaySoundLayout alarmSoundLayout;

    public static OverhaulDetailFragment newInstance(long repairId) {
        Bundle args = new Bundle();
        args.putLong(ConstantStr.KEY_BUNDLE_LONG, repairId);
        OverhaulDetailFragment fragment = new OverhaulDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        repairId = getArguments().getLong(ConstantStr.KEY_BUNDLE_LONG);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_overhaul, container, false);
        noDataLayout = rootView.findViewById(R.id.layout_no_data);
        mScrollView = rootView.findViewById(R.id.scroll_view);
        mScrollView.setVisibility(View.GONE);
        card_view_repair = rootView.findViewById(R.id.card_view_repair);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mPresenter != null) {
            mPresenter.getRepairDetailData(String.valueOf(repairId));
        }
    }

    @Override
    public void showLoading() {
        showEvLoading();
    }

    @Override
    public void hideLoading() {
        hideEvLoading();
    }

    @Override
    public void showData(@NonNull OverhaulBean repairWorkBean) {
        overhaulBean = repairWorkBean;
        mScrollView.setVisibility(View.VISIBLE);
        if (getView() == null) {
            return;
        }
        TextView tv_start_time = getView().findViewById(R.id.tv_start_time);
        TextView tv_overhaul_time = getView().findViewById(R.id.tv_overhaul_time);
        TextView tv_state = getView().findViewById(R.id.tv_equip_state);
        TextView tv_repair_name = getView().findViewById(R.id.tv_repair_name);
        TextView tv_equip_name = getView().findViewById(R.id.tv_equip_name);
        TextView tv_equip_alias = getView().findViewById(R.id.tv_equip_alias);
        ShowImageLayout alarm_show_image = getView().findViewById(R.id.alarm_show_image);
        alarmSoundLayout = getView().findViewById(R.id.alarm_play_sound);

        if (overhaulBean.getStartTime() == 0) {
            tv_start_time.setVisibility(View.GONE);
        } else {
            tv_start_time.setVisibility(View.VISIBLE);
            tv_start_time.setText(MessageFormat.format("计划开始时间：{0}", DataUtil.timeFormat(overhaulBean.getStartTime(), null)));
        }
        if (overhaulBean.getEndTime() == 0) {
            tv_overhaul_time.setVisibility(View.GONE);
        } else {
            tv_overhaul_time.setVisibility(View.VISIBLE);
            tv_overhaul_time.setText(MessageFormat.format("计划截至时间：{0}", DataUtil.timeFormat(overhaulBean.getEndTime(), null)));
        }
        tv_state.setText(InspectionApp.getInstance().getMapOption().get("7").get(overhaulBean.getRepairState() + ""));
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < overhaulBean.getRepairUsers().size(); i++) {
            sb.append(overhaulBean.getRepairUsers().get(i).getUser().getRealName());
            if (i != overhaulBean.getRepairUsers().size() - 1) {
                sb.append(" ");
            }
        }
        tv_repair_name.setText(String.format("检修人：%s", sb.toString()));
        String equipmentName;
        if (TextUtils.isEmpty(overhaulBean.getEquipment().getEquipmentSn())) {
            equipmentName = overhaulBean.getEquipment().getEquipmentName();
        } else {
            equipmentName = overhaulBean.getEquipment().getEquipmentName() + "(" + overhaulBean.getEquipment().getEquipmentSn() + ")";
        }
        tv_equip_name.setText(equipmentName);
        tv_equip_alias.setText(String.format("设备别名:%s", TextUtils.isEmpty(overhaulBean.getEquipment().getEquipmentAlias())
                ? "" : overhaulBean.getEquipment().getEquipmentAlias()));
        String[] faultPicUrl;
        if (overhaulBean.getAddType() == 0) {
            faultPicUrl = new String[overhaulBean.getFault().getFaultPics().size()];
            for (int i = 0; i < overhaulBean.getFault().getFaultPics().size(); i++) {
                faultPicUrl[i] = overhaulBean.getFault().getFaultPics().get(i).getPicUrl();
            }
            alarmSoundLayout.setContent(overhaulBean.getFault().getVoiceUrl()
                    , overhaulBean.getFault().getSoundTimescale(), "故障内容", overhaulBean.getFault().getFaultDescript());
        } else {
            faultPicUrl = new String[overhaulBean.getRepairPicsAdd().size()];
            for (int i = 0; i < overhaulBean.getRepairPicsAdd().size(); i++) {
                faultPicUrl[i] = overhaulBean.getRepairPicsAdd().get(i).getPicUrl();
            }
            alarmSoundLayout.setContent(overhaulBean.getVoiceUrlAdd()
                    , overhaulBean.getSoundTimescaleAdd(), "工作内容", overhaulBean.getRepairIntro());
        }
        alarm_show_image.showImage(faultPicUrl);
        alarmSoundLayout.setOnPlaySoundListener(new PlaySoundLayout.OnPlaySoundListener() {
            @Override
            public void onPlaySound() {
                if (repairSoundLayout != null) {
                    repairSoundLayout.cancelPlay();
                }
            }
        });

        TextView tv_repair_result = getView().findViewById(R.id.tv_repair_result);
        //检修结果
        if (overhaulBean.getRepairState() < 3) {
            card_view_repair.setVisibility(View.GONE);
            return;
        }
        if (overhaulBean.getRepairResult() == 0) {
            tv_repair_result.setText("检修结果:");
        } else {
            tv_repair_result.setText(MessageFormat.format("检修结果:{0}"
                    , InspectionApp.getInstance().getMapOption().get("4").get(String.valueOf(overhaulBean.getRepairResult()))));
        }
        TextView tv_repair_time = getView().findViewById(R.id.id_repair_time);
        tv_repair_time.setText(MessageFormat.format("检修时间：{0}", DataUtil.timeFormat(overhaulBean.getCommitTime(), null)));
        String[] repairPicUr = new String[overhaulBean.getRepairPics().size()];
        for (int i = 0; i < overhaulBean.getRepairPics().size(); i++) {
            repairPicUr[i] = overhaulBean.getRepairPics().get(i).getPicUrl();
        }
        ShowImageLayout repair_show_image = getView().findViewById(R.id.repair_show_image);
        repair_show_image.showImage(repairPicUr);
        repairSoundLayout = getView().findViewById(R.id.repair_play_sound);
        repairSoundLayout.setContent(overhaulBean.getVoiceUrl(), overhaulBean.getSoundTimescale(), "结果描述", overhaulBean.getRepairRemark());
        repairSoundLayout.setOnPlaySoundListener(new PlaySoundLayout.OnPlaySoundListener() {
            @Override
            public void onPlaySound() {
                if (alarmSoundLayout != null) {
                    alarmSoundLayout.cancelPlay();
                }
            }
        });
    }

    @Override
    public void noData() {
        mScrollView.setVisibility(View.GONE);
        noDataLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void setPresenter(OverhaulDetailContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MediaPlayerManager.release();

    }

    @Override
    public void onPause() {
        super.onPause();
        if (alarmSoundLayout != null) {
            alarmSoundLayout.cancelPlay();
        }
        if (repairSoundLayout != null) {
            repairSoundLayout.cancelPlay();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MediaPlayerManager.resume();
    }
}
