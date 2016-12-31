/*
 * Copyright (c) 2016 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package com.jason.trip.eventbus;


import com.jason.trip.network.api.UserInfo;

public class UserInfoUpdatedEvent extends Event {

    public UserInfo userInfo;

    public UserInfoUpdatedEvent(UserInfo userInfo, Object source) {
        super(source);

        this.userInfo = userInfo;
    }
}
