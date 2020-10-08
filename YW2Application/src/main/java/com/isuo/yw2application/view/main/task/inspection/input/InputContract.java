package com.isuo.yw2application.view.main.task.inspection.input;

import com.isuo.yw2application.mode.bean.equip.FocusBean;
import com.isuo.yw2application.mode.bean.inspection.DataItemBean;
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
         * @param taskEquipmentBean 设备数据
         * @param isAuto            是否自动保存
         */
        void saveData(TaskEquipmentBean taskEquipmentBean, boolean isAuto);

        /**
         * 获取设备重点关注数据
         *
         * @param equipmentId 设备id
         */
        void getEquipmentCare(long equipmentId);

        /**
         * 获取共享信息
         *
         * @param taskEquipmentBean 设备数据
         */
        void getShareData(TaskEquipmentBean taskEquipmentBean);

        /**
         * 获取保存在sp文件中的设备信息
         */
        void getTaskEquipFromCache();

        /**
         * 将数据保存到cache中
         *
         * @param taskEquipmentBean 设备
         */
        void saveTaskEquipToCache(TaskEquipmentBean taskEquipmentBean);

    }

    interface View extends BaseView<Presenter> {

        /**
         * 上传照片成功
         */
        void uploadPhotoSuccess();

        /**
         * 上传照片失败
         */
        void uploadPhotoFail();

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
         * 显示设备数据
         */
        void showTaskEquipmentData();

        /**
         * 获取缓存的数据
         *
         * @param taskEquipmentBean 设备
         */
        void showTaskEquipFromCache(TaskEquipmentBean taskEquipmentBean);

    }

}
