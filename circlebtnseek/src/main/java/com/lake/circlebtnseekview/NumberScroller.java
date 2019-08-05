package com.lake.circlebtnseekview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.NumberPicker;

public class NumberScroller extends NumberPicker {

    private boolean scrollable = true;

    public NumberScroller(Context context) {
        super(context);
    }

    public NumberScroller(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NumberScroller(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Log.e("lake", "onLayout: " + getChildCount());
        View view = getChildAt(0);
        view.setScaleX(1.5f);
        view.setScaleY(1.5f);
        ((EditText)view).setTextColor(Color.parseColor("#3b4664"));
        Spannable spn = ((EditText)view).getText();
        spn.setSpan(new StyleSpan(Typeface.BOLD), ((EditText)view).getSelectionStart(), ((EditText)view).getSelectionEnd(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (!scrollable)
            return true;
        return super.dispatchTouchEvent(event);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (child instanceof EditText) {
            child.setFocusable(false);
            child.setClickable(false);
            ((EditText) child).setTextSize(32);
            ((EditText) child).setTextColor(Color.parseColor("#d4d6db"));
        }
        super.addView(child, index, params);
    }

    public void setScrollable(boolean able) {
        this.scrollable = able;
    }
}
