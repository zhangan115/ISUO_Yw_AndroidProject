package com.sito.evpro.inspection.view.repair.inspection.report;

import android.support.annotation.NonNull;

import com.sito.evpro.inspection.mode.bean.db.RoomDb;
import com.sito.evpro.inspection.mode.bean.inspection.DataItemBean;
import com.sito.evpro.inspection.mode.bean.inspection.RoomListBean;
import com.sito.evpro.inspection.mode.bean.inspection.TaskEquipmentBean;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

import java.util.List;

/**
 * Created by zhangan on 2017-06-26.
 */

public interface ReportContract {

    interface Presenter extends BasePresenter {

        void startAutoUpload(int position, List<TaskEquipmentBean> taskEquipmentBeans);

        void uploadData(int position, List<TaskEquipmentBean> taskEquipmentBeans, boolean isAuto);

        void uploadRandomImage(RoomDb roomDb);

        void loadInspectionDataFromDb(long taskId, @NonNull RoomListBean roomListBean);

        void uploadUserPhoto(long taskId, long equipmentId, String url);

        boolean checkPhotoNeedUpload(List<TaskEquipmentBean> taskEquipmentBeans);

        void uploadPhotoList(RoomDb roomDb);

    }

    interface View extends BaseView<Presenter> {

        void showLoading();

        void hideLoading();

        void uploadRandomSuccess();

        void uploadRandomFail();

        void showData(@NonNull RoomListBean roomListBean);

        void showUploadLoading();

        void hideUploadLoading();

        void uploadError();

        void showUploadSuccess(boolean isAuto);

        void noDataUpload();

        boolean canUpload();

        void uploadUserPhotoSuccess();

        void uploadUserPhotoFail();

        void uploadOfflinePhotoFinish();

        void uploadOfflinePhotoFail();
    }

}
