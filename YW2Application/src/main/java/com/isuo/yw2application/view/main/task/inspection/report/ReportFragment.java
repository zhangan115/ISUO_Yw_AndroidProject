package com.isuo.yw2application.view.main.task.inspection.report;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
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

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.iflytek.cloud.thirdparty.S;
import com.isuo.yw2application.R;
import com.isuo.yw2application.app.Yw2Application;
import com.isuo.yw2application.common.ConstantInt;
import com.isuo.yw2application.common.ConstantStr;
import com.isuo.yw2application.mode.bean.User;
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
import com.qw.soul.permission.SoulPermission;
import com.qw.soul.permission.bean.Permission;
import com.qw.soul.permission.bean.Permissions;
import com.qw.soul.permission.callbcak.CheckRequestPermissionsListener;
import com.sito.library.adapter.RVAdapter;
import com.sito.library.utils.ActivityUtils;
import com.sito.library.utils.GlideUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;


/**
 * 巡检提交数据
 * Created by zhangan on 2017-06-26.
 */

public class ReportFragment extends MvpFragment<ReportContract.Presenter> implements ReportContract.View {
    //data
    private RoomDb roomDb;
    private InspectionDetailBean mInspectionDetailBean;
    private RoomListBean mRoomListBean;
    private ArrayList<TaskEquipmentBean> mTaskEquipmentBean;
    private ArrayList<TaskEquipmentBean> showBean;
    private int roomPosition = -1, editPosition;
    private long taskId;
    private boolean canUpload = false;
    private static final int ACTION_START_INPUT = 101;
    private boolean caEdit = true;
    //view
    private TextView bottomView;
    private TextView mTitleTv;
    private RecyclerView mRecyclerView;
    private RelativeLayout noDataLayout;


    public static ReportFragment newInstance(long taskId, int position) {
        Bundle args = new Bundle();
        args.putLong(ConstantStr.KEY_BUNDLE_LONG, taskId);
        args.putInt(ConstantStr.KEY_BUNDLE_INT, position);
        ReportFragment fragment = new ReportFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new ReportPresenter(InspectionRepository.getRepository(getActivity()), this);

        mTaskEquipmentBean = new ArrayList<>();
        showBean = new ArrayList<>();
        if (savedInstanceState != null) {
            editPosition = savedInstanceState.getInt("editPosition");
        }
        Bundle bundle = getArguments();
        if (bundle != null) {
            roomPosition = bundle.getInt(ConstantStr.KEY_BUNDLE_INT);
            taskId = bundle.getLong(ConstantStr.KEY_BUNDLE_LONG);
        }
        mInspectionDetailBean = mPresenter.getInspectionData(taskId);
        if (mInspectionDetailBean != null) {
            mRoomListBean = mInspectionDetailBean.getRoomList().get(roomPosition);
            roomDb = mRoomListBean.getRoomDb();
            caEdit = mInspectionDetailBean.getTaskState() != ConstantInt.TASK_STATE_4;
            for (int i = 0; i < mRoomListBean.getTaskEquipment().size(); i++) {
                if (mRoomListBean.getTaskEquipment().get(i).getEquipment().getEquipmentId() == roomDb.getTakePhotoPosition()) {
                    takePhotoEquipmentBean = mRoomListBean.getTaskEquipment().get(i);
                    break;
                }
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
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
                ImageView haveDataIv = (ImageView) vHolder.getView(R.id.haveDataIv);
                View view_division = vHolder.getView(R.id.view_division);
                String str = position + 1 + ".  " + data.getEquipment().getEquipmentName().replace("\n", "");
                tv_equipment_name.setText(str);
                if (data.getEquipment().getEquipmentDb().getAlarmState()) {
                    iv_state.setVisibility(View.VISIBLE);
                } else {
                    iv_state.setVisibility(View.GONE);
                }
                if (mPresenter.getEquipmentFinishState(mInspectionDetailBean.getTaskId(), mRoomListBean.getTaskRoomId(), data.getTaskEquipmentId())) {
                    tv_equipment_state.setVisibility(View.VISIBLE);
                    haveDataIv.setVisibility(View.GONE);
                } else {
                    tv_equipment_state.setVisibility(View.GONE);
                    long count = mPresenter.getEquipmentDataFinishCount(mInspectionDetailBean.getTaskId(), mRoomListBean.getTaskRoomId(), data.getTaskEquipmentId());
                    if (count > 0) {
                        haveDataIv.setVisibility(View.VISIBLE);
                    } else {
                        haveDataIv.setVisibility(View.GONE);
                    }
                }
                if (position == showBean.size() - 1) {
                    view_division.setVisibility(View.GONE);
                } else {
                    view_division.setVisibility(View.VISIBLE);
                }
            }
        };
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener((view, position) -> {
            editPosition = position;
            Intent intent = new Intent(getActivity(), InputActivity.class);
            mPresenter.saveEditTaskEquipToCache(showBean.get(position));
            intent.putExtra(ConstantStr.KEY_BUNDLE_BOOLEAN, caEdit);
            intent.putExtra(ConstantStr.KEY_BUNDLE_LONG, roomDb.getTaskId());
            intent.putExtra(ConstantStr.KEY_BUNDLE_LONG_1, roomDb.get_id());
            intent.putExtra(ConstantStr.KEY_BUNDLE_INT, roomPosition);
            Yw2Application.getInstance().hideSoftKeyBoard(getActivity());
            startActivityForResult(intent, ACTION_START_INPUT);
        });
        bottomView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!canUpload) {
                    Yw2Application.getInstance().showToast("有设备没有完成巡检");
                    return;
                }
                if (TextUtils.isEmpty(roomDb.getPhotoUrl())) {
                    toUploadEquipmentPhoto();
                } else {
                    showUploadNoteDialog("是否上传巡检数据?");
                }
            }
        });
        if (caEdit) {
            rootView.findViewById(R.id.ib_increment).setVisibility(View.VISIBLE);
            bottomView.setVisibility(View.VISIBLE);
        }
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
        if (mRecyclerView.getAdapter() != null) {
            mRecyclerView.getAdapter().notifyDataSetChanged();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mPresenter != null && mRoomListBean != null) {
            mPresenter.loadInspectionDataFromDb(roomDb.getTaskId(), mRoomListBean);
        }
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
            mInspectionDetailBean.getRoomList().get(roomPosition).setTaskEquipment(mTaskEquipmentBean);
            showBean.clear();
            showBean.addAll(roomListBean.getTaskEquipment());
        }
        noDataLayout.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
        if (mRecyclerView.getAdapter() != null) {
            mRecyclerView.getAdapter().notifyDataSetChanged();
        }
        onDataChange();
    }

    private MaterialDialog takePhotoDialog;
    private ImageView equipmentTakePhotoIV;
    TaskEquipmentBean takePhotoEquipmentBean;
    private File photoFile;
    private static final int ACTION_START_EQUIPMENT = 102;
    LocalBroadcastManager manager;

    /******* 随机生成的需要拍照的设备******/
    private void toUploadEquipmentPhoto() {
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_take_photo, null);
        view.findViewById(R.id.tv_cancel).setOnClickListener(v -> {
            if (takePhotoDialog != null) {
                takePhotoDialog.dismiss();
            }
        });
        TextView equipmentName = view.findViewById(R.id.tv_equipment_name);
        String noteStr = "请拍一张" + takePhotoEquipmentBean.getEquipment().getEquipmentName() + "【" + roomDb.getDataItemName() + "】的照片";
        equipmentName.setText(noteStr);
        equipmentTakePhotoIV = view.findViewById(R.id.iv_take_equipment_photo);
        GlideUtils.ShowImage(this, roomDb.getPhotoUrl(), equipmentTakePhotoIV, R.drawable.photograph);
        equipmentTakePhotoIV.setOnClickListener(v -> {
            Permissions permissions = Permissions.build(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA);
            SoulPermission.getInstance().checkAndRequestPermissions(permissions,
                    new CheckRequestPermissionsListener() {
                        @Override
                        public void onAllPermissionOk(Permission[] allPermissions) {
                            if (TextUtils.isEmpty(roomDb.getPhotoUrl())) {
                                photoFile = new File(Yw2Application.getInstance().imageCacheFile(), System.currentTimeMillis() + ".jpg");
                                ActivityUtils.startCameraToPhoto(ReportFragment.this, photoFile, ACTION_START_EQUIPMENT);
                            } else {
                                ViewPagePhotoActivity.startActivity(getActivity(), new String[]{roomDb.getPhotoUrl()}, 0);
                            }
                        }

                        @Override
                        public void onPermissionDenied(Permission[] refusedPermissions) {
                            new AppSettingsDialog.Builder(getActivity())
                                    .setRationale(getString(R.string.need_save_setting))
                                    .setTitle(getString(R.string.request_permissions))
                                    .setPositiveButton(getString(R.string.sure))
                                    .setNegativeButton(getString(R.string.cancel))
                                    .build()
                                    .show();
                        }
                    });
        });
        equipmentTakePhotoIV.setOnLongClickListener(v -> {
            if (TextUtils.isEmpty(roomDb.getPhotoUrl())) {
                return false;
            }
            Permissions permissions = Permissions.build(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA);
            SoulPermission.getInstance().checkAndRequestPermissions(permissions,
                    new CheckRequestPermissionsListener() {
                        @Override
                        public void onAllPermissionOk(Permission[] allPermissions) {
                            new MaterialDialog.Builder(getActivity())
                                    .items(R.array.choose_condition_2)
                                    .itemsCallback(new MaterialDialog.ListCallback() {
                                        @Override
                                        public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                                            //重新拍照
                                            if (position == 0) {//删除照片
                                                roomDb.setUploadPhotoUrl(null);
                                                roomDb.setPhotoUrl(null);
                                                Yw2Application.getInstance().getDaoSession().getRoomDbDao().insertOrReplaceInTx(roomDb);
                                                if (equipmentTakePhotoIV != null) {
                                                    GlideUtils.ShowImage(getActivity(), roomDb.getPhotoUrl(), equipmentTakePhotoIV, R.drawable.photograph);
                                                }
                                            } else {
                                                roomDb.setUploadPhotoUrl(null);
                                                roomDb.setPhotoUrl(null);
                                                Yw2Application.getInstance().getDaoSession().getRoomDbDao().insertOrReplaceInTx(roomDb);
                                                if (equipmentTakePhotoIV != null) {
                                                    GlideUtils.ShowImage(getActivity(), roomDb.getPhotoUrl(), equipmentTakePhotoIV, R.drawable.photograph);
                                                }
                                                photoFile = new File(Yw2Application.getInstance().imageCacheFile(), System.currentTimeMillis() + ".jpg");
                                                ActivityUtils.startCameraToPhoto(ReportFragment.this, photoFile, ACTION_START_EQUIPMENT);
                                            }
                                        }
                                    })
                                    .show();
                        }

                        @Override
                        public void onPermissionDenied(Permission[] refusedPermissions) {
                            new AppSettingsDialog.Builder(getActivity())
                                    .setRationale(getString(R.string.need_save_setting))
                                    .setTitle(getString(R.string.request_permissions))
                                    .setPositiveButton(getString(R.string.sure))
                                    .setNegativeButton(getString(R.string.cancel))
                                    .build()
                                    .show();
                        }
                    });
            return true;
        });
        view.findViewById(R.id.tv_sure).setOnClickListener(v -> {
            if (takePhotoDialog != null) {
                if (TextUtils.isEmpty(roomDb.getPhotoUrl())) {
                    Yw2Application.getInstance().showToast("请拍照!");
                    return;
                }
                takePhotoDialog.dismiss();
                showUploadNoteDialog("是否上传巡检数据?");
            }
        });
        view.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                roomDb.setPhotoUrl(null);
                Yw2Application.getInstance().getDaoSession().getRoomDbDao().insertOrReplaceInTx(roomDb);
                takePhotoDialog.dismiss();
            }
        });
        takePhotoDialog = new MaterialDialog.Builder(getActivity())
                .customView(view, false)
                .build();
        takePhotoDialog.show();
    }

    private void showUploadNoteDialog(String note) {
        new MaterialDialog.Builder(getActivity())
                .title("提示").content(note)
                .negativeText("稍后")
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        //状态进行中，完成了任务
                        finishRoom();
                        getActivity().finish();
                    }
                })
                .positiveText("确定")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        mPresenter.uploadTaskData(mInspectionDetailBean, mRoomListBean);
                    }
                }).build().show();
    }

    private void finishRoom() {
        roomDb.setTaskState(ConstantInt.ROOM_STATE_3);
        roomDb.setEndTime(System.currentTimeMillis());
        Yw2Application.getInstance().getDaoSession().getRoomDbDao().insertOrReplaceInTx(roomDb);
        Intent intent = new Intent(ConstantStr.ROOM_STATE_CHANGE);
        intent.putExtra(ConstantStr.KEY_BUNDLE_LONG, roomDb.getTaskRoomId());
        intent.putExtra(ConstantStr.KEY_BUNDLE_INT, 2);
        if (manager != null) {
            manager.sendBroadcast(intent);
        }
    }

    @Override
    public void showUploadLoading() {
        showProgressDialog("数据上传中...");
    }

    @Override
    public void hideUploadLoading() {
        hideProgressDialog();
    }

    @Override
    public void uploadDataError() {
        showUploadNoteDialog("数据上传失败了，是否重新上传？");
    }

    @Override
    public void uploadDataSuccess(List<User> users) {
        finishRoom();
        if (users != null && !users.isEmpty()) {
            //完成了任务巡检
            Intent intent = new Intent();
            intent.putExtra(ConstantStr.KEY_BUNDLE_LONG, this.taskId);
            intent.putParcelableArrayListExtra(ConstantStr.KEY_BUNDLE_LIST_1, new ArrayList<>(users));
            intent.setAction(ConstantStr.TASK_STATE_FINISH);
            manager.sendBroadcast(intent);
        }
        getActivity().finish();
    }

    public void onDataChange() {
        int checkCount = mPresenter.getEquipmentFinishCount(this.mInspectionDetailBean.getTaskId(), mRoomListBean);
        roomDb.setCheckCount(checkCount);
        String str = "开始巡检(" + checkCount + "/" + mRoomListBean.getTaskEquipment().size() + ")";
        canUpload = checkCount == mRoomListBean.getTaskEquipment().size();
        mTitleTv.setText(str);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() != null) {
            manager = LocalBroadcastManager.getInstance(getActivity());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Activity.RESULT_OK == resultCode && requestCode == ACTION_START_INPUT) {
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
                    if (mRecyclerView.getAdapter() != null) {
                        mRecyclerView.getAdapter().notifyDataSetChanged();
                    }
                    onDataChange();
                }
            }
            if (roomDb.getTaskState() == ConstantInt.ROOM_STATE_3 && roomDb.getCheckCount() != mRoomListBean.getTaskEquipment().size()) {
                //状态已经完成，未完成任务
                roomDb.setTaskState(ConstantInt.ROOM_STATE_2);
                roomDb.setEndTime(0);
                Yw2Application.getInstance().getDaoSession().getRoomDbDao().insertOrReplaceInTx(roomDb);
                Intent intent = new Intent(ConstantStr.ROOM_STATE_CHANGE);
                intent.putExtra(ConstantStr.KEY_BUNDLE_LONG, roomDb.getTaskRoomId());
                intent.putExtra(ConstantStr.KEY_BUNDLE_INT, 1);
                if (manager != null) {
                    manager.sendBroadcast(intent);
                }
            }
        } else if (requestCode == ACTION_START_EQUIPMENT && resultCode == Activity.RESULT_OK) {
            String mark = mInspectionDetailBean.getTaskName();
            mark = mark + "\n" + takePhotoEquipmentBean.getEquipment().getEquipmentName();
            if (!TextUtils.isEmpty(takePhotoEquipmentBean.getEquipment().getEquipmentFsn())) {
                mark = mark + " " + takePhotoEquipmentBean.getEquipment().getEquipmentFsn();
            }
            if (roomDb != null && !TextUtils.isEmpty(roomDb.getDataItemName())) {
                mark = mark + "\n" + roomDb.getDataItemName();
            }
            PhotoUtils.cropPhoto(getActivity(), photoFile, mark, file -> {
                if (mPresenter == null) {
                    return;
                }
                if (roomDb != null) {
                    roomDb.setPhotoUrl(file.getAbsolutePath());
                }
                if (equipmentTakePhotoIV != null) {
                    GlideUtils.ShowImage(getActivity(), file, equipmentTakePhotoIV, R.drawable.photograph);
                }
            });
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("editPosition", editPosition);
    }

}
