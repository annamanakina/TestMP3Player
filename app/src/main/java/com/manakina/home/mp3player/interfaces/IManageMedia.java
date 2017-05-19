package com.manakina.home.mp3player.interfaces;

import com.manakina.home.mp3player.model.Song;
import java.util.ArrayList;


public interface IManageMedia {
    void play();
    void pause();
    void nextSong();
    void prevSong();
    void repeat();
    void shuffle();
    void setPlaylist(ArrayList<Song> list, int position);


}
