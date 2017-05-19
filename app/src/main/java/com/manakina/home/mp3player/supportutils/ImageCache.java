package com.manakina.home.mp3player.supportutils;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;



public class ImageCache extends LruCache<String, Bitmap>{
    private volatile static ImageCache sImageCache;

    private ImageCache( int maxSize) {
        super(maxSize);
    }

    public static ImageCache getInstance(int size) {
        if (sImageCache == null) {
            synchronized (ImageCache.class) {
                if (sImageCache == null) {
                    sImageCache = new ImageCache(size);
                }
            }
        }
        return sImageCache;
    }

    @Override
    protected int sizeOf(String key, Bitmap value) {
        return value.getByteCount() / 1024;
    }

    public static Bitmap getBitmapFromMemoryCache(String key) {
      //  Log.i("TAG", "getBitmapFromMemoryCache!- " + key);
        if (key == null) {
            return null;
        }
        return sImageCache.get(key);
    }

    public static void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemoryCache(key) == null) {
            sImageCache.put(key, bitmap);
        }
    }
}
