package com.sito.evpro.inspection.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sito.evpro.inspection.R;
import com.sito.evpro.inspection.app.InspectionApp;
import com.sito.evpro.inspection.utils.Utils;


/**
 * 特殊字符表
 * Created by zhangan on 2017-9-30.
 */
public class SpecialChatView extends LinearLayout implements View.OnClickListener {
    private LinearLayout mMarkList;
    private ImageView mImgSwitch;
    private final int MARKS_MODE_HEIGHT = 40;
    private ISelected mISelected;
    private Context mContext;
    private Activity activity;

    public SpecialChatView(Context context) {
        super(context);
        init(context);
    }

    public SpecialChatView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SpecialChatView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.special_chat, this);
        this.mContext = context;
        if (context instanceof Activity) {
            activity = (Activity) context;
        }
        mMarkList = (LinearLayout) this.findViewById(R.id.layout_special);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mMarkList.getLayoutParams();
        layoutParams.height = Utils.dip2px(context, MARKS_MODE_HEIGHT);
        mMarkList.setLayoutParams(layoutParams);
        mImgSwitch = (ImageView) findViewById(R.id.img_switch);
        findViewById(R.id.switch_ll).setOnClickListener(this);
    }

    public void setData() {
        try {
            String[] array = getResources().getStringArray(R.array.special_chats);
            int cel = 5;
            int row = array.length / cel;
            row = row + ((array.length % cel) > 0 ? 1 : 0);
            for (int i = 0; i < row; i++) {
                LinearLayout childLayout = new LinearLayout(getContext());
                childLayout.setWeightSum(cel);
                childLayout.setGravity(Gravity.LEFT);
                childLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                mMarkList.addView(childLayout);
                for (int j = 0; j < cel; j++) {
                    int index = i * cel + j;
                    String str = "";
                    if (index < array.length) {
                        str = array[index];
                    }
                    LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1);
                    params.leftMargin = Utils.dip2px(mContext, 3);
                    params.rightMargin = Utils.dip2px(mContext, 3);
                    childLayout.addView(createMark(str), params);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private View createMark(String text) {
        LinearLayout layout = new LinearLayout(getContext());
        layout.setPadding(0, Utils.dip2px(mContext, 8), 0, Utils.dip2px(mContext, 8));
        layout.setGravity(Gravity.CENTER);
        TextView mark = new TextView(getContext());
        mark.setGravity(Gravity.CENTER);
        mark.setPadding(Utils.dip2px(mContext, 1), Utils.dip2px(mContext, 5), Utils.dip2px(mContext, 1), Utils.dip2px(mContext, 5));
        mark.setTag(text);
        mark.setText(text);
        mark.setTextSize(12);
        ColorStateList cls = new ColorStateList(new int[][]{{android.R.attr.state_pressed}, {0}}
                , new int[]{getResources().getColor(R.color.sp_tv_pressed), getResources().getColor(R.color.sp_tv_normal)});
        mark.setTextColor(cls);
        if (!"".equals(text)) {
            mark.setOnClickListener(this);
            mark.setBackgroundResource(R.drawable.bg_speclise_tv);
        }
        layout.addView(mark);
        return layout;
    }

    public void setOnSelected(ISelected iSelected) {
        this.mISelected = iSelected;
    }

    @Override
    public void onClick(View view) {
        if (view.getTag() != null) {
            String text = (String) view.getTag();
            if (mISelected != null) {
                mISelected.onSelected(text);
            }
        } else if (view.getId() == R.id.switch_ll) {
            if (mMarkList.getVisibility() == View.GONE) {
                mImgSwitch.setImageResource(R.drawable.arrow_down);
                mMarkList.setVisibility(View.VISIBLE);
                InspectionApp.getInstance().hideSoftKeyBoard(activity);
            } else {
                mImgSwitch.setImageResource(R.drawable.arrow_up);
                mMarkList.setVisibility(View.GONE);
            }
        }
    }


    public interface ISelected {
        void onSelected(String tag);
    }

    public void hide() {
        mImgSwitch.setImageResource(R.drawable.arrow_up);
        mMarkList.setVisibility(View.GONE);
    }

    public boolean isShow() {
        return mMarkList.getVisibility() == View.VISIBLE;
    }

    public void show() {
        mImgSwitch.setImageResource(R.drawable.arrow_down);
        mMarkList.setVisibility(View.VISIBLE);
    }
}
