package com.sito.evpro.inspection.view;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.sito.evpro.inspection.R;
import com.sito.evpro.inspection.app.AppStatusConstant;
import com.sito.evpro.inspection.app.AppStatusManager;
import com.sito.evpro.inspection.app.InspectionApp;
import com.sito.evpro.inspection.mode.bean.option.OptionBean;
import com.sito.evpro.inspection.view.home.HomeActivity;
import com.sito.library.base.AbsBaseActivity;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.logging.Logger;


/**
 * BaseActivity 页面基类
 * 类描述：页面基类
 *
 * @author zhuzhu
 * @version v0.1
 * @since 2015-04-13
 */
public abstract class BaseActivity extends AbsBaseActivity implements OnClickListener {
    public final String TAG = this.getClass().getSimpleName();
    protected boolean isDestroy = false;
    protected MaterialDialog loadingDialog = null;
    private TextView mTitleTv;
    private OnToolbarClickListener onToolbarClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AppStatusManager.getInstance().getAppStatus() == AppStatusConstant.STATUS_FORCE_KILLED) {
            restartApp();
            return;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (AppStatusManager.getInstance().getAppStatus() == AppStatusConstant.STATUS_FORCE_KILLED) {
            restartApp();
        }
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    protected void restartApp() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra(AppStatusConstant.KEY_HOME_ACTION, AppStatusConstant.ACTION_RESTART_APP);
        startActivity(intent);
    }

    public void setOnToolbarClickListener(OnToolbarClickListener onToolbarClickListener) {
        this.onToolbarClickListener = onToolbarClickListener;
    }

    interface OnToolbarClickListener {
        void onToolBarBackClick();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
    }


    /**
     * 设置当前布局资源(不包含Toolbar)
     *
     * @param layoutId 布局资源
     */
    public void setLayoutAndToolbar(int layoutId) {
        this.setLayoutAndToolbar(layoutId, -1);
    }

    /**
     * 设置界面资源与toolbar配置
     *
     * @param layoutId   布局资源
     * @param titleResId 标题
     */
    public void setLayoutAndToolbar(int layoutId, int titleResId) {
        if (titleResId != -1) {
            this.setLayoutAndToolbar(layoutId, titleResId, true);
        } else {
            this.setLayoutAndToolbar(layoutId, titleResId, false);
        }
    }

    /**
     * 设置界面资源与toolbar配置
     *
     * @param layoutId   布局资源
     * @param titleResId 标题
     */
    public void setLayoutAndToolbar(int layoutId, String titleResId) {
        if (TextUtils.isEmpty(titleResId)) {
            this.setContentView(layoutId);
            return;
        }
        this.setLayoutAndToolbar(layoutId, titleResId, true);
    }

    /**
     * 设置界面资源与toolbar配置
     *
     * @param layoutId    布局资源
     * @param titleResId  标题
     * @param haveToolbar 是否包含toolbar 默认不包含
     */
    public void setLayoutAndToolbar(int layoutId, int titleResId, boolean haveToolbar) {
        this.setContentView(layoutId);
        if (haveToolbar) {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            if (toolbar == null) {
                return;
            }
            if (mTitleTv == null) {
                mTitleTv = (TextView) toolbar.findViewById(R.id.titleId);
            }
            mTitleTv.setText(titleResId);
            setSupportActionBar(toolbar);
            final ActionBar ab = getSupportActionBar();
            assert ab != null;
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setDisplayShowTitleEnabled(false);
            toolbar.setNavigationOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    toolBarClick();
                }
            });
        }
    }

    /**
     * 设置界面资源与toolbar配置
     *
     * @param layoutId    布局资源
     * @param titleResId  标题
     * @param haveToolbar 是否包含toolbar 默认不包含
     */
    public void setLayoutAndToolbar(int layoutId, String titleResId, boolean haveToolbar) {
        this.setContentView(layoutId);
        if (haveToolbar) {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            if (toolbar == null) {
                return;
            }
            mTitleTv = (TextView) toolbar.findViewById(R.id.titleId);
            if (mTitleTv == null) {
                mTitleTv = (TextView) toolbar.findViewById(R.id.titleId);
            }
            mTitleTv.setText(titleResId);
            setSupportActionBar(toolbar);
            final ActionBar ab = getSupportActionBar();
            if (ab != null) {
                ab.setDisplayHomeAsUpEnabled(true);
                ab.setDisplayShowTitleEnabled(false);
            }
            toolbar.setNavigationOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    toolBarClick();
                }
            });
        }
    }


    public Toolbar getToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            return toolbar;
        }
        return null;
    }

    public void toolBarClick() {
        if (onToolbarClickListener != null) {
            onToolbarClickListener.onToolBarBackClick();
        }
        finish();
    }

    protected void onDestroy() {
        isDestroy = true;
        super.onDestroy();
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
        if (evLoading != null && evLoading.isShowing()) {
            evLoading.dismiss();
        }
        onCancel();
    }


    @Override
    public void onClick(View v) {

    }

    public void hideProgressDialog() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    public int findColorById(int color) {
        return getResources().getColor(color);
    }

    public String findStrById(int str) {
        return getResources().getString(str);
    }

    public Drawable findDrawById(int draw) {
        return getResources().getDrawable(draw);
    }

    public Dialog showProgressDialog() {
        return showProgressDialog("加载中...");
    }


    public MaterialDialog showProgressDialog(String message) {
        if (loadingDialog == null) {
            loadingDialog = new MaterialDialog.Builder(this)
                    .content(message)
                    .progress(true, 0)
                    .cancelListener(this)
                    .progressIndeterminateStyle(false).build();
        } else {
            loadingDialog.setContent(message);
        }
        loadingDialog.show();
        return loadingDialog;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        onCancel();
    }

    protected void onCancel() {

    }


}