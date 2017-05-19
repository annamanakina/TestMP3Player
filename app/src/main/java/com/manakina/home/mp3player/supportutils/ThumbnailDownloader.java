package com.manakina.home.mp3player.supportutils;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class ThumbnailDownloader<Token> extends HandlerThread {
    private static final String TAG_THUMBNAIL = "ThumbnailDownloader";
    private static final int MESSAGE_DOWNLOAD = 0;
   // private static final int MESSAGE_CACHE = 1;


    static Handler mHandler;
    Handler mResponceHandler;

    Listener<Token> mListener;
    Map<Token, String> requestMap = Collections.synchronizedMap(new HashMap<Token, String>());

    public interface Listener<Token> {
        void onThumbnailDownloaded(Token token, Bitmap bitmap);
    }

    public void setListener(Listener<Token> listener) {
        mListener = listener;
    }


    public ThumbnailDownloader(Handler responceHandler) {
        super(TAG_THUMBNAIL);
        mResponceHandler = responceHandler;
    }

    //create a receiver object
    @SuppressLint("HandlerLeak")
    @Override
    protected void onLooperPrepared() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                if (msg.what == MESSAGE_DOWNLOAD) {
                    @SuppressWarnings("unchecked")
                       Token token = (Token) msg.obj;
                    handleRequest(token);
                }
            }
        };

    }

    private void handleRequest(final Token token) {
        try {
            final String path = requestMap.get(token);

            if (path == null ) {
                return;
            }
            final Bitmap bitmap;

            if (ImageCache.getBitmapFromMemoryCache(path) == null) {

                bitmap = MusicThumbnailUtils.decodeSampledBitmapFromResource(path, 90, 90);
                if (bitmap != null) {
                    ImageCache.addBitmapToMemoryCache(path, bitmap);
                }

            } else {
                bitmap = ImageCache.getBitmapFromMemoryCache(path);

            }


            mResponceHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (requestMap.get(token) != path) return;
                    requestMap.remove(token);
                    mListener.onThumbnailDownloaded(token, bitmap);
                }
            });
        } catch (Exception ioex) {
            Log.i("TAG", "Error downloading image", ioex);
        }
    }

    public void queueThumbnail(Token token, String path) {
        requestMap.put(token, path);

            mHandler.obtainMessage(MESSAGE_DOWNLOAD, token)
                    .sendToTarget();
    }

    public void clearQueue() {
        mHandler.removeMessages(MESSAGE_DOWNLOAD);
        requestMap.clear();
    }

    /*  public void queueThumbnail(String path) {
        Log.i("TAG", "queueThumbnail MESSAGE_CACHE: " + path);
        mHandler.obtainMessage(MESSAGE_CACHE, path)
                .sendToTarget();
    }*/




    /*public void clearCacheImage() {
        bitmapCache.evictAll();
        Log.i("TAG", "clearCacheImage" + (int) (Runtime.getRuntime().totalMemory() / 1024));
    }*/

}
