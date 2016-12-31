/*
 * Copyright (c) 2016 Jason <jasonlinkinbright@gmail.com>
 * All Rights Reserved
 */

package com.jason.trip.eventbus;


import com.jason.trip.network.api.Broadcast;

public class BroadcastUpdatedEvent extends Event {

    public Broadcast broadcast;

    public BroadcastUpdatedEvent(Broadcast broadcast, Object source) {
        super(source);

        this.broadcast = broadcast;
    }
}
