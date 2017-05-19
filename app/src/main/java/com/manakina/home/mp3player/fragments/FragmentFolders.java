package com.manakina.home.mp3player.fragments;

import android.app.Activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;

import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;


import com.manakina.home.mp3player.R;
import com.manakina.home.mp3player.model.Folder;
import com.manakina.home.mp3player.musicadapters.FoldersAdapter;
import com.manakina.home.mp3player.supportutils.ThumbnailDownloader;
import com.manakina.home.mp3player.threads.FolderLoader;
import com.manakina.home.mp3player.threads.SongLoader;

import java.util.ArrayList;


public class FragmentFolders extends RootFragment {

    GridView mGridViewFolder;
    Folder mFolder;
    ArrayList<Folder> mFolders;
    ThumbnailDownloader<ImageView> mThumbnailDownloader;
    FoldersAdapter mAdapter;

    public FragmentFolders() {
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Log.i("TAG", "FragmentFolders onAttach");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLoaderManager().initLoader(SongLoader.LOAD_FOLDER, null, new FolderLoaderCallbacks());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      //  Log.i("TAG", "FragmentFolders onCreateView");
        View rootView = inflater.inflate(R.layout.fragment_folders, container, false);

        mGridViewFolder = (GridView) rootView.findViewById(R.id.gridviewFragm);
        mGridViewFolder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mFolder = (Folder) parent.getAdapter().getItem(position);
                openPlayListFragment(mFolder);
            }
        });
        mThumbnailDownloader = new ThumbnailDownloader<ImageView>(new Handler());
        mThumbnailDownloader.setListener(new ThumbnailDownloader.Listener<ImageView>() {
            @Override
            public void onThumbnailDownloaded(ImageView imageView, Bitmap bitmap) {
                if (isVisible()) {
                    if (bitmap != null) {
                        imageView.setImageBitmap(bitmap);
                         // Log.i("TAG", "FragmentFolders onCreateView:  imageView.setImageBitmap");
                    } else {
                        imageView.setImageResource(R.drawable.icon_music);
                        //Log.i("TAG", "FragmentFolders onCreateView: else imageView.setImageResource");
                    }
                }
              }
        });

        mThumbnailDownloader.start();
         mThumbnailDownloader.getLooper();
        return rootView;
    }






    private void openPlayListFragment(Folder folder) {
        FragmentPlayList fragmentPlayList = FragmentPlayList.newInstance(folder.getId());
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_folderLayout, fragmentPlayList)
                .addToBackStack(null).commit();
        //Log.i("TAG", "openPlayListFragment");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mGridViewFolder.setAdapter(null);
        mThumbnailDownloader.clearQueue();
       //  Log.i("TAG", "FragmentFolders onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mThumbnailDownloader != null) {
            mThumbnailDownloader.quit();
        }

      //   Log.i("TAG", "FragmentFolders onDestroy");
    }




    private void setGridViewFolderAdapter() {
        if (this == null || mGridViewFolder == null) return;

        if (mFolders != null) {
            //  Log.i("TAG", "FragmentFrolder mFilesList != null ");
            mAdapter = new FoldersAdapter(getActivity(), mThumbnailDownloader, mFolders);
            mGridViewFolder.setAdapter(mAdapter);
            //   Log.i("TAG", "FragmentFrolder mGridViewFolder.setAdapter ");
        } else {
            mGridViewFolder.setAdapter(null);
            // Log.i("TAG", "FragmentFrolder can't set adapter ");
        }
    }


    private class FolderLoaderCallbacks implements LoaderManager.LoaderCallbacks<ArrayList<Folder>> {

        @Override
        public Loader<ArrayList<Folder>> onCreateLoader(int id, Bundle args) {
            return new FolderLoader(getActivity());
        }

        @Override
        public void onLoadFinished(Loader<ArrayList<Folder>> loader, ArrayList<Folder> data) {
            mFolders = data;
            setGridViewFolderAdapter();
        }

        @Override
        public void onLoaderReset(Loader<ArrayList<Folder>> loader) {

        }
    }

}
