package io.agore.openvcall.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.google.gson.Gson;
import com.onlyhiedu.pro.Model.bean.CourseList;
import com.onlyhiedu.pro.Model.bean.CourseWareImageList;
import com.onlyhiedu.pro.Model.bean.RoomInfo;
import com.onlyhiedu.pro.Model.bean.board.BoardBean;
import com.onlyhiedu.pro.R;
import com.onlyhiedu.pro.Service.NetworkStateService;
import com.onlyhiedu.pro.Utils.DateUtil;
import com.onlyhiedu.pro.Utils.DialogListener;
import com.onlyhiedu.pro.Utils.DialogUtil;
import com.onlyhiedu.pro.Utils.JsonUtil;
import com.onlyhiedu.pro.Utils.SPUtil;
import com.onlyhiedu.pro.Utils.SnackBarUtils;
import com.onlyhiedu.pro.Widget.MyWhiteBoardView;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;
import cn.robotpen.model.entity.note.NoteEntity;
import cn.robotpen.model.entity.note.TrailsEntity;
import cn.robotpen.model.symbol.DeviceType;
import cn.robotpen.views.widget.WhiteBoardView;
import io.agora.AgoraAPI;
import io.agora.AgoraAPIOnlySignal;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;
import io.agore.openvcall.model.AGEventHandler;
import io.agore.openvcall.model.ConstantApp;
import io.agore.openvcall.model.User;

import static com.onlyhiedu.pro.R.id.ll_video;
import static com.onlyhiedu.pro.R.id.whiteBoardView;
import static com.onlyhiedu.pro.Utils.Encrypt.md5hex;

public abstract class ChatActivity2 extends BaseActivity2<ChatPresenter2> implements AGEventHandler, ChatContract2.View, Chronometer.OnChronometerTickListener, WhiteBoardView.WhiteBoardInterface {

    private final HashMap<Integer, SoftReference<SurfaceView>> mUidsList = new HashMap<>(); // uid = 0 || uid == EngineConfig.mUid
    public static final String TAG = ChatActivity2.class.getSimpleName();
    //    private GoogleApiClient client;
    private String mUid;

    @BindView(ll_video)
    LinearLayout mLlVideo;
    @BindView(R.id.rl_bg)
    RelativeLayout mRl_bg;
    @BindView(R.id.chronometer)
    Chronometer mChronometer;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tv_total_room)
    TextView mTv_Total_Room;
    @BindView(R.id.msg_list)
    RecyclerView msgListView;
    @BindView(R.id.edit)
    EditText mEditText;
    @BindView(R.id.ll_msg)
    LinearLayout mLlMsg;
    @BindView(R.id.but_im)
    TextView mTvIM;
    @BindView(R.id.im_point)
    View mIMPoint;
    @BindView(R.id.but_dismiss)
    TextView mButDismiss;
    private AgoraAPIOnlySignal m_agoraAPI;
    @BindView(R.id.rel_tea)
    RelativeLayout mRel_Tea;
    @BindView(R.id.rel_stu)
    RelativeLayout mRel_Stu;
    @BindView(whiteBoardView)
    MyWhiteBoardView mWhiteBoardView;


    private String mChannelName;
    private RoomInfo mRoomInfo;
    public RequestManager mRequestManager;
    public List<CourseWareImageList> mCourseWareImageLists;//课件列表数据

    //课程id
    private String mUuid;
    private CourseList.ListBean mListBean;
    private Handler mHandler = null;
    private static final int UPDATE_TIME = 0;
    private static final int UPDATE_FINISH_ROOM = 1;
    private static final int UPDATE_NOTIFY = 2;
    private Timer mTimer = null;
    private TimerTask mTimerTask = null;
    private Timer mFinishTimer = null;
    private TimerTask mFinishTimerTask = null;
    private static int delay = 1000;  //1s
    private static int period = 1000;  //1s
    private String mRoomStartTime;
    private String mRoomEndTime;
    private String mEndtime1;
    private String mTime;
    private long mRoomDix;
    private long mLong;
    private boolean mIsBack; //返回键是否可点击
    private boolean isStartTime;
    private boolean isTeacherJoined;


    int mLastDownY;
    private int mCurryY;

    private int mDelY;
    //向上滑动基数
    private int mUpValue = 3;
    private SurfaceView mStuSurfView;
    private Gson mGson;

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_chat2;
    }

    @Override
    protected void initUIandEvent() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        mRequestManager = Glide.with(this);

        setToolBar();
        event().addEventHandler(this);

        initRoomData();
        //登录信令系统成功后  登录通信频道
        initSignalling();
        mStuSurfView = RtcEngine.CreateRendererView(getApplicationContext());
        rtcEngine().setupLocalVideo(new VideoCanvas(mStuSurfView, VideoCanvas.RENDER_MODE_HIDDEN, 0));
        mStuSurfView.setZOrderOnTop(false);
        mStuSurfView.setZOrderMediaOverlay(false);
        mUidsList.put(0, new SoftReference<>(mStuSurfView)); // get first surface view
//        mGridVideoViewContainer.initViewContainer(getApplicationContext(), Integer.parseInt(mUid), mUidsList); // first is now full view

        rtcEngine().muteLocalAudioStream(false);
        rtcEngine().enableLocalVideo(true);
        //不发送本地视频流
        rtcEngine().muteLocalVideoStream(false);
        //暂停所有远端视频流
        rtcEngine().muteAllRemoteAudioStreams(false);
        //暂停所有远端音频
        rtcEngine().muteAllRemoteAudioStreams(false);
        worker().preview(true, mStuSurfView, Integer.parseInt(mUid));

        initMessageList();
        startCountTimeThread();

        mToolbar.animate().translationY(mToolbar.getHeight()).setInterpolator(new DecelerateInterpolator(2));
        visableTag = 1;

        //白板
        mWhiteBoardView.setIsTouchWrite(false);//默认用手输入
//        mWhiteBoardView.setDaoSession(App.getInstance().getDaoSession());
        mWhiteBoardView.setLoadIgnorePhoto(false);//设置加载时是否忽略图片
    }

    @Override
    public void onResume() {
        super.onResume();
        mWhiteBoardView.initDrawArea();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, NetworkStateService.class);
        startService(intent);
    }


    @Override
    protected void onStop() {
        super.onStop();
        Intent intent = new Intent(this, NetworkStateService.class);
        stopService(intent);
    }

    private InChannelMessageListAdapter mMsgAdapter;

    private ArrayList<io.agore.openvcall.model.Message> mMsgList;


    private void initMessageList() {
        mMsgList = new ArrayList<>();
        mMsgAdapter = new InChannelMessageListAdapter(this, mMsgList);
        mMsgAdapter.setHasStableIds(true);
        msgListView.setAdapter(mMsgAdapter);
        msgListView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        msgListView.addItemDecoration(new MessageListDecoration());
    }


    private void initRoomData() {
        mChronometer.setBase(SystemClock.elapsedRealtime());
        int hour = (int) ((SystemClock.elapsedRealtime() - mChronometer.getBase()) / 1000 / 60);
        mChronometer.setFormat("0" + String.valueOf(hour) + ":%s");
        mChronometer.setText("00:00:00");
        mChronometer.setOnChronometerTickListener(this);
        //获取频道 id  老师uid 学生uid
        mRoomInfo = (RoomInfo) getIntent().getSerializableExtra("roomInfo");
        mListBean = (CourseList.ListBean) getIntent().getSerializableExtra("ListBean");
        if (mListBean != null) {
            mUuid = mListBean.getUuid();
            //计时
            initRoomTime();
        }
        if (mRoomInfo != null) {
            Log.d(TAG, "item:" + mRoomInfo.getSignallingChannelId());
            //课程频道
            mChannelName = mRoomInfo.getCommChannelId();
            //学生uid
            mUid = SPUtil.getAgoraUid() + "";
        }
    }


    private void initRoomTime() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case UPDATE_TIME:
                        startRoomTime();
                        break;
                    case UPDATE_FINISH_ROOM:
                        finishRoom();
                        break;
                    case UPDATE_NOTIFY:
                        initSoonDialog();
                        break;
                    default:
                        break;
                }
            }
        };
        String startTime = mListBean.getStartTime() + ":00";
        String courseDate = mListBean.getCourseDate();
        String endTime_start = mListBean.getEndTime() + ":00";
//        String startTime = "11:55:00";
//        String courseDate = "2017-04-14";
//        String endTime_start = "11:56:00";
        mTime = DateUtil.getTime(courseDate + " " + startTime);
        mEndtime1 = DateUtil.getTime(courseDate + " " + endTime_start);
        mRoomStartTime = DateUtil.getStrTime(mTime);
        mRoomEndTime = DateUtil.getStrTime(mEndtime1);
        String nowTime = DateUtil.formatDate(new Date(System.currentTimeMillis()), DateUtil.yyyyMMddHHmmss);
        DateFormat df = new SimpleDateFormat(DateUtil.yyyyMMddHHmmss);
        try {
            Date room_start = df.parse(mRoomStartTime);
            Date room_end = df.parse(mRoomEndTime);
            mRoomDix = room_end.getTime() - room_start.getTime();
            DateUtil.updateTimeFormat(mTv_Total_Room,   mRoomDix);
            Date now = df.parse(nowTime);
            long diff = room_start.getTime() - now.getTime();
            Log.d(TAG, "diffs:" + diff / (1000 * 60));
            //没到开始时间
            if (diff > 0) {
                startTimer();
            } else if (diff < 0) {
                //从迟到时间开始计时
                mChronometer.setBase(SystemClock.elapsedRealtime() + diff);
                mChronometer.start();
            } else {
//                mChronometer.start();

            }
        } catch (Exception e) {
        }
    }

    private void initSoonDialog() {
        DialogUtil.showOnlyAlert(this,
                "提示"
                , "本节课即将在3分钟后关闭！"
                , "知道了"
                , ""
                , true, true, new DialogListener() {
                    @Override
                    public void onPositive(DialogInterface dialog) {
                    }

                    @Override
                    public void onNegative(DialogInterface dialog) {
                    }
                }
        );
    }

    private void finishRoom() {
        if (m_agoraAPI != null) {
            m_agoraAPI.logout();
            Log.d(TAG, "信令退出");
        }
        if (mUidsList != null) {
            mUidsList.clear();
        }
        DialogUtil.showOnlyAlert(this,
                "提示"
                , "已超出课堂15分钟，自动关闭"
                , "离开教室"
                , ""
                , false, false, new DialogListener() {
                    @Override
                    public void onPositive(DialogInterface dialog) {
                        quitCall();
                    }

                    @Override
                    public void onNegative(DialogInterface dialog) {
                    }
                }
        );
    }

    private void startTimer() {
        if (mTimer == null) {
            mTimer = new Timer();
        }
        if (mTimerTask == null) {
            mTimerTask = new TimerTask() {
                @Override
                public void run() {
                    if (DateUtil.formatDate(new Date(System.currentTimeMillis()), DateUtil.yyyyMMddHHmmss).equals(mRoomStartTime)) {
                        sendMessage(UPDATE_TIME);
                    }
                }
            };
        }
        if (mTimer != null && mTimerTask != null)
            mTimer.schedule(mTimerTask, delay, period);
    }

    private void startFinishTimer() {
        if (mFinishTimer == null) {
            mFinishTimer = new Timer();
        }
        if (mFinishTimerTask == null) {
            mFinishTimerTask = new TimerTask() {
                @Override
                public void run() {
                    long l = SystemClock.elapsedRealtime() - mChronometer.getBase();
                    //超时15分钟
                    if (l > (mRoomDix + 15 * 60 * 1000) && l < (mRoomDix + 15 * 60 * 1000 + 1000)) {
                        sendMessage(UPDATE_FINISH_ROOM);
                    }
                    // 提前3分钟 通知即将推出教室
                    if (l > (mRoomDix + 12 * 60 * 1000) && l < (mRoomDix + 12 * 60 * 1000 + 1000)) {
                        sendMessage(UPDATE_NOTIFY);
                    }
                }
            };
        }
        if (mFinishTimer != null && mFinishTimerTask != null)
            mFinishTimer.schedule(mFinishTimerTask, delay, period);
    }

    private void stopTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
    }

    private void stopFinishTimer() {
        if (mFinishTimer != null) {
            mFinishTimer.cancel();
            mFinishTimer = null;
        }
        if (mFinishTimerTask != null) {
            mFinishTimerTask.cancel();
            mFinishTimerTask = null;
        }
    }

    public void sendMessage(int id) {
        if (mHandler != null) {
            Message message = Message.obtain(mHandler, id);
            mHandler.sendMessage(message);
        }
    }

    private void startRoomTime() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mChronometer.setBase(SystemClock.elapsedRealtime());
                int hour = (int) ((SystemClock.elapsedRealtime() - mChronometer.getBase()) / 1000 / 60);
                mChronometer.setFormat("0" + String.valueOf(hour) + ":%s");
                mChronometer.setText("00:00:00");
                mChronometer.start();
            }
        });
    }

    private void setToolBar() {
        mToolbar.setBackgroundResource(R.color.c33);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle("");
        mToolbar.setNavigationIcon(R.drawable.back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsBack) {
                    finishClassRoom();
                } else {
                    Toast.makeText(mContext, "课程还未结束,可点击我要下课", Toast.LENGTH_SHORT).show();
                }
            }
        });
        /*mToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/

        mToolbar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        mLastDownY = (int) event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
                        mCurryY = (int) event.getY();
                        mDelY = mCurryY - mLastDownY;
                        if (mDelY < 0) {
                            // 向上滑动超过 基数 开启向上消失动画
                            if (Math.abs(mDelY) > mUpValue) {
                                if (visableTag == 1 && mToolbar != null) {
                                    visableTag = 0;
                                    mToolbar.animate().translationY(-mToolbar.getHeight()).setInterpolator(new AccelerateInterpolator(2));
                                    if (mViewHandler != null) {
                                        mViewHandler.removeCallbacksAndMessages(null);
                                    }
                                }
                            }
                        }
                        break;
                }

                return false;
            }
        });

    }


    private void finishClassRoom() {
        if (m_agoraAPI != null) {
            m_agoraAPI.logout();
            Log.d(TAG, "信令退出");
        }
        if (mUidsList != null) {
            mUidsList.clear();
        }
        if (mViewHandler != null) {
            mViewHandler.removeCallbacksAndMessages(null);
        }
        quitCall();
    }


    private void initRoom() {
        worker().joinChannel(mChannelName, Integer.parseInt(mUid));
    }

    private void initSignalling() {
        //从服务器获取
        String certificate = this.getString(R.string.private_app_cate);
        String appId = this.getString(R.string.private_app_id);
        String account = SPUtil.getAgoraUid() + "";
        m_agoraAPI = AgoraAPIOnlySignal.getInstance(this, appId);
        long expiredTime = System.currentTimeMillis() / 1000 + 3600;
        String token = calcToken(appId, certificate, account, expiredTime);
        m_agoraAPI.login2(appId, account, token, 0, "", 60, 5);
        m_agoraAPI.callbackSet(new AgoraAPI.CallBack() {
            @Override
            public void onLoginSuccess(int uid, int fd) {
                Log.d(TAG, "Login successfully" + uid);
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

            @Override
            public void onChannelUserLeaved(String account, int uid) {
                //信令通道当有其他用户退出
                if (uid == mListBean.channelTeacherId) {
                    SnackBarUtils.show(mToolbar, "老师已退出课堂", Color.GREEN);
                }
            }

            //对方将收到 onMessageInstantReceive 回调。
            @Override
            public void onMessageInstantReceive(String account, int uid, String msg) {
                Log.d(TAG, "点对点消息：" + account + " : " + (long) (uid & 0xffffffffl) + " : " + msg);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (mListBean.channelTeacherId == Integer.parseInt(account)) {
                            if (mListBean.channelTeacherId == Integer.parseInt(account)) {
                                if (msg.startsWith("{\"Block\":")) {
                                    Log.d("Xwc", "进来了");
                                    TrailsEntity json = mPresenter.getJson(msg);
                                    mWhiteBoardView.drawTrailsPoint(json);
                                }
                            }


//                            if (msg.startsWith("p_")) {
//                                mPresenter.drawDevicePoint(msg, mWhiteBoardView);
//                                return;
//                            }
                            switch (msg) {
                                case "00":
                                    initDismissDialog();
                                    break;
                                case "ok":
                                    //老师同意下课
                                    if (m_agoraAPI != null) {
                                        m_agoraAPI.logout();
                                        Log.d(TAG, "信令退出");
                                    }
                                    if (mUidsList != null) {
                                        mUidsList.clear();
                                    }

                                    DialogUtil.showOnlyAlert(ChatActivity2.this,
                                            "提示"
                                            , "老师同意了您的下课请求"
                                            , "离开教室"
                                            , ""
                                            , false, false, new DialogListener() {
                                                @Override
                                                public void onPositive(DialogInterface dialog) {
                                                    quitCall();
                                                }

                                                @Override
                                                public void onNegative(DialogInterface dialog) {
                                                }
                                            }
                                    );
                                    break;
                                case "no":
                                    DialogUtil.showOnlyAlert(ChatActivity2.this,
                                            "提示"
                                            , "老师拒绝了您的下课请求"
                                            , "知道了"
                                            , ""
                                            , false, false, new DialogListener() {
                                                @Override
                                                public void onPositive(DialogInterface dialog) {
                                                }

                                                @Override
                                                public void onNegative(DialogInterface dialog) {
                                                }
                                            }
                                    );
                                    break;
                                case "clean":
                                    mWhiteBoardView.cleanScreen();
                                    break;
                            }
                        }
                    }
                });

            }

            //收到频道消息回调(onMessageChannelReceive)
            @Override
            public void onMessageChannelReceive(String channelID, String account, int uid, String msg) {
                Log.d(TAG, "频道消息：" + channelID + " " + account + " : " + msg);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mUid.equals(account)) {
                            return;
                        }
                        BoardBean boardBean = JsonUtil.parseJson(msg, BoardBean.class);
                        if (boardBean == null) {
                            return;
                        }
                        switch (boardBean.methodtype) {
                            case "IM":
                                try {
                                    JSONObject object = new JSONObject(boardBean.methodparam);
                                    String objectString = object.getString("msg");
                                    notifyMessageChanged(new io.agore.openvcall.model.Message(object.getInt("role"), new User(mListBean.channelTeacherId, boardBean.scaling), objectString));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                break;
                        }
                    }
                });

            }

            @Override
            public void onMessageSendSuccess(String messageID) {
                super.onMessageSendSuccess(messageID);
                Log.d(TAG, "发送成功");
                if (messageID.equals("stu_ok")) {
                    Log.d(TAG, "学生同意下课");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mContext, "同意老师下课，退出教室了。", Toast.LENGTH_SHORT).show();
                        }
                    });
                    finishClassRoom();
                }
                if (messageID.equals("stu_no")) {
                    Log.d(TAG, "学生拒绝下课");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mContext, "拒绝了下课请求", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                if (messageID.equals(requestFinishClassTag)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mContext, "下课请求已发送", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
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
                //error  103 自动退出
                if (ecode == 103) {
                    //暂停音视频流
                    //将自己静音
                    rtcEngine().muteLocalAudioStream(true);
                    //禁用本地视频功能
                    rtcEngine().enableLocalVideo(false);
                    //不发送本地视频流
                    rtcEngine().muteLocalVideoStream(true);
                    //暂停所有远端视频流
                    rtcEngine().muteAllRemoteVideoStreams(true);
                    //暂停所有远端音频
                    rtcEngine().muteAllRemoteAudioStreams(true);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            DialogUtil.showOnlyAlert(ChatActivity2.this,
                                    "提示"
                                    , "该账号已经在别处登录。"
                                    , "知道了"
                                    , ""
                                    , false, false, new DialogListener() {
                                        @Override
                                        public void onPositive(DialogInterface dialog) {
                                            finishClassRoom();
                                        }

                                        @Override
                                        public void onNegative(DialogInterface dialog) {
                                        }
                                    }
                            );
                        }
                    });

                }
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

            @Override
            public void onChannelUserList(String[] accounts, int[] uids) {
                super.onChannelUserList(accounts, uids);
                Log.d(TAG, "频道accounts：" + accounts.length);
            }
        });
    }

    private void joinSignalling() {
        // 信令频道
        String channelName = mRoomInfo.getSignallingChannelId();
        Log.d(TAG, "Join channel " + channelName);
        m_agoraAPI.channelJoin(channelName);
    }

    private void initDismissDialog() {
        DialogUtil.showOnlyAlert(this,
                ""
                , "老师请求下课"
                , "确定"
                , "取消"
                , true, true, new DialogListener() {
                    @Override
                    public void onPositive(DialogInterface dialog) {
                        if (mRoomInfo != null) {
                            //call  的对象 假数据即老师信令的id
                            String peer = mListBean.channelTeacherId + "";
                            m_agoraAPI.messageInstantSend(peer, 0, "ok", "stu_ok");
                        }
                    }

                    @Override
                    public void onNegative(DialogInterface dialog) {
                        //取消
                        Log.d(TAG, "取消");
                        if (mRoomInfo != null) {
                            String peer = mListBean.channelTeacherId + "";
                            m_agoraAPI.messageInstantSend(peer, 0, "no", "stu_no");
                        }
                    }
                }
        );
    }

    private void initFinishClassDialog() {
        DialogUtil.showOnlyAlert(this,
                ""
                , "确定结束本次课程并离开教室？"
                , "确定"
                , "取消"
                , true, true, new DialogListener() {
                    @Override
                    public void onPositive(DialogInterface dialog) {

                        finishClassRoom();
                    }

                    @Override
                    public void onNegative(DialogInterface dialog) {
                        //取消
                        Log.d(TAG, "取消");
                    }
                }
        );
    }


    //上传流时长完成
    @Override
    public void showFlowStatistics() {
        Log.d(TAG, "上传流时长完成");
//        Toast.makeText(ChatActivity.this, "上传流时长完成", Toast.LENGTH_SHORT).show();
        finish();
    }


    @OnClick({R.id.but_dismiss, R.id.tv_send, R.id.but_im})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.but_dismiss:
                canFinshClass();
                break;
            case R.id.but_im:
                if (mLlMsg.getVisibility() == View.GONE) {
                    mIMPoint.setVisibility(View.GONE);
                    showIMLayout();
                } else {
                    hindIMLayout();
                }
                break;
            case R.id.tv_send:
                String msgStr = mEditText.getText().toString();
                if (TextUtils.isEmpty(msgStr)) {
                    return;
                }
                sendChannelMsg(msgStr);

                mEditText.setText("");

                io.agore.openvcall.model.Message msg = new io.agore.openvcall.model.Message(io.agore.openvcall.model.Message.MSG_TYPE_TEXT,
                        new User(config().mUid, String.valueOf(config().mUid)), msgStr);
                notifyMessageChanged(msg);

                break;
        }
    }


    private void canFinshClass() {
        if (isStartTime && isTeacherJoined && (mIsBack == false)) {
            //学生点击我要下课
            requestFinishClass();
        } else {
            //没有开始上课计时      老师没有进入教室         当文案现实 退出教室时
            //isStartTime ==false isTeacherJoined==false mIsBack=true
            initFinishClassDialog();
        }
    }


    private void hindIMLayout() {
        mLlMsg.setVisibility(View.VISIBLE);
        mTvIM.setBackgroundResource(R.drawable.im_text_bg2);
        mTvIM.setTextColor(getResources().getColor(R.color.im_text_color2));
        TranslateAnimation animation = new TranslateAnimation(0, -mLlMsg.getWidth(), 1, 1);
        animation.setDuration(300);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mLlMsg.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mLlMsg.startAnimation(animation);
    }

    private void showIMLayout() {
        mLlMsg.setVisibility(View.VISIBLE);
        mTvIM.setBackgroundResource(R.drawable.im_text_bg);
        mTvIM.setTextColor(getResources().getColor(R.color.im_text_color));
        TranslateAnimation animation = new TranslateAnimation(-mLlMsg.getWidth(), 0, 1, 1);
        animation.setDuration(300);
        mLlMsg.startAnimation(animation);
    }


    private int mDataStreamId;

    private void sendChannelMsg(String msgStr) {
        BoardBean boardBean = new BoardBean();
        boardBean.methodtype = "IM";
        boardBean.methodparam = msgStr;
        boardBean.scaling = mUid;
        if (mGson == null) {
            mGson = new Gson();
        }
        m_agoraAPI.messageChannelSend(mRoomInfo.getSignallingChannelId(), mGson.toJson(boardBean), "sendIM");

//        RtcEngine rtcEngine = rtcEngine();
//        if (mDataStreamId <= 0) {
//            mDataStreamId = rtcEngine.createDataStream(true, true); // boolean reliable, boolean ordered
//        }
//
//        if (mDataStreamId < 0) {
//            String errorMsg = "Create data stream error happened " + mDataStreamId;
//            showLongToast(errorMsg);
//            return;
//        }
//
//        byte[] encodedMsg;
//        try {
//            encodedMsg = msgStr.getBytes("UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            encodedMsg = msgStr.getBytes();
//        }
//
//        rtcEngine.sendStreamMessage(mDataStreamId, encodedMsg);
    }

    private void notifyMessageChanged(io.agore.openvcall.model.Message msg) {
        mMsgList.add(msg);
        int position = mMsgList.size() - 1;
        LinearLayoutManager m = (LinearLayoutManager) msgListView.getLayoutManager();
        int firstItem = m.findFirstVisibleItemPosition();
        int lastItem = m.findLastVisibleItemPosition();
        if (position <= firstItem) {
            msgListView.smoothScrollToPosition(position);
        } else if (position <= lastItem) {
            int top = msgListView.getChildAt(position - firstItem).getTop();
            msgListView.smoothScrollBy(0, top);
        } else {
            msgListView.smoothScrollToPosition(position);
        }
        mMsgAdapter.notifyDataSetChanged();

        if (mLlMsg.getVisibility() == View.GONE) {
            mIMPoint.setVisibility(View.VISIBLE);
            mToolbar.animate().translationY(mToolbar.getHeight()).setInterpolator(new DecelerateInterpolator(2));
            visableTag = 1;
        }

    }


    //请求 下课 tag
    private static final String requestFinishClassTag = "requestFinishClassTag";

    private void requestFinishClass() {
        if (mRoomInfo != null) {
            //学生 给老师发送 我要下课请求
            String peer = mListBean.channelTeacherId + "";
            //发送点对点 消息
            m_agoraAPI.messageInstantSend(peer, 0, "00", requestFinishClassTag);
        }
    }


    //封装到uitl中
    public String calcToken(String appID, String certificate, String account, long expiredTime) {
        // Token = 1:appID:expiredTime:sign
        // Token = 1:appID:expiredTime:md5(account + vendorID + certificate + expiredTime)

        String sign = md5hex((account + appID + certificate + expiredTime).getBytes());
        return "1:" + appID + ":" + expiredTime + ":" + sign;

    }

    @Override
    protected void deInitUIandEvent() {
        doLeaveChannel();
    }

    private void doLeaveChannel() {
        worker().leaveChannel(mChannelName);
        worker().preview(false, null, 0);

    }


    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopTimer();
        stopFinishTimer();
        if (mChronometer != null) {
            mChronometer.stop();
        }
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
        isStartTime = false;

        if (mWhiteBoardView != null) {
            mWhiteBoardView.cleanScreen();  // 清屏
            mWhiteBoardView.dispose();
            mWhiteBoardView = null;
        }
    }

    private void quitCall() {
        deInitUIandEvent();
    }

    //远端 限定 只显示老师
    @Override
    public void onFirstRemoteVideoDecoded(int uid, int width, int height, int elapsed) {
        if (uid == mListBean.channelTeacherId) {
            initTeaView(uid);
        }
    }

    private void initTeaView(final int uid) {
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
                surfaceV.setZOrderOnTop(false);
                surfaceV.setZOrderMediaOverlay(false);
                //设置远端视频显示属性 (setupRemoteVideo)
                rtcEngine().setupRemoteVideo(new VideoCanvas(surfaceV, VideoCanvas.RENDER_MODE_HIDDEN, uid));
                mRel_Tea.addView(surfaceV);
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
                if (isFinishing()) {
                    return;
                }
                SoftReference<SurfaceView> local = mUidsList.remove(0);
                if (local == null) {
                    return;
                }
                mUidsList.put(uid, local);
                if (mRel_Stu != null && mStuSurfView != null) {
                    mRel_Stu.addView(mStuSurfView);
                }
            }
        });
    }

    @Override
    public void onRejoinChannelSuccess(String channel, int uid, int elapsed) {

    }

    //其他用户加入当前频道回调
    @Override
    public void onUserJoined(int uid, int elapsed) {
        Log.d(TAG, "uid:onUserJoined " + uid);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mRl_bg.setVisibility(View.GONE);
                if (uid == mListBean.channelTeacherId) {
                    isTeacherJoined = true;
                }
            }
        });
    }

    //其他用户离开当前频道回调
    @Override
    public void onUserOffline(int uid, int reason) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                isTeacherJoined = false;
                //当有其他用户退出
                if (uid == mListBean.channelTeacherId) {
                    SnackBarUtils.show(mToolbar, "老师已退出课堂", Color.GREEN);
                }
            }
        });
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

    @Override
    public void onLeaveChannel(IRtcEngineEventHandler.RtcStats stats) {
        //totalDuration：通话时长（秒），累计值
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                BigDecimal b = new BigDecimal((double) stats.totalDuration / 60.00);
                float f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
                try {
                    if (mListBean != null) {
                        mUuid = mListBean.getUuid();
                        mPresenter.uploadStatistics(String.valueOf(f1), mUuid);
                        event().removeEventHandler(ChatActivity2.this);
                    }
                } catch (Exception e) {
                    finish();
                }
//                Toast.makeText(ChatActivity.this, "流统计时长: " + f1 + " 分钟", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void doHandleExtraCallback(int type, Object... data) {
        Log.d(TAG, "type:" + type);
        int peerUid;
        boolean muted;
        switch (type) {


            case AGEventHandler.EVENT_TYPE_ON_USER_AUDIO_MUTED:

                break;

            case AGEventHandler.EVENT_TYPE_ON_USER_VIDEO_MUTED:
//                peerUid = (Integer) data[0];
//                muted = (boolean) data[1];
//                doHideTargetView(peerUid, muted);
                break;
            //远端视频统计回调
            case AGEventHandler.EVENT_TYPE_ON_USER_VIDEO_STATS:

                break;
            case AGEventHandler.EVENT_TYPE_ON_SPEAKER_STATS:

                break;
            case AGEventHandler.EVENT_TYPE_ON_APP_ERROR:
                //网络发生波动，请检查网络哦！
                int subType = (int) data[0];
                if (subType == ConstantApp.AppError.NO_NETWORK_CONNECTION) {
                    showLongToast(getString(R.string.msg_no_network_connection));
                }
                break;
            case AGEventHandler.EVENT_TYPE_ON_DATA_CHANNEL_MSG:

                peerUid = (Integer) data[0];
                final byte[] content = (byte[]) data[1];
                notifyMessageChanged(new io.agore.openvcall.model.Message(new User(peerUid, String.valueOf(peerUid)), new String(content)));
                break;
            case AGEventHandler.EVENT_TYPE_ON_AGORA_MEDIA_ERROR: {
                break;
            }
        }
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
                if (mRel_Tea != null && mRoomInfo != null && mListBean.channelTeacherId == uid) {

                    mRel_Tea.removeAllViews();
                }
            }
        });
    }


    public static final int LAYOUT_TYPE_DEFAULT = 0;


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mLastDownY = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                mCurryY = (int) event.getY();
                mDelY = mCurryY - mLastDownY;
                // 滑动超过 基数
                if (Math.abs(mDelY) < 2) {
                    mCountTimeThread.reset();
                    if (visableTag == 0) {
                        //可见
                        mToolbar.animate().translationY(mToolbar.getHeight()).setInterpolator(new DecelerateInterpolator(2));
                        visableTag = 1;
//                        return false;
                    } else {
                        if (visableTag == 1 && mToolbar != null) {
                            visableTag = 0;
                            mToolbar.animate().translationY(-mToolbar.getHeight()).setInterpolator(new AccelerateInterpolator(2));
                        }
                    }
                }
        }
        return super.dispatchTouchEvent(event);
    }

    int visableTag = 0;
    private CountTimeThread mCountTimeThread;

    /**
     * 开始启动线程控制按钮组件的显示.
     */
    private void startCountTimeThread() {
        mCountTimeThread = new CountTimeThread(3);
        mCountTimeThread.start();
    }

    /**
     * 隐藏控制按钮的控件
     */
    private void hide() {
        if (visableTag == 1 && mToolbar != null) {
            visableTag = 0;
            mToolbar.animate().translationY(-mToolbar.getHeight()).setInterpolator(new AccelerateInterpolator(2));
        }
    }

    private MainHandler mViewHandler = new MainHandler(ChatActivity2.this);


    private static class MainHandler extends Handler {
        /**
         * 隐藏按钮消息id
         */
        private final int MSG_HIDE = 0x0001;

        private WeakReference<ChatActivity2> weakRef;

        public MainHandler(ChatActivity2 activity) {
            weakRef = new WeakReference<ChatActivity2>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final ChatActivity2 mainActivity = weakRef.get();
            if (mainActivity != null) {
                switch (msg.what) {
                    case MSG_HIDE:
                        mainActivity.hide();
                        break;
                }
            }

            super.handleMessage(msg);
        }

        public void sendHideControllMessage() {
            obtainMessage(MSG_HIDE).sendToTarget();
        }
    }

    ;

    /**
     * 如果可见, 则无操作second秒之后隐藏.
     */
    private class CountTimeThread extends Thread {
        private final long maxVisibleTime;
        private long startVisibleTime;

        /**
         * @param second 设置按钮控件最大可见时间,单位是秒
         */
        public CountTimeThread(int second) {
            // 将时间换算成毫秒
            maxVisibleTime = second * 1000;

            // 设置为后台线程.
            setDaemon(true);
        }

        /**
         * 每当界面有操作时就需要重置mControllButtonLayout开始显示的时间,
         */
        public synchronized void reset() {
            startVisibleTime = System.currentTimeMillis();
        }

        public void run() {
            startVisibleTime = System.currentTimeMillis();

            while (true) {
                // 如果已经到达了最大显示时间, 则隐藏功能控件.
                if ((startVisibleTime + maxVisibleTime) < System.currentTimeMillis()) {
                    // 发送隐藏按钮控件消息.
                    mViewHandler.sendHideControllMessage();

                    startVisibleTime = System.currentTimeMillis();
                }

                try {
                    // 线程休眠1s.
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }


    @Override
    public void showError(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onChronometerTick(Chronometer chronometer) {
        isStartTime = true;
        mLong = SystemClock.elapsedRealtime() - chronometer.getBase();
        if (mLong > mRoomDix) {
            mIsBack = true;
            chronometer.stop();
            startFinishTimer();
            mButDismiss.setText("退出教室");
        }
        //真实数据不会 截取
        if (chronometer.getText().toString().length() > 8) {
            String substring = chronometer.getText().toString().substring(3);
            chronometer.setText(substring);
        }
    }

    /* $ $ $ $ $ $ $ $ $ $ $ $ $ $ $ $ $ $ $ $ $ $ $ $            白板         $ $ $ $ $ $ $ $ $ $ $ $ $ $ $ $ $ $ $ $ $ $ $ $ $ $ $ $ $ $ $ $ $ $ $ $ */
    @Override
    public void onStateChanged(int state, String addr) {

    }

    @Override
    public void onPenServiceError(String msg) {

    }

    @Override
    public void onPageInfo(int currentPage, int totalPage) {

    }

    @Override
    public void onPageNumberAndCategory(int pageNumber, int category) {

    }

    @Override
    public void onSupportPenPressureCheck(boolean flag) {

    }

    @Override
    public void onCheckPressureing() {

    }

    @Override
    public void onCheckPressurePen() {

    }

    @Override
    public void onCheckPressureFinish(int flag) {

    }

    @Override
    public void onCheckModuleUpdate() {

    }

    @Override
    public void onCheckModuleUpdateFinish(byte[] data) {

    }

    public boolean isScreenLanscape() {
        Configuration mConfiguration = this.getResources().getConfiguration(); //获取设置的配置信息
        int ori = mConfiguration.orientation; //获取屏幕方向
        if (ori == mConfiguration.ORIENTATION_LANDSCAPE) {
            return true;//横屏
        } else if (ori == mConfiguration.ORIENTATION_PORTRAIT) {
            return false;//竖屏
        }
        return false;
    }

    DeviceType mDeDeviceType = DeviceType.T7;//默认连接设备为P1 当与连接设备有冲突时则需要进行切换
    float mPenWeight = 2;//笔粗细
    int mPenColor = Color.BLACK;//笔颜色
    int isRubber;//是否是橡皮
    String mNoteKey = NoteEntity.KEY_NOTEKEY_TMP;

    @Override
    public DeviceType getDeviceType() {
        return mDeDeviceType;
    }

    @Override
    public float getPenWeight() {
        return mPenWeight;
    }

    @Override
    public int getPenColor() {
        return mPenColor;
    }

    @Override
    public float getIsRubber() {
        return isRubber;
    }

    @Override
    public boolean getIsPressure() {
        return true;
    }

    @Override
    public boolean getIsHorizontal() {
        return isScreenLanscape();
    }

    @Override
    public long getCurrUserId() {
        return 0;
    }

    @Override
    public String getNoteKey() {
        return mNoteKey;
    }

    @Override
    public String getNewNoteName() {
        return "123";
    }

    private boolean BOARD_AREA_COMPLETE;

    @Override
    public boolean onEvent(WhiteBoardView.BoardEvent boardEvent, Object o) {
        switch (boardEvent) {
            case BOARD_AREA_COMPLETE: //白板区域加载完成
                mWhiteBoardView.beginBlock();
                BOARD_AREA_COMPLETE = true;
                break;
            case ERROR_DEVICE_TYPE: //检测到连接设备更换
                break;
            case ERROR_SCENE_TYPE: //横竖屏更换
                break;
        }
        return true;
    }

    @Override
    public boolean onMessage(String s, Object o) {
        return false;
    }

    @Override
    public void onPageInfoUpdated(String s) {

    }

}
