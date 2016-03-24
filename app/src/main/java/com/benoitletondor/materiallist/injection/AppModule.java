package com.benoitletondor.materiallist.injection;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.benoitletondor.materiallist.App;
import com.benoitletondor.materiallist.stub.Webservices;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Module for {@link AppComponent} that provides items available app widely
 *
 * @author Benoit LETONDOR
 */
@Module
public class AppModule
{
    @NonNull
    private App app;

    public AppModule(@NonNull App app)
    {
        this.app = app;
    }

    @Provides
    public Context provideApplicationContext()
    {
        return app;
    }

    @Provides
    @Singleton
    public Webservices provideWebservices()
    {
        return new Webservices();
    }

    @Provides
    @Singleton
    public SharedPreferences provideSharedPreferences()
    {
        return app.getSharedPreferences("app_values", Context.MODE_PRIVATE);
    }
}
