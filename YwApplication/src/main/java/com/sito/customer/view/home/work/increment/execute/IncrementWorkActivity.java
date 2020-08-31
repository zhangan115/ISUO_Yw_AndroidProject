package com.sito.customer.view.home.work.increment.execute;

import android.os.Bundle;

import com.sito.customer.R;
import com.sito.customer.common.ConstantStr;
import com.sito.customer.mode.bean.work.IncrementBean;
import com.sito.customer.view.BaseActivity;
import com.sito.library.utils.ActivityUtils;

/**
 * 专项工作详情
 * Created by zhangan on 2017/10/10.
 */

public class IncrementWorkActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IncrementBean incrementBean = getIntent().getParcelableExtra(ConstantStr.KEY_BUNDLE_OBJECT);
        if (incrementBean == null) {
            finish();
            return;
        }
        setLayoutAndToolbar(R.layout.activity_container_toolbar, "专项工作详情");
        IncrementWorkFragment fragment = (IncrementWorkFragment) getFragmentManager().findFragmentById(R.id.frame_container);
        if (fragment == null) {
            fragment = IncrementWorkFragment.newInstance(incrementBean);
            ActivityUtils.addFragmentToActivity(getFragmentManager(), fragment, R.id.frame_container);
        }
    }
}
