/*
 * Copyright (c) 2016 Jason <jasonlinkinbright@gmail.com>
 * All Rights Reserved
 */

package com.jason.trip.util;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {

    private ToastUtils() {}

    public static void show(CharSequence text, int duration, Context context) {
        Toast.makeText(context, text, duration).show();
    }
    public static void show(int resId, int duration, Context context) {
        show(context.getText(resId), duration, context);
    }

    public static void show(CharSequence text, Context context) {
        show(text, Toast.LENGTH_SHORT, context);
    }

    public static void show(int resId, Context context) {
        show(context.getText(resId), context);
    }

    public static void showLong(CharSequence text, Context context) {
        show(text, Toast.LENGTH_LONG, context);
    }

    public static void showLong(int resId, Context context) {
        showLong(context.getText(resId), context);
    }
}
