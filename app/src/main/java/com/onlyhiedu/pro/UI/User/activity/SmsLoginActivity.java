package com.onlyhiedu.pro.UI.User.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import com.onlyhiedu.pro.UI.User.presenter.SmsLoginPresenter;
import com.onlyhiedu.pro.UI.User.presenter.contract.SmsLoginContract;
import com.onlyhiedu.pro.Utils.Encrypt;
import com.onlyhiedu.pro.Utils.SPUtil;
import com.onlyhiedu.pro.Utils.StringUtils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.umeng.message.common.inter.ITagManager;
import com.umeng.message.tag.TagManager;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

public class SmsLoginActivity extends BaseActivity<SmsLoginPresenter> implements SmsLoginContract.View {

    public static final String TAG = SmsLoginActivity.class.getSimpleName();

    @BindView(R.id.edit_number)
    EditText mEditNumber;
    @BindView(R.id.edit_code)
    EditText mEditCode;
    @BindView(R.id.tv_code)
    TextView mTvCode;
    @BindView(R.id.btn_sign)
    Button mBtnSign;

    private int mShowHomePosition;
    private AbortableFuture<LoginInfo> loginRequest;
    private boolean mAccountEdgeOut;
    private ProgressDialog mPd;

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_sms_login;
    }


    @Override
    protected void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        mShowHomePosition = getIntent().getIntExtra(MainActivity.showPagePosition, 0);

        mPd = new ProgressDialog(SmsLoginActivity.this);
        mPd.setCanceledOnTouchOutside(false);
        mPd.setMessage("正在登录...");
    }

    @Override
    protected void initData() {
        mEditNumber.addTextChangedListener(mTextWatcher);
    }

    @Override
    public void showSecond(int second) {
        if (second == 0) {
            mTvCode.setEnabled(true);
            mTvCode.setText(getString(R.string.text_get_verification_code));
            return;
        }
        mTvCode.setText(getString(R.string.text_get_verification_code_again, second));
    }

    @Override
    public void showAuthSuccess() {
    }


    @Override
    public void setPush() {

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
                    startActivity(new Intent(SmsLoginActivity.this, MainActivity.class));
                } else {
                    EventBus.getDefault().post(new MainActivityTabSelectPos(mShowHomePosition));
                }

                finish();
                AppManager.getAppManager().finishActivity(OpenIDActivity.class);
                AppManager.getAppManager().finishActivity(LoginActivity.class);
            }

            @Override
            public void onFailed(int code) {
                onLoginDone();
                if (code == 302 || code == 404) {
                    Toast.makeText(SmsLoginActivity.this, R.string.login_failed, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SmsLoginActivity.this, "登录失败: " + code, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onException(Throwable exception) {
                Toast.makeText(SmsLoginActivity.this, R.string.login_exception, Toast.LENGTH_LONG).show();
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

    private void addUTag() {
        //tag 手机号码 md5
        String tag = Encrypt.getMD5(mEditNumber.getText().toString());
        Log.d(TAG, "tag:" + tag + "长度：" + tag.length());
        PushAgent.getInstance(mContext).getTagManager().add(new TagManager.TCallBack() {
            @Override
            public void onMessage(final boolean isSuccess, final ITagManager.Result result) {
                //isSuccess表示操作是否成功
                Log.d(TAG, "ITag:" + result);
                if (isSuccess) {
                    mPresenter.setPushToken(PushAgent.getInstance(mContext).getRegistrationId(), tag);
                }
            }
        }, tag);
    }

    @Override
    public void showError(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }

    @OnClick({R.id.tv_code, R.id.btn_sign, R.id.ic_delete, R.id.rl_pwd_login, R.id.btn_sign_in})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_code:
                //获取验证码
                getMsgCode();

                break;
            case R.id.btn_sign:
                toLogin();
                MobclickAgent.onEvent(this, "sms_login");
                break;
            case R.id.ic_delete:
                finish();
                break;
            case R.id.rl_pwd_login:
                finish();
                break;
            case R.id.btn_sign_in:
                startActivity(new Intent(this, RegActivity.class));
                break;
        }
    }

    private void toLogin() {
        String number = mEditNumber.getText().toString();
        String code = mEditCode.getText().toString();

        if (StringUtils.isMobile(number)) {
            mPd.show();
            mPresenter.authLogin(code, number, StringUtils.getDeviceId(this));
            Log.d(TAG, "StringUtils.getDeviceId():" + StringUtils.getDeviceId(this));
        }
    }

    private void getMsgCode() {
        String number = mEditNumber.getText().toString();
        if (StringUtils.isMobile(number)) {
            MobclickAgent.onEvent(mContext, "register_identifying_code");
            mTvCode.setEnabled(false);
            mPresenter.readSecond();
            mPresenter.getAuthCode(mEditNumber.getText().toString());
        }
    }

    TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (charSequence.length() == 0) {
                mBtnSign.setEnabled(false);
                mBtnSign.setTextColor(getResources().getColor(R.color.c_FFAEBA));
            } else {
                mBtnSign.setEnabled(true);
                mBtnSign.setTextColor(getResources().getColor(R.color.c9));
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    };


    @Override
    public void showUser() {
        mPresenter.registerUikit();
    }

    @Override
    public void IMLoginFailure(String error) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(SmsLoginActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
