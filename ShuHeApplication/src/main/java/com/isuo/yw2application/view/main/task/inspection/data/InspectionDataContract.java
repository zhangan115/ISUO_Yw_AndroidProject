package com.isuo.yw2application.view.main.task.inspection.data;

import com.isuo.yw2application.mode.bean.work.InspectionDataBean;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

/**
 * 巡检数据合约
 * Created by zhangan on 2017/9/27.
 */

interface InspectionDataContract {

    interface View extends BaseView<Presenter> {

        void showLoading();

        void hideLoading();

        void showInspectionData(InspectionDataBean dataBean);

        void noData();
    }

    interface Presenter extends BasePresenter {
        /**
         * 获取巡检数据
         *
         * @param taskId 任务id
         */
        void getInspectionData(Long taskId);
    }

}
