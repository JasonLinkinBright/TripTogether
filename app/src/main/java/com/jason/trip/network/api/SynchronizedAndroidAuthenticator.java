/*
 * Copyright (c) 2016 Jason <jasonlinkinbright@gmail.com>
 * All Rights Reserved
 */

package com.jason.trip.network.api;

import android.accounts.Account;
import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.toolbox.AndroidAuthenticator;

/**
 * An {@link AndroidAuthenticator} with {@link #getAuthToken()} synchronized.
 */
public class SynchronizedAndroidAuthenticator extends AndroidAuthenticator {

    public SynchronizedAndroidAuthenticator(Context context, Account account,
                                            String authTokenType) {
        super(context, account, authTokenType);
    }

    public SynchronizedAndroidAuthenticator(Context context, Account account, String authTokenType,
                                            boolean notifyAuthFailure) {
        super(context, account, authTokenType, notifyAuthFailure);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized String getAuthToken() throws AuthFailureError {
        return super.getAuthToken();
    }
}
