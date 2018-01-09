package com.onlyhiedu.pro.Utils;

import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;

import com.onlyhiedu.pro.Model.bean.CommonCons;

/**
 * Created by pengpeng on 2017/9/9.
 */

public class UpdateAppManager {
    private static final String TAG = "UpdateAppManager";
    private static DownloadManager manager;


//

    /**
     * 下载Apk, 并设置Apk地址,
     * 默认位置: /storage/sdcard0/Download
     *
     * @param context     上下文
     * @param downLoadUrl 下载地址
     * @param infoName    通知名称
     * @param description 通知描述
     */
    @SuppressWarnings("unused")
    public static void initManger(Context context) {

            manager = (DownloadManager)
            context.getSystemService(Context.DOWNLOAD_SERVICE);
    }
    public static void downloadApk(
            Context context,
            String downLoadUrl,
            String description,
            String infoName) {

        if (!isDownloadManagerAvailable()) {
            return;
        }

        String appUrl = downLoadUrl;
        if (appUrl == null || appUrl.isEmpty()) {

            return;
        }
        appUrl = appUrl.trim(); // 去掉首尾空格
        if (!appUrl.startsWith("http")) {
            appUrl = "http://" + appUrl; // 添加Http信息
        }


        DownloadManager.Request request;
        try {
            request = new DownloadManager.Request(Uri.parse(appUrl));
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        request.setTitle(infoName);
        request.setDescription(description);


        //在通知栏显示下载进度
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }

        //sdcard目录下的download文件夹
        request.setDestinationInExternalPublicDir(CommonCons.SAVE_APP_LOCATION, CommonCons.SAVE_APP_NAME);

        Context appContext = context.getApplicationContext();

        //每下载的一个文件对应一个id，通过此id可以查询数据。
        long id = manager.enqueue(request);
        SPUtil.setUpdateApkId(id);

    }

    // 最小版本号大于9
    private static boolean isDownloadManagerAvailable() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
    }

    //查询id
    public static boolean queryApk(Context context) {

        DownloadManager.Query query = new DownloadManager.Query();
        Cursor cursor = manager.query(query.setFilterById(SPUtil.getUpdateApkId()));
        if (cursor != null && cursor.moveToFirst()) {
            //下载的文件到本地的目录
            String address = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
            //已经下载的字节数
            int bytes_downloaded = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
            //总需下载的字节数
            int bytes_total = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
            //Notification 标题
            String title = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_TITLE));
            //描述
            String description = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_DESCRIPTION));
            //下载对应id
            long id = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_ID));
            //下载文件名称
            String filename = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));
            //下载文件的URL链接
            String url = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_URI));
            if (bytes_total == bytes_downloaded && bytes_total > 0 && bytes_downloaded > 0) {
                return true;
            }
        }
        return false;
    }
}
