package com.isuo.yw2application.view.main.work.enterprise_standard;

import android.support.annotation.NonNull;

import com.isuo.yw2application.mode.IObjectCallBack;
import com.isuo.yw2application.mode.bean.discover.StandBean;
import com.isuo.yw2application.mode.customer.CustomerRepository;

import rx.subscriptions.CompositeSubscription;

public class EnterpriseStandardPresenter implements EnterpriseStandardContact.Presenter {

    private CustomerRepository mRepository;
    private EnterpriseStandardContact.View mView;
    @NonNull
    private CompositeSubscription mSubscription;

    EnterpriseStandardPresenter(CustomerRepository repository, EnterpriseStandardContact.View view) {
        this.mRepository = repository;
        this.mView = view;
        mSubscription = new CompositeSubscription();
        mView.setPresenter(this);
    }

    @Override
    public void getEnterpriseStandardList() {
        mSubscription.add(mRepository.getSecurityList(new IObjectCallBack<StandBean>() {
            @Override
            public void onSuccess(@NonNull StandBean standBean) {
                mView.showData(standBean.getList());
            }

            @Override
            public void onError(String message) {
                mView.noData();
            }

            @Override
            public void onFinish() {

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
