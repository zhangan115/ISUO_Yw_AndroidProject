package com.isuo.yw2application.view.main.equip.time.detail;

import com.isuo.yw2application.mode.bean.equip.EquipRecordDetail;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

/**
 * * 检测详情,大修详情,实验详情
 * Created by zhangan on 2017/10/13.
 */

interface EquipmentRecordDetailContract {

    interface Presenter extends BasePresenter {

        void getRecordDetail(long equipmentRecordId);

        void downLoadFile(String url, String savePath, String fileName);

    }

    interface View extends BaseView<Presenter> {

        void showLoading();

        void hideLoading();

        void showData(EquipRecordDetail equipRecordDetail);

        void downLoadSuccess(String filePath);

        void showDownLoadProgress();

        void hideDownProgress();

        void downLoadFail();
    }
}
