package com.manakina.home.mp3player.threads;

import android.content.Context;

import com.manakina.home.mp3player.model.AudioManager;
import com.manakina.home.mp3player.model.Song;

import java.util.ArrayList;


public class SongLoader extends DataLoader<ArrayList<Song>> {

    public static final int LOAD_SONG = 101;
    public static final int LOAD_FOLDER = 102;
    public static final int LOAD_ARTIST = 103;
    public static final int LOAD_GENRE = 104;
    public static final int LOAD_ALBUM = 105;


    public static final int ITEM_ALL_SONGS = 0;
    public static final int ITEM_ARTIST = 1;
    public static final int ITEM_GENRE = 2;
    public static final int ITEM_ALBUM = 3;
    public static final int ITEM_FOLDER = 4;

    private int mId;
    private int mItem;


    public SongLoader(Context context, int item) {
        super(context);
        mItem = item;
    }

    public SongLoader(Context context, int item, int id) {
        super(context);
        mId = id;
        mItem = item;
    }


    @Override
    public ArrayList<Song> loadInBackground() {
        switch (mItem) {
            case ITEM_ALL_SONGS:
             //   Log.i("TAG", "SongLoader - ITEM_ALL_SONGS");
                return  AudioManager.getInstance(getContext()).getSongs();

            case ITEM_ARTIST:
               // Log.i("TAG", "SongLoader - ITEM_ARTIST");
                return AudioManager.getInstance(getContext()).getSongsByArtistId(mId);

            case ITEM_GENRE:
               // Log.i("TAG", "SongLoader - ITEM_GENRE");
                return AudioManager.getInstance(getContext()).getSongsByGenreId(mId);

            case ITEM_ALBUM:
              //  Log.i("TAG", "SongLoader - ITEM_ALBUM");
                return AudioManager.getInstance(getContext()).getSongsByAlbumId(mId);


            case ITEM_FOLDER:
              //  Log.i("TAG", "SongLoader - ITEM_ALBUM");
                return AudioManager.getInstance(getContext()).getSongsByFolderId(mId);

        }
        return null;
    }
}
