package com.e7.whatisthecolor.data.repository;

import android.support.test.InstrumentationRegistry;

import com.e7.whatisthecolor.data.repository.datasource.ColorDataSourceFactory;
import com.e7.whatisthecolor.data.repository.datasource.ColorLocalApiDataSource;
import com.e7.whatisthecolor.data.repository.datasource.IDataSource;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Enrique on 20/09/2017.
 */

public class ColorDataSourceFactoryTest {
    private ColorDataSourceFactory colorDataSourceFactory;

    @Before
    public void setUp() {
        colorDataSourceFactory = new ColorDataSourceFactory(InstrumentationRegistry.getContext());
    }

    @Test
    public void givenAnInstanceColorsLocalApiDataSource() {
        IDataSource dataSource = colorDataSourceFactory.createDataSource();
        assertThat(dataSource, is(notNullValue()));
        assertThat(dataSource, is(instanceOf(ColorLocalApiDataSource.class)));
    }
}
