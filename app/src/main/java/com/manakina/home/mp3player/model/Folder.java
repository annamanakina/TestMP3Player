package com.manakina.home.mp3player.model;


import android.os.Parcel;
import android.os.Parcelable;

public class Folder implements Parcelable {
    private int mId;
    private String mTitle;
    private String mPath;


    public Folder() {
    }

    protected Folder(Parcel in) {
        mId = in.readInt();
        mTitle = in.readString();
        mPath = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mTitle);
        dest.writeString(mPath);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Folder> CREATOR = new Creator<Folder>() {
        @Override
        public Folder createFromParcel(Parcel in) {
            return new Folder(in);
        }

        @Override
        public Folder[] newArray(int size) {
            return new Folder[size];
        }
    };

    @Override
    public String toString() {
        return mTitle;
    }

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

    public String getPath() {
        return mPath;
    }

    public void setPath(String mPath) {
        this.mPath = mPath;
    }


   /* @Override
    public boolean equals(Object other) {
        if (other == null) return false;

        Folder folder = (Folder) other;
        return this.getId() == folder.getId() & this.getTitle().equals(folder.getTitle())
                & this.getPath().equals(folder.getPath());
    }

    @Override
    public int hashCode() {
        int result = mId;
        result = 31 * result + mTitle.hashCode();
        result = 31 * result + mPath.hashCode();
        return result;
    }*/
}
