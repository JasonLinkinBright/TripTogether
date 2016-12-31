/*
 * Copyright (c) 2016 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package com.jason.trip.link;

import android.content.Context;

import com.jason.trip.R;
import com.jason.trip.util.ToastUtils;


public class NotImplementedManager {

    private NotImplementedManager() {}

    public static void sendBroadcast(String topic, Context context) {
        if (!FrodoBridge.sendBroadcast(topic, context)) {
            UrlHandler.open("https://www.douban.com/#isay-cont", context);
        }
    }

    public static void showNotYetImplementedToast(Context context) {
        ToastUtils.show(R.string.not_yet_implemented, context);
    }
}
