package com.manakina.home.mp3player.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import com.manakina.home.mp3player.R;
import com.manakina.home.mp3player.background.MusicPlaybackManager;
import com.manakina.home.mp3player.model.Song;
import com.manakina.home.mp3player.musicadapters.SongListAdapter;
import com.manakina.home.mp3player.supportutils.ThumbnailDownloader;
import com.manakina.home.mp3player.threads.SongLoader;
import java.util.ArrayList;



public class FragmentPlayListByGenres extends RootFragment {
    private static final String ARGUMENT_GENRE = "argument_genre";

    private ListView mListView;
    private ThumbnailDownloader<ImageView> mThumbnailDownloader;
    // OnUpdateControls mCallback;
    private int idGenre;

    private ArrayList<Song> mSongs;
    private SongListAdapter mListAdapter;
    private MusicPlaybackManager mMusicManager;

    public FragmentPlayListByGenres() {
        // Required empty public constructor
    }


    public static FragmentPlayListByGenres newInstance(int idGenre){
        FragmentPlayListByGenres pageFragment = new FragmentPlayListByGenres();
        Bundle arguments = new Bundle();
        arguments.putInt(ARGUMENT_GENRE, idGenre);
        pageFragment.setArguments(arguments);
        return pageFragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  Log.i("TAG", "FragmentPlayListByGenres onCreate " );
        idGenre = getArguments().getInt(ARGUMENT_GENRE);

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
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mMusicManager.setPlaylist(mSongs, position);
                mMusicManager.play();
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
       } else {
            mListView.setAdapter(null);
       }
    }


    private class SongLoaderCallbacks implements LoaderManager.LoaderCallbacks<ArrayList<Song>> {

        @Override
        public Loader<ArrayList<Song>> onCreateLoader(int id, Bundle args) {
            return new SongLoader(getActivity(), SongLoader.ITEM_GENRE, idGenre);
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




    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mThumbnailDownloader.clearQueue();
        // Log.i("TAG", "FragmentAllTracks onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mThumbnailDownloader.quit();
        //  Log.i("TAG", "FragmentAllTracks onDestroy");
    }
}
