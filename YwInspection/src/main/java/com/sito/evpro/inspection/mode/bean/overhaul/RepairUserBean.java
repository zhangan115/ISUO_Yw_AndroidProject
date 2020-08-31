package com.sito.evpro.inspection.mode.bean.overhaul;

/**
 * Created by zhangan on 2017-08-01.
 */

public class RepairUserBean {

    /**
     * id : 408
     * user : {"createTime":1499823981000,"deleteState":0,"joinTime":1499823981000,"realName":"闫鑫","userId":33,"userName":"yanxin","userPhone":"123456","userRoleNames":"运行班长","userTelephone":"运行一班-班长","userType":2}
     */

    private int id;
    private UserBean user;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public static class UserBean {
        /**
         * createTime : 1499823981000
         * deleteState : 0
         * joinTime : 1499823981000
         * realName : 闫鑫
         * userId : 33
         * userName : yanxin
         * userPhone : 123456
         * userRoleNames : 运行班长
         * userTelephone : 运行一班-班长
         * userType : 2
         */

        private long createTime;
        private int deleteState;
        private long joinTime;
        private String realName;
        private int userId;
        private String userName;
        private String userPhone;
        private String userRoleNames;
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

        public String getUserRoleNames() {
            return userRoleNames;
        }

        public void setUserRoleNames(String userRoleNames) {
            this.userRoleNames = userRoleNames;
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
