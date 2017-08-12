package com.e7.whatisthecolor.view.presenter;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;
import android.widget.ImageView;

import com.e7.whatisthecolor.utils.Image;
import com.e7.whatisthecolor.domain.model.Color;
import com.e7.whatisthecolor.domain.usecase.GetColors;
import com.e7.whatisthecolor.domain.usecase.UseCaseObserver;
import com.e7.whatisthecolor.view.activity.IMainActivity;
import com.e7.whatisthecolor.view.viewmodel.ColorViewModel;
import com.e7.whatisthecolor.view.viewmodel.mapper.ColorViewModelToColorMapper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Enrique on 14/06/2017.
 */

public class MainPresenter extends Presenter<IMainActivity> {

    private Context context;
    private IMainActivity view;
    private GetColors getColors;
    private ColorViewModelToColorMapper mapper;
    private List<ColorViewModel> colors;

    public IMainActivity getView() {
        return view;
    }

    public void setView(IMainActivity view) {
        this.view = view;
    }

    /** Constructor to set the context and the interface view. */
    @Inject
    public MainPresenter (Context context, GetColors getColors, ColorViewModelToColorMapper mapper) {
        this.context = context;
        this.getColors = getColors;
        this.mapper = mapper;
    }

    /** Start loading colors from a file with Asynctask. */
    public void start() {
        getView().showProgress("Loading Colors...");
        getColors.execute(new ColorListObserver());
    }

    public void destroy() {
        this.getColors.dispose();
        setView(null);
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

    /** Decode bitmap and show and speech color name */
    public void takeAPicture(byte[] data) {
        Bitmap bmp = decodeBitmap(data);
        bitmapColor(bmp);
    }

    /** Decode image data to bitmap */
    private Bitmap decodeBitmap(final byte[] data) {
        return Image.decodeBitmap(data);
    }

    /** Animation to fade an ImageView. */
    public void fadeAnimation(final ImageView v,
                                     final float begin_alpha, final float end_alpha, int time) {
        ObjectAnimator fade = ObjectAnimator.ofFloat(v, "alpha",  begin_alpha, end_alpha);
        fade.setDuration(time);

        final AnimatorSet mAnimationSet = new AnimatorSet();

        mAnimationSet.play(fade);
        mAnimationSet.start();
    }


    /** Get color from a bitmap. */
    private void bitmapColor(final Bitmap bmp) {
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
                final String colorName = getColorName(colorHex);
                Log.e(context.getPackageName(), "COLOR NAME: " + colorName);

                //Speech the color
                view.speechText(colorName);
            }
        }).start();
    }

    /**
     * Returns a String with the name of the color more approximated
     * from the colors array loaded.
     * <p>
     * This method calc the RGB and HSL from the hexadecimal String
     * and compare it with all the list from colors.
     *
     * @param  color the hexadecimal number from a color
     * @return       a color name
     */
    public String getColorName(String color) {
        int c = (int)Long.parseLong(color, 16);
        int r = (c >> 16) & 0xFF;
        int g = (c >> 8) & 0xFF;
        int b = (c >> 0) & 0xFF;

        int []hsl = Color.rgbToHsl(r,g,b);
        int h = hsl[0], s = hsl[1], l = hsl[2];

        int ndf1;
        int ndf2;
        int ndf;
        int cl = -1, df = -1;

        for(int i = 0; i < colors.size(); i++)
        {
            if(color == "#" + colors.get(i).getHex())
                return colors.get(i).getName();

            ndf1 = (int) (Math.pow(r - Integer.parseInt(colors.get(i).getRed()), 2) + Math.pow(g - Integer.parseInt(colors.get(i).getGreen()), 2) + Math.pow(b - Integer.parseInt(colors.get(i).getBlue()), 2));
            ndf2 = (int) (Math.pow(h - Integer.parseInt(colors.get(i).getHue()), 2) + Math.pow(s - Integer.parseInt(colors.get(i).getSaturation()), 2) + Math.pow(l - Integer.parseInt(colors.get(i).getLightness()), 2));
            ndf = ndf1 + ndf2 * 2;
            if(df < 0 || df > ndf)
            {
                df = ndf;
                cl = i;
            }
        }

        return colors.get(cl).getName();
    }

    private final class ColorListObserver extends UseCaseObserver<List<Color>> {

        @Override public void onComplete() {
            getView().dismissProgress();
        }

        @Override public void onError(Throwable e) {
            getView().dismissProgress();
            e.printStackTrace();
        }

        @Override public void onNext(List<Color> colorList) {
            List<ColorViewModel> colorViewModels = mapper.reverseMap(colorList);
            colors = new ArrayList<ColorViewModel>(colorViewModels);
        }
    }
}
