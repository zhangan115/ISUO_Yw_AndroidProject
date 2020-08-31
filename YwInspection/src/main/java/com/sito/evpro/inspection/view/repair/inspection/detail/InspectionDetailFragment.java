package com.sito.evpro.inspection.view.repair.inspection.detail;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sito.evpro.inspection.R;
import com.sito.evpro.inspection.app.InspectionApp;
import com.sito.evpro.inspection.common.ConstantInt;
import com.sito.evpro.inspection.common.ConstantStr;
import com.sito.evpro.inspection.mode.bean.db.RoomDb;
import com.sito.evpro.inspection.mode.bean.db.TaskDb;
import com.sito.evpro.inspection.mode.bean.employee.EmployeeBean;
import com.sito.evpro.inspection.mode.bean.inspection.InspectionDetailBean;
import com.sito.evpro.inspection.mode.bean.inspection.RoomListBean;
import com.sito.evpro.inspection.view.MvpFragment;
import com.sito.evpro.inspection.view.employee.EmployeeActivity;
import com.sito.evpro.inspection.view.repair.inspection.report.ReportActivity;
import com.sito.evpro.inspection.widget.RoomListLayout;
import com.sito.library.utils.DisplayUtil;
import com.sito.library.utils.SPHelper;
import com.sito.library.utils.SystemUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * 今日巡检详情列表
 * Created by zhangan on 2017-06-26.
 */

public class InspectionDetailFragment extends MvpFragment<InspectionDetailContract.Presenter> implements InspectionDetailContract.View, View.OnClickListener, IFindRoomListener {

    private RelativeLayout noDataLayout;
    private LinearLayout mRoomsLayout;
    private LinearLayout addEmployeeLayout;
    private List<RoomListBean> mList;
    private RoomListBean roomListBean;
    private String taskId;
    private List<TaskDb> chooseEmployeeBeen;//已经添加的人员
    private int mPosition;
    private int mOperation = 1;
    private String RECE_DATA_ACTION = "com.se4500.onDecodeComplete";    //解码广播
    private String START_SCAN_ACTION = "com.geomobile.se4500barcode";    //调用扫描广播
    private String STOP_SCAN = "com.geomobile.se4500barcodestop";    //停止扫描广播
    private boolean isFlag = false;
    private Intent reportIntent, addEmployeeIntent;
    private int REQUEST_CODE = 200;
    private int REQUEST_CODE_START = 201;
    private boolean isAutoSendAction = true;
    private TextView finishInspectionTv;
    private View headerView;
    private List<RoomListLayout> roomListLayouts;
    private Subscription subscription;
    private boolean canScan = ConstantStr.ALPS.equals(SystemUtil.getDeviceBrand());
    private String mExecutorUserIds;
    private String taskStartType, taskStartDesc;

    public static InspectionDetailFragment newInstance(String taskId) {
        Bundle args = new Bundle();
        args.putString(ConstantStr.KEY_BUNDLE_STR, taskId);
        InspectionDetailFragment fragment = new InspectionDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chooseEmployeeBeen = new ArrayList<>();
        roomListLayouts = new ArrayList<>();
        taskId = getArguments().getString(ConstantStr.KEY_BUNDLE_STR);
        //获取开始状态
        if (InspectionApp.getInstance().getCurrentUser().getCustomer().getCustomerConfig() != null
                && InspectionApp.getInstance().getCurrentUser().getCustomer().getCustomerConfig().size() > 0) {
            for (int i = 0; i < InspectionApp.getInstance().getCurrentUser().getCustomer().getCustomerConfig().size(); i++) {
                if (InspectionApp.getInstance().getCurrentUser().getCustomer().getCustomerConfig().get(i).getConfigCode().equals("startType")) {
                    taskStartType = InspectionApp.getInstance().getCurrentUser().getCustomer().getCustomerConfig().get(i).getConfigValue();
                    taskStartDesc = InspectionApp.getInstance().getCurrentUser().getCustomer().getCustomerConfig().get(i).getConfigDesc();
                    break;
                }
            }
        }
        //没有配置，默认为无要求
        if (TextUtils.isEmpty(taskStartType)) {
            taskStartType = ConstantStr.START_TYPE_0;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fmg_recycle_view_data, container, false);
        noDataLayout = (RelativeLayout) rootView.findViewById(R.id.layout_no_data);
        mRoomsLayout = (LinearLayout) rootView.findViewById(R.id.room_layout);
        finishInspectionTv = (TextView) rootView.findViewById(R.id.tv_finish_inspection);
        finishInspectionTv.setOnClickListener(this);
        finishInspectionTv.setVisibility(View.GONE);
        return rootView;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        headerView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_header_inspection_detail, null);
        addEmployeeLayout = (LinearLayout) headerView.findViewById(R.id.ll_employee_add);
        mList = new ArrayList<>();
        if (mPresenter != null && !TextUtils.isEmpty(taskId)) {
            mPresenter.getInspectionDetailList(taskId);
            mPresenter.loadTaskUserFromDb(Long.valueOf(taskId));
        }
    }

    @Override
    public void setPresenter(InspectionDetailContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showData(InspectionDetailBean inspectionBeen) {
        InspectionApp.getInstance().setInspectionDetailBean(inspectionBeen);
        mList.clear();
        mList.addAll(inspectionBeen.getRoomList());
        finishInspectionTv.setVisibility(View.VISIBLE);
        addRoomToLayout();
    }

    private void addRoomToLayout() {
        mRoomsLayout.removeAllViews();
        mRoomsLayout.addView(headerView);
        addEmployee();
        roomListLayouts.clear();
        for (int i = 0; i < mList.size(); i++) {
            RoomListLayout roomListLayout = new RoomListLayout(getActivity());
            roomListLayout.setRoomBean(mList.get(i), i);
            roomListLayout.setListener(onStartListener, onFinishListener);
            roomListLayouts.add(roomListLayout);
            mRoomsLayout.addView(roomListLayout);
        }
        subscription = rx.Observable.interval(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        if (roomListLayouts.size() > 0) {
                            for (int i = 0; i < roomListLayouts.size(); i++) {
                                roomListLayouts.get(i).timer();
                            }
                        }
                    }
                });
    }

    private RoomListLayout.OnStartListener onStartListener = new RoomListLayout.OnStartListener() {
        @Override
        public void onStart(RoomListBean data, int position) {
            isAutoSendAction = false;
            mPosition = position;
            roomListBean = data;
            if (roomListBean.getTaskRoomState() == ConstantInt.ROOM_STATE_1) {
                if (taskStartType.equals(ConstantStr.START_TYPE_0)) {//无要求，直接开始任务
                    receiverAction();
                } else if (canScan && taskStartType.equals(ConstantStr.START_TYPE_1)) {//手机支持扫描，去扫码
                    startScan();
                } else {
                    InspectionApp.getInstance().showToast(taskStartDesc);
                }
            } else {
                startReportActivity(false);
            }
        }
    };

    private RoomListLayout.OnFinishListener onFinishListener = new RoomListLayout.OnFinishListener() {
        @Override
        public void onFinish(RoomListBean data, int position) {
            isAutoSendAction = false;
            mPosition = position;
            roomListBean = data;
            if (roomListBean.getTaskRoomState() == ConstantInt.ROOM_STATE_1) {
                getApp().showToast("还没有开始");
            } else if (roomListBean.getTaskRoomState() == ConstantInt.ROOM_STATE_2) {
                if (mPresenter != null) {
                    if (mPresenter.checkEquipUploadState(roomListBean.getRoomDb().getTaskId(), roomListBean.getRoomDb().getRoomId())) {
                        receiverAction();
                    } else {
                        startReportActivity(true);
                    }
                }
            } else if (roomListBean.getTaskRoomState() == ConstantInt.ROOM_STATE_3) {
                startReportActivity(false);
            }
        }
    };

    @Override
    public void showLoading() {
        showEvLoading();
    }

    @Override
    public void hideLoading() {
        hideEvLoading();
    }

    @Override
    public void noData() {
        noDataLayout.setVisibility(View.GONE);
    }

    @Override
    public void showTaskUser(@NonNull List<TaskDb> taskDbList) {
        chooseEmployeeBeen = taskDbList;
        addEmployee();
    }

    @Override
    public void updateRoomStateSuccess(long roomId) {
        InspectionApp.getInstance().showToast("成功");
        if (mOperation == 1) {
            //开始
            SPHelper.write(getActivity()
                    , ConstantStr.USER_DATA, InspectionApp.getInstance().getCurrentUser().getUserId()
                            + "_" + roomListBean.getRoomDb().getTaskId()
                            + "_" + roomListBean.getRoomDb().getRoomId() + "start_time", System.currentTimeMillis());
            roomListBean.setTaskRoomState(ConstantInt.ROOM_STATE_2);
            addRoomToLayout();
            startReportActivity(false);
        } else if (mOperation == 2) {
            roomListBean.setTaskRoomState(ConstantInt.ROOM_STATE_3);
            SPHelper.write(getActivity()
                    , ConstantStr.USER_DATA, InspectionApp.getInstance().getCurrentUser().getUserId()
                            + "_" + roomListBean.getRoomDb().getTaskId()
                            + "_" + roomListBean.getRoomDb().getRoomId() + ConstantStr.ROOM_FINISH_TIME, System.currentTimeMillis());
            addRoomToLayout();
        }
    }

    @Override
    public void finishAllRoom() {
        getApp().showToast("已完成所有的检测");
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    @Override
    public void refreshRoomData(RoomDb roomDb) {
        mList.get(mPosition).setRoomDb(roomDb);
        addRoomToLayout();
    }

    private View.OnClickListener addEmployeeClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (addEmployeeIntent == null) {
                addEmployeeIntent = new Intent(getActivity(), EmployeeActivity.class);
            }
            if (chooseEmployeeBeen != null && chooseEmployeeBeen.size() > 0) {
                String[] userIds = new String[chooseEmployeeBeen.size()];
                for (int i = 0; i < chooseEmployeeBeen.size(); i++) {
                    userIds[i] = String.valueOf(chooseEmployeeBeen.get(i).getUserId());
                }
                addEmployeeIntent.putExtra(ConstantStr.KEY_BUNDLE_LIST_1, userIds);
            }
            startActivityForResult(addEmployeeIntent, REQUEST_CODE);
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE) {
                ArrayList<EmployeeBean> employeeBeen = data.getParcelableArrayListExtra(ConstantStr.KEY_BUNDLE_LIST);
                chooseEmployeeBeen.clear();
                if (employeeBeen != null && employeeBeen.size() > 0) {
                    if (mPresenter != null) {
                        mPresenter.saveEmployee(Long.valueOf(taskId), employeeBeen);
                    }
                    for (int i = 0; i < employeeBeen.size(); i++) {
                        chooseEmployeeBeen.add(new TaskDb(Long.valueOf(taskId), employeeBeen.get(i).getUser().getUserId(), employeeBeen.get(i).getUser().getRealName()));
                    }
                }
                addEmployee();
            }
        }
        if (requestCode == REQUEST_CODE_START) {
            if (resultCode == Activity.RESULT_OK) {
                if (roomListBean.getTaskRoomState() == ConstantInt.ROOM_STATE_2) {
                    String TAT = InspectionApp.getInstance().getCurrentUser().getUserId() +
                            "_" + roomListBean.getRoomDb().getTaskId() + "_" + roomListBean.getRoomDb().getRoomId();
                    int count = SPHelper.readInt(getActivity(), ConstantStr.USER_DATA, TAT, 0);
                    if (count != roomListBean.getTaskEquipment().size()) {
                        return;
                    }
                    if (roomListBean.getTaskRoomState() == ConstantInt.ROOM_STATE_2) {
                        //已经开始了，完成
                        if (mPresenter != null) {
                            mOperation = 2;
                            mPresenter.updateRoomState(Long.valueOf(taskId), roomListBean.getTaskRoomId(), mOperation);
                        }
                    }
                }
            }
            if (mPresenter != null && roomListBean != null) {
                mPresenter.refreshRoomData(Long.valueOf(taskId), roomListBean.getRoomDb());
            }
        }

    }

    private void addEmployee() {
        addEmployeeLayout.removeAllViews();
        for (int i = 0; i < chooseEmployeeBeen.size(); i++) {
            TextView textView = new TextView(getActivity());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 0, DisplayUtil.dip2px(getActivity(), 10), 0);
            textView.setLayoutParams(params);
            textView.setBackground(findDrawById(R.drawable.bg_choose_employee));
            textView.setText(chooseEmployeeBeen.get(i).getUserName());
            textView.setTextSize(12);
            textView.setTextColor(findColorById(R.color.colorWhite));
            textView.setGravity(Gravity.CENTER);
            addEmployeeLayout.addView(textView);
        }
        ImageView addIv = new ImageView(getActivity());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        addIv.setLayoutParams(params);
        addIv.setImageDrawable(findDrawById(R.drawable.bg_choose_emp));
        addIv.setOnClickListener(addEmployeeClickListener);
        addEmployeeLayout.addView(addIv);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(android.content.Context context,
                              android.content.Intent intent) {
            String action = intent.getAction();
            if (action.equals(RECE_DATA_ACTION) && !isFlag && taskStartType.equals(ConstantStr.START_TYPE_1)) {
                String data = intent.getStringExtra("se4500");
                if (!TextUtils.isEmpty(data)) {
                    //扫描成功
                    if (isAutoSendAction) {
                        roomListBean = null;
                        for (int i = 0; i < mList.size(); i++) {
                            if (mList.get(i).getRoom().getRoomId() == Long.valueOf(data)) {
                                roomListBean = mList.get(i);
                                mPosition = i;
                                break;
                            }
                        }
                        if (roomListBean == null) {
                            getApp().showToast("无匹配的属地");
                        } else {
                            receiverAction();
                        }
                    } else {
                        if (roomListBean.getRoom().getRoomId() == Long.valueOf(data)) {
                            receiverAction();
                        } else {
                            getApp().showToast("二维码与属地不匹配");
                        }
                    }
                    isAutoSendAction = true;
                }
                cancelRepeat();
            }
        }
    };

    private void receiverAction() {
        isFlag = true;
        if (mPresenter == null) {
            return;
        }
        if (roomListBean.getTaskRoomState() == ConstantInt.ROOM_STATE_1) {
            //未开始，进行中
            mOperation = 1;
            mPresenter.updateRoomState(Long.valueOf(taskId), roomListBean.getTaskRoomId(), mOperation);
        } else {
            startReportActivity(false);
        }
        isFlag = false;
    }

    private void startReportActivity(boolean isCheck) {
        isFlag = true;
        if (reportIntent == null) {
            reportIntent = new Intent(getActivity(), ReportActivity.class);
        }
        reportIntent.putExtra(ConstantStr.KEY_BUNDLE_INT, mPosition);
        if (isCheck) {
            getApp().showToast("还有设备没有上传,请完成巡检");
            reportIntent.putExtra(ConstantStr.KEY_BUNDLE_BOOLEAN, true);
        }
        startActivityForResult(reportIntent, REQUEST_CODE_START);
    }

    private void startScan() {
        getApp().showToast("扫描中...");
        Intent intent = new Intent();
        intent.setAction(STOP_SCAN);
        getActivity().sendBroadcast(intent);
        if (canScan) {
            try {
                SystemProperties.set("persist.sys.scanstopimme", "true");
                SystemClock.sleep(20);
                SystemProperties.set("persist.sys.scanstopimme", "false");
            } catch (Exception e) {
                canScan = false;
                return;
            }
        }
        intent.setAction(START_SCAN_ACTION);
        getActivity().sendBroadcast(intent, null);
    }


    @Override
    public void onResume() {
        super.onResume();
        isFlag = false;
        //注册系统广播  解码扫描到的数据
        IntentFilter iFilter = new IntentFilter();
        iFilter.addAction(RECE_DATA_ACTION);
        getActivity().registerReceiver(receiver, iFilter);
        if (canScan) {
            try {
                SystemProperties.set("persist.sys.scanstopimme", "false");
            } catch (Exception e) {
                e.printStackTrace();
                canScan = false;
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        cancelRepeat();
        try {
            getActivity().unregisterReceiver(receiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cancelRepeat();
        if (subscription != null)
            subscription.unsubscribe();
    }

    private void cancelRepeat() {
        isFlag = false;
        Intent intent = new Intent();
        intent.setAction("com.geomobile.se4500barcodestop");
        getActivity().sendBroadcast(intent);
        if (canScan) {
            try {
                SystemProperties.set("persist.sys.scanstopimme", "true");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_finish_inspection:
                int finishCount = 0;
                for (int i = 0; i < mList.size(); i++) {
                    if (mList.get(i).getTaskRoomState() == ConstantInt.ROOM_STATE_3) {
                        finishCount++;
                    }
                }
                if (finishCount == mList.size()) {
                    //全部完成
                    StringBuilder sb = new StringBuilder();
                    if (TextUtils.isEmpty(mExecutorUserIds) && headerView.getVisibility() == View.VISIBLE) {
                        if (chooseEmployeeBeen != null && chooseEmployeeBeen.size() > 0) {
                            for (int i = 0; i < chooseEmployeeBeen.size(); i++) {
                                sb.append(chooseEmployeeBeen.get(i).getUserId());
                                if (i != chooseEmployeeBeen.size() - 1) {
                                    sb.append(",");
                                }
                            }
                        }
                    } else {
                        sb.append(mExecutorUserIds);
                    }
                    if (mPresenter != null) {
                        mOperation = 3;
                        mPresenter.updateTaskAll(Long.valueOf(taskId), mOperation, sb.toString());
                    }
                } else {
                    InspectionApp.getInstance().showToast("还有没有完成的巡检");
                }
                break;
        }
    }

    @Override
    public boolean findRoom(String roomIdFromTag) {
        roomListBean = null;
        if (TextUtils.isEmpty(roomIdFromTag)) {
            return false;
        }
        if (mList != null && mList.size() > 0) {
            for (int i = 0; i < mList.size(); i++) {
                if (String.valueOf(mList.get(i).getRoom().getRoomId()).equals(roomIdFromTag)) {
                    roomListBean = mList.get(i);
                    mPosition = i;
                    receiverAction();
                    return true;
                }
            }
            return false;
        }
        return false;
    }
}
