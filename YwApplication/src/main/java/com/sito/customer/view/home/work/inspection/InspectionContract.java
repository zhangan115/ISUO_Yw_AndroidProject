package com.sito.customer.view.home.work.inspection;

import android.support.annotation.NonNull;

import com.sito.customer.mode.bean.work.InspectionBean;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

import java.util.List;

/**
 * 巡检界面
 * Created by zhangan on 2017-06-22.
 */
interface InspectionContract {

    interface Presenter extends BasePresenter {

        void getData(int inspectionType, @NonNull String time);

        void getDataMore(int inspectionType,@NonNull String time, @NonNull String lastId);

        void operationTask(String taskId, int position);
    }

    interface View extends BaseView<Presenter> {

        void showData(List<InspectionBean> been);

        void showMoreData(List<InspectionBean> been);

        void showLoading();

        void hideLoading();

        void noData();

        void noMoreData();

        void hideLoadingMore();

        void operationSuccess(int position);
    }

}
