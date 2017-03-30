package com.onlyhiedu.mobile.UI.Setting.activity;

import android.view.View;
import android.widget.Toast;

import com.onlyhiedu.mobile.Base.BaseActivity;
import com.onlyhiedu.mobile.R;
import com.onlyhiedu.mobile.UI.Setting.presenter.ModifyPwPresenter;
import com.onlyhiedu.mobile.UI.Setting.presenter.contract.ModifyPwContract;
import com.onlyhiedu.mobile.Utils.DialogUtil;
import com.onlyhiedu.mobile.Widget.InputTextView;
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
        setToolBar("设置");
    }

    @OnClick(R.id.btn_confirm)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_confirm:
                String newPwd = mEdit_New_Pw.getEditText();
                String configPwd = mEdit_Confirm_Pw.getEditText();

                if(newPwd.equals(configPwd)){
                    mPresenter.updatePassword(mEdit_Old_Number.getEditText(), System.currentTimeMillis(), newPwd);
                }else{
                    Toast.makeText(mContext, "两次密码输入不一致", Toast.LENGTH_SHORT).show();
                }
                MobclickAgent.onEvent(this,"modify_confirm_pw");
                break;
        }
    }

    @Override
    public void showUpdate(String msg) {
        mEdit_Old_Number.clean();
        mEdit_New_Pw.clean();
        mEdit_Confirm_Pw.clean();
        DialogUtil.showOnlyAlert(this,
                "提示"
                , msg
                , ""
                , ""
                , true, true, null
        );
    }

    @Override
    public void showError(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }
}
