package com.isuo.yw2application.view.main.work.all;

import android.app.Activity;
import android.content.Intent;

import com.isuo.yw2application.mode.bean.work.WorkItem;
import com.isuo.yw2application.view.main.alarm.fault.FaultActivity;
import com.isuo.yw2application.view.main.device.equipment.CreateEquipmentActivity;
import com.isuo.yw2application.view.main.generate.repair.GenerateRepairActivity;
import com.isuo.yw2application.view.main.task.increment.WorkIncrementActivity;
import com.isuo.yw2application.view.main.task.overhaul.WorkOverhaulActivity;


/**
 * 根据work item id
 * Created by zhangan on 2018/3/27.
 */

public class WorkItemIntent {

    public static Intent startWorkItem(Activity activity, WorkItem workItem) {
        Intent intent = new Intent();
        switch (workItem.getId()) {
            case 1://代办事项
//                intent.setClass(activity, AwaitActivity.class);
                break;
            case 2://故障上报
                intent.setClass(activity, FaultActivity.class);
                break;
            case 3://日常巡检
//                intent.setClass(activity, WorkInspectionActivity.class);
                break;
            case 4://检修工作
                intent.setClass(activity, WorkOverhaulActivity.class);
                break;
            case 5://专项工作
                intent.setClass(activity, WorkIncrementActivity.class);
                break;
            case 6://注油管理
//                intent.setClass(activity, InjectActivity.class);
                break;
            case 7://紧急电话
//                intent.setClass(activity, SOSActivity.class);
                break;
            case 8: //指派检修
                intent.setClass(activity, GenerateRepairActivity.class);
                break;
            case 9://工具管理
//                intent.setClass(activity, ToolsActivity.class);
                break;
            case 10://发布通知
//                intent.setClass(activity, SendMessageActivity.class);
                break;
            case 11://台账录入
                intent.setClass(activity, CreateEquipmentActivity.class);
                break;
        }
        return intent;
    }

}
