package com.manakina.home.mp3player.fragments;


import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.manakina.home.mp3player.R;
import com.manakina.home.mp3player.model.Artist;

import com.manakina.home.mp3player.musicadapters.ArtistListAdapter;
import com.manakina.home.mp3player.threads.ArtistLoader;
import com.manakina.home.mp3player.threads.SongLoader;

import java.util.ArrayList;



public class FragmentArtists extends RootFragment {

    ArrayList<Artist> mArtists;
    ListView mListView;
    ArtistListAdapter mListAdapter;

    public FragmentArtists() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(SongLoader.LOAD_ARTIST, null, new ArtistLoaderCallbacks());
        //    Log.i("TAG", "FragmentArtists onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_artists, container, false);
        mListView = (ListView) rootView.findViewById(R.id.playlist_artists);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Artist artist = mListAdapter.getItem(position);
                openPlayListFragment(artist.getId());
            }
        });

        return rootView;
    }



    private void openPlayListFragment(int idArtist) {
        FragmentPlayListByArtist fragmentPlayList = FragmentPlayListByArtist.newInstance(idArtist);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_artistLayout, fragmentPlayList)
                .addToBackStack(null).commit();
    }


    private void setListAdapter() {
        if (this == null || mListView == null) return;

        if (mArtists != null) {
            //  Log.i("TAG", "mArtists != null ");
            mListAdapter = new ArtistListAdapter(getActivity(), mArtists);
            mListView.setAdapter(mListAdapter);
        } else {
            mListView.setAdapter(null);
            //    Log.i("TAG", "FragmentArtists can't set adapter ");
        }
    }


    private class ArtistLoaderCallbacks implements LoaderManager.LoaderCallbacks<ArrayList<Artist>> {

        @Override
        public Loader<ArrayList<Artist>> onCreateLoader(int id, Bundle args) {
            if (id == SongLoader.LOAD_ARTIST) {
                return new ArtistLoader(getActivity());
            } else return null;
        }

        @Override
        public void onLoadFinished(Loader<ArrayList<Artist>> loader, ArrayList<Artist> data) {
            mArtists = data;
            setListAdapter();
        }

        @Override
        public void onLoaderReset(Loader<ArrayList<Artist>> loader) {

        }
    }


}
