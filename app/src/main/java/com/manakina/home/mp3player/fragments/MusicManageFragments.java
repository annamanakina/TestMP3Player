package com.manakina.home.mp3player.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.manakina.home.mp3player.R;
import com.manakina.home.mp3player.interfaces.OnBackPressListener;
import com.manakina.home.mp3player.musicadapters.ViewPagerAdapter;


public class MusicManageFragments extends Fragment implements ViewPager.OnPageChangeListener {
    ViewPager mViewPager;
    ViewPagerAdapter adapter;
    private Toolbar mToolBar;
    private TabLayout mTabLayout;
    OnBackPressListener currentFragment;

    public MusicManageFragments() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragments_swipe, container, false);

        mToolBar = (Toolbar) rootView.findViewById(R.id.music_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolBar);
        mTabLayout = (TabLayout) rootView.findViewById(R.id.tabLayout);

        mViewPager = (ViewPager) rootView.findViewById(R.id.pagerSwipeFragm);
        mViewPager.setOnPageChangeListener(this);
       // Log.i("TAG", "MusicManageFragments onCreateView");
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
               adapter = new ViewPagerAdapter(getResources(), getChildFragmentManager());
                mViewPager.setAdapter(adapter);
                mTabLayout.setupWithViewPager(mViewPager);
       // Log.i("TAG", "MusicManageFragments onActivityCreated");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
      //  Log.i("TAG", "MusicManageFragments onAttach");
    }



    /**
     * Retrieve the currently visible Tab Fragment and propagate the onBackPressed callback
     *
     * @return true = if this fragment and/or one of its associates Fragment can handle the backPress
     */
    public boolean onBackPressed() {
        // currently visible tab Fragment    OnBackPressListener
        currentFragment = (OnBackPressListener) adapter.getRegisteredFragment(mViewPager.getCurrentItem());
        if (currentFragment != null) {
            return currentFragment.onBackPressed();
        }
        // this Fragment couldn't handle the onBackPressed call
        return false;
    }



    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }




}
