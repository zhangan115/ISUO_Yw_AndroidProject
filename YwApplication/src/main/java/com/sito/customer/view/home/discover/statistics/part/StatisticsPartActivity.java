package com.sito.customer.view.home.discover.statistics.part;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sito.customer.R;
import com.sito.customer.app.CustomerApp;
import com.sito.customer.mode.bean.count.PartPersonStatistics;
import com.sito.customer.view.BaseActivity;
import com.sito.customer.widget.ChooseTimeLayout;
import com.sito.library.utils.DataUtil;

/**
 * 统计组织
 * Created by zhangan on 2018/4/17.
 */

public class StatisticsPartActivity extends BaseActivity implements ChooseTimeLayout.IChooseTime, StatisticsPartContract.View {

    private LinearLayout llChooseTime, llEmpty, llShowContent;
    private StatisticsPartContract.Presenter mPresenter;
    private TextView tvTime;
    private ImageView ivTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new StatisticsPartPresenter(CustomerApp.getInstance().getRepositoryComponent().getRepository(), this);
        setLayoutAndToolbar(R.layout.statistics_part_acivitiy, "组织");
        findViewById(R.id.llEmpty).setOnClickListener(this);
        findViewById(R.id.llOpenChooseTime).setOnClickListener(this);
        llChooseTime = (LinearLayout) findViewById(R.id.llChooseTime);
        llEmpty = (LinearLayout) findViewById(R.id.llEmpty);
        llShowContent = (LinearLayout) findViewById(R.id.llShowContent);
        tvTime = (TextView) findViewById(R.id.tvTime);
        ivTime = (ImageView) findViewById(R.id.ivTime);
        ChooseTimeLayout layout = new ChooseTimeLayout(this);
        layout.setChooseTimeListener(this, getSupportFragmentManager());
        llChooseTime.addView(layout);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llEmpty:
                llChooseTime.setVisibility(View.GONE);
                llEmpty.setVisibility(View.GONE);
                ivTime.setImageDrawable(findDrawById(R.drawable.drop_down));
                break;
            case R.id.llOpenChooseTime:
                if (llChooseTime.getVisibility() == View.VISIBLE) {
                    llChooseTime.setVisibility(View.GONE);
                    llEmpty.setVisibility(View.GONE);
                    ivTime.setImageDrawable(findDrawById(R.drawable.drop_down));
                } else {
                    llChooseTime.setVisibility(View.VISIBLE);
                    llEmpty.setVisibility(View.VISIBLE);
                    ivTime.setImageDrawable(findDrawById(R.drawable.pack_up));
                }
                break;
        }
    }

    @Override
    public void onTimeChange(String startTime, String endTime, String title) {
        llChooseTime.setVisibility(View.GONE);
        llEmpty.setVisibility(View.GONE);
        ivTime.setImageDrawable(findDrawById(R.drawable.drop_down));
        tvTime.setText(title);
        mPresenter.getCStatisticsPartData(startTime, endTime);
    }

    @Override
    public void setPresenter(StatisticsPartContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showData(PartPersonStatistics statistics) {
        ((TextView) findViewById(R.id.tv_1_1_1)).setText("日检 " + statistics.getTaskStat().getDayFinishCount() + "/" + statistics.getTaskStat().getDayAllCount());
        ((TextView) findViewById(R.id.tv_1_1_2)).setText(statistics.getTaskStat().getDayEquipmentAvg() + "/次");
        ((TextView) findViewById(R.id.tv_1_1_3)).setText("平均" + statistics.getTaskStat().getDayMinutesAvg() + "分钟/台");
        ((TextView) findViewById(R.id.tv_1_2_1)).setText("周检 " + statistics.getTaskStat().getWeekFinishCount() + "/" + statistics.getTaskStat().getWeekAllCount());
        ((TextView) findViewById(R.id.tv_1_2_2)).setText(statistics.getTaskStat().getWeekEquipmentAvg() + "/次");
        ((TextView) findViewById(R.id.tv_1_2_3)).setText("平均" + statistics.getTaskStat().getWeekMinutesAvg() + "分钟/台");
        ((TextView) findViewById(R.id.tv_1_3_1)).setText("月检 " + statistics.getTaskStat().getMonthFinishCount() + "/" + statistics.getTaskStat().getMonthAllCount());
        ((TextView) findViewById(R.id.tv_1_3_2)).setText(statistics.getTaskStat().getMonthEquipmentAvg() + "/次");
        ((TextView) findViewById(R.id.tv_1_3_3)).setText("平均" + statistics.getTaskStat().getMonthMinutesAvg() + "分钟/台");
        ((TextView) findViewById(R.id.tv_1_4_1)).setText("特检 " + statistics.getTaskStat().getManualFinishCount() + "/" + statistics.getTaskStat().getManualAllCount());
        ((TextView) findViewById(R.id.tv_1_4_2)).setText(statistics.getTaskStat().getManualEquipmentAvg() + "/次");
        ((TextView) findViewById(R.id.tv_1_4_3)).setText("平均" + statistics.getTaskStat().getManualMinutesAvg() + "分钟/台");
        ((TextView) findViewById(R.id.tv_2_1_1)).setText("已处理" + statistics.getFaultStat().getFaultFinishCount());
        ((TextView) findViewById(R.id.tv_2_1_2)).setText("共" + statistics.getFaultStat().getFaultAllCount());
        ((TextView) findViewById(R.id.tv_2_2_1)).setText("已处理" + statistics.getFaultStat().getRepairFinishCount());
        ((TextView) findViewById(R.id.tv_2_2_2)).setText("共" + statistics.getFaultStat().getRepairAllCount());
        LinearLayout llIncrementItem = (LinearLayout) findViewById(R.id.llIncrementItem);
        for (int i = 0; i < statistics.getIncrementStat().size(); i++) {
            View view = getLayoutInflater().inflate(R.layout.statistics_increment_item, null);
            TextView tv1 = view.findViewById(R.id.tv_1);
            TextView tv2 = view.findViewById(R.id.tv_2);
            TextView tv3 = view.findViewById(R.id.tv_3);
            tv1.setText(statistics.getIncrementStat().get(i).getIncrementWorkTypeName());
            tv2.setText("已处理" + statistics.getIncrementStat().get(i).getFinishCount());
            tv3.setText("共" + statistics.getIncrementStat().get(i).getAllCount());
            llIncrementItem.addView(view);
        }
        ((TextView) findViewById(R.id.tv_4_1)).setText("最多故障设备类型:" + statistics.getTheMostStat().getFlimsyEquipmentType().getEquipmentTypeName());
        ((TextView) findViewById(R.id.tv_4_2)).setText("最多故障类型:" + statistics.getTheMostStat().getTheMostFaultType());
        ((TextView) findViewById(R.id.tv_4_3_1)).setText(statistics.getTheMostStat().getFastestTimeCost() / 60000 + "分");
        ((TextView) findViewById(R.id.tv_4_3_2)).setText("平均耗时" + statistics.getTheMostStat().getFastestTimeCost() / 60000 + "分");
        ((TextView) findViewById(R.id.tv_4_4_1)).setText(statistics.getTheMostStat().getSlowestTimeCost() / 60000 + "分");
        ((TextView) findViewById(R.id.tv_4_4_2)).setText("平均耗时" + statistics.getTheMostStat().getFastestTimeCost() / 60000 + "分");
        ((TextView) findViewById(R.id.tv_4_5_1)).setText(DataUtil.timeFormat(statistics.getTheMostStat().getEarliestTask().getStartTime(), "HH:mm") + "开始");
        ((TextView) findViewById(R.id.tv_4_5_2)).setText(DataUtil.timeFormat(statistics.getTheMostStat().getEarliestTask().getEndTime(), "HH:mm") + "结束");
        ((TextView) findViewById(R.id.tv_4_6_1)).setText(DataUtil.timeFormat(statistics.getTheMostStat().getLatestTask().getStartTime(), "HH:mm") + "开始");
        ((TextView) findViewById(R.id.tv_4_6_2)).setText(DataUtil.timeFormat(statistics.getTheMostStat().getLatestTask().getEndTime(), "HH:mm") + "结束");
        if (statistics.getTheMostStat().getMostRepairUsers() != null) {
            StringBuilder sb1 = new StringBuilder();
            for (int i = 0; i < statistics.getTheMostStat().getMostRepairUsers().size(); i++) {
                sb1.append(statistics.getTheMostStat().getMostRepairUsers().get(i).getUser().getRealName());
                if (i != statistics.getTheMostStat().getMostRepairUsers().size() - 1) {
                    sb1.append("、");
                }
            }
            ((TextView) findViewById(R.id.tv_4_7_1)).setText(sb1.toString());
        }
        if (statistics.getTheMostStat().getMostRepairUsers() != null) {
            StringBuilder sb2 = new StringBuilder();
            for (int i = 0; i < statistics.getTheMostStat().getLeastRepairUsers().size(); i++) {
                sb2.append(statistics.getTheMostStat().getLeastRepairUsers().get(i).getUser().getRealName());
                if (i != statistics.getTheMostStat().getLeastRepairUsers().size() - 1) {
                    sb2.append("、");
                }
            }
            ((TextView) findViewById(R.id.tv_4_7_2)).setText(sb2.toString());
        }
        ((TextView) findViewById(R.id.tv_4_8)).setText(statistics.getTheMostStat().getHardestRepair().getRepairName());
    }

    @Override
    public void showLoading() {
        llShowContent.setVisibility(View.GONE);
        showEvLoading();
    }

    @Override
    public void hideLoading() {
        llShowContent.setVisibility(View.VISIBLE);
        hideEvLoading();
    }
}
