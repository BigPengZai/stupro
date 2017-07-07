package com.onlyhiedu.mobile.UI.User.fragment;

import android.content.Intent;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.onlyhiedu.mobile.Base.BaseFragment;
import com.onlyhiedu.mobile.R;
import com.onlyhiedu.mobile.UI.Home.activity.MainActivity;
import com.onlyhiedu.mobile.UI.User.activity.FindPwdActivity;
import com.onlyhiedu.mobile.UI.User.presenter.LoginPresenter;
import com.onlyhiedu.mobile.UI.User.presenter.contract.LoginContract;
import com.onlyhiedu.mobile.Utils.Encrypt;
import com.onlyhiedu.mobile.Utils.SPUtil;
import com.onlyhiedu.mobile.Utils.StringUtils;
import com.onlyhiedu.mobile.Utils.UIUtils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.umeng.message.common.inter.ITagManager;
import com.umeng.message.tag.TagManager;
import com.umeng.socialize.bean.SHARE_MEDIA;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/3/7.
 */

public class LoginFragment extends BaseFragment<LoginPresenter> implements LoginContract.View {


    /* @BindView(R.id.edit_pwd)
         InputTextView mEditPwd;
         @BindView(R.id.edit_confirm_pwd)
         InputTextView mEditConfirmPwd;*/
    private boolean isChecked = true;
    @BindView(R.id.edit_number)
    EditText mEdit_Num;
    @BindView(R.id.edit_pwd)
    EditText mEdit_Pwd;
    @BindView(R.id.img_show)
    ImageView mImg_Show;
    @BindView(R.id.btn_sign)
    Button mBtnSign;

    public static final String TAG = LoginFragment.class.getSimpleName();

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_login;
    }

    @Override
    protected void initView() {
        mEdit_Num.setText(SPUtil.getPhone());
        UIUtils.initCursor(mEdit_Num);
        UIUtils.setTextChanged(mBtnSign, mEdit_Num);
        if (!TextUtils.isEmpty(mEdit_Num.getText().toString())) {
            mBtnSign.setEnabled(true);
        }
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.btn_sign, R.id.tv_forget_pwd, R.id.img_show})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_sign:
                toLogin();
                MobclickAgent.onEvent(mContext, "login_login");
                break;
            case R.id.tv_forget_pwd:
                startActivity(new Intent(mContext, FindPwdActivity.class));
                break;
            case R.id.img_show:
                showPwd();
                break;
        }
    }

    @Override
    public void showUser() {
//        mPresenter.setPushToken(PushAgent.getInstance(mContext).getRegistrationId());
        addUTag();
        startActivity(new Intent(getActivity(), MainActivity.class));
        getActivity().finish();
    }

    private void addUTag() {
        //tag 手机号码 md5
        String tag = Encrypt.getMD5(mEdit_Num.getText().toString());
        Log.d(TAG, "tag:" + tag + "长度：" + tag.length());
        PushAgent.getInstance(mContext).getTagManager().add(new TagManager.TCallBack() {
            @Override
            public void onMessage(final boolean isSuccess, final ITagManager.Result result) {
                //isSuccess表示操作是否成功
                Log.d(TAG, "ITag:" + result);
                if (isSuccess) {
                    mPresenter.setPushToken(PushAgent.getInstance(mContext).getRegistrationId(), tag);
                }
            }
        }, tag);
    }

    @Override
    public void setPush() {

    }

    @Override
    public void isShowBingActivity(String token, String phone, SHARE_MEDIA share_media, String uid) {

    }

    @Override
    public void emcRegisterSucess() {

    }

    @Override
    public void showError(String msg) {

    }

    private void toLogin() {
        String number = mEdit_Num.getText().toString();
        String pwd = mEdit_Pwd.getText().toString();
        if (StringUtils.isMobile(number) && StringUtils.checkPassword(pwd)) {
            mPresenter.getUser(number, pwd, StringUtils.getDeviceId(mContext));
            Log.d(TAG, "StringUtils.getDeviceId():" + StringUtils.getDeviceId(mContext));
        }
    }

    private void showPwd() {
        if (isChecked) {
            mEdit_Pwd.setTransformationMethod(HideReturnsTransformationMethod
                    .getInstance());
            isChecked = false;
            mImg_Show.setImageResource(R.mipmap.visible);
        } else {
            mEdit_Pwd.setTransformationMethod(PasswordTransformationMethod
                    .getInstance());
            isChecked = true;
            mImg_Show.setImageResource(R.mipmap.ic_pwd_hide);
        }
        UIUtils.initCursor(mEdit_Pwd);
    }


}
