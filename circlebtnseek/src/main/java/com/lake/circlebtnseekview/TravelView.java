package com.lake.circlebtnseekview;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DebugUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.NumberPicker;

import java.lang.reflect.Field;

public class TravelView extends FrameLayout {
    private long lastPassedEventTime = 0;
    private int minInterval = 100 / 60;//16ms
    private float radius;
    private float iconRadius;
    private float centerX;
    private float centerY;

    private final float paddingRadiusPx;
    private PointF currentPoint = new PointF();

    private TravelSeekBar seekBar;
    private NumberPicker numPicker;
    private CircleTravel palette;

    private OnCircleProgressChangeListener onCircleProgressChangeListener;

    public TravelView(Context context) {
        this(context, null);
    }

    public TravelView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TravelView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paddingRadiusPx = 20 * getResources().getDisplayMetrics().density;

        {
            FrameLayout.MarginLayoutParams layoutParams = new FrameLayout.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.setMargins(240, 240, 240, 240);
            numPicker = new NumberPicker(context);
            numPicker.setMaxValue(8);
            numPicker.setMinValue(0);
            numPicker.setDisplayedValues(new String[]{"32", "31", "30", "29", "28", "27", "26", "25", "24"});
            numPicker.setValue(4);
            setNumberPickerDividerColor(numPicker);
            addView(numPicker, layoutParams);
        }

        {
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            ImageView imageView = new ImageView(context);
            imageView.setBackgroundResource(R.mipmap.img_airconditioner_annulus_gray2);
            layoutParams.setMargins(80, 80, 80, 80);
            addView(imageView, layoutParams);
        }

        {
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            palette = new CircleTravel(context);
            int padding = (int) paddingRadiusPx;
            palette.setPadding(padding, padding, padding, padding);
            addView(palette, layoutParams);
        }


        {
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            seekBar = new TravelSeekBar(context);
            addView(seekBar, layoutParams);
        }

    }

    /**
     * 反射去掉横线
     *
     * @param numberPicker
     */
    private void setNumberPickerDividerColor(NumberPicker numberPicker) {
        NumberPicker picker = numberPicker;
        Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) {
                pf.setAccessible(true);
                try {
                    //设置分割线的颜色值
                    pf.set(picker, new ColorDrawable(Color.TRANSPARENT));
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
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
        radius = Math.min(netWidth, netHeight) * 0.5f - paddingRadiusPx;
        if (radius < 0) return;
        iconRadius = radius - 90;
        centerX = netWidth * 0.5f;
        centerY = netHeight * 0.5f;

        currentPoint.x = centerX + iconRadius;
        currentPoint.y = centerY;
        seekBar.setCurrentPoint(currentPoint);
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
        PointF pointF = seekBar.getCurrentPoint();
        float px = pointF.x;
        float py = pointF.y;
        if ((Math.abs(px - x) < 90) && (Math.abs(py - y) < 90))
            updateSelector(x, y);
    }

    private void updateSelector(float eventX, float eventY) {
        float x = eventX - centerX;
        float y = eventY - centerY;
        double r = Math.sqrt(x * x + y * y);
        if (r > iconRadius) {
            x *= iconRadius / r;
            y *= iconRadius / r;
        } else {
            x /= r / iconRadius;
            y /= r / iconRadius;
        }
        currentPoint.x = x + centerX;
        currentPoint.y = y + centerY;
//        seekBar.setCurrentPoint(currentPoint);
        double jiao = Math.toDegrees(Math.atan(Math.abs(x) / Math.abs(y)));
        if (currentPoint.y < centerY) {
            jiao = 180 - jiao;
        }
        seekBar.setCurrentPointAndRotation(currentPoint, (float) jiao);
        if (onCircleProgressChangeListener != null) {
            onCircleProgressChangeListener.OnCircleProgressChanged(jiao / 180);
        }
        double progress = jiao / 180 * 100;
        Log.e("lake", "updateSelector: 百分比：" + progress);
        numPicker.setValue(8 - (int) Math.round(jiao / 180 * 8));
        palette.showProgress(jiao);
        Log.e("lake", "updateSelector: 度数：" + jiao);
    }

    private boolean updateable() {
        long current = System.currentTimeMillis();
        if (current - lastPassedEventTime <= minInterval) {
            return false;
        }
        lastPassedEventTime = current;
        return true;
    }

    public interface OnCircleProgressChangeListener {
        void OnCircleProgressChanged(double point);
    }

    public void setOnColorChangeListener(OnCircleProgressChangeListener listener) {
        this.onCircleProgressChangeListener = listener;
    }
}
