package com.jason.trip.custom.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.jason.trip.R;

/**
 * Version V1.0 <Trip客户端>
 * ClassName:intStateListImageView
 * Description:
 * Created by Jason on 16/12/15.
 */

public class TintStateListImageView extends AppCompatImageView {

    private ColorStateList mTintList;

    public TintStateListImageView(Context context) {
        super(context);

        init(getContext(), null, 0, 0);
    }

    public TintStateListImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(getContext(), attrs, 0, 0);
    }

    public TintStateListImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(getContext(), attrs, defStyleAttr, 0);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TintStateListImageView,
                defStyleAttr, defStyleRes);
        mTintList = a.getColorStateList(R.styleable.TintStateListImageView_tint);
        updateTintColorFilter();
        a.recycle();
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();

        updateTintColorFilter();
    }

    public void setColorFilter(ColorStateList tint) {
        mTintList = tint;
        updateTintColorFilter();
    }

    private void updateTintColorFilter() {
        if (mTintList != null) {
            int color = mTintList.getColorForState(getDrawableState(), mTintList.getDefaultColor());
            setColorFilter(color);
        }
    }
}
