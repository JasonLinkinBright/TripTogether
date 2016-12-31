package com.jason.trip.followship;

import android.content.Context;

import com.jason.trip.R;
import com.jason.trip.content.ResourceWriterManager;
import com.jason.trip.network.api.UserInfo;
import com.jason.trip.util.ToastUtils;

/**
 * Version V1.0 <Trip客户端>
 * ClassName:FollowUserManager
 * Description:
 * Created by Jason on 16/12/19.
 */

public class FollowUserManager extends ResourceWriterManager<FollowUserWriter> {

    private static class InstanceHolder {
        public static final FollowUserManager VALUE = new FollowUserManager();
    }

    public static FollowUserManager getInstance() {
        return InstanceHolder.VALUE;
    }

    /**
     * @deprecated Use {@link #write(UserInfo, boolean, Context)} instead.
     */
    public void write(String userIdOrUid, boolean like, Context context) {
        add(new FollowUserWriter(userIdOrUid, like, this), context);
    }

    public boolean write(UserInfo userInfo, boolean like, Context context) {
        if (shouldWrite(userInfo, context)) {
            add(new FollowUserWriter(userInfo, like, this), context);
            return true;
        } else {
            return false;
        }
    }

    private boolean shouldWrite(UserInfo userInfo, Context context) {
        if (userInfo.isOneself(context)) {
            ToastUtils.show(R.string.user_follow_error_cannot_follow_oneself, context);
            return false;
        } else {
            return true;
        }
    }

    public boolean isWriting(String userIdOrUid) {
        return findWriter(userIdOrUid) != null;
    }

    public boolean isWritingFollow(String userIdOrUid) {
        FollowUserWriter writer = findWriter(userIdOrUid);
        return writer != null && writer.isFollow();
    }

    private FollowUserWriter findWriter(String userIdOrUid) {
        for (FollowUserWriter writer : getWriters()) {
            if (writer.hasUserIdOrUid(userIdOrUid)) {
                return writer;
            }
        }
        return null;
    }
}

