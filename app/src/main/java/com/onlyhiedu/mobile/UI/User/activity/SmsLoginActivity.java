package com.onlyhiedu.mobile.UI.User.activity;

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

import com.onlyhiedu.mobile.App.App;
import com.onlyhiedu.mobile.App.AppManager;
import com.onlyhiedu.mobile.Base.BaseActivity;
import com.onlyhiedu.mobile.Model.bean.AuthUserDataBean;
import com.onlyhiedu.mobile.R;
import com.onlyhiedu.mobile.UI.Home.activity.MainActivity;
import com.onlyhiedu.mobile.UI.User.presenter.SmsLoginPresenter;
import com.onlyhiedu.mobile.UI.User.presenter.contract.SmsLoginContract;
import com.onlyhiedu.mobile.Utils.Encrypt;
import com.onlyhiedu.mobile.Utils.StringUtils;
import com.onlyhiedu.mobile.Utils.UIUtils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.umeng.message.common.inter.ITagManager;
import com.umeng.message.tag.TagManager;

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
    public void showAuthSuccess(int authCode) {
        mAuthCode = authCode;
        Log.d(TAG, "验证码：" + mAuthCode);
    }

    @Override
    public void showAuthLoginSuccess(AuthUserDataBean info) {
        if (info != null) {
            Log.d(TAG, "" + info.getDeviceId());
            addUTag();
            startActivity(new Intent(this, MainActivity.class));
            AppManager.getAppManager().finishActivity(LoginActivity.class);
            AppManager.getAppManager().finishActivity(OpenIDActivity.class);
            finish();
        }
    }

    @Override
    public void setPush() {

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
        addUTag();
        startActivity(new Intent(this, MainActivity.class));
        AppManager.getAppManager().finishActivity(LoginActivity.class);
        finish();
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
