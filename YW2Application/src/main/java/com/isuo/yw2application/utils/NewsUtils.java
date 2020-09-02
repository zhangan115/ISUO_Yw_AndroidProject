package com.isuo.yw2application.utils;

import android.text.TextUtils;

import com.isuo.yw2application.R;
import com.isuo.yw2application.app.Yw2Application;
import com.isuo.yw2application.mode.bean.news.MessageContent;
import com.isuo.yw2application.mode.bean.news.NewsBean;
import com.sito.library.utils.DataUtil;

/**
 * 消息类型
 * Created by zhangan on 2017/10/16.
 */

public class NewsUtils {

    public static NewsBean getNewsBean(MessageContent message) {
        NewsBean newsBean = new NewsBean();
        newsBean.setContentInfo(message.getAppContent());
        newsBean.setMessageTime(message.getMessageTime());
        newsBean.setTip(message.getTip());
        newsBean.setMessageType(message.getMessageType());
        newsBean.setSmallType(message.getContentInfo().getSmallType());
        getNewsContent(newsBean, message);
        return newsBean;
    }

    public static String title(NewsBean newsBean, int type) {
        String title;
        switch (newsBean.getSmallType()) {
            case 204:
            case 207:
                if (newsBean.isMe() && type == 0) {
                    title = "任务指派";
                } else {
                    title = newsBean.getTip();
                }
                break;
            case 301:
                title = newsBean.getTitle();
                break;
            default:
                title = newsBean.getTip();
        }
        return title;
    }

    public static int getNewsNotifyDraw(NewsBean newsBean, int type) {
//        switch (newsBean.getSmallType()) {
//            case 101:
//                return R.drawable.message_report_icon;
//            case 102:
//                return R.drawable.message_close_icon;
//            case 103:
//                return R.drawable.message_circulation_icon;
//            case 104:
//                return R.drawable.message_assign_icon;
//            case 201:
//                return R.drawable.message_receive_icon;
//            case 202:
//                return R.drawable.message_start_icon;
//            case 203:
//                return R.drawable.message_complete_icon;
//            case 204:
//                if (newsBean.isMe() && type == 0) {
//                    return R.drawable.message_assign_icon;
//                }
//                return R.drawable.message_report_icon;
//            case 205:
//                return R.drawable.message_start_icon;
//            case 206:
//                return R.drawable.message_complete_icon;
//            case 207:
//                if (newsBean.isMe() && type == 0) {
//                    return R.drawable.message_assign_icon;
//                }
//                return R.drawable.message_report_icon;
//            case 208:
//                return R.drawable.message_start_icon;
//            case 209:
//                return R.drawable.message_complete_icon;
//            case 301:
//                return R.drawable.message_enterprise_icon;
//            case 401:
//                return R.drawable.message_ambulance_icon;
//            case 402:
//                return R.drawable.message_fire_icon;
//            case 501:
//            case 502:
//            case 503:
//            case 601:
//                return R.drawable.message_remind_icon;
//        }
//        return R.drawable.message_remind_icon;
    return 0;
    }

    public static int getNewsIntent(NewsBean newsBean) {
        if (newsBean.isWork()) {
            return 0;
        }
        if (newsBean.isAlarm()) {
            return 1;
        }
        if (newsBean.isEnterprise()) {
            return 2;
        }
        if (newsBean.isMe()) {
            return 3;
        }
        return 0;
    }

    private static void getNewsContent(NewsBean newsBean, MessageContent message) {
        ContentBean contentBean = message.getContentInfo().getContent();
        switch (message.getContentInfo().getSmallType()) {
            case 101:
                newsBean.setAlarm(true);
                newsBean.setTaskId(contentBean.getFaultId());
                StringBuilder sb101 = new StringBuilder();
                sb101.append(contentBean.getUserRealName());
                sb101.append("上报一条故障");
                sb101.append(contentBean.getEquipmentName());
                if (!TextUtils.isEmpty(contentBean.getEquipmentSn())) {
                    sb101.append("(").append(contentBean.getEquipmentSn()).append(")");
                }
                sb101.append(contentBean.getFaultType());
                sb101.append("至");
                sb101.append(contentBean.getUserRealNames());
                newsBean.setNewsContent(sb101.toString());
                newsBean.setNotifyId(1);
                newsBean.setNotifyContent("你有新的消息(工作动态,故障消息),请注意查看");
                if (!TextUtils.isEmpty(message.getContentInfo().getUserIds())) {
                    String[] userIds = message.getContentInfo().getUserIds().split(",");
                    for (String userId : userIds) {
                        if (userId.equals(String.valueOf(Yw2Application.getInstance().getCurrentUser().getUserId()))) {
                            newsBean.setMe(true);
                            StringBuilder sb = new StringBuilder();
                            sb.append(contentBean.getUserRealName())
                                    .append("指派给你:")
                                    .append(contentBean.getEquipmentName());
                            if (!TextUtils.isEmpty(contentBean.getEquipmentSn())) {
                                sb.append("(").append(contentBean.getEquipmentSn()).append(")");
                            }
                            sb.append(contentBean.getFaultType());
                            sb.append(",请及时处理");
                            newsBean.setMeContent(sb.toString());
                            newsBean.setNotifyId(-1);
                            StringBuilder sbNotify = new StringBuilder();
                            sbNotify.append("故障流转：").append(contentBean.getEquipmentName());
                            if (!TextUtils.isEmpty(contentBean.getEquipmentSn())) {
                                sbNotify.append("(").append(contentBean.getEquipmentSn()).append(")");
                            }
                            sbNotify.append(contentBean.getFaultType());
                            sbNotify.append("待你处理");
                            newsBean.setNotifyContent(sbNotify.toString());
                            break;
                        }
                    }
                }
                break;
            case 102:
                newsBean.setAlarm(true);
                newsBean.setTaskId(contentBean.getFaultId());
                StringBuilder sb102 = new StringBuilder();
                sb102.append(contentBean.getEquipmentName());
                sb102.append(contentBean.getFaultType());
                if (!TextUtils.isEmpty(contentBean.getEquipmentSn())) {
                    sb102.append("(").append(contentBean.getEquipmentSn()).append(")");
                }
                sb102.append("已关闭");
                newsBean.setNewsContent(sb102.toString());
                newsBean.setNotifyId(1);
                newsBean.setNotifyContent("你有新的消息(工作动态,故障消息),请注意查看");
                break;
            case 103:
                newsBean.setAlarm(true);
                newsBean.setTaskId(contentBean.getFaultId());
                StringBuilder sb103 = new StringBuilder();
                sb103.append(contentBean.getUserRealName());
                sb103.append("指派给");
                if (!TextUtils.isEmpty(contentBean.getUserRealNames())) {
                    sb103.append(contentBean.getUserRealNames());
                }
                sb103.append(":");
                sb103.append(contentBean.getEquipmentName());
                if (!TextUtils.isEmpty(contentBean.getEquipmentSn())) {
                    sb103.append("(").append(contentBean.getEquipmentSn()).append(")");
                }
                sb103.append(contentBean.getFaultType());
                newsBean.setNewsContent(sb103.toString());
                newsBean.setNotifyId(1);
                newsBean.setNotifyContent("你有新的消息(工作动态,故障消息),请注意查看");
                if (!TextUtils.isEmpty(message.getContentInfo().getUserIds())) {
                    String[] userIds = message.getContentInfo().getUserIds().split(",");
                    for (String userId : userIds) {
                        if (userId.equals(String.valueOf(Yw2Application.getInstance().getCurrentUser().getUserId()))) {
                            newsBean.setMe(true);
                            StringBuilder sb = new StringBuilder();
                            sb.append(contentBean.getUserRealName());
                            sb.append("指派给你:");
                            sb.append(contentBean.getEquipmentName());
                            if (!TextUtils.isEmpty(contentBean.getEquipmentSn())) {
                                sb.append("(").append(contentBean.getEquipmentSn()).append(")");
                            }
                            sb.append(contentBean.getFaultType());
                            sb.append(",请及时处理");
                            newsBean.setMeContent(sb.toString());
                            newsBean.setNotifyId(-1);
                            StringBuilder sbNotify = new StringBuilder();
                            sbNotify.append("故障流转：").append(contentBean.getEquipmentName());
                            if (!TextUtils.isEmpty(contentBean.getEquipmentSn())) {
                                sbNotify.append("(").append(contentBean.getEquipmentSn()).append(")");
                            }
                            sbNotify.append(contentBean.getFaultType());
                            sbNotify.append("待你处理");
                            newsBean.setNotifyContent(sbNotify.toString());
                            break;
                        }
                    }
                }
                break;
            case 104:
                newsBean.setAlarm(true);
                newsBean.setTaskId(contentBean.getFaultId());
                StringBuilder sb104 = new StringBuilder();
                sb104.append(contentBean.getUserRealName());
                sb104.append("指派检修工作给");
                sb104.append(contentBean.getUserRealNames()).append(":");
                sb104.append(contentBean.getEquipmentName());
                if (!TextUtils.isEmpty(contentBean.getEquipmentSn())) {
                    sb104.append("(").append(contentBean.getEquipmentSn()).append(")");
                }
                sb104.append(contentBean.getFaultType());
                sb104.append(",将于");
                sb104.append(DataUtil.timeFormat(contentBean.getRepairTime(), "MM-dd-HH:mm"));
                sb104.append("执行");
                newsBean.setNewsContent(sb104.toString());
                newsBean.setNotifyId(1);
                newsBean.setNotifyContent("你有新的消息(工作动态,故障消息),请注意查看");
                if (!TextUtils.isEmpty(message.getContentInfo().getUserIds())) {
                    String[] userIds = message.getContentInfo().getUserIds().split(",");
                    for (String userId : userIds) {
                        if (userId.equals(String.valueOf(Yw2Application.getInstance().getCurrentUser().getUserId()))) {
                            newsBean.setMe(true);
                            StringBuilder sb = new StringBuilder();
                            sb.append(contentBean.getUserRealName());
                            sb.append("指派检修工作给你:");
                            sb.append(contentBean.getEquipmentName());
                            if (!TextUtils.isEmpty(contentBean.getEquipmentSn())) {
                                sb.append("(").append(contentBean.getEquipmentSn()).append(")");
                            }
                            sb.append(contentBean.getFaultType())
                                    .append(",请在")
                                    .append(DataUtil.timeFormat(contentBean.getRepairTime(), "MM月dd日 HH:mm"))
                                    .append("执行");
                            newsBean.setMeContent(sb.toString());
                            newsBean.setNotifyId(-1);
                            StringBuilder sbNotify = new StringBuilder();
                            sbNotify.append("故障流转：").append(contentBean.getEquipmentName());
                            if (!TextUtils.isEmpty(contentBean.getEquipmentSn())) {
                                sbNotify.append("(").append(contentBean.getEquipmentSn()).append(")");
                            }
                            sbNotify.append(contentBean.getFaultType());
                            sbNotify.append("待你处理");
                            newsBean.setNotifyContent(sbNotify.toString());
                            break;
                        }
                    }
                }
                break;
            case 201:
                newsBean.setWork(true);
                newsBean.setTaskId(contentBean.getTaskId());
                StringBuilder sb201 = new StringBuilder();
                sb201.append(contentBean.getUserRealName());
                sb201.append("领取了");
                if (!TextUtils.isEmpty(contentBean.getTaskName())) {
                    sb201.append(contentBean.getTaskName());
                }
                newsBean.setNewsContent(sb201.toString());
                newsBean.setNotifyId(1);
                newsBean.setNotifyContent("你有新的消息(工作动态,故障消息),请注意查看");
                break;
            case 202:
                newsBean.setWork(true);
                newsBean.setTaskId(contentBean.getTaskId());
                StringBuilder sb202 = new StringBuilder();
                sb202.append(contentBean.getUserRealName());
                sb202.append("开始了");
                if (!TextUtils.isEmpty(contentBean.getTaskName())) {
                    sb202.append("''").append(contentBean.getTaskName()).append("''");
                }
                newsBean.setTaskId(contentBean.getTaskId());
                newsBean.setNewsContent(sb202.toString());
                newsBean.setNotifyId(1);
                newsBean.setNotifyContent("你有新的消息(工作动态,故障消息),请注意查看");
                break;
            case 203:
                newsBean.setWork(true);
                newsBean.setTaskId(contentBean.getTaskId());
                StringBuilder sb203 = new StringBuilder();
                sb203.append(contentBean.getUserRealName());
                sb203.append("完成了");
                if (!TextUtils.isEmpty(contentBean.getTaskName())) {
                    sb203.append("''").append(contentBean.getTaskName()).append("''");
                }
                sb203.append("巡检");
                newsBean.setNewsContent(sb203.toString());
                newsBean.setNotifyId(1);
                newsBean.setNotifyContent("你有新的消息(工作动态,故障消息),请注意查看");
                break;
            case 204:
                newsBean.setTaskId(contentBean.getRepairId());
                newsBean.setWork(true);
                String sb204 = contentBean.getUserRealName() +
                        "指派" +
                        "检修工作" + "''" +
                        contentBean.getRepairName() +
                        "''" +
                        "给" + contentBean.getUserRealNames() +
                        ",要求" + DataUtil.timeFormat(contentBean.getStartTime(), "MM月dd日 HH:mm") +
                        "执行";
                newsBean.setNewsContent(sb204);
                newsBean.setNotifyId(1);
                newsBean.setNotifyContent("你有新的消息(工作动态,故障消息),请注意查看");
                if (!TextUtils.isEmpty(message.getContentInfo().getUserIds())) {
                    String[] userIds = message.getContentInfo().getUserIds().split(",");
                    for (String userId : userIds) {
                        if (userId.equals(String.valueOf(Yw2Application.getInstance().getCurrentUser().getUserId()))) {
                            newsBean.setMe(true);
                            String sb = contentBean.getUserRealName() +
                                    "指派检修工作" + "''" +
                                    contentBean.getRepairName() +
                                    "''" +
                                    "给你,请于" +
                                    DataUtil.timeFormat(contentBean.getStartTime(), "MM月dd日 HH:mm") +
                                    "执行";
                            newsBean.setMeContent(sb);
                            newsBean.setNotifyId(-1);
                            newsBean.setNotifyContent("任务指派：检修工作''"
                                    + contentBean.getRepairName()
                                    + "''待你处理");
                            break;
                        }
                    }
                }
                break;
            case 205:
                newsBean.setWork(true);
                newsBean.setTaskId(contentBean.getRepairId());
                StringBuilder sb205 = new StringBuilder();
                sb205.append(contentBean.getUserRealName());
                sb205.append("开始了");
                if (!TextUtils.isEmpty(contentBean.getRepairName())) {
                    sb205.append("''").append(contentBean.getRepairName()).append("''");
                }
                sb205.append("检修工作");
                newsBean.setNewsContent(sb205.toString());
                newsBean.setNotifyId(1);
                newsBean.setNotifyContent("你有新的消息(工作动态,故障消息),请注意查看");
                break;
            case 206:
                newsBean.setWork(true);
                newsBean.setTaskId(contentBean.getRepairId());
                StringBuilder sb206 = new StringBuilder();
                sb206.append(contentBean.getUserRealName());
                sb206.append("完成了");
                if (!TextUtils.isEmpty(contentBean.getRepairName())) {
                    sb206.append("''" + contentBean.getRepairName() + "''");
                }
                sb206.append("检修工作");
                newsBean.setNewsContent(sb206.toString());
                newsBean.setNotifyId(1);
                newsBean.setNotifyContent("你有新的消息(工作动态,故障消息),请注意查看");
                break;
            case 207:
                newsBean.setTaskId(contentBean.getWorkId());
                newsBean.setWork(true);
                String sb207 = contentBean.getUserRealName() +
                        "发布了" +
                        "专项工作(" +
                        contentBean.getWorkType() +
                        ")给" + contentBean.getUserRealNames() +
                        ",要求" + DataUtil.timeFormat(contentBean.getStartTime(), "MM月dd日 HH:mm") +
                        "执行";
                newsBean.setNewsContent(sb207);
                newsBean.setNotifyId(1);
                newsBean.setNotifyContent("你有新的消息(工作动态,故障消息),请注意查看");
                if (!TextUtils.isEmpty(message.getContentInfo().getUserIds())) {
                    String[] userIds = message.getContentInfo().getUserIds().split(",");
                    for (String userId : userIds) {
                        if (userId.equals(String.valueOf(Yw2Application.getInstance().getCurrentUser().getUserId()))) {
                            newsBean.setMe(true);
                            String sb = contentBean.getUserRealName() +
                                    "指派''" +
                                    contentBean.getWorkType() +
                                    "专项工作''" +
                                    "给你,请于" +
                                    DataUtil.timeFormat(contentBean.getStartTime(), "MM月dd日 HH:mm") +
                                    "执行";
                            newsBean.setMeContent(sb);
                            newsBean.setNotifyId(-1);
                            newsBean.setNotifyContent("任务指派：''"
                                    + contentBean.getWorkType() + "专项任务"
                                    + "''待你处理");
                            break;
                        }
                    }
                }
                break;
            case 208:
                newsBean.setWork(true);
                newsBean.setTaskId(contentBean.getWorkId());
                String sb208 = contentBean.getUserRealName() +
                        "开始执行''" +
                        contentBean.getWorkType() +
                        "专项工作''";
                newsBean.setNewsContent(sb208);
                newsBean.setNotifyId(1);
                newsBean.setNotifyContent("你有新的消息(工作动态,故障消息),请注意查看");
                break;
            case 209:
                newsBean.setWork(true);
                newsBean.setTaskId(contentBean.getWorkId());
                String sb209 = contentBean.getUserRealName() +
                        "完成了''" +
                        contentBean.getWorkType() +
                        "专项工作''";
                newsBean.setNewsContent(sb209);
                newsBean.setNotifyId(1);
                newsBean.setNotifyContent("你有新的消息(工作动态,故障消息),请注意查看");
                break;
            case 301:
                newsBean.setEnterprise(true);
                newsBean.setNewsContent(contentBean.getContent());
                newsBean.setTitle(contentBean.getTitle());
                newsBean.setNotifyContent("企业通知：" + contentBean.getTitle());
                break;
            case 401:
                newsBean.setIsWork(true);
                StringBuilder sb401 = new StringBuilder();
                sb401.append("在").append(contentBean.getPlace()).append("有人员需要救护");
                newsBean.setMeContent(sb401.toString());
                newsBean.setNotifyContent("救护通知：" + sb401.toString());
                break;
            case 402:
                newsBean.setIsWork(true);
                StringBuilder sb402 = new StringBuilder();
                sb402.append("在").append(contentBean.getPlace()).append("出现火情");
                newsBean.setMeContent(sb402.toString());
                newsBean.setNotifyContent("火警通知：" + sb402.toString());
                break;
            case 501:
                newsBean.setMe(true);
                StringBuilder sb501 = new StringBuilder();
                newsBean.setTaskId(contentBean.getTaskId());
                sb501.append("在").append(DataUtil.timeFormat(contentBean.getStartTime(), "HH:mm"))
                        .append("有你执行的巡检任务")
                        .append("''")
                        .append(contentBean.getTaskName())
                        .append("''");
                newsBean.setMeContent(sb501.toString());
                newsBean.setNotifyContent("工作提醒：" + contentBean.getTaskName() + "("
                        + DataUtil.timeFormat(contentBean.getStartTime(), "HH:mm") + ")");
                break;
            case 502:
                newsBean.setMe(true);
                newsBean.setTaskId(contentBean.getRepairId());
                StringBuilder sb502 = new StringBuilder();
                sb502.append("在").append(DataUtil.timeFormat(contentBean.getStartTime(), "HH:mm"))
                        .append("有你执行的检修工作")
                        .append("''")
                        .append(contentBean.getRepairName())
                        .append("''");
                newsBean.setMeContent(sb502.toString());
                newsBean.setNotifyContent("工作提醒：" + contentBean.getRepairName() + "("
                        + DataUtil.timeFormat(contentBean.getStartTime(), "HH:mm") + ")");
                break;
            case 503:
                newsBean.setMe(true);
                newsBean.setTaskId(contentBean.getWorkId());
                StringBuilder sb503 = new StringBuilder();
                sb503.append("在").append(DataUtil.timeFormat(contentBean.getStartTime(), "HH:mm"))
                        .append("有你执行的专项工作")
                        .append("''")
                        .append(contentBean.getWorkType())
                        .append("''");
                newsBean.setMeContent(sb503.toString());
                newsBean.setNotifyContent("工作提醒：" + contentBean.getWorkType() + "("
                        + DataUtil.timeFormat(contentBean.getStartTime(), "HH:mm") + ")");
                break;
            case 601:
                newsBean.setMe(true);
                newsBean.setTaskId(contentBean.getToolLogId());
                StringBuilder sb601 = new StringBuilder();
                sb601.append("是否借用").append(contentBean.getToolName()).append("?");
                newsBean.setMeContent(sb601.toString());
                newsBean.setNotifyContent("工具借用确认");
                break;
            case 602:
                newsBean.setMe(true);
                newsBean.setTaskId(contentBean.getToolLogId());
                StringBuilder sb602 = new StringBuilder();
                sb602.append("请及时归还").append(contentBean.getToolName());
                newsBean.setMeContent(sb602.toString());
                newsBean.setNotifyContent("工具催还");
                break;
            case 701:
                newsBean.setMe(true);
                newsBean.setTaskId(contentBean.getToolLogId());
                StringBuilder sb701 = new StringBuilder();
                sb701.append("请及时归还").append(contentBean.getToolName());
                newsBean.setMeContent(sb701.toString());
                newsBean.setNotifyContent("注油提醒");
                break;
        }
    }
}
