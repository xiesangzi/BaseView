package com.yhms.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import androidx.annotation.RequiresApi;


public class ProgressView extends ProgressBar {

    public ProgressView(Context context) {
        super(context);
    }

    public ProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(21)
    public ProgressView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    protected Paint mPaint = new Paint();

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();

        mPaint.setColor(0x41A2A0A0);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(0f);
        RectF oval3 = new RectF(0, 0, width, height);
        canvas.drawRoundRect(oval3, 20, 20, mPaint);
        super.onDraw(canvas);
    }
}