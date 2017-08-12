package com.e7.whatisthecolor.di.components;

import android.content.Context;

import com.e7.whatisthecolor.view.activity.MainActivity;
import com.e7.whatisthecolor.di.modules.MainModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Enrique on 11/08/2017.
 */

@Singleton
@Component(modules = MainModule.class) public interface MainComponent {

    void inject(MainActivity activity);

    Context context();
}