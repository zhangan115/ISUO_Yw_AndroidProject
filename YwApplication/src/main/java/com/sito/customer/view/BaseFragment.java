package com.sito.customer.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.afollestad.materialdialogs.MaterialDialog;
import com.sito.customer.R;
import com.sito.customer.app.CustomerApp;
import com.sito.library.base.AbsBaseFragment;


/**
 * base fragment
 * Created by zhangan on 2017-02-23.
 */

public class BaseFragment extends AbsBaseFragment implements DialogInterface.OnCancelListener, BaseActivity.OnToolbarClickListener {

    public String TAG = this.getClass().getSimpleName();
    protected BaseActivity baseActivity;

    public CustomerApp getApp() {
        return (CustomerApp) getActivity().getApplication();
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
    protected AlertDialog evLoading = null;

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

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        getActivity().overridePendingTransition(R.anim.slideinright, R.anim.slideoutleft);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slideinright, R.anim.slideoutleft);
    }
}
