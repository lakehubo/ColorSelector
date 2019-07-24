package com.lake.colorselecter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import java.util.Locale;
import static com.lake.colorselecter.Constants.SELECTOR_RADIUS_DP;

public class CircleSelectFrame extends FrameLayout {

    private long lastPassedEventTime = 0;
    private int minInterval = Constants.EVENT_MIN_INTERVAL;//16ms
    private float radius;
    private float centerX;
    private float centerY;

    private float selectorRadiusPx = SELECTOR_RADIUS_DP * 3;
    private PointF currentPoint = new PointF();
    private int currentColor = Color.MAGENTA;

    private ColorCircleSelector selector;
    private OnColorChangeListener onColorChangeListener;

    public CircleSelectFrame(Context context) {
        this(context, null);
    }

    public CircleSelectFrame(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleSelectFrame(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        selectorRadiusPx = SELECTOR_RADIUS_DP * getResources().getDisplayMetrics().density;

        {
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            ColorCircleView palette = new ColorCircleView(context);
            int padding = (int) selectorRadiusPx;
            palette.setPadding(padding, padding, padding, padding);
            addView(palette, layoutParams);
        }

        {
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            selector = new ColorCircleSelector(context);
            selector.setCurrentPointColor(getColorAtPoint(centerX, centerY));
            selector.setSelectorRadiusPx(selectorRadiusPx);
            addView(selector, layoutParams);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int maxWidth = MeasureSpec.getSize(widthMeasureSpec);
        int maxHeight = MeasureSpec.getSize(heightMeasureSpec);

        int width, height;
        width = height = Math.min(maxWidth, maxHeight);
        super.onMeasure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        int netWidth = w - getPaddingLeft() - getPaddingRight();
        int netHeight = h - getPaddingTop() - getPaddingBottom();
        radius = Math.min(netWidth, netHeight) * 0.5f - selectorRadiusPx;
        if (radius < 0) return;
        centerX = netWidth * 0.5f;
        centerY = netHeight * 0.5f;
        setColor(currentColor);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                if (updateable())
                    update(event);
                return true;
            case MotionEvent.ACTION_UP:
                update(event);
                return true;
        }
        return super.onTouchEvent(event);
    }

    public void update(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        boolean isTouchUpEvent = event.getActionMasked() == MotionEvent.ACTION_UP || event.getActionMasked() == MotionEvent.ACTION_MOVE;
        if (isTouchUpEvent) {
            selector.setCurrentPointColor(getColorAtPoint(x, y));
            currentColor = getColorAtPoint(x, y);
            if (onColorChangeListener != null)
                onColorChangeListener.OnColorChanged(colorHex(currentColor));
        }
        updateSelector(x, y);
    }

    private int getColorAtPoint(float eventX, float eventY) {
        float x = eventX - centerX;
        float y = eventY - centerY;
        double r = Math.sqrt(x * x + y * y);
        float[] hsv = {0, 0, 1};
        hsv[0] = (float) (Math.atan2(y, -x) / Math.PI * 180f) + 180;
        hsv[1] = Math.max(0f, Math.min(1f, (float) (r / radius)));
        return Color.HSVToColor(hsv);
    }

    public void setColor(String rgb) {
        int c = Integer.parseInt(rgb, 16);
        if (c != getColorValue())
            setColor(c);
    }

    public void setColor(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        float r = hsv[1] * radius;
        float radian = (float) (hsv[0] / 180f * Math.PI);
        float x = (float) (r * Math.cos(radian) + centerX);
        float y = (float) (-r * Math.sin(radian) + centerY);
        updateSelector(x, y);
        currentColor = color;
        selector.setCurrentPointColor(getColorAtPoint(x,y));
    }

    private void updateSelector(float eventX, float eventY) {
        float x = eventX - centerX;
        float y = eventY - centerY;
        double r = Math.sqrt(x * x + y * y);
        if (r > radius) {
            x *= radius / r;
            y *= radius / r;
        }
        currentPoint.x = x + centerX;
        currentPoint.y = y + centerY;
        selector.setCurrentPoint(currentPoint);
    }

    private boolean updateable() {
        long current = System.currentTimeMillis();
        if (current - lastPassedEventTime <= minInterval) {
            return false;
        }
        lastPassedEventTime = current;
        return true;
    }

    public int getColorValue() {
        return currentColor;
    }

    private String colorHex(int color) {
//        int a = Color.alpha(color);
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        return String.format(Locale.getDefault(), "%02X%02X%02X", r, g, b);
    }

    private int colorInt(String c) {
        int rgb = Integer.parseInt(c, 16);
        return rgb;
    }

    public interface OnColorChangeListener {
        void OnColorChanged(String rgb);
    }

    public void setOnColorChangeListener(OnColorChangeListener onColorChangeListener) {
        this.onColorChangeListener = onColorChangeListener;
    }
}
