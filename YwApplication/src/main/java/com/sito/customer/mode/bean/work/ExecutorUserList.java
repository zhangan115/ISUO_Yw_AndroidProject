package com.sito.customer.mode.bean.work;


import android.os.Parcel;
import android.os.Parcelable;

import com.sito.customer.mode.bean.User;

/**
 * Created by zhangan on 2017/11/10.
 */

public class ExecutorUserList  implements Parcelable{
    private long taskId;
    private User user;

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.taskId);
        dest.writeParcelable(this.user, flags);
    }

    public ExecutorUserList() {
    }

    protected ExecutorUserList(Parcel in) {
        this.taskId = in.readLong();
        this.user = in.readParcelable(User.class.getClassLoader());
    }

    public static final Creator<ExecutorUserList> CREATOR = new Creator<ExecutorUserList>() {
        @Override
        public ExecutorUserList createFromParcel(Parcel source) {
            return new ExecutorUserList(source);
        }

        @Override
        public ExecutorUserList[] newArray(int size) {
            return new ExecutorUserList[size];
        }
    };
}
