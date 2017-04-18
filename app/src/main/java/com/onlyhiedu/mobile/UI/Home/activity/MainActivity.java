package com.onlyhiedu.mobile.UI.Home.activity;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;
import android.widget.Toast;

import com.onlyhiedu.mobile.App.AppManager;
import com.onlyhiedu.mobile.Base.SimpleActivity;
import com.onlyhiedu.mobile.R;
import com.onlyhiedu.mobile.UI.Home.fragment.ClassFragment;
import com.onlyhiedu.mobile.UI.Home.fragment.MeFragment;
import com.onlyhiedu.mobile.Utils.UIUtils;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;

public class MainActivity extends SimpleActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    public static final String TAG = MainActivity.class.getSimpleName();
    public static final int CALL_REQUEST_CODE = 110;

    private ClassFragment mClassFragment;
    private MeFragment mMeFragment;
    private long mExitTime = 0;


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
//        String deviceInfo = getDeviceInfo(this);
//        Log.d(TAG, "deviceInfo:" + deviceInfo);
        ;
    }


//    public static boolean checkPermission(Context context, String permission) {
//        boolean result = false;
//        if (Build.VERSION.SDK_INT >= 23) {
//            try {
//                Class<?> clazz = Class.forName("android.content.Context");
//                Method method = clazz.getMethod("checkSelfPermission", String.class);
//                int rest = (Integer) method.invoke(context, permission);
//                if (rest == PackageManager.PERMISSION_GRANTED) {
//                    result = true;
//                } else {
//                    result = false;
//                }
//            } catch (Exception e) {
//                result = false;
//            }
//        } else {
//            PackageManager pm = context.getPackageManager();
//            if (pm.checkPermission(permission, context.getPackageName()) == PackageManager.PERMISSION_GRANTED) {
//                result = true;
//            }
//        }
//        return result;
//    }
//
//    public static String getDeviceInfo(Context context) {
//        try {
//            org.json.JSONObject json = new org.json.JSONObject();
//            android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
//                    .getSystemService(Context.TELEPHONY_SERVICE);
//            String device_id = null;
//            if (checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
//                device_id = tm.getDeviceId();
//            }
//            String mac = null;
//            FileReader fstream = null;
//            try {
//                fstream = new FileReader("/sys/class/net/wlan0/address");
//            } catch (FileNotFoundException e) {
//                fstream = new FileReader("/sys/class/net/eth0/address");
//            }
//            BufferedReader in = null;
//            if (fstream != null) {
//                try {
//                    in = new BufferedReader(fstream, 1024);
//                    mac = in.readLine();
//                } catch (IOException e) {
//                } finally {
//                    if (fstream != null) {
//                        try {
//                            fstream.close();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    if (in != null) {
//                        try {
//                            in.close();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }
//            json.put("mac", mac);
//            if (TextUtils.isEmpty(device_id)) {
//                device_id = mac;
//            }
//            if (TextUtils.isEmpty(device_id)) {
//                device_id = android.provider.Settings.Secure.getString(context.getContentResolver(),
//                        android.provider.Settings.Secure.ANDROID_ID);
//            }
//            json.put("device_id", device_id);
//            return json.toString();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.one) {
            showHideFragment(mClassFragment);
        }
        if (item.getItemId() == R.id.tow) {
            showHideFragment(mMeFragment);
            MobclickAgent.onEvent(this, "me_me");
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


    public void requestPermissions(Activity context, int code, String[] permission) {
        if (UIUtils.requestPermission(context, code, permission)) {
            UIUtils.callLine(this, "400-876-3300");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CALL_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    UIUtils.callLine(this, "400-876-3300");
                } else {
                    Toast.makeText(mContext, "拨打电话权限未授权", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

}
