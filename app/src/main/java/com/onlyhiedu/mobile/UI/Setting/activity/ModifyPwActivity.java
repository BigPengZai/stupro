package com.onlyhiedu.mobile.UI.Setting.activity;

import android.content.DialogInterface;

import com.onlyhiedu.mobile.Base.SimpleActivity;
import com.onlyhiedu.mobile.R;
import com.onlyhiedu.mobile.Utils.DialogListener;
import com.onlyhiedu.mobile.Utils.DialogUtil;
import com.onlyhiedu.mobile.Widget.InputTextView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by pengpeng on 2017/3/3.
 */

public class ModifyPwActivity extends SimpleActivity {

    @BindView(R.id.edit_old_pw)
    InputTextView mEdit_Old_Number;
    @BindView(R.id.edit_new_pw)
    InputTextView mEdit_New_Pw;
    @BindView(R.id.edit_confirm_pw)
    InputTextView mEdit_Confirm_Pw;


    @Override
    protected int getLayout() {
        return R.layout.activity_modify;
    }

    @Override
    protected void initEventAndData() {
        setToolBar("设置");
    }


    @OnClick(R.id.btn_confirm)
    public void onClick() {
        DialogUtil.showOnlyAlert(this,
                "提示"
                , "修改成功"
                , ""
                , ""
                , true, true, new DialogListener() {
                    @Override
                    public void onPositive(DialogInterface dialog) {

                    }

                    @Override
                    public void onNegative(DialogInterface dialog) {

                    }
                }
        );
    }
}
