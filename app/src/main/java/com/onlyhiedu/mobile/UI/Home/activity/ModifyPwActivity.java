package com.onlyhiedu.mobile.UI.Home.activity;

import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.onlyhiedu.mobile.Base.SimpleActivity;
import com.onlyhiedu.mobile.R;
import com.onlyhiedu.mobile.Utils.DialogListener;
import com.onlyhiedu.mobile.Utils.DialogUtil;

import butterknife.BindView;

/**
 * Created by pengpeng on 2017/3/3.
 */

public class ModifyPwActivity extends SimpleActivity implements View.OnClickListener {

    @BindView(R.id.edit_old_pw)
    EditText mEdit_Old_Number;
    @BindView(R.id.edit_new_pw)
    EditText mEdit_New_Pw;
    @BindView(R.id.edit_confirm_pw)
    EditText mEdit_Confirm_Pw;

    @BindView(R.id.btn_confirm)
    Button mBtn_Confirm;
    @Override
    protected int getLayout() {

        return R.layout.activity_modify;
    }

    @Override
    protected void initEventAndData() {
        initListener();
    }

    private void initListener() {
        mBtn_Confirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_confirm:
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
                break;
        }
    }
}
