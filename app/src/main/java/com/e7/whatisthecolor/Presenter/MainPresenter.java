package com.e7.whatisthecolor.Presenter;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.e7.whatisthecolor.Controller.Colors;
import com.e7.whatisthecolor.Controller.Image;
import com.e7.whatisthecolor.View.IMainActivity;

/**
 * Created by Enrique on 14/06/2017.
 */

public class MainPresenter implements IMainPresenter {

    private IMainActivity view;
    private Context context;

    /** Constructor to set the context and the interface view. */
    public MainPresenter (IMainActivity view, Context context) {
        this.view = view;
        this.context = context;
    }

    /** Start loading colors from a file with Asynctask. */
    public void start() {
        new LoadColors().execute();
    }

    /** Animation to fade an ImageView. */
    public void FadeAnimation(final ImageView v,
                                     final float begin_alpha, final float end_alpha, int time) {
        ObjectAnimator fade = ObjectAnimator.ofFloat(v, "alpha",  begin_alpha, end_alpha);
        fade.setDuration(time);

        final AnimatorSet mAnimationSet = new AnimatorSet();

        mAnimationSet.play(fade);
        mAnimationSet.start();
    }

    /** Get color from a bitmap. */
    public void BitmapColor(final Bitmap bmp) {
        new Thread(new Runnable() {
            public void run() {
                //Reduce bitmap size
                final Bitmap resizebitmap = Bitmap.createBitmap(bmp,
                        bmp.getWidth() / 2, bmp.getHeight() / 2, 60, 60);

                //Get color from bitmap in Hex format
                final int color = Image.getAverageColor(resizebitmap);

                String colorHex = String.format("%06X", 0xFFFFFF & color);
                Log.e(context.getPackageName(), "COLOR: " + colorHex);

                //Get the color String name
                final String colorName = Colors.getColorName(colorHex);
                Log.e(context.getPackageName(), "COLOR NAME: " + colorName);

                //Speech the color
                view.speechText(colorName);
            }
        }).start();
    }

    /** Asynchronous task Class to load color. */
    class LoadColors extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            view.showProgress("Loading colors...");
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Colors.loadColors(context, "colors.json");
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            view.dismissProgress();
        }

    }
}
