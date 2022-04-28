package com.isuo.yw2application.view.main.device.info;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.isuo.yw2application.R;
import com.isuo.yw2application.common.ConstantStr;
import com.isuo.yw2application.mode.bean.create.ChooseRoomOrType;
import com.isuo.yw2application.mode.create.CreateRepository;
import com.isuo.yw2application.view.base.MvpFragment;
import com.sito.library.adapter.RVAdapter;
import com.sito.library.widget.PinnedHeaderExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 选择对象区域，对象类型
 * Created by zhangan on 2017/10/9.
 */

public class CreateEquipInfoFragment extends MvpFragment<CreateEquipInfoContract.Presenter> implements CreateEquipInfoContract.View, View.OnClickListener {

    private RecyclerView mRecyclerView;
    private EditText edit_content;
    private int chooseType;
    private List<ChooseRoomOrType> dataList;

    private ChooseRoomOrType deleteBean;
    private int deletePosition;
    private EquipmentTypeAdapter mEquipmentTypeAdapter;

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
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_equip_info, container, false);
        mRecyclerView = rootView.findViewById(R.id.recycleViewId);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        edit_content = rootView.findViewById(R.id.edit_content);
        rootView.findViewById(R.id.tv_add).setOnClickListener(this);
        dataList = new ArrayList<>();
        RVAdapter<ChooseRoomOrType> adapter = new RVAdapter<ChooseRoomOrType>(mRecyclerView, dataList, R.layout.item_choose_room_type) {
            @Override
            public void showData(ViewHolder vHolder, ChooseRoomOrType data, int position) {
                TextView name = (TextView) vHolder.getView(R.id.tv_name);
                name.setText(data.getName());
            }
        };
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener((view, position) -> {
            Intent intent = new Intent();
            intent.putExtra(ConstantStr.KEY_BUNDLE_LONG, dataList.get(position).getId());
            intent.putExtra(ConstantStr.KEY_BUNDLE_STR, dataList.get(position).getName());
            getActivity().setResult(Activity.RESULT_OK, intent);
            getActivity().finish();
        });
        PinnedHeaderExpandableListView listView = rootView.findViewById(R.id.listView);
        mEquipmentTypeAdapter = new EquipmentTypeAdapter(getActivity(), listView, R.layout.equipment_level_1, R.layout.equipment_level_2);
        listView.setAdapter(mEquipmentTypeAdapter);
        mEquipmentTypeAdapter.setDeleteCallback(chooseRoomOrType -> new MaterialDialog.Builder(getActivity())
                .content("是否删除当前的对象类型")
                .negativeText("取消")
                .positiveText("确定")
                .onPositive((dialog, which) -> {
                    if (mPresenter != null) {
                        mPresenter.deleteEquipmentType(chooseRoomOrType.getId());
                    }
                })
                .show());
        if (chooseType == 0) {
            edit_content.setHint("添加属地名称");
            if (mPresenter != null) {
                mPresenter.getRoomList();
            }
        } else {
            edit_content.setHint("添加对象类型名称");
            mRecyclerView.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            if (mPresenter != null) {
                mPresenter.getEquipmentList();
            }
        }
        adapter.setOnItemLongListener((view, position) -> {
            String note;
            if (chooseType == 0) {
                note = "是否删除当前属地?";
            } else {
                note = "是否删除当前对象类型?";
            }
            new MaterialDialog.Builder(getActivity())
                    .content(note)
                    .negativeText("取消")
                    .positiveText("确定")
                    .onPositive((dialog, which) -> {
                        deleteBean = dataList.get(position);
                        deletePosition = position;
                        if (chooseType == 0) {
                            if (mPresenter != null) {
                                mPresenter.deleteRoom(dataList.get(position).getId());
                            }
                        } else {
                            if (mPresenter != null) {
                                mPresenter.deleteEquipmentType(dataList.get(position).getId());
                            }
                        }
                    })
                    .show();
        });
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_sure, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_sure) {
            ChooseRoomOrType chooseRoomOrType = null;
            for (ChooseRoomOrType type1 : dataList) {
                if (type1.isSelect()) {
                    chooseRoomOrType = type1;
                    break;
                }
                if (type1.getChooseRoomOrTypeList() != null) {
                    for (ChooseRoomOrType type2 : type1.getChooseRoomOrTypeList()) {
                        if (type2.isSelect()) {
                            chooseRoomOrType = type2;
                            break;
                        }
                    }
                }
            }
            if (chooseRoomOrType != null) {
                Intent intent = new Intent();
                intent.putExtra(ConstantStr.KEY_BUNDLE_LONG, chooseRoomOrType.getId());
                intent.putExtra(ConstantStr.KEY_BUNDLE_STR, chooseRoomOrType.getName());
                getActivity().setResult(Activity.RESULT_OK, intent);
                getActivity().finish();
            } else {
                showMessage("请选中一个对象类型");
            }
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_add) {
            if (!TextUtils.isEmpty(edit_content.getText().toString())) {
                if (chooseType == 0) {
                    new MaterialDialog.Builder(getActivity())
                            .items(R.array.room_type)
                            .itemsCallback((dialog, itemView, position, text) -> {
                                if (mPresenter != null) {
                                    mPresenter.addRoom(position, edit_content.getText().toString());
                                }
                            })
                            .show();
                } else {
                    List<String> items = new ArrayList<>();
                    items.add("创建一级对象类型");
                    for (ChooseRoomOrType type : dataList) {
                        items.add(type.getName());
                    }
                    new MaterialDialog.Builder(getActivity())
                            .items(items)
                            .itemsCallback((dialog, itemView, position, text) -> {
                                if (position == 0) {
                                    if (mPresenter != null) {
                                        mPresenter.addEquipmentType(null, 1, edit_content.getText().toString());
                                    }
                                } else {
                                    if (mPresenter != null) {
                                        mPresenter.addEquipmentType(dataList.get(position - 1).getId(), 2, edit_content.getText().toString());
                                    }
                                }
                            })
                            .show();
                }
            }
        }
    }

    @Override
    public void setPresenter(CreateEquipInfoContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showRoomOrTypeList(List<ChooseRoomOrType> chooseRoomOrTypes) {
        edit_content.setText("");
        dataList.clear();
        if (chooseType == 0) {
            dataList.addAll(chooseRoomOrTypes);
            if (mRecyclerView.getAdapter() != null) {
                mRecyclerView.getAdapter().notifyDataSetChanged();
            }
        } else {
            Map<Long, List<ChooseRoomOrType>> map = new HashMap<>();
            List<ChooseRoomOrType> levelTypes = new ArrayList<>();
            for (ChooseRoomOrType type : chooseRoomOrTypes) {
                if (type.getLevel() == 1) {
                    levelTypes.add(type);
                } else if (type.getLevel() == 2) {
                    List<ChooseRoomOrType> levelTypes2;
                    if (!map.containsKey(type.getParentId())) {
                        map.put(type.getParentId(), new ArrayList<>());
                    }
                    levelTypes2 = map.get(type.getParentId());
                    if (levelTypes2 != null) {
                        levelTypes2.add(type);
                    }
                }
            }
            for (ChooseRoomOrType levelType : levelTypes) {
                if (map.containsKey(levelType.getId())) {
                    levelType.setChooseRoomOrTypeList(map.get(levelType.getId()));
                }
            }
            dataList.addAll(levelTypes);
            mEquipmentTypeAdapter.setDataList(dataList);
        }
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
        if (chooseType == 0) {
            dataList.remove(deleteBean);
            if (mRecyclerView.getAdapter() != null) {
                mRecyclerView.getAdapter().notifyItemRemoved(deletePosition);
            }
            if (deletePosition != dataList.size()) { // 如果移除的是最后一个，忽略
                mRecyclerView.getAdapter().notifyItemRangeChanged(deletePosition, dataList.size() - deletePosition);
            }
        }
    }

    @Override
    public void deleteEquipmentTypeSuccess() {
        if (mPresenter != null) {
            mPresenter.getEquipmentList();
        }
    }
}
