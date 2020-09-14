package com.isuo.yw2application.view.main.task;

import android.os.Bundle;

import com.isuo.yw2application.view.base.BaseFragmentV4;

public class TaskFragment extends BaseFragmentV4 {

    public static TaskFragment newInstance() {
        Bundle args = new Bundle();
        TaskFragment fragment = new TaskFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
