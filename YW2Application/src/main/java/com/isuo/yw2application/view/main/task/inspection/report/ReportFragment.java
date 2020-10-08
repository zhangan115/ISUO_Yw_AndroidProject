package com.isuo.yw2application.view.main.task.inspection.report;

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
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.isuo.yw2application.R;
import com.isuo.yw2application.app.Yw2Application;
import com.isuo.yw2application.common.BroadcastAction;
import com.isuo.yw2application.common.ConstantInt;
import com.isuo.yw2application.common.ConstantStr;
import com.isuo.yw2application.mode.bean.db.RoomDb;
import com.isuo.yw2application.mode.bean.inspection.InspectionDetailBean;
import com.isuo.yw2application.mode.bean.inspection.RoomListBean;
import com.isuo.yw2application.mode.bean.inspection.TaskEquipmentBean;
import com.isuo.yw2application.mode.inspection.InspectionRepository;
import com.isuo.yw2application.utils.PhotoUtils;
import com.isuo.yw2application.view.base.MvpFragment;
import com.isuo.yw2application.view.main.task.increment.submit.IncrementActivity;
import com.isuo.yw2application.view.main.task.inspection.input.InputActivity;
import com.isuo.yw2application.view.photo.ViewPagePhotoActivity;
import com.sito.library.adapter.RVAdapter;
import com.sito.library.utils.ActivityUtils;
import com.sito.library.utils.GlideUtils;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


/**
 * 巡检提交数据
 * Created by zhangan on 2017-06-26.
 */

public class ReportFragment extends MvpFragment<ReportContract.Presenter> implements ReportContract.View {
    //data
    private File photoFile;
    private RoomDb roomDb;
    private RoomListBean mRoomListBean;
    private InspectionDetailBean inspectionDetailBean;
    private ArrayList<TaskEquipmentBean> mTaskEquipmentBean;
    private ArrayList<TaskEquipmentBean> showBean;
    private int roomPosition = -1, editPosition;
    private long takePhotoEquipmentId = -1;

    private static final int ACTION_START_CAMERA = 100;
    private static final int ACTION_START_INPUT = 101;
    private boolean caEdit = true;
    private boolean autoUpload = false, canUpload = true;
    //view
    private TextView bottomView;
    private TextView mTitleTv;
    private RecyclerView mRecyclerView;
    private ProgressBar dialogProgressBar;
    private MaterialDialog takePhotoDialog;
    private ImageView equipmentTakePhotoIV;
    private RelativeLayout noDataLayout;


    public static ReportFragment newInstance(int position) {
        Bundle args = new Bundle();
        args.putInt(ConstantStr.KEY_BUNDLE_INT, position);
        ReportFragment fragment = new ReportFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new ReportPresenter(InspectionRepository.getRepository(getActivity()), this);
        inspectionDetailBean = mPresenter.getInspectionData();
        mTaskEquipmentBean = new ArrayList<>();
        showBean = new ArrayList<>();
        if (savedInstanceState != null) {
            editPosition = savedInstanceState.getInt("editPosition");
        }
        Bundle bundle = getArguments();
        if (bundle != null) {
            roomPosition = bundle.getInt(ConstantStr.KEY_BUNDLE_INT);
        }
        if (inspectionDetailBean != null) {
            mRoomListBean = inspectionDetailBean.getRoomList().get(roomPosition);
            roomDb = mRoomListBean.getRoomDb();
            caEdit = !(mRoomListBean.getTaskRoomState() == ConstantInt.ROOM_STATE_3);
        }
        myHandle = new MyHandle(new WeakReference<>(getActivity()));
        uploadReceiver = new UploadReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(BroadcastAction.AUTO_REFRESH_UI);
        getActivity().registerReceiver(uploadReceiver, filter);
        autoUpload = true;
        new Thread(new MyRunnable()).start();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fmg_report, container, false);
        Toolbar toolbar = rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        final ActionBar ab = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setDisplayShowTitleEnabled(false);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        mTitleTv = rootView.findViewById(R.id.titleId);
        if (caEdit) {
            rootView.findViewById(R.id.ib_increment).setVisibility(View.VISIBLE);
        }
        rootView.findViewById(R.id.ib_increment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), IncrementActivity.class));
            }
        });
        noDataLayout = rootView.findViewById(R.id.layout_no_data);
        final EditText editText = rootView.findViewById(R.id.edit_search);
        final ImageView cleanEdit = rootView.findViewById(R.id.iv_clean_edit);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                searchEquipment(s.toString());
                if (s.length() > 0) {
                    cleanEdit.setVisibility(View.VISIBLE);
                } else {
                    cleanEdit.setVisibility(View.GONE);
                }
            }
        });
        cleanEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("");
                editText.setSelection(0);
            }
        });
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (!TextUtils.isEmpty(editText.getText().toString())) {
                        searchEquipment(editText.getText().toString());
                        return true;
                    }
                }
                return false;
            }
        });
        mRecyclerView = rootView.findViewById(R.id.recycleViewId);
        bottomView = rootView.findViewById(R.id.tv_report);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        RVAdapter<TaskEquipmentBean> adapter = new RVAdapter<TaskEquipmentBean>(mRecyclerView, showBean, R.layout.item_task_equipment_list) {
            @Override
            public void showData(ViewHolder vHolder, TaskEquipmentBean data, int position) {
                TextView tv_equipment_name = (TextView) vHolder.getView(R.id.tv_equipment_name);
                TextView tv_equipment_state = (TextView) vHolder.getView(R.id.tv_equipment_state);
                ImageView iv_state = (ImageView) vHolder.getView(R.id.iv_state);
                View view_division = vHolder.getView(R.id.view_division);
                String str = position + 1 + ".  " + data.getEquipment().getEquipmentName().replace("\n", "");
                tv_equipment_name.setText(str);
                if (data.getEquipment().getEquipmentDb().getAlarmState()) {
                    iv_state.setVisibility(View.VISIBLE);
                } else {
                    iv_state.setVisibility(View.GONE);
                }
                if (data.getEquipment().getEquipmentDb().getCanUpload()) {
                    tv_equipment_state.setVisibility(View.VISIBLE);
                } else {
                    tv_equipment_state.setVisibility(View.INVISIBLE);
                }
                if (position == showBean.size() - 1) {
                    view_division.setVisibility(View.GONE);
                } else {
                    view_division.setVisibility(View.VISIBLE);
                }
            }
        };
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new RVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                editPosition = position;
                Intent intent = new Intent(getActivity(), InputActivity.class);
                mPresenter.saveEditTaskEquipToCache(showBean.get(position));
                intent.putExtra(ConstantStr.KEY_BUNDLE_BOOLEAN, caEdit);
                intent.putExtra(ConstantStr.KEY_BUNDLE_LONG, roomDb.getTaskId());
                Yw2Application.getInstance().hideSoftKeyBoard(getActivity());
                startActivityForResult(intent, ACTION_START_INPUT);
            }
        });
        return rootView;
    }

    private void searchEquipment(String s) {
        List<TaskEquipmentBean> searchBean = new ArrayList<>();
        for (int i = 0; i < mTaskEquipmentBean.size(); i++) {
            if (mTaskEquipmentBean.get(i).getEquipment().getEquipmentName().contains(s)) {
                searchBean.add(mTaskEquipmentBean.get(i));
            }
        }
        showBean.clear();
        showBean.addAll(searchBean);
        if (showBean.size() == 0) {
            mRecyclerView.setVisibility(View.GONE);
            noDataLayout.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            noDataLayout.setVisibility(View.GONE);
        }
        mRecyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!caEdit) {
            bottomView.setVisibility(View.GONE);
        }
        bottomView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPresenter == null) {
                    return;
                }
                if (roomDb.getCheckCount() != mRoomListBean.getTaskEquipment().size()) {
                    getApp().showToast("有漏检项目!请检查");
                    return;
                }
                @SuppressLint("InflateParams")
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_take_photo, null);
                view.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (takePhotoDialog != null) {
                            takePhotoDialog.dismiss();
                        }
                    }
                });
                TextView equipmentName = view.findViewById(R.id.tv_equipment_name);
                String noteStr = "";
                for (int i = 0; i < mTaskEquipmentBean.size(); i++) {
                    if (mTaskEquipmentBean.get(i).getTaskEquipmentId() == roomDb.getTakePhotoPosition()) {
                        takePhotoEquipmentId = mTaskEquipmentBean.get(i).getEquipment().getEquipmentId();
                        noteStr = "请拍一张" + mTaskEquipmentBean.get(i).getEquipment().getEquipmentName() + "的照片";
                        break;
                    }
                }
                equipmentName.setText(noteStr);
                equipmentTakePhotoIV = view.findViewById(R.id.iv_take_equipment_photo);
                dialogProgressBar = view.findViewById(R.id.progressBar);
                GlideUtils.ShowImage(getActivity(), roomDb.getPhotoUrl(), equipmentTakePhotoIV, R.drawable.photo_button);
                equipmentTakePhotoIV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (TextUtils.isEmpty(roomDb.getPhotoUrl())) {
                            photoFile = new File(Yw2Application.getInstance().imageCacheFile(), System.currentTimeMillis() + ".jpg");
                            ActivityUtils.startCameraToPhoto(ReportFragment.this, photoFile, ACTION_START_CAMERA);
                        } else {
                            ViewPagePhotoActivity.startActivity(getActivity(), new String[]{roomDb.getPhotoUrl()}, 0);
                        }
                    }
                });
                equipmentTakePhotoIV.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if (TextUtils.isEmpty(roomDb.getPhotoUrl())) {
                            return false;
                        }
                        new MaterialDialog.Builder(getActivity())
                                .items(R.array.choose_condition_2)
                                .itemsCallback(new MaterialDialog.ListCallback() {
                                    @Override
                                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                                        switch (position) {
                                            case 0://删除照片
                                                roomDb.setUploadPhotoUrl(null);
                                                roomDb.setPhotoUrl(null);
                                                Yw2Application.getInstance().getDaoSession().getRoomDbDao().insertOrReplaceInTx(roomDb);
                                                if (equipmentTakePhotoIV != null) {
                                                    GlideUtils.ShowImage(getActivity(), roomDb.getPhotoUrl(), equipmentTakePhotoIV, R.drawable.photo_button);
                                                }
                                                break;
                                            default://重新拍照
                                                roomDb.setUploadPhotoUrl(null);
                                                roomDb.setPhotoUrl(null);
                                                Yw2Application.getInstance().getDaoSession().getRoomDbDao().insertOrReplaceInTx(roomDb);
                                                if (equipmentTakePhotoIV != null) {
                                                    GlideUtils.ShowImage(getActivity(), roomDb.getPhotoUrl(), equipmentTakePhotoIV, R.drawable.photo_button);
                                                }
                                                photoFile = new File(Yw2Application.getInstance().imageCacheFile(), System.currentTimeMillis() + ".jpg");
                                                ActivityUtils.startCameraToPhoto(ReportFragment.this, photoFile, ACTION_START_CAMERA);
                                                break;
                                        }
                                    }
                                })
                                .show();
                        return true;
                    }
                });
                view.findViewById(R.id.tv_sure).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (takePhotoDialog != null) {
                            if (!TextUtils.isEmpty(roomDb.getPhotoUrl())) {
                                if (isPhotoUpload) {
                                    getApp().showToast("照片正在上传中...");
                                    return;
                                }
                                if (mPresenter.checkPhotoNeedUpload(mTaskEquipmentBean) && !TextUtils.isEmpty(roomDb.getUploadPhotoUrl())) {
                                    //图片拍照了，而且上传了，或者没有拍照，走之前的逻辑
                                    uploadInspectionData();
                                } else {
                                    //有图片没有上传的，上传图片先
                                    showUploadLoading();
                                    mPresenter.uploadPhotoList(roomDb);
                                }
                                takePhotoDialog.dismiss();
                            } else {
                                getApp().showToast("请拍照!");
                            }
                        }
                    }
                });
                takePhotoDialog = new MaterialDialog.Builder(getActivity())
                        .customView(view, false)
                        .build();
                takePhotoDialog.show();
            }
        });
        if (mPresenter != null && mRoomListBean != null) {
            mPresenter.loadInspectionDataFromDb(roomDb.getTaskId(), mRoomListBean);
        }
    }

    private void uploadInspectionData() {
        if (mPresenter != null && takePhotoEquipmentId != -1) {
            mPresenter.uploadUserPhoto(roomDb.getTaskId()
                    , takePhotoEquipmentId
                    , roomDb.getUploadPhotoUrl());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        canUpload = false;
    }

    @Override
    public void setPresenter(ReportContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showData(@NonNull RoomListBean roomListBean) {
        if (mTaskEquipmentBean != null && showBean != null) {
            mTaskEquipmentBean.clear();
            mTaskEquipmentBean.addAll(roomListBean.getTaskEquipment());
            inspectionDetailBean.getRoomList().get(roomPosition).setTaskEquipment(mTaskEquipmentBean);
            showBean.clear();
            showBean.addAll(roomListBean.getTaskEquipment());
        }
        noDataLayout.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
        mRecyclerView.getAdapter().notifyDataSetChanged();
        onDataChange();
    }

    @Override
    public void showUploadLoading() {
        showProgressDialog("上传中...");
    }

    @Override
    public void hideUploadLoading() {
        hideProgressDialog();
    }

    @Override
    public void uploadError() {
        getApp().showToast("上传失败了");
    }

    @Override
    public void showUploadSuccess(boolean isAuto) {
        if (!isAuto) {
            getApp().showToast("上传成功");
            roomDb.setTaskState(ConstantInt.ROOM_STATE_3);
            getActivity().setResult(Activity.RESULT_OK);
            getActivity().finish();
        }
    }

    @Override
    public void noDataUpload() {
        getApp().showToast("存在漏检项,不能完成巡检");
    }

    @Override
    public void onResume() {
        super.onResume();
        canUpload = true;
        onDataChange();
    }

    private MyHandle myHandle;

    public static class MyHandle extends Handler {

        private final WeakReference<Activity> activity;

        MyHandle(WeakReference<Activity> activity) {
            this.activity = activity;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1 && activity != null && activity.get() != null) {
                Intent intent = new Intent();
                intent.setAction(BroadcastAction.AUTO_REFRESH_UI);
                activity.get().sendBroadcast(intent);
            }
        }
    }

    private class MyRunnable implements Runnable {

        @Override
        public void run() {
            while (autoUpload) {
                try {
                    Thread.sleep(ConstantInt.AUTO_UPLOAD_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (mPresenter != null && mPresenter.isUploading()
                        && canUpload && myHandle != null) {
                    myHandle.sendEmptyMessage(1);
                }
            }
        }
    }

    private UploadReceiver uploadReceiver;

    private class UploadReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (mPresenter != null && roomPosition != -1 && inspectionDetailBean != null) {
                mPresenter.uploadData(roomPosition, inspectionDetailBean, true);
            }
        }
    }

    public void onDataChange() {
        int checkCount = 0;
        for (int i = 0; i < mTaskEquipmentBean.size(); i++) {
            if (mTaskEquipmentBean.get(i).getEquipment().getEquipmentDb() == null) {
                return;
            }
            if (mTaskEquipmentBean.get(i).getEquipment().getEquipmentDb().getCanUpload()) {
                ++checkCount;
            }
        }
        String str = "开始巡检(" + checkCount + "/" + mRoomListBean.getTaskEquipment().size() + ")";
        mTitleTv.setText(str);
        roomDb.setCheckCount(checkCount);
        Yw2Application.getInstance().getDaoSession().getRoomDbDao().insertOrReplaceInTx(roomDb);
    }

    //图片正在上传
    private boolean isPhotoUpload;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ACTION_START_CAMERA && resultCode == Activity.RESULT_OK) {
            PhotoUtils.cropPhoto(getActivity(), photoFile, new PhotoUtils.PhotoListener() {
                @Override
                public void onSuccess(File file) {
                    if (mPresenter == null) {
                        return;
                    }
                    if (roomDb != null) {
                        roomDb.setPhotoUrl(file.getAbsolutePath());
                    }
                    if (equipmentTakePhotoIV != null) {
                        GlideUtils.ShowImage(getActivity(), file, equipmentTakePhotoIV, R.drawable.photo_button);
                    }
                    if (dialogProgressBar != null) {
                        dialogProgressBar.setVisibility(View.VISIBLE);
                    }
                    isPhotoUpload = true;
                    mPresenter.uploadRandomImage(roomDb);

                }
            });
        } else if (Activity.RESULT_OK == resultCode && requestCode == ACTION_START_INPUT) {
            if (showBean.size() > 0 && showBean.size() > editPosition) {
                TaskEquipmentBean taskEquipmentBean = mPresenter.getTaskEquipFromRepository();
                if (taskEquipmentBean != null) {
                    showBean.remove(editPosition);
                    showBean.add(editPosition, taskEquipmentBean);
                    for (int i = 0; i < mTaskEquipmentBean.size(); i++) {
                        if (taskEquipmentBean.getTaskEquipmentId() == mTaskEquipmentBean.get(i).getTaskEquipmentId()) {
                            mTaskEquipmentBean.remove(i);
                            mTaskEquipmentBean.add(i, taskEquipmentBean);
                            break;
                        }
                    }
                    mRecyclerView.getAdapter().notifyDataSetChanged();
                    onDataChange();
                }
            }
        }
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
    public void uploadRandomSuccess() {
        if (dialogProgressBar != null) {
            dialogProgressBar.setVisibility(View.GONE);
        }
        isPhotoUpload = false;
    }

    @Override
    public void uploadRandomFail() {
        if (dialogProgressBar != null) {
            dialogProgressBar.setVisibility(View.GONE);
        }
        isPhotoUpload = false;
    }

    @Override
    public void uploadUserPhotoSuccess() {
        if (mPresenter != null) {
            mPresenter.uploadData(roomPosition, inspectionDetailBean, false);
        }
    }

    @Override
    public void uploadUserPhotoFail() {
        roomDb.setPhotoUrl(null);
        roomDb.setUploadPhotoUrl(null);
        Yw2Application.getInstance().getDaoSession().getRoomDbDao().insertOrReplace(roomDb);
        getApp().showToast("上传图片失败了!");
    }

    @Override
    public void uploadOfflinePhotoFinish() {
        uploadInspectionData();
    }

    @Override
    public void uploadOfflinePhotoFail() {
        getApp().showToast("上传图片失败了!");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("editPosition", editPosition);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        autoUpload = false;
        try {
            if (myHandle != null) {
                myHandle.removeCallbacksAndMessages(null);
            }
            if (uploadReceiver != null) {
                getActivity().unregisterReceiver(uploadReceiver);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
