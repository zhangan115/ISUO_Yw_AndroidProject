package com.isuo.yw2application.view.main.task.inspection.work;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.isuo.yw2application.mode.bean.db.TaskDb;
import com.isuo.yw2application.mode.bean.employee.EmployeeBean;
import com.isuo.yw2application.mode.bean.inspection.InspectionDetailBean;
import com.isuo.yw2application.mode.bean.inspection.RoomListBean;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

import java.util.ArrayList;
import java.util.List;

/**
 * 巡检room
 * Created by zhangan on 2017-06-26.
 */

interface InspectionRoomContract {

    interface Presenter extends BasePresenter {

        /**
         * 获取任务数据
         *
         * @param taskId 任务id
         */
        void getInspectionDataList(long taskId);

        /**
         * 从数据库获取保存的巡检人员
         *
         * @param taskId 任务id
         */
        void loadTaskUserFromDb(long taskId);

        /**
         * 保存人员
         *
         * @param taskId     任务id
         * @param taskDbList 人员集合
         */
        void saveEmployee(long taskId, @NonNull List<EmployeeBean> taskDbList);

        /**
         * 更新配电室状态
         *
         * @param taskId       任务id
         * @param roomListBean 配电室
         * @param operation    状态
         */
        void updateRoomState(long taskId, RoomListBean roomListBean, int operation);

        /**
         * 完成任务
         *
         * @param taskId    任务id
         * @param operation 状态
         * @param userIds   用户id
         */
        void roomListFinish(long taskId, int operation, String userIds);

        /**
         * 从数据库获取配电室信息
         *
         * @param taskId 任务id
         * @param list   配电室集合
         */
        void loadRoomDataFromDb(long taskId, List<RoomListBean> list);

        /**
         * 从cache获取任务数据
         *
         * @return 任务
         */
        InspectionDetailBean getInspectionFromCache();

        /**
         * 保存巡检到cache
         *
         * @param bean 任务
         */
        void saveInspectionToCache(@Nullable InspectionDetailBean bean);
    }

    interface View extends BaseView<Presenter> {

        void showData(InspectionDetailBean inspectionBeen);

        void showData();

        void showLoading();

        void hideLoading();

        void noData();

        void showMessage(String message);

        void showTaskUser(@NonNull ArrayList<TaskDb> taskDbList);

        void updateRoomStateSuccess();

        void finishAllRoom();
    }
}
