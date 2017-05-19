package com.manakina.home.mp3player.threads;

import android.content.Context;

import com.manakina.home.mp3player.model.AudioManager;
import com.manakina.home.mp3player.model.Genre;
import java.util.ArrayList;


public class GenreLoader extends DataLoader<ArrayList<Genre>> {

    public GenreLoader(Context context) {
        super(context);
    }

    @Override
    public ArrayList<Genre> loadInBackground() {
        return AudioManager.getInstance(getContext()).getGenres();
    }
}
