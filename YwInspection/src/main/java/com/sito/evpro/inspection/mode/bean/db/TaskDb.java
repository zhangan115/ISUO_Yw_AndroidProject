package com.sito.evpro.inspection.mode.bean.db;

import com.sito.evpro.inspection.app.InspectionApp;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by zhangan on 2017-07-10.
 */
@Entity(nameInDb = "task")
public class TaskDb {

    @Id
    private Long _id;
    private Long taskId;
    private long userId;//该项目的检测人员id
    private String userName;//该任务保存的人员name
    private long currectUserId = InspectionApp.getInstance().getCurrentUser().getUserId();

    public TaskDb() {
    }

    public TaskDb(Long taskId, long userId, String userName) {
        this.taskId = taskId;
        this.userId = userId;
        this.userName = userName;
    }

    @Generated(hash = 866250853)
    public TaskDb(Long _id, Long taskId, long userId, String userName, long currectUserId) {
        this._id = _id;
        this.taskId = taskId;
        this.userId = userId;
        this.userName = userName;
        this.currectUserId = currectUserId;
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

    public long getCurrectUserId() {
        return currectUserId;
    }

    public void setCurrectUserId(long currectUserId) {
        this.currectUserId = currectUserId;
    }
}
