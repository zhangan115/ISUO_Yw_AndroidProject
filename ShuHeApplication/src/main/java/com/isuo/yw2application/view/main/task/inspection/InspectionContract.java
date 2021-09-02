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

        /**
         * 从网络中获取数据
         *
         * @param inspectionType 巡检类型
         * @param time           时间
         */
        void getData(int inspectionType, @NonNull String time);

        /**
         * 从缓存中获取数据
         *
         * @param inspection 巡检类型
         * @param time       时间
         */
        void getDataFromCache(int inspection, String time);

        /**
         * 保存数据到缓存中
         *
         * @param inspection         巡检类型
         * @param time               时间
         * @param inspectionBeanList 数据
         */
        void toSaveInspectionDataToCache(int inspection, String time, List<InspectionBean> inspectionBeanList);

        /**
         * 领取任务
         *
         * @param taskId         任务id
         * @param inspectionBean 任务
         */
        void operationTask(String taskId, InspectionBean inspectionBean);

        /**
         * 获取任务数据
         *
         * @param inspectionBean 任务
         */
        void getInspectionDataList(InspectionBean inspectionBean);

        /**
         * 获取可以上传的数据
         *
         * @param list 集合
         */
        List<InspectionBean>  getUploadTask(List<InspectionBean> list);

        /**
         * 上传任务数据
         *
         * @param task 任务
         */
        void uploadTaskData(InspectionBean task);
    }

    interface View extends BaseView<Presenter> {

        void showData(List<InspectionBean> been);

        void showLoading();

        void hideLoading();

        void noData();

        void operationSuccess(InspectionBean bean);

        void uploadNext();

    }

}
