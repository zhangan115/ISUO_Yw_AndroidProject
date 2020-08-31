package com.sito.customer.view.home.work.sendmessage;

import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

import org.json.JSONObject;

import java.util.List;

/**
 * 发送消息
 * Created by zhangan on 2018/3/6.
 */

public interface SendMessageContract {

    interface Presenter extends BasePresenter {

        void sendMessage(JSONObject jsonObject);

        void uploadFile(List<String> fileList);

    }

    interface View extends BaseView<Presenter> {

        void sendSuccess();

        void showMessage(String message);

        void uploadFileSuccess(String fileUrl);

        void uploadFileFail();

        void showUploadProgress();

        void hideUploadProgress();
    }
}
