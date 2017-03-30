package com.onlyhiedu.mobile.UI.User.fragment;

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
import com.onlyhiedu.mobile.R;
import com.onlyhiedu.mobile.UI.User.presenter.RegPresenter;
import com.onlyhiedu.mobile.UI.User.presenter.contract.RegContract;
import com.onlyhiedu.mobile.Utils.StringUtils;
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
    @BindView(R.id.edit_number)
    InputTextView mEditNumber;
    @BindView(R.id.edit_code)
    EditText mEditCode;
    @BindView(R.id.edit_name)
    InputTextView mEditName;
    @BindView(R.id.edit_pwd)
    InputTextView mEditPwd;
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

    }




    @OnClick({R.id.btn_next_number, R.id.btn_next_name, R.id.btn_register,R.id.tv_code,R.id.cb_check})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_next_number:
                nextNumber();
                break;
            case R.id.btn_next_name:
                nextCodeAndName();
                break;
            case R.id.btn_register:
                nextRegister();
                MobclickAgent.onEvent(mContext,"register_register");
                break;
            case R.id.tv_code:
                MobclickAgent.onEvent(mContext,"register_identifying_code");
                break;
            case R.id.cb_check:
                MobclickAgent.onEvent(mContext,"register_clause");
                break;
        }
    }

    private void nextNumber() {
        mTvCode.setEnabled(false);
        mPresenter.readSecond();

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
        String confirmPwd = mEditConfirmPwd.getEditText();
        if (StringUtils.checkPassword(pwd) && StringUtils.checkPassword(confirmPwd)) {

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
    public void showError(String msg) {
        Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
    }
}
