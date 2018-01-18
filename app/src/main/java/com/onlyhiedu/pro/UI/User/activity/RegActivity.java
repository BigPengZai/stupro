package com.onlyhiedu.pro.UI.User.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.onlyhiedu.pro.App.AppManager;
import com.onlyhiedu.pro.Base.BaseActivity;
import com.onlyhiedu.pro.Model.event.MainActivityTabSelectPos;
import com.onlyhiedu.pro.R;
import com.onlyhiedu.pro.UI.Home.activity.MainActivity;
import com.onlyhiedu.pro.UI.User.presenter.RegPresenter;
import com.onlyhiedu.pro.UI.User.presenter.contract.RegContract;
import com.onlyhiedu.pro.Utils.Encrypt;
import com.onlyhiedu.pro.Utils.SPUtil;
import com.onlyhiedu.pro.Utils.StringUtils;
import com.onlyhiedu.pro.Utils.UIUtils;
import com.onlyhiedu.pro.Widget.InputTextView;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.umeng.message.common.inter.ITagManager;
import com.umeng.message.tag.TagManager;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;


public class RegActivity extends BaseActivity<RegPresenter> implements RegContract.View {


    @BindView(R.id.btn_next_number)
    Button mBtnNextNumber;
    @BindView(R.id.btn_next_name)
    Button mBtnNextName;
    @BindView(R.id.btn_register)
    Button mBtnRegister;
    //手机号码
    @BindView(R.id.edit_number)
    InputTextView mEditNumber;
    //验证码输入框
    @BindView(R.id.edit_code)
    EditText mEditCode;
    //姓名
    @BindView(R.id.edit_name)
    InputTextView mEditName;
    //设置密码
    @BindView(R.id.edit_pwd)
    InputTextView mEditPwd;
    //确认密码
    @BindView(R.id.edit_confirm_pwd)
    InputTextView mEditConfirmPwd;
    @BindView(R.id.ll_reg_step3)
    LinearLayout mLlRegStep3;
    @BindView(R.id.ll_reg_step2)
    LinearLayout mLlRegStep2;
    @BindView(R.id.ll_reg_step1)
    LinearLayout mLlRegStep1;
    @BindView(R.id.tv_code)
    TextView mTvCode;

    @BindView(R.id.cb_check)
    CheckBox mCb_Check;
    public static final String TAG = RegActivity.class.getSimpleName();
    private ProgressDialog mProgressDialog;


    @BindView(R.id.ll_term_service)
    LinearLayout mLl_Term_Service;


    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_reg;
    }

    @Override
    protected void initView() {
        setToolBar("注册");
        if (mToolbar != null) {
            mToolbar.setNavigationIcon(R.mipmap.back);
            mToolbar.setBackgroundColor(Color.parseColor("#F42440"));
        }
        mEditPwd.setPassword(true);
        mEditConfirmPwd.setPassword(true);

        mEditNumber.setButton(mBtnNextNumber);
        mEditName.setButton(mBtnNextName);
        mEditPwd.setButton(mBtnRegister);
    }


    @OnClick({R.id.btn_next_number, R.id.btn_next_name, R.id.btn_register, R.id.tv_code, R.id.cb_check, R.id.ll_term_service})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_next_number:
                //下一步
                if (StringUtils.isMobile(mEditNumber.getEditText())) {
                    nextNumber();
                }
                break;
            case R.id.btn_next_name:
                if (mPresenter.confirmThird(mEditCode.getText().toString(), mEditName.getEditText())) {
                    nextCodeAndName();
                }
                break;
            case R.id.btn_register:
                //注册
                confirmThird();
                MobclickAgent.onEvent(mContext, "register_register");
                break;
            case R.id.tv_code:
                //获取验证码
                if ("获取验证码".equals(mTvCode.getText().toString())) {
                    mPresenter.readSecond();
                    mPresenter.getAuthCode(mEditNumber.getEditText());
                    MobclickAgent.onEvent(mContext, "register_identifying_code");
                }
                break;
            case R.id.cb_check:
                MobclickAgent.onEvent(mContext, "register_clause");
                break;
            case R.id.ll_term_service:
                //服务条款
                readService();
                break;
        }
    }

    private void readService() {
        startActivity(new Intent(mContext, TermServiceActivity.class));
    }

    //第三步验证
    private void confirmThird() {
        String pwd = mEditPwd.getEditText();
        String confirmPwd = mEditConfirmPwd.getEditText();
        String phone = mEditNumber.getEditText();

        if (!pwd.equals(confirmPwd)) {
            Toast.makeText(mContext, "两次密码不一致", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!StringUtils.checkPassword(pwd) || !StringUtils.checkPassword(confirmPwd)) {
            return;
        }

        if (mProgressDialog == null) {
            mProgressDialog = ProgressDialog.show(this, null, "请稍后..");
        }

        mPresenter.registerUser(mEditName.getEditText(), phone, UIUtils.sha512(phone, pwd), mEditCode.getText().toString());
    }

    private void nextNumber() {
        mPresenter.isRegister(mEditNumber.getEditText());
    }

    private void nextCodeAndName() {
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_left_out);
        animation.setFillAfter(true);
        mLlRegStep2.startAnimation(animation);
        mBtnNextName.startAnimation(animation);
        Animation animation2 = AnimationUtils.loadAnimation(mContext, R.anim.slide_right_in);
        animation2.setFillAfter(true);
        mBtnRegister.startAnimation(animation2);
        mBtnRegister.setVisibility(View.VISIBLE);
        mLlRegStep3.startAnimation(animation2);
        mLlRegStep3.setVisibility(View.VISIBLE);
        mEditCode.setEnabled(false);
        mEditName.setInputEnable(false);
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
    public void showAuthSuccess() {

        mTvCode.setEnabled(false);
        mPresenter.readSecond();
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_left_out);
        animation.setFillAfter(true);
        mLlRegStep1.startAnimation(animation);
        mBtnNextNumber.startAnimation(animation);

        Animation animation2 = AnimationUtils.loadAnimation(mContext, R.anim.slide_right_in);
        animation2.setFillAfter(true);
        mLlRegStep2.startAnimation(animation2);
        mBtnNextName.startAnimation(animation2);
        mLlRegStep2.setVisibility(View.VISIBLE);
        mBtnNextName.setVisibility(View.VISIBLE);


        mEditNumber.setInputEnable(false);
    }

    @Override
    public void showRegState(boolean isReg) {
        if (!isReg) {
            mPresenter.getAuthCode(mEditNumber.getEditText());
        } else {
            Toast.makeText(this, "手机号码已经注册", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void IMLoginFailure(String s) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
                Toast.makeText(RegActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void showUser() {
        addUTag();
        if (SPUtil.getGuest()) {
            SPUtil.setGuest(false);
            EventBus.getDefault().post(new MainActivityTabSelectPos(0));
        }
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        startActivity(new Intent(this, MainActivity.class));
        finish();
        AppManager.getAppManager().finishActivity(OpenIDActivity.class);
    }


    private void addUTag() {
        //tag 手机号码 md5
        String tag = Encrypt.getMD5(mEditNumber.getEditText());
        Log.d(TAG, "tag:" + tag + "长度：" + tag.length());
        PushAgent.getInstance(this).getTagManager().add(new TagManager.TCallBack() {
            @Override
            public void onMessage(final boolean isSuccess, final ITagManager.Result result) {
                //isSuccess表示操作是否成功
                Log.d(TAG, "ITag:" + result);
                if (isSuccess) {
                    mPresenter.setPushToken(PushAgent.getInstance(RegActivity.this).getRegistrationId(), tag);
                }
            }
        }, tag);
    }

    @Override
    public void setPush() {

    }

    @Override
    public void showError(String msg) {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}