package com.sito.evpro.inspection.view.contact;

import android.os.Bundle;


import com.sito.evpro.inspection.R;
import com.sito.evpro.inspection.app.InspectionApp;
import com.sito.evpro.inspection.view.BaseActivity;
import com.sito.library.utils.ActivityUtils;

/**
 * 联系人界面
 * Created by zhangan on 2017/9/5.
 */

public class ContactActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutAndToolbar(R.layout.activity_container_toolbar, "联系人");
        ContactFragment fragment = ContactFragment.newInstance();
        ActivityUtils.addFragmentToActivity(getFragmentManager(), fragment, R.id.frame_container);
        new ContactPresenter(InspectionApp.getInstance().getEmployeeRepositoryComponent().getRepository(), fragment);
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slideinleft, R.anim.slideoutright);
    }


}
