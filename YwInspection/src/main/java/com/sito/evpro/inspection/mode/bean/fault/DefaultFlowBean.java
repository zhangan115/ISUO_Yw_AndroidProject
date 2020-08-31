package com.sito.evpro.inspection.mode.bean.fault;

import com.sito.evpro.inspection.mode.bean.User;

import java.util.List;

/**
 * Created by zhangan on 2017/8/30.
 */

public class DefaultFlowBean {

    private long createTime;
    private CustomerBean customer;
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

    public CustomerBean getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerBean customer) {
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

    public static class CustomerBean {

        private String contractTime;
        private long createTime;
        private String customerAddress;
        private long customerId;
        private String customerImage;
        private String customerIntroduce;
        private String customerLinkman;
        private String customerName;
        private String customerNumber;
        private String customerPhone;
        private int deleteState;
        private String inTime;
        private int isOpen;
        private String postCode;

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

        public long getCustomerId() {
            return customerId;
        }

        public void setCustomerId(long customerId) {
            this.customerId = customerId;
        }

        public String getCustomerImage() {
            return customerImage;
        }

        public void setCustomerImage(String customerImage) {
            this.customerImage = customerImage;
        }

        public String getCustomerIntroduce() {
            return customerIntroduce;
        }

        public void setCustomerIntroduce(String customerIntroduce) {
            this.customerIntroduce = customerIntroduce;
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

        public int getIsOpen() {
            return isOpen;
        }

        public void setIsOpen(int isOpen) {
            this.isOpen = isOpen;
        }

        public String getPostCode() {
            return postCode;
        }

        public void setPostCode(String postCode) {
            this.postCode = postCode;
        }
    }

    public static class DeptBean {

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
    }
}
