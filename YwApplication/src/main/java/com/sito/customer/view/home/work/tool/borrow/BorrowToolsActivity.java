package com.sito.customer.view.home.work.tool.borrow;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.sito.customer.R;
import com.sito.customer.app.CustomerApp;
import com.sito.customer.common.ConstantStr;
import com.sito.customer.mode.bean.employee.EmployeeBean;
import com.sito.customer.mode.tools.ToolsRepository;
import com.sito.customer.mode.tools.bean.CheckListBean;
import com.sito.customer.mode.tools.bean.Tools;
import com.sito.customer.view.BaseActivity;
import com.sito.customer.view.alarm.adduser.EmployeeActivity;
import com.sito.customer.widget.CheckListItemLayout;
import com.sito.customer.widget.DatePick;
import com.sito.library.utils.DataUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * 外借
 * Created by zhangan on 2018/4/8.
 */

public class BorrowToolsActivity extends BaseActivity implements BorrowContract.View {
    //data
    private BorrowContract.Presenter mPresenter;
    private JSONObject jsonObject;
    private Calendar mCreateCalender;
    private final static int REQUEST_CODE_USER = 2;
    private ArrayList<EmployeeBean> chooseEmployeeBeen;
    //view
    private EditText editToolsUse;
    private LinearLayout llCheckList, llAllCheckList;
    private TextView tvToolsBorrowUser, tvBorrowToolsTime, tvToolsReturnTime;
    private List<CheckListBean> checkListBeans;
    private Button borrowBtn;
    private DatePick datePick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutAndToolbar(R.layout.borrow_tools_activity, "外借");
        new BorrowPresenter(ToolsRepository.getRepository(this), this);
        mPresenter.subscribe();
        Tools tools = getIntent().getParcelableExtra(ConstantStr.KEY_BUNDLE_OBJECT);
        if (tools == null) {
            finish();
            return;
        }
        jsonObject = new JSONObject();
        mCreateCalender = Calendar.getInstance(Locale.CHINA);
        chooseEmployeeBeen = new ArrayList<>();
        try {
            jsonObject.put("toolId", String.valueOf(tools.getToolId()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        TextView tvToolsName = (TextView) findViewById(R.id.tvToolsName);
        tvToolsBorrowUser = (TextView) findViewById(R.id.tvToolsBorrowUser);
        editToolsUse = (EditText) findViewById(R.id.editToolsUse);
        tvBorrowToolsTime = (TextView) findViewById(R.id.tvBorrowToolsTime);
        tvToolsReturnTime = (TextView) findViewById(R.id.tvToolsReturnTime);
        llCheckList = (LinearLayout) findViewById(R.id.llCheckList);
        llAllCheckList = (LinearLayout) findViewById(R.id.llAllCheckList);
        datePick = (DatePick) findViewById(R.id.date_pick);
        tvToolsName.setText(tools.getToolName());
        findViewById(R.id.llBorrowUser).setOnClickListener(this);
        findViewById(R.id.llBorrowTime).setOnClickListener(this);
        findViewById(R.id.llReturnTime).setOnClickListener(this);
        borrowBtn = (Button) findViewById(R.id.btnBorrow);
        borrowBtn.setOnClickListener(this);
        borrowBtn.setVisibility(View.GONE);
        mPresenter.getCheckList(tools.getToolId());
        mPresenter.getToolsState(tools.getToolId());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llBorrowUser:
                Intent intentUser = new Intent(this, EmployeeActivity.class);
                intentUser.putParcelableArrayListExtra(ConstantStr.KEY_BUNDLE_LIST, chooseEmployeeBeen);
                intentUser.putExtra(ConstantStr.KEY_BUNDLE_BOOLEAN_1, true);
                startActivityForResult(intentUser, REQUEST_CODE_USER);
                break;
            case R.id.llBorrowTime:
                datePick.show(mCreateCalender, new DatePick.IChooseTimeListener() {
                    @Override
                    public void onDataChange(Calendar calendar) {
                        mCreateCalender = calendar;
                        String time = getDataStr(mCreateCalender);
                        tvBorrowToolsTime.setText(time);
                        try {
                            jsonObject.put("useTime", time);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                break;
            case R.id.llReturnTime:
                datePick.show(mCreateCalender, new DatePick.IChooseTimeListener() {
                    @Override
                    public void onDataChange(Calendar calendar) {
                        mCreateCalender = calendar;
                        String time = getDataStr(mCreateCalender);
                        try {
                            jsonObject.put("preReturnTime", time);
                            tvToolsReturnTime.setText(time);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                break;
            case R.id.btnBorrow:
                String useStr = editToolsUse.getText().toString();
                if (TextUtils.isEmpty(useStr)) {
                    CustomerApp.getInstance().showToast("请输入用途");
                    return;
                } else {
                    try {
                        jsonObject.put("use", useStr);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if (checkListBeans != null) {
                    for (int i = 0; i < checkListBeans.size(); i++) {
                        if (TextUtils.isEmpty(checkListBeans.get(i).getValue())) {
                            CustomerApp.getInstance().showToast("请完成检测项");
                            return;
                        }
                    }
                    try {
                        jsonObject.put("checkListValues", new Gson().toJson(checkListBeans));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                mPresenter.borrowTools(jsonObject);
                break;
        }
    }

    @Override
    public void setPresenter(BorrowContract.Presenter presenter) {
        mPresenter = presenter;
    }

    private String getDataStr(Calendar calendar) {
        return DataUtil.timeFormat(calendar.getTimeInMillis(), "yyyy-MM-dd HH:mm");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_USER && resultCode == RESULT_OK) {
            ArrayList<EmployeeBean> employeeBeen = data.getParcelableArrayListExtra(ConstantStr.KEY_BUNDLE_LIST);
            chooseEmployeeBeen.clear();
            if (employeeBeen != null && employeeBeen.size() > 0) {
                chooseEmployeeBeen.addAll(employeeBeen);
            }
            if (chooseEmployeeBeen.size() == 1) {
                try {
                    jsonObject.put("useUser", String.valueOf(chooseEmployeeBeen.get(0).getUser().getUserId()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                tvToolsBorrowUser.setText(chooseEmployeeBeen.get(0).getUser().getRealName());
            }
        }
    }

    @Override
    public void borrowSuccess() {
        setResult(Activity.RESULT_OK);
        finish();
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
    public void toolsCanBorrow() {
        borrowBtn.setVisibility(View.VISIBLE);
    }

    @Override
    public void toolsCantBorrow() {
        borrowBtn.setVisibility(View.VISIBLE);
        borrowBtn.setText("工具已经借出");
        borrowBtn.setTextColor(findColorById(R.color.color_bg_nav_normal));
        borrowBtn.setBackgroundColor(findColorById(R.color.news_time_gray));
    }
}
