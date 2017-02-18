package com.onlyhiedu.mobile.Dagger.Component;


import com.onlyhiedu.mobile.App.App;
import com.onlyhiedu.mobile.Dagger.ContextLife;
import com.onlyhiedu.mobile.Dagger.Modul.AppModule;
import com.onlyhiedu.mobile.Model.http.RetrofitHelper;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by xuwc on 2016/11/24.
 */

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {

    @ContextLife("Application")
    App getContext();  // 提供App的Context

    RetrofitHelper retrofitHelper();  //提供http的帮助类


}
