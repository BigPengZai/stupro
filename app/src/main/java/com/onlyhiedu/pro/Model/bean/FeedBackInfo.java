package com.onlyhiedu.pro.Model.bean;

/**
 * Created by pengpeng on 2017/3/30.
 */

public class FeedBackInfo {

    /**
     * hasError : true
     * code : 0
     * message : 成功
     * data : null
     */

    private boolean hasError;
    private int code;
    private String message;
    private Object data;

    public boolean isHasError() {
        return hasError;
    }

    public void setHasError(boolean hasError) {
        this.hasError = hasError;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
