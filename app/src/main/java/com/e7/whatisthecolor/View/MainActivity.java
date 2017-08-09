package com.e7.whatisthecolor.View;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.hardware.Camera;
import android.media.AudioManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ImageView;
import android.widget.Toast;

import com.e7.whatisthecolor.Presenter.IMainPresenter;
import com.e7.whatisthecolor.Presenter.MainPresenter;
import com.e7.whatisthecolor.R;
import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.aboutlibraries.LibsBuilder;

import java.io.IOException;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity  implements IMainActivity, SurfaceHolder.Callback {

    //region VARIABLES
    // a variable to store a reference to the Image View at the main.xml file
    @BindView(R.id.imageView) ImageView iv_image;
    // a variable to store a reference to the Surface View at the main.xml file
    @BindView(R.id.surfaceView) SurfaceView sv;
    // the toolbar for the app
    @BindView(R.id.toolbar) Toolbar toolbar;

    // a bitmap to display the captured image
    private Bitmap bmp;

    // Camera variables
    // a surface holder
    private SurfaceHolder sHolder;
    // a variable to control the camera
    private Camera mCamera;
    // the camera parameters
    private Camera.Parameters parameters;
    private Camera.PictureCallback mCall;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private boolean safeToTakePicture = false;
    private TextToSpeech tts;
    private ProgressDialog progress;

    private IMainPresenter presenter;
    //endregion

    public void setPresenter(IMainPresenter presenter) {
        this.presenter = presenter;
    }

    public IMainPresenter presenter() {
        if (presenter == null) {
            throw new IllegalStateException("Trying to use unseted presenter");
        }

        return presenter;
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        toolbar.setTitleTextColor(Color.BLACK);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_launcher);

        setPresenter(new MainPresenter(this, this));
        presenter().start();

        // Init TextToSpeech
        initTTS();

        pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        editor = pref.edit();

        // Get a surface
        sHolder = sv.getHolder();

        // Add the callback interface methods defined below as the Surface View callbacks
        sHolder.addCallback(this);

        // Tells Android that this surface will have its data constantly replaced
        sHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    /** Init TextToSpeech and select default language */
    private void initTTS() {
        tts=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    int result = tts.setLanguage(Locale.US);

                    //If language not exists, install it
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        showError("This Language is not supported");
                        Intent installIntent = new Intent();
                        installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                        startActivity(installIntent);
                    }
                }
            }
        });
    }

    //region SHARED FUNCTIONS
    /** Speech the text on UI thread with TTS. */
    public void speechText(final String text) {
        runOnUiThread(new Runnable() {
            public void run() {
                if (!tts.isSpeaking()) {
                    Toast.makeText(getApplicationContext(), text,
                            Toast.LENGTH_SHORT).show();
                    tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });
    }

    /** Show progress view. */
    public void showProgress(String message) {
        progress = new ProgressDialog(MainActivity.this);
        progress.setMessage(message);
        progress.setTitle(getResources().getString(R.string.app_name));
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();
    }

    /** Dismiss progress view. */
    public void dismissProgress() {
        progress.dismiss();
    }

    /** Show error message in a toast and in the log */
    public void showError(String error) {
        Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
        Log.i(getPackageName(), error);
    }
    //endregion

    // region GET PHOTO FROM CAMERA WHEN TOUCH THE SCREEN
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
            AudioManager mgr = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            mgr.playSoundEffect(AudioManager.FLAG_PLAY_SOUND);
            presenter().FadeAnimation(iv_image,0f,.7f,500);
        }
    };

    //region SURFACE METHODS
    /** Method to detect surface is changed. */
    @Override
    public void surfaceChanged(SurfaceHolder sv, int arg1, int arg2, int arg3) {
        // get camera parameters
        parameters = mCamera.getParameters();

        // get biggest picture size
        int width = pref.getInt("Picture_Width", 0);
        int height = pref.getInt("Picture_height", 0);
        parameters = presenter().setBestPictureResolution(parameters, width, height, editor);
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        presenter().setCameraDisplayOrientation(mCamera);

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
                    bmp = presenter().decodeBitmap(data);
                }
                // set the iv_image
                if (bmp != null) {
                    presenter().FadeAnimation(iv_image,.7f,0f,500);
                    presenter().BitmapColor(bmp);
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
        mCamera = presenter().getCameraInstance();
        if (mCamera != null) {
            try {
                mCamera.setPreviewDisplay(holder);

            } catch (IOException exception) {
                mCamera.release();
                mCamera = null;
            }
        } else
            Toast.makeText(getApplicationContext(), "Camera is not available",
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
    //endregion

    //endregion

    //region MENU
    /** Options Menu. */
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.about_menu, menu);
        return true;
    }

    /** Item from menu selected. */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.info:
                new LibsBuilder()
                        .withActivityTitle("About " + getString(R.string.app_name))
                        .withFields(R.string.class.getFields())
                        .withAboutIconShown(true)
                        .withAboutVersionShownName(true)
                        .withAboutDescription("Copyright \u00a9 2017 - E7 Company<br>All rights reserved<br><a href=http://www.e7company.tk>www.e7company.tk</a>")
                        .withActivityStyle(Libs.ActivityStyle.LIGHT)
                        //start the activity
                        .start(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    //endregion

}
