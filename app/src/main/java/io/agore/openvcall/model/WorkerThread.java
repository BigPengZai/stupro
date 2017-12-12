package io.agore.openvcall.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceView;

import com.onlyhiedu.mobile.R;
import com.onlyhiedu.mobile.Utils.DynamicKey4;

import java.io.File;
import java.util.Date;
import java.util.Random;

import io.agora.rtc.Constants;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.RtcEngineEx;
import io.agora.rtc.video.VideoCanvas;

public class WorkerThread extends Thread {
//    private final static Logger log = LoggerFactory.getLogger(WorkerThread.class);

    private final Context mContext;

    private static final int ACTION_WORKER_THREAD_QUIT = 0X1010; // quit this thread

    private static final int ACTION_WORKER_JOIN_CHANNEL = 0X2010;

    private static final int ACTION_WORKER_LEAVE_CHANNEL = 0X2011;

    private static final int ACTION_WORKER_CONFIG_ENGINE = 0X2012;

    private static final int ACTION_WORKER_PREVIEW = 0X2014;

    public static final String TAG = "WorkerThread";
    private String mChannelAppId;

    private static final class WorkerThreadHandler extends Handler {

        private WorkerThread mWorkerThread;

        WorkerThreadHandler(WorkerThread thread) {
            this.mWorkerThread = thread;
        }

        public void release() {
            mWorkerThread = null;
        }

        @Override
        public void handleMessage(Message msg) {
            if (this.mWorkerThread == null) {
//                log.warn("handler is already released! " + msg.what);
                return;
            }

            switch (msg.what) {
                case ACTION_WORKER_THREAD_QUIT:
                    mWorkerThread.exit();
                    break;
                case ACTION_WORKER_JOIN_CHANNEL:
                    String[] data = (String[]) msg.obj;
                    mWorkerThread.joinChannel(data[0], msg.arg1);
                    break;
                case ACTION_WORKER_LEAVE_CHANNEL:
                    String channel = (String) msg.obj;
                    mWorkerThread.leaveChannel(channel);
                    break;
                case ACTION_WORKER_CONFIG_ENGINE:
                    Object[] configData = (Object[]) msg.obj;
                    mWorkerThread.configEngine((int) configData[0],(int) configData[1]);
                    break;
                case ACTION_WORKER_PREVIEW:
                    Object[] previewData = (Object[]) msg.obj;
                    mWorkerThread.preview((boolean) previewData[0], (SurfaceView) previewData[1], (int) previewData[2]);
                    break;
            }
        }
    }

    private WorkerThreadHandler mWorkerHandler;

    private boolean mReady;

    public final void waitForReady() {
        while (!mReady) {
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            log.debug("wait for " + WorkerThread.class.getSimpleName());
        }
    }

    @Override
    public void run() {
//        log.trace("start to run");
        Looper.prepare();

        mWorkerHandler = new WorkerThreadHandler(this);

        ensureRtcEngineReadyLock();

        mReady = true;

        // enter thread looper
        Looper.loop();
    }

    private RtcEngine mRtcEngine;

    public final void enablePreProcessor() {
    }
    public final void disablePreProcessor() {
    }

    public final void joinChannel(final String channel, int uid) {
        if (Thread.currentThread() != this) {
//            log.warn("joinChannel() - worker thread asynchronously " + channel + " " + uid);
            Message envelop = new Message();
            envelop.what = ACTION_WORKER_JOIN_CHANNEL;
            envelop.obj = new String[]{channel};
            envelop.arg1 = uid;
            mWorkerHandler.sendMessage(envelop);
            return;
        }

        ensureRtcEngineReadyLock();


        mChannelAppId = mContext.getString(R.string.private_app_id);
        int tsWrong = (int)(new Date().getTime()/1000);
        int ts = (int) (System.currentTimeMillis()/1000);

        int r = new Random().nextInt();
//        long uid = my_uid;
        int expiredTs = 0;

        Log.d(TAG, "appId:"+ mChannelAppId);
        try {
            mChannelAppId = DynamicKey4.generateMediaChannelKey(mChannelAppId, "a8e0dbb830d44c9f8e7e5a4e32c62c53", channel, ts, r, uid, expiredTs);
        } catch (Exception e) {
            e.printStackTrace();
        }


        int openVCall = mRtcEngine.joinChannel(mChannelAppId, channel, "OpenVCall", uid);
        Log.d(TAG, "openVCall:"+openVCall);
        Log.d(TAG, "appId:"+ mChannelAppId);
        mEngineConfig.mChannel = channel;

        enablePreProcessor();
//        log.debug("joinChannel " + channel + " " + uid);
    }

    public final void leaveChannel(String channel) {
        if (Thread.currentThread() != this) {
//            log.warn("leaveChannel() - worker thread asynchronously " + channel);
            Message envelop = new Message();
            envelop.what = ACTION_WORKER_LEAVE_CHANNEL;
            envelop.obj = channel;
            mWorkerHandler.sendMessage(envelop);
            return;
        }

        if (mRtcEngine != null) {
            mRtcEngine.leaveChannel();
        }

        disablePreProcessor();

        mEngineConfig.reset();
//        log.debug("leaveChannel " + channel);
    }

    private EngineConfig mEngineConfig;

    public final EngineConfig getEngineConfig() {
        return mEngineConfig;
    }

    private final MyEngineEventHandler mEngineEventHandler;

    public final void configEngine(int cRole,int vProfile) {
        if (Thread.currentThread() != this) {
            Log.d(TAG,"configEngine() - worker thread asynchronously " + vProfile );
            Message envelop = new Message();
            envelop.what = ACTION_WORKER_CONFIG_ENGINE;
            envelop.obj = new Object[]{cRole,vProfile};
            mWorkerHandler.sendMessage(envelop);
            return;
        }

        ensureRtcEngineReadyLock();
        mEngineConfig.mVideoProfile = vProfile;
//240P_4	24	424x240	15	220
        mRtcEngine.setVideoProfile(24, false);
        mRtcEngine.setClientRole(cRole, mChannelAppId);
    }

    public final void preview(boolean start, SurfaceView view, int uid) {
        if (Thread.currentThread() != this) {
//            log.warn("preview() - worker thread asynchronously " + start + " " + view + " " + (uid & 0XFFFFFFFFL));
            Message envelop = new Message();
            envelop.what = ACTION_WORKER_PREVIEW;
            envelop.obj = new Object[]{start, view, uid};
            mWorkerHandler.sendMessage(envelop);
            return;
        }

        ensureRtcEngineReadyLock();
        if (start) {
            mRtcEngine.setupLocalVideo(new VideoCanvas(view, VideoCanvas.RENDER_MODE_HIDDEN, uid));
            mRtcEngine.startPreview();
        } else {
            mRtcEngine.stopPreview();
        }
    }

    public static String getDeviceID(Context context) {
        // XXX according to the API docs, this value may change after factory reset
        // use Android id as device id
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    private RtcEngine ensureRtcEngineReadyLock() {
        if (mRtcEngine == null) {
            String appId = mContext.getString(R.string.private_app_id);
            if (TextUtils.isEmpty(appId)) {
                throw new RuntimeException("NEED TO use your App ID, get your own ID at https://dashboard.agora.io/");
            }
            try {
                mRtcEngine = RtcEngineEx.create(mContext, appId, mEngineEventHandler.mRtcEventHandler);
            } catch (Exception e) {
            }
            //CHANNEL_PROFILE_COMMUNICATION 通信模式
            //CHANNEL_PROFILE_LIVE_BROADCASTING 直播模式
            mRtcEngine.setChannelProfile(Constants.CHANNEL_PROFILE_LIVE_BROADCASTING);
            //
            mRtcEngine.enableVideo();
            mRtcEngine.setDefaultAudioRoutetoSpeakerphone(true);
            mRtcEngine.setEnableSpeakerphone(true);
            mRtcEngine.setSpeakerphoneVolume(90);
            mRtcEngine.setLogFile(Environment.getExternalStorageDirectory()
                    + File.separator + mContext.getPackageName() + "/log/agora-rtc.log");
        }
        return mRtcEngine;
    }

    public MyEngineEventHandler eventHandler() {
        return mEngineEventHandler;
    }

    public RtcEngine getRtcEngine() {
        return mRtcEngine;
    }

    /**
     * call this method to exit
     * should ONLY call this method when this thread is running
     */
    public final void exit() {
        if (Thread.currentThread() != this) {
//            log.warn("exit() - exit app thread asynchronously");
            mWorkerHandler.sendEmptyMessage(ACTION_WORKER_THREAD_QUIT);
            return;
        }

        mReady = false;

        // TODO should remove all pending(read) messages

//        log.debug("exit() > start");

        // exit thread looper
        Looper.myLooper().quit();

        mWorkerHandler.release();

//        log.debug("exit() > end");
    }

    public WorkerThread(Context context) {
        this.mContext = context;

        this.mEngineConfig = new EngineConfig();
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        this.mEngineConfig.mUid = pref.getInt(ConstantApp.PrefManager.PREF_PROPERTY_UID, 0);

        this.mEngineEventHandler = new MyEngineEventHandler(mContext, this.mEngineConfig);
    }
}
