package com.onlyhiedu.pro.Dagger.Component;

import android.app.Activity;

import com.onlyhiedu.pro.Dagger.FragmentScope;
import com.onlyhiedu.pro.Dagger.Modul.FragmentModule;
import com.onlyhiedu.pro.UI.Course.fragment.CourseDiscountFragment;
import com.onlyhiedu.pro.UI.Home.fragment.CourseFragment;
import com.onlyhiedu.pro.UI.Home.fragment.SmallCourseFragment;
import com.onlyhiedu.pro.UI.Home.fragment.CourseRecordFragment;
import com.onlyhiedu.pro.UI.Home.fragment.SmallCourseRecordFragment;
import com.onlyhiedu.pro.UI.Home.fragment.HomeFragment;
import com.onlyhiedu.pro.UI.Home.fragment.MeFragment;
import com.onlyhiedu.pro.UI.Home.fragment.OrderFragment;

import dagger.Component;

@FragmentScope
@Component(dependencies = AppComponent.class, modules = FragmentModule.class)
public interface FragmentComponent {

    Activity getActivity();


    void inject(MeFragment fragment);

    void inject(CourseFragment fragment);

    void inject(CourseRecordFragment fragment);

    void inject(HomeFragment fragment);

    void inject(CourseDiscountFragment courseDiscountFragment);

    void inject(OrderFragment fragment);

    void inject(SmallCourseFragment fragment);

    void inject(SmallCourseRecordFragment fragment);
}