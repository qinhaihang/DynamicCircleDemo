package com.sensetime.qinhaihang_vendor.qhhview.progressbar.circlebar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.sensetime.qinhaihang_vendor.qhhview.R;
import com.sensetime.qinhaihang_vendor.qhhview.progressbar.listener.IProgressListener;
import com.sensetime.qinhaihang_vendor.qhhview.utils.Dimension;

/**
 * @author qinhaihang_vendor
 * @version $Rev$
 * @time 2019/2/21 15:02
 * @des
 * @packgename com.sensetime.qinhaihang_vendor.qhhview.progressbar.circlebar
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes
 */
public class CircleProgressBar extends View {

    private Context mContext;
    private Canvas mCanvas;

    /* 加一个默认的padding值*/
    private float mDefaultPadding = 8;
    private float mPaddingLeft;
    private float mPaddingTop;
    private float mPaddingRight;
    private float mPaddingBottom;

    /* 基础圆形参数 刻度线画笔 */
    private Paint mScaleLinePaint;
    private int mDefaultBarColor;
    private float mRadius;
    /* 刻度线长度 */
    private float mScaleLength;
    /* 刻度圆弧的外接矩形 */
    private RectF mCircleRectF = new RectF();
    private Paint mPercentPaint;

    /*percent文字*/
    private int mPercentTextColor;
    private float mPercentTextSize;
    private int mPercent = 0;
    private int mPercentMax;

    /*接口回调*/
    private IProgressListener mProgressListener;
    private Paint mProgressLinePaint;
    private int mProgressBarColor;

    public CircleProgressBar(Context context) {
        super(context);
        init(context, null, 0);
    }

    public CircleProgressBar(Context context, @androidx.annotation.Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public CircleProgressBar(Context context, @androidx.annotation.Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, @androidx.annotation.Nullable AttributeSet attrs, int defStyleAttr) {
        mContext = context;

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressBar);

        mDefaultBarColor = ta.getColor(R.styleable.CircleProgressBar_default_bar_color, Color.parseColor("#FF686565"));
        mProgressBarColor = ta.getColor(R.styleable.CircleProgressBar_progress_bar_color, Color.parseColor("#FF686565"));
        mPercentTextColor = ta.getColor(R.styleable.CircleProgressBar_percent_color, Color.parseColor("#FF686565"));
        mPercentTextSize = ta.getDimension(R.styleable.CircleProgressBar_percent_text_size, 20f);
        mPercentMax = ta.getInteger(R.styleable.CircleProgressBar_percent_max, 100);
        mRadius = ta.getFloat(R.styleable.CircleProgressBar_radius, 20f);

        ta.recycle();

        initPaint();

    }

    private void initPaint() {
        mScaleLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mScaleLinePaint.setStyle(Paint.Style.FILL);
        mScaleLinePaint.setColor(mDefaultBarColor);

        mPercentPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPercentPaint.setStyle(Paint.Style.FILL);
        mPercentPaint.setColor(mPercentTextColor);
        mPercentPaint.setTextSize(mPercentTextSize);

        mProgressLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mProgressLinePaint.setStyle(Paint.Style.FILL);
        mProgressLinePaint.setColor(mProgressBarColor);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d("qhh", "onMeasure");
        setMeasuredDimension(Dimension.measureDimension(widthMeasureSpec),
                Dimension.measureDimension(heightMeasureSpec));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.d("qhh", "onSizeChanged,w = " + w + " ,h = " + h);
        mRadius = Math.min(w - getPaddingLeft() - getPaddingRight() - mDefaultPadding,
                h - getPaddingTop() - getPaddingBottom() - mDefaultPadding) / 2;
        Log.i("qhh", "radius = " + mRadius);

        mPaddingLeft = mDefaultPadding + getPaddingLeft();
        mPaddingRight = mDefaultPadding + getPaddingRight();
        mPaddingTop = mDefaultPadding + getPaddingTop();
        mPaddingBottom = mDefaultPadding + getPaddingBottom();

        mScaleLength = 0.12f * mRadius;//根据比例确定刻度线长度
        mScaleLinePaint.setStrokeWidth(0.012f * mRadius);
        mProgressLinePaint.setStrokeWidth(0.018f * mRadius);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d("qhh", "onDraw");
        mCanvas = canvas;
        drawPercentText();
        drawDefaultCircle();
        drawProgressCircle();
    }

    private void drawPercentText() {

        mCanvas.save();

        String percentText = mPercent + "%";

        float textWidth = mPercentPaint.measureText(percentText);
        float textHeightHalf = Math.abs(mPercentPaint.ascent() + mPercentPaint.descent()) / 2;

        float width = getWidth() - getPaddingLeft() - getPaddingRight() - mDefaultPadding;
        float height = getHeight() - getPaddingTop() - getPaddingBottom() - mDefaultPadding;

        mCanvas.drawText(percentText,
                width / 2 - textWidth / 2,
                height / 2 + textHeightHalf,
                mPercentPaint);

        mCanvas.restore();
    }

    private void drawDefaultCircle() {
        int width = getWidth();
        int height = getHeight();
        float rotate = 360f / mPercentMax;

        //画背景刻度
        if (width >= height) {

            for (int i = 0; i < mPercentMax; i++) {
                mCanvas.drawLine(getWidth() / 2, mPaddingTop,
                        getWidth() / 2, mPaddingTop + mScaleLength, mScaleLinePaint);
                mCanvas.rotate(rotate, getWidth() / 2, getHeight() / 2);
            }

        } else {

            for (int i = 0; i < mPercentMax; i++) {
                mCanvas.drawLine(mPaddingLeft, getHeight() / 2,
                        mPaddingLeft + mScaleLength, getHeight() / 2, mScaleLinePaint);
                mCanvas.rotate(rotate, getWidth() / 2, getHeight() / 2);
            }

        }

    }

    private void drawProgressCircle() {
        int width = getWidth();
        int height = getHeight();
        float rotate = 360f / mPercentMax;

        mCanvas.save();

        //画进度刻度
        if (width >= height) {

            for (int i = 0; i < mPercent; i++) {
                mCanvas.drawLine(getWidth() / 2, mPaddingTop,
                        getWidth() / 2, mPaddingTop + mScaleLength, mProgressLinePaint);
                mCanvas.rotate(rotate, getWidth() / 2, getHeight() / 2);
            }
        } else {
            mCanvas.rotate(90f, getWidth() / 2, getHeight() / 2);
            for (int i = 0; i < mPercent; i++) {
                mCanvas.drawLine(mPaddingLeft, getHeight() / 2,
                        mPaddingLeft + mScaleLength, getHeight() / 2, mProgressLinePaint);
                mCanvas.rotate(rotate, getWidth() / 2, getHeight() / 2);
            }
        }

        mCanvas.restore();
    }

    public void setPercent(int percent) {
        mPercent = percent;
        if (mPercent <= mPercentMax)
            invalidate();

        if (null != mProgressListener) {
            mProgressListener.onProgress(mPercent);
            if (mPercent == mPercentMax)
                mProgressListener.onProgressFinish();
        }
    }

    public void setProgressListener(IProgressListener progressListener) {
        mProgressListener = progressListener;
    }
}











