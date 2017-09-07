package com.onlyhiedu.mobile.UI.Home.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.onlyhiedu.mobile.R;
import com.onlyhiedu.mobile.Utils.SPUtil;


/**
 * Created by pengpeng on 2017/3/14.
 * demo  后期要更改
 */

public class SplashActivity extends Activity {
    private static final int sleepTime = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Thread(new Runnable() {
            public void run() {
                if (/*DemoHelper.getInstance().isLoggedIn()&&*/!TextUtils.isEmpty(SPUtil.getToken())) {
                    long start = System.currentTimeMillis();
//                    EMClient.getInstance().chatManager().loadAllConversations();
//                    EMClient.getInstance().groupManager().loadAllGroups();
                    long costTime = System.currentTimeMillis() - start;
                    if (sleepTime - costTime > 0) {
                        try {
                            Thread.sleep(sleepTime - costTime);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                }else {
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                    }
                    SPUtil.setGuest(true);
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                }
            }
        }).start();
    }
}
