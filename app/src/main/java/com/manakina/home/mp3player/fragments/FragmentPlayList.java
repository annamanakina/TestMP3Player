package com.manakina.home.mp3player.fragments;


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
import com.manakina.home.mp3player.R;
import com.manakina.home.mp3player.interfaces.OnUpdateControls;
import com.manakina.home.mp3player.model.Song;
import com.manakina.home.mp3player.musicadapters.SongListAdapter;
import com.manakina.home.mp3player.supportutils.ThumbnailDownloader;
import com.manakina.home.mp3player.threads.SongLoader;

import java.util.ArrayList;


public class FragmentPlayList extends RootFragment {
    private static final String ARGUMENT_ID = "id";

    ListView mListView;
    ArrayList<Song> mSongs;
    ThumbnailDownloader<ImageView> mThumbnailDownloader;
    OnUpdateControls mCallback;
    SongListAdapter mListAdapter;
    private int idFolder;




    public FragmentPlayList() {

    }

    public static FragmentPlayList newInstance(int id){
        FragmentPlayList pageFragment = new FragmentPlayList();
        Bundle arguments = new Bundle();
        arguments.putInt(ARGUMENT_ID, id);
       // Log.i("TAG", "newInstance: folderName: " + folder);
        pageFragment.setArguments(arguments);
        return pageFragment;
    }

    /*@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (OnUpdateControls) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }

    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        idFolder = getArguments().getInt(ARGUMENT_ID);
        Bundle args = getArguments();
        getLoaderManager().initLoader(SongLoader.LOAD_SONG, args, new SongLoaderCallbacks());

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
                    //  Log.i("TAG", "MusicActivity onStart: else imageView.setImageResource");
                }
            }
        });
        mThumbnailDownloader.start();
        mThumbnailDownloader.getLooper();

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private void setListAdapter() {
        if (this == null || mListView == null) return;

        if (mSongs != null) {
            // Log.i("TAG", "mTrackNames != null ");
            mListAdapter = new SongListAdapter(getActivity(), mThumbnailDownloader, mSongs);
            mListView.setAdapter(mListAdapter);
             // Log.i("TAG", "FragmentPlayList set adapter");
        } else {
            mListView.setAdapter(null);
          //  Log.i("TAG", "FragmentPlayList can't set adapter ");
        }
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mThumbnailDownloader.clearQueue();
    //    Log.i("TAG", "PlayListFragment onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mThumbnailDownloader.quit();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }



    private class SongLoaderCallbacks implements LoaderManager.LoaderCallbacks<ArrayList<Song>> {

        @Override
        public Loader<ArrayList<Song>> onCreateLoader(int id, Bundle args) {

            return new SongLoader(getActivity(), SongLoader.ITEM_FOLDER, idFolder);
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
