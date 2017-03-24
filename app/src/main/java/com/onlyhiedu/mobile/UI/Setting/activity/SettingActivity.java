package com.onlyhiedu.mobile.UI.Setting.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.onlyhiedu.mobile.Base.SimpleActivity;
import com.onlyhiedu.mobile.R;
import com.onlyhiedu.mobile.Utils.DialogListener;
import com.onlyhiedu.mobile.Utils.DialogUtil;
import com.onlyhiedu.mobile.Utils.UIUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by pengpeng on 2017/3/2.
 */

public class SettingActivity extends SimpleActivity {


    @BindView(R.id.tv_cache_size)
    TextView mTvCacheSize;

    @Override
    protected int getLayout() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initEventAndData() {
        setToolBar("设置");
        mTvCacheSize.setText(UIUtils.calculateCacheSize(this));
    }


    @OnClick({R.id.setting_pwd, R.id.setting_feedback, R.id.setting_about, R.id.btn_sign,R.id.ll_clean_cache})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.setting_pwd:
                startActivity(new Intent(this, ModifyPwActivity.class));
                break;
            case R.id.setting_feedback:
                startActivity(new Intent(this, FeedBackActivity.class));
                break;
            case R.id.setting_about:
                startActivity(new Intent(this, AboutActivity.class));
                break;
            case R.id.btn_sign:
                UIUtils.startLoginActivity(this);
                break;
            case R.id.ll_clean_cache:
                cleanAppCache();
                break;

        }
    }

    private void cleanAppCache(){
        DialogUtil.showOnlyAlert(this,
                "提示"
                , "是否清空缓存?"
                , "确定"
                , "取消"
                , false, true, new DialogListener() {
                    @Override
                    public void onPositive(DialogInterface dialog) {
                        UIUtils.clearAppCache(true);
                        mTvCacheSize.setText("0KB");
                    }

                    @Override
                    public void onNegative(DialogInterface dialog) {

                    }
                }
        );
    }


}
