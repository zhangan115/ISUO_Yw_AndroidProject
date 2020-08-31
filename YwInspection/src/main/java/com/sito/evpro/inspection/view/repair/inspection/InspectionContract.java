package com.sito.evpro.inspection.view.repair.inspection;

import android.support.annotation.NonNull;

import com.sito.evpro.inspection.mode.bean.inspection.InspectionBean;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

import java.util.List;

/**
 * Created by zhangan on 2017-06-22.
 */
interface InspectionContract {

    interface Presenter extends BasePresenter {

        void getInspectionList(@NonNull String time);

        void getInspectionListMore(@NonNull String time, @NonNull String lastId);

        void operationTask(String taskId, int position);
    }

    interface View extends BaseView<Presenter> {

        void showData(List<InspectionBean> inspectionBeen);

        void showMoreData(List<InspectionBean> inspectionBeen);

        void showLoading();

        void hideLoading();

        void noData();

        void noMoreData();

        void hideLoadingMore();

        void operationSuccess(int position);
    }

}
