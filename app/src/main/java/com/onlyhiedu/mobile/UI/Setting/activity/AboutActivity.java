package com.onlyhiedu.mobile.UI.Setting.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.onlyhiedu.mobile.Base.BaseActivity;
import com.onlyhiedu.mobile.Model.bean.UpdataVersionInfo;
import com.onlyhiedu.mobile.R;
import com.onlyhiedu.mobile.UI.Setting.presenter.UpdataPresenter;
import com.onlyhiedu.mobile.UI.Setting.presenter.contract.UpdataContract;
import com.onlyhiedu.mobile.Utils.AppUtil;
import com.onlyhiedu.mobile.Utils.Config;
import com.onlyhiedu.mobile.Utils.DialogListener;
import com.onlyhiedu.mobile.Utils.DialogUtil;
import com.onlyhiedu.mobile.Utils.OKHttpUICallback;
import com.onlyhiedu.mobile.Utils.OkHttpManger;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by pengpeng on 2017/3/2.
 */

public class AboutActivity extends BaseActivity<UpdataPresenter> implements UpdataContract.View {

    @BindView(R.id.rl_update)
    RelativeLayout mRl_Update;

    @BindView(R.id.ll_down)
    LinearLayout mLl_Down;
    @BindView(R.id.pb_down)
    ProgressBar mPb_Down;
    public static final String TAG = AboutActivity.class.getSimpleName();

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_about;
    }

    @Override
    protected void initView() {
        setToolBar("关于我们");
    }

    @OnClick(R.id.rl_update)
    public void onClick() {
        mPresenter.updataVersion();
    }

    @Override
    public void showUpdataSuccess(UpdataVersionInfo versionInfo) {
        if (versionInfo != null && !versionInfo.getVersion().equals(AppUtil.getVerName(this))) {
            Log.d(TAG, "Version:" + versionInfo.getVersion());
            Log.d(TAG, "" + AppUtil.getVerName(this));
            DialogUtil.showOnlyAlert(this, "版本更新", "有了新版本" + versionInfo.getVersion(), "更新", "取消", true, true, new DialogListener() {
                @Override
                public void onPositive(DialogInterface dialog) {
                    //确定
                    Log.d(TAG, "onPositive");
                    Log.d(TAG, "" + versionInfo.getUrl());
                    downApk(versionInfo.getUrl());
/*
                    ProgressDialog mypDialog = new ProgressDialog(AboutActivity.this);
                    mypDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    //设置进度条风格，风格为长形，有刻度的
                    mypDialog.setTitle("地狱怒兽");
                    //设置ProgressDialog 标题
                    mypDialog.setMessage("");
                    //设置ProgressDialog 提示信息
                    mypDialog.setIcon(R.mipmap.ic_launcher);
                    //设置ProgressDialog 标题图标
                    mypDialog.setProgress(59);
                    //设置ProgressDialog 进度条进度
                    mypDialog.setIndeterminate(false);
                    //设置ProgressDialog 的进度条是否不明确
                    mypDialog.setCancelable(true);
                    //设置ProgressDialog 是否可以按退回按键取消
                    mypDialog.show();*/
                }

                @Override
                public void onNegative(DialogInterface dialog) {
                    //取消
                    Log.d(TAG, "onNegative");
                }
            });
        }
    }

    private void downApk(String url) {
        mLl_Down.setVisibility(View.VISIBLE);
        try {
            OkHttpManger.getInstance().downloadAsync(url, Config.getDirFile("download").getAbsolutePath(), new OKHttpUICallback.ProgressCallback() {
                @Override
                public void onSuccess(Call call, Response response, String path) {
                    Log.i("MainActivity", "path:" + path);
                    mLl_Down.setVisibility(View.GONE);
                    DialogUtil.showOnlyAlert(AboutActivity.this, "更新完成", "更新的版本是。。。。", "更新", "取消", false, false, new DialogListener() {
                        @Override
                        public void onPositive(DialogInterface dialog) {
                            //更新
                            Log.d(TAG, "onPositive");
                            installApp(path);
                        }

                        @Override
                        public void onNegative(DialogInterface dialog) {
                            Log.d(TAG, "onNegative");
                        }
                    });
                }

                @Override
                public void onProgress(long byteReadOrWrite, long contentLength, boolean done) {
                    Log.i("MainActivity", "byteReadOrWrite:" + byteReadOrWrite + ",contentLength:" + contentLength + ",done:" + done);
                    if (!done) {
                        mPb_Down.setMax((int) contentLength);
                    }
                    mPb_Down.setProgress((int) byteReadOrWrite);
                }

                @Override
                public void onError(Call call, IOException e) {
                    Log.i("MainActivity", "onError");
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void showError(String msg) {
        Log.d(TAG, "msg:" + msg);
    }


    /**
     * 安装新版本应用
     */
    private void installApp(String path) {
        File appFile = new File(path);
        if (!appFile.exists()) {
            return;
        }
        // 跳转到新版本应用安装页面
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://" + appFile.toString()), "application/vnd.android.package-archive");
        startActivity(intent);
    }
}
