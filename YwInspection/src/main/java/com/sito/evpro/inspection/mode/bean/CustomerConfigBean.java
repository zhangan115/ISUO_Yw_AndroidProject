package com.sito.evpro.inspection.mode.bean;

/**
 * Created by zhangan on 2018/1/18.
 */

public class CustomerConfigBean {

    /**
     * configCode : startType
     * configDesc : 无要求
     * configId : 1
     * configName : 巡检流程开始方式
     * configValue : 0
     */

    private String configCode;
    private String configDesc;
    private long configId;
    private String configName;
    private String configValue;

    public String getConfigCode() {
        return configCode;
    }

    public void setConfigCode(String configCode) {
        this.configCode = configCode;
    }

    public String getConfigDesc() {
        return configDesc;
    }

    public void setConfigDesc(String configDesc) {
        this.configDesc = configDesc;
    }

    public long getConfigId() {
        return configId;
    }

    public void setConfigId(long configId) {
        this.configId = configId;
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }
}
