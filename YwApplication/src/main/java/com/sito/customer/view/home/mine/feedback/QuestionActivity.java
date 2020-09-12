package com.sito.customer.view.home.mine.feedback;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.sito.customer.R;
import com.sito.customer.app.CustomerApp;
import com.sito.customer.view.BaseActivity;

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
        DaggerQuestionComponent.builder()
                .customerRepositoryComponent(CustomerApp.getInstance().getRepositoryComponent())
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
                    CustomerApp.getInstance().showToast("请输入内容");
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
        CustomerApp.getInstance().showToast("提交成功！");
        finish();
    }

    @Override
    public void postFail() {
        CustomerApp.getInstance().showToast("提交失败了！");
    }

    @Override
    public void postFinish() {

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