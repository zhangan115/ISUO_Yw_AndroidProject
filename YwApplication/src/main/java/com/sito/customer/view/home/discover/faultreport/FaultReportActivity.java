package com.sito.customer.view.home.discover.faultreport;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.sito.customer.R;
import com.sito.customer.app.CustomerApp;
import com.sito.customer.mode.bean.discover.DeptType;
import com.sito.customer.mode.bean.discover.FaultReport;
import com.sito.customer.view.BaseActivity;
import com.sito.library.chart.ChartView;
import com.sito.library.chart.ChartXYView;
import com.sito.library.utils.DisplayUtil;
import com.sito.library.widget.DateDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

public class FaultReportActivity extends BaseActivity implements FaultReportContract.View {

    private String deptType = "1,2";//部门ID
    private long mDeptId;
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
        setLayoutAndToolbar(R.layout.activity_fault_report, "上报故障统计");
        DaggerFaultReportComponent.builder().customerRepositoryComponent(CustomerApp.getInstance().getRepositoryComponent())
                .faultReportModule(new FaultReportModule(this)).build()
                .inject(this);
        mCurrentCalendar = Calendar.getInstance(Locale.CHINA);
        addViewLayout = (LinearLayout) findViewById(R.id.ll_add_view);
        chooseTime = (TextView) findViewById(R.id.id_fault_time);
        chooseDept = (TextView) findViewById(R.id.id_fault_group);
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
                final DateDialog dateDlg = new DateDialog(this, R.style.MyDateDialog, mCurrentCalendar.get(Calendar.YEAR)
                        , mCurrentCalendar.get(Calendar.MONTH) + 1
                        , mCurrentCalendar.get(Calendar.DAY_OF_MONTH));
                dateDlg.setConfirmButton(getString(R.string.ok), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mCurrentCalendar = dateDlg.getDate();
                        chooseTime.setText(getDate(mCurrentCalendar));
                        mPresenter.getChartData(mDeptId, getDate(mCurrentCalendar));
                    }
                });
                dateDlg.setBackButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dateDlg.cancel();
                    }
                });
                dateDlg.pickMonth();
                dateDlg.show();
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
        ChartXYView chartXYView = (ChartXYView) findViewById(R.id.chart_bg_view);
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
        sb.append(String.valueOf(year)).append("-");
        if (monthOfYear < 10) {
            sb.append("0").append(String.valueOf(monthOfYear));
        } else {
            sb.append(String.valueOf(monthOfYear));
        }
        return sb.toString();
    }
}
