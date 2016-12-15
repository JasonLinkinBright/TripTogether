package com.jason.trip.custom.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.jason.trip.util.MathUtils;

/**
 * Version V1.0 <Trip客户端>
 * ClassName:RatioImageView
 * Description:
 * Created by Jason on 16/12/15.
 */

public class RatioImageView extends ImageView {

    private float mRatio;

    public RatioImageView(Context context) {
        super(context);

        init(getContext(), null, 0, 0);
    }

    public RatioImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(getContext(), attrs, 0, 0);
    }

    public RatioImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(getContext(), attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RatioImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init(getContext(), attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        setScaleType(ScaleType.CENTER_CROP);
    }

    public float getRatio() {
        return mRatio;
    }

    public void setRatio(float ratio) {
        if (mRatio != ratio) {
            mRatio = ratio;
            requestLayout();
            invalidate();
        }
    }

    public void setRatio(float width, float height) {
        setRatio(width / height);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        if (mRatio > 0) {
            if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY) {
                int height = MeasureSpec.getSize(heightMeasureSpec);
                int width = Math.round(mRatio * height);
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
                    width = MathUtils.clamp(width, getMinimumWidth(), getMaxWidth());
                    if (getAdjustViewBounds()) {
                        width = Math.min(width, getMeasuredWidth());
                    }
                }
                widthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
            } else {
                int width = MeasureSpec.getSize(widthMeasureSpec);
                int height = Math.round(width / mRatio);
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
                    height = MathUtils.clamp(height, getMinimumHeight(), getMaxHeight());
                    if (getAdjustViewBounds()) {
                        height = Math.min(height, getMeasuredHeight());
                    }
                }
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
            }
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}

