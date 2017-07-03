package com.onlyhiedu.mobile.App;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
//import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.EaseUI;
import com.onlyhiedu.mobile.Dagger.Component.AppComponent;
import com.onlyhiedu.mobile.Dagger.Component.DaggerAppComponent;
import com.onlyhiedu.mobile.Dagger.Modul.AppModule;
import com.onlyhiedu.mobile.UI.Home.activity.MainActivity;
import com.onlyhiedu.mobile.Utils.DaoUtil;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.common.QueuedWork;

import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

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

    public boolean isTag;
    public static boolean bIsGuestLogin = true;//是否游客登录
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
    // 记录是否已经初始化
    private boolean isInit = false;
    // 上下文菜单
    private Context mContext;
    @Override
    public void onCreate() {
//        MultiDex.install(this);
        super.onCreate();
        instance = this;
        mContext = this;

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

        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
            @Override
            public void dealWithCustomAction(Context context, UMessage msg) {
                isTag = true;
                Intent intent = new Intent(App.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
//                Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();
            }
        };
        mPushAgent.setNotificationClickHandler(notificationClickHandler);
        // 初始化环信SDK
        EaseUI.getInstance().init(this, null);

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
        PlatformConfig.setWeixin("wxfeb18b738b0c2f1c", "bed2a2109e97ec3b280eaff88dd0a03f");
        PlatformConfig.setQQZone("1105946445", "yp52LFZMgwMx35h2");
        PlatformConfig.setSinaWeibo("3139333765", "65fe958da4bcd72a1c701c98fec4f91e","http://www.onlyhi.cn/");
    }

}
