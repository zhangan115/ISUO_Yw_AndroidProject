package com.isuo.yw2application.view.main.task.overhaul.security;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.isuo.yw2application.R;
import com.isuo.yw2application.app.Yw2Application;
import com.isuo.yw2application.common.ConstantStr;
import com.isuo.yw2application.mode.bean.inspection.SecureBean;
import com.isuo.yw2application.mode.bean.overhaul.OverhaulNoteBean;
import com.isuo.yw2application.mode.overhaul.OverhaulRepository;
import com.isuo.yw2application.view.base.WebActivity;
import com.isuo.yw2application.view.main.task.overhaul.execute.OverhaulExecuteActivity;
import com.sito.library.utils.SPHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * 检修安全包
 * Created by zhangan on 2018/3/20.
 */

public class SecurityPackageActivity extends WebActivity implements SecurityPackageContract.View {

    private WebView mContent;
    private TextView mPrevious;//上一页
    private TextView mNext;//下一页
    private long mSecurityId;
    private long mTaskId;
    private int mPosition, previous, next;
    private List<SecureBean.PageListBean> mList;
    private SecurityPackageContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new SecurityPackagePresenter(OverhaulRepository.getRepository(this), this);
        setLayoutAndToolbar(R.layout.security_package_activity, "",true);
        mTaskId = getIntent().getLongExtra(ConstantStr.KEY_BUNDLE_LONG, -1);
        mSecurityId = getIntent().getLongExtra(ConstantStr.KEY_BUNDLE_LONG_1, -1);
        mContent = findViewById(R.id.id_secure_content);
        mPrevious = findViewById(R.id.id_previous_page);
        mNext = findViewById(R.id.id_next_page);
        mNext.setOnClickListener(this);
        mPrevious.setOnClickListener(this);
        mList = new ArrayList<>();
        mPresenter.getSecureInfo(mSecurityId);
    }

    @Override
    public void setPresenter(SecurityPackageContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showData(SecureBean secureBean) {
        if (secureBean != null && secureBean.getPageList() != null
                && secureBean.getPageList().size() > 0) {
            mTitleTv.setText(secureBean.getSecurityName());
            next = 0;
            previous = 0;
            mList.clear();
            mList.addAll(secureBean.getPageList());
            String mContentStr = mList.get(next).getPageContent();
            showWeb(mContent, mContentStr);
            mPrevious.setVisibility(View.GONE);
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
    public void showMessage(String message) {
        Yw2Application.getInstance().showToast(message);
    }

    @Override
    public void noData() {
        startRoomList();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
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
                if (next < mList.size() - 1) {
                    mPrevious.setVisibility(View.VISIBLE);
                    mPrevious.setText("上一页");
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
        SPHelper.write(this, ConstantStr.SECURITY_OVERHAUL_INFO, String.valueOf(mTaskId), true);
        Intent intent = new Intent(this, OverhaulExecuteActivity.class);
        intent.putExtra(ConstantStr.KEY_BUNDLE_LONG, mTaskId);
        intent.putExtra(ConstantStr.KEY_BUNDLE_LONG_1, mSecurityId);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCancel() {
        mPresenter.unSubscribe();
    }
}
