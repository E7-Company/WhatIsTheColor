package com.e7.whatisthecolor.data.repository.datasource.mapper;

import com.e7.whatisthecolor.data.entity.ColorEntity;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Enrique on 11/08/2017.
 */

public class ColorEntityJsonMapper {
    private final Gson gson;

    @Inject
    public ColorEntityJsonMapper() {
        gson = new Gson();
    }

    public ColorEntity transformColorEntity(String colorJsonResponse) throws JsonSyntaxException {
        ColorEntity colorEntity;
        try {
            Type typeColor = new TypeToken<ColorEntity>() {
            }.getType();
            colorEntity = this.gson.fromJson(colorJsonResponse, typeColor);
            return colorEntity;
        } catch (JsonSyntaxException exception) {
            exception.printStackTrace();
            throw exception;
        }
    }

    public List<ColorEntity> transformColorEntityCollection(String colorListJsonResponse)
            throws JsonSyntaxException {
        List<ColorEntity> colorEntityList;
        try {
            Type typeColorList = new TypeToken<List<ColorEntity>>() {
            }.getType();
            colorEntityList = this.gson.fromJson(colorListJsonResponse, typeColorList);
            return colorEntityList;
        } catch (JsonSyntaxException exception) {
            exception.printStackTrace();
            throw exception;
        }
    }
}