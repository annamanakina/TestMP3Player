package com.manakina.home.mp3player.musicadapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.manakina.home.mp3player.R;
import com.manakina.home.mp3player.supportutils.ImageCache;
import com.manakina.home.mp3player.supportutils.ThumbnailDownloader;

import java.text.SimpleDateFormat;

/**
 * Created by Anna on 15.03.2017.
 */
public class MP3SimpleCursorAdapter extends SimpleCursorAdapter {
    private int layout;
    ThumbnailDownloader mThumbnailDownloader;

    public MP3SimpleCursorAdapter(Context context, ThumbnailDownloader loader, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
        this.layout = layout;
        mThumbnailDownloader = loader;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        int titleColumn = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
        String title = cursor.getString(titleColumn);

        int artistColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
        String artist = cursor.getString(artistColumn);

        int durationColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
        int duration = cursor.getInt(durationColumn);
        SimpleDateFormat durationFormat = new SimpleDateFormat("mm:ss");
        String totaltime = durationFormat.format(duration);

        TextView songTitle = (TextView) view.findViewById(R.id.textView_title);
        songTitle.setText(title);

        TextView songArtist = (TextView) view.findViewById(R.id.textView_artist);
        songArtist.setText(artist);

        TextView songDuration = (TextView) view.findViewById(R.id.textView_duration);
        songDuration.setText(totaltime);

        int songPathColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
        String songPath = cursor.getString(songPathColumn);
        Bitmap bitmap = ImageCache.getBitmapFromMemoryCache(songPath);

      //  Log.i("TAG", "MP3SimpleCursorAdapter songPath " +songPath);
        ImageView songImageView = (ImageView) view.findViewById(R.id.imageViewTrack);
            if (bitmap == null) {
                mThumbnailDownloader.queueThumbnail(songImageView, songPath);
            } else {
                songImageView.setImageBitmap(bitmap);
            }

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(layout, parent, false);
        return view;

    }
}
