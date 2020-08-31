package com.sito.customer.mode.bean.inspection;

import android.os.Parcel;
import android.os.Parcelable;

import com.sito.customer.mode.bean.db.EquipmentDataDb;

import java.util.List;

/**
 * 巡检项目对象
 * Created by zhangan on 2017-07-10.
 */

public class DataItemBean implements Parcelable {

    private long createTime;
    private int deleteState;
    private long deleteTime;
    private long inspectionId;
    private String inspectionName;
    private int inspectionType;
    private List<InspectionItemOption> inspectionItemOptionList;
    private String quantityLowlimit;
    private String quantityUplimit;
    private String quantityUnit;
    private String value;
    private int isRequired;//1 必填，0 非必填
    private int isShareValue;//是否共享
    private String localFile;
    //本地添加的字段，进行逻辑处理
    private String chooseInspectionName;//本地添加属性：选择结果名称
    private EquipmentDataDb equipmentDataDb;//本地添加属性：保存的设备数据
    private boolean isUploading;

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

    public long getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(long deleteTime) {
        this.deleteTime = deleteTime;
    }

    public long getInspectionId() {
        return inspectionId;
    }

    public void setInspectionId(long inspectionId) {
        this.inspectionId = inspectionId;
    }

    public String getInspectionName() {
        return inspectionName;
    }

    public void setInspectionName(String inspectionName) {
        this.inspectionName = inspectionName;
    }

    public int getInspectionType() {
        return inspectionType;
    }

    public void setInspectionType(int inspectionType) {
        this.inspectionType = inspectionType;
    }

    public List<InspectionItemOption> getInspectionItemOptionList() {
        return inspectionItemOptionList;
    }

    public void setInspectionItemOptionList(List<InspectionItemOption> inspectionItemOptionList) {
        this.inspectionItemOptionList = inspectionItemOptionList;
    }

    public String getQuantityLowlimit() {
        return quantityLowlimit;
    }

    public void setQuantityLowlimit(String quantityLowlimit) {
        this.quantityLowlimit = quantityLowlimit;
    }

    public String getQuantityUplimit() {
        return quantityUplimit;
    }

    public void setQuantityUplimit(String quantityUplimit) {
        this.quantityUplimit = quantityUplimit;
    }

    public String getQuantityUnit() {
        return quantityUnit;
    }

    public void setQuantityUnit(String quantityUnit) {
        this.quantityUnit = quantityUnit;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getIsShareValue() {
        return isShareValue;
    }

    public void setIsShareValue(int isShareValue) {
        this.isShareValue = isShareValue;
    }

    public String getLocalFile() {
        return localFile;
    }

    public void setLocalFile(String localFile) {
        this.localFile = localFile;
    }

    public String getChooseInspectionName() {
        return chooseInspectionName;
    }

    public void setChooseInspectionName(String chooseInspectionName) {
        this.chooseInspectionName = chooseInspectionName;
    }

    public EquipmentDataDb getEquipmentDataDb() {
        return equipmentDataDb;
    }

    public void setEquipmentDataDb(EquipmentDataDb equipmentDataDb) {
        this.equipmentDataDb = equipmentDataDb;
    }

    public int getIsRequired() {
        return isRequired;
    }

    public void setIsRequired(int isRequired) {
        this.isRequired = isRequired;
    }

    public boolean isUploading() {
        return isUploading;
    }

    public void setUploading(boolean uploading) {
        isUploading = uploading;
    }


    public DataItemBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.createTime);
        dest.writeInt(this.deleteState);
        dest.writeLong(this.deleteTime);
        dest.writeLong(this.inspectionId);
        dest.writeString(this.inspectionName);
        dest.writeInt(this.inspectionType);
        dest.writeTypedList(this.inspectionItemOptionList);
        dest.writeString(this.quantityLowlimit);
        dest.writeString(this.quantityUplimit);
        dest.writeString(this.quantityUnit);
        dest.writeString(this.value);
        dest.writeInt(this.isRequired);
        dest.writeInt(this.isShareValue);
        dest.writeString(this.localFile);
        dest.writeString(this.chooseInspectionName);
        dest.writeParcelable(this.equipmentDataDb, flags);
        dest.writeByte(this.isUploading ? (byte) 1 : (byte) 0);
    }

    protected DataItemBean(Parcel in) {
        this.createTime = in.readLong();
        this.deleteState = in.readInt();
        this.deleteTime = in.readLong();
        this.inspectionId = in.readLong();
        this.inspectionName = in.readString();
        this.inspectionType = in.readInt();
        this.inspectionItemOptionList = in.createTypedArrayList(InspectionItemOption.CREATOR);
        this.quantityLowlimit = in.readString();
        this.quantityUplimit = in.readString();
        this.quantityUnit = in.readString();
        this.value = in.readString();
        this.isRequired = in.readInt();
        this.isShareValue = in.readInt();
        this.localFile = in.readString();
        this.chooseInspectionName = in.readString();
        this.equipmentDataDb = in.readParcelable(EquipmentDataDb.class.getClassLoader());
        this.isUploading = in.readByte() != 0;
    }

    public static final Creator<DataItemBean> CREATOR = new Creator<DataItemBean>() {
        @Override
        public DataItemBean createFromParcel(Parcel source) {
            return new DataItemBean(source);
        }

        @Override
        public DataItemBean[] newArray(int size) {
            return new DataItemBean[size];
        }
    };
}
