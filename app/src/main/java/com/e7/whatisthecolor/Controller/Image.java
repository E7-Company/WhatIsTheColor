package com.e7.whatisthecolor.Controller;

import android.graphics.*;

/**
 * Created by Enrique on 10/06/2017.
 */

/** Singleton Image class */
public class Image {

    /**
     * Returns a Bitmap object conversion from a byte array data.
     * <p>
     * If data is null return a null Bitmap object.
     *
     * @param  data an array of bytes from an image
     * @return      the data converted in a Bitmap object
     * @see         Bitmap
     */
    public static Bitmap decodeBitmap(byte[] data) {

        Bitmap bitmap = null;
        BitmapFactory.Options bfOptions = new BitmapFactory.Options();
        bfOptions.inDither = false; // Disable Dithering mode
        bfOptions.inPurgeable = true; // Tell to gc that whether it needs free
        // memory, the Bitmap can be cleared
        bfOptions.inInputShareable = true; // Which kind of reference will be
        // used to recover the Bitmap data
        // after being clear, when it will
        // be used in the future
        bfOptions.inTempStorage = new byte[32 * 1024];

        if (data != null)
            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length,
                    bfOptions);

        return bitmap;
    }

    /**
     * Function to get the average RGB color from a Bitmap object.
     *
     * @param  bitmap a Bitmap to obtain the color
     * @return        the average RGB from Bitmap
     */
    public static int getAverageColor(Bitmap bitmap) {
        int redBucket = 0;
        int greenBucket = 0;
        int blueBucket = 0;
        int pixelCount = 0;

        //Get all the RGB data from pixels at the image
        for (int y = 0; y < bitmap.getHeight(); y++) {
            for (int x = 0; x < bitmap.getWidth(); x++) {
                int c = bitmap.getPixel(x, y);

                pixelCount++;
                redBucket += android.graphics.Color.red(c);
                greenBucket += android.graphics.Color.green(c);
                blueBucket += android.graphics.Color.blue(c);
            }
        }

        int averageColor = android.graphics.Color.rgb(redBucket / pixelCount, greenBucket
                / pixelCount, blueBucket / pixelCount);
        return averageColor;
    }

}
