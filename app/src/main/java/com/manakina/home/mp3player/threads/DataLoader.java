package com.manakina.home.mp3player.threads;


import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;


public abstract class DataLoader<D> extends AsyncTaskLoader<D> {
private D mData;

    public DataLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {

        if (mData != null) {
            deliverResult(mData);
            //Log.i("TAG", "DataLoader - onStartLoading  if (mData != null) ");
        } else {
            forceLoad();
           // Log.i("TAG", "DataLoader - onStartLoading  else ");
        }
    }

    @Override
    public void deliverResult(D data) {
        mData = data;
        if (isStarted())
        super.deliverResult(data);
      //  Log.i("TAG", "DataLoader - deliverResult");
    }
}
