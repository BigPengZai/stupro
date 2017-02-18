package com.onlyhiedu.mobile.Dagger.Modul;

import android.app.Activity;

import com.onlyhiedu.mobile.Dagger.ActivityScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by xuwc on 2016/11/24.
 */
@Module
public class ActivityModule {
    private Activity mActivity;

    public ActivityModule(Activity activity) {
        this.mActivity = activity;
    }

    @Provides
    @ActivityScope
    public Activity provideActivity() {
        return mActivity;
    }
}
