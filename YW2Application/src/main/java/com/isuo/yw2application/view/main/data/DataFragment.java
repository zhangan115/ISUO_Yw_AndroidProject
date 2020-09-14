package com.isuo.yw2application.view.main.data;

import android.os.Bundle;

import com.isuo.yw2application.view.base.BaseFragmentV4;

public class DataFragment extends BaseFragmentV4 {

    public static DataFragment newInstance() {
        Bundle args = new Bundle();
        DataFragment fragment = new DataFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
