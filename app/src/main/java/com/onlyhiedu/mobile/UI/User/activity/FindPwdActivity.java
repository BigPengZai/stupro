package com.onlyhiedu.mobile.UI.User.activity;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.onlyhiedu.mobile.Base.BaseActivity;
import com.onlyhiedu.mobile.Model.bean.AuthCodeInfo;
import com.onlyhiedu.mobile.R;
import com.onlyhiedu.mobile.UI.User.presenter.FindPwdPresenter;
import com.onlyhiedu.mobile.UI.User.presenter.contract.FindPwdContract;
import com.onlyhiedu.mobile.Utils.StringUtils;
import com.onlyhiedu.mobile.Utils.UIUtils;
import com.onlyhiedu.mobile.Widget.InputTextView;
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
    public void showRetrievePwd() {
        Log.d(TAG, "suce");

    }

    @Override
    public void showError(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }


    @OnClick({R.id.tv_code, R.id.btn_sign_in})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_code:
                String editText = mEditNumber.getEditText();
                if (TextUtils.isEmpty(editText)) {
                    Toast.makeText(mContext, "请输入手机号码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!StringUtils.isMobile(editText)) {
                    return;
                }
                mTvCode.setEnabled(false);
                mPresenter.readSecond();
                MobclickAgent.onEvent(this, "forgot_identifying_code");
                break;
            case R.id.btn_sign_in:
                retrievePwd();
                MobclickAgent.onEvent(this, "forgot_register");
                break;
        }
    }

    private void retrievePwd() {
        String phone = mEditNumber.getEditText();
        String password = mEditPwd.getEditText();
        String pwd = UIUtils.sha512(phone, password);
        String authcode = mEditCode.getText().toString();
        if (!StringUtils.isMobile(phone)) {
            return;
        }
        if (!StringUtils.checkPassword(password)) {
            return;
        }
        if (authcode.length() == 0) {
            Toast.makeText(mContext, "请输入验证码", Toast.LENGTH_SHORT).show();
            return;
        }
        String s = "" + mCodeInfo.getAuthCode();
        if (mCodeInfo != null && !s.equals(authcode)) {
            Toast.makeText(mContext, "验证码不正确", Toast.LENGTH_SHORT).show();
            return;
        }
        mPresenter.retrievePwd(phone, pwd, authcode);
    }


}
