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
     * 校验密码 ：密码要求6-12位,非纯数字,非纯字母
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
    public static boolean isNumeric(String str){
        for (int i = str.length();--i>=0;){
            if (!Character.isDigit(str.charAt(i))){
                return false;
            }
        }
        return true;
    }

}
