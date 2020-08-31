package com.sito.customer.mode.bean.news;

import java.util.List;

/**
 * Created by Yangzb on 2017/7/14 13:57
 * E-mail：yangzongbin@si-top.com
 * 今日工作汇总
 */
public class Aggregate {

    /**
     * repairList : [{"createTime":1499938649000,"endTime":1499947200000,"repairId":9,"repairName":"11","repairState":1,"startTime":1499943600000,"user":{"createTime":1499825977000,"deleteState":0,"joinTime":1499825977000,"realName":"赵刚","userId":86,"userName":"zhaogang","userPhone":"123","userType":2}},{"createTime":1499933957000,"endTime":1499940000000,"repairId":8,"repairName":"123","repairState":1,"startTime":1499936400000,"user":{"createTime":1499825977000,"deleteState":0,"joinTime":1499825977000,"realName":"赵刚","userId":86,"userName":"zhaogang","userPhone":"123","userType":2}}]
     * unreceive : 0
     * taskCount : 0
     * receive : 2
     * execution : 0
     * specialCount : 2
     * incrementCount : 0
     * finish : 0
     * specialList : [{"isManualCreated":1,"planEndTime":1500015000000,"planStartTime":1499117100000,"taskId":46,"taskName":"123","taskState":2},{"isManualCreated":1,"planEndTime":1501239300000,"planStartTime":1499049900000,"taskId":47,"taskName":"123","taskState":2}]
     * repairCount : 5
     * allCount : 2
     */

    private int unreceive;//未领取
    private int taskCount;//日常巡检
    private int receive;//已领取
    private int execution;//执行中
    private int specialCount;
    private int incrementCount;
    private int finish;
    private int repairCount;//检修
    private int allCount;//巡检工作
    private int allFault;//故障
    private List<RepairListBean> repairList;
    private List<SpecialListBean> specialList;

    public int getAllFault() {
        return allFault;
    }

    public void setAllFault(int allFault) {
        this.allFault = allFault;
    }

    public int getUnreceive() {
        return unreceive;
    }

    public void setUnreceive(int unreceive) {
        this.unreceive = unreceive;
    }

    public int getTaskCount() {
        return taskCount;
    }

    public void setTaskCount(int taskCount) {
        this.taskCount = taskCount;
    }

    public int getReceive() {
        return receive;
    }

    public void setReceive(int receive) {
        this.receive = receive;
    }

    public int getExecution() {
        return execution;
    }

    public void setExecution(int execution) {
        this.execution = execution;
    }

    public int getSpecialCount() {
        return specialCount;
    }

    public void setSpecialCount(int specialCount) {
        this.specialCount = specialCount;
    }

    public int getIncrementCount() {
        return incrementCount;
    }

    public void setIncrementCount(int incrementCount) {
        this.incrementCount = incrementCount;
    }

    public int getFinish() {
        return finish;
    }

    public void setFinish(int finish) {
        this.finish = finish;
    }

    public int getRepairCount() {
        return repairCount;
    }

    public void setRepairCount(int repairCount) {
        this.repairCount = repairCount;
    }

    public int getAllCount() {
        return allCount;
    }

    public void setAllCount(int allCount) {
        this.allCount = allCount;
    }

    public List<RepairListBean> getRepairList() {
        return repairList;
    }

    public void setRepairList(List<RepairListBean> repairList) {
        this.repairList = repairList;
    }

    public List<SpecialListBean> getSpecialList() {
        return specialList;
    }

    public void setSpecialList(List<SpecialListBean> specialList) {
        this.specialList = specialList;
    }

    public static class RepairListBean {
        /**
         * createTime : 1499938649000
         * endTime : 1499947200000
         * repairId : 9
         * repairName : 11
         * repairState : 1
         * startTime : 1499943600000
         * user : {"createTime":1499825977000,"deleteState":0,"joinTime":1499825977000,"realName":"赵刚","userId":86,"userName":"zhaogang","userPhone":"123","userType":2}
         */

        private long createTime;
        private long endTime;
        private int repairId;
        private String repairName;
        private int repairState;
        private long startTime;
        private UserBean user;

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public long getEndTime() {
            return endTime;
        }

        public void setEndTime(long endTime) {
            this.endTime = endTime;
        }

        public int getRepairId() {
            return repairId;
        }

        public void setRepairId(int repairId) {
            this.repairId = repairId;
        }

        public String getRepairName() {
            return repairName;
        }

        public void setRepairName(String repairName) {
            this.repairName = repairName;
        }

        public int getRepairState() {
            return repairState;
        }

        public void setRepairState(int repairState) {
            this.repairState = repairState;
        }

        public long getStartTime() {
            return startTime;
        }

        public void setStartTime(long startTime) {
            this.startTime = startTime;
        }

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public static class UserBean {
            /**
             * createTime : 1499825977000
             * deleteState : 0
             * joinTime : 1499825977000
             * realName : 赵刚
             * userId : 86
             * userName : zhaogang
             * userPhone : 123
             * userType : 2
             */

            private long createTime;
            private int deleteState;
            private long joinTime;
            private String realName;
            private int userId;
            private String userName;
            private String userPhone;
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

            public int getUserType() {
                return userType;
            }

            public void setUserType(int userType) {
                this.userType = userType;
            }
        }
    }

    public static class SpecialListBean {
        /**
         * isManualCreated : 1
         * planEndTime : 1500015000000
         * planStartTime : 1499117100000
         * taskId : 46
         * taskName : 123
         * taskState : 2
         */

        private int isManualCreated;
        private long planEndTime;
        private long planStartTime;
        private int taskId;
        private String taskName;
        private int taskState;

        public int getIsManualCreated() {
            return isManualCreated;
        }

        public void setIsManualCreated(int isManualCreated) {
            this.isManualCreated = isManualCreated;
        }

        public long getPlanEndTime() {
            return planEndTime;
        }

        public void setPlanEndTime(long planEndTime) {
            this.planEndTime = planEndTime;
        }

        public long getPlanStartTime() {
            return planStartTime;
        }

        public void setPlanStartTime(long planStartTime) {
            this.planStartTime = planStartTime;
        }

        public int getTaskId() {
            return taskId;
        }

        public void setTaskId(int taskId) {
            this.taskId = taskId;
        }

        public String getTaskName() {
            return taskName;
        }

        public void setTaskName(String taskName) {
            this.taskName = taskName;
        }

        public int getTaskState() {
            return taskState;
        }

        public void setTaskState(int taskState) {
            this.taskState = taskState;
        }
    }
}
