package com.isuo.yw2application.view.main.work.all;

import android.app.Activity;
import android.content.Intent;

import com.isuo.yw2application.mode.bean.work.WorkItem;
import com.isuo.yw2application.view.main.alarm.fault.FaultActivity;
import com.isuo.yw2application.view.main.data.fault_report.FaultReportActivity;
import com.isuo.yw2application.view.main.device.equipment.CreateEquipmentActivity;
import com.isuo.yw2application.view.main.generate.repair.GenerateRepairActivity;
import com.isuo.yw2application.view.main.task.increment.WorkIncrementActivity;
import com.isuo.yw2application.view.main.task.inspection.WorkInspectionActivity;
import com.isuo.yw2application.view.main.task.overhaul.WorkOverhaulActivity;
import com.isuo.yw2application.view.main.work.await.AwaitActivity;
import com.isuo.yw2application.view.main.work.enterprise_standard.EnterpriseStandardActivity;
import com.isuo.yw2application.view.main.work.inject.InjectActivity;
import com.isuo.yw2application.view.main.work.send_message.SendMessageActivity;
import com.isuo.yw2application.view.main.work.sos.SOSActivity;
import com.isuo.yw2application.view.main.work.tool.ToolsActivity;


/**
 * 根据work item id
 * Created by zhangan on 2018/3/27.
 */

public class WorkItemAllIntent {

    public static Intent startWorkItem(Activity activity, WorkItem workItem) {
        Intent intent = new Intent();
        switch (workItem.getId()) {
            case 1://专项工作
                intent.setClass(activity, WorkIncrementActivity.class);
                break;
            case 2://检修工作
                intent.setClass(activity, WorkOverhaulActivity.class);
                break;
            case 3://指派检修
                intent.setClass(activity, GenerateRepairActivity.class);
                break;
            case 4://发布通知
                intent.setClass(activity, SendMessageActivity.class);
                break;
            case 5://故障上报
                intent.setClass(activity, FaultActivity.class);
                break;
            case 6://台账录入
                intent.setClass(activity, CreateEquipmentActivity.class);
                break;
            case 7://企业规范
                intent.setClass(activity, EnterpriseStandardActivity.class);
                break;
            case 8: //代办事项
                intent.setClass(activity, AwaitActivity.class);
                break;
            case 9://日常巡检
                intent.setClass(activity, WorkInspectionActivity.class);
                break;
            case 20://润油管理
                intent.setClass(activity, InjectActivity.class);
                break;
            case 21://工具管理
                intent.setClass(activity, ToolsActivity.class);
                break;
        }
        return intent;
    }

}
