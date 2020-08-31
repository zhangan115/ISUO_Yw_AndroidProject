package com.sito.customer.widget;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sito.customer.R;

/**
 * 文件item
 * Created by zhangan on 2018/4/11.
 */

public class FileItemLayout extends LinearLayout {

    public FileItemLayout(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.item_file_list, this);
    }

    public void setData(String fileName, int position, OnClickListener clickListener) {
        TextView name = findViewById(R.id.tvFileName);
        name.setText(fileName);
        findViewById(R.id.ivDelIcon).setTag(position);
        findViewById(R.id.ivDelIcon).setOnClickListener(clickListener);
    }
}
