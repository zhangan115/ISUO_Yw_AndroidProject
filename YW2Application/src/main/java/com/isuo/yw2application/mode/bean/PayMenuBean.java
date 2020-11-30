package com.isuo.yw2application.mode.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class PayMenuBean implements Parcelable {

    private int equipmentCount;
    private int faultCount;
    private int safetySo;
    private long id;
    private int workSpace;
    private int incrementWorkSo;
    private String menuName;
    private String menuView;
    private int menuType;
    private int oilSo;
    private int planCount;
    private int planEquipmentCount;
    private int price;
    private int repairCount;
    private int selectPersonCount;
    private int selectTaskCount;
    private int selectTimeCount;
    private int selectWorkCount;
    private int toolSo;
    private int deptSo;
    private int selfSo;
    private int personSo;
    private int faultSo;
    private int userCount;


    public int getEquipmentCount() {
        return equipmentCount;
    }

    public void setEquipmentCount(int equipmentCount) {
        this.equipmentCount = equipmentCount;
    }

    public int getFaultCount() {
        return faultCount;
    }

    public void setFaultCount(int faultCount) {
        this.faultCount = faultCount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getIncrementWorkSo() {
        return incrementWorkSo;
    }

    public void setIncrementWorkSo(int incrementWorkSo) {
        this.incrementWorkSo = incrementWorkSo;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuView() {
        return menuView;
    }

    public void setMenuView(String menuView) {
        this.menuView = menuView;
    }

    public int getMenuType() {
        return menuType;
    }

    public void setMenuType(int menuType) {
        this.menuType = menuType;
    }

    public int getOilSo() {
        return oilSo;
    }

    public void setOilSo(int oilSo) {
        this.oilSo = oilSo;
    }

    public int getPlanCount() {
        return planCount;
    }

    public void setPlanCount(int planCount) {
        this.planCount = planCount;
    }

    public int getPlanEquipmentCount() {
        return planEquipmentCount;
    }

    public void setPlanEquipmentCount(int planEquipmentCount) {
        this.planEquipmentCount = planEquipmentCount;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getRepairCount() {
        return repairCount;
    }

    public void setRepairCount(int repairCount) {
        this.repairCount = repairCount;
    }

    public int getSelectPersonCount() {
        return selectPersonCount;
    }

    public void setSelectPersonCount(int selectPersonCount) {
        this.selectPersonCount = selectPersonCount;
    }

    public int getSelectTaskCount() {
        return selectTaskCount;
    }

    public void setSelectTaskCount(int selectTaskCount) {
        this.selectTaskCount = selectTaskCount;
    }

    public int getSelectTimeCount() {
        return selectTimeCount;
    }

    public void setSelectTimeCount(int selectTimeCount) {
        this.selectTimeCount = selectTimeCount;
    }

    public int getSelectWorkCount() {
        return selectWorkCount;
    }

    public void setSelectWorkCount(int selectWorkCount) {
        this.selectWorkCount = selectWorkCount;
    }

    public int getToolSo() {
        return toolSo;
    }

    public void setToolSo(int toolSo) {
        this.toolSo = toolSo;
    }

    public int getUserCount() {
        return userCount;
    }

    public void setUserCount(int userCount) {
        this.userCount = userCount;
    }

    public int getDeptSo() {
        return deptSo;
    }

    public void setDeptSo(int deptSo) {
        this.deptSo = deptSo;
    }

    public int getSelfSo() {
        return selfSo;
    }

    public void setSelfSo(int selfSo) {
        this.selfSo = selfSo;
    }

    public int getPersonSo() {
        return personSo;
    }

    public void setPersonSo(int personSo) {
        this.personSo = personSo;
    }

    public int getFaultSo() {
        return faultSo;
    }

    public void setFaultSo(int faultSo) {
        this.faultSo = faultSo;
    }

    public int getWorkSpace() {
        return workSpace;
    }

    public void setWorkSpace(int workSpace) {
        this.workSpace = workSpace;
    }

    public int getSafetySo() {
        return safetySo;
    }

    public void setSafetySo(int safetySo) {
        this.safetySo = safetySo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.equipmentCount);
        dest.writeInt(this.faultCount);
        dest.writeInt(this.safetySo);
        dest.writeLong(this.id);
        dest.writeInt(this.workSpace);
        dest.writeInt(this.incrementWorkSo);
        dest.writeString(this.menuName);
        dest.writeString(this.menuView);
        dest.writeInt(this.menuType);
        dest.writeInt(this.oilSo);
        dest.writeInt(this.planCount);
        dest.writeInt(this.planEquipmentCount);
        dest.writeInt(this.price);
        dest.writeInt(this.repairCount);
        dest.writeInt(this.selectPersonCount);
        dest.writeInt(this.selectTaskCount);
        dest.writeInt(this.selectTimeCount);
        dest.writeInt(this.selectWorkCount);
        dest.writeInt(this.toolSo);
        dest.writeInt(this.deptSo);
        dest.writeInt(this.selfSo);
        dest.writeInt(this.personSo);
        dest.writeInt(this.faultSo);
        dest.writeInt(this.userCount);
    }

    public PayMenuBean() {
    }

    protected PayMenuBean(Parcel in) {
        this.equipmentCount = in.readInt();
        this.faultCount = in.readInt();
        this.safetySo = in.readInt();
        this.id = in.readLong();
        this.workSpace = in.readInt();
        this.incrementWorkSo = in.readInt();
        this.menuName = in.readString();
        this.menuView = in.readString();
        this.menuType = in.readInt();
        this.oilSo = in.readInt();
        this.planCount = in.readInt();
        this.planEquipmentCount = in.readInt();
        this.price = in.readInt();
        this.repairCount = in.readInt();
        this.selectPersonCount = in.readInt();
        this.selectTaskCount = in.readInt();
        this.selectTimeCount = in.readInt();
        this.selectWorkCount = in.readInt();
        this.toolSo = in.readInt();
        this.deptSo = in.readInt();
        this.selfSo = in.readInt();
        this.personSo = in.readInt();
        this.faultSo = in.readInt();
        this.userCount = in.readInt();
    }

    public static final Creator<PayMenuBean> CREATOR = new Creator<PayMenuBean>() {
        @Override
        public PayMenuBean createFromParcel(Parcel source) {
            return new PayMenuBean(source);
        }

        @Override
        public PayMenuBean[] newArray(int size) {
            return new PayMenuBean[size];
        }
    };
}
