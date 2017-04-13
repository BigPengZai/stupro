package com.onlyhiedu.mobile.Model.http;

import com.onlyhiedu.mobile.App.App;
import com.onlyhiedu.mobile.Utils.UIUtils;

public class onlyHttpResponse<T> {

    private boolean hasError;
    private int code;
    private String message;
    private T data;
    //token失效
    public static final int TOKEN_LOSE = 112;
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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}