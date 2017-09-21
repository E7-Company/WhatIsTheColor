package com.e7.whatisthecolor.data.repository;

import android.support.test.runner.AndroidJUnit4;

import com.e7.whatisthecolor.data.entity.ColorEntity;
import com.e7.whatisthecolor.data.local.LocalApi;
import com.e7.whatisthecolor.data.repository.datasource.ColorLocalApiDataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * Created by Enrique on 20/09/2017.
 */

@RunWith(AndroidJUnit4.class)
public class ColorLocalApiDataSourceTest {

    @Mock
    LocalApi localApi;

    private ColorLocalApiDataSource localApiDataSource;

    @Before
    public void setUp() {
        localApiDataSource = new ColorLocalApiDataSource(localApi);
    }

    @Test
    public void givenAColorEntityListFromLocalApi() {
        localApiDataSource.colorEntityList();
        verify(localApi).colorEntityList();
    }

    @Test public void givenAnObservableCollectionColorEntity() {
        List<ColorEntity> colorEntities = new ArrayList<>();
        Observable<List<ColorEntity>> fakeListObservable = Observable.just(colorEntities);
        given(localApi.colorEntityList()).willReturn(fakeListObservable);
    }
}
