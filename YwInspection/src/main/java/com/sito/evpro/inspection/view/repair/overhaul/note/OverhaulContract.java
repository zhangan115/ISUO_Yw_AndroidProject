package com.sito.evpro.inspection.view.repair.overhaul.note;

import android.support.annotation.NonNull;

import com.sito.evpro.inspection.mode.bean.overhaul.OverhaulNoteBean;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

/**
 * 工作包
 * Created by zhangan on 2017/8/24.
 */

interface OverhaulContract {

    interface Presenter extends BasePresenter {

        void getRepairState(@NonNull String repairId, String jobId);

        void setRepairState(@NonNull String repairId, String jobId);

    }

    interface View extends BaseView<Presenter> {

        void showOverhaulWork();

        void showOverhaulNote(OverhaulNoteBean overhaulNoteBean);

        void noData();

        void showLoading();

        void hidLoading();

        void showLayout();
    }
}
