package com.example.hadad.towme.Others;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by hadad on 1/30/2017.
 */

public class InterceptViewPager extends ViewPager{
    public InterceptViewPager(Context context) {
        super(context);
    }

    public InterceptViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private boolean isPagingEnabled = true;


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return this.isPagingEnabled && super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return this.isPagingEnabled && super.onInterceptTouchEvent(event);
    }

    public void setPagingEnabled(boolean b) {
        this.isPagingEnabled = b;
    }
}
