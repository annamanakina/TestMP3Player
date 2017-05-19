package com.manakina.home.mp3player.threads;

import android.content.Context;
import com.manakina.home.mp3player.model.Album;
import com.manakina.home.mp3player.model.AudioManager;

import java.util.ArrayList;


public class AlbumLoader extends DataLoader<ArrayList<Album>> {

    public AlbumLoader(Context context) {
        super(context);
    }

    @Override
    public ArrayList<Album> loadInBackground() {

        return AudioManager.getInstance(getContext()).getAlbums();
    }

}
