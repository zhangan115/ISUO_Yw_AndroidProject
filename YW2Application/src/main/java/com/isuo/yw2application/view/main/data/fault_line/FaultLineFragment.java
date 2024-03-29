package com.isuo.yw2application.view.main.data.fault_line;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.isuo.yw2application.R;
import com.isuo.yw2application.app.Yw2Application;
import com.isuo.yw2application.common.ConstantStr;
import com.isuo.yw2application.mode.bean.ChartData;
import com.isuo.yw2application.mode.bean.fault.AlarmCount;
import com.isuo.yw2application.mode.bean.fault.FaultCountBean;
import com.isuo.yw2application.mode.bean.fault.FaultDayCountBean;
import com.isuo.yw2application.mode.bean.fault.FaultYearCountBean;
import com.isuo.yw2application.mode.bean.news.Fault;
import com.isuo.yw2application.utils.ChooseDateDialog;
import com.isuo.yw2application.view.base.MvpFragmentV4;
import com.isuo.yw2application.view.main.alarm.equipalarm.EquipAlarmActivity;
import com.isuo.yw2application.view.main.alarm.list.AlarmListActivity;
import com.sito.library.widget.DateDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * 故障报警
 */
public class FaultLineFragment extends MvpFragmentV4<FaultLineContract.Presenter> implements FaultLineContract.View, View.OnClickListener
        , SwipeRefreshLayout.OnRefreshListener {

    private BarChart mLineChart;
    private LinearLayout mllFault;
    private int mCount = 0;
    private View todayView;
    private View yesterdayView;
    private View alarmView;
    private LinearLayout countCardView;
    private LinearLayout chartCardView;
    private LinearLayout mCountLayout;
    private LinearLayout mAlarmTitleLayout, mAlarmCountLayout;
    private SwipeRefreshLayout mRefreshLayout;
    int[] colors = new int[]{R.color.line_chart_color_2, R.color.line_chart_color_3
            , R.color.line_chart_color_4, R.color.line_chart_color_5};
    private TextView chooseYear;

    public static FaultLineFragment newInstance() {
        Bundle args = new Bundle();
        FaultLineFragment fragment = new FaultLineFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new FaultLinePresenter(Yw2Application.getInstance().getRepositoryComponent().getRepository(), this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_alarm_line, container, false);
        rootView.findViewById(R.id.rl_alarm_list).setOnClickListener(this);
        rootView.findViewById(R.id.ll_today).setOnClickListener(this);
        rootView.findViewById(R.id.ll_yesterday).setOnClickListener(this);
        rootView.findViewById(R.id.ll_alarm).setOnClickListener(this);
        chooseYear = rootView.findViewById(R.id.tv_choose_year);
        chooseYear.setOnClickListener(this);
        mRefreshLayout = rootView.findViewById(R.id.refreshLoadLayoutId);
        mRefreshLayout.setColorSchemeColors(findColorById(R.color.colorPrimary));
        mRefreshLayout.setOnRefreshListener(this);
        mCountLayout = rootView.findViewById(R.id.ll_count);
        mCurrentCalendar = Calendar.getInstance(Locale.CHINA);
        ((TextView) rootView.findViewById(R.id.tv_choose_year)).setText(String.valueOf(mCurrentCalendar.get(Calendar.YEAR)));
        countCardView = rootView.findViewById(R.id.card_view_count);
        chartCardView = rootView.findViewById(R.id.card_view_chart);
        mAlarmTitleLayout = rootView.findViewById(R.id.ll_alarm_title_5);
        mAlarmCountLayout = rootView.findViewById(R.id.ll_content_5);
        todayView = rootView.findViewById(R.id.view_today);
        yesterdayView = rootView.findViewById(R.id.view_yesterday);
        alarmView = rootView.findViewById(R.id.view_alarm);
        mLineChart = rootView.findViewById(R.id.lineChart);
        mLineChart.setNoDataTextColor(findColorById(R.color.colorPrimary));
        mLineChart.setNoDataText("");
        mllFault = rootView.findViewById(R.id.id_alarm_ll);
        mllFault.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mPresenter != null) {
            showEvLoading();
            Calendar calendar = Calendar.getInstance(Locale.CHINA);
            calendar.add(Calendar.DATE, -1);
            mCurrentCalendar = calendar;
            mPresenter.getFaultCount();
            mPresenter.getFaultDayCount(getDataStr(calendar));
            mPresenter.getFaultYearCount(String.valueOf(mCurrentCalendar.get(Calendar.YEAR)));
        }
    }

    @Override
    public void setPresenter(FaultLineContract.Presenter presenter) {
        mPresenter = presenter;
    }

    private int chooseTye = 0;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_alarm_list:
                startActivity(new Intent(getActivity(), AlarmListActivity.class));
                break;
            case R.id.id_alarm_ll:
                Intent intent = new Intent(getActivity(), EquipAlarmActivity.class);
                switch (chooseTye) {
                    case 0:
                        intent.putExtra(ConstantStr.KEY_BUNDLE_OBJECT, getDataStr(mCurrentCalendar));
                        intent.putExtra(ConstantStr.KEY_BUNDLE_STR, "昨日统计");
                        break;
                    case 1:
                        intent.putExtra(ConstantStr.KEY_BUNDLE_OBJECT, getDataStr(mCurrentCalendar));
                        intent.putExtra(ConstantStr.KEY_BUNDLE_STR, "今日统计");
                        break;
                    case 2:
                        intent.putExtra(ConstantStr.KEY_BUNDLE_STR, "遗留事件");
                        break;
                }
                startActivity(intent);
                break;
            case R.id.ll_today:
                if (chooseTye != 1) {
                    if (mPresenter != null) {
                        Calendar calendar = Calendar.getInstance(Locale.CHINA);
                        mAlarmTitleLayout.setVisibility(View.GONE);
                        mAlarmCountLayout.setVisibility(View.GONE);
                        showCount();
                        mCurrentCalendar = calendar;
                        mPresenter.getFaultDayCount(getDataStr(calendar));
                    }
                }
                chooseTye = 1;
                todayView.setVisibility(View.VISIBLE);
                yesterdayView.setVisibility(View.INVISIBLE);
                alarmView.setVisibility(View.INVISIBLE);
                break;
            case R.id.ll_yesterday:
                if (chooseTye != 0) {
                    if (mPresenter != null) {
                        Calendar calendar = Calendar.getInstance(Locale.CHINA);
                        calendar.add(Calendar.DATE, -1);
                        mAlarmTitleLayout.setVisibility(View.VISIBLE);
                        mAlarmCountLayout.setVisibility(View.VISIBLE);
                        showCount();
                        mCurrentCalendar = calendar;
                        mPresenter.getFaultDayCount(getDataStr(calendar));
                    }
                }
                todayView.setVisibility(View.INVISIBLE);
                yesterdayView.setVisibility(View.VISIBLE);
                alarmView.setVisibility(View.INVISIBLE);
                chooseTye = 0;
                break;
            case R.id.ll_alarm:
                if (chooseTye != 2) {
                    if (mPresenter != null) {
                        mAlarmTitleLayout.setVisibility(View.GONE);
                        mAlarmCountLayout.setVisibility(View.GONE);
                        showAlarm();
                        mPresenter.getAlarmCount();
                    }
                }
                chooseTye = 2;
                todayView.setVisibility(View.INVISIBLE);
                yesterdayView.setVisibility(View.INVISIBLE);
                alarmView.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_choose_year:
                new ChooseDateDialog(getActivity(), R.style.MyDateDialog)
                        .pickYear()
                        .setCurrent(mCurrentCalendar)
                        .setResultListener(new ChooseDateDialog.OnDateChooseListener() {
                            @Override
                            public void onDate(Calendar calendar) {
                                mCurrentCalendar = calendar;
                                chooseYear.setText(String.valueOf(mCurrentCalendar.get(Calendar.YEAR)));
                                if (mPresenter != null) {
                                    mPresenter.getFaultYearCount(String.valueOf(mCurrentCalendar.get(Calendar.YEAR)));
                                }
                            }
                        }).show();
                break;
        }
    }

    private Calendar mCurrentCalendar;

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {
        mCount++;
        if (mCount == 3) {
            countCardView.setVisibility(View.GONE);
            chartCardView.setVisibility(View.VISIBLE);
            mCountLayout.setVisibility(View.GONE);
            hideEvLoading();
        }
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showAlarmCount(AlarmCount bean) {
        if (getView() == null) {
            return;
        }
        ((TextView) getView().findViewById(R.id.tv_1)).setText(String.valueOf(bean.getAllCount()));
        ((TextView) getView().findViewById(R.id.tv_2)).setText(String.valueOf(bean.getaFaultCount()));
        ((TextView) getView().findViewById(R.id.tv_3)).setText(String.valueOf(bean.getbFaultCount()));
        ((TextView) getView().findViewById(R.id.tv_4)).setText(String.valueOf(bean.getcFaultCount()));
    }

    @Override
    public void showFaultCount(FaultCountBean bean) {
        if (getView() == null) {
            return;
        }
        ((TextView) getView().findViewById(R.id.tv_alarm_count)).setText(String.valueOf(bean.getAllAlarm()));
        ((TextView) getView().findViewById(R.id.tv_alarm_total_count)).setText(String.valueOf(bean.getAllFault()));
    }

    @Override
    public void showFaultDayCount(FaultDayCountBean bean) {
        if (getView() == null) {
            return;
        }
        ((TextView) getView().findViewById(R.id.tv_1)).setText(String.valueOf(bean.getAllFault()));
        ((TextView) getView().findViewById(R.id.tv_2)).setText(String.valueOf(bean.getPendingCount()));
        ((TextView) getView().findViewById(R.id.tv_3)).setText(String.valueOf(bean.getFlowingCount()));
        ((TextView) getView().findViewById(R.id.tv_4)).setText(String.valueOf(bean.getRepairCount()));
        ((TextView) getView().findViewById(R.id.tv_5)).setText(String.valueOf(bean.getCloseCount()));
    }

    @Override
    public void showFaultYearCount(FaultYearCountBean bean) {
        mLineChart.removeAllViews();
        List<ChartData> chartDates = new ArrayList<>();
        //待处理
        ChartData chartData = new ChartData();
        List<ChartData.Data> dataList2 = new ArrayList<>();
        for (int i = 0; i < bean.getPendingList().size(); i++) {
            ChartData.Data data = new ChartData.Data();
            data.setDataValue(bean.getPendingList().get(i));
            dataList2.add(data);
        }
        chartData.setData(dataList2);
        chartDates.add(chartData);
        //处理中
        chartData = new ChartData();
        List<ChartData.Data> dataList3 = new ArrayList<>();
        for (int i = 0; i < bean.getFlowingList().size(); i++) {
            ChartData.Data data = new ChartData.Data();
            data.setDataValue(bean.getFlowingList().get(i));
            dataList3.add(data);
        }
        chartData.setData(dataList3);
        chartDates.add(chartData);
        //转检修
        chartData = new ChartData();
        List<ChartData.Data> dataList4 = new ArrayList<>();
        for (int i = 0; i < bean.getRepairList().size(); i++) {
            ChartData.Data data = new ChartData.Data();
            data.setDataValue(bean.getRepairList().get(i));
            dataList4.add(data);
        }
        chartData.setData(dataList4);
        chartDates.add(chartData);
        //关闭
        chartData = new ChartData();
        List<ChartData.Data> dataList5 = new ArrayList<>();
        for (int i = 0; i < bean.getCloseList().size(); i++) {
            ChartData.Data data = new ChartData.Data();
            data.setDataValue(bean.getCloseList().get(i));
            dataList5.add(data);
        }
        chartData.setData(dataList5);
        chartDates.add(chartData);

        initLineChart(mLineChart, chartDates);
        mLineChart.setData(getLineData(bean));
    }

    @NonNull
    private String getDataStr(Calendar calendar) {
        StringBuilder sb = new StringBuilder();
        sb.append(calendar.get(Calendar.YEAR));
        sb.append("-");
        int month = calendar.get(Calendar.MONTH) + 1;
        if (month < 10) {
            sb.append("0").append(month);
        } else {
            sb.append(month);
        }
        sb.append("-");
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        if (day < 10) {
            sb.append("0").append(day);
        } else {
            sb.append(day);
        }
        return sb.toString();
    }

    private void initLineChart(BarChart lineChart, List<ChartData> dataValues) {
        lineChart.clear();
        lineChart.setDescription(null);
        lineChart.setNoDataText("");
        lineChart.setAlpha(1f);
        lineChart.setTouchEnabled(false);
        lineChart.setDragEnabled(false);
        lineChart.setScaleEnabled(false);
        lineChart.setPinchZoom(false);
        lineChart.getLegend().setEnabled(false);
        //x
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.getXAxis().setTextSize(10f);
        lineChart.getXAxis().setLabelCount(12, false);
        lineChart.getXAxis().setTextColor(findColorById(R.color.gray_999999));
        lineChart.getXAxis().setDrawAxisLine(true);
        lineChart.getXAxis().setDrawGridLines(false);
        lineChart.getXAxis().setAxisLineColor(findColorById(R.color.colorDADFE3));
        lineChart.getXAxis().setValueFormatter(new ChartXFormatter(dataValues));
        //左边Y
        lineChart.getAxisLeft().setDrawGridLines(false);
        lineChart.getAxisLeft().setDrawAxisLine(true);
        lineChart.getAxisLeft().setDrawLabels(true);
        lineChart.getAxisLeft().setTextSize(12f);
        lineChart.getAxisLeft().setDrawZeroLine(false);
        lineChart.getAxisLeft().setAxisMinimum(0f);
        lineChart.getAxisLeft().setValueFormatter(new ChartYFormatter(dataValues));

        lineChart.getAxisLeft().setTextColor(findColorById(R.color.color949AAC));
        lineChart.getAxisLeft().setAxisLineColor(findColorById(R.color.colorDADFE3));
        //右边Y
        lineChart.getAxisRight().setEnabled(false);
    }

    private BarData getLineData(FaultYearCountBean bean) {
        List<IBarDataSet> dataSets = new ArrayList<>();
        if (bean.getPendingList().size() == 12 && bean.getCloseList().size() == 12
                && bean.getFlowingList().size() == 12 && bean.getRepairList().size() == 12) {
            List<BarEntry> entries = new ArrayList<>();
                for (int i=0;i<bean.getPendingList().size();i++){
                    float[] values = new float[4];
                    values[0]=(bean.getPendingList().get(i).floatValue());
                    values[1]=(bean.getFlowingList().get(i).floatValue());
                    values[2]=(bean.getRepairList().get(i).floatValue());
                    values[3]=(bean.getCloseList().get(i).floatValue());
                    entries.add(new BarEntry(i,values));
                }
            BarDataSet dataSet = new BarDataSet(entries, "");
            ArrayList<Integer> colors = new ArrayList<>();
            colors.add(findColorById(this.colors[0]));
            colors.add(findColorById(this.colors[1]));
            colors.add(findColorById(this.colors[2]));
            colors.add(findColorById(this.colors[3]));
            dataSet.setColors(colors);
            dataSet.setValueFormatter(new ChartValueFormatter());
            dataSet.setValueTextSize(12f);
            dataSet.setDrawValues(true);
            dataSet.setHighLightColor(findColorById(R.color.transparent));
            dataSets.add(dataSet);
        }
        return new BarData(dataSets);
    }

    @Override
    public void onRefresh() {
        if (mPresenter != null) {
            mCount = 0;
            mPresenter.getFaultYearCount(String.valueOf(mCurrentCalendar.get(Calendar.YEAR)));
            if (chooseTye == 2) {
                mPresenter.getAlarmCount();
            } else {
                mPresenter.getFaultDayCount(getDataStr(mCurrentCalendar));
            }
            mPresenter.getFaultCount();
        }
    }

    private void showCount() {
        if (getView() == null) {
            return;
        }
        ((TextView) getView().findViewById(R.id.tv_title_1)).setText(findStrById(R.string.str_all_count));
        ((TextView) getView().findViewById(R.id.tv_title_2)).setText(findStrById(R.string.str_pending_count));
        ((TextView) getView().findViewById(R.id.tv_title_3)).setText(findStrById(R.string.str_flowing_count));
        ((TextView) getView().findViewById(R.id.tv_title_4)).setText(findStrById(R.string.str_repair_count));
        ((TextView) getView().findViewById(R.id.tv_title_5)).setText(findStrById(R.string.str_close_count));
    }

    private void showAlarm() {
        if (getView() == null) {
            return;
        }
        ((TextView) getView().findViewById(R.id.tv_title_2)).setText(findStrById(R.string.str_a_count));
        ((TextView) getView().findViewById(R.id.tv_title_3)).setText(findStrById(R.string.str_b_count));
        ((TextView) getView().findViewById(R.id.tv_title_4)).setText(findStrById(R.string.str_c_count));
    }
}
