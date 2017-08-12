package com.e7.whatisthecolor.data.repository;

import android.support.annotation.NonNull;

import com.e7.whatisthecolor.data.entity.ColorEntity;
import com.e7.whatisthecolor.data.repository.datasource.ColorDataSourceFactory;
import com.e7.whatisthecolor.data.repository.datasource.IDataSource;
import com.e7.whatisthecolor.data.repository.datasource.mapper.ColorToColorEntityMapper;
import com.e7.whatisthecolor.domain.model.Color;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * Created by Enrique on 11/08/2017.
 */

@Singleton
public class ColorRepository implements IColorRepository {

    private final ColorToColorEntityMapper colorToColorEntityMapper;
    private final IDataSource dataSource;

    @Inject
    public ColorRepository(@NonNull ColorDataSourceFactory colorDataSourceFactory,
                           @NonNull ColorToColorEntityMapper colorToColorEntityMapper) {
        this.colorToColorEntityMapper = colorToColorEntityMapper;
        this.dataSource = colorDataSourceFactory.createDataSource();
    }

    @Override
    public Observable<List<Color>> colorList() {
        return dataSource.colorEntityList().map(new Function<List<ColorEntity>, List<Color>>() {
            @Override public List<Color> apply(List<ColorEntity> colorEntities) throws Exception {
                return colorToColorEntityMapper.reverseMap(colorEntities);
            }
        });
    }
}
