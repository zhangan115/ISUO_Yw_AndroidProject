package com.isuo.yw2application.view.main.task.overhaul.detail;

import android.os.Bundle;

import com.isuo.yw2application.R;
import com.isuo.yw2application.app.Yw2Application;
import com.isuo.yw2application.common.ConstantStr;
import com.isuo.yw2application.view.base.NotifyActivity;

/**
 * Created by zhangan on 2017-07-03.
 */

public class OverhaulDetailActivity extends NotifyActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutAndToolbar(R.layout.activity_container_toolbar, "检修详情");
        long repairId = getIntent().getLongExtra(ConstantStr.KEY_BUNDLE_LONG, -1L);
        if (repairId == -1) {
            finish();
            return;
        }
        OverhaulDetailFragment fragment = (OverhaulDetailFragment) getFragmentManager().findFragmentById(R.id.frame_container);
        if (fragment == null) {
            fragment = OverhaulDetailFragment.newInstance(repairId);
        }
        new OverhaulDetailPresenter(Yw2Application.getInstance().getWorkRepositoryComponent().getRepository(), fragment);
        getFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).commit();
    }

}
