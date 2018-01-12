package com.onlyhiedu.pro.UI.Home.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.Gson;
import com.onlyhiedu.pro.IM.fragment.ContactsFragment;
import com.netease.nim.uikit.business.recent.RecentContactsFragment;
import com.netease.nim.uikit.common.fragment.TFragment;
import com.onlyhiedu.pro.App.App;
import com.onlyhiedu.pro.App.AppManager;
import com.onlyhiedu.pro.Base.VersionUpdateActivity;
import com.onlyhiedu.pro.Model.bean.socket.LoginRequest;
import com.onlyhiedu.pro.Model.bean.socket.LoginResponse;
import com.onlyhiedu.pro.Model.event.MainActivityShowGuest;
import com.onlyhiedu.pro.Model.event.MainActivityTabSelectPos;
import com.onlyhiedu.pro.Model.event.NightModeEvent;
import com.onlyhiedu.pro.Model.http.onlyApis;
import com.onlyhiedu.pro.R;
import com.onlyhiedu.pro.UI.Home.fragment.ClassFragment;
import com.onlyhiedu.pro.UI.Home.fragment.HomeFragment;
import com.onlyhiedu.pro.UI.Home.fragment.MeFragment;
import com.onlyhiedu.pro.Utils.SPUtil;
import com.onlyhiedu.pro.Utils.SystemUtil;
import com.onlyhiedu.pro.Utils.UIUtils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareAPI;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class MainActivity extends VersionUpdateActivity implements BottomNavigationView.OnNavigationItemSelectedListener {


    public static final String TAG = MainActivity.class.getSimpleName();
    public static final int CALL_REQUEST_CODE = 110;
    public static final String showPagePosition = "showPagePosition";
    private ClassFragment mClassFragment;
    private HomeFragment mHomeFragment;
    private MeFragment mMeFragment;
    //    private SmallClassFragment mSmallClassFragment;
    private RecentContactsFragment mSessionListFragment;
    private ContactsFragment mContactListFragment;


    public boolean isConflict = false;
    private boolean isCurrentAccountRemoved = false;
    private long mExitTime = 0;

    @BindView(R.id.navigation)
    BottomNavigationView mNavigation;
    private String oldPhone; //上次登录的账号
    private Gson mGson;

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        oldPhone = SPUtil.getPhone();
        EventBus.getDefault().register(this);
        mGson = new Gson();

        if (!SPUtil.getGuest()) {
            checkHeartbeat();
        }

    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 3:
                    Toast.makeText(MainActivity.this, "您的账号以在别处登录", Toast.LENGTH_SHORT).show();
                    UIUtils.startLoginActivity(MainActivity.this);
                    break;
                case 2:
                    break;
            }
        }
    };

    /**
     * 心跳
     */
    private Socket mSocket;

    private boolean noNetwork; //没有网络
    private boolean sendData; //发送过数据

    private void heartbeat(final boolean isConnect) {

        new Thread() {

            private OutputStream outputStream;

            public void run() {
                try {

                    if (!isConnect) { //没有连接过
                        mSocket = new Socket();
                        mSocket.connect(new InetSocketAddress(onlyApis.IP, 30000), 2000);
                        outputStream = mSocket.getOutputStream();
                        String s = mGson.toJson(new LoginRequest(oldPhone + "student", 2, SPUtil.getToken()));
                        outputStream.write(s.getBytes());
                        outputStream.flush();
                        sendData = true;
                    }

                    while (true) {
                        if (!SystemUtil.isNetworkConnected()) {
                            mSocket.isClosed();
                            noNetwork = true;
                        } else {
                            if (noNetwork) {
                                noNetwork = false;
                                mSocket = new Socket();
                                //101.132.116.184
                                mSocket.connect(new InetSocketAddress(onlyApis.IP, 30000), 2000);

                                outputStream = mSocket.getOutputStream();
                                String s = "";
                                if (sendData) {
                                    s = mGson.toJson(new LoginRequest(oldPhone + "student", 3, SPUtil.getToken()));
                                } else {
                                    s = mGson.toJson(new LoginRequest(oldPhone + "student", 2, SPUtil.getToken()));
                                    sendData = true;
                                }
                                Log.d("socket", s);
                                outputStream.write(s.getBytes());
                                outputStream.flush();
                            }
                        }
                        // 接受服务器的信息
                        if (!mSocket.isClosed() && mSocket.isConnected()) {

                            Log.d("socket", "进来了");
                            InputStream input = mSocket.getInputStream();
                            byte[] buffer = new byte[input.available()];
                            input.read(buffer);
                            String responseInfo = new String(buffer);
                            LoginResponse json = null;
                            if (!TextUtils.isEmpty(responseInfo)) {
                                json = mGson.fromJson(responseInfo, LoginResponse.class);
                            }
                            if (json != null) {
                                Message message = new Message();
                                message.what = json.reply;
                                handler.sendMessage(message);
                                Log.d("socket_reply", json.reply + "");
                                if (json.reply == 3) {
                                    mSocket.close();
                                    sendData = false;
                                    return;
                                }

                            }
                        }
                        SystemClock.sleep(1000);
                    }
                } catch (UnknownHostException e) {
                    Log.d("socket_Exception_Msg", e.getMessage());
                    e.printStackTrace();
                    SystemClock.sleep(2000);

                } catch (IOException e) {
                    Log.d("socket_Exception_Msg", e.getMessage());
                    if (e instanceof SocketException) {
                        SystemClock.sleep(2000);
                        heartbeat(true);
                    } else if (e instanceof ConnectException) {
                        Log.d("socket_Exception_Msg", "网络异常");
                        SystemClock.sleep(2000);
                        heartbeat(true);
                    }
                }
            }
        }.start();
    }

    private void checkHeartbeat() {
        if (mSocket == null || !mSocket.isConnected() || mSocket.isClosed()) { //还没有连接过socket，直接连接
            Log.d("socket", "还没有连接过socket，直接连接");
            heartbeat(false);
            oldPhone = SPUtil.getPhone();
            return;
        }

        if (!SPUtil.getPhone().equals(oldPhone)) { //换不同的账号需要重新连接
            try {
                oldPhone = SPUtil.getPhone();
                if (!mSocket.isClosed()) {
                    mSocket.close();
                }
                Log.d("socket", "换不同的账号重新连接");
                heartbeat(false);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        if (SPUtil.getPhone().equals(oldPhone)) { //如果账号相等的话不需要重连
            Log.d("socket", "和上次登录账号相同");
            if (mSocket.isClosed()) {
                heartbeat(false);
            }
        }
    }


    @Override
    protected void initView() {
        mClassFragment = new ClassFragment();
        mMeFragment = new MeFragment();
        mHomeFragment = new HomeFragment();
//        mSmallClassFragment = new SmallClassFragment();
        mSessionListFragment = new RecentContactsFragment();
        mContactListFragment = new ContactsFragment();
        BottomNavigationViewHelper.disableShiftMode(mNavigation);

        //不隐藏首页
        if (App.getInstance().isTag) {
            mNavigation.setSelectedItemId(R.id.tow);
            loadMultipleRootFragment(R.id.fl_main_content, 1, mHomeFragment, mSessionListFragment, mContactListFragment, mClassFragment, mMeFragment);

            App.getInstance().isTag = false;
        } else {
            loadMultipleRootFragment(R.id.fl_main_content, 0, mHomeFragment,  mSessionListFragment, mContactListFragment, mClassFragment, mMeFragment);

        }
        mNavigation.setOnNavigationItemSelectedListener(this);
        mNavigation.setItemIconTintList(null);

    }


    @Override
    protected void initData() {
        super.initData();
        mPresenter.updateVersion(true);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.one) {
            showHideFragment(mHomeFragment);
        }
        if (item.getItemId() == R.id.tow) {
            if (SPUtil.getGuest()) {
                UIUtils.startGuestLoginActivity(this, 1);

                return false;
            } else showHideFragment(mSessionListFragment);
        }
        if (item.getItemId() == R.id.thr) {
            if (SPUtil.getGuest()) {
                UIUtils.startGuestLoginActivity(this, 1);

                return false;
            } else  showHideFragment(mContactListFragment);
        }

        if (item.getItemId() == R.id.four) {
            if (SPUtil.getGuest()) {
                UIUtils.startGuestLoginActivity(this, 1);

                return false;
            } else showHideFragment(mClassFragment);
        }

        if (item.getItemId() == R.id.five) {
            showHideFragment(mMeFragment);
            MobclickAgent.onEvent(this, "me_me");
        }

        return true;
    }


    @Override
    public void onBackPressedSupport() {
        if (System.currentTimeMillis() - mExitTime > 2000) {
            Toast.makeText(mContext, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
            AppManager.getAppManager().AppExit();
        }
    }


    public void requestPermissions(Activity context, int code, String[] permission) {
        if (UIUtils.requestPermission(context, code, permission)) {
            UIUtils.callLine(this, "400-876-3300");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CALL_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    UIUtils.callLine(this, "400-876-3300");
                } else {
                    Toast.makeText(mContext, "拨打电话权限未授权", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("SplashScreen11"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
        getSwipeBackLayout().setEnableGesture(false);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("SplashScreen11"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSocket != null) {
            if (!mSocket.isClosed()) {
                try {
                    mSocket.close();
                    sendData = false;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        UMShareAPI.get(this).release();
        EventBus.getDefault().unregister(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEventMain(MainActivityTabSelectPos event) {
        SPUtil.setGuest(false);
        mMeFragment.setTextStyle();

        //连接心跳
        checkHeartbeat();


        switch (event.tabPosition) {

            case 3:
                showHideFragment(mClassFragment);
                mNavigation.setSelectedItemId(R.id.four);
                break;
            case 4:
                mNavigation.setSelectedItemId(R.id.five);
                showHideFragment(mMeFragment);
//                mNavigation.setSelectedItemId(R.id.thr);
//                showHideFragment(conversationListFragment);
                break;
            case 5:
//                mNavigation.setSelectedItemId(R.id.fou);
//                showHideFragment(mMeFragment);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEventMain2(MainActivityShowGuest event) {
        sendData = false;
        if (!mSocket.isClosed()) {
            try {
                mSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (event.isGuest) {
            mMeFragment.showGuestUI();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEventMain3(NightModeEvent event) {
        useNightMode(event.mode);
    }


    /**
     * fragment management
     */
    public TFragment addFragment(TFragment fragment) {
        List<TFragment> fragments = new ArrayList<TFragment>(1);
        fragments.add(fragment);

        List<TFragment> fragments2 = addFragments(fragments);
        return fragments2.get(0);
    }


    public List<TFragment> addFragments(List<TFragment> fragments) {
        List<TFragment> fragments2 = new ArrayList<TFragment>(fragments.size());

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();

        boolean commit = false;
        for (int i = 0; i < fragments.size(); i++) {
            // install
            TFragment fragment = fragments.get(i);
            int id = fragment.getContainerId();

            // exists
            TFragment fragment2 = (TFragment) fm.findFragmentById(id);

            if (fragment2 == null) {
                fragment2 = fragment;
                transaction.add(id, fragment);
                commit = true;
            }

            fragments2.add(i, fragment2);
        }

        if (commit) {
            try {
                transaction.commitAllowingStateLoss();
            } catch (Exception e) {

            }
        }

        return fragments2;
    }
}
