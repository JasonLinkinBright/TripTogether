package com.jason.trip.base.framwork;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

/**
 * Version V1.0 <Trip客户端>
 * ClassName:BaseTabFragment
 * Description:
 * Created by Jason on 16/9/4.
 */
public class BaseTabFragment extends Fragment {
    public FragmentActivity fragmentActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.fragmentActivity = getActivity();
    }

    /**
     * Launch new activity by className
     * @param tClass activity
     */
    public void startActivity(Class<?> tClass){
        fragmentActivity.startActivity(new Intent(fragmentActivity,tClass));
    }

    /**
     * Launch new activity by intent
     * @param intent
     */
    public void startActivity(Intent intent){
        fragmentActivity.startActivity(intent);
    }


}
