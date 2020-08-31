package com.sito.evpro.inspection.view.setting.feedback;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.sito.evpro.inspection.R;
import com.sito.evpro.inspection.app.InspectionApp;
import com.sito.evpro.inspection.view.BaseActivity;

import javax.inject.Inject;

public class QuestionActivity extends BaseActivity implements QuestionContract.View {
    private EditText mTitle;
    private EditText mContent;
    private Button mButton;
    @Inject
    QuestionPresenter presenter;
    QuestionContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutAndToolbar(R.layout.activity_question, "意见反馈");
        DaggerQuestionComponent.builder().inspectionRepositoryComponent(InspectionApp.getInstance().getRepositoryComponent())
                .questionModule(new QuestionModule(this)).build().inject(this);
        initViews();
    }

    private void initViews() {
        mTitle = (EditText) findViewById(R.id.id_question_title);
        mContent = (EditText) findViewById(R.id.id_question_content);
        mButton = (Button) findViewById(R.id.id_question_btn);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mContent.getText().toString())) {
                    InspectionApp.getInstance().showToast("请输入内容");
                    return;
                }
                if (mPresenter != null) {
                    mPresenter.postQuestion(mTitle.getText().toString(), mContent.getText().toString());
                }
            }
        });
    }

    @Override
    public void postSuccess() {
        InspectionApp.getInstance().showToast("提交成功！");
        finish();
    }

    @Override
    public void postFail() {
        InspectionApp.getInstance().showToast("提交失败了！");
    }

    @Override
    public void postFinish() {

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mPresenter != null) {
            mPresenter.unSubscribe();
        }
    }

    @Override
    public void showLoading() {
        showProgressDialog("提交中...");
    }

    @Override
    public void hideLoading() {
        hideProgressDialog();
    }

    @Override
    public void setPresenter(QuestionContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
