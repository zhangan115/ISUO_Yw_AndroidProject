package com.isuo.yw2application.view.main.task.inspection.input;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.isuo.yw2application.R;
import com.isuo.yw2application.app.Yw2Application;
import com.isuo.yw2application.common.BroadcastAction;
import com.isuo.yw2application.common.ConstantInt;
import com.isuo.yw2application.common.ConstantStr;
import com.isuo.yw2application.mode.bean.db.EquipmentDataDb;
import com.isuo.yw2application.mode.bean.equip.FocusBean;
import com.isuo.yw2application.mode.bean.inspection.DataItemBean;
import com.isuo.yw2application.mode.bean.inspection.DataItemValueListBean;
import com.isuo.yw2application.mode.bean.inspection.TaskEquipmentBean;
import com.isuo.yw2application.mode.inspection.InspectionRepository;
import com.isuo.yw2application.utils.PhotoUtils;
import com.isuo.yw2application.view.base.BaseActivity;
import com.isuo.yw2application.view.main.alarm.fault.FaultActivity;
import com.isuo.yw2application.widget.Type1Layout;
import com.isuo.yw2application.widget.Type2_4Layout;
import com.isuo.yw2application.widget.Type3Layout;
import com.sito.library.utils.ActivityUtils;
import com.sito.library.utils.DataUtil;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 录入数据界面
 * Created by zhangan on 2018/2/27.
 */

public class InputActivity extends BaseActivity implements Type3Layout.OnTakePhotoListener, InputContract.View {

    private TaskEquipmentBean taskEquipmentBean;
    private File photoFile;
    private static final int ACTION_START_CAMERA = 100;
    private static final int ACTION_START_ALARM = 101;
    private DataItemBean mDataItemBean;
    private List<Type3Layout> type3Layouts;
    private InputContract.Presenter mPresenter;
    private ImageView alarmIV;
    private long taskId;
    private boolean canEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutAndToolbar(R.layout.activity_input_data, "录入数据");
        new InputPresenter(InspectionRepository.getRepository(this), this);
        taskId = getIntent().getLongExtra(ConstantStr.KEY_BUNDLE_LONG, -1);
        canEdit = getIntent().getBooleanExtra(ConstantStr.KEY_BUNDLE_BOOLEAN, false);
        mPresenter.getTaskEquipFromCache();
        if (!canEdit) {
            findViewById(R.id.tv_finish_input).setVisibility(View.GONE);
        }
        findViewById(R.id.tv_finish_input).setOnClickListener(this);
        myHandle = new MyHandle(new WeakReference<>(this));
        autoSaveReceiver = new AutoSaveReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(BroadcastAction.AUTO_SAVE_DATA);
        registerReceiver(autoSaveReceiver, filter);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.alarm:
                Intent intent = new Intent(this, FaultActivity.class);
                intent.putExtra(ConstantStr.KEY_BUNDLE_LONG, taskId);
                intent.putExtra(ConstantStr.KEY_BUNDLE_LONG_1, taskEquipmentBean.getEquipment().getEquipmentId());
                intent.putExtra(ConstantStr.KEY_BUNDLE_STR, taskEquipmentBean.getEquipment().getEquipmentName());
                startActivityForResult(intent, ACTION_START_ALARM);
                break;
            case R.id.tv_finish_input:
                boolean canFinish = true;
                for (int i = 0; i < taskEquipmentBean.getDataList().get(0).getDataItemValueList().size(); i++) {
                    if (taskEquipmentBean.getDataList().get(0).getDataItemValueList().get(i).getDataItem().getIsRequired() == 0)
                        continue;
                    if (taskEquipmentBean.getDataList().get(0).getDataItemValueList().get(i).getDataItem().getInspectionType() == ConstantInt.DATA_VALUE_TYPE_3) {
                        if (TextUtils.isEmpty(taskEquipmentBean.getDataList().get(0).getDataItemValueList().get(i).getDataItem().getLocalFile())) {
                            canFinish = false;
                            break;
                        }
                    } else {
                        if (TextUtils.isEmpty(taskEquipmentBean.getDataList().get(0).getDataItemValueList().get(i).getDataItem().getValue())) {
                            canFinish = false;
                            break;
                        }
                    }
                }
                if (canFinish) {
                    finishInputData(false);
                } else {
                    Yw2Application.getInstance().showToast("还有数据没有录入!");
                }
                break;
        }
    }

    private void finishInputData(final boolean isAuto) {
        boolean isPhotoUpload = false;
        if (taskEquipmentBean.getDataList().get(0).getDataItemValueList() != null) {
            for (int i = 0; i < taskEquipmentBean.getDataList().get(0).getDataItemValueList().size(); i++) {
                if (taskEquipmentBean.getDataList().get(0).getDataItemValueList().get(i).getDataItem().getInspectionType() == ConstantInt.DATA_VALUE_TYPE_3) {
                    if (taskEquipmentBean.getDataList().get(0).getDataItemValueList().get(i).getDataItem().isUploading()) {
                        isPhotoUpload = true;
                        break;
                    }
                }
            }
        }
        if (isPhotoUpload) {
            new MaterialDialog.Builder(this).content("照片正在上传中,是否退出？")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            dateEditFinish(isAuto);
                        }
                    })
                    .negativeText("取消")
                    .positiveText("确定")
                    .show();
        } else {
            dateEditFinish(isAuto);
        }
    }

    private void dateEditFinish(boolean isAuto) {
        mPresenter.saveData(taskEquipmentBean, isAuto);
        mPresenter.saveTaskEquipToCache(taskEquipmentBean);
        setResult(Activity.RESULT_OK);
        finish();
    }

    @Override
    public void onBackPressed() {
        finishInputData(true);
    }

    @Override
    public void toolBarClick() {
        finishInputData(true);
    }

    @Override
    public void onTakePhoto(DataItemBean dataItemBean) {
        this.mDataItemBean = dataItemBean;
        photoFile = new File(Yw2Application.getInstance().imageCacheFile(), System.currentTimeMillis() + ".jpg");
        ActivityUtils.startCameraToPhoto(this, photoFile, ACTION_START_CAMERA);
    }

    @Override
    public void onDeletePhoto(DataItemBean dataItemBean) {
        dataItemBean.setValue("");
        dataItemBean.setLocalFile(null);
        EquipmentDataDb dataDb = dataItemBean.getEquipmentDataDb();
        dataDb.setValue(null);
        dataDb.setLocalPhoto(null);
        Yw2Application.getInstance().getDaoSession().getEquipmentDataDbDao().insertOrReplaceInTx(dataDb);
        refreshUi();
    }

    @Override
    public void onAgainPhoto(DataItemBean dataItemBean) {
        onDeletePhoto(dataItemBean);
        onTakePhoto(dataItemBean);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ACTION_START_CAMERA && resultCode == Activity.RESULT_OK) {
            PhotoUtils.cropPhoto(this, photoFile, new PhotoUtils.PhotoListener() {
                @Override
                public void onSuccess(File file) {
                    if (mPresenter == null) {
                        return;
                    }
                    if (mDataItemBean != null) {
                        mDataItemBean.setLocalFile(file.getAbsolutePath());
                        mDataItemBean.setUploading(true);
                        if (taskEquipmentBean.getEquipment().getEquipmentDb().getUploadState()) {
                            taskEquipmentBean.getEquipment().getEquipmentDb().setUploadState(false);
                        }
                        if (taskEquipmentBean.getEquipment().getEquipmentDb().getCanUpload()) {
                            taskEquipmentBean.getEquipment().getEquipmentDb().setCanUpload(false);
                        }
                        Yw2Application.getInstance().getDaoSession().getEquipmentDbDao()
                                .insertOrReplace(taskEquipmentBean.getEquipment().getEquipmentDb());

                        mDataItemBean.getEquipmentDataDb().setLocalPhoto(file.getAbsolutePath());
                        Yw2Application.getInstance().getDaoSession().getEquipmentDataDbDao()
                                .insertOrReplace(mDataItemBean.getEquipmentDataDb());
                        mPresenter.uploadImage(mDataItemBean);
                        refreshUi();
                    }
                }
            });
        } else if (Activity.RESULT_OK == resultCode && requestCode == ACTION_START_ALARM) {
            taskEquipmentBean.getEquipment().getEquipmentDb().setAlarmState(true);
            Yw2Application.getInstance().getDaoSession().getEquipmentDbDao()
                    .insertOrReplaceInTx(taskEquipmentBean.getEquipment().getEquipmentDb());
            alarmIV.setImageDrawable(findDrawById(R.drawable.fault_call_icon_r_normal));
        }
    }

    private void refreshUi() {
        if (type3Layouts != null && type3Layouts.size() > 0) {
            for (Type3Layout layout : type3Layouts) {
                layout.refreshUi();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        autoSave = false;
        try {
            if (myHandle != null) {
                myHandle.removeCallbacksAndMessages(null);
            }
            if (autoSaveReceiver != null) {
                unregisterReceiver(autoSaveReceiver);
            }
            if (mPresenter != null) {
                mPresenter.unSubscribe();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void uploadPhotoSuccess() {
        refreshUi();
    }

    @Override
    public void uploadPhotoFail() {
        refreshUi();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void showCareData(FocusBean f) {
        findViewById(R.id.ll_care).setVisibility(View.VISIBLE);
        TextView care_time = findViewById(R.id.care_time);
        TextView care_des = findViewById(R.id.care_des);
        care_time.setText("结束日期:" + DataUtil.timeFormat(f.getEndTime(), "yyy-MM-dd"));
        if (!TextUtils.isEmpty(f.getDescription())) {
            care_des.setText("内容描述:" + f.getDescription());
        } else {
            care_des.setVisibility(View.GONE);
        }
    }

    @Override
    public void showCareDataFail() {
        findViewById(R.id.ll_care).setVisibility(View.GONE);
    }

    @Override
    public void showTaskEquipmentData() {
        LinearLayout inputDataLayout = findViewById(R.id.ll_input_data);
        TextView equipmentName = findViewById(R.id.equipment_name);
        equipmentName.setText(taskEquipmentBean.getEquipment().getEquipmentName().replace("\n", ""));
        alarmIV = findViewById(R.id.alarm);
        alarmIV.setOnClickListener(this);
        if (taskEquipmentBean.getEquipment().getEquipmentDb().getAlarmState()) {
            alarmIV.setImageDrawable(findDrawById(R.drawable.fault_call_icon_selected));
        }
        type3Layouts = new ArrayList<>();
        if (taskEquipmentBean.getDataList() != null && taskEquipmentBean.getDataList().size() > 0
                && taskEquipmentBean.getDataList().get(0).getDataItemValueList() != null
                && taskEquipmentBean.getDataList().get(0).getDataItemValueList().size() > 0) {
            for (int i = 0; i < taskEquipmentBean.getDataList().get(0).getDataItemValueList().size(); i++) {
                DataItemValueListBean dataItemValueListBean = taskEquipmentBean.getDataList().get(0).getDataItemValueList().get(i);
                if (dataItemValueListBean.getDataItem() != null) {
                    switch (dataItemValueListBean.getDataItem().getInspectionType()) {
                        case ConstantInt.DATA_VALUE_TYPE_1:
                            Type1Layout type1Layout = new Type1Layout(InputActivity.this);
                            type1Layout.setDataToView(canEdit, dataItemValueListBean, taskEquipmentBean.getEquipment());
                            inputDataLayout.addView(type1Layout);
                            break;
                        case ConstantInt.DATA_VALUE_TYPE_2:
                        case ConstantInt.DATA_VALUE_TYPE_4:
                            Type2_4Layout type2_4Layout = new Type2_4Layout(InputActivity.this);
                            type2_4Layout.setDataToView(canEdit, dataItemValueListBean, taskEquipmentBean.getEquipment());
                            inputDataLayout.addView(type2_4Layout);
                            break;
                        case ConstantInt.DATA_VALUE_TYPE_3:
                            Type3Layout type3Layout = new Type3Layout(InputActivity.this);
                            type3Layout.setDataToView(canEdit, dataItemValueListBean, this);
                            type3Layouts.add(type3Layout);
                            inputDataLayout.addView(type3Layout);
                            break;
                    }
                }
            }
            if (inputDataLayout.getChildCount() > 0) {
                autoSave = true;
                new Thread(new MyRunnable()).start();
            } else {
                findViewById(R.id.tv_finish_input).setVisibility(View.GONE);
            }
            if (taskEquipmentBean.getEquipment().getIsOnFocus() == 1) {
                mPresenter.getEquipmentCare(taskEquipmentBean.getEquipment().getEquipmentId());
            }
        }
    }

    @Override
    public void showTaskEquipFromCache(TaskEquipmentBean taskEquipmentBean) {
        this.taskEquipmentBean = taskEquipmentBean;
        mPresenter.getShareData(taskEquipmentBean);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mPresenter.saveTaskEquipToCache(taskEquipmentBean);
    }

    private boolean autoSave;
    private MyHandle myHandle;

    public static class MyHandle extends Handler {

        private final WeakReference<InputActivity> activity;

        MyHandle(WeakReference<InputActivity> activity) {
            this.activity = activity;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1 && activity != null && activity.get() != null) {
                Intent intent = new Intent();
                intent.setAction(BroadcastAction.AUTO_SAVE_DATA);
                activity.get().sendBroadcast(intent);
            }
        }
    }

    private class MyRunnable implements Runnable {

        @Override
        public void run() {
            while (autoSave) {
                try {
                    Thread.sleep(ConstantInt.AUTO_SAVE_DATA);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (mPresenter != null && myHandle != null) {
                    myHandle.sendEmptyMessage(1);
                }
            }
        }
    }

    private AutoSaveReceiver autoSaveReceiver;

    private class AutoSaveReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (mPresenter != null && taskEquipmentBean != null) {
                mPresenter.saveData(taskEquipmentBean, true);
            }
        }
    }

    @Override
    public void setPresenter(InputContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
