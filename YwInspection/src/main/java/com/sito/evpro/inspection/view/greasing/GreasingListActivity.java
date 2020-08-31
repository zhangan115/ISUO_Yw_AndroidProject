package com.sito.evpro.inspection.view.greasing;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.sito.evpro.inspection.R;
import com.sito.evpro.inspection.app.InspectionApp;
import com.sito.evpro.inspection.mode.bean.greasing.InjectEquipment;
import com.sito.evpro.inspection.mode.bean.greasing.InjectRoomBean;
import com.sito.evpro.inspection.view.BaseActivity;
import com.sito.evpro.inspection.widget.InjectionView;
import com.sito.library.adapter.RVAdapter;
import com.sito.library.utils.DataUtil;
import com.sito.library.widget.ExpendRecycleView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class GreasingListActivity extends BaseActivity implements View.OnClickListener, GreasingListContract.View {
    //view
    private RelativeLayout mNoDataLayout;
    private ExpendRecycleView mExpendRecycleView;
    private LinearLayout ll_choose_local;
    private TextView mStation;
    private EditText mSearchEditText;
    private ImageView needInjectIB;
    private ImageView cleanEditTv;
    //data
    private List<String> roomList;
    private boolean needInject;
    private GreasingListContract.Presenter mPresenter;

    @Inject
    public GreasingListPresenter presenter;
    private List<InjectRoomBean> mRoomBeanList;
    private InjectRoomBean currentRoomBean;
    private List<InjectEquipment> mEquipment;
    private List<InjectEquipment> mAllEquipment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutAndToolbar(R.layout.activity_greasing_list, "注油管理");
        DaggerGreasingListComponent.builder().inspectionRepositoryComponent(InspectionApp.getInstance().getRepositoryComponent())
                .greasingListModule(new GreasingListModule(this))
                .build().inject(this);
        initView();
        initData();
        mPresenter.getRoomList();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slideinleft, R.anim.slideoutright);
    }


    private void initData() {
        roomList = new ArrayList<>();
        mRoomBeanList = new ArrayList<>();
        mEquipment = new ArrayList<>();
        mAllEquipment = new ArrayList<>();
        RVAdapter<InjectEquipment> adapter = new RVAdapter<InjectEquipment>(mExpendRecycleView, mEquipment, R.layout.item_greas_list) {
            @Override
            public void showData(ViewHolder vHolder, InjectEquipment data, int position) {
                TextView tv_days = (TextView) vHolder.getView(R.id.tv_days);
                TextView mId = (TextView) vHolder.getView(R.id.id_greas_item_id);
                if (TextUtils.isEmpty(data.getEquipmentSn())) {
                    mId.setText(data.getEquipmentName());
                } else {
                    mId.setText(data.getEquipmentSn());
                }
                ImageView mImg = (ImageView) vHolder.getView(R.id.id_greas_item_img);
                ImageView iv_oil = (ImageView) vHolder.getView(R.id.iv_oil);
                String days = "";
                if (data.isInject()) {
                    days = "";
                    iv_oil.setVisibility(View.VISIBLE);
                    mImg.setImageDrawable(findDrawById(R.drawable.view_injection));
                    AnimationDrawable animationDrawable = (AnimationDrawable) mImg.getDrawable();
                    animationDrawable.start();
                } else {
                    mImg.clearAnimation();
                    if (data.getInjectionOil() == null) {
                        days = "";
                        mImg.setImageDrawable(findDrawById(R.drawable.work_oil_red));
                        iv_oil.setVisibility(View.VISIBLE);
                    } else {
                        try {
                            long aa = DataUtil.getDistanceDays(DataUtil.timeFormat(data.getInjectionOil().getCreateTime(), null)
                                    , DataUtil.timeFormat(System.currentTimeMillis(), null));
                            days = String.valueOf(data.getCycle() - aa) + "天";
                            if (data.getCycle() - aa <= 0) {
                                mImg.setImageDrawable(findDrawById(R.drawable.work_oil_red));
                                iv_oil.setVisibility(View.VISIBLE);
                            } else {
                                mImg.setImageDrawable(findDrawById(R.drawable.work_oil_green));
                                iv_oil.setVisibility(View.GONE);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            mImg.setImageDrawable(findDrawById(R.drawable.work_oil_red));
                            iv_oil.setVisibility(View.VISIBLE);
                        }
                    }
                }
                tv_days.setText(days);
            }
        };
        mExpendRecycleView.setAdapter(adapter);
        adapter.setOnItemClickListener(new RVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                createDialog(position).show();
            }
        });
    }

    private void initView() {
        mExpendRecycleView = (ExpendRecycleView) findViewById(R.id.recycleViewId);
        mNoDataLayout = (RelativeLayout) findViewById(R.id.layout_no_data);
        mExpendRecycleView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 4));
        mStation = (TextView) findViewById(R.id.id_greasing_station);
        ll_choose_local = (LinearLayout) findViewById(R.id.ll_choose_local);
        cleanEditTv = (ImageView) findViewById(R.id.iv_clean_edit);
        cleanEditTv.setOnClickListener(this);
        ll_choose_local.setOnClickListener(this);
        findViewById(R.id.ll_choose_type).setOnClickListener(this);
        needInjectIB = (ImageView) findViewById(R.id.iv_choose_type_icon);
        mSearchEditText = (EditText) findViewById(R.id.edit_search);
        mSearchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (!TextUtils.isEmpty(mSearchEditText.getText().toString())) {
                        mPresenter.searchEquipment(mAllEquipment, mSearchEditText.getText().toString());
                        return true;
                    }
                }
                return false;
            }
        });
        mSearchEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mPresenter.searchEquipment(mAllEquipment, s.toString());
                if (s.length() > 0) {
                    cleanEditTv.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_choose_local:
                new MaterialDialog.Builder(GreasingListActivity.this)
                        .items(roomList)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                                mStation.setText(roomList.get(position));
                                currentRoomBean = mRoomBeanList.get(position);
                                mPresenter.getRoomEquipmentList(currentRoomBean.getRoomId());
                            }
                        })
                        .show();
                break;
            case R.id.ll_choose_type:
                needInject = !needInject;
                if (needInject) {
                    needInjectIB.setBackground(findDrawById(R.drawable.choose_select));
                    mPresenter.getNeedInjectEqu(mAllEquipment);
                } else {
                    needInjectIB.setBackground(findDrawById(R.drawable.choose_normal));
                    showNeedInjectEqu(mAllEquipment);
                }
                break;
            case R.id.iv_clean_edit:
                mSearchEditText.setText("");
                mSearchEditText.setSelection(0);
                break;
        }
    }

    @Override
    public void setPresenter(GreasingListContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void noData() {
        mEquipment.clear();
        mExpendRecycleView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void showRoomList(List<InjectRoomBean> roomBeanList) {
        ll_choose_local.setVisibility(View.VISIBLE);
        this.mRoomBeanList = roomBeanList;
        for (int i = 0; i < roomBeanList.size(); i++) {
            roomList.add(roomBeanList.get(i).getRoomName());
        }
        mStation.setText(roomBeanList.get(0).getRoomName());
        currentRoomBean = roomBeanList.get(0);
        mPresenter.getRoomEquipmentList(currentRoomBean.getRoomId());
    }

    @Override
    public void getRoomListError() {
        ll_choose_local.setVisibility(View.GONE);
    }

    @Override
    public void showRoomEquipment(List<InjectEquipment> injectEquipments) {
        mAllEquipment = injectEquipments;
        mEquipment.clear();
        mEquipment.addAll(injectEquipments);
        mExpendRecycleView.getAdapter().notifyDataSetChanged();
        if (needInject) {
            mPresenter.getNeedInjectEqu(mAllEquipment);
        }
    }

    @Override
    public void getEquipmentError() {

    }

    @Override
    public void injectSuccess(int position) {
        mPresenter.getRoomEquipmentList(currentRoomBean.getRoomId());
    }

    @Override
    public void injectFail(int position) {
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
        mEquipment.get(position).setInject(false);
        mExpendRecycleView.getAdapter().notifyItemChanged(position);
        InspectionApp.getInstance().showToast("提交失败");
    }

    @Override
    public void showNeedInjectEqu(List<InjectEquipment> injectEquipments) {
        mEquipment.clear();
        mEquipment.addAll(injectEquipments);
        mExpendRecycleView.getAdapter().notifyDataSetChanged();
    }

    private AlertDialog alertDialog;

    private AlertDialog createDialog(int position) {
        InjectionView injectionView = new InjectionView(GreasingListActivity.this, mEquipment.get(position), position) {
            @Override
            public void injectEquipment(int position, Integer cycle) {
                mEquipment.get(position).setInject(true);
                mExpendRecycleView.getAdapter().notifyItemChanged(position);
                mPresenter.injectionEquipment(mEquipment.get(position).getEquipmentId(), cycle, position);
            }

            @Override
            public void cancel() {
                if (alertDialog != null) {
                    alertDialog.dismiss();
                }
            }
        };
        alertDialog = new AlertDialog.Builder(this, R.style.dialog)
                .setView(injectionView)
                .create();
        return alertDialog;
    }

    @Override
    protected void onPause() {
        super.onPause();
        InspectionApp.getInstance().hideSoftKeyBoard(this);
        if (mPresenter != null) {
            mPresenter.unSubscribe();
        }
    }

}
