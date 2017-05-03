package com.onlyhiedu.mobile.Utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by Administrator on 2017/5/3.
 */

public class VersionUpdateHelper {


    public ProgressDialog getDialog(Context context) {
        ProgressDialog mDownDialog = new ProgressDialog(context);
        mDownDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mDownDialog.setMessage("正在下载...");
        mDownDialog.setIndeterminate(false);
        mDownDialog.setCanceledOnTouchOutside(false);
        mDownDialog.setCancelable(true);
        mDownDialog.setProgressNumberFormat("%1d M/%2d M");
        return mDownDialog;
    }
    /**
     * 获取安装App 意图
     */
    public Intent installAppIntent(String path) {
        // 跳转到新版本应用安装页面
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://" + path), "application/vnd.android.package-archive");
        return intent;
    }

}
