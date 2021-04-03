package com.isuo.yw2application.view.main.task.inspection.work;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.isuo.yw2application.R;
import com.isuo.yw2application.app.Yw2Application;
import com.isuo.yw2application.common.BroadcastAction;
import com.isuo.yw2application.common.ConstantInt;
import com.isuo.yw2application.common.ConstantStr;
import com.isuo.yw2application.mode.bean.db.TaskDb;
import com.isuo.yw2application.mode.bean.employee.EmployeeBean;
import com.isuo.yw2application.mode.bean.inspection.InspectionDetailBean;
import com.isuo.yw2application.mode.bean.inspection.RoomListBean;
import com.isuo.yw2application.mode.inspection.InspectionRepository;
import com.isuo.yw2application.view.base.MvpFragmentV4;
import com.isuo.yw2application.view.main.adduser.EmployeeActivity;
import com.isuo.yw2application.view.main.task.inspection.report.ReportActivity;
import com.isuo.yw2application.widget.RoomListLayout;
import com.sito.library.utils.DisplayUtil;
import com.sito.library.utils.SystemUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


/**
 * 巡检room list
 * Created by zhangan on 2018/3/20.
 */

public class InspectionRoomFragment extends MvpFragmentV4<InspectionRoomContract.Presenter> implements InspectionRoomContract.View
        , View.OnClickListener, IFindRoomListener {
    //view
    private RelativeLayout noDataLayout;
    private LinearLayout mRoomsLayout;
    private LinearLayout addEmployeeLayout;
    private View headerView;
    private TextView finishInspectionTv;
    //data
    private long taskId;
    private int REQUEST_CODE = 200;
    private int REQUEST_CODE_START = 201;
    private String taskStartType, taskStartDesc;
    private List<RoomListLayout> roomListLayouts;
    private boolean canScan = ConstantStr.ALPS.equals(SystemUtil.getDeviceBrand());
    //需要保存的状态
    private boolean isFlag = false;
    private boolean isAutoSendAction = true;
    private ArrayList<RoomListBean> mList;
    private RoomListBean roomListBean;
    private int mPosition = -1;
    private int mOperation = 1;
    private ArrayList<TaskDb> chooseEmployeeBeen;//已经添加的人员
    private InspectionDetailBean inspectionDetailBean;//巡检数据

    public static InspectionRoomFragment newInstance(long taskId) {
        Bundle args = new Bundle();
        args.putLong(ConstantStr.KEY_BUNDLE_LONG, taskId);
        InspectionRoomFragment fragment = new InspectionRoomFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() instanceof InspectionRoomActivity) {
            ((InspectionRoomActivity) getActivity()).setFindRoomListener(this);
        }
        new InspectionRoomPresenter(this, InspectionRepository.getRepository(getActivity()));
        Bundle bundle = getArguments();
        if (bundle != null) {
            taskId = bundle.getLong(ConstantStr.KEY_BUNDLE_LONG);
        }
        mList = new ArrayList<>();
        if (savedInstanceState != null) {
            isFlag = savedInstanceState.getBoolean("isFlag");
            isAutoSendAction = savedInstanceState.getBoolean("isAutoSendAction");
            mPosition = savedInstanceState.getInt("mPosition");
            mOperation = savedInstanceState.getInt("mOperation");
            inspectionDetailBean = mPresenter.getInspectionFromCache();
            if (inspectionDetailBean != null) {
                mList.addAll(inspectionDetailBean.getRoomList());
                if (mPosition != -1) {
                    roomListBean = mList.get(mPosition);
                }
            }
            chooseEmployeeBeen = savedInstanceState.getParcelableArrayList("chooseEmployeeBeen");
        }
        if (chooseEmployeeBeen == null) {
            chooseEmployeeBeen = new ArrayList<>();
        }
        roomListLayouts = new ArrayList<>();
        //获取开始状态
        if (Yw2Application.getInstance().getCurrentUser().getCustomer().getCustomerConfig() != null
                && Yw2Application.getInstance().getCurrentUser().getCustomer().getCustomerConfig().size() > 0) {
            for (int i = 0; i < Yw2Application.getInstance().getCurrentUser().getCustomer().getCustomerConfig().size(); i++) {
                if (Yw2Application.getInstance().getCurrentUser().getCustomer().getCustomerConfig().get(i).getConfigCode().equals("startType")) {
                    taskStartType = Yw2Application.getInstance().getCurrentUser().getCustomer().getCustomerConfig().get(i).getConfigValue();
                    taskStartDesc = Yw2Application.getInstance().getCurrentUser().getCustomer().getCustomerConfig().get(i).getConfigDesc();
                    break;
                }
            }
        }
        //没有配置，默认为无要求
        if (TextUtils.isEmpty(taskStartType)) {
            taskStartType = ConstantStr.START_TYPE_0;
        }
        myHandle = new MyHandle(new WeakReference<>(getActivity()));
        refreshUiReceiver = new RefreshUiReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(BroadcastAction.AUTO_UPLOAD_DATA);
        getActivity().registerReceiver(refreshUiReceiver, filter);
        autoRefresh = true;
        new Thread(new MyRunnable()).start();
    }

    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.inspection_room_fragment, container, false);
        noDataLayout = rootView.findViewById(R.id.layout_no_data);
        mRoomsLayout = rootView.findViewById(R.id.room_layout);
        finishInspectionTv = rootView.findViewById(R.id.tv_finish_inspection);
        finishInspectionTv.setOnClickListener(this);
        finishInspectionTv.setVisibility(View.GONE);
        headerView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_header_inspection_detail, null);
        addEmployeeLayout = headerView.findViewById(R.id.ll_employee_add);
        if (inspectionDetailBean != null && roomListBean != null && chooseEmployeeBeen != null) {
            showData(inspectionDetailBean);
            showTaskUser(chooseEmployeeBeen);
        } else if (mPresenter != null) {
            mPresenter.getInspectionDataList(taskId);
            mPresenter.loadTaskUserFromDb(taskId);
        }
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("isFlag", isFlag);
        outState.putBoolean("isAutoSendAction", isAutoSendAction);
        outState.putInt("mPosition", mPosition);
        outState.putInt("mOperation", mOperation);
        outState.putParcelableArrayList("chooseEmployeeBeen", chooseEmployeeBeen);
    }

    @Override
    public void setPresenter(InspectionRoomContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showData(InspectionDetailBean inspectionBeen) {
        this.inspectionDetailBean = inspectionBeen;
        mList.clear();
        mList.addAll(inspectionBeen.getRoomList());
        finishInspectionTv.setVisibility(View.VISIBLE);
        addRoomToLayout();
    }

    @Override
    public void showData() {
        addRoomToLayout();
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
    public void noData() {
        noDataLayout.setVisibility(View.GONE);
    }

    @Override
    public void showTaskUser(@NonNull ArrayList<TaskDb> taskDbList) {
        chooseEmployeeBeen = taskDbList;
        addEmployee();
    }

    @Override
    public void updateRoomStateSuccess() {
        Yw2Application.getInstance().showToast("成功");
        if (mOperation == 1) {
            //开始
            addRoomToLayout();
            startReportActivity();
        } else if (mOperation == 2) {
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
    public void showMessage(String message) {
        getApp().showToast(message);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            ArrayList<EmployeeBean> employeeBeen = data.getParcelableArrayListExtra(ConstantStr.KEY_BUNDLE_LIST);
            chooseEmployeeBeen.clear();
            if (employeeBeen != null && employeeBeen.size() > 0) {
                if (mPresenter != null) {
                    mPresenter.saveEmployee(taskId, employeeBeen);
                }
                for (int i = 0; i < employeeBeen.size(); i++) {
                    chooseEmployeeBeen.add(new TaskDb(taskId, employeeBeen.get(i).getUser().getUserId()
                            , employeeBeen.get(i).getUser().getRealName()));
                }
            }
            addEmployee();
        } else if (requestCode == REQUEST_CODE_START) {
            if (roomListBean == null || mList == null) {
                return;
            }
            if (resultCode == Activity.RESULT_OK) {
                if (roomListBean.getTaskRoomState() == ConstantInt.ROOM_STATE_2) {
                    //已经开始了，完成
                    if (mPresenter != null) {
                        mOperation = 2;
                        mPresenter.updateRoomState(taskId, roomListBean, mOperation);
                    }
                }
            }
            if (mPresenter != null && mList.size() > 0) {
                mPresenter.loadRoomDataFromDb(taskId, mList);
            }
        }
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
                    Yw2Application.getInstance().showToast(taskStartDesc);
                }
            } else {
                startReportActivity();
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
            } else {
                startReportActivity();
            }
        }
    };


    private MyHandle myHandle;

    public static class MyHandle extends Handler {

        private final WeakReference<FragmentActivity> activity;

        MyHandle(WeakReference<FragmentActivity> activity) {
            this.activity = activity;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1 && activity != null && activity.get() != null) {
                Intent intent = new Intent();
                intent.setAction(BroadcastAction.AUTO_UPLOAD_DATA);
                activity.get().sendBroadcast(intent);
            }
        }
    }

    private boolean autoRefresh = false;

    private class MyRunnable implements Runnable {

        @Override
        public void run() {
            while (autoRefresh) {
                try {
                    Thread.sleep(ConstantInt.AUTO_REFRESH_UI);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (myHandle != null) {
                    myHandle.sendEmptyMessage(1);
                }
            }
        }
    }

    private RefreshUiReceiver refreshUiReceiver;

    private class RefreshUiReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (roomListLayouts != null && roomListLayouts.size() > 0) {
                for (int i = 0; i < roomListLayouts.size(); i++) {
                    roomListLayouts.get(i).timer();
                }
            }
        }
    }

    private void addEmployee() {
        addEmployeeLayout.removeAllViews();
        for (int i = 0; i < chooseEmployeeBeen.size(); i++) {
            TextView textView = new TextView(getActivity());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DisplayUtil.dip2px(getActivity(), 60)
                    , DisplayUtil.dip2px(getActivity(), 60));
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
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DisplayUtil.dip2px(getActivity(), 60)
                , DisplayUtil.dip2px(getActivity(), 60));
        addIv.setLayoutParams(params);
        addIv.setImageDrawable(findDrawById(R.drawable.add_btn));
        addIv.setOnClickListener(addEmployeeClickListener);
        addEmployeeLayout.addView(addIv);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (TextUtils.isEmpty(action)) {
                return;
            }
            if (action.equals(ConstantStr.RECE_DATA_ACTION) && !isFlag && taskStartType.equals(ConstantStr.START_TYPE_1)) {
                String data = intent.getStringExtra("se4500");
                if (!TextUtils.isEmpty(data)) {
                    //扫描成功
                    if (isAutoSendAction) {
                        roomListBean = null;
                        for (int i = 0; i < mList.size(); i++) {
                            if (mList.get(i).getRoom().getRoomId() == Long.parseLong(data)) {
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
                        if (roomListBean.getRoom().getRoomId() == Long.parseLong(data)) {
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
            mPresenter.updateRoomState(taskId, roomListBean, mOperation);
        } else {
            startReportActivity();
        }
        isFlag = false;
    }

    private void startReportActivity() {
        isFlag = true;
        Intent reportIntent = new Intent(getActivity(), ReportActivity.class);
        reportIntent.putExtra(ConstantStr.KEY_BUNDLE_INT, mPosition);
        mPresenter.saveInspectionToCache(inspectionDetailBean);
        startActivityForResult(reportIntent, REQUEST_CODE_START);
    }

    private void startScan() {
        getApp().showToast("扫描中...");
        Intent intent = new Intent();
        intent.setAction(ConstantStr.STOP_SCAN);
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
        intent.setAction(ConstantStr.START_SCAN_ACTION);
        getActivity().sendBroadcast(intent, null);
    }


    @Override
    public void onResume() {
        super.onResume();
        isFlag = false;
        //注册系统广播  解码扫描到的数据
        IntentFilter iFilter = new IntentFilter();
        iFilter.addAction(ConstantStr.RECE_DATA_ACTION);
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
        autoRefresh = false;
        try {
            if (myHandle != null) {
                myHandle.removeCallbacksAndMessages(null);
            }
            if (refreshUiReceiver != null) {
                getActivity().unregisterReceiver(refreshUiReceiver);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                    if (chooseEmployeeBeen != null && chooseEmployeeBeen.size() > 0) {
                        for (int i = 0; i < chooseEmployeeBeen.size(); i++) {
                            sb.append(chooseEmployeeBeen.get(i).getUserId());
                            if (i != chooseEmployeeBeen.size() - 1) {
                                sb.append(",");
                            }
                        }
                    }
                    if (mPresenter != null) {
                        mOperation = 3;
                        mPresenter.roomListFinish(taskId, mOperation, sb.toString());
                    }
                } else {
                    Yw2Application.getInstance().showToast("还有没有完成的巡检");
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

    private View.OnClickListener addEmployeeClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent addEmployeeIntent = new Intent(getActivity(), EmployeeActivity.class);
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

}
