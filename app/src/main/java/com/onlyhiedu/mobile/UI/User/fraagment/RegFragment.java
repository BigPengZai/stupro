package com.onlyhiedu.mobile.UI.User.fraagment;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.onlyhiedu.mobile.Base.SimpleFragment;
import com.onlyhiedu.mobile.R;
import com.onlyhiedu.mobile.Widget.InputTextView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/3/7.
 */

public class RegFragment extends SimpleFragment {

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
    @BindView(R.id.edit_pwd)
    InputTextView mEditPwd;
    @BindView(R.id.edit_confirm_pw)
    InputTextView mEditConfirmPw;
    @BindView(R.id.ll_reg_step3)
    LinearLayout mLlRegStep3;
    @BindView(R.id.ll_reg_step2)
    LinearLayout mLlRegStep2;
    @BindView(R.id.ll_reg_step1)
    LinearLayout mLlRegStep1;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_reg;
    }

    @Override
    protected void initEventAndData() {

    }


    @OnClick({R.id.btn_next_number, R.id.btn_next_name, R.id.btn_register})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_next_number:
                nextNumber();
                break;
            case R.id.btn_next_name:
                nestCodeAndName();
                break;
            case R.id.btn_register:
                nestRegister();
                break;
        }
    }

    private void nextNumber() {
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_left_out);
        animation.setFillAfter(true);
        mLlRegStep1.startAnimation(animation);
        mBtnNextNumber.startAnimation(animation);
        Animation animation2 = AnimationUtils.loadAnimation(mContext, R.anim.slide_right_in);
        animation.setFillAfter(true);
        mLlRegStep2.startAnimation(animation2);
        mBtnNextName.startAnimation(animation2);
        mLlRegStep2.setVisibility(View.VISIBLE);
        mBtnNextName.setVisibility(View.VISIBLE);

    }

    private void nestCodeAndName() {
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_left_out);
        animation.setFillAfter(true);
        mLlRegStep2.startAnimation(animation);
        mBtnNextName.startAnimation(animation);
        Animation animation2 = AnimationUtils.loadAnimation(mContext, R.anim.slide_right_in);
        animation.setFillAfter(true);
        mLlRegStep3.startAnimation(animation2);
        mBtnRegister.startAnimation(animation2);
        mBtnRegister.setVisibility(View.VISIBLE);
        mLlRegStep3.setVisibility(View.VISIBLE);
    }

    private void nestRegister() {

    }

}
