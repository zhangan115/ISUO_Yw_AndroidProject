package com.isuo.yw2application.view.main.device.info;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.isuo.yw2application.R;
import com.isuo.yw2application.common.ConstantStr;
import com.isuo.yw2application.mode.bean.create.ChooseRoomOrType;
import com.isuo.yw2application.mode.create.CreateRepository;
import com.isuo.yw2application.view.base.MvpFragment;
import com.sito.library.adapter.RVAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 选择设备区域，设备类型
 * Created by zhangan on 2017/10/9.
 */

public class CreateEquipInfoFragment extends MvpFragment<CreateEquipInfoContract.Presenter> implements CreateEquipInfoContract.View, View.OnClickListener {

    private RecyclerView mRecyclerView;
    private EditText edit_content;
    private int chooseType;
    private List<ChooseRoomOrType> datas;

    private ChooseRoomOrType deleteBean;
    private int deletePosition;

    public static CreateEquipInfoFragment newInstance(int chooseType) {
        Bundle args = new Bundle();
        args.putInt(ConstantStr.KEY_BUNDLE_INT, chooseType);
        CreateEquipInfoFragment fragment = new CreateEquipInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new CreateEquipInfoPresenter(CreateRepository.getRepository(), this);
        chooseType = getArguments().getInt(ConstantStr.KEY_BUNDLE_INT);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_equip_info, container, false);
        mRecyclerView = rootView.findViewById(R.id.recycleViewId);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        edit_content = rootView.findViewById(R.id.edit_content);
        rootView.findViewById(R.id.tv_add).setOnClickListener(this);
        if (chooseType == 0) {
            edit_content.setHint("添加属地名称");
        } else {
            edit_content.setHint("添加设备类型名称");
        }
        datas = new ArrayList<>();
        RVAdapter<ChooseRoomOrType> adapter = new RVAdapter<ChooseRoomOrType>(mRecyclerView, datas, R.layout.item_choose_room_type) {
            @Override
            public void showData(ViewHolder vHolder, ChooseRoomOrType data, int position) {
                TextView name = (TextView) vHolder.getView(R.id.tv_name);
                name.setText(data.getName());
            }
        };
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new RVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent();
                intent.putExtra(ConstantStr.KEY_BUNDLE_LONG, datas.get(position).getId());
                intent.putExtra(ConstantStr.KEY_BUNDLE_STR, datas.get(position).getName());
                getActivity().setResult(Activity.RESULT_OK, intent);
                getActivity().finish();
            }
        });
        if (chooseType == 0) {
            if (mPresenter != null)
                mPresenter.getRoomList();
        } else {
            if (mPresenter != null)
                mPresenter.getEquipmentList();
        }
        adapter.setOnItemLongListener(new RVAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, final int position) {
                String note;
                if (chooseType == 0) {
                    note = "是否删除当前属地?";
                } else {
                    note = "是否删除当前设备类型?";
                }
                new MaterialDialog.Builder(getActivity())
                        .content(note)
                        .negativeText("取消")
                        .positiveText("确定")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                deleteBean = datas.get(position);
                                deletePosition = position;
                                if (chooseType == 0) {
                                    if (mPresenter != null) {
                                        mPresenter.deleteRoom(datas.get(position).getId());
                                    }
                                } else {
                                    if (mPresenter != null) {
                                        mPresenter.deleteEquipmentType(datas.get(position).getId());
                                    }
                                }
                            }
                        })
                        .show();
            }
        });
        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_add:
                if (!TextUtils.isEmpty(edit_content.getText().toString())) {
                    if (chooseType == 0) {
                        new MaterialDialog.Builder(getActivity())
                                .items(R.array.room_type)
                                .itemsCallback(new MaterialDialog.ListCallback() {
                                    @Override
                                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                                        if (mPresenter != null) {
                                            mPresenter.addRoom(position, edit_content.getText().toString());
                                        }
                                    }
                                })
                                .show();
                    } else {
                        if (mPresenter != null) {
                            mPresenter.addEquipmentType(edit_content.getText().toString());
                        }
                    }
                }
                break;
        }
    }

    @Override
    public void setPresenter(CreateEquipInfoContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showRoomOrTypeList(List<ChooseRoomOrType> chooseRoomOrTypes) {
        edit_content.setText("");
        datas.clear();
        datas.addAll(chooseRoomOrTypes);
        mRecyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void addRoomOrTypeSuccess() {
        if (chooseType == 0) {
            if (mPresenter != null)
                mPresenter.getRoomList();
        } else {
            if (mPresenter != null)
                mPresenter.getEquipmentList();
        }
    }

    @Override
    public void showMessage(String message) {
        if (!TextUtils.isEmpty(message)) {
            getApp().showToast(message);
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
    public void deleteRoomSuccess() {
        datas.remove(deleteBean);
        mRecyclerView.getAdapter().notifyItemRemoved(deletePosition);
        if (deletePosition != datas.size()) { // 如果移除的是最后一个，忽略
            mRecyclerView.getAdapter().notifyItemRangeChanged(deletePosition, datas.size() - deletePosition);
        }
    }

    @Override
    public void deleteEquipmentTypeSuccess() {
        datas.remove(deleteBean);
        mRecyclerView.getAdapter().notifyItemRemoved(deletePosition);
        if (deletePosition != datas.size()) { // 如果移除的是最后一个，忽略
            mRecyclerView.getAdapter().notifyItemRangeChanged(deletePosition, datas.size() - deletePosition);
        }
    }
}
