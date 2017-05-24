package com.onlyhiedu.mobile.App;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.onlyhiedu.mobile.Dagger.Component.AppComponent;
import com.onlyhiedu.mobile.Dagger.Component.DaggerAppComponent;
import com.onlyhiedu.mobile.Dagger.Modul.AppModule;
import com.onlyhiedu.mobile.Utils.DaoUtil;
import com.squareup.leakcanary.LeakCanary;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.common.QueuedWork;

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
    private PushAgent mPushAgent;

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

//        LeakCanary.install(this);

        initGlide();
        DaoUtil.getInstance(this);
        initWorkerThread();
        MobclickAgent.openActivityDurationTrack(false);
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);

        //开启debug模式，方便定位错误，具体错误检查方式可以查看http://dev.umeng.com/social/android/quick-integration的报错必看，正式发布，请关闭该模式
        Config.DEBUG = false;
        QueuedWork.isUseThreadPool = false;
        UMShareAPI.get(this);
        mPushAgent = PushAgent.getInstance(this);
        mPushAgent.setDebugMode(false);
        Config.isJumptoAppStore = true;
        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回device token
                Log.d("App", "pushToken:" + deviceToken);
            }

            @Override
            public void onFailure(String s, String s1) {
                Log.d("App", "onFailure:" + s+s1);
            }
        });
    }

    private void initGlide() {
        Glide.get(this).register(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(new OkHttpClient()));
    }


    public static AppComponent getAppComponent() {
        return DaggerAppComponent.builder()
                .appModule(new AppModule(instance))
                .build();
    }

    public static final CurrentUserSettings mVideoSettings = new CurrentUserSettings();

    {
        //配置 微信 以及 QQ app_id
        PlatformConfig.setWeixin("wxfeb18b738b0c2f1c", "wxfeb18b738b0c2f1c");
        PlatformConfig.setQQZone("1105946445", "1105946445");
    }

}
