package com.isuo.yw2application.view.test;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutAndToolbar(R.layout.layout_text, "设置IP");
        final EditText et = findViewById(R.id.edit);
        et.setText(Yw2Application.getInstance().AppHost());
        findViewById(R.id.sureBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String host = et.getText().toString();
                Yw2Application.getInstance().editHost(host);
                Yw2Application.getInstance().showToast(getString(R.string.please_waite));
                Api.clean();
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
        });
    }
}
