package com.manakina.home.mp3player.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.manakina.home.mp3player.R;
import com.manakina.home.mp3player.model.Album;
import com.manakina.home.mp3player.musicadapters.AlbumAdapter;
import com.manakina.home.mp3player.threads.AlbumLoader;
import com.manakina.home.mp3player.threads.SongLoader;

import java.util.ArrayList;

public class FragmentAlbums extends RootFragment  {
    private GridView mGridView;
    private ArrayList<Album> mAlbums;
    private AlbumAdapter mAdapter;


    public FragmentAlbums() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLoaderManager().initLoader(SongLoader.LOAD_ALBUM, null, new AlbumLoaderCallbacks());
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_albums, container, false);
        mGridView = (GridView) rootView.findViewById(R.id.gridviewFragm);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Album album = (Album) parent.getAdapter().getItem(position);
               openPlayListFragment(album);
            }
        });

        return rootView;
    }

    private void openPlayListFragment(Album album) {
        FragmentPlayListByAlbum fragmentPlayList = FragmentPlayListByAlbum.newInstance(album.getId());
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_albumsLayout, fragmentPlayList)
                .addToBackStack(null).commit();
        //Log.i("TAG", "openPlayListFragment");
    }




    private void setGridViewAdapter() {
        if (this == null || mGridView == null) return;

        if (mAlbums != null) {
            //  Log.i("TAG", "FragmentFrolder mFilesList != null ");
            mAdapter = new AlbumAdapter(getActivity(), mAlbums); //mThumbnailDownloader
            mGridView.setAdapter(mAdapter);
            //   Log.i("TAG", "FragmentFrolder mGridViewFolder.setAdapter ");
        } else {
            mGridView.setAdapter(null);
            // Log.i("TAG", "FragmentFrolder can't set adapter ");
        }
    }


    private class AlbumLoaderCallbacks implements LoaderManager.LoaderCallbacks<ArrayList<Album>> {

        @Override
        public Loader<ArrayList<Album>> onCreateLoader(int id, Bundle args) {
            if (id == SongLoader.LOAD_ALBUM) {
                return new AlbumLoader(getActivity());
            } else return null;
        }

        @Override
        public void onLoadFinished(Loader<ArrayList<Album>> loader, ArrayList<Album> data) {
            mAlbums = data;
            setGridViewAdapter();
        }

        @Override
        public void onLoaderReset(Loader<ArrayList<Album>> loader) {

        }
    }





    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mGridView.setAdapter(null);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
