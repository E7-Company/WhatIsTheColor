package com.e7.whatisthecolor.Presenter;

import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * Created by Enrique on 14/06/2017.
 */

public interface IMainPresenter {

    /**
     * Start presenter process.
     */
    public void start();

    /**
     * Fade animation process to set an alpha start and end in a specified time.
     *
     * @param  v            ImageView on set the fade animation
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
