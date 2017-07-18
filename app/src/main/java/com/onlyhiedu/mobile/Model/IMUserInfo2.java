package com.onlyhiedu.mobile.Model;

import com.onlyhiedu.mobile.App.App;
import com.onlyhiedu.mobile.Model.bean.IMUserInfo;
import com.onlyhiedu.mobile.Utils.UIUtils;

/**
 * Created by Administrator on 2017/7/18.
 */

public class IMUserInfo2 {

    //token失效
    public static final int TOKEN_LOSE = 112;

    public boolean hasError;
    public int code;
    private String message;
    public IMUserInfo data;


    public boolean isHasError() {
        return hasError;
    }

    public void setHasError(boolean hasError) {
        this.hasError = hasError;
    }

    public String getMessage() {
        if (code == TOKEN_LOSE) {
            UIUtils.startLoginActivity(App.getInstance());
        }
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }


}
