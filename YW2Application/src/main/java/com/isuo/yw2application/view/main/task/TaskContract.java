package com.isuo.yw2application.view.main.task;

import com.isuo.yw2application.mode.bean.work.WorkState;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

/**
 * 工作首页
 * Created by Administrator on 2017/6/28.
 */
interface TaskContract {

    interface Presenter extends BasePresenter {

        void getFinishWorkCount();

        void getWorkCount();
    }

    interface View extends BaseView<Presenter> {

        void showWorkCount(WorkState workState);

        void showFinishWorkCount(int count);
    }

}