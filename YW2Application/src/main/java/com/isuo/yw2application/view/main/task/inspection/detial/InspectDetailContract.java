package com.isuo.yw2application.view.main.task.inspection.detial;

import com.isuo.yw2application.mode.bean.check.CheckBean;
import com.isuo.yw2application.mode.bean.check.FaultList;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

import java.util.List;

/**
 * Created by Yangzb on 2017/7/17 19:33
 * E-mail：yangzongbin@si-top.com
 */
interface InspectDetailContract {

    interface Presenter extends BasePresenter {

        void getCheckInfo(long taskId);

        void getFaultList(long taskId);

        void getMoreFaultList(long taskId, int lastId);
    }

    interface View extends BaseView<Presenter> {

        void showCheckInfo(CheckBean bean);

        void showFaultList(List<FaultList> faultLists);

        void showMoreFaultList(List<FaultList> faultLists);

        void showLoading();

        void hideLoading();

        void noData();

        void noMoreData();

        void hideLoadingMore();
    }

}