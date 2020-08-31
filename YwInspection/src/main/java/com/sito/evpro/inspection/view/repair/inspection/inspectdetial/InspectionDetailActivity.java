package com.sito.evpro.inspection.view.repair.inspection.inspectdetial;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sito.evpro.inspection.R;
import com.sito.evpro.inspection.app.InspectionApp;
import com.sito.evpro.inspection.common.ConstantStr;
import com.sito.evpro.inspection.mode.bean.fault.CheckBean;
import com.sito.evpro.inspection.mode.bean.fault.FaultList;
import com.sito.evpro.inspection.utils.CountDownTimerUtils;
import com.sito.evpro.inspection.view.BaseActivity;
import com.sito.evpro.inspection.view.repair.inspection.data.InspectionDataActivity;
import com.sito.evpro.inspection.view.speech.voice.MediaPlayerManager;
import com.sito.evpro.inspection.widget.ShowImageLayout;
import com.sito.library.adapter.RVAdapter;
import com.sito.library.utils.DataUtil;
import com.sito.library.widget.ExpendRecycleView;
import com.sito.library.widget.RecycleRefreshLoadLayout;
import com.sito.library.widget.TextViewVertical;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class InspectionDetailActivity extends BaseActivity implements InspectionDetailContract.View, SwipeRefreshLayout.OnRefreshListener, RecycleRefreshLoadLayout.OnLoadListener {
    //view
    private RelativeLayout mNoDataLayout;
    private ExpendRecycleView mExpendRecycleView;
    private RecycleRefreshLoadLayout mRecycleRefreshLoadLayout;
    private View mHeaderView;
    private TextView mInspectionPlace;
    private TextView mInspectionState;
    private TextView mInspectionNum;
    private TextView mInspectionReceive;
    private TextView mInspectionReceiveTime;
    private TextView mInspectionCarry;
    private TextView mInspectionCarryTime;
    private TextView mInspectionReport;
    private TextView mInspectionDeal;
    private TextView mInspectionCheck;
    private TextView mInspectionDo;
    private TextView mInspectionClose;
    private TextView tv_task_name;
    //data
    private List<FaultList> mList;
    private long mTaskId;
    private boolean isRefresh;
    private CountDownTimerUtils mCountDownTimerUtils;
    private AnimationDrawable animation;
    private boolean isPlay;
    @Inject
    InspectionDetailPresenter inspectionDetailPresenter;
    InspectionDetailContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutAndToolbar(R.layout.activity_inspec_detial, "巡检详情");
        DaggerInspectionDetailComponent.builder().inspectionRepositoryComponent(InspectionApp.getInstance().getRepositoryComponent())
                .inspectionDetailModule(new InspectionDetailModule(this)).build()
                .inject(this);
        mTaskId = getIntent().getLongExtra(ConstantStr.KEY_BUNDLE_LONG, 0);
        initView();
        initData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_equip, menu);
        MenuItem menuItem = menu.findItem(R.id.menu);
        menuItem.setTitle("巡检数据");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu) {
            Intent intent = new Intent(this, InspectionDataActivity.class);
            intent.putExtra(ConstantStr.KEY_BUNDLE_LONG, mTaskId);
            startActivity(intent);
        }
        return true;
    }

    private void initData() {
        mList = new ArrayList<>();
        RVAdapter<FaultList> adapter = new RVAdapter<FaultList>(mExpendRecycleView, mList, R.layout.item_fault_list) {
            @Override
            public void showData(ViewHolder vHolder, final FaultList data, final int position) {
                TextView mAddress = (TextView) vHolder.getView(R.id.id_equip_address);
                mAddress.setText(MessageFormat.format("属地:{0}", data.getEquipment().getRoom().getRoomName()));
                TextView mFaultState = (TextView) vHolder.getView(R.id.id_equip_state);
                mFaultState.setText(InspectionApp.getInstance().getMapOption().get("9").get(data.getFaultState() + ""));
                TextView mEquipName = (TextView) vHolder.getView(R.id.tv_equip_name);
                mEquipName.setText(data.getEquipment().getEquipmentName());
                TextView mUser = (TextView) vHolder.getView(R.id.tv_user);
                mUser.setText(data.getUser().getRealName());
                TextView mTime = (TextView) vHolder.getView(R.id.tv_start_time);
                mTime.setText(MessageFormat.format("上报时间:{0}", DataUtil.timeFormat(data.getCreateTime(), "yyyy-MM-dd HH:mm")));
                TextViewVertical mFault = (TextViewVertical) vHolder.getView(R.id.tv_alarm);
                ImageView mFaultImg = (ImageView) vHolder.getView(R.id.img_alarm);
                if (data.getFaultType() == 1) {
                    mFault.setText("A类故障");
                    mFaultImg.setImageDrawable(findDrawById(R.drawable.work_a));
                }
                if (data.getFaultType() == 2) {
                    mFault.setText("B类故障");
                    mFaultImg.setImageDrawable(findDrawById(R.drawable.work_b));
                }
                if (data.getFaultType() == 3) {
                    mFault.setText("C类故障");
                    mFaultImg.setImageDrawable(findDrawById(R.drawable.work_c));
                }
                TextView mFaultDec = (TextView) vHolder.getView(R.id.tv_content);
                mFaultDec.setText(data.getFaultDescript());
                final TextView mVoiceTime = (TextView) vHolder.getView(R.id.tv_seconds);
                String[] images = new String[data.getFaultPics().size()];
                for (int i = 0; i < data.getFaultPics().size(); i++) {
                    images[i] = data.getFaultPics().get(i).getPicUrl();
                }
                ShowImageLayout showImageLayout = (ShowImageLayout) vHolder.getView(R.id.re_image);
                showImageLayout.showImage(images);
                if (data.getSoundTimescale() == 0 || TextUtils.isEmpty(data.getVoiceUrl())) {
                    mVoiceTime.setVisibility(View.GONE);
                } else {
                    mVoiceTime.setVisibility(View.VISIBLE);
                }
                if (data.isPlay() && isPlay) {
                    mVoiceTime.setBackgroundResource(R.drawable.play_anim);
                    animation = (AnimationDrawable) mVoiceTime.getBackground();
                    mCountDownTimerUtils = new CountDownTimerUtils(mVoiceTime, data.getSoundTimescale() * 1000, 1000, data.getSoundTimescale() + "''", "#ffffff");
                    MediaPlayerManager.playSound(data.getVoiceUrl(), new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            mVoiceTime.setBackgroundResource(R.drawable.record_play_3);
                        }
                    }, new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mp.start();
                            mCountDownTimerUtils.start();
                            animation.start();
                        }
                    });
                } else {
                    mVoiceTime.setText(data.getSoundTimescale() + "''");
                    mVoiceTime.setBackgroundResource(R.drawable.record_play_3);
                }
                mVoiceTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isPlay = true;
                        MediaPlayerManager.release();
                        if (mCountDownTimerUtils != null) {
                            mCountDownTimerUtils.cancel();
                        }
                        if (animation != null) {
                            mVoiceTime.setBackgroundResource(R.drawable.record_play_3);
                        }
                        for (int i = 0; i < mList.size(); i++) {
                            if (position == i) {
                                mList.get(i).setPlay(true);
                            } else {
                                mList.get(i).setPlay(false);
                            }
                        }
                        mExpendRecycleView.getAdapter().notifyDataSetChanged();
                    }
                });
            }
        };
        mExpendRecycleView.setAdapter(adapter);
        mExpendRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == 1) {
                    isPlay = false;
                }
            }
        });
        mPresenter.getCheckInfo(mTaskId);
        mPresenter.getFaultList(mTaskId);
    }

    private void initView() {
        mHeaderView = LayoutInflater.from(this).inflate(R.layout.header_inspect_detial, null);
        mInspectionPlace = mHeaderView.findViewById(R.id.id_inspect_place);
        mInspectionState = mHeaderView.findViewById(R.id.id_inspect_state);
        mInspectionNum = mHeaderView.findViewById(R.id.id_inspect_num);
        mInspectionReceive = mHeaderView.findViewById(R.id.id_inspect_receive);
        mInspectionReceiveTime = mHeaderView.findViewById(R.id.id_inspect_receive_time);
        mInspectionCarry = mHeaderView.findViewById(R.id.id_inspect_carry);
        mInspectionCarryTime = mHeaderView.findViewById(R.id.id_inspect_carry_time);
        mInspectionReport = mHeaderView.findViewById(R.id.id_inspect_report);
        mInspectionCheck = mHeaderView.findViewById(R.id.id_inspect_repair);
        mInspectionDeal = mHeaderView.findViewById(R.id.id_inspect_deal);
        mInspectionDo = mHeaderView.findViewById(R.id.id_inspect_do);
        mInspectionClose = mHeaderView.findViewById(R.id.id_inspect_close);
        tv_task_name = mHeaderView.findViewById(R.id.tv_task_name);
        mRecycleRefreshLoadLayout = (RecycleRefreshLoadLayout) findViewById(R.id.refreshLoadLayoutId);
        mRecycleRefreshLoadLayout.setColorSchemeColors(findColorById(R.color.colorPrimary));
        mExpendRecycleView = (ExpendRecycleView) findViewById(R.id.recycleViewId);
        mNoDataLayout = (RelativeLayout) findViewById(R.id.layout_no_data);
        mExpendRecycleView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
        mExpendRecycleView.addHeaderView(mHeaderView);
        mRecycleRefreshLoadLayout.setOnRefreshListener(this);
        View loadFooterView = LayoutInflater.from(this).inflate(R.layout.view_load_more_inspection, null);
        mRecycleRefreshLoadLayout.setViewFooter(loadFooterView);
    }

    @Override
    public void showCheckInfo(CheckBean bean) {
        String rooms = "";
        if (bean.getRooms() != null && bean.getRooms().size() > 0) {
            for (int i = 0; i < bean.getRooms().size(); i++) {
                rooms = rooms + "," + bean.getRooms().get(i);
            }
        }
        if (!TextUtils.isEmpty(rooms)) {
            mInspectionPlace.setText(MessageFormat.format("属地:{0}", rooms.substring(1, rooms.length())));
        }
        mInspectionState.setText(InspectionApp.getInstance().getMapOption().get("5").get(bean.getTaskState() + ""));
        mInspectionNum.setText(String.valueOf(bean.getUploadCount()) + "/" + bean.getCount() + "台");
        if (bean.getReceiveUser() != null && bean.getPlanStartTime() != 0) {
            mInspectionReceive.setText(bean.getReceiveUser().getRealName());
            mInspectionReceiveTime.setText(DataUtil.timeFormat(bean.getPlanStartTime(), "yyyy-MM-dd HH:mm"));
        }
        if (bean.getUsers() != null && bean.getUsers().size() > 0 && bean.getStartTime() != 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bean.getUsers().size(); i++) {
                sb.append(bean.getUsers().get(i).getRealName());
                if (i != bean.getUsers().size() - 1) {
                    sb.append(" ");
                }
            }
            mInspectionCarry.setText(sb.toString());
            mInspectionCarryTime.setText(DataUtil.timeFormat(bean.getStartTime(), "yyyy-MM-dd HH:mm"));
        }
        tv_task_name.setText(bean.getTaskName());
        if (bean.getExecutorUserList() != null && bean.getExecutorUserList().size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bean.getExecutorUserList().size(); i++) {
                if (bean.getExecutorUserList().get(i).getUser() == null) {
                    continue;
                }
                if (!TextUtils.isEmpty(bean.getExecutorUserList().get(i).getUser().getRealName())) {
                    sb.append(bean.getExecutorUserList().get(i).getUser().getRealName());
                    if (i != bean.getExecutorUserList().size() - 1) {
                        sb.append("、");
                    }
                }
            }
            TextView tv_executor_user = (TextView) findViewById(R.id.tv_executor_user);
            tv_executor_user.setText(sb.toString());
        }
        mInspectionReport.setText(MessageFormat.format("{0}个", bean.getAllFault()));
        mInspectionDeal.setText(MessageFormat.format("{0}个", bean.getFlowingCount()));
        mInspectionCheck.setText(MessageFormat.format("{0}个", bean.getRepairCount()));
        mInspectionDo.setText(MessageFormat.format("{0}个", bean.getPendingCount()));
        mInspectionClose.setText(MessageFormat.format("{0}个", bean.getCloseCount()));
    }

    @Override
    public void showFaultList(List<FaultList> faultLists) {
        if (isRefresh) {
            mList.clear();
            isRefresh = false;
            if (mCountDownTimerUtils != null) {
                mCountDownTimerUtils.cancel();
            }
            MediaPlayerManager.release();
        }
        mList.addAll(faultLists);
        if (faultLists.size() > 0) {
            mNoDataLayout.setVisibility(View.GONE);
        } else {
            noData();
        }
        mExpendRecycleView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void showMoreFaultList(List<FaultList> faultLists) {
        mList.addAll(faultLists);
        mExpendRecycleView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void showLoading() {
        if (!isRefresh) {
            showEvLoading();
        } else {
            mRecycleRefreshLoadLayout.setRefreshing(true);
        }
    }

    @Override
    public void hideLoading() {
        mRecycleRefreshLoadLayout.setRefreshing(false);
        hideEvLoading();
    }

    @Override
    public void noData() {
        mNoDataLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void noMoreData() {
        mRecycleRefreshLoadLayout.setNoMoreData(true);
    }

    @Override
    public void hideLoadingMore() {
        mRecycleRefreshLoadLayout.loadFinish();
    }

    @Override
    public void setPresenter(InspectionDetailContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onRefresh() {
        isPlay = false;
        isRefresh = true;
        mNoDataLayout.setVisibility(View.GONE);
        mRecycleRefreshLoadLayout.setNoMoreData(false);
        mPresenter.getFaultList(mTaskId);
    }

    @Override
    public void onLoadMore() {
        if (mList.size() > 1 && !isRefresh) {
            mPresenter.getMoreFaultList(mTaskId, mList.get(mList.size() - 1).getFaultId());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MediaPlayerManager.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MediaPlayerManager.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MediaPlayerManager.pause();
    }
}
