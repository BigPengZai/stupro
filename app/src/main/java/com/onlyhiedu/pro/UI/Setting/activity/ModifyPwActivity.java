package com.onlyhiedu.pro.UI.Setting.activity;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.onlyhiedu.pro.Base.BaseActivity;
import com.onlyhiedu.pro.R;
import com.onlyhiedu.pro.UI.Setting.presenter.ModifyPwPresenter;
import com.onlyhiedu.pro.UI.Setting.presenter.contract.ModifyPwContract;
import com.onlyhiedu.pro.Utils.StringUtils;
import com.onlyhiedu.pro.Utils.UIUtils;
import com.onlyhiedu.pro.Widget.InputTextView;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by pengpeng on 2017/3/3.
 */

public class ModifyPwActivity extends BaseActivity<ModifyPwPresenter> implements ModifyPwContract.View {


    @BindView(R.id.edit_old_pw)
    InputTextView mEdit_Old_Number;
    @BindView(R.id.edit_new_pw)
    InputTextView mEdit_New_Pw;
    @BindView(R.id.edit_confirm_pw)
    InputTextView mEdit_Confirm_Pw;
    @BindView(R.id.btn_confirm)
    Button mBtnConfirm;

    private boolean mOldNumberIsNull = true;
    private boolean mNewPwdIsNuLL = true;
    private boolean mConfirmPwIsNuLL = true;

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_modify;
    }

    @Override
    protected void initView() {
        setToolBar("修改密码");

        mEdit_Old_Number.getEditTextView().addTextChangedListener(mTextWatcher);
        mEdit_New_Pw.getEditTextView().addTextChangedListener(mTextWatcher2);
        mEdit_Confirm_Pw.getEditTextView().addTextChangedListener(mTextWatcher3);
    }

    TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (TextUtils.isEmpty(mEdit_Old_Number.getEditText())) {
                mOldNumberIsNull = true;
            } else {
                mOldNumberIsNull = false;
            }
            btnEnable();
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    };

    TextWatcher mTextWatcher2 = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (TextUtils.isEmpty(mEdit_New_Pw.getEditText())) {
                mNewPwdIsNuLL = true;
            } else {
                mNewPwdIsNuLL = false;
            }
            btnEnable();
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    };

    TextWatcher mTextWatcher3 = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (mEdit_Confirm_Pw.getEditText().isEmpty()) {
                mConfirmPwIsNuLL = true;
            } else {
                mConfirmPwIsNuLL = false;
            }
            btnEnable();
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    };

    public void btnEnable() {
        if (!mOldNumberIsNull && !mNewPwdIsNuLL && !mConfirmPwIsNuLL) {
            mBtnConfirm.setEnabled(true);
        } else {
            mBtnConfirm.setEnabled(false);
        }
    }


    @OnClick(R.id.btn_confirm)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_confirm:

                String newPwd = mEdit_New_Pw.getEditText();
                String configPwd = mEdit_Confirm_Pw.getEditText();

                if(StringUtils.checkPassword(newPwd) && StringUtils.checkPassword(configPwd)){
                    if(newPwd.equals(configPwd)){
                        MobclickAgent.onEvent(this, "modify_confirm_pw");
                        mPresenter.updatePassword(mEdit_Old_Number.getEditText(), System.currentTimeMillis(), newPwd);
                    }else{
                        Toast.makeText(mContext, "两次密码输入不一致", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }


    @Override
    public void showUpdate(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
        UIUtils.startLoginActivity(this);
    }

    @Override
    public void showError(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }

}

