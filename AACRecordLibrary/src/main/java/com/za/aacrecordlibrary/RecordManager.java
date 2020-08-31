package com.za.aacrecordlibrary;

import android.os.Bundle;

import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by pingan on 2017/9/20.
 */

public class RecordManager implements RecognizerListener {

    private SpeechRecognizer speechRecognizer;
    private String fileName;
    private HashMap<String, String> hashMap;
    private RecordListener recordListener;
    private AACRecord aac;
    private boolean isCancel;
    private String voiceFile;
    private long startTime;

    public RecordManager setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }


    public RecordManager setSpeechRecognizer(SpeechRecognizer speechRecognizer) {
        this.speechRecognizer = speechRecognizer;
        return this;
    }

    /**
     * @param recordListener 设置监听事件
     * @return RecordManager
     */
    public RecordManager setRecordListener(RecordListener recordListener) {
        this.recordListener = recordListener;
        return this;
    }

    public void start() {
        isCancel = false;
        hashMap = new LinkedHashMap<>();
        voiceFile = fileName + File.separator + System.currentTimeMillis() + ".aac";
        aac = new AACRecord(voiceFile);
        aac.sampleRateInHz(16000);
        startTime = System.currentTimeMillis();
        aac.start(new AACRecord.IRecordListener() {
            @Override
            public void onRecord(byte[] bytes, int offset, int size) {
                if (speechRecognizer != null && speechRecognizer.isListening()) {
                    speechRecognizer.writeAudio(bytes, offset, size);
                }
            }
        });
        if (speechRecognizer != null) {
            speechRecognizer.startListening(this);
        }
    }

    public void stop() {
        if (aac != null) {
            aac.stop();
            aac = null;
        }
        if (speechRecognizer != null) {
            speechRecognizer.stopListening();
        }
    }

    public void onCancel() {
        isCancel = true;
        if (aac != null) {
            aac.stop();
            aac = null;
        }
        if (speechRecognizer != null) {
            speechRecognizer.stopListening();
        }
        File file = new File(voiceFile);
        if (file.exists()) {
            file.delete();
        }
    }

    @Override
    public void onVolumeChanged(int i, byte[] bytes) {

    }

    @Override
    public void onBeginOfSpeech() {

    }

    @Override
    public void onEndOfSpeech() {

    }

    @Override
    public void onResult(RecognizerResult recognizerResult, boolean isLast) {
        saveResult(recognizerResult);
        if (isLast) {
            StringBuilder resultBuffer = new StringBuilder();
            for (String key : hashMap.keySet()) {
                resultBuffer.append(hashMap.get(key));
            }
            if (speechRecognizer != null) {
                speechRecognizer.cancel();
                speechRecognizer.destroy();
            }
            if (recordListener != null && !isCancel) {
                int time = (int) (System.currentTimeMillis() - startTime) / 1000;
                recordListener.onSpeechFinish(time, resultBuffer.toString(), voiceFile);
            }
        }
    }

    @Override
    public void onError(SpeechError speechError) {
        if (recordListener != null) {
            recordListener.onSpeechRecognizerError(speechError.getMessage());
        }
    }

    @Override
    public void onEvent(int i, int i1, int i2, Bundle bundle) {

    }

    private void saveResult(RecognizerResult results) {
        String text = JsonParser.parseIatResult(results.getResultString());
        String sn = null;
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        hashMap.put(sn, text);
    }


    public interface RecordListener {

        /**
         * @param errorMessage 出错信息
         */
        void onSpeechRecognizerError(String errorMessage);

        /**
         * @param content
         * @param voiceFile
         */
        void onSpeechFinish(int time, String content, String voiceFile);

    }
}
