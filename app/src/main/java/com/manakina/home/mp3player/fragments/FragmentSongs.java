package com.manakina.home.mp3player.fragments;

import android.app.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import java.util.ArrayList;
import com.manakina.home.mp3player.R;
import com.manakina.home.mp3player.background.MusicPlaybackManager;
import com.manakina.home.mp3player.background.MusicPlaybackService;
import com.manakina.home.mp3player.model.Song;
import com.manakina.home.mp3player.musicadapters.SongListAdapter;
import com.manakina.home.mp3player.supportutils.ThumbnailDownloader;
import com.manakina.home.mp3player.threads.SongLoader;



public class FragmentSongs extends RootFragment implements LoaderManager.LoaderCallbacks<ArrayList<Song>>{

    public final static String EXTRA_SONG_LIST = "song_list";
   // public final static String PARAM_PINTENT = "pendingIntent";

    public final static String EXTRA_POSITION = "position";
   // public final static int TASK_CODE = 0;

    private ThumbnailDownloader<ImageView> mThumbnailDownloader;
    private OnUpdateFragmentControls mCallback;
    private ListView mListView;
    private SongListAdapter mListAdapter;
    private ArrayList<Song> mSongs;



    public FragmentSongs(){}


    public interface OnUpdateFragmentControls {
        void onSongSelected(Song song);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (OnUpdateFragmentControls) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLoaderManager().initLoader(SongLoader.LOAD_SONG, null, this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_alltracks, container, false);
        mListView = (ListView) rootView.findViewById(R.id.playlist_all_tracks);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Song song = mListAdapter.getItem(position);
                mCallback.onSongSelected(song);

                getActivity().startService(new Intent(getActivity(), MusicPlaybackService.class)
                        .putParcelableArrayListExtra(EXTRA_SONG_LIST, mSongs)
                        .putExtra(EXTRA_POSITION, position));
               }
        });
        mThumbnailDownloader = new ThumbnailDownloader<ImageView>(new Handler());
        mThumbnailDownloader.setListener(new ThumbnailDownloader.Listener<ImageView>() {
            @Override
            public void onThumbnailDownloaded(ImageView imageView, Bitmap bitmap) {
                if (isVisible()) {
                    if (bitmap != null) {
                        imageView.setImageBitmap(bitmap);

                    } else {
                        imageView.setImageResource(R.drawable.icon_music);

                    }
                }
            }
        });
        mThumbnailDownloader.start();
        mThumbnailDownloader.getLooper();
      //  Log.i("TAG", "FragmentSongs onCreateView");

        return rootView;
    }




    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mThumbnailDownloader.clearQueue();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mThumbnailDownloader.quit();
    }


    @Override
    public Loader<ArrayList<Song>> onCreateLoader(int id, Bundle args) {
        if (id == SongLoader.LOAD_SONG) {
            return new SongLoader(getActivity(), SongLoader.ITEM_ALL_SONGS);
        } else return null;
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Song>> loader, ArrayList<Song> data) {
        mSongs = data;
        setListAdapter();
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Song>> loader) {

    }

    private void setListAdapter() {
        if (this == null || mListView == null) return;
        if (mSongs != null) {
            mListAdapter = new SongListAdapter(getActivity(), mThumbnailDownloader, mSongs);
            mListView.setAdapter(mListAdapter);

        } else {
            mListView.setAdapter(null);

        }
    }


}
