package com.isuo.yw2application.view.main.work.tool.add;

import com.isuo.yw2application.mode.tools.ToolsSourceData;
import com.isuo.yw2application.mode.tools.bean.Tools;

import java.io.File;

import rx.subscriptions.CompositeSubscription;

/**
 * 工具类
 * Created by zhangan on 2018/4/4.
 */

class AddToolsPresenter implements AddToolsContract.Presenter {

    private final ToolsSourceData sourceData;
    private final AddToolsContract.View mView;

    private CompositeSubscription subscription;

    AddToolsPresenter(ToolsSourceData sourceData, AddToolsContract.View mView) {
        this.sourceData = sourceData;
        this.mView = mView;
        mView.setPresenter(this);
    }

    @Override
    public void uploadImage(File image) {
        subscription.add(sourceData.uploadImageFile(image, new ToolsSourceData.IUploadToolsCallBack() {
            @Override
            public void onSuccess(String url) {
                mView.uploadImageSuccess(url);
            }

            @Override
            public void onError() {
                mView.uploadImageFail();
            }
        }));
    }

    @Override
    public void addTools(Tools tools) {
        subscription.add(sourceData.addTools(tools, new ToolsSourceData.IUploadToolsCallBack() {
            @Override
            public void onSuccess(String url) {
                mView.uploadToolsSuccess();
            }

            @Override
            public void onError() {
                mView.uploadToolsFail();
            }
        }));
    }

    @Override
    public void subscribe() {
        subscription = new CompositeSubscription();
    }

    @Override
    public void unSubscribe() {
        subscription.clear();
    }
}
