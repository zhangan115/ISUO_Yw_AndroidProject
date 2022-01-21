package com.isuo.yw2application.view.main.task.inspection.user_list;

import com.isuo.yw2application.mode.bean.work.WorkInspectionBean;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

import java.util.List;

public interface InspectionTaskContract {

    interface Presenter extends BasePresenter {

        void getTaskList(int userId, int count);

        void getMoreTaskList(int userId, int count, long lastId);
    }

    interface View extends BaseView<Presenter> {

        void showData(List<WorkInspectionBean> lists);

        void showMoreData(List<WorkInspectionBean> lists);

        void noMoreData();

        void hideLoadingMore();

        void showLoading();

        void hideLoading();

        void noData();
    }
}
