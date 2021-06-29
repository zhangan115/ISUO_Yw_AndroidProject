package com.isuo.yw2application.mode.bean.fault;


import android.os.Parcel;
import android.os.Parcelable;

import com.isuo.yw2application.mode.bean.User;

import java.util.List;

/**
 * Created by zhangan on 2017/8/30.
 */

public class DefaultFlowBean  implements Parcelable{

    private long createTime;
    private User.CustomerBean customer;
    private long customerId;
    private long defaultFlowId;
    private String defaultFlowName;
    private int defaultFlowNo;
    private int deleteState;
    private DeptBean dept;
    private long deptId;
    private List<User> usersN;

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public User.CustomerBean getCustomer() {
        return customer;
    }

    public void setCustomer(User.CustomerBean customer) {
        this.customer = customer;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public long getDefaultFlowId() {
        return defaultFlowId;
    }

    public void setDefaultFlowId(long defaultFlowId) {
        this.defaultFlowId = defaultFlowId;
    }

    public String getDefaultFlowName() {
        return defaultFlowName;
    }

    public void setDefaultFlowName(String defaultFlowName) {
        this.defaultFlowName = defaultFlowName;
    }

    public int getDefaultFlowNo() {
        return defaultFlowNo;
    }

    public void setDefaultFlowNo(int defaultFlowNo) {
        this.defaultFlowNo = defaultFlowNo;
    }

    public int getDeleteState() {
        return deleteState;
    }

    public void setDeleteState(int deleteState) {
        this.deleteState = deleteState;
    }

    public DeptBean getDept() {
        return dept;
    }

    public void setDept(DeptBean dept) {
        this.dept = dept;
    }

    public long getDeptId() {
        return deptId;
    }

    public void setDeptId(long deptId) {
        this.deptId = deptId;
    }

    public List<User> getUsersN() {
        return usersN;
    }

    public void setUsersN(List<User> usersN) {
        this.usersN = usersN;
    }

    public static class DeptBean implements Parcelable {

        private long createTime;
        private int deleteState;
        private long deptId;
        private int deptLevel;
        private String deptName;
        private int deptType;
        private long parentId;

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

        public long getDeptId() {
            return deptId;
        }

        public void setDeptId(long deptId) {
            this.deptId = deptId;
        }

        public int getDeptLevel() {
            return deptLevel;
        }

        public void setDeptLevel(int deptLevel) {
            this.deptLevel = deptLevel;
        }

        public String getDeptName() {
            return deptName;
        }

        public void setDeptName(String deptName) {
            this.deptName = deptName;
        }

        public int getDeptType() {
            return deptType;
        }

        public void setDeptType(int deptType) {
            this.deptType = deptType;
        }

        public long getParentId() {
            return parentId;
        }

        public void setParentId(long parentId) {
            this.parentId = parentId;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(this.createTime);
            dest.writeInt(this.deleteState);
            dest.writeLong(this.deptId);
            dest.writeInt(this.deptLevel);
            dest.writeString(this.deptName);
            dest.writeInt(this.deptType);
            dest.writeLong(this.parentId);
        }

        public DeptBean() {
        }

        protected DeptBean(Parcel in) {
            this.createTime = in.readLong();
            this.deleteState = in.readInt();
            this.deptId = in.readLong();
            this.deptLevel = in.readInt();
            this.deptName = in.readString();
            this.deptType = in.readInt();
            this.parentId = in.readLong();
        }

        public static final Creator<DeptBean> CREATOR = new Creator<DeptBean>() {
            @Override
            public DeptBean createFromParcel(Parcel source) {
                return new DeptBean(source);
            }

            @Override
            public DeptBean[] newArray(int size) {
                return new DeptBean[size];
            }
        };
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.createTime);
        dest.writeParcelable(this.customer, flags);
        dest.writeLong(this.customerId);
        dest.writeLong(this.defaultFlowId);
        dest.writeString(this.defaultFlowName);
        dest.writeInt(this.defaultFlowNo);
        dest.writeInt(this.deleteState);
        dest.writeParcelable(this.dept, flags);
        dest.writeLong(this.deptId);
        dest.writeTypedList(this.usersN);
    }

    public DefaultFlowBean() {
    }

    protected DefaultFlowBean(Parcel in) {
        this.createTime = in.readLong();
        this.customer = in.readParcelable(User.CustomerBean.class.getClassLoader());
        this.customerId = in.readLong();
        this.defaultFlowId = in.readLong();
        this.defaultFlowName = in.readString();
        this.defaultFlowNo = in.readInt();
        this.deleteState = in.readInt();
        this.dept = in.readParcelable(DeptBean.class.getClassLoader());
        this.deptId = in.readLong();
        this.usersN = in.createTypedArrayList(User.CREATOR);
    }

    public static final Creator<DefaultFlowBean> CREATOR = new Creator<DefaultFlowBean>() {
        @Override
        public DefaultFlowBean createFromParcel(Parcel source) {
            return new DefaultFlowBean(source);
        }

        @Override
        public DefaultFlowBean[] newArray(int size) {
            return new DefaultFlowBean[size];
        }
    };
}
