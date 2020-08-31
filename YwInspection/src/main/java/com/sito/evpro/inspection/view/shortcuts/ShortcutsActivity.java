package com.sito.evpro.inspection.view.shortcuts;

import android.content.Intent;
import android.os.Bundle;

import com.sito.evpro.inspection.app.AppStatusConstant;
import com.sito.evpro.inspection.app.AppStatusManager;
import com.sito.evpro.inspection.app.InspectionApp;
import com.sito.evpro.inspection.view.BaseActivity;
import com.sito.evpro.inspection.view.fault.FaultActivity;
import com.sito.evpro.inspection.view.increment.IncrementActivity;
import com.sito.evpro.inspection.view.sos.SOSActivity;

/**
 * 快捷方式
 * Created by zhangan on 2017/10/24.
 */

public class ShortcutsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppStatusManager.getInstance().setAppStatus(AppStatusConstant.STATUS_NORMAL);//进入应用初始化设置成未登录状态
        super.onCreate(savedInstanceState);
        String action = getIntent().getAction();
        InspectionApp.getInstance().getOptionInfo();
        switch (action) {
            case SOSActivity.ACTION:
                startActivity(new Intent(this, SOSActivity.class));
                break;
            case FaultActivity.ACTION:
                startActivity(new Intent(this, FaultActivity.class));
                break;
            case IncrementActivity.ACTION:
                startActivity(new Intent(this, IncrementActivity.class));
                break;
        }
        finish();
    }
}
