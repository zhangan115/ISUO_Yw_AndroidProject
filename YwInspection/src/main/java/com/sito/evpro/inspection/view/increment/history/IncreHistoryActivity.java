package com.sito.evpro.inspection.view.increment.history;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sito.evpro.inspection.R;
import com.sito.evpro.inspection.app.InspectionApp;
import com.sito.evpro.inspection.common.ConstantInt;
import com.sito.evpro.inspection.common.ConstantStr;
import com.sito.evpro.inspection.mode.bean.increment.IncreList;
import com.sito.evpro.inspection.mode.bean.increment.IncrementBean;
import com.sito.evpro.inspection.utils.CountDownTimerUtils;
import com.sito.evpro.inspection.view.BaseActivity;
import com.sito.evpro.inspection.view.photo.ViewPagePhotoActivity;
import com.sito.evpro.inspection.view.repair.increment.detail.IncrementDetailActivity;
import com.sito.evpro.inspection.view.repair.increment.work.IncrementWorkActivity;
import com.sito.evpro.inspection.view.speech.voice.MediaPlayerManager;
import com.sito.evpro.inspection.widget.ShowImageLayout;
import com.sito.library.adapter.RVAdapter;
import com.sito.library.utils.DataUtil;
import com.sito.library.utils.DisplayUtil;
import com.sito.library.utils.GlideUtils;
import com.sito.library.widget.ExpendRecycleView;
import com.sito.library.widget.RecycleRefreshLoadLayout;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class IncreHistoryActivity extends BaseActivity implements IncreHistoryContract.View, SwipeRefreshLayout.OnRefreshListener, RecycleRefreshLoadLayout.OnLoadListener {
    //view
    private RelativeLayout mNoDataLayout;
    private ExpendRecycleView mExpendRecycleView;
    private RecycleRefreshLoadLayout mRecycleRefreshLoadLayout;
    //data
    private List<IncrementBean> mList;
    private boolean isRefresh;
    private CountDownTimerUtils mCountDownTimerUtils;
    private AnimationDrawable animation;
    private boolean isPlay;
    @Inject
    IncreHistoryPresenter mIncreHistoryPresenter;
    IncreHistoryContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutAndToolbar(R.layout.activity_incre_history, "历史记录");
        DaggerIncreHistoryComponent.builder().inspectionRepositoryComponent(InspectionApp.getInstance().getRepositoryComponent())
                .increHistoryModule(new IncreHistoryModule(this)).build()
                .inject(this);
        initView();
        initData();
    }

    private void initData() {
        mList = new ArrayList<>();
        RVAdapter<IncrementBean> adapter = new RVAdapter<IncrementBean>(mExpendRecycleView, mList, R.layout.item_increment) {
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
                TextView tv_equip_name = (TextView) vHolder.getView(R.id.tv_equip_name);
                TextView tv_title_sound = (TextView) vHolder.getView(R.id.tv_title_sound);
                LinearLayout ll_start_task = (LinearLayout) vHolder.getView(R.id.ll_start_task);
                ll_start_task.setVisibility(View.GONE);
                LinearLayout show_time = (LinearLayout) vHolder.getView(R.id.show_time);
                if (data.getIssuedUser() != null) {
                    tv_from.setVisibility(View.VISIBLE);
                    tv_from.setText(MessageFormat.format("来源:{0}", data.getIssuedUser().getRealName()));
                } else {
                    tv_from.setVisibility(View.GONE);
                }
                tv_seconds.setText(data.getSoundTimescale() + "''");
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
                        tv_equip_name.setText(data.getEquipment().getEquipmentName() + "(" + data.getEquipment().getEquipmentSn() + ")");
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
                        tv_increment_state.setTextColor(findColorById(R.color.color_not_start));
                        state = "待开始";
                        break;
                    case 1:
                        tv_increment_state.setTextColor(findColorById(R.color.color_start));
                        state = "进行中";
                        break;
                    default:
                        tv_increment_state.setTextColor(findColorById(R.color.color_finish));
                        state = "已完成";
                        break;
                }
                tv_increment_state.setText(state);
                showImageLayout.showImage(images);
                tv_increment_name.setText(InspectionApp.getInstance().getMapOption().get("1").get(String.valueOf(data.getWorkType())));
                if (soundTime == 0 || TextUtils.isEmpty(soundUrl)) {
                    tv_seconds.setVisibility(View.GONE);
                } else {
                    tv_seconds.setVisibility(View.VISIBLE);
                }
                if (data.isPlay() && isPlay) {
                    tv_seconds.setBackgroundResource(R.drawable.play_anim);
                    animation = (AnimationDrawable) tv_seconds.getBackground();
                    mCountDownTimerUtils = new CountDownTimerUtils(tv_seconds, soundTime * 1000, 1000, soundTime + "''", "#ffffff");
                    MediaPlayerManager.playSound(soundUrl, new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            tv_seconds.setBackgroundResource(R.drawable.record_play_3);
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
                    tv_seconds.setText(soundTime + "''");
                    tv_seconds.setBackgroundResource(R.drawable.record_play_3);
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
                            tv_seconds.setBackgroundResource(R.drawable.record_play_3);
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
        adapter.setOnItemClickListener(new RVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(IncreHistoryActivity.this, IncrementDetailActivity.class);
                intent.putExtra(ConstantStr.KEY_BUNDLE_OBJECT, mList.get(position));
                startActivity(intent);
            }
        });
        mExpendRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == 1) {
                    isPlay = false;
                }
            }
        });
        mPresenter.getIncreList(ConstantInt.PAGE_SIZE);
    }


    private void initView() {
        mRecycleRefreshLoadLayout = (RecycleRefreshLoadLayout) findViewById(R.id.refreshLoadLayoutId);
        mRecycleRefreshLoadLayout.setColorSchemeColors(findColorById(R.color.colorPrimary));
        mExpendRecycleView = (ExpendRecycleView) findViewById(R.id.recycleViewId);
        mNoDataLayout = (RelativeLayout) findViewById(R.id.layout_no_data);
        mExpendRecycleView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
        mRecycleRefreshLoadLayout.setOnLoadListener(this);
        mRecycleRefreshLoadLayout.setOnRefreshListener(this);
        View loadFooterView = LayoutInflater.from(this).inflate(R.layout.view_load_more_inspection, null);
        mRecycleRefreshLoadLayout.setViewFooter(loadFooterView);
    }

    @Override
    public void showData(List<IncrementBean> lists) {
        if (isRefresh) {
            mList.clear();
            isRefresh = false;
            if (mCountDownTimerUtils != null) {
                mCountDownTimerUtils.cancel();
            }
            MediaPlayerManager.release();
        }
        mList.addAll(lists);
        if (lists.size() > 0) {
            mNoDataLayout.setVisibility(View.GONE);
        } else {
            noData();
        }
        mExpendRecycleView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void showMoreData(List<IncrementBean> lists) {
        mList.addAll(lists);
        mExpendRecycleView.getAdapter().notifyDataSetChanged();
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
    public void setPresenter(IncreHistoryContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onRefresh() {
        isPlay = false;
        isRefresh = true;
        mNoDataLayout.setVisibility(View.GONE);
        mRecycleRefreshLoadLayout.setNoMoreData(false);
        mPresenter.getIncreList(ConstantInt.PAGE_SIZE);
    }

    @Override
    public void onLoadMore() {
        if (mList.size() > 1 && !isRefresh) {
            mPresenter.getMoreIncreList(ConstantInt.PAGE_SIZE, mList.get(mList.size() - 1).getWorkId());
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
//        MediaPlayerManager.pause();
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
