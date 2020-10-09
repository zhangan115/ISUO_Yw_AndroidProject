package com.isuo.yw2application.view.main.data.fault_time;

import android.content.DialogInterface;
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
import com.isuo.yw2application.utils.Utils;
import com.isuo.yw2application.view.base.BaseActivity;
import com.sito.library.widget.DateDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import javax.inject.Inject;

public class FaultTimeActivity extends BaseActivity implements FaultTimeContract.View {
    private LineChart mLineChart;
    private TextView mTime;
    private int mYear;
    private int mMonth;
    private String mMonthStr;
    private boolean isExp;

    @Inject
    FaultTimePresenter mFaultTimePresenter;
    FaultTimeContract.Presenter mPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutAndToolbar(R.layout.activity_fault_time, "故障排除时效");
        DaggerFaultTimeComponent.builder().customerRepositoryComponent(Yw2Application.getInstance().getRepositoryComponent())
                .faultTimeModule(new FaultTimeModule(this)).build()
                .inject(this);
        mTime = findViewById(R.id.id_fault_time);
        mLineChart = findViewById(R.id.lineChart);
        mLineChart.setNoDataTextColor(findColorById(R.color.colorPrimary));
        mLineChart.setNoDataText("");
        List<ChartData> chartDatas = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            ChartData chartData = new ChartData();
            List<ChartData.Data> dataList = new ArrayList<>();
            for (int j = 0; j < 15; j++) {
                ChartData.Data data = new ChartData.Data();
                data.setDataValue((int) (10 * Math.random()));
                dataList.add(data);
            }
            chartData.setData(dataList);
            chartDatas.add(chartData);
        }
        initLineChart(mLineChart, chartDatas);
        int[] colors = new int[]{R.color.line_color_1, R.color.line_color_2, R.color.line_color_4};
        mLineChart.setData(getLineData(chartDatas, colors));

        TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
        mYear = Calendar.getInstance(TimeZone.getDefault()).get(Calendar.YEAR);
        mMonth = Calendar.getInstance(TimeZone.getDefault()).get(Calendar.MONTH);
        if (mMonth < 10) {
            mMonthStr = "0" + mMonth;
        } else {
            mMonthStr = mMonth + "";
        }
        mTime.setText(mYear + "-" + mMonthStr);

        mTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isExp) {
                    isExp = true;
                    //箭头向上
                    Utils.setDrawable(FaultTimeActivity.this, R.drawable.up_arrow, mTime, 2);
                } else {
                    isExp = false;
                    Utils.setDrawable(FaultTimeActivity.this, R.drawable.drop_down_arrow, mTime, 2);
                }
                final DateDialog dateDlg = new DateDialog(FaultTimeActivity.this, R.style.MyDateDialog, mYear, mMonth, 1);
                dateDlg.setConfirmButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Calendar calendar = dateDlg.getDate();
                        mYear = calendar.get(Calendar.YEAR);
                        mMonth = calendar.get(Calendar.MONTH) + 1;
                        if (mMonth < 10) {
                            mMonthStr = "0" + mMonth;
                        } else {
                            mMonthStr = mMonth + "";
                        }
                        mTime.setText(mYear + "-" + mMonthStr);
                        isExp = false;
                        Utils.setDrawable(FaultTimeActivity.this, R.drawable.drop_down_arrow, mTime, 2);
                        //mPresenter.getChartData(mYear+"-"+mMonthStr);
                    }
                });
                dateDlg.setBackButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dateDlg.cancel();
                        isExp = false;
                        Utils.setDrawable(FaultTimeActivity.this, R.drawable.drop_down_arrow, mTime, 2);
                    }
                });
                dateDlg.pickMonth();
                dateDlg.show();
            }
        });
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
        lineChart.getXAxis().setTextSize(12f);
        lineChart.getXAxis().setTextColor(findColorById(R.color.colorPrimary));
        lineChart.getXAxis().setDrawAxisLine(true);
        lineChart.getXAxis().setDrawGridLines(false);
        lineChart.getXAxis().setAxisLineColor(findColorById(R.color.colorPrimary));
        lineChart.getXAxis().setValueFormatter(new ChartXFormatter(dataValues));
        //左边Y
        lineChart.getAxisLeft().setDrawGridLines(false);
        lineChart.getAxisLeft().setDrawAxisLine(true);
        lineChart.getAxisLeft().setDrawLabels(true);
        lineChart.getAxisLeft().setTextSize(12f);
        lineChart.getAxisLeft().setDrawZeroLine(false);
        lineChart.getAxisLeft().setTextColor(findColorById(R.color.colorPrimary));
        lineChart.getAxisLeft().setAxisLineColor(findColorById(R.color.colorPrimary));
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
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void setPresenter(FaultTimeContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
