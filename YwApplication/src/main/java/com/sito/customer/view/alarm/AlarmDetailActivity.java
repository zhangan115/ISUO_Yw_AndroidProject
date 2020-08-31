package com.sito.customer.view.alarm;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.afollestad.materialdialogs.MaterialDialog;
import com.sito.customer.R;
import com.sito.customer.app.CustomerApp;
import com.sito.customer.common.ConstantStr;
import com.sito.customer.mode.bean.User;
import com.sito.customer.mode.bean.employee.EmployeeBean;
import com.sito.customer.mode.bean.fault.FaultDetail;
import com.sito.customer.mode.bean.fault.JobPackageBean;
import com.sito.customer.utils.Utils;
import com.sito.customer.view.BaseActivity;
import com.sito.customer.view.alarm.adduser.EmployeeActivity;
import com.sito.customer.widget.FlowLayout;
import com.sito.customer.widget.PlaySoundLayout;
import com.sito.customer.widget.ProgressItemLayout;
import com.sito.customer.widget.ProgressTitleLayout;
import com.sito.customer.widget.ShowImageLayout;
import com.sito.library.utils.DataUtil;
import com.sito.library.utils.DisplayUtil;
import com.sito.library.widget.ShowUserLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * 故障详情界面（分已检修，未检修）
 * Created by zhangan on 2017/7/2.
 */

public class AlarmDetailActivity extends BaseActivity implements AlarmDetailContract.View {

    private AlarmDetailContract.Presenter mPresenter;
    private LinearLayout alarmCardView, dealCardView, careContent, careLayout;
    private ScrollView scrollView;
    private ShowImageLayout faultImageLayout, repairImageLayout;
    private PlaySoundLayout faultPlaySoundLayout, repairPlaySoundLayout;
    private ProgressTitleLayout progressTitleLayout1;
    private ProgressTitleLayout progressTitleLayout2;
    private ProgressTitleLayout progressTitleLayout3;
    private ImageView ivYes, ivNo;
    private TextView[] titleTv;
    private FaultDetail mFaultDetail;
    private boolean isAwait, isToRepair;
    private int REQUEST_CODE_ADD_USER = 200;
    private ArrayList<EmployeeBean> employeeBeen;
    private String startTime;
    private String endTime;
    private String jobId;
    private int actionType, careType;
    private String careEndTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutAndToolbar(R.layout.activity_alarm_detail, " 故障详情");
        isAwait = getIntent().getBooleanExtra(ConstantStr.KEY_BUNDLE_BOOLEAN, false);
        new AlarmDetailPresenter(CustomerApp.getInstance().getFaultRepositoryComponent().getRepository(), this);
        String mFaultId = getIntent().getStringExtra(ConstantStr.KEY_BUNDLE_STR);
        alarmCardView = (LinearLayout) findViewById(R.id.card_view_alarm_upload);
        dealCardView = (LinearLayout) findViewById(R.id.card_view_alarm_deal);
        careContent = (LinearLayout) findViewById(R.id.ll_care_content);
        careLayout = (LinearLayout) findViewById(R.id.ll_care);
        scrollView = (ScrollView) findViewById(R.id.scroll_view);
        scrollView.setVisibility(View.GONE);

        faultImageLayout = (ShowImageLayout) findViewById(R.id.fault_show_image);
        repairImageLayout = (ShowImageLayout) findViewById(R.id.repair_show_image);
        faultPlaySoundLayout = (PlaySoundLayout) findViewById(R.id.fault_sound);
        repairPlaySoundLayout = (PlaySoundLayout) findViewById(R.id.repair_play_sound);
        progressTitleLayout1 = (ProgressTitleLayout) findViewById(R.id.progress_1);
        progressTitleLayout2 = (ProgressTitleLayout) findViewById(R.id.progress_2);
        progressTitleLayout3 = (ProgressTitleLayout) findViewById(R.id.progress_3);
        ProgressTitleLayout progressTitleLayout4 = (ProgressTitleLayout) findViewById(R.id.progress_4);
        progressTitleLayout1.setContent(findDrawById(R.drawable.found_icon_press), "故障上报", true);
        progressTitleLayout2.setContent(findDrawById(R.drawable.assign_icon_press), "指派流转", true);
        progressTitleLayout3.setContent(findDrawById(R.drawable.overhaul_icon_press), "故障检修", true);
        progressTitleLayout4.setContent(findDrawById(R.drawable.result_icon_press), "结果检验", true);
        ivNo = (ImageView) findViewById(R.id.iv_no);
        ivYes = (ImageView) findViewById(R.id.iv_yes);
        mPresenter.getFaultDetailData(mFaultId);
        mPresenter.getJobPackage();
    }

    @Override
    public void showData(final FaultDetail faultDetail) {
        if (faultDetail == null || faultDetail.getFaultFlows().size() == 0) {
            return;
        }
        this.mFaultDetail = faultDetail;
        scrollView.setVisibility(View.VISIBLE);
        TextView id_equip_address = (TextView) findViewById(R.id.id_equip_address);
        TextView id_equip_state = (TextView) findViewById(R.id.id_equip_state);
        TextView tv_equip_name = (TextView) findViewById(R.id.tv_equip_name);
        TextView tv_start_time = (TextView) findViewById(R.id.tv_start_time);
        findViewById(R.id.tv_upload).setOnClickListener(this);
        TextView tv_user = (TextView) findViewById(R.id.tv_user);
        TextView tv_alarm_type = (TextView) findViewById(R.id.tv_alarm_type);
        id_equip_address.setText(MessageFormat.format("属地:{0}", faultDetail.getEquipment().getRoom().getRoomName()));
        id_equip_state.setText(CustomerApp.getInstance().getMapOption().get("9").get(String.valueOf(faultDetail.getFaultState())));
        if (faultDetail.getEquipment() != null) {
            if (TextUtils.isEmpty(faultDetail.getEquipment().getEquipmentSn())) {
                tv_equip_name.setText(faultDetail.getEquipment().getEquipmentName());
            } else {
                tv_equip_name.setText(MessageFormat.format("{0}({1})", faultDetail.getEquipment().getEquipmentName()
                        , faultDetail.getEquipment().getEquipmentSn()));
            }
        }
        tv_start_time.setText(MessageFormat.format("上报时间:{0}", DataUtil.timeFormat(faultDetail.getCreateTime(), "yyyy-MM-dd HH:mm")));
        tv_user.setText(MessageFormat.format("上报人:{0}", faultDetail.getUser().getRealName()));
        if (faultDetail.getFaultType() == 1) {
            tv_alarm_type.setText("A");
            tv_alarm_type.setTextColor(findColorById(R.color.colorAlarmA));
        } else if (faultDetail.getFaultType() == 2) {
            tv_alarm_type.setText("B");
            tv_alarm_type.setTextColor(findColorById(R.color.colorAlarmB));
        } else {
            tv_alarm_type.setText("C");
            tv_alarm_type.setTextColor(findColorById(R.color.colorAlarmC));
        }
        faultPlaySoundLayout.setContent(faultDetail.getVoiceUrl(), faultDetail.getSoundTimescale(), "故障描述", faultDetail.getFaultDescript());
        faultPlaySoundLayout.setOnPlaySoundListener(new PlaySoundLayout.OnPlaySoundListener() {
            @Override
            public void onPlaySound() {
                if (repairPlaySoundLayout != null) {
                    repairPlaySoundLayout.cancelPlay();
                }
            }
        });
        String[] images = new String[faultDetail.getFaultPics().size()];
        for (int i = 0; i < faultDetail.getFaultPics().size(); i++) {
            images[i] = faultDetail.getFaultPics().get(i).getPicUrl();
        }
        faultImageLayout.showImage(images);
        if (isAwait) {
            dealCardView.setVisibility(View.GONE);
            if (faultDetail.getFaultFlows().size() > faultDetail.getCurrentFlowIndex()
                    && faultDetail.getFaultFlows().get(faultDetail.getCurrentFlowIndex()).getUsersN() != null
                    && faultDetail.getFaultFlows().get(faultDetail.getCurrentFlowIndex()).getUsersN().size() > 0
                    && faultDetail.getFaultState() < 3) {
                if (!TextUtils.isEmpty(faultDetail.getFaultFlows().get(faultDetail.getCurrentFlowIndex()).getUsersNext())) {
                    String[] ids = faultDetail.getFaultFlows().get(faultDetail.getCurrentFlowIndex()).getUsersNext().split(",");
                    for (String userId : ids) {
                        if (userId.equals(String.valueOf(CustomerApp.getInstance().getCurrentUser().getUserId()))) {
                            alarmCardView.setVisibility(View.VISIBLE);
                            break;
                        }
                    }
                }
            }
            isToRepair = faultDetail.getDefaultFlowId() != null && faultDetail.getDefaultFlow() != null && faultDetail.getUsersN() != null;
            if (alarmCardView.getVisibility() == View.VISIBLE) {
                TextView tvLeft = (TextView) findViewById(R.id.tv_left);
                TextView tvCenter = (TextView) findViewById(R.id.tv_center);
                TextView tvRight = (TextView) findViewById(R.id.tv_right);
                tvLeft.setOnClickListener(this);
                tvCenter.setOnClickListener(this);
                tvRight.setOnClickListener(this);
                if (CustomerApp.getInstance().getCurrentUser().getCustomer().getIsOpen() == 1) {
                    tvCenter.setVisibility(View.GONE);
                    titleTv = new TextView[2];
                    if (isToRepair) {
                        tvLeft.setText(R.string.title_alarm_detail_1);
                        showPointView();
                    } else {
                        tvLeft.setText(R.string.title_alarm_detail_3);
                        showOverhaulView();
                    }
                    tvRight.setText(R.string.title_alarm_detail_2);
                    titleTv[0] = tvLeft;
                    titleTv[1] = tvRight;
                } else if (CustomerApp.getInstance().getCurrentUser().getIsRepair() != 1) {
                    titleTv = new TextView[2];
                    tvCenter.setVisibility(View.GONE);
                    tvLeft.setText(R.string.title_alarm_detail_1);
                    tvRight.setText(R.string.title_alarm_detail_2);
                    titleTv[0] = tvLeft;
                    titleTv[1] = tvRight;
                    showPointView();
                } else {
                    titleTv = new TextView[3];
                    tvLeft.setText(R.string.title_alarm_detail_3);
                    tvCenter.setText(R.string.title_alarm_detail_1);
                    tvRight.setText(R.string.title_alarm_detail_2);
                    titleTv[0] = tvLeft;
                    titleTv[1] = tvCenter;
                    titleTv[2] = tvRight;
                    showOverhaulView();
                }
                if (titleTv.length == 2) {
                    titleTv[0].setTextColor(findColorById(R.color.colorWhite));
                    titleTv[0].setBackground(findDrawById(R.drawable.bg_point_blue_left));
                    titleTv[1].setTextColor(findColorById(R.color.color6F88A9));
                    titleTv[1].setBackground(findDrawById(R.drawable.bg_point_gray_right));
                } else {
                    titleTv[0].setTextColor(findColorById(R.color.colorWhite));
                    titleTv[0].setBackground(findDrawById(R.drawable.bg_point_blue_left));
                    titleTv[1].setTextColor(findColorById(R.color.color6F88A9));
                    titleTv[1].setBackground(findDrawById(R.drawable.bg_point_gray_center));
                    titleTv[2].setTextColor(findColorById(R.color.color6F88A9));
                    titleTv[2].setBackground(findDrawById(R.drawable.bg_point_gray_right));
                }
                careLayout.setVisibility(mFaultDetail.getEquipment().getIsOnFocus() > 0 ? View.GONE : View.VISIBLE);
                careEquipment();
            }
        } else {
            alarmCardView.setVisibility(View.GONE);
            if (faultDetail.getRepair() == null || faultDetail.getRepair().getRepairState() < 3) {
                dealCardView.setVisibility(View.GONE);
            } else {
                dealCardView.setVisibility(View.VISIBLE);
                TextView tv_time_deal = (TextView) findViewById(R.id.tv_repair_time);
                TextView tv_repair_state = (TextView) findViewById(R.id.tv_repair_state);
                tv_repair_state.setText(CustomerApp.getInstance().getMapOption().get("4").get(String.valueOf(faultDetail.getRepair().getRepairResult())));
                tv_time_deal.setText(MessageFormat.format("检修时间:{0}", DataUtil.timeFormat(faultDetail.getRepair().getCommitTime(), "yyyy-MM-dd HH:mm")));
                repairPlaySoundLayout.setContent(faultDetail.getRepair().getVoiceUrl()
                        , faultDetail.getRepair().getSoundTimescale(), "检修结果", faultDetail.getRepair().getRepairRemark());
                repairPlaySoundLayout.setOnPlaySoundListener(new PlaySoundLayout.OnPlaySoundListener() {
                    @Override
                    public void onPlaySound() {
                        if (faultPlaySoundLayout != null) {
                            faultPlaySoundLayout.cancelPlay();
                        }
                    }
                });
                String[] repairImages = new String[faultDetail.getFaultPics().size()];
                for (int i = 0; i < faultDetail.getRepair().getRepairPics().size(); i++) {
                    repairImages[i] = faultDetail.getRepair().getRepairPics().get(i).getPicUrl();
                }
                repairImageLayout.showImage(repairImages);
            }
        }
        if (faultDetail.getFaultFlows() != null) {
            addInfoToLayout(faultDetail.getFaultFlows());
        }
    }

    private void careEquipment() {
        findViewById(R.id.ll_care_yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivNo.setImageDrawable(findDrawById(R.drawable.choose_normal));
                ivYes.setImageDrawable(findDrawById(R.drawable.choose_select));
                careContent.setVisibility(View.VISIBLE);
                careType = 1;
            }
        });
        findViewById(R.id.ll_care_no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivNo.setImageDrawable(findDrawById(R.drawable.choose_select));
                ivYes.setImageDrawable(findDrawById(R.drawable.choose_normal));
                careContent.setVisibility(View.GONE);
                careType = 0;
            }
        });
        final Calendar mCurrentCalender = Calendar.getInstance(Locale.CHINA);
        mCurrentCalender.add(Calendar.DAY_OF_YEAR, 1);
        final TextView care_end_time = (TextView) findViewById(R.id.tv_care_end_time);
        care_end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AlarmDetailActivity.this
                        , new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, final int year, final int month, final int dayOfMonth) {
                        mCurrentCalender.set(year, month, dayOfMonth);
                        careEndTime = DataUtil.timeFormat(mCurrentCalender.getTimeInMillis(), "yyyy-MM-dd");
                        care_end_time.setText(careEndTime);
                    }
                }, mCurrentCalender.get(Calendar.YEAR)
                        , mCurrentCalender.get(Calendar.MONTH)
                        , mCurrentCalender.get(Calendar.DAY_OF_MONTH));
                DatePicker dp = datePickerDialog.getDatePicker();
                dp.setMinDate(mCurrentCalender.getTimeInMillis());
                datePickerDialog.show();
            }
        });
    }

    @Override
    public void showLoading() {
        showEvLoading();
    }

    @Override
    public void hideLoading() {
        hideEvLoading();
    }

    @Override
    public void uploadSuccess() {
        if (careType == 1) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("equipmentId", mFaultDetail.getEquipment().getEquipmentId());
                jsonObject.put("endTime", careEndTime);
                EditText et_care_content = (EditText) findViewById(R.id.et_care_content);
                if (!TextUtils.isEmpty(et_care_content.getText().toString())) {
                    jsonObject.put("description", et_care_content.getText().toString());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mPresenter.careEquipment(jsonObject);
        } else {
            hideProgressDialog();
            setResult(RESULT_OK);
            finish();
        }
    }

    @Override
    public void uploadFail() {
        hideProgressDialog();
    }

    @Override
    public void uploadProgress() {
        showProgressDialog("流转中....");
    }

    private JobPackageBean jobPackageBeen;

    @Override
    public void showJobPackage(JobPackageBean jobPackageBeen) {
        this.jobPackageBeen = jobPackageBeen;
    }

    @Override
    public void careSuccess() {
        hideProgressDialog();
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void careFail() {
        hideProgressDialog();
    }

    @Override
    public void setPresenter(AlarmDetailContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_left:
                if (titleTv.length == 2) {
                    titleTv[0].setTextColor(findColorById(R.color.colorWhite));
                    titleTv[0].setBackground(findDrawById(R.drawable.bg_point_blue_left));
                    titleTv[1].setTextColor(findColorById(R.color.color6F88A9));
                    titleTv[1].setBackground(findDrawById(R.drawable.bg_point_gray_right));
                    if (CustomerApp.getInstance().getCurrentUser().getCustomer().getIsOpen() == 1) {
                        if (isToRepair) {
                            showPointView();
                        } else {
                            showOverhaulView();
                        }
                    } else if (CustomerApp.getInstance().getCurrentUser().getIsRepair() != 1) {
                        showPointView();
                    } else {
                        showOverhaulView();
                    }
                } else {
                    titleTv[0].setTextColor(findColorById(R.color.colorWhite));
                    titleTv[0].setBackground(findDrawById(R.drawable.bg_point_blue_left));
                    titleTv[1].setTextColor(findColorById(R.color.color6F88A9));
                    titleTv[1].setBackground(findDrawById(R.drawable.bg_point_gray_center));
                    titleTv[2].setTextColor(findColorById(R.color.color6F88A9));
                    titleTv[2].setBackground(findDrawById(R.drawable.bg_point_gray_right));
                    showOverhaulView();
                }
                break;
            case R.id.tv_center:
                titleTv[0].setTextColor(findColorById(R.color.color6F88A9));
                titleTv[0].setBackground(findDrawById(R.drawable.bg_point_gray_left));
                titleTv[1].setTextColor(findColorById(R.color.colorWhite));
                titleTv[1].setBackground(findDrawById(R.drawable.bg_point_blue_center));
                titleTv[2].setTextColor(findColorById(R.color.color6F88A9));
                titleTv[2].setBackground(findDrawById(R.drawable.bg_point_gray_right));
                showPointView();
                break;
            case R.id.tv_right:
                if (titleTv.length == 2) {
                    titleTv[0].setTextColor(findColorById(R.color.color6F88A9));
                    titleTv[0].setBackground(findDrawById(R.drawable.bg_point_gray_left));
                    titleTv[1].setTextColor(findColorById(R.color.colorWhite));
                    titleTv[1].setBackground(findDrawById(R.drawable.bg_point_blue_right));
                } else {
                    titleTv[0].setTextColor(findColorById(R.color.color6F88A9));
                    titleTv[0].setBackground(findDrawById(R.drawable.bg_point_gray_left));
                    titleTv[1].setTextColor(findColorById(R.color.color6F88A9));
                    titleTv[1].setBackground(findDrawById(R.drawable.bg_point_gray_center));
                    titleTv[2].setTextColor(findColorById(R.color.colorWhite));
                    titleTv[2].setBackground(findDrawById(R.drawable.bg_point_blue_right));
                }
                showCloseView();
                break;
            case R.id.tv_upload:
                if (careType == 1 && TextUtils.isEmpty(careEndTime)) {
                    CustomerApp.getInstance().showToast("请选择关注截止日期");
                    return;
                }
                if (actionType == 1) {
                    Map<String, String> map = new HashMap<>();
                    if (CustomerApp.getInstance().getCurrentUser().getCustomer().getIsOpen() == 1) {
                        map.put("usersNext", "-");
                        if (mFaultDetail.getDefaultFlowId() == null) {
                            CustomerApp.getInstance().showToast("未配置流转人员");
                            return;
                        } else {
                            map.put("defaultFlowId", String.valueOf(mFaultDetail.getDefaultFlowId()));
                        }
                    } else {
                        if (TextUtils.isEmpty(getUserIds())) {
                            CustomerApp.getInstance().showToast("请选择人员");
                            return;
                        } else {
                            map.put("usersNext", getUserIds());
                        }
                    }
                    EditText editText = (EditText) findViewById(R.id.et_content);
                    if (!TextUtils.isEmpty(editText.getText().toString())) {
                        map.put("flowRemark", editText.getText().toString());
                    }
                    map.put("faultId", String.valueOf(mFaultDetail.getFaultId()));
                    mPresenter.uploadFaultInfo(map);
                } else if (actionType == 2) {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("faultId", mFaultDetail.getFaultId());
                        if (TextUtils.isEmpty(getUserIds())) {
                            CustomerApp.getInstance().showToast("请选择检修人员");
                            return;
                        }
                        jsonObject.put("usersNext", getUserIds());
                        if (TextUtils.isEmpty(startTime)) {
                            CustomerApp.getInstance().showToast("请选择开始时间");
                            return;
                        }
                        jsonObject.put("startTime", startTime);
                        if (TextUtils.isEmpty(endTime)) {
                            CustomerApp.getInstance().showToast("请选择结束时间");
                            return;
                        }
                        jsonObject.put("endTime", endTime);
                        EditText editText = (EditText) findViewById(R.id.et_content);
                        if (!TextUtils.isEmpty(editText.getText().toString())) {
                            jsonObject.put("repairIntro", editText.getText().toString());
                        }
                        jsonObject.put("jobId", jobId);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mPresenter.overhaulFault(jsonObject);
                } else {
                    Map<String, String> map = new HashMap<>();
                    map.put("faultId", String.valueOf(mFaultDetail.getFaultId()));
                    EditText editText = (EditText) findViewById(R.id.et_content);
                    if (!TextUtils.isEmpty(editText.getText().toString())) {
                        map.put("flowRemark", editText.getText().toString());
                    }
                    mPresenter.closeFault(map);
                }
                break;
        }
    }

    private void showPointView() {
        actionType = 1;
        LinearLayout to_overhaul = (LinearLayout) findViewById(R.id.to_overhaul);
        LinearLayout ll_choose_user = (LinearLayout) findViewById(R.id.ll_choose_user);
        to_overhaul.setVisibility(View.GONE);
        ll_choose_user.setVisibility(View.VISIBLE);
        TextView tv_next_user = (TextView) findViewById(R.id.tv_next_user);
        tv_next_user.setText("指派给");
        LinearLayout ll_add_user = (LinearLayout) findViewById(R.id.ll_add_user);
        if (CustomerApp.getInstance().getCurrentUser().getCustomer().getIsOpen() == 1) {
            if (mFaultDetail.getDefaultFlow() != null && mFaultDetail.getUsersN() != null) {
                tv_next_user.setVisibility(View.GONE);
                FlowLayout flowLayout = new FlowLayout(this);
                flowLayout.setContent(mFaultDetail.getDefaultFlow().getDefaultFlowName(), mFaultDetail.getUsersN());
                if (ll_add_user.getChildCount() == 0) {
                    ll_add_user.addView(flowLayout);
                }
            } else {
                tv_next_user.setVisibility(View.VISIBLE);
            }
        } else {
            if (ll_add_user.getChildCount() == 0) {
                ll_add_user.addView(getAddUserBtn());
            }
        }
    }

    private int chooseWhich;

    private void showOverhaulView() {
        actionType = 2;
        LinearLayout to_overhaul = (LinearLayout) findViewById(R.id.to_overhaul);
        LinearLayout ll_choose_user = (LinearLayout) findViewById(R.id.ll_choose_user);
        to_overhaul.setVisibility(View.VISIBLE);
        ll_choose_user.setVisibility(View.VISIBLE);
        TextView tv_next_user = (TextView) findViewById(R.id.tv_next_user);
        tv_next_user.setText("指派给");
        final TextView start_time = (TextView) findViewById(R.id.start_time);
        final TextView end_time = (TextView) findViewById(R.id.end_time);
        final Calendar mCurrentCalender = Calendar.getInstance(Locale.CHINA);
        start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AlarmDetailActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, final int year, final int month, final int dayOfMonth) {
                        new TimePickerDialog(AlarmDetailActivity.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                mCurrentCalender.set(year, month, dayOfMonth, hourOfDay, minute);
                                startTime = getDataStr(mCurrentCalender);
                                start_time.setText(getShowDataStr(mCurrentCalender));
                            }
                        }, 0, 0, true).show();
                    }
                }, mCurrentCalender.get(Calendar.YEAR), mCurrentCalender.get(Calendar.MONTH)
                        , mCurrentCalender.get(Calendar.DAY_OF_MONTH))
                        .show();
            }
        });
        end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AlarmDetailActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, final int year, final int month, final int dayOfMonth) {
                        new TimePickerDialog(AlarmDetailActivity.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                mCurrentCalender.set(year, month, dayOfMonth, hourOfDay, minute);
                                endTime = getDataStr(mCurrentCalender);
                                end_time.setText(getShowDataStr(mCurrentCalender));
                            }
                        }, 0, 0, true).show();
                    }
                }, mCurrentCalender.get(Calendar.YEAR), mCurrentCalender.get(Calendar.MONTH)
                        , mCurrentCalender.get(Calendar.DAY_OF_MONTH))
                        .show();
            }
        });
        LinearLayout ll_add_user = (LinearLayout) findViewById(R.id.ll_add_user);
        LinearLayout ll_choose_pack = (LinearLayout) findViewById(R.id.ll_choose_pack);
        ll_choose_pack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (jobPackageBeen == null || jobPackageBeen.getList() == null || jobPackageBeen.getList().size() == 0) {
                    CustomerApp.getInstance().showToast("没有作业包,无法选择");
                    return;
                }
                List<String> jobListStr = new ArrayList<>();
                for (int i = 0; i < jobPackageBeen.getList().size(); i++) {
                    jobListStr.add(jobPackageBeen.getList().get(i).getJobName());
                }
                new MaterialDialog.Builder(AlarmDetailActivity.this)
                        .title("选择作业包")
                        .items(jobListStr)
                        .positiveColor(findColorById(R.color.colorPrimary))
                        .itemsCallbackSingleChoice(chooseWhich, new MaterialDialog.ListCallbackSingleChoice() {

                            @Override
                            public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                                if (jobPackageBeen == null || jobPackageBeen.getList() == null || jobPackageBeen.getList().size() == 0) {
                                    return false;
                                }
                                chooseWhich = which;
                                TextView tv_choose_job = (TextView) findViewById(R.id.tv_choose_job);
                                jobId = String.valueOf(jobPackageBeen.getList().get(which).getJobId());
                                tv_choose_job.setText(MessageFormat.format("作业包:{0}", text));
                                return true;
                            }
                        }).positiveText(R.string.mdtp_ok).show();
            }
        });
        if (ll_add_user.getChildCount() == 0) {
            ll_add_user.addView(getAddUserBtn());
        }
    }

    private void showCloseView() {
        actionType = 3;
        LinearLayout to_overhaul = (LinearLayout) findViewById(R.id.to_overhaul);
        LinearLayout ll_choose_user = (LinearLayout) findViewById(R.id.ll_choose_user);
        to_overhaul.setVisibility(View.GONE);
        ll_choose_user.setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD_USER && resultCode == Activity.RESULT_OK) {
            employeeBeen = data.getParcelableArrayListExtra(ConstantStr.KEY_BUNDLE_LIST);
            LinearLayout ll_add_user = (LinearLayout) findViewById(R.id.ll_add_user);
            ll_add_user.removeAllViews();
            if (employeeBeen != null && employeeBeen.size() > 0) {
                for (int i = 0; i < employeeBeen.size(); i++) {
                    ll_add_user.addView(addUser(employeeBeen.get(i).getUser()));
                }
                ll_add_user.addView(getAddUserBtn());
            } else {
                ll_add_user.addView(getAddUserBtn());
            }
        }
    }

    private ImageButton getAddUserBtn() {
        ImageButton imageButton = new ImageButton(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DisplayUtil.dip2px(this, 45)
                , DisplayUtil.dip2px(this, 45));
        imageButton.setBackground(findDrawById(R.drawable.bg_add_user));
        imageButton.setLayoutParams(params);
        imageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AlarmDetailActivity.this, EmployeeActivity.class);
                intent.putParcelableArrayListExtra(ConstantStr.KEY_BUNDLE_LIST, employeeBeen);
                startActivityForResult(intent, REQUEST_CODE_ADD_USER);
            }
        });
        return imageButton;
    }

    private View addUser(User user) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, DisplayUtil.dip2px(this, 10), 0);
        ShowUserLayout layout = new ShowUserLayout(this, user.getRealName(), user.getPortraitUrl(), findColorById(R.color.colorPrimary));
        layout.setLayoutParams(params);
        return layout;
    }

    private String getUserIds() {
        StringBuilder sb = new StringBuilder();
        if (employeeBeen != null && employeeBeen.size() >= 0) {
            for (int i = 0; i < employeeBeen.size(); i++) {
                sb.append(String.valueOf(employeeBeen.get(i).getUser().getUserId()));
                if (i != employeeBeen.size() - 1) {
                    sb.append(",");
                }
            }
        }
        return sb.toString();
    }

    private String getDataStr(Calendar calendar) {
        calendar.getTimeInMillis();
        return DataUtil.timeFormat(calendar.getTimeInMillis(), "yyyy-MM-dd HH:mm:00");
    }

    private String getShowDataStr(Calendar calendar) {
        calendar.getTimeInMillis();
        return DataUtil.timeFormat(calendar.getTimeInMillis(), "yyyy-MM-dd HH:mm");
    }

    @Override
    protected void onCancel() {
        mPresenter.unSubscribe();
    }

    @Override
    protected void onPause() {
        super.onPause();
        repairPlaySoundLayout.cancelPlay();
        faultPlaySoundLayout.cancelPlay();
    }

    private void addInfoToLayout(List<FaultDetail.FaultFlowsBean> faultFlows) {
        for (int i = 0; i < faultFlows.size(); i++) {
            String time = DataUtil.timeFormat(faultFlows.get(i).getFlowTime(), null);
            String content = faultFlows.get(i).getFlowRemark();
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            switch (faultFlows.get(i).getFaultState()) {
                case 1:
                    StringBuilder sb1 = new StringBuilder();
                    sb1.append(faultFlows.get(i).getUser().getRealName());
                    sb1.append("上报故障给");
                    for (int j = 0; j < faultFlows.get(i).getUsersN().size(); j++) {
                        sb1.append(faultFlows.get(i).getUsersN().get(j).getRealName());
                        if (j != faultFlows.get(i).getUsersN().size() - 1) {
                            sb1.append("  ");
                        }
                    }
                    ProgressItemLayout itemLayout1 = new ProgressItemLayout(this);
                    itemLayout1.setContent(time, sb1.toString(), content, i == faultFlows.size() - 1);
                    progressTitleLayout1.addItem(itemLayout1);
                    itemLayout1.setLayoutParams(params);
                    itemLayout1.hideAdvice();
                    break;
                case 2:
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(faultFlows.get(i).getUser().getRealName());
                    sb2.append("任务指派给");
                    for (int j = 0; j < faultFlows.get(i).getUsersN().size(); j++) {
                        sb2.append(faultFlows.get(i).getUsersN().get(j).getRealName());
                        if (j != faultFlows.get(i).getUsersN().size() - 1) {
                            sb2.append("  ");
                        }
                    }
                    ProgressItemLayout itemLayout2 = new ProgressItemLayout(this);
                    itemLayout2.setLayoutParams(params);
                    itemLayout2.setContent(time, sb2.toString(), content, i == faultFlows.size() - 1);
                    progressTitleLayout2.addItem(itemLayout2);
                    break;
                case 3:
                    String sb3 = faultFlows.get(i).getUser().getRealName() +
                            "关闭故障";
                    ProgressItemLayout itemLayout3 = new ProgressItemLayout(this);
                    itemLayout3.setLayoutParams(params);
                    itemLayout3.setContent(time, sb3, content, i == faultFlows.size() - 1);
                    progressTitleLayout3.addItem(itemLayout3);
                    break;
                case 4:
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append(faultFlows.get(i).getUser().getRealName());
                    sb4.append("任务指派给");
                    for (int j = 0; j < faultFlows.get(i).getUsersN().size(); j++) {
                        sb4.append(faultFlows.get(i).getUsersN().get(j).getRealName());
                        if (j != faultFlows.get(i).getUsersN().size() - 1) {
                            sb4.append("  ");
                        }
                    }
                    ProgressItemLayout itemLayout4 = new ProgressItemLayout(this);
                    itemLayout4.setLayoutParams(params);
                    itemLayout4.setContent(time, sb4.toString(), content, i == faultFlows.size() - 1);
                    progressTitleLayout3.addItem(itemLayout4);
                    break;
            }
        }
    }

}
