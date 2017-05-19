package com.manakina.home.mp3player;

import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.manakina.home.mp3player.background.MusicPlaybackService;
import com.manakina.home.mp3player.fragments.BottomFragment;
import com.manakina.home.mp3player.fragments.FragmentSongs;
import com.manakina.home.mp3player.fragments.MusicManageFragments;
import com.manakina.home.mp3player.model.Song;
import com.manakina.home.mp3player.supportutils.ImageCache;


public class MainActivity extends AppCompatActivity implements FragmentSongs.OnUpdateFragmentControls{
    private static final String TAG = "MainActivity";

    private MusicManageFragments manageFragments;
    private BottomFragment bottomFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

       if (savedInstanceState == null) {
            initFragmentScreen();
            initFragmentBottom();

            final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
            // Use 1/8th of the available memory for this memory cache.
            final int cacheSize = maxMemory / 8;
            ImageCache.getInstance(cacheSize);

       } else {
            // restoring the previously created fragment
            // and getting the reference
            manageFragments = (MusicManageFragments) getSupportFragmentManager().getFragments().get(0);
            bottomFragment = (BottomFragment) getSupportFragmentManager().findFragmentByTag("BottomFragment");
       }



    }


    private void initFragmentScreen() {
        // Creating the ViewPager container fragment once
        manageFragments = new MusicManageFragments();
        final FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragmentSwipeContainer, manageFragments, "MusicManageFragments")
                .commit();
    }

    private void initFragmentBottom() {
        bottomFragment = new BottomFragment();
        final FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragmentBottomContainer, bottomFragment, "BottomFragment")
                .commit();
    }


    //save info if device were rotated
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }




    @Override
    public void onBackPressed() {
        if (!manageFragments.onBackPressed()) {
            // container Fragment or its associates couldn't handle the back pressed task
            // delegating the task to super class
            super.onBackPressed();
          //  Log.i("TAG", "MainActivity !manageFragments.onBackPressed()");
        } else {
            // carousel handled the back pressed task
            // do not call super
         //   Log.i("TAG", "MainActivity else onBackPressed");
        }
    }






    @Override
    public void onSongSelected(Song song) {
        //find fragment by id
        final FragmentManager fragmentManager = getSupportFragmentManager();
        BottomFragment fragment = (BottomFragment) fragmentManager
                .findFragmentById(R.id.fragmentBottomContainer);
        if (fragment != null) {
            //Log.i("TAG", "MainActivity onSongSelected song " + song.getTitle());
            fragment.updateControls(song);
        }
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {

            case R.id.action_stop_app:
                stopService(new Intent(this, MusicPlaybackService.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }




}
