package io.agore.openvcall.ui;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothProfile;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.onlyhiedu.mobile.App.Constants;
import com.onlyhiedu.mobile.Model.bean.CourseWareImageList;
import com.onlyhiedu.mobile.Model.bean.RoomInfo;
import com.onlyhiedu.mobile.R;
import com.onlyhiedu.mobile.Utils.DialogListener;
import com.onlyhiedu.mobile.Utils.DialogUtil;
import com.onlyhiedu.mobile.Utils.ImageLoader;
import com.onlyhiedu.mobile.Utils.StringUtils;
import com.onlyhiedu.mobile.Widget.draw.DrawView;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.List;
import io.agora.AgoraAPI;
import io.agora.AgoraAPIOnlySignal;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;
import io.agore.openvcall.model.AGEventHandler;
import io.agore.openvcall.model.ConstantApp;
import io.agore.propeller.Constant;
import io.agore.propeller.UserStatusData;
import io.agore.propeller.VideoInfoData;
import io.agore.propeller.headset.HeadsetBroadcastReceiver;
import io.agore.propeller.headset.HeadsetPlugManager;
import io.agore.propeller.headset.IHeadsetPlugListener;
import io.agore.propeller.headset.bluetooth.BluetoothHeadsetBroadcastReceiver;
import io.agore.propeller.preprocessing.VideoPreProcessing;
import static com.onlyhiedu.mobile.Utils.Encrypt.md5hex;

public class ChatActivity extends BaseActivity<ChatPresenter> implements AGEventHandler, IHeadsetPlugListener, View.OnClickListener, ChatContract.View {

    private TeacherVideoView mGridVideoViewContainer;
    private RelativeLayout mSmallVideoViewDock;
    private final HashMap<Integer, SoftReference<SurfaceView>> mUidsList = new HashMap<>(); // uid = 0 || uid == EngineConfig.mUid

    private volatile boolean mVideoMuted = false;

    private volatile boolean mAudioMuted = false;

    private volatile boolean mEarpiece = false;

    private volatile boolean mWithHeadset = false;

    public static final String TAG = ChatActivity.class.getSimpleName();
    private HeadsetBroadcastReceiver mHeadsetListener;
    private BluetoothAdapter mBtAdapter;
    private BluetoothProfile mBluetoothProfile;
    private BluetoothHeadsetBroadcastReceiver mBluetoothHeadsetBroadcastListener;
    private BluetoothProfile.ServiceListener mBluetoothHeadsetListener = new BluetoothProfile.ServiceListener() {

        @Override
        public void onServiceConnected(int profile, BluetoothProfile headset) {
            if (profile == BluetoothProfile.HEADSET) {
                mBluetoothProfile = headset;
                List<BluetoothDevice> devices = headset.getConnectedDevices();
                headsetPlugged(devices != null && devices.size() > 0);
            }
        }

        @Override
        public void onServiceDisconnected(int profile) {
            mBluetoothProfile = null;
        }
    };
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
//    private GoogleApiClient client;
    private String mUid;
    private RelativeLayout mRl_bg;
    private DrawView mDrawView;
    private ImageView mImageCourseWare;

    private AgoraAPIOnlySignal m_agoraAPI;
    private Button mMBut_dimiss;
    private int msgid = 0;
    private String mChannelName;
    private RoomInfo mRoomInfo;
    private Chronometer mMChronometer;

    private void headsetPlugged(final boolean plugged) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (isFinishing()) {
                    return;
                }

                RtcEngine rtcEngine = rtcEngine();
                rtcEngine.setEnableSpeakerphone(!plugged);
            }
        }).start();
    }


    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_chat;
    }


    @Override
    protected void initUIandEvent() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        //测试 狗屎代码
        mRl_bg = (RelativeLayout) findViewById(R.id.rl_bg);
        mMBut_dimiss = (Button) findViewById(R.id.but_dimiss);
        mDrawView = (DrawView) findViewById(R.id.draw_view);
        mImageCourseWare = (ImageView) findViewById(R.id.image_course_ware);

        mMChronometer = (Chronometer) findViewById(R.id.chronometer);
        mMChronometer.setBase(SystemClock.elapsedRealtime());//计时器清零
        int hour = (int) ((SystemClock.elapsedRealtime() - mMChronometer.getBase()) / 1000 / 60);
        mMChronometer.setFormat("0" + String.valueOf(hour) + ":%s");

        mMBut_dimiss.setOnClickListener(this);
        //获取频道 id  老师uid 学生uid
        mRoomInfo = (RoomInfo) getIntent().getSerializableExtra("roomInfo");
        if (mRoomInfo != null) {
            Log.d(TAG, "item:" + mRoomInfo.getSignallingChannelId());
            //课程频道
            mChannelName = mRoomInfo.getCommChannelId();
            //学生uid
            mUid = mRoomInfo.getChannelStudentId() + "";
        } else {
            return;
        }
        if (!StringUtils.isNumeric(mRoomInfo.getChannelStudentId() + "")) {
            Log.d(TAG, "uid不是数字组成");
            DialogUtil.showOnlyAlert(this,
                    "提示"
                    , "课程准备异常，请联系客服"
                    , "知道了"
                    , ""
                    , false, true, new DialogListener() {
                        @Override
                        public void onPositive(DialogInterface dialog) {
                            //退出教室
                            finish();
                        }

                        @Override
                        public void onNegative(DialogInterface dialog) {

                        }
                    }
            );
            return;
        }
        //登录信令系统成功后  登录通信频道
        initSignalling();
        event().addEventHandler(this);
//        doConfigEngine(encryptionKey, encryptionMode);
        //RecyclerView
        SurfaceView surfaceV = RtcEngine.CreateRendererView(getApplicationContext());
        surfaceV.setZOrderOnTop(false);
        surfaceV.setZOrderMediaOverlay(false);
        mUidsList.put(0, new SoftReference<>(surfaceV)); // get first surface view
        mGridVideoViewContainer = (TeacherVideoView) findViewById(R.id.grid_video_view_container);
        mGridVideoViewContainer.initViewContainer(getApplicationContext(), Integer.parseInt(mUid), mUidsList); // first is now full view
        worker().preview(true, surfaceV, Integer.parseInt(mUid));
    }

    private void initRoom() {
        worker().joinChannel(mChannelName, Integer.parseInt(mUid));
//        TextView textChannelName = (TextView) findViewById(R.id.channel_name);
//        textChannelName.setText(mChannelName);

        mPresenter.getCourseWareImageList("f35b6c57c9484a5dab9795a1fd42bea8");

        optional();
//       /* LinearLayout bottomContainer = (LinearLayout) findViewById(R.id.bottom_container);
//        FrameLayout.MarginLayoutParams fmp = (FrameLayout.MarginLayoutParams) bottomContainer.getLayoutParams();
//        fmp.bottomMargin = virtualKeyHeight() + 16;
//        initMessageList();*/
    }

    private void initSignalling() {
        //从服务器获取
        String certificate = this.getString(R.string.private_app_cate);
        String appId = this.getString(R.string.private_app_id);
        //假数据
        String account = mRoomInfo.getChannelStudentId()+"";
        m_agoraAPI = AgoraAPIOnlySignal.getInstance(this, appId);
        long expiredTime = System.currentTimeMillis() / 1000 + 3600;
        String token = calcToken(appId, certificate, account, expiredTime);
        m_agoraAPI.login2(appId, account, token, 0, "", 60, 5);
        m_agoraAPI.callbackSet(new AgoraAPI.CallBack() {
            @Override
            public void onLoginSuccess(int uid, int fd) {
                Log.d(TAG, "Login successfully");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        joinSignalling();
                    }
                });

            }

            @Override
            public void onLoginFailed(int ecode) {
                Log.d(TAG, "Login failed" + ecode);
                //进行异常处理的逻辑

            }

            //对方将收到 onMessageInstantReceive 回调。
            @Override
            public void onMessageInstantReceive(String account, int uid, String msg) {
                Log.d(TAG, "点对点消息：" + account + " : " + (long) (uid & 0xffffffffl) + " : " + msg);
                //进行 下课，白版等业务逻辑的处理
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        initDismissDialog();
//                    }
//                });
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mDrawView.eventActionDown(598.3423f, 1807.26172f);
                        mDrawView.eventActionMove(587.44653f, 1804.27344f);
                        mDrawView.eventActionMove(569.23495f, 1809.27728f);
                        mDrawView.eventActionMove(549.37833f, 1814.63873f);
                        mDrawView.eventActionMove(535.32938f, 1818.7486f);
                        mDrawView.eventActionMove(514.23633f, 1829.7107f);
                        mDrawView.eventActionMove(504.03308f, 1833.38202f);
                        mDrawView.eventActionMove(495.6029f, 1843.06268f);
                        mDrawView.eventActionMove(480.0887f, 1855.4698f);
                        mDrawView.eventActionMove(474.37845f, 1867.63525f);
                        mDrawView.eventActionMove(464.10852f, 1876.40698f);
                        mDrawView.eventActionMove(457.64493f, 1885.87323f);
                        mDrawView.eventActionUp(457.64493f, 1885.87323f);
                    }
                });


            }

            //收到频道消息回调(onMessageChannelReceive)
            @Override
            public void onMessageChannelReceive(String channelID, String account, int uid, String msg) {
                Log.d(TAG, "频道消息：" + channelID + " " + account + " : " + msg);

            }

            @Override
            public void onMessageSendSuccess(String messageID) {
                super.onMessageSendSuccess(messageID);
                Log.d(TAG, "发送成功");
            }

            @Override
            public void onMessageSendError(String messageID, int ecode) {
                super.onMessageSendError(messageID, ecode);
                Log.d(TAG, "Error:" + messageID + ecode);
            }

            @Override
            public void onLogout(int ecode) {
                super.onLogout(ecode);
                Log.d(TAG, "Logout:" + ecode);
            }

            //加入频道后 回调
            @Override
            public void onChannelJoined(String chanID) {
                Log.d(TAG, "Join channel " + chanID + " successfully"); // + " docall " + doCall);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initRoom();
                    }
                });
            }
        });
    }

    private void joinSignalling() {
        //测试数据 信令频道
        String channelName = mRoomInfo.getSignallingChannelId();
        Log.d(TAG,"Join channel " + channelName);
        m_agoraAPI.channelJoin(channelName);
    }
    private void initDismissDialog() {
        DialogUtil.showOnlyAlert(this,
                ""
                , "我要下课"
                , "确定"
                , "取消"
                , true, true, new DialogListener() {
                    @Override
                    public void onPositive(DialogInterface dialog) {
                        //取消
                        Log.d(TAG, "取消");
                    }

                    @Override
                    public void onNegative(DialogInterface dialog) {
                        //退出教室
                        if (m_agoraAPI != null) {
                            m_agoraAPI.logout();
                        }
                        if (mUidsList != null) {
                            mUidsList.clear();
                        }
                        quitCall();
                    }
                }
        );
    }


    @Override
    public void showCourseWareImageList(List<CourseWareImageList> data) {
        Log.d(Constants.Async, "课件图片size : " + data.size());
        //TODO 数据有问题
//        ImageLoader.loadImage(Glide.with(this), mImageCourseWare, data.get(0).imageUrl);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.but_dimiss:
                //call  的对象 假数据即老师信令的id
                String peer = mRoomInfo.getChannelTeacherId()+"";
                //发送点对点 消息
                m_agoraAPI.messageInstantSend(peer, 0, "finishClass", "");
                break;

        }
    }


    //封装到uitl中
    public String calcToken(String appID, String certificate, String account, long expiredTime) {
        // Token = 1:appID:expiredTime:sign
        // Token = 1:appID:expiredTime:md5(account + vendorID + certificate + expiredTime)

        String sign = md5hex((account + appID + certificate + expiredTime).getBytes());
        return "1:" + appID + ":" + expiredTime + ":" + sign;

    }
    private void optional() {
        HeadsetPlugManager.getInstance().registerHeadsetPlugListener(this);
        mHeadsetListener = new HeadsetBroadcastReceiver();
        registerReceiver(mHeadsetListener, new IntentFilter(Intent.ACTION_HEADSET_PLUG));

        mBluetoothHeadsetBroadcastListener = new BluetoothHeadsetBroadcastReceiver();
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBtAdapter != null && BluetoothProfile.STATE_CONNECTED == mBtAdapter.getProfileConnectionState(BluetoothProfile.HEADSET)) { // on some devices, BT is not supported
            boolean bt = mBtAdapter.getProfileProxy(getBaseContext(), mBluetoothHeadsetListener, BluetoothProfile.HEADSET);
            int connection = mBtAdapter.getProfileConnectionState(BluetoothProfile.HEADSET);
        }

        IntentFilter i = new IntentFilter(BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED);
        i.addAction(BluetoothHeadset.ACTION_AUDIO_STATE_CHANGED);
        i.addAction(AudioManager.ACTION_SCO_AUDIO_STATE_UPDATED);
        registerReceiver(mBluetoothHeadsetBroadcastListener, i);

        setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
    }

    private void optionalDestroy() {
        if (mBtAdapter != null) {
            mBtAdapter.closeProfileProxy(BluetoothProfile.HEADSET, mBluetoothProfile);
            mBluetoothProfile = null;
            mBtAdapter = null;
        }

        if (mBluetoothHeadsetBroadcastListener != null) {
            unregisterReceiver(mBluetoothHeadsetBroadcastListener);
            mBluetoothHeadsetBroadcastListener = null;
        }

        if (mHeadsetListener != null) {
            unregisterReceiver(mHeadsetListener);
            mHeadsetListener = null;
        }
        HeadsetPlugManager.getInstance().unregisterHeadsetPlugListener(this);
    }

    private int getVideoProfileIndex() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        int profileIndex = pref.getInt(ConstantApp.PrefManager.PREF_PROPERTY_PROFILE_IDX, ConstantApp.DEFAULT_PROFILE_IDX);
        if (profileIndex > ConstantApp.VIDEO_PROFILES.length - 1) {
            profileIndex = ConstantApp.DEFAULT_PROFILE_IDX;

            // save the new value
            SharedPreferences.Editor editor = pref.edit();
            editor.putInt(ConstantApp.PrefManager.PREF_PROPERTY_PROFILE_IDX, profileIndex);
            editor.apply();
        }
        return profileIndex;
    }

    /*private void doConfigEngine(String encryptionKey, String encryptionMode) {
        int vProfile = ConstantApp.VIDEO_PROFILES[getVideoProfileIndex()];

        worker().configEngine(vProfile, encryptionKey, encryptionMode);
    }*/
    @Override
    protected void deInitUIandEvent() {
        optionalDestroy();

        doLeaveChannel();
        event().removeEventHandler(this);

    }

    private void doLeaveChannel() {
        worker().leaveChannel(mChannelName);
        worker().preview(false, null, 0);

    }
    @Override
    public void onBackPressedSupport() {
        if (m_agoraAPI != null) {
            m_agoraAPI.logout();
            Log.d(TAG, "信令退出");
        }
        if (mUidsList != null) {
            mUidsList.clear();
        }
        quitCall();
        Log.d(TAG, "onBackPressed");
    }
    private void quitCall() {
        deInitUIandEvent();
        mUid = "";
        finish();
    }

    private VideoPreProcessing mVideoPreProcessing;

    private void doHideTargetView(int targetUid, boolean hide) {
        HashMap<Integer, Integer> status = new HashMap<>();
        status.put(targetUid, hide ? UserStatusData.VIDEO_MUTED : UserStatusData.DEFAULT_STATUS);
        if (mLayoutType == LAYOUT_TYPE_DEFAULT) {
            mGridVideoViewContainer.notifyUiChanged(mUidsList, targetUid, status, null);
            Log.d(TAG, "LAYOUT_TYPE_DEFAULT");
        } else if (mLayoutType == LAYOUT_TYPE_SMALL) {
            UserStatusData bigBgUser = mGridVideoViewContainer.getItem(0);
            Log.d(TAG, "LAYOUT_TYPE_SMALL");
            if (bigBgUser.mUid == targetUid) { // big background is target view
                mGridVideoViewContainer.notifyUiChanged(mUidsList, targetUid, status, null);
            } else { // find target view in small video view list
//                log.warn("SmallVideoViewAdapter call notifyUiChanged " + mUidsList + " " + (bigBgUser.mUid & 0xFFFFFFFFL) + " taget: " + (targetUid & 0xFFFFFFFFL) + "==" + targetUid + " " + status);
                mSmallVideoViewAdapter.notifyUiChanged(mUidsList, bigBgUser.mUid, status, null);
            }
        }
    }
    //远端 限定 只显示老师
    @Override
    public void onFirstRemoteVideoDecoded(int uid, int width, int height, int elapsed) {
        if (uid == mRoomInfo.getChannelTeacherId()) {
            doRenderRemoteUi(uid);
        }
    }

    private void doRenderRemoteUi(final int uid) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isFinishing()) {
                    return;
                }
                if (mUidsList.containsKey(uid)) {
                    return;
                }
                SurfaceView surfaceV = RtcEngine.CreateRendererView(getApplicationContext());
                mUidsList.put(uid, new SoftReference<>(surfaceV));
                Log.d(TAG, "远端下 集合长度：" + mUidsList.size());
                boolean useDefaultLayout = mLayoutType == LAYOUT_TYPE_DEFAULT && mUidsList.size() != 2;
                surfaceV.setZOrderOnTop(!useDefaultLayout);
                surfaceV.setZOrderMediaOverlay(!useDefaultLayout);
//设置远端视频显示属性 (setupRemoteVideo)
                rtcEngine().setupRemoteVideo(new VideoCanvas(surfaceV, VideoCanvas.RENDER_MODE_HIDDEN, uid));

               /* if (useDefaultLayout) {
                    log.debug("doRenderRemoteUi LAYOUT_TYPE_DEFAULT " + (uid & 0xFFFFFFFFL));
                    switchToDefaultVideoView();
                } else {
                    int bigBgUid = mSmallVideoViewAdapter == null ? uid : mSmallVideoViewAdapter.getExceptedUid();
                    Log.d(TAG, "bigBgUid:" + bigBgUid);
                    Log.d(TAG, "uid:" + uid);
                    switchToSmallVideoView(bigBgUid);
                }*/
                switchToSmallVideoView(uid);

            }
        });
    }
    @Override
    public void onJoinChannelSuccess(String channel, final int uid, int elapsed) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "加入成功后uid: " + uid);
                mRl_bg.setVisibility(View.GONE);
                mMChronometer.start();
                if (isFinishing()) {
                    return;
                }
                SoftReference<SurfaceView> local = mUidsList.remove(0);
                if (local == null) {
                    return;
                }
                mUidsList.put(uid, local);
                rtcEngine().muteLocalAudioStream(mAudioMuted);
                worker().getRtcEngine().setEnableSpeakerphone(true);
                mRl_bg.setVisibility(View.GONE);
            }
        });
    }
    @Override
    public void onUserJoined(int uid, int elapsed) {
        Log.d(TAG, "uid:onUserJoined " + uid);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mRl_bg.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public void onUserOffline(int uid, int reason) {
        doRemoveRemoteUi(uid);
    }

    @Override
    public void onExtraCallback(final int type, final Object... data) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isFinishing()) {
                    return;
                }
                doHandleExtraCallback(type, data);
            }
        });
    }

    private void doHandleExtraCallback(int type, Object... data) {
        Log.d(TAG, "type:" + type);
        int peerUid;
        boolean muted;
        switch (type) {
            case AGEventHandler.EVENT_TYPE_ON_USER_AUDIO_MUTED:
                peerUid = (Integer) data[0];
                muted = (boolean) data[1];
                if (mLayoutType == LAYOUT_TYPE_DEFAULT) {
                    HashMap<Integer, Integer> status = new HashMap<>();
                    status.put(peerUid, muted ? UserStatusData.AUDIO_MUTED : UserStatusData.DEFAULT_STATUS);
                    mGridVideoViewContainer.notifyUiChanged(mUidsList, config().mUid, status, null);
                }

                break;

            case AGEventHandler.EVENT_TYPE_ON_USER_VIDEO_MUTED:
                peerUid = (Integer) data[0];
                muted = (boolean) data[1];
                doHideTargetView(peerUid, muted);
                break;
            //远端视频统计回调
            case AGEventHandler.EVENT_TYPE_ON_USER_VIDEO_STATS:
                IRtcEngineEventHandler.RemoteVideoStats stats = (IRtcEngineEventHandler.RemoteVideoStats) data[0];
                if (Constant.SHOW_VIDEO_INFO) {
                    if (mLayoutType == LAYOUT_TYPE_DEFAULT) {
                        Log.d(TAG, "远端视频统计回调 LAYOUT_TYPE_DEFAULT");
                        Log.d(TAG, "stats.uid:" + stats.uid);
                        mGridVideoViewContainer.addVideoInfo(stats.uid, new VideoInfoData(stats.width, stats.height, stats.delay, stats.receivedFrameRate, stats.receivedBitrate));
                        int uid = config().mUid;
                        int profileIndex = getVideoProfileIndex();
                        String resolution = getResources().getStringArray(R.array.string_array_resolutions)[profileIndex];
                        String fps = getResources().getStringArray(R.array.string_array_frame_rate)[profileIndex];
                        String bitrate = getResources().getStringArray(R.array.string_array_bit_rate)[profileIndex];
                        String[] rwh = resolution.split("x");
                        int width = Integer.valueOf(rwh[0]);
                        int height = Integer.valueOf(rwh[1]);
                        mGridVideoViewContainer.addVideoInfo(uid, new VideoInfoData(width > height ? width : height,
                                width > height ? height : width,
                                0, Integer.valueOf(fps), Integer.valueOf(bitrate)));
                    }
                } else {
                    mGridVideoViewContainer.cleanVideoInfo();
                    Log.d(TAG, "远端视频统计回调 else");
                }
                break;
            case AGEventHandler.EVENT_TYPE_ON_SPEAKER_STATS:
                IRtcEngineEventHandler.AudioVolumeInfo[] infos = (IRtcEngineEventHandler.AudioVolumeInfo[]) data[0];
                if (infos.length == 1 && infos[0].uid == 0) { // local guy, ignore it
                    break;
                }
                if (mLayoutType == LAYOUT_TYPE_DEFAULT) {
                    HashMap<Integer, Integer> volume = new HashMap<>();
                    for (IRtcEngineEventHandler.AudioVolumeInfo each : infos) {
                        peerUid = each.uid;
                        int peerVolume = each.volume;
                        if (peerUid == 0) {
                            continue;
                        }
                        volume.put(peerUid, peerVolume);
                    }
                    if (mGridVideoViewContainer != null) {
                        mGridVideoViewContainer.notifyUiChanged(mUidsList, config().mUid, null, volume);
                    }
                }
                break;
            case AGEventHandler.EVENT_TYPE_ON_APP_ERROR:
                int subType = (int) data[0];
                if (subType == ConstantApp.AppError.NO_NETWORK_CONNECTION) {
                    showLongToast(getString(R.string.msg_no_network_connection));
                }
                break;
            case AGEventHandler.EVENT_TYPE_ON_DATA_CHANNEL_MSG:
                peerUid = (Integer) data[0];
                final byte[] content = (byte[]) data[1];
//                notifyMessageChanged(new Message(new User(peerUid, String.valueOf(peerUid)), new String(content)));
                break;
            case AGEventHandler.EVENT_TYPE_ON_AGORA_MEDIA_ERROR: {
                int error = (int) data[0];
                String description = (String) data[1];
//                notifyMessageChanged(new Message(new User(0, null), error + " " + description));
                break;
            }
        }
    }

    private void requestRemoteStreamType(final int currentHostCount) {
//        log.debug("requestRemoteStreamType " + currentHostCount);
    }

    private void doRemoveRemoteUi(final int uid) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isFinishing()) {
                    return;
                }

                Object target = mUidsList.remove(uid);
                if (target == null) {
                    return;
                }
                int bigBgUid = -1;
                if (mSmallVideoViewAdapter != null) {
                    bigBgUid = mSmallVideoViewAdapter.getExceptedUid();
                }
//                log.debug("doRemoveRemoteUi " + (uid & 0xFFFFFFFFL) + " " + (bigBgUid & 0xFFFFFFFFL) + " " + mLayoutType);
                if (mLayoutType == LAYOUT_TYPE_DEFAULT || uid == bigBgUid) {
                    switchToDefaultVideoView();
                } else {
                    switchToSmallVideoView(bigBgUid);
                }
            }
        });
    }

    private SmallVideoViewAdapter mSmallVideoViewAdapter;

    private void switchToDefaultVideoView() {
        if (mSmallVideoViewDock != null) {
            mSmallVideoViewDock.setVisibility(View.GONE);
        }
        if (mGridVideoViewContainer != null) {
            mGridVideoViewContainer.initViewContainer(getApplicationContext(), config().mUid, mUidsList);
            mLayoutType = LAYOUT_TYPE_DEFAULT;
        }
    }

    private void switchToSmallVideoView(int bigBgUid) {
        HashMap<Integer, SoftReference<SurfaceView>> slice = new HashMap<>(1);
        slice.put(bigBgUid, mUidsList.get(bigBgUid));
        if (mGridVideoViewContainer != null) {
            mGridVideoViewContainer.initViewContainer(getApplicationContext(), bigBgUid, slice);
        }
        bindToSmallVideoView(bigBgUid);
        mLayoutType = LAYOUT_TYPE_SMALL;
        requestRemoteStreamType(mUidsList.size());

    }

    public int mLayoutType = LAYOUT_TYPE_DEFAULT;

    public static final int LAYOUT_TYPE_DEFAULT = 0;

    public static final int LAYOUT_TYPE_SMALL = 1;

    private void bindToSmallVideoView(int exceptUid) {
        if (mSmallVideoViewDock == null) {
            ViewStub stub = (ViewStub) findViewById(R.id.small_video_view_dock);
            mSmallVideoViewDock = (RelativeLayout) stub.inflate();
        }

        boolean twoWayVideoCall = mUidsList.size() == 2;
        RecyclerView recycler = (RecyclerView) findViewById(R.id.small_video_view_container);

        boolean create = false;

        if (mSmallVideoViewAdapter == null) {
            create = true;
            mSmallVideoViewAdapter = new SmallVideoViewAdapter(this, config().mUid, exceptUid, mUidsList, new VideoViewEventListener() {
                @Override
                public void onItemDoubleClick(View v, Object item) {
//                    switchToDefaultVideoView();
                }
            });
//            mSmallVideoViewAdapter.setHasStableIds(true);
        }
//        recycler.setHasFixedSize(true);

//        log.debug("bindToSmallVideoView " + twoWayVideoCall + " " + (exceptUid & 0xFFFFFFFFL));

        if (twoWayVideoCall) {
//            recycler.setLayoutManager(new RtlLinearLayoutManager(this, RtlLinearLayoutManager.HORIZONTAL, false));
        } else {
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false) {

            @Override
            public boolean canScrollVertically() {
                return false;


            }
        };
        recycler.setLayoutManager(linearLayoutManager);
        recycler.addItemDecoration(new SmallVideoViewDecoration());
        recycler.setAdapter(mSmallVideoViewAdapter);

        if (!create) {
            mSmallVideoViewAdapter.setLocalUid(config().mUid);
            mSmallVideoViewAdapter.notifyUiChanged(mUidsList, exceptUid, null, null);
        }
        recycler.setVisibility(View.VISIBLE);
        mSmallVideoViewDock.setVisibility(View.VISIBLE);



    }

    @Override
    public void notifyHeadsetPlugged(final boolean plugged, Object... extraData) {
//        log.info("notifyHeadsetPlugged " + plugged + " " + extraData);
        boolean bluetooth = false;
        if (extraData != null && extraData.length > 0 && (Integer) extraData[0] == HeadsetPlugManager.BLUETOOTH) { // this is only for bluetooth
            bluetooth = true;
        }
        headsetPlugged(plugged);
        mWithHeadset = plugged;
    }

    @Override
    public void showError(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }
}
