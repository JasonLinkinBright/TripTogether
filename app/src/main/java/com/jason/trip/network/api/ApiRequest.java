/*
 * Copyright (c) 2016 Jason <jasonlinkinbright@gmail.com>
 * All Rights Reserved
 */

package com.jason.trip.network.api;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Authenticator;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.jason.trip.util.GsonHelper;
import com.jason.trip.util.LogUtils;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;



public class ApiRequest<T> extends Request<T> {

    private Type mType;
    private Authenticator mAuthenticator;
    private String mAuthToken;

    /**
     * @param context Only used for {@code Volley.getInstance(context).getAuthenticator()}.
     */
    public ApiRequest(int method, String url, Type type, Context context) {
        super(method, url);

        mType = type;
        mAuthenticator = Volley.getInstance(context).getAuthenticator();

        setRetryPolicy(new RetryPolicy(ApiContract.Request.Base.INITIAL_TIMEOUT_MS,
                ApiContract.Request.Base.MAX_NUM_RETRIES,
                ApiContract.Request.Base.BACKOFF_MULTIPLIER));
    }

    /**
     * @param context Only used for {@code Volley.getInstance(context).getAuthenticator()}.
     */
    public ApiRequest(int method, String url, TypeToken<T> typeToken, Context context) {
        this(method, url, typeToken.getType(), context);
    }

    @Override
    public void onPreparePerformRequest() throws AuthFailureError {
        setAuthorization();
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        Gson gson = GsonHelper.get();
        String responseString;
        try {
            responseString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
        try {
            return Response.success(gson.<T>fromJson(responseString, mType),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (JsonParseException | OutOfMemoryError e) {
            LogUtils.e("Error when parsing response: " + responseString);
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected ApiError parseNetworkError(VolleyError volleyError) {
        return ApiError.wrap(volleyError);
    }

    private void setAuthorization() throws AuthFailureError {
        mAuthToken = mAuthenticator.getAuthToken();
        addHeaderAuthorizationBearer(mAuthToken);
    }

    private class RetryPolicy extends DefaultRetryPolicy {

        public RetryPolicy(int initialTimeoutMs, int maxNumRetries, float backoffMultiplier) {
            super(initialTimeoutMs, maxNumRetries, backoffMultiplier);
        }

        @Override
        public void retry(VolleyError error) throws VolleyError {
            //noinspection ThrowableResultOfMethodCallIgnored
            ApiError apiError = ApiError.wrap(error);
            switch (apiError.code) {
                case ApiContract.Response.Error.Codes.Token.INVALID_ACCESS_TOKEN:
                case ApiContract.Response.Error.Codes.Token.ACCESS_TOKEN_HAS_EXPIRED:
                case ApiContract.Response.Error.Codes.Token.INVALID_REFRESH_TOKEN:
                case ApiContract.Response.Error.Codes.Token
                        .ACCESS_TOKEN_HAS_EXPIRED_SINCE_PASSWORD_CHANGED:
                    mAuthenticator.invalidateAuthToken(mAuthToken);
                    setAuthorization();
                    super.retry(error);
                    break;
                default:
                    // Don't retry on other error.
                    throw error;
            }
        }
    }
}
