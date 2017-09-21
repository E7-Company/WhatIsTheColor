package com.e7.whatisthecolor.domain;

import android.support.test.runner.AndroidJUnit4;

import com.e7.whatisthecolor.data.repository.ColorRepository;
import com.e7.whatisthecolor.domain.usecase.GetColors;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import io.reactivex.schedulers.Schedulers;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * Created by Enrique on 20/09/2017.
 */

@RunWith(AndroidJUnit4.class)
public class GetColorsTest {
    @Mock
    ColorRepository repository;
    private GetColors getColors;

    @Before
    public void setUp() {
        getColors = givenAColorListUseCase();
    }

    @Test
    public void givenAConcreteUseCaseOfGetColor() {
        assertThat(getColors, instanceOf(GetColors.class));
    }

    @Test public void getColorListObservableFromRepository() {
        getColors.createObservableUseCase();
        verify(repository).colorList();
        verifyNoMoreInteractions(repository);
    }

    private GetColors givenAColorListUseCase() {
        return new GetColors(Schedulers.trampoline(), Schedulers.trampoline(), repository);
    }
}
