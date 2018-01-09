package com.onlyhiedu.pro.Utils;

import android.content.Context;

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
//            Logger.d("保存失败");
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



    /**
     * 获取目录文件大小
     *
     * @param dir
     * @return
     */
    public static long getDirSize(File dir) {
        if (dir == null) {
            return 0;
        }
        if (!dir.isDirectory()) {
            return 0;
        }
        long dirSize = 0;
        File[] files = dir.listFiles();
        if (files != null) {

            for (File file : files) {
                if (file.isFile()) {
                    dirSize += file.length();
                } else if (file.isDirectory()) {
                    dirSize += file.length();
                    dirSize += getDirSize(file); // 递归调用继续统计
                }
            }
        }
        return dirSize;
    }

    /**
     * 转换文件大小
     *
     * @param fileS
     * @return B/KB/MB/GB
     */
    public static String formatFileSize(long fileS) {
        java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "G";
        }
        return fileSizeString;
    }

    /**
     * 清除本应用内部缓存
     * (/data/data/com.xxx.xxx/cache)
     *
     * @param context
     */
    public static void cleanInternalCache(Context context) {
        deleteFilesByDirectory(context.getCacheDir());
        deleteFilesByDirectory(context.getFilesDir());
    }
    /**
     * 删除方法 这里只会删除某个文件夹下的文件，如果传入的directory是个文件，将不做处理
     *
     * @param directory
     */
    public static void deleteFilesByDirectory(File directory) {
        if (directory != null && directory.exists() && directory.isDirectory()) {
            for (File child : directory.listFiles()) {
                if (child.isDirectory()) {
                    deleteFilesByDirectory(child);
                }
                child.delete();
            }
        }
    }

}