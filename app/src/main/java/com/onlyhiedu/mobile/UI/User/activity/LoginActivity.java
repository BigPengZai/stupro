package com.onlyhiedu.mobile.UI.User.activity;

import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Intent;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.onlyhiedu.mobile.App.App;
import com.onlyhiedu.mobile.Base.BaseActivity;
import com.onlyhiedu.mobile.R;
import com.onlyhiedu.mobile.UI.Home.activity.MainActivity;
import com.onlyhiedu.mobile.UI.User.presenter.LoginPresenter;
import com.onlyhiedu.mobile.UI.User.presenter.contract.LoginContract;
import com.onlyhiedu.mobile.Utils.Encrypt;
import com.onlyhiedu.mobile.Utils.MyUMAuthListener;
import com.onlyhiedu.mobile.Utils.SPUtil;
import com.onlyhiedu.mobile.Utils.StringUtils;
import com.onlyhiedu.mobile.Utils.SystemUtil;
import com.onlyhiedu.mobile.Utils.UIUtils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.umeng.message.common.inter.ITagManager;
import com.umeng.message.tag.TagManager;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;


public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginContract.View {

    public static final String TAG = LoginActivity.class.getSimpleName();


    @BindView(R.id.edit_number)
    EditText mEditNumber;
    @BindView(R.id.edit_pwd)
    EditText mEditPwd;
    @BindView(R.id.img_show)
    ImageView mImg_Show;
    @BindView(R.id.btn_sign)
    Button mButton;

    private int REQUEST_CODE = 11;
    private boolean isChecked = true;
    private UMShareAPI mShareAPI;

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        mShareAPI = UMShareAPI.get( this );
        mEditNumber.setText(SPUtil.getPhone());
        mEditNumber.addTextChangedListener(mTextWatcher);

        UIUtils.initCursor(mEditNumber);
        if (!TextUtils.isEmpty(mEditNumber.getText().toString())) {
            mButton.setEnabled(true);
            mButton.setTextColor(getResources().getColor(R.color.c9));
        }
    }


    TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (charSequence.length() == 0) {
                mButton.setEnabled(false);
                mButton.setTextColor(getResources().getColor(R.color.c_FFAEBA));
            } else {
                mButton.setEnabled(true);
                mButton.setTextColor(getResources().getColor(R.color.c9));
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    };

    @OnClick({R.id.tv_sms_sign, R.id.tv_find_pwd, R.id.btn_sign, R.id.btn_sign_in, R.id.img_show, R.id.edit_number, R.id.btn_openid_qq, R.id.btn_openid_wx, R.id.btn_openid_sina})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_sms_sign:
                //短信验证码登录
                startActivity(new Intent(this, SmsLoginActivity.class));
                MobclickAgent.onEvent(this, "login_sms_login");
                break;
            case R.id.tv_find_pwd:
                //忘记密码
                startActivity(new Intent(this, FindPwdActivity.class));
                MobclickAgent.onEvent(this, "login_forget_pw");
                break;
            case R.id.btn_sign:
                SystemUtil.hideKeyboard(mButton, this);
                //登录
                toLogin();
                MobclickAgent.onEvent(this, "login_login");
                break;
            case R.id.btn_sign_in:
                //注册
//                startActivity(new Intent(this, RegActivity.class));
                startActivityForResult(new Intent(this, RegActivity.class), REQUEST_CODE);
                break;
            case R.id.img_show:
                showPwd();
                break;
            case R.id.btn_openid_qq:
                mShareAPI.doOauthVerify(this, SHARE_MEDIA.QQ, umAuthListener);
                break;
            case R.id.btn_openid_wx:
                mShareAPI.deleteOauth(this, SHARE_MEDIA.WEIXIN, wxAuthLister);
                break;
            case R.id.btn_openid_sina:
                mShareAPI.doOauthVerify(this, SHARE_MEDIA.SINA, umAuthListener);
                break;
        }
    }

    private void showPwd() {
        if (isChecked) {
            mEditPwd.setTransformationMethod(HideReturnsTransformationMethod
                    .getInstance());
            isChecked = false;
            mImg_Show.setImageResource(R.mipmap.visible);
        } else {
            mEditPwd.setTransformationMethod(PasswordTransformationMethod
                    .getInstance());
            isChecked = true;
            mImg_Show.setImageResource(R.mipmap.ic_pwd_hide);
        }
        UIUtils.initCursor(mEditPwd);
    }

    private void toLogin() {
        String number = mEditNumber.getText().toString();
        String pwd = mEditPwd.getText().toString();
        if (StringUtils.isMobile(number) && StringUtils.checkPassword(pwd)) {
            mPresenter.getUser(number, pwd, StringUtils.getDeviceId(this));
            Log.d(TAG, ":" + StringUtils.getDeviceId(this));
        }
    }

    @Override
    public void showUser() {
        if (TextUtils.isEmpty(SPUtil.getToken())) {
            App.bIsGuestLogin = true;
        }else{
            App.bIsGuestLogin = false;
        }
        addUTag();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void addUTag() {
        //tag 手机号码 md5
        String tag = Encrypt.getMD5(mEditNumber.getText().toString());
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
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
        Log.d(TAG, "msg:" + msg);
    }


    private UMAuthListener wxAuthLister = new MyUMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
            mShareAPI.doOauthVerify(LoginActivity.this, SHARE_MEDIA.WEIXIN,umAuthListener);
        }
    };

    private UMAuthListener umAuthListener = new MyUMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
            //回调成功，即登陆成功后这里返回Map<String, String> map，map里面就是用户的信息，可以拿出来使用了
            Toast.makeText(getApplicationContext(), "授权成功", Toast.LENGTH_SHORT).show();
            if (map!=null){
                App.bIsGuestLogin = true;  //设置为游客
                startActivity(new Intent(LoginActivity.this,BindActivity.class));
                Log.d("auth callbacl","getting data");
                Toast.makeText(getApplicationContext(), map.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mShareAPI.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            String username = data.getStringExtra("username");
            switch (requestCode) {
                case 11:
                    mEditNumber.setText(username);
                    UIUtils.initCursor(mEditNumber);
                    break;
                default:
                    break;
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //防止内存泄漏
        UMShareAPI.get(this).release();
    }

}
