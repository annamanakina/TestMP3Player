package com.manakina.home.mp3player.model;


import android.os.Parcel;
import android.os.Parcelable;

public class FolderName implements Parcelable {
    private String mName;
    private String mPath;

    public FolderName (){ }


    public String getName() {
        return mName;
    }

    public void setName(String path) {
        mName = path;
    }

    public String getPath() {
        return mPath;
    }

    public void setPath(String path) {
        mPath = path;
    }

    @Override
    public String toString() {
        return mName;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{mName, mPath});
    }


    public FolderName(Parcel in) {
        String[] data = new String[2];
        in.readStringArray(data);
        mName = data[0];
        mPath = data[1];
    }

    public static final Creator CREATOR = new Creator() {
        public FolderName createFromParcel(Parcel in) {
            return new FolderName (in);
        }

        public FolderName [] newArray(int size) {
            return new FolderName [size];
        }
    };


}
