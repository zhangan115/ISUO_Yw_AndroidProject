package com.sito.evpro.inspection.view.repair.inspection.input;

import com.sito.evpro.inspection.mode.bean.inspection.DataItemBean;
import com.sito.evpro.inspection.mode.bean.inspection.TaskEquipmentBean;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

/**
 * 数据录入
 * Created by zhangan on 2018/2/27.
 */

public interface InputContract {

    interface Presenter extends BasePresenter {

        void uploadImage(DataItemBean dataItemBean);

        void saveData(TaskEquipmentBean taskEquipmentBean, boolean isAuto);

        void startAutoSave(TaskEquipmentBean taskEquipmentBean);

        void stopAutoSave();
    }

    interface View extends BaseView<Presenter> {

        void uploadPhotoSuccess();

        void uploadPhotoFail();

    }

}
