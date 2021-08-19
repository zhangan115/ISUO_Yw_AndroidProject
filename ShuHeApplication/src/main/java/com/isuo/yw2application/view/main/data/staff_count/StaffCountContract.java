package com.isuo.yw2application.view.main.data.staff_count;

import com.isuo.yw2application.mode.bean.count.ComeCount;
import com.isuo.yw2application.mode.bean.count.MonthCount;
import com.isuo.yw2application.mode.bean.count.WeekCount;
import com.isuo.yw2application.mode.bean.count.WeekList;
import com.isuo.yw2application.mode.bean.discover.DeptType;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

import java.util.List;

/**
 * Created by Yangzb on 2017/7/4 16:06
 * E-mail：yangzongbin@si-top.com
 */
interface StaffCountContract {

    interface Presenter extends BasePresenter {
        void getDeptId(String types);

        void getComeCount(String startTime, String endTime,String deptId);

        void getComeCount(String time, String deptId);

        void getWeekCount(String time, String deptId);

        void getMonthCount(String time, String deptId);

        void getWeekList(String time, String deptId);
    }

    interface View extends BaseView<Presenter> {
        void showDeptId(List<DeptType> deptTypes);

        void showComeCount(List<ComeCount> comeCounts);

        void showWeekCount(List<WeekCount> weekCounts);

        void showMonthCount(List<MonthCount> monthCounts);

        void showWeekList(List<WeekList> weekLists);

        void showLoading();

        void hideLoading();

        void noData();
    }
}