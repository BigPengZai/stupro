package com.onlyhiedu.mobile.Dagger.Modul;


import com.onlyhiedu.mobile.App.App;
import com.onlyhiedu.mobile.Dagger.ContextLife;
import com.onlyhiedu.mobile.Model.http.RetrofitHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by xuwc on 2016/11/24.
 */
@Module
public class AppModule {
    private final App application;

    public AppModule(App application) {
        this.application = application;
    }

    @Provides
    @Singleton
    @ContextLife("Application")
    App provideApplicationContext() {
        return application;
    }

    @Provides
    @Singleton
    RetrofitHelper provideRetrofitHelper() {
        return new RetrofitHelper();
    }

}
