package com.sito.evpro.inspection.view.create;

import android.os.Bundle;

import com.sito.evpro.inspection.R;
import com.sito.evpro.inspection.view.BaseActivity;

/**
 * 配电室列表
 * Created by zhangan on 2017/9/22.
 */

public class CreateRoomListActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutAndToolbar(R.layout.activity_container_toolbar, "区域列表");
        
    }
}
