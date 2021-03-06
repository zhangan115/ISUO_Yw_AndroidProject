package com.isuo.yw2application.view.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.isuo.yw2application.view.base.BaseFragment;
import com.sito.library.base.BasePresenter;

/**
 * mvp fragment
 * <p>
 * Created by zhangan on 2017-04-27.
 */

public class MvpFragment<T extends BasePresenter> extends BaseFragment {

    protected T mPresenter;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected void onCancel() {
        if (mPresenter != null) {
            mPresenter.unSubscribe();
        }
    }
}
