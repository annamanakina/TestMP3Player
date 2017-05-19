package com.manakina.home.mp3player.fragments;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.manakina.home.mp3player.R;
import com.manakina.home.mp3player.interfaces.OnUpdateControls;
import com.manakina.home.mp3player.model.Song;
import com.manakina.home.mp3player.musicadapters.SongListAdapter;
import com.manakina.home.mp3player.supportutils.ThumbnailDownloader;
import com.manakina.home.mp3player.threads.SongLoader;

import java.util.ArrayList;

public class FragmentPlayListByAlbum extends RootFragment {

    private static final String ARGUMENT_ID = "id";

    ListView mListView;
    ArrayList<Song> mSongs;
    ThumbnailDownloader<ImageView> mThumbnailDownloader;
    //OnUpdateControls mCallback;
    SongListAdapter mListAdapter;
    private int mId;


    public FragmentPlayListByAlbum() {
        // Required empty public constructor
    }

    public static FragmentPlayListByAlbum newInstance(int id){
        FragmentPlayListByAlbum pageFragment = new FragmentPlayListByAlbum();
        Bundle arguments = new Bundle();
        arguments.putInt(ARGUMENT_ID, id);
        pageFragment.setArguments(arguments);
        return pageFragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mId = getArguments().getInt(ARGUMENT_ID);
        Bundle args = getArguments();
        getLoaderManager().initLoader(SongLoader.LOAD_SONG, args, new SongLoaderCallbacks());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_playlist_infolder, container, false);
        mListView = (ListView) rootView.findViewById(R.id.playlist_infolder);

        mThumbnailDownloader = new ThumbnailDownloader<ImageView>(new Handler());
        mThumbnailDownloader.setListener(new ThumbnailDownloader.Listener<ImageView>() {
            @Override
            public void onThumbnailDownloaded(ImageView imageView, Bitmap bitmap) {
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                } else {
                    imageView.setImageResource(R.drawable.icon_music);
                    //  Log.i("TAG", "MusicActivity onStart: else imageView.setImageResource");
                }
            }
        });
        mThumbnailDownloader.start();
        mThumbnailDownloader.getLooper();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              //  mMusicManager.setPlaylist(mSongs, position);
              //  mMusicManager.play();
            }
        });

        return rootView;
    }


    private void setListAdapter() {
        if (this == null || mListView == null) return;

        if (mSongs != null) {
            // Log.i("TAG", "mTrackNames != null ");
            mListAdapter = new SongListAdapter(getActivity(), mThumbnailDownloader, mSongs);
            mListView.setAdapter(mListAdapter);
           // Log.i("TAG", "FragmentPlayListByAlbum set adapter");
        } else {
            mListView.setAdapter(null);
          //  Log.i("TAG", "FragmentPlayListByAlbum can't set adapter ");
        }
    }


    private class SongLoaderCallbacks implements LoaderManager.LoaderCallbacks<ArrayList<Song>> {

        @Override
        public Loader<ArrayList<Song>> onCreateLoader(int id, Bundle args) {
            return new SongLoader(getActivity(), SongLoader.ITEM_ALBUM, mId);
        }

        @Override
        public void onLoadFinished(Loader<ArrayList<Song>> loader, ArrayList<Song> data) {
            mSongs = data;
            setListAdapter();
        }

        @Override
        public void onLoaderReset(Loader<ArrayList<Song>> loader) {

        }
    }

}
