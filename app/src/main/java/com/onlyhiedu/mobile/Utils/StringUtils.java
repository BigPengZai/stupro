package com.onlyhiedu.mobile.Utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.widget.Toast;

import com.onlyhiedu.mobile.App.App;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xuwc on 2016/11/30.
 */
public class StringUtils {

    /**
     * 验证用户名只包含字母，中文
     *
     * @param account
     * @return
     */
    public static boolean checkAccountMark(String account) {
        String all = "^[a-zA-Z\\u4e00-\\u9fa5]+$";
        Pattern pattern = Pattern.compile(all);
        boolean matches = pattern.matches(all, account);
        if (account.getBytes().length >= 4 && account.getBytes().length <= 20 && matches) {
            return true;
        } else {
            Toast.makeText(App.getInstance().getApplicationContext(), "姓名格式为:字母,中文、长度：4-20", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    /**
     * 手机号码验证
     */
    public static boolean isMobile(String mobileNumber) {
        if (TextUtils.isEmpty(mobileNumber)) {
            Toast.makeText(App.getInstance().getApplicationContext(), "手机号码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        Pattern p = Pattern
                .compile("^((1[3,5,8][0-9])|(14[5,7])|(17[0,6,7,8]))\\d{8}$");
        Matcher m = p.matcher(mobileNumber);
        if (m.matches()) {
            return true;
        } else {
            Toast.makeText(App.getInstance().getApplicationContext(), "手机号码格式有误", Toast.LENGTH_SHORT).show();
            return false;
        }

    }

    /**
     * 校验密码 ：密码要求6-12位,非纯数字,非纯字母
     *
     * @param
     * @param password
     * @return
     */
    public static boolean checkPassword(String password) {

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(App.getInstance().getApplicationContext(), "密码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }

        // 判断一个字符串是否含有中文
        if (isContainsChinese(password)) {
            Toast.makeText(App.getInstance().getApplicationContext(), "不能包含中文", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (password.length() < 6 || password.length() > 20) {
            Toast.makeText(App.getInstance().getApplicationContext(), "密码要在6-20位之间", Toast.LENGTH_SHORT).show();
            return false;
        }

//        if (password.matches("^[a-zA-Z]+$") || password.matches("^[0-9]+$")) {
//            Toast.makeText(App.getInstance().getApplicationContext(), "密码不能是纯数字或字母", Toast.LENGTH_SHORT).show();
//            return false;
//        }

        return true;
    }

    // 判断一个字符串是否含有中文
    public static boolean isContainsChinese(String str) {
        String regEx = "[\u4e00-\u9fa5]";
        Pattern pat = Pattern.compile(regEx);
        Matcher matcher = pat.matcher(str);
        boolean flg = false;
        if (matcher.find()) {
            flg = true;
        }
        return flg;
    }

    //检查字符串是否数字
    public static boolean isNumeric(String str) {
        for (int i = str.length(); --i >= 0; ) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * @return 获取手机唯一标识
     */
   /* public static String getDeviceId() {
        TelephonyManager TelephonyMgr = (TelephonyManager) App.getInstance().getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        return TelephonyMgr.getDeviceId();
    }*/

   /* public static String getDeviceId() {
        String id;
        //android.telephony.TelephonyManager
        TelephonyManager mTelephony = (TelephonyManager)App.getInstance().getSystemService(Context.TELEPHONY_SERVICE);
        if (mTelephony.getDeviceId() != null) {
            id = mTelephony.getDeviceId();
        } else {
            //android.provider.Settings;
            id = Settings.Secure.getString(App.getInstance().getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        return id;
    }*/

    public static String getDeviceId(Context context) {
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



}
