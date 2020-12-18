package com.isuo.yw2application.view.main.data;

import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.WebView;

import com.isuo.yw2application.R;
import com.isuo.yw2application.common.ConstantStr;
import com.isuo.yw2application.view.base.WebActivity;

public class StandInfoActivity extends WebActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String title = getIntent().getStringExtra(ConstantStr.KEY_TITLE);
        String content = getIntent().getStringExtra(ConstantStr.KEY_BUNDLE_STR);
        if (TextUtils.isEmpty(content)) {
            finish();
            return;
        }
        setLayoutAndToolbar(R.layout.activity_stand_info, title);
        WebView tvContent = findViewById(R.id.id_stand_info);
        if (content.startsWith("http") || content.startsWith("www")) {
            showWebUrl(tvContent, content);
        } else {
            showWeb(tvContent, content);
        }
    }

}
