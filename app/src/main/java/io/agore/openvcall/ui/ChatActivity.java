package io.agore.openvcall.ui;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.google.gson.Gson;
import com.onlyhiedu.mobile.App.App;
import com.onlyhiedu.mobile.Model.bean.AgoraUidBean;
import com.onlyhiedu.mobile.Model.bean.CourseList;
import com.onlyhiedu.mobile.Model.bean.CourseWareImageList;
import com.onlyhiedu.mobile.Model.bean.RoomInfo;
import com.onlyhiedu.mobile.Model.bean.board.BoardBean;
import com.onlyhiedu.mobile.Model.bean.board.LineBean;
import com.onlyhiedu.mobile.R;
import com.onlyhiedu.mobile.Service.NetworkStateService;
import com.onlyhiedu.mobile.Utils.DateUtil;
import com.onlyhiedu.mobile.Utils.DialogListener;
import com.onlyhiedu.mobile.Utils.DialogUtil;
import com.onlyhiedu.mobile.Utils.ImageLoader;
import com.onlyhiedu.mobile.Utils.JsonUtil;
import com.onlyhiedu.mobile.Utils.PhotoUtil;
import com.onlyhiedu.mobile.Utils.SPUtil;
import com.onlyhiedu.mobile.Utils.ScreenUtil;
import com.onlyhiedu.mobile.Utils.SnackBarUtils;
import com.onlyhiedu.mobile.Utils.SystemUtil;
import com.onlyhiedu.mobile.Widget.MyScrollView;
import com.onlyhiedu.mobile.Widget.TakePhotoPopWin;
import com.onlyhiedu.mobile.Widget.draw.DrawView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;
import io.agora.AgoraAPI;
import io.agora.AgoraAPIOnlySignal;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;
import io.agore.openvcall.model.AGEventHandler;
import io.agore.openvcall.model.ConstantApp;
import io.agore.openvcall.model.User;

import static com.onlyhiedu.mobile.Utils.Encrypt.md5hex;
import static io.agore.openvcall.ui.ChatPresenter.AfterJoin;
import static io.agore.openvcall.ui.ChatPresenter.PEN;

public class ChatActivity extends BaseActivity<ChatPresenter> implements AGEventHandler, ChatContract.View {

    private final HashMap<Integer, SoftReference<SurfaceView>> mUidsList = new HashMap<>(); // uid = 0 || uid == EngineConfig.mUid
    public static final String TAG = ChatActivity.class.getSimpleName();
    private String mUid;
    private int mScreenWidth;

    @BindView(R.id.ll_board)
    LinearLayout mLl_Board;


    @BindView(R.id.scrollView)
    MyScrollView mScrollView;

    @BindView(R.id.draw_view)
    DrawView mDrawView;
    @BindView(R.id.image_course_ware)
    ImageView mImageCourseWare;

    @BindView(R.id.toolbar)
    RelativeLayout mToolbar;
    @BindView(R.id.img_back)
    ImageView mImg_Back;
    @BindView(R.id.tv_total_room)
    TextView mTv_Total_Room;
    @BindView(R.id.image_full_screen)
    ImageView mImageFullScreen;
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
    //本地视频流
    @BindView(R.id.tv_video_local)
    TextView mTv_Video_Local;
    //本地音频流
    @BindView(R.id.tv_audio_local)
    TextView mTv_Audio_Local;

    //远端视频流
    @BindView(R.id.tv_video_mute)
    TextView mTv_Video_Mute;
    //远端音频流
    @BindView(R.id.tv_audio_mute)
    TextView mTv_Audio_Mute;
    //倒计时
    @BindView(R.id.tv_count)
    TextView mTv_Count;
    //提示
    @BindView(R.id.tv_point)
    TextView mTv_Point;
    //拍照上传
    @BindView(R.id.but_upload)
    Button mBut_Upload;
    //
    @BindView(R.id.constraint)
    ConstraintLayout mConstraintLayout;
    @BindView(R.id.switch_btn)
    Switch mDrawSwitch;

    private CountDownTimer timer;
    private final long INTERVAL = 1000L;
    private Timer mTimer = null;
    private TimerTask mTimerTask = null;
    private static final int UPDATE_TIME = 0;


    private String mChannelName;
    private RoomInfo mRoomInfo;
    public RequestManager mRequestManager;
    public List<CourseWareImageList> mCourseWareImageLists;//课件列表数据

    //课程id
    private String mUuid;
    private CourseList.ListBean mListBean;

    private String mRoomStartTime;
    private String mRoomEndTime;
    private String mEndtime1;
    private String mTime;
    private long mRoomDix;
    private long mLong;
    private boolean mIsBack; //返回键是否可点击
    private boolean isStartTime;
    private boolean isTeacherJoined;
    private boolean selfJoinChannelSuccess;
    private Gson mGson;

    int mLastDownY;
    private int mCurryY;

    private int mDelY;
    //向上滑动基数
    private int mUpValue = 3;
    private SurfaceView mStuSurfView;
    private SurfaceView mTeaSurfaceV;

    private final int MY_PERMISSIONS_REQUEST_CALL_PHONE2 = 2;

    // 拍照路径
    public static String SAVED_IMAGE_DIR_PATH = Environment.getExternalStorageDirectory()
            + "/Onlyhi/camera/";
    private String // 指定相机拍摄照片保存地址
            cameraPath = SAVED_IMAGE_DIR_PATH +
            System.currentTimeMillis() + ".png";
    private TakePhotoPopWin mTakePhotoPopWin;
    public final static int ALBUM_REQUEST_CODE = 1;
    public final static int CAMERA_REQUEST_CODE = 3;

    private File mFileOut;
    private Date mRoom_start;
    //标记 超过20分钟 一个人 调下课接口
    private boolean mUpdateEndTime;
    private Dialog mFinshDialog;
    private AgoraUidBean mAgoraUidBean;
    private boolean isPatriarchId;

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
        mDrawView.setMySchoolTime(System.currentTimeMillis());

        EventBus.getDefault().register(this);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        mScreenWidth = ScreenUtil.getScreenWidth(this);

        mRequestManager = Glide.with(this);

        float v = (float) mScreenWidth * (float) 0.7;
        rate = (float) mScreenWidth / v;
        mImageFullScreen.setEnabled(false);

        setToolBar();
        event().addEventHandler(this);
        //cRole: 1 主播 2 观众（默认）
        worker().configEngine(1,24);
        initRoomData();
        //登录信令系统成功后  登录通信频道
        initSignalling();
        mStuSurfView = RtcEngine.CreateRendererView(getApplicationContext());
        rtcEngine().setupLocalVideo(new VideoCanvas(mStuSurfView, VideoCanvas.RENDER_MODE_HIDDEN, 0));
        mStuSurfView.setZOrderOnTop(false);
        mStuSurfView.setZOrderMediaOverlay(false);
        mUidsList.put(0, new SoftReference<>(mStuSurfView)); // get first surface view
        worker().preview(true, mStuSurfView, Integer.parseInt(mUid));

        initMessageList();
        startCountTimeThread();

        mToolbar.animate().translationY(mToolbar.getHeight()).setInterpolator(new DecelerateInterpolator(2));
        visableTag = 1;

        mScrollView.setIntercept(false);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEventMain(LineBean event) {
        event.boardWidth = mPresenter.mCurrentBoardWidth;
        event.boardHeight = mPresenter.mCurrentBoardHeight;
        if (mGson == null) {
            mGson = new Gson();
        }

        String s = mGson.toJson(event);
        BoardBean boardBean = new BoardBean();
        boardBean.methodtype = "01";
        boardBean.methodparam = s;
        boardBean.scaling = "";
        mPresenter.add(event.drawMode, event.points, Color.BLACK, event.lineWidth, null);
        m_agoraAPI.messageChannelSend(mRoomInfo.getSignallingChannelId(), mGson.toJson(boardBean), "sendDrawLine");
//        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, NetworkStateService.class);
        startService(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        getSwipeBackLayout().setEnableGesture(false);
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
        //获取频道 id  老师uid 学生uid
        mRoomInfo = (RoomInfo) getIntent().getSerializableExtra("roomInfo");
        mListBean = (CourseList.ListBean) getIntent().getSerializableExtra("ListBean");
        mAgoraUidBean = (AgoraUidBean) getIntent().getSerializableExtra("agoraUidBean");
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
        String startTime = mListBean.getStartTime() + ":00";
        String courseDate = mListBean.getCourseDate();
        String endTime_start = mListBean.getEndTime() + ":00";
//        String startTime = "19:41:00";
//        String courseDate = "2017-11-23";
//        String endTime_start = "19:51:00";
        mTime = DateUtil.getTime(courseDate + " " + startTime);
        mEndtime1 = DateUtil.getTime(courseDate + " " + endTime_start);
        mRoomStartTime = DateUtil.getStrTime(mTime);
        mRoomEndTime = DateUtil.getStrTime(mEndtime1);
        String nowTime = DateUtil.formatDate(new Date(System.currentTimeMillis()), DateUtil.yyyyMMddHHmmss);
        DateFormat df = new SimpleDateFormat(DateUtil.yyyyMMddHHmmss);
        try {
            mRoom_start = df.parse(mRoomStartTime);
            Date room_end = df.parse(mRoomEndTime);
            mRoomDix = room_end.getTime() - mRoom_start.getTime();
            DateUtil.updateTimeFormat(mTv_Total_Room, (int) mRoomDix);
            Date now = df.parse(nowTime);
            long diff = mRoom_start.getTime() - now.getTime();
            Log.d(TAG, "diffs:" + diff / (1000 * 60));
            //没到开始时间
            if (diff > 0) {
                mIsBack = true;
                startTimer(mRoomDix);
            } else {
                //从迟到时间开始计时
                startCountTime(mRoomDix + diff);
            }
        } catch (Exception e) {
        }
    }

    private void startTimer(long diff) {
        if (mTimer == null) {
            mTimer = new Timer();
        }
        if (mTimerTask == null) {
            mTimerTask = new TimerTask() {
                @Override
                public void run() {
                    if (DateUtil.formatDate(new Date(System.currentTimeMillis()), DateUtil.yyyyMMddHHmmss).equals(mRoomStartTime)) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                startCountTime(diff);
                            }
                        });

                    }
                }
            };
        }
        if (mTimer != null && mTimerTask != null)
            mTimer.schedule(mTimerTask, 1000, 1000);

    }

    private void startCountTime(long diff) {
        mIsBack = false;
        isStartTime = true;
        mButDismiss.setText("我要下课");
        timer = new CountDownTimer(diff + 1050L, INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");//初始化Formatter的转换格式。
                formatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
                String hms = formatter.format(millisUntilFinished - 1000L);
                mTv_Count.setText(hms);
                if ("00:03:00".equals(mTv_Count.getText().toString())) {
                    initSoonDialog();
                }
                initAfterClass();
              /*  if ("00:00:10".equals(mTv_Count.getText().toString())) {
                    initSoonDialog();
                }*/
            }

            @Override
            public void onFinish() {
                Log.d(TAG, "停止计时");
                isStartTime = false;
                finishRoom();
            }

        }.start();
    }

    private void initAfterClass() {
        // 20分钟  课程开始后，只有一个人在教室内：
//                1.<20分钟：点击左下角下课按钮仅退出教室，还可以再次进入；
//                2.>=20分钟，点击左下角下课按钮，调下课接口，结束该次课程并退出教室。
//                3.=20分钟，弹窗提示：“老师繁忙，未能及时赶到，请联系您的班主任。”，点击【确定】按钮关闭弹窗。 一个人退出教室调接口 20*60
        if (!isTeacherJoined) {
            //课程时间开始 秒值
            //标记20分钟
            Long mTag = mRoom_start.getTime() / 1000 +20*60;
            if (System.currentTimeMillis() / 1000 >= mTag) {
                Log.d(TAG, "迟到超过20");
                mUpdateEndTime = true;
            }
            if (System.currentTimeMillis() / 1000 == mTag) {
                Log.d(TAG, "刚好迟到20");
                showRemindTeaDialog();
            }
            if (System.currentTimeMillis() / 1000 <mTag){
                //老师没有进来 小于20分钟
                Log.d(TAG, "老师没有进来 小于20");
                mButDismiss.setText("退出教室");
            }

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

    private void showRemindTeaDialog() {
        DialogUtil.showOnlyAlert(this,
                "提示"
                , "老师繁忙，未能及时赶到，请联系您的班主任。"
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
        quitCall();
    }

    private void setToolBar() {
        mToolbar.setBackgroundResource(R.color.c33);
        mToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

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
            m_agoraAPI.channelLeave(mRoomInfo.getSignallingChannelId());
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
        m_agoraAPI.login2(appId, account, token, 0, "", 30, 3);
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
            public void onChannelLeaved(String channelID, int ecode) {
                super.onChannelLeaved(channelID, ecode);
                Log.d(TAG, "离开信令频道：" + channelID);
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
                    SnackBarUtils.show(mDrawView, "老师已退出课堂", Color.GREEN);
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

                                    DialogUtil.showOnlyAlert(ChatActivity.this,
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
                                    DialogUtil.showOnlyAlert(ChatActivity.this,
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
                        if (mListBean.channelTeacherId != Integer.parseInt(account)) {
                            return;
                        }

                        BoardBean boardBean = JsonUtil.parseJson(msg, BoardBean.class);
                        if (boardBean == null) {
                            return;
                        }
                        switch (boardBean.methodtype) {
                            case AfterJoin:
                                mImageCourseWare.setImageResource(R.drawable.transparent);
                                long l = System.currentTimeMillis();
                                mPresenter.AfterJoin(boardBean.methodparam, ChatActivity.this, mDrawView);
                                Log.d("xwc", System.currentTimeMillis() - l + "");
                                break;
                            case PEN:
                                mPresenter.draw2(boardBean.methodparam, mDrawView, ChatActivity.this);
                                break;
                            case "08"://清屏
                                mPresenter.cleanDrawData(mDrawView);
                                break;
                            case "09"://选择课件
                                mPresenter.cleanDrawData(mDrawView);
                                mPresenter.getCourseWareImageList(boardBean.methodparam, 0, true);
                                break;
                            case "10": //翻页
                                mPresenter.changePage(ChatActivity.this, mDrawView, mImageCourseWare, boardBean.methodparam);
                                break;
                            case "12":
                                mPresenter.initBoard(boardBean.methodparam, mDrawView, ChatActivity.this);
                                break;
                            case "IM":
                                notifyMessageChanged(new io.agore.openvcall.model.Message(new User(mListBean.channelTeacherId, boardBean.scaling), boardBean.methodparam));
                                break;
                            case "15":
                                mToolbar.animate().translationY(mToolbar.getHeight()).setInterpolator(new DecelerateInterpolator(2));
                                visableTag = 1;
                                Toast.makeText(ChatActivity.this, "老师允许了画板操作", Toast.LENGTH_SHORT).show();
                                mDrawSwitch.setVisibility(View.VISIBLE);
                                break;
                            case "16":
                                mToolbar.animate().translationY(mToolbar.getHeight()).setInterpolator(new DecelerateInterpolator(2));
                                visableTag = 1;
                                Toast.makeText(
                                        ChatActivity.this, "老师禁止了画板操作", Toast.LENGTH_SHORT).show();
                                mDrawSwitch.setChecked(false);
                                mScrollView.setIntercept(false);
                                mDrawSwitch.setVisibility(View.GONE);
                                break;
                        }
                    }
                });

            }

            //指令发送成功回调
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
                    if (mFinshDialog==null) {
                        mFinshDialog = DialogUtil.showProgressDialog(ChatActivity.this, "正在退出房间...", true, true);
                    }
                    mPresenter.getStopRecord(mListBean.courseUuid);
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
                if (!selfJoinChannelSuccess) {
                    finish();
                }
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
                            DialogUtil.showOnlyAlert(ChatActivity.this,
                                    "提示"
                                    , "该账号已经在别处登录。"
                                    , "知道了"
                                    , ""
                                    , false, false, new DialogListener() {
                                        @Override
                                        public void onPositive(DialogInterface dialog) {
                                            if (mFinshDialog==null) {
                                                mFinshDialog = DialogUtil.showProgressDialog(ChatActivity.this, "正在退出房间...", true, true);
                                            }
                                            mPresenter.getStopRecord(mListBean.courseUuid);
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

            @Override
            public void onReconnecting(int nretry) {
                super.onReconnecting(nretry);
                Log.d(TAG, "onReconnecting"+nretry);
            }

            @Override
            public void onReconnected(int fd) {
                super.onReconnected(fd);
                Log.d(TAG, "onReconnected"+fd);
            }

            //加入频道后 回调
            @Override
            public void onChannelJoined(String chanID) {
                Log.d(TAG, "Join channel " + chanID + " successfully"); // + " docall " + doCall);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        initRoom();
                    }
                });
            }

            //当有用户加入频道触发此回调。
            @Override
            public void onChannelUserJoined(String account, int uid) {
                if (account.equals(String.valueOf(mListBean.channelTeacherId))) {
                    Log.d(TAG, "信令频道其他用户加入：" + account);
                    initRoom();
                }

                if (account.equals(String.valueOf(mListBean.channelPatriarchId))) {
                    isPatriarchId=true;
                }

            }

            @Override
            public void onChannelQueryUserNumResult(String channelID, int ecode, int num) {
                Log.d(TAG, "查询的用户数量num:" + num);
            }

            //信令频道list  有老师 再加入老师
            @Override
            public void onChannelUserList(String[] accounts, int[] uids) {
                super.onChannelUserList(accounts, uids);
                Log.d(TAG, "查询的用户数量num:" + accounts.length);
                List<String> strings = Arrays.asList(accounts);
                if (strings.contains(String.valueOf(mListBean.channelTeacherId))||strings.contains(String.valueOf(mListBean.channelPatriarchId))) {
                    Log.d(TAG, "信令频道有老师加入:" + mListBean.channelTeacherId);
                    initRoom();
                }
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
                        if (mFinshDialog==null) {
                            mFinshDialog = DialogUtil.showProgressDialog(ChatActivity.this, "正在退出房间...", true, true);
                        }
                        mPresenter.getStopRecord(mListBean.courseUuid);
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

    @Override
    public void showCourseWareImageList(List<CourseWareImageList> data, int pageNum, boolean restart) {

        if (mPresenter.mBoardWidth > data.get(0).width) {  //PC白板宽度如果大于课件图片宽度
            setBoardViewLayoutParams(data.get(0).width, data.get(0).height);
        } else {
            double v = (double) mPresenter.mBoardWidth / (double) data.get(0).width;
            double height = v * (double) data.get(0).height;

            setBoardViewLayoutParams(mPresenter.mBoardWidth, (int) (height + 0.5));
        }


        if (data != null && data.size() > 0) {
            mCourseWareImageLists = data;
            if (restart) {
                mDrawView.restartDrawing();
            }
            ImageLoader.loadImage(mRequestManager, mImageCourseWare, data.get(pageNum).imageUrl);
        } else {
            Toast.makeText(this, "课件加载失败", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showUpdateEndTime(String msg) {
        if ("成功".equals(msg)) {
            Log.d(TAG, "下课接口 拉取成功");
            if (mFinshDialog==null) {
                mFinshDialog = DialogUtil.showProgressDialog(this, "正在退出房间...", true, true);
            }
            mPresenter.getStopRecord(mListBean.courseUuid);
        }
    }

    @Override
    public void showStopRecord(String msg) {
        if (mFinshDialog!=null) {
            DialogUtil.dismiss(mFinshDialog);
        }
        if ("成功".equals(msg)) {
            finishClassRoom();
        }
    }

    private FrameLayout.LayoutParams mDrawViewP;

    private FrameLayout.LayoutParams mDrawViewFullP;

    private float rate;      //缩放比例
    private boolean mSwitch; //全屏半屏  true 全屏，false半屏

    @OnClick({R.id.img_back, R.id.but_dismiss, R.id.image_full_screen, R.id.but_im, R.id.switch_btn, R.id.tv_send, R.id.tv_video_local, R.id.tv_video_mute, R.id.tv_audio_local, R.id.tv_audio_mute, R.id.but_upload})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.but_dismiss:
                canFinshClass();
                break;
            case R.id.image_full_screen:
                if (visableTag == 1 && mToolbar != null) {
                    visableTag = 0;
                    mToolbar.animate().translationY(-mToolbar.getHeight()).setInterpolator(new AccelerateInterpolator(2));
                }
                setBoardViewLayoutParams(mSwitch);
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
            case R.id.switch_btn:
                if (mScrollView.isIntercept()) {
                    mScrollView.setIntercept(false);
                } else {
                    mScrollView.setIntercept(true);
                }
                break;

            case R.id.tv_video_local:
                //本地视频关闭
                initLocalVideo();
                break;
            case R.id.tv_audio_local:
                //本地音频关闭
                initLocalAudio();
                break;
            case R.id.tv_video_mute:
                //远端视频
                initMuteVideo();
                break;
            case R.id.tv_audio_mute:
                //远端音频
                initMuteAudio();
                break;

            case R.id.but_upload:
                //拍照上传
                upLoadHomeWork();
                break;
            case R.id.img_back:
            /*    if (mUpdateEndTime) {
                    initUpdateEndTime();
                    mPresenter.getUpdateEndTime(mListBean.courseUuid);
                } else {
                    finishClassRoom();
//                    Toast.makeText(mContext, "课程还未结束,可点击我要下课", Toast.LENGTH_SHORT).show();
                }*/
                break;
        }
    }



    private void upLoadHomeWork() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                    MY_PERMISSIONS_REQUEST_CALL_PHONE2);

        } else {
            uploadHeadPhoto();
        }

    }

    private void uploadHeadPhoto() {
        mTakePhotoPopWin = new TakePhotoPopWin(this, onClickListener);
        //showAtLocation(View parent, int gravity, int x, int y)
        mTakePhotoPopWin.showAtLocation(mConstraintLayout, Gravity.CENTER, 0, 0);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.btn_take_photo:
                    rtcEngine().enableLocalVideo(false);
                    rtcEngine().muteLocalVideoStream(true);
                    //从拍照
                    String state = Environment.getExternalStorageState();
                    if (state.equals(Environment.MEDIA_MOUNTED)) {
                        Intent intent = new Intent();
                        // 指定开启系统相机的Action
                        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                        String out_file_path = SAVED_IMAGE_DIR_PATH;
                        File dir = new File(out_file_path);
                        if (!dir.exists()) {
                            dir.mkdirs();
                        }
                        // 把文件地址转换成Uri格式
//                        Uri uri = Uri.fromFile(new File(cameraPath));
                        Uri uri;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                            uri = FileProvider.getUriForFile(ChatActivity.this,
                                    "com.onlyhiedu.mobile.fileprovider",
                                    new File(cameraPath));
                            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        } else {
                            uri = Uri.fromFile(new File(cameraPath));
                        }
                        // 设置系统相机拍摄照片完成后图片文件的存放地址
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                        startActivityForResult(intent, CAMERA_REQUEST_CODE);
                    } else {
                        Toast.makeText(ChatActivity.this, "请确认已经插入SD卡",
                                Toast.LENGTH_LONG).show();
                    }
                    mTakePhotoPopWin.dismiss();
                    break;
                case R.id.btn_pick_photo:
                    //从系统相册
                    Intent intent = new Intent(Intent.ACTION_PICK, null);
                    intent.setDataAndType(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent, ALBUM_REQUEST_CODE);
                    mTakePhotoPopWin.dismiss();
                    break;
            }
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL_PHONE2:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    uploadHeadPhoto();
                } else {
                    // Permission Denied
                    DialogUtil.showPresimissFialDialog(this, "SD卡存储");
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CAMERA_REQUEST_CODE:
                rtcEngine().enableLocalVideo(true);
                rtcEngine().muteLocalVideoStream(false);
//                Drawable fromPath = BitmapDrawable.createFromPath(cameraPath);
//                mImageCourseWare.setImageDrawable(fromPath);

                break;
            case ALBUM_REQUEST_CODE:
                try {
                    Uri uri = data.getData();
                    String absolutePath =
                            PhotoUtil.getAbsolutePath(this, uri);
                    Log.d(TAG, "path=" + absolutePath);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }

    }

    private void initMuteAudio() {
        if ("音频关闭".equals(mTv_Audio_Mute.getText().toString())) {
            int i = rtcEngine().muteRemoteAudioStream(mListBean.channelTeacherId, true);
            if (i == 0) {
                mTv_Audio_Mute.setText("音频打开");
            }
        } else {
            int i = rtcEngine().muteRemoteAudioStream(mListBean.channelTeacherId, false);
            if (i == 0) {
                mTv_Audio_Mute.setText("音频关闭");
            }
        }
    }

    private void initMuteVideo() {
        if ("视频关闭".equals(mTv_Video_Mute.getText().toString())) {
            int i = rtcEngine().muteRemoteVideoStream(mListBean.channelTeacherId, true);
            if (i == 0) {
                mTv_Video_Mute.setText("视频打开");
                mRel_Tea.removeAllViews();
            }
        } else {
            int i = rtcEngine().muteRemoteVideoStream(mListBean.channelTeacherId, false);
            if (i == 0 && mTeaSurfaceV != null) {
                mTv_Video_Mute.setText("视频关闭");
                mRel_Tea.addView(mTeaSurfaceV);
            }
        }
    }

    private void initLocalAudio() {
        if ("音频关闭".equals(mTv_Audio_Local.getText().toString())) {
            int i = rtcEngine().muteLocalAudioStream(true);
            if (i == 0) {
                mTv_Audio_Local.setText("音频打开");
            }
        } else {
            int i = rtcEngine().muteLocalAudioStream(false);
            if (i == 0) {
                mTv_Audio_Local.setText("音频关闭");
            }
        }
    }

    private void initLocalVideo() {
        if ("视频关闭".equals(mTv_Video_Local.getText().toString())) {
            int i = rtcEngine().muteLocalVideoStream(true);
            int i1 = rtcEngine().enableLocalVideo(false);
            if (i == 0 && i1 == 0) {
                mRel_Stu.removeAllViews();
                mTv_Video_Local.setText("视频打开");
            } else {
                rtcEngine().muteLocalVideoStream(false);
                rtcEngine().enableLocalVideo(true);
                mTv_Video_Local.setText("视频关闭");
            }
        } else {
            int i = rtcEngine().muteLocalVideoStream(false);
            int i1 = rtcEngine().enableLocalVideo(true);
            if (i == 0 && i1 == 0) {
                mRel_Stu.addView(mStuSurfView);
                mTv_Video_Local.setText("视频关闭");
            }
        }
    }

    private void canFinshClass() {
//        mPresenter.getUpdateEndTime();
        if ("我要下课".equals(mButDismiss.getText().toString()) && isTeacherJoined) {
            requestFinishClass();
        } else {
            showUpdateEndTimeDialog(mButDismiss.getText().toString());
        }
      /*  if (isStartTime && isTeacherJoined && (mIsBack == false)) {
            //学生点击我要下课
            requestFinishClass();
        } else {
            //没有开始上课计时      老师没有进入教室         当文案现实 退出教室时
            //isStartTime ==false isTeacherJoined==false mIsBack=true
            initFinishClassDialog();
        }*/
    }

    private void showUpdateEndTimeDialog(String msg) {
        if (msg.equals("我要下课")) {
            msg = "确定结束本次课程并离开教室？";
        } else {
            msg = "确定离开教室？";
        }
        DialogUtil.showOnlyAlert(this,
                "提示"
                , msg
                , "确认"
                , ""
                , true, true, new DialogListener() {
                    @Override
                    public void onPositive(DialogInterface dialog) {
                        Log.d(TAG, "拉取下课接口");
                        //正式用的接口
//                         mPresenter.getUpdateEndTime(mListBean.courseUuid);

                        //测试用的接口
                        if (mFinshDialog==null) {
                            mFinshDialog = DialogUtil.showProgressDialog(ChatActivity.this, "正在退出房间...", true, true);
                        }
                        mPresenter.getStopRecord(mListBean.courseUuid);
                    }

                    @Override
                    public void onNegative(DialogInterface dialog) {
                        Log.d(TAG, "onNegative:");
                    }
                }
        );
    }


    private void hindIMLayout() {
        mLlMsg.setVisibility(View.VISIBLE);
        mTvIM.setBackgroundResource(R.drawable.im_text_bg2);
        SystemUtil.hideKeyboard(mTvIM, this);
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


    private void sendChannelMsg(String msgStr) {

        BoardBean boardBean = new BoardBean();
        boardBean.methodtype = "IM";
        boardBean.methodparam = msgStr;
        boardBean.scaling = mUid;
        if (mGson == null) {
            mGson = new Gson();
        }
        m_agoraAPI.messageChannelSend(mRoomInfo.getSignallingChannelId(), mGson.toJson(boardBean), "sendIM");
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
        }

    }

    /**
     * 设置白板相关控件LayoutParams
     *
     * @param flag
     */
    public void setBoardViewLayoutParams(boolean flag) {
        ConstraintLayout.LayoutParams constraint = new ConstraintLayout.LayoutParams(0, 0);
        if (flag) {
            mRel_Stu.setVisibility(View.VISIBLE);
            mRel_Tea.setVisibility(View.VISIBLE);
          /*  mTv_Video_Local.setVisibility(View.VISIBLE);
            mTv_Video_Mute.setVisibility(View.VISIBLE);
            mTv_Audio_Local.setVisibility(View.VISIBLE);
            mTv_Audio_Mute.setVisibility(View.VISIBLE);*/
  /*    app:layout_constraintLeft_toLeftOf="@+id/guideline_v1"
        app:layout_constraintRight_toLeftOf="@+id/guideline_v2"
        app:layout_constraintBottom_toBottomOf="@+id/guideline_h1"
        app:layout_constraintTop_toBottomOf="@+id/guideline_h0"*/
            constraint.leftToRight = R.id.guideline_v1;
            constraint.rightToLeft = R.id.guideline_v2;
            constraint.bottomToBottom = R.id.guideline_h1;
            constraint.topToBottom = R.id.guideline_h0;
            mLl_Board.setLayoutParams(constraint);
            mSwitch = false;
            mPresenter.setFullScreen(mSwitch);

            mDrawView.restartDrawing();
            mDrawView.setLayoutParams(mDrawViewP);
            mImageCourseWare.setLayoutParams(mDrawViewP);
            mDrawView.setEraserSize(12 * mPresenter.getScreenRate());
            mPresenter.reDraw(mDrawView);

            mImageFullScreen.setImageResource(R.mipmap.ic_full_screen);

        } else {
            mRel_Stu.setVisibility(View.GONE);
            mRel_Tea.setVisibility(View.GONE);
            mTv_Video_Local.setVisibility(View.GONE);
            mTv_Video_Mute.setVisibility(View.GONE);
            mTv_Audio_Local.setVisibility(View.GONE);
            mTv_Audio_Mute.setVisibility(View.GONE);
            constraint.rightToLeft = R.id.guideline_v2;
            constraint.bottomToBottom = R.id.guideline_h1;
            constraint.topToBottom = R.id.guideline_h0;
            constraint.leftToRight = R.id.guideline_v;
            mLl_Board.setLayoutParams(constraint);
            mSwitch = true;
            mPresenter.setFullScreen(mSwitch);

            mDrawView.restartDrawing();
            mDrawView.setLayoutParams(mDrawViewFullP);
            mImageCourseWare.setLayoutParams(mDrawViewFullP);
            mDrawView.setEraserSize(12 * mPresenter.getScreenRate());
            mPresenter.reDraw(mDrawView);

            mImageFullScreen.setImageResource(R.mipmap.ic_full_screen2);
        }

    }


    public void setBoardViewLayoutParams(int width, int height) {

        ConstraintLayout.LayoutParams constraint = new ConstraintLayout.LayoutParams(0, 0);
        constraint.leftToRight = R.id.guideline_v1;
        constraint.rightToLeft = R.id.guideline_v2;
        constraint.bottomToBottom = R.id.guideline_h1;
        constraint.topToBottom = R.id.guideline_h0;
        mLl_Board.setLayoutParams(constraint);
        int imageWidth = (int) (mScreenWidth * 0.7);
        float rate = (float) imageWidth / Float.valueOf(width);
        mPresenter.setHalfScreenRate(rate);
        int halfImageHeight = (int) (Float.valueOf(height) * rate);
        mDrawViewP = new FrameLayout.LayoutParams(imageWidth, halfImageHeight);
        if (!mSwitch) {
            mImageCourseWare.setLayoutParams(mDrawViewP);
            mDrawView.setLayoutParams(mDrawViewP);
        }
        float rates = (float) mScreenWidth / Float.valueOf(width);
        mPresenter.setFullScreenRate(rates);
        int FullImageHeight = (int) (Float.valueOf(height) * rates);
        mDrawViewFullP = new FrameLayout.LayoutParams(mScreenWidth, FullImageHeight);
        if (mSwitch) {
            mImageCourseWare.setLayoutParams(mDrawViewFullP);
            mDrawView.setLayoutParams(mDrawViewFullP);
        }
        mDrawView.setEraserSize(mSwitch ? 12 * rates : 12 * rate);

        mImageFullScreen.setEnabled(true);
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
        String sign = md5hex((account + appID + certificate + expiredTime).getBytes());
        return "1:" + appID + ":" + expiredTime + ":" + sign;
    }

    @Override
    protected void deInitUIandEvent() {
        doLeaveChannel();
    }

    private void doLeaveChannel() {
        if (worker() != null) {
            worker().leaveChannel(mChannelName);
            worker().preview(false, null, 0);
        }

    }

    @Override
    public void onBackPressedSupport() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (m_agoraAPI != null) {
            m_agoraAPI.channelLeave(mRoomInfo.getSignallingChannelId());
            m_agoraAPI.logout();
            Log.d(TAG, "信令退出");
        }
        if (mUidsList != null) {
            mUidsList.clear();
        }
        isStartTime = false;
        EventBus.getDefault().unregister(this);
    }

    private void quitCall() {
        deInitUIandEvent();
    }

    //远端 限定 只显示老师
    @Override
    public void onFirstRemoteVideoDecoded(int uid, int width, int height, int elapsed) {
        if (uid == mListBean.channelTeacherId||uid == Integer.parseInt(mListBean.channelPatriarchId)) {
            initTeaView(uid);
        }
        /*if (uid == Integer.parseInt(mListBean.channelPatriarchId)) {
            initTeaView(uid);
        }*/
    }

    //其他用户停止/重启视频回调
    @Override
    public void onUserMuteVideo(int uid, boolean muted) {
        if (uid == mListBean.channelTeacherId) {
            if (muted) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ChatActivity.this, "老师关闭视频", Toast.LENGTH_SHORT).show();
                        mRel_Tea.removeAllViews();
                    }
                });
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ChatActivity.this, "老师打开视频", Toast.LENGTH_SHORT).show();
                        if (mTeaSurfaceV != null) {
                            mRel_Tea.addView(mTeaSurfaceV);
                        }
                    }
                });
            }
        }
    }

    @Override
    public void onUserMuteAudio(int uid, boolean muted) {
        if (uid == mListBean.channelTeacherId) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(ChatActivity.this, "老师" + (muted ? "静音" : "取消静音"), Toast.LENGTH_SHORT).show();
                }
            });
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
                mTeaSurfaceV = RtcEngine.CreateRendererView(getApplicationContext());
                mUidsList.put(uid, new SoftReference<>(mTeaSurfaceV));
                Log.d(TAG, "远端下 集合长度：" + mUidsList.size());
                mTeaSurfaceV.setZOrderOnTop(false);
                mTeaSurfaceV.setZOrderMediaOverlay(false);
                //设置远端视频显示属性 (setupRemoteVideo)
                rtcEngine().setupRemoteVideo(new VideoCanvas(mTeaSurfaceV, VideoCanvas.RENDER_MODE_HIDDEN, uid));
                mRel_Tea.addView(mTeaSurfaceV);
            }
        });
    }

    @Override
    public void onJoinChannelSuccess(String channel, final int uid, int elapsed) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "加入成功后uid: " + uid);
                selfJoinChannelSuccess = true;
                mTv_Point.setVisibility(View.GONE);
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

    //客户端可能会和服务器失去连接，SDK会进行自动重连，自动重连成功后触发此回调方法
    @Override
    public void onRejoinChannelSuccess(String channel, int uid, int elapsed) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (mRel_Tea != null && mRel_Stu != null && mTeaSurfaceV != null && mStuSurfView != null) {
                    if (uid == mListBean.channelTeacherId || uid == Integer.parseInt(mUid)) {
                        rtcEngine().muteLocalAudioStream(false);
                        rtcEngine().enableLocalVideo(true);
                        rtcEngine().muteLocalVideoStream(false);
                        rtcEngine().muteRemoteVideoStream(mListBean.channelTeacherId, false);
                        mRel_Tea.removeAllViews();
                        mRel_Stu.removeAllViews();
                        mRel_Stu.addView(mStuSurfView);
                        mRel_Tea.addView(mTeaSurfaceV);
                    }
                }

            }
        });
    }

    //其他用户加入当前频道回调
    @Override
    public void onUserJoined(int uid, int elapsed) {
        Log.d(TAG, "uid:onUserJoined " + uid);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (uid == mListBean.channelTeacherId) {
                    isTeacherJoined = true;
                    mButDismiss.setText("我要下课");
//                    mDrawView.restartDrawing();
//                    mImageCourseWare.setImageResource(R.drawable.transparent);
                    int i = rtcEngine().muteLocalVideoStream(false);
                    rtcEngine().muteLocalAudioStream(false);
                    int i1 = rtcEngine().enableLocalVideo(true);
                    if (mRel_Stu != null && mStuSurfView != null) {
                        mRel_Stu.removeAllViews();
                        mRel_Stu.addView(mStuSurfView);
                    }
                }
                if (uid == mListBean.channelTeacherId) {
                    initTeaView(uid);
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
                //当有其他用户退出
                if (uid == mListBean.channelTeacherId) {
                    isTeacherJoined = false;
                    SnackBarUtils.show(mDrawView, "老师已退出课堂", Color.GREEN);
                }
            }
        });
        removeRemoteUi(uid);
        if (uid==Integer.parseInt(mListBean.channelPatriarchId)) {
            isPatriarchId=false;
        }
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
                    if (mListBean != null && mPresenter != null) {
                        mUuid = mListBean.getUuid();
                        mPresenter.uploadStatistics(String.valueOf(f1), mUuid);
                        event().removeEventHandler(ChatActivity.this);
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


    private void removeRemoteUi(final int uid) {
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
                if (mRel_Tea != null && mRoomInfo != null && mListBean.channelTeacherId == uid || Integer.parseInt(mListBean.channelPatriarchId)==uid) {
                    mRel_Tea.removeAllViews();
                }
                //将本地 音视频关闭 移除view
               /* int i = rtcEngine().muteLocalVideoStream(true);
                rtcEngine().muteLocalAudioStream(true);
                int i1 = rtcEngine().enableLocalVideo(false);
                if (i == 0 && i1 == 0) {
                    mRel_Stu.removeAllViews();
                }*/
            }
        });
    }


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

    private MainHandler mViewHandler = new MainHandler(ChatActivity.this);

    private static class MainHandler extends Handler {
        /**
         * 隐藏按钮消息id
         */
        private final int MSG_HIDE = 0x0001;

        private WeakReference<ChatActivity> weakRef;

        public MainHandler(ChatActivity activity) {
            weakRef = new WeakReference<ChatActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final ChatActivity mainActivity = weakRef.get();
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

}
