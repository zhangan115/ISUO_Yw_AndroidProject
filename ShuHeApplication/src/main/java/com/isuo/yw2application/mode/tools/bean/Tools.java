package com.isuo.yw2application.mode.tools.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.isuo.yw2application.mode.bean.User;


/**
 * 工具实体类
 *
 * @author wanwentao
 */
public class Tools implements Parcelable {

    private Long toolId;
    private String toolName;
    private String toolModel;//工具型号
    private User.CustomerBean customer;
    private String toolNumber;//工具编号
    /**
     * 1:消耗品 2:公用品
     */
    private String toolType;
    private long buyTime;//购买日期
    private String manufacturer;//生产厂家
    private Integer toolCount;//数量
    private float unitPrice;//单价
    private String toolPic;//图片
    private String isUse;//是否在库  0在库房 1外借
    private String toolStatus;//工具状态 1：正常 2：遗失 3：损坏
    private User createUser;
    private String toolDesc;//描述
    private String useUser;
    private String depositaryPlace;
    private ToolsLog toolsLog;

    private String toolCertificateNo;//样品编号/证书编号
    private String toolDetectionType;//检测类型
    private String toolDetectionSite;//检测地点
    private long detectionTime;//检测/校准日期
    private Integer isQualified;//是否合格 0：合格，1：不合格
    private Integer validityTerm;//有效期 单位为年

    public Long getToolId() {
        return toolId;
    }

    public void setToolId(Long toolId) {
        this.toolId = toolId;
    }

    public String getToolName() {
        return toolName;
    }

    public void setToolName(String toolName) {
        this.toolName = toolName;
    }

    public String getToolModel() {
        return toolModel;
    }

    public void setToolModel(String toolModel) {
        this.toolModel = toolModel;
    }

    public User.CustomerBean getCustomer() {
        return customer;
    }

    public void setCustomer(User.CustomerBean customer) {
        this.customer = customer;
    }

    public String getToolNumber() {
        return toolNumber;
    }

    public void setToolNumber(String toolNumber) {
        this.toolNumber = toolNumber;
    }

    public String getToolType() {
        return toolType;
    }

    public void setToolType(String toolType) {
        this.toolType = toolType;
    }

    public long getBuyTime() {
        return buyTime;
    }

    public void setBuyTime(long buyTime) {
        this.buyTime = buyTime;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public Integer getToolCount() {
        return toolCount;
    }

    public void setToolCount(Integer toolCount) {
        this.toolCount = toolCount;
    }

    public float getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(float unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getToolPic() {
        return toolPic;
    }

    public void setToolPic(String toolPic) {
        this.toolPic = toolPic;
    }

    public String getIsUse() {
        return isUse;
    }

    public void setIsUse(String isUse) {
        this.isUse = isUse;
    }

    public String getToolStatus() {
        return toolStatus;
    }

    public void setToolStatus(String toolStatus) {
        this.toolStatus = toolStatus;
    }

    public User getCreateUser() {
        return createUser;
    }

    public void setCreateUser(User createUser) {
        this.createUser = createUser;
    }

    public String getToolDesc() {
        return toolDesc;
    }

    public void setToolDesc(String toolDesc) {
        this.toolDesc = toolDesc;
    }

    public String getUseUser() {
        return useUser;
    }

    public void setUseUser(String useUser) {
        this.useUser = useUser;
    }

    public String getDepositaryPlace() {
        return depositaryPlace;
    }

    public void setDepositaryPlace(String depositaryPlace) {
        this.depositaryPlace = depositaryPlace;
    }

    public ToolsLog getToolsLog() {
        return toolsLog;
    }

    public void setToolsLog(ToolsLog toolsLog) {
        this.toolsLog = toolsLog;
    }

    public String getToolCertificateNo() {
        return toolCertificateNo;
    }

    public void setToolCertificateNo(String toolCertificateNo) {
        this.toolCertificateNo = toolCertificateNo;
    }

    public String getToolDetectionType() {
        return toolDetectionType;
    }

    public void setToolDetectionType(String toolDetectionType) {
        this.toolDetectionType = toolDetectionType;
    }

    public String getToolDetectionSite() {
        return toolDetectionSite;
    }

    public void setToolDetectionSite(String toolDetectionSite) {
        this.toolDetectionSite = toolDetectionSite;
    }

    public long getDetectionTime() {
        return detectionTime;
    }

    public void setDetectionTime(long detectionTime) {
        this.detectionTime = detectionTime;
    }

    public Integer getIsQualified() {
        return isQualified;
    }

    public void setIsQualified(Integer isQualified) {
        this.isQualified = isQualified;
    }

    public Integer getValidityTerm() {
        return validityTerm;
    }

    public void setValidityTerm(Integer validityTerm) {
        this.validityTerm = validityTerm;
    }

    public Tools() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.toolId);
        dest.writeString(this.toolName);
        dest.writeString(this.toolModel);
        dest.writeParcelable(this.customer, flags);
        dest.writeString(this.toolNumber);
        dest.writeString(this.toolType);
        dest.writeLong(this.buyTime);
        dest.writeString(this.manufacturer);
        dest.writeValue(this.toolCount);
        dest.writeFloat(this.unitPrice);
        dest.writeString(this.toolPic);
        dest.writeString(this.isUse);
        dest.writeString(this.toolStatus);
        dest.writeParcelable(this.createUser, flags);
        dest.writeString(this.toolDesc);
        dest.writeString(this.useUser);
        dest.writeString(this.depositaryPlace);
        dest.writeParcelable(this.toolsLog, flags);
        dest.writeString(this.toolCertificateNo);
        dest.writeString(this.toolDetectionType);
        dest.writeString(this.toolDetectionSite);
        dest.writeLong(this.detectionTime);
        dest.writeValue(this.isQualified);
        dest.writeValue(this.validityTerm);
    }

    protected Tools(Parcel in) {
        this.toolId = (Long) in.readValue(Long.class.getClassLoader());
        this.toolName = in.readString();
        this.toolModel = in.readString();
        this.customer = in.readParcelable(User.CustomerBean.class.getClassLoader());
        this.toolNumber = in.readString();
        this.toolType = in.readString();
        this.buyTime = in.readLong();
        this.manufacturer = in.readString();
        this.toolCount = (Integer) in.readValue(Integer.class.getClassLoader());
        this.unitPrice = in.readFloat();
        this.toolPic = in.readString();
        this.isUse = in.readString();
        this.toolStatus = in.readString();
        this.createUser = in.readParcelable(User.class.getClassLoader());
        this.toolDesc = in.readString();
        this.useUser = in.readString();
        this.depositaryPlace = in.readString();
        this.toolsLog = in.readParcelable(ToolsLog.class.getClassLoader());
        this.toolCertificateNo = in.readString();
        this.toolDetectionType = in.readString();
        this.toolDetectionSite = in.readString();
        this.detectionTime = in.readLong();
        this.isQualified = (Integer) in.readValue(Integer.class.getClassLoader());
        this.validityTerm = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Creator<Tools> CREATOR = new Creator<Tools>() {
        @Override
        public Tools createFromParcel(Parcel source) {
            return new Tools(source);
        }

        @Override
        public Tools[] newArray(int size) {
            return new Tools[size];
        }
    };
}
