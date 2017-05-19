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
import android.widget.ListView;
import com.manakina.home.mp3player.R;
import com.manakina.home.mp3player.model.Genre;
import com.manakina.home.mp3player.musicadapters.GenreFolderAdapter;
import com.manakina.home.mp3player.threads.GenreLoader;
import com.manakina.home.mp3player.threads.SongLoader;

import java.util.ArrayList;

public class FragmentGenres extends RootFragment  {

    ArrayList<Genre> mGenres;
    ListView mListView;
    GenreFolderAdapter mListAdapter;

    public FragmentGenres() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(SongLoader.LOAD_GENRE, null, new GenreLoaderCallbacks());
        //  Log.i("TAG", "FragmentGenres onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_genres, container, false);
        mListView = (ListView) rootView.findViewById(R.id.list_genres);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Genre mGenre = (Genre) parent.getAdapter().getItem(position);
                openPlayListFragment(mGenre.getId());
            }
        });
        return rootView;
    }






    private void openPlayListFragment(int idGenre) {
        FragmentPlayListByGenres fragmentPlayList = FragmentPlayListByGenres.newInstance(idGenre);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_genres, fragmentPlayList)
                .addToBackStack(null).commit();
      //  Log.i("TAG", "Genre openPlayListFragment");
    }



    private void setListAdapter() {
        if (this == null || mListView == null) return;

        if (mGenres != null) {
            //  Log.i("TAG", "mGenre != null ");
            mListAdapter = new GenreFolderAdapter(getActivity(),  mGenres);
            mListView.setAdapter(mListAdapter);
            //   Log.i("TAG", "FragmentFrolder mGridViewFolder.setAdapter ");
        } else {
            mListView.setAdapter(null);
            //   Log.i("TAG", "FragmentArtists can't set adapter ");
        }
    }

    private class GenreLoaderCallbacks implements LoaderManager.LoaderCallbacks<ArrayList<Genre>> {

        @Override
        public Loader<ArrayList<Genre>> onCreateLoader(int id, Bundle args) {
            return new GenreLoader(getActivity());
        }

        @Override
        public void onLoadFinished(Loader<ArrayList<Genre>> loader, ArrayList<Genre> data) {
            mGenres = data;
            setListAdapter();
        }

        @Override
        public void onLoaderReset(Loader<ArrayList<Genre>> loader) {

        }
    }
}
