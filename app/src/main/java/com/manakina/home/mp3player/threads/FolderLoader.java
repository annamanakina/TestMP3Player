package com.manakina.home.mp3player.threads;

import android.content.Context;

import com.manakina.home.mp3player.model.AudioManager;
import com.manakina.home.mp3player.model.Folder;

import java.util.ArrayList;

public class FolderLoader extends DataLoader<ArrayList<Folder>> {

    public FolderLoader(Context context) {
        super(context);
    }

    @Override
    public ArrayList<Folder> loadInBackground() {
        if (AudioManager.getInstance(getContext()).getFolders().size() == 0) {
          //  Log.i("TAG", "AudioManager FolderLoader getFolders() size() == 0");
            AudioManager.getInstance(getContext()).fetchFolders();
        }
        return AudioManager.getInstance(getContext()).getFolders();
    }
}
