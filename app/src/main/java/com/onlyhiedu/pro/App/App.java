package com.onlyhiedu.pro.App;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatDelegate;
import android.text.TextUtils;
import android.util.Log;

import com.baidu.wallet.api.BaiduWallet;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.api.UIKitOptions;
import com.netease.nim.uikit.business.contact.core.query.PinYin;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.mixpush.NIMPushClient;
import com.netease.nimlib.sdk.util.NIMUtil;
import com.onlyhiedu.pro.Dagger.Component.AppComponent;
import com.onlyhiedu.pro.Dagger.Component.DaggerAppComponent;
import com.onlyhiedu.pro.Dagger.Modul.AppModule;
import com.onlyhiedu.pro.IM.DemoCache;
import com.onlyhiedu.pro.IM.NIMInitManager;
import com.onlyhiedu.pro.IM.NimSDKOptionConfig;
import com.onlyhiedu.pro.IM.chatroom.ChatRoomSessionHelper;
import com.onlyhiedu.pro.IM.common.util.crash.AppCrashHandler;
import com.onlyhiedu.pro.IM.config.preference.Preferences;
import com.onlyhiedu.pro.IM.config.preference.UserPreferences;
import com.onlyhiedu.pro.IM.contact.ContactHelper;
import com.onlyhiedu.pro.IM.event.DemoOnlineStateContentProvider;
import com.onlyhiedu.pro.IM.session.NimDemoLocationProvider;
import com.onlyhiedu.pro.IM.session.SessionHelper;
import com.onlyhiedu.pro.UI.Home.activity.MainActivity;
import com.onlyhiedu.pro.Utils.DaoUtil;
import com.pingplusplus.android.Pingpp;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.common.QueuedWork;

import cn.robotpen.model.db.DBConfig;
import cn.robotpen.model.db.DaoMaster;
import cn.robotpen.model.db.DaoSession;
import io.agore.openvcall.model.CurrentUserSettings;
import io.agore.openvcall.model.WorkerThread;

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
        Beta.installTinker();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

//        initGlide();
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
        //bugly

//        CrashReport.initCrashReport(getApplicationContext(), "8f4d5d918c", true);
        //ping++
        Pingpp.enableDebugLog(true);
        //百度钱包
        BaiduWallet.getInstance().initWallet(this, "outanglihai");
        //bugly 热跟新 调试时，将第三个参数改为true
        Bugly.init(this,"8f4d5d918c",false);

        //IM
        DemoCache.setContext(this);
        // 注册小米推送，参数：小米推送证书名称（需要在云信管理后台配置）、appID 、appKey，该逻辑放在 NIMClient init 之前
        NIMPushClient.registerMiPush(this, "DEMO_MI_PUSH", "2882303761517502883", "5671750254883");
        // 注册华为推送，参数：华为推送证书名称（需要在云信管理后台配置）
        NIMPushClient.registerHWPush(this, "DEMO_HW_PUSH");

        // 注册自定义推送消息处理，这个是可选项
//        NIMPushClient.registerMixPushMessageHandler(new DemoMixPushMessageHandler());
        //自动登录
        NIMClient.init(this, getLoginInfo(), NimSDKOptionConfig.getSDKOptions(this));

        // crash handler
        AppCrashHandler.getInstance(this);

        // 以下逻辑只在主进程初始化时执行
        if (NIMUtil.isMainProcess(this)) {
            // 初始化红包模块，在初始化UIKit模块之前执行
//            NIMRedPacketClient.init(this);
            // init pinyin
            PinYin.init(this);
            PinYin.validate();
            // 初始化UIKit模块
            initUIKit();
            // 初始化消息提醒
            NIMClient.toggleNotification(UserPreferences.getNotificationToggle());
            // 云信sdk相关业务初始化
            NIMInitManager.getInstance().init(true);
        }

    }

    private LoginInfo getLoginInfo() {
        String account = Preferences.getUserAccount();
        String token = Preferences.getUserToken();

        if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(token)) {
            DemoCache.setAccount(account.toLowerCase());
            return new LoginInfo(account, token);
        } else {
            return null;
        }
    }

    private void initUIKit() {
        // 初始化
        NimUIKit.init(this, buildUIKitOptions());

        // 设置地理位置提供者。如果需要发送地理位置消息，该参数必须提供。如果不需要，可以忽略。
        NimUIKit.setLocationProvider(new NimDemoLocationProvider());

        // IM 会话窗口的定制初始化。
        SessionHelper.init();

        // 聊天室聊天窗口的定制初始化。
        ChatRoomSessionHelper.init();

        // 通讯录列表定制初始化
        ContactHelper.init();

        // 添加自定义推送文案以及选项，请开发者在各端（Android、IOS、PC、Web）消息发送时保持一致，以免出现通知不一致的情况
        //NimUIKit.setCustomPushContentProvider(new DemoPushContentProvider());

        NimUIKit.setOnlineStateContentProvider(new DemoOnlineStateContentProvider());
    }

    private UIKitOptions buildUIKitOptions() {
        UIKitOptions options = new UIKitOptions();
        // 设置app图片/音频/日志等缓存目录
        options.appCacheDir = NimSDKOptionConfig.getAppCacheDir(this) + "/app";

        return options;
    }


   /* private void initGlide() {
        Glide.get(this).register(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(new OkHttpClient()));

    }*/


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
