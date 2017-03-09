package com.onlyhiedu.mobile.UI.User.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.onlyhiedu.mobile.Base.BaseActivity;
import com.onlyhiedu.mobile.R;
import com.onlyhiedu.mobile.UI.User.presenter.FindPwdPresenter;
import com.onlyhiedu.mobile.UI.User.presenter.contract.FindPwdContract;
import com.onlyhiedu.mobile.Widget.InputTextView;

import butterknife.BindView;
import butterknife.OnClick;

public class FindPwdActivity extends BaseActivity<FindPwdPresenter> implements FindPwdContract.View {


    @BindView(R.id.edit_number)
    InputTextView mEditNumber;
    @BindView(R.id.edit_pwd)
    InputTextView mEditPwd;
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
        return R.layout.activity_find_pwd;
    }


    @Override
    protected void initView() {
        setToolBar("找回密码", R.mipmap.close);
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


    @OnClick({R.id.tv_code, R.id.btn_sign_in})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_code:
                mTvCode.setEnabled(false);
                mPresenter.readSecond();
                break;
            case R.id.btn_sign_in:
                break;
        }
    }


}
