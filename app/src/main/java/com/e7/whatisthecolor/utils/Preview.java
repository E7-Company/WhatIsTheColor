package com.e7.whatisthecolor.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.view.Display;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.hardware.Camera;
import android.view.WindowManager;
import android.widget.Toast;

import com.e7.whatisthecolor.view.presenter.MainPresenter;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Enrique on 13/08/2017.
 */

class Preview extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder mHolder;
    private Camera mCamera;
    private Camera.PictureCallback mCall;
    private SharedPreferences pref;

    private boolean safeToTakePicture = false;
    @Inject
    MainPresenter presenter;

    public Preview(Context context) {
        super(context);

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        pref = context.getSharedPreferences("MyPref", 0);
    }

    /** Touch screen event to take a picture. */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getAction()) {
            case MotionEvent.ACTION_UP:
                // The pressed gesture has finished,
                if (mCamera != null && safeToTakePicture) {
                    // Take a picture
                    mCamera.takePicture(shutterCallback, null, mCall);
                    safeToTakePicture = false;
                }
                break;

            default:
                break;
        }
        return true;
    }

    /** Call to sound and animation screen for camera shutter. */
    private final Camera.ShutterCallback shutterCallback = new Camera.ShutterCallback() {
        public void onShutter() {
            AudioManager mgr = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
            mgr.playSoundEffect(AudioManager.FLAG_PLAY_SOUND);
            //presenter.fadeAnimation(iv_image,0f,.7f,500);
        }
    };

    /** Method to detect surface is changed. */
    @Override
    public void surfaceChanged(SurfaceHolder sv, int arg1, int arg2, int arg3) {
        // get camera parameters
        Camera.Parameters parameters = mCamera.getParameters();

        // get biggest picture size
        SharedPreferences.Editor editor = pref.edit();
        int width = pref.getInt("Picture_Width", 0);
        int height = pref.getInt("Picture_height", 0);
        parameters = presenter.setBestPictureResolution(parameters, width, height, editor);
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        presenter.setCameraDisplayOrientation(mCamera);

        try {
            mCamera.setPreviewDisplay(sv);
        } catch (IOException e) {
            e.printStackTrace();
        }

        mCamera.setParameters(parameters);
        // set camera parameters

        mCamera.startPreview();
        safeToTakePicture = true;

        // sets what code should be executed after the picture is taken
        mCall = new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(final byte[] data, Camera camera) {
                // decode the data obtained by the camera into a Bitmap
                if (data != null) {
                    //presenter.fadeAnimation(iv_image,.7f,0f,500);
                    presenter.takeAPicture(data);
                    mCamera.startPreview();
                    safeToTakePicture = true;
                }
            }
        };
    }

    /** Method to detect surface is created. */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, acquire the camera and tell it where
        // to draw the preview.
        mCamera = presenter.getCameraInstance();
        if (mCamera != null) {
            try {
                mCamera.setPreviewDisplay(holder);

            } catch (IOException exception) {
                mCamera.release();
                mCamera = null;
            }
        } else
            Toast.makeText(getContext(), "Camera is not available",
                    Toast.LENGTH_SHORT).show();
    }

    /** Destroy method for surface. */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (mCamera != null) {
            // stop the preview
            mCamera.stopPreview();
            // release the camera
            mCamera.release();
        }
    }

    /** A safe way to get an instance of the Camera object. */
    public android.hardware.Camera getCameraInstance() {
        android.hardware.Camera c = null;
        try {
            c = android.hardware.Camera.open(); // attempt to get a Camera instance
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
            e.printStackTrace();
        }
        return c; // returns null if camera is unavailable
    }

    /** Set the camera display oriented to screen. */
    public void setCameraDisplayOrientation(android.hardware.Camera camera) {
        android.hardware.Camera.CameraInfo camInfo =
                new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(getBackFacingCameraId(), camInfo);

        Display display = ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
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
        if (camInfo.facing == android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT) {
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
        int numberOfCameras = android.hardware.Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
            android.hardware.Camera.getCameraInfo(i, info);
            if (info.facing == android.hardware.Camera.CameraInfo.CAMERA_FACING_BACK) {
                cameraId = i;
                break;
            }
        }
        return cameraId;
    }

    /** Set better picture resolution to get the color from photo. */
    public android.hardware.Camera.Parameters setBestPictureResolution(android.hardware.Camera.Parameters parameters, int width, int height, SharedPreferences.Editor editor) {

        if (width == 0 | height == 0) {
            android.hardware.Camera.Size pictureSize = getBiggesttPictureSize(parameters);
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
    private android.hardware.Camera.Size getBiggesttPictureSize(android.hardware.Camera.Parameters parameters) {
        android.hardware.Camera.Size result = null;

        for (android.hardware.Camera.Size size : parameters.getSupportedPictureSizes()) {
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
}

