package com.sito.customer.view.count.work;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.sito.customer.R;
import com.sito.customer.app.CustomerApp;
import com.sito.customer.mode.bean.count.WorkCount;
import com.sito.customer.mode.bean.discover.DeptType;
import com.sito.customer.view.BaseActivity;
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
 * 工作量统计
 * Created by zhangan on 2017/7/2.
 */

public class WorkCountActivity extends BaseActivity implements WorkCountContract.View {

    private String deptType = "1,2";//部门ID
    private LinearLayout addViewLayout;
    private WorkCountContract.Presenter mPresenter;
    private List<WorkCount> mWorkCounts;
    private List<DeptType> mDeptTypes;
    private TextView chooseDept, chooseTime;
    private Calendar mCurrentCalendar;
    private List<String> listStr;
    private String mDeptId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutAndToolbar(R.layout.activity_count_work, "工作量统计");
        new WorkCountPresenter(CustomerApp.getInstance().getCountRepositoryComponent().getRepository(), this);
        mCurrentCalendar = Calendar.getInstance(Locale.CHINA);
        mPresenter.getDeptList(deptType);
        addViewLayout = (LinearLayout) findViewById(R.id.ll_add_view);
        chooseDept = (TextView) findViewById(R.id.tv_choose_user);
        chooseTime = (TextView) findViewById(R.id.tv_choose_time);
        chooseTime.setText(getDate(mCurrentCalendar));
        chooseTime.setOnClickListener(this);
        chooseDept.setOnClickListener(this);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_choose_user:
                new MaterialDialog.Builder(this)
                        .items(listStr)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                                mDeptId = String.valueOf(mDeptTypes.get(position).getDeptId());
                                chooseDept.setText(mDeptTypes.get(position).getDeptName());
                                mPresenter.getWorkCountData(mDeptId, getDate(mCurrentCalendar));
                            }
                        })
                        .show();
                break;
            case R.id.tv_choose_time:
                final DateDialog dateDlg = new DateDialog(this, R.style.MyDateDialog, mCurrentCalendar.get(Calendar.YEAR)
                        , mCurrentCalendar.get(Calendar.MONTH) + 1
                        , mCurrentCalendar.get(Calendar.DAY_OF_MONTH));
                dateDlg.setConfirmButton(getString(R.string.ok), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mCurrentCalendar = dateDlg.getDate();
                        chooseTime.setText(getDate(mCurrentCalendar));
                        mPresenter.getWorkCountData(mDeptId, getDate(mCurrentCalendar));
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
    public void showDeptList(List<DeptType> deptTypes) {
        mDeptTypes = deptTypes;
        listStr = new ArrayList<>();
        for (int i = 0; i < mDeptTypes.size(); i++) {
            listStr.add(mDeptTypes.get(i).getDeptName());
        }
        mDeptId = String.valueOf(mDeptTypes.get(0).getDeptId());
        chooseDept.setText(mDeptTypes.get(0).getDeptName());
        mPresenter.getWorkCountData(mDeptId, getDate(mCurrentCalendar));
    }

    @Override
    public void showWorkCountData(List<WorkCount> workCounts) {
        this.mWorkCounts = workCounts;
        addViewLayout.removeAllViews();
        List<ChartData> chartDataList = new ArrayList<>();
        for (int i = 0; i < workCounts.size(); i++) {
            ChartData chartData = new ChartData();
            List<ChartData.Value> values = new ArrayList<>();
            values.add(new ChartData.Value(mWorkCounts.get(i).getTaskCount()));
            values.add(new ChartData.Value(mWorkCounts.get(i).getRepairCount()));
            values.add(new ChartData.Value(mWorkCounts.get(i).getIncrementCount()));
            chartData.setValueList(values);
            chartData.setUserName(mWorkCounts.get(i).getRealName());
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
    public void setPresenter(WorkCountContract.Presenter presenter) {
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
