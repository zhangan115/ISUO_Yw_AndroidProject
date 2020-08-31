package com.sito.evpro.inspection.view.setting.feedback;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2017/6/14.
 */
@Module
class QuestionModule {

    private final QuestionContract.View mView;

    QuestionModule(QuestionContract.View view) {
        mView = view;
    }

    @Provides
    QuestionContract.View provideQuestionContractView() {
        return mView;
    }

}