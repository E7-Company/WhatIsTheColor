package com.e7.whatisthecolor.data.repository.datasource;

import com.e7.whatisthecolor.data.entity.ColorEntity;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Enrique on 11/08/2017.
 */

public interface IDataSource {
    /**
     * Get an 'Observable<List<ColorEntity>>'  which will emit a List of {@link ColorEntity}.
     */
    Observable<List<ColorEntity>> colorEntityList();

}
