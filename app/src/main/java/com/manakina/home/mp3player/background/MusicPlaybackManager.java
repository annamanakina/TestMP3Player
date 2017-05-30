package com.manakina.home.mp3player.background;


import android.content.Context;
import android.content.Intent;
import android.media.*;
import android.util.Log;
import android.widget.SeekBar;

import com.manakina.home.mp3player.interfaces.IManageMedia;
import com.manakina.home.mp3player.model.Song;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;


public class MusicPlaybackManager implements IManageMedia, MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnErrorListener {


    private static MusicPlaybackManager sMusicManager;
    private Context mContext;
    private MediaPlayer mediaPlayer;
    private ArrayList<Song> mSongs;
    private int currentSong;

    private int duration = 0;

    private boolean isShuffle = false;
    private boolean isRepeat = false;


    private MusicPlaybackManager(Context context) {
        mContext = context;

    }

    public static MusicPlaybackManager getInstance(Context context) {
        if (sMusicManager == null) {
            sMusicManager = new MusicPlaybackManager(context.getApplicationContext());
        }
        return sMusicManager;
    }


    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
      //  Toast.makeText(this, "music player failed", Toast.LENGTH_SHORT).show();
        if(mediaPlayer != null)
        {
            try{
                mediaPlayer.stop();
                mediaPlayer.release();
                Log.i("TAG", "onError try ");
            }finally {
                mediaPlayer = null;
                Log.i("TAG", "onError finally ");
            }
        }
        return false;
    }

    public void seekTo(int position) {
        mediaPlayer.seekTo(position);
        //  Log.i("TAG", "seekTo position " + position);
        //   Log.i("TAG", "seekTo getCurrentPosition() " + mediaPlayer.getCurrentPosition());
    }

    //@Override
    // public void onSeekComplete(MediaPlayer mp) {
    //  mediaPlayer.seekTo(currentPosition);
    //   Log.i("TAG", "onSeekComplete currentPosition " + currentPosition);
    // }

    enum AudioState {INIT_MEDIA, PREPARE_MEDIA, PLAY_MEDIA, PAUSE_MEDIA, STOP_MEDIA}
    AudioState mState = AudioState.STOP_MEDIA;



    public AudioState getAudioState(){
        return mState;
    }

    public ArrayList<Song> getPlaybackSongs(){
        return mSongs;
    }




    @Override
    public void setPlaylist(ArrayList<Song> list, int index) {
        //if list == null > restore from sharedpreferences

        //else
        mSongs = list;
        currentSong = index;
       // Log.i("TAG", "setPlaylist currentSong " + currentSong);
    }


    @Override
    public void play() {
        releaseMedia();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(android.media.AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(this);
        // mediaPlayer.setOnSeekCompleteListener(this);
        if (isRepeat) {
            mediaPlayer.setLooping(true);
        } else {
            mediaPlayer.setLooping(false);
        }
        try {
            String path = mSongs.get(currentSong).getPath();
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();

           // mediaPlayer.prepareAsync();
            } catch (IllegalStateException excep) {
          //  Log.i("TAG", "getCurrentPosition() IllegalStateException " + excep.getMessage());
            }
            catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mState = AudioState.PREPARE_MEDIA;
        mediaPlayer.start();
        duration = mediaPlayer.getDuration();
        // Log.i("TAG", "onPrepared duration " + duration);
        //  isReady = true;
        mState = AudioState.PLAY_MEDIA;
    }

    public int getDuration(){
        return duration;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
         //  Log.i("TAG", "onCompletion currentSong " + currentSong);
        if (currentSong >= mSongs.size() - 1) {
            releaseMedia();
            mState = AudioState.INIT_MEDIA;

        } else {
            nextSong();
            //   Log.i("TAG", "onCompletion nextSong ");
        }
    }


    @Override
    public void pause() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                mState = AudioState.PAUSE_MEDIA;
                mContext.sendBroadcast(new Intent(MusicPlaybackService.BROADCAST_UPDATE_CONTROLS)
                        .putExtra(MusicPlaybackService.ACTION_CHANGE, MusicPlaybackService.PAUSE_SONG));
             //   Log.i("TAG", "pause mState " + mState);
            } else {
                mediaPlayer.start();
                mState = AudioState.PLAY_MEDIA;
                mContext.sendBroadcast(new Intent(MusicPlaybackService.BROADCAST_UPDATE_CONTROLS)
                        .putExtra(MusicPlaybackService.ACTION_CHANGE, MusicPlaybackService.PLAY_SONG));
               // Log.i("TAG", "pause mState " + mState);
            }
        } else {
            play();
        }
    }


    @Override
    public void nextSong() {
        if (!isShuffle) {
            currentSong = (currentSong + 1) % mSongs.size();
        } else {
            Random rand = new Random();
            currentSong = rand.nextInt(mSongs.size());
        }
        if (currentSong < mSongs.size()) {
           // Log.i("TAG", "nextSong " + currentSong);

            mContext.sendBroadcast(new Intent(MusicPlaybackService.BROADCAST_UPDATE_CONTROLS)
                    .putExtra(MusicPlaybackService.ACTION_CHANGE, MusicPlaybackService.NEXT_SONG)
                    .putExtra(MusicPlaybackService.EXTRA_SONG, mSongs.get(currentSong)));
            play();
        } else {
            releaseMedia();
        }
    }

    @Override
    public void prevSong() {
        if (!isShuffle) {
            currentSong = (currentSong - 1) % mSongs.size();
            //  Log.i("TAG", "play " + isShuffle);
        } else {
            Random rand = new Random();
            currentSong = rand.nextInt(mSongs.size());
            // Log.i("TAG", "play " + isShuffle);
        }

        if (currentSong == -1) {
            currentSong = mSongs.size() - 1;
        }
        // Log.i("TAG", "prevSong currentSong " + currentSong);

        mContext.sendBroadcast(new Intent(MusicPlaybackService.BROADCAST_UPDATE_CONTROLS)
                .putExtra(MusicPlaybackService.ACTION_CHANGE, MusicPlaybackService.PREV_SONG)
                .putExtra(MusicPlaybackService.EXTRA_SONG, mSongs.get(currentSong)))  ;
        play();
    }


    @Override
    public void repeat() {
        if (!isRepeat) {
            isRepeat = true;
        } else {
            isRepeat = false;
        }
    }

    @Override
    public void shuffle() {
        if (!isShuffle) {
            isShuffle = true;
        } else {
            isShuffle = false;
        }
    }


    public int getCurrentPosition() {
        int currentPosition = 0;
        try {
            if (!mState.equals(AudioState.STOP_MEDIA)) {
                currentPosition = mediaPlayer.getCurrentPosition();
                //  Log.i("TAG", "getCurrentPosition() currentPosition - " + currentPosition);
            }
        } catch (IllegalStateException excep){
            currentPosition = mediaPlayer.getCurrentPosition();
            // Log.i("TAG", "getCurrentPosition() IllegalStateException currentPosition " + currentPosition);
            Log.i("TAG", "getCurrentPosition() IllegalStateException " + excep.getMessage());
        }

        return currentPosition;
    }



    public void releaseMedia() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mState = AudioState.STOP_MEDIA;
            mediaPlayer.release();
            mediaPlayer = null;
        }

    }


}
