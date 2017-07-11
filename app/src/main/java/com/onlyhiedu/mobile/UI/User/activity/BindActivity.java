package com.onlyhiedu.mobile.UI.User.activity;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.onlyhiedu.mobile.Base.BaseActivity;
import com.onlyhiedu.mobile.Model.bean.AuthUserDataBean;
import com.onlyhiedu.mobile.R;
import com.onlyhiedu.mobile.UI.User.presenter.BindPresenter;
import com.onlyhiedu.mobile.UI.User.presenter.contract.BindContract;
import com.onlyhiedu.mobile.Utils.StringUtils;
import com.onlyhiedu.mobile.Widget.InputTextView;
import com.umeng.socialize.bean.SHARE_MEDIA;

import butterknife.BindView;
import butterknife.OnClick;



public class BindActivity extends BaseActivity<BindPresenter> implements BindContract.View {

    public static final String TAG = BindActivity.class.getSimpleName();
    public static final String share_media = "share_media";
    public static final String share_media_uid = "share_media_uid";

    @BindView(R.id.edit_phone)
    InputTextView mEditPhone;
    @BindView(R.id.edit_code)
    EditText mEditCode;
    @BindView(R.id.tv_code)
    TextView mTvCode;
    @BindView(R.id.edit_name)
    InputTextView mEditName;
    @BindView(R.id.btn_bind)
    Button mBtnBind;


    private SHARE_MEDIA mBindType;   //三方登录类型
    private String uid;//第三方登录之后返回uid；
    private int mServeCode;  //服务端发过来的验证码

    @Override
    protected void initView() {
        mBindType = (SHARE_MEDIA) getIntent().getSerializableExtra(share_media);
        uid = getIntent().getStringExtra(share_media_uid);

        mEditPhone.setButton(mBtnBind);
        setToolBar("绑定手机号");
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_bind;
    }


    @Override
    public void showBindUser(AuthUserDataBean data) {
        Log.d(TAG, data.toString());
    }

    @Override
    public void getAuthCodeSuccess(int data) {
        mServeCode = data;
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
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


    @OnClick({R.id.tv_code, R.id.btn_bind})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_code:
                String phone = mEditPhone.getEditText();
                if (StringUtils.isMobile(phone)) {
                    mTvCode.setEnabled(false);
                    mPresenter.getAuthCode(phone);
                    mPresenter.readSecond();
                }
                break;
            case R.id.btn_bind:
                String name = mEditName.getEditText();
                String phone2 = mEditPhone.getEditText();
                String code = mEditCode.getText().toString();
                if(!StringUtils.isMobile(phone2)  ){
                    return;
                }
                if(TextUtils.isEmpty(code)){
                    mEditCode.setError("验证码不能为空");
                    return;
                }
                if(TextUtils.isEmpty(name)){
                    mEditName.getEditTextView().setError("姓名不能为空");
                    return;
                }
                if(!code.equals(mServeCode)){
                    Toast.makeText(this, "验证码不正确", Toast.LENGTH_SHORT).show();
                }
                mPresenter.bindUser(mBindType,uid,phone2,name,code);
                break;
        }
    }

}
