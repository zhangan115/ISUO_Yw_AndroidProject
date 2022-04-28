package com.isuo.yw2application.view.main.task.inspection.input;

import android.support.annotation.Nullable;

import com.isuo.yw2application.mode.bean.db.RoomDb;
import com.isuo.yw2application.mode.bean.equip.FocusBean;
import com.isuo.yw2application.mode.bean.inspection.DataItemBean;
import com.isuo.yw2application.mode.bean.inspection.InspectionDetailBean;
import com.isuo.yw2application.mode.bean.inspection.TaskEquipmentBean;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

/**
 * 数据录入
 * Created by zhangan on 2018/2/27.
 */

public interface InputContract {

    interface Presenter extends BasePresenter {

        /**
         * 上传照片
         *
         * @param dataItemBean 巡检数据对象
         */
        void uploadImage(DataItemBean dataItemBean);

        /**
         * 保存数据
         *
         * @param taskEquipmentBean 对象数据
         * @param isFinish            是否完成巡检，并可以上传
         */
        void saveData(TaskEquipmentBean taskEquipmentBean, boolean isFinish);

        /**
         * 获取对象重点关注数据
         *
         * @param equipmentId 对象id
         */
        void getEquipmentCare(long equipmentId);

        /**
         * 获取共享信息
         *
         * @param taskEquipmentBean 对象数据
         */
        void getShareData(TaskEquipmentBean taskEquipmentBean);

        /**
         * 获取保存在sp文件中的对象信息
         */
        void getTaskEquipFromCache();

        /**
         * 将数据保存到cache中
         *
         * @param taskEquipmentBean 对象
         */
        void saveTaskEquipToCache(TaskEquipmentBean taskEquipmentBean);

        /**
         * 获取任务数据
         *
         * @return 任务
         */
        @Nullable
        InspectionDetailBean getInspectionData(long taskId);

        /**
         * step 1
         * 上传需要拍照的对象照片
         *
         * @param roomDb room
         */
        void uploadEquipmentPhotoImage(RoomDb roomDb);

        /**
         * step 2
         * 上传需要拍照的对象信息
         *
         * @param taskId      任务id
         * @param equipmentId 对象id
         * @param url         图片地址
         */
        void uploadEquipmentInfo(long taskId, long equipmentId, String url);

        /**
         * step 3
         *
         * @param position             位置
         * @param inspectionDetailBean 任务
         * @param equipmentBean        录入的对象
         */
        void uploadTaskData(int position, InspectionDetailBean inspectionDetailBean, TaskEquipmentBean equipmentBean);

    }

    interface View extends BaseView<Presenter> {

        /**
         * 显示对象数据
         */
        void showTaskEquipmentData();

        /**
         * 获取缓存的数据
         *
         * @param taskEquipmentBean 对象
         */
        void showTaskEquipFromCache(TaskEquipmentBean taskEquipmentBean);

        /**
         * 显示重点关注信息
         *
         * @param f 关注信息
         */
        void showCareData(FocusBean f);

        /**
         * 请求重点关注信息出错
         */
        void showCareDataFail();

        /**
         * 上传照片完成
         *
         * @param isSuccess 是否成功
         */
        void uploadItemPhotoFinish(boolean isSuccess);

        /**
         * 上传对象图片完成
         *
         * @param isSuccess 是否成功
         */
        void uploadREquipmentPhotoFinish(boolean isSuccess);

        /**
         * 提交对象图片信息完成
         *
         * @param isSuccess 是否成功
         */
        void uploadEquipmentInfoFinish(boolean isSuccess);

        /**
         * 显示上传loading
         */
        void showUploadLoading();

        /**
         * 隐藏上传loading
         */
        void hideUploadLoading();

        /**
         * 上传对象录入数据失败
         */
        void uploadDataError();

        /**
         * 上传对象录入数据成功
         */
        void uploadDataSuccess();
    }

}
