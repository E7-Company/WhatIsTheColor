package com.e7.whatisthecolor.domain.usecase;

import com.e7.whatisthecolor.data.repository.ColorRepository;
import com.e7.whatisthecolor.domain.model.Color;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

/**
 * Created by Enrique on 11/08/2017.
 */

public class GetColors extends UseCase<List<Color>> {

    private final ColorRepository colorRepository;

    @Inject
    public GetColors(@Named("executor_thread") Scheduler executorThread,
                        @Named("ui_thread") Scheduler uiThread, ColorRepository colorRepository) {
        super(executorThread, uiThread);
        this.colorRepository = colorRepository;
    }

    @Override public Observable<List<Color>> createObservableUseCase() {
        return this.colorRepository.colorList();
    }

}
