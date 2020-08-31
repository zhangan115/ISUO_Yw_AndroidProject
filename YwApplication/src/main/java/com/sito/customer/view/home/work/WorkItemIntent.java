package com.sito.customer.view.home.work;

import android.app.Activity;
import android.content.Intent;

import com.sito.customer.mode.bean.work.WorkItem;
import com.sito.customer.view.home.discover.generate.repair.GenerateRepairActivity;
import com.sito.customer.view.home.work.await.AwaitActivity;
import com.sito.customer.view.home.work.create.equipment.CreateEquipmentActivity;
import com.sito.customer.view.home.work.fault.FaultActivity;
import com.sito.customer.view.home.work.increment.WorkIncrementActivity;
import com.sito.customer.view.home.work.inject.InjectActivity;
import com.sito.customer.view.home.work.inspection.WorkInspectionActivity;
import com.sito.customer.view.home.work.overhaul.WorkOverhaulActivity;
import com.sito.customer.view.home.work.sendmessage.SendMessageActivity;
import com.sito.customer.view.home.work.sos.SOSActivity;
import com.sito.customer.view.home.work.tool.ToolsActivity;

/**
 * 根据work item id
 * Created by zhangan on 2018/3/27.
 */

public class WorkItemIntent {

    public static Intent startWorkItem(Activity activity, WorkItem workItem) {
        Intent intent = new Intent();
        switch (workItem.getId()) {
            case 1://代办事项
                intent.setClass(activity, AwaitActivity.class);
                break;
            case 2://故障上报
                intent.setClass(activity, FaultActivity.class);
                break;
            case 3://日常巡检
                intent.setClass(activity, WorkInspectionActivity.class);
                break;
            case 4://检修工作
                intent.setClass(activity, WorkOverhaulActivity.class);
                break;
            case 5://专项工作
                intent.setClass(activity, WorkIncrementActivity.class);
                break;
            case 6://注油管理
                intent.setClass(activity, InjectActivity.class);
                break;
            case 7://紧急电话
                intent.setClass(activity, SOSActivity.class);
                break;
            case 8: //指派检修
                intent.setClass(activity, GenerateRepairActivity.class);
                break;
            case 9://工具管理
                intent.setClass(activity, ToolsActivity.class);
                break;
            case 10://发布通知
                intent.setClass(activity, SendMessageActivity.class);
                break;
            case 11://台账录入
                intent.setClass(activity, CreateEquipmentActivity.class);
                break;
        }
        return intent;
    }

}
