package com.isuo.yw2application.view.main.data.fault_report;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.isuo.yw2application.R;
import com.isuo.yw2application.app.Yw2Application;
import com.isuo.yw2application.mode.bean.discover.DeptType;
import com.isuo.yw2application.mode.bean.discover.FaultReport;
import com.isuo.yw2application.utils.ChooseDateDialog;
import com.isuo.yw2application.view.base.BaseActivity;
import com.sito.library.chart.ChartView;
import com.sito.library.chart.ChartXYView;
import com.sito.library.utils.DisplayUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

public class FaultReportActivity extends BaseActivity implements FaultReportContract.View {

    private String deptType = "1,2";//部门ID
    private long mDeptId = -1;
    private LinearLayout addViewLayout;
    private List<DeptType> mDeptTypes;
    private TextView chooseDept, chooseTime;
    private Calendar mCurrentCalendar;
    private List<String> listStr;

    @Inject
    FaultReportPresenter mFaultReportPresenter;
    FaultReportContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutAndToolbar(R.layout.activity_fault_report, "上报量");
        DaggerFaultReportComponent.builder().customerRepositoryComponent(Yw2Application.getInstance().getRepositoryComponent())
                .faultReportModule(new FaultReportModule(this)).build()
                .inject(this);
        mCurrentCalendar = Calendar.getInstance(Locale.CHINA);
        addViewLayout = findViewById(R.id.ll_add_view);
        chooseTime = findViewById(R.id.id_fault_time);
        chooseDept = findViewById(R.id.id_fault_group);
        chooseDept.setOnClickListener(this);
        chooseTime.setOnClickListener(this);
        chooseTime.setText(getDate(mCurrentCalendar));
        mPresenter.getDeptId(deptType);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_fault_group:
                if (listStr == null || listStr.size() == 0) {
                    return;
                }
                new MaterialDialog.Builder(this)
                        .items(listStr)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                                mDeptId = mDeptTypes.get(position).getDeptId();
                                chooseDept.setText(mDeptTypes.get(position).getDeptName());
                                mPresenter.getChartData(mDeptId, getDate(mCurrentCalendar));
                            }
                        })
                        .show();
                break;
            case R.id.id_fault_time:
                new ChooseDateDialog(this, R.style.MyDateDialog)
                        .pickMonth()
                        .setCurrent(mCurrentCalendar)
                        .setResultListener(new ChooseDateDialog.OnDateChooseListener() {
                            @Override
                            public void onDate(Calendar calendar) {
                                mCurrentCalendar = calendar;
                                chooseTime.setText(getDate(mCurrentCalendar));
                                if (mDeptId==-1){
                                    return;
                                }
                                mPresenter.getChartData(mDeptId, getDate(mCurrentCalendar));
                            }
                        }).show();
                break;
        }
    }

    @Override
    public void showDeptId(List<DeptType> deptTypes) {
        mDeptTypes = deptTypes;
        listStr = new ArrayList<>();
        for (int i = 0; i < mDeptTypes.size(); i++) {
            listStr.add(mDeptTypes.get(i).getDeptName());
        }
        if (!mDeptTypes.isEmpty()){
            chooseDept.setVisibility(View.VISIBLE);
        }
        mDeptId = mDeptTypes.get(0).getDeptId();
        chooseDept.setText(mDeptTypes.get(0).getDeptName());
        mPresenter.getChartData(mDeptId, getDate(mCurrentCalendar));
    }

    @Override
    public void showChartData(List<FaultReport> faultReports) {
        addViewLayout.removeAllViews();
        List<com.sito.library.chart.ChartData> chartDataList = new ArrayList<>();
        for (int i = 0; i < faultReports.size(); i++) {
            com.sito.library.chart.ChartData chartData = new com.sito.library.chart.ChartData();
            List<com.sito.library.chart.ChartData.Value> values = new ArrayList<>();
            values.add(new com.sito.library.chart.ChartData.Value(faultReports.get(i).getFaultCount()));
            chartData.setValueList(values);
            chartData.setUserName(faultReports.get(i).getRealName());
            chartDataList.add(chartData);
        }
        int[] colors = new int[]{findColorById(R.color.color69A7FB)
                , findColorById(R.color.colorF6B433)
                , findColorById(R.color.color62D181)};
        ChartXYView chartXYView = findViewById(R.id.chart_bg_view);
        chartXYView.setTextSize(getResources().getDimensionPixelSize(R.dimen.SP_12))
                .setChartDataList(chartDataList)
                .setTextColor(findColorById(R.color.text_item_title_1))
                .setLineSize(1)
                .setLineColor(findColorById(R.color.color_division))
                .show();
        ChartView chartView = new ChartView(getApplicationContext());
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DisplayUtil.dip2px(getApplicationContext(), 61 * chartDataList.size() + 15));
        chartView.setLayoutParams(params);
        chartView.setTextSize(getResources().getDimensionPixelSize(R.dimen.SP_12))
                .setLineSize(DisplayUtil.dip2px(getApplicationContext(), 12))
                .setTextColor(findColorById(R.color.text_item_title_1))
                .setLineColors(colors)
                .setChartDataList(chartDataList)
                .show();
        addViewLayout.addView(chartView);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void noData() {
        addViewLayout.removeAllViews();
    }

    @Override
    public void setPresenter(FaultReportContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    protected void onCancel() {
        super.onCancel();
        mPresenter.unSubscribe();
    }

    private String getDate(Calendar calendar) {
        int year = calendar.get(Calendar.YEAR);
        int monthOfYear = calendar.get(Calendar.MONTH) + 1;
        StringBuilder sb = new StringBuilder();
        sb.append(year).append("-");
        if (monthOfYear < 10) {
            sb.append("0").append(monthOfYear);
        } else {
            sb.append(monthOfYear);
        }
        return sb.toString();
    }
}
