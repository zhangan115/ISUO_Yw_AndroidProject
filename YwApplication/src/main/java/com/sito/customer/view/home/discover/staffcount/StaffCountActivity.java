package com.sito.customer.view.home.discover.staffcount;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.sito.customer.R;
import com.sito.customer.app.CustomerApp;
import com.sito.customer.mode.bean.count.ComeCount;
import com.sito.customer.mode.bean.count.MonthCount;
import com.sito.customer.mode.bean.count.WeekCount;
import com.sito.customer.mode.bean.count.WeekList;
import com.sito.customer.mode.bean.discover.DeptType;
import com.sito.customer.utils.Utils;
import com.sito.customer.view.BaseActivity;
import com.sito.customer.widget.StaffDialog;
import com.sito.library.adapter.RVAdapter;
import com.sito.library.utils.DataUtil;
import com.sito.library.widget.ExpendRecycleView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class StaffCountActivity extends BaseActivity implements StaffCountContract.View {
    //view
    private TextView mYesterday;
    private TextView mToday;

    private LinearLayout mTimeList;
    private ExpendRecycleView mComeRecy;

    private ExpendRecycleView mRecy01;
    private ExpendRecycleView mRecy02;
    private ExpendRecycleView mRecy03;
    private ExpendRecycleView mRecy04;
    private ExpendRecycleView mRecy05;
    private ExpendRecycleView mRecy06;
    private ExpendRecycleView mRecy07;

    private TextView mLeaveCount;
    private ExpendRecycleView mRecyLeave;
    //data
    private List<String> mData;
    private List<ComeCount> mComeCounts;

    private List<WeekList.WeekBean> mWeek01;
    private List<WeekList.WeekBean> mWeek02;
    private List<WeekList.WeekBean> mWeek03;
    private List<WeekList.WeekBean> mWeek04;
    private List<WeekList.WeekBean> mWeek05;
    private List<WeekList.WeekBean> mWeek06;
    private List<WeekList.WeekBean> mWeek07;

    private List<MonthCount> mMonthCounts;
    private int mMonthNoGet = 0;
    private TextView mMonthNum;
    private String mNowTime;//当前时间
    private String mChoseTime;
    @Inject
    StaffCountPresenter mStaffCountPresenter;
    StaffCountContract.Presenter mPresenter;
    private TextView mGroup;
    private boolean isShowGrop;
    private List<DeptType> mDeptTypes;
    private int mDeptId = -1;
    private List<String> names;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_staff_count);
        setLayoutAndToolbar(R.layout.activity_staff_count, "人员到岗统计");
        DaggerStaffCountComponent.builder().customerRepositoryComponent(CustomerApp.getInstance().getRepositoryComponent())
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
//                mPresenter.getWeekCount(mNowTime, "1");
//                mPresenter.getMonthCount(mNowTime, "1");
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
//                mPresenter.getWeekCount(mNowTime, "1");
//                mPresenter.getMonthCount(mNowTime, "1");
            }
        });
    }

    private void initData() {
        mComeCounts = new ArrayList<>();
        RVAdapter<ComeCount> adapterCome = new RVAdapter<ComeCount>(mComeRecy, mComeCounts, R.layout.item_staff_today) {
            @Override
            public void showData(ViewHolder vHolder, ComeCount data, int position) {
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
                } else if (data.getTaskStationState() == 3){
                    mState.setText("未开始");
                    mState.setBackground(findDrawById(R.drawable.shape_gray));
                }
            }
        };
        mComeRecy.setAdapter(adapterCome);
        adapterCome.setOnItemClickListener(new RVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String name = mComeCounts.get(position).getTaskName();
                String planTime = DataUtil.timeFormat(mComeCounts.get(position).getPlanStartTime(), "yyyy-MM-dd HH:mm") + "至" + DataUtil.timeFormat(mComeCounts.get(position).getPlanEndTime(), "yyyy-MM-dd HH:mm");
                String rooms = "";
                StringBuilder stringBuilder = new StringBuilder();
                if (mComeCounts.get(position).getStartTime() != 0 ) {
                    stringBuilder.append(DataUtil.timeFormat(mComeCounts.get(position).getStartTime(), "yyyy-MM-dd HH:mm") + "至");
                }
                if(mComeCounts.get(position).getEndTime()!= 0){
                    stringBuilder.append(DataUtil.timeFormat(mComeCounts.get(position).getEndTime(), "yyyy-MM-dd HH:mm"));
                }
                if (mComeCounts.get(position).getRooms() != null && mComeCounts.get(position).getRooms().size() > 0) {
                    for (int j = 0; j < mComeCounts.get(position).getRooms().size(); j++) {
                        rooms = rooms + "," + mComeCounts.get(position).getRooms().get(j);
                    }
                    if (!TextUtils.isEmpty(rooms)) {
                        rooms = rooms.substring(1, rooms.length());
                    }
                }
                new StaffDialog(StaffCountActivity.this, name, "计划时间：" + planTime, "巡检时间：" + stringBuilder.toString(), "巡检区域：" + rooms, "1").show();
            }
        });

        mWeek01 = new ArrayList<>();
        RVAdapter<WeekList.WeekBean> adapter01 = new RVAdapter<WeekList.WeekBean>(mRecy01, mWeek01, R.layout.item_staff_time) {
            @Override
            public void showData(ViewHolder vHolder, WeekList.WeekBean data, int position) {
                TextView mTime = (TextView) vHolder.getView(R.id.id_staff_item_time);
//                mTime.setText(DataUtil.timeFormat(data.getPlanStartTime(), "HH:mm") + "-" + DataUtil.timeFormat(data.getPlanEndTime(), "HH:mm"));
                mTime.setText(data.getTaskName());
            }
        };
        mRecy01.setAdapter(adapter01);
        adapter01.setOnItemClickListener(new RVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String name = mWeek01.get(position).getTaskName();
                String planTime = DataUtil.timeFormat(mWeek01.get(position).getPlanStartTime(), "yyyy-MM-dd HH:mm") + "至" + DataUtil.timeFormat(mWeek01.get(position).getPlanEndTime(), "yyyy-MM-dd HH:mm");
                String rooms = "";
                StringBuilder stringBuilder = new StringBuilder();
                if (mWeek01.get(position).getStartTime() != 0 ) {
                    stringBuilder.append(DataUtil.timeFormat(mWeek01.get(position).getStartTime(), "yyyy-MM-dd HH:mm") + "至");
                }
                if(mWeek01.get(position).getEndTime()!= 0){
                    stringBuilder.append(DataUtil.timeFormat(mWeek01.get(position).getEndTime(), "yyyy-MM-dd HH:mm"));
                }
                if (mWeek01.get(position).getRooms() != null && mWeek01.get(position).getRooms().size() > 0) {
                    for (int j = 0; j < mWeek01.get(position).getRooms().size(); j++) {
                        rooms = rooms + "," + mWeek01.get(position).getRooms().get(j);
                    }
                    if (!TextUtils.isEmpty(rooms)) {
                        rooms = rooms.substring(1, rooms.length());
                    }
                }
                new StaffDialog(StaffCountActivity.this, name, "计划时间：" + planTime, "巡检时间：" + stringBuilder.toString(), "巡检区域：" + rooms, "1").show();
            }
        });
        mWeek02 = new ArrayList<>();
        RVAdapter<WeekList.WeekBean> adapter02 = new RVAdapter<WeekList.WeekBean>(mRecy02, mWeek02, R.layout.item_staff_time) {
            @Override
            public void showData(ViewHolder vHolder, WeekList.WeekBean data, int position) {
                TextView mTime = (TextView) vHolder.getView(R.id.id_staff_item_time);
                mTime.setText(data.getTaskName());
            }
        };
        mRecy02.setAdapter(adapter02);
        adapter02.setOnItemClickListener(new RVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String name = mWeek02.get(position).getTaskName();
                String planTime = DataUtil.timeFormat(mWeek02.get(position).getPlanStartTime(), "yyyy-MM-dd HH:mm") + "至" + DataUtil.timeFormat(mWeek02.get(position).getPlanEndTime(), "yyyy-MM-dd HH:mm");
                String rooms = "";
                StringBuilder stringBuilder = new StringBuilder();
                if (mWeek02.get(position).getStartTime() != 0 ) {
                    stringBuilder.append(DataUtil.timeFormat(mWeek02.get(position).getStartTime(), "yyyy-MM-dd HH:mm") + "至");
                }
                if(mWeek02.get(position).getEndTime()!= 0){
                    stringBuilder.append(DataUtil.timeFormat(mWeek02.get(position).getEndTime(), "yyyy-MM-dd HH:mm"));
                }
                if (mWeek02.get(position).getRooms() != null && mWeek02.get(position).getRooms().size() > 0) {
                    for (int j = 0; j < mWeek02.get(position).getRooms().size(); j++) {
                        rooms = rooms + "," + mWeek02.get(position).getRooms().get(j);
                    }
                    if (!TextUtils.isEmpty(rooms)) {
                        rooms = rooms.substring(1, rooms.length());
                    }
                }
                new StaffDialog(StaffCountActivity.this, name, "计划时间：" + planTime, "巡检时间：" + stringBuilder.toString(), "巡检区域：" + rooms, "1").show();
            }
        });
        mWeek03 = new ArrayList<>();
        RVAdapter<WeekList.WeekBean> adapter03 = new RVAdapter<WeekList.WeekBean>(mRecy03, mWeek03, R.layout.item_staff_time) {
            @Override
            public void showData(ViewHolder vHolder, WeekList.WeekBean data, int position) {
                TextView mTime = (TextView) vHolder.getView(R.id.id_staff_item_time);
                mTime.setText(data.getTaskName());
            }
        };
        mRecy03.setAdapter(adapter03);
        adapter03.setOnItemClickListener(new RVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String name = mWeek03.get(position).getTaskName();
                String planTime = DataUtil.timeFormat(mWeek03.get(position).getPlanStartTime(), "yyyy-MM-dd HH:mm") + "至" + DataUtil.timeFormat(mWeek03.get(position).getPlanEndTime(), "yyyy-MM-dd HH:mm");
                String rooms = "";
                StringBuilder stringBuilder = new StringBuilder();
                if (mWeek03.get(position).getStartTime() != 0 ) {
                    stringBuilder.append(DataUtil.timeFormat(mWeek03.get(position).getStartTime(), "yyyy-MM-dd HH:mm") + "至");
                }
                if(mWeek03.get(position).getEndTime()!= 0){
                    stringBuilder.append(DataUtil.timeFormat(mWeek03.get(position).getEndTime(), "yyyy-MM-dd HH:mm"));
                }
                if (mWeek03.get(position).getRooms() != null && mWeek03.get(position).getRooms().size() > 0) {
                    for (int j = 0; j < mWeek03.get(position).getRooms().size(); j++) {
                        rooms = rooms + "," + mWeek03.get(position).getRooms().get(j);
                    }
                    if (!TextUtils.isEmpty(rooms)) {
                        rooms = rooms.substring(1, rooms.length());
                    }
                }
                new StaffDialog(StaffCountActivity.this, name, "计划时间：" + planTime, "巡检时间：" + stringBuilder.toString(), "巡检区域：" + rooms, "1").show();
            }
        });
        mWeek04 = new ArrayList<>();
        RVAdapter<WeekList.WeekBean> adapter04 = new RVAdapter<WeekList.WeekBean>(mRecy04, mWeek04, R.layout.item_staff_time) {
            @Override
            public void showData(ViewHolder vHolder, WeekList.WeekBean data, int position) {
                TextView mTime = (TextView) vHolder.getView(R.id.id_staff_item_time);
                mTime.setText(data.getTaskName());
            }
        };
        mRecy04.setAdapter(adapter04);
        adapter04.setOnItemClickListener(new RVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String name = mWeek04.get(position).getTaskName();
                String planTime = DataUtil.timeFormat(mWeek04.get(position).getPlanStartTime(), "yyyy-MM-dd HH:mm") + "至" + DataUtil.timeFormat(mWeek04.get(position).getPlanEndTime(), "yyyy-MM-dd HH:mm");
                String rooms = "";
                StringBuilder stringBuilder = new StringBuilder();
                if (mWeek04.get(position).getStartTime() != 0 ) {
                    stringBuilder.append(DataUtil.timeFormat(mWeek04.get(position).getStartTime(), "yyyy-MM-dd HH:mm") + "至");
                }
                if(mWeek04.get(position).getEndTime()!= 0){
                    stringBuilder.append(DataUtil.timeFormat(mWeek04.get(position).getEndTime(), "yyyy-MM-dd HH:mm"));
                }
                if (mWeek04.get(position).getRooms() != null && mWeek04.get(position).getRooms().size() > 0) {
                    for (int j = 0; j < mWeek04.get(position).getRooms().size(); j++) {
                        rooms = rooms + "," + mWeek04.get(position).getRooms().get(j);
                    }
                    if (!TextUtils.isEmpty(rooms)) {
                        rooms = rooms.substring(1, rooms.length());
                    }
                }
                new StaffDialog(StaffCountActivity.this, name, "计划时间：" + planTime, "巡检时间：" + stringBuilder.toString(), "巡检区域：" + rooms, "1").show();
            }
        });

        mWeek05 = new ArrayList<>();
        RVAdapter<WeekList.WeekBean> adapter05 = new RVAdapter<WeekList.WeekBean>(mRecy05, mWeek05, R.layout.item_staff_time) {
            @Override
            public void showData(ViewHolder vHolder, WeekList.WeekBean data, int position) {
                TextView mTime = (TextView) vHolder.getView(R.id.id_staff_item_time);
                mTime.setText(data.getTaskName());
            }
        };
        mRecy05.setAdapter(adapter05);
        adapter05.setOnItemClickListener(new RVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String name = mWeek05.get(position).getTaskName();
                String planTime = DataUtil.timeFormat(mWeek05.get(position).getPlanStartTime(), "yyyy-MM-dd HH:mm") + "至" + DataUtil.timeFormat(mWeek05.get(position).getPlanEndTime(), "yyyy-MM-dd HH:mm");
                String rooms = "";
                StringBuilder stringBuilder = new StringBuilder();
                if (mWeek05.get(position).getStartTime() != 0 ) {
                    stringBuilder.append(DataUtil.timeFormat(mWeek05.get(position).getStartTime(), "yyyy-MM-dd HH:mm") + "至");
                }
                if(mWeek05.get(position).getEndTime()!= 0){
                    stringBuilder.append(DataUtil.timeFormat(mWeek05.get(position).getEndTime(), "yyyy-MM-dd HH:mm"));
                }
                if (mWeek05.get(position).getRooms() != null && mWeek05.get(position).getRooms().size() > 0) {
                    for (int j = 0; j < mWeek05.get(position).getRooms().size(); j++) {
                        rooms = rooms + "," + mWeek05.get(position).getRooms().get(j);
                    }
                    if (!TextUtils.isEmpty(rooms)) {
                        rooms = rooms.substring(1, rooms.length());
                    }
                }
                new StaffDialog(StaffCountActivity.this, name, "计划时间：" + planTime, "巡检时间：" + stringBuilder.toString(), "巡检区域：" + rooms, "1").show();
            }
        });
        mWeek06 = new ArrayList<>();
        RVAdapter<WeekList.WeekBean> adapter06 = new RVAdapter<WeekList.WeekBean>(mRecy06, mWeek06, R.layout.item_staff_time) {
            @Override
            public void showData(ViewHolder vHolder, WeekList.WeekBean data, int position) {
                TextView mTime = (TextView) vHolder.getView(R.id.id_staff_item_time);
                mTime.setText(data.getTaskName());
            }
        };
        mRecy06.setAdapter(adapter06);
        adapter06.setOnItemClickListener(new RVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String name = mWeek06.get(position).getTaskName();
                String planTime = DataUtil.timeFormat(mWeek06.get(position).getPlanStartTime(), "yyyy-MM-dd HH:mm") + "至" + DataUtil.timeFormat(mWeek06.get(position).getPlanEndTime(), "yyyy-MM-dd HH:mm");
                String rooms = "";
                StringBuilder stringBuilder = new StringBuilder();
                if (mWeek06.get(position).getStartTime() != 0 ) {
                    stringBuilder.append(DataUtil.timeFormat(mWeek06.get(position).getStartTime(), "yyyy-MM-dd HH:mm") + "至");
                }
                if(mWeek06.get(position).getEndTime()!= 0){
                    stringBuilder.append(DataUtil.timeFormat(mWeek06.get(position).getEndTime(), "yyyy-MM-dd HH:mm"));
                }
                if (mWeek06.get(position).getRooms() != null && mWeek06.get(position).getRooms().size() > 0) {
                    for (int j = 0; j < mWeek06.get(position).getRooms().size(); j++) {
                        rooms = rooms + "," + mWeek06.get(position).getRooms().get(j);
                    }
                    if (!TextUtils.isEmpty(rooms)) {
                        rooms = rooms.substring(1, rooms.length());
                    }
                }
                new StaffDialog(StaffCountActivity.this, name, "计划时间：" + planTime, "巡检时间：" + stringBuilder.toString(), "巡检区域：" + rooms, "1").show();
            }
        });
        mWeek07 = new ArrayList<>();
        RVAdapter<WeekList.WeekBean> adapter07 = new RVAdapter<WeekList.WeekBean>(mRecy07, mWeek07, R.layout.item_staff_time) {
            @Override
            public void showData(ViewHolder vHolder, WeekList.WeekBean data, int position) {
                TextView mTime = (TextView) vHolder.getView(R.id.id_staff_item_time);
                mTime.setText(data.getTaskName());
            }
        };
        mRecy07.setAdapter(adapter07);
        adapter07.setOnItemClickListener(new RVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String name = mWeek07.get(position).getTaskName();
                String planTime = DataUtil.timeFormat(mWeek07.get(position).getPlanStartTime(), "yyyy-MM-dd HH:mm") + "至" + DataUtil.timeFormat(mWeek07.get(position).getPlanEndTime(), "yyyy-MM-dd HH:mm");
                String rooms = "";
                StringBuilder stringBuilder = new StringBuilder();
                if (mWeek07.get(position).getStartTime() != 0 ) {
                    stringBuilder.append(DataUtil.timeFormat(mWeek07.get(position).getStartTime(), "yyyy-MM-dd HH:mm") + "至");
                }
                if(mWeek07.get(position).getEndTime()!= 0){
                    stringBuilder.append(DataUtil.timeFormat(mWeek07.get(position).getEndTime(), "yyyy-MM-dd HH:mm"));
                }
                if (mWeek07.get(position).getRooms() != null && mWeek07.get(position).getRooms().size() > 0) {
                    for (int j = 0; j < mWeek07.get(position).getRooms().size(); j++) {
                        rooms = rooms + "," + mWeek07.get(position).getRooms().get(j);
                    }
                    if (!TextUtils.isEmpty(rooms)) {
                        rooms = rooms.substring(1, rooms.length());
                    }
                }
                new StaffDialog(StaffCountActivity.this, name, "计划时间：" + planTime, "巡检时间：" + stringBuilder.toString(), "巡检区域：" + rooms, "1").show();
            }
        });
        mMonthCounts = new ArrayList<>();
        RVAdapter<MonthCount> adapter = new RVAdapter<MonthCount>(mRecyLeave, mMonthCounts, R.layout.item_staff_leave) {
            @Override
            public void showData(ViewHolder vHolder, MonthCount data, int position) {
                TextView name = (TextView) vHolder.getView(R.id.id_staff_item_leave);
                name.setText(data.getTaskName());
            }
        };
        mRecyLeave.setAdapter(adapter);
        adapter.setOnItemClickListener(new RVAdapter.OnItemClickListener()

        {
            @Override
            public void onItemClick(View view, int position) {
                String name = mMonthCounts.get(position).getTaskName();
                String planTime = DataUtil.timeFormat(mMonthCounts.get(position).getPlanStartTime(), "yyyy-MM-dd HH:mm") + "至" + DataUtil.timeFormat(mMonthCounts.get(position).getPlanEndTime(), "yyyy-MM-dd HH:mm");
                String rooms = "";
                StringBuilder stringBuilder = new StringBuilder();
                if (mMonthCounts.get(position).getStartTime() != 0 ) {
                    stringBuilder.append(DataUtil.timeFormat(mMonthCounts.get(position).getStartTime(), "yyyy-MM-dd HH:mm") + "至");
                }
                if(mMonthCounts.get(position).getEndTime()!= 0){
                    stringBuilder.append(DataUtil.timeFormat(mMonthCounts.get(position).getEndTime(), "yyyy-MM-dd HH:mm"));
                }
                if (mMonthCounts.get(position).getRooms() != null && mMonthCounts.get(position).getRooms().size() > 0) {
                    for (int j = 0; j < mMonthCounts.get(position).getRooms().size(); j++) {
                        rooms = rooms + "," + mMonthCounts.get(position).getRooms().get(j);
                    }
                    if (!TextUtils.isEmpty(rooms)) {
                        rooms = rooms.substring(1, rooms.length());
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
        mGroup = (TextView) findViewById(R.id.id_fault_group);
        mYesterday = (TextView) findViewById(R.id.id_staff_yesterday);
        mToday = (TextView) findViewById(R.id.id_staff_today);

        mTimeList = (LinearLayout) findViewById(R.id.id_staff_time_list);
        mComeRecy = (ExpendRecycleView) findViewById(R.id.id_staff_time);
        mComeRecy.setLayoutManager(new GridLayoutManager(this, 1));

        //设置布局管理器
        LinearLayoutManager linearLayoutManager01 = new LinearLayoutManager(this);
        linearLayoutManager01.setOrientation(LinearLayoutManager.VERTICAL);//水平滑动
        mRecy01 = (ExpendRecycleView) findViewById(R.id.id_staff_rec01);
        mRecy01.setLayoutManager(linearLayoutManager01);

        LinearLayoutManager linearLayoutManager02 = new LinearLayoutManager(this);
        linearLayoutManager02.setOrientation(LinearLayoutManager.VERTICAL);//水平滑动
        mRecy02 = (ExpendRecycleView) findViewById(R.id.id_staff_rec02);
        mRecy02.setLayoutManager(linearLayoutManager02);

        LinearLayoutManager linearLayoutManager03 = new LinearLayoutManager(this);
        linearLayoutManager03.setOrientation(LinearLayoutManager.VERTICAL);//水平滑动
        mRecy03 = (ExpendRecycleView) findViewById(R.id.id_staff_rec03);
        mRecy03.setLayoutManager(linearLayoutManager03);

        LinearLayoutManager linearLayoutManager04 = new LinearLayoutManager(this);
        linearLayoutManager04.setOrientation(LinearLayoutManager.VERTICAL);//水平滑动
        mRecy04 = (ExpendRecycleView) findViewById(R.id.id_staff_rec04);
        mRecy04.setLayoutManager(linearLayoutManager04);

        LinearLayoutManager linearLayoutManager05 = new LinearLayoutManager(this);
        linearLayoutManager05.setOrientation(LinearLayoutManager.VERTICAL);//水平滑动
        mRecy05 = (ExpendRecycleView) findViewById(R.id.id_staff_rec05);
        mRecy05.setLayoutManager(linearLayoutManager05);

        LinearLayoutManager linearLayoutManager06 = new LinearLayoutManager(this);
        linearLayoutManager06.setOrientation(LinearLayoutManager.VERTICAL);//水平滑动
        mRecy06 = (ExpendRecycleView) findViewById(R.id.id_staff_rec06);
        mRecy06.setLayoutManager(linearLayoutManager06);

        LinearLayoutManager linearLayoutManager07 = new LinearLayoutManager(this);
        linearLayoutManager07.setOrientation(LinearLayoutManager.VERTICAL);//水平滑动
        mRecy07 = (ExpendRecycleView) findViewById(R.id.id_staff_rec07);
        mRecy07.setLayoutManager(linearLayoutManager07);

        mRecyLeave = (ExpendRecycleView) findViewById(R.id.id_staff_leave);
        mRecyLeave.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
        mMonthNum = (TextView) findViewById(R.id.id_staff_leavecount);
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
        final List<String> mNames = new ArrayList<>();
        for (int i = 0; i < mDeptTypes.size(); i++) {
            mNames.add(mDeptTypes.get(i).getDeptName());
        }
        mGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isShowGrop) {
                    isShowGrop = true;
                    Utils.setDrawable(StaffCountActivity.this, R.drawable.up_arrow, mGroup, 2);
                } else {
                    isShowGrop = false;
                    Utils.setDrawable(StaffCountActivity.this, R.drawable.drop_down_arrow, mGroup, 2);
                }
                new MaterialDialog.Builder(StaffCountActivity.this)
                        .items(mNames)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                                mGroup.setText(mDeptTypes.get(position).getDeptName());
                                mDeptId = mDeptTypes.get(position).getDeptId();
                                isShowGrop = false;
                                Utils.setDrawable(StaffCountActivity.this, R.drawable.drop_down_arrow, mGroup, 2);
                                mPresenter.getComeCount(mNowTime, mDeptId + "");
//                                mPresenter.getWeekCount(mNowTime, mDeptId+"");
                                mPresenter.getWeekList(mNowTime, mDeptId + "");
                                mPresenter.getMonthCount(mNowTime, mDeptId + "");
                            }
                        })
                        .show();
            }
        });
        if (mDeptId != -1) {
            mPresenter.getComeCount(mNowTime, mDeptId + "");
//            mPresenter.getWeekCount(mNowTime, mDeptId+"");
            mPresenter.getWeekList(mNowTime, mDeptId + "");
            mPresenter.getMonthCount(mNowTime, mDeptId + "");
        }
    }

    @Override
    public void showComeCount(List<ComeCount> comeCounts) {
        mComeCounts.clear();
//        mTimeList.removeAllViews();
        mComeCounts.addAll(comeCounts);
        if (mComeCounts.size() > 0) {
            mComeRecy.setVisibility(View.VISIBLE);
        } else {
            mComeRecy.setVisibility(View.GONE);
        }
        mComeRecy.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void showWeekCount(List<WeekCount> weekCounts) {

    }

    @Override
    public void showMonthCount(List<MonthCount> monthCounts) {
        mMonthNoGet = monthCounts.size();
        mMonthNum.setText("本月未到岗统计 " + mMonthNoGet);
        mMonthCounts.clear();
        mMonthCounts.addAll(monthCounts);
        mRecyLeave.getAdapter().notifyDataSetChanged();
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
                //mWeek01.clear();
                mWeek01.addAll(weekLists.get(0).getWeek());

            }
            mRecy01.getAdapter().notifyDataSetChanged();
            if (weekLists.get(1) != null && weekLists.get(1).getWeek() != null && weekLists.get(1).getWeek().size() > 0) {
                //mWeek02.clear();
                mWeek02.addAll(weekLists.get(1).getWeek());

            }
            mRecy02.getAdapter().notifyDataSetChanged();
            if (weekLists.get(2) != null && weekLists.get(2).getWeek() != null && weekLists.get(2).getWeek().size() > 0) {
                //  mWeek03.clear();
                mWeek03.addAll(weekLists.get(2).getWeek());

            }
            mRecy03.getAdapter().notifyDataSetChanged();
            if (weekLists.get(3) != null && weekLists.get(3).getWeek() != null && weekLists.get(3).getWeek().size() > 0) {
                //mWeek04.clear();
                mWeek04.addAll(weekLists.get(3).getWeek());

            }
            mRecy04.getAdapter().notifyDataSetChanged();
            if (weekLists.get(4) != null && weekLists.get(4).getWeek() != null && weekLists.get(4).getWeek().size() > 0) {
                //mWeek05.clear();
                mWeek05.addAll(weekLists.get(4).getWeek());

            }
            mRecy05.getAdapter().notifyDataSetChanged();
            if (weekLists.get(5) != null && weekLists.get(5).getWeek() != null && weekLists.get(5).getWeek().size() > 0) {
                //mWeek06.clear();
                mWeek06.addAll(weekLists.get(5).getWeek());

            }
            mRecy06.getAdapter().notifyDataSetChanged();
            if (weekLists.get(6) != null && weekLists.get(6).getWeek() != null && weekLists.get(6).getWeek().size() > 0) {
                // mWeek07.clear();
                mWeek07.addAll(weekLists.get(6).getWeek());

            }
            mRecy07.getAdapter().notifyDataSetChanged();
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
