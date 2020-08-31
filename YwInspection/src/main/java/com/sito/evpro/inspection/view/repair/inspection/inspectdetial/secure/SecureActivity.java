package com.sito.evpro.inspection.view.repair.inspection.inspectdetial.secure;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.sito.evpro.inspection.R;
import com.sito.evpro.inspection.app.InspectionApp;
import com.sito.evpro.inspection.common.ConstantStr;
import com.sito.evpro.inspection.mode.bean.inspection.SecureBean;
import com.sito.evpro.inspection.view.WebActivity;
import com.sito.evpro.inspection.view.repair.inspection.detail.InspectionDetailActivity;
import com.sito.library.utils.SPHelper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


public class SecureActivity extends WebActivity implements View.OnClickListener, SecureContract.View {
    private WebView mContent;
    private TextView mPrvious;//上一页
    private TextView mNext;//下一页
    @Inject
    SecurePresenter mSecurePresenter;
    SecureContract.Presenter mPresenter;
    private long mSecurityId;
    private String mTaskId;
    private String mContentStr;
    private int mPosition;
    private int prvious;
    private int next;
    private List<SecureBean.PageListBean> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutAndToolbar(R.layout.activity_secure, "管理规定");
        DaggerSecureComponent.builder().inspectionRepositoryComponent(InspectionApp.getInstance().getRepositoryComponent())
                .secureModule(new SecureModule(this)).build()
                .inject(this);
        mSecurityId = getIntent().getLongExtra(ConstantStr.KEY_BUNDLE_INT, -1);
        mTaskId = getIntent().getStringExtra(ConstantStr.KEY_BUNDLE_STR);
        mPresenter.getSecureInfo(mSecurityId);
        mList = new ArrayList<>();
        initView();
        initEvent();
    }

    private void initEvent() {
        mNext.setOnClickListener(this);
        mPrvious.setOnClickListener(this);
    }

    private void initView() {
        mContent = (WebView) findViewById(R.id.id_secure_content);
        mPrvious = (TextView) findViewById(R.id.id_previous_page);
        mNext = (TextView) findViewById(R.id.id_next_page);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.id_previous_page:
                prvious = mPosition;
                mNext.setText("下一页");
                if (prvious > 0) {
                    --prvious;
                    showWeb(mContent, mList.get(prvious).getPageContent());
                    mPosition = prvious;
                    if (prvious == 0) {
                        mPrvious.setVisibility(View.GONE);
                    }
                }
                break;
            case R.id.id_next_page:
                next = mPosition;
                if (next < mList.size() - 1) {
                    mPrvious.setVisibility(View.VISIBLE);
                    mPrvious.setText("上一页");
                    ++next;
                    showWeb(mContent, mList.get(next).getPageContent());
                    mPosition = next;
                    if (next == mList.size() - 1) {
                        mNext.setText("确定");
                    }
                } else {
                    startRoomList();
                }
                break;
        }
    }

    private void startRoomList() {
        SPHelper.write(this, ConstantStr.SECURIT_INFO, mTaskId, true);
        Intent intent = new Intent(this, InspectionDetailActivity.class);
        intent.putExtra(ConstantStr.KEY_BUNDLE_STR, mTaskId);
        intent.putExtra(ConstantStr.KEY_BUNDLE_INT, mSecurityId);
        startActivity(intent);
        finish();
    }

    @Override
    public void showData(SecureBean secureBean) {
        if (secureBean != null && secureBean.getPageList() != null && secureBean.getPageList().size() > 0) {
            next = 0;
            prvious = 0;
            mList.clear();
            mList.addAll(secureBean.getPageList());
            mContentStr = mList.get(next).getPageContent();
            showWeb(mContent, mContentStr);
            mPrvious.setVisibility(View.GONE);
            if (mList != null && mList.size() == 1) {
                mNext.setText("确定");
            }
        }
    }

    @Override
    public void showLoading() {
        showEvLoading();
    }

    @Override
    public void hideLoading() {
        hideEvLoading();
    }

    @Override
    public void noData() {
        startRoomList();
    }

    @Override
    public void setPresenter(SecureContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
