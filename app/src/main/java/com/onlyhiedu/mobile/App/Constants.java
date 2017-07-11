package com.onlyhiedu.mobile.App;

import java.io.File;

/**
 * Created by xuwc on 2016/11/24.
 */
public class Constants {


    public static final String PATH_DATA = App.getInstance().getCacheDir().getAbsolutePath() + File.separator + "data";
    public static final String PATH_CACHE = PATH_DATA + "/com.onlyhiedu.mobile.NetCache";
    public static final String NET_ERROR = "网络异常";
    public static final String TAG = "OnlyhiAPP";
    public static final String Async = "Async";

    public static final int NUM_OF_PAGE = 20;

    //================= TYPE ==================== //
    public static final int TYPE_A = 101;
    public static final int TYPE_B = 102;
    public static final int TYPE_C = 103;



    //================= KEY ==================== //

    //================= Intent ================= //

    //================= sp_key ================= //
    public static final String TOKEN = "token";
    public static final String PHONE = "phone";
    public static final String PASSWORD = "Password";
    public static final String USERNAME = "userName";
    public static final String EMCREGNAME = "emcregname";
}
