package com.jason.trip.custom.view;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Version V1.0 <Trip客户端>
 * ClassName:NestedRatioHeightRecyclerView
 * Description:
 * Created by Jason on 16/12/15.
 */

public class NestedRatioHeightRecyclerView extends RatioHeightRecyclerView {

    public NestedRatioHeightRecyclerView(Context context) {
        super(context);

        init();
    }

    public NestedRatioHeightRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public NestedRatioHeightRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        init();
    }

    private void init() {
        setFocusableInTouchMode(false);
    }
}

