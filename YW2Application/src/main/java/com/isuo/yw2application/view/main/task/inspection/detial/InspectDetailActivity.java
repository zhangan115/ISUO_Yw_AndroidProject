package com.isuo.yw2application.view.main.task.inspection.detial;

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

import com.isuo.yw2application.R;
import com.isuo.yw2application.app.Yw2Application;
import com.isuo.yw2application.common.ConstantStr;
import com.isuo.yw2application.mode.bean.check.CheckBean;
import com.isuo.yw2application.mode.bean.check.FaultList;
import com.isuo.yw2application.utils.CountDownTimerUtils;
import com.isuo.yw2application.utils.MediaPlayerManager;
import com.isuo.yw2application.view.base.NotifyActivity;
import com.isuo.yw2application.view.main.task.inspection.data.InspectionDataActivity;
import com.isuo.yw2application.widget.ShowImageLayout;
import com.sito.library.adapter.RVAdapter;
import com.sito.library.utils.DataUtil;
import com.sito.library.widget.ExpendRecycleView;
import com.sito.library.widget.RecycleRefreshLoadLayout;
import com.sito.library.widget.TextViewVertical;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class InspectDetailActivity extends NotifyActivity implements InspectDetailContract.View
        , SwipeRefreshLayout.OnRefreshListener, RecycleRefreshLoadLayout.OnLoadListener {
    //view
    private RelativeLayout mNoDataLayout;
    private ExpendRecycleView mExpendRecycleView;
    private RecycleRefreshLoadLayout mRecycleRefreshLoadLayout;

    private TextView mInspectPlace;
    private TextView mInspectState;
    private TextView mInspectNum;

    private TextView mInspectReceive;
    private TextView mInspectReceiveTime;

    private TextView mInspectCarry;
    private TextView mInspectCarryTime;

    private TextView mInspectReport;
    private TextView mInspectDeal;
    private TextView mInspectCheck;
    private TextView mInspectDo;
    private TextView mInspectClose;
    private TextView tv_task_name;
    //data
    private List<FaultList> mList;
    private long mTaskId;
    private boolean isRefresh;
    private CountDownTimerUtils mCountDownTimerUtils;
    private AnimationDrawable animation;
    private boolean isPlay;
    @Inject
    InspectDetailPresenter inspectDetailPresenter;
    InspectDetailContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutAndToolbar(R.layout.activity_inspec_detial, "巡检详情");
        DaggerInspectDetailComponent.builder().customerRepositoryComponent(Yw2Application.getInstance().getRepositoryComponent())
                .inspectDetailModule(new InspectDetailModule(this)).build()
                .inject(this);
        mTaskId = getIntent().getLongExtra(ConstantStr.KEY_BUNDLE_LONG, -1);
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
            //搜索 根据taskId查询设备
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
                mAddress.setText("属地:" + data.getEquipment().getRoom().getRoomName());
                TextView mFaultState = (TextView) vHolder.getView(R.id.id_equip_state);
                mFaultState.setText(Yw2Application.getInstance().getMapOption().get("9").get(data.getFaultState() + ""));
                TextView mEquipName = (TextView) vHolder.getView(R.id.tv_equip_name);
                mEquipName.setText(data.getEquipment().getEquipmentName());
                TextView mUser = (TextView) vHolder.getView(R.id.tv_user);
                mUser.setText(data.getUser().getRealName());
                TextView mTime = (TextView) vHolder.getView(R.id.tv_start_time);
                mTime.setText("上报时间:" + DataUtil.timeFormat(data.getCreateTime(), "yyyy-MM-dd HH:mm"));
                TextView mFault = (TextView) vHolder.getView(R.id.tv_alarm);
                ImageView mFaultImg = (ImageView) vHolder.getView(R.id.img_alarm);
                if (data.getFaultType() == 1) {
                    mFault.setText("A类事件");
                    mFaultImg.setImageDrawable(findDrawById(R.drawable.work_a));
                }
                if (data.getFaultType() == 2) {
                    mFault.setText("B类事件");
                    mFaultImg.setImageDrawable(findDrawById(R.drawable.work_b));
                }
                if (data.getFaultType() == 3) {
                    mFault.setText("C类事件");
                    mFaultImg.setImageDrawable(findDrawById(R.drawable.work_c));
                }
                TextView mFaultDec = (TextView) vHolder.getView(R.id.tv_content);
                mFaultDec.setText(data.getFaultDescript());
                final TextView mVoiceTime = (TextView) vHolder.getView(R.id.tv_seconds);
                final String[] imgs = new String[data.getFaultPics().size()];
                for (int i = 0; i < data.getFaultPics().size(); i++) {
                    imgs[i] = data.getFaultPics().get(i).getPicUrl();
                }
                ShowImageLayout showImageLayout = (ShowImageLayout) vHolder.getView(R.id.show_image);
                showImageLayout.showImage(imgs);
                if (data.getSoundTimescale() == 0 || TextUtils.isEmpty(data.getVoiceUrl())) {
                    mVoiceTime.setVisibility(View.GONE);
                } else {
                    mVoiceTime.setVisibility(View.VISIBLE);
                }
                if (data.isPlay() && isPlay) {
                    mVoiceTime.setBackgroundResource(R.drawable.play_anim);
                    animation = (AnimationDrawable) mVoiceTime.getBackground();
                    mCountDownTimerUtils = new CountDownTimerUtils(mVoiceTime
                            , data.getSoundTimescale() * 1000, 1000
                            , data.getSoundTimescale() + "s", "#999999");
                    MediaPlayerManager.playSound(data.getVoiceUrl(), new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            mVoiceTime.setBackgroundResource(R.drawable.voice_three);
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
                    mVoiceTime.setText(MessageFormat.format("{0}s", data.getSoundTimescale()));
                    mVoiceTime.setBackgroundResource(R.drawable.voice_three);
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
                            mVoiceTime.setBackgroundResource(R.drawable.voice_three);
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
                if (newState == 1) {//正在拖动
                    isPlay = false;
                }
            }
        });
        mPresenter.getCheckInfo(mTaskId);
        mPresenter.getFaultList(mTaskId);
    }

    private void initView() {
        View mHeaderView = LayoutInflater.from(this).inflate(R.layout.header_inspect_detial, null);
        mInspectPlace = mHeaderView.findViewById(R.id.id_inspect_place);
        mInspectState = mHeaderView.findViewById(R.id.id_inspect_state);
        mInspectNum = mHeaderView.findViewById(R.id.id_inspect_num);
        mInspectReceive = mHeaderView.findViewById(R.id.id_inspect_receive);
        mInspectReceiveTime = mHeaderView.findViewById(R.id.id_inspect_receive_time);
        mInspectCarry = mHeaderView.findViewById(R.id.id_inspect_carry);
        mInspectCarryTime = mHeaderView.findViewById(R.id.id_inspect_carry_time);
        mInspectReport = mHeaderView.findViewById(R.id.id_inspect_report);
        mInspectCheck = mHeaderView.findViewById(R.id.id_inspect_repair);
        mInspectDeal = mHeaderView.findViewById(R.id.id_inspect_deal);
        mInspectDo = mHeaderView.findViewById(R.id.id_inspect_do);
        mInspectClose = mHeaderView.findViewById(R.id.id_inspect_close);
        tv_task_name = mHeaderView.findViewById(R.id.tv_task_name);
        mRecycleRefreshLoadLayout = findViewById(R.id.refreshLoadLayoutId);
        mRecycleRefreshLoadLayout.setColorSchemeColors(findColorById(R.color.colorPrimary));
        mExpendRecycleView = findViewById(R.id.recycleViewId);
        mNoDataLayout = findViewById(R.id.layout_no_data);
        mExpendRecycleView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
        mExpendRecycleView.addHeaderView(mHeaderView);
        mRecycleRefreshLoadLayout.setOnRefreshListener(this);
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
            mInspectPlace.setText(rooms.substring(1));
        }
        mInspectState.setText(Yw2Application.getInstance().getMapOption().get("5").get(bean.getTaskState() + ""));
        mInspectNum.setText(MessageFormat.format("{0}/{1}台", bean.getUploadCount(), bean.getCount()));
        if (bean.getReceiveUser() != null && bean.getPlanStartTime() != 0) {
            mInspectReceive.setText(bean.getReceiveUser().getRealName());
            mInspectReceiveTime.setText(DataUtil.timeFormat(bean.getPlanStartTime(), "yyyy-MM-dd HH:mm"));
        }
        if (bean.getUsers() != null && bean.getUsers().size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bean.getUsers().size(); i++) {
                sb.append(bean.getUsers().get(i).getRealName());
                if (i != bean.getUsers().size() - 1) {
                    sb.append(" ");
                }
            }
            mInspectCarry.setText(sb.toString());
        }
        if (bean.getStartTime() != 0) {
            mInspectCarryTime.setText(DataUtil.timeFormat(bean.getStartTime(), "yyyy-MM-dd HH:mm"));
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
            TextView tv_executor_user = findViewById(R.id.tv_executor_user);
            tv_executor_user.setText(sb.toString());
        }
        mInspectReport.setText(MessageFormat.format("{0}个", bean.getAllFault()));
        mInspectDeal.setText(MessageFormat.format("{0}个", bean.getFlowingCount()));
        mInspectCheck.setText(MessageFormat.format("{0}个", bean.getRepairCount()));
        mInspectDo.setText(MessageFormat.format("{0}个", bean.getPendingCount()));
        mInspectClose.setText(MessageFormat.format("{0}个", bean.getCloseCount()));

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
    public void setPresenter(InspectDetailContract.Presenter presenter) {
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
        if (mPresenter != null) {
            mPresenter.unSubscribe();
        }
        MediaPlayerManager.release();
        if (mCountDownTimerUtils != null) {
            mCountDownTimerUtils.cancel();
        }
        if (animation != null && animation.isRunning()) {
            animation.stop();
        }
        isPlay = false;
        mExpendRecycleView.getAdapter().notifyDataSetChanged();
    }

}
