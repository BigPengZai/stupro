package com.onlyhiedu.mobile.UI.Home.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.onlyhiedu.mobile.App.Constants;
import com.onlyhiedu.mobile.R;
import com.onlyhiedu.mobile.UI.User.activity.OpenIDActivity;
import com.onlyhiedu.mobile.Utils.SPUtil;


/**
 * Created by pengpeng on 2017/3/14.
 * demo  后期要更改
 */

public class SplashActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                if (TextUtils.isEmpty(SPUtil.getToken())) {
                    Log.d(Constants.TAG,SPUtil.getToken());
                    startActivity(new Intent(SplashActivity.this, OpenIDActivity.class));
                } else {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                }
                finish();
            }
        }, 1000);
    }
}
