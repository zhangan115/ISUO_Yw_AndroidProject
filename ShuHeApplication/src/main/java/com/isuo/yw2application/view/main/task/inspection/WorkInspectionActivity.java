package com.isuo.yw2application.view.main.task.inspection;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.isuo.yw2application.R;
import com.isuo.yw2application.app.Yw2Application;
import com.isuo.yw2application.common.ConstantInt;
import com.isuo.yw2application.common.ConstantStr;
import com.isuo.yw2application.mode.bean.work.InspectionBean;
import com.isuo.yw2application.mode.bean.work.InspectionRegionModel;
import com.isuo.yw2application.utils.ACache;
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
import com.sito.library.utils.SPHelper;
import com.sito.library.widget.PinnedHeaderExpandableListView;
import com.wdullaer.materialdatetimepicker.date.DatePickerView;

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

public class WorkInspectionActivity extends BaseActivity implements DatePickerView.OnDateSetListener, InspectionContract.View
        , SwipeRefreshLayout.OnRefreshListener {

    private LinearLayout mChooseDayLayout;
    private DatePickerView mDatePickerView;
    private TextView mYearTv;
    private TextView conditionsTv;
    private TextView[] dayTvs = new TextView[7];
    private TextView[] dayWeekNumTvs = new TextView[7];
    private LinearLayout[] dayOfWeekLayout = new LinearLayout[7];
    private RecyclerView mRecyclerView;
    private RelativeLayout noDataLayout;
    private PinnedHeaderExpandableListView expandableListView;

    private InspectionContract.Presenter mPresenter;
    private List<Date> dateList = new ArrayList<>();
    private Calendar mCurrentDay;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String mDate;
    private int inspectionType;//巡检类型
    private List<InspectionBean> mList;
    private List<InspectionRegionModel> dataList = new ArrayList<>();

    private WorkInspectionAdapter inspectionAdapter;

    @Nullable
    private NfcAdapter nfcAdapter;
    private PendingIntent mPendingIntent;
    private NdefMessage ndefMessage;
    private Switch mTimeSt;
    private int currentState = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String title = getIntent().getStringExtra(ConstantStr.KEY_BUNDLE_STR);
        if (TextUtils.isEmpty(title)) {
            title = "执行巡检";
        }
        inspectionType = getIntent().getIntExtra(ConstantStr.KEY_BUNDLE_INT, -1);
        setLayoutAndToolbar(R.layout.activivity_inspection_work_list, title);
        new InspectionPresenter(Yw2Application.getInstance().getWorkRepositoryComponent().getRepository(), this);
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
        initNfcAdapter();
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
        inspectionAdapter = new WorkInspectionAdapter(this, expandableListView, R.layout.item_equip_group, R.layout.item_day_inspection);
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
        ACache.get(Yw2Application.getInstance()).clear();
        dataList.clear();
        inspectionAdapter.notifyDataSetChanged();
        onRefresh();
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
                bean.setRegionName("未分区域");
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
    public void showMoreData(List<InspectionBean> been) {

    }

    @Override
    public void showLoading() {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void noData() {
        mList.clear();
        this.dataList.clear();
        inspectionAdapter.setData(this.dataList);
        if (mRecyclerView.getAdapter() == null) {
            return;
        }
        mRecyclerView.getAdapter().notifyDataSetChanged();
        noDataLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void operationSuccess(InspectionBean inspectionBean) {
        inspectionBean.setTaskState(ConstantInt.TASK_STATE_2);
        inspectionBean.setReceiveUser(Yw2Application.getInstance().getCurrentUser());
        if (inspectionAdapter != null) {
            inspectionAdapter.notifyDataSetChanged();
        }
        int securityId = -1;
        if (inspectionBean.getSecurityPackage() != null) {
            securityId = inspectionBean.getSecurityPackage().getSecurityId();
        }
        startTask(inspectionBean.getTaskId(),securityId);
    }

    @Override
    public void setPresenter(InspectionContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onRefresh() {
        if (mPresenter != null) {
            noDataLayout.setVisibility(View.GONE);
            mPresenter.getData(inspectionType, mDate);
        }
    }

    private boolean isClick;


    @Override
    public void onResume() {
        super.onResume();
        if (isClick) {
            onRefresh();
        }
        isClick = false;
        if (nfcAdapter != null) {
            //隐式启动
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

    //获取系统隐式启动的
    @Override
    public void onNewIntent(Intent intent) {
        setIntent(intent);
        processAdapterAction(intent);
    }

    public void processAdapterAction(Intent intent) {
        // 当系统检测到tag中含有NDEF格式的数据时，且系统中有activity声明可以接受包含NDEF数据的Intent的时候，系统会优先发出这个action的intent。
        // 得到是否检测到ACTION_NDEF_DISCOVERED触发 序号1
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            // 处理该intent
            processIntent(intent);
            return;
        }
        // 当没有任何一个activity声明自己可以响应ACTION_NDEF_DISCOVERED时，系统会尝试发出TECH的intent.即便你的tag中所包含的数据是NDEF的，但是如果这个数据的MIME
        // type或URI不能和任何一个activity所声明的想吻合，系统也一样会尝试发出tech格式的intent，而不是NDEF.
        // 得到是否检测到ACTION_TECH_DISCOVERED触发 序号2
        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction())) {
            // 处理该intent
            processIntent(intent);
            return;
        }
        // 当系统发现前两个intent在系统中无人会接受的时候，就只好发这个默认的TAG类型的
        // 得到是否检测到ACTION_TAG_DISCOVERED触发 序号3
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            // 处理该intent
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
            mPresenter.operationTask(String.valueOf(inspectionBeans.get(index).getTaskId()), inspectionBeans.get(0));
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
}
