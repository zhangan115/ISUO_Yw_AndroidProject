package com.isuo.yw2application.view.main.task.inspection.report;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.isuo.yw2application.mode.bean.db.RoomDb;
import com.isuo.yw2application.mode.bean.inspection.InspectionDetailBean;
import com.isuo.yw2application.mode.bean.inspection.RoomListBean;
import com.isuo.yw2application.mode.bean.inspection.TaskEquipmentBean;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

import java.util.List;

/**
 * 巡检设备列表
 * Created by zhangan on 2017-06-26.
 */

public interface ReportContract {

    interface Presenter extends BasePresenter {

        /**
         * 从数据库获取任务
         *
         * @param taskId       任务id
         * @param roomListBean 配电室
         */
        void loadInspectionDataFromDb(long taskId, @NonNull RoomListBean roomListBean);

        /**
         * 保存需要编辑的设备到缓存
         *
         * @param taskEquipmentBean 设备集合
         */
        void saveEditTaskEquipToCache(TaskEquipmentBean taskEquipmentBean);

        /**
         * 获取设备信息
         *
         * @return 设备
         */
        @Nullable
        TaskEquipmentBean getTaskEquipFromRepository();

        /**
         * 获取任务数据
         *
         * @return 任务
         */
        @Nullable
        InspectionDetailBean getInspectionData();
    }

    interface View extends BaseView<Presenter> {

        void showLoading();

        void hideLoading();

        void showData(@NonNull RoomListBean roomListBean);

    }

}
