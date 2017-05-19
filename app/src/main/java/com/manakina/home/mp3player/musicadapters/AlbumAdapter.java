package com.manakina.home.mp3player.musicadapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.manakina.home.mp3player.R;
import com.manakina.home.mp3player.model.Album;
import com.manakina.home.mp3player.model.Folder;
import com.manakina.home.mp3player.supportutils.ImageCache;
import com.manakina.home.mp3player.supportutils.ThumbnailDownloader;

import java.util.ArrayList;


public class AlbumAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Album> mAlbumList;
    private ThumbnailDownloader mThumbnailDownloader;
    private LayoutInflater layoutInflater;


    public AlbumAdapter(Context context, ArrayList<Album> list) {

        mAlbumList = list;
        mContext = context;
        layoutInflater = LayoutInflater.from(context);
    }

    public AlbumAdapter(Context context,ThumbnailDownloader loader, ArrayList<Album> list) {

        mAlbumList = list;
        mThumbnailDownloader = loader;
        mContext = context;
        layoutInflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return mAlbumList.size();
    }

    public Object getItem(int position) {
        return mAlbumList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        View gridView = convertView;
        if (gridView == null) {
            gridView = layoutInflater.inflate(R.layout.gridview_item, parent, false);
        }

        ImageView imageView = (ImageView) gridView.findViewById(R.id.image_part);
        imageView.setImageResource(R.drawable.icon_music);
        Album album = (Album) getItem(position);

        TextView titleTextView = (TextView) gridView.findViewById(R.id.text_part);
        titleTextView.setText(album.getTitle());

        //пока оставить
       /* String pathToImage = getPathToImage(folder);
        Bitmap bitmap = ImageCache.getBitmapFromMemoryCache(pathToImage);

        if (bitmap == null) {
            mThumbnailDownloader.queueThumbnail(imageView, pathToImage);
        } else {
            //   if (isVisible()) {
            imageView.setImageBitmap(bitmap);
            // }
        }*/
        return gridView;
    }
}
