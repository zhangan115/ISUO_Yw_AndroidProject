package com.isuo.yw2application.view.main.work.safe_manager;

import android.support.annotation.NonNull;

import com.isuo.yw2application.mode.IObjectCallBack;
import com.isuo.yw2application.mode.bean.discover.StandBean;
import com.isuo.yw2application.mode.customer.CustomerRepository;

import rx.subscriptions.CompositeSubscription;

public class SafeManagerPresenter implements SafeManagerContract.Presenter {

    private CustomerRepository mRepository;
    private SafeManagerContract.View mView;
    @NonNull
    private CompositeSubscription mSubscription;

    public SafeManagerPresenter(CustomerRepository repository, SafeManagerContract.View view) {
        this.mRepository = repository;
        this.mView = view;
        mSubscription = new CompositeSubscription();
        mView.setPresenter(this);
    }


    @Override
    public void getSafeManagerList() {
        mSubscription.add(mRepository.getStandInfo(new IObjectCallBack<StandBean>() {
            @Override
            public void onSuccess(@NonNull StandBean standBean) {
                if (standBean.getList().isEmpty()){
                    mView.noData();
                }else{
                    mView.showData(standBean.getList());
                }

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
