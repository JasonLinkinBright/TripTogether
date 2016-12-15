package com.jason.trip.custom.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

import com.jason.trip.util.LogUtils;
import com.jason.trip.util.TimeUtils;

import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.DateTimeParseException;

/**
 * Version V1.0 <Trip客户端>
 * ClassName:TimeTextView
 * Description:
 * Created by Jason on 16/12/15.
 */

public class TimeTextView extends TextView {

    private static final int UPDATE_TIME_TEXT_INTERVAL_MILLI = 30 * 1000;

    private final Runnable mUpdateTimeTextRunnable = new Runnable() {
        @Override
        public void run() {
            updateTimeText();
        }
    };

    private ZonedDateTime mTime;

    public TimeTextView(Context context) {
        super(context);
    }

    public TimeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TimeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TimeTextView(Context context, AttributeSet attrs, int defStyleAttr,
                        int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public ZonedDateTime getTime() {
        return mTime;
    }

    public void setTime(ZonedDateTime time) {
        mTime = time;
        updateTimeText();
    }

    /**
     * Should behave the same as {@link android.util.TimeUtils(String, Context)}.
     */
    public void setDoubanTime(String doubanTime) {
        try {
            setTime(TimeUtils.parseDoubanDateTime(doubanTime));
        } catch (DateTimeParseException e) {
            LogUtils.e("Unable to parse date time: " + doubanTime);
            e.printStackTrace();
            setText(doubanTime);
        }
    }

    private void updateTimeText() {
        removeCallbacks(mUpdateTimeTextRunnable);
        if (mTime != null) {
            setTimeText(formatTime(mTime));
            postDelayed(mUpdateTimeTextRunnable, UPDATE_TIME_TEXT_INTERVAL_MILLI);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (mTime != null) {
            updateTimeText();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        removeCallbacks(mUpdateTimeTextRunnable);
    }

    protected String formatTime(ZonedDateTime time) {
        return TimeUtils.formatDateTime(time, getContext());
    }

    protected void setTimeText(String timeText) {
        setText(timeText);
    }
}

