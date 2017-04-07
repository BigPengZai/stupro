package com.onlyhiedu.mobile.UI.User.fragment;

import android.content.Intent;
import android.nfc.Tag;
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

import com.onlyhiedu.mobile.Base.BaseFragment;
import com.onlyhiedu.mobile.Model.bean.AuthCodeInfo;
import com.onlyhiedu.mobile.R;
import com.onlyhiedu.mobile.UI.User.presenter.RegPresenter;
import com.onlyhiedu.mobile.UI.User.presenter.contract.RegContract;
import com.onlyhiedu.mobile.Utils.StringUtils;
import com.onlyhiedu.mobile.Utils.UIUtils;
import com.onlyhiedu.mobile.Widget.InputTextView;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/3/7.
 */

public class RegFragment extends BaseFragment<RegPresenter> implements RegContract.View {


    private boolean mPwd;
    private boolean mConfirmPwd;

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
    public static final String TAG = RegFragment.class.getSimpleName();

    private AuthCodeInfo mCodeInfo;

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_reg;
    }

    @Override
    protected void initView() {

        mEditPwd.setPassword(true);
        mEditConfirmPwd.setPassword(true);

    }

    @Override
    protected void initData() {
        mCodeInfo = new AuthCodeInfo();
    }


    @OnClick({R.id.btn_next_number, R.id.btn_next_name, R.id.btn_register, R.id.tv_code, R.id.cb_check})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_next_number:
                //下一步
                if (StringUtils.isMobile(mEditNumber.getEditText())) {
                    nextNumber();
                }
                break;
            case R.id.btn_next_name:
                if (mCodeInfo != null) {
                    confirmSecond();
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
        }
    }
    //第二步验证
    private void confirmSecond() {
        Log.d(TAG, "验证码"+mCodeInfo.getAuthCode());
        if (mEditCode.getText().length() == 0) {
            Toast.makeText(mContext, "请填写验证码信息", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mEditName.getEditText().length() == 0) {
            Toast.makeText(mContext, "请填写姓名", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!mEditCode.getText().toString().equals(mCodeInfo.getAuthCode())) {
            Toast.makeText(mContext, "验证码信息错误", Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringUtils.checkAccountMark(mEditName.getEditText())) {
            nextCodeAndName();
        }
    }

    //第三步验证
    private void confirmThird() {
        if (mEditPwd.getEditText().length() == 0) {
            Toast.makeText(mContext, "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mEditConfirmPwd.getEditText().length() == 0) {
            Toast.makeText(mContext, "请再次输入确认密码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!mEditPwd.getEditText().equals(mEditConfirmPwd.getEditText())) {
            Toast.makeText(mContext, "两次密码不一致", Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringUtils.checkPassword(mEditPwd.getEditText())) {
            nextRegister();
        }

    }
    private void nextNumber() {
        mTvCode.setEnabled(false);
        mPresenter.readSecond();
        mPresenter.getAuthCode(mEditNumber.getEditText());
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_left_out);
        animation.setFillAfter(true);
        mLlRegStep1.startAnimation(animation);
        mBtnNextNumber.startAnimation(animation);
        Animation animation2 = AnimationUtils.loadAnimation(mContext, R.anim.slide_right_in);
        animation.setFillAfter(true);

        setAnimation(animation2, mLlRegStep2, mBtnNextName);

        mEditNumber.setInputEnable(false);
    }

    private void nextCodeAndName() {
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_left_out);
        animation.setFillAfter(true);
        mLlRegStep2.startAnimation(animation);
        mBtnNextName.startAnimation(animation);
        Animation animation2 = AnimationUtils.loadAnimation(mContext, R.anim.slide_right_in);
        animation.setFillAfter(true);

        setAnimation(animation2, mBtnRegister, mLlRegStep3);

        mEditCode.setEnabled(false);
        mEditName.setInputEnable(false);
    }

    private void nextRegister() {
        String pwd = mEditPwd.getEditText();
        String phone = mEditNumber.getEditText();
        String pw = UIUtils.sha512(phone, pwd);
        String username = mEditName.getEditText();
        String confirmPwd = mEditConfirmPwd.getEditText();
        if (StringUtils.checkPassword(pwd)
                && StringUtils.checkPassword(confirmPwd)
                && username != null
                && phone != null) {
            mPresenter.registerUser(username, phone, pw);
        }

    }

    private void setAnimation(Animation animation, View view, View view2) {
        view.startAnimation(animation);
        view2.startAnimation(animation);
        view.setVisibility(View.VISIBLE);
        view2.setVisibility(View.VISIBLE);
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
    public void showSuccess(String info) {
        if (info != null) {
            Log.d(TAG, "注册完成");
            Toast.makeText(getContext(), info, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.putExtra("username", mEditNumber.getEditText());
            getActivity().setResult(11, intent);
            getActivity().finish();
        }
    }

    @Override
    public void showAuthSuccess(AuthCodeInfo info) {
        if (info != null) {
            Log.d(TAG, "验证码：" + info.getAuthCode());
            mCodeInfo.setAuthCode(info.getAuthCode());
        }
    }

    @Override
    public void showError(String msg) {
        Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
    }
}
