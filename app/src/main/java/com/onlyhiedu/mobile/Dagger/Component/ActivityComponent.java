package com.onlyhiedu.mobile.Dagger.Component;

import android.app.Activity;

import com.onlyhiedu.mobile.Dagger.ActivityScope;
import com.onlyhiedu.mobile.Dagger.Modul.ActivityModule;

import dagger.Component;

/**
 * Created by xuwc on 2016/11/24.
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    Activity getActivity();

}
