package com.jason.trip.custom.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Version V1.0 <Trip客户端>
 * ClassName:FriendlyViewPager
 * Description:
 * Created by Jason on 16/12/19.
 */

public class FriendlyViewPager extends ViewPager {

    public FriendlyViewPager(Context context) {
        super(context);
    }

    public FriendlyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        try {
            return super.onInterceptTouchEvent(event);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        }
    }
}
