

/*
 * Copyright (c) 2016 Jason <jasonlinkinbright@gmail.com>
 * All Rights Reserved
 */

package com.jason.trip.home;

import android.content.Context;

import com.jason.trip.R;
import com.jason.trip.content.ResourceWriterManager;
import com.jason.trip.network.api.Broadcast;
import com.jason.trip.util.ToastUtils;


public class RebroadcastBroadcastManager extends ResourceWriterManager<RebroadcastBroadcastWriter> {

    private static class InstanceHolder {
        public static final RebroadcastBroadcastManager VALUE = new RebroadcastBroadcastManager();
    }

    public static RebroadcastBroadcastManager getInstance() {
        return InstanceHolder.VALUE;
    }

    /**
     * @deprecated Use {@link #write(Broadcast, boolean, Context)} instead.
     */
    public void write(long broadcastId, boolean rebroadcast, Context context) {
        add(new RebroadcastBroadcastWriter(broadcastId, rebroadcast, this), context);
    }

    public boolean write(Broadcast broadcast, boolean rebroadcast, Context context) {
        if (shouldWrite(broadcast, context)) {
            add(new RebroadcastBroadcastWriter(broadcast, rebroadcast, this), context);
            return true;
        } else {
            return false;
        }
    }

    private boolean shouldWrite(Broadcast broadcast, Context context) {
        if (broadcast.isAuthorOneself(context)) {
            ToastUtils.show(R.string.broadcast_rebroadcast_error_cannot_rebroadcast_oneself,
                    context);
            return false;
        } else {
            return true;
        }
    }

    public boolean isWriting(long broadcastId) {
        return findWriter(broadcastId) != null;
    }

    public boolean isWritingRebroadcast(long broadcastId) {
        RebroadcastBroadcastWriter writer = findWriter(broadcastId);
        return writer != null && writer.isRebroadcast();
    }

    private RebroadcastBroadcastWriter findWriter(long broadcastId) {
        for (RebroadcastBroadcastWriter writer : getWriters()) {
            if (writer.getBroadcastId() == broadcastId) {
                return writer;
            }
        }
        return null;
    }
}
