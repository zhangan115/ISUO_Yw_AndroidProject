package com.sito.customer.mode.bean.work;

import android.os.Parcel;
import android.os.Parcelable;

import com.sito.customer.mode.bean.User;
import com.sito.customer.mode.bean.equip.EquipmentBean;


/**
 * 专项工作
 * Created by zhangan on 2017-07-06.
 */

public class IncrementBean implements Parcelable {

    private long createTime;
    private long sourceId;
    private long commitTime;
    private String workContent;
    private long workId;
    private String workImages;
    private String workSound;
    private int workType;
    private int soundTimescale;
    private boolean isPlay = false;

    private long endTime;
    private EquipmentBean equipment;
    private int operation;
    private String sourceName;
    private long startTime;
    private long workIdX;
    private int workIssued;
    private int workState;
    private String workTypeName;
    private int xsoundTimescale;
    private String xworkContent;
    private String xworkImages;
    private String xworkSound;
    private User user;
    private User issuedUser;
    private String userNames;
    private String userIds;

    public String getUserNames() {
        return userNames;
    }

    public void setUserNames(String userNames) {
        this.userNames = userNames;
    }

    public User getIssuedUser() {
        return issuedUser;
    }

    public void setIssuedUser(User issuedUser) {
        this.issuedUser = issuedUser;
    }

    public boolean isPlay() {
        return isPlay;
    }

    public long getCommitTime() {
        return commitTime;
    }

    public void setCommitTime(long commitTime) {
        this.commitTime = commitTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setPlay(boolean play) {
        isPlay = play;
    }

    public EquipmentBean getEquipment() {
        return equipment;
    }

    public void setEquipment(EquipmentBean equipment) {
        this.equipment = equipment;
    }

    public int getSoundTimescale() {
        return soundTimescale;
    }

    public void setSoundTimescale(int soundTimescale) {
        this.soundTimescale = soundTimescale;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getSourceId() {
        return sourceId;
    }

    public void setSourceId(long sourceId) {
        this.sourceId = sourceId;
    }

    public String getWorkContent() {
        return workContent;
    }

    public void setWorkContent(String workContent) {
        this.workContent = workContent;
    }

    public long getWorkId() {
        return workId;
    }

    public void setWorkId(long workId) {
        this.workId = workId;
    }

    public String getWorkImages() {
        return workImages;
    }

    public void setWorkImages(String workImages) {
        this.workImages = workImages;
    }

    public String getWorkSound() {
        return workSound;
    }

    public void setWorkSound(String workSound) {
        this.workSound = workSound;
    }

    public int getWorkType() {
        return workType;
    }

    public void setWorkType(int workType) {
        this.workType = workType;
    }

    public IncrementBean() {
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }


    public int getOperation() {
        return operation;
    }

    public void setOperation(int operation) {
        this.operation = operation;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getWorkIdX() {
        return workIdX;
    }

    public void setWorkIdX(long workIdX) {
        this.workIdX = workIdX;
    }

    public int getWorkIssued() {
        return workIssued;
    }

    public void setWorkIssued(int workIssued) {
        this.workIssued = workIssued;
    }

    public int getWorkState() {
        return workState;
    }

    public void setWorkState(int workState) {
        this.workState = workState;
    }

    public String getWorkTypeName() {
        return workTypeName;
    }

    public void setWorkTypeName(String workTypeName) {
        this.workTypeName = workTypeName;
    }

    public int getXsoundTimescale() {
        return xsoundTimescale;
    }

    public void setXsoundTimescale(int xsoundTimescale) {
        this.xsoundTimescale = xsoundTimescale;
    }

    public String getXworkContent() {
        return xworkContent;
    }

    public void setXworkContent(String xworkContent) {
        this.xworkContent = xworkContent;
    }

    public String getXworkImages() {
        return xworkImages;
    }

    public void setXworkImages(String xworkImages) {
        this.xworkImages = xworkImages;
    }

    public String getXworkSound() {
        return xworkSound;
    }

    public void setXworkSound(String xworkSound) {
        this.xworkSound = xworkSound;
    }

    public String getUserIds() {
        return userIds;
    }

    public void setUserIds(String userIds) {
        this.userIds = userIds;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.createTime);
        dest.writeLong(this.sourceId);
        dest.writeLong(this.commitTime);
        dest.writeString(this.workContent);
        dest.writeLong(this.workId);
        dest.writeString(this.workImages);
        dest.writeString(this.workSound);
        dest.writeInt(this.workType);
        dest.writeInt(this.soundTimescale);
        dest.writeByte(this.isPlay ? (byte) 1 : (byte) 0);
        dest.writeLong(this.endTime);
        dest.writeParcelable(this.equipment, flags);
        dest.writeInt(this.operation);
        dest.writeString(this.sourceName);
        dest.writeLong(this.startTime);
        dest.writeLong(this.workIdX);
        dest.writeInt(this.workIssued);
        dest.writeInt(this.workState);
        dest.writeString(this.workTypeName);
        dest.writeInt(this.xsoundTimescale);
        dest.writeString(this.xworkContent);
        dest.writeString(this.xworkImages);
        dest.writeString(this.xworkSound);
        dest.writeParcelable(this.user, flags);
        dest.writeParcelable(this.issuedUser, flags);
        dest.writeString(this.userNames);
        dest.writeString(this.userIds);
    }

    protected IncrementBean(Parcel in) {
        this.createTime = in.readLong();
        this.sourceId = in.readLong();
        this.commitTime = in.readLong();
        this.workContent = in.readString();
        this.workId = in.readLong();
        this.workImages = in.readString();
        this.workSound = in.readString();
        this.workType = in.readInt();
        this.soundTimescale = in.readInt();
        this.isPlay = in.readByte() != 0;
        this.endTime = in.readLong();
        this.equipment = in.readParcelable(EquipmentBean.class.getClassLoader());
        this.operation = in.readInt();
        this.sourceName = in.readString();
        this.startTime = in.readLong();
        this.workIdX = in.readLong();
        this.workIssued = in.readInt();
        this.workState = in.readInt();
        this.workTypeName = in.readString();
        this.xsoundTimescale = in.readInt();
        this.xworkContent = in.readString();
        this.xworkImages = in.readString();
        this.xworkSound = in.readString();
        this.user = in.readParcelable(User.class.getClassLoader());
        this.issuedUser = in.readParcelable(User.class.getClassLoader());
        this.userNames = in.readString();
        this.userIds = in.readString();
    }

    public static final Creator<IncrementBean> CREATOR = new Creator<IncrementBean>() {
        @Override
        public IncrementBean createFromParcel(Parcel source) {
            return new IncrementBean(source);
        }

        @Override
        public IncrementBean[] newArray(int size) {
            return new IncrementBean[size];
        }
    };
}
