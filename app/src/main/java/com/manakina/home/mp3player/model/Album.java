package com.manakina.home.mp3player.model;

public class Album {

    private int mId;
    private String mTitle;
    private int mNumberOfSongs;
    private String mLastYear;
    private String mArtist;

    public Album (){}

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public int getNumberOfSongs() {
        return mNumberOfSongs;
    }

    public void setNumberOfSongs(int number) {
        this.mNumberOfSongs = number;
    }

    public String getLastYear() {
        return mLastYear;
    }

    public void setLastYear(String year) {
        this.mLastYear = year;
    }

    public String getArtist() {
        return mArtist;
    }

    public void setArtist(String artist) {
        this.mArtist = artist;
    }

    @Override
    public String toString() {
        return mTitle;
    }

}
