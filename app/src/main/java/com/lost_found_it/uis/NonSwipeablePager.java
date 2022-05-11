package com.lost_found_it.uis;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import java.lang.reflect.Field;

public class NonSwipeablePager extends ViewPager {
    public NonSwipeablePager(@NonNull Context context) {
        super(context);
        setMyScroll();
    }

    public NonSwipeablePager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setMyScroll();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }

    private void setMyScroll() {
        try {
            Class<?> pager = ViewPager.class;
            Field mScroller = pager.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            mScroller.set(this, new MyScroll(getContext()));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static class MyScroll extends Scroller {
        public MyScroll(Context context) {
            super(context, new DecelerateInterpolator());
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, 0);
        }
    }
}
