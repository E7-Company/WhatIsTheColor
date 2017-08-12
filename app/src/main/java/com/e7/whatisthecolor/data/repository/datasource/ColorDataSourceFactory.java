package com.e7.whatisthecolor.data.repository.datasource;

import android.content.Context;
import android.support.annotation.NonNull;

import com.e7.whatisthecolor.data.local.LocalApi;
import com.e7.whatisthecolor.data.repository.datasource.mapper.ColorEntityJsonMapper;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Enrique on 11/08/2017.
 */

@Singleton
public class ColorDataSourceFactory {
    private final Context context;

    @Inject
    public ColorDataSourceFactory(@NonNull Context context) {
        this.context = context;
    }

    public ColorLocalApiDataSource createDataSource() {
        ColorEntityJsonMapper teamEntityJsonMapper = new ColorEntityJsonMapper();
        LocalApi local = new LocalApi(context, teamEntityJsonMapper);
        return new ColorLocalApiDataSource(local);
    }
}
