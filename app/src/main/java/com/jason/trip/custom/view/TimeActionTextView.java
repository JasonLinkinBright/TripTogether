package com.jason.trip.custom.view;

import android.content.Context;
import android.util.AttributeSet;

import com.jason.trip.R;
import com.jason.trip.util.LogUtils;
import com.jason.trip.util.TimeUtils;

import org.threeten.bp.format.DateTimeParseException;

/**
 * Version V1.0 <Trip客户端>
 * ClassName:TimeActionTextView
 * Description:
 * Created by Jason on 16/12/15.
 */

public class TimeActionTextView extends TimeTextView {

    private String mAction;

    public TimeActionTextView(Context context) {
        super(context);
    }

    public TimeActionTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TimeActionTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TimeActionTextView(Context context, AttributeSet attrs, int defStyleAttr,
                              int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void setDoubanTime(String doubanTime) {
        throw new UnsupportedOperationException("Use setDoubanTimeAndAction() instead.");
    }

    /**
     * Should behave the same as .
     */
    public void setDoubanTimeAndAction(String doubanTime, String action) {
        mAction = action;
        try {
            setTime(TimeUtils.parseDoubanDateTime(doubanTime));
        } catch (DateTimeParseException e) {
            LogUtils.e("Unable to parse date time: " + doubanTime);
            e.printStackTrace();
            setTimeText(doubanTime);
        }
    }

    @Override
    protected void setTimeText(String timeText) {
        setText(getContext().getString(R.string.broadcast_time_action_format, timeText, mAction));
    }
}
