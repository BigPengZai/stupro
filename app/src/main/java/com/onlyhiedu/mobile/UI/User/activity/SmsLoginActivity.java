package com.onlyhiedu.mobile.UI.User.activity;

import android.view.View;
import android.widget.EditText;

import com.onlyhiedu.mobile.Base.SimpleActivity;
import com.onlyhiedu.mobile.R;

import butterknife.BindView;
import butterknife.OnClick;

public class SmsLoginActivity extends SimpleActivity {


    @BindView(R.id.edit_number)
    EditText mEditNumber;
    @BindView(R.id.edit_code)
    EditText mEditCode;

    @Override
    protected int getLayout() {
        return R.layout.activity_sms_login;
    }

    @Override
    protected void initEventAndData() {

    }


    @OnClick({R.id.tv_code, R.id.btn_sign})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_code:
                break;
            case R.id.btn_sign:
                break;
        }
    }
}
