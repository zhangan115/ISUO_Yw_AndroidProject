package com.isuo.yw2application.view.main.device;

import android.os.Bundle;

import com.isuo.yw2application.view.base.BaseFragmentV4;

public class DeviceFragment extends BaseFragmentV4 {

    public static DeviceFragment newInstance() {
        Bundle args = new Bundle();
        DeviceFragment fragment = new DeviceFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
