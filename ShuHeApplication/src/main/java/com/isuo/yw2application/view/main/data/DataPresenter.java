package com.isuo.yw2application.view.main.data;

import android.support.annotation.NonNull;

import com.isuo.yw2application.mode.IObjectCallBack;
import com.isuo.yw2application.mode.bean.discover.ValueAddedBean;
import com.isuo.yw2application.mode.customer.CustomerRepository;

import javax.inject.Inject;

import rx.subscriptions.CompositeSubscription;

/**
 * 发现
 * Created by Administrator on 2017/6/28.
 */
public class DataPresenter implements DataContract.Presenter {
    private CustomerRepository mRepository;
    private DataContract.View mView;
    @NonNull
    private CompositeSubscription mSubscription;

    @Inject
    public DataPresenter(CustomerRepository repository, DataContract.View view) {
        this.mRepository = repository;
        this.mView = view;
        mSubscription = new CompositeSubscription();
        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        mSubscription.clear();
    }

    @Override
    public void getValueAdded() {
        mSubscription.add(mRepository.getValueAdded(new IObjectCallBack<ValueAddedBean>() {

            @Override
            public void onSuccess(@NonNull ValueAddedBean valueAddedBean) {
                mView.showValueAddedData(valueAddedBean.getList());
            }

            @Override
            public void onError(String message) {

            }

            @Override
            public void onFinish() {
                mView.noValueAdded();
            }
        }));
    }
}
