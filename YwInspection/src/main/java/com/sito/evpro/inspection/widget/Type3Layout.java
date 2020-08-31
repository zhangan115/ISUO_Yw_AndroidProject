package com.sito.evpro.inspection.widget;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.sito.evpro.inspection.R;
import com.sito.evpro.inspection.mode.bean.inspection.DataItemBean;
import com.sito.evpro.inspection.mode.bean.inspection.DataItemValueListBean;
import com.sito.evpro.inspection.view.photo.ViewPagePhotoActivity;
import com.sito.library.utils.GlideUtils;

/**
 * 录入数据 拍照类型
 * Created by zhangan on 2018/2/27.
 */

public class Type3Layout extends LinearLayout {

    private Context mContext;
    private DataItemBean dataItemBean;
    private ProgressBar progressBar;
    private ImageView iv_take_photo;

    public Type3Layout(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.layout_input_type_3, this);
        this.mContext = context;
    }

    public void setDataToView(final boolean canEdit, final DataItemValueListBean bean, final OnTakePhotoListener onTakePhotoListener) {
        dataItemBean = bean.getDataItem();
        progressBar = findViewById(R.id.progressBar);
        TextView tv_title = findViewById(R.id.tv_title);
        iv_take_photo = findViewById(R.id.iv_take_photo);
        tv_title.setText(dataItemBean.getInspectionName());
        if (dataItemBean.getEquipmentDataDb() != null && TextUtils.isEmpty(dataItemBean.getLocalFile())) {
            dataItemBean.setLocalFile(dataItemBean.getEquipmentDataDb().getLocalPhoto());
        }
        if (TextUtils.isEmpty(dataItemBean.getLocalFile())) {
            iv_take_photo.setImageDrawable(mContext.getResources().getDrawable(R.drawable.photo_button));
        } else {
            GlideUtils.ShowImage(mContext, dataItemBean.getLocalFile(), iv_take_photo, R.drawable.picture_default);
        }
        iv_take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(dataItemBean.getLocalFile())) {
                    ViewPagePhotoActivity.startActivity(mContext, new String[]{dataItemBean.getLocalFile()}, 0);
                    return;
                }
                if (!canEdit) {
                    return;
                }
                if (onTakePhotoListener != null) {
                    onTakePhotoListener.onTakePhoto(dataItemBean);
                }
            }
        });
        iv_take_photo.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (TextUtils.isEmpty(dataItemBean.getLocalFile())) {
                    return false;
                }
                new MaterialDialog.Builder(mContext)
                        .items(R.array.choose_condition_2)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                                if (onTakePhotoListener == null) {
                                    return;
                                }
                                if (!canEdit) {
                                    return;
                                }
                                switch (position) {
                                    case 0://删除照片
                                        onTakePhotoListener.onDeletePhoto(dataItemBean);
                                        break;
                                    default://重新拍照
                                        onTakePhotoListener.onAgainPhoto(dataItemBean);
                                        break;
                                }
                            }
                        })
                        .show();
                return true;
            }
        });
    }

    public void refreshUi() {
        if (progressBar == null || iv_take_photo == null) return;
        if (dataItemBean.isUploading()) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
        if (TextUtils.isEmpty(dataItemBean.getLocalFile())) {
            iv_take_photo.setImageDrawable(mContext.getResources().getDrawable(R.drawable.photo_button));
        } else {
            GlideUtils.ShowImage(mContext, dataItemBean.getLocalFile(), iv_take_photo, R.drawable.picture_default);
        }
    }

    public interface OnTakePhotoListener {

        void onTakePhoto(DataItemBean dataItemBean);

        void onDeletePhoto(DataItemBean dataItemBean);

        void onAgainPhoto(DataItemBean dataItemBean);
    }
}
