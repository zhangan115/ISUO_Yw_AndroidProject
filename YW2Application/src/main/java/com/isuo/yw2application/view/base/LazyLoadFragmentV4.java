package com.isuo.yw2application.view.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.isuo.yw2application.view.base.BaseFragmentV4;
import com.sito.library.base.BasePresenter;

/**
 * 懒加载fragment
 * Created by zhangan on 2018/3/15.
 */

public abstract class LazyLoadFragmentV4<T extends BasePresenter> extends BaseFragmentV4 {

    @Nullable
    protected T mPresenter;
    protected boolean isViewInitiated;
    protected boolean isDataLoaded;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isViewInitiated = true;
        prepareRequestData();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        prepareRequestData();
    }

    public abstract void requestData();

    public boolean prepareRequestData() {
        return prepareRequestData(false);
    }

    public boolean prepareRequestData(boolean forceUpdate) {
        if (getUserVisibleHint() && isViewInitiated && (!isDataLoaded || forceUpdate)) {
            requestData();
            isDataLoaded = true;
            return true;
        }
        return false;
    }

    @Override
    protected void onCancel() {
        if (mPresenter != null) {
            mPresenter.unSubscribe();
        }
    }
}
