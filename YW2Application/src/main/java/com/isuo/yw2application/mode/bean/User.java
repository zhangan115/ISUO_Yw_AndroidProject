package com.isuo.yw2application.mode.bean;

import android.os.Parcel;
import android.os.Parcelable;


import com.isuo.yw2application.mode.bean.resource.ResTree;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户类
 * Created by zhangan on 2017-06-22.
 */

public class User implements Parcelable {

    private CustomerBean customer;
    private int deleteState;
    private long joinTime;
    private int userId;
    private int userIdentity;
    private String userName;
    private String userPhone;
    private int userType;
    private int isRepair;
    private String userRoleNames;
    private String portraitUrl;
    private long createTime;
    private List<ResTree> resTreeList;

    private String realName;
    private String birthDay;
    private int age = -1;
    private int sex = -1;
    private String height;
    private String post;
    private String ability;
    private String entryTime;
    private String userCode;
    private int isHideName;
    private int isHidePhone;
    private PayMenuBean customerSetMenu;


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

    public List<ResTree> getResTreeList() {
        return resTreeList;
    }

    public void setResTreeList(List<ResTree> resTreeList) {
        this.resTreeList = resTreeList;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getAbility() {
        return ability;
    }

    public void setAbility(String ability) {
        this.ability = ability;
    }

    public String getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(String entryTime) {
        this.entryTime = entryTime;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public int getIsHideName() {
        return isHideName;
    }

    public void setIsHideName(int isHideName) {
        this.isHideName = isHideName;
    }

    public int getIsHidePhone() {
        return isHidePhone;
    }

    public void setIsHidePhone(int isHidePhone) {
        this.isHidePhone = isHidePhone;
    }

    public static class CustomerBean implements Parcelable {

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

        public List<CustomerConfigBean> getCustomerConfig() {
            return customerConfig;
        }

        public void setCustomerConfig(List<CustomerConfigBean> customerConfig) {
            this.customerConfig = customerConfig;
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

        public CustomerBean() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.contractTime);
            dest.writeLong(this.createTime);
            dest.writeString(this.customerAddress);
            dest.writeInt(this.customerId);
            dest.writeString(this.customerImage);
            dest.writeString(this.customerLinkman);
            dest.writeString(this.customerName);
            dest.writeString(this.customerNumber);
            dest.writeString(this.customerPhone);
            dest.writeString(this.customerRemark);
            dest.writeInt(this.deleteState);
            dest.writeString(this.inTime);
            dest.writeInt(this.isOpen);
            dest.writeTypedList(this.customerConfig);
            dest.writeList(this.roomList);
        }

        protected CustomerBean(Parcel in) {
            this.contractTime = in.readString();
            this.createTime = in.readLong();
            this.customerAddress = in.readString();
            this.customerId = in.readInt();
            this.customerImage = in.readString();
            this.customerLinkman = in.readString();
            this.customerName = in.readString();
            this.customerNumber = in.readString();
            this.customerPhone = in.readString();
            this.customerRemark = in.readString();
            this.deleteState = in.readInt();
            this.inTime = in.readString();
            this.isOpen = in.readInt();
            this.customerConfig = in.createTypedArrayList(CustomerConfigBean.CREATOR);
            this.roomList = new ArrayList<RoomListBean>();
            in.readList(this.roomList, RoomListBean.class.getClassLoader());
        }

        public static final Creator<CustomerBean> CREATOR = new Creator<CustomerBean>() {
            @Override
            public CustomerBean createFromParcel(Parcel source) {
                return new CustomerBean(source);
            }

            @Override
            public CustomerBean[] newArray(int size) {
                return new CustomerBean[size];
            }
        };
    }

    public User() {
    }

    public PayMenuBean getCustomerSetMenu() {
        return customerSetMenu;
    }

    public void setCustomerSetMenu(PayMenuBean customerSetMenu) {
        this.customerSetMenu = customerSetMenu;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.customer, flags);
        dest.writeInt(this.deleteState);
        dest.writeLong(this.joinTime);
        dest.writeInt(this.userId);
        dest.writeInt(this.userIdentity);
        dest.writeString(this.userName);
        dest.writeString(this.userPhone);
        dest.writeInt(this.userType);
        dest.writeInt(this.isRepair);
        dest.writeString(this.userRoleNames);
        dest.writeString(this.portraitUrl);
        dest.writeLong(this.createTime);
        dest.writeTypedList(this.resTreeList);
        dest.writeString(this.realName);
        dest.writeString(this.birthDay);
        dest.writeInt(this.age);
        dest.writeInt(this.sex);
        dest.writeString(this.height);
        dest.writeString(this.post);
        dest.writeString(this.ability);
        dest.writeString(this.entryTime);
        dest.writeString(this.userCode);
        dest.writeInt(this.isHideName);
        dest.writeInt(this.isHidePhone);
        dest.writeParcelable(this.customerSetMenu, flags);
    }

    protected User(Parcel in) {
        this.customer = in.readParcelable(CustomerBean.class.getClassLoader());
        this.deleteState = in.readInt();
        this.joinTime = in.readLong();
        this.userId = in.readInt();
        this.userIdentity = in.readInt();
        this.userName = in.readString();
        this.userPhone = in.readString();
        this.userType = in.readInt();
        this.isRepair = in.readInt();
        this.userRoleNames = in.readString();
        this.portraitUrl = in.readString();
        this.createTime = in.readLong();
        this.resTreeList = in.createTypedArrayList(ResTree.CREATOR);
        this.realName = in.readString();
        this.birthDay = in.readString();
        this.age = in.readInt();
        this.sex = in.readInt();
        this.height = in.readString();
        this.post = in.readString();
        this.ability = in.readString();
        this.entryTime = in.readString();
        this.userCode = in.readString();
        this.isHideName = in.readInt();
        this.isHidePhone = in.readInt();
        this.customerSetMenu = in.readParcelable(PayMenuBean.class.getClassLoader());
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
