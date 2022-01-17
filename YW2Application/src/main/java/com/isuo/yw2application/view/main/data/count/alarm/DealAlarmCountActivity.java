package com.isuo.yw2application.view.main.data.count.alarm;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.isuo.yw2application.R;
import com.isuo.yw2application.app.Yw2Application;
import com.isuo.yw2application.mode.bean.count.FaultCount;
import com.isuo.yw2application.mode.bean.discover.DeptType;
import com.isuo.yw2application.utils.ChooseDateDialog;
import com.isuo.yw2application.view.base.BaseActivity;
import com.sito.library.chart.ChartData;
import com.sito.library.chart.ChartView;
import com.sito.library.chart.ChartXYView;
import com.sito.library.utils.DisplayUtil;
import com.sito.library.widget.DateDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * 故障处理统计
 * Created by zhangan on 2017/7/2.
 */

public class DealAlarmCountActivity extends BaseActivity implements FaultCountContract.View {

    private String deptType = "2";//部门ID
    private LinearLayout addViewLayout;
    private FaultCountContract.Presenter mPresenter;
    private List<FaultCount> mFaultCounts;
    private List<DeptType> mDeptTypes;
    private TextView chooseDept, chooseTime;
    private Calendar mCurrentCalendar;
    private List<String> listStr;
    private String mDeptId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutAndToolbar(R.layout.activity_fault_work, "事件处理");
        new FaultCountPresenter(Yw2Application.getInstance().getCountRepositoryComponent().getRepository(), this);
        mCurrentCalendar = Calendar.getInstance(Locale.CHINA);
        mPresenter.getDeptList(deptType);
        addViewLayout = findViewById(R.id.ll_add_view);
        chooseDept = findViewById(R.id.tv_choose_user);
        chooseTime = findViewById(R.id.tv_choose_time);
        chooseTime.setText(getDate(mCurrentCalendar));
        chooseTime.setOnClickListener(this);
        chooseDept.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_choose_user:
                if (listStr != null && listStr.size() > 0) {
                    new MaterialDialog.Builder(this)
                            .items(listStr)
                            .itemsCallback(new MaterialDialog.ListCallback() {
                                @Override
                                public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                                    mDeptId = String.valueOf(mDeptTypes.get(position).getDeptId());
                                    chooseDept.setText(mDeptTypes.get(position).getDeptName());
                                    mPresenter.getFaultCountData(mDeptId, getDate(mCurrentCalendar));
                                }
                            })
                            .show();
                }
                break;
            case R.id.tv_choose_time:
                new ChooseDateDialog(this, R.style.MyDateDialog)
                        .pickMonth()
                        .setCurrent(mCurrentCalendar)
                        .setResultListener(new ChooseDateDialog.OnDateChooseListener() {
                            @Override
                            public void onDate(Calendar calendar) {
                                mCurrentCalendar = calendar;
                                chooseTime.setText(getDate(mCurrentCalendar));
                                if (TextUtils.isEmpty(mDeptId)) {
                                    return;
                                }
                                mPresenter.getFaultCountData(mDeptId, getDate(mCurrentCalendar));
                            }
                        }).show();
                break;
        }
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
    public void showDeptList(List<DeptType> deptTypes) {
        mDeptTypes = deptTypes;
        if (mDeptTypes != null && mDeptTypes.size() > 0) {
            chooseDept.setVisibility(View.VISIBLE);
        }
        listStr = new ArrayList<>();
        for (int i = 0; i < mDeptTypes.size(); i++) {
            listStr.add(mDeptTypes.get(i).getDeptName());
        }
        mDeptId = String.valueOf(mDeptTypes.get(0).getDeptId());
        chooseDept.setText(mDeptTypes.get(0).getDeptName());
        mPresenter.getFaultCountData(mDeptId, getDate(mCurrentCalendar));
    }

    @Override
    public void showFaultCountData(List<FaultCount> faultCounts) {
        this.mFaultCounts = faultCounts;
        addViewLayout.removeAllViews();
        List<ChartData> chartDataList = new ArrayList<>();
        for (int i = 0; i < faultCounts.size(); i++) {
            ChartData chartData = new ChartData();
            List<ChartData.Value> values = new ArrayList<>();
            values.add(new ChartData.Value(mFaultCounts.get(i).getAFaultCount()));
            values.add(new ChartData.Value(mFaultCounts.get(i).getBFaultCount()));
            values.add(new ChartData.Value(mFaultCounts.get(i).getCFaultCount()));
            chartData.setValueList(values);
            chartData.setUserName(mFaultCounts.get(i).getRealName());
            chartDataList.add(chartData);
        }
        int[] colors = new int[]{findColorById(R.color.colorFF6461)
                , findColorById(R.color.colorF0A426)
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
    public void setPresenter(FaultCountContract.Presenter presenter) {
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
