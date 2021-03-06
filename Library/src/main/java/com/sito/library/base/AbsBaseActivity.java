package com.sito.library.base;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.sito.library.R;


/**
 * AbsBaseActivity 页面基类
 * 类描述：页面基类
 *
 * @author zhuzhu
 * @version v0.1
 * @since 2015-04-13
 */
public abstract class AbsBaseActivity extends AppCompatActivity implements DialogInterface.OnCancelListener {

    public final String TAG = this.getClass().getSimpleName();//TAG
    @Nullable
    private AbsBaseApp mApp = null;//当前APP
    protected AlertDialog evLoading = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getApplication() instanceof AbsBaseApp) {
            mApp = (AbsBaseApp) getApplication();
        }
        if (mApp != null) {
            mApp.openActivity(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideEvLoading();
        if (mApp != null) {
            mApp.closeActivity(this);
        }
    }

    /**
     * 状态栏完全透明
     */
    public void transparentStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow()
                    .getDecorView()
                    .setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }

    /**
     * 修改状态栏icon 颜色
     *
     * @param bDark 是否将icon 颜色变为灰色
     */
    public void setDarkStatusIcon(boolean bDark) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decorView = getWindow().getDecorView();
            if (decorView != null) {
                int vis = decorView.getSystemUiVisibility();
                if (bDark) {
                    vis |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                } else {
                    vis &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                }
                decorView.setSystemUiVisibility(vis);
            }
        }
    }

    public void showEvLoading() {
        if (evLoading == null) {
            ImageView imageView = (ImageView) LayoutInflater.from(this).inflate(R.layout.loading_layout, null);
            evLoading = new AlertDialog.Builder(this, R.style.ProgressDialogTheme)
                    .setView(imageView)
                    .setOnCancelListener(this)
                    .create();
            AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getDrawable();
            animationDrawable.start();
        }
        evLoading.show();
    }

    public void hideEvLoading() {
        if (evLoading != null) {
            evLoading.dismiss();
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

    @Override
    public void onCancel(DialogInterface dialog) {

    }

    public int getStatusHeight(){
        int statusBarHeight = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
        return  statusBarHeight;
    }
}