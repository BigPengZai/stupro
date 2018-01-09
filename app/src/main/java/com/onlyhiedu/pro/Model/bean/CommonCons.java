package com.onlyhiedu.pro.Model.bean;

import com.onlyhiedu.pro.Utils.DateUtil;

import java.io.File;

/**
 * Created by pengpeng on 2017/9/9.
 */

public class CommonCons {

    public final static String SAVE_APP_NAME = DateUtil.getNowTime()+"-HiEducation.apk";

    public final static String SAVE_APP_LOCATION = "/hieducation";


    public final static String APP_FILE_NAME = "/sdcard"+SAVE_APP_LOCATION+ File.separator + SAVE_APP_NAME;
    public static final String DOWNLOAD_APK_ID_PREFS = "download_apk_id_prefs";
}
