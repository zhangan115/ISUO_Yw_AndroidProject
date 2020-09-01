package com.isuo.yw2application.mode.bean.equip;

import java.util.List;

/**
 * Created by zhangan on 2017/10/13.
 */

public class EquipRecordDetail {

    private long createTime;
    private EquipmentBean equipment;
    private long equipmentRecordId;
    private String recordContent;
    private String recordName;
    private String staffName;
    private int type;
    private List<RecordAppendicesBean> recordAppendices;
    private List<RecordImagesBean> recordImages;

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public EquipmentBean getEquipment() {
        return equipment;
    }

    public void setEquipment(EquipmentBean equipment) {
        this.equipment = equipment;
    }

    public long getEquipmentRecordId() {
        return equipmentRecordId;
    }

    public void setEquipmentRecordId(long equipmentRecordId) {
        this.equipmentRecordId = equipmentRecordId;
    }

    public String getRecordContent() {
        return recordContent;
    }

    public void setRecordContent(String recordContent) {
        this.recordContent = recordContent;
    }

    public String getRecordName() {
        return recordName;
    }

    public void setRecordName(String recordName) {
        this.recordName = recordName;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<RecordAppendicesBean> getRecordAppendices() {
        return recordAppendices;
    }

    public void setRecordAppendices(List<RecordAppendicesBean> recordAppendices) {
        this.recordAppendices = recordAppendices;
    }

    public List<RecordImagesBean> getRecordImages() {
        return recordImages;
    }

    public void setRecordImages(List<RecordImagesBean> recordImages) {
        this.recordImages = recordImages;
    }


    public static class RecordAppendicesBean {


        private int fileType;
        private String fileUrl;
        private String fileName;

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public int getFileType() {
            return fileType;
        }

        public void setFileType(int fileType) {
            this.fileType = fileType;
        }

        public String getFileUrl() {
            return fileUrl;
        }

        public void setFileUrl(String fileUrl) {
            this.fileUrl = fileUrl;
        }
    }

    public static class RecordImagesBean {
        /**
         * fileType : 0
         * fileUrl : 22
         * id : 17
         */

        private int fileType;
        private String fileUrl;
        private String fileName;

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }


        public int getFileType() {
            return fileType;
        }

        public void setFileType(int fileType) {
            this.fileType = fileType;
        }

        public String getFileUrl() {
            return fileUrl;
        }

        public void setFileUrl(String fileUrl) {
            this.fileUrl = fileUrl;
        }
    }
}
