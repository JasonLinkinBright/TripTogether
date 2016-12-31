/*
 * Copyright (c) 2016 Jason <jasonlinkinbright@gmail.com>
 * All Rights Reserved
 */

package com.jason.trip.network.api;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Authenticator;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HttpStack;
import com.jason.trip.account.AccountContract;
import com.jason.trip.account.AccountUtils;

import java.io.File;



public class Volley {

    private static final Object INSTANCE_LOCK = new Object();
    private static volatile Volley sInstance;

    private Authenticator mAuthenticator;
    private RequestQueue mRequestQueue;

    private Volley(Context context) {

        context = context.getApplicationContext();

        notifyActiveAccountChanged(context);

        mRequestQueue = newRequestQueue(context);
        mRequestQueue.start();
    }

    public static Volley peekInstance() {
        return sInstance;
    }

    public static Volley getInstance(Context context) {
        if (sInstance == null) {
            synchronized (INSTANCE_LOCK) {
                if (sInstance == null) {
                    sInstance = new Volley(context);
                }
            }
        }
        return sInstance;
    }

    /**
     * @see com.android.volley.toolbox.Volley#newRequestQueue(Context, HttpStack, int)
     */
    private static RequestQueue newRequestQueue(Context context) {
        RequestQueue queue = new RequestQueue(new DiskBasedCache(new File(context.getCacheDir(),
                "volley")), new BasicNetwork(new HurlStack()));
        queue.start();
        return queue;
    }

    public void notifyActiveAccountChanged(Context context) {
        context = context.getApplicationContext();
        mAuthenticator = new SynchronizedAndroidAuthenticator(context,
                AccountUtils.getActiveAccount(context), AccountContract.AUTH_TOKEN_TYPE, true);
    }

    public Authenticator getAuthenticator() {
        return mAuthenticator;
    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

    public <T> Request<T> addToRequestQueue(Request<T> request) {
        mRequestQueue.add(request);
        return request;
    }

    public void cancelRequests(Object tag) {
        mRequestQueue.cancelAll(tag);
    }
}
