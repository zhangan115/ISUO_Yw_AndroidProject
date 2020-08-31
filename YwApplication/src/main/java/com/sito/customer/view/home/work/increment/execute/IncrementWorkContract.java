package com.sito.customer.view.home.work.increment.execute;

import com.sito.customer.mode.bean.db.Image;
import com.sito.customer.mode.bean.db.Voice;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

import java.util.List;

/**
 * 专项工作详情
 * Created by zhangan on 2017/10/10.
 */

interface IncrementWorkContract {

    interface View extends BaseView<Presenter> {

        void uploadVoiceSuccess(String url);

        void showMessage(String message);

        void uploadImageSuccess(Image image);

        void uploadImageSuccess(int position, Image image);

        void uploadAllDataSuccess();

        void showDataFromDb(List<Image> imageList, Voice voice);

        void uploadImageSuccess();

        void uploadImageFail(Image image);

        void startSuccess();

    }

    interface Presenter extends BasePresenter {

        void loadDataFromDb(long workId);

        void postVoiceFile(String businessType, Voice voice);

        void saveVoiceToDb(Voice voice);

        void uploadIncrementInfo(String jsonStr);

        void cleanAllData(List<Image> imageList, Voice voice);

        /**
         * 上传照片数据
         *
         * @param image 图片
         */
        void uploadImage(int workType, long workId, String businessType, Image image);

        void startIncrementWork(long workId);
    }
}
