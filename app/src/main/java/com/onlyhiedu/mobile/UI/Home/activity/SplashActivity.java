package com.onlyhiedu.mobile.UI.Home.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.onlyhiedu.mobile.R;
import com.onlyhiedu.mobile.UI.User.activity.LoginActivity;


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

                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();
            }
        }, 2000);
    }
}
