package com.sito.evpro.inspection.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.sito.evpro.inspection.R;

/**
 * Created by Administrator on 2017/6/26.
 */

public class VoiceView extends View {
    private Paint mPaint;
    //默认线条颜色
    private int lineColor = Color.parseColor("#58CAF0");

    private int mWidth;
    private int mHeight;

    public VoiceView(Context context) {
        super(context, null);
    }

    public VoiceView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public VoiceView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //read custom attrs
        TypedArray t = context.obtainStyledAttributes(attrs,
                R.styleable.voiceview, 0, 0);
        lineColor = t.getColor(R.styleable.voiceview_voiceview_color, lineColor);
        // we should always recycle after used
        t.recycle();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(lineColor);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY) {
            mWidth = widthSize;
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            mHeight = heightSize;
        }
        setMeasuredDimension(mWidth, mHeight);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.TRANSPARENT);//设置背景透明
        mPaint.setStrokeWidth((float) 1.0);//设置线宽
        //这里每一行4个数组，前两个为起始端点，后两个为终止端点
        //第一条线的位置
        //(0,height/2-2,0,height/2+2)
        //(width/6,height/2-3,width/6,height/2+3)
        float[] pts = {0, mHeight / 2 - 1, 0, mHeight / 2 + 1,
                mWidth / 6, mHeight / 2 - 2, mWidth / 6, mHeight / 2 + 2,
                mWidth / 6 * 2, mHeight / 2 - 3, mWidth / 6 * 2, mHeight / 2 + 3,
                mWidth / 6 * 3, mHeight / 2 - 4, mWidth / 6 * 3, mHeight / 2 + 4,
                mWidth / 6 * 4, mHeight / 2 - 3, mWidth / 6 * 4, mHeight / 2 + 3,
                mWidth / 6 * 5, mHeight / 2 - 2, mWidth / 6 * 4, mHeight / 2 + 2,
                mWidth, mHeight / 2 - 1, mWidth, mHeight / 2 + 1};
        canvas.drawLines(pts,mPaint);
    }
}
