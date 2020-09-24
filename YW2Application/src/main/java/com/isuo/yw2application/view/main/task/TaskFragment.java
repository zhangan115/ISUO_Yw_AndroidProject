package com.isuo.yw2application.view.main.task;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.isuo.yw2application.R;
import com.isuo.yw2application.app.Yw2Application;
import com.isuo.yw2application.mode.bean.work.WorkState;
import com.isuo.yw2application.view.base.MvpFragmentV4;
import com.sito.library.utils.DisplayUtil;

public class TaskFragment extends MvpFragmentV4<TaskContract.Presenter> implements TaskContract.View, View.OnClickListener {

    private TextView finishCountTv, monthCountTv, weakCountTv, dayCountTv;

    public static TaskFragment newInstance() {
        Bundle args = new Bundle();
        TaskFragment fragment = new TaskFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new TaskPresenter(Yw2Application.getInstance().getWorkRepositoryComponent().getRepository(), this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_task, container, false);
        finishCountTv = rootView.findViewById(R.id.finishCountTv);
        monthCountTv = rootView.findViewById(R.id.monthCountTv);
        weakCountTv = rootView.findViewById(R.id.workCountTv);
        dayCountTv = rootView.findViewById(R.id.dayCountTv);
        int width = (getActivity().getResources().getDisplayMetrics().widthPixels - DisplayUtil.dip2px(getActivity(), 60)) / 2;
        int height = width / 142 * 90;
        rootView.findViewById(R.id.taskIv1).setLayoutParams(new LinearLayout.LayoutParams(width,height));
        rootView.findViewById(R.id.taskIv2).setLayoutParams(new LinearLayout.LayoutParams(width,height));
        rootView.findViewById(R.id.taskIv3).setLayoutParams(new LinearLayout.LayoutParams(width,height));
        rootView.findViewById(R.id.taskIv4).setLayoutParams(new LinearLayout.LayoutParams(width,height));
        rootView.findViewById(R.id.taskIv1).setOnClickListener(this);
        rootView.findViewById(R.id.taskIv2).setOnClickListener(this);
        rootView.findViewById(R.id.taskIv3).setOnClickListener(this);
        rootView.findViewById(R.id.taskIv4).setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPresenter != null) {
            mPresenter.getWorkCount();
            mPresenter.getFinishWorkCount();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.taskIv1:
                break;
            case R.id.taskIv2:
                break;
            case R.id.taskIv3:
                break;
            case R.id.taskIv4:
                break;
        }
    }

    @Override
    public void showWorkCount(WorkState workState) {
        monthCountTv.setText(String.format("%s/%s", workState.getMonthFinishCount(), workState.getMonthAllCount()));
        weakCountTv.setText(String.format("%s/%s", workState.getWeekFinishCount(), workState.getWeekAllCount()));
        dayCountTv.setText(String.format("%s/%s", workState.getDayFinishCount(), workState.getDayAllCount()));
    }

    @Override
    public void showFinishWorkCount(int count) {

    }

    @Override
    public void setPresenter(TaskContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
