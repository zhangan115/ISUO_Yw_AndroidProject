package com.sito.evpro.inspection.view.repair.increment.work;

import android.os.Bundle;

import com.sito.evpro.inspection.R;
import com.sito.evpro.inspection.app.InspectionApp;
import com.sito.evpro.inspection.common.ConstantStr;
import com.sito.evpro.inspection.mode.bean.increment.IncrementBean;
import com.sito.evpro.inspection.view.BaseActivity;
import com.sito.library.utils.ActivityUtils;

import javax.inject.Inject;

/**
 * 专项工作详情
 * Created by zhangan on 2017/10/10.
 */

public class IncrementWorkActivity extends BaseActivity {

    @Inject
    IncrementWorkPresenter presenter;

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
        }
        DaggerIncrementWorkComponent.builder().commitRepositoryComponent(InspectionApp.getInstance().getCommitRepositoryComponent())
                .incrementWorkModule(new IncrementWorkModule(fragment)).build()
                .inject(this);
        ActivityUtils.addFragmentToActivity(getFragmentManager(), fragment, R.id.frame_container);
    }
}
