package com.jason.trip.snr;

import android.os.Bundle;

import com.jason.trip.base.framwork.BaseTabFragment;

/**
 * Version V1.0 <Trip客户端>
 * ClassName:SnrTabFragment
 * Description:
 * Created by Jason on 16/9/13.
 */
public class SnrTabFragment extends BaseTabFragment {
    public static final String TAG = "SnrTabFragment";


    public static SnrTabFragment newInstance() {

        Bundle args = new Bundle();

        SnrTabFragment fragment = new SnrTabFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public SnrTabFragment() {
    }
}
