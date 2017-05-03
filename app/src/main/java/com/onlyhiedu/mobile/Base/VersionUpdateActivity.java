package com.onlyhiedu.mobile.Base;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.onlyhiedu.mobile.Model.bean.UpdateVersionInfo;
import com.onlyhiedu.mobile.UI.Setting.activity.AboutActivity;
import com.onlyhiedu.mobile.UI.Setting.presenter.UpdatePresenter;
import com.onlyhiedu.mobile.UI.Setting.presenter.contract.UpdateContract;
import com.onlyhiedu.mobile.Utils.AppUtil;
import com.onlyhiedu.mobile.Utils.Config;
import com.onlyhiedu.mobile.Utils.DialogListener;
import com.onlyhiedu.mobile.Utils.DialogUtil;
import com.onlyhiedu.mobile.Utils.OKHttpUICallback;
import com.onlyhiedu.mobile.Utils.OkHttpManger;
import com.onlyhiedu.mobile.Utils.VersionUpdateHelper;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/5/3.
 */

public abstract class VersionUpdateActivity extends BaseActivity<UpdatePresenter> implements UpdateContract.View {

    public static final String TAG = AboutActivity.class.getSimpleName();
    private final int DOWN_REQUEST_CODE = 2;


    private String mUrl;
    private ProgressDialog mDownDialog;
    private VersionUpdateHelper mVersionUpdateHelper;

    public Call mCall;

    @Override
    public void showUpdateSuccess(UpdateVersionInfo versionInfo) {
        if (versionInfo != null && !versionInfo.getVersion().equals(AppUtil.getVerName(this))) {
            DialogUtil.showOnlyAlert(this, "版本更新", "有了新版本" + versionInfo.getVersion(), "更新", "取消", true, true, new DialogListener() {
                @Override
                public void onPositive(DialogInterface dialog) {
                    //确定
                    String url = versionInfo.getUrl();
                    if (url != null) {
                        requestDownPermission(url);
                    } else {
                        Toast.makeText(VersionUpdateActivity.this, "已是最新版本", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onNegative(DialogInterface dialog) {
                    //取消
                    Log.d(TAG, "onNegative");
                }
            });
        } else {
            Toast.makeText(this, "已是最新版本", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case DOWN_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) downApk(mUrl);
                else Toast.makeText(this, "SD卡存储权限未授权", Toast.LENGTH_SHORT).show();
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void requestDownPermission(String url) {
        mUrl = url;
        if (Build.VERSION.SDK_INT >= 23) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(mContext, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, DOWN_REQUEST_CODE);
            } else {
                downApk(url);
            }
        } else {
            downApk(url);
        }
    }

    private void downApk(String url) {
        if (mDownDialog == null) {
            mVersionUpdateHelper = new VersionUpdateHelper();
            mDownDialog = mVersionUpdateHelper.getDialog(this);
        }
        mDownDialog.show();
        try {
            //更新
            mCall = OkHttpManger.getInstance().downloadAsync(url, Config.getDirFile("download").getAbsolutePath(), new OKHttpUICallback.ProgressCallback() {
                @Override
                public void onSuccess(Call call, Response response, String path) {
                    mDownDialog.cancel();
                    Log.d(TAG, "path:" + path);
                    startActivity(mVersionUpdateHelper.installAppIntent(path));
                }

                @Override
                public void onProgress(long byteReadOrWrite, long contentLength, boolean done) {
                    Log.d(TAG, "byteReadOrWrite:" + byteReadOrWrite + ",contentLength:" + contentLength + ",done:" + done);
                    if (!done) {
                        mDownDialog.setMax((int) contentLength / 1024 / 1024);
                    }
                    mDownDialog.setProgress((int) byteReadOrWrite / 1024 / 1024);
                }

                @Override
                public void onError(Call call, IOException e) {
                    e.printStackTrace();
                    if (mDownDialog != null) {
                        mDownDialog.dismiss();
                    }
                    Toast.makeText(VersionUpdateActivity.this, "下载出错", Toast.LENGTH_SHORT).show();
                }

            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        mDownDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (mCall != null) {
                    if(!mCall.isCanceled()){
                        mCall.cancel();
                    }
                    Log.d(TAG, "取消下载。。。。。。。");
                }
            }
        });
    }


    @Override
    public void showError(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


//    /**
//     * 安装新版本应用
//     */
//    private void installApp(String path) {
//        File appFile = new File(path);
//        if (!appFile.exists()) {
//            return;
//        }
//        // 跳转到新版本应用安装页面
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setDataAndType(Uri.parse("file://" + path), "application/vnd.android.package-archive");
//        startActivity(intent);
//    }
}
