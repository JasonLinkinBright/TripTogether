package com.jason.trip.custom.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

import com.jason.trip.util.ViewUtils;

/**
 * Version V1.0 <Trip客户端>
 * ClassName:AutoGoneTextView
 * Description:
 * Created by Jason on 16/12/15.
 */

public class AutoGoneTextView extends TextView {

    public AutoGoneTextView(Context context) {
        super(context);
    }

    public AutoGoneTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoGoneTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AutoGoneTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);

        ViewUtils.setVisibleOrGone(this, !TextUtils.isEmpty(text));
    }
}

