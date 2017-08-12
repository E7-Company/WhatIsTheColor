package com.e7.whatisthecolor;

import android.app.Application;

import com.e7.whatisthecolor.di.components.DaggerMainComponent;
import com.e7.whatisthecolor.di.components.MainComponent;
import com.e7.whatisthecolor.di.modules.MainModule;

/**
 * Created by Enrique on 10/08/2017.
 */

public class WITCApplication extends Application {
    private MainComponent mainComponent;

    @Override public void onCreate() {
        super.onCreate();
        initializeInjector();
    }

    private void initializeInjector() {
        mainComponent = DaggerMainComponent.builder().mainModule(new MainModule(this)).build();
    }

    public MainComponent getMainComponent() {
        return mainComponent;
    }
}
