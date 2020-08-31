package com.sito.evpro.inspection.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.sito.evpro.inspection.R;


/**
 * 自定义的音频模拟条形图
 * Created by shize on 2016/9/5.
 */
public class MyAF extends View {
    // 音频矩形的数量
    private int mRectCount;
    // 音频矩形的画笔
    private Paint mRectPaint;
    // 渐变颜色的两种
    private int topColor, downColor;
    // 音频矩形的宽和高
    private int mRectWidth, mRectHeight;
    //中间线高度
    private float mCenter;
    // 偏移量
    private int offset;
    // 频率速度
    private int mSpeed;

    public MyAF(Context context) {
        super(context);
    }

    public MyAF(Context context, AttributeSet attrs) {
        super(context, attrs);
        setPaint(context, attrs);
    }

    public MyAF(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setPaint(context, attrs);
    }

    public void setPaint(Context context, AttributeSet attrs) {
        // 将属性存储到TypedArray中
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MyAF);
        mRectPaint = new Paint();
        // 添加矩形画笔的基础颜色
        mRectPaint.setColor(ta.getColor(R.styleable.MyAF_AFTopColor,
                ContextCompat.getColor(context, R.color.color_fault_blue)));
        // 添加矩形渐变色的上面部分
        topColor = ta.getColor(R.styleable.MyAF_AFTopColor,
                ContextCompat.getColor(context, R.color.color_shape_gray));
        // 添加矩形渐变色的下面部分
        downColor = ta.getColor(R.styleable.MyAF_AFDownColor,
                ContextCompat.getColor(context, R.color.colorBlack));
        // 设置矩形的数量
        mRectCount = ta.getInt(R.styleable.MyAF_AFCount, 10);
        // 设置重绘的时间间隔，也就是变化速度
        mSpeed = ta.getInt(R.styleable.MyAF_AFSpeed, 300);
        // 每个矩形的间隔
        offset = ta.getInt(R.styleable.MyAF_AFOffset, 5);
        // 回收TypeArray
        mCenter = ta.getDimension(R.styleable.MyAF_AFHeight, 40);
        ta.recycle();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
        // 渐变效果
        LinearGradient mLinearGradient;
        // 画布的宽
        int mWidth;
        // 获取画布的宽
        mWidth = getWidth();
        if (mWidth <= 300) {
            offset = 30;
        }
        //300
        // 获取矩形的最大高度
        mRectHeight = (int) mCenter;
        // 获取单个矩形的宽度(减去的部分为到右边界的间距)
        mRectWidth = (mWidth - offset) / mRectCount;
        // 实例化一个线性渐变
        mLinearGradient = new LinearGradient(
                0,
                0,
                mRectWidth,
                mRectHeight,
                topColor,
                downColor,
                Shader.TileMode.CLAMP
        );
        // 添加进画笔的着色器
        mRectPaint.setShader(mLinearGradient);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        double mRandom;
        float currentHeight;
        for (int i = 0; i < mRectCount; i++) {
            // 由于只是简单的案例就不监听音频输入，随机模拟一些数字即可
            mRandom = Math.random();
            currentHeight = (float) (mCenter / 2 * mRandom);

            // 矩形的绘制是从左边开始到上、右、下边（左右边距离左边画布边界的距离，上下边距离上边画布边界的距离）
            canvas.drawRect(
                    (float) (mRectWidth * i + offset),
                    mCenter - currentHeight,
                    (float) (mRectWidth * (i + 1)),
                    mCenter + currentHeight,
                    mRectPaint
            );
        }
        // 使得view延迟重绘
        postInvalidateDelayed(mSpeed);
    }

}
