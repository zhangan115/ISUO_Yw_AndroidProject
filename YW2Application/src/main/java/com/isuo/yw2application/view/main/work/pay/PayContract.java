package com.isuo.yw2application.view.main.work.pay;

import com.isuo.yw2application.mode.bean.PayMenuBean;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

import org.json.JSONObject;

import java.util.List;

public interface PayContract {

    interface Presenter extends BasePresenter {

        void getPayList(JSONObject jsonObject);

        void getWeiXinPayInfo(JSONObject jsonObject);

        void getAlPayInfo(JSONObject jsonObject);

        void getPayInfo(JSONObject jsonObject);

        void paySuccess(JSONObject jsonObject);
    }

    interface View extends BaseView<Presenter> {

        void showPayList(List<PayMenuBean> list);

        void paySuccess();

        void payFail();

        void payAli(String payMessage);

        void payWeiXin(WeiXinPayBean weiXinBean);
    }
}
