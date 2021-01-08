package com.isuo.yw2application.view.main.alarm;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.isuo.yw2application.R;
import com.isuo.yw2application.app.Yw2Application;
import com.isuo.yw2application.common.ConstantStr;
import com.isuo.yw2application.mode.bean.check.FaultList;
import com.isuo.yw2application.mode.bean.fault.AlarmCount;
import com.isuo.yw2application.mode.bean.fault.FaultCount;
import com.isuo.yw2application.mode.bean.fault.FaultDayCountBean;
import com.isuo.yw2application.mode.bean.work.WorkState;
import com.isuo.yw2application.utils.CountDownTimerUtils;
import com.isuo.yw2application.utils.MediaPlayerManager;
import com.isuo.yw2application.view.base.MvpFragmentV4;
import com.isuo.yw2application.view.main.alarm.detail.AlarmDetailActivity;
import com.isuo.yw2application.view.main.alarm.equipalarm.EquipAlarmActivity;
import com.isuo.yw2application.view.main.alarm.fault.FaultActivity;
import com.isuo.yw2application.view.main.alarm.fault.today.TodayFaultActivity;
import com.isuo.yw2application.view.main.alarm.list.AlarmListActivity;
import com.isuo.yw2application.widget.ShowImageLayout;
import com.sito.library.adapter.RVAdapter;
import com.sito.library.utils.DataUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class AlarmFragment extends MvpFragmentV4<AlarmContract.Presenter> implements View.OnClickListener, AlarmContract.View {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private List<FaultList> alarmList;
    private TextView[] alarmCount = new TextView[5];

    private CountDownTimerUtils mCountDownTimerUtils;
    private AnimationDrawable animation;
    private boolean isPlay;

    public static AlarmFragment newInstance() {
        Bundle args = new Bundle();
        AlarmFragment fragment = new AlarmFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        alarmList = new ArrayList<>();
        new AlarmPresenter(Yw2Application.getInstance().getRepositoryComponent().getRepository()
                , Yw2Application.getInstance().getFaultRepositoryComponent().getRepository()
                , Yw2Application.getInstance().getWorkRepositoryComponent().getRepository(), this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_alarm, container, false);
        rootView.findViewById(R.id.reportAlarmIv).setOnClickListener(this);
        rootView.findViewById(R.id.rightTv).setOnClickListener(this);
        rootView.findViewById(R.id.layout_1).setOnClickListener(this);
        rootView.findViewById(R.id.layout_2).setOnClickListener(this);
        rootView.findViewById(R.id.layout_3).setOnClickListener(this);
        rootView.findViewById(R.id.layout_4).setOnClickListener(this);
        rootView.findViewById(R.id.layout_5).setOnClickListener(this);
        swipeRefreshLayout = rootView.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorScheme(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mPresenter != null) {
                    mPresenter.getAlarmList();
                    mPresenter.getAlarmCount();
                }
            }
        });
        alarmCount[0] = rootView.findViewById(R.id.alarmTv1);
        alarmCount[1] = rootView.findViewById(R.id.alarmTv2);
        alarmCount[2] = rootView.findViewById(R.id.alarmTv3);
        alarmCount[3] = rootView.findViewById(R.id.alarmTv4);
        alarmCount[4] = rootView.findViewById(R.id.alarmTv5);
        recyclerView = rootView.findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        recyclerView.setNestedScrollingEnabled(false);
        RVAdapter<FaultList> adapter = new RVAdapter<FaultList>(recyclerView, this.alarmList, R.layout.item_alarm) {
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
                    mVoiceTime.setText(String.format("%ss", data.getSoundTimescale()));
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
                        for (int i = 0; i < alarmList.size(); i++) {
                            if (position == i) {
                                alarmList.get(i).setPlay(true);
                            } else {
                                alarmList.get(i).setPlay(false);
                            }
                        }
                        recyclerView.getAdapter().notifyDataSetChanged();
                    }
                });
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new RVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), AlarmDetailActivity.class);
                intent.putExtra(ConstantStr.KEY_BUNDLE_STR, String.valueOf(alarmList.get(position).getFaultId()));
                startActivity(intent);
            }
        });
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mPresenter != null) {
            mPresenter.getAlarmList();
            mPresenter.getAlarmCount();
        }
    }


    @Override
    public void showFaultCount(FaultCount bean) {
        alarmCount[0].setText(String.valueOf(bean.getDayCount()));
        alarmCount[1].setText(String.valueOf(bean.getFlowingCount()));
        alarmCount[2].setText(String.valueOf(bean.getCloseCount()));
        alarmCount[3].setText(String.valueOf(bean.getRepairCount()));
        alarmCount[4].setText(String.valueOf(bean.getRelicFaultCount()));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.reportAlarmIv:
                startActivity(new Intent(getActivity(), FaultActivity.class));
                break;
            case R.id.rightTv:
                startActivity(new Intent(getActivity(), AlarmListActivity.class));
                break;
            case R.id.layout_1:
            case R.id.layout_2:
            case R.id.layout_3:
            case R.id.layout_4:
            case R.id.layout_5:
                int type = Integer.parseInt(view.getTag().toString());
                Intent intent = new Intent(getActivity(), EquipAlarmActivity.class);
                if (type == 0) {
//                    Calendar calendar = Calendar.getInstance();
//                    intent.putExtra(ConstantStr.KEY_BUNDLE_OBJECT, DataUtil.timeFormat(calendar.getTimeInMillis(), "yyyy-MM-dd"));
//                    intent.putExtra(ConstantStr.KEY_BUNDLE_STR, "今日故障");
                    startActivity(new Intent(getActivity(),TodayFaultActivity.class));
                    return;
                } else if (type == 1) {
                    Calendar calendar = Calendar.getInstance();
                    intent.putExtra(ConstantStr.KEY_BUNDLE_OBJECT, DataUtil.timeFormat(calendar.getTimeInMillis(), "yyyy-MM-dd"));
                    intent.putExtra(ConstantStr.KEY_BUNDLE_STR, "今日故障");
                } else {
                    intent.putExtra(ConstantStr.KEY_BUNDLE_STR, "遗留故障");
                }
                startActivity(intent);
                break;

        }
    }

    @Override
    public void showLoading() {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showFaultList(List<FaultList> list) {
        this.alarmList.clear();
        this.alarmList.addAll(list);
        recyclerView.getAdapter().notifyDataSetChanged();
    }


    @Override
    public void setPresenter(AlarmContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
