package com.jason.trip.custom.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;

import com.jason.trip.R;
import com.jason.trip.util.ViewUtils;

/**
 * Version V1.0 <Trip客户端>
 * ClassName: FriendlySwipeRefreshLayout
 * Description: 下拉刷新控件
 * Created by Jason on 16/12/15.
 */

public class FriendlySwipeRefreshLayout extends SwipeRefreshLayout {

    private static final int CIRCLE_DIAMETER_DP = 40;
    private static final int CIRCLE_DIAMETER_LARGE_DP = 56;
    private static final int CIRCLE_SHADOW_DP = 7;
    private static final int DEFAULT_CIRCLE_DISTANCE_DP = 64;

    private int mSize = DEFAULT;
    private int mCircleDiameter;
    private int mDefaultCircleDistance;

    private CanChildScrollUpCallback mCanChildScrollUpCallback;

    public FriendlySwipeRefreshLayout(Context context) {
        super(context);

        init(getContext(), null);
    }

    public FriendlySwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(getContext(), attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        updateCircleDiameter();
        mDefaultCircleDistance = ViewUtils.dpToPxInt(DEFAULT_CIRCLE_DISTANCE_DP, context);

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.FriendlySwipeRefreshLayout, 0, 0);
        int progressOffset = a.getDimensionPixelOffset(
                R.styleable.FriendlySwipeRefreshLayout_progressOffset, 0);
        int progressDistanceOffset = a.getDimensionPixelOffset(
                R.styleable.FriendlySwipeRefreshLayout_progressDistanceOffset, 0);
        a.recycle();

        if (progressOffset != 0 || progressDistanceOffset != 0) {
            setProgressViewOffset(progressOffset, progressDistanceOffset);
        }

        setColorSchemeColors(ViewUtils.getColorFromAttrRes(R.attr.colorPrimary, Color.BLACK,
                context));
    }

    @Override
    public void setSize(int size) {
        super.setSize(size);

        if (size == LARGE || size == DEFAULT) {
            mSize = size;
            updateCircleDiameter();
        }
    }

    private void updateCircleDiameter() {
        int circleDiameterDp = mSize == DEFAULT ? CIRCLE_DIAMETER_DP : CIRCLE_DIAMETER_LARGE_DP;
        circleDiameterDp += CIRCLE_SHADOW_DP;
        mCircleDiameter = ViewUtils.dpToPxInt(circleDiameterDp, getContext());
    }

    public void setProgressViewOffset(int offset, int distanceOffset) {
        int progressStart = offset - mCircleDiameter;
        int progressEnd = progressStart + mDefaultCircleDistance + distanceOffset;
        setProgressViewOffset(false, progressStart, progressEnd);
    }

    public void setProgressViewOffset(int offset) {
        setProgressViewOffset(offset, 0);
    }

    public CanChildScrollUpCallback getCanChildScrollUpCallback() {
        return mCanChildScrollUpCallback;
    }

    public void setCanChildScrollUpCallback(CanChildScrollUpCallback canChildScrollUpCallback) {
        mCanChildScrollUpCallback = canChildScrollUpCallback;
    }

    @Override
    public boolean canChildScrollUp() {
        if (mCanChildScrollUpCallback != null) {
            return mCanChildScrollUpCallback.canChildScrollUp();
        }
        return super.canChildScrollUp();
    }

    public interface CanChildScrollUpCallback {
        boolean canChildScrollUp();
    }
}
