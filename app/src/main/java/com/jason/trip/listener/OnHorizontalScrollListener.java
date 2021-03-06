package com.jason.trip.listener;

import android.support.v7.widget.RecyclerView;

/**
 * Version V1.0 <豆芽——豆瓣客户端>
 * ClassName:onHor
 * Description:
 * Created by Jason on 16/12/18.
 */

public abstract class OnHorizontalScrollListener extends RecyclerView.OnScrollListener {

    @Override
    public final void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        if (!recyclerView.canScrollHorizontally(-1)) {
            onScrolledToLeft();
        } else if (!recyclerView.canScrollHorizontally(1)) {
            onScrolledToRight();
        } else if (dx < 0) {
            onScrolledLeft();
        } else if (dx > 0) {
            onScrolledRight();
        }
    }

    public void onScrolledLeft() {}

    public void onScrolledRight() {}

    public void onScrolledToLeft() {}

    public void onScrolledToRight() {}
}
