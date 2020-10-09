package com.isuo.yw2application.view.main.data.staff_time;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.isuo.yw2application.R;
import com.isuo.yw2application.app.Yw2Application;
import com.isuo.yw2application.mode.bean.ChartData;
import com.isuo.yw2application.mode.bean.discover.DeptType;
import com.isuo.yw2application.mode.bean.discover.FaultReport;
import com.isuo.yw2application.utils.Utils;
import com.isuo.yw2application.view.base.BaseActivity;
import com.sito.library.widget.DateDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import javax.inject.Inject;

public class StaffTimeActivity extends BaseActivity implements StaffTimeContract.View {
    private HorizontalBarChart mBarChart;
    private TextView mTime;
    private TextView mGroup;

    private int mYear;
    private int mMonth;
    private String mMonthStr;
    private boolean isExp;
    private boolean isShowGrop;

    @Inject
    StaffTimePresenter mStaffTimePresenter;
    StaffTimeContract.Presenter mPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutAndToolbar(R.layout.activity_staff_time,"人员工作时效");
        DaggerStaffTimeComponent.builder().customerRepositoryComponent(Yw2Application.getInstance().getRepositoryComponent())
                .staffTimeModule(new StaffTimeModule(this)).build()
                .inject(this);
        mTime = findViewById(R.id.id_fault_time);
        mGroup = findViewById(R.id.id_fault_group);

        mBarChart = findViewById(R.id.barChart);
        mBarChart.setNoDataTextColor(findColorById(R.color.colorPrimary));
        mBarChart.setNoDataText("");

        List<ChartData> chartDatas = new ArrayList<>();
        ChartData chartData = new ChartData();
        List<ChartData.Data> dataList = new ArrayList<>();
        for (int j = 0; j < 6; j++) {
            ChartData.Data data = new ChartData.Data();
            data.setDataValue((int) (10 * Math.random() + 1));
            dataList.add(data);
        }
        chartData.setData(dataList);
        chartDatas.add(chartData);
        initLineChart(mBarChart, chartDatas);
        mBarChart.setData(getLineData(chartDatas));
        mBarChart.animateXY(200, 500);

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
                    Utils.setDrawable(StaffTimeActivity.this, R.drawable.up_arrow, mTime, 2);
                } else {
                    isExp = false;
                    Utils.setDrawable(StaffTimeActivity.this, R.drawable.drop_down_arrow, mTime, 2);
                }
                final DateDialog dateDlg = new DateDialog(StaffTimeActivity.this, R.style.MyDateDialog, mYear, mMonth, 1);
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
                        Utils.setDrawable(StaffTimeActivity.this, R.drawable.drop_down_arrow, mTime, 2);
                        //mPresenter.getChartData(mYear+"-"+mMonthStr);
                    }
                });
                dateDlg.setBackButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dateDlg.cancel();
                        isExp = false;
                        Utils.setDrawable(StaffTimeActivity.this, R.drawable.drop_down_arrow, mTime, 2);
                    }
                });
                dateDlg.pickMonth();
                dateDlg.show();
            }
        });

        final List<String> groups = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            groups.add("运行" + (i + 1) + "班");
        }

        mGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isShowGrop) {
                    isShowGrop = true;
                    Utils.setDrawable(StaffTimeActivity.this, R.drawable.up_arrow, mGroup, 2);
                } else {
                    isShowGrop = false;
                    Utils.setDrawable(StaffTimeActivity.this, R.drawable.drop_down_arrow, mGroup, 2);
                }
                new MaterialDialog.Builder(StaffTimeActivity.this)
                        .items(groups)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                                mGroup.setText(groups.get(position));
                                isShowGrop = false;
                                Utils.setDrawable(StaffTimeActivity.this, R.drawable.drop_down_arrow, mGroup, 2);
                            }
                        })
                        .show();
            }
        });
    }
    private void initLineChart(BarChart barChart, List<ChartData> dataValues) {
        final List<String> names = new ArrayList<>();
        for (int i = 0; i < dataValues.get(0).getData().size(); i++) {
            names.add("姓名" + i);
        }

        barChart.clear();
//        barChart.clear();
//        barChart.setDescription(null);
//        barChart.setTouchEnabled(false);
//        barChart.setDragEnabled(false);
        barChart.setScaleEnabled(false);
        barChart.setPinchZoom(false);
//        barChart.setDrawMarkers(false);
        barChart.setDrawBarShadow(false);
        barChart.setMaxVisibleValueCount(5);
        barChart.setPinchZoom(false);
        barChart.setDrawGridBackground(false);
        barChart.setExtraRightOffset(20f);
//        barChart.setExtraLeftOffset(10f);
        //Description
        barChart.getDescription().setEnabled(false);
        barChart.getDescription().setText("用电量(kWh)");
        barChart.getDescription().setTextColor(findColorById(R.color.colorPrimary));
        barChart.getDescription().setTextSize(12f);
        barChart.getDescription().setYOffset(4f);
        //x
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getXAxis().setTextSize(12f);
        barChart.getXAxis().setTextColor(findColorById(R.color.colorPrimary));
        barChart.getXAxis().setDrawAxisLine(false);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getXAxis().setGridColor(findColorById(R.color.colorPrimary));
        barChart.getXAxis().setAvoidFirstLastClipping(true);
        barChart.getXAxis().setAxisLineColor(findColorById(R.color.colorPrimary));
        barChart.getXAxis().setLabelCount(names.size());
        barChart.getXAxis().setValueFormatter(new ChartXFormatterN(names));

        //左边Y
        barChart.getAxisRight().setDrawAxisLine(true);
        barChart.getAxisRight().setDrawGridLines(false);
        barChart.getAxisRight().setTextSize(12f);
        //barChart.getAxisRight().setLabelCount(8, false);
        barChart.getAxisRight().setGridColor(findColorById(R.color.colorPrimary));
//        barChart.getAxisRight().setAxisMinimum(0f);
        barChart.getAxisRight().setTextColor(findColorById(R.color.colorPrimary));
        barChart.getAxisRight().setAxisLineColor(findColorById(R.color.colorPrimary));
//        //右边Y
        barChart.getAxisLeft().setEnabled(false);
//        barChart.getAxisLeft().setDrawAxisLine(true);
//        barChart.getAxisLeft().setDrawLabels(false);
//        barChart.getAxisLeft().setAxisLineColor(findColorById(R.color.colorStartPrimary));
        barChart.getLegend().setEnabled(false);
    }

    private BarData getLineData(List<ChartData> loadBeen) {
        ArrayList<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < loadBeen.size(); i++) {
            for (int j = 0; j < loadBeen.get(i).getData().size(); j++) {
                float vaY = loadBeen.get(i).getData().get(j).getDataValue();
                entries.add(new BarEntry(j, vaY));
            }
        }
        BarDataSet dataSet = new BarDataSet(entries, "");
        dataSet.setColor(findColorById(R.color.colorPrimary));//设置柱子颜色
        dataSet.setDrawValues(true);//是否显示柱子上面的数值
        dataSet.setValueTextColor(findColorById(R.color.colorPrimary));
        dataSet.setValueTextSize(12f);
        dataSet.setHighLightColor(findColorById(R.color.transparent));
        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(dataSet);
        return new BarData(dataSets);
    }

    @Override
    public void showData(List<ChartData> chartDatas) {

    }

    @Override
    public void showDeptId(List<DeptType> deptTypes) {

    }

    @Override
    public void showChartData(List<FaultReport> faultReports) {

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
    public void setPresenter(StaffTimeContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
