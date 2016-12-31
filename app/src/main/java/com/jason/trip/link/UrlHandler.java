/*
 * Copyright (c) 2016 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package com.jason.trip.link;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import com.jason.trip.settings.Settings;
import com.jason.trip.util.AppUtils;
import com.jason.trip.util.UrlUtils;

import org.chromium.customtabsclient.CustomTabsActivityHelper;


import static com.jason.trip.settings.Settings.OpenUrlWithMethod.CUSTOM_TABS;

public class UrlHandler {

    private static final CustomTabsActivityHelper.CustomTabsFallback sFallback =
            new CustomTabsActivityHelper.CustomTabsFallback() {
                @Override
                public void openUri(Activity activity, Uri uri) {
                    open(uri, activity, false);
                }
            };

    private UrlHandler() {}

    private static void open(Uri uri, Context context, boolean enableCustomTabs) {

        String scheme = uri.getScheme();
        if (!TextUtils.isEmpty(scheme)) {
            switch (uri.getScheme()) {
                case "http":
                case "https":
                case "ftp":
                    break;
                default:
                    UrlUtils.openWithIntent(uri, context);
                    return;
            }
        }

        switch (Settings.OPEN_URL_WITH_METHOD.getEnumValue(context)) {
            case CUSTOM_TABS:
                if (enableCustomTabs) {
                    Activity activity = AppUtils.getActivityFromContext(context);
                    if (activity != null) {
                        UrlUtils.openWithCustomTabs(uri, sFallback, activity);
                    }
                    break;
                }
                // Fall through!
            case WEBVIEW:
                UrlUtils.openWithWebViewActivity(uri, context);
                break;
            case INTENT:
                UrlUtils.openWithIntent(uri, context);
                break;
        }
    }

    public static void open(Uri uri, Context context) {
        open(uri, context, true);
    }

    public static void open(String url, Context context) {
        open(Uri.parse(url), context);
    }
}
