package com.sito.evpro.inspection.mode.bean.fault;

import com.sito.evpro.inspection.mode.bean.User;
import com.sito.evpro.inspection.mode.bean.inspection.ExecutorUserList;

import java.util.List;

/**
 * Created by Yangzb on 2017/7/17 19:18
 * E-mail：yangzongbin@si-top.com
 */
public class CheckBean {

    private int flowingCount;
    private int uploadCount;
    private long planStartTime;
    private int closeCount;
    private int allFault;
    private int count;
    private int repairCount;
    private ReceiveUserBean receiveUser;
    private int taskState;
    private String taskName;
    private int pendingCount;
    private long planEndTime;
    private long startTime;
    private long endTime;
    private List<User> users;
    private List<String> rooms;
    private List<ExecutorUserList> executorUserList;
    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public int getFlowingCount() {
        return flowingCount;
    }

    public void setFlowingCount(int flowingCount) {
        this.flowingCount = flowingCount;
    }

    public int getUploadCount() {
        return uploadCount;
    }

    public void setUploadCount(int uploadCount) {
        this.uploadCount = uploadCount;
    }

    public long getPlanStartTime() {
        return planStartTime;
    }

    public void setPlanStartTime(long planStartTime) {
        this.planStartTime = planStartTime;
    }

    public int getCloseCount() {
        return closeCount;
    }

    public void setCloseCount(int closeCount) {
        this.closeCount = closeCount;
    }

    public int getAllFault() {
        return allFault;
    }

    public void setAllFault(int allFault) {
        this.allFault = allFault;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getRepairCount() {
        return repairCount;
    }

    public void setRepairCount(int repairCount) {
        this.repairCount = repairCount;
    }

    public ReceiveUserBean getReceiveUser() {
        return receiveUser;
    }

    public void setReceiveUser(ReceiveUserBean receiveUser) {
        this.receiveUser = receiveUser;
    }

    public int getTaskState() {
        return taskState;
    }

    public void setTaskState(int taskState) {
        this.taskState = taskState;
    }

    public int getPendingCount() {
        return pendingCount;
    }

    public void setPendingCount(int pendingCount) {
        this.pendingCount = pendingCount;
    }

    public long getPlanEndTime() {
        return planEndTime;
    }

    public void setPlanEndTime(long planEndTime) {
        this.planEndTime = planEndTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<String> getRooms() {
        return rooms;
    }

    public void setRooms(List<String> rooms) {
        this.rooms = rooms;
    }

    public List<ExecutorUserList> getExecutorUserList() {
        return executorUserList;
    }

    public void setExecutorUserList(List<ExecutorUserList> executorUserList) {
        this.executorUserList = executorUserList;
    }

    public static class ReceiveUserBean {
        /**
         * createTime : 1499824688000
         * deleteState : 0
         * joinTime : 1499824688000
         * realName : 何江辉
         * userId : 45
         * userName : hejianghui
         * userPhone : 123456
         * userTelephone : 运行一班-班员
         * userType : 2
         */

        private long createTime;
        private int deleteState;
        private long joinTime;
        private String realName;
        private int userId;
        private String userName;
        private String userPhone;
        private String userTelephone;
        private int userType;

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public int getDeleteState() {
            return deleteState;
        }

        public void setDeleteState(int deleteState) {
            this.deleteState = deleteState;
        }

        public long getJoinTime() {
            return joinTime;
        }

        public void setJoinTime(long joinTime) {
            this.joinTime = joinTime;
        }

        public String getRealName() {
            return realName;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserPhone() {
            return userPhone;
        }

        public void setUserPhone(String userPhone) {
            this.userPhone = userPhone;
        }

        public String getUserTelephone() {
            return userTelephone;
        }

        public void setUserTelephone(String userTelephone) {
            this.userTelephone = userTelephone;
        }

        public int getUserType() {
            return userType;
        }

        public void setUserType(int userType) {
            this.userType = userType;
        }
    }

    public static class ExecuteUserBean {
        /**
         * createTime : 1499824688000
         * deleteState : 0
         * joinTime : 1499824688000
         * realName : 何江辉
         * userId : 45
         * userName : hejianghui
         * userPhone : 123456
         * userTelephone : 运行一班-班员
         * userType : 2
         */

        private long createTime;
        private int deleteState;
        private long joinTime;
        private String realName;
        private int userId;
        private String userName;
        private String userPhone;
        private String userTelephone;
        private int userType;

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public int getDeleteState() {
            return deleteState;
        }

        public void setDeleteState(int deleteState) {
            this.deleteState = deleteState;
        }

        public long getJoinTime() {
            return joinTime;
        }

        public void setJoinTime(long joinTime) {
            this.joinTime = joinTime;
        }

        public String getRealName() {
            return realName;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserPhone() {
            return userPhone;
        }

        public void setUserPhone(String userPhone) {
            this.userPhone = userPhone;
        }

        public String getUserTelephone() {
            return userTelephone;
        }

        public void setUserTelephone(String userTelephone) {
            this.userTelephone = userTelephone;
        }

        public int getUserType() {
            return userType;
        }

        public void setUserType(int userType) {
            this.userType = userType;
        }
    }
}
