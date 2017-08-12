package com.e7.whatisthecolor.view.viewmodel.mapper;

import com.e7.whatisthecolor.data.repository.datasource.mapper.Mapper;
import com.e7.whatisthecolor.domain.model.Color;
import com.e7.whatisthecolor.view.viewmodel.ColorViewModel;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Enrique on 12/08/2017.
 */

@Singleton
public class ColorViewModelToColorMapper extends Mapper<ColorViewModel, Color> {

    @Inject public ColorViewModelToColorMapper() {}

    @Override
    public Color map(ColorViewModel value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ColorViewModel reverseMap(Color value) {
        ColorViewModel colorViewModel = new ColorViewModel();
        colorViewModel.setHex(value.getHex());
        colorViewModel.setName(value.getName());
        colorViewModel.setRed(value.getRed());
        colorViewModel.setGreen(value.getGreen());
        colorViewModel.setBlue(value.getBlue());
        colorViewModel.setHue(value.getHue());
        colorViewModel.setSaturation(value.getSaturation());
        colorViewModel.setLightness(value.getLightness());
        return colorViewModel;
    }
}
