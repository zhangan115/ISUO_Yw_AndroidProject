package com.isuo.yw2application.view.main.task.inspection.report;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.isuo.yw2application.R;
import com.isuo.yw2application.app.Yw2Application;
import com.isuo.yw2application.common.ConstantInt;
import com.isuo.yw2application.common.ConstantStr;
import com.isuo.yw2application.mode.bean.db.EquipmentDataDb;
import com.isuo.yw2application.mode.bean.db.RoomDb;
import com.isuo.yw2application.mode.bean.inspection.InspectionDetailBean;
import com.isuo.yw2application.mode.bean.inspection.RoomListBean;
import com.isuo.yw2application.mode.bean.inspection.TaskEquipmentBean;
import com.isuo.yw2application.mode.inspection.InspectionRepository;
import com.isuo.yw2application.view.base.MvpFragment;
import com.isuo.yw2application.view.main.task.increment.submit.IncrementActivity;
import com.isuo.yw2application.view.main.task.inspection.input.InputActivity;
import com.sito.library.adapter.RVAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * 巡检提交数据
 * Created by zhangan on 2017-06-26.
 */

public class ReportFragment extends MvpFragment<ReportContract.Presenter> implements ReportContract.View {
    //data
    private RoomDb roomDb;
    private RoomListBean mRoomListBean;
    private InspectionDetailBean inspectionDetailBean;
    private ArrayList<TaskEquipmentBean> mTaskEquipmentBean;
    private ArrayList<TaskEquipmentBean> showBean;
    private int roomPosition = -1, editPosition;

    private static final int ACTION_START_INPUT = 101;
    private boolean caEdit = true;
    //view
    private TextView bottomView;
    private TextView mTitleTv;
    private RecyclerView mRecyclerView;
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
                ImageView haveDataIv = (ImageView)vHolder.getView(R.id.haveDataIv);
                View view_division = vHolder.getView(R.id.view_division);
                String str = position + 1 + ".  " + data.getEquipment().getEquipmentName().replace("\n", "");
                tv_equipment_name.setText(str);
                if (data.getEquipment().getEquipmentDb().getAlarmState()) {
                    iv_state.setVisibility(View.VISIBLE);
                } else {
                    iv_state.setVisibility(View.GONE);
                }
                long count = mPresenter.getEquipmentDataFinishCount(inspectionDetailBean.getTaskId(), mRoomListBean.getTaskRoomId(), data.getTaskEquipmentId());
                if (count == 0){
                    tv_equipment_state.setVisibility(View.GONE);
                    haveDataIv.setVisibility(View.GONE);
                }else if(count > 0 && count < data.getDataList().get(0).getDataItemValueList().size()){
                    tv_equipment_state.setVisibility(View.GONE);
                    haveDataIv.setVisibility(View.VISIBLE);
                }else{
                    tv_equipment_state.setVisibility(View.VISIBLE);
                    haveDataIv.setVisibility(View.GONE);
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
                intent.putExtra(ConstantStr.KEY_BUNDLE_LONG_1, roomDb.get_id());
                intent.putExtra(ConstantStr.KEY_BUNDLE_INT, roomPosition);
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
                } else {
                    new MaterialDialog.Builder(getActivity()).content("是否确认完成巡检？")
                            .negativeText("取消")
                            .positiveText("确定")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    roomDb.setTaskState(ConstantInt.ROOM_STATE_3);
                                    getActivity().setResult(Activity.RESULT_OK);
                                    getActivity().finish();
                                }
                            }).show();
                }
            }
        });
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
    public void onResume() {
        super.onResume();
        onDataChange();
    }

    public void onDataChange() {
        int checkCount = mPresenter.getEquipmentFinishCount(this.inspectionDetailBean.getTaskId(), mRoomListBean);
        roomDb.setCheckCount(checkCount);
        String str = "开始巡检(" + checkCount + "/" + mRoomListBean.getTaskEquipment().size() + ")";
        mTitleTv.setText(str);
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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("editPosition", editPosition);
    }

}
