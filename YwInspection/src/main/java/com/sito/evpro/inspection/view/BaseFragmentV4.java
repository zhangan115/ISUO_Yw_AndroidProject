package com.sito.evpro.inspection.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.afollestad.materialdialogs.MaterialDialog;
import com.sito.evpro.inspection.R;
import com.sito.evpro.inspection.app.InspectionApp;
import com.sito.library.base.AbsBaseFragmentV4;

/**
 * Created by zhangan on 2017-06-22.
 */

public class BaseFragmentV4 extends AbsBaseFragmentV4 implements BaseActivity.OnToolbarClickListener, DialogInterface.OnCancelListener {
    public String TAG = this.getClass().getSimpleName();
    protected BaseActivity baseActivity;

    public InspectionApp getApp() {
        return (InspectionApp) getActivity().getApplication();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BaseActivity) {
            baseActivity = (BaseActivity) context;
            baseActivity.setOnToolbarClickListener(this);
        }
    }

    protected MaterialDialog loadingDialog = null;

    public Dialog showProgressDialog() {
        return showProgressDialog("加载中...");
    }

    public void hideProgressDialog() {
        if (loadingDialog != null) {
            loadingDialog.hide();
        }
    }

    public MaterialDialog showProgressDialog(String message) {
        return showProgressDialog(message, true);
    }

    public MaterialDialog showProgressDialog(String message, boolean cancel) {
        if (loadingDialog == null) {
            loadingDialog = new MaterialDialog.Builder(getActivity())
                    .content(message)
                    .progress(true, 0)
                    .cancelable(cancel)
                    .cancelListener(this)
                    .progressIndeterminateStyle(false).build();
        }
        loadingDialog.show();
        return loadingDialog;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        onCancel();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
        if (evLoading != null && evLoading.isShowing()) {
            evLoading.dismiss();
        }
        onCancel();
    }

    protected void onCancel() {

    }

    @Override
    public void onToolBarBackClick() {

    }
}
