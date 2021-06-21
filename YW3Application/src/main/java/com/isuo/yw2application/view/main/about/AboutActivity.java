package com.isuo.yw2application.view.main.about;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.isuo.yw2application.R;
import com.isuo.yw2application.view.base.BaseActivity;


/**
 * 关于
 */
public class AboutActivity extends BaseActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutAndToolbar(R.layout.about, "关于我们");
        findViewById(R.id.tv_content).setOnClickListener(this);
        TextView mTel = findViewById(R.id.mine_tel);
        mTel.setOnClickListener(this);
        mTel.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
        mTel.getPaint().setAntiAlias(true);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.mine_tel) {
            call();
        }
    }
    private void call() {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "029-85792830"));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}

