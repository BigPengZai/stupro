package com.onlyhiedu.mobile.UI.User.activity;

import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.onlyhiedu.mobile.App.AppManager;
import com.onlyhiedu.mobile.Base.BaseActivity;
import com.onlyhiedu.mobile.Model.bean.AuthUserDataBean;
import com.onlyhiedu.mobile.R;
import com.onlyhiedu.mobile.UI.Home.activity.MainActivity;
import com.onlyhiedu.mobile.UI.User.presenter.SmsLoginPresenter;
import com.onlyhiedu.mobile.UI.User.presenter.contract.SmsLoginContract;
import com.onlyhiedu.mobile.Utils.StringUtils;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.OnClick;

public class SmsLoginActivity extends BaseActivity<SmsLoginPresenter> implements SmsLoginContract.View {

    @BindView(R.id.edit_number)
    EditText mEditNumber;
    @BindView(R.id.edit_code)
    EditText mEditCode;
    @BindView(R.id.tv_code)
    TextView mTvCode;

    public static final String TAG = SmsLoginActivity.class.getSimpleName();
    private int mAuthCode;

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
    }

    @Override
    protected void initData() {
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
    public void showAuthSuccess(int authCode) {
        mAuthCode = authCode;
        Log.d(TAG, "验证码：" + mAuthCode);
    }

    @Override
    public void showAuthLoginSuccess(AuthUserDataBean info) {
        if (info != null) {
            Log.d(TAG, "" + info.getDeviceId());
            startActivity(new Intent(this, MainActivity.class));
            AppManager.getAppManager().finishActivity(LoginActivity.class);
            finish();
        }
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
                if ("获取验证码".equals(mTvCode.getText().toString())) {
                    mPresenter.getAuthCode(mEditNumber.getText().toString());
                    MobclickAgent.onEvent(mContext, "register_identifying_code");
                }
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
        if (!code.equals(mAuthCode + "")) {
            Toast.makeText(mContext, "验证码信息错误", Toast.LENGTH_SHORT).show();
            return;
        }


        if (StringUtils.isMobile(number)) {
            mPresenter.authLogin(code, number, StringUtils.getDeviceId(this));
            Log.d(TAG, "StringUtils.getDeviceId():" + StringUtils.getDeviceId(this));
        }
    }

    private void getMsgCode() {
        String number = mEditNumber.getText().toString();
        if (StringUtils.isMobile(number)) {
            mTvCode.setEnabled(false);
            mPresenter.readSecond();
        }
    }


}
