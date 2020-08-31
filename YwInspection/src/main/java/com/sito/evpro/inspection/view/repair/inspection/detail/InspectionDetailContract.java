package com.sito.evpro.inspection.view.repair.inspection.detail;

import android.support.annotation.NonNull;

import com.sito.evpro.inspection.mode.bean.db.RoomDb;
import com.sito.evpro.inspection.mode.bean.db.TaskDb;
import com.sito.evpro.inspection.mode.bean.employee.EmployeeBean;
import com.sito.evpro.inspection.mode.bean.inspection.InspectionDetailBean;
import com.sito.evpro.inspection.mode.bean.inspection.RoomListBean;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

import java.util.List;

/**
 * Created by zhangan on 2017-06-26.
 */

interface InspectionDetailContract {

    interface Presenter extends BasePresenter {

        void getInspectionDetailList(@NonNull String taskId);

        void saveEmployee(long taskId, @NonNull List<EmployeeBean> employeeBeen);

        void loadTaskUserFromDb(long taskId);

        void changeRoomState(long taskId, RoomListBean roomListBean);

        void refreshRoomData(long taskId, RoomDb roomDb);

        void updateRoomState(long taskId, long taskRoomId, int operation);

        void updateTaskAll(long taskId, int operation, String userIds);

        boolean checkEquipUploadState(long taskId, long roomId);
    }

    interface View extends BaseView<Presenter> {

        void showData(InspectionDetailBean inspectionBeen);

        void showLoading();

        void hideLoading();

        void noData();

        void showTaskUser(@NonNull List<TaskDb> taskDbList);

        void updateRoomStateSuccess(long roomId);

        void finishAllRoom();

        void refreshRoomData(RoomDb roomDb);
    }
}
