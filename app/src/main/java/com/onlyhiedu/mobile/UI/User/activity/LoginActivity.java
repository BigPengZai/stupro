package com.onlyhiedu.mobile.UI.User.activity;

import android.content.Intent;
import android.os.Build;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.onlyhiedu.mobile.Base.BaseActivity;
import com.onlyhiedu.mobile.R;
import com.onlyhiedu.mobile.UI.Home.activity.MainActivity;
import com.onlyhiedu.mobile.UI.User.presenter.LoginPresenter;
import com.onlyhiedu.mobile.UI.User.presenter.contract.LoginContract;
import com.onlyhiedu.mobile.Utils.Encrypt;
import com.onlyhiedu.mobile.Utils.SPUtil;
import com.onlyhiedu.mobile.Utils.StringUtils;
import com.onlyhiedu.mobile.Utils.UIUtils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.umeng.message.common.inter.ITagManager;
import com.umeng.message.tag.TagManager;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginContract.View {


    private boolean isChecked = true;

    @BindView(R.id.edit_number)
    EditText mEditNumber;
    @BindView(R.id.edit_pwd)
    EditText mEditPwd;
    @BindView(R.id.img_show)
    ImageView mImg_Show;
    private int REQUEST_CODE = 11;
    public static final String TAG = LoginActivity.class.getSimpleName();

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
        mEditNumber.setText(SPUtil.getPhone());
        UIUtils.initCursor(mEditNumber);
    }


    @OnClick({R.id.tv_sms_sign, R.id.tv_find_pwd, R.id.btn_sign, R.id.btn_sign_in, R.id.img_show, R.id.edit_number})
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
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void addUTag() {
        //tag 手机号码 md5
        String tag = Encrypt.getMD5(mEditNumber.getText().toString());
        Log.d(TAG, "tag:"+tag+"长度："+tag.length());
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
        Toast.makeText(this, "" + PushAgent.getInstance(this).getRegistrationId(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showError(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
        Log.d(TAG, "msg:" + msg);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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

}
