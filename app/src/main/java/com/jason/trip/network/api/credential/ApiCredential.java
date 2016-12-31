/*
 * Copyright (c) 2016 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package com.jason.trip.network.api.credential;


import com.jason.trip.app.TripApplication;

public final class ApiCredential {

    private ApiCredential() {}

    public static class Frodo {
        public static String KEY = HackyApiCredentialHelper.getApiKey();
        public static String SECRET = HackyApiCredentialHelper.getApiSecret();
    }

    public static class ApiV2 {
        public static String KEY = HackyApiCredentialHelper.getApiKey();
        public static String SECRET = HackyApiCredentialHelper.getApiSecret();
    }
}

class HackyApiCredentialHelper {

    public static String getApiKey() {
        //noinspection deprecation
        return ApiCredentialManager.getApiKey(TripApplication.getInstance());
    }

    public static String getApiSecret() {
        //noinspection deprecation
        return ApiCredentialManager.getApiSecret(TripApplication.getInstance());
    }
}
