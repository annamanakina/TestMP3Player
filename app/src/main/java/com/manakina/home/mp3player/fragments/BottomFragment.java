package com.manakina.home.mp3player.fragments;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.manakina.home.mp3player.MainActivity;
import com.manakina.home.mp3player.R;
import com.manakina.home.mp3player.SongActivity;
import com.manakina.home.mp3player.background.MusicPlaybackManager;
import com.manakina.home.mp3player.background.MusicPlaybackService;
import com.manakina.home.mp3player.model.Song;



public class BottomFragment extends Fragment {

    public static final String EXTRA_PLAYLIST = "PlayList";

    private LinearLayout mSongLayout;
    private ImageView mImageView;
    private TextView mTitle; //, mBeginTime, mTotalTime;
    private SeekBar mSeekBar;
    private ImageButton mPlayPauseButton, mPlayPreviousButton, mPlayNextButton;

    //for updating seekbar
    private Handler mHandler = new Handler();
    private int mSeekBarPosition;



    public BottomFragment() {
    }

    Runnable updateProgress = new Runnable() {
        public void run() {
            mSeekBar.setProgress(mSeekBarPosition);

        }
    };


    private BroadcastReceiver mUpdateControlsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(MusicPlaybackService.BROADCAST_UPDATE_CONTROLS)) {
                mSeekBarPosition = intent.getIntExtra(MusicPlaybackService.PARAM_RESULT, MusicPlaybackService.DEFAULT);
                Log.i("TAG", "BottomFragment onReceive mSeekBarPosition - " + mSeekBarPosition);
                if (mSeekBarPosition > MusicPlaybackService.DEFAULT) {
                  //  Log.i("TAG", "BottomFragment onReceive mSeekBarPosition - " + mSeekBarPosition);
                    mHandler.post(updateProgress);
                }

                int action = intent.getIntExtra(MusicPlaybackService.ACTION_CHANGE, MusicPlaybackService.DEFAULT);
                if (action != MusicPlaybackService.DEFAULT) {
                   // Log.i("TAG", "BottomFragment onReceive action - " + action);
                    Song song = intent.getParcelableExtra(MusicPlaybackService.EXTRA_SONG);
                   // Log.i("TAG", "BottomFragment onReceive action song - " + song);
                    switch (action) {

                        case MusicPlaybackService.PREV_SONG:
                        case MusicPlaybackService.NEXT_SONG:
                            if (song != null) {
                                mTitle.setText(new StringBuilder().append(song.getArtist())
                                        .append("\r\n").append(song.getTitle()));
                            }
                            mPlayPauseButton.setImageResource(R.drawable.ic_action_pause);
                            break;

                        case MusicPlaybackService.PAUSE_SONG:
                            mPlayPauseButton.setImageResource(R.drawable.ic_action_play);
                            break;

                        case MusicPlaybackService.PLAY_SONG:
                            mPlayPauseButton.setImageResource(R.drawable.ic_action_pause);
                            break;

                    }
                }
            }

        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.bottom_layout, container, false);
        initSeekBar(rootView);
        initButtons(rootView);
        initOtherControls(rootView);

        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(MusicPlaybackService.BROADCAST_UPDATE_CONTROLS);
        getActivity().registerReceiver(mUpdateControlsReceiver, intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(mUpdateControlsReceiver);
    }

    private void initSeekBar(View view) {
        mSeekBar = (SeekBar) view.findViewById(R.id.seekbar);
        mSeekBar.setVisibility(SeekBar.VISIBLE);
        mSeekBar.setMax(0);
        mSeekBar.setProgress(0);
        mSeekBar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        mSeekBar.getThumb().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //  Log.i("TAG", "BottomFragment onProgressChanged - " + progress);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Log.i("TAG", "BottomFragment onStartTrackingTouch - " + seekBar.getProgress());
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mSeekBar.setProgress(seekBar.getProgress());
                getActivity().sendBroadcast(new Intent(MusicPlaybackService.BROADCAST_ACTION)
                        .putExtra(MusicPlaybackService.ACTION_CHANGE, MusicPlaybackService.SEEK_TO)
                        .putExtra(MusicPlaybackService.EXTRA_SEEKBAR, seekBar.getProgress())
                );

                //  Log.i("TAG", "BottomFragment onStopTrackingTouch - " + seekBar.getProgress());
            }
        });

    }

    private void initButtons(View view) {
        mPlayPauseButton = (ImageButton) view.findViewById(R.id.play_pause_music);
        mPlayPauseButton.setImageResource(R.drawable.ic_action_play);
        mPlayPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 getActivity().sendBroadcast(new Intent(MusicPlaybackService.BROADCAST_ACTION)
                        .putExtra(MusicPlaybackService.ACTION_CHANGE, MusicPlaybackService.PAUSE_SONG));
            }
        });

        mPlayPreviousButton = (ImageButton) view.findViewById(R.id.play_previous);
        mPlayPreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().sendBroadcast(new Intent(MusicPlaybackService.BROADCAST_ACTION)
                        .putExtra(MusicPlaybackService.ACTION_CHANGE, MusicPlaybackService.PREV_SONG));
            }
        });
        mPlayNextButton = (ImageButton) view.findViewById(R.id.play_next);
        mPlayNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //а если серсис не включен еще, то работать ничего не будет
                getActivity().sendBroadcast(new Intent(MusicPlaybackService.BROADCAST_ACTION)
                        .putExtra(MusicPlaybackService.ACTION_CHANGE, MusicPlaybackService.NEXT_SONG));
            }
        });
    }

    private void initOtherControls(View view) {
        mSongLayout = (LinearLayout) view.findViewById(R.id.song_layout);
       /* mSongLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/
        mImageView = (ImageView) view.findViewById(R.id.imageViewSong);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SongActivity.class)
                        .putExtra(EXTRA_PLAYLIST, MusicPlaybackManager.getInstance(getActivity()).getPlaybackSongs()));
            }
        });

        mTitle = (TextView) view.findViewById(R.id.textviewTitle);
    }


    public void updateControls(final Song song) {
        // Log.i("TAG", "BottomFragment updateControls end" + song.getTitle());
        mSeekBar.setMax(song.getDuration());
        mTitle.setText(new StringBuilder().append(song.getArtist()).append("\r\n").append(song.getTitle()));
        mPlayPauseButton.setImageResource(R.drawable.ic_action_pause);
    }



}
