package com.isuo.yw2application.view.main.task.inspection;

import android.support.annotation.NonNull;

import com.isuo.yw2application.mode.bean.work.InspectionBean;
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

        void operationTask(String taskId, int groupPosition,int childPosition);
    }

    interface View extends BaseView<Presenter> {

        void showData(List<InspectionBean> been);

        void showMoreData(List<InspectionBean> been);

        void showLoading();

        void hideLoading();

        void noData();

        void operationSuccess(int group,int child);
    }

}
