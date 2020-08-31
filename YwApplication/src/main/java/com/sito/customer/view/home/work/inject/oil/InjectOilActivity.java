package com.sito.customer.view.home.work.inject.oil;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.sito.customer.R;
import com.sito.customer.app.CustomerApp;
import com.sito.customer.common.ConstantStr;
import com.sito.customer.mode.inject.bean.InjectEquipment;
import com.sito.customer.mode.inject.InjectOilRepository;
import com.sito.customer.mode.inject.bean.InjectItemBean;
import com.sito.customer.mode.inject.bean.UploadBean;
import com.sito.customer.view.BaseActivity;
import com.sito.customer.widget.InjectItemLayout;
import com.sito.library.utils.DataUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 注油
 * Created by zhangan on 2018/4/12.
 */

public class InjectOilActivity extends BaseActivity implements InjectOilContract.View {

    private InjectOilContract.Presenter mPresenter;
    private InjectEquipment equipment;
    private LinearLayout llItems;
    private TextView tvNextTime;
    private JSONObject jsonObject;
    private InjectItemBean itemBean;
    private int injectBefore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutAndToolbar(R.layout.inject_oil_activity, "注油");
        equipment = getIntent().getParcelableExtra(ConstantStr.KEY_BUNDLE_OBJECT);
        injectBefore = getIntent().getIntExtra(ConstantStr.KEY_BUNDLE_INT, 1);
        if (equipment == null) {
            finish();
            return;
        }
        new InjectOilAPresenter(this, InjectOilRepository.getRepository(this));
        jsonObject = new JSONObject();
        findViewById(R.id.btnInject).setOnClickListener(this);
        findViewById(R.id.llNextTime).setOnClickListener(this);
        llItems = (LinearLayout) findViewById(R.id.llItems);
        TextView name = (TextView) findViewById(R.id.tvEquipmentName);
        name.setText(equipment.getEquipmentName());
        TextView sn = (TextView) findViewById(R.id.tvEquipmentSn);
        if (TextUtils.isEmpty(equipment.getEquipmentSn())) {
            sn.setVisibility(View.GONE);
        } else {
            sn.setText(equipment.getEquipmentSn());
        }
        TextView tvTime = (TextView) findViewById(R.id.tvTime);
        if (equipment.getInjectionOil() != null) {
            if (injectBefore == 1) {
                if (equipment.getBeforeInjectionOil() != null) {
                    tvTime.setText(DataUtil.timeFormat(equipment.getBeforeInjectionOil().getCreateTime(), "yyyy-MM-dd"));
                }
            } else {
                if (equipment.getBackInjectionOil() != null) {
                    tvTime.setText(DataUtil.timeFormat(equipment.getBackInjectionOil().getCreateTime(), "yyyy-MM-dd"));
                }
            }
        }
        tvNextTime = (TextView) findViewById(R.id.tvNextTime);
        mPresenter.subscribe();
        mPresenter.getInjectList();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnInject:
                if (this.itemBean == null) {
                    return;
                }
                try {
                    if (!jsonObject.has("nextTime")) {
                        CustomerApp.getInstance().showToast("请选择下次注油时间");
                        return;
                    }
                    List<UploadBean> uploadBeans = new ArrayList<>();
                    for (int i = 0; i < itemBean.getList().size(); i++) {
                        String value = itemBean.getList().get(i).getValue();
                        if (TextUtils.isEmpty(value) && itemBean.getList().get(i).getIsRequeired() == 1) {
                            CustomerApp.getInstance().showToast("有漏检");
                            return;
                        }
                        uploadBeans.add(new UploadBean(itemBean.getList().get(i).getItemId()
                                , itemBean.getList().get(i).getValue()));
                    }
                    jsonObject.put("equipmentId", equipment.getEquipmentId());
                    jsonObject.put("injectionOilDataList", new Gson().toJson(uploadBeans));
                    jsonObject.put("beforeOrBack", injectBefore);
                    mPresenter.injectOilEquipment(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.llNextTime:
                final Calendar mCreateCalender = Calendar.getInstance();
                DatePickerDialog dig = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, final int year, final int month, final int dayOfMonth) {
                        mCreateCalender.set(year, month, dayOfMonth);
                        String time = getDataStr(mCreateCalender);
                        tvNextTime.setText(time);
                        try {
                            jsonObject.put("nextTime", mCreateCalender.getTimeInMillis());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, mCreateCalender.get(Calendar.YEAR), mCreateCalender.get(Calendar.MONTH)
                        , mCreateCalender.get(Calendar.DAY_OF_MONTH));
                DatePicker dp = dig.getDatePicker();
                dp.setMinDate(mCreateCalender.getTimeInMillis());
                dig.show();
                break;
        }

    }

    @Override
    public void setPresenter(InjectOilContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showInjectItem(InjectItemBean itemBean) {
        this.itemBean = itemBean;
        for (int i = 0; i < itemBean.getList().size(); i++) {
            InjectItemLayout layout = new InjectItemLayout(this);
            layout.setData(itemBean.getList().get(i));
            llItems.addView(layout);
        }
        findViewById(R.id.btnInject).setVisibility(View.VISIBLE);
        llItems.setVisibility(View.VISIBLE);
    }

    @Override
    public void noData() {

    }


    @Override
    public void injectSuccess() {
        CustomerApp.getInstance().showToast("注油完成");
        setResult(Activity.RESULT_OK);
        finish();
    }

    @Override
    public void injectFail() {

    }

    private String getDataStr(Calendar calendar) {
        return DataUtil.timeFormat(calendar.getTimeInMillis(), "yyyy-MM-dd");
    }


}
