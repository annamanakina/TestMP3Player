package com.manakina.home.mp3player.model;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;

import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;


public class AudioManager {

    private static AudioManager sAudioManager;
    private Context mContext;

    private ArrayList<Song> mSongs;
    private ArrayList<Folder> mFolders;
    private LinkedHashMap<Integer, ArrayList<Song>> mFolderMap;



    private AudioManager(Context context) {
        mContext = context;
        mFolders = new ArrayList<>();
        mSongs = new ArrayList<>();
        mFolderMap = new LinkedHashMap<>();

    }

    public static synchronized AudioManager getInstance(Context context) {
        if (sAudioManager == null) {
            sAudioManager = new AudioManager(context.getApplicationContext());

        }
        return sAudioManager;
    }

    public ArrayList<Artist> getArtists() {
        ArrayList<Artist> artists = new ArrayList<Artist>();
        Cursor cursor = queryArtists();
        if (cursor != null && cursor.moveToFirst()) {
            int idColumn = cursor.getColumnIndex(MediaStore.Audio.Artists._ID);
            int artistColumn = cursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST);
            int numberColumn = cursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_TRACKS);

            do {
                Artist artist = new Artist();
                int id = cursor.getInt(idColumn);
                artist.setId(id);
                String name = cursor.getString(artistColumn);
                artist.setArtist(name);
                int numberOfSongs = cursor.getInt(numberColumn);
                artist.setNumberOfSongs(numberOfSongs);
                artists.add(artist);
            } while (cursor.moveToNext());
        }
        if (cursor != null)
            cursor.close();
        return artists;
    }

    public ArrayList<Song> getSongs() {
        //  ArrayList<Song> songs = new ArrayList<Song>();
        Cursor cursor = querySongs();
        if (cursor != null && cursor.moveToFirst()) {
            int idColumn = cursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int artistColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int titleColumn = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int albumIdColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
            int albumColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
            int pathColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            int durationColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION);

            do {
                Song song = new Song();
                int id = cursor.getInt(idColumn);
                song.setId(id);
                String artist = cursor.getString(artistColumn);
                song.setArtist(artist);
                String title = cursor.getString(titleColumn);
                song.setTitle(title);
                int albumId = cursor.getInt(albumIdColumn);
                song.setAlbumId(albumId);
                String album = cursor.getString(albumColumn);
                song.setAlbum(album);
                String path = cursor.getString(pathColumn);
                song.setPath(path);
                int duration = cursor.getInt(durationColumn);
                song.setDuration(duration);
                mSongs.add(song);
            } while (cursor.moveToNext());
        }
        if (cursor != null)
            cursor.close();
        return mSongs;
    }

    public ArrayList<Genre> getGenres() {
        ArrayList<Genre> genres = new ArrayList<>();
        Cursor cursor = queryGenres();
        if (cursor != null && cursor.moveToFirst()) {
            int idColumn = cursor.getColumnIndex(MediaStore.Audio.Genres._ID);
            int nameColumn = cursor.getColumnIndex(MediaStore.Audio.Genres.NAME);
            do {
                Genre genre = new Genre();
                int id = cursor.getInt(idColumn);
                genre.setId(id);
                String name = cursor.getString(nameColumn);
                genre.setName(name);
                genres.add(genre);
            } while (cursor.moveToNext());
        }
        if (cursor != null)
            cursor.close();
        return genres;
    }

    public ArrayList<Album> getAlbums() {
        ArrayList<Album> albums = new ArrayList<>();
        Cursor cursor = queryAlbums();
        if (cursor != null && cursor.moveToFirst()) {
            int idColumn = cursor.getColumnIndex(MediaStore.Audio.Albums._ID);
            int albumColumn = cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM);
            int numberColumn = cursor.getColumnIndex(MediaStore.Audio.Albums.NUMBER_OF_SONGS);
            int yearColumn = cursor.getColumnIndex(MediaStore.Audio.Albums.LAST_YEAR);
            int artistColumn = cursor.getColumnIndex(MediaStore.Audio.Albums.ARTIST);

            do {
                int id = cursor.getInt(idColumn);
                String title = cursor.getString(albumColumn);
                int number = cursor.getInt(numberColumn);
                String year = cursor.getString(yearColumn);
                String artist = cursor.getString(artistColumn);

                Album album = new Album();
                album.setId(id);
                album.setTitle(title);
                album.setNumberOfSongs(number);
                album.setLastYear(year);
                album.setArtist(artist);
               /* Log.i("TAG", "Album id: " + id);
                Log.i("TAG", "Album title: " + title);
                Log.i("TAG", "Album number: " + number);
                Log.i("TAG", "Album year: " + year);
                Log.i("TAG", "Album artist: " + artist);*/

                albums.add(album);
            } while (cursor.moveToNext());
        }
        if (cursor != null)
            cursor.close();
        return albums;
    }

    public ArrayList<Song> getSongsByArtistId(int idArtist) {
        ArrayList<Song> songs = new ArrayList<Song>();
        Cursor cursor = querySongsByArtist(idArtist);
        saveToList(cursor, songs);
        return songs;
    }

    public ArrayList<Song> getSongsByGenreId(int idGenre) {
        ArrayList<Song> songs = new ArrayList<Song>();
        Cursor cursor = querySongsByGenre(idGenre);
        saveToList(cursor, songs);
        return songs;
    }

    public ArrayList<Song> getSongsByAlbumId(int idAlbum) {
        ArrayList<Song> songs = new ArrayList<Song>();
        Cursor cursor = querySongsByAlbum(idAlbum);
        saveToList(cursor, songs);
        return songs;
    }


    private Cursor querySongsByGenre(int idGenre) {

        String[] projection = {MediaStore.Audio.Genres.Members._ID,
                MediaStore.Audio.Genres.Members.ARTIST,
                MediaStore.Audio.Genres.Members.TITLE,
                MediaStore.Audio.Genres.Members.ALBUM_ID,
                MediaStore.Audio.Genres.Members.ALBUM,
                MediaStore.Audio.Genres.Members.DATA,
                MediaStore.Audio.Genres.Members.DISPLAY_NAME,
                MediaStore.Audio.Genres.Members.DURATION,
        };
        String selection = MediaStore.Audio.Genres.Members.IS_MUSIC + " = 1";

        return queryAudioFiles(mContext, MediaStore.Audio.Genres.Members.getContentUri("external", Long.valueOf(idGenre)),
                projection, selection, null, null, 0);
    }


    private Cursor querySongsByAlbum(int idAlbum) {
        String[] projection = {
                MediaStore.Audio.Media._ID,             // 0
                MediaStore.Audio.Media.ARTIST,          // 1
                MediaStore.Audio.Media.TITLE,           // 2
                MediaStore.Audio.Media.ALBUM_ID,     // 3
                MediaStore.Audio.Media.ALBUM,           // 4
                MediaStore.Audio.Media.DATA,            // 5
                MediaStore.Audio.Media.DURATION
        };

        String selection = MediaStore.Audio.Media.IS_MUSIC + " = 1 AND " + MediaStore.Audio.Media.ALBUM_ID + "=?";
        String[] selectionArgs = new String[]{"" + idAlbum};

        return queryAudioFiles(mContext, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection, selection, selectionArgs, null, 0);
    }

    private Cursor querySongsByArtist(int idArtist) {
        String[] projection = {
                MediaStore.Audio.Media._ID,             // 0
                MediaStore.Audio.Media.ARTIST,          // 1
                MediaStore.Audio.Media.TITLE,           // 2
                MediaStore.Audio.Media.ALBUM_ID,     // 3
                MediaStore.Audio.Media.ALBUM,           // 4
                MediaStore.Audio.Media.DATA,            // 5
                MediaStore.Audio.Media.DURATION
        };      // 7

        String selection = MediaStore.Audio.Media.IS_MUSIC + " = 1 AND " + MediaStore.Audio.Media.ARTIST_ID + "=?";
        String[] selectionArgs = new String[]{"" + idArtist};

        return queryAudioFiles(mContext, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection, selection, selectionArgs, null, 0);
    }



    private Cursor querySongs() {
        String[] projection = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DURATION};
        String selection = MediaStore.Audio.Media.IS_MUSIC + " = 1";

        return queryAudioFiles(mContext, MediaStore.Audio.Media.getContentUri("external"),
                projection, selection, null, null, 0);
    }

    private Cursor queryArtists() {
        String[] projection = {
                MediaStore.Audio.Artists._ID,
                MediaStore.Audio.Artists.ARTIST,
                MediaStore.Audio.Artists.NUMBER_OF_ALBUMS,  //perhaps, it's not necessary
                MediaStore.Audio.Artists.NUMBER_OF_TRACKS}; //
        String sortOrder = MediaStore.Audio.Artists.ARTIST + " ASC";

        return queryAudioFiles(mContext, MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
                projection, null, null, sortOrder, 0);
    }

    private Cursor queryGenres() {
        String[] projection = {
                MediaStore.Audio.Genres._ID,
                MediaStore.Audio.Genres.NAME};
        return queryAudioFiles(mContext, MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI,
                projection, null, null, MediaStore.Audio.Genres.NAME, 0);
    }

    private Cursor queryAlbums() {
        String[] projection = {
                MediaStore.Audio.Albums._ID,
                MediaStore.Audio.Albums.ALBUM,            // 0
                MediaStore.Audio.Albums.NUMBER_OF_SONGS,  // 1
                MediaStore.Audio.Albums.LAST_YEAR,       // 2
                MediaStore.Audio.Albums.ARTIST,           // 3
        };

        return queryAudioFiles(mContext, MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                projection, null , null, null, 0);
    }


    private void saveToList(Cursor cursor, ArrayList<Song> list) {
        if (cursor != null && cursor.moveToFirst()) {
            int idColumn = cursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int artistColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int titleColumn = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int albumIdColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
            int albumColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
            int pathColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            int durationColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION);

            do {
                Song song = new Song();
                int id = cursor.getInt(idColumn);
                song.setId(id);
                String artist = cursor.getString(artistColumn);
                song.setArtist(artist);
                String title = cursor.getString(titleColumn);
                song.setTitle(title);
              //  Log.i("TAG", "saveToList title " + title);
                int albumId = cursor.getInt(albumIdColumn);
                song.setAlbumId(albumId);
                String album = cursor.getString(albumColumn);
                song.setAlbum(album);
                String path = cursor.getString(pathColumn);
                song.setPath(path);
                int duration = cursor.getInt(durationColumn);
                song.setDuration(duration);
                list.add(song);
            } while (cursor.moveToNext());
        }
        if (cursor != null)
            cursor.close();
    }


    private Cursor queryAudioFiles(Context context, Uri uri, String[] projection,
                                   String selection, String[] selectionArgs, String sortOrder, int limit) {
        try {
            ContentResolver resolver = context.getContentResolver();
            if (resolver == null) {
                return null;
            }
            if (limit > 0) {
                uri = uri.buildUpon().appendQueryParameter("limit", "" + limit).build();
            }
            return resolver.query(uri, projection, selection, selectionArgs, sortOrder);

        } catch (UnsupportedOperationException ex) {
            return null;
        }
    }

    public void fetchFolders() {
        if (mSongs == null) return;

        Folder folder = null;
        int id = 0;
        for (Song song : mSongs) {
            String pathToFolder = cutString(song.getPath());
            if (isFolderExist(pathToFolder)) {
                addSongToMap(folder, song);
            } else {
                folder = new Folder();
                folder.setId(id);
                folder.setTitle(getFolderTitle(pathToFolder));
                folder.setPath(pathToFolder);
                mFolders.add(folder);
                id++;
                addSongToMap(folder, song);
            }
        }
    }

    private void addSongToMap(Folder folder, Song song) {
        if (mFolderMap.size() == 0|| !mFolderMap.containsKey(folder.getId())) {
            ArrayList<Song> list = new ArrayList<>();
            list.add(song);
            mFolderMap.put(folder.getId(), list);
        } else {
            ArrayList<Song> list = mFolderMap.get(folder.getId());
            list.add(song);
        }
    }

    private boolean isFolderExist(String path) {
        if (mFolders == null) return false;
        int count = 0;
        for (Folder folder : mFolders) {
            if (folder.getPath().equals(path)) {
                count++;
                break;
            }
        }
        if (count == 1) return true;
        else return false;
    }

    private String cutString(String fullPath) {
        char slash = '/';
        String path = "";
        path = fullPath.substring(0, fullPath.lastIndexOf(slash));
        return path;
    }

    private String getFolderTitle(String path) {
        File file = new File(path);
        return file.getName();
    }


    public ArrayList<Song> getSongListByFolder(Folder folder) {
        if (mFolderMap.containsKey(folder.getId())) {
            return mFolderMap.get(folder.getId());
        }
        return null;
    }


    public ArrayList<Song> getSongsByFolderId(int id) {
        Set<Map.Entry<Integer, ArrayList<Song>>> set = mFolderMap.entrySet();
        for (Map.Entry<Integer, ArrayList<Song>> t : set) {
            if (t.getKey() == id)
                return t.getValue();
        }
        return null;
    }


    public ArrayList<Folder> getFolders() {
        return mFolders;
    }

}
