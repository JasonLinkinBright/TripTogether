/*
 * Copyright (c) 2016 Jason <jasonlinkinbright@gmail.com>
 * All Rights Reserved
 */

package com.jason.trip.eventbus;

public class BroadcastDeletedEvent extends Event {

    public long broadcastId;

    public BroadcastDeletedEvent(long broadcastId, Object source) {
        super(source);

        this.broadcastId = broadcastId;
    }
}
