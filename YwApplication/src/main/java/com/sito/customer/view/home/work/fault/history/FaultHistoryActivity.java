package com.sito.customer.view.home.work.fault.history;

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

import com.sito.customer.R;
import com.sito.customer.app.CustomerApp;
import com.sito.customer.common.ConstantInt;
import com.sito.customer.mode.bean.check.FaultList;
import com.sito.customer.mode.fault.FaultRepository;
import com.sito.customer.utils.CountDownTimerUtils;
import com.sito.customer.utils.MediaPlayerManager;
import com.sito.customer.view.BaseActivity;
import com.sito.customer.view.photo.ViewPagePhotoActivity;
import com.sito.library.adapter.RVAdapter;
import com.sito.library.utils.DataUtil;
import com.sito.library.utils.DisplayUtil;
import com.sito.library.utils.GlideUtils;
import com.sito.library.widget.ExpendRecycleView;
import com.sito.library.widget.RecycleRefreshLoadLayout;

import java.util.ArrayList;
import java.util.List;

public class FaultHistoryActivity extends BaseActivity implements FaultHistoryContract.View, SwipeRefreshLayout.OnRefreshListener
        , RecycleRefreshLoadLayout.OnLoadListener {
    //view
    private RelativeLayout mNoDataLayout;
    private ExpendRecycleView mExpendRecycleView;
    private RecycleRefreshLoadLayout mRecycleRefreshLoadLayout;
    //data
    private List<FaultList> mList;
    private boolean isRefresh;
    private CountDownTimerUtils mCountDownTimerUtils;
    private AnimationDrawable animation;
    private boolean isPlay;
    private FaultHistoryContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutAndToolbar(R.layout.activity_fault_history, "历史故障");
        new FaultHistoryPresenter(CustomerApp.getInstance().getFaultRepositoryComponent().getRepository(), this);
        initView();
        initData();
    }

    private void initData() {
        mList = new ArrayList<>();
        RVAdapter<FaultList> adapter = new RVAdapter<FaultList>(mExpendRecycleView, mList, R.layout.item_fault_history) {
            @Override
            public void showData(ViewHolder vHolder, final FaultList data, final int position) {
                TextView mEquipName = (TextView) vHolder.getView(R.id.tv_equip_name);
                TextView mFaultType = (TextView) vHolder.getView(R.id.tv_alarm_name);
                TextView mTvFrom = (TextView) vHolder.getView(R.id.tv_from);
                mTvFrom.setVisibility(View.GONE);
                TextView mTvTime = (TextView) vHolder.getView(R.id.tv_time);
                TextView mTvContent = (TextView) vHolder.getView(R.id.id_tv_voicecontent);
                mTvContent.setText(data.getFaultDescript());
                mTvTime.setText(DataUtil.timeFormat(data.getCreateTime(), null));
                final TextView mTvSeconds = (TextView) vHolder.getView(R.id.tv_seconds);
                String timeStr = data.getSoundTimescale() + "''";
                mTvSeconds.setText(timeStr);
                if (data.getEquipment() != null && !TextUtils.isEmpty(data.getEquipment().getEquipmentName())) {
                    mEquipName.setVisibility(View.VISIBLE);
                    mEquipName.setText(data.getEquipment().getEquipmentName());
                } else {
                    mEquipName.setVisibility(View.GONE);
                }
                mFaultType.setText(CustomerApp.getInstance().getMapOption().get(ConstantInt.FAULT + "").get(data.getFaultType() + ""));
                final String[] imgs = new String[data.getFaultPics().size()];
                for (int i = 0; i < data.getFaultPics().size(); i++) {
                    imgs[i] = data.getFaultPics().get(i).getPicUrl();
                }
                LinearLayout re_image = (LinearLayout) vHolder.getView(R.id.re_image);
                re_image.removeAllViews();
                for (int i = 0; i < imgs.length; i++) {
                    re_image.addView(getImageView(i, imgs));
                }
                if (data.getSoundTimescale() == 0 || TextUtils.isEmpty(data.getVoiceUrl())) {
                    mTvSeconds.setVisibility(View.GONE);
                } else {
                    mTvSeconds.setVisibility(View.VISIBLE);
                }
                if (data.isPlay() && isPlay) {
                    mTvSeconds.setBackgroundResource(R.drawable.play_anim);
                    animation = (AnimationDrawable) mTvSeconds.getBackground();
                    mCountDownTimerUtils = new CountDownTimerUtils(mTvSeconds
                            , data.getSoundTimescale() * 1000
                            , 1000
                            , data.getSoundTimescale() + "''"
                            , "#ffffff");
                    MediaPlayerManager.playSound(data.getVoiceUrl(), new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            mTvSeconds.setBackgroundResource(R.drawable.record_play_3);
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
                    mTvSeconds.setText(timeStr);
                    mTvSeconds.setBackgroundResource(R.drawable.record_play_3);
                }
                mTvSeconds.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isPlay = true;
                        MediaPlayerManager.release();
                        if (mCountDownTimerUtils != null) {
                            mCountDownTimerUtils.cancel();
                        }
                        if (animation != null) {
                            mTvSeconds.setBackgroundResource(R.drawable.record_play_3);
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
        mPresenter.getFaultList(ConstantInt.PAGE_SIZE);
    }

    private ImageView getImageView(int position, String[] url) {
        int width = CustomerApp.getInstance().getDisplayWidth();
        width = (width - DisplayUtil.dip2px(this, 180)) / 3;
        ImageView imageView = new ImageView(this);
        imageView.setTag(R.id.tag_task, url);
        imageView.setTag(R.id.tag_position, position);
        imageView.setOnClickListener(onClickListener);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, width);
        params.setMargins(10, 0, 0, 0);//4个参数按顺序分别是左上右下
        imageView.setLayoutParams(params);
        GlideUtils.ShowImage(this, url[position], imageView, R.drawable.picture_default);
        return imageView;
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String[] urls = (String[]) v.getTag(R.id.tag_task);
            int position = (int) v.getTag(R.id.tag_position);
            ViewPagePhotoActivity.startActivity(FaultHistoryActivity.this, urls, position);
        }
    };

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
    public void showData(List<FaultList> lists) {
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
    public void showMoreData(List<FaultList> lists) {
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
    public void setPresenter(FaultHistoryContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onRefresh() {
        isPlay = false;
        isRefresh = true;
        mNoDataLayout.setVisibility(View.GONE);
        mRecycleRefreshLoadLayout.setNoMoreData(false);
        mPresenter.getFaultList(ConstantInt.PAGE_SIZE);
    }

    @Override
    public void onLoadMore() {
        if (mList.size() > 1 && !isRefresh) {
            mPresenter.getMoreFaultList(ConstantInt.PAGE_SIZE, mList.get(mList.size() - 1).getFaultId());
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
