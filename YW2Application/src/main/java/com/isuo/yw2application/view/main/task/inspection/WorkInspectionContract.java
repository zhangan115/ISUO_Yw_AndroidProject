package com.isuo.yw2application.view.main.task.inspection;

import android.support.annotation.NonNull;

import com.isuo.yw2application.mode.bean.inspection.InspectionBean;
import com.isuo.yw2application.mode.bean.inspection.RoomListBean;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

import java.util.List;

/**
 * 巡检界面
 * Created by zhangan on 2017-06-22.
 * Update by zhangan on 2022-01-21.
 */
interface WorkInspectionContract {

    interface Presenter extends BasePresenter {

        /**
         * 从网络中获取数据
         *
         * @param inspectionType 巡检类型
         * @param time           时间
         */
        void getData(int inspectionType, @NonNull String time);

        /**
         * 从网络中获取数据
         *
         * @param inspectionType 巡检类型
         * @param time           时间
         * @param isMonitor      观察模式
         */
        void getData(int inspectionType, @NonNull String time, boolean isMonitor);

        /**
         * 从缓存中获取数据
         *
         * @param inspection 巡检类型
         * @param time       时间
         */
        void getDataFromAcCache(int inspection, String time);

        /**
         * 保存数据到缓存中
         *
         * @param inspection         巡检类型
         * @param time               时间
         * @param inspectionBeanList 数据
         */
        void toSaveInspectionDataToAcCache(int inspection, String time, List<InspectionBean> inspectionBeanList);

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
        void getInspectionDetailDataList(InspectionBean inspectionBean);

        /**
         * 获取可以上传的任务数据
         *
         * @param list 集合
         */
        List<InspectionBean> getUploadTask(List<InspectionBean> list);

        /**
         * 上传任务数据
         *
         * @param task         任务
         * @param roomListBean 配电室
         */
        void uploadTaskData(InspectionBean task, RoomListBean roomListBean);
    }

    interface View extends BaseView<Presenter> {

        /**
         * 显示数据
         *
         * @param been 巡检数据
         */
        void showData(List<InspectionBean> been);

        /**
         * 显示Loading
         */
        void showLoading();

        /**
         * 显示TOAST
         *
         * @param message 信息
         */
        void showToast(String message);

        /**
         * 隐藏Loading
         */
        void hideLoading();

        /**
         * 没有数据
         */
        void noData();

        /**
         * 领取数据
         *
         * @param bean 巡检数据
         */
        void operationSuccess(InspectionBean bean);

        /**
         * 上传下一个任务
         */
        void uploadNext();

    }

}
