package com.onlyhiedu.mobile.Model.bean.socket;

/**
 * Created by Administrator on 2017/11/2.
 */

public class LoginRequest {
    public String userMark;
    public int type;
    private String token;


    public LoginRequest(String userMark, int type, String token) {
        this.userMark = userMark;
        this.type = type;
        this.token = token;
    }
}
