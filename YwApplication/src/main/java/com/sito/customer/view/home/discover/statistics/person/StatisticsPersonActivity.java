package com.sito.customer.view.home.discover.statistics.person;

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

/**
 * 统计，个人
 * Created by zhangan on 2018/4/17.
 */

public class StatisticsPersonActivity extends BaseActivity implements ChooseTimeLayout.IChooseTime
        , StatisticsPersonContract.View {

    private LinearLayout llChooseTime, llEmpty, llShowContent;
    private StatisticsPersonContract.Presenter mPresenter;
    private TextView tvTime;
    private ImageView ivTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new StatisticsPersonPresenter(CustomerApp.getInstance().getRepositoryComponent().getRepository(), this);
        setLayoutAndToolbar(R.layout.statistics_person_acivitiy, "个人");
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
        mPresenter.getCStatisticsPersonData(startTime, endTime);
    }

    @Override
    public void setPresenter(StatisticsPersonContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showData(PartPersonStatistics statistics) {
        ((TextView) findViewById(R.id.tv_1_1)).setText(statistics.getWorkStat().getTaskFinishCount() + "次");
        ((TextView) findViewById(R.id.tv_1_2)).setText(statistics.getWorkStat().getTaskEquipmentCount() + "台");
        ((TextView) findViewById(R.id.tv_1_3)).setText(statistics.getWorkStat().getOilCount() + "次");
        ((TextView) findViewById(R.id.tv_1_4_1)).setText(statistics.getWorkStat().getFaultCreateCount() + "个");
        ((TextView) findViewById(R.id.tv_1_4_2)).setText(statistics.getWorkStat().getFaultFinishCount() + "个");
        ((TextView) findViewById(R.id.tv_1_5)).setText(statistics.getWorkStat().getIncrementCount() + "个");

        ((TextView) findViewById(R.id.tv_2_1)).setText("排名第" + statistics.getBoardStat().getTaskRank());
        ((TextView) findViewById(R.id.tv_2_2)).setText("排名第" + statistics.getBoardStat().getTaskEquipmentRank());
        ((TextView) findViewById(R.id.tv_2_3)).setText("排名第" + statistics.getBoardStat().getIncrementRank());

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
