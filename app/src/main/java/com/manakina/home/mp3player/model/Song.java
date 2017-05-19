package com.manakina.home.mp3player.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;


public class Song implements Parcelable {
    private int mId;
    private String mTitle;
    private String mArtist;
    private String mPath;
    private int mAlbumId;
    private String mAlbum;
    private int mDuration;

    public Song(){}

    protected Song(Parcel in) {
        mId = in.readInt();
        mTitle = in.readString();
        mArtist = in.readString();
        mPath = in.readString();
        mAlbumId = in.readInt();
        mAlbum = in.readString();
        mDuration = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mTitle);
        dest.writeString(mArtist);
        dest.writeString(mPath);
        dest.writeInt(mAlbumId);
        dest.writeString(mAlbum);
        dest.writeInt(mDuration);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Song> CREATOR = new Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };

    public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getArtist() {
        return mArtist;
    }

    public void setArtist(String mArtist) {
        this.mArtist = mArtist;
    }

    public String getPath() {
        return mPath;
    }

    public void setPath(String mPath) {
        this.mPath = mPath;
    }

    public int getAlbumId() {
        return mAlbumId;
    }

    public void setAlbumId(int mAlbumId) {
        this.mAlbumId = mAlbumId;
    }

    public String getAlbum() {
        return mAlbum;
    }

    public void setAlbum(String mAlbum) {
        this.mAlbum = mAlbum;
    }

    public int getDuration() {
        return mDuration;
    }

    public void setDuration(int mDuration) {
        this.mDuration = mDuration;
    }

    public String formatSeconds() {
        SimpleDateFormat durationFormat = new SimpleDateFormat("mm:ss");
        return durationFormat.format(mDuration);
    }

    @Override
    public String toString() {
        return mTitle;
    }
}
