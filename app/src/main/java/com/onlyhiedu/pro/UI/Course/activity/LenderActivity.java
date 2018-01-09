package com.onlyhiedu.pro.UI.Course.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;

import com.onlyhiedu.pro.Base.SimpleActivity;
import com.onlyhiedu.pro.R;
import com.onlyhiedu.pro.Utils.StringUtils;

import butterknife.BindView;
import butterknife.OnClick;

import static com.onlyhiedu.pro.R.id.btn_confirm;

/**
 * Created by Administrator on 2017/8/11.
 */

public class LenderActivity extends SimpleActivity {

    @BindView(R.id.edit_name)
    EditText mEditName;
    @BindView(R.id.edit_phone)
    EditText mEditPhone;


    @Override
    protected int getLayout() {
        return R.layout.activity_lender;
    }

    @Override
    protected void initEventAndData() {
        setToolBar("贷款人信息");
    }



    @OnClick({btn_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case btn_confirm:
                String name = mEditName.getText().toString();
                String phone = mEditPhone.getText().toString();
                if (StringUtils.checkAccountMark(name) && StringUtils.isMobile(phone)) {
                    setResult(Activity.RESULT_OK, new Intent().putExtra("name", name).putExtra("phone", phone));
                    finish();
                }
                break;
        }
    }

}
