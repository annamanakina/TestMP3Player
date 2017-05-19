package com.manakina.home.mp3player.interfaces;


import com.manakina.home.mp3player.model.Song;


import java.util.ArrayList;

public interface OnUpdateControls {
  //  void updateControls(ArrayList<Song> list, Song song); //Song track
    void updateControls(Song song);

}
