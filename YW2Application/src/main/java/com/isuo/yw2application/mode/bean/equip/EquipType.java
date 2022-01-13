package com.isuo.yw2application.mode.bean.equip;

/**
 * 作者：Yangzb on 2017/7/3 15:05
 * 邮箱：yangzongbin@si-top.com
 */
public class EquipType {

    private String name;
    private boolean isSelect;
    private long createTime;
    private long parentId;
    private int deleteState;
    private int equipmentTypeId;
    private String equipmentTypeName;
    private String equipmentTypeRemark;
    private int level;
    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public int getEquipmentTypeId() {
        return equipmentTypeId;
    }

    public void setEquipmentTypeId(int equipmentTypeId) {
        this.equipmentTypeId = equipmentTypeId;
    }

    public String getEquipmentTypeName() {
        return equipmentTypeName;
    }

    public void setEquipmentTypeName(String equipmentTypeName) {
        this.equipmentTypeName = equipmentTypeName;
    }

    public String getEquipmentTypeRemark() {
        return equipmentTypeRemark;
    }

    public void setEquipmentTypeRemark(String equipmentTypeRemark) {
        this.equipmentTypeRemark = equipmentTypeRemark;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }
}
