package com.sito.evpro.inspection.widget;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.sito.evpro.inspection.R;


/**
 * Created by Administrator on 2017/6/14.
 */

public abstract class EnergyDialog extends Dialog implements View.OnClickListener {
    private Activity activity;
    private ImageView img_cancel;
    private ImageView img_insulate;//隔离
    private TextView text_id;
    private String mId;


    public EnergyDialog(Activity activity) {
        super(activity, R.style.dialog);
        this.activity = activity;
    }
    public EnergyDialog(Activity activity,String id) {
        super(activity, R.style.dialog);
        this.activity = activity;
        this.mId = id;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.energy_dialog);

        img_cancel = (ImageView) findViewById(R.id.id_energy_dialog_cancle);
        img_insulate = (ImageView) findViewById(R.id.id_energy_dialog_insulate);
        text_id = (TextView) findViewById(R.id.id_energy_dialog_id);
        text_id.setText(mId);

        img_cancel.setOnClickListener(this);
        img_insulate.setOnClickListener(this);

        setViewLocation();
        setCanceledOnTouchOutside(true);//外部点击取消
    }

    /**
     * 设置dialog位于屏幕中间
     */
    private void setViewLocation(){
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int height = dm.heightPixels;
        int width  = dm.widthPixels;
        Window window = this.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        window.setGravity(Gravity.CENTER);
        lp.x = (int)(width*0.8);
        lp.y = (int)(height*0.8);
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
         //设置显示位置
        onWindowAttributesChanged(lp);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_energy_dialog_insulate:
                insulate();
                this.cancel();
                break;
            case R.id.id_energy_dialog_cancle:
                this.cancel();
                break;
        }
    }

    public abstract void insulate();//隔离
}
