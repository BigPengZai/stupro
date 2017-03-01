package com.onlyhiedu.mobile.UI.User.activity;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;

import com.onlyhiedu.mobile.Base.SimpleActivity;
import com.onlyhiedu.mobile.R;
import com.onlyhiedu.mobile.UI.Home.activity.MainActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends SimpleActivity {


    @BindView(R.id.edit_number)
    EditText mEditNumber;
    @BindView(R.id.edit_pwd)
    EditText mEditPwd;

    @Override
    protected int getLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void initEventAndData() {

    }


    @OnClick({R.id.tv_sms_sign, R.id.tv_find_pwd, R.id.btn_sign, R.id.btn_sign_in})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_sms_sign:
                startActivity(new Intent(this, SmsLoginActivity.class));
                break;
            case R.id.tv_find_pwd:
                startActivity(new Intent(this, FindPwdActivity.class));
                break;
            case R.id.btn_sign:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.btn_sign_in:
                startActivity(new Intent(this, RegActivity.class));
                break;
        }
    }
}
