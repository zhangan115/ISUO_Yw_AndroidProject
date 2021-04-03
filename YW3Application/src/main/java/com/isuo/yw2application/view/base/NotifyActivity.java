package com.isuo.yw2application.view.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.isuo.yw2application.app.Yw2Application;
import com.isuo.yw2application.common.ConstantStr;
import com.isuo.yw2application.mode.bean.db.NewsBean;

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
            Yw2Application.getInstance().getDaoSession().getNewsBeanDao().updateInTx(mNewsBean);
        }
        int notifyId = getIntent().getIntExtra(ConstantStr.KEY_NEW_ID, -1);
        if (notifyId != -1) {
//            Yw2Application.getInstance().cleanNotify(notifyId);
        }
    }
}
