/*
 * Copyright (c) 2016 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package com.jason.trip.eventbus;


import com.jason.trip.network.api.Comment;

public class BroadcastCommentSentEvent extends Event {

    public long broadcastId;

    public Comment comment;

    public BroadcastCommentSentEvent(long broadcastId, Comment comment, Object source) {
        super(source);

        this.broadcastId = broadcastId;
        this.comment = comment;
    }
}
