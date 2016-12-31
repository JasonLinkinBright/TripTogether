/*
 * Copyright (c) 2016 Jason <jasonlinkinbright@gmail.com>
 * All Rights Reserved
 */

package com.jason.trip.network.api;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.jason.trip.network.api.credential.ApiCredential;

import java.lang.reflect.Type;



public class FrodoRequest<T> extends ApiRequest<T> {

    public FrodoRequest(int method, String url, Type type, Context context) {
        super(method, url, type, context);

        init();
    }

    public FrodoRequest(int method, String url, TypeToken<T> typeToken, Context context) {
        super(method, url, typeToken, context);

        init();
    }

    private void init() {

        addHeaderUserAgent(ApiContract.Request.Frodo.USER_AGENT);

        addParam(ApiContract.Request.Frodo.Base.API_KEY, ApiCredential.Frodo.KEY);
        addParam(ApiContract.Request.Frodo.Base.CHANNEL,
                ApiContract.Request.Frodo.Base.Channels.DOUBAN);
    }
}
