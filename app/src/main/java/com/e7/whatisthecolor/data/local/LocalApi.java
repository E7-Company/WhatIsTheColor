package com.e7.whatisthecolor.data.local;

import android.content.Context;
import android.support.annotation.NonNull;

import com.e7.whatisthecolor.data.entity.ColorEntity;
import com.e7.whatisthecolor.data.repository.datasource.mapper.ColorEntityJsonMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * Created by Enrique on 11/08/2017.
 */

public class LocalApi implements ILocalApi {

    private final Context context;
    private final ColorEntityJsonMapper colorEntityJsonMapper;

    public LocalApi(@NonNull Context context, @NonNull ColorEntityJsonMapper colorEntityJsonMapper) {
        this.context = context;
        this.colorEntityJsonMapper = colorEntityJsonMapper;
    }

    @Override
    public Observable<List<ColorEntity>> colorEntityList() {
        return Observable.create(new ObservableOnSubscribe<List<ColorEntity>>() {
            @Override public void subscribe(ObservableEmitter<List<ColorEntity>> emitter)
                    throws Exception {
                List<ColorEntity> colorEntityList = getAll();
                if (colorEntityList != null) {
                    emitter.onNext(colorEntityList);
                    emitter.onComplete();
                } else {
                    emitter.onError(
                            new Throwable("Error getting color data list from the local json (colors.json)"));
                }
            }
        });
    }

    /**
     * This method works to obtain a collection of data {@link ColorEntity}.
     */

    private List<ColorEntity> getAll() {
        return colorEntityJsonMapper.transformColorEntityCollection(getResponseFromLocalJson());
    }

    /**
     * This methods works to read a local JSON (colors.json) from assets.
     */

    private String getResponseFromLocalJson() {
        final String COLORS_DATA_FILE = "colors.json";
        String str = "";
        try {
            StringBuilder builder = new StringBuilder();
            InputStream json = context.getAssets().open(COLORS_DATA_FILE);
            BufferedReader in = new BufferedReader(new InputStreamReader(json));

            while ((str = in.readLine()) != null) {
                builder.append(str);
            }
            in.close();
            str = builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }
}
