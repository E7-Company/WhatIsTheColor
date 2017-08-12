package com.e7.whatisthecolor.view.base.view;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.e7.whatisthecolor.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Enrique on 12/08/2017.
 */

public abstract class BaseActivity extends AppCompatActivity {

    // the toolbar for the app
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        bindViews();
        setupToolbar();
        initView();
    }

    /**
     * Use this method to initialize view components. This method is called after {@link
     * BaseActivity#bindViews()}
     */
    public void initView() {
    }

    /**
     * Its common use a toolbar within activity, if it exists in the
     * layout this will be configured
     */
    public void setupToolbar() {
        toolbar.setTitleTextColor(Color.BLACK);
        if(toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setIcon(R.drawable.ic_launcher);
        }
    }

    /**
     * Every object annotated with {@link butterknife.Bind} its gonna injected trough butterknife
     */
    private void bindViews() {
        ButterKnife.bind(this);
    }

    @Nullable
    public Toolbar getToolbar() {
        return toolbar;
    }

    /**
     * @return The layout id that's gonna be the activity view.
     */
    protected abstract int getLayoutId();
}