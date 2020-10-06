package com.isuo.yw2application.view.main.task.overhaul;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.isuo.yw2application.R;
import com.isuo.yw2application.app.Yw2Application;
import com.isuo.yw2application.common.ConstantStr;
import com.isuo.yw2application.mode.bean.overhaul.OverhaulBean;
import com.isuo.yw2application.view.base.BaseActivity;
import com.isuo.yw2application.view.main.task.overhaul.detail.OverhaulDetailActivity;
import com.isuo.yw2application.view.main.task.overhaul.execute.OverhaulExecuteActivity;
import com.isuo.yw2application.view.main.task.overhaul.security.SecurityPackageActivity;
import com.sito.library.adapter.RVAdapter;
import com.sito.library.utils.CalendarUtil;
import com.sito.library.utils.DataUtil;
import com.sito.library.utils.SPHelper;
import com.sito.library.widget.TextViewVertical;
import com.wdullaer.materialdatetimepicker.date.DatePickerView;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 巡检列表
 * Created by zhangan on 2018/3/27.
 */

public class WorkOverhaulActivity extends BaseActivity implements DatePickerView.OnDateSetListener
        , OverhaulContract.View, SwipeRefreshLayout.OnRefreshListener {

    private LinearLayout mChooseDayLayout;
    private DatePickerView mDatePickerView;
    private TextView mYearTv;
    private TextView[] dayTvs = new TextView[7];
    private TextView[] dayWeekNumTvs = new TextView[7];
    private LinearLayout[] dayOfWeekLayout = new LinearLayout[7];
    private RecyclerView mRecyclerView;
    private RelativeLayout noDataLayout;

    private OverhaulContract.Presenter mPresenter;
    private List<Date> dateList = new ArrayList<>();
    private Calendar mCurrentDay;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String mDate;
    private List<OverhaulBean> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutAndToolbar(R.layout.activivity_inspection_work_list, "检修");
        new OverhaulPresenter(Yw2Application.getInstance().getWorkRepositoryComponent().getRepository(), this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar == null) {
            return;
        }
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolBarClick();
            }
        });
        mRecyclerView = findViewById(R.id.recycleViewId);
        swipeRefreshLayout = findViewById(R.id.swipeLayout);
        swipeRefreshLayout.setColorSchemeColors(findColorById(R.color.colorPrimary));
        swipeRefreshLayout.setOnRefreshListener(this);
        noDataLayout = findViewById(R.id.layout_no_data);
        mYearTv = findViewById(R.id.tv_year);
        mChooseDayLayout = findViewById(R.id.ll_choose_day);
        LinearLayout addDatePickLayout = findViewById(R.id.ll_choose_day_content);
        mDatePickerView = DatePickerView.newInstance(this, this);
        addDatePickLayout.addView(mDatePickerView);
        mCurrentDay = Calendar.getInstance(Locale.CHINA);
        dateList = CalendarUtil.getDaysOfWeek(mCurrentDay.getTime());

        dayTvs[0] = findViewById(R.id.tv_1);
        dayTvs[1] = findViewById(R.id.tv_2);
        dayTvs[2] = findViewById(R.id.tv_3);
        dayTvs[3] = findViewById(R.id.tv_4);
        dayTvs[4] = findViewById(R.id.tv_5);
        dayTvs[5] = findViewById(R.id.tv_6);
        dayTvs[6] = findViewById(R.id.tv_7);

        dayWeekNumTvs[0] = findViewById(R.id.tv_1_1);
        dayWeekNumTvs[1] = findViewById(R.id.tv_2_1);
        dayWeekNumTvs[2] = findViewById(R.id.tv_3_1);
        dayWeekNumTvs[3] = findViewById(R.id.tv_4_1);
        dayWeekNumTvs[4] = findViewById(R.id.tv_5_1);
        dayWeekNumTvs[5] = findViewById(R.id.tv_6_1);
        dayWeekNumTvs[6] = findViewById(R.id.tv_7_1);

        dayOfWeekLayout[0] = findViewById(R.id.ll_monday);
        dayOfWeekLayout[1] = findViewById(R.id.ll_tuesday);
        dayOfWeekLayout[2] = findViewById(R.id.ll_wednesday);
        dayOfWeekLayout[3] = findViewById(R.id.ll_thursday);
        dayOfWeekLayout[4] = findViewById(R.id.ll_friday);
        dayOfWeekLayout[5] = findViewById(R.id.ll_saturday);
        dayOfWeekLayout[6] = findViewById(R.id.ll_sunday);

        for (LinearLayout layout : dayOfWeekLayout) {
            layout.setOnClickListener(this);
        }
        findViewById(R.id.ll_choose_month_day).setOnClickListener(this);
        findViewById(R.id.ll_choose_day_empty).setOnClickListener(this);
        getDate(mCurrentDay.get(Calendar.YEAR), mCurrentDay.get(Calendar.MONTH) + 1, mCurrentDay.get(Calendar.DAY_OF_MONTH));
        mList = new ArrayList<>();
        initRecycleView();
        setDayToView();
    }

    private void initRecycleView() {
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        RVAdapter<OverhaulBean> adapter = new RVAdapter<OverhaulBean>(mRecyclerView, mList, R.layout.item_overhaul) {
            @Override
            public void showData(ViewHolder vHolder, OverhaulBean data, int position) {
                TextView tv_repair_name = (TextView) vHolder.getView(R.id.tv_repair_name);
                TextView tv_belong_place = (TextView) vHolder.getView(R.id.tv_belong_place);
                TextView tv_state = (TextView) vHolder.getView(R.id.tv_state);
                TextView tv_equip_name = (TextView) vHolder.getView(R.id.tv_equip_name);
                TextView tv_user = (TextView) vHolder.getView(R.id.tv_user);
                TextView tv_start_time = (TextView) vHolder.getView(R.id.tv_start_time);
                LinearLayout ll_start_task = (LinearLayout) vHolder.getView(R.id.ll_start_task);
                TextView tv_alarm = (TextView) vHolder.getView(R.id.tv_alarm);
                ImageView iv_state = (ImageView) vHolder.getView(R.id.iv_state);
                if (data.getEquipment() != null) {
                    if (TextUtils.isEmpty(data.getEquipment().getEquipmentSn())) {
                        tv_equip_name.setText(data.getEquipment().getEquipmentName());
                    } else {
                        String str = data.getEquipment().getEquipmentName() + "(" + data.getEquipment().getEquipmentSn() + ")";
                        tv_equip_name.setText(str);
                    }
                    tv_belong_place.setText(data.getEquipment().getRoom().getRoomName());
                }
                tv_start_time.setText(DataUtil.timeFormat(data.getStartTime(), "yyyy-MM-dd HH:mm"));
                if (data.getAddType() == 1) {
                    tv_alarm.setVisibility(View.GONE);
                    iv_state.setVisibility(View.GONE);
                } else {
                    if (data.getFault() != null) {
                        iv_state.setVisibility(View.VISIBLE);
                        tv_alarm.setVisibility(View.VISIBLE);
                        if (data.getFault().getFaultType() == 1) {
                            tv_alarm.setText("A类故障");
                            iv_state.setImageDrawable(findDrawById(R.drawable.fault_a));
                        } else if (data.getFault().getFaultType() == 2) {
                            tv_alarm.setText("B类故障");
                            iv_state.setImageDrawable(findDrawById(R.drawable.fault_b));
                        } else {
                            tv_alarm.setText("C类故障");
                            iv_state.setImageDrawable(findDrawById(R.drawable.fault_c));
                        }
                    }
                }
                boolean isIn = false;
                if (data.getRepairUsers() != null && data.getRepairUsers().size() > 0) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < data.getRepairUsers().size(); i++) {
                        if (data.getRepairUsers().get(i).getUser().getUserId() == Yw2Application.getInstance().getCurrentUser().getUserId()) {
                            isIn = true;
                        }
                        sb.append(data.getRepairUsers().get(i).getUser().getRealName());
                        if (i != data.getRepairUsers().size() - 1) {
                            sb.append("、");
                        }
                    }
                    tv_user.setText(sb.toString());
                }
                tv_repair_name.setText(data.getRepairName());
                TextView tv_repair_result = (TextView) vHolder.getView(R.id.tv_repair_result);
                if (data.getRepairResult() == 0) {
                    tv_repair_result.setVisibility(View.GONE);
                } else {
                    tv_repair_result.setVisibility(View.VISIBLE);
                    tv_repair_result.setText(MessageFormat.format("检修结果:{0}", Yw2Application.getInstance()
                            .getMapOption().get("4").get(String.valueOf(data.getRepairResult()))));
                }
                String state;
                switch (data.getRepairState()) {
                    case 1:
                        state = "待开始";
                        tv_state.setTextColor(findColorById(R.color.color_not_start));
                        ll_start_task.setVisibility(isIn ? View.VISIBLE : View.GONE);
                        break;
                    case 2:
                        tv_state.setTextColor(findColorById(R.color.color_start));
                        state = "进行中";
                        ll_start_task.setVisibility(isIn ? View.VISIBLE : View.GONE);
                        break;
                    default:
                        tv_state.setTextColor(findColorById(R.color.color_finish));
                        state = "已完成";
                        ll_start_task.setVisibility(View.GONE);
                        break;
                }
                tv_state.setText(state);
                ll_start_task.setTag(R.id.tag_position, data.getRepairId());
                ll_start_task.setTag(R.id.tag_position_1, data.getJobPackage() == null ? -1L : data.getJobPackage().getJobId());
                ll_start_task.setOnClickListener(onClickListener);
            }
        };
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new RVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                long repairId = mList.get(position).getRepairId();
                Intent intent = new Intent(WorkOverhaulActivity.this, OverhaulDetailActivity.class);
                intent.putExtra(ConstantStr.KEY_BUNDLE_LONG, repairId);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onDateSet(DatePickerView datePickerView, int year, int monthOfYear, int dayOfMonth) {
        mCurrentDay.set(year, monthOfYear, dayOfMonth);
        dateList = CalendarUtil.getDaysOfWeek(mCurrentDay.getTime());
        setDayToView();
        getDate(year, monthOfYear, dayOfMonth);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_calendar_close);
        mDatePickerView.startAnimation(animation);
        animation.setAnimationListener(animationListener);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_choose_month_day:
                if (mChooseDayLayout.getVisibility() == View.GONE) {
                    mChooseDayLayout.setVisibility(View.VISIBLE);
                    Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_calendar_open);
                    mDatePickerView.startAnimation(animation);
                } else {
                    Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_calendar_close);
                    mDatePickerView.startAnimation(animation);
                    animation.setAnimationListener(animationListener);
                }
                break;
            case R.id.ll_monday:
                mCurrentDay.setTime(dateList.get(0));
                setDayToView();
                break;
            case R.id.ll_tuesday:
                mCurrentDay.setTime(dateList.get(1));
                setDayToView();
                break;
            case R.id.ll_wednesday:
                mCurrentDay.setTime(dateList.get(2));
                setDayToView();
                break;
            case R.id.ll_thursday:
                mCurrentDay.setTime(dateList.get(3));
                setDayToView();
                break;
            case R.id.ll_friday:
                mCurrentDay.setTime(dateList.get(4));
                setDayToView();
                break;
            case R.id.ll_saturday:
                mCurrentDay.setTime(dateList.get(5));
                setDayToView();
                break;
            case R.id.ll_sunday:
                mCurrentDay.setTime(dateList.get(6));
                setDayToView();
                break;
            case R.id.ll_choose_day_empty:
                Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_calendar_close);
                mDatePickerView.startAnimation(animation);
                animation.setAnimationListener(animationListener);
                break;
        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            long repairId = (long) v.getTag(R.id.tag_position);
            long jobId = (long) v.getTag(R.id.tag_position_1);
            Intent intent = new Intent();
            if (!SPHelper.readBoolean(WorkOverhaulActivity.this, ConstantStr.SECURITY_OVERHAUL_INFO
                    , String.valueOf(repairId), false) && jobId != -1) {
                intent.setClass(WorkOverhaulActivity.this, SecurityPackageActivity.class);
            } else {
                intent.setClass(WorkOverhaulActivity.this, OverhaulExecuteActivity.class);
            }
            intent.putExtra(ConstantStr.KEY_BUNDLE_LONG, repairId);
            intent.putExtra(ConstantStr.KEY_BUNDLE_LONG_1, jobId);
            isClick = true;
            startActivity(intent);
        }
    };

    private boolean isClick;

    @Override
    public void onResume() {
        super.onResume();
        if (isClick) {
            onRefresh();
        }
        isClick = false;
    }


    private void setDayToView() {
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        for (int i = 0; i < dayTvs.length; i++) {
            calendar.setTime(dateList.get(i));
            if (calendar.get(Calendar.DAY_OF_WEEK) == mCurrentDay.get(Calendar.DAY_OF_WEEK)) {
                dayTvs[i].setTextColor(findColorById(R.color.colorWhite));
                dayWeekNumTvs[i].setTextColor(findColorById(R.color.colorWhite));
                dayOfWeekLayout[i].setBackgroundResource(R.drawable.day_green_gb);
            } else {
                dayTvs[i].setTextColor(findColorById(R.color.text333));
                dayWeekNumTvs[i].setTextColor(findColorById(R.color.gray_999999));
                dayOfWeekLayout[i].setBackgroundColor(findColorById(R.color.colorWhite));
            }
            dayTvs[i].setText(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
        }
        getDate(mCurrentDay.get(Calendar.YEAR), mCurrentDay.get(Calendar.MONTH), mCurrentDay.get(Calendar.DAY_OF_MONTH));
        onRefresh();
    }


    private void getDate(int year, int monthOfYear, int dayOfMonth) {
        Calendar newCalendar = Calendar.getInstance();
        newCalendar.set(year, monthOfYear, dayOfMonth);
        mDate = DataUtil.timeFormat(newCalendar.getTime().getTime(), "yyyy-MM-dd");
        mYearTv.setText(DataUtil.timeFormat(newCalendar.getTime().getTime(), "yyyy年MM月dd日"));
    }

    private Animation.AnimationListener animationListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            mChooseDayLayout.setVisibility(View.GONE);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };

    @Override
    public void showData(List<OverhaulBean> been) {
        mList.clear();
        mList.addAll(been);
        mRecyclerView.getAdapter().notifyDataSetChanged();
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
    public void noData() {
        mList.clear();
        if (mRecyclerView.getAdapter() == null) {
            return;
        }
        mRecyclerView.getAdapter().notifyDataSetChanged();
        noDataLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void setPresenter(OverhaulContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onRefresh() {
        if (mPresenter != null) {
            noDataLayout.setVisibility(View.GONE);
            mPresenter.getData(mDate);
        }
    }

}
