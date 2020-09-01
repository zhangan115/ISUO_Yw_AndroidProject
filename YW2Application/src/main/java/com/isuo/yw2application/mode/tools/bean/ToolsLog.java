package com.isuo.yw2application.mode.tools.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.isuo.yw2application.mode.bean.User;

import java.util.List;


/**
 * 工具使用记录实体类
 *
 * @author wanwentao
 */

public class ToolsLog implements Parcelable {

    private Long logId;
    private Tools tools;
    private User useUser;//借用人
    private String use;//用途
    private long useTime;//借用日期
    private long preReturnTime;//预计归还日期
    private long returnTime;//归还日期
    /**
     * 借用状态:
     * 1、管理员填写外借信息，借用人未确认 ，工具状态还是在库
     * 2、借用人已确认 ，工具状态为外借
     */
    private String outStatus;
    private List<CheckListBean> checkList;

    public ToolsLog() {
    }

    public Long getLogId() {
        return logId;
    }

    public void setLogId(Long logId) {
        this.logId = logId;
    }

    public Tools getTools() {
        return tools;
    }

    public void setTools(Tools tools) {
        this.tools = tools;
    }

    public User getUseUser() {
        return useUser;
    }

    public void setUseUser(User useUser) {
        this.useUser = useUser;
    }

    public String getUse() {
        return use;
    }

    public void setUse(String use) {
        this.use = use;
    }

    public long getUseTime() {
        return useTime;
    }

    public void setUseTime(long useTime) {
        this.useTime = useTime;
    }

    public long getPreReturnTime() {
        return preReturnTime;
    }

    public void setPreReturnTime(long preReturnTime) {
        this.preReturnTime = preReturnTime;
    }

    public long getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(long returnTime) {
        this.returnTime = returnTime;
    }

    public String getOutStatus() {
        return outStatus;
    }

    public void setOutStatus(String outStatus) {
        this.outStatus = outStatus;
    }

    public List<CheckListBean> getCheckList() {
        return checkList;
    }

    public void setCheckList(List<CheckListBean> checkList) {
        this.checkList = checkList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.logId);
        dest.writeParcelable(this.tools, flags);
        dest.writeParcelable(this.useUser, flags);
        dest.writeString(this.use);
        dest.writeLong(this.useTime);
        dest.writeLong(this.preReturnTime);
        dest.writeLong(this.returnTime);
        dest.writeString(this.outStatus);
        dest.writeTypedList(this.checkList);
    }

    protected ToolsLog(Parcel in) {
        this.logId = (Long) in.readValue(Long.class.getClassLoader());
        this.tools = in.readParcelable(Tools.class.getClassLoader());
        this.useUser = in.readParcelable(User.class.getClassLoader());
        this.use = in.readString();
        this.useTime = in.readLong();
        this.preReturnTime = in.readLong();
        this.returnTime = in.readLong();
        this.outStatus = in.readString();
        this.checkList = in.createTypedArrayList(CheckListBean.CREATOR);
    }

    public static final Creator<ToolsLog> CREATOR = new Creator<ToolsLog>() {
        @Override
        public ToolsLog createFromParcel(Parcel source) {
            return new ToolsLog(source);
        }

        @Override
        public ToolsLog[] newArray(int size) {
            return new ToolsLog[size];
        }
    };
}
