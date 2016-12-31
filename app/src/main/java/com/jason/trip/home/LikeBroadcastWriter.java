/*
 * Copyright (c) 2016 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package com.jason.trip.home;

import android.content.Context;
import android.support.annotation.Keep;

import com.android.volley.VolleyError;
import com.jason.trip.R;
import com.jason.trip.content.ResourceWriter;
import com.jason.trip.eventbus.BroadcastUpdatedEvent;
import com.jason.trip.eventbus.BroadcastWriteFinishedEvent;
import com.jason.trip.eventbus.BroadcastWriteStartedEvent;
import com.jason.trip.eventbus.EventBusUtils;
import com.jason.trip.network.api.ApiContract;
import com.jason.trip.network.api.ApiError;
import com.jason.trip.network.api.ApiRequests;
import com.jason.trip.network.api.Broadcast;
import com.jason.trip.network.api.Request;
import com.jason.trip.util.LogUtils;
import com.jason.trip.util.ToastUtils;


class LikeBroadcastWriter extends ResourceWriter<LikeBroadcastWriter, Broadcast> {

    private long mBroadcastId;
    private Broadcast mBroadcast;
    private boolean mLike;

    private LikeBroadcastWriter(long broadcastId, Broadcast broadcast, boolean like,
                                LikeBroadcastManager manager) {
        super(manager);

        mBroadcastId = broadcastId;
        mBroadcast = broadcast;
        mLike = like;

        EventBusUtils.register(this);
    }

    LikeBroadcastWriter(long broadcastId, boolean like, LikeBroadcastManager manager) {
        this(broadcastId, null, like, manager);
    }

    LikeBroadcastWriter(Broadcast broadcast, boolean like, LikeBroadcastManager manager) {
        this(broadcast.id, broadcast, like, manager);
    }

    public long getBroadcastId() {
        return mBroadcastId;
    }

    public boolean isLike() {
        return mLike;
    }

    @Override
    protected Request<Broadcast> onCreateRequest() {
        return ApiRequests.newLikeBroadcastRequest(mBroadcastId, mLike, getContext());
    }

    @Override
    public void onStart() {
        super.onStart();

        EventBusUtils.postAsync(new BroadcastWriteStartedEvent(mBroadcastId, this));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        EventBusUtils.unregister(this);
    }

    @Override
    public void onResponse(Broadcast response) {

        ToastUtils.show(mLike ? R.string.broadcast_like_successful
                : R.string.broadcast_unlike_successful, getContext());

        EventBusUtils.postAsync(new BroadcastUpdatedEvent(response, this));

        stopSelf();
    }

    @Override
    public void onErrorResponse(VolleyError error) {

        LogUtils.e(error.toString());
        Context context = getContext();
        ToastUtils.show(context.getString(mLike ? R.string.broadcast_like_failed_format
                        : R.string.broadcast_unlike_failed_format,
                ApiError.getErrorString(error, context)), context);

        boolean notified = false;
        if (mBroadcast != null && error instanceof ApiError) {
            // Correct our local state if needed.
            ApiError apiError = (ApiError) error;
            Boolean shouldBeLiked = null;
            if (apiError.code == ApiContract.Response.Error.Codes.LikeBroadcast.ALREADY_LIKED) {
                shouldBeLiked = true;
            } else if (apiError.code == ApiContract.Response.Error.Codes.LikeBroadcast.NOT_LIKED_YET) {
                shouldBeLiked = false;
            }
            if (shouldBeLiked != null) {
                mBroadcast.fixLiked(shouldBeLiked);
                EventBusUtils.postAsync(new BroadcastUpdatedEvent(mBroadcast, this));
                notified = true;
            }
        }
        if (!notified) {
            // Must notify to reset pending status. Off-screen items also needs to be invalidated.
            EventBusUtils.postAsync(new BroadcastWriteFinishedEvent(mBroadcastId, this));
        }

        stopSelf();
    }

    @Keep
    public void onEventMainThread(BroadcastUpdatedEvent event) {

        if (event.isFromMyself(this)) {
            return;
        }

        if (event.broadcast.id == mBroadcast.id) {
            mBroadcast = event.broadcast;
        }
    }
}
