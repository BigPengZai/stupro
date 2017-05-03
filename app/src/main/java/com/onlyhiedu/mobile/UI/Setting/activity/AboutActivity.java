package com.onlyhiedu.mobile.UI.Setting.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.onlyhiedu.mobile.Base.VersionUpdateActivity;
import com.onlyhiedu.mobile.R;
import com.onlyhiedu.mobile.UI.Home.activity.MainActivity;
import com.onlyhiedu.mobile.Utils.UIUtils;

import butterknife.BindView;
import butterknife.OnClick;

import static com.onlyhiedu.mobile.UI.Home.activity.MainActivity.CALL_REQUEST_CODE;

/**
 * Created by pengpeng on 2017/3/2.
 */

public class AboutActivity extends VersionUpdateActivity {

    public static final String TAG = AboutActivity.class.getSimpleName();

    @BindView(R.id.rl_update)
    RelativeLayout mRl_Update;
    @BindView(R.id.rl_line)
    RelativeLayout mRl_Line;

    public static final String PHONE_NUM = "400-876-3300";

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

    @OnClick({R.id.rl_update, R.id.rl_line})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_update:
                mPresenter.updateVersion();
                break;
            case R.id.rl_line:
                if (UIUtils.requestPermission(this, MainActivity.CALL_REQUEST_CODE, new String[]{Manifest.permission.CALL_PHONE})) {
                    UIUtils.callLine(this, PHONE_NUM);
                }
                break;
        }
    }

    @Override
    public void onBackPressedSupport() {
        super.onBackPressedSupport();
        if (mCall != null) {
            if(!mCall.isCanceled()){
                mCall.cancel();
            }
            Log.d(TAG, "取消下载。。。。。。。");
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
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
