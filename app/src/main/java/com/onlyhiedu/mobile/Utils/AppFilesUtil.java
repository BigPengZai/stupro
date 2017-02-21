package com.onlyhiedu.mobile.Utils;

import android.content.Context;

import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 *  保存在项目的Files目录下面，不是Sd卡里面,只要是实现了Serializable都可以保存
 */
public class AppFilesUtil {


    /**
     * 保存缓存
     * @param context context
     * @param object 缓存对象
     * @param fn 文件名
     */
    public static void saveObject(Context context, Serializable object, String fn){
        ObjectOutputStream oos = null;
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(fn, Context.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(object);
            oos.flush();
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
            Logger.d("保存失败");
        }finally {
            if (fos!=null){
                try {
                    fos.close();
                } catch (IOException e1) {
                    //pass
                }
            }
            if (oos!=null){
                try {
                    oos.close();
                } catch (IOException e1) {
                    //pass
                }
            }
        }
    }

    /**
     * 读取缓存
     * @param context context
     * @param fn 文件名
     * @return
     */
    public static <T> T readObject(Context context, String fn){
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try{
            fis = context.openFileInput(fn);
            ois = new ObjectInputStream(fis);
            return (T) ois.readObject();
        }catch (Exception e){
            e.printStackTrace();
            File file = context.getFileStreamPath(fn);
            if (file!=null){
                file.delete();
            }
            return null;
        }finally {
            if (fis!=null){
                try {
                    fis.close();
                } catch (IOException e1) {
                    //pass
                }
            }
            if (ois!=null){
                try {
                    ois.close();
                } catch (IOException e1) {
                    //pass
                }
            }
        }
    }

    /**
     * 删除文件
     * @param context
     * @param fn
     */
    public static void deleteFile(Context context,String fn){
        File file = context.getFileStreamPath(fn);
        if (file!=null){
            file.delete();
        }
    }


}