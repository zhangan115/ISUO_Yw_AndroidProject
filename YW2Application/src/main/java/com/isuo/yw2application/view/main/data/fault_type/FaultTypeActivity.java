package com.isuo.yw2application.view.main.data.fault_type;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.isuo.yw2application.R;
import com.isuo.yw2application.app.Yw2Application;
import com.isuo.yw2application.mode.bean.discover.FaultLevel;
import com.isuo.yw2application.utils.ChooseDateDialog;
import com.isuo.yw2application.view.base.BaseActivity;
import com.sito.library.utils.DataUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import javax.inject.Inject;

public class FaultTypeActivity extends BaseActivity implements FaultTypeContract.View {
    private PieChart pieChart;
    private TextView mTime;
    int[] colors = new int[]{R.color.line_chart_color_2, R.color.line_chart_color_3, R.color.line_chart_color_4};
    @Inject
    FaultTypePresenter mFaultTypePresenter;
    FaultTypeContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutAndToolbar(R.layout.activity_fault_type, "事件等级");
        DaggerFaultTypeComponent.builder().customerRepositoryComponent(Yw2Application.getInstance().getRepositoryComponent())
                .faultTypeModule(new FaultTypeModule(this)).build()
                .inject(this);
        mTime = findViewById(R.id.id_fault_time);
        pieChart = findViewById(R.id.pieChart);
        pieChart.setNoDataTextColor(findColorById(R.color.colorPrimary));
        pieChart.setNoDataText("");
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        String dateStr = DataUtil.timeFormat(calendar.getTime().getTime(),"yyyy-MM");
        mTime.setText(dateStr);
        mTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ChooseDateDialog(FaultTypeActivity.this, R.style.MyDateDialog)
                        .pickMonth()
                        .setResultListener(new ChooseDateDialog.OnDateChooseListener() {
                            @Override
                            public void onDate(Calendar calendar) {
                                String dateStr = DataUtil.timeFormat(calendar.getTime().getTime(),"yyyy-MM");
                                mTime.setText(dateStr);
                                mPresenter.getChartData(dateStr);
                            }
                        }).show();
            }
        });
        initView();
        mPresenter.getChartData(dateStr);
    }

    @Override
    public void showChartData(FaultLevel bean) {
        setData(bean);
    }
    
    private void initView(){
        pieChart.setUsePercentValues(false);
        pieChart.setDescription(null);
        pieChart.setCenterText("");
        pieChart.setExtraOffsets(25f, 25.f, 25.f, 25.f);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.TRANSPARENT);
        pieChart.setTransparentCircleColor(Color.WHITE);
        pieChart.setTransparentCircleAlpha(110);
        pieChart.setTransparentCircleRadius(50f);
        pieChart.setHoleRadius(50f);
        pieChart.setDrawCenterText(true);
        pieChart.setCenterTextColor(findColorById(R.color.text333));
        pieChart.setCenterTextSize(14);
        pieChart.setRotationAngle(20);
        pieChart.setRotationEnabled(false);
        pieChart.getLegend().setEnabled(false);
    }

    private void setData(FaultLevel bean) {
        List<PieEntry> yValues1 = new ArrayList<>();
        yValues1.add(new PieEntry(bean.getAFaultCount()));
        yValues1.add(new PieEntry(bean.getBFaultCount()));
        yValues1.add(new PieEntry(bean.getCFaultCount()));
        PieDataSet dataSet = new PieDataSet(yValues1, "");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(findColorById(this.colors[0]));
        colors.add(findColorById(this.colors[1]));
        colors.add(findColorById(this.colors[2]));
        dataSet.setColors(colors);
        dataSet.setValueLinePart1Length(0.3f);
        dataSet.setValueLinePart2Length(0.4f);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new ChartXFormatter());
        data.setValueTextSize(12f);
        data.setValueTextColor(findColorById(R.color.text333));
        pieChart.setCenterText("事件总数:"+bean.getAllFaultCount());
        pieChart.setData(data);
        pieChart.highlightValues(null);
        pieChart.invalidate();
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
