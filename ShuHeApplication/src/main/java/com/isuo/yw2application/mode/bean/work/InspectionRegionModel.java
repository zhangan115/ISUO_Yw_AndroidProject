package com.isuo.yw2application.mode.bean.work;

import java.util.ArrayList;
import java.util.List;

public class InspectionRegionModel {
    private String regionName;
    private List<InspectionBean> inspectionBeanList;


    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public List<InspectionBean> getInspectionBeanList() {
        if (inspectionBeanList == null) {
            return new ArrayList<>();
        }
        return inspectionBeanList;
    }

    public void setInspectionBeanList(List<InspectionBean> inspectionBeanList) {
        this.inspectionBeanList = inspectionBeanList;
    }

    public void addInspection(InspectionBean inspectionBean){
        if (this.inspectionBeanList ==null){
            inspectionBeanList = new ArrayList<>();
        }
        inspectionBeanList.add(inspectionBean);
    }

    @Override
    public String toString() {
        return "InspectionRegionModel{" +
                "regionName='" + regionName + '\'' +
                ", inspectionBean=" + inspectionBeanList +
                '}';
    }
}
