package com.sito.customer.view.home.work.today.fault;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sito.customer.R;
import com.sito.customer.app.CustomerApp;
import com.sito.customer.common.ConstantStr;
import com.sito.customer.mode.bean.check.FaultList;
import com.sito.customer.utils.CountDownTimerUtils;
import com.sito.customer.utils.MediaPlayerManager;
import com.sito.customer.view.BaseActivity;
import com.sito.customer.view.alarm.AlarmDetailActivity;
import com.sito.customer.widget.ShowImageLayout;
import com.sito.library.adapter.RVAdapter;
import com.sito.library.utils.DataUtil;
import com.sito.library.widget.TextViewVertical;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class TodayFaultActivity extends BaseActivity implements TodayFaultContract.View, SwipeRefreshLayout.OnRefreshListener {


    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private RelativeLayout noDataLayout;

    private TodayFaultContract.Presenter mPresenter;
    private String time;
    private CountDownTimerUtils mCountDownTimerUtils;
    private AnimationDrawable animation;
    private boolean isPlay;
    private List<FaultList> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        time = getIntent().getStringExtra(ConstantStr.KEY_BUNDLE_STR);
        setLayoutAndToolbar(R.layout.activity_today_doing, TextUtils.isEmpty(time) ? "遗留故障" : "今日故障");
        new TodayFaultPresenter(CustomerApp.getInstance().getWorkRepositoryComponent().getRepository(), this);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLayout);
        refreshLayout.setColorSchemeColors(findColorById(R.color.colorPrimary));
        refreshLayout.setOnRefreshListener(this);
        recyclerView = (RecyclerView) findViewById(R.id.recycleViewId);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        noDataLayout = (RelativeLayout) findViewById(R.id.layout_no_data);
        noDataLayout.setVisibility(View.VISIBLE);
        mList = new ArrayList<>();
        RVAdapter<FaultList> adapter = new RVAdapter<FaultList>(recyclerView, mList, R.layout.item_equip_alarm) {
            @Override
            public void showData(ViewHolder vHolder, final FaultList data, final int position) {
                TextView id_content = (TextView) vHolder.getView(R.id.id_tv_voicecontent);
                id_content.setText(data.getFaultDescript());
                TextView id_equip_address = (TextView) vHolder.getView(R.id.id_equip_address);
                TextView id_equip_state = (TextView) vHolder.getView(R.id.id_equip_state);
                TextView tv_equip_name = (TextView) vHolder.getView(R.id.tv_equip_name);
                TextView tv_user = (TextView) vHolder.getView(R.id.tv_user);
                TextView tv_start_time = (TextView) vHolder.getView(R.id.tv_start_time);
                TextViewVertical tv_alarm = (TextViewVertical) vHolder.getView(R.id.tv_alarm);
                vHolder.getView(R.id.rl_alarm_detail).setTag(R.id.tag_object, data.getFaultId());
                ImageView iv_state = (ImageView) vHolder.getView(R.id.iv_state);
                id_equip_address.setText(data.getEquipment().getRoom().getRoomName());
                id_equip_state.setText(CustomerApp.getInstance().getMapOption().get("9").get(String.valueOf(data.getFaultState())));
                if (data.getEquipment() != null) {
                    if (TextUtils.isEmpty(data.getEquipment().getEquipmentSn())) {
                        tv_equip_name.setText(data.getEquipment().getEquipmentName());
                    } else {
                        tv_equip_name.setText(data.getEquipment().getEquipmentName() + "(" + data.getEquipment().getEquipmentSn() + ")");
                    }
                }
                tv_user.setText(data.getUser().getRealName());
                tv_alarm.setText(CustomerApp.getInstance().getMapOption().get("2").get(String.valueOf(data.getFaultType())));
                tv_start_time.setText(MessageFormat.format("上报时间{0}", DataUtil.timeFormat(data.getCreateTime(), "yyyy-MM-dd HH:mm")));
                if (data.getFaultType() == 1) {
                    iv_state.setImageDrawable(findDrawById(R.drawable.work_a));
                } else if (data.getFaultType() == 2) {
                    iv_state.setImageDrawable(findDrawById(R.drawable.work_b));
                } else {
                    iv_state.setImageDrawable(findDrawById(R.drawable.work_c));
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
                    mCountDownTimerUtils = new CountDownTimerUtils(mVoiceTime, data.getSoundTimescale() * 1000
                            , 1000, data.getSoundTimescale() + "''", "#ffffff");
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
                        recyclerView.getAdapter().notifyDataSetChanged();
                    }
                });

            }
        };
        adapter.setOnItemClickListener(new RVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(TodayFaultActivity.this, AlarmDetailActivity.class);
                intent.putExtra(ConstantStr.KEY_BUNDLE_STR, String.valueOf(mList.get(position).getFaultId()));
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == 1) {//正在拖动
                    isPlay = false;
                }
            }
        });
        if (mPresenter != null) {
            onRefresh();
        }
    }

    @Override
    public void showLoading() {
        refreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void showData(List<FaultList> list) {
        mList.clear();
        isRefresh = false;
        mList.addAll(list);
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void noData() {
        mList.clear();
        recyclerView.getAdapter().notifyDataSetChanged();
        noDataLayout.setVisibility(View.VISIBLE);
    }

    boolean isRefresh;

    @Override
    public void onRefresh() {
        isPlay = false;
        isRefresh = true;
        noDataLayout.setVisibility(View.GONE);
        mPresenter.getData(time);
    }

    @Override
    public void setPresenter(TodayFaultContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.unSubscribe();
        }
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
        recyclerView.getAdapter().notifyDataSetChanged();
    }
}
