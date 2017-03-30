package com.onlyhiedu.mobile.App;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;

import com.bumptech.glide.Glide;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.onlyhiedu.mobile.Dagger.Component.AppComponent;
import com.onlyhiedu.mobile.Dagger.Component.DaggerAppComponent;
import com.onlyhiedu.mobile.Dagger.Modul.AppModule;
import com.onlyhiedu.mobile.Utils.DaoUtil;
import com.squareup.leakcanary.LeakCanary;
import com.umeng.analytics.MobclickAgent;

import java.io.InputStream;

import io.agore.openvcall.model.CurrentUserSettings;
import io.agore.openvcall.model.WorkerThread;
import okhttp3.OkHttpClient;

/**
 * 全局应用程序类
 * Created by xuwc on 2016/11/21.
 */
public class App extends Application {

    private static App instance;

    public static synchronized App getInstance() {
        return instance;
    }

    static {
        AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_NO);
    }


    private WorkerThread mWorkerThread;

    public synchronized void initWorkerThread() {
        if (mWorkerThread == null) {
            mWorkerThread = new WorkerThread(getApplicationContext());
            mWorkerThread.start();

            mWorkerThread.waitForReady();
        }
    }

    public synchronized WorkerThread getWorkerThread() {
        return mWorkerThread;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        LeakCanary.install(this);

        initGlide();

        DaoUtil.getInstance(this);
       initWorkerThread();
        //初始化友盟
//        MobclickAgent.startWithConfigure(
//                new MobclickAgent.UMAnalyticsConfig(
//                        this, "58da12f44ad1564af8001e09", "Umeng",
//                MobclickAgent.EScenarioType.E_UM_NORMAL));

        MobclickAgent.setDebugMode(true);
        // SDK在统计Fragment时，需要关闭Activity自带的页面统计，
        // 然后在每个页面中重新集成页面统计的代码(包括调用了 onResume 和 onPause 的Activity)。
        MobclickAgent.openActivityDurationTrack(false);
        // MobclickAgent.setAutoLocation(true);
        // MobclickAgent.setSessionContinueMillis(1000);
        // MobclickAgent.startWithConfigure(
        // new UMAnalyticsConfig(mContext, "4f83c5d852701564c0000011", "Umeng",
        // EScenarioType.E_UM_NORMAL));
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
    }
    private void initGlide(){
        Glide.get(this).register(GlideUrl.class,InputStream.class,new OkHttpUrlLoader.Factory(new OkHttpClient()));
    }


    public static AppComponent getAppComponent(){
        return DaggerAppComponent.builder()
                .appModule(new AppModule(instance))
                .build();
    }
    public static final CurrentUserSettings mVideoSettings = new CurrentUserSettings();

}
