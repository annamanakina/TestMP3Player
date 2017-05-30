package com.manakina.home.mp3player;


import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.manakina.home.mp3player.fragments.SongFragment;



//activity for fragment with one song item
public class SongActivity extends AppCompatActivity {
    private SongFragment songFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_song);
      //  ArrayList<Song> list = getIntent().getParcelableArrayListExtra("PlayList");
        if (savedInstanceState == null) {
            initFragmentScreen();
        } else {
            songFragment = (SongFragment) getSupportFragmentManager().findFragmentByTag("SongFragment");
        }
    }


    private void initFragmentScreen() {
        // Creating the ViewPager container fragment once
        songFragment = new SongFragment();
        final FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_song_container, songFragment, "SongFragment")
                .commit();

    }



}
