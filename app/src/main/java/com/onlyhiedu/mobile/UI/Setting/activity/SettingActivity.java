package com.onlyhiedu.mobile.UI.Setting.activity;

import android.content.Intent;
import android.view.View;

import com.onlyhiedu.mobile.Base.SimpleActivity;
import com.onlyhiedu.mobile.R;
import com.onlyhiedu.mobile.Utils.UIUtils;

import butterknife.OnClick;

/**
 * Created by pengpeng on 2017/3/2.
 */

public class SettingActivity extends SimpleActivity {


    @Override
    protected int getLayout() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initEventAndData() {
        setToolBar("设置");
    }


    @OnClick({R.id.setting_pwd, R.id.setting_feedback, R.id.setting_about, R.id.btn_sign})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.setting_pwd:
                startActivity(new Intent(this, ModifyPwActivity.class));
                break;
            case R.id.setting_feedback:
                startActivity(new Intent(this, FeedBackActivity.class));
                break;
            case R.id.setting_about:
                startActivity(new Intent(this, AboutActivity.class));
                break;
            case R.id.btn_sign:
                UIUtils.startLoginActivity(this);
                break;

        }
    }
}
