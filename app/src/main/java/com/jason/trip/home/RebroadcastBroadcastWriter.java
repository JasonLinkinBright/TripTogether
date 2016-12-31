
/*
 * Copyright (c) 2016 Jason <jasonlinkinbright@gmail.com>
 * All Rights Reserved
 */

package com.jason.trip.home;

import android.content.Context;
import android.support.annotation.Keep;

import com.android.volley.VolleyError;
import com.jason.trip.R;
import com.jason.trip.content.ResourceWriter;
import com.jason.trip.content.ResourceWriterManager;
import com.jason.trip.eventbus.BroadcastDeletedEvent;
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


class RebroadcastBroadcastWriter extends ResourceWriter<RebroadcastBroadcastWriter, Broadcast> {

    private long mBroadcastId;
    private Broadcast mBroadcast;
    private boolean mRebroadcast;

    private RebroadcastBroadcastWriter(long broadcastId, Broadcast broadcast, boolean rebroadcast,
                                       ResourceWriterManager<RebroadcastBroadcastWriter> manager) {
        super(manager);

        mBroadcastId = broadcastId;
        mBroadcast = broadcast;
        mRebroadcast = rebroadcast;

        EventBusUtils.register(this);
    }

    RebroadcastBroadcastWriter(long broadcastId, boolean rebroadcast,
                               ResourceWriterManager<RebroadcastBroadcastWriter> manager) {
        this(broadcastId, null, rebroadcast, manager);
    }

    RebroadcastBroadcastWriter(Broadcast broadcast, boolean rebroadcast,
                               ResourceWriterManager<RebroadcastBroadcastWriter> manager) {
        this(broadcast.id, broadcast, rebroadcast, manager);
    }

    public long getBroadcastId() {
        return mBroadcastId;
    }

    public boolean isRebroadcast() {
        return mRebroadcast;
    }

    @Override
    protected Request<Broadcast> onCreateRequest() {
        return ApiRequests.newRebroadcastBroadcastRequest(mBroadcastId, mRebroadcast, getContext());
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

        ToastUtils.show(mRebroadcast ? R.string.broadcast_rebroadcast_successful
                : R.string.broadcast_unrebroadcast_successful, getContext());

        if (!mRebroadcast) {
            // Delete the rebroadcast broadcast by user. Must be done before we
            // update the broadcast so that we can retrieve rebroadcastId for the
            // old one.
            if (mBroadcast != null && mBroadcast.rebroadcastId != null) {
                EventBusUtils.postAsync(new BroadcastDeletedEvent(mBroadcast.rebroadcastId, this));
            }
        }

        EventBusUtils.postAsync(new BroadcastUpdatedEvent(response, this));

        stopSelf();
    }

    @Override
    public void onErrorResponse(VolleyError error) {

        LogUtils.e(error.toString());
        Context context = getContext();
        ToastUtils.show(context.getString(mRebroadcast ? R.string.broadcast_rebroadcast_failed_format
                        : R.string.broadcast_unrebroadcast_failed_format,
                ApiError.getErrorString(error, context)), context);

        boolean notified = false;
        if (mBroadcast != null && error instanceof ApiError) {
            // Correct our local state if needed.
            ApiError apiError = (ApiError) error;
            Boolean shouldBeRebroadcasted = null;
            if (apiError.code == ApiContract.Response.Error.Codes.RebroadcastBroadcast.ALREADY_REBROADCASTED) {
                shouldBeRebroadcasted = true;
            } else if (apiError.code == ApiContract.Response.Error.Codes.RebroadcastBroadcast.NOT_REBROADCASTED_YET) {
                shouldBeRebroadcasted = false;
            }
            if (shouldBeRebroadcasted != null) {
                mBroadcast.fixRebroadcasted(shouldBeRebroadcasted);
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
