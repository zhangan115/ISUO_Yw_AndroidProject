package com.sito.evpro.inspection.view.repair.increment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sito.evpro.inspection.R;
import com.sito.evpro.inspection.app.InspectionApp;
import com.sito.evpro.inspection.common.ConstantStr;
import com.sito.evpro.inspection.mode.bean.increment.IncrementBean;
import com.sito.evpro.inspection.utils.CountDownTimerUtils;
import com.sito.evpro.inspection.view.MvpFragmentV4;
import com.sito.evpro.inspection.view.repair.IRepairDataChangeListener;
import com.sito.evpro.inspection.view.repair.RepairActivity;
import com.sito.evpro.inspection.view.repair.increment.detail.IncrementDetailActivity;
import com.sito.evpro.inspection.view.repair.increment.work.IncrementWorkActivity;
import com.sito.evpro.inspection.view.speech.voice.MediaPlayerManager;
import com.sito.evpro.inspection.widget.ShowImageLayout;
import com.sito.library.adapter.RVAdapter;
import com.sito.library.utils.DataUtil;
import com.sito.library.widget.RecycleRefreshLoadLayout;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 增值工作
 * Created by zhangan on 2017-06-22.
 */

public class IncrementFragment extends MvpFragmentV4<IncrementContract.Presenter> implements IncrementContract.View, IRepairDataChangeListener
        , SwipeRefreshLayout.OnRefreshListener, RecycleRefreshLoadLayout.OnLoadListener {

    //view
    private RecyclerView mRecyclerView;
    private RecycleRefreshLoadLayout mRecycleRefreshLoadLayout;
    private RelativeLayout noDataLayout;
    //data
    private String mDate;
    private boolean isLoad, isRefresh, isPlay, isClick;
    private int mPosition;
    private List<IncrementBean> mList;
    private CountDownTimerUtils mCountDownTimerUtils;
    private AnimationDrawable animation;

    public static IncrementFragment newInstance(int position, String date) {
        Bundle args = new Bundle();
        args.putInt(ConstantStr.KEY_BUNDLE_INT, position);
        args.putString(ConstantStr.KEY_BUNDLE_STR, date);
        IncrementFragment fragment = new IncrementFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        RepairActivity repairActivity = (RepairActivity) activity;
        repairActivity.addChangeListeners(this);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mList = new ArrayList<>();
        mPosition = getArguments().getInt(ConstantStr.KEY_BUNDLE_INT);
        mDate = getArguments().getString(ConstantStr.KEY_BUNDLE_STR);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fmg_recycle_view_load_data, container, false);
        mRecyclerView = rootView.findViewById(R.id.recycleViewId);
        noDataLayout = rootView.findViewById(R.id.layout_no_data);
        mRecycleRefreshLoadLayout = rootView.findViewById(R.id.refreshLoadLayoutId);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        mRecycleRefreshLoadLayout.setColorSchemeColors(findColorById(R.color.colorPrimary));
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecycleRefreshLoadLayout.setOnRefreshListener(this);
        RVAdapter<IncrementBean> adapter = new RVAdapter<IncrementBean>(mRecyclerView, mList, R.layout.item_increment) {
            @Override
            public void showData(RVAdapter.ViewHolder vHolder, final IncrementBean data, final int position) {
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
                LinearLayout show_time = (LinearLayout) vHolder.getView(R.id.show_time);
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
                    tv_title_sound.setText("工作内容:");
                    ll_start_task.setVisibility(View.GONE);
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
                    tv_title_sound.setText("工作要求:");
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
                tv_increment_name.setText(InspectionApp.getInstance().getMapOption().get("1").get(String.valueOf(data.getWorkType())));
                if (TextUtils.isEmpty(soundUrl) || soundTime == 0) {
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
                        mRecyclerView.getAdapter().notifyDataSetChanged();
                    }
                });
                ll_start_task.setTag(R.id.tag_position, position);
                ll_start_task.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = (int) v.getTag(R.id.tag_position);
                        if (mList.get(position).getWorkState() == 0) {
                            if (mPresenter != null) {
                                mPresenter.startIncrement(position, mList.get(position).getWorkId());
                            }
                        } else {
                            Intent intent = new Intent(getActivity(), IncrementWorkActivity.class);
                            intent.putExtra(ConstantStr.KEY_BUNDLE_OBJECT, mList.get(position));
                            isClick = true;
                            startActivity(intent);
                        }
                    }
                });
            }
        };
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new RVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), IncrementDetailActivity.class);
                intent.putExtra(ConstantStr.KEY_BUNDLE_OBJECT, mList.get(position));
                isClick = true;
                startActivity(intent);
            }
        });
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == 1) {
                    isPlay = false;
                }
            }
        });
    }

    @Override
    public void setPresenter(IncrementContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onPageChanged(int p) {
        isPlay = false;
        onPause();
        if (p != mPosition) {
            return;
        }
        if (isLoad) {
            return;
        }
        if (mPresenter != null) {
            isLoad = true;
            mList.clear();
            mRecyclerView.getAdapter().notifyDataSetChanged();
            noDataLayout.setVisibility(View.GONE);
            mPresenter.getIncrementList(mDate);
        }
    }


    @Override
    public void onDataChange(String data, int position) {
        mDate = data;
        isLoad = false;
        mList.clear();
        mRecyclerView.getAdapter().notifyDataSetChanged();
        onRefresh();
    }

    @Override
    public void showData(List<IncrementBean> been) {
        isRefresh = false;
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
        if (!isRefresh) {
            showEvLoading();
        } else {
            mRecycleRefreshLoadLayout.setRefreshing(true);
        }
    }

    @Override
    public void hideLoading() {
        hideEvLoading();
        mRecycleRefreshLoadLayout.setRefreshing(false);
    }

    @Override
    public void noData() {
        isRefresh = false;
        mList.clear();
        mRecyclerView.getAdapter().notifyDataSetChanged();
        noDataLayout.setVisibility(View.VISIBLE);
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
    public void startSuccess(int position) {
        Intent intent = new Intent(getActivity(), IncrementWorkActivity.class);
        intent.putExtra(ConstantStr.KEY_BUNDLE_OBJECT, mList.get(position));
        isClick = true;
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        isPlay = false;
        if (mPresenter != null) {
            isRefresh = true;
            noDataLayout.setVisibility(View.GONE);
            mRecycleRefreshLoadLayout.setNoMoreData(false);
            mPresenter.getIncrementList(mDate);
        }
    }

    @Override
    public void onLoadMore() {
        if (isRefresh || mList.size() == 0) {
            return;
        }
        if (mPresenter != null) {
            mPresenter.getIncrementListMore(mDate, String.valueOf(mList.get(mList.size() - 1).getWorkId()));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MediaPlayerManager.release();
    }

    @Override
    public void onResume() {
        super.onResume();
        MediaPlayerManager.resume();
        if (isClick) {
            isClick = false;
            onRefresh();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        MediaPlayerManager.release();
        if (mCountDownTimerUtils != null) {
            mCountDownTimerUtils.cancel();
        }
        if (animation != null && animation.isRunning()) {
            animation.stop();
        }
        isPlay = false;
        mRecyclerView.getAdapter().notifyDataSetChanged();
    }

}
