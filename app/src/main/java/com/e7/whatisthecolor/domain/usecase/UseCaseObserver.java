package com.e7.whatisthecolor.domain.usecase;

import io.reactivex.observers.DisposableObserver;

/**
 * Created by Enrique on 11/08/2017.
 */

public abstract class UseCaseObserver<T> extends DisposableObserver<T> {

    @Override
    public void onComplete() {
    }

    @Override
    public void onError(Throwable e) {
    }

    @Override
    public void onNext(T t) {
    }
}