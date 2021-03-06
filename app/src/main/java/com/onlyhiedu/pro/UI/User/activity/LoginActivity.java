package com.onlyhiedu.pro.UI.User.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.onlyhiedu.pro.App.AppManager;
import com.onlyhiedu.pro.Base.BaseActivity;
import com.onlyhiedu.pro.IM.DemoCache;
import com.onlyhiedu.pro.IM.config.preference.Preferences;
import com.onlyhiedu.pro.IM.config.preference.UserPreferences;
import com.onlyhiedu.pro.Model.event.MainActivityTabSelectPos;
import com.onlyhiedu.pro.R;
import com.onlyhiedu.pro.UI.Home.activity.MainActivity;
import com.onlyhiedu.pro.UI.User.presenter.LoginPresenter;
import com.onlyhiedu.pro.UI.User.presenter.contract.LoginContract;
import com.onlyhiedu.pro.Utils.Encrypt;
import com.onlyhiedu.pro.Utils.SPUtil;
import com.onlyhiedu.pro.Utils.StringUtils;
import com.onlyhiedu.pro.Utils.SystemUtil;
import com.onlyhiedu.pro.Utils.UIUtils;
import com.onlyhiedu.pro.Widget.InputTextView;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.umeng.message.common.inter.ITagManager;
import com.umeng.message.tag.TagManager;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

import static com.onlyhiedu.pro.R.id.btn_sign;
import static com.onlyhiedu.pro.R.id.edit_phone;
import static com.onlyhiedu.pro.R.id.tv_find_pwd;
import static com.onlyhiedu.pro.R.id.tv_sms_sign;


public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginContract.View {

    public static final String TAG = LoginActivity.class.getSimpleName();

    @BindView(edit_phone)
    InputTextView mEditNumber;
    @BindView(R.id.edit_pwd)
    InputTextView mEditPwd;
    @BindView(btn_sign)
    Button mButton;

    private int mShowHomePosition;
    private ProgressDialog mPd;
    private boolean mAccountEdgeOut;
    private AbortableFuture<LoginInfo> loginRequest;

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_login;
    }


    @Override
    protected void initView() {
        mEditPwd.setPassword(true);
        mEditPwd.setShowIcon(true);
        setToolBar("手机号登录");
        if (mToolbar != null) {
            mToolbar.setNavigationIcon(R.mipmap.back);
            mToolbar.setBackgroundColor(Color.parseColor("#F42440"));
        }


        mShowHomePosition = getIntent().getIntExtra(MainActivity.showPagePosition, 0);
        mAccountEdgeOut = getIntent().getBooleanExtra(OpenIDActivity.AccountEdgeOut, false);

        mEditNumber.setButton(mButton);

        mEditNumber.getEditTextView().setText(SPUtil.getPhone());
        UIUtils.initCursor(mEditNumber.getEditTextView());
        mPd = new ProgressDialog(LoginActivity.this);
        mPd.setCanceledOnTouchOutside(false);
        mPd.setMessage("正在登录...");
    }


    @OnClick({tv_sms_sign, tv_find_pwd, btn_sign})
    public void onClick(View view) {
        switch (view.getId()) {
            case tv_sms_sign:
                //短信验证码登录
                startActivity(new Intent(this, SmsLoginActivity.class).putExtra(MainActivity.showPagePosition, mShowHomePosition));
                MobclickAgent.onEvent(this, "login_sms_login");
                break;
            case tv_find_pwd:
                //忘记密码
                startActivity(new Intent(this, FindPwdActivity.class));
                MobclickAgent.onEvent(this, "login_forget_pw");
                break;
            case btn_sign:
                SystemUtil.hideKeyboard(mButton, this);
                //登录
                toLogin();
                MobclickAgent.onEvent(this, "login_login");
                break;
        }
    }


    private void toLogin() {
//        CrashReport.testJavaCrash();
        String number = mEditNumber.getEditText();
        String pwd = mEditPwd.getEditText();
        if (StringUtils.isMobile(number) && StringUtils.checkPassword(pwd)) {
            mPd.show();
            mPresenter.getUser(number, pwd, StringUtils.getDeviceId(this));

            Log.d(TAG, ":" + StringUtils.getDeviceId(this));
        }

    }

    @Override
    public void IMLoginFailure(String s) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(LoginActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void showUser() {
//        addUTag();
        mPresenter.registerUikit();
    }


    private void addUTag() {
        //tag 手机号码 md5
        String tag = Encrypt.getMD5(mEditNumber.getEditText());
        Log.d(TAG, "tag:" + tag + "长度：" + tag.length());
        PushAgent.getInstance(this).getTagManager().add(new TagManager.TCallBack() {
            @Override
            public void onMessage(final boolean isSuccess, final ITagManager.Result result) {
                //isSuccess表示操作是否成功
                Log.d(TAG, "ITag:" + result);
                if (isSuccess) {
                    mPresenter.setPushToken(PushAgent.getInstance(LoginActivity.this).getRegistrationId(), tag);
                }
            }
        }, tag);
    }

    @Override
    public void setPush() {
//        Toast.makeText(this, "" + PushAgent.getInstance(this).getRegistrationId(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getUikitDate() {
        // 登录
        loginRequest = NimUIKit.login(new LoginInfo(SPUtil.getUikitAccid(), SPUtil.getUikitToken()), new RequestCallback<LoginInfo>() {
            @Override
            public void onSuccess(LoginInfo param) {
                Log.d(TAG, "login success");
                SPUtil.setGuest(false);
                onLoginDone();

                DemoCache.setAccount(SPUtil.getUikitAccid());
                saveLoginInfo(SPUtil.getUikitAccid(), SPUtil.getUikitToken());

                // 初始化消息提醒配置
                initNotificationConfig();

                // 进入主界面
                if (mAccountEdgeOut) {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                } else {
                    EventBus.getDefault().post(new MainActivityTabSelectPos(mShowHomePosition));
                }

                finish();
                AppManager.getAppManager().finishActivity(OpenIDActivity.class);
            }

            @Override
            public void onFailed(int code) {
                onLoginDone();
                if (code == 302 || code == 404) {
                    Toast.makeText(LoginActivity.this, R.string.login_failed, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "登录失败: " + code, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onException(Throwable exception) {
                Toast.makeText(LoginActivity.this, R.string.login_exception, Toast.LENGTH_LONG).show();
                onLoginDone();
            }
        });

    }

    private void onLoginDone() {
        loginRequest = null;
        mPd.dismiss();
    }

    private void saveLoginInfo(final String account, final String token) {
        Preferences.saveUserAccount(account);
        Preferences.saveUserToken(token);
    }

    private void initNotificationConfig() {
        // 初始化消息提醒
        NIMClient.toggleNotification(UserPreferences.getNotificationToggle());

        // 加载状态栏配置
        StatusBarNotificationConfig statusBarNotificationConfig = UserPreferences.getStatusConfig();
        if (statusBarNotificationConfig == null) {
            statusBarNotificationConfig = DemoCache.getNotificationConfig();
            UserPreferences.setStatusConfig(statusBarNotificationConfig);
        }
        // 更新配置
        NIMClient.updateStatusBarNotificationConfig(statusBarNotificationConfig);
    }

    @Override
    public void showError(String msg) {
        if (mPd.isShowing()) {
            mPd.dismiss();
        }
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
        Log.d(TAG, "msg:" + msg);
    }

}
