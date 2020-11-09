package com.isuo.yw2application.view.main.work.inject;

import android.os.Bundle;

import com.isuo.yw2application.R;
import com.isuo.yw2application.app.Yw2Application;
import com.isuo.yw2application.common.ConstantStr;
import com.isuo.yw2application.view.base.BaseActivity;
import com.sito.library.utils.ActivityUtils;

/**
 * 润油管理
 * Created by zhangan on 2017/9/21.
 */

public class InjectActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutAndToolbar(R.layout.activity_container_toolbar, "润油管理");
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
        Yw2Application.getInstance().hideSoftKeyBoard(this);
    }
}
