package com.onlyhiedu.mobile.App;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;

import com.baidu.wallet.api.BaiduWallet;
import com.bumptech.glide.Glide;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.onlyhiedu.mobile.Dagger.Component.AppComponent;
import com.onlyhiedu.mobile.Dagger.Component.DaggerAppComponent;
import com.onlyhiedu.mobile.Dagger.Modul.AppModule;
import com.onlyhiedu.mobile.UI.Home.activity.MainActivity;
import com.onlyhiedu.mobile.Utils.DaoUtil;
import com.pingplusplus.android.Pingpp;
import com.tencent.bugly.crashreport.CrashReport;
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

import cn.robotpen.model.db.DBConfig;
import cn.robotpen.model.db.DaoMaster;
import cn.robotpen.model.db.DaoSession;
import io.agore.openvcall.model.CurrentUserSettings;
import io.agore.openvcall.model.WorkerThread;
import okhttp3.OkHttpClient;

//import com.tencent.bugly.beta.Beta;

//import com.tencent.bugly.beta.Beta;

/**
 * 全局应用程序类
 * Created by xuwc on 2016/11/21.
 */
public class App extends Application {

    private static App instance;
    private PushAgent mPushAgent;
//    public AgoraAPIOnlySignal mAgoraSocket;

    public static synchronized App getInstance() {
        return instance;
    }

    private DaoSession daoSession;

    public boolean isTag;

    //    public static boolean bIsGuestLogin = SPUtil.getGuest();//是否游客登录
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

    public static String currentUserNick = "";

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
//        Beta.installTinker();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

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
                Log.d("App", "onFailure:" + s + s1);
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
//        DemoHelper.getInstance().init(this);
        //bugly

        CrashReport.initCrashReport(getApplicationContext(), "8f4d5d918c", true);
        //ping++
        Pingpp.enableDebugLog(true);
        //百度钱包
        BaiduWallet.getInstance().initWallet(this, "outanglihai");
        //bugly 热跟新 调试时，将第三个参数改为true
//        Bugly.init(this,"8f4d5d918c",true);

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
        PlatformConfig.setSinaWeibo("3139333765", "65fe958da4bcd72a1c701c98fec4f91e", "http://www.onlyhi.cn/");
    }

    /*
   * 统一创建session
   * @return
   */
    public DaoSession getDaoSession() {
        if (null == daoSession) {
            SQLiteDatabase db = new DaoMaster.DevOpenHelper(instance, DBConfig.DB_NAME).getWritableDatabase();
            this.daoSession = new DaoMaster(db).newSession();
        }
        return daoSession;
    }
}
