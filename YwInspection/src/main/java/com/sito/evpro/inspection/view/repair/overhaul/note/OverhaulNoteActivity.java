package com.sito.evpro.inspection.view.repair.overhaul.note;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.sito.evpro.inspection.R;
import com.sito.evpro.inspection.app.InspectionApp;
import com.sito.evpro.inspection.common.ConstantStr;
import com.sito.evpro.inspection.mode.bean.overhaul.OverhaulNoteBean;
import com.sito.evpro.inspection.view.WebActivity;
import com.sito.evpro.inspection.view.repair.overhaul.work.OverhaulWorkActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 工作包展示
 * Created by zhangan on 2017/8/24.
 */

public class OverhaulNoteActivity extends WebActivity implements OverhaulContract.View {

    private String repairId, jobId;
    private OverhaulContract.Presenter mPresenter;
    private int mPosition;
    private int previous;
    private int next;
    private WebView mContent;
    private TextView mPrevious;
    private TextView mNext;
    private List<OverhaulNoteBean.PageListBean> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        repairId = getIntent().getStringExtra(ConstantStr.KEY_BUNDLE_STR);
        jobId = getIntent().getStringExtra(ConstantStr.KEY_BUNDLE_STR_1);
        mList = new ArrayList<>();
        new OverhaulNotePresenter(InspectionApp.getInstance().getRepositoryComponent().getRepository(), this);
        mPresenter.getRepairState(repairId, jobId);
    }

    @Override
    public void showLayout() {
        setLayoutAndToolbar(R.layout.activity_secure, "管理规定");
        mContent = (WebView) findViewById(R.id.id_secure_content);
        mPrevious = (TextView) findViewById(R.id.id_previous_page);
        mNext = (TextView) findViewById(R.id.id_next_page);
        mNext.setOnClickListener(this);
        mPrevious.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_previous_page:
                previous = mPosition;
                mNext.setText("下一页");
                if (previous > 0) {
                    --previous;
                    showWeb(mContent, mList.get(previous).getPageContent());
                    mPosition = previous;
                    if (previous == 0) {
                        mPrevious.setVisibility(View.GONE);
                    }
                }
                break;
            case R.id.id_next_page:
                next = mPosition;
                if (mList.size() > 0 && next < mList.size() - 1) {
                    mPrevious.setVisibility(View.VISIBLE);
                    mPrevious.setText("上一页");
                    ++next;
                    showWeb(mContent, mList.get(next).getPageContent());
                    mPosition = next;
                    if (next == mList.size() - 1) {
                        mNext.setText("确定");
                    }
                } else {
                    mPresenter.setRepairState(repairId, jobId);
                }
                break;
        }
    }


    private void startWork() {
        Intent intent = new Intent(this, OverhaulWorkActivity.class);
        intent.putExtra(ConstantStr.KEY_BUNDLE_STR, repairId);
        startActivity(intent);
        finish();
    }

    @Override
    public void showOverhaulWork() {
        startWork();
    }

    @Override
    public void showOverhaulNote(OverhaulNoteBean overhaulNoteBean) {
        if (overhaulNoteBean != null && overhaulNoteBean.getPageList() != null && overhaulNoteBean.getPageList().size() > 0) {
            next = 0;
            previous = 0;
            mList.clear();
            mList.addAll(overhaulNoteBean.getPageList());
            String mContentStr = mList.get(next).getPageContent();
            showWeb(mContent, mContentStr);
            mPrevious.setVisibility(View.GONE);
            if (mList != null && mList.size() == 1) {
                mNext.setText("确定");
            }
        }
    }

    @Override
    public void noData() {
        startWork();
    }

    @Override
    public void showLoading() {
        showEvLoading();
    }

    @Override
    public void hidLoading() {
        hideEvLoading();
    }


    @Override
    public void setPresenter(OverhaulContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
