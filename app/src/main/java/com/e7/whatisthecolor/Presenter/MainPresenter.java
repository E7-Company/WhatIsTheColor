package com.e7.whatisthecolor.Presenter;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;
import android.widget.ImageView;

import com.e7.whatisthecolor.Functions.Colors;
import com.e7.whatisthecolor.Functions.Image;
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

    //region CAMERA PARAMETERS AND OPTIONS
    /** A safe way to get an instance of the Camera object. */
    public Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
            String error = "Camera is not available (in use or does not exist): " + e;
            view.showError(error);
        }
        return c; // returns null if camera is unavailable
    }

    /** Set the camera display oriented to screen. */
    public void setCameraDisplayOrientation(android.hardware.Camera camera) {
        android.hardware.Camera.CameraInfo camInfo =
                new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(getBackFacingCameraId(), camInfo);

        Display display = ((WindowManager) this.context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int rotation = display.getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (camInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (camInfo.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (camInfo.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }

    /** Detect if mobile has back camera. */
    private int getBackFacingCameraId() {
        int cameraId = -1;
        // Search for the front facing camera
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                cameraId = i;
                break;
            }
        }
        return cameraId;
    }

    /** Set better picture resolution to get the color from photo. */
    public Camera.Parameters setBestPictureResolution(Camera.Parameters parameters, int width, int height, SharedPreferences.Editor editor) {

        if (width == 0 | height == 0) {
            Camera.Size pictureSize = getBiggesttPictureSize(parameters);
            if (pictureSize != null)
                parameters
                        .setPictureSize(pictureSize.width, pictureSize.height);
            // save width and height in sharedprefrences
            width = pictureSize.width;
            height = pictureSize.height;
            editor.putInt("Picture_Width", width);
            editor.putInt("Picture_height", height);
            editor.commit();

        } else {
            parameters.setPictureSize(width, height);
        }

        return parameters;
    }

    /** Get the default biggest picture size. */
    private Camera.Size getBiggesttPictureSize(Camera.Parameters parameters) {
        Camera.Size result = null;

        for (Camera.Size size : parameters.getSupportedPictureSizes()) {
            if (result == null) {
                result = size;
            } else {
                int resultArea = result.width * result.height;
                int newArea = size.width * size.height;

                if (newArea > resultArea) {
                    result = size;
                }
            }
        }

        return (result);
    }

    //endregion

    /** Decode image data to bitmap */
    public Bitmap decodeBitmap(final byte[] data) {
        return Image.decodeBitmap(data);
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
