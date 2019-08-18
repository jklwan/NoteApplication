package com.chends.note.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.chends.note.R;


/**
 * 是否可以滑动的ViewPager
 */
public class CustomViewPager extends ViewPager {

    private boolean isScrollable = true;

    /**
     * 设置是否允许手势滑动
     *
     * @param isScrollable true为可以滑动/false为不可
     */
    public void setScrollable(boolean isScrollable) {
        this.isScrollable = isScrollable;
    }

    public boolean getScrollable() {
        return isScrollable;
    }

    public CustomViewPager(Context context) {
        this(context, null);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CustomViewPager);
        isScrollable = ta.getBoolean(R.styleable.CustomViewPager_isScrollable, true);
        ta.recycle();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isScrollable){
            return super.onTouchEvent(ev);
        } else {
            return false;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isScrollable){
            return super.onInterceptTouchEvent(ev);
        } else {
            return false;
        }
    }

    @Override
    public boolean canScrollHorizontally(int direction) {
        if (isScrollable){
            return super.canScrollHorizontally(direction);
        } else {
            return false;
        }
    }
}
