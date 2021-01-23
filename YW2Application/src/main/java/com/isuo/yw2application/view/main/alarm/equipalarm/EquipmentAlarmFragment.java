package com.isuo.yw2application.view.main.alarm.equipalarm;

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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.isuo.yw2application.view.base.IRepairDataChangeListener;
import com.isuo.yw2application.R;
import com.isuo.yw2application.app.Yw2Application;
import com.isuo.yw2application.common.ConstantStr;
import com.isuo.yw2application.mode.bean.check.FaultList;
import com.isuo.yw2application.utils.CountDownTimerUtils;
import com.isuo.yw2application.utils.MediaPlayerManager;
import com.isuo.yw2application.view.base.LazyLoadFragmentV4;
import com.isuo.yw2application.view.main.alarm.detail.AlarmDetailActivity;
import com.isuo.yw2application.widget.ShowImageLayout;
import com.sito.library.adapter.RVAdapter;
import com.sito.library.utils.DataUtil;
import com.sito.library.widget.ExpendRecycleView;
import com.sito.library.widget.RecycleRefreshLoadLayout;
import com.sito.library.widget.TextViewVertical;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 故障统计
 * Created by zhangan on 2017/9/27.
 */

public class EquipmentAlarmFragment extends LazyLoadFragmentV4<EquipAlarmContract.Presenter> implements EquipAlarmContract.View
        , RecycleRefreshLoadLayout.OnLoadListener, SwipeRefreshLayout.OnRefreshListener, IRepairDataChangeListener {

    //view
    private RelativeLayout mNoDataLayout;
    private ExpendRecycleView mExpendRecycleView;
    private RecycleRefreshLoadLayout mRecycleRefreshLoadLayout;
    private boolean isRefresh;
    private boolean isPlay;
    private boolean isLoad;
    //data
    private List<FaultList> mList;
    private CountDownTimerUtils mCountDownTimerUtils;
    private AnimationDrawable animation;
    @Nullable
    private String mCalendar;
    @Nullable
    private String dataType;
    private int mAlarmType;
    private EquipAlarmContract.Presenter mPresenter;

    public static EquipmentAlarmFragment newInstance(String s,String dataType, int type) {
        Bundle args = new Bundle();
        args.putString(ConstantStr.KEY_BUNDLE_STR, s);
        args.putString(ConstantStr.KEY_BUNDLE_STR_1, dataType);
        args.putInt(ConstantStr.KEY_BUNDLE_INT, type);
        EquipmentAlarmFragment fragment = new EquipmentAlarmFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCalendar = getArguments().getString(ConstantStr.KEY_BUNDLE_STR);
        dataType = getArguments().getString(ConstantStr.KEY_BUNDLE_STR_1);
        mAlarmType = getArguments().getInt(ConstantStr.KEY_BUNDLE_INT);
        new EquipAlarmPresenter(Yw2Application.getInstance().getRepositoryComponent().getRepository(), this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_equipment_alarm, container, false);
        mRecycleRefreshLoadLayout = rootView.findViewById(R.id.refreshLoadLayoutId);
        mRecycleRefreshLoadLayout.setColorSchemeColors(findColorById(R.color.colorPrimary));
        View loadFooterView = LayoutInflater.from(getActivity()).inflate(R.layout.view_load_more_inspection, null);
        mRecycleRefreshLoadLayout.setViewFooter(loadFooterView);
        mExpendRecycleView = rootView.findViewById(R.id.recycleViewId);
        mNoDataLayout = rootView.findViewById(R.id.layout_no_data);
        mExpendRecycleView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        mRecycleRefreshLoadLayout.setOnLoadListener(this);
        mRecycleRefreshLoadLayout.setOnRefreshListener(this);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
    }

    @Override
    public void requestData() {
        if (isLoad) {
            return;
        }
        mList.clear();
        mExpendRecycleView.getAdapter().notifyDataSetChanged();
        mNoDataLayout.setVisibility(View.GONE);
        isLoad = true;
        if (mCalendar != null) {
            mPresenter.getTodayFault(null, null, mAlarmType);
        } else {
            mPresenter.getFaultList(mAlarmType);
        }
    }

    private void initData() {
        mList = new ArrayList<>();
        RVAdapter<FaultList> adapter = new RVAdapter<FaultList>(mExpendRecycleView, mList, R.layout.item_fault_list) {
            @Override
            public void showData(ViewHolder vHolder, final FaultList data, final int position) {
                TextView mAddress = (TextView) vHolder.getView(R.id.id_equip_address);
                mAddress.setText(data.getEquipment().getRoom().getRoomName() == null ? "" : data.getEquipment().getRoom().getRoomName());
                TextView mFaultState = (TextView) vHolder.getView(R.id.id_equip_state);
                mFaultState.setText(Yw2Application.getInstance().getMapOption().get("9").get(data.getFaultState() + ""));
                TextView mEquipName = (TextView) vHolder.getView(R.id.tv_equip_name);
                mEquipName.setText(data.getEquipment().getEquipmentName());
                TextView mUser = (TextView) vHolder.getView(R.id.tv_user);
                mUser.setText(data.getUser().getRealName());
                TextView mTime = (TextView) vHolder.getView(R.id.tv_start_time);
                mTime.setText(DataUtil.timeFormat(data.getCreateTime(), "yyyy-MM-dd HH:mm"));
                TextView mFault = (TextView) vHolder.getView(R.id.tv_alarm);
                ImageView mFaultImg = (ImageView) vHolder.getView(R.id.img_alarm);
                if (data.getFaultType() == 1) {
                    mFault.setText("A类故障");
                    mFaultImg.setImageDrawable(findDrawById(R.drawable.fault_a));
                }
                if (data.getFaultType() == 2) {
                    mFault.setText("B类故障");
                    mFaultImg.setImageDrawable(findDrawById(R.drawable.fault_b));
                }
                if (data.getFaultType() == 3) {
                    mFault.setText("C类故障");
                    mFaultImg.setImageDrawable(findDrawById(R.drawable.fault_c));
                }
                TextView mContent = (TextView) vHolder.getView(R.id.tv_content);
                mContent.setText(data.getFaultDescript());
                final TextView mVoiceTime = (TextView) vHolder.getView(R.id.tv_seconds);
                final String[] images = new String[data.getFaultPics().size()];
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
                    mCountDownTimerUtils = new CountDownTimerUtils(mVoiceTime, data.getSoundTimescale() * 1000, 1000
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
                    if (mCountDownTimerUtils != null) {
                        mCountDownTimerUtils.cancel();
                    }
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
        adapter.setOnItemClickListener(new RVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), AlarmDetailActivity.class);
                intent.putExtra(ConstantStr.KEY_BUNDLE_STR, String.valueOf(mList.get(position).getFaultId()));
                startActivity(intent);
            }
        });
        mExpendRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == 1) {//正在拖动
                    isPlay = false;
                }
            }
        });
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
        hideEvLoading();
        mRecycleRefreshLoadLayout.setRefreshing(false);
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
        if (!TextUtils.isEmpty(mCalendar)) {
            mPresenter.getTodayFault(mCalendar, mCalendar, mAlarmType);
        } else {
            mPresenter.getFaultList(mAlarmType);
        }
    }

    @Override
    public void onLoadMore() {
        if (mList.size() > 1 && !isRefresh) {
            if (!TextUtils.isEmpty(mCalendar)) {
                mPresenter.getMoreTodayFault(mList.get(mList.size() - 1).getFaultId(), mCalendar, mCalendar, mAlarmType);
            } else {
                mPresenter.getMoreFaultList(mList.get(mList.size() - 1).getFaultId(), mAlarmType);
            }
        }
    }

    @Override
    public void setPresenter(EquipAlarmContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onPageChanged(int p) {
        isPlay = false;
        MediaPlayerManager.pause();
        if (mAlarmType != p) {
            int playCount = 0;
            for (int i = 0; i < mList.size(); i++) {
                if (mList.get(i).isPlay()) {
                    playCount++;
                }
                mList.get(i).setPlay(false);
            }
            if (playCount > 0) {
                mExpendRecycleView.getAdapter().notifyDataSetChanged();
            }
            return;
        }
        requestData();
    }

    @Override
    public void onDataChange(String data, int position) {

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
