package com.sito.evpro.inspection.view.home;

import com.sito.evpro.inspection.mode.bean.NewVersion;
import com.sito.evpro.inspection.mode.bean.equip.EquipBean;
import com.sito.evpro.inspection.mode.bean.option.OptionBean;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

import java.util.List;

/**
 * Created by Yangzb on 2017/7/5 17:59
 * E-mail：yangzongbin@si-top.com
 */
interface HomeContract {

    interface Presenter extends BasePresenter {
        void getOptionInfo();

        void getEquipInfo();

        void exitApp();
    }

    interface View extends BaseView<Presenter> {
        void saveEquipData(List<EquipBean> list);

        void saveOptionData(List<OptionBean> list);

        void showLoading();

        void hideLoading();

        void saveSuccess();

        void saveFail();

        void showNewVersion(NewVersion version);



    }

}