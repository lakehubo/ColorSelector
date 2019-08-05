package com.lake.circlebtnseekview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class TravelSeekBar extends View {
    private Paint selectorPaint, defaultPaint;
    static final int SELECTOR_RADIUS_DP = 20;
    private float selectorRadiusPx = SELECTOR_RADIUS_DP * 3;
    private PointF currentPoint = new PointF();
    private final Bitmap arrowIcon;
    private final int iconW;
    private final int iconH;
    private volatile float rotation = 0f;//旋转角度

    public TravelSeekBar(Context context) {
        this(context, null);
    }

    public TravelSeekBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TravelSeekBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        selectorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        selectorPaint.setFilterBitmap(true);

        defaultPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        defaultPaint.setColor(Color.WHITE);
        defaultPaint.setStyle(Paint.Style.FILL);
        defaultPaint.setShadowLayer(5, 0, 4, Color.LTGRAY);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        arrowIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.img_airconditioner_twoarrows, options).copy(Bitmap.Config.ARGB_8888, true);
        arrowIcon.setDensity(getResources().getDisplayMetrics().densityDpi);

        iconW = arrowIcon.getWidth() / 2;
        iconH = arrowIcon.getHeight() / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(currentPoint.x, currentPoint.y, selectorRadiusPx * 1f, defaultPaint);
        canvas.rotate(rotation, currentPoint.x, currentPoint.y);
        canvas.drawBitmap(arrowIcon, currentPoint.x - iconW, currentPoint.y - iconH, selectorPaint);
    }

    /**
     * 设置位置和角度
     *
     * @param rotation
     */
    public void setCurrentPointAndRotation(PointF currentPoint, Float rotation) {
        this.currentPoint = currentPoint;
        if (rotation < 0) {
            this.rotation = rotation < -90f ? (rotation + 90f) : (rotation - 90f);
        } else {
            this.rotation = rotation > 90f ? -(rotation - 90f) : -(rotation + 90f);
        }
        invalidate();
    }

    public void setCurrentPoint(PointF currentPoint) {
        this.currentPoint = currentPoint;
        invalidate();
    }

    public PointF getCurrentPoint() {
        return currentPoint;
    }
}
