package com.jason.trip.home;

import android.os.Bundle;
import android.support.annotation.Keep;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.android.volley.VolleyError;
import com.jason.trip.content.ResourceFragment;
import com.jason.trip.eventbus.BroadcastDeletedEvent;
import com.jason.trip.eventbus.BroadcastUpdatedEvent;
import com.jason.trip.eventbus.BroadcastWriteFinishedEvent;
import com.jason.trip.eventbus.BroadcastWriteStartedEvent;
import com.jason.trip.eventbus.EventBusUtils;
import com.jason.trip.network.api.ApiRequest;
import com.jason.trip.network.api.ApiRequests;
import com.jason.trip.network.api.Broadcast;
import com.jason.trip.network.api.RequestFragment;
import com.jason.trip.util.FragmentUtils;

import java.util.Collections;
import java.util.List;

/**
 * Version V1.0 <Trip客户端>
 * ClassName:BroadcastListResource
 * Description:
 * Created by Jason on 16/12/27.
 */

public class BroadcastListResource extends ResourceFragment
        implements RequestFragment.Listener<List<Broadcast>, BroadcastListResource.State> {

    private static final int DEFAULT_COUNT_PER_LOAD = 20;

    // Not static because we are to be subclassed.
    private final String KEY_PREFIX = getClass().getName() + '.';

    public final String EXTRA_USER_ID_OR_UID = KEY_PREFIX + "user_id_or_uid";
    public final String EXTRA_TOPIC = KEY_PREFIX + "topic";

    private String mUserIdOrUid;
    private String mTopic;

    private List<Broadcast> mBroadcastList;

    private boolean mCanLoadMore = true;
    private boolean mLoading;
    private boolean mLoadingMore;

    private static final String FRAGMENT_TAG_DEFAULT = BroadcastListResource.class.getName();

    private static BroadcastListResource newInstance(String userIdOrUid, String topic) {
        //noinspection deprecation
        BroadcastListResource resource = new BroadcastListResource();
        resource.setArguments(userIdOrUid, topic);
        return resource;
    }

    public static BroadcastListResource attachTo(String userIdOrUid, String topic,
                                                 FragmentActivity activity, String tag,
                                                 int requestCode) {
        return attachTo(userIdOrUid, topic, activity, tag, true, null, requestCode);
    }

    public static BroadcastListResource attachTo(String userIdOrUid, String topic,
                                                 FragmentActivity activity) {
        return attachTo(userIdOrUid, topic, activity, FRAGMENT_TAG_DEFAULT, REQUEST_CODE_INVALID);
    }

    public static BroadcastListResource attachTo(String userIdOrUid, String topic,
                                                 Fragment fragment, String tag, int requestCode) {
        return attachTo(userIdOrUid, topic, fragment.getActivity(), tag, false, fragment,
                requestCode);
    }

    public static BroadcastListResource attachTo(String userIdOrUid, String topic,
                                                 Fragment fragment) {
        return attachTo(userIdOrUid, topic, fragment, FRAGMENT_TAG_DEFAULT, REQUEST_CODE_INVALID);
    }

    private static BroadcastListResource attachTo(String userIdOrUid, String topic,
                                                  FragmentActivity activity, String tag,
                                                  boolean targetAtActivity, Fragment targetFragment,
                                                  int requestCode) {
        BroadcastListResource resource = FragmentUtils.findByTag(activity, tag);
        if (resource == null) {
            resource = newInstance(userIdOrUid, topic);
            if (targetAtActivity) {
                resource.targetAtActivity(requestCode);
            } else {
                resource.targetAtFragment(targetFragment, requestCode);
            }
            FragmentUtils.add(resource, activity, tag);
        }
        return resource;
    }

    /**
     * @deprecated Use {@code attachTo()} instead.
     */
    public BroadcastListResource() {}

    protected void setArguments(String userIdOrUid, String topic) {
        Bundle arguments = FragmentUtils.ensureArguments(this);
        arguments.putString(EXTRA_USER_ID_OR_UID, userIdOrUid);
        arguments.putString(EXTRA_TOPIC, topic);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        mUserIdOrUid = arguments.getString(EXTRA_USER_ID_OR_UID);
        mTopic = arguments.getString(EXTRA_TOPIC);
    }

    /**
     * @return Unmodifiable broadcast list, or {@code null}.
     */
    public List<Broadcast> get() {
        return mBroadcastList != null ? Collections.unmodifiableList(mBroadcastList) : null;
    }

    public boolean has() {
        return mBroadcastList != null;
    }

    public boolean isEmpty() {
        return mBroadcastList == null || mBroadcastList.isEmpty();
    }

    public boolean isLoading() {
        return mLoading;
    }

    public boolean isLoadingMore() {
        return mLoadingMore;
    }

    @Override
    public void onStart() {
        super.onStart();

        EventBusUtils.register(this);

        if (mBroadcastList == null || (mBroadcastList.isEmpty() && mCanLoadMore)) {
            loadOnStart();
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        EventBusUtils.unregister(this);
    }

    public void load(boolean loadMore, int count) {

        if (mLoading || (loadMore && !mCanLoadMore)) {
            return;
        }

        mLoading = true;
        mLoadingMore = loadMore;
        getListener().onLoadBroadcastListStarted(getRequestCode());

        Long untilId = null;
        if (loadMore && mBroadcastList != null) {
            int size = mBroadcastList.size();
            if (size > 0) {
                untilId = mBroadcastList.get(size - 1).id;
            }
        }
        ApiRequest<List<Broadcast>> request = ApiRequests.newBroadcastListRequest(mUserIdOrUid,
                mTopic, untilId, count, getActivity());
        State state = new State(loadMore, count);
        RequestFragment.startRequest(request, state, this);
    }

    public void load(boolean loadMore) {
        load(loadMore, DEFAULT_COUNT_PER_LOAD);
    }

    protected void loadOnStart() {
        load(false);
    }

    @Override
    public void onVolleyResponse(int requestCode, final boolean successful,
                                 final List<Broadcast> result, final VolleyError error,
                                 final State requestState) {
        postOnResumed(new Runnable() {
            @Override
            public void run() {
                onLoadFinished(successful, result, error, requestState.loadMore,
                        requestState.count);
            }
        });
    }

    private void onLoadFinished(boolean successful, List<Broadcast> broadcastList,
                                VolleyError error, boolean loadMore, int count) {

        mLoading = false;
        mLoadingMore = false;
        getListener().onLoadBroadcastListFinished(getRequestCode());

        if (successful) {
            mCanLoadMore = broadcastList.size() == count;
            if (loadMore) {
                mBroadcastList.addAll(broadcastList);
                getListener().onBroadcastListAppended(getRequestCode(),
                        Collections.unmodifiableList(broadcastList));
                for (Broadcast broadcast : broadcastList) {
                    EventBusUtils.postAsync(new BroadcastUpdatedEvent(broadcast, this));
                }
            } else {
                set(broadcastList);
            }
        } else {
            getListener().onLoadBroadcastListError(getRequestCode(), error);
        }
    }

    @Keep
    public void onEventMainThread(BroadcastUpdatedEvent event) {

        if (event.isFromMyself(this) || mBroadcastList == null) {
            return;
        }

        for (int i = 0, size = mBroadcastList.size(); i < size; ++i) {
            Broadcast broadcast = mBroadcastList.get(i);
            boolean changed = false;
            if (broadcast.id == event.broadcast.id) {
                mBroadcastList.set(i, event.broadcast);
                changed = true;
            } else if (broadcast.rebroadcastedBroadcast != null
                    && broadcast.rebroadcastedBroadcast.id == event.broadcast.id) {
                broadcast.rebroadcastedBroadcast = event.broadcast;
                changed = true;
            }
            if (changed) {
                getListener().onBroadcastChanged(getRequestCode(), i, mBroadcastList.get(i));
            }
        }
    }

    @Keep
    public void onEventMainThread(BroadcastDeletedEvent event) {

        if (event.isFromMyself(this) || mBroadcastList == null) {
            return;
        }

        for (int i = 0, size = mBroadcastList.size(); i < size; ) {
            Broadcast broadcast = mBroadcastList.get(i);
            if (broadcast.id == event.broadcastId
                    || (broadcast.rebroadcastedBroadcast != null
                    && broadcast.rebroadcastedBroadcast.id == event.broadcastId)) {
                mBroadcastList.remove(i);
                getListener().onBroadcastRemoved(getRequestCode(), i);
                --size;
            } else {
                ++i;
            }
        }
    }

    @Keep
    public void onEventMainThread(BroadcastWriteStartedEvent event) {

        if (event.isFromMyself(this) || mBroadcastList == null) {
            return;
        }

        for (int i = 0, size = mBroadcastList.size(); i < size; ++i) {
            Broadcast broadcast = mBroadcastList.get(i);
            if (broadcast.id == event.broadcastId
                    || (broadcast.rebroadcastedBroadcast != null
                    && broadcast.rebroadcastedBroadcast.id == event.broadcastId)) {
                getListener().onBroadcastWriteStarted(getRequestCode(), i);
            }
        }
    }

    @Keep
    public void onEventMainThread(BroadcastWriteFinishedEvent event) {

        if (event.isFromMyself(this) || mBroadcastList == null) {
            return;
        }

        for (int i = 0, size = mBroadcastList.size(); i < size; ++i) {
            Broadcast broadcast = mBroadcastList.get(i);
            if (broadcast.id == event.broadcastId
                    || (broadcast.rebroadcastedBroadcast != null
                    && broadcast.rebroadcastedBroadcast.id == event.broadcastId)) {
                getListener().onBroadcastWriteFinished(getRequestCode(), i);
            }
        }
    }

    protected void setLoading(boolean loading) {
        if (mLoading == loading) {
            return;
        }
        mLoading = loading;
        if (mLoading) {
            getListener().onLoadBroadcastListStarted(getRequestCode());
        } else {
            getListener().onLoadBroadcastListFinished(getRequestCode());
        }
    }

    protected void set(List<Broadcast> broadcastList) {
        mBroadcastList = broadcastList;
        getListener().onBroadcastListChanged(getRequestCode(),
                Collections.unmodifiableList(broadcastList));
    }

    private Listener getListener() {
        return (Listener) getTarget();
    }

    static class State {

        public boolean loadMore;
        public int count;

        public State(boolean loadMore, int count) {
            this.loadMore = loadMore;
            this.count = count;
        }
    }

    public interface Listener {
        void onLoadBroadcastListStarted(int requestCode);
        void onLoadBroadcastListFinished(int requestCode);
        void onLoadBroadcastListError(int requestCode, VolleyError error);
        /**
         * @param newBroadcastList Unmodifiable.
         */
        void onBroadcastListChanged(int requestCode, List<Broadcast> newBroadcastList);
        /**
         * @param appendedBroadcastList Unmodifiable.
         */
        void onBroadcastListAppended(int requestCode, List<Broadcast> appendedBroadcastList);
        void onBroadcastChanged(int requestCode, int position, Broadcast newBroadcast);
        void onBroadcastRemoved(int requestCode, int position);
        void onBroadcastWriteStarted(int requestCode, int position);
        void onBroadcastWriteFinished(int requestCode, int position);
    }
}

