package com.onlyhiedu.mobile.UI.Home.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.LocalBroadcastManager;
import android.view.MenuItem;
import android.widget.Toast;

import com.hyphenate.EMContactListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.util.EMLog;
import com.onlyhiedu.mobile.App.App;
import com.onlyhiedu.mobile.App.AppManager;
import com.onlyhiedu.mobile.Base.VersionUpdateActivity;
import com.onlyhiedu.mobile.Model.event.MainActivityTabSelectPos;
import com.onlyhiedu.mobile.R;
import com.onlyhiedu.mobile.UI.Emc.ChatActivity;
import com.onlyhiedu.mobile.UI.Emc.Constant;
import com.onlyhiedu.mobile.UI.Emc.ConversationListFragment;
import com.onlyhiedu.mobile.UI.Emc.DemoHelper;
import com.onlyhiedu.mobile.UI.Home.fragment.ClassFragment;
import com.onlyhiedu.mobile.UI.Home.fragment.HomeFragment;
import com.onlyhiedu.mobile.UI.Home.fragment.MeFragment;
import com.onlyhiedu.mobile.UI.User.activity.LoginActivity;
import com.onlyhiedu.mobile.Utils.UIUtils;
import com.onlyhiedu.mobile.db.InviteMessgeDao;
import com.onlyhiedu.mobile.db.UserDao;
import com.onlyhiedu.mobile.runtimepermissions.PermissionsManager;
import com.onlyhiedu.mobile.runtimepermissions.PermissionsResultAction;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareAPI;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;


public class MainActivity extends VersionUpdateActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    public static final String TAG = MainActivity.class.getSimpleName();
    public static final int CALL_REQUEST_CODE = 110;
    public static String showPagePosition = "showPagePosition";
    private ClassFragment mClassFragment;
    private MeFragment mMeFragment;
    private HomeFragment mHomeFragment;
    // user logged into another device
    public boolean isConflict = false;
    // user account was removed
    private boolean isCurrentAccountRemoved = false;
    private long mExitTime = 0;
    private int currentTabIndex;

    @BindView(R.id.navigation)
    BottomNavigationView mNavigation;

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
        EventBus.getDefault().register(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String packageName = getPackageName();
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                try {
                    Intent intent = new Intent();
                    intent.setAction(android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                    intent.setData(Uri.parse("package:" + packageName));
                    startActivity(intent);
                } catch (Exception e) {
                }
            }
        }
        //make sure activity will not in background if user is logged into another device or removed
        if (savedInstanceState != null && savedInstanceState.getBoolean(Constant.ACCOUNT_REMOVED, false)) {
            DemoHelper.getInstance().logout(false, null);
//            finish();
            startActivity(new Intent(this, LoginActivity.class));
            return;
        } else if (savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false)) {
//            finish();
//            startActivity(new Intent(this, LoginActivity.class));
            return;
        }
        // runtime permission for android 6.0, just require all permissions here for simple
        requestPermissions();
    }

    @Override
    protected void initView() {

        mClassFragment = new ClassFragment();
        mMeFragment = new MeFragment();
        mHomeFragment = new HomeFragment();
        conversationListFragment = new ConversationListFragment();


        BottomNavigationViewHelper.disableShiftMode(mNavigation);
        showExceptionDialogFromIntent(getIntent());
        inviteMessgeDao = new InviteMessgeDao(this);
        UserDao userDao = new UserDao(this);

//        contactListFragment = new ContactListFragment();


        //不隐藏首页
        if (App.getInstance().isTag) {
            mNavigation.setSelectedItemId(R.id.tow);
            loadMultipleRootFragment(R.id.fl_main_content, 1, mHomeFragment, mClassFragment, conversationListFragment, mMeFragment);
            App.getInstance().isTag = false;
        } else {
            loadMultipleRootFragment(R.id.fl_main_content, 0, mHomeFragment, mClassFragment, conversationListFragment, mMeFragment);
        }
        mNavigation.setOnNavigationItemSelectedListener(this);
        mNavigation.setItemIconTintList(null);
    }

    @Override
    protected void initData() {
        super.initData();
        mPresenter.updateVersion(true);
        //register broadcast receiver to receive the change of group from DemoHelper
        registerBroadcastReceiver();
        EMClient.getInstance().contactManager().setContactListener(new MyContactListener());
        //debug purpose only
//        registerInternalDebugReceiver();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        //不隐藏首页
        if (item.getItemId() == R.id.one) {
            showHideFragment(mHomeFragment);
        }
        if (item.getItemId() == R.id.tow) {
            if (App.bIsGuestLogin) {
                UIUtils.startGuestLoginActivity(this, 1);
                return false;
            } else showHideFragment(mClassFragment);

        }
        if (item.getItemId() == R.id.thr) {
            if (App.bIsGuestLogin) {
                UIUtils.startGuestLoginActivity(this, 2);
                return false;
            } else showHideFragment(conversationListFragment);


        }
        if (item.getItemId() == R.id.fou) {
            showHideFragment(mMeFragment);
            MobclickAgent.onEvent(this, "me_me");
        }
        return true;
    }


    @Override
    public void onBackPressedSupport() {
//        super.onBackPressedSupport();
        exit();
    }

    private void exit() {
        if (System.currentTimeMillis() - mExitTime > 2000) {
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
            EMClient.getInstance().chatManager().removeMessageListener(messageListener);
            DemoHelper sdkHelper = DemoHelper.getInstance();
            sdkHelper.popActivity(this);
            AppManager.getAppManager().AppExit();
        }
    }

    @TargetApi(23)
    private void requestPermissions() {
        PermissionsManager.getInstance().requestAllManifestPermissionsIfNecessary(this, new PermissionsResultAction() {
            @Override
            public void onGranted() {
//				Toast.makeText(MainActivity.this, "All permissions have been granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDenied(String permission) {
                //Toast.makeText(MainActivity.this, "Permission " + permission + " has been denied", Toast.LENGTH_SHORT).show();
            }
        });
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
        PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults);
    }

    EMMessageListener messageListener = new EMMessageListener() {

        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            // notify new message
            for (EMMessage message : messages) {
                DemoHelper.getInstance().getNotifier().onNewMsg(message);
            }
            refreshUIWithMessage();
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
            //red packet code : 处理红包回执透传消息
            for (EMMessage message : messages) {
                EMCmdMessageBody cmdMsgBody = (EMCmdMessageBody) message.getBody();
                final String action = cmdMsgBody.action();//获取自定义action
//                if (action.equals(RPConstant.REFRESH_GROUP_RED_PACKET_ACTION)) {
//                    RedPacketUtil.receiveRedPacketAckMessage(message);
//                }
            }
            //end of red packet code
            refreshUIWithMessage();
        }

        @Override
        public void onMessageRead(List<EMMessage> messages) {
        }

        @Override
        public void onMessageDelivered(List<EMMessage> message) {
        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {
        }
    };

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("SplashScreen11"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
//        if (!isConflict && !isCurrentAccountRemoved) {
//            updateUnreadLabel();
//            updateUnreadAddressLable();
//        }
        updateUnreadLabel();
        updateUnreadAddressLable();
        DemoHelper sdkHelper = DemoHelper.getInstance();
        sdkHelper.pushActivity(this);
        EMClient.getInstance().chatManager().addMessageListener(messageListener);
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
        if (exceptionBuilder != null) {
            exceptionBuilder.create().dismiss();
            exceptionBuilder = null;
            isExceptionDialogShow = false;
        }
        unregisterBroadcastReceiver();

        UMShareAPI.get(this).release();
        EventBus.getDefault().unregister(this);
    }

    private void unregisterBroadcastReceiver() {
        broadcastManager.unregisterReceiver(broadcastReceiver);
    }

    /**
     * update unread message count
     */
    public void updateUnreadLabel() {
        int count = getUnreadMsgCountTotal();
        if (mNavigation!=null) {
            if (count > 0) {
                mNavigation.getMenu().getItem(2).setIcon(R.mipmap.ic_launcher);
            } else {
                mNavigation.getMenu().getItem(2).setIcon(R.drawable.em_conversation_selected);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEventMain(MainActivityTabSelectPos event) {
        App.bIsGuestLogin = false;
        mMeFragment.setTextStyle();

        switch (event.tabPosition) {
            case 1:
                showHideFragment(mClassFragment);
                mNavigation.setSelectedItemId(R.id.tow);
                break;
            case 2:
                mNavigation.setSelectedItemId(R.id.thr);
                showHideFragment(conversationListFragment);
                break;
            case 3:
                mNavigation.setSelectedItemId(R.id.fou);
                showHideFragment(mMeFragment);
                break;
        }
    }

    /**
     * update the total unread count
     */
    public void updateUnreadAddressLable() {
        runOnUiThread(new Runnable() {
            public void run() {
                int count = getUnreadAddressCountTotal();
                if (count > 0) {
                    conversationListFragment.setRightBar(true);
                } else {
                    conversationListFragment.setRightBar(false);
                }
            }
        });

    }


    /**
     * get unread message count
     *
     * @return
     */
    public int getUnreadMsgCountTotal() {
        return EMClient.getInstance().chatManager().getUnreadMsgsCount();
    }


    private InviteMessgeDao inviteMessgeDao;


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("isConflict", isConflict);
        outState.putBoolean(Constant.ACCOUNT_REMOVED, isCurrentAccountRemoved);
        super.onSaveInstanceState(outState);
    }


    private android.app.AlertDialog.Builder exceptionBuilder;
    private boolean isExceptionDialogShow = false;
    private BroadcastReceiver internalDebugReceiver;
    private ConversationListFragment conversationListFragment;
    private BroadcastReceiver broadcastReceiver;
    private LocalBroadcastManager broadcastManager;

    private int getExceptionMessageId(String exceptionType) {
        if (exceptionType.equals(Constant.ACCOUNT_CONFLICT)) {
            return R.string.connect_conflict;
        } else if (exceptionType.equals(Constant.ACCOUNT_REMOVED)) {
            return R.string.em_user_remove;
        } else if (exceptionType.equals(Constant.ACCOUNT_FORBIDDEN)) {
            return R.string.user_forbidden;
        }
        return R.string.Network_error;
    }

    /**
     * show the dialog when user met some exception: such as login on another device, user removed or user forbidden
     */
    private void showExceptionDialog(String exceptionType) {
        isExceptionDialogShow = true;
        DemoHelper.getInstance().logout(false, null);
        isConflict = true;
        isExceptionDialogShow = false;
        UIUtils.startLoginActivity(this);
//        String st = getResources().getString(R.string.Logoff_notification);
//        if (!MainActivity.this.isFinishing()) {
//            // clear up global variables
//            try {
//                if (exceptionBuilder == null)
//                    exceptionBuilder = new android.app.AlertDialog.Builder(MainActivity.this);
//                exceptionBuilder.setTitle(st);
//                exceptionBuilder.setMessage(getExceptionMessageId(exceptionType));
//                exceptionBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                        exceptionBuilder = null;
//                        isExceptionDialogShow = false;
//                        finish();
////                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
////                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
////                        startActivity(intent);
//                    }
//                });
//                exceptionBuilder.setCancelable(false);
//                exceptionBuilder.create().show();
//                isConflict = true;
//            } catch (Exception e) {
//                EMLog.e(TAG, "---------color conflictBuilder error" + e.getMessage());
//            }
//        }
    }

    private void showExceptionDialogFromIntent(Intent intent) {
        EMLog.e(TAG, "showExceptionDialogFromIntent");
        if (!isExceptionDialogShow && intent.getBooleanExtra(Constant.ACCOUNT_CONFLICT, false)) {
            showExceptionDialog(Constant.ACCOUNT_CONFLICT);
        } else if (!isExceptionDialogShow && intent.getBooleanExtra(Constant.ACCOUNT_REMOVED, false)) {
            showExceptionDialog(Constant.ACCOUNT_REMOVED);
        } else if (!isExceptionDialogShow && intent.getBooleanExtra(Constant.ACCOUNT_FORBIDDEN, false)) {
            showExceptionDialog(Constant.ACCOUNT_FORBIDDEN);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        showExceptionDialogFromIntent(intent);
    }


    private void refreshUIWithMessage() {
        runOnUiThread(new Runnable() {
            public void run() {
                // refresh unread count
                updateUnreadLabel();
                if (currentTabIndex == 0) {
                    // refresh conversation list
                    if (conversationListFragment != null) {
                        conversationListFragment.refresh();
                    }
                }
            }
        });
    }

    /**
     * get unread event notification count, including application, accepted, etc
     *
     * @return
     */
    public int getUnreadAddressCountTotal() {
        int unreadAddressCountTotal = 0;
        unreadAddressCountTotal = inviteMessgeDao.getUnreadMessagesCount();
        return unreadAddressCountTotal;
    }


    private void registerBroadcastReceiver() {
        broadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.ACTION_CONTACT_CHANAGED);
        intentFilter.addAction(Constant.ACTION_GROUP_CHANAGED);
//        intentFilter.addAction(RPConstant.REFRESH_GROUP_RED_PACKET_ACTION);
        broadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                updateUnreadLabel();
                updateUnreadAddressLable();
                if (conversationListFragment != null) {
                    conversationListFragment.refresh();
                }
                if(conversationListFragment != null) {
                    conversationListFragment.refresh();
                }
                if (currentTabIndex == 0) {
                    // refresh conversation list
                    if (conversationListFragment != null) {
                        conversationListFragment.refresh();
                    }
                } else if (currentTabIndex == 1) {
                    if(conversationListFragment != null) {
                        conversationListFragment.refresh();
                    }
                }
                String action = intent.getAction();
                if (action.equals(Constant.ACTION_GROUP_CHANAGED)) {
//                    if (EaseCommonUtils.getTopActivity(MainActivity.this).equals(GroupsActivity.class.getName())) {
//                        GroupsActivity.instance.onResume();
//                    }
                }
                //end of red packet code
            }
        };
        broadcastManager.registerReceiver(broadcastReceiver, intentFilter);
    }

    public class MyContactListener implements EMContactListener {
        @Override
        public void onContactAdded(String username) {
        }

        @Override
        public void onContactDeleted(final String username) {
            runOnUiThread(new Runnable() {
                public void run() {
                    if (ChatActivity.activityInstance != null && ChatActivity.activityInstance.toChatUsername != null &&
                            username.equals(ChatActivity.activityInstance.toChatUsername)) {
                        String st10 = getResources().getString(R.string.have_you_removed);
                        Toast.makeText(MainActivity.this, ChatActivity.activityInstance.getToChatUsername() + st10, Toast.LENGTH_LONG)
                                .show();
                        ChatActivity.activityInstance.finish();
                    }
                }
            });
            updateUnreadAddressLable();
        }

        @Override
        public void onContactInvited(String username, String reason) {
        }

        @Override
        public void onFriendRequestAccepted(String username) {
        }

        @Override
        public void onFriendRequestDeclined(String username) {
        }
    }

}
