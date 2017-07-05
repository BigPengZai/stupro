package com.onlyhiedu.mobile.UI.User.activity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.onlyhiedu.mobile.Base.BaseActivity;
import com.onlyhiedu.mobile.Model.bean.AuthUserDataBean;
import com.onlyhiedu.mobile.Model.bean.UserIsRegister;
import com.onlyhiedu.mobile.R;
import com.onlyhiedu.mobile.UI.User.presenter.BindPresenter;
import com.onlyhiedu.mobile.UI.User.presenter.contract.BindContract;
import com.onlyhiedu.mobile.Utils.StringUtils;
import com.umeng.socialize.bean.SHARE_MEDIA;

import butterknife.BindView;
import butterknife.OnClick;


public class BindActivity extends BaseActivity<BindPresenter> implements BindContract.View {

    public static final String TAG = BindActivity.class.getSimpleName();
    public static final String share_media = "share_media";
    public static final String share_media_uid = "share_media_uid";

    @BindView(R.id.edit_phone)
    EditText mEditPhone;
    @BindView(R.id.edit_name)
    EditText mEditName;
    @BindView(R.id.btn_code)
    Button mBtnCode;


    private SHARE_MEDIA mBindType;   //三方登录类型
    private String uid;//第三方登录之后返回uid；

    @Override
    protected void initView() {
        mBindType = (SHARE_MEDIA) getIntent().getSerializableExtra(share_media);
        uid = getIntent().getStringExtra(share_media_uid);
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
    public void showRegState(UserIsRegister data) {

        //手机号已注册，显示username
        if (data.registerFlag) {
            Toast.makeText(this, "手机注册过", Toast.LENGTH_SHORT).show();
            mEditName.setText(data.userName == null ? "" : data.userName);
        }

    }

    @Override
    public void showBindUser(AuthUserDataBean data) {
        Log.d(TAG,data.toString());

    }


    @Override
    public void showError(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }



    @OnClick({R.id.btn_code, R.id.btn_bind})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_code:
                String phone = mEditPhone.getText().toString();
                if (StringUtils.isMobile(phone)) {
                    mPresenter.getRegState(phone);
                } else {
                    mEditPhone.setError("???????");
                }
                break;
            case R.id.btn_bind:
                String name = mEditName.getText().toString();
                String phone2 = mEditPhone.getText().toString();

                 //验证还没写

                mPresenter .bindUser(mBindType,uid,phone2,name);

                break;
        }
    }



}
