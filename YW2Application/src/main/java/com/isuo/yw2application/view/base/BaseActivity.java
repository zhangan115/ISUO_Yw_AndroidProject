package com.isuo.yw2application.view.base;


import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.isuo.yw2application.R;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.orhanobut.logger.Logger;

import com.qw.soul.permission.SoulPermission;
import com.qw.soul.permission.bean.Permission;
import com.qw.soul.permission.bean.Permissions;
import com.qw.soul.permission.callbcak.CheckRequestPermissionsListener;
import com.sito.library.base.AbsBaseActivity;
import com.umeng.analytics.MobclickAgent;

import java.io.File;


/**
 * BaseActivity 页面基类
 * 类描述：页面基类
 *
 * @author zhuzhu
 * @version v0.1
 * @since 2015-04-13
 */
public abstract class BaseActivity extends AbsBaseActivity implements OnClickListener {
    protected boolean isDestroy = false;
    protected boolean useAnimToBack = true;
    protected boolean useAnimToNewAct = true;
    protected MaterialDialog loadingDialog = null;
    protected TextView mTitleTv;
    protected OnToolbarClickListener onToolbarClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.d(TAG);
    }

    private BasePopupView popupView;

    public void showPopupLoading() {
        popupView = new XPopup.Builder(this)
                .asLoading("正在加载...")
                .show();
    }

    public void hidePopupLoading() {
        if (popupView != null) {
            popupView.doDismissAnimation();
        }
    }

    public void setOnToolbarClickListener(OnToolbarClickListener onToolbarClickListener) {
        this.onToolbarClickListener = onToolbarClickListener;
    }

    public interface OnToolbarClickListener {
        void onToolBarBackClick();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
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
            Toolbar toolbar = findViewById(R.id.toolbar);
            if (toolbar == null) {
                return;
            }
            if (mTitleTv == null) {
                mTitleTv = toolbar.findViewById(R.id.titleId);
            }
            if (titleResId != -1){
                mTitleTv.setText(titleResId);
            }
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
            Toolbar toolbar = findViewById(R.id.toolbar);
            if (toolbar == null) {
                return;
            }
            mTitleTv = toolbar.findViewById(R.id.titleId);
            if (mTitleTv == null) {
                mTitleTv = toolbar.findViewById(R.id.titleId);
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
        return (Toolbar) findViewById(R.id.toolbar);
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
        hideProgressDialog();
        onCancel();
        Logger.d(TAG);
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

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        if (useAnimToNewAct) {
            overridePendingTransition(R.anim.slideinright, R.anim.slideoutleft);
        }
    }

    @Override
    public void startActivity(Intent intent, @Nullable Bundle options) {
        super.startActivity(intent, options);
        if (useAnimToNewAct) {
            overridePendingTransition(R.anim.slideinright, R.anim.slideoutleft);
        }
    }

    @Override
    public void finish() {
        super.finish();
        if (useAnimToBack) {
            overridePendingTransition(R.anim.slideinleft, R.anim.slideoutright);
        }
    }

    public interface OnPhotoShowListener {
        void showPhoto();
    }

    public void showPhotoDialog(final Activity activity, final File photoFile, final int REQUEST_CODE, final int ACTION_START_PHOTO, final OnPhotoShowListener listener) {
        Permissions permissions = Permissions.build(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA);
        SoulPermission.getInstance().checkAndRequestPermissions(permissions, new CheckRequestPermissionsListener() {
            @Override
            public void onAllPermissionOk(Permission[] allPermissions) {
                String[] array = new String[]{"查看", "拍照", "相册"};
                new XPopup.Builder(activity).asCenterList("", array, new OnSelectListener() {
                    @Override
                    public void onSelect(int position, String text) {
                        if (position == 0) {
                            listener.showPhoto();
                        } else if (position == 1) {
                            startCameraToPhoto(activity, photoFile, REQUEST_CODE);
                        } else {
                            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                            intent.setType("image/*");
                            activity.startActivityForResult(intent, ACTION_START_PHOTO);
                        }
                    }
                }).show();
            }

            @Override
            public void onPermissionDenied(Permission[] refusedPermissions) {

            }
        });
    }

    /**
     * 启动相机拍摄照片
     *
     * @param photoFile    文件
     * @param REQUEST_CODE 请求code
     */
    public void startCameraToPhoto(Activity activity, File photoFile, int REQUEST_CODE) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, "com.isuo.yw2application.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                activity.startActivityForResult(takePictureIntent, REQUEST_CODE);
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}