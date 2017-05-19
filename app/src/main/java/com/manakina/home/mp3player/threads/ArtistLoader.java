package com.manakina.home.mp3player.threads;


import android.content.Context;

import com.manakina.home.mp3player.model.Artist;
import com.manakina.home.mp3player.model.AudioManager;

import java.util.ArrayList;

public class ArtistLoader extends DataLoader<ArrayList<Artist>>{


    public ArtistLoader(Context context) {
        super(context);

    }

    @Override
    public ArrayList<Artist> loadInBackground() {
        return AudioManager.getInstance(getContext()).getArtists();
    }
}
