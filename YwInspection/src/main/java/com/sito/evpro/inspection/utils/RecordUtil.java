package com.sito.evpro.inspection.utils;

import com.sito.evpro.inspection.app.InspectionApp;
import com.za.aacrecordlibrary.AACRecord;

import java.io.File;


/**
 * Created by Yangzb on 2017/7/26 10:21
 * E-mail：yangzongbin@si-top.com
 */
public class RecordUtil {
    private AACRecord aac;
    private String fileName;
    private int sampleRateInHz = 16000;

    public RecordUtil(String name) {
        this.fileName = name;
    }

    //开始录音
    public void startRecord() {
        String path = InspectionApp.getInstance().voiceCacheFile() + File.separator + fileName + ".aac";
        aac = new AACRecord(path);
        aac.sampleRateInHz(sampleRateInHz);
        aac.start();
    }

    //开始录音
    public void startRecord(AACRecord.IRecordListener iRecordListener) {
        String path = InspectionApp.getInstance().voiceCacheFile() + File.separator + fileName + ".aac";
        aac = new AACRecord(path);
        aac.sampleRateInHz(sampleRateInHz);
        aac.start(iRecordListener);
    }

    //停止录音
    public void stopRecord() {
        if (aac != null) {
            aac.stop();
            aac = null;
        }
    }

}
