package com.e7.whatisthecolor.data.entity.mapper;

import com.e7.whatisthecolor.data.entity.ColorEntity;
import com.e7.whatisthecolor.data.entity.mapper.data.FakeColorLocalAPI;
import com.e7.whatisthecolor.data.repository.datasource.mapper.ColorToColorEntityMapper;
import com.e7.whatisthecolor.domain.model.Color;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Enrique on 20/09/2017.
 */

@RunWith(MockitoJUnitRunner.class)
public class ColorToColorEntityMapperTest {
    private final static String FAKE_COLOR_HEX = "000000";
    private final static String FAKE_COLOR_NAME = "Black";
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    private ColorToColorEntityMapper colorToColorEntityMapper;

    @Before
    public void setUp() {
        colorToColorEntityMapper = new ColorToColorEntityMapper();
    }

    @Test
    public void givenTransformColorEntityToColor() throws Exception {
        ColorEntity colorEntity = FakeColorLocalAPI.getFakeColorEntity();
        Color color = colorToColorEntityMapper.reverseMap(colorEntity);
        assertThat(color, is(instanceOf(Color.class)));
        assertThat(color.getHex(), is(FAKE_COLOR_HEX));
        assertThat(color.getName(), is(FAKE_COLOR_NAME));
    }

    @Test public void givenExpectedExceptionTransformUserEntityCollectionNotValidResponse() {
        expectedException.expect(UnsupportedOperationException.class);
        colorToColorEntityMapper.map(new Color(FAKE_COLOR_HEX, FAKE_COLOR_NAME));
    }
}
