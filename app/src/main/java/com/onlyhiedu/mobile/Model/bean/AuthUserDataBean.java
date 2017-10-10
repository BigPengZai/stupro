package com.onlyhiedu.mobile.Model.bean;

/**
 * Created by pengpeng on 2017/4/12.
 */

public class AuthUserDataBean {
    /**
     * token : 659e0adbde6a306635bb4a67f2137b9d
     * userName : 杨澜
     * userType : STUDENT
     * phone : 13994413502
     * deviceType : IOS
     * deviceId : 123456
     */
    public String token;
    public String userName;
    public String phone;
    public String deviceId;
    public String userUuid;
    public boolean registerIMFlag;
    public String avatarUrl;
    public String agoroUid;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
