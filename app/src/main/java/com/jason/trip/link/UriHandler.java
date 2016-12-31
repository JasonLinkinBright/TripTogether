/*
 * Copyright (c) 2016 Jason <jasonlinkinbright@gmail.com>
 * All Rights Reserved
 */

package com.jason.trip.link;

import android.content.Context;
import android.net.Uri;

public class UriHandler {

    private UriHandler() {}

    public static void open(Uri uri, Context context) {
        if (DoubanUriHandler.open(uri, context)) {
            return;
        }
        if (FrodoBridge.openFrodoUri(uri, context)) {
            return;
        }
        UrlHandler.open(uri, context);
    }

    public static void open(String url, Context context) {
        open(Uri.parse(url), context);
    }
}
