package com.isuo.yw2application.mode.bean.work;

import android.os.Parcel;
import android.os.Parcelable;

import com.isuo.yw2application.mode.bean.User;

import java.util.List;

/**
 * 巡检任务
 * Created by zhangan on 2017-07-14.
 */

public class InspectionBean implements Parcelable{
    private int count;
    private long planEndTime;
    private long planStartTime;
    private long startTime;
    private long endTime;
    private long taskId;
    private String taskName;
    private int taskState;
    private int uploadCount;
    private List<String> rooms;
    private SecurityPackage securityPackage;
    private int planPeriodType;//检测周期类型 1：每日 2：每周 3：每月
    private int isManualCreated;//是否是人工创建的 1：是 0：不是 人工创建的均视为特检任务
    private User receiveUser;
    private List<User> users;
    private List<ExecutorUserList> executorUserList;//指定的执行人
    private String regionName;
    private long[] roomIds;

    public User getReceiveUser() {
        return receiveUser;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public void setReceiveUser(User receiveUser) {
        this.receiveUser = receiveUser;
    }

    public SecurityPackage getSecurityPackage() {
        return securityPackage;
    }

    public void setSecurityPackage(SecurityPackage securityPackage) {
        this.securityPackage = securityPackage;
    }

    public List<ExecutorUserList> getExecutorUserList() {
        return executorUserList;
    }

    public void setExecutorUserList(List<ExecutorUserList> executorUserList) {
        this.executorUserList = executorUserList;
    }

    public int getPlanPeriodType() {
        return planPeriodType;
    }

    public void setPlanPeriodType(int planPeriodType) {
        this.planPeriodType = planPeriodType;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

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

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
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

    public int getUploadCount() {
        return uploadCount;
    }

    public void setUploadCount(int uploadCount) {
        this.uploadCount = uploadCount;
    }

    public List<String> getRooms() {
        return rooms;
    }

    public void setRooms(List<String> rooms) {
        this.rooms = rooms;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public long[] getRoomIds() {
        return roomIds;
    }

    public void setRoomIds(long[] roomIds) {
        this.roomIds = roomIds;
    }

    public static class SecurityPackage {

        /**
         * createTime : 1500003846000
         * deleteState : 0
         * securityId : 9
         * securityName : 西拓研发部安全包
         * securityRemark : 西拓研发部安全包
         */

        private long createTime;
        private int deleteState;
        private int securityId;
        private String securityName;
        private String securityRemark;

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

        public int getSecurityId() {
            return securityId;
        }

        public void setSecurityId(int securityId) {
            this.securityId = securityId;
        }

        public String getSecurityName() {
            return securityName;
        }

        public void setSecurityName(String securityName) {
            this.securityName = securityName;
        }

        public String getSecurityRemark() {
            return securityRemark;
        }

        public void setSecurityRemark(String securityRemark) {
            this.securityRemark = securityRemark;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.count);
        dest.writeLong(this.planEndTime);
        dest.writeLong(this.planStartTime);
        dest.writeLong(this.startTime);
        dest.writeLong(this.endTime);
        dest.writeLong(this.taskId);
        dest.writeString(this.taskName);
        dest.writeInt(this.taskState);
        dest.writeInt(this.uploadCount);
        dest.writeStringList(this.rooms);
        dest.writeInt(this.planPeriodType);
        dest.writeInt(this.isManualCreated);
        dest.writeParcelable(this.receiveUser, flags);
        dest.writeTypedList(this.users);
        dest.writeTypedList(this.executorUserList);
        dest.writeString(this.regionName);
        dest.writeLongArray(this.roomIds);
    }

    public InspectionBean() {
    }

    protected InspectionBean(Parcel in) {
        this.count = in.readInt();
        this.planEndTime = in.readLong();
        this.planStartTime = in.readLong();
        this.startTime = in.readLong();
        this.endTime = in.readLong();
        this.taskId = in.readLong();
        this.taskName = in.readString();
        this.taskState = in.readInt();
        this.uploadCount = in.readInt();
        this.rooms = in.createStringArrayList();
        this.planPeriodType = in.readInt();
        this.isManualCreated = in.readInt();
        this.receiveUser = in.readParcelable(User.class.getClassLoader());
        this.users = in.createTypedArrayList(User.CREATOR);
        this.executorUserList = in.createTypedArrayList(ExecutorUserList.CREATOR);
        this.regionName = in.readString();
    }

    public static final Creator<InspectionBean> CREATOR = new Creator<InspectionBean>() {
        @Override
        public InspectionBean createFromParcel(Parcel source) {
            return new InspectionBean(source);
        }

        @Override
        public InspectionBean[] newArray(int size) {
            return new InspectionBean[size];
        }
    };
}
