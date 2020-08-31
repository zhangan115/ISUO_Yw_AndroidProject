package com.sito.evpro.inspection.widget.voiceview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.sito.evpro.inspection.R;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by carlos on 2016/1/29.
 * 自定义声音振动曲线view
 */
public class VoiceLineView extends View {
    private final int LINE = 0;
    private final int RECT = 1;
    private final int VERLINE = 2;

    private int middleLineColor = Color.BLACK;
    private int voiceLineColor = Color.BLACK;
    private float middleLineHeight = 4;
    private Paint paint;
    private Paint paintVoicLine;
    private Paint mPaintVerLine;
    private float mVerHeight;
    private float mVerWidth;
    private int mode;
    /**
     * 灵敏度
     */
    private int sensibility = 4;

    private float maxVolume = 100;


    private float translateX = 0;
    private boolean isSet = false;

    /**
     * 振幅
     */
    private float amplitude = 1;
    /**
     * 音量
     */
    private float volume = 10;
    private int fineness = 1;
    private float targetVolume = 1;


    private long speedY = 50;
    private float rectWidth = 25;
    private float rectSpace = 5;
    private float rectInitHeight = 4;
    private List<Rect> rectList;

    private long lastTime = 0;
    private int lineSpeed = 90;

    List<Path> paths = null;

    public VoiceLineView(Context context) {
        super(context);
    }

    public VoiceLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAtts(context, attrs);
    }

    public VoiceLineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAtts(context, attrs);
    }

    private void initAtts(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.voiceView);
        mode = typedArray.getInt(R.styleable.voiceView_viewMode, 0);
        voiceLineColor = typedArray.getColor(R.styleable.voiceView_voiceLine, Color.BLACK);
        maxVolume = typedArray.getFloat(R.styleable.voiceView_maxVolume, 100);
        sensibility = typedArray.getInt(R.styleable.voiceView_sensibility, 4);
        if (mode == RECT) {
            rectWidth = typedArray.getDimension(R.styleable.voiceView_rectWidth, 25);
            rectSpace = typedArray.getDimension(R.styleable.voiceView_rectSpace, 5);
            rectInitHeight = typedArray.getDimension(R.styleable.voiceView_rectInitHeight, 4);
        } else if (mode == LINE) {
            middleLineColor = typedArray.getColor(R.styleable.voiceView_middleLine, Color.BLACK);
            middleLineHeight = typedArray.getDimension(R.styleable.voiceView_middleLineHeight, 4);
            lineSpeed = typedArray.getInt(R.styleable.voiceView_lineSpeed, 90);
            fineness = typedArray.getInt(R.styleable.voiceView_fineness, 1);
            paths = new ArrayList<>(20);
            for (int i = 0; i < 20; i++) {
                paths.add(new Path());
            }
        } else {
            mVerWidth = typedArray.getDimension(R.styleable.voiceView_verLineWidth, 40);
            mVerHeight = typedArray.getDimension(R.styleable.voiceView_verLineHeight, 20);
        }
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
//        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
//        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
//        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
//        int width;
//        int height ;
//        if (widthMode == MeasureSpec.EXACTLY)
//        {
//            width = widthSize;
//        } else
//        {
//            width = (int)mVerWidth;
//        }
//
//        if (heightMode == MeasureSpec.EXACTLY)
//        {
//            height = heightSize;
//        } else
//        {
//          height = (int)mVerHeight;
//        }
//
//
//
//        setMeasuredDimension(width, height);
        int width = onMeasureR(0, widthMeasureSpec);
        int height = onMeasureR(1, heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    /**
     * 计算控件宽高
     * <p>
     * [0宽,1高]
     *
     * @param oldMeasure
     * @author Ruffian
     */
    public int onMeasureR(int attr, int oldMeasure) {

        int newSize = 0;
        int mode = MeasureSpec.getMode(oldMeasure);
        int oldSize = MeasureSpec.getSize(oldMeasure);

        switch (mode) {
            case MeasureSpec.EXACTLY:
                newSize = oldSize;
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:

                float value;

                if (attr == 0) {

                    value = mVerWidth;
                    // value = mPaint.measureText(mText);

                    // 控件的宽度  + getPaddingLeft() +  getPaddingRight()
                    newSize = (int) (getPaddingLeft() + value + getPaddingRight());

                } else if (attr == 1) {

                    value = mVerHeight;
                    // FontMetrics fontMetrics = mPaint.getFontMetrics();
                    // value = Math.abs((fontMetrics.descent - fontMetrics.ascent));

                    // 控件的高度  + getPaddingTop() +  getPaddingBottom()
                    newSize = (int) (getPaddingTop() + value + getPaddingBottom());

                }

                break;
        }

        return newSize;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mode == RECT) {
            drawVoiceRect(canvas);
        } else if (mode == LINE) {
            drawMiddleLine(canvas);
            drawVoiceLine(canvas);
        } else {
            drawVoiceVerLine(canvas);
        }
//        run();
    }

    private void drawVoiceVerLine(Canvas canvas) {
        int mWidth = (int) mVerWidth;
        int mHeight = (int) mVerHeight;
        float[] pts = {mHeight / 6 * 0, mHeight / 2 - mHeight / 6, mHeight / 6 * 0, mHeight / 2 + mHeight / 6,
                mWidth / 6 * 1, mHeight / 2 - mHeight / 6 * 2, mWidth / 6 * 1, mHeight / 2 + mHeight / 6 * 2,
                mWidth / 6 * 2, mHeight / 2 - mHeight / 6 * 2.5f, mWidth / 6 * 2, mHeight / 2 + mHeight / 6 * 2.5f,
                mWidth / 6 * 3, mHeight / 2 - mHeight / 6 * 3, mWidth / 6 * 3, mHeight / 2 + mHeight / 6 * 3,
                mWidth / 6 * 4, mHeight / 2 - mHeight / 6 * 2.5f, mWidth / 6 * 4, mHeight / 2 + mHeight / 6 * 2.5f,
                mWidth / 6 * 5, mHeight / 2 - mHeight / 6 * 2, mWidth / 6 * 5, mHeight / 2 + mHeight / 6 * 2,
                mWidth / 6 * 6, mHeight / 2 - mHeight / 6, mWidth / 6 * 6, mHeight / 2 + mHeight / 6};
        if (mPaintVerLine == null) {
            mPaintVerLine = new Paint();
            mPaintVerLine.setColor(voiceLineColor);
            mPaintVerLine.setAntiAlias(true);
            mPaintVerLine.setStyle(Paint.Style.STROKE);
            mPaintVerLine.setStrokeWidth(6);
        }
//        canvas.drawLines(pts, mPaintVerLine);
        //canvas.restore();
//        canvas.drawLine(0, mHeight / 2 - mHeight / 6, 0, mHeight / 2 + mHeight / 6, mPaintVerLine);
//        canvas.drawLine(mWidth / 6, mHeight / 2 - mHeight / 6 * 2, mWidth / 6, mHeight / 2 + mHeight / 6 * 2, mPaintVerLine);
//        canvas.drawLine(mWidth / 6 * 2, mHeight / 2 - mHeight / 6 * 2.5f, mWidth / 6 * 2, mHeight / 2 + mHeight / 6 * 2.5f, mPaintVerLine);
//        canvas.drawLine(mWidth / 6 * 3, mHeight / 2 - mHeight / 6 * 3, mWidth / 6 * 3, mHeight / 2 + mHeight / 6 * 3, mPaintVerLine);
//        canvas.drawLine(mWidth / 6 * 4, mHeight / 2 - mHeight / 6 * 2.5f, mWidth / 6 * 4, mHeight / 2 + mHeight / 6 * 2.5f, mPaintVerLine);
//        canvas.drawLine(mWidth / 6 * 5, mHeight / 2 - mHeight / 6 * 2, mWidth / 6 * 5, mHeight / 2 + mHeight / 6 * 2, mPaintVerLine);
//        canvas.drawLine(mWidth, mHeight / 2 - mHeight / 6, mWidth, mHeight / 2 + mHeight / 6, mPaintVerLine);

//        invalidate();
        verLineChange(canvas);
        //循环执行

    }

    private void verLineChange(Canvas canvas) {
        int mWidth = (int) mVerWidth;
        int mHeight = (int) mVerHeight;
        int i = (int) (Math.random() * 7);
        float[] pts = {mHeight / 6 * i, mHeight / 2 - mHeight / 6, mHeight / 6 * i, mHeight / 2 + mHeight / 6,
                mWidth / 6 * i, mHeight / 2 - mHeight / 6 * 2, mWidth / 6 * i, mHeight / 2 + mHeight / 6 * 2,
                mWidth / 6 * i, mHeight / 2 - mHeight / 6 * 2.5f, mWidth / 6 * i, mHeight / 2 + mHeight / 6 * 2.5f,
                mWidth / 6 * i, mHeight / 2 - mHeight / 6 * 3, mWidth / 6 * i, mHeight / 2 + mHeight / 6 * 3,
                mWidth / 6 * i, mHeight / 2 - mHeight / 6 * 2.5f, mWidth / 6 * i, mHeight / 2 + mHeight / 6 * 2.5f,
                mWidth / 6 * i, mHeight / 2 - mHeight / 6 * 2, mWidth / 6 * i, mHeight / 2 + mHeight / 6 * 2,
                mWidth / 6 * i, mHeight / 2 - mHeight / 6, mWidth / 6 * i, mHeight / 2 + mHeight / 6};
        canvas.drawLines(pts, mPaintVerLine);
        //invalidate();
        postInvalidateDelayed(30);
    }

    private void drawMiddleLine(Canvas canvas) {
        if (paint == null) {
            paint = new Paint();
            paint.setColor(middleLineColor);
            paint.setAntiAlias(true);
        }
        canvas.save();
        canvas.drawRect(0, getHeight() / 2 - middleLineHeight / 2, getWidth(), getHeight() / 2 + middleLineHeight / 2, paint);
        canvas.restore();
    }

    private void drawVoiceLine(Canvas canvas) {
        lineChange();
        if (paintVoicLine == null) {
            paintVoicLine = new Paint();
            paintVoicLine.setColor(voiceLineColor);
            paintVoicLine.setAntiAlias(true);
            paintVoicLine.setStyle(Paint.Style.STROKE);
            paintVoicLine.setStrokeWidth(2);
        }
        canvas.save();
        int moveY = getHeight() / 2;
        for (int i = 0; i < paths.size(); i++) {
            paths.get(i).reset();
            paths.get(i).moveTo(getWidth(), getHeight() / 2);
        }
        for (float i = getWidth() - 1; i >= 0; i -= fineness) {
            amplitude = 4 * volume * i / getWidth() - 4 * volume * i * i / getWidth() / getWidth();
            for (int n = 1; n <= paths.size(); n++) {
                float sin = amplitude * (float) Math.sin((i - Math.pow(1.22, n)) * Math.PI / 180 - translateX);
                paths.get(n - 1).lineTo(i, (2 * n * sin / paths.size() - 15 * sin / paths.size() + moveY));
            }
        }
        for (int n = 0; n < paths.size(); n++) {
            if (n == paths.size() - 1) {
                paintVoicLine.setAlpha(255);
            } else {
                paintVoicLine.setAlpha(n * 130 / paths.size());
            }
            if (paintVoicLine.getAlpha() > 0) {
                canvas.drawPath(paths.get(n), paintVoicLine);
            }
        }
        canvas.restore();
    }

    private void drawVoiceRect(Canvas canvas) {
        if (paintVoicLine == null) {
            paintVoicLine = new Paint();
            paintVoicLine.setColor(voiceLineColor);
            paintVoicLine.setAntiAlias(true);
            paintVoicLine.setStyle(Paint.Style.STROKE);
            paintVoicLine.setStrokeWidth(2);
        }
        if (rectList == null) {
            rectList = new LinkedList<>();
        }
        int totalWidth = (int) (rectSpace + rectWidth);
        if (speedY % totalWidth < 6) {
            Rect rect = new Rect((int) (-rectWidth - 10 - speedY + speedY % totalWidth),
                    (int) (getHeight() / 2 - rectInitHeight / 2 - (volume == 10 ? 0 : volume / 2)),
                    (int) (-10 - speedY + speedY % totalWidth),
                    (int) (getHeight() / 2 + rectInitHeight / 2 + (volume == 10 ? 0 : volume / 2)));
            if (rectList.size() > getWidth() / (rectSpace + rectWidth) + 2) {
                rectList.remove(0);
            }
            rectList.add(rect);
        }
        canvas.translate(speedY, 0);
        for (int i = rectList.size() - 1; i >= 0; i--) {
            canvas.drawRect(rectList.get(i), paintVoicLine);
        }
        rectChange();
    }

    public void setVolume(int volume) {
        if (volume > maxVolume * sensibility / 25) {
            isSet = true;
            this.targetVolume = getHeight() * volume / 2 / maxVolume;
        }
    }

    private void lineChange() {
        if (lastTime == 0) {
            lastTime = System.currentTimeMillis();
            translateX += 1.5;
        } else {
            if (System.currentTimeMillis() - lastTime > lineSpeed) {
                lastTime = System.currentTimeMillis();
                translateX += 1.5;
            } else {
                return;
            }
        }
        if (volume < targetVolume && isSet) {
            volume += getHeight() / 30;
        } else {
            isSet = false;
            if (volume <= 10) {
                volume = 10;
            } else {
                if (volume < getHeight() / 30) {
                    volume -= getHeight() / 60;
                } else {
                    volume -= getHeight() / 30;
                }
            }
        }
    }

    private void rectChange() {
        speedY += 6;
        if (volume < targetVolume && isSet) {
            volume += getHeight() / 30;
        } else {
            isSet = false;
            if (volume <= 10) {
                volume = 10;
            } else {
                if (volume < getHeight() / 30) {
                    volume -= getHeight() / 60;
                } else {
                    volume -= getHeight() / 30;
                }
            }
        }
    }

    public void run() {
        if (mode == RECT) {
            postInvalidateDelayed(30);
        } else {
            invalidate();
        }
    }

}