package com.lake.colorselecter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class ColorCircleView extends View {

    private float radius;
    private float centerX;
    private float centerY;

    private Paint huePaint;
    private Paint saturationPaint;

    public ColorCircleView(Context context) {
        this(context, null);
    }

    public ColorCircleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorCircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        huePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        saturationPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        int netWidth = w - getPaddingLeft() - getPaddingRight();
        int netHeight = h - getPaddingTop() - getPaddingBottom();
        radius = Math.min(netWidth, netHeight) * 0.5f;
        if (radius < 0) return;
        centerX = w * 0.5f;
        centerY = h * 0.5f;
        //扫描/梯度渲染
        Shader sweepGradient = new SweepGradient(centerX, centerY,
                new int[]{Color.RED, Color.MAGENTA, Color.BLUE, Color.CYAN, Color.GREEN, Color.YELLOW, Color.RED},
                null);
        huePaint.setShader(sweepGradient);

        //环形渲染
        Shader radialGradient = new RadialGradient(centerX, centerY, radius,
                Color.WHITE, 0x00FFFFFF, Shader.TileMode.CLAMP);
        saturationPaint.setShader(radialGradient);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(centerX, centerY, radius, huePaint);
        canvas.drawCircle(centerX, centerY, radius, saturationPaint);
    }
}