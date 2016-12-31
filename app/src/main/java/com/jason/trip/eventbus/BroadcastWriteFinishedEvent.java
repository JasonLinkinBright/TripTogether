package com.jason.trip.eventbus;

public class BroadcastWriteFinishedEvent extends Event {

    public long broadcastId;

    public BroadcastWriteFinishedEvent(long broadcastId, Object source) {
        super(source);

        this.broadcastId = broadcastId;
    }
}
