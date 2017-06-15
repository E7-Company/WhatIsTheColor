package com.e7.whatisthecolor.View;

/**
 * Created by Enrique on 14/06/2017.
 */

public interface IMainActivity {

    /**
     * Speech a text with TTS.
     *
     * @param  text   String with the text to speech
     */
    public void speechText(final String text);

    /**
     * Show a progress view with a defined message.
     *
     * @param  message   String with the message to show
     */
    public void showProgress(String message);

    /**
     * Dismiss the progress view.
     */
    public void dismissProgress();

}
