package com.lake.circlebtnseekview;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class CircleTravel extends View {
    private float radius;
    private float centerX;
    private float centerY;
    private Paint saturationPaint;
    private Paint defaultPaint;
    private float mCircleLineStrokeWidth = 6;
    // 线条数
    private int mDottedLineCount = 140;
    // 线条宽度
    private float mDottedLineWidth = 50;

    //颜色进度
    private int start_degrees = 270;
    private int stop_degrees = 90;

    public CircleTravel(Context context) {
        this(context, null);
    }

    public CircleTravel(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleTravel(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        this.setLayerType(LAYER_TYPE_SOFTWARE,null);//关闭硬件加速
        saturationPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        saturationPaint.setColor(Color.GRAY);
        saturationPaint.setStrokeWidth(mCircleLineStrokeWidth);
        saturationPaint.setStyle(Paint.Style.STROKE);
        saturationPaint.setStrokeCap(Paint.Cap.ROUND);

        defaultPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        defaultPaint.setColor(Color.GRAY);
        defaultPaint.setStrokeWidth(mCircleLineStrokeWidth);
        defaultPaint.setStyle(Paint.Style.STROKE);
        defaultPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        int netWidth = w - getPaddingLeft() - getPaddingRight();
        int netHeight = h - getPaddingTop() - getPaddingBottom();
        radius = Math.min(netWidth, netHeight) * 0.5f;
        if (radius < 0) return;
        centerX = w * 0.5f;
        centerY = h * 0.5f;

        Shader sweepGradient = new SweepGradient(centerX, centerY,
                new int[]{Color.YELLOW, Color.CYAN, Color.CYAN, Color.YELLOW, Color.RED, Color.RED, Color.YELLOW},
                null);
        saturationPaint.setShader(sweepGradient);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //canvas.drawCircle(centerX, centerY, radius, saturationPaint);
        canvas.save();
        // 360 * Math.PI / 180
        float evenryDegrees = (float) (2.0f * Math.PI / mDottedLineCount);
        float startDegress = (float) (start_degrees * Math.PI / 180);
        float endDegress = (float) (stop_degrees * Math.PI / 180);
        for (int i = 0; i < mDottedLineCount; i++) {
            float degrees = i * evenryDegrees;

            float startX = centerX + (float) Math.sin(degrees) * (radius - mDottedLineWidth);
            float startY = centerX - (float) Math.cos(degrees) * (radius - mDottedLineWidth);

            float stopX = centerX + (float) Math.sin(degrees) * radius;
            float stopY = centerX - (float) Math.cos(degrees) * radius;
            if (degrees > startDegress || degrees < endDegress) {
                canvas.drawLine(startX, startY, stopX, stopY, defaultPaint);
                continue;
            }
            canvas.drawLine(startX, startY, stopX, stopY, saturationPaint);
        }

        //canvas.rotate(mStartAngle, mCenterPoint.x, mCenterPoint.y);

        // 第一个参数 oval 为 RectF 类型，即圆弧显示区域
        // startAngle 和 sweepAngle  均为 float 类型，分别表示圆弧起始角度和圆弧度数
        // 3点钟方向为0度，顺时针递增
        // 如果 startAngle < 0 或者 > 360,则相当于 startAngle % 360
        // useCenter:如果为True时，在绘制圆弧时将圆心包括在内，通常用来绘制扇形
        canvas.restore();
    }

    public void showProgress(double progress) {
        if (progress < 0 || progress > 180)
            return;
        start_degrees = 180 + (int) progress;
        stop_degrees = 180 - (int) progress;
        postInvalidate();
    }
}
