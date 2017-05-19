package com.manakina.home.mp3player.musicadapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.manakina.home.mp3player.R;
import com.manakina.home.mp3player.model.Song;
import com.manakina.home.mp3player.supportutils.ImageCache;
import com.manakina.home.mp3player.supportutils.ThumbnailDownloader;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class SongListAdapter extends ArrayAdapter<Song> {
    private LayoutInflater layoutInflater;
    private Context appContext;
    private ThumbnailDownloader mThumbnailDownloader;


    public SongListAdapter(Context context, List<Song> objects) {
        super(context, 0, objects);
        appContext = context;
        layoutInflater = LayoutInflater.from(appContext);
    }

    public SongListAdapter(Context context, ThumbnailDownloader loader, ArrayList<Song> items) {
        super(context, 0, items);
        appContext = context;
        layoutInflater = LayoutInflater.from(context);
        mThumbnailDownloader = loader;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listView = convertView;
        if (listView == null) {
            listView = layoutInflater.inflate(R.layout.item_for_music_list, parent, false);
        }
        Song song = getItem(position);
        ImageView imageView = (ImageView) listView.findViewById(R.id.imageViewTrack);

        Bitmap bitmap = ImageCache.getBitmapFromMemoryCache(song.getPath());
        if (bitmap == null) {
            mThumbnailDownloader.queueThumbnail(imageView, song.getPath());
        } else {
            imageView.setImageBitmap(bitmap);
        }


        TextView titleTextView = (TextView) listView.findViewById(R.id.textView_title);
        titleTextView.setText(song.getTitle());
       // Log.i("TAG", "getView titleTextView - "+song.getTitle());

        TextView artistTextView = (TextView) listView.findViewById(R.id.textView_artist);
        artistTextView.setText(song.getArtist());
        // Log.i("TAG", "getView artistTextView");

        TextView durationTextView = (TextView) listView.findViewById(R.id.textView_duration);
        SimpleDateFormat durationFormat = new SimpleDateFormat("mm:ss");
        String totaltime = durationFormat.format(song.getDuration());
        durationTextView.setText(totaltime);

        return listView;
    }
}
