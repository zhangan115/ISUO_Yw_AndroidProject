package com.sito.customer.mode.bean.db;

import android.os.Parcel;
import android.os.Parcelable;

import com.sito.customer.app.CustomerApp;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * 数据库保存的巡检人员信息
 * Created by zhangan on 2017-07-10.
 */
@Entity(nameInDb = "task")
public class TaskDb implements Parcelable {

    @Id(autoincrement = true)
    private Long _id;
    private Long taskId;
    private long userId;//该项目的检测人员id
    private String userName;//该任务保存的人员name
    private long currentUserId = CustomerApp.getInstance().getCurrentUser().getUserId();

    public TaskDb() {
    }

    public TaskDb(Long taskId, long userId, String userName) {
        this.taskId = taskId;
        this.userId = userId;
        this.userName = userName;
    }

    public Long get_id() {
        return _id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(long currentUserId) {
        this.currentUserId = currentUserId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this._id);
        dest.writeValue(this.taskId);
        dest.writeLong(this.userId);
        dest.writeString(this.userName);
        dest.writeLong(this.currentUserId);
    }

    protected TaskDb(Parcel in) {
        this._id = (Long) in.readValue(Long.class.getClassLoader());
        this.taskId = (Long) in.readValue(Long.class.getClassLoader());
        this.userId = in.readLong();
        this.userName = in.readString();
        this.currentUserId = in.readLong();
    }

    @Generated(hash = 12857823)
    public TaskDb(Long _id, Long taskId, long userId, String userName, long currentUserId) {
        this._id = _id;
        this.taskId = taskId;
        this.userId = userId;
        this.userName = userName;
        this.currentUserId = currentUserId;
    }

    public static final Parcelable.Creator<TaskDb> CREATOR = new Parcelable.Creator<TaskDb>() {
        @Override
        public TaskDb createFromParcel(Parcel source) {
            return new TaskDb(source);
        }

        @Override
        public TaskDb[] newArray(int size) {
            return new TaskDb[size];
        }
    };
}
