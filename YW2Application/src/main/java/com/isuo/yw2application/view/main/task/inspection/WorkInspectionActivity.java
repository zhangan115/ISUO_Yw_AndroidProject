package com.isuo.yw2application.view.main.task.inspection;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.iflytek.cloud.thirdparty.V;
import com.isuo.yw2application.R;
import com.isuo.yw2application.app.Yw2Application;
import com.isuo.yw2application.common.ConstantInt;
import com.isuo.yw2application.common.ConstantStr;
import com.isuo.yw2application.mode.bean.User;
import com.isuo.yw2application.mode.bean.db.TaskDb;
import com.isuo.yw2application.mode.bean.inspection.InspectionBean;
import com.isuo.yw2application.mode.bean.inspection.InspectionRegionModel;
import com.isuo.yw2application.mode.inspection.InspectionRepository;
import com.isuo.yw2application.utils.Utils;
import com.isuo.yw2application.view.base.BaseActivity;
import com.isuo.yw2application.view.main.task.inspection.detial.InspectDetailActivity;
import com.isuo.yw2application.view.main.task.inspection.security.SecurityPackageActivity;
import com.isuo.yw2application.view.main.task.inspection.work.InspectionRoomActivity;
import com.king.zxing.CaptureActivity;
import com.qw.soul.permission.SoulPermission;
import com.qw.soul.permission.bean.Permission;
import com.qw.soul.permission.callbcak.CheckRequestPermissionListener;
import com.sito.library.utils.CalendarUtil;
import com.sito.library.utils.DataUtil;
import com.sito.library.utils.DisplayUtil;
import com.sito.library.utils.SPHelper;
import com.sito.library.widget.PinnedHeaderExpandableListView;
import com.wdullaer.materialdatetimepicker.date.DatePickerView;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import pub.devrel.easypermissions.AppSettingsDialog;

/**
 * 巡检列表
 * Created by zhangan on 2018/3/27.
 */

public class WorkInspectionActivity extends BaseActivity implements DatePickerView.OnDateSetListener, WorkInspectionContract.View
        , SwipeRefreshLayout.OnRefreshListener {

    private LinearLayout mChooseDayLayout;
    private DatePickerView mDatePickerView;
    private TextView mYearTv;
    private TextView conditionsTv;
    private final TextView[] dayTvs = new TextView[7];
    private final TextView[] dayWeekNumTvs = new TextView[7];
    private final LinearLayout[] dayOfWeekLayout = new LinearLayout[7];
    private RecyclerView mRecyclerView;
    private RelativeLayout noDataLayout;
    private PinnedHeaderExpandableListView expandableListView;

    private WorkInspectionContract.Presenter mPresenter;
    private List<Date> dateList = new ArrayList<>();
    private Calendar mCurrentDay;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String mDate;
    private int inspectionType;//巡检类型
    private List<InspectionBean> mList;
    private final List<InspectionRegionModel> dataList = new ArrayList<>();

    private WorkInspectionAdapter inspectionAdapter;

    @Nullable
    private NfcAdapter nfcAdapter;
    private PendingIntent mPendingIntent;
    private NdefMessage ndefMessage;
    private Switch mTimeSt;
    private int currentState = 0;
    private boolean isMonitor = false;

    private LocalBroadcastManager manager;
    private TaskStateChangeReceiver taskStateChangeReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String title = getIntent().getStringExtra(ConstantStr.KEY_BUNDLE_STR);
        if (TextUtils.isEmpty(title)) {
            title = "执行巡检";
        }
        inspectionType = getIntent().getIntExtra(ConstantStr.KEY_BUNDLE_INT, -1);
        isMonitor = getIntent().getBooleanExtra(ConstantStr.KEY_BUNDLE_BOOLEAN,false);
        setLayoutAndToolbar(R.layout.activivity_inspection_work_list, title);
        new WorkInspectionPresenter(InspectionRepository.getRepository(this), this);
        mRecyclerView = findViewById(R.id.recycleViewId);
        swipeRefreshLayout = findViewById(R.id.swipeLayout);
        swipeRefreshLayout.setColorSchemeColors(findColorById(R.color.colorPrimary));
        swipeRefreshLayout.setOnRefreshListener(this);
        noDataLayout = findViewById(R.id.layout_no_data);
        mYearTv = findViewById(R.id.tv_year);
        mChooseDayLayout = findViewById(R.id.ll_choose_day);
        LinearLayout addDatePickLayout = findViewById(R.id.ll_choose_day_content);
        mDatePickerView = DatePickerView.newInstance(this, this);
        addDatePickLayout.addView(mDatePickerView);
        mCurrentDay = Calendar.getInstance(Locale.CHINA);
        dateList = CalendarUtil.getDaysOfWeek(mCurrentDay.getTime());
        expandableListView = findViewById(R.id.expandableListView);
        expandableListView.setOnHeaderUpdateListener(new PinnedHeaderExpandableListView.OnHeaderUpdateListener() {

            @Override
            public View getPinnedHeader() {
                View headerView = LayoutInflater.from(WorkInspectionActivity.this).inflate(R.layout.item_equip_group_copy, expandableListView, false);
                headerView.setBackgroundColor(findColorById(R.color.equip_search_bg_gray));
                return headerView;
            }

            @Override
            public void updatePinnedHeader(View headerView, int firstVisibleGroupPos) {
                TextView mPlace = (TextView) headerView.findViewById(R.id.id_item_equip_place);
                TextView unitTv = (TextView) headerView.findViewById(R.id.unitTv);
                ImageView stateIv = (ImageView) headerView.findViewById(R.id.iv_state);
                if (firstVisibleGroupPos != -1) {
                    mPlace.setText(dataList.get(firstVisibleGroupPos).getRegionName());
                    int notFinishCount = 0;
                    int taskCount = 0;
                    if (dataList.get(firstVisibleGroupPos).getInspectionBeanList() != null) {
                        for (int i = 0; i < dataList.get(firstVisibleGroupPos).getInspectionBeanList().size(); i++) {
                            if (dataList.get(firstVisibleGroupPos).getInspectionBeanList().get(i).getTaskState() < ConstantInt.TASK_STATE_4) {
                                notFinishCount++;
                            }
                        }
                        taskCount = dataList.get(firstVisibleGroupPos).getInspectionBeanList().size();
                    }
                    unitTv.setText(MessageFormat.format("总数:{0} 未完成:{1}", taskCount, notFinishCount));
                    boolean isExpanded = expandableListView.isGroupExpanded(firstVisibleGroupPos);
                    stateIv.setVisibility(View.VISIBLE);
                    if (isExpanded) {
                        stateIv.setImageDrawable(findDrawById(R.drawable.bg_employee_arrow_open));
                    } else {
                        stateIv.setImageDrawable(findDrawById(R.drawable.bg_employee_arrow));
                    }
                }else {
                    //stateIv.setVisibility(View.GONE);
                }
            }
        });
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return false;
            }
        }, true);
        expandableListView.setOnHeaderViewClickListener(new PinnedHeaderExpandableListView.OnHeaderViewClickListener() {
            @Override
            public void onViewClick(int groupPosition) {
                if (expandableListView.isGroupExpanded(groupPosition)) {
                    expandableListView.collapseGroup(groupPosition);
                } else {
                    expandableListView.expandGroup(groupPosition, true);
                }
            }
        });
        dayTvs[0] = findViewById(R.id.tv_1);
        dayTvs[1] = findViewById(R.id.tv_2);
        dayTvs[2] = findViewById(R.id.tv_3);
        dayTvs[3] = findViewById(R.id.tv_4);
        dayTvs[4] = findViewById(R.id.tv_5);
        dayTvs[5] = findViewById(R.id.tv_6);
        dayTvs[6] = findViewById(R.id.tv_7);

        dayWeekNumTvs[0] = findViewById(R.id.tv_1_1);
        dayWeekNumTvs[1] = findViewById(R.id.tv_2_1);
        dayWeekNumTvs[2] = findViewById(R.id.tv_3_1);
        dayWeekNumTvs[3] = findViewById(R.id.tv_4_1);
        dayWeekNumTvs[4] = findViewById(R.id.tv_5_1);
        dayWeekNumTvs[5] = findViewById(R.id.tv_6_1);
        dayWeekNumTvs[6] = findViewById(R.id.tv_7_1);

        dayOfWeekLayout[0] = findViewById(R.id.ll_monday);
        dayOfWeekLayout[1] = findViewById(R.id.ll_tuesday);
        dayOfWeekLayout[2] = findViewById(R.id.ll_wednesday);
        dayOfWeekLayout[3] = findViewById(R.id.ll_thursday);
        dayOfWeekLayout[4] = findViewById(R.id.ll_friday);
        dayOfWeekLayout[5] = findViewById(R.id.ll_saturday);
        dayOfWeekLayout[6] = findViewById(R.id.ll_sunday);
        conditionsTv = findViewById(R.id.conditionsTv);

        for (LinearLayout layout : dayOfWeekLayout) {
            layout.setOnClickListener(this);
        }
        findViewById(R.id.screenConditionsLayout).setOnClickListener(this);
        findViewById(R.id.ll_choose_month_day).setOnClickListener(this);
        findViewById(R.id.ll_choose_day_empty).setOnClickListener(this);
        if (isMonitor) {
            findViewById(R.id.uploadBtn).setVisibility(View.GONE);
        }else {
            findViewById(R.id.uploadBtn).setVisibility(View.VISIBLE);
            findViewById(R.id.uploadBtn).setOnClickListener(this);
        }
        mTimeSt = findViewById(R.id.switchSt);
        mTimeSt.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                timeData(currentState);
            } else {
                defaultData(currentState);
            }
        });
        getDate(mCurrentDay.get(Calendar.YEAR), mCurrentDay.get(Calendar.MONTH), mCurrentDay.get(Calendar.DAY_OF_MONTH));
        mList = new ArrayList<>();
        initRecycleView();
        setDayToView();
        if (!isMonitor) {
            initNfcAdapter();
        }
        manager = LocalBroadcastManager.getInstance(this);
        taskStateChangeReceiver = new TaskStateChangeReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConstantStr.TASK_STATE_START);
        intentFilter.addAction(ConstantStr.TASK_STATE_FINISH);
        manager.registerReceiver(taskStateChangeReceiver, intentFilter);
    }

    private void initNfcAdapter() {
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        //拦截系统级的NFC扫描，例如扫描蓝牙
        mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
                getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        ndefMessage = new NdefMessage(new NdefRecord[]{Utils.newTextRecord("",
                Locale.ENGLISH, true)});
        //只允许使用NFC开启任务
        if (nfcAdapter == null) {
            Yw2Application.getInstance().showToast("当前设备不支持NFC");
        } else if (!nfcAdapter.isEnabled()) {
            new MaterialDialog.Builder(this)
                    .content("请打开NFC功能")
                    .negativeText("取消")
                    .positiveText("确定")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                        }
                    })
                    .show();
        }
    }

    private void initRecycleView() {
        if (isMonitor) {
            inspectionAdapter = new WorkInspectionAdapter(this, expandableListView, R.layout.item_equip_group, R.layout.item_day_inspection, true);
        }else {
            inspectionAdapter = new WorkInspectionAdapter(this, expandableListView, R.layout.item_equip_group, R.layout.item_day_inspection);
        }
        expandableListView.setAdapter(inspectionAdapter);
        inspectionAdapter.setItemListener(new WorkInspectionAdapter.ItemClickListener() {
            @Override
            public void onItemClick(InspectionBean inspectionBean) {
                Intent intent = new Intent(WorkInspectionActivity.this, InspectDetailActivity.class);
                intent.putExtra(ConstantStr.KEY_BUNDLE_LONG, inspectionBean.getTaskId());
                intent.putExtra(ConstantStr.KEY_BUNDLE_STR, inspectionBean.getTaskName());
                startActivity(intent);
            }

            @Override
            public void operationTask(String id, int groupPosition, int childPosition) {
                if (isClick) {
                    return;
                }
                mPresenter.operationTask(id, dataList.get(groupPosition).getInspectionBeanList().get(childPosition));
            }

            @Override
            public void toStartActivity(long taskId, long securityId) {
                startTask(taskId, securityId);
            }
        });
    }

    private void startTask(long taskId, long securityId) {
        if (isClick) {
            return;
        }
        Intent intent = new Intent();
        if (!SPHelper.readBoolean(WorkInspectionActivity.this
                , ConstantStr.SECURITY_INFO, String.valueOf(taskId), false) && securityId != -1) {
            intent.setClass(WorkInspectionActivity.this, SecurityPackageActivity.class);
        } else {
            intent.setClass(WorkInspectionActivity.this, InspectionRoomActivity.class);
        }
        intent.putExtra(ConstantStr.KEY_BUNDLE_LONG, taskId);
        intent.putExtra(ConstantStr.KEY_BUNDLE_LONG_1, securityId);
        isClick = true;
        startActivity(intent);
    }

    @Override
    public void onDateSet(DatePickerView datePickerView, int year, int monthOfYear, int dayOfMonth) {
        mCurrentDay.set(year, monthOfYear, dayOfMonth);
        dateList = CalendarUtil.getDaysOfWeek(mCurrentDay.getTime());
        setDayToView();
        getDate(year, monthOfYear, dayOfMonth);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_calendar_close);
        mDatePickerView.startAnimation(animation);
        animation.setAnimationListener(animationListener);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_choose_month_day:
                if (mChooseDayLayout.getVisibility() == View.GONE) {
                    mChooseDayLayout.setVisibility(View.VISIBLE);
                    Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_calendar_open);
                    mDatePickerView.startAnimation(animation);
                } else {
                    Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_calendar_close);
                    mDatePickerView.startAnimation(animation);
                    animation.setAnimationListener(animationListener);
                }
                break;
            case R.id.ll_monday:
                mCurrentDay.setTime(dateList.get(0));
                setDayToView();
                break;
            case R.id.ll_tuesday:
                mCurrentDay.setTime(dateList.get(1));
                setDayToView();
                break;
            case R.id.ll_wednesday:
                mCurrentDay.setTime(dateList.get(2));
                setDayToView();
                break;
            case R.id.ll_thursday:
                mCurrentDay.setTime(dateList.get(3));
                setDayToView();
                break;
            case R.id.ll_friday:
                mCurrentDay.setTime(dateList.get(4));
                setDayToView();
                break;
            case R.id.ll_saturday:
                mCurrentDay.setTime(dateList.get(5));
                setDayToView();
                break;
            case R.id.ll_sunday:
                mCurrentDay.setTime(dateList.get(6));
                setDayToView();
                break;
            case R.id.ll_choose_day_empty:
                Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_calendar_close);
                mDatePickerView.startAnimation(animation);
                animation.setAnimationListener(animationListener);
                break;
            case R.id.screenConditionsLayout:
                ArrayList<String> mTypeItem = new ArrayList<>();
                mTypeItem.add("默认");
                mTypeItem.add("未领取");
                mTypeItem.add("已领取");
                mTypeItem.add("进行中");
                mTypeItem.add("已完成");
                new MaterialDialog.Builder(this)
                        .items(mTypeItem)
                        .itemsCallback((dialog, itemView, position, text) -> {
                            currentState = position;
                            if (mTimeSt.isChecked()) {
                                timeData(currentState);
                            } else {
                                defaultData(currentState);
                            }
                            conditionsTv.setText(text);
                        })
                        .show();
                break;
            case R.id.uploadBtn:
                if (mPresenter != null) {
                    uploadTask = mPresenter.getUploadTask(this.mList);
                    if (uploadTask != null && !uploadTask.isEmpty()) {
                        currentUploadIndex = 0;
                        showProgressDialog("数据上传中...");
                        mPresenter.uploadTaskData(uploadTask.get(currentUploadIndex), null);
                    } else {
                        Yw2Application.getInstance().showToast("暂无数据需要上传");
                    }
                }
                break;
        }
    }

    private int currentUploadIndex = 0;
    private List<InspectionBean> uploadTask;


    @Override
    public void uploadNext() {
        currentUploadIndex++;
        if (currentUploadIndex >= uploadTask.size()) {
            mPresenter.toSaveInspectionDataToAcCache(WorkInspectionActivity.this.inspectionType, WorkInspectionActivity.this.mDate, mList);
            if (inspectionAdapter != null) {
                inspectionAdapter.notifyDataSetChanged();
            }
            hideProgressDialog();
        } else {
            mPresenter.uploadTaskData(uploadTask.get(currentUploadIndex), null);
        }
    }

    private void setDayToView() {
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        for (int i = 0; i < dayTvs.length; i++) {
            calendar.setTime(dateList.get(i));
            if (calendar.get(Calendar.DAY_OF_WEEK) == mCurrentDay.get(Calendar.DAY_OF_WEEK)) {
                dayTvs[i].setTextColor(findColorById(R.color.colorWhite));
                dayWeekNumTvs[i].setTextColor(findColorById(R.color.colorWhite));
                dayOfWeekLayout[i].setBackgroundResource(R.drawable.day_green_gb);
            } else {
                dayTvs[i].setTextColor(findColorById(R.color.text333));
                dayWeekNumTvs[i].setTextColor(findColorById(R.color.gray_999999));
                dayOfWeekLayout[i].setBackgroundColor(findColorById(R.color.colorWhite));
            }
            dayTvs[i].setText(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
        }
        getDate(mCurrentDay.get(Calendar.YEAR), mCurrentDay.get(Calendar.MONTH), mCurrentDay.get(Calendar.DAY_OF_MONTH));
        dataList.clear();
        inspectionAdapter.notifyDataSetChanged();
        if (isMonitor) {
            onRefresh();
        }else {
            getDataFromCache();
        }
    }

    private void getDate(int year, int monthOfYear, int dayOfMonth) {
        Calendar newCalendar = Calendar.getInstance();
        newCalendar.set(year, monthOfYear, dayOfMonth);
        mDate = DataUtil.timeFormat(newCalendar.getTime().getTime(), "yyyy-MM-dd");
        mYearTv.setText(DataUtil.timeFormat(newCalendar.getTime().getTime(), "yyyy年MM月dd日"));
    }

    private final Animation.AnimationListener animationListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            mChooseDayLayout.setVisibility(View.GONE);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };

    @Override
    public void showData(List<InspectionBean> been) {
        noDataLayout.setVisibility(View.GONE);
        mList.clear();
        mList.addAll(been);
        if (mTimeSt.isChecked()) {
            timeData(currentState);
        } else {
            defaultData(currentState);
        }
        if (mRecyclerView.getAdapter() != null) {
            mRecyclerView.getAdapter().notifyDataSetChanged();
        }
    }

    private void defaultData(int state) {
        dataList.clear();
        for (InspectionBean bean : mList) {
            boolean hasBean = false;
            InspectionRegionModel regionModel = null;
            if (TextUtils.isEmpty(bean.getRegionName())) {
                bean.setRegionName("未分类");
            }
            for (InspectionRegionModel model : dataList) {
                if (model.getRegionName().equals(bean.getRegionName())) {
                    hasBean = true;
                    regionModel = model;
                    break;
                }
            }
            if (hasBean) {
                if (state == 0) {
                    regionModel.addInspection(bean);
                } else if (state == bean.getTaskState()) {
                    regionModel.addInspection(bean);
                }
            } else {
                InspectionRegionModel model = new InspectionRegionModel();
                model.setRegionName(bean.getRegionName());
                if (state == 0) {
                    model.addInspection(bean);
                } else if (state == bean.getTaskState()) {
                    model.addInspection(bean);
                }
                dataList.add(model);
            }
        }
        inspectionAdapter.setData(this.dataList);
    }

    private void timeData(int state) {
        //早班、白班、前夜班
        dataList.clear();
        InspectionRegionModel model1 = new InspectionRegionModel();
        model1.setRegionName("早班");
        model1.setInspectionBeanList(new ArrayList<>());
        InspectionRegionModel model2 = new InspectionRegionModel();
        model2.setRegionName("白班");
        model2.setInspectionBeanList(new ArrayList<>());
        InspectionRegionModel model3 = new InspectionRegionModel();
        model3.setRegionName("前夜班");
        model3.setInspectionBeanList(new ArrayList<>());
        InspectionRegionModel model4 = new InspectionRegionModel();
        model4.setRegionName("未分区域");
        model4.setInspectionBeanList(new ArrayList<>());

        for (InspectionBean bean : mList) {
            if (bean.getTaskName().contains(model1.getRegionName())) {
                if (0 == state) {
                    model1.getInspectionBeanList().add(bean);
                } else if (bean.getTaskState() == state) {
                    model1.getInspectionBeanList().add(bean);
                }
            } else if (bean.getTaskName().contains(model2.getRegionName())) {
                if (0 == state) {
                    model2.getInspectionBeanList().add(bean);
                } else if (bean.getTaskState() == state) {
                    model2.getInspectionBeanList().add(bean);
                }
            } else if (bean.getTaskName().contains(model3.getRegionName())) {
                if (0 == state) {
                    model3.getInspectionBeanList().add(bean);
                } else if (bean.getTaskState() == state) {
                    model3.getInspectionBeanList().add(bean);
                }
            } else if (TextUtils.isEmpty(bean.getRegionName())) {
                if (0 == state) {
                    model4.getInspectionBeanList().add(bean);
                } else if (bean.getTaskState() == state) {
                    model4.getInspectionBeanList().add(bean);
                }
            }
        }
        if (!model1.getInspectionBeanList().isEmpty()) {
            this.dataList.add(model1);
        }
        if (!model2.getInspectionBeanList().isEmpty()) {
            this.dataList.add(model2);
        }
        if (!model3.getInspectionBeanList().isEmpty()) {
            this.dataList.add(model3);
        }
        if (!model4.getInspectionBeanList().isEmpty()) {
            this.dataList.add(model4);
        }
        inspectionAdapter.setData(this.dataList);
    }

    @Override
    public void showLoading() {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void showToast(String message) {
        Yw2Application.getInstance().showToast(message);
    }

    @Override
    public void hideLoading() {
        swipeRefreshLayout.setRefreshing(false);
        hideProgressDialog();
        hidePopupLoading();
    }

    @Override
    public void noData() {
        mList.clear();
        this.dataList.clear();
        inspectionAdapter.setData(this.dataList);
        if (mRecyclerView.getAdapter() != null) {
            mRecyclerView.getAdapter().notifyDataSetChanged();
        }
        noDataLayout.setVisibility(View.VISIBLE);
    }

    /**
     * 领取成功后保存数据
     *
     * @param inspectionBean 巡检数据
     */
    @Override
    public void operationSuccess(InspectionBean inspectionBean) {
        inspectionBean.setTaskState(ConstantInt.TASK_STATE_2);
        inspectionBean.setReceiveUser(Yw2Application.getInstance().getCurrentUser());
        if (inspectionAdapter != null) {
            inspectionAdapter.notifyDataSetChanged();
        }
        if (mPresenter != null) {
            mPresenter.toSaveInspectionDataToAcCache(this.inspectionType, this.mDate, mList);
        }
        Yw2Application.getInstance().showToast("任务领取成功");
    }

    @Override
    public void setPresenter(WorkInspectionContract.Presenter presenter) {
        mPresenter = presenter;
    }

    private void getDataFromCache() {
        if (mPresenter != null) {
            noDataLayout.setVisibility(View.GONE);
            mPresenter.getDataFromAcCache(inspectionType, mDate);
        }
    }

    @Override
    public void onRefresh() {
        if (mPresenter != null) {
            noDataLayout.setVisibility(View.GONE);
            mPresenter.getData(inspectionType, mDate,isMonitor);
        }
    }

    private boolean isClick;

    @Override
    public void onResume() {
        super.onResume();
        isClick = false;
        if (nfcAdapter != null) {
            nfcAdapter.enableForegroundDispatch(this, mPendingIntent, null, null);
            nfcAdapter.enableForegroundNdefPush(this, ndefMessage);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (nfcAdapter != null) {
            nfcAdapter.disableForegroundDispatch(this);
            nfcAdapter.disableForegroundNdefPush(this);
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        setIntent(intent);
        processAdapterAction(intent);
    }

    public void processAdapterAction(Intent intent) {
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            processIntent(intent);
            return;
        }
        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction())) {
            processIntent(intent);
            return;
        }
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            processIntent(intent);
        }
    }

    @Nullable
    private String read(Tag tag) throws Exception {
        if (tag != null) {
            Ndef ndef = Ndef.get(tag);
            if (ndef == null) {
                return null;
            }
            //打开连接
            ndef.connect();
            NdefMessage message = ndef.getNdefMessage();
            if (message != null) {
                //将消息转换成字节数组
                byte[] data = message.toByteArray();
                //将字节数组转换成字符串
                String str = new String(data, "UTF-8");
                //关闭连接
                ndef.close();
                if (str.length() > 7) {
                    str = str.substring(7);
                }
                return str;
            }
        } else {
            Yw2Application.getInstance().showToast("设备与nfc卡连接断开，请重新连接...");
        }
        return null;
    }

    private void processIntent(Intent intent) {
        if (intent == null) {
            return;
        }
        Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        String roomId = "";
        try {
            roomId = read(tagFromIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        findRoom(roomId);
    }

    private final int SCANNER_CODE = 202;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == SCANNER_CODE) {
            if (data != null) {
                String result = data.getStringExtra(CaptureActivity.KEY_RESULT);
                if (TextUtils.isEmpty(result)) {
                    Yw2Application.getInstance().showToast("未找到数据,请从新扫码");
                    return;
                }
                findRoom(result);
            } else {
                Yw2Application.getInstance().showToast("扫码失败");
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_scan, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_id_scan) {
            scan();
        }
        return true;
    }

    private void scan() {
        SoulPermission.getInstance().checkAndRequestPermission(Manifest.permission.CAMERA,
                new CheckRequestPermissionListener() {
                    @Override
                    public void onPermissionOk(Permission permission) {
                        startActivityForResult(new Intent(WorkInspectionActivity.this, CaptureActivity.class), SCANNER_CODE);
                    }

                    @Override
                    public void onPermissionDenied(Permission permission) {
                        new AppSettingsDialog.Builder(WorkInspectionActivity.this)
                                .setRationale(getString(R.string.need_camera_setting))
                                .setTitle(getString(R.string.request_permissions))
                                .setPositiveButton(getString(R.string.sure))
                                .setNegativeButton(getString(R.string.cancel))
                                .build()
                                .show();

                    }
                });
    }

    private void findRoom(String roomId) {
        try {
            List<InspectionBean> inspectionBeans = new ArrayList<>();
            long roomIdLong = Long.parseLong(roomId);
            for (int i = 0; i < mList.size(); i++) {
                if (mList.get(i).getRooms() == null) {
                    continue;
                }
                for (int j = 0; j < mList.get(i).getRooms().size(); j++) {
                    long roomIds = mList.get(i).getRoomIds()[j];
                    if (roomIdLong == roomIds) {
                        inspectionBeans.add(mList.get(i));
                    }
                }
            }
            if (inspectionBeans.isEmpty()) {
                Yw2Application.getInstance().showToast("没有找到相关的任务");
            } else {
                if (inspectionBeans.size() == 1) {
                    scanResult(inspectionBeans, 0);
                } else {
                    List<String> taskNames = new ArrayList<>();
                    for (int i = 0; i < inspectionBeans.size(); i++) {
                        taskNames.add(inspectionBeans.get(i).getTaskName());
                    }
                    new MaterialDialog.Builder(this)
                            .items(taskNames)
                            .itemsCallback((dialog, itemView, position, text) -> {
                                scanResult(inspectionBeans, position);
                            })
                            .show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void scanResult(List<InspectionBean> inspectionBeans, int index) {
        if (inspectionBeans.get(index).getTaskState() < ConstantInt.TASK_STATE_2) {
            //去领取
            mPresenter.operationTask(String.valueOf(inspectionBeans.get(index).getTaskId()), inspectionBeans.get(index));
        } else {
            //打开
            long taskId = inspectionBeans.get(index).getTaskId();
            int securityId = -1;
            if (inspectionBeans.get(index).getSecurityPackage() != null) {
                securityId = inspectionBeans.get(index).getSecurityPackage().getSecurityId();
            }
            startTask(taskId, securityId);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (manager != null && taskStateChangeReceiver != null) {
            manager.unregisterReceiver(taskStateChangeReceiver);
        }
    }

    private class TaskStateChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && intent.getAction() != null) {
                InspectionBean inspectionBean = null;
                long taskId = intent.getLongExtra(ConstantStr.KEY_BUNDLE_LONG, -1);
                for (int i = 0; i < mList.size(); i++) {
                    if (mList.get(i).getTaskId() == taskId) {
                        inspectionBean = mList.get(i);
                        break;
                    }
                }
                if (intent.getAction().equals(ConstantStr.TASK_STATE_START)) {
                    if (inspectionBean != null) {
                        inspectionBean.setTaskState(ConstantInt.TASK_STATE_3);
                        inspectionBean.setStartTime(System.currentTimeMillis());
                    }
                } else if (intent.getAction().equals(ConstantStr.TASK_STATE_FINISH)) {
                    if (inspectionBean != null) {
                        ArrayList<User> users = new ArrayList<>();
                        ArrayList<TaskDb> taskDbs = intent.getParcelableArrayListExtra(ConstantStr.KEY_BUNDLE_LIST);
                        ArrayList<User> userList = intent.getParcelableArrayListExtra(ConstantStr.KEY_BUNDLE_LIST_1);
                        if (taskDbs != null) {
                            for (int i = 0; i < taskDbs.size(); i++) {
                                TaskDb taskDb = taskDbs.get(i);
                                User user = new User();
                                user.setUserId(((int) taskDb.getUserId()));
                                user.setRealName(taskDb.getUserName());
                                users.add(user);
                            }
                        } else if (userList != null) {
                            users.addAll(userList);
                        }
                        inspectionBean.setTaskState(ConstantInt.TASK_STATE_4);
                        inspectionBean.setUsers(users);
                        inspectionBean.setUploadCount(inspectionBean.getCount());
                        inspectionBean.setEndTime(System.currentTimeMillis());
                    }
                }
                mPresenter.toSaveInspectionDataToAcCache(WorkInspectionActivity.this.inspectionType, WorkInspectionActivity.this.mDate, mList);
                if (inspectionAdapter != null) {
                    inspectionAdapter.notifyDataSetChanged();
                }
            }
        }
    }
}
