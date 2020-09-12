package com.isuo.yw2application.view.test;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.isuo.yw2application.R;
import com.isuo.yw2application.api.Api;
import com.isuo.yw2application.app.Yw2Application;
import com.isuo.yw2application.view.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * 测试界面</br>
 * 修改IP<p>
 * Created by zhangan on 2017/11/8.
 */

public class TestActivity extends BaseActivity {

    private static final String HOST_TEST = "https://www.ewaypro.cn/euvtest/api";//测试版
    private static final String HOST = "http://172.16.40.240:8080/sitopeuv/api";//正式版
    private TextView hostTv, hostTestTv, host_240;
    private List<String> hostList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutAndToolbar(R.layout.layout_text, "设置IP");
        initData();
        hostTv = (TextView) findViewById(R.id.tv_host_1);
        String url = Yw2Application.getInstance().AppHost();
        String str = "当前请求IP为";
        switch (url) {
            case HOST:
                str = str + "正式版";
                break;
            case HOST_TEST:
                str = str + "测试版";
                break;
        }
        hostTv.setText(str);
    }

    private void initData() {
        hostList = new ArrayList<>();
        hostList.add(HOST);
        hostList.add(HOST_TEST);
    }

    public void onChooseIp(View view) {
        List<String> strings = new ArrayList<>();
        strings.add("正式版");
        strings.add("测试版");
        new MaterialDialog.Builder(this)
                .items(strings)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        Yw2Application.getInstance().editHost(hostList.get(position));
                        Yw2Application.getInstance().showToast(getString(R.string.please_waite));
                        final Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
                        if (intent != null) {
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            Observable.just(null).delaySubscription(2, TimeUnit.SECONDS)
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Action1<Object>() {
                                        @Override
                                        public void call(Object o) {
                                            finish();
                                            Api.clean();
                                            startActivity(intent);
                                        }
                                    });
                        }
                    }
                })
                .show();
    }
}
