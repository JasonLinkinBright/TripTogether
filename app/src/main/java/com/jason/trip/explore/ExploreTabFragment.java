package com.jason.trip.explore;

import android.os.Bundle;

import com.jason.trip.base.framwork.BaseTabFragment;

/**
 * Version V1.0 <Trip客户端>
 * ClassName:ExploreTabFragment
 * Description:
 * Created by Jason on 16/9/13.
 */
public class ExploreTabFragment extends BaseTabFragment {
    public static final String TAG = "ExploreTabFragment";

    public static ExploreTabFragment newInstance() {

        Bundle args = new Bundle();

        ExploreTabFragment fragment = new ExploreTabFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public ExploreTabFragment() {
    }
}
