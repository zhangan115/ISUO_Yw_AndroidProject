package com.sito.customer.view.contact;

import android.os.Bundle;

import com.sito.customer.R;
import com.sito.customer.view.BaseActivity;
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
        ContactFragment fragment = null;
        fragment = (ContactFragment) getFragmentManager().findFragmentById(R.id.frame_container);
        if (fragment == null) {
             fragment= ContactFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getFragmentManager(), fragment, R.id.frame_container);
        }
    }
}
