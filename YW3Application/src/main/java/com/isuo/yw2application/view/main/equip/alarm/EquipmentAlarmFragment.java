package com.isuo.yw2application.view.main.equip.alarm;

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

import com.isuo.yw2application.R;
import com.isuo.yw2application.app.Yw2Application;
import com.isuo.yw2application.common.ConstantStr;
import com.isuo.yw2application.mode.bean.check.FaultList;
import com.isuo.yw2application.utils.CountDownTimerUtils;
import com.isuo.yw2application.utils.MediaPlayerManager;
import com.isuo.yw2application.view.base.MvpFragment;
import com.isuo.yw2application.view.photo.ViewPagePhotoActivity;
import com.isuo.yw2application.widget.ShowImageLayout;
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

public class EquipmentAlarmFragment extends MvpFragment<EquipmentAlarmContact.Presenter> implements EquipmentAlarmContact.View
        , SwipeRefreshLayout.OnRefreshListener, RecycleRefreshLoadLayout.OnLoadListener {


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
        mExpendRecycleView = rootView.findViewById(R.id.recycleViewId);
        mNoDataLayout = rootView.findViewById(R.id.layout_no_data);
        mRecycleRefreshLoadLayout = rootView.findViewById(R.id.refreshLoadLayoutId);
        mExpendRecycleView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        mRecycleRefreshLoadLayout.setColorSchemeColors(findColorById(R.color.colorPrimary));
        mRecycleRefreshLoadLayout.setOnRefreshListener(this);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mList = new ArrayList<>();
        RVAdapter<FaultList> adapter = new RVAdapter<FaultList>(mExpendRecycleView, mList, R.layout.item_alarm) {
            @Override
            public void showData(ViewHolder vHolder, FaultList data, final int position) {
                TextView id_equip_address = (TextView) vHolder.getView(R.id.id_equip_address);
                id_equip_address.setText(data.getEquipment().getRoom().getRoomName());
                TextView tv_equip_name = (TextView) vHolder.getView(R.id.tv_equip_name);
                if (data.getEquipment() != null) {
                    if (TextUtils.isEmpty(data.getEquipment().getEquipmentSn())) {
                        tv_equip_name.setText(data.getEquipment().getEquipmentName());
                    } else {
                        tv_equip_name.setText(String.format("%s(%s)", data.getEquipment().getEquipmentName(), data.getEquipment().getEquipmentSn()));
                    }
                }
                TextView tv_user = (TextView) vHolder.getView(R.id.tv_user);
                tv_user.setText(data.getUser().getRealName());
                TextView tv_start_time = (TextView) vHolder.getView(R.id.tv_start_time);
                tv_start_time.setText(DataUtil.timeFormat(data.getCreateTime(), "yyyy-MM-dd HH:mm"));
                TextView id_content = (TextView) vHolder.getView(R.id.id_tv_voice_content);
                id_content.setText(data.getFaultDescript());
                TextView id_equip_state = (TextView) vHolder.getView(R.id.id_alarm_state);
                TextView tv_alarm = (TextView) vHolder.getView(R.id.tv_alarm_type);
                ImageView iv_state = (ImageView) vHolder.getView(R.id.iv_state);
                id_equip_state.setText(Yw2Application.getInstance().getMapOption().get("9")
                        .get(String.valueOf(data.getFaultState())));
                tv_alarm.setText(Yw2Application.getInstance().getMapOption().get("2")
                        .get(String.valueOf(data.getFaultType())));
                if (data.getFaultType() == 1) {
                    iv_state.setImageDrawable(findDrawById(R.drawable.fault_a));
                    id_equip_state.setTextColor(findColorById(R.color.fault_color_a));
                } else if (data.getFaultType() == 2) {
                    iv_state.setImageDrawable(findDrawById(R.drawable.fault_b));
                    id_equip_state.setTextColor(findColorById(R.color.fault_color_b));
                } else {
                    iv_state.setImageDrawable(findDrawById(R.drawable.fault_c));
                    id_equip_state.setTextColor(findColorById(R.color.fault_color_c));
                }
                ShowImageLayout re_image = (ShowImageLayout) vHolder.getView(R.id.show_image);
                String[] images = new String[data.getFaultPics().size()];
                for (int i = 0; i < data.getFaultPics().size(); i++) {
                    images[i] = data.getFaultPics().get(i).getPicUrl();
                }
                re_image.showImage(images);
                final TextView mVoiceTime = (TextView) vHolder.getView(R.id.tv_seconds);
                if (data.getSoundTimescale() == 0 || TextUtils.isEmpty(data.getVoiceUrl())) {
                    mVoiceTime.setVisibility(View.GONE);
                } else {
                    mVoiceTime.setVisibility(View.VISIBLE);
                }
                if (data.isPlay() && isPlay) {
                    mVoiceTime.setBackgroundResource(R.drawable.play_anim);
                    animation = (AnimationDrawable) mVoiceTime.getBackground();
                    mCountDownTimerUtils = new CountDownTimerUtils(mVoiceTime, data.getSoundTimescale() * 1000, 1000, data.getSoundTimescale() + "s", "#999999");
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
                    mVoiceTime.setText(String.format("%ss",data.getSoundTimescale()));
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
