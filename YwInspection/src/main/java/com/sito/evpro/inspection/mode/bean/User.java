package com.sito.evpro.inspection.mode.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by zhangan on 2017-06-22.
 */

public class User implements Parcelable{

    private CustomerBean customer;
    private int deleteState;
    private long joinTime;
    private String realName;
    private int userId;
    private int userIdentity;
    private String userName;
    private String userPhone;
    private int userType;
    private String userRoleNames;
    private int isRepair;//是否检修身份
    private String portraitUrl;

    public String getPortraitUrl() {
        return portraitUrl;
    }

    public void setPortraitUrl(String portraitUrl) {
        this.portraitUrl = portraitUrl;
    }

    public int getIsRepair() {
        return isRepair;
    }

    public void setIsRepair(int isRepair) {
        this.isRepair = isRepair;
    }

    public String getUserRoleNames() {
        return userRoleNames;
    }

    public void setUserRoleNames(String userRoleNames) {
        this.userRoleNames = userRoleNames;
    }

    public CustomerBean getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerBean customer) {
        this.customer = customer;
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

    public int getUserIdentity() {
        return userIdentity;
    }

    public void setUserIdentity(int userIdentity) {
        this.userIdentity = userIdentity;
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

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public static class CustomerBean {

        private String contractTime;
        private long createTime;
        private String customerAddress;
        private int customerId;
        private String customerImage;
        private String customerLinkman;
        private String customerName;
        private String customerNumber;
        private String customerPhone;
        private String customerRemark;
        private int deleteState;
        private String inTime;
        private int isOpen;
        private List<CustomerConfigBean> customerConfig;
        private List<RoomListBean> roomList;

        public List<CustomerConfigBean> getCustomerConfig() {
            return customerConfig;
        }

        public void setCustomerConfig(List<CustomerConfigBean> customerConfig) {
            this.customerConfig = customerConfig;
        }

        public int getIsOpen() {
            return isOpen;
        }

        public void setIsOpen(int isOpen) {
            this.isOpen = isOpen;
        }

        public String getContractTime() {
            return contractTime;
        }

        public void setContractTime(String contractTime) {
            this.contractTime = contractTime;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public String getCustomerAddress() {
            return customerAddress;
        }

        public void setCustomerAddress(String customerAddress) {
            this.customerAddress = customerAddress;
        }

        public int getCustomerId() {
            return customerId;
        }

        public void setCustomerId(int customerId) {
            this.customerId = customerId;
        }

        public String getCustomerImage() {
            return customerImage;
        }

        public void setCustomerImage(String customerImage) {
            this.customerImage = customerImage;
        }

        public String getCustomerLinkman() {
            return customerLinkman;
        }

        public void setCustomerLinkman(String customerLinkman) {
            this.customerLinkman = customerLinkman;
        }

        public String getCustomerName() {
            return customerName;
        }

        public void setCustomerName(String customerName) {
            this.customerName = customerName;
        }

        public String getCustomerNumber() {
            return customerNumber;
        }

        public void setCustomerNumber(String customerNumber) {
            this.customerNumber = customerNumber;
        }

        public String getCustomerPhone() {
            return customerPhone;
        }

        public void setCustomerPhone(String customerPhone) {
            this.customerPhone = customerPhone;
        }

        public String getCustomerRemark() {
            return customerRemark;
        }

        public void setCustomerRemark(String customerRemark) {
            this.customerRemark = customerRemark;
        }

        public int getDeleteState() {
            return deleteState;
        }

        public void setDeleteState(int deleteState) {
            this.deleteState = deleteState;
        }

        public String getInTime() {
            return inTime;
        }

        public void setInTime(String inTime) {
            this.inTime = inTime;
        }

        public List<RoomListBean> getRoomList() {
            return roomList;
        }

        public void setRoomList(List<RoomListBean> roomList) {
            this.roomList = roomList;
        }

        public static class RoomListBean {

            private String createTime;
            private int deleteState;
            private int roomId;
            private String roomName;
            private String roomRemark;

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public int getDeleteState() {
                return deleteState;
            }

            public void setDeleteState(int deleteState) {
                this.deleteState = deleteState;
            }

            public int getRoomId() {
                return roomId;
            }

            public void setRoomId(int roomId) {
                this.roomId = roomId;
            }

            public String getRoomName() {
                return roomName;
            }

            public void setRoomName(String roomName) {
                this.roomName = roomName;
            }

            public String getRoomRemark() {
                return roomRemark;
            }

            public void setRoomRemark(String roomRemark) {
                this.roomRemark = roomRemark;
            }
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.deleteState);
        dest.writeLong(this.joinTime);
        dest.writeString(this.realName);
        dest.writeInt(this.userId);
        dest.writeInt(this.userIdentity);
        dest.writeString(this.userName);
        dest.writeString(this.userPhone);
        dest.writeInt(this.userType);
        dest.writeString(this.userRoleNames);
        dest.writeInt(this.isRepair);
        dest.writeString(this.portraitUrl);
    }

    public User() {
    }

    protected User(Parcel in) {
        this.deleteState = in.readInt();
        this.joinTime = in.readLong();
        this.realName = in.readString();
        this.userId = in.readInt();
        this.userIdentity = in.readInt();
        this.userName = in.readString();
        this.userPhone = in.readString();
        this.userType = in.readInt();
        this.userRoleNames = in.readString();
        this.isRepair = in.readInt();
        this.portraitUrl = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
