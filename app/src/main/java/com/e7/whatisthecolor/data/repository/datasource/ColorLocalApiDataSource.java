package com.e7.whatisthecolor.data.repository.datasource;

import com.e7.whatisthecolor.data.entity.ColorEntity;
import com.e7.whatisthecolor.data.local.LocalApi;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Enrique on 11/08/2017.
 */

public class ColorLocalApiDataSource implements IDataSource {
    private final LocalApi localApi;

    public ColorLocalApiDataSource(LocalApi localApi) {
        this.localApi = localApi;
    }

    @Override
    public Observable<List<ColorEntity>> colorEntityList() {
        return this.localApi.colorEntityList();
    }
}
