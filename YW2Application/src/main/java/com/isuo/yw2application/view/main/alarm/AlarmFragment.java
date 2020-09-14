package com.isuo.yw2application.view.main.alarm;

import android.os.Bundle;

import com.isuo.yw2application.view.base.BaseFragmentV4;

public class AlarmFragment extends BaseFragmentV4 {

    public static AlarmFragment newInstance() {
        Bundle args = new Bundle();
        AlarmFragment fragment = new AlarmFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
