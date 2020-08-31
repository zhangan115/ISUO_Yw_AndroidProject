package com.sito.evpro.inspection.view.repair.inspection.input;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.sito.evpro.inspection.R;
import com.sito.evpro.inspection.app.InspectionApp;
import com.sito.evpro.inspection.common.ConstantInt;
import com.sito.evpro.inspection.common.ConstantStr;
import com.sito.evpro.inspection.mode.bean.db.EquipmentDataDb;
import com.sito.evpro.inspection.mode.bean.db.EquipmentDb;
import com.sito.evpro.inspection.mode.bean.inspection.DataItemBean;
import com.sito.evpro.inspection.mode.bean.inspection.DataItemValueListBean;
import com.sito.evpro.inspection.mode.bean.inspection.TaskEquipmentBean;
import com.sito.evpro.inspection.utils.PhotoUtils;
import com.sito.evpro.inspection.view.BaseActivity;
import com.sito.evpro.inspection.view.fault.FaultActivity;
import com.sito.evpro.inspection.widget.Type1Layout;
import com.sito.evpro.inspection.widget.Type2_4Layout;
import com.sito.evpro.inspection.widget.Type3Layout;
import com.sito.library.utils.ActivityUtils;

import java.io.File;
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
    private long taskId;
    private List<Type3Layout> type3Layouts;
    InputContract.Presenter mPresenter;
    private ImageView alarmIV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutAndToolbar(R.layout.activity_input_data, "录入数据");
        taskEquipmentBean = getIntent().getParcelableExtra(ConstantStr.KEY_BUNDLE_OBJECT);
        boolean canEdit = getIntent().getBooleanExtra(ConstantStr.KEY_BUNDLE_BOOLEAN, false);
        taskId = getIntent().getLongExtra(ConstantStr.KEY_BUNDLE_LONG, -1);
        if (taskEquipmentBean == null) {
            finish();
            return;
        }
        new InputPresenter(InspectionApp.getInstance().getInspectionWorkRepositoryComponent().getRepository(), this);
        LinearLayout inputDataLayout = (LinearLayout) findViewById(R.id.ll_input_data);
        if (!canEdit) {
            findViewById(R.id.tv_finish_input).setVisibility(View.GONE);
        }
        findViewById(R.id.tv_finish_input).setOnClickListener(this);
        TextView equipmentName = (TextView) findViewById(R.id.equipment_name);
        equipmentName.setText(taskEquipmentBean.getEquipment().getEquipmentName().replace("\n", ""));
        alarmIV = (ImageView) findViewById(R.id.alarm);
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
                mPresenter.startAutoSave(taskEquipmentBean);
            } else {
                findViewById(R.id.tv_finish_input).setVisibility(View.GONE);
            }
        }
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
                    InspectionApp.getInstance().showToast("还有数据没有录入!");
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
        mPresenter.stopAutoSave();
        mPresenter.saveData(taskEquipmentBean, isAuto);
        Intent intentData = new Intent();
        intentData.putExtra(ConstantStr.KEY_BUNDLE_OBJECT, taskEquipmentBean);
        setResult(Activity.RESULT_OK, intentData);
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
        photoFile = new File(InspectionApp.getInstance().imageCacheFile(), System.currentTimeMillis() + ".jpg");
        ActivityUtils.startCameraToPhoto(this, photoFile, ACTION_START_CAMERA);
    }

    @Override
    public void onDeletePhoto(DataItemBean dataItemBean) {
        dataItemBean.setValue("");
        dataItemBean.setLocalFile(null);
        EquipmentDataDb dataDb = dataItemBean.getEquipmentDataDb();
        dataDb.setValue(null);
        dataDb.setLocalPhoto(null);
        InspectionApp.getInstance().getDaoSession().getEquipmentDataDbDao().insertOrReplaceInTx(dataDb);
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
                        InspectionApp.getInstance().getDaoSession().getEquipmentDbDao()
                                .insertOrReplace(taskEquipmentBean.getEquipment().getEquipmentDb());

                        mDataItemBean.getEquipmentDataDb().setLocalPhoto(file.getAbsolutePath());
                        InspectionApp.getInstance().getDaoSession().getEquipmentDataDbDao()
                                .insertOrReplace(mDataItemBean.getEquipmentDataDb());
                        mPresenter.uploadImage(mDataItemBean);
                        refreshUi();
                    }
                }
            });
        } else if (Activity.RESULT_OK == resultCode && requestCode == ACTION_START_ALARM) {
            taskEquipmentBean.getEquipment().getEquipmentDb().setAlarmState(true);
            InspectionApp.getInstance().getDaoSession().getEquipmentDbDao()
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
        if (mPresenter != null) {
            mPresenter.unSubscribe();
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

    @Override
    public void setPresenter(InputContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
