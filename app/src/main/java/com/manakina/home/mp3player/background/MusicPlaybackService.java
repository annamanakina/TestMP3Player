package com.manakina.home.mp3player.background;


import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import com.manakina.home.mp3player.fragments.FragmentSongs;
import com.manakina.home.mp3player.model.Song;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MusicPlaybackService extends Service {

    public final static String BROADCAST_ACTION = "com.manakina.home.mp3player.BUTTON_ACTION";
    public final static String BROADCAST_UPDATE_CONTROLS = "com.manakina.home.mp3player.UPDATE_CONTROLS";
    /*public static final String ACTION_NEXT = "action_next";
    public static final String ACTION_PREV = "action_prev";
    public static final String ACTION_PAUSE = "action_pause";
    public static final String ACTION_PLAY = "action_play";*/

    public static final String EXTRA_SONG = "extra_song";
    public static final String ACTION_CHANGE = "action_change";

    public final static String PARAM_RESULT = "result";


    public static final int DEFAULT = -1;
    public static final int PREV_SONG = 0;
    public static final int PAUSE_SONG = 1;
    public static final int PLAY_SONG = 2;
    public static final int NEXT_SONG = 3;


    private ExecutorService executorService;
    private MusicPlaybackManager playbackManager;

    private BroadcastReceiver mActionReceiver;
    private boolean isRunning = false;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



    public void onCreate() {
        super.onCreate();
        mActionReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int action = intent.getIntExtra(ACTION_CHANGE, -1);
              //  Log.i("TAG", "BroadcastReceiver action from button click - " + action);
                switch (action) {

                    case PREV_SONG:
                        playbackManager.prevSong();
                      //  Log.i("TAG", "BroadcastReceiver action - prevSong");
                        break;

                    case PAUSE_SONG:
                        playbackManager.pause();
                       // Log.i("TAG", "BroadcastReceiver action - pause");
                        break;

                    case NEXT_SONG:
                        playbackManager.nextSong();
                     //   Log.i("TAG", "BroadcastReceiver action - nextSong");
                        break;


                }
            }
        };
        getApplicationContext().registerReceiver(mActionReceiver, new IntentFilter(BROADCAST_ACTION));

        playbackManager = MusicPlaybackManager.getInstance(getApplicationContext());
        executorService = Executors.newFixedThreadPool(1);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Log.i("TAG", "MusicPlaybackService onStartCommand flags: " + flags + ", startId: " + startId);
        if (intent != null) {
            ArrayList<Song> mSongs = intent.getParcelableArrayListExtra(FragmentSongs.EXTRA_SONG_LIST);
            int mCurrentPosition = intent.getIntExtra(FragmentSongs.EXTRA_POSITION, 0);
            playbackManager.setPlaylist(mSongs, mCurrentPosition);
            PlayMusicTaskThread taskThread = new PlayMusicTaskThread();
            isRunning = true;
            executorService.execute(taskThread); //playbackManager

        }

        return START_NOT_STICKY;
    }


    class PlayMusicTaskThread implements Runnable {
        public PlayMusicTaskThread() {
            playbackManager.play();
        }

        public void run() {
            while (isRunning) {
                try {

                    int currentPosition;
                    int duration;
                    do {
                            duration = playbackManager.getDuration();
                            currentPosition = playbackManager.getCurrentPosition();

                            if (duration > 0) {
                                getApplicationContext().sendBroadcast(new Intent(BROADCAST_UPDATE_CONTROLS)
                                        .putExtra(PARAM_RESULT, (int) (currentPosition * 100 / duration)));
                                Thread.sleep(20);
                            }



                    } while (currentPosition < duration);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                stop();
            }
        }

        void stop() {
            Thread.currentThread().interrupt();
            isRunning = false;
            stopSelf();
        }
    }




    @Override
    public void onDestroy() {
        super.onDestroy();
        playbackManager.releaseMedia();

        if (!executorService.isShutdown()) {
            executorService.shutdownNow();
            executorService = null;
        }
        getApplicationContext().unregisterReceiver(mActionReceiver);

    }


}
