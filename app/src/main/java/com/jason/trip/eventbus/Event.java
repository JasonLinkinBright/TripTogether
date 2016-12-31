/*
 * Copyright (c) 2016 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package com.jason.trip.eventbus;

public class Event {

    private Object mSource;

    public Event(Object source) {
        this.mSource = source;
    }

    public boolean isFromMyself(Object me) {
        return me == mSource;
    }
}
