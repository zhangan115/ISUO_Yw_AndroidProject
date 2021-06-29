package com.isuo.yw2application.view.main.work.enterprise;

import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.isuo.yw2application.R;
import com.isuo.yw2application.app.Yw2Application;
import com.isuo.yw2application.common.ConstantStr;
import com.isuo.yw2application.mode.bean.news.EnterpriseDetailBean;
import com.isuo.yw2application.view.base.BaseActivity;
import com.sito.library.utils.DataUtil;

import java.io.File;

/**
 * 企业通知详情
 * Created by zhangan on 2018/4/23.
 */

public class EnterpriseActivity extends BaseActivity implements EnterpriseContract.View {

    private EnterpriseContract.Presenter mPresenter;
    private TextView tvFile;
    private RelativeLayout noDataLayout;
    private LinearLayout llContent;
    private static final String DOWNLOAD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/EvPro/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        long messageId = getIntent().getLongExtra(ConstantStr.KEY_BUNDLE_LONG, -1);
        if (messageId == -1) {
            finish();
            return;
        }
        setLayoutAndToolbar(R.layout.message_enterprise, "通知详情");
        new EnterprisePresenter(Yw2Application.getInstance().getRepositoryComponent().getRepository(), this);
        tvFile = (TextView) findViewById(R.id.tvFile);
        noDataLayout = (RelativeLayout) findViewById(R.id.layout_no_data);
        llContent = (LinearLayout) findViewById(R.id.llContent);
        llContent.setVisibility(View.GONE);
        tvFile.setOnClickListener(this);
        tvFile.setVisibility(View.GONE);
        mPresenter.getMessageDetail(messageId);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tvFile) {
            String url = (String) v.getTag();
            String fileName = (String) v.getTag(R.id.tag_position);
            if (!TextUtils.isEmpty(url)) {
                mPresenter.downLoadFile(url, DOWNLOAD_PATH, fileName);
            }
        }
    }

    @Override
    public void setPresenter(EnterpriseContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    protected void onCancel() {
        super.onCancel();
        mPresenter.unSubscribe();
    }

    @Override
    public void showEnterpriseDetailBean(EnterpriseDetailBean bean) {
        llContent.setVisibility(View.VISIBLE);
        noDataLayout.setVisibility(View.GONE);
        ((TextView) (findViewById(R.id.tvMessageTitle))).setText(bean.getTitle());
        ((TextView) (findViewById(R.id.tvContent))).setText(bean.getContent());
        ((TextView) (findViewById(R.id.tvTime))).setText(DataUtil.timeFormat(bean.getCreateTime(), null));
        if (TextUtils.isEmpty(bean.getAppendicesUrl())) {
            tvFile.setVisibility(View.GONE);
        } else {
            tvFile.setVisibility(View.VISIBLE);
            int position = bean.getAppendicesUrl().lastIndexOf(File.separator);
            String fileName = bean.getAppendicesUrl().substring(position + 1);
            tvFile.setText(fileName);
            tvFile.setTag(bean.getAppendicesUrl());
            tvFile.setTag(R.id.tag_position, fileName);
        }
    }

    @Override
    public void noData() {
        noDataLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void downLoadSuccess() {
        Yw2Application.getInstance().showToast("下载成功");
    }

    @Override
    public void showDownLoading() {
        showProgressDialog("下载中...");
    }

    @Override
    public void hideDownLoading() {
        hideProgressDialog();
    }
}
