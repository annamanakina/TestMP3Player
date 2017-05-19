package com.manakina.home.mp3player.musicadapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.manakina.home.mp3player.R;
import com.manakina.home.mp3player.model.Artist;
import com.manakina.home.mp3player.model.Song;

import java.text.SimpleDateFormat;
import java.util.List;


public class ArtistListAdapter extends ArrayAdapter<Artist> {
    private LayoutInflater layoutInflater;
    private Context appContext;

    public ArtistListAdapter(Context context, List<Artist> objects) {
        super(context, 0, objects);
        appContext = context;
        layoutInflater = LayoutInflater.from(appContext);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listView = convertView;
        if (listView == null) {
            listView = layoutInflater.inflate(R.layout.item_artist, parent, false);
        }
        Artist artist = getItem(position);
        //пока картинок нет
        //ImageView imageView = (ImageView) listView.findViewById(R.id.imageViewTrack);

        TextView artistTextView = (TextView) listView.findViewById(R.id.artist_title);
        artistTextView.setText(artist.getArtist());

        TextView numberOfSongsTextView = (TextView) listView.findViewById(R.id.artist_number_of_tracks);
        int number = artist.getNumberOfSongs();
        if (number == 1) {
            numberOfSongsTextView.setText(number + " track");
        } else {
            numberOfSongsTextView.setText(number + " tracks");
        }
        return listView;
    }
}
