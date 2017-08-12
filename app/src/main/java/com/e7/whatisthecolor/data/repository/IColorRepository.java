package com.e7.whatisthecolor.data.repository;

import com.e7.whatisthecolor.domain.model.Color;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Enrique on 11/08/2017.
 */

public interface IColorRepository {
    /**
     * Get an 'Observable<List<Color>>'  which will emit a List of {@link Color}.
     */
    Observable<List<Color>> colorList();
}
