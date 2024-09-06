package com.k.deeplinkingtesting.Drawer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;


public class ListenableScrollView extends ScrollView {
    private ScrollViewListener scrollViewListener = null;

    public ListenableScrollView(Context context) {
        super(context);
    }

    public ListenableScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public ListenableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setScrollViewListener(ScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        if (scrollViewListener != null) {
            boolean didReachEnd = false;
            View view = getChildAt(getChildCount() - 1);
            int diff = (view.getBottom() - (getHeight() + getScrollY()));
            int delta;
            ScrollDirection direction;
            if (oldl > l) {
                direction = ScrollDirection.LEFT;
                delta = oldl - l;
            } else if (oldl < l) {
                direction = ScrollDirection.RIGHT;
                delta = l - oldl;
            } else if (oldt > t) {
                direction = ScrollDirection.UP;
                delta = oldt - t;
            } else {
                direction = ScrollDirection.DOWN;
                delta = t - oldt;
            }

            if (diff == 0) {
                didReachEnd = true;
            }

            scrollViewListener.onScrollChanged(this, l, t, direction, delta, didReachEnd);
        }
    }

    public enum ScrollDirection {DOWN, UP, RIGHT, LEFT}

    private boolean mScrollable = true;

    public void setScrollingEnabled(boolean enabled) {
        mScrollable = enabled;
    }

    public boolean isScrollable() {
        return mScrollable;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // if we can scroll pass the event to the superclass
                if (mScrollable) return super.onTouchEvent(ev);
                // only continue to handle the touch event if scrolling enabled
                return mScrollable; // mScrollable is always false at this point
            default:
                return super.onTouchEvent(ev);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // Don't do anything with intercepted touch events if
        // we are not scrollable
        return mScrollable && super.onInterceptTouchEvent(ev);
    }
}