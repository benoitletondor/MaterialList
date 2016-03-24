package com.benoitletondor.materiallist;

import android.app.Application;

import com.benoitletondor.materiallist.injection.AppComponent;
import com.benoitletondor.materiallist.injection.AppModule;
import com.benoitletondor.materiallist.injection.DaggerAppComponent;

/**
 * Base class of the Application
 *
 * @author Benoit LETONDOR
 */
public class App extends Application
{
    /**
     * Dagger component
     */
    private AppComponent component;

    @Override
    public void onCreate()
    {
        super.onCreate();

        component = DaggerAppComponent.builder()
            .appModule(new AppModule(this))
            .build();
    }

    /**
     * Get the dagger {@link AppComponent}
     *
     * @return the dagger app component
     */
    public AppComponent getComponent()
    {
        return component;
    }
}