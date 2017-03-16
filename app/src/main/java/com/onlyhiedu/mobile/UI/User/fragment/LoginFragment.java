package com.onlyhiedu.mobile.UI.User.fragment;

import android.content.Intent;
import android.view.View;

import com.onlyhiedu.mobile.Base.SimpleFragment;
import com.onlyhiedu.mobile.R;
import com.onlyhiedu.mobile.UI.User.activity.FindPwdActivity;
import com.onlyhiedu.mobile.Widget.InputTextView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/3/7.
 */

public class LoginFragment extends SimpleFragment {

    @BindView(R.id.edit_pwd)
    InputTextView mEditPwd;
    @BindView(R.id.edit_confirm_pwd)
    InputTextView mEditConfirmPwd;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_login;
    }

    @Override
    protected void initEventAndData() {

    }


    @OnClick({R.id.btn_sign, R.id.tv_forget_pwd})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_sign:
                break;
            case R.id.tv_forget_pwd:
                startActivity(new Intent(mContext, FindPwdActivity.class));
                break;
        }
    }
}
