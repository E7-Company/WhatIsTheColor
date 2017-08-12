package com.e7.whatisthecolor.view.presenter;

import com.e7.whatisthecolor.view.activity.IMainActivity;

/**
 * Created by Enrique on 12/08/2017.
 */

public class Presenter<T extends IMainActivity> {

    private T view;

    public T getView() {
        return view;
    }

    public void setView(T view) {
        this.view = view;
    }

    public void initialize() {

    }
}
