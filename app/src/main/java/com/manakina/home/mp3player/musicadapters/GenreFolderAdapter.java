package com.manakina.home.mp3player.musicadapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.manakina.home.mp3player.R;
import com.manakina.home.mp3player.model.FolderName;
import com.manakina.home.mp3player.model.Genre;
import com.manakina.home.mp3player.supportutils.ImageCache;
import com.manakina.home.mp3player.supportutils.ThumbnailDownloader;

import java.util.ArrayList;

/**
 * Created by Anna on 13.09.2016.
 */
public class GenreFolderAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Genre> mGenreList;
    //private ThumbnailDownloader mThumbnailDownloader;
    private LayoutInflater layoutInflater;

    public GenreFolderAdapter(Context context,  ArrayList<Genre> list) {
    //ThumbnailDownloader loader,
        mGenreList = list;
     //    mThumbnailDownloader = loader;
        mContext = context;
        layoutInflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return mGenreList.size();
    }

    @Override
    public Object getItem(int position) {
        return mGenreList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //  Log.i("TAG", "FoldersAdapter getView");
        View gridView = convertView;
        if (gridView == null) {
            gridView = layoutInflater.inflate(R.layout.item_for_music_list, parent, false);
            //   Log.i("TAG", "FoldersAdapter getView convertView == null");
        }

        /*ImageView imageView = (ImageView) gridView.findViewById(R.id.image_part);
        imageView.setImageResource(R.drawable.icon_music);*/
        Genre genre = (Genre) getItem(position);
      //  Log.i("TAG", "GenreFolderAdapter genre " + genre.getName());
        TextView titleTextView = (TextView) gridView.findViewById(R.id.textView_title);
        titleTextView.setText(genre.getName());

        /*TextView titleTextView = (TextView) gridView.findViewById(R.id.text_part);
        titleTextView.setText(genre.getName());*/

        return gridView;
    }



}
