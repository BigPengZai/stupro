package com.onlyhiedu.mobile.UI.Setting.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.onlyhiedu.mobile.Base.BaseActivity;
import com.onlyhiedu.mobile.Model.bean.UpdataVersionInfo;
import com.onlyhiedu.mobile.R;
import com.onlyhiedu.mobile.UI.Home.activity.MainActivity;
import com.onlyhiedu.mobile.UI.Setting.presenter.UpdataPresenter;
import com.onlyhiedu.mobile.UI.Setting.presenter.contract.UpdataContract;
import com.onlyhiedu.mobile.Utils.AppUtil;
import com.onlyhiedu.mobile.Utils.Config;
import com.onlyhiedu.mobile.Utils.DialogListener;
import com.onlyhiedu.mobile.Utils.DialogUtil;
import com.onlyhiedu.mobile.Utils.OKHttpUICallback;
import com.onlyhiedu.mobile.Utils.OkHttpManger;
import com.onlyhiedu.mobile.Utils.UIUtils;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

import static com.onlyhiedu.mobile.UI.Home.activity.MainActivity.CALL_REQUEST_CODE;

/**
 * Created by pengpeng on 2017/3/2.
 */

public class AboutActivity extends BaseActivity<UpdataPresenter> implements UpdataContract.View {

    public static final String TAG = AboutActivity.class.getSimpleName();

    @BindView(R.id.rl_update)
    RelativeLayout mRl_Update;
    @BindView(R.id.ll_down)
    LinearLayout mLl_Down;
    @BindView(R.id.pb_down)
    ProgressBar mPb_Down;
    @BindView(R.id.rl_line)
    RelativeLayout mRl_Line;


    public static final String PHONE_NUM = "400-876-3300";
    private final int DOWN_REQUEST_CODE = 2;
    private String mUrl;
    private ProgressDialog mDownDialog;
    private Call mCall;

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
        mDownDialog = new ProgressDialog(AboutActivity.this);
        mDownDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mDownDialog.setMessage("正在下载...");
        mDownDialog.setIndeterminate(false);
        mDownDialog.setCanceledOnTouchOutside(false);
        mDownDialog.setCancelable(true);
        mDownDialog.setProgressNumberFormat("%1d M/%2d M");
    }

    @OnClick({R.id.rl_update, R.id.rl_line})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_update:
                mPresenter.updataVersion();
                break;
            case R.id.rl_line:
                if (UIUtils.requestPermission(this, MainActivity.CALL_REQUEST_CODE, new String[]{Manifest.permission.CALL_PHONE})) {
                    UIUtils.callLine(this, PHONE_NUM);
                }
                break;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CALL_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    UIUtils.callLine(this, PHONE_NUM);
                } else {
                    Toast.makeText(AboutActivity.this, "拨打电话权限未授权", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            case DOWN_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (mUrl != null) {
                        downApk(mUrl);
                    }
                } else {
                    Toast.makeText(AboutActivity.this, "SD卡存储权限未授权", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    @Override
    public void showUpdataSuccess(UpdataVersionInfo versionInfo) {
        if (versionInfo != null && !versionInfo.getVersion().equals(AppUtil.getVerName(this))) {
            DialogUtil.showOnlyAlert(this, "版本更新", "有了新版本" + versionInfo.getVersion(), "更新", "取消", true, true, new DialogListener() {
                @Override
                public void onPositive(DialogInterface dialog) {
                    //确定
                    mUrl = versionInfo.getUrl();
                    if (mUrl != null) {
                        requestDownPermission();
                    }

                }

                @Override
                public void onNegative(DialogInterface dialog) {
                    //取消
                    Log.d(TAG, "onNegative");
                }
            });
        }
    }

    private void requestDownPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(mContext, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, DOWN_REQUEST_CODE);
            } else {
                if (mUrl != null) {
                    downApk(mUrl);
                }
            }
        } else {
            if (mUrl != null) {
                downApk(mUrl);
            }
        }
    }

    private void downApk(String url) {
        mDownDialog.show();
        try {
            //更新
            mCall = OkHttpManger.getInstance().downloadAsync(url, Config.getDirFile("download").getAbsolutePath(), new OKHttpUICallback.ProgressCallback() {
                @Override
                public void onSuccess(Call call, Response response, String path) {
                    mDownDialog.cancel();
                    Log.d(TAG, "path:" + path);
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
                    Log.d(TAG, "byteReadOrWrite:" + byteReadOrWrite + ",contentLength:" + contentLength + ",done:" + done);
                    if (!done && mPb_Down != null) {
                        mDownDialog.setMax((int) contentLength / 1024 / 1024);
                    }
                    mDownDialog.setProgress((int) byteReadOrWrite / 1024 / 1024);
                }

                @Override
                public void onError(Call call, IOException e) {
                    Log.i("MainActivity", "onError");
                }

            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        mDownDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (mCall != null) {
                    mCall.cancel();
                    Log.d(TAG, "取消下载。。。。。。。");
                }
            }
        });
    }

    @Override
    public void onBackPressedSupport() {
        super.onBackPressedSupport();
        if (mCall != null) {
            mCall.cancel();
            Log.d(TAG, "取消下载。。。。。。。");
        }
    }

    @Override
    public void showError(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
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
