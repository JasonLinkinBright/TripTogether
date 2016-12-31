/*
 * Copyright (c) 2016 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package com.jason.trip.content;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.jason.trip.followship.FollowUserManager;
import com.jason.trip.home.LikeBroadcastManager;
import com.jason.trip.home.RebroadcastBroadcastManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;



public class ResourceWriterService extends Service {

    private List<ResourceWriterManager> mWriterManagers = new ArrayList<>();

    public static Intent makeIntent(Context context) {
        return new Intent(context, ResourceWriterService.class);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        addWriterManager(FollowUserManager.getInstance());
        addWriterManager(LikeBroadcastManager.getInstance());
        addWriterManager(RebroadcastBroadcastManager.getInstance());
//        addWriterManager(DeleteBroadcastManager.getInstance());
//        addWriterManager(DeleteBroadcastCommentManager.getInstance());
//        addWriterManager(SendBroadcastCommentManager.getInstance());
    }

    private void addWriterManager(ResourceWriterManager writerManager) {
        mWriterManagers.add(writerManager);
        writerManager.onBind(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        removeWriterManagers();
    }

    private void removeWriterManagers() {
        Iterator<ResourceWriterManager> iterator = mWriterManagers.iterator();
        while (iterator.hasNext()) {
            ResourceWriterManager writerManager = iterator.next();
            writerManager.onUnbind();
            iterator.remove();
        }
    }
}
