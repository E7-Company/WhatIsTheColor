package com.e7.whatisthecolor.data.entity.mapper;

import com.e7.whatisthecolor.data.entity.ColorEntity;
import com.e7.whatisthecolor.data.entity.mapper.data.FakeColorLocalAPI;
import com.e7.whatisthecolor.data.repository.datasource.mapper.ColorEntityJsonMapper;
import com.google.gson.JsonSyntaxException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Collection;

/**
 * Created by Enrique on 19/09/2017.
 */

public class ColorEntityJsonMapperTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    private ColorEntityJsonMapper colorEntityJsonMapper;

    @Before
    public void setUp() {
        colorEntityJsonMapper = new ColorEntityJsonMapper();
    }

    @Test
    public void givenTransformCollectionColorEntityCorrectly() {
        final String FAKE_JSON_RESPONSE_COLOR_COLLECTION =
                FakeColorLocalAPI.getJsonResponseColorCollection();
        Collection<ColorEntity> colorEntities =
                colorEntityJsonMapper.transformColorEntityCollection(FAKE_JSON_RESPONSE_COLOR_COLLECTION);
        final ColorEntity colorEntityOne = ((ColorEntity) colorEntities.toArray()[0]);
        final ColorEntity colorEntityTwo = ((ColorEntity) colorEntities.toArray()[1]);
        assertThat(colorEntityOne.getHex(), is("000000"));
        assertThat(colorEntityTwo.getHex(), is("FFFFFF"));
        assertThat(colorEntities.size(), is(2));
    }

    @Test public void givenTransformColorEntityCorrectly() {
        final String FAKE_JSON_RESPONSE_COLOR = FakeColorLocalAPI.getJsonResponseColor();
        ColorEntity colorEntity = colorEntityJsonMapper.transformColorEntity(FAKE_JSON_RESPONSE_COLOR);
        assertThat(colorEntity.getHex(), is("000000"));
        assertThat(colorEntity.getName(), is("Black"));
    }

    @Test public void givenExpectedExceptionTransformUserEntityCollectionNotValidResponse() {
        expectedException.expect(JsonSyntaxException.class);
        colorEntityJsonMapper.transformColorEntityCollection("Expects a json array like response");
    }

    @Test public void givenExpectedExceptionTransformUserEntityNotValidResponse() {
        expectedException.expect(JsonSyntaxException.class);
        colorEntityJsonMapper.transformColorEntity("Expects a json object like response");
    }
}
