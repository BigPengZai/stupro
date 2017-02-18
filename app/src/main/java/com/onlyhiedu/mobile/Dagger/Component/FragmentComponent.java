package com.onlyhiedu.mobile.Dagger.Component;

import android.app.Activity;

import com.onlyhiedu.mobile.Dagger.FragmentScope;
import com.onlyhiedu.mobile.Dagger.Modul.FragmentModule;

import dagger.Component;

@FragmentScope
@Component(dependencies = AppComponent.class, modules = FragmentModule.class)
public interface FragmentComponent {

    Activity getActivity();


}