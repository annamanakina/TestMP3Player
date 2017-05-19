package com.manakina.home.mp3player.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.manakina.home.mp3player.R;
import com.manakina.home.mp3player.model.Song;
import com.manakina.home.mp3player.musicadapters.SongListAdapter;
import com.manakina.home.mp3player.supportutils.ThumbnailDownloader;
import com.manakina.home.mp3player.threads.SongLoader;

import java.util.ArrayList;

public class FragmentPlayListByArtist extends RootFragment  {
    private static final String ARGUMENT_ARTIST = "argument_artist";


   // private OnUpdateControls mCallback;
    private int idArtist;
    private ThumbnailDownloader<ImageView> mThumbnailDownloader;
    private ListView mListView;

    private ArrayList<Song> mSongs;
    private SongListAdapter mListAdapter;


    public FragmentPlayListByArtist(){}

    public static FragmentPlayListByArtist newInstance(int idArtist){
        FragmentPlayListByArtist pageFragment = new FragmentPlayListByArtist();
        Bundle arguments = new Bundle();
        arguments.putInt(ARGUMENT_ARTIST, idArtist);
        pageFragment.setArguments(arguments);
        return pageFragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     //   Log.i("TAG", "FragmentPlayListByGenres onCreate " );
        idArtist = getArguments().getInt(ARGUMENT_ARTIST);
      //  Log.i("TAG", "FragmentPlayListByArtist onCreate idArtist - "+ idArtist );

        Bundle args = getArguments();
        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(SongLoader.LOAD_SONG, args, new SongLoaderCallbacks());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_playlist_infolder, container, false);
        mListView = (ListView) rootView.findViewById(R.id.playlist_infolder);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

               // mMusicManager.setPlaylist(mSongs, position);
               // mMusicManager.play();
            }
        });

        mThumbnailDownloader = new ThumbnailDownloader<ImageView>(new Handler());
        mThumbnailDownloader.setListener(new ThumbnailDownloader.Listener<ImageView>() {
            @Override
            public void onThumbnailDownloaded(ImageView imageView, Bitmap bitmap) {
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                } else {
                    imageView.setImageResource(R.drawable.icon_music);
                }
            }
        });
        mThumbnailDownloader.start();
        mThumbnailDownloader.getLooper();

        return rootView;
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


    private class SongLoaderCallbacks implements LoaderCallbacks<ArrayList<Song>>{

        @Override
        public Loader<ArrayList<Song>> onCreateLoader(int id, Bundle args) {
            return new SongLoader(getActivity(), SongLoader.ITEM_ARTIST, idArtist);
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
