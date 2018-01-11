package com.onlyhiedu.pro.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.onlyhiedu.pro.App.App;
import com.onlyhiedu.pro.App.Constants;

/**
 * Created by xuwc on 2017/2/21.
 */

public class SPUtil {

    private static final String shared_preferences_name = "config";

    public static SharedPreferences getAppSp() {
        return App.getInstance().getSharedPreferences(shared_preferences_name, Context.MODE_PRIVATE);
    }


    public static void AppSpClean() {
        App.getInstance().getSharedPreferences(shared_preferences_name, Context.MODE_PRIVATE).edit().clear().apply();
    }

    public static void removeKey(String key) {
        App.getInstance().getSharedPreferences(shared_preferences_name, Context.MODE_PRIVATE).edit().remove(key).commit();
    }

    public static void setToken(String token) {
        getAppSp().edit().putString(Constants.TOKEN, token).apply();
    }

    public static String getToken() {
        return getAppSp().getString(Constants.TOKEN, "");
    }

    public static String getPhone() {
        return getAppSp().getString(Constants.PHONE, "");
    }

    public static void setPhone(String phone) {
        getAppSp().edit().putString(Constants.PHONE, phone).apply();
    }

    public static void setUpdateApkId(long id) {
        getAppSp().edit().putLong(Constants.UpdateApkId, id).apply();
    }

    public static long getUpdateApkId() {
        return getAppSp().getLong(Constants.UpdateApkId, 0);
    }

    public static String getName() {
        return getAppSp().getString(Constants.USERNAME, "");
    }

    public static void setName(String name) {
        getAppSp().edit().putString(Constants.USERNAME, name).apply();
    }

    public static String getEmcRegName() {
        return getAppSp().getString(Constants.EMCREGNAME, "");
    }

    public static void setEmcRegName(String name) {
        getAppSp().edit().putString(Constants.EMCREGNAME, name).apply();
    }

    public static void setAvatarUrl(String avatarUrl) {
        getAppSp().edit().putString(Constants.EMCAVATARURL, avatarUrl).apply();
    }

    public static String getAvatarUrl() {
        return getAppSp().getString(Constants.EMCAVATARURL, "");
    }

    public static void setAgoraUid(String agoraUid) {
        getAppSp().edit().putString(Constants.AGORAUID, agoraUid).apply();
    }

    public static String getAgoraUid() {
        return getAppSp().getString(Constants.AGORAUID, "");
    }

    public static void setGuest(boolean guest) {
        getAppSp().edit().putBoolean(Constants.IsGuest, guest).apply();
    }

    public static boolean getGuest() {
        return getAppSp().getBoolean(Constants.IsGuest, true);
    }

    public static void setNightModeStatt(boolean guest) {
        getAppSp().edit().putBoolean(Constants.NightModeState, guest).apply();
    }

    public static boolean getNightModeState() {
        return getAppSp().getBoolean(Constants.NightModeState, false);
    }

    //============================================= 设置基本信息
    public static void setUserInfo(String emcRegName, String token, String phone, String userName, String avatarUrl, String agoraUid) {
        SPUtil.setEmcRegName(emcRegName);
        SPUtil.setToken(token);
        SPUtil.setPhone(phone);
        SPUtil.setName(userName);
        SPUtil.setAvatarUrl(avatarUrl);
        SPUtil.setAgoraUid(agoraUid);
    }



    public static void setUikitAccid(String accid) {
        getAppSp().edit().putString(Constants.NETEASEACCID,accid).apply();
    }
    public static String getUikitAccid() {
        return getAppSp().getString(Constants.NETEASEACCID,"");
    }

    public static void setUikitToken(String uikitToken) {
        getAppSp().edit().putString(Constants.NETEASETOKEN,uikitToken).apply();
    }
    public static String getUikitToken() {
        return getAppSp().getString(Constants.NETEASETOKEN,"");
    }


}
