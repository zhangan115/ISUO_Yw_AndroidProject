package com.sito.customer.view.home.work.inject;

import android.os.Bundle;

import com.sito.customer.R;
import com.sito.customer.app.CustomerApp;
import com.sito.customer.common.ConstantStr;
import com.sito.customer.view.BaseActivity;
import com.sito.library.utils.ActivityUtils;

import javax.inject.Inject;

/**
 * 注油管理
 * Created by zhangan on 2017/9/21.
 */

public class InjectActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutAndToolbar(R.layout.activity_container_toolbar, "注油管理");
        InjectFragment injectFragment = (InjectFragment) getFragmentManager().findFragmentById(R.id.frame_container);
        long roomId = getIntent().getLongExtra(ConstantStr.KEY_BUNDLE_LONG, -1);
        if (injectFragment == null) {
            injectFragment = InjectFragment.newInstance(roomId);
            ActivityUtils.addFragmentToActivity(getFragmentManager(), injectFragment, R.id.frame_container);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        CustomerApp.getInstance().hideSoftKeyBoard(this);
    }
}
