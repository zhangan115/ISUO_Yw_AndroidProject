package com.sito.evpro.inspection.view.home;

import android.support.annotation.NonNull;

import com.sito.evpro.inspection.mode.IListCallBack;
import com.sito.evpro.inspection.mode.bean.NewVersion;
import com.sito.evpro.inspection.mode.bean.equip.EquipBean;
import com.sito.evpro.inspection.mode.bean.option.OptionBean;
import com.sito.evpro.inspection.mode.inspection.InspectionDataSource;
import com.sito.evpro.inspection.mode.inspection.InspectionRepository;

import java.util.List;

import javax.inject.Inject;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by Yangzb on 2017/7/5 18:07
 * E-mail：yangzongbin@si-top.com
 */
final class HomePresenter implements HomeContract.Presenter {
    private InspectionRepository mRepository;
    private HomeContract.View mView;
    @NonNull
    private CompositeSubscription mSubscription;

    @Inject
    HomePresenter(InspectionRepository repository, HomeContract.View view) {
        this.mRepository = repository;
        this.mView = view;
        mSubscription = new CompositeSubscription();
    }

    @Inject
    void setUpListeners() {
        mView.setPresenter(this);
    }

    @Override
    public void getOptionInfo() {
        mSubscription.add(mRepository.getOptionInfo(new IListCallBack<OptionBean>() {
            @Override
            public void onSuccess(@NonNull List<OptionBean> list) {
                mView.saveOptionData(list);
            }

            @Override
            public void onError(String message) {
                mView.saveFail();
            }

            @Override
            public void onFinish() {

            }
        }));
    }

    @Override
    public void getEquipInfo() {
        mSubscription.add(mRepository.getEquipInfo(new IListCallBack<EquipBean>() {
            @Override
            public void onSuccess(@NonNull List<EquipBean> list) {
                mView.saveEquipData(list);
            }

            @Override
            public void onError(String message) {
                mView.saveFail();
            }

            @Override
            public void onFinish() {

            }
        }));
    }

    @Override
    public void exitApp() {
        mSubscription.add(mRepository.exitApp());
    }


    @Override
    public void subscribe() {
        mSubscription.add(mRepository.getNewVersion(new InspectionDataSource.NewVersionCallBack() {
            @Override
            public void newVersion(NewVersion result) {
                mView.showNewVersion(result);
            }

            @Override
            public void noNewVersion() {

            }
        }));
    }

    @Override
    public void unSubscribe() {
        mSubscription.clear();
    }
}