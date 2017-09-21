package com.e7.whatisthecolor.data.entity.mapper.data;

import com.e7.whatisthecolor.data.entity.ColorEntity;

/**
 * Created by Enrique on 20/09/2017.
 */

public class FakeColorLocalAPI {
    private static final String JSON_RESPONSE_COLOR ="{\"hex\" : \"000000\", \"name\" : \"Black\" }";
    private static final String JSON_RESPONSE_COLOR_COLLECTION = "[\n" +
            "\t\t{\"hex\" : \"000000\", \"name\" : \"Black\" },\n" +
            "\t\t{\"hex\" : \"FFFFFF\", \"name\" : \"White\" }\n" +
            "\t]";

    public static String getJsonResponseColor() {
        return JSON_RESPONSE_COLOR;
    }

    public static String getJsonResponseColorCollection() {
        return JSON_RESPONSE_COLOR_COLLECTION;
    }

    private final static String FAKE_COLOR_HEX = "000000";
    private final static String FAKE_COLOR_NAME = "Black";

    public static ColorEntity getFakeColorEntity() {
        ColorEntity colorEntity = new ColorEntity();
        colorEntity.setHex(FAKE_COLOR_HEX);
        colorEntity.setName(FAKE_COLOR_NAME);
        //you can try set each attribute is possible
        return colorEntity;
    }
}
