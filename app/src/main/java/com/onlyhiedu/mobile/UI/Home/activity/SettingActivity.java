package com.onlyhiedu.mobile.UI.Home.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.onlyhiedu.mobile.Base.SimpleActivity;
import com.onlyhiedu.mobile.R;

import butterknife.BindView;

/**
 * Created by pengpeng on 2017/3/2.
 */

public class SettingActivity extends SimpleActivity implements View.OnClickListener {
    @BindView(R.id.tv_modify_pw)
    TextView mTv_Modify_Pw;

    @BindView(R.id.tv_feed_back)
    TextView mTv_Feed_Back;

    @BindView(R.id.tv_log_off)
    TextView mTv_Log_Off;
    @Override
    protected int getLayout() {

        return R.layout.activity_setting;
    }

    @Override
    protected void initEventAndData() {
        initListenr();
    }

    private void initListenr() {
        mTv_Modify_Pw.setOnClickListener(this);
        mTv_Feed_Back.setOnClickListener(this);
        mTv_Log_Off.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_modify_pw:
             startActivity(new Intent(this,ModifyPwActivity.class));
                break;
            case R.id.tv_feed_back:
                startActivity(new Intent(this,FeedBackActivity.class));
                break;
            case R.id.tv_log_off:
//                finish();
                break;
        }
    }
}
