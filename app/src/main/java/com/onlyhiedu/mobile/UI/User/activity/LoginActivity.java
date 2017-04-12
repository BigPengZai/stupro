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
import com.onlyhiedu.mobile.Utils.SPUtil;
import com.onlyhiedu.mobile.Utils.StringUtils;
import com.onlyhiedu.mobile.Utils.UIUtils;
import com.umeng.analytics.MobclickAgent;

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
            if (SPUtil.getToken().equals("")) {
                mPresenter.getUser(number, pwd, null, StringUtils.getDeviceId(this));
                Log.d(TAG, "StringUtils.getDeviceId():" + StringUtils.getDeviceId(this));
            } else {
                mPresenter.getUser(number, pwd, SPUtil.getToken(), StringUtils.getDeviceId(this));
            }
        }
    }

    @Override
    public void showUser() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void showError(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
        Log.d(TAG, "msg:"+msg);
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
