package com.isuo.yw2application.view.main.device.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.isuo.yw2application.R;
import com.isuo.yw2application.app.Yw2Application;
import com.isuo.yw2application.common.ConstantStr;
import com.isuo.yw2application.mode.bean.equip.EquipRoom;
import com.isuo.yw2application.mode.bean.equip.EquipType;
import com.isuo.yw2application.view.base.BaseActivity;
import com.isuo.yw2application.view.main.device.list.EquipListActivity;
import com.sito.library.adapter.RVAdapter;
import com.sito.library.widget.ExpendRecycleView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class EquipSearchActivity extends BaseActivity implements View.OnClickListener, EquipSearchContract.View {
    //view
    private EditText mSearchName;
    private TextView mSearchTrans;
    private TextView mSearchSwitch;
    private TextView mSearchGis;
    private TextView mSearchBreak;

    private TextView mReset;
    private TextView mCommit;

    private TextView mType;
    private TextView mPlace;

    private boolean isExpPlace;
    private boolean isExpType;

    private ExpendRecycleView mTypeList;
    private ExpendRecycleView mPlaceList;

    //data
    private List<EquipType> mTypeDatas;
    private List<EquipRoom> mPlaceDatas;

    private List<EquipType> mTempType;
    private List<EquipRoom> mTempPlace;

    private int typePosition;
    private int placePosition;

    @Inject
    EquipSearchPresenter mEquipSearchPresenter;
    EquipSearchContract.Presenter mPresenter;

    private int mEquipTypeId;
    private int mEquipRoomId;
    private String mRoomName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutAndToolbar(R.layout.activity_equip_search, "设备列表");
        DaggerEquipSearchComponent.builder().customerRepositoryComponent(Yw2Application.getInstance().getRepositoryComponent())
                .equipSearchModule(new EquipSearchModule(this)).build()
                .inject(this);
        initView();
        initEvent();
        initData();
    }

    private void initData() {
        mTypeDatas = new ArrayList<>();
        mPlaceDatas = new ArrayList<>();
        mTempPlace = new ArrayList<>();
        mTempType = new ArrayList<>();
        RVAdapter<EquipType> mTypeAdapter = new RVAdapter<EquipType>(mTypeList, mTypeDatas, R.layout.item_equip_type) {
            @Override
            public void showData(ViewHolder vHolder, EquipType data, int position) {
                TextView mText = (TextView) vHolder.getView(R.id.id_equip_seach_name);
                mText.setText(data.getEquipmentTypeName());
                if (mTypeDatas.get(position).isSelect()) {
                    mText.setBackground(findDrawById(R.drawable.bg_equip_text_green));
                    mText.setTextColor(findColorById(R.color.equip_search_green));
                } else {
                    mText.setBackground(findDrawById(R.drawable.bg_equip_text_gray));
                    mText.setTextColor(findColorById(R.color.text333));
                }
            }
        };
        mTypeList.setAdapter(mTypeAdapter);
        mTypeAdapter.setOnItemClickListener(new RVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                typePosition = position;
                mEquipTypeId = mTypeDatas.get(position).getEquipmentTypeId();
                for (int i = 0; i < mTypeDatas.size(); i++) {
                    if (i == position) {
                        mTypeDatas.get(i).setSelect(true);
                    } else {
                        mTypeDatas.get(i).setSelect(false);
                    }
                }
                mTypeList.getAdapter().notifyDataSetChanged();
            }
        });

        RVAdapter<EquipRoom> mPlaceAdapter = new RVAdapter<EquipRoom>(mTypeList, mPlaceDatas, R.layout.item_equip_type) {
            @Override
            public void showData(ViewHolder vHolder, EquipRoom data, int position) {
                TextView mText = (TextView) vHolder.getView(R.id.id_equip_seach_name);
                mText.setText(data.getRoomName());
                if (mPlaceDatas.get(position).isSelect()) {
                    mText.setBackground(findDrawById(R.drawable.bg_equip_text_green));
                    mText.setTextColor(findColorById(R.color.equip_search_green));
                } else {
                    mText.setBackground(findDrawById(R.drawable.bg_equip_text_gray));
                    mText.setTextColor(findColorById(R.color.text333));
                }
            }
        };
        mPlaceList.setAdapter(mPlaceAdapter);
        mPlaceAdapter.setOnItemClickListener(new RVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                placePosition = position;
                mEquipRoomId = mPlaceDatas.get(position).getRoomId();
                mRoomName = mPlaceDatas.get(position).getRoomName();
                for (int i = 0; i < mPlaceDatas.size(); i++) {
                    if (i == position) {
                        mPlaceDatas.get(i).setSelect(true);
                    } else {
                        mPlaceDatas.get(i).setSelect(false);
                    }
                }
                mPlaceList.getAdapter().notifyDataSetChanged();
            }
        });
        mPresenter.getEquipType();
        mPresenter.getEquipPlace();
    }

    private void initEvent() {
        mSearchTrans.setOnClickListener(this);
        mSearchSwitch.setOnClickListener(this);
        mSearchGis.setOnClickListener(this);
        mSearchBreak.setOnClickListener(this);
        mCommit.setOnClickListener(this);
        mReset.setOnClickListener(this);
    }

    private void initView() {
        mSearchName = findViewById(R.id.id_equip_name);
        mSearchTrans = findViewById(R.id.id_equip_seach_trans);
        mSearchSwitch = findViewById(R.id.id_equip_seach_switch);
        mSearchGis = findViewById(R.id.id_equip_seach_gis);
        mSearchBreak = findViewById(R.id.id_equip_seach_break);
        mType = findViewById(R.id.id_equip_type);
        mPlace = findViewById(R.id.id_equip_place);

        mTypeList = findViewById(R.id.recycleView_type);
        mPlaceList = findViewById(R.id.recycleView_place);
        mTypeList.setLayoutManager(new GridLayoutManager(getApplicationContext(), 4));
        mPlaceList.setLayoutManager(new GridLayoutManager(getApplicationContext(), 4));

        mReset = findViewById(R.id.id_equip_reset);
        mCommit = findViewById(R.id.id_equip_commit);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_equip_reset:
                placePosition = 0;
                typePosition = 0;
                mSearchName.setText("");
                mTypeDatas.clear();
                for (EquipType type:mTempType){
                    type.setSelect(false);
                }
                for (EquipRoom room:mTempPlace){
                    room.setSelect(false);
                }
                mTypeDatas.addAll(mTempType);
                mPlaceDatas.clear();
                mPlaceDatas.addAll(mTempPlace);
                mPlaceList.getAdapter().notifyDataSetChanged();
                mTypeList.getAdapter().notifyDataSetChanged();
                mEquipTypeId = 0;
                mEquipRoomId = 0;
                mRoomName = "";
                break;
            case R.id.id_equip_commit:
                Intent intent = new Intent(this, EquipListActivity.class);
                intent.putExtra(ConstantStr.KEY_BUNDLE_INT_1, mEquipTypeId);
                intent.putExtra(ConstantStr.KEY_BUNDLE_INT_2, mEquipRoomId);
                intent.putExtra(ConstantStr.KEY_BUNDLE_BOOLEAN_1, true);
                intent.putExtra(ConstantStr.KEY_BUNDLE_BOOLEAN_3, true);
                intent.putExtra(ConstantStr.KEY_BUNDLE_STR, mSearchName.getText().toString());
                intent.putExtra(ConstantStr.KEY_BUNDLE_STR_1, mRoomName);
                startActivity(intent);
                break;
            case R.id.id_equip_place:
                placeClick();
                break;
            case R.id.id_equip_type:
                typeClick();
                break;
        }
    }

    private void typeClick() {
        if (!isExpType) {
            if (mTempType.size() > 4) {
                for (int i = 4; i < mTempType.size(); i++) {
                    if (typePosition == i) {
                        mTempType.get(i).setSelect(true);
                    }
                    mTypeDatas.add(mTempType.get(i));
                }
                mTypeList.getAdapter().notifyDataSetChanged();
            }
            isExpType = true;
        } else {
            if (mTempType.size() > 4) {
                mTypeDatas.clear();
                for (int i = 0; i < 4; i++) {
                    mTypeDatas.add(mTempType.get(i));
                }
                mTypeList.getAdapter().notifyDataSetChanged();
            }
            isExpType = false;
        }
    }

    private void placeClick() {
        if (!isExpPlace) {
            if (mTempPlace.size() > 4) {
                for (int i = 4; i < mTempPlace.size(); i++) {
                    if (placePosition == i) {
                        mTempPlace.get(i).setSelect(true);
                    }
                    mPlaceDatas.add(mTempPlace.get(i));
                }
                mPlaceList.getAdapter().notifyDataSetChanged();
            }
            isExpPlace = true;
        } else {
            if (mTempPlace.size() > 4) {
                mPlaceDatas.clear();
                for (int i = 0; i < 4; i++) {
                    mPlaceDatas.add(mTempPlace.get(i));
                }
                mPlaceList.getAdapter().notifyDataSetChanged();
            }
            isExpPlace = false;
        }
    }

    @Override
    public void showEquipType(List<EquipType> equipTypes) {
        mTempType.clear();
        mTempType.addAll(equipTypes);
        if (mTempType != null) {
            if (mTempType.size() > 4) {
                for (int i = 0; i < 4; i++) {
                    mTypeDatas.add(mTempType.get(i));
                }
            } else {
                mTypeDatas.addAll(mTempType);
            }
            mTypeList.getAdapter().notifyDataSetChanged();
        }
        typeClick();
    }

    @Override
    public void showEquipRoom(List<EquipRoom> equipRooms) {
        mTempPlace.clear();
        mTempPlace.addAll(equipRooms);
        if (mTempPlace != null) {
            if (mTempPlace.size() > 4) {
                for (int i = 0; i < 4; i++) {
                    mPlaceDatas.add(mTempPlace.get(i));
                }
            } else {
                mPlaceDatas.addAll(mTempPlace);
            }
            mPlaceList.getAdapter().notifyDataSetChanged();
        }
        placeClick();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void noDdata() {

    }

    @Override
    public void setPresenter(EquipSearchContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
