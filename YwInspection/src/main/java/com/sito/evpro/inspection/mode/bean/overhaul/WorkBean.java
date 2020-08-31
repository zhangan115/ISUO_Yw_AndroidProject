package com.sito.evpro.inspection.mode.bean.overhaul;

import com.sito.evpro.inspection.mode.bean.db.Image;
import com.sito.evpro.inspection.mode.bean.db.Voice;

import java.util.List;

/**
 * 维修工作封装bean
 * Created by zhangan on 2017-07-08.
 */

public class WorkBean {
    private List<Image> images;
    private Voice voice;
    private long repairId;
    private String repairResult;

    public WorkBean() {
    }

    public WorkBean(List<Image> images, Voice voicePath) {
        this.images = images;
        this.voice = voicePath;
    }

    public String getRepairResult() {
        return repairResult;
    }

    public void setRepairResult(String repairResult) {
        this.repairResult = repairResult;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public Voice getVoice() {
        return voice;
    }

    public void setVoice(Voice voice) {
        this.voice = voice;
    }

    public long getRepairId() {
        return repairId;
    }

    public void setRepairId(long repairId) {
        this.repairId = repairId;
    }
}
