package com.sito.evpro.inspection.api;


import com.sito.evpro.inspection.R;
import com.sito.evpro.inspection.app.InspectionApp;
import com.sito.evpro.inspection.mode.Bean;

import rx.Observer;

/**
 * call back
 * Created by zhangan on 2017-02-17.
 */

public abstract class ApiCallBack1<T> implements Observer<Bean<T>> {

    @Override
    public void onNext(Bean<T> bean) {
        try {
            if (bean != null) {
                int errorCode = bean.getErrorCode();
                if (errorCode == ApiErrorCode.SUCCEED) {
                    onSuccess(bean.getData());
                } else if (errorCode == ApiErrorCode.NOT_LOGGED) {
                    InspectionApp.getInstance().needLogin();
                } else {
                    String errorMessage = bean.getMessage();
                    onError(new NotLoggedThrowable(errorMessage));
                }
            } else {
                onError(new Throwable(InspectionApp.getInstance().getString(R.string.str_request_error)));
            }
        } catch (Exception e) {
            e.printStackTrace();
            onError(new Throwable(e));
        }
    }

    @Override
    public void onError(Throwable e) {
        onCompleted();
        e.printStackTrace();
        onError(e.getMessage());
        InspectionApp.getInstance().showToast(e.getMessage());
    }

    public abstract void onSuccess(T result);

    public abstract void onError(String message);

    public static class NotLoggedThrowable extends Throwable {

        public NotLoggedThrowable() {
        }

        public NotLoggedThrowable(String message) {
            super(message);
        }

        public NotLoggedThrowable(String message, Throwable cause) {
            super(message, cause);
        }

        public NotLoggedThrowable(Throwable cause) {
            super(cause);
        }
    }

}
