package com.isuo.yw2application.mode.bean.inspection;

import android.os.Parcel;
import android.os.Parcelable;

import com.isuo.yw2application.mode.bean.User;

import java.util.List;

/**
 * 巡检列表对象
 * Created by zhangan on 2017-07-05.
 */

public class InspectionBean implements Parcelable {

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

    protected InspectionBean(Parcel in) {
        count = in.readInt();
        planEndTime = in.readLong();
        planStartTime = in.readLong();
        startTime = in.readLong();
        endTime = in.readLong();
        taskId = in.readLong();
        taskName = in.readString();
        taskState = in.readInt();
        uploadCount = in.readInt();
        rooms = in.createStringArrayList();
        planPeriodType = in.readInt();
        isManualCreated = in.readInt();
        receiveUser = in.readParcelable(User.class.getClassLoader());
        users = in.createTypedArrayList(User.CREATOR);
        executorUserList = in.createTypedArrayList(ExecutorUserList.CREATOR);
        regionName = in.readString();
        roomIds = in.createLongArray();
    }

    public static final Creator<InspectionBean> CREATOR = new Creator<InspectionBean>() {
        @Override
        public InspectionBean createFromParcel(Parcel in) {
            return new InspectionBean(in);
        }

        @Override
        public InspectionBean[] newArray(int size) {
            return new InspectionBean[size];
        }
    };

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
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

    public SecurityPackage getSecurityPackage() {
        return securityPackage;
    }

    public void setSecurityPackage(SecurityPackage securityPackage) {
        this.securityPackage = securityPackage;
    }

    public int getPlanPeriodType() {
        return planPeriodType;
    }

    public void setPlanPeriodType(int planPeriodType) {
        this.planPeriodType = planPeriodType;
    }

    public int getIsManualCreated() {
        return isManualCreated;
    }

    public void setIsManualCreated(int isManualCreated) {
        this.isManualCreated = isManualCreated;
    }

    public User getReceiveUser() {
        return receiveUser;
    }

    public void setReceiveUser(User receiveUser) {
        this.receiveUser = receiveUser;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<ExecutorUserList> getExecutorUserList() {
        return executorUserList;
    }

    public void setExecutorUserList(List<ExecutorUserList> executorUserList) {
        this.executorUserList = executorUserList;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(count);
        dest.writeLong(planEndTime);
        dest.writeLong(planStartTime);
        dest.writeLong(startTime);
        dest.writeLong(endTime);
        dest.writeLong(taskId);
        dest.writeString(taskName);
        dest.writeInt(taskState);
        dest.writeInt(uploadCount);
        dest.writeStringList(rooms);
        dest.writeInt(planPeriodType);
        dest.writeInt(isManualCreated);
        dest.writeParcelable(receiveUser, flags);
        dest.writeTypedList(users);
        dest.writeTypedList(executorUserList);
        dest.writeString(regionName);
        dest.writeLongArray(roomIds);
    }
}
