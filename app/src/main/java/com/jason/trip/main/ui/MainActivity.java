package com.jason.trip.main.ui;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jason.trip.R;
import com.jason.trip.custom.view.CircleMenuLayout;

public class MainActivity extends AppCompatActivity {


    private CircleMenuLayout mCircleMenuLayout;

    private String[] mItemTexts = new String[]{"我的旅程", "导游请求", "导游服务",
            "一起旅游", "咨询服务", "咨询请求"};
    private int[] mItemImgs = new int[]{R.drawable.selector_bottom_bar_explore,
            R.drawable.selector_bottom_bar_explore, R.drawable.selector_bottom_bar_explore,
            R.drawable.selector_bottom_bar_explore, R.drawable.selector_bottom_bar_explore,
            R.drawable.selector_bottom_bar_explore};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.include_popup_layout);
        mCircleMenuLayout = (CircleMenuLayout) findViewById(R.id.id_menulayout);
        mCircleMenuLayout.setMenuItemIconsAndTexts(mItemImgs, mItemTexts);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Handler handler=new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {

            }
        });
    }
}
