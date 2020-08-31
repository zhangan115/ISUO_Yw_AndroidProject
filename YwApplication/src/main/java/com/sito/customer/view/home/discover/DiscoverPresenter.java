package com.sito.customer.view.home.discover;

import android.support.annotation.NonNull;

import com.sito.customer.mode.IListCallBack;
import com.sito.customer.mode.IObjectCallBack;
import com.sito.customer.mode.bean.discover.StandBean;
import com.sito.customer.mode.bean.discover.ValueAddedBean;
import com.sito.customer.mode.customer.CustomerRepository;

import java.util.List;

import javax.inject.Inject;

import rx.subscriptions.CompositeSubscription;

/**
 * 发现
 * Created by Administrator on 2017/6/28.
 */
public class DiscoverPresenter implements DiscoverContract.Presenter {
    private CustomerRepository mRepository;
    private DiscoverContract.View mView;
    @NonNull
    private CompositeSubscription mSubscription;

    @Inject
    public DiscoverPresenter(CustomerRepository repository, DiscoverContract.View view) {
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
    public void getStandInfo() {
        mSubscription.add(mRepository.getStandInfo(new IObjectCallBack<StandBean>() {
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
