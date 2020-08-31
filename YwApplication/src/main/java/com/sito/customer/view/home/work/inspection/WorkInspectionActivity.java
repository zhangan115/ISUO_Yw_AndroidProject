package com.sito.customer.view.home.work.inspection;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sito.customer.R;
import com.sito.customer.app.CustomerApp;
import com.sito.customer.common.ConstantInt;
import com.sito.customer.common.ConstantStr;
import com.sito.customer.mode.bean.work.InspectionBean;
import com.sito.customer.view.BaseActivity;
import com.sito.customer.view.home.work.inspection.detial.InspectDetailActivity;
import com.sito.customer.view.home.work.inspection.security.SecurityPackageActivity;
import com.sito.customer.view.home.work.inspection.work.InspectionRoomActivity;
import com.sito.library.adapter.RVAdapter;
import com.sito.library.utils.CalendarUtil;
import com.sito.library.utils.DataUtil;
import com.sito.library.utils.SPHelper;
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

public class WorkInspectionActivity extends BaseActivity implements DatePickerView.OnDateSetListener, InspectionContract.View
        , SwipeRefreshLayout.OnRefreshListener {

    private LinearLayout mChooseDayLayout;
    private DatePickerView mDatePickerView;
    private TextView mYearTv, mMonthTv, mDayTv;
    private TextView[] dayTvs = new TextView[7];
    private LinearLayout[] dayOfWeekLayout = new LinearLayout[7];
    private RecyclerView mRecyclerView;
    private RelativeLayout noDataLayout;

    private InspectionContract.Presenter mPresenter;
    private List<Date> dateList = new ArrayList<>();
    private Calendar mCurrentDay;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String mDate;
    private int inspectionType;//巡检类型
    private List<InspectionBean> mList;

    private int[] icons = new int[]{R.drawable.work_day_icon
            , R.drawable.work_week_icon
            , R.drawable.work_month_icon
            , R.drawable.work_special_icon};
    private String[] inspectionTypeStr = new String[]{"日检", "周检", "月检", "特检"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String title = getIntent().getStringExtra(ConstantStr.KEY_BUNDLE_STR);
        inspectionType = getIntent().getIntExtra(ConstantStr.KEY_BUNDLE_INT, -1);
        setLayoutAndToolbar(R.layout.activivity_inspection_work_list,  "巡检" );
        new InspectionPresenter(CustomerApp.getInstance().getWorkRepositoryComponent().getRepository(), this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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
        mRecyclerView = (RecyclerView) findViewById(R.id.recycleViewId);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLayout);
        swipeRefreshLayout.setColorSchemeColors(findColorById(R.color.colorPrimary));
        swipeRefreshLayout.setOnRefreshListener(this);
        noDataLayout = (RelativeLayout) findViewById(R.id.layout_no_data);
        mYearTv = (TextView) findViewById(R.id.tv_year);
        mMonthTv = (TextView) findViewById(R.id.tv_month);
        mDayTv = (TextView) findViewById(R.id.tv_day);
        mChooseDayLayout = (LinearLayout) findViewById(R.id.ll_choose_day);
        LinearLayout addDatePickLayout = (LinearLayout) findViewById(R.id.ll_choose_day_content);
        mDatePickerView = DatePickerView.newInstance(this, this);
        addDatePickLayout.addView(mDatePickerView);
        mCurrentDay = Calendar.getInstance(Locale.CHINA);
        dateList = CalendarUtil.getDaysOfWeek(mCurrentDay.getTime());

        dayTvs[0] = (TextView) findViewById(R.id.tv_1);
        dayTvs[1] = (TextView) findViewById(R.id.tv_2);
        dayTvs[2] = (TextView) findViewById(R.id.tv_3);
        dayTvs[3] = (TextView) findViewById(R.id.tv_4);
        dayTvs[4] = (TextView) findViewById(R.id.tv_5);
        dayTvs[5] = (TextView) findViewById(R.id.tv_6);
        dayTvs[6] = (TextView) findViewById(R.id.tv_7);
        dayOfWeekLayout[0] = (LinearLayout) findViewById(R.id.ll_monday);
        dayOfWeekLayout[1] = (LinearLayout) findViewById(R.id.ll_tuesday);
        dayOfWeekLayout[2] = (LinearLayout) findViewById(R.id.ll_wednesday);
        dayOfWeekLayout[3] = (LinearLayout) findViewById(R.id.ll_thursday);
        dayOfWeekLayout[4] = (LinearLayout) findViewById(R.id.ll_friday);
        dayOfWeekLayout[5] = (LinearLayout) findViewById(R.id.ll_saturday);
        dayOfWeekLayout[6] = (LinearLayout) findViewById(R.id.ll_sunday);

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
        RVAdapter<InspectionBean> adapter = new RVAdapter<InspectionBean>(mRecyclerView, mList, R.layout.item_day_inspection) {
            @Override
            public void showData(ViewHolder vHolder, InspectionBean data, int position) {
                TextView tv_belong_place = (TextView) vHolder.getView(R.id.tv_belong_place);
                TextView tv_task_name = (TextView) vHolder.getView(R.id.tv_task_name);
                TextView tv_equip_num = (TextView) vHolder.getView(R.id.tv_equip_num);
                TextView tv_time_plan = (TextView) vHolder.getView(R.id.tv_time_plan_start);
                TextView tv_time_actual = (TextView) vHolder.getView(R.id.tv_time_actual_start);
                TextView tv_executor_user_type = (TextView) vHolder.getView(R.id.tv_executor_user_type);
                TextView tv_time_plan_end = (TextView) vHolder.getView(R.id.tv_time_plan_end);
                TextView tv_time_actual_end = (TextView) vHolder.getView(R.id.tv_time_actual_end);
                ImageView iv_state = (ImageView) vHolder.getView(R.id.iv_state);
                LinearLayout ll_inspection_type = (LinearLayout) vHolder.getView(R.id.ll_inspection_type);
                LinearLayout ll_actual_time = (LinearLayout) vHolder.getView(R.id.ll_actual_time);
                ImageView iv_inspection_type = (ImageView) vHolder.getView(R.id.iv_inspection_type);
                TextView tv_inspection_type = (TextView) vHolder.getView(R.id.tv_inspection_type);
                TextView tv_executor_inspection_user = (TextView) vHolder.getView(R.id.tv_executor_inspection_user);
                LinearLayout startTaskLayout = (LinearLayout) vHolder.getView(R.id.ll_start_task);
                if (data.getIsManualCreated() == 0) {
                    if (data.getPlanPeriodType() == 0) {
                        ll_inspection_type.setVisibility(View.GONE);
                    } else {
                        ll_inspection_type.setVisibility(View.VISIBLE);
                        iv_inspection_type.setImageDrawable(findDrawById(icons[data.getPlanPeriodType() - 1]));
                        tv_inspection_type.setText(inspectionTypeStr[data.getPlanPeriodType() - 1]);
                    }
                } else {
                    ll_inspection_type.setVisibility(View.VISIBLE);
                    iv_inspection_type.setImageDrawable(findDrawById(icons[3]));
                    tv_inspection_type.setText(inspectionTypeStr[3]);
                }
                tv_task_name.setText(data.getTaskName());
                if (data.getTaskState() == ConstantInt.TASK_STATE_1) {
                    ll_actual_time.setVisibility(View.GONE);
                    iv_state.setTag(R.id.tag_object, data);
                    iv_state.setTag(R.id.tag_position, position);
                    iv_state.setImageDrawable(findDrawById(R.drawable.home_bg_get_task));
                    if (data.getExecutorUserList() != null && data.getExecutorUserList().size() > 0) {
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < data.getExecutorUserList().size(); i++) {
                            if (data.getExecutorUserList().get(i).getUser() == null) {
                                continue;
                            }
                            if (!TextUtils.isEmpty(data.getExecutorUserList().get(i).getUser().getRealName())) {
                                sb.append(data.getExecutorUserList().get(i).getUser().getRealName());
                                if (i != data.getExecutorUserList().size() - 1) {
                                    sb.append("、");
                                }
                            }
                        }
                        tv_executor_inspection_user.setText(sb.toString());
                    }
                    tv_executor_user_type.setText("被指派人:");
                    startTaskLayout.setVisibility(View.GONE);
                } else if (data.getTaskState() == ConstantInt.TASK_STATE_2) {
                    ll_actual_time.setVisibility(View.GONE);
                    iv_state.setImageDrawable(findDrawById(R.drawable.work_receive_tag));
                    tv_executor_user_type.setText("领 取 人:");
                    tv_executor_inspection_user.setText(data.getReceiveUser().getRealName());
                    startTaskLayout.setVisibility(View.VISIBLE);
                } else if (data.getTaskState() == ConstantInt.TASK_STATE_3) {
                    ll_actual_time.setVisibility(View.GONE);
                    iv_state.setImageDrawable(findDrawById(R.drawable.work_on_tag));
                    tv_executor_user_type.setText("领 取 人:");
                    tv_executor_inspection_user.setText(data.getReceiveUser().getRealName());
                    startTaskLayout.setVisibility(View.VISIBLE);
                } else if (data.getTaskState() == ConstantInt.TASK_STATE_4) {
                    ll_actual_time.setVisibility(View.VISIBLE);
                    iv_state.setImageDrawable(findDrawById(R.drawable.work_complete_tag));
                    tv_time_actual.setText("巡检截至时间:" + DataUtil.timeFormat(data.getStartTime(), "yyyy-MM-dd HH:mm"));
                    tv_time_actual_end.setText(DataUtil.timeFormat(data.getEndTime(), "yyyy-MM-dd HH:mm"));
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < data.getUsers().size(); i++) {
                        if (data.getUsers().get(i) == null) {
                            continue;
                        }
                        sb.append(data.getUsers().get(i).getRealName());
                        if (i != data.getUsers().size() - 1) {
                            sb.append("、");
                        }
                    }
                    tv_executor_inspection_user.setText(sb.toString());
                    tv_executor_user_type.setText("执 行 人:");
                    startTaskLayout.setVisibility(View.GONE);
                }
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < data.getRooms().size(); i++) {
                    sb.append(data.getRooms().get(i));
                    if (i != data.getRooms().size() - 1) {
                        sb.append("、");
                    }
                }
                tv_belong_place.setText(sb.toString());
                String str = String.valueOf(data.getUploadCount()) + "/" + data.getCount();
                tv_equip_num.setText(str);
                if (data.getPlanStartTime() != 0) {
                    tv_time_plan.setVisibility(View.VISIBLE);
                    tv_time_plan.setText(MessageFormat.format("计划起止时间:{0}"
                            , DataUtil.timeFormat(data.getPlanStartTime(), "yyyy-MM-dd HH:mm")));
                } else {
                    tv_time_plan.setVisibility(View.GONE);
                }
                if (data.getPlanEndTime() != 0) {
                    tv_time_plan_end.setVisibility(View.VISIBLE);
                    tv_time_plan_end.setText(DataUtil.timeFormat(data.getPlanEndTime()
                            , "yyyy-MM-dd HH:mm"));
                } else {
                    tv_time_plan_end.setVisibility(View.GONE);
                }
                iv_state.setOnClickListener(unReceiveListener);
                startTaskLayout.setTag(R.id.tag_task, data.getTaskId());
                startTaskLayout.setTag(R.id.tag_position_1, data.getSecurityPackage() == null ? -1L : data.getSecurityPackage().getSecurityId());
                startTaskLayout.setOnClickListener(startTaskListener);
            }
        };
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new RVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (mList == null || mList.size() == 0) {
                    return;
                }
                Intent intent = new Intent(WorkInspectionActivity.this, InspectDetailActivity.class);
                intent.putExtra(ConstantStr.KEY_BUNDLE_LONG, mList.get(position).getTaskId());
                intent.putExtra(ConstantStr.KEY_BUNDLE_STR, mList.get(position).getTaskName());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onDateSet(DatePickerView datePickerView, int year, int monthOfYear, int dayOfMonth) {
        mCurrentDay.set(year, monthOfYear, dayOfMonth);
        dateList = CalendarUtil.getDaysOfWeek(mCurrentDay.getTime());
        setDayToView();
        monthOfYear = monthOfYear + 1;
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

    private void setDayToView() {
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        for (int i = 0; i < dayTvs.length; i++) {
            calendar.setTime(dateList.get(i));
            if (calendar.get(Calendar.DAY_OF_WEEK) == mCurrentDay.get(Calendar.DAY_OF_WEEK)) {
                dayOfWeekLayout[i].setBackgroundColor(findColorById(R.color.color6FA9F8));
            } else {
                dayOfWeekLayout[i].setBackgroundColor(findColorById(R.color.colorPrimary));
            }
            dayTvs[i].setText(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
        }
        getDate(mCurrentDay.get(Calendar.YEAR), mCurrentDay.get(Calendar.MONTH) + 1, mCurrentDay.get(Calendar.DAY_OF_MONTH));
        onRefresh();
    }

    private void getDate(int year, int monthOfYear, int dayOfMonth) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.valueOf(year)).append("-");
        mYearTv.setText(String.valueOf(year));
        if (monthOfYear < 10) {
            sb.append("0").append(String.valueOf(monthOfYear)).append("-");
            mMonthTv.setText("0" + String.valueOf(monthOfYear) + "月");
        } else {
            sb.append(String.valueOf(monthOfYear)).append("-");
            mMonthTv.setText(MessageFormat.format("{0}月", String.valueOf(monthOfYear)));
        }
        if (dayOfMonth < 10) {
            sb.append("0").append(String.valueOf(dayOfMonth));
            mDayTv.setText(String.format("0%s日", String.valueOf(dayOfMonth)));
        } else {
            sb.append(MessageFormat.format("{0}", String.valueOf(dayOfMonth)));
            mDayTv.setText(MessageFormat.format("{0}日", String.valueOf(dayOfMonth)));
        }
        mDate = sb.toString();
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
    public void showData(List<InspectionBean> been) {
        mList.clear();
        mList.addAll(been);
        mRecyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void showMoreData(List<InspectionBean> been) {

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
    public void noMoreData() {

    }

    @Override
    public void hideLoadingMore() {

    }

    @Override
    public void operationSuccess(int position) {
        onRefresh();
    }

    @Override
    public void setPresenter(InspectionContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onRefresh() {
        if (mPresenter != null) {
            noDataLayout.setVisibility(View.GONE);
            mPresenter.getData(inspectionType, mDate);
        }
    }

    private boolean isClick;
    private View.OnClickListener startTaskListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (isClick) {
                return;
            }
            Intent intent = new Intent();
            long taskId = (long) v.getTag(R.id.tag_task);
            long securityId = (long) v.getTag(R.id.tag_position_1);
            if (!SPHelper.readBoolean(WorkInspectionActivity.this
                    , ConstantStr.SECURITY_INFO, String.valueOf(taskId), false) && securityId != -1) {
                intent.setClass(WorkInspectionActivity.this, SecurityPackageActivity.class);
            } else {
                intent.setClass(WorkInspectionActivity.this, InspectionRoomActivity.class);
            }
            intent.putExtra(ConstantStr.KEY_BUNDLE_LONG, taskId);
            intent.putExtra(ConstantStr.KEY_BUNDLE_LONG_1, securityId);
            isClick = true;
            startActivity(intent);
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        if (isClick) {
            onRefresh();
        }
        isClick = false;
    }

    private View.OnClickListener unReceiveListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getTag(R.id.tag_object) != null) {
                int position = (int) v.getTag(R.id.tag_position);
                if (mList.get(position).getTaskState() > ConstantInt.TASK_STATE_1) {
                    return;
                }
                if (mPresenter != null) {
                    mPresenter.operationTask(String.valueOf(mList.get(position).getTaskId()), position);
                }
            }
        }
    };
}
