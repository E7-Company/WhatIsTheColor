package com.e7.whatisthecolor.Presenter;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.widget.ImageView;

/**
 * Created by Enrique on 14/06/2017.
 */

public interface IMainPresenter {

    /**
     * Start presenter process.
     */
    public void start();

    /** A safe way to get an instance of the Camera object.
     *
     * @return           the camera object
     */
    public Camera getCameraInstance();

    /** Set the camera display oriented to screen.
     *
     * @param camera    the camera object to set the orientation
     * */
    public void setCameraDisplayOrientation(android.hardware.Camera camera);

    /** Set better picture resolution to get the color from photo.
     * @param  parameters   camera parameters by default
     * @param  width        width resolution for the picture
     * @param  height       height resolution for the picture
     * @param  editor       preference object to save the data
     *
     * @return              the best parameters for the picture resolution
     */
    public Camera.Parameters setBestPictureResolution(Camera.Parameters parameters, int width, int height, SharedPreferences.Editor editor);

    /** Decode image data to bitmap
     *
     * @param  data     byte array with the data from image
     * @return          bitmap created by data image
     */
    public Bitmap decodeBitmap(final byte[] data);

    /**
     * Fade animation process to set an alpha start and end in a specified time.
     *
     * @param  v            imageView on set the fade animation
     * @param  begin_alpha  float number with the alpha to start
     * @param  end_alpha    float number with the alpha to end
     * @param  time         time to do the animation
     */
    public void FadeAnimation(final ImageView v,
                                     final float begin_alpha, final float end_alpha, int time);

    /**
     * Get the color from a bitmap and speech his name.
     *
     * @param  bmp  bitmap to get the color
     */
    public void BitmapColor(final Bitmap bmp);
}
