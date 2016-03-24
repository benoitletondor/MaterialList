package com.benoitletondor.materiallist.injection;

import com.benoitletondor.materiallist.LoginActivity;
import com.benoitletondor.materiallist.MainActivity;
import com.benoitletondor.materiallist.RecyclerViewAdapter;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Dagger component for items available app widely
 *
 * @author Benoit LETONDOR
 */
@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent
{
    void inject(LoginActivity loginActivity);
    void inject(MainActivity mainActivity);
    void inject(RecyclerViewAdapter adapter);
}
