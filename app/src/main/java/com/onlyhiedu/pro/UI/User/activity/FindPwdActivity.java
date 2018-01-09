package com.onlyhiedu.pro.UI.User.activity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.onlyhiedu.pro.Base.BaseActivity;
import com.onlyhiedu.pro.Model.bean.AuthCodeInfo;
import com.onlyhiedu.pro.R;
import com.onlyhiedu.pro.UI.User.presenter.FindPwdPresenter;
import com.onlyhiedu.pro.UI.User.presenter.contract.FindPwdContract;
import com.onlyhiedu.pro.Utils.StringUtils;
import com.onlyhiedu.pro.Utils.UIUtils;
import com.onlyhiedu.pro.Widget.InputTextView;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.OnClick;

public class FindPwdActivity extends BaseActivity<FindPwdPresenter> implements FindPwdContract.View {

    //手机号码
    @BindView(R.id.edit_number)
    InputTextView mEditNumber;
    //重置密码
    @BindView(R.id.edit_pwd)
    InputTextView mEditPwd;
    @BindView(R.id.edit_code)
    EditText mEditCode;
    @BindView(R.id.tv_code)
    TextView mTvCode;
    @BindView(R.id.btn_sign_in)
    Button mBtnSignIn;

    public static final String TAG = FindPwdActivity.class.getSimpleName();

    private AuthCodeInfo mCodeInfo;

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_find_pwd;
    }


    @Override
    protected void initView() {
        setToolBar("找回密码", R.mipmap.close);

        mEditPwd.setPassword(true);
        mCodeInfo = new AuthCodeInfo();
        mEditNumber.setButton(mBtnSignIn);
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
    public void showAuthSuccess(AuthCodeInfo info) {
        if (info != null) {
            Log.d(TAG, "验证码：" + info.getAuthCode());
            mCodeInfo.setAuthCode(info.getAuthCode());
        }
    }


    @Override
    public void showError(String msg) {
        if (msg.equals("成功")) {
            finish();
        }
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }


    @OnClick({R.id.tv_code, R.id.btn_sign_in})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_code:
                String phone = mEditNumber.getEditText();
                if (StringUtils.isMobile(phone)) {
                    mTvCode.setEnabled(false);
                    mPresenter.getAuthCode(phone);
                    mPresenter.readSecond();
                    MobclickAgent.onEvent(this, "forgot_identifying_code");
                }

                break;
            case R.id.btn_sign_in:
                String phone2 = mEditNumber.getEditText();
                String pwd = mEditPwd.getEditText();
                String authCode = mEditCode.getText().toString();
                if (StringUtils.isMobile(phone2) && StringUtils.checkPassword(pwd)) {
                    String s = "" + mCodeInfo.getAuthCode();
                    if (mCodeInfo != null && !s.equals(authCode)) {
                        Toast.makeText(mContext, "验证码不正确", Toast.LENGTH_SHORT).show();
                    } else {
                        mPresenter.retrievePwd(phone2, UIUtils.sha512(phone2, pwd), authCode);
                        MobclickAgent.onEvent(this, "forgot_register");
                    }
                }

                break;
        }
    }


}
