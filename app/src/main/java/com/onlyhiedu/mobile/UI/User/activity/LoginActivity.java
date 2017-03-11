package com.onlyhiedu.mobile.UI.User.activity;

import android.content.Intent;
import android.os.Build;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.onlyhiedu.mobile.Base.SimpleActivity;
import com.onlyhiedu.mobile.R;
import com.onlyhiedu.mobile.UI.Home.activity.MainActivity;
import com.onlyhiedu.mobile.Utils.StringUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends SimpleActivity {


    private boolean isChecked = true;
    public static final String TAG = LoginActivity.class.getSimpleName();

    @BindView(R.id.edit_number)
    EditText mEditNumber;
    @BindView(R.id.edit_pwd)
    EditText mEditPwd;
    @BindView(R.id.img_show)
    ImageView mImg_Show;

    @Override
    protected int getLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void initEventAndData() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    @OnClick({R.id.tv_sms_sign, R.id.tv_find_pwd, R.id.btn_sign, R.id.btn_sign_in, R.id.img_show, R.id.edit_number})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_sms_sign:
                startActivity(new Intent(this, SmsLoginActivity.class));
                break;
            case R.id.tv_find_pwd:
                startActivity(new Intent(this, FindPwdActivity.class));
                break;
            case R.id.btn_sign:
                toLogin();
                break;
            case R.id.btn_sign_in:
                startActivity(new Intent(this, RegActivity.class));
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
        initEditText();
    }

    private void initEditText() {
        CharSequence s = mEditPwd.getText();
        if (s instanceof Spannable) {
            Spannable spanText = (Spannable) s;
            Selection.setSelection(spanText, s.length());
        }
    }

    private void toLogin() {
//        String number = mEditNumber.getText().toString();
//        String pwd = mEditPwd.getText().toString();
//        if (StringUtils.isMobile(number) && StringUtils.checkPassword(pwd)) {
//            startActivity(new Intent(this, MainActivity.class));
//            finish();
//        }

        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
