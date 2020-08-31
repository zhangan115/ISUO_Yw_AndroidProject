package com.sito.evpro.inspection.view.equipment.alarm;

import android.content.Context;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.sito.evpro.inspection.R;
import com.sito.evpro.inspection.app.InspectionApp;
import com.sito.evpro.inspection.common.ConstantStr;
import com.sito.evpro.inspection.mode.bean.fault.FaultList;
import com.sito.evpro.inspection.utils.CountDownTimerUtils;
import com.sito.evpro.inspection.view.MvpFragment;
import com.sito.evpro.inspection.view.MvpFragmentV4;
import com.sito.evpro.inspection.view.photo.ViewPagePhotoActivity;
import com.sito.evpro.inspection.view.repair.IViewPagerChangeListener;
import com.sito.evpro.inspection.view.speech.voice.MediaPlayerManager;
import com.sito.evpro.inspection.widget.ShowImageLayout;
import com.sito.library.adapter.RVAdapter;
import com.sito.library.utils.DataUtil;
import com.sito.library.utils.DisplayUtil;
import com.sito.library.utils.GlideUtils;
import com.sito.library.widget.ExpendRecycleView;
import com.sito.library.widget.RecycleRefreshLoadLayout;
import com.sito.library.widget.TextViewVertical;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangan on 2017/7/2.
 */

public class EquipmentAlarmFragment extends MvpFragment<EquipmentAlarmContact.Presenter> implements EquipmentAlarmContact.View, IViewPagerChangeListener, SwipeRefreshLayout.OnRefreshListener, RecycleRefreshLoadLayout.OnLoadListener {


    private long mEquipId;
    private RelativeLayout mNoDataLayout;
    private ExpendRecycleView mExpendRecycleView;
    private RecycleRefreshLoadLayout mRecycleRefreshLoadLayout;

    //data
    private List<FaultList> mList;
    private boolean isRefresh;
    private CountDownTimerUtils mCountDownTimerUtils;
    private AnimationDrawable animation;
    private boolean isPlay;

    public static EquipmentAlarmFragment newInstance(long equipmentId) {
        Bundle args = new Bundle();
        args.putLong(ConstantStr.KEY_BUNDLE_LONG, equipmentId);
        EquipmentAlarmFragment fragment = new EquipmentAlarmFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mEquipId = getArguments().getLong(ConstantStr.KEY_BUNDLE_LONG);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fmg_equip_data, container, false);
        mExpendRecycleView = (ExpendRecycleView) rootView.findViewById(R.id.recycleViewId);
        mNoDataLayout = (RelativeLayout) rootView.findViewById(R.id.layout_no_data);
        mRecycleRefreshLoadLayout = (RecycleRefreshLoadLayout) rootView.findViewById(R.id.refreshLoadLayoutId);
        mExpendRecycleView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        mRecycleRefreshLoadLayout.setColorSchemeColors(findColorById(R.color.colorPrimary));
        mRecycleRefreshLoadLayout.setOnRefreshListener(this);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mList = new ArrayList<>();
        RVAdapter<FaultList> adapter = new RVAdapter<FaultList>(mExpendRecycleView, mList, R.layout.item_fault_list_n) {
            @Override
            public void showData(ViewHolder vHolder, final FaultList data, final int position) {
                TextView mAddress = (TextView) vHolder.getView(R.id.id_equip_address);
                mAddress.setText(data.getEquipment().getRoom().getRoomName());
                TextView mFaultState = (TextView) vHolder.getView(R.id.id_equip_state);
                mFaultState.setText(InspectionApp.getInstance().getMapOption().get("9").get(data.getFaultState() + ""));
                TextView mEquipName = (TextView) vHolder.getView(R.id.tv_equip_name);
                mEquipName.setText(data.getEquipment().getEquipmentName());
                TextView mUser = (TextView) vHolder.getView(R.id.tv_user);
                mUser.setText(data.getUser().getRealName());
                TextView mTime = (TextView) vHolder.getView(R.id.tv_start_time);
                mTime.setText(MessageFormat.format("上报时间{0}", DataUtil.timeFormat(data.getCreateTime(), "yyyy-MM-dd HH:mm")));
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
                TextView mContent = (TextView) vHolder.getView(R.id.tv_content);
                mContent.setText(data.getFaultDescript());
                final TextView mVoiceTime = (TextView) vHolder.getView(R.id.tv_seconds);
                String[] images = new String[data.getFaultPics().size()];
                for (int i = 0; i < data.getFaultPics().size(); i++) {
                    images[i] = data.getFaultPics().get(i).getPicUrl();
                }
                ShowImageLayout showImageLayout = (ShowImageLayout) vHolder.getView(R.id.show_image);
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
                    mVoiceTime.setText(MessageFormat.format("{0}''''", data.getSoundTimescale()));
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
        if (mPresenter != null) {
            mPresenter.getFaultByEId(mEquipId);
        }

    }

    private ImageView getImageView(int position, String[] url) {
        ImageView imageView = new ImageView(getActivity());
        imageView.setTag(R.id.tag_task, url);
        imageView.setTag(R.id.tag_position, position);
        imageView.setOnClickListener(onClickListener);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DisplayUtil.dip2px(getActivity(), 60), DisplayUtil.dip2px(getActivity(), 60));
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
            ViewPagePhotoActivity.startActivity(getActivity(), urls, position);
        }
    };

    public static EquipmentAlarmFragment newInstance(int position) {
        Bundle args = new Bundle();
        args.putInt(ConstantStr.KEY_BUNDLE_INT, position);
        EquipmentAlarmFragment fragment = new EquipmentAlarmFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static EquipmentAlarmFragment newInstance(int position, int equipId) {
        Bundle args = new Bundle();
        args.putInt(ConstantStr.KEY_BUNDLE_INT, position);
        args.putInt(ConstantStr.KEY_BUNDLE_INT_1, equipId);
        EquipmentAlarmFragment fragment = new EquipmentAlarmFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setPresenter(EquipmentAlarmContact.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onPageChanged(int p) {

    }

    @Override
    public void showFault(List<FaultList> lists) {
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
    public void showMoreFault(List<FaultList> lists) {
        mList.addAll(lists);
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
    public void onRefresh() {
        isPlay = false;
        isRefresh = true;
        mNoDataLayout.setVisibility(View.GONE);
        mRecycleRefreshLoadLayout.setNoMoreData(false);
        mPresenter.getFaultByEId(mEquipId);
    }

    @Override
    public void onLoadMore() {
        if (mList.size() > 1 && !isRefresh) {
            mPresenter.getMoreFaultById(mEquipId, mList.get(mList.size() - 1).getFaultId());
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
    }

    @Override
    public void onPause() {
        super.onPause();
        MediaPlayerManager.pause();
    }
}
