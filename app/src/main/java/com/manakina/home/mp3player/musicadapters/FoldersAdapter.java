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
import com.manakina.home.mp3player.model.AudioManager;
import com.manakina.home.mp3player.model.Folder;
import com.manakina.home.mp3player.model.Song;
import com.manakina.home.mp3player.supportutils.ImageCache;
import com.manakina.home.mp3player.supportutils.ThumbnailDownloader;



import java.util.ArrayList;


public class FoldersAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Folder> mFolderList;
    private ThumbnailDownloader mThumbnailDownloader;
    private LayoutInflater layoutInflater;

    public FoldersAdapter(Context context,ThumbnailDownloader loader, ArrayList<Folder> list) {

        mFolderList = list;
        mThumbnailDownloader = loader;
        mContext = context;
        layoutInflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return mFolderList.size();
    }

    public Object getItem(int position) {
        return mFolderList.get(position);
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
        Folder folder = (Folder) getItem(position);

        TextView titleTextView = (TextView) gridView.findViewById(R.id.text_part);
        titleTextView.setText(folder.getTitle());

        String pathToImage = getPathToImage(folder);
        Bitmap bitmap = ImageCache.getBitmapFromMemoryCache(pathToImage);

        if (bitmap == null) {
            mThumbnailDownloader.queueThumbnail(imageView, pathToImage);
        } else {
            imageView.setImageBitmap(bitmap);
        }
         return gridView;
    }




    private void loadImage(int position, ImageView imageView) {
        Folder folder = (Folder) getItem(position);
        mThumbnailDownloader.queueThumbnail(imageView, getPathToImage(folder));
    }

    private String getPathToImage(Folder folder) {
        ArrayList<Song> list = AudioManager.getInstance(mContext).getSongsByFolderId(folder.getId());
        int index = (int)(Math.random()*(list.size()-1));

        return list.get(index).getPath();
    }

}
