package com.isuo.yw2application.view.main.feedback;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.isuo.yw2application.R;
import com.isuo.yw2application.app.Yw2Application;
import com.isuo.yw2application.view.base.BaseActivity;

import javax.inject.Inject;

public class QuestionActivity extends BaseActivity implements QuestionContract.View {
    private EditText mTitle;
    private EditText mContent;
    @Inject
    QuestionPresenter presenter;
    QuestionContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutAndToolbar(R.layout.activity_question, "意见反馈");
        DaggerQuestionComponent.builder()
                .customerRepositoryComponent(Yw2Application.getInstance().getRepositoryComponent())
                .questionModule(new QuestionModule(this)).build().inject(this);
        initViews();
    }

    private void initViews() {
        mTitle = findViewById(R.id.id_question_title);
        mContent = findViewById(R.id.id_question_content);
        Button mButton = findViewById(R.id.id_question_btn);
        mButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mContent.getText().toString())) {
                    Yw2Application.getInstance().showToast("请输入内容");
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
        Yw2Application.getInstance().showToast("提交成功！");
        finish();
    }

    @Override
    public void postFail() {
        Yw2Application.getInstance().showToast("提交失败了！");
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
