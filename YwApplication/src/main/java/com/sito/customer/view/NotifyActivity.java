package com.sito.customer.view;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.sito.customer.app.CustomerApp;
import com.sito.customer.common.ConstantStr;
import com.sito.customer.mode.bean.db.NewsBean;

/**
 * 通知类
 * Created by zhangan on 2017/9/14.
 */

public class NotifyActivity extends BaseActivity {

    @Nullable
    protected NewsBean mNewsBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        notifyHandle();
    }

    /**
     * 通知处理
     */
    protected void notifyHandle() {
        mNewsBean = getIntent().getParcelableExtra(ConstantStr.KEY_NEW_BEAN);
        if (mNewsBean != null) {
            mNewsBean.setHasRead(true);
            CustomerApp.getInstance().getDaoSession().getNewsBeanDao().updateInTx(mNewsBean);
        }
        int notifyId = getIntent().getIntExtra(ConstantStr.KEY_NEW_ID, -1);
        if (notifyId != -1) {
            CustomerApp.getInstance().cleanNotify(notifyId);
        }
    }
}
