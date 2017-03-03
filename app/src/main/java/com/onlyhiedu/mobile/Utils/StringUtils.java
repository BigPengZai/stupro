package com.onlyhiedu.mobile.Utils;

import android.text.TextUtils;
import android.widget.Toast;

import com.onlyhiedu.mobile.App.App;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xuwc on 2016/11/30.
 */
public class StringUtils {

    /**
     * 手机号码验证
     */
    public static boolean isMobile(String mobileNumber) {
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
     * 密码
     *
     * @param context
     * @param password
     * @return
     */
    public static boolean checkPassword(String password) {
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(App.getInstance().getApplicationContext(), "密码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (password.length() < 6 || password.length() > 20) {
            Toast.makeText(App.getInstance().getApplicationContext(), "密码要在8-20位之间", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}
