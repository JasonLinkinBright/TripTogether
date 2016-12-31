/*
 * Copyright (c) 2016 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package com.jason.trip.network.api;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

// HACK: Silly Frodo API take requests with uid for invalid uri, because of a whitelist activated
// when access token is sent (actually a bug in the whitelist). As a hack we drop the access token
// when requesting with uid.
// FIXME: This will render friend-visible diaries invisible.
public class UserIdOrUidFrodoRequest<T> extends FrodoRequest<T> {

    private boolean mIsUid;

    public UserIdOrUidFrodoRequest(int method, String url, Type type, Context context) {
        super(method, url, type, context);
    }

    public UserIdOrUidFrodoRequest(int method, String url, TypeToken<T> typeToken,
                                   Context context) {
        super(method, url, typeToken, context);
    }

    public UserIdOrUidFrodoRequest<T> withUserIdOrUid(String userIdOrUid) {
        mIsUid = !userIdOrUid.matches("\\d+");
        return this;
    }

    @Override
    public void onPreparePerformRequest() throws AuthFailureError {
        if (!mIsUid) {
            super.onPreparePerformRequest();
        }
    }
}
