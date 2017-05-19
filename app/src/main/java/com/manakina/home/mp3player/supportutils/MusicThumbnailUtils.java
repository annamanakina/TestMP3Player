package com.manakina.home.mp3player.supportutils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;


public class MusicThumbnailUtils {



    public static Bitmap decodeSampledBitmapFromResource(String path,
                                                         int reqWidth, int reqHeight) {

        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(path);

        byte[] bitmapBytes = mmr.getEmbeddedPicture();

        if (bitmapBytes == null) {
            mmr.release();
            return null;
        } else {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length, options);
            options.inSampleSize = calculateInSampleSize(options, reqWidth,
                    reqHeight);

            options.inJustDecodeBounds = false;

            options.inPreferredConfig = Bitmap.Config.RGB_565;
            mmr.release();
            return BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length, options);
        }

    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {

        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

}
