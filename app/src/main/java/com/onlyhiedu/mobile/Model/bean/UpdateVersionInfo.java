package com.onlyhiedu.mobile.Model.bean;

/**
 * Created by pengpeng on 2017/3/24.
 */

public class UpdateVersionInfo {

    /**
     * url : http://192.168.1.252:8089/apkpackage\android\2.0.0\app-2.0.0.apk
     * version : 2.0.0
     */

    private String url;
    private String version;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
