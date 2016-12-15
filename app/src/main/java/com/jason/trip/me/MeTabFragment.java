package com.jason.trip.me;

import android.os.Bundle;

import com.jason.trip.base.framwork.BaseTabFragment;

/**
 * Version V1.0 <Trip客户端>
 * ClassName:MeTabFragment
 * Description:
 * Created by Jason on 16/9/13.
 */
public class MeTabFragment extends BaseTabFragment {
    public static final String TAG = "MeTabFragment";

    public static MeTabFragment newInstance() {

        Bundle args = new Bundle();

        MeTabFragment fragment = new MeTabFragment();
        fragment.setArguments(args);
        return fragment;
    }
    public MeTabFragment() {
    }
}
