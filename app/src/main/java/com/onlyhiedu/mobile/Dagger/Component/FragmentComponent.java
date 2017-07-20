package com.onlyhiedu.mobile.Dagger.Component;

import android.app.Activity;

import com.onlyhiedu.mobile.Dagger.FragmentScope;
import com.onlyhiedu.mobile.Dagger.Modul.FragmentModule;
import com.onlyhiedu.mobile.UI.Emc.ContactListFragment;
import com.onlyhiedu.mobile.UI.Emc.EaseConversationListFragment;
import com.onlyhiedu.mobile.UI.Home.fragment.CourseFragment;
import com.onlyhiedu.mobile.UI.Home.fragment.CourseRecordFragment;
import com.onlyhiedu.mobile.UI.Home.fragment.HomeFragment;
import com.onlyhiedu.mobile.UI.Home.fragment.MeFragment;

import dagger.Component;

@FragmentScope
@Component(dependencies = AppComponent.class, modules = FragmentModule.class)
public interface FragmentComponent {

    Activity getActivity();


    void inject(MeFragment fragment);

    void inject(CourseFragment fragment);

    void inject(CourseRecordFragment fragment);


    void inject(ContactListFragment fragment);

    void inject(EaseConversationListFragment fragment);

    void inject(HomeFragment fragment);
}