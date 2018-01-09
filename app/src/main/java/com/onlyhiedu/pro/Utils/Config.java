package com.onlyhiedu.pro.Utils;

import android.os.Environment;

import java.io.File;

/**
 * Created by pengpeng on 2017/3/25.
 */

public class Config {

    public static File getDirFile(String subFileName){
        if(isSDMounted()){
            return mkdirsFolder(getSDirAbsolutePath() + "/onlyhieduDownload/" + subFileName);
        }
        return null;
    }

    public static boolean isSDMounted(){
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static File mkdirsFolder(String path){
        File file = new File(path);
        if(!file.exists()){
            return file.mkdirs()?file:null;
        }
        return file;
    }

    public static String getSDirAbsolutePath(){
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }
}
