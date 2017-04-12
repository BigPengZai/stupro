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
        private String token;
        private String userName;
        private String phone;
        private String deviceId;
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
