package com.lake.circlebtnseekview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.NumberPicker;

import java.util.ArrayList;
import java.util.List;

public class NumberScroller extends NumberPicker {
    private Paint numpaint;
    private int Min = 16;
    private int Max = 32;
    private String[] nums;
    private float centerX;
    private float centerY;

    public NumberScroller(Context context) {
        super(context);
    }

    public NumberScroller(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NumberScroller(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initNums();
    }

    private void initNums() {
        List<String> list = new ArrayList<>();
        for (int i = Max; i > Min; i--) {
            list.add(i + "");
        }
        nums = list.toArray(nums);
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
        centerX = netWidth * 0.5f;
        centerY = netHeight * 0.5f;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }


}
