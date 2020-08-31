package com.sito.customer.view.home.discover;

import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.WebView;

import com.sito.customer.R;
import com.sito.customer.common.ConstantStr;
import com.sito.customer.view.WebActivity;

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
        WebView tvContent = (WebView) findViewById(R.id.id_stand_info);
        if (content.startsWith("http")) {
            showWebUrl(tvContent, content);
        } else {
            showWeb(tvContent, content);
        }
    }

}
