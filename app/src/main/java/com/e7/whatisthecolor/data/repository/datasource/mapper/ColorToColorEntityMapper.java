package com.e7.whatisthecolor.data.repository.datasource.mapper;

import com.e7.whatisthecolor.data.entity.ColorEntity;
import com.e7.whatisthecolor.domain.model.Color;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Enrique on 11/08/2017.
 */

@Singleton public class ColorToColorEntityMapper extends Mapper <Color, ColorEntity> {

    @Inject public ColorToColorEntityMapper() {}

    @Override
    public ColorEntity map(Color value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Color reverseMap(ColorEntity value) {
        Color color = new Color(value.getHex(), value.getName());
        return color;
    }
}
