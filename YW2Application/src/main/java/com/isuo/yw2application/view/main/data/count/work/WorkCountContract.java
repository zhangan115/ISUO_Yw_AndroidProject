package com.isuo.yw2application.view.main.data.count.work;

import com.isuo.yw2application.mode.bean.count.WorkCount;
import com.isuo.yw2application.mode.bean.discover.DeptType;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

import java.util.List;

/**
 * Created by zhangan on 2017-07-19.
 */

 class WorkCountContract {

    interface Presenter extends BasePresenter {

        void getDeptList(String deptType);

        void getWorkCountData(String deptId, String time);

    }

    interface View extends BaseView<Presenter> {

        void showLoading();

        void hideLoading();

        void noData();

        void showDeptList(List<DeptType> deptTypes);

        void showWorkCountData(List<WorkCount> workCounts);
    }
}
