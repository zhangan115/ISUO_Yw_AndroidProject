package com.isuo.yw2application.view.main.task.inspection.input.widget;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.isuo.yw2application.R;
import com.isuo.yw2application.app.Yw2Application;
import com.isuo.yw2application.mode.bean.inspection.DataItemBean;
import com.isuo.yw2application.mode.bean.inspection.DataItemValueListBean;
import com.isuo.yw2application.view.photo.ViewPagePhotoActivity;
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
    private boolean canEdit = false;

    public Type3Layout(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.layout_input_type_3, this);
        this.mContext = context;
    }

    public void setDataToView(boolean canEdit, final DataItemValueListBean bean, final OnTakePhotoListener onTakePhotoListener) {
        this.dataItemBean = bean.getDataItem();
        this.progressBar = findViewById(R.id.progressBar);
        this.canEdit=canEdit;
        if (canEdit && !TextUtils.isEmpty(bean.getValue())) {
            this.canEdit = false;
        }
        TextView tv_title = findViewById(R.id.tv_title);
        iv_take_photo = findViewById(R.id.iv_take_photo);
        tv_title.setText(dataItemBean.getInspectionName());
        if (dataItemBean.getEquipmentDataDb() != null && TextUtils.isEmpty(dataItemBean.getLocalFile())) {
            dataItemBean.setLocalFile(dataItemBean.getEquipmentDataDb().getLocalPhoto());
        }
        if (TextUtils.isEmpty(dataItemBean.getLocalFile())) {
            if (!TextUtils.isEmpty(dataItemBean.getEquipmentDataDb().getValue())){
                GlideUtils.ShowImage(mContext, dataItemBean.getEquipmentDataDb().getValue(), iv_take_photo, R.drawable.img_default);
            }
            iv_take_photo.setImageDrawable(mContext.getResources().getDrawable(R.drawable.photograph));
        } else {
            GlideUtils.ShowImage(mContext, dataItemBean.getLocalFile(), iv_take_photo, R.drawable.img_default);
        }
        iv_take_photo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(dataItemBean.getLocalFile())) {
                    ViewPagePhotoActivity.startActivity(mContext, new String[]{dataItemBean.getLocalFile()}, 0);
                    return;
                }else if (!TextUtils.isEmpty(dataItemBean.getEquipmentDataDb().getValue())){
                    ViewPagePhotoActivity.startActivity(mContext, new String[]{dataItemBean.getEquipmentDataDb().getValue()}, 0);
                    return;
                }
                if (!Type3Layout.this.canEdit) {
                    return;
                }
                if (onTakePhotoListener != null) {
                    onTakePhotoListener.onTakePhoto(dataItemBean);
                }
            }
        });
        iv_take_photo.setOnLongClickListener(new OnLongClickListener() {
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
                                if (!Type3Layout.this.canEdit) {
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
            iv_take_photo.setImageDrawable(mContext.getResources().getDrawable(R.drawable.photograph));
        } else {
            GlideUtils.ShowImage(mContext, dataItemBean.getLocalFile(), iv_take_photo, R.drawable.img_default);
        }
    }

    public interface OnTakePhotoListener {

        void onTakePhoto(DataItemBean dataItemBean);

        void onDeletePhoto(DataItemBean dataItemBean);

        void onAgainPhoto(DataItemBean dataItemBean);
    }
}
