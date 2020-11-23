package com.isuo.yw2application.view.main.data.fault_type;

import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.isuo.yw2application.R;
import com.isuo.yw2application.app.Yw2Application;
import com.isuo.yw2application.mode.bean.ChartData;
import com.isuo.yw2application.mode.bean.discover.FaultLevel;
import com.isuo.yw2application.utils.ChooseDateDialog;
import com.isuo.yw2application.view.base.BaseActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import javax.inject.Inject;

public class FaultTypeActivity extends BaseActivity implements FaultTypeContract.View {
    private LineChart mLineChart;
    private TextView mTime;
    private int mYear;
    List<String> month;
    @Inject
    FaultTypePresenter mFaultTypePresenter;
    FaultTypeContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutAndToolbar(R.layout.activity_fault_type, "故障等级");
        DaggerFaultTypeComponent.builder().customerRepositoryComponent(Yw2Application.getInstance().getRepositoryComponent())
                .faultTypeModule(new FaultTypeModule(this)).build()
                .inject(this);
        mTime = findViewById(R.id.id_fault_time);
        mLineChart = findViewById(R.id.lineChart);
        mLineChart.setNoDataTextColor(findColorById(R.color.colorPrimary));
        mLineChart.setNoDataText("");


        TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
        mYear = Calendar.getInstance(TimeZone.getDefault()).get(Calendar.YEAR);
        mTime.setText(String.valueOf(mYear));

        mTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ChooseDateDialog(FaultTypeActivity.this, R.style.MyDateDialog)
                        .pickYear()
                        .setResultListener(new ChooseDateDialog.OnDateChooseListener() {
                            @Override
                            public void onDate(Calendar calendar) {
                                mYear = calendar.get(Calendar.YEAR);
                                mTime.setText(String.valueOf(mYear));
                                mPresenter.getChartData(String.valueOf(mYear));
                            }
                        }).show();
            }
        });
        mPresenter.getChartData(mTime.getText().toString());
    }

    private void initLineChart(LineChart lineChart, List<ChartData> dataValues) {
        lineChart.clear();
        lineChart.setDescription(null);
        lineChart.setNoDataText("");
        lineChart.setAlpha(1f);
        lineChart.setTouchEnabled(true);
        lineChart.setDragEnabled(false);
        lineChart.setScaleEnabled(false);
        lineChart.setPinchZoom(false);
        lineChart.getLegend().setEnabled(false);
        //x
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.getXAxis().setTextSize(10f);
        lineChart.getXAxis().setTextColor(findColorById(R.color.news_text_black));
        lineChart.getXAxis().setDrawAxisLine(true);
        lineChart.getXAxis().setDrawGridLines(false);
        lineChart.getXAxis().setAxisLineColor(findColorById(R.color.color_news_line_gray));
        lineChart.getXAxis().setLabelCount(month.size(), true);
        lineChart.getXAxis().setValueFormatter(new ChartXFormatterN(month));
        lineChart.getXAxis().setAvoidFirstLastClipping(true);
        //左边Y
        lineChart.getAxisLeft().setDrawGridLines(false);
        lineChart.getAxisLeft().setDrawAxisLine(true);
        lineChart.getAxisLeft().setDrawLabels(true);
        lineChart.getAxisLeft().setTextSize(10f);
        lineChart.getAxisLeft().setDrawZeroLine(false);
        lineChart.getAxisLeft().setTextColor(findColorById(R.color.news_text_black));
        lineChart.getAxisLeft().setAxisLineColor(findColorById(R.color.color_news_line_gray));
        lineChart.getAxisLeft().setStartAtZero(true);
        //右边Y
        lineChart.getAxisRight().setEnabled(false);
    }

    private void initDataSet(LineDataSet dataSet, @ColorInt int color) {
        dataSet.setLineWidth(2.0f);
        dataSet.setColor(findColorById(color));
        dataSet.setCircleColor(findColorById(color));
        dataSet.setCircleRadius(2.5f);
        dataSet.setDrawValues(false);
        dataSet.setDrawCircles(true);
        dataSet.setMode(LineDataSet.Mode.LINEAR);
        dataSet.setHighLightColor(findColorById(R.color.transparent));
    }


    private LineData getLineData(List<ChartData> chartDatas, int[] color) {
        List<ILineDataSet> dataSets = new ArrayList<>();
        for (int i = 0; i < chartDatas.size(); i++) {
            List<Entry> entries = new ArrayList<>();
            for (int j = 0; j < chartDatas.get(i).getData().size(); j++) {
                entries.add(new Entry(j, chartDatas.get(i).getData().get(j).getDataValue()));
            }
            LineDataSet dataSet = new LineDataSet(entries, "");
            initDataSet(dataSet, color[i]);
            dataSets.add(dataSet);
        }

        return new LineData(dataSets);
    }

    @Override
    public void showData(List<ChartData> chartDatas) {

    }

    @Override
    public void showChartData(List<FaultLevel> faultLevels) {
        List<ChartData> chartDatas = new ArrayList<>();

        ChartData allFault = new ChartData();
        List<ChartData.Data> allList = new ArrayList<>();

        ChartData aFault = new ChartData();
        List<ChartData.Data> aList = new ArrayList<>();

        ChartData bFault = new ChartData();
        List<ChartData.Data> bList = new ArrayList<>();

        ChartData cFault = new ChartData();
        List<ChartData.Data> cList = new ArrayList<>();

        month = new ArrayList<>();
        for (int j = 0; j < faultLevels.size(); j++) {
            ChartData.Data allData = new ChartData.Data();
            allData.setDataValue(faultLevels.get(j).getAllFaultCount());
            allList.add(allData);

            ChartData.Data aData = new ChartData.Data();
            aData.setDataValue(faultLevels.get(j).getAFaultCount());
            aList.add(aData);

            ChartData.Data bData = new ChartData.Data();
            bData.setDataValue(faultLevels.get(j).getBFaultCount());
            bList.add(bData);


            ChartData.Data cData = new ChartData.Data();
            cData.setDataValue(faultLevels.get(j).getCFaultCount());
            cList.add(cData);

            month.add((j + 1) + "月");
        }
        allFault.setData(allList);
        aFault.setData(aList);
        bFault.setData(bList);
        cFault.setData(cList);
        chartDatas.add(allFault);
        chartDatas.add(aFault);
        chartDatas.add(bFault);
        chartDatas.add(cFault);

        initLineChart(mLineChart, chartDatas);
        int[] colors = new int[]{R.color.color_fault_pink, R.color.line_color_4, R.color.line_color_1, R.color.line_color_2};
        mLineChart.setData(getLineData(chartDatas, colors));
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void noData() {

    }

    @Override
    public void setPresenter(FaultTypeContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
