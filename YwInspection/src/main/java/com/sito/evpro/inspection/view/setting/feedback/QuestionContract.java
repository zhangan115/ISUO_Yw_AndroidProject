package com.sito.evpro.inspection.view.setting.feedback;


import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

/**
 * Created by Administrator on 2017/6/14.
 */
interface QuestionContract {

    interface Presenter extends BasePresenter {
        void postQuestion(String title, String content);
    }

    interface View extends BaseView<Presenter> {

        void postSuccess();

        void postFail();

        void postFinish();

        void showLoading();

        void hideLoading();
    }

}