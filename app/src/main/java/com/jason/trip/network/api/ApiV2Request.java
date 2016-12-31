/*
 * Copyright (c) 2016 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package com.jason.trip.network.api;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.jason.trip.network.api.credential.ApiCredential;

import java.lang.reflect.Type;



public class ApiV2Request<T> extends ApiRequest<T> {

    public ApiV2Request(int method, String url, Type type, Context context) {
        super(method, url, type, context);

        init();
    }

    public ApiV2Request(int method, String url, TypeToken<T> typeToken, Context context) {
        super(method, url, typeToken, context);

        init();
    }

    private void init() {
        addParam(ApiContract.Request.ApiV2.Base.API_KEY, ApiCredential.ApiV2.KEY);
    }
}
