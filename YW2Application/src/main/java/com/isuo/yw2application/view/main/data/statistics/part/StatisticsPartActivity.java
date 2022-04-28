package com.isuo.yw2application.view.main.data.statistics.part;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.isuo.yw2application.R;
import com.isuo.yw2application.app.Yw2Application;
import com.isuo.yw2application.mode.bean.count.PartPersonStatistics;
import com.isuo.yw2application.view.base.BaseActivity;
import com.isuo.yw2application.widget.ChooseTimeLayout;
import com.sito.library.utils.DataUtil;
import com.sito.library.utils.NumberUtils;

import java.text.MessageFormat;

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
        new StatisticsPartPresenter(Yw2Application.getInstance().getRepositoryComponent().getRepository(), this);
        setLayoutAndToolbar(R.layout.statistics_part_acivitiy, "任务统计");
        findViewById(R.id.llEmpty).setOnClickListener(this);
        findViewById(R.id.llOpenChooseTime).setOnClickListener(this);
        llChooseTime = findViewById(R.id.llChooseTime);
        llEmpty = findViewById(R.id.llEmpty);
        llShowContent = findViewById(R.id.llShowContent);
        tvTime = findViewById(R.id.tvTime);
        ivTime = findViewById(R.id.ivTime);
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
        ((TextView) findViewById(R.id.tv_1_1_1)).setText(MessageFormat.format("日检 {0}/{1}", statistics.getTaskStat().getDayFinishCount(), statistics.getTaskStat().getDayAllCount()));
        ((TextView) findViewById(R.id.tv_1_1_2)).setText(MessageFormat.format("{0}分钟/次", NumberUtils.formatDouble(statistics.getTaskStat().getDayEquipmentAvg())));
        ((TextView) findViewById(R.id.tv_1_1_3)).setText(MessageFormat.format("平均{0}分钟/台", statistics.getTaskStat().getDayMinutesAvg()));
        ((TextView) findViewById(R.id.tv_1_2_1)).setText(MessageFormat.format("周检 {0}/{1}", statistics.getTaskStat().getWeekFinishCount(), statistics.getTaskStat().getWeekAllCount()));
        ((TextView) findViewById(R.id.tv_1_2_2)).setText(MessageFormat.format("{0}分钟/次", NumberUtils.formatDouble(statistics.getTaskStat().getWeekEquipmentAvg())));
        ((TextView) findViewById(R.id.tv_1_2_3)).setText(MessageFormat.format("平均{0}分钟/台", statistics.getTaskStat().getWeekMinutesAvg()));
        ((TextView) findViewById(R.id.tv_1_3_1)).setText(MessageFormat.format("月检 {0}/{1}", statistics.getTaskStat().getMonthFinishCount(), statistics.getTaskStat().getMonthAllCount()));
        ((TextView) findViewById(R.id.tv_1_3_2)).setText(MessageFormat.format("{0}分钟/次", NumberUtils.formatDouble(statistics.getTaskStat().getMonthEquipmentAvg())));
        ((TextView) findViewById(R.id.tv_1_3_3)).setText(MessageFormat.format("平均{0}分钟/台", statistics.getTaskStat().getMonthMinutesAvg()));
        ((TextView) findViewById(R.id.tv_1_4_1)).setText(MessageFormat.format("特检 {0}/{1}", statistics.getTaskStat().getManualFinishCount(), statistics.getTaskStat().getManualAllCount()));
        ((TextView) findViewById(R.id.tv_1_4_2)).setText(MessageFormat.format("{0}分钟/次", NumberUtils.formatDouble(statistics.getTaskStat().getManualEquipmentAvg())));
        ((TextView) findViewById(R.id.tv_1_4_3)).setText(MessageFormat.format("平均{0}分钟/台", statistics.getTaskStat().getManualMinutesAvg()));
        ((TextView) findViewById(R.id.tv_2_1_1)).setText(MessageFormat.format("已处理{0}", statistics.getFaultStat().getFaultFinishCount()));
        ((TextView) findViewById(R.id.tv_2_1_2)).setText(MessageFormat.format("共{0}", statistics.getFaultStat().getFaultAllCount()));
        ((TextView) findViewById(R.id.tv_2_2_1)).setText(MessageFormat.format("已处理{0}", statistics.getFaultStat().getRepairFinishCount()));
        ((TextView) findViewById(R.id.tv_2_2_2)).setText(MessageFormat.format("共{0}", statistics.getFaultStat().getRepairAllCount()));
        LinearLayout llIncrementItem = findViewById(R.id.llIncrementItem);
        llIncrementItem.removeAllViews();
        for (int i = 0; i < statistics.getIncrementStat().size(); i++) {
            View view = getLayoutInflater().inflate(R.layout.statistics_increment_item, null);
            TextView tv1 = view.findViewById(R.id.tv_1);
            TextView tv2 = view.findViewById(R.id.tv_2);
            TextView tv3 = view.findViewById(R.id.tv_3);
            tv1.setText(statistics.getIncrementStat().get(i).getIncrementWorkTypeName());
            tv2.setText(MessageFormat.format("已处理{0}", statistics.getIncrementStat().get(i).getFinishCount()));
            tv3.setText(MessageFormat.format("共{0}", statistics.getIncrementStat().get(i).getAllCount()));
            llIncrementItem.addView(view);
        }
        if (!TextUtils.isEmpty(statistics.getTheMostStat().getFlimsyEquipmentType().getEquipmentTypeName())){
            ((TextView) findViewById(R.id.tv_4_1)).setText(MessageFormat.format("最多事件对象类型:{0}", statistics.getTheMostStat().getFlimsyEquipmentType().getEquipmentTypeName()));
        }else{
            ((TextView) findViewById(R.id.tv_4_1)).setText("最多事件对象类型:");
        }
        if (!TextUtils.isEmpty(statistics.getTheMostStat().getTheMostFaultType())){
            ((TextView) findViewById(R.id.tv_4_2)).setText(MessageFormat.format("最多事件类型:{0}", statistics.getTheMostStat().getTheMostFaultType()));
        }else {
            ((TextView) findViewById(R.id.tv_4_2)).setText("最多事件类型:");
        }
        String value1 = String.format("%.2f",statistics.getTheMostStat().getFastestTimeCost() / 60000.00);
        ((TextView) findViewById(R.id.tv_4_3_1)).setText( value1 + "分钟");
        String value2 = String.format("平均耗时%.2f",statistics.getTheMostStat().getAverageTimeCost() / 60000.00);
        ((TextView) findViewById(R.id.tv_4_3_2)).setText(value2+"分钟");
        String value3 = String.format("%.2f",statistics.getTheMostStat().getSlowestTimeCost() / 60000.00);
        ((TextView) findViewById(R.id.tv_4_4_1)).setText(value3 + "分钟");
        ((TextView) findViewById(R.id.tv_4_4_2)).setText("平均耗时" + statistics.getTheMostStat().getFastestTimeCost() / 60000.00 + "分");
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
