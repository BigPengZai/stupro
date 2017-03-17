package com.onlyhiedu.mobile.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.onlyhiedu.mobile.App.App;
import com.onlyhiedu.mobile.App.Constants;

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



}
