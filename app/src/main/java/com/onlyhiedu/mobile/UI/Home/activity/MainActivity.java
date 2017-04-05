package com.onlyhiedu.mobile.UI.Home.activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.onlyhiedu.mobile.App.AppManager;
import com.onlyhiedu.mobile.Base.SimpleActivity;
import com.onlyhiedu.mobile.R;
import com.onlyhiedu.mobile.UI.Home.fragment.ClassFragment;
import com.onlyhiedu.mobile.UI.Home.fragment.MeFragment;
import com.umeng.analytics.MobclickAgent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;

import butterknife.BindView;

public class MainActivity extends SimpleActivity implements BottomNavigationView.OnNavigationItemSelectedListener {


    private ClassFragment mClassFragment;
    private MeFragment mMeFragment;
    private long mExitTime = 0;

    public static final String TAG = MainActivity.class.getSimpleName();
    @BindView(R.id.navigation)
    BottomNavigationView mNavigation;

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initEventAndData() {
        mClassFragment = new ClassFragment();
        mMeFragment = new MeFragment();
        loadMultipleRootFragment(R.id.fl_main_content, 0, mClassFragment, mMeFragment);
        mNavigation.setOnNavigationItemSelectedListener(this);
        showHideFragment(mClassFragment);
        mNavigation.setItemIconTintList(null);
        String deviceInfo = getDeviceInfo(this);
        Log.d(TAG, "deviceInfo:"+deviceInfo);

        File root = new File(Environment.getExternalStorageDirectory()
                + File.separator + "myDir" + File.separator);
        boolean mkdirs = root.mkdirs();
        Log.d(TAG, "mkdirs:"+mkdirs);
    }



    public static boolean checkPermission(Context context, String permission) {
        boolean result = false;
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                Class<?> clazz = Class.forName("android.content.Context");
                Method method = clazz.getMethod("checkSelfPermission", String.class);
                int rest = (Integer) method.invoke(context, permission);
                if (rest == PackageManager.PERMISSION_GRANTED) {
                    result = true;
                } else {
                    result = false;
                }
            } catch (Exception e) {
                result = false;
            }
        } else {
            PackageManager pm = context.getPackageManager();
            if (pm.checkPermission(permission, context.getPackageName()) == PackageManager.PERMISSION_GRANTED) {
                result = true;
            }
        }
        return result;
    }
    public static String getDeviceInfo(Context context) {
        try {
            org.json.JSONObject json = new org.json.JSONObject();
            android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String device_id = null;
            if (checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
                device_id = tm.getDeviceId();
            }
            String mac = null;
            FileReader fstream = null;
            try {
                fstream = new FileReader("/sys/class/net/wlan0/address");
            } catch (FileNotFoundException e) {
                fstream = new FileReader("/sys/class/net/eth0/address");
            }
            BufferedReader in = null;
            if (fstream != null) {
                try {
                    in = new BufferedReader(fstream, 1024);
                    mac = in.readLine();
                } catch (IOException e) {
                } finally {
                    if (fstream != null) {
                        try {
                            fstream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            json.put("mac", mac);
            if (TextUtils.isEmpty(device_id)) {
                device_id = mac;
            }
            if (TextUtils.isEmpty(device_id)) {
                device_id = android.provider.Settings.Secure.getString(context.getContentResolver(),
                        android.provider.Settings.Secure.ANDROID_ID);
            }
            json.put("device_id", device_id);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.one) {
            showHideFragment(mClassFragment);
        }
        if (item.getItemId() == R.id.tow) {
            showHideFragment(mMeFragment);
            MobclickAgent.onEvent(this,"me_me");
        }
        return true;
    }

    @Override
    public void onBackPressedSupport() {
//        super.onBackPressedSupport();
        exit();
    }

    private void exit() {
        if (System.currentTimeMillis() - mExitTime > 2000) {
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
            AppManager.getAppManager().AppExit();
        }
    }
}
