package com.jason.trip.index;


import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jason.trip.R;
import com.jason.trip.base.framwork.BaseTabFragment;
import com.jason.trip.custom.view.AddFloatingActionButton;
import com.jason.trip.explore.ExploreTabFragment;
import com.jason.trip.home.HomeTabFragment;
import com.jason.trip.me.MeTabFragment;
import com.jason.trip.snr.SnrTabFragment;

import java.util.HashMap;
import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Version V1.0 <Trip客户端>
 * ClassName:IndexActivity
 * Description:主页入口
 * Created by Jason on 16/9/4.
 */
public class IndexActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.iv_bottom_bar_home)
    ImageView ivBottomBarHome;
    @BindView(R.id.tv_bottom_bar_home)
    TextView tvBottomBarHome;
    @BindView(R.id.ll_bottom_bar_home)
    LinearLayout llBottomBarHome;
    @BindView(R.id.iv_bottom_bar_explore)
    ImageView ivBottomBarExplore;
    @BindView(R.id.tv_bottom_bar_explore)
    TextView tvBottomBarExplore;
    @BindView(R.id.ll_bottom_bar_explore)
    LinearLayout llBottomBarExplore;
    @BindView(R.id.iv_bottom_bar_snr)
    ImageView ivBottomBarSnr;
    @BindView(R.id.tv_bottom_bar_snr)
    TextView tvBottomBarSnr;
    @BindView(R.id.ll_bottom_bar_snr)
    LinearLayout llBottomBarSnr;
    @BindView(R.id.iv_bottom_bar_me)
    ImageView ivBottomBarMe;
    @BindView(R.id.tv_bottom_bar_me)
    TextView tvBottomBarMe;
    @BindView(R.id.ll_bottom_bar_me)
    LinearLayout llBottomBarMe;
    @BindView(R.id.bottom_bar_center_btn)
    AddFloatingActionButton bottomBarCenterBtn;
    @BindView(R.id.btn_ck)
    FrameLayout btnCk;
    @BindView(R.id.content)
    FrameLayout content;

    //中心加号按钮是否打开
    private boolean fabOpened = false;
    private static final int TAB_COUNT = 4;
    private String mCurrentTabStr;
    private String mCurrentLayoutStr;
    private View mSelectTabView;
    private LinearLayout mSelectedLayout;
    private BaseTabFragment mTabFragment;
    private HashMap<String, BaseTabFragment> mTabs;
    private LinkedHashMap<String, LinearLayout> mLayouts;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        ButterKnife.bind(this);
        initListener();

        mTabs = new HashMap<>(TAB_COUNT);
        mLayouts = new LinkedHashMap<>(TAB_COUNT);
        if (savedInstanceState == null) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            mTabs.put(HomeTabFragment.TAG, (BaseTabFragment) fragmentManager.findFragmentByTag(HomeTabFragment.TAG));
            mTabs.put(ExploreTabFragment.TAG, (BaseTabFragment) fragmentManager.findFragmentByTag(ExploreTabFragment.TAG));
            mTabs.put(SnrTabFragment.TAG, (BaseTabFragment) fragmentManager.findFragmentByTag(SnrTabFragment.TAG));
            mTabs.put(MeTabFragment.TAG, (BaseTabFragment) fragmentManager.findFragmentByTag(MeTabFragment.TAG));

            mLayouts.put(HomeTabFragment.TAG, llBottomBarHome);
            mLayouts.put(ExploreTabFragment.TAG, llBottomBarExplore);
            mLayouts.put(SnrTabFragment.TAG, llBottomBarSnr);
            mLayouts.put(MeTabFragment.TAG, llBottomBarMe);
        }

        llBottomBarHome.performClick();


    }

    private void initListener() {
        bottomBarCenterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!fabOpened) {
                    openMenu(view);
                } else {
                    closeMenu(view);
                }
            }
        });
        llBottomBarHome.setOnClickListener(this);
        llBottomBarExplore.setOnClickListener(this);
        llBottomBarSnr.setOnClickListener(this);
        llBottomBarMe.setOnClickListener(this);
    }


    private void openMenu(View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "rotation", 0, -155, -135);
        animator.setDuration(500);
        animator.start();
        fabOpened = true;
    }

    private void closeMenu(View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "rotation", -135, 20, 0);
        animator.setDuration(500);
        animator.start();
        fabOpened = false;
    }


    @Override
    public void onClick(View view) {
        String tag = null;
        switch (view.getId()) {
            case R.id.ll_bottom_bar_home:
                tag = HomeTabFragment.TAG;
                mSelectedLayout = llBottomBarHome;
                break;
            case R.id.ll_bottom_bar_explore:
                tag = ExploreTabFragment.TAG;
                mSelectedLayout = llBottomBarExplore;
                break;
            case R.id.ll_bottom_bar_snr:
                tag = SnrTabFragment.TAG;
                mSelectedLayout = llBottomBarSnr;
                break;
            case R.id.ll_bottom_bar_me:
                tag = MeTabFragment.TAG;
                mSelectedLayout = llBottomBarMe;
                break;
        }
        onSelectTab(tag);
        changeBarStatus(tag);

    }

    private void onSelectTab(String tag) {
        if (tag.equals(mCurrentTabStr)) {
            // TODO:SOME REFRESH WORK
            return;
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        hideFragments(transaction);

        BaseTabFragment selectedTab = mTabs.get(tag);
        if (selectedTab == null) {
            selectedTab = createTabFragment(tag);
            mTabs.put(tag, selectedTab);
            transaction.add(R.id.content, selectedTab, tag);
        } else {
            transaction.show(selectedTab);
        }

        transaction.commit();
        mCurrentTabStr = tag;
        mTabFragment = selectedTab;
    }

    /**
     * 将所有Fragment都置为隐藏状态
     */

    private void hideFragments(FragmentTransaction transaction) {
        if (mTabs.get(HomeTabFragment.TAG) != null) {
            transaction.hide(mTabs.get(HomeTabFragment.TAG));
        }
        if (mTabs.get(ExploreTabFragment.TAG) != null) {
            transaction.hide(mTabs.get(ExploreTabFragment.TAG));
        }

        if (mTabs.get(SnrTabFragment.TAG) != null) {
            transaction.hide(mTabs.get(SnrTabFragment.TAG));
        }
        if (mTabs.get(MeTabFragment.TAG) != null) {
            transaction.hide(mTabs.get(MeTabFragment.TAG));
        }
    }

    private BaseTabFragment createTabFragment(String tag) {
        BaseTabFragment tabFragment;

        switch (tag) {
            case HomeTabFragment.TAG:
                tabFragment = HomeTabFragment.newInstance();
                break;
            case ExploreTabFragment.TAG:
                tabFragment = ExploreTabFragment.newInstance();
                break;
            case SnrTabFragment.TAG:
                tabFragment = SnrTabFragment.newInstance();
                break;
            case MeTabFragment.TAG:
                tabFragment = MeTabFragment.newInstance();
                break;
            default:
                tabFragment = HomeTabFragment.newInstance();
                break;
        }
        return tabFragment;
    }

    private void changeBarStatus(String tag) {

        if (mCurrentLayoutStr != null) {
            mLayouts.get(mCurrentLayoutStr).getChildAt(0).setSelected(false);
            ((TextView) mLayouts.get(mCurrentLayoutStr).getChildAt(1)).setTextColor(ContextCompat.getColor(IndexActivity.this, R.color.textLightFF99999));
        }
        mLayouts.get(tag).getChildCount();
        mLayouts.get(tag).getChildAt(0).setSelected(true);
        ((TextView) mLayouts.get(tag).getChildAt(1)).setTextColor(ContextCompat.getColor(IndexActivity.this, R.color.colorPrimary));

        mCurrentLayoutStr = tag;
    }


}
