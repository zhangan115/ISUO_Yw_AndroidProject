package com.sito.evpro.inspection.widget.inspectionitem;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sito.evpro.inspection.R;
import com.sito.evpro.inspection.mode.bean.inspection.DataItemBean;
import com.sito.evpro.inspection.view.photo.ViewPagePhotoActivity;
import com.sito.library.utils.GlideUtils;

/**
 * Created by zhangan on 2017-07-10.
 */

public class InspectionItemType3Layout extends LinearLayout {

    private Context mContext;
    private DataItemBean mDataItemBean;
    private ImageView iv_take_photo;
    private TextView tv_title;
    private int mPosition;
    private boolean canEdit;
    @Nullable
    private TaskPhotoListener taskPhotoListener;

    public InspectionItemType3Layout(Context context) {
        super(context);
        init(context);
    }

    public InspectionItemType3Layout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        inflate(context, R.layout.layout_inspection_item_type_3, this);
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_take_photo = (ImageView) findViewById(R.id.iv_take_photo);
        iv_take_photo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(mDataItemBean.getValue())) {
                    ViewPagePhotoActivity.startActivity(mContext, new String[]{mDataItemBean.getValue()}, 0);
                    return;
                }
                if (!canEdit) {
                    return;
                }
                if (taskPhotoListener == null) {
                    return;
                }
                taskPhotoListener.onTakePhoto(mPosition, mDataItemBean.getEquipmentDataDb().getDataItemId());
            }
        });
    }

    public interface TaskPhotoListener {
        void onTakePhoto(int position, long itemId);
    }

    public void setContent(DataItemBean dataItemBean, boolean canEdit) {
        this.mDataItemBean = dataItemBean;
        this.canEdit = canEdit;
        tv_title.setText(dataItemBean.getInspectionName());
        if (TextUtils.isEmpty(dataItemBean.getValue())) {
            iv_take_photo.setImageDrawable(mContext.getResources().getDrawable(R.drawable.photo_button));
        } else {
            GlideUtils.ShowImage(mContext, dataItemBean.getValue(), iv_take_photo, R.drawable.picture_default);
        }
    }

    public void setTaskPhotoListener(int position, @Nullable TaskPhotoListener taskPhotoListener) {
        this.mPosition = position;
        this.taskPhotoListener = taskPhotoListener;
    }
}
