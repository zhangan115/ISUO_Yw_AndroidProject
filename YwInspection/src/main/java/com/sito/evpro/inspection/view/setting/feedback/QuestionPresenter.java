package com.sito.evpro.inspection.view.setting.feedback;

import android.support.annotation.NonNull;

import com.sito.evpro.inspection.mode.IObjectCallBack;
import com.sito.evpro.inspection.mode.inspection.InspectionRepository;

import javax.inject.Inject;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by Administrator on 2017/6/14.
 */
final class QuestionPresenter implements QuestionContract.Presenter {
    private InspectionRepository mRepository;
    private QuestionContract.View mView;
    @NonNull
    private CompositeSubscription mSubscription;

    @Inject
    QuestionPresenter(InspectionRepository repository, QuestionContract.View view) {
        this.mRepository = repository;
        this.mView = view;
        mSubscription = new CompositeSubscription();
    }

    @Inject
    void setUpListeners() {
        mView.setPresenter(this);
    }

    @Override
    public void postQuestion(String title, String content) {
        mView.showLoading();
        mSubscription.add(mRepository.postQuestion(title, content, new IObjectCallBack<String>() {
            @Override
            public void onSuccess(@NonNull String s) {
                mView.postSuccess();
            }

            @Override
            public void onError(String message) {
                mView.postFail();
            }

            @Override
            public void onFinish() {
                mView.hideLoading();
            }
        }));
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        mSubscription.clear();
    }
}
