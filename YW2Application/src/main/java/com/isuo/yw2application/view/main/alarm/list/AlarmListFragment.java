package com.isuo.yw2application.view.main.alarm.list;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.isuo.yw2application.R;
import com.isuo.yw2application.app.Yw2Application;
import com.isuo.yw2application.common.ConstantStr;
import com.isuo.yw2application.mode.bean.check.FaultList;
import com.isuo.yw2application.mode.bean.equip.EquipType;
import com.isuo.yw2application.utils.CountDownTimerUtils;
import com.isuo.yw2application.utils.MediaPlayerManager;
import com.isuo.yw2application.view.base.MvpFragment;
import com.isuo.yw2application.view.main.alarm.detail.AlarmDetailActivity;
import com.isuo.yw2application.widget.ShowImageLayout;
import com.sito.library.adapter.RVAdapter;
import com.sito.library.utils.DataUtil;
import com.sito.library.widget.DateDialog;
import com.sito.library.widget.ExpendRecycleView;
import com.sito.library.widget.RecycleRefreshLoadLayout;
import com.sito.library.widget.TextViewVertical;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * 故障警报
 * Created by zhangan on 2017-06-30.
 */

public class AlarmListFragment extends MvpFragment<AlarmListContact.Presenter> implements AlarmListContact.View
        , View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, RecycleRefreshLoadLayout.OnLoadListener {

    //data
    private int mOpenPosition, mCurrentPosition;
    private boolean isOpenCondition;
    private EquipType mChooseEquipType;
    //view
    private Drawable mNormalDraw, mOpenDraw;
    private View[] conditionsViews = new View[4];
    private View[] conditionsDivisions = new View[4];
    private TextView[] conditionTitleTvs = new TextView[4];
    private LinearLayout[] conditionLLs = new LinearLayout[4];
    private Animation mOpenAnimation, mCloseAnimation;
    private ExpendRecycleView mRecyclerView;//recycle view
    private LinearLayout mConditionLayout, mConditionContentLayout1, mConditionContentLayout2;//条件
    private RecycleRefreshLoadLayout mRecycleRefreshLoadLayout;
    private TextView mStartTimeTv;
    private TextView mEndTimeTv;
    private List<FaultList> mList;
    private String equipmentTypeStr;
    private RelativeLayout noDataLayout;
    private RelativeLayout mOtherChooseTimeLayout;
    private CountDownTimerUtils mCountDownTimerUtils;
    private AnimationDrawable animation;
    private boolean isPlay;

    public static AlarmListFragment newInstance() {
        Bundle args = new Bundle();
        AlarmListFragment fragment = new AlarmListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new AlarmListPresenter(Yw2Application.getInstance().getFaultRepositoryComponent().getRepository(), this);
        mNormalDraw = findDrawById(R.drawable.bg_alarm_list_title_2);
        mOpenDraw = findDrawById(R.drawable.bg_alarm_list_title_3);
        mOpenAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_calendar_open);
        mCloseAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_calendar_close);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fmg_alarm_list, container, false);
        rootView.findViewById(R.id.frame_condition_1).setOnClickListener(this);
        rootView.findViewById(R.id.frame_condition_2).setOnClickListener(this);
        rootView.findViewById(R.id.frame_condition_3).setOnClickListener(this);
        rootView.findViewById(R.id.frame_condition_4).setOnClickListener(this);
        noDataLayout = rootView.findViewById(R.id.layout_no_data);
        mRecycleRefreshLoadLayout = rootView.findViewById(R.id.refreshLoadLayoutId);
        mRecycleRefreshLoadLayout.setColorSchemeColors(findColorById(R.color.colorPrimary));
        mRecycleRefreshLoadLayout.setOnRefreshListener(this);
        mRecycleRefreshLoadLayout.setOnLoadListener(this);
        View loadFooterView = LayoutInflater.from(getActivity()).inflate(R.layout.view_load_more_inspection, null);
        mRecycleRefreshLoadLayout.setViewFooter(loadFooterView);
        mRecyclerView = rootView.findViewById(R.id.recycleViewId);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));

        mConditionLayout = rootView.findViewById(R.id.ll_list);
        mConditionContentLayout1 = rootView.findViewById(R.id.ll_condition_content_1);
        mConditionContentLayout2 = rootView.findViewById(R.id.ll_condition_content_2);
        mConditionContentLayout2.setOnClickListener(this);
        conditionTitleTvs[0] = rootView.findViewById(R.id.tv_condition_1);
        conditionTitleTvs[1] = rootView.findViewById(R.id.tv_condition_2);
        conditionTitleTvs[2] = rootView.findViewById(R.id.tv_condition_3);
        conditionTitleTvs[3] = rootView.findViewById(R.id.tv_condition_4);

        conditionLLs[0] = rootView.findViewById(R.id.ll_condition_1);
        conditionLLs[1] = rootView.findViewById(R.id.ll_condition_2);
        conditionLLs[2] = rootView.findViewById(R.id.ll_condition_3);
        conditionLLs[3] = rootView.findViewById(R.id.ll_condition_4);

        conditionsDivisions[0] = rootView.findViewById(R.id.view_division_white_1);
        conditionsDivisions[1] = rootView.findViewById(R.id.view_division_white_2);
        conditionsDivisions[2] = rootView.findViewById(R.id.view_division_white_3);
        conditionsDivisions[3] = rootView.findViewById(R.id.view_division_white_4);

        initConditionView();
        return rootView;
    }

    private ImageView[] conditionIcon2 = new ImageView[3];
    private TextView[] conditionContentTv2 = new TextView[3];
    private ImageView[] conditionIcon3 = new ImageView[4];
    private TextView[] conditionContentTv3 = new TextView[4];
    private ImageView[] conditionIcon4 = new ImageView[5];
    private TextView[] conditionContentTv4 = new TextView[5];

    @SuppressLint("InflateParams")
    private void initConditionView() {
        conditionsViews[0] = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.layout_condition_1, null);
        conditionsViews[1] = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.layout_condition_2, null);
        conditionsViews[1].findViewById(R.id.ll_alarm_state_condition2_1).setOnClickListener(this);
        conditionsViews[1].findViewById(R.id.ll_alarm_state_condition2_2).setOnClickListener(this);
        conditionsViews[1].findViewById(R.id.ll_alarm_state_condition2_3).setOnClickListener(this);

        conditionIcon2[0] = (ImageView) conditionsViews[1].findViewById(R.id.iv_condition_2_icon_1);
        conditionIcon2[1] = (ImageView) conditionsViews[1].findViewById(R.id.iv_condition_2_icon_2);
        conditionIcon2[2] = (ImageView) conditionsViews[1].findViewById(R.id.iv_condition_2_icon_3);

        conditionContentTv2[0] = (TextView) conditionsViews[1].findViewById(R.id.tv_alarm_state_condition2_1);
        conditionContentTv2[1] = (TextView) conditionsViews[1].findViewById(R.id.tv_alarm_state_condition2_2);
        conditionContentTv2[2] = (TextView) conditionsViews[1].findViewById(R.id.tv_alarm_state_condition2_3);

        conditionsViews[2] = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.layout_condition_3, null);
        conditionsViews[2].findViewById(R.id.tv_alarm_state_condition3_1).setOnClickListener(this);
        conditionsViews[2].findViewById(R.id.tv_alarm_state_condition3_2).setOnClickListener(this);
        conditionsViews[2].findViewById(R.id.tv_alarm_state_condition3_3).setOnClickListener(this);
        conditionsViews[2].findViewById(R.id.ll_alarm_state_condition3_4).setOnClickListener(this);

        conditionIcon3[0] = (ImageView) conditionsViews[2].findViewById(R.id.iv_condition_3_icon_1);
        conditionIcon3[1] = (ImageView) conditionsViews[2].findViewById(R.id.iv_condition_3_icon_2);
        conditionIcon3[2] = (ImageView) conditionsViews[2].findViewById(R.id.iv_condition_3_icon_3);
        conditionIcon3[3] = (ImageView) conditionsViews[2].findViewById(R.id.iv_condition_3_icon_4);

        conditionContentTv3[0] = (TextView) conditionsViews[2].findViewById(R.id.tv_alarm_state_condition3_1);
        conditionContentTv3[1] = (TextView) conditionsViews[2].findViewById(R.id.tv_alarm_state_condition3_2);
        conditionContentTv3[2] = (TextView) conditionsViews[2].findViewById(R.id.tv_alarm_state_condition3_3);
        conditionContentTv3[3] = (TextView) conditionsViews[2].findViewById(R.id.tv_alarm_state_condition3_4);

        conditionsViews[3] = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.layout_condition_4, null);
        mStartTimeTv = (TextView) conditionsViews[3].findViewById(R.id.tv_choose_start_day);
        mEndTimeTv = (TextView) conditionsViews[3].findViewById(R.id.tv_choose_end_day);
        conditionsViews[3].findViewById(R.id.ll_alarm_state_condition4_1).setOnClickListener(this);
        conditionsViews[3].findViewById(R.id.ll_alarm_state_condition4_2).setOnClickListener(this);
        conditionsViews[3].findViewById(R.id.ll_alarm_state_condition4_3).setOnClickListener(this);
        conditionsViews[3].findViewById(R.id.ll_alarm_state_condition4_4).setOnClickListener(this);
        conditionsViews[3].findViewById(R.id.ll_alarm_state_condition4_5).setOnClickListener(this);
        conditionsViews[3].findViewById(R.id.tv_choose_start_day).setOnClickListener(this);
        conditionsViews[3].findViewById(R.id.tv_choose_end_day).setOnClickListener(this);
        conditionsViews[3].findViewById(R.id.tv_reset).setOnClickListener(this);
        conditionsViews[3].findViewById(R.id.tv_sure).setOnClickListener(this);

        conditionContentTv4[0] = (TextView) conditionsViews[3].findViewById(R.id.tv_alarm_state_condition4_1);
        conditionContentTv4[1] = (TextView) conditionsViews[3].findViewById(R.id.tv_alarm_state_condition4_2);
        conditionContentTv4[2] = (TextView) conditionsViews[3].findViewById(R.id.tv_alarm_state_condition4_3);
        conditionContentTv4[3] = (TextView) conditionsViews[3].findViewById(R.id.tv_alarm_state_condition4_4);
        conditionContentTv4[4] = (TextView) conditionsViews[3].findViewById(R.id.tv_alarm_state_condition4_5);
        mOtherChooseTimeLayout = (RelativeLayout) conditionsViews[3].findViewById(R.id.rl_choose_other_time);
        conditionIcon4[0] = (ImageView) conditionsViews[3].findViewById(R.id.iv_condition_4_icon_1);
        conditionIcon4[1] = (ImageView) conditionsViews[3].findViewById(R.id.iv_condition_4_icon_2);
        conditionIcon4[2] = (ImageView) conditionsViews[3].findViewById(R.id.iv_condition_4_icon_3);
        conditionIcon4[3] = (ImageView) conditionsViews[3].findViewById(R.id.iv_condition_4_icon_4);
        conditionIcon4[4] = (ImageView) conditionsViews[3].findViewById(R.id.iv_condition_4_icon_5);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mList = new ArrayList<>();
        RVAdapter<FaultList> adapter = new RVAdapter<FaultList>(mRecyclerView, mList, R.layout.item_equip_alarm) {
            @Override
            public void showData(ViewHolder vHolder, final FaultList data, final int position) {
                TextView id_content = (TextView) vHolder.getView(R.id.id_tv_voicecontent);
                id_content.setText(data.getFaultDescript());
                TextView id_equip_address = (TextView) vHolder.getView(R.id.id_equip_address);
                TextView id_equip_state = (TextView) vHolder.getView(R.id.id_equip_state);
                TextView tv_equip_name = (TextView) vHolder.getView(R.id.tv_equip_name);
                TextView tv_user = (TextView) vHolder.getView(R.id.tv_user);
                TextView tv_start_time = (TextView) vHolder.getView(R.id.tv_start_time);
                TextView tv_alarm = (TextView) vHolder.getView(R.id.tv_alarm);
                vHolder.getView(R.id.rl_alarm_detail).setTag(R.id.tag_object, data.getFaultId());
                ImageView iv_state = (ImageView) vHolder.getView(R.id.iv_state);
                id_equip_address.setText(data.getEquipment().getRoom().getRoomName());
                id_equip_state.setText(Yw2Application.getInstance().getMapOption().get("9").get(String.valueOf(data.getFaultState())));
                if (data.getEquipment() != null) {
                    if (TextUtils.isEmpty(data.getEquipment().getEquipmentSn())) {
                        tv_equip_name.setText(data.getEquipment().getEquipmentName());
                    } else {
                        tv_equip_name.setText(MessageFormat.format("{0}({1})", data.getEquipment().getEquipmentName(), data.getEquipment().getEquipmentSn()));
                    }
                }
                tv_user.setText(data.getUser().getRealName());
                tv_alarm.setText(Yw2Application.getInstance().getMapOption().get("2").get(String.valueOf(data.getFaultType())));
                tv_start_time.setText(MessageFormat.format("{0}", DataUtil.timeFormat(data.getCreateTime(), "yyyy-MM-dd HH:mm")));
                if (data.getFaultType() == 1) {
                    iv_state.setImageDrawable(findDrawById(R.drawable.fault_a));
                } else if (data.getFaultType() == 2) {
                    iv_state.setImageDrawable(findDrawById(R.drawable.fault_b));
                } else {
                    iv_state.setImageDrawable(findDrawById(R.drawable.fault_c));
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
                            , 1000, data.getSoundTimescale() + "s", "#999999");
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
                        mRecyclerView.getAdapter().notifyDataSetChanged();
                    }
                });

            }
        };
        adapter.setOnItemClickListener(new RVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), AlarmDetailActivity.class);
                intent.putExtra(ConstantStr.KEY_BUNDLE_STR, String.valueOf(mList.get(position).getFaultId()));
                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
            mPresenter.getEquipmentType();
        }
    }

    @Override
    public void setPresenter(AlarmListContact.Presenter presenter) {
        mPresenter = presenter;
    }

    @Nullable
    private String chooseAlarmType = null;
    @Nullable
    private String chooseAlarmState = null;
    @Nullable
    private String chooseAlarmTime = null;
    @Nullable
    private String alarmStartTimeStr = null;
    @Nullable
    private String alarmEndTimeStr = null;
    @Nullable
    private Calendar calendarStartTime = null;
    @Nullable
    private Calendar calendarEndTime = null;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.frame_condition_1:
                clickConditionLayout(0);
                break;
            case R.id.frame_condition_2:
                clickConditionLayout(1);
                break;
            case R.id.frame_condition_3:
                clickConditionLayout(2);
                break;
            case R.id.frame_condition_4:
                clickConditionLayout(3);
                break;
            case R.id.ll_condition_content_2:
                closeCondition(mOpenPosition);
                break;
            case R.id.ll_alarm_state_condition2_1:
                chooseAlarmType("1");
                closeCondition(1);
                break;
            case R.id.ll_alarm_state_condition2_2:
                chooseAlarmType("2");
                closeCondition(1);
                break;
            case R.id.ll_alarm_state_condition2_3:
                chooseAlarmType("3");
                closeCondition(1);
                break;
            case R.id.tv_alarm_state_condition3_1:
                changeAlarmState("1");
                closeCondition(2);
                break;
            case R.id.tv_alarm_state_condition3_2:
                changeAlarmState("2");
                closeCondition(2);
                break;
            case R.id.tv_alarm_state_condition3_3:
                changeAlarmState("3");
                closeCondition(2);
                break;
            case R.id.ll_alarm_state_condition3_4:
                changeAlarmState("4");
                closeCondition(2);
                break;
            case R.id.ll_alarm_state_condition4_1:
                //当天
                changeAlarmTime("1");
                Calendar calendar1 = Calendar.getInstance(Locale.CHINA);
                calendarStartTime = calendar1;
                calendarEndTime = calendar1;
                settingTime(calendarStartTime, calendarEndTime);
                closeCondition(3);
                break;
            case R.id.ll_alarm_state_condition4_2:
                //最近三天
                changeAlarmTime("2");
                calendarEndTime = Calendar.getInstance(Locale.CHINA);
                Calendar calendar2_start = Calendar.getInstance(Locale.CHINA);
                calendar2_start.add(Calendar.DATE, -3);
                calendarStartTime = calendar2_start;
                settingTime(calendarStartTime, calendarEndTime);
                closeCondition(3);
                break;
            case R.id.ll_alarm_state_condition4_3:
                //最近一周
                changeAlarmTime("3");
                calendarEndTime = Calendar.getInstance(Locale.CHINA);
                Calendar calendar3 = Calendar.getInstance(Locale.CHINA);
                calendar3.add(Calendar.DATE, -7);
                calendarStartTime = calendar3;
                settingTime(calendarStartTime, calendarEndTime);
                closeCondition(3);
                break;
            case R.id.ll_alarm_state_condition4_4:
                //最近一个月
                changeAlarmTime("4");
                calendarEndTime = Calendar.getInstance(Locale.CHINA);
                Calendar calendar4 = Calendar.getInstance(Locale.CHINA);
                calendar4.add(Calendar.MONTH, -1);
                calendarStartTime = calendar4;
                settingTime(calendarStartTime, calendarEndTime);
                closeCondition(3);
                break;
            case R.id.ll_alarm_state_condition4_5:
                changeAlarmTime("5");
                break;
            case R.id.tv_choose_start_day:
                //选择开始时间
                final DateDialog dateDlg;
                if (calendarStartTime == null) {
                    Calendar calendar = Calendar.getInstance(Locale.CHINA);
                    dateDlg = new DateDialog(getActivity(), R.style.MyDateDialog, calendar.get(Calendar.YEAR)
                            , (calendar.get(Calendar.MONTH) + 1), calendar.get(Calendar.DAY_OF_MONTH));
                } else {
                    dateDlg = new DateDialog(getActivity(), R.style.MyDateDialog, calendarStartTime.get(Calendar.YEAR)
                            , (calendarStartTime.get(Calendar.MONTH) + 1), calendarStartTime.get(Calendar.DAY_OF_MONTH));
                }
                dateDlg.setConfirmButton(getString(R.string.ok), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        calendarStartTime = dateDlg.getDate();
                        setDateToTv(calendarStartTime, calendarEndTime);
                    }
                });
                dateDlg.setBackButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dateDlg.cancel();
                    }
                });
                dateDlg.show();
                break;
            case R.id.tv_choose_end_day:
                //选择结束时间
                final DateDialog dateEndDlg;
                if (calendarEndTime == null) {
                    Calendar calendar = Calendar.getInstance(Locale.CHINA);
                    dateEndDlg = new DateDialog(getActivity(), R.style.MyDateDialog, calendar.get(Calendar.YEAR)
                            , (calendar.get(Calendar.MONTH) + 1), calendar.get(Calendar.DAY_OF_MONTH));
                } else {
                    dateEndDlg = new DateDialog(getActivity(), R.style.MyDateDialog, calendarEndTime.get(Calendar.YEAR)
                            , (calendarEndTime.get(Calendar.MONTH) + 1), calendarEndTime.get(Calendar.DAY_OF_MONTH));
                }
                dateEndDlg.setConfirmButton(getString(R.string.ok), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        calendarEndTime = dateEndDlg.getDate();
                        setDateToTv(calendarStartTime, calendarEndTime);
                    }
                });
                dateEndDlg.setBackButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dateEndDlg.cancel();
                    }
                });
                dateEndDlg.show();
                break;
        }
    }

    private String[] alarmTypes = new String[]{"A类事件", "B类事件", "C类事件"};
    private String[] alarmState = new String[]{"待处理", "处理中", "关闭", "转检修"};
    private String[] alarmTime = new String[]{"当天", "最近三天", "最近一周", "最近一月", "其他"};

    private void chooseAlarmType(String type) {
        if (chooseAlarmType != null) {
            if (chooseAlarmType.equals(type)) {
                chooseAlarmType = null;
                conditionTitleTvs[1].setText("事件等级");
            } else {
                conditionTitleTvs[1].setText(alarmTypes[Integer.parseInt(type) - 1]);
                chooseAlarmType = type;
            }
        } else {
            conditionTitleTvs[1].setText(alarmTypes[Integer.parseInt(type) - 1]);
            chooseAlarmType = type;
        }
        if (chooseAlarmType == null) {
            for (ImageView view : conditionIcon2) {
                view.setVisibility(View.INVISIBLE);
            }
            for (TextView textView : conditionContentTv2) {
                textView.setTextColor(findColorById(R.color.color535862));
            }
        } else {
            for (int i = 0; i < conditionIcon2.length; i++) {
                if (i == (Integer.parseInt(type) - 1)) {
                    conditionIcon2[i].setVisibility(View.VISIBLE);
                    conditionContentTv2[i].setTextColor(findColorById(R.color.color5F9FF7));
                } else {
                    conditionIcon2[i].setVisibility(View.INVISIBLE);
                    conditionContentTv2[i].setTextColor(findColorById(R.color.color535862));
                }
            }
        }
    }

    private void changeAlarmState(String type) {
        if (chooseAlarmState != null) {
            if (chooseAlarmState.equals(type)) {
                chooseAlarmState = null;
                conditionTitleTvs[2].setText("事件状态");
            } else {
                conditionTitleTvs[2].setText(alarmState[Integer.parseInt(type) - 1]);
                chooseAlarmState = type;
            }
        } else {
            conditionTitleTvs[2].setText(alarmState[Integer.parseInt(type) - 1]);
            chooseAlarmState = type;
        }
        if (chooseAlarmState == null) {
            for (ImageView view : conditionIcon3) {
                view.setVisibility(View.INVISIBLE);
            }
            for (TextView textView : conditionContentTv3) {
                textView.setTextColor(findColorById(R.color.color535862));
            }
        } else {
            for (int i = 0; i < conditionIcon3.length; i++) {
                if (i == (Integer.parseInt(type) - 1)) {
                    conditionIcon3[i].setVisibility(View.VISIBLE);
                    conditionContentTv3[i].setTextColor(findColorById(R.color.color5F9FF7));
                } else {
                    conditionIcon3[i].setVisibility(View.INVISIBLE);
                    conditionContentTv3[i].setTextColor(findColorById(R.color.color535862));
                }
            }
        }
    }

    private void changeAlarmTime(String type) {
        if (chooseAlarmTime != null) {
            if (chooseAlarmTime.equals(type)) {
                chooseAlarmTime = null;
                alarmStartTimeStr = null;
                alarmEndTimeStr = null;
                conditionTitleTvs[3].setText("上报时间");
                mOtherChooseTimeLayout.setVisibility(View.GONE);
            } else {
                conditionTitleTvs[3].setText(alarmTime[Integer.valueOf(type) - 1]);
                chooseAlarmTime = type;
                if (type.equals("5")) {
                    alarmStartTimeStr = null;
                    alarmEndTimeStr = null;
                    mOtherChooseTimeLayout.setVisibility(View.VISIBLE);
                }
            }
        } else {
            conditionTitleTvs[3].setText(alarmTime[Integer.valueOf(type) - 1]);
            chooseAlarmTime = type;
            if (type.equals("5")) {
                alarmStartTimeStr = null;
                alarmEndTimeStr = null;
                mOtherChooseTimeLayout.setVisibility(View.VISIBLE);
            }
        }
        if (chooseAlarmTime == null) {
            for (ImageView view : conditionIcon4) {
                view.setVisibility(View.INVISIBLE);
            }
            for (TextView textView : conditionContentTv4) {
                textView.setTextColor(findColorById(R.color.color535862));
            }
        } else {
            for (int i = 0; i < conditionIcon4.length; i++) {
                if (i == (Integer.valueOf(type) - 1)) {
                    conditionIcon4[i].setVisibility(View.VISIBLE);
                    conditionContentTv4[i].setTextColor(findColorById(R.color.color5F9FF7));
                } else {
                    conditionIcon4[i].setVisibility(View.INVISIBLE);
                    conditionContentTv4[i].setTextColor(findColorById(R.color.color535862));
                }
            }
        }
    }


    private void clickConditionLayout(int position) {
        if (isOpenCondition) {
            closeCondition(position);
        } else {
            openCondition(position);
        }
    }

    /**
     * 关闭条件选择界面
     *
     * @param position 位置
     */
    private void closeCondition(int position) {
        conditionsViews[mOpenPosition].startAnimation(mCloseAnimation);
        mCurrentPosition = position;
        mCloseAnimation.setAnimationListener(mCloseAnimationListener);
        isOpenCondition = !isOpenCondition;
    }

    /**
     * 开启条件选择界面
     *
     * @param position 位置
     */
    private void openCondition(int position) {
        mConditionLayout.setVisibility(View.VISIBLE);
        mConditionContentLayout1.removeAllViews();
        mConditionContentLayout1.addView(conditionsViews[position]);
        mOpenPosition = position;
        showTitleBackground();
        conditionsViews[position].startAnimation(mOpenAnimation);
        isOpenCondition = !isOpenCondition;
    }

    private Animation.AnimationListener mCloseAnimationListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            mConditionContentLayout1.removeAllViews();
            if (mCurrentPosition != mOpenPosition) {
                openCondition(mCurrentPosition);
            } else {
                closeTitleBackground();
                mConditionLayout.setVisibility(View.GONE);
                onRefresh();
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };

    private void showTitleBackground() {
        for (int i = 0; i < conditionTitleTvs.length; i++) {
            if (i == mOpenPosition) {
                conditionLLs[i].setBackground(mOpenDraw);
                conditionTitleTvs[i].setBackground(null);
                conditionsDivisions[i].setBackgroundColor(findColorById(R.color.colorWhite));
            } else {
                conditionTitleTvs[i].setBackground(mNormalDraw);
                conditionLLs[i].setBackground(null);
                conditionsDivisions[i].setBackgroundColor(findColorById(R.color.color_division));
            }
        }
    }

    private void closeTitleBackground() {
        for (int i = 0; i < conditionTitleTvs.length; i++) {
            conditionTitleTvs[i].setBackground(mNormalDraw);
            conditionLLs[i].setBackground(null);
            conditionsDivisions[i].setBackgroundColor(findColorById(R.color.color_division));
        }

    }

    @Override
    public void showEquipmentType(final List<EquipType> equipmentTypeBeen) {
        final RecyclerView recyclerView = (RecyclerView) conditionsViews[0].findViewById(R.id.recycleViewId);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        RVAdapter<EquipType> adapter = new RVAdapter<EquipType>(recyclerView, equipmentTypeBeen, R.layout.item_equip_type_1) {
            @Override
            public void showData(ViewHolder vHolder, EquipType data, int position) {
                TextView name = (TextView) vHolder.getView(R.id.tv_equip_name);
                ImageView icon = (ImageView) vHolder.getView(R.id.icon);
                name.setText(data.getEquipmentTypeName());
                if (data.isSelect()) {
                    icon.setVisibility(View.VISIBLE);
                    name.setTextColor(findColorById(R.color.color5F9FF7));
                } else {
                    name.setTextColor(findColorById(R.color.color535862));
                    icon.setVisibility(View.INVISIBLE);
                }
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new RVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                for (int i = 0; i < equipmentTypeBeen.size(); i++) {
                    if (position == i) {
                        equipmentTypeBeen.get(i).setSelect(!equipmentTypeBeen.get(i).isSelect());
                    } else {
                        equipmentTypeBeen.get(i).setSelect(false);
                    }
                }
                String title = "设备类型";
                equipmentTypeStr = null;
                for (int i = 0; i < equipmentTypeBeen.size(); i++) {
                    if (equipmentTypeBeen.get(i).isSelect()) {
                        equipmentTypeStr = String.valueOf(equipmentTypeBeen.get(i).getEquipmentTypeId());
                        title = equipmentTypeBeen.get(i).getEquipmentTypeName();
                        break;
                    }
                }
                conditionTitleTvs[0].setText(title);
                recyclerView.getAdapter().notifyDataSetChanged();
                closeCondition(0);
            }
        });
    }

    @Override
    public void showFaultList(List<FaultList> been) {
        mList.clear();
        isRefresh = false;
        mList.addAll(been);
        mRecyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void showFaultListMore(List<FaultList> been) {
        mList.addAll(been);
        mRecyclerView.getAdapter().notifyDataSetChanged();
    }

    boolean isRefresh;

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

    private void setDateToTv(@Nullable Calendar start, @Nullable Calendar end) {
        if (start != null) {
            mStartTimeTv.setText(getDate(start.get(Calendar.YEAR), start.get(Calendar.MONTH), start.get(Calendar.DAY_OF_MONTH)));
        } else {
            mStartTimeTv.setText("选择开始时间");
        }
        if (end != null) {
            mEndTimeTv.setText(getDate(end.get(Calendar.YEAR), end.get(Calendar.MONTH), end.get(Calendar.DAY_OF_MONTH)));
        } else {
            mEndTimeTv.setText("选择结束时间");
        }
        if (start != null && end != null) {
            settingTime(calendarStartTime, calendarEndTime);
            closeCondition(3);
        }
    }

    private String getDate(int year, int monthOfYear, int dayOfMonth) {
        monthOfYear = monthOfYear + 1;
        StringBuilder sb = new StringBuilder();
        sb.append(String.valueOf(year)).append("-");
        if (monthOfYear < 10) {
            sb.append("0").append(String.valueOf(monthOfYear)).append("-");
        } else {
            sb.append(String.valueOf(monthOfYear)).append("-");
        }
        if (dayOfMonth < 10) {
            sb.append("0").append(String.valueOf(dayOfMonth));

        } else {
            sb.append(MessageFormat.format("{0}", String.valueOf(dayOfMonth)));
        }
        return sb.toString();
    }

    private void settingTime(Calendar start, Calendar end) {
        if (start != null) {
            alarmStartTimeStr = getDate(start.get(Calendar.YEAR), start.get(Calendar.MONTH), start.get(Calendar.DAY_OF_MONTH));
        }
        if (end != null) {
            alarmEndTimeStr = getDate(end.get(Calendar.YEAR), end.get(Calendar.MONTH), end.get(Calendar.DAY_OF_MONTH));
        }
    }


    @Override
    public void onRefresh() {
        isPlay = false;
        isRefresh = true;
        noDataLayout.setVisibility(View.GONE);
        mRecycleRefreshLoadLayout.setNoMoreData(false);
        requestFaultList(false);
    }

    @Override
    public void onLoadMore() {
        if (isRefresh || mList.size() == 0) {
            return;
        }
        requestFaultList(true);
    }

    private void requestFaultList(boolean loadMore) {
        if (mChooseEquipType != null) {
            equipmentTypeStr = String.valueOf(mChooseEquipType.getEquipmentTypeId());
        }
        if (mPresenter != null) {
            if (loadMore) {
                mPresenter.getFaultList(equipmentTypeStr, chooseAlarmType, chooseAlarmState, alarmStartTimeStr, alarmEndTimeStr
                        , String.valueOf(mList.get(mList.size() - 1).getFaultId()));
            } else {
                mPresenter.getFaultList(equipmentTypeStr, chooseAlarmType, chooseAlarmState, alarmStartTimeStr, alarmEndTimeStr);
            }
        }
    }

    View.OnClickListener startAlarm = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int faultId = (int) v.getTag(R.id.tag_object);
            Intent intent = new Intent(getActivity(), AlarmDetailActivity.class);
            intent.putExtra(ConstantStr.KEY_BUNDLE_STR, String.valueOf(faultId));
            startActivity(intent);
        }
    };

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
        mRecyclerView.getAdapter().notifyDataSetChanged();
    }
}
