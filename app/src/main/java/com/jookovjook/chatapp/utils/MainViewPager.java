package com.jookovjook.chatapp.utils;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

import java.lang.reflect.Field;

public class MainViewPager extends ViewPager {

    private int currentPage;

    public MainViewPager(Context context) {
        super(context);
        setMyScroller();
    }

    public MainViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        currentPage = this.getCurrentItem();
        if(currentPage == -1) setMyScroller();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        currentPage = this.getCurrentItem();
        return !(currentPage == -1) && super.onInterceptTouchEvent(event);
        // Never allow swiping to switch between pages
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Never allow swiping to switch between pages
        currentPage = this.getCurrentItem();
        return !(currentPage == -1) && super.onInterceptTouchEvent(event);
    }

    //down one is added for smooth scrolling

    private void setMyScroller() {
        try {
            Class<?> viewpager = ViewPager.class;
            Field scroller = viewpager.getDeclaredField("mScroller");
            scroller.setAccessible(true);
            scroller.set(this, new MyScroller(getContext()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class MyScroller extends Scroller {

        public MyScroller(Context context) {
            super(context, new DecelerateInterpolator());
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, 350 /*1 secs*/);
        }
    }
}
