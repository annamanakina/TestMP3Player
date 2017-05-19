package com.manakina.home.mp3player.fragments;

import android.support.v4.app.Fragment;
import android.util.Log;

import com.manakina.home.mp3player.interfaces.OnBackPressListener;


public class RootFragment extends Fragment implements OnBackPressListener {

    @Override
    public boolean onBackPressed() {
      //  Log.i("TAG", "class RootFragment onBackPressed");
        return new BackPressImpl(this).onBackPressed();
    }
}