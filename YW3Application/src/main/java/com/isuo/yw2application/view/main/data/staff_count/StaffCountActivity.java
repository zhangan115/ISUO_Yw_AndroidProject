package com.isuo.yw2application.view.main.data.staff_count;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.isuo.yw2application.R;
import com.isuo.yw2application.app.Yw2Application;
import com.isuo.yw2application.mode.bean.count.ComeCount;
import com.isuo.yw2application.mode.bean.count.MonthCount;
import com.isuo.yw2application.mode.bean.count.WeekCount;
import com.isuo.yw2application.mode.bean.count.WeekList;
import com.isuo.yw2application.mode.bean.discover.DeptType;
import com.isuo.yw2application.utils.Utils;
import com.isuo.yw2application.view.base.BaseActivity;
import com.isuo.yw2application.widget.StaffDialog;
import com.sito.library.adapter.RVAdapter;
import com.sito.library.utils.DataUtil;
import com.sito.library.widget.ExpendRecycleView;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class StaffCountActivity extends BaseActivity implements StaffCountContract.View {
    //view
    private TextView mYesterday;
    private TextView mToday;

    private ExpendRecycleView mComeRecycle;

    private ExpendRecycleView mRecycle01;
    private ExpendRecycleView mRecycle02;
    private ExpendRecycleView mRecycle03;
    private ExpendRecycleView mRecycle04;
    private ExpendRecycleView mRecycle05;
    private ExpendRecycleView mRecycle06;
    private ExpendRecycleView mRecycle07;

    private ExpendRecycleView mRecycleLeave;
    //data
    private List<ComeCount> mComeCounts;

    private List<WeekList.WeekBean> mWeek01;
    private List<WeekList.WeekBean> mWeek02;
    private List<WeekList.WeekBean> mWeek03;
    private List<WeekList.WeekBean> mWeek04;
    private List<WeekList.WeekBean> mWeek05;
    private List<WeekList.WeekBean> mWeek06;
    private List<WeekList.WeekBean> mWeek07;

    private List<MonthCount> mMonthCounts;
    private TextView mMonthNum;
    private String mNowTime;//当前时间
    private String mChoseTime;
    @Inject
    StaffCountPresenter mStaffCountPresenter;
    StaffCountContract.Presenter mPresenter;
    private TextView mGroup;
    private boolean isShowGroup;
    private List<DeptType> mDeptTypes;
    private int mDeptId = -1;
    private List<String> names;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutAndToolbar(R.layout.activity_staff_count, "任务到位");
        DaggerStaffCountComponent.builder().customerRepositoryComponent(Yw2Application.getInstance().getRepositoryComponent())
                .staffCountModule(new StaffCountModule(this)).build()
                .inject(this);
        initView();
        initEvent();
        initData();
    }

    private void initEvent() {
        mYesterday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mYesterday.setBackground(findDrawById(R.drawable.staff_bg_blue_trans));
                mYesterday.setTextColor(findColorById(R.color.colorWhite));
                mToday.setBackground(findDrawById(R.drawable.staff_bg_gray_trans));
                mToday.setTextColor(findColorById(R.color.color_bg_staff_gray));

                long time = System.currentTimeMillis() - 24 * 60 * 60 * 1000;
                mChoseTime = DataUtil.timeFormat(time, "yyyy-MM-dd");
                mPresenter.getComeCount(mChoseTime, mDeptId + "");
                mNowTime = mChoseTime;
            }
        });
        mToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mToday.setBackground(findDrawById(R.drawable.staff_bg_blue));
                mToday.setTextColor(findColorById(R.color.colorWhite));
                mYesterday.setBackground(findDrawById(R.drawable.staff_bg_gray));
                mYesterday.setTextColor(findColorById(R.color.color_bg_staff_gray));

                long time = System.currentTimeMillis();
                mNowTime = DataUtil.timeFormat(time, "yyyy-MM-dd");
                mPresenter.getComeCount(mNowTime, mDeptId + "");
            }
        });
    }

    private void initData() {
        mComeCounts = new ArrayList<>();
        RVAdapter<ComeCount> adapterCome = new RVAdapter<ComeCount>(mComeRecycle, mComeCounts, R.layout.item_staff_today) {
            @Override
            public void showData(ViewHolder vHolder, ComeCount data, int position) {
                View lineView = vHolder.getView(R.id.id_staff_line);
                lineView.setVisibility(position == 0 ? View.GONE : View.VISIBLE);
                TextView mName = (TextView) vHolder.getView(R.id.id_staff_name);
                mName.setText(data.getTaskName());
                TextView mState = (TextView) vHolder.getView(R.id.id_staff_state);
                if (data.getTaskStationState() == 1) {
                    mState.setText("未到岗");
                    mState.setBackground(findDrawById(R.drawable.shape_red));
                } else if (data.getTaskStationState() == 2) {
                    mState.setText("到岗");
                    mState.setBackground(findDrawById(R.drawable.shape_green));
                } else if (data.getTaskStationState() == 4) {
                    mState.setText("延迟");
                    mState.setBackground(findDrawById(R.drawable.shape_yellow));
                } else if (data.getTaskStationState() == 3) {
                    mState.setText("未开始");
                    mState.setBackground(findDrawById(R.drawable.shape_gray));
                }
            }
        };
        mComeRecycle.setAdapter(adapterCome);
        adapterCome.setOnItemClickListener(new RVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String name = mComeCounts.get(position).getTaskName();
                String planTime = DataUtil.timeFormat(mComeCounts.get(position).getPlanStartTime(), "yyyy-MM-dd HH:mm") + "至" + DataUtil.timeFormat(mComeCounts.get(position).getPlanEndTime(), "yyyy-MM-dd HH:mm");
                String rooms = "";
                StringBuilder stringBuilder = new StringBuilder();
                if (mComeCounts.get(position).getStartTime() != 0) {
                    stringBuilder.append(DataUtil.timeFormat(mComeCounts.get(position).getStartTime(), "yyyy-MM-dd HH:mm")).append("至");
                }
                if (mComeCounts.get(position).getEndTime() != 0) {
                    stringBuilder.append(DataUtil.timeFormat(mComeCounts.get(position).getEndTime(), "yyyy-MM-dd HH:mm"));
                }
                if (mComeCounts.get(position).getRooms() != null && mComeCounts.get(position).getRooms().size() > 0) {
                    for (int j = 0; j < mComeCounts.get(position).getRooms().size(); j++) {
                        rooms = MessageFormat.format("{0},{1}", rooms, mComeCounts.get(position).getRooms().get(j));
                    }
                    if (!TextUtils.isEmpty(rooms)) {
                        rooms = rooms.substring(1);
                    }
                }
                new StaffDialog(StaffCountActivity.this, name, "计划时间：" + planTime, "巡检时间：" + stringBuilder.toString(), "巡检区域：" + rooms, "1").show();
            }
        });

        mWeek01 = new ArrayList<>();
        RVAdapter<WeekList.WeekBean> adapter01 = new RVAdapter<WeekList.WeekBean>(mRecycle01, mWeek01, R.layout.item_staff_time) {
            @Override
            public void showData(ViewHolder vHolder, WeekList.WeekBean data, int position) {
                TextView mTime = (TextView) vHolder.getView(R.id.id_staff_item_time);
                mTime.setText(data.getTaskName());
            }
        };
        mRecycle01.setAdapter(adapter01);
        adapter01.setOnItemClickListener(new RVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String name = mWeek01.get(position).getTaskName();
                String planTime = DataUtil.timeFormat(mWeek01.get(position).getPlanStartTime(), "yyyy-MM-dd HH:mm") + "至" + DataUtil.timeFormat(mWeek01.get(position).getPlanEndTime(), "yyyy-MM-dd HH:mm");
                String rooms = "";
                StringBuilder stringBuilder = new StringBuilder();
                if (mWeek01.get(position).getStartTime() != 0) {
                    stringBuilder.append(DataUtil.timeFormat(mWeek01.get(position).getStartTime(), "yyyy-MM-dd HH:mm")).append("至");
                }
                if (mWeek01.get(position).getEndTime() != 0) {
                    stringBuilder.append(DataUtil.timeFormat(mWeek01.get(position).getEndTime(), "yyyy-MM-dd HH:mm"));
                }
                if (mWeek01.get(position).getRooms() != null && mWeek01.get(position).getRooms().size() > 0) {
                    for (int j = 0; j < mWeek01.get(position).getRooms().size(); j++) {
                        rooms = MessageFormat.format("{0},{1}", rooms, mWeek01.get(position).getRooms().get(j));
                    }
                    if (!TextUtils.isEmpty(rooms)) {
                        rooms = rooms.substring(1);
                    }
                }
                new StaffDialog(StaffCountActivity.this, name, "计划时间：" + planTime, "巡检时间：" + stringBuilder.toString(), "巡检区域：" + rooms, "1").show();
            }
        });
        mWeek02 = new ArrayList<>();
        RVAdapter<WeekList.WeekBean> adapter02 = new RVAdapter<WeekList.WeekBean>(mRecycle02, mWeek02, R.layout.item_staff_time) {
            @Override
            public void showData(ViewHolder vHolder, WeekList.WeekBean data, int position) {
                TextView mTime = (TextView) vHolder.getView(R.id.id_staff_item_time);
                mTime.setText(data.getTaskName());
            }
        };
        mRecycle02.setAdapter(adapter02);
        adapter02.setOnItemClickListener(new RVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String name = mWeek02.get(position).getTaskName();
                String planTime = DataUtil.timeFormat(mWeek02.get(position).getPlanStartTime(), "yyyy-MM-dd HH:mm") + "至" + DataUtil.timeFormat(mWeek02.get(position).getPlanEndTime(), "yyyy-MM-dd HH:mm");
                String rooms = "";
                StringBuilder stringBuilder = new StringBuilder();
                if (mWeek02.get(position).getStartTime() != 0) {
                    stringBuilder.append(DataUtil.timeFormat(mWeek02.get(position).getStartTime(), "yyyy-MM-dd HH:mm")).append("至");
                }
                if (mWeek02.get(position).getEndTime() != 0) {
                    stringBuilder.append(DataUtil.timeFormat(mWeek02.get(position).getEndTime(), "yyyy-MM-dd HH:mm"));
                }
                if (mWeek02.get(position).getRooms() != null && mWeek02.get(position).getRooms().size() > 0) {
                    for (int j = 0; j < mWeek02.get(position).getRooms().size(); j++) {
                        rooms = MessageFormat.format("{0},{1}", rooms, mWeek02.get(position).getRooms().get(j));
                    }
                    if (!TextUtils.isEmpty(rooms)) {
                        rooms = rooms.substring(1);
                    }
                }
                new StaffDialog(StaffCountActivity.this, name, "计划时间：" + planTime, "巡检时间：" + stringBuilder.toString(), "巡检区域：" + rooms, "1").show();
            }
        });
        mWeek03 = new ArrayList<>();
        RVAdapter<WeekList.WeekBean> adapter03 = new RVAdapter<WeekList.WeekBean>(mRecycle03, mWeek03, R.layout.item_staff_time) {
            @Override
            public void showData(ViewHolder vHolder, WeekList.WeekBean data, int position) {
                TextView mTime = (TextView) vHolder.getView(R.id.id_staff_item_time);
                mTime.setText(data.getTaskName());
            }
        };
        mRecycle03.setAdapter(adapter03);
        adapter03.setOnItemClickListener(new RVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String name = mWeek03.get(position).getTaskName();
                String planTime = DataUtil.timeFormat(mWeek03.get(position).getPlanStartTime(), "yyyy-MM-dd HH:mm") + "至" + DataUtil.timeFormat(mWeek03.get(position).getPlanEndTime(), "yyyy-MM-dd HH:mm");
                String rooms = "";
                StringBuilder stringBuilder = new StringBuilder();
                if (mWeek03.get(position).getStartTime() != 0) {
                    stringBuilder.append(DataUtil.timeFormat(mWeek03.get(position).getStartTime(), "yyyy-MM-dd HH:mm")).append("至");
                }
                if (mWeek03.get(position).getEndTime() != 0) {
                    stringBuilder.append(DataUtil.timeFormat(mWeek03.get(position).getEndTime(), "yyyy-MM-dd HH:mm"));
                }
                if (mWeek03.get(position).getRooms() != null && mWeek03.get(position).getRooms().size() > 0) {
                    for (int j = 0; j < mWeek03.get(position).getRooms().size(); j++) {
                        rooms = MessageFormat.format("{0},{1}", rooms, mWeek03.get(position).getRooms().get(j));
                    }
                    if (!TextUtils.isEmpty(rooms)) {
                        rooms = rooms.substring(1);
                    }
                }
                new StaffDialog(StaffCountActivity.this, name, "计划时间：" + planTime, "巡检时间：" + stringBuilder.toString(), "巡检区域：" + rooms, "1").show();
            }
        });
        mWeek04 = new ArrayList<>();
        RVAdapter<WeekList.WeekBean> adapter04 = new RVAdapter<WeekList.WeekBean>(mRecycle04, mWeek04, R.layout.item_staff_time) {
            @Override
            public void showData(ViewHolder vHolder, WeekList.WeekBean data, int position) {
                TextView mTime = (TextView) vHolder.getView(R.id.id_staff_item_time);
                mTime.setText(data.getTaskName());
            }
        };
        mRecycle04.setAdapter(adapter04);
        adapter04.setOnItemClickListener(new RVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String name = mWeek04.get(position).getTaskName();
                String planTime = DataUtil.timeFormat(mWeek04.get(position).getPlanStartTime(), "yyyy-MM-dd HH:mm") + "至" + DataUtil.timeFormat(mWeek04.get(position).getPlanEndTime(), "yyyy-MM-dd HH:mm");
                String rooms = "";
                StringBuilder stringBuilder = new StringBuilder();
                if (mWeek04.get(position).getStartTime() != 0) {
                    stringBuilder.append(DataUtil.timeFormat(mWeek04.get(position).getStartTime(), "yyyy-MM-dd HH:mm")).append("至");
                }
                if (mWeek04.get(position).getEndTime() != 0) {
                    stringBuilder.append(DataUtil.timeFormat(mWeek04.get(position).getEndTime(), "yyyy-MM-dd HH:mm"));
                }
                if (mWeek04.get(position).getRooms() != null && mWeek04.get(position).getRooms().size() > 0) {
                    for (int j = 0; j < mWeek04.get(position).getRooms().size(); j++) {
                        rooms = MessageFormat.format("{0},{1}", rooms, mWeek04.get(position).getRooms().get(j));
                    }
                    if (!TextUtils.isEmpty(rooms)) {
                        rooms = rooms.substring(1);
                    }
                }
                new StaffDialog(StaffCountActivity.this, name, "计划时间：" + planTime, "巡检时间：" + stringBuilder.toString(), "巡检区域：" + rooms, "1").show();
            }
        });

        mWeek05 = new ArrayList<>();
        RVAdapter<WeekList.WeekBean> adapter05 = new RVAdapter<WeekList.WeekBean>(mRecycle05, mWeek05, R.layout.item_staff_time) {
            @Override
            public void showData(ViewHolder vHolder, WeekList.WeekBean data, int position) {
                TextView mTime = (TextView) vHolder.getView(R.id.id_staff_item_time);
                mTime.setText(data.getTaskName());
            }
        };
        mRecycle05.setAdapter(adapter05);
        adapter05.setOnItemClickListener(new RVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String name = mWeek05.get(position).getTaskName();
                String planTime = DataUtil.timeFormat(mWeek05.get(position).getPlanStartTime(), "yyyy-MM-dd HH:mm") + "至" + DataUtil.timeFormat(mWeek05.get(position).getPlanEndTime(), "yyyy-MM-dd HH:mm");
                String rooms = "";
                StringBuilder stringBuilder = new StringBuilder();
                if (mWeek05.get(position).getStartTime() != 0) {
                    stringBuilder.append(DataUtil.timeFormat(mWeek05.get(position).getStartTime(), "yyyy-MM-dd HH:mm")).append("至");
                }
                if (mWeek05.get(position).getEndTime() != 0) {
                    stringBuilder.append(DataUtil.timeFormat(mWeek05.get(position).getEndTime(), "yyyy-MM-dd HH:mm"));
                }
                if (mWeek05.get(position).getRooms() != null && mWeek05.get(position).getRooms().size() > 0) {
                    for (int j = 0; j < mWeek05.get(position).getRooms().size(); j++) {
                        rooms = MessageFormat.format("{0},{1}", rooms, mWeek05.get(position).getRooms().get(j));
                    }
                    if (!TextUtils.isEmpty(rooms)) {
                        rooms = rooms.substring(1);
                    }
                }
                new StaffDialog(StaffCountActivity.this, name, "计划时间：" + planTime, "巡检时间：" + stringBuilder.toString(), "巡检区域：" + rooms, "1").show();
            }
        });
        mWeek06 = new ArrayList<>();
        RVAdapter<WeekList.WeekBean> adapter06 = new RVAdapter<WeekList.WeekBean>(mRecycle06, mWeek06, R.layout.item_staff_time) {
            @Override
            public void showData(ViewHolder vHolder, WeekList.WeekBean data, int position) {
                TextView mTime = (TextView) vHolder.getView(R.id.id_staff_item_time);
                mTime.setText(data.getTaskName());
            }
        };
        mRecycle06.setAdapter(adapter06);
        adapter06.setOnItemClickListener(new RVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String name = mWeek06.get(position).getTaskName();
                String planTime = DataUtil.timeFormat(mWeek06.get(position).getPlanStartTime(), "yyyy-MM-dd HH:mm") + "至" + DataUtil.timeFormat(mWeek06.get(position).getPlanEndTime(), "yyyy-MM-dd HH:mm");
                String rooms = "";
                StringBuilder stringBuilder = new StringBuilder();
                if (mWeek06.get(position).getStartTime() != 0) {
                    stringBuilder.append(DataUtil.timeFormat(mWeek06.get(position).getStartTime(), "yyyy-MM-dd HH:mm")).append("至");
                }
                if (mWeek06.get(position).getEndTime() != 0) {
                    stringBuilder.append(DataUtil.timeFormat(mWeek06.get(position).getEndTime(), "yyyy-MM-dd HH:mm"));
                }
                if (mWeek06.get(position).getRooms() != null && mWeek06.get(position).getRooms().size() > 0) {
                    for (int j = 0; j < mWeek06.get(position).getRooms().size(); j++) {
                        rooms = MessageFormat.format("{0},{1}", rooms, mWeek06.get(position).getRooms().get(j));
                    }
                    if (!TextUtils.isEmpty(rooms)) {
                        rooms = rooms.substring(1);
                    }
                }
                new StaffDialog(StaffCountActivity.this, name, "计划时间：" + planTime, "巡检时间：" + stringBuilder.toString(), "巡检区域：" + rooms, "1").show();
            }
        });
        mWeek07 = new ArrayList<>();
        RVAdapter<WeekList.WeekBean> adapter07 = new RVAdapter<WeekList.WeekBean>(mRecycle07, mWeek07, R.layout.item_staff_time) {
            @Override
            public void showData(ViewHolder vHolder, WeekList.WeekBean data, int position) {
                TextView mTime = (TextView) vHolder.getView(R.id.id_staff_item_time);
                mTime.setText(data.getTaskName());
            }
        };
        mRecycle07.setAdapter(adapter07);
        adapter07.setOnItemClickListener(new RVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String name = mWeek07.get(position).getTaskName();
                String planTime = DataUtil.timeFormat(mWeek07.get(position).getPlanStartTime(), "yyyy-MM-dd HH:mm") + "至" + DataUtil.timeFormat(mWeek07.get(position).getPlanEndTime(), "yyyy-MM-dd HH:mm");
                String rooms = "";
                StringBuilder stringBuilder = new StringBuilder();
                if (mWeek07.get(position).getStartTime() != 0) {
                    stringBuilder.append(DataUtil.timeFormat(mWeek07.get(position).getStartTime(), "yyyy-MM-dd HH:mm")).append("至");
                }
                if (mWeek07.get(position).getEndTime() != 0) {
                    stringBuilder.append(DataUtil.timeFormat(mWeek07.get(position).getEndTime(), "yyyy-MM-dd HH:mm"));
                }
                if (mWeek07.get(position).getRooms() != null && mWeek07.get(position).getRooms().size() > 0) {
                    for (int j = 0; j < mWeek07.get(position).getRooms().size(); j++) {
                        rooms = MessageFormat.format("{0},{1}", rooms, mWeek07.get(position).getRooms().get(j));
                    }
                    if (!TextUtils.isEmpty(rooms)) {
                        rooms = rooms.substring(1);
                    }
                }
                new StaffDialog(StaffCountActivity.this, name, "计划时间：" + planTime, "巡检时间：" + stringBuilder.toString(), "巡检区域：" + rooms, "1").show();
            }
        });
        mMonthCounts = new ArrayList<>();
        RVAdapter<MonthCount> adapter = new RVAdapter<MonthCount>(mRecycleLeave, mMonthCounts, R.layout.item_staff_leave) {
            @Override
            public void showData(ViewHolder vHolder, MonthCount data, int position) {
                View lineView = vHolder.getView(R.id.id_staff_line);
                lineView.setVisibility(position == 0 ? View.GONE : View.VISIBLE);
                TextView name = (TextView) vHolder.getView(R.id.id_staff_item_leave);
                name.setText(data.getTaskName());
            }
        };
        mRecycleLeave.setAdapter(adapter);
        adapter.setOnItemClickListener(new RVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String name = mMonthCounts.get(position).getTaskName();
                String planTime = DataUtil.timeFormat(mMonthCounts.get(position).getPlanStartTime(), "yyyy-MM-dd HH:mm") + "至" + DataUtil.timeFormat(mMonthCounts.get(position).getPlanEndTime(), "yyyy-MM-dd HH:mm");
                String rooms = "";
                StringBuilder stringBuilder = new StringBuilder();
                if (mMonthCounts.get(position).getStartTime() != 0) {
                    stringBuilder.append(DataUtil.timeFormat(mMonthCounts.get(position).getStartTime(), "yyyy-MM-dd HH:mm")).append("至");
                }
                if (mMonthCounts.get(position).getEndTime() != 0) {
                    stringBuilder.append(DataUtil.timeFormat(mMonthCounts.get(position).getEndTime(), "yyyy-MM-dd HH:mm"));
                }
                if (mMonthCounts.get(position).getRooms() != null && mMonthCounts.get(position).getRooms().size() > 0) {
                    for (int j = 0; j < mMonthCounts.get(position).getRooms().size(); j++) {
                        rooms = MessageFormat.format("{0},{1}", rooms, mMonthCounts.get(position).getRooms().get(j));
                    }
                    if (!TextUtils.isEmpty(rooms)) {
                        rooms = rooms.substring(1);
                    }
                }
                new StaffDialog(StaffCountActivity.this, name, "计划时间：" + planTime, "巡检时间：" + stringBuilder.toString(), "巡检区域：" + rooms, "1").show();
            }
        });
        mPresenter.getDeptId("1");
        long time = System.currentTimeMillis();
        mNowTime = DataUtil.timeFormat(time, "yyyy-MM-dd");
    }

    private void initView() {
        mGroup = findViewById(R.id.id_fault_group);
        mYesterday = findViewById(R.id.id_staff_yesterday);
        mToday = findViewById(R.id.id_staff_today);

        mComeRecycle = findViewById(R.id.id_staff_time);
        mComeRecycle.setLayoutManager(new GridLayoutManager(this, 1));

        //设置布局管理器
        LinearLayoutManager linearLayoutManager01 = new LinearLayoutManager(this);
        linearLayoutManager01.setOrientation(LinearLayoutManager.VERTICAL);//水平滑动
        mRecycle01 = findViewById(R.id.id_staff_rec01);
        mRecycle01.setLayoutManager(linearLayoutManager01);
        mRecycle01.setNestedScrollingEnabled(false);

        LinearLayoutManager linearLayoutManager02 = new LinearLayoutManager(this);
        linearLayoutManager02.setOrientation(LinearLayoutManager.VERTICAL);//水平滑动
        mRecycle02 = findViewById(R.id.id_staff_rec02);
        mRecycle02.setLayoutManager(linearLayoutManager02);
        mRecycle02.setNestedScrollingEnabled(false);

        LinearLayoutManager linearLayoutManager03 = new LinearLayoutManager(this);
        linearLayoutManager03.setOrientation(LinearLayoutManager.VERTICAL);//水平滑动
        mRecycle03 = findViewById(R.id.id_staff_rec03);
        mRecycle03.setLayoutManager(linearLayoutManager03);
        mRecycle03.setNestedScrollingEnabled(false);

        LinearLayoutManager linearLayoutManager04 = new LinearLayoutManager(this);
        linearLayoutManager04.setOrientation(LinearLayoutManager.VERTICAL);//水平滑动
        mRecycle04 = findViewById(R.id.id_staff_rec04);
        mRecycle04.setLayoutManager(linearLayoutManager04);
        mRecycle04.setNestedScrollingEnabled(false);

        LinearLayoutManager linearLayoutManager05 = new LinearLayoutManager(this);
        linearLayoutManager05.setOrientation(LinearLayoutManager.VERTICAL);//水平滑动
        mRecycle05 = findViewById(R.id.id_staff_rec05);
        mRecycle05.setLayoutManager(linearLayoutManager05);
        mRecycle05.setNestedScrollingEnabled(false);

        LinearLayoutManager linearLayoutManager06 = new LinearLayoutManager(this);
        linearLayoutManager06.setOrientation(LinearLayoutManager.VERTICAL);//水平滑动
        mRecycle06 = findViewById(R.id.id_staff_rec06);
        mRecycle06.setLayoutManager(linearLayoutManager06);
        mRecycle06.setNestedScrollingEnabled(false);

        LinearLayoutManager linearLayoutManager07 = new LinearLayoutManager(this);
        linearLayoutManager07.setOrientation(LinearLayoutManager.VERTICAL);//水平滑动
        mRecycle07 = findViewById(R.id.id_staff_rec07);
        mRecycle07.setLayoutManager(linearLayoutManager07);
        mRecycle07.setNestedScrollingEnabled(false);

        mRecycleLeave = findViewById(R.id.id_staff_leave);
        mRecycleLeave.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
        mRecycleLeave.setNestedScrollingEnabled(false);
        mMonthNum = findViewById(R.id.id_staff_leavecount);
    }

    @Override
    public void setPresenter(StaffCountContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showDeptId(List<DeptType> deptTypes) {
        mDeptTypes = new ArrayList<>();
        mDeptTypes.addAll(deptTypes);
        mGroup.setText(deptTypes.get(0).getDeptName());
        mDeptId = deptTypes.get(0).getDeptId();
        names = new ArrayList<>();
        for (int i = 0; i < mDeptTypes.size(); i++) {
            names.add(mDeptTypes.get(i).getDeptName());
        }
        mGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isShowGroup) {
                    isShowGroup = true;
                    Utils.setDrawable(StaffCountActivity.this, R.drawable.up_arrow, mGroup, 2);
                } else {
                    isShowGroup = false;
                    Utils.setDrawable(StaffCountActivity.this, R.drawable.drop_down_arrow, mGroup, 2);
                }
                new MaterialDialog.Builder(StaffCountActivity.this)
                        .items(names)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                                mGroup.setText(mDeptTypes.get(position).getDeptName());
                                mDeptId = mDeptTypes.get(position).getDeptId();
                                isShowGroup = false;
                                Utils.setDrawable(StaffCountActivity.this, R.drawable.drop_down_arrow, mGroup, 2);
                                mPresenter.getComeCount(mNowTime, mDeptId + "");
                                mPresenter.getWeekList(mNowTime, mDeptId + "");
                                mPresenter.getMonthCount(mNowTime, mDeptId + "");
                            }
                        })
                        .show();
            }
        });
        if (mDeptId != -1) {
            mPresenter.getComeCount(mNowTime, mDeptId + "");
            mPresenter.getWeekList(mNowTime, mDeptId + "");
            mPresenter.getMonthCount(mNowTime, mDeptId + "");
        }
    }

    @Override
    public void showComeCount(List<ComeCount> comeCounts) {
        mComeCounts.clear();
        mComeCounts.addAll(comeCounts);
        if (mComeCounts.size() > 0) {
            mComeRecycle.setVisibility(View.VISIBLE);
        } else {
            mComeRecycle.setVisibility(View.GONE);
        }
        mComeRecycle.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void showWeekCount(List<WeekCount> weekCounts) {

    }

    @Override
    public void showMonthCount(List<MonthCount> monthCounts) {
        mMonthNum.setText(MessageFormat.format("本月未到岗统计 {0}", monthCounts.size()));
        mMonthCounts.clear();
        mMonthCounts.addAll(monthCounts);
        mRecycleLeave.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void showWeekList(List<WeekList> weekLists) {
        mWeek01.clear();
        mWeek02.clear();
        mWeek03.clear();
        mWeek04.clear();
        mWeek05.clear();
        mWeek06.clear();
        mWeek07.clear();
        if (weekLists != null && weekLists.size() > 0) {
            if (weekLists.get(0) != null && weekLists.get(0).getWeek() != null && weekLists.get(0).getWeek().size() > 0) {
                mWeek01.addAll(weekLists.get(0).getWeek());

            }
            mRecycle01.getAdapter().notifyDataSetChanged();
            if (weekLists.get(1) != null && weekLists.get(1).getWeek() != null && weekLists.get(1).getWeek().size() > 0) {
                mWeek02.addAll(weekLists.get(1).getWeek());

            }
            mRecycle02.getAdapter().notifyDataSetChanged();
            if (weekLists.get(2) != null && weekLists.get(2).getWeek() != null && weekLists.get(2).getWeek().size() > 0) {
                mWeek03.addAll(weekLists.get(2).getWeek());

            }
            mRecycle03.getAdapter().notifyDataSetChanged();
            if (weekLists.get(3) != null && weekLists.get(3).getWeek() != null && weekLists.get(3).getWeek().size() > 0) {
                mWeek04.addAll(weekLists.get(3).getWeek());

            }
            mRecycle04.getAdapter().notifyDataSetChanged();
            if (weekLists.get(4) != null && weekLists.get(4).getWeek() != null && weekLists.get(4).getWeek().size() > 0) {
                mWeek05.addAll(weekLists.get(4).getWeek());

            }
            mRecycle05.getAdapter().notifyDataSetChanged();
            if (weekLists.get(5) != null && weekLists.get(5).getWeek() != null && weekLists.get(5).getWeek().size() > 0) {
                mWeek06.addAll(weekLists.get(5).getWeek());

            }
            mRecycle06.getAdapter().notifyDataSetChanged();
            if (weekLists.get(6) != null && weekLists.get(6).getWeek() != null && weekLists.get(6).getWeek().size() > 0) {
                mWeek07.addAll(weekLists.get(6).getWeek());

            }
            mRecycle07.getAdapter().notifyDataSetChanged();
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void noData() {

    }
}
