package com.e7.whatisthecolor.di.modules;

import android.content.Context;

import com.e7.whatisthecolor.WITCApplication;
import com.e7.whatisthecolor.data.repository.ColorRepository;
import com.e7.whatisthecolor.data.repository.IColorRepository;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Enrique on 11/08/2017.
 */

@Module
public class MainModule {
    private final WITCApplication witcApplication;

    public MainModule(WITCApplication witcApplication) {
        this.witcApplication = witcApplication;
    }

    @Provides
    @Singleton
    Context provideApplicationContext() {
        return witcApplication;
    }

    @Provides @Singleton
    IColorRepository provideRepository(ColorRepository colorRepository) {
        return colorRepository;
    }

    @Provides @Named("executor_thread")
    Scheduler provideExecutorThread() {
        return Schedulers.io();
    }

    @Provides @Named("ui_thread") Scheduler provideUiThread() {
        return AndroidSchedulers.mainThread();
    }
}
