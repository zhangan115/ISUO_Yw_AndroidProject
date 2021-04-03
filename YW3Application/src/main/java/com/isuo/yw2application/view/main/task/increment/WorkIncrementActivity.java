package com.isuo.yw2application.view.main.task.increment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.isuo.yw2application.R;
import com.isuo.yw2application.app.Yw2Application;
import com.isuo.yw2application.common.ConstantStr;
import com.isuo.yw2application.mode.bean.work.IncrementBean;
import com.isuo.yw2application.utils.CountDownTimerUtils;
import com.isuo.yw2application.utils.MediaPlayerManager;
import com.isuo.yw2application.view.base.BaseActivity;
import com.isuo.yw2application.view.main.generate.increment.GenerateIncrementActivity;
import com.isuo.yw2application.view.main.task.increment.detail.IncrementDetailActivity;
import com.isuo.yw2application.view.main.task.increment.execute.IncrementWorkActivity;
import com.isuo.yw2application.view.main.task.increment.submit.IncrementActivity;
import com.isuo.yw2application.widget.ShowImageLayout;
import com.sito.library.adapter.RVAdapter;
import com.sito.library.utils.DataUtil;
import com.sito.library.widget.ExpendRecycleView;
import com.sito.library.widget.RecycleRefreshLoadLayout;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 巡检列表
 * Created by zhangan on 2018/3/27.
 */

public class WorkIncrementActivity extends BaseActivity implements IncrementContract.View, SwipeRefreshLayout.OnRefreshListener
        , RecycleRefreshLoadLayout.OnLoadListener {

    private ExpendRecycleView mRecyclerView;
    private RelativeLayout noDataLayout;

    private IncrementContract.Presenter mPresenter;
    private RecycleRefreshLoadLayout swipeRefreshLayout;
    private List<IncrementBean> mList;

    private CountDownTimerUtils mCountDownTimerUtils;
    private AnimationDrawable animation;
    private boolean isPlay, isRefresh;
    private int REQUEST_CODE = 100;
    @Nullable
    private String time;
    private boolean unFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutAndToolbar(R.layout.activivity_increment_work_list, "专项工作");
        time = getIntent().getStringExtra(ConstantStr.KEY_BUNDLE_STR);
        unFinish = getIntent().getBooleanExtra(ConstantStr.KEY_BUNDLE_BOOLEAN, false);
        new IncrementPresenter(Yw2Application.getInstance().getWorkRepositoryComponent().getRepository(), this);
        mRecyclerView = findViewById(R.id.recycleViewId);
        @SuppressLint("InflateParams")
        View loadFooterView = LayoutInflater.from(this).inflate(R.layout.view_load_more_inspection, null);
        swipeRefreshLayout = findViewById(R.id.swipeLayout);
        swipeRefreshLayout.setColorSchemeColors(findColorById(R.color.colorPrimary));
        swipeRefreshLayout.setOnRefreshListener(this);
        if (TextUtils.isEmpty(time)) {
            swipeRefreshLayout.setOnLoadListener(this);
        }
        swipeRefreshLayout.setViewFooter(loadFooterView);
        noDataLayout = findViewById(R.id.layout_no_data);
        mList = new ArrayList<>();
        initRecycleView();
        mPresenter.getData(time, unFinish);
    }

    private void initRecycleView() {
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        @SuppressLint("InflateParams")
        View headerView = LayoutInflater.from(this).inflate(R.layout.header_increment_work, null);
        if (TextUtils.isEmpty(time) && !unFinish) {
            mRecyclerView.addHeaderView(headerView);
        }
        headerView.findViewById(R.id.btnPoint).setOnClickListener(this);
        headerView.findViewById(R.id.btnSubmit).setOnClickListener(this);
        RVAdapter<IncrementBean> adapter = new RVAdapter<IncrementBean>(mRecyclerView, mList, R.layout.item_increment) {

            @Override
            public void showData(ViewHolder vHolder, final IncrementBean data, final int position) {
                ShowImageLayout showImageLayout = (ShowImageLayout) vHolder.getView(R.id.ll_increment_image);
                TextView tv_increment_name = (TextView) vHolder.getView(R.id.tv_increment_name);
                TextView tv_increment_state = (TextView) vHolder.getView(R.id.tv_increment_state);
                final TextView tv_seconds = (TextView) vHolder.getView(R.id.id_increment_time);
                TextView tv_from = (TextView) vHolder.getView(R.id.tv_from);
                TextView tv_time = (TextView) vHolder.getView(R.id.tv_time);
                TextView start_time = (TextView) vHolder.getView(R.id.tv_plan_start_time);
                TextView end_time = (TextView) vHolder.getView(R.id.tv_plan_end_time);
                TextView tvWorkState = (TextView) vHolder.getView(R.id.tvWorkState);
                TextView tv_equip_name = (TextView) vHolder.getView(R.id.tv_equip_name);
                TextView tv_title_sound = (TextView) vHolder.getView(R.id.tv_title_sound);
                LinearLayout show_time = (LinearLayout) vHolder.getView(R.id.show_time);
                LinearLayout ll_start_task = (LinearLayout) vHolder.getView(R.id.ll_start_task);
                if (data.getIssuedUser() != null) {
                    tv_from.setVisibility(View.VISIBLE);
                    tv_from.setText(MessageFormat.format("来源:{0}", data.getIssuedUser().getRealName()));
                } else {
                    tv_from.setVisibility(View.GONE);
                }
                TextView tv_sound_content = (TextView) vHolder.getView(R.id.tv_increment_play_text);
                String[] images;
                int soundTime;
                String soundUrl;
                if (data.getEquipment() == null) {
                    tv_equip_name.setVisibility(View.GONE);
                } else {
                    tv_equip_name.setVisibility(View.VISIBLE);
                    if (TextUtils.isEmpty(data.getEquipment().getEquipmentSn())) {
                        tv_equip_name.setText(data.getEquipment().getEquipmentName());
                    } else {
                        String str = data.getEquipment().getEquipmentName() + "(" + data.getEquipment().getEquipmentSn() + ")";
                        tv_equip_name.setText(str);
                    }
                }
                if (data.getWorkIssued() == 0) {
                    show_time.setVisibility(View.GONE);
                    images = data.getWorkImages().split(",");
                    soundTime = data.getSoundTimescale();
                    tv_sound_content.setText(data.getWorkContent());
                    soundUrl = data.getWorkSound();
                    start_time.setVisibility(View.GONE);
                    end_time.setVisibility(View.GONE);
                    tv_title_sound.setText("工作内容");
                    ll_start_task.setVisibility(View.GONE);
                    if (data.getIssuedUser() != null && data.getIssuedUser().getUserId() == Yw2Application.getInstance().getCurrentUser().getUserId()) {
                        tvWorkState.setText("我的上报");
                    } else {
                        tvWorkState.setText("员工上报");
                    }
                } else {
                    show_time.setVisibility(View.GONE);
                    images = data.getXworkImages().split(",");
                    soundTime = data.getXsoundTimescale();
                    tv_sound_content.setText(data.getXworkContent());
                    soundUrl = data.getXworkSound();
                    start_time.setVisibility(View.VISIBLE);
                    start_time.setText(DataUtil.timeFormat(data.getStartTime(), null));
                    end_time.setVisibility(View.VISIBLE);
                    end_time.setText(DataUtil.timeFormat(data.getEndTime(), null));
                    tv_title_sound.setText("工作要求");
                    boolean isIn = false;
                    if (!TextUtils.isEmpty(data.getUserIds())) {
                        String[] userIds = data.getUserIds().split(",");
                        for (String userId : userIds) {
                            if (userId.equals(String.valueOf(Yw2Application.getInstance().getCurrentUser().getUserId()))) {
                                isIn = true;
                                break;
                            }
                        }
                    }
                    if (isIn) {
                        tvWorkState.setText("我的工作");
                    } else {
                        if (data.getIssuedUser() != null && data.getIssuedUser().getUserId() == Yw2Application.getInstance().getCurrentUser().getUserId()) {
                            tvWorkState.setText("我的指派");
                        } else {
                            tvWorkState.setText("员工指派");
                        }
                    }
                }
                if (data.getCreateTime() != 0) {
                    tv_time.setVisibility(View.VISIBLE);
                    tv_time.setText(DataUtil.timeFormat(data.getCreateTime(), null));
                } else {
                    tv_time.setVisibility(View.GONE);
                }
                String state;
                switch (data.getWorkState()) {
                    case 0:
                        ll_start_task.setVisibility(View.VISIBLE);
                        tv_increment_state.setTextColor(findColorById(R.color.color_not_start));
                        state = "待开始";
                        break;
                    case 1:
                        ll_start_task.setVisibility(View.VISIBLE);
                        tv_increment_state.setTextColor(findColorById(R.color.color_start));
                        state = "进行中";
                        break;
                    default:
                        ll_start_task.setVisibility(View.GONE);
                        tv_increment_state.setTextColor(findColorById(R.color.color_finish));
                        state = "已完成";
                        break;
                }
                tv_increment_state.setText(state);
                showImageLayout.showImage(images);
                if (TextUtils.isEmpty(soundUrl) || soundTime == 0) {
                    tv_seconds.setVisibility(View.GONE);
                } else {
                    tv_seconds.setVisibility(View.VISIBLE);
                }
                tv_increment_name.setText(Yw2Application.getInstance().getMapOption().get("1").get(String.valueOf(data.getWorkType())));
                if (data.isPlay() && isPlay) {
                    tv_seconds.setBackgroundResource(R.drawable.play_anim);
                    animation = (AnimationDrawable) tv_seconds.getBackground();
                    mCountDownTimerUtils = new CountDownTimerUtils(tv_seconds, soundTime * 1000
                            , 1000, soundTime + "s", "#999999");
                    MediaPlayerManager.playSound(soundUrl, new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            tv_seconds.setBackgroundResource(R.drawable.voice_three);
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
                    String str = soundTime + "s";
                    tv_seconds.setText(str);
                    tv_seconds.setBackgroundResource(R.drawable.voice_three);
                }
                tv_seconds.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MediaPlayerManager.release();
                        isPlay = true;
                        if (mCountDownTimerUtils != null) {
                            mCountDownTimerUtils.cancel();
                        }
                        if (animation != null) {
                            tv_seconds.setBackgroundResource(R.drawable.voice_three);
                        }
                        for (int i = 0; i < mList.size(); i++) {
                            if (position == i) {
                                mList.get(i).setPlay(true);
                            } else {
                                mList.get(i).setPlay(false);
                            }
                        }
                        mRecyclerView.getAdapter().notifyDataSetChanged();
                    }
                });
                ll_start_task.setTag(R.id.tag_position, position);
                ll_start_task.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = (int) v.getTag(R.id.tag_position);
                        Intent intent = new Intent(WorkIncrementActivity.this
                                , IncrementWorkActivity.class);
                        intent.putExtra(ConstantStr.KEY_BUNDLE_OBJECT, mList.get(position));
                        isClick = true;
                        startActivity(intent);
                    }
                });
            }
        };
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new RVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(WorkIncrementActivity.this, IncrementDetailActivity.class);
                intent.putExtra(ConstantStr.KEY_BUNDLE_OBJECT, mList.get(position));
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnPoint:
                startActivityForResult(new Intent(WorkIncrementActivity.this, GenerateIncrementActivity.class)
                        , REQUEST_CODE);
                break;
            case R.id.btnSubmit:
                startActivityForResult(new Intent(WorkIncrementActivity.this, IncrementActivity.class)
                        , REQUEST_CODE);
                break;
        }
    }

    @Override
    public void showData(List<IncrementBean> been) {
        mList.clear();
        mList.addAll(been);
        mRecyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void showMoreData(List<IncrementBean> been) {
        mList.addAll(been);
        mRecyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void showLoading() {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        isRefresh = false;
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void noData() {
        mList.clear();
        if (mRecyclerView.getAdapter() == null) {
            return;
        }
        mRecyclerView.getAdapter().notifyDataSetChanged();
        noDataLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void noMoreData() {
        swipeRefreshLayout.setNoMoreData(true);
    }

    @Override
    public void hideLoadingMore() {
        swipeRefreshLayout.loadFinish();
    }

    @Override
    public void setPresenter(IncrementContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onRefresh() {
        if (mPresenter != null) {
            isRefresh = true;
            noDataLayout.setVisibility(View.GONE);
            swipeRefreshLayout.setNoMoreData(false);
            mPresenter.getData(time, unFinish);
        }
    }

    private boolean isClick;

    @Override
    public void onResume() {
        super.onResume();
        if (isClick) {
            onRefresh();
        }
        isClick = false;
    }

    @Override
    public void onLoadMore() {
        if (isRefresh || mList.size() == 0)
            return;
        mPresenter.getDataMore(time, unFinish, String.valueOf(mList.get(mList.size() - 1).getWorkId()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            onRefresh();
        }
    }
}
