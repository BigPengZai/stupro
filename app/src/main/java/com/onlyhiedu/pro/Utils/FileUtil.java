package com.onlyhiedu.pro.Utils;

import android.os.Environment;

import java.io.File;

/**
 * Created by pengpeng on 2017/9/9.
 */

public class FileUtil {
    public static boolean isExistPath(String path){
        if(Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)){
            File file = new File(path);
            if(file.exists()){
                return true;
            }
        }
        return false;
    }


}
