package com.isuo.yw2application.wxapi;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.isuo.yw2application.app.Yw2Application;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

/**
 * 分享
 * Created by zhangan on 2017/8/30.
 */

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Yw2Application.iwxapi.handleIntent(getIntent(), this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        switch (baseResp.errCode) { //根据需要的情况进行处理
            case BaseResp.ErrCode.ERR_OK:
                //正确返回
                Log.d("za", "正确返回");
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                //用户取消
                Log.d("za", "用户取消");
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                //认证被否决
                Log.d("za", "认证被否决");
                break;
            case BaseResp.ErrCode.ERR_SENT_FAILED:
                //发送失败
                Log.d("za", "发送失败");
                break;
            case BaseResp.ErrCode.ERR_UNSUPPORT:
                Log.d("za", "不支持错误");
                break;
            case BaseResp.ErrCode.ERR_COMM:
                //一般错误
                Log.d("za", "一般错误");
                break;
            default:
                Log.d("za", "其他不可名状的情况");
                break;
        }
    }

}
