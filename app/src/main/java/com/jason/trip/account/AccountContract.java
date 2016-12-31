package com.jason.trip.account;

import com.jason.trip.BuildConfig;

/**
 * Version V1.0 <豆芽——豆瓣客户端>
 * ClassName:AccountContract
 * Description:
 * Created by Jason on 16/12/16.
 */

public class AccountContract {

    public static final String ACCOUNT_TYPE = BuildConfig.APPLICATION_ID;

    public static final String AUTH_TOKEN_TYPE = BuildConfig.APPLICATION_ID;

    public static final String KEY_USER_NAME = BuildConfig.APPLICATION_ID + ".user_name";
    public static final String KEY_USER_ID = BuildConfig.APPLICATION_ID + ".user_id";
    public static final long INVALID_USER_ID = -1;
    public static final String KEY_REFRESH_TOKEN = BuildConfig.APPLICATION_ID
            + ".refresh_token";

    private AccountContract() {}
}