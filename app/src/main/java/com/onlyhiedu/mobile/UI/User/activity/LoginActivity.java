package com.onlyhiedu.mobile.UI.User.activity;

import android.content.Intent;
import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.onlyhiedu.mobile.App.App;
import com.onlyhiedu.mobile.App.Constants;
import com.onlyhiedu.mobile.Base.BaseActivity;
import com.onlyhiedu.mobile.R;
import com.onlyhiedu.mobile.UI.Home.activity.MainActivity;
import com.onlyhiedu.mobile.UI.Info.activity.MyInfoActivity;
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

import static com.onlyhiedu.mobile.R.id.btn_cancel;
import static com.onlyhiedu.mobile.R.id.btn_guest;
import static com.onlyhiedu.mobile.R.id.btn_openid_qq;
import static com.onlyhiedu.mobile.R.id.btn_openid_sina;
import static com.onlyhiedu.mobile.R.id.btn_openid_wx;
import static com.onlyhiedu.mobile.R.id.btn_sign;
import static com.onlyhiedu.mobile.R.id.btn_sign_in;
import static com.onlyhiedu.mobile.R.id.edit_number;
import static com.onlyhiedu.mobile.R.id.img_show;
import static com.onlyhiedu.mobile.R.id.tv_find_pwd;
import static com.onlyhiedu.mobile.R.id.tv_sms_sign;


public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginContract.View {

    public static final String TAG = LoginActivity.class.getSimpleName();
    public static final String cancelShow = "cancelShow";  //取消按钮是否可见
    public static final String information = "information"; //游客模式下，是否点击各人信息进入的首页

    @BindView(edit_number)
    EditText mEditNumber;
    @BindView(R.id.edit_pwd)
    EditText mEditPwd;
    @BindView(img_show)
    ImageView mImg_Show;
    @BindView(btn_sign)
    Button mButton;
    @BindView(R.id.btn_cancel)
    TextView mTvCancel;
    @BindView(R.id.btn_guest)
    TextView mBtnGuest;


    private int REQUEST_CODE = 11;
    private boolean isChecked = true;
    private UMShareAPI mShareAPI;
    private boolean mBooleanExtra;

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
        boolean extra = getIntent().getBooleanExtra(cancelShow, false);
        mBooleanExtra = getIntent().getBooleanExtra(information, false);


        if (extra) mTvCancel.setVisibility(View.VISIBLE);
        else mTvCancel.setVisibility(View.GONE);
        if (extra) mBtnGuest.setVisibility(View.GONE);
        else mBtnGuest.setVisibility(View.VISIBLE);

        mShareAPI = UMShareAPI.get(this);
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

    @OnClick({tv_sms_sign, tv_find_pwd, btn_sign, btn_sign_in, img_show, edit_number, btn_openid_qq, btn_openid_wx, btn_openid_sina, btn_guest, btn_cancel})
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
            case btn_sign_in:
                //注册
//                startActivity(new Intent(this, RegActivity.class));
                startActivityForResult(new Intent(this, RegActivity.class), REQUEST_CODE);
                break;
            case img_show:
                showPwd();
                break;
            case btn_openid_qq:
                mShareAPI.doOauthVerify(this, SHARE_MEDIA.QQ, umAuthListener);
                break;
            case btn_openid_wx:
                mShareAPI.deleteOauth(this, SHARE_MEDIA.WEIXIN, wxAuthLister);
                break;
            case btn_openid_sina:
                mShareAPI.doOauthVerify(this, SHARE_MEDIA.SINA, umAuthListener);
                break;
            case btn_guest:
                App.bIsGuestLogin = true;
//                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                startActivity(new Intent(LoginActivity.this, ECLoginActivity.class));
                break;
            case btn_cancel:
                finish();
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
        addUTag();
        App.bIsGuestLogin = false;
        if (mBooleanExtra) {
            startActivity(new Intent(this, MyInfoActivity.class));
        } else {
//            startActivity(new Intent(this, MainActivity.class));
            mPresenter.emcLogin(mEditNumber.getText().toString(),mEditPwd.getText().toString(),this);
            startActivity(new Intent(this, MainActivity.class));
        }
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
            mShareAPI.doOauthVerify(LoginActivity.this, SHARE_MEDIA.WEIXIN, umAuthListener);
        }
    };

    private UMAuthListener umAuthListener = new MyUMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
            //回调成功，即登陆成功后这里返回Map<String, String> map，map里面就是用户的信息，可以拿出来使用了
//            Toast.makeText(getApplicationContext(), "授权成功", Toast.LENGTH_SHORT).show();
            if (map != null) {
                mShareAPI.getPlatformInfo(LoginActivity.this, share_media, umAuthListener2);

//                App.bIsGuestLogin = true;  //设置为游客
//                startActivity(new Intent(LoginActivity.this,BindActivity.class));
//                Log.d("auth callbacl","getting data");
//
//                Log.d(Constants.Async,map.toString());
//
//                Toast.makeText(getApplicationContext(), map.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    };

    private UMAuthListener umAuthListener2 = new MyUMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
            //回调成功，即登陆成功后这里返回Map<String, String> map，map里面就是用户的信息，可以拿出来使用了
            Toast.makeText(getApplicationContext(), "授权成功", Toast.LENGTH_SHORT).show();
            if (map != null) {
                App.bIsGuestLogin = true;  //设置为游客
                startActivity(new Intent(LoginActivity.this, BindActivity.class));
                Log.d("auth callbacl", "getting data");

                Log.d(Constants.Async, map.toString());

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
