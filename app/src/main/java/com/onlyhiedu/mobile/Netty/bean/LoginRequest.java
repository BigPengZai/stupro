package com.onlyhiedu.mobile.Netty.bean;

/**
 * Created by Administrator on 2017/11/2.
 */

public class LoginRequest {
    public String userMark;
    public int type;

    public LoginRequest(String userMark, int type) {
        this.userMark = userMark;
        this.type = type;
    }
}
