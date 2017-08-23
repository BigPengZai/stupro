package com.onlyhiedu.mobile.UI.User.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.onlyhiedu.mobile.App.AppManager;
import com.onlyhiedu.mobile.Base.BaseActivity;
import com.onlyhiedu.mobile.Model.event.MainActivityTabSelectPos;
import com.onlyhiedu.mobile.R;
import com.onlyhiedu.mobile.UI.Home.activity.MainActivity;
import com.onlyhiedu.mobile.UI.Info.activity.MyInfoActivity;
import com.onlyhiedu.mobile.UI.User.presenter.LoginPresenter;
import com.onlyhiedu.mobile.UI.User.presenter.contract.LoginContract;
import com.onlyhiedu.mobile.Utils.Encrypt;
import com.onlyhiedu.mobile.Utils.SPUtil;
import com.onlyhiedu.mobile.Utils.StringUtils;
import com.onlyhiedu.mobile.Utils.SystemUtil;
import com.onlyhiedu.mobile.Utils.UIUtils;
import com.onlyhiedu.mobile.Widget.InputTextView;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.umeng.message.common.inter.ITagManager;
import com.umeng.message.tag.TagManager;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

import static com.onlyhiedu.mobile.R.id.btn_sign;
import static com.onlyhiedu.mobile.R.id.edit_phone;
import static com.onlyhiedu.mobile.R.id.tv_find_pwd;
import static com.onlyhiedu.mobile.R.id.tv_sms_sign;
import static com.onlyhiedu.mobile.UI.User.activity.OpenIDActivity.information;


public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginContract.View {

    public static final String TAG = LoginActivity.class.getSimpleName();

    @BindView(edit_phone)
    InputTextView mEditNumber;
    @BindView(R.id.edit_pwd)
    InputTextView mEditPwd;
    @BindView(btn_sign)
    Button mButton;

    private boolean mBooleanExtra;
    private int mIntExtra;
    private ProgressDialog mPd;

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

        setToolBar("手机号登录");

        mBooleanExtra = getIntent().getBooleanExtra(information, false);
        mIntExtra = getIntent().getIntExtra(MainActivity.showPagePosition, 0);

        mEditNumber.setButton(mButton);
        mEditNumber.getEditTextView().setText(SPUtil.getPhone());
        UIUtils.initCursor(mEditNumber.getEditTextView());
        mPd = new ProgressDialog(LoginActivity.this);
        mPd.setCanceledOnTouchOutside(false);
        mPd.setMessage(getString(R.string.Is_landing));
    }


    @OnClick({tv_sms_sign, tv_find_pwd, btn_sign})
    public void onClick(View view) {
        switch (view.getId()) {
            case tv_sms_sign:
                //短信验证码登录
                startActivity(new Intent(this, SmsLoginActivity.class));
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
//            mPd.show();
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
        addUTag();
//        App.bIsGuestLogin = false;

        SPUtil.setGuest(false);

        if (mBooleanExtra) {
            startActivity(new Intent(this, MyInfoActivity.class));
            EventBus.getDefault().post(new MainActivityTabSelectPos(mIntExtra));
        } else {
            startActivity(new Intent(this, MainActivity.class));
            EventBus.getDefault().post(new MainActivityTabSelectPos(mIntExtra));
        }
        mPd.dismiss();
        finish();
        AppManager.getAppManager().finishActivity(OpenIDActivity.class);
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
    public void showError(String msg) {
        if(mPd.isShowing()){
            mPd.dismiss();
        }
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
        Log.d(TAG, "msg:" + msg);
    }

}
