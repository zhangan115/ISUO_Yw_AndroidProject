package com.isuo.yw2application.view.main.work.tool.return_tools;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.gson.Gson;
import com.isuo.yw2application.R;
import com.isuo.yw2application.app.Yw2Application;
import com.isuo.yw2application.common.ConstantStr;
import com.isuo.yw2application.mode.tools.ToolsRepository;
import com.isuo.yw2application.mode.tools.bean.CheckListBean;
import com.isuo.yw2application.mode.tools.bean.Tools;
import com.isuo.yw2application.mode.tools.bean.ToolsLog;
import com.isuo.yw2application.view.base.BaseActivity;
import com.isuo.yw2application.widget.CheckListItemLayout;
import com.sito.library.utils.DataUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * 归还
 * Created by zhangan on 2018/4/8.
 */

public class ReturnToolsActivity extends BaseActivity implements ReturnToolsContract.View {

    private ReturnToolsContract.Presenter mPresenter;
    private JSONObject jsonObject;
    private Calendar mCreateCalender;
    //view
    private TextView tvToolsReturnTime;
    private LinearLayout llCheckList, llAllCheckList;
    private List<CheckListBean> checkListBeans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutAndToolbar(R.layout.return_tools_activity, "归还");
        Tools tools = getIntent().getParcelableExtra(ConstantStr.KEY_BUNDLE_OBJECT);
        if (tools == null) {
            finish();
            return;
        }
        jsonObject = new JSONObject();
        new ReturnToolsPresenter(ToolsRepository.getRepository(this), this);
        mPresenter.subscribe();
        mCreateCalender = Calendar.getInstance(Locale.CHINA);
        ((TextView) findViewById(R.id.tvToolsName)).setText(tools.getToolName());
        tvToolsReturnTime = findViewById(R.id.tvToolsReturnTime);
        findViewById(R.id.llReturnTime).setOnClickListener(this);
        findViewById(R.id.btnReturn).setOnClickListener(this);
        llCheckList = findViewById(R.id.llCheckList);
        llAllCheckList = findViewById(R.id.llAllCheckList);
        mPresenter.getToolsLog(tools.getToolId());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnReturn:
                if (!jsonObject.has("returnTime")) {
                    Yw2Application.getInstance().showToast("请选择归还时间");
                    return;
                }
                if (checkListBeans != null) {
                    for (int i = 0; i < checkListBeans.size(); i++) {
                        if (TextUtils.isEmpty(checkListBeans.get(i).getValue())) {
                            Yw2Application.getInstance().showToast("请完成检测项");
                            return;
                        }
                    }
                    try {
                        jsonObject.put("checkListValues", new Gson().toJson(checkListBeans));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                mPresenter.returnTools(jsonObject);
                break;
            case R.id.llReturnTime:
                new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, final int year, final int month, final int dayOfMonth) {
                        new TimePickerDialog(ReturnToolsActivity.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                mCreateCalender.set(year, month, dayOfMonth, hourOfDay, minute);
                                String time = getDataStr(mCreateCalender);
                                tvToolsReturnTime.setText(time);
                                try {
                                    jsonObject.put("returnTime", time);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, mCreateCalender.get(Calendar.HOUR_OF_DAY), mCreateCalender.get(Calendar.MINUTE), true).show();
                    }
                }, mCreateCalender.get(Calendar.YEAR), mCreateCalender.get(Calendar.MONTH)
                        , mCreateCalender.get(Calendar.DAY_OF_MONTH))
                        .show();
                break;
        }
    }

    @Override
    public void returnSuccess() {
        setResult(Activity.RESULT_OK);
        finish();
    }

    @Override
    public void showToolsLog(ToolsLog toolsLog) {
        ((TextView) findViewById(R.id.tvToolsBorrowUser)).setText(toolsLog.getUseUser().getRealName());
        ((TextView) findViewById(R.id.tvToolsUse)).setText(toolsLog.getUse());
        ((TextView) findViewById(R.id.tvBorrowToolsTime)).setText(DataUtil.timeFormat(toolsLog.getUseTime(), "yyyy-MM-dd HH:mm"));
        try {
            jsonObject.put("logId", toolsLog.getLogId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (toolsLog.getCheckList() != null && toolsLog.getCheckList().size() > 0) {
            showCheckList(toolsLog.getCheckList());
        } else {
            noCheckList();
        }
    }

    @Override
    public void toolsLogError() {

    }

    @Override
    public void showCheckList(List<CheckListBean> checkListBeans) {
        this.checkListBeans = checkListBeans;
        llAllCheckList.setVisibility(View.VISIBLE);
        for (int i = 0; i < checkListBeans.size(); i++) {
            CheckListItemLayout layout = new CheckListItemLayout(this);
            layout.setCheckListBean(checkListBeans.get(i));
            llCheckList.addView(layout);
        }
    }

    @Override
    public void noCheckList() {
        llAllCheckList.setVisibility(View.GONE);
    }

    @Override
    public void setPresenter(ReturnToolsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    private String getDataStr(Calendar calendar) {
        return DataUtil.timeFormat(calendar.getTimeInMillis(), "yyyy-MM-dd HH:mm");
    }

}
