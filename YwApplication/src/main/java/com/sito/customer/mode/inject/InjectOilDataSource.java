package com.sito.customer.mode.inject;

import android.support.annotation.NonNull;

import com.sito.customer.mode.IListCallBack;
import com.sito.customer.mode.IObjectCallBack;
import com.sito.customer.mode.inject.bean.InjectEquipment;
import com.sito.customer.mode.inject.bean.InjectEquipmentLog;
import com.sito.customer.mode.inject.bean.InjectRoomBean;
import com.sito.customer.mode.inject.bean.InjectItemBean;

import org.json.JSONObject;

import java.util.Map;

import rx.Subscription;

/**
 * 注油
 * Created by zhangan on 2018/4/9.
 */

public interface InjectOilDataSource {

    /**
     * 获取注油区域
     *
     * @param callBack 回调
     * @return 订阅
     */
    @NonNull
    Subscription getInjectRoomList(@NonNull IListCallBack<InjectRoomBean> callBack);

    /**
     * 获取注油设备
     *
     * @param roomId   区域id
     * @param callBack 回调
     * @return 订阅
     */
    @NonNull
    Subscription getInjectEquipmentList(long roomId, @NonNull IListCallBack<InjectEquipment> callBack);

    @NonNull
    Subscription getInjectEquipmentList(Map<String, String> map, @NonNull IListCallBack<InjectEquipment> callBack);

    @NonNull
    Subscription getInjectItem(@NonNull IObjectCallBack<InjectItemBean> callBack);

    @NonNull
    Subscription injectOilEquipment(JSONObject jsonObject, IObjectCallBack<String> iObjectCallBack);

    @NonNull
    Subscription getInjectEquipLogList(Map<String, String> map, @NonNull IListCallBack<InjectEquipmentLog.ItemList> callBack);
}
