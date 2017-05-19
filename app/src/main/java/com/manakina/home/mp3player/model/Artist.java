package com.manakina.home.mp3player.model;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Artist implements Parcelable {
    private int mId;
    private String mArtist;
    private int mNumberOfSongs;

    public Artist(){}

    protected Artist(Parcel in) {
        mId = in.readInt();
        mArtist = in.readString();
        mNumberOfSongs = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mArtist);
        dest.writeInt(mNumberOfSongs);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Artist> CREATOR = new Creator<Artist>() {
        @Override
        public Artist createFromParcel(Parcel in) {
            return new Artist(in);
        }

        @Override
        public Artist[] newArray(int size) {
            return new Artist[size];
        }
    };

    public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    public String getArtist() {
        return mArtist;
    }

    public void setArtist(String mArtist) {
        this.mArtist = mArtist;
    }

    public int getNumberOfSongs() {
        return mNumberOfSongs;
    }

    public void setNumberOfSongs(int mNumberOfSongs) {
        this.mNumberOfSongs = mNumberOfSongs;
    }

    @Override
    public String toString() {
        return mArtist;
    }
}
