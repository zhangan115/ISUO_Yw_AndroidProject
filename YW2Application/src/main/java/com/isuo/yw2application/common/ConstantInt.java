package com.isuo.yw2application.common;

/**
 * int 型常量
 * Created by zhangan on 2017-06-21.
 */

public interface ConstantInt {
    int PAGE_SIZE = 20;
    int MAX_PAGE_SIZE = 10000;
    int OPERATION_STATE_1 = 1;//领取
    int OPERATION_STATE_2 = 2;//执行
    int OPERATION_STATE_3 = 3;//完成

    int INCREMENTTYPE = 1;//增值任务类型
    int FAULTTYPE = 2; //故障类型
    int SOURCE = 3;//增值工作来源
    int REPAIRRESULT = 4;//维修结果
    int SEND_MESSAGE = 16;//发送企业通知，获取通知类型

    int TASK_STATE_1 = 1;//待领取
    int TASK_STATE_2 = 2;//已领取
    int TASK_STATE_3 = 3;//进行中
    int TASK_STATE_4 = 4;//已完成


    int INCREMENT = 1;//增值工作
    int FAULT = 2;//故障上报
    int CHECKPAIR = 3;//检修
    int INSPECTION = 4;//巡检

    int ROOM_STATE_1 = 1;//未开始
    int ROOM_STATE_2 = 2;//进行中
    int ROOM_STATE_3 = 3;//已完成


    int TYPE_REPAIR = 1;//大修记录
    int TYPE_EXPERIMENT = 2;//实验记录
    int TYPE_CHECK = 2;//带电检测记录

    int DATA_VALUE_TYPE_1 = 1;
    int DATA_VALUE_TYPE_2 = 2;//数字输入
    int DATA_VALUE_TYPE_3 = 3;//拍照
    int DATA_VALUE_TYPE_4 = 4;//文本输入

    int INCREMENT_WORK = 5;//增值工作

    int AUTO_UPLOAD_TIME = 30000;//自动上传时间
    int AUTO_REFRESH_UI = 1000;//自动刷新界面时间
    int AUTO_SAVE_DATA = 10000;//自动保存数据时间
}
