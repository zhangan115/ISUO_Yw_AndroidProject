package com.isuo.yw2application.mode.bean.db;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 配电室列表
 * Created by zhangan on 2021-08-16.
 */
@Entity(nameInDb = "inspection_task_db")
public class InspectionTaskDb implements Parcelable {

    @Id(autoincrement = true)
    private Long _id;
    private String time; //时间
    private int taskType;//类型
    private String content;//内容

    protected InspectionTaskDb(Parcel in) {
        if (in.readByte() == 0) {
            _id = null;
        } else {
            _id = in.readLong();
        }
        time = in.readString();
        taskType = in.readInt();
        content = in.readString();

    }

    @Generated(hash = 1805931846)
    public InspectionTaskDb(Long _id, String time, int taskType, String content) {
        this._id = _id;
        this.time = time;
        this.taskType = taskType;
        this.content = content;
    }

    @Generated(hash = 995284115)
    public InspectionTaskDb() {
    }

    public static final Creator<InspectionTaskDb> CREATOR = new Creator<InspectionTaskDb>() {
        @Override
        public InspectionTaskDb createFromParcel(Parcel in) {
            return new InspectionTaskDb(in);
        }

        @Override
        public InspectionTaskDb[] newArray(int size) {
            return new InspectionTaskDb[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (_id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(_id);
        }
        dest.writeString(time);
        dest.writeInt(taskType);
        dest.writeString(content);
    }

    public Long get_id() {
        return this._id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getTaskType() {
        return this.taskType;
    }

    public void setTaskType(int taskType) {
        this.taskType = taskType;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
