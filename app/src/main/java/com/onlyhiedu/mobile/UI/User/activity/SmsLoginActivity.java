package com.onlyhiedu.mobile.UI.User.activity;

import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.onlyhiedu.mobile.Base.BaseActivity;
import com.onlyhiedu.mobile.R;
import com.onlyhiedu.mobile.UI.User.presenter.SmsLoginPresenter;
import com.onlyhiedu.mobile.UI.User.presenter.contract.SmsLoginContract;
import com.onlyhiedu.mobile.Utils.StringUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class SmsLoginActivity extends BaseActivity<SmsLoginPresenter> implements SmsLoginContract.View {

    @BindView(R.id.edit_number)
    EditText mEditNumber;
    @BindView(R.id.edit_code)
    EditText mEditCode;
    @BindView(R.id.tv_code)
    TextView mTvCode;


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
    public void showError(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }

    @OnClick({R.id.tv_code, R.id.btn_sign, R.id.ic_delete, R.id.rl_pwd_login, R.id.btn_sign_in})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_code:

                getMsgCode();
                break;
            case R.id.btn_sign:

                toLogin();
                break;
            case R.id.ic_delete:

                finish();
                break;
            case R.id.rl_pwd_login:

                finish();
            case R.id.btn_sign_in:

                startActivity(new Intent(this, RegActivity.class));
                break;
        }
    }

    private void toLogin() {
        String number = mEditNumber.getText().toString();
        String code = mEditCode.getText().toString();

    }

    private void getMsgCode() {
        String number = mEditNumber.getText().toString();
        if (StringUtils.isMobile(number)) {
            mTvCode.setEnabled(false);
            mPresenter.readSecond();
        }
    }


}
