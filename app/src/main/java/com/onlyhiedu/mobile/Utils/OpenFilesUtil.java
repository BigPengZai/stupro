package com.onlyhiedu.mobile.Utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by pengpeng on 2017/4/20.
 */

public class OpenFilesUtil {

    private static String filePath;

    //android获取一个用于打开Word文件的intent
    public static Intent getWordFileIntent(File param) {
        Intent intent = null;
        try {
            intent = new Intent("android.intent.action.VIEW");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri uri = Uri.fromFile(param);
            intent.setDataAndType(uri, "application/msword");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return intent;
    }
    public static void write(Context context) {
        filePath = android.os.Environment.getExternalStorageDirectory() + "/service";
//        if (!isExist()) {
//            write(context);
//        }
        InputStream inputStream;
        try {
            inputStream = context.getResources().getAssets().open("assets/" + "service.doc");
            File file = new File(filePath);
            if (!file.exists()) {
                file.mkdirs();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(filePath + "/service.doc");
            byte[] buffer = new byte[512];
            int count = 0;
            while ((count = inputStream.read(buffer)) > 0) {
                fileOutputStream.write(buffer, 0, count);
            }
            fileOutputStream.flush();
            fileOutputStream.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isExist() {
        File file = new File(filePath + "/service.doc");
        if (file.exists()) {
            return true;
        } else {
            return false;
        }
    }

}
