package com.lake.circlebtnseekview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class CircleTravel extends View {
    private float radius;
    private float centerX;
    private float centerY;
    private RectF rectF;
    private Paint saturationPaint;
    private float mCircleLineStrokeWidth = 20;

    public CircleTravel(Context context) {
        this(context, null);
    }

    public CircleTravel(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleTravel(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        saturationPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        saturationPaint.setColor(Color.RED);
        saturationPaint.setStrokeWidth(mCircleLineStrokeWidth);
        saturationPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        int netWidth = w - getPaddingLeft() - getPaddingRight();
        int netHeight = h - getPaddingTop() - getPaddingBottom();
        radius = Math.min(netWidth, netHeight) * 0.5f;
        if (radius < 0) return;
        centerX = w * 0.5f;
        centerY = h * 0.5f;
        rectF = new RectF();
        rectF.left = mCircleLineStrokeWidth / 2 ; // 左上角x
        rectF.top = mCircleLineStrokeWidth / 2 ; // 左上角y
        rectF.right = w - mCircleLineStrokeWidth / 2 ; // 左下角x
        rectF.bottom = h - mCircleLineStrokeWidth / 2 ; // 右下角y
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawArc(rectF, 0, 360, false,saturationPaint);
    }
}
