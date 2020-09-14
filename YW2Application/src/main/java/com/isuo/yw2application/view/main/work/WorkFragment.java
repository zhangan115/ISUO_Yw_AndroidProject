package com.isuo.yw2application.view.main.work;

import android.os.Bundle;

import com.isuo.yw2application.view.base.BaseFragmentV4;

public class WorkFragment extends BaseFragmentV4 {

    public static WorkFragment newInstance() {
        Bundle args = new Bundle();
        WorkFragment fragment = new WorkFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
