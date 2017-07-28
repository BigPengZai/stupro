package com.onlyhiedu.mobile.UI.Setting.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.onlyhiedu.mobile.Base.VersionUpdateActivity;
import com.onlyhiedu.mobile.R;
import com.onlyhiedu.mobile.UI.Home.activity.MainActivity;
import com.onlyhiedu.mobile.UI.User.activity.TermServiceActivity;
import com.onlyhiedu.mobile.Utils.AppUtil;
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
    @BindView(R.id.tv_vc)
    TextView mTv_Vc;
    @BindView(R.id.tv_vc_def)
    TextView mTv_vc_def;
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
        mTv_vc_def.setText("嗨课堂V" + AppUtil.getVerName(AboutActivity.this));
        mTv_Vc.setText(AppUtil.getVerName(AboutActivity.this));
    }

    @OnClick({R.id.rl_update, R.id.rl_line, R.id.tv_official, R.id.tv_information})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_update:
                mPresenter.updateVersion(false);
                break;
            case R.id.rl_line:
                if (UIUtils.requestPermission(this, MainActivity.CALL_REQUEST_CODE, new String[]{Manifest.permission.CALL_PHONE})) {
                    UIUtils.callLine(this, PHONE_NUM);
                }
                break;
            case R.id.tv_official:
                UIUtils.startHomeNewsWebViewAct(mContext, "http://www.onlyhi.cn/", "官网");
                break;
            case R.id.tv_information:
                startActivity(new Intent(mContext, TermServiceActivity.class));
                break;
        }
    }

    @Override
    public void onBackPressedSupport() {
        super.onBackPressedSupport();
        if (mCall != null) {
            if (!mCall.isCanceled()) {
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
