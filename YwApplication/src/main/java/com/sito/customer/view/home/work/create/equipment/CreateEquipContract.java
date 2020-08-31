package com.sito.customer.view.home.work.create.equipment;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

import org.json.JSONObject;

/**
 * 创建设备/修改设备信息
 * Created by zhangan on 2017/9/30.
 */

interface CreateEquipContract {

    interface Presenter extends BasePresenter {

        void uploadImage(@NonNull String businessType, @NonNull String path);

        void uploadEquipment(JSONObject jsonObject);

        void editEquipment(JSONObject jsonObject);

    }

    interface View extends BaseView<Presenter> {

        void showImage(String url);

        void showMessage(@Nullable String message);

        void showLoading();

        void hideLoading();

        void uploadEquipmentSuccess();
    }
}
