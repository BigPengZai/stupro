package com.onlyhiedu.pro.UI.Setting.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.onlyhiedu.pro.Base.SimpleActivity;
import com.onlyhiedu.pro.Listener.MyDialogListener;
import com.onlyhiedu.pro.Model.event.MainActivityShowGuest;
import com.onlyhiedu.pro.Model.event.NightModeEvent;
import com.onlyhiedu.pro.R;
import com.onlyhiedu.pro.Utils.DialogListener;
import com.onlyhiedu.pro.Utils.DialogUtil;
import com.onlyhiedu.pro.Utils.SPUtil;
import com.onlyhiedu.pro.Utils.UIUtils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.IUmengCallback;
import com.umeng.message.PushAgent;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by pengpeng on 2017/3/2.
 */

public class SettingActivity extends SimpleActivity implements CompoundButton.OnCheckedChangeListener {


    @BindView(R.id.tv_cache_size)
    TextView mTvCacheSize;
    @BindView(R.id.toggle_btn)
    ToggleButton mToggle_btn;
    @BindView(R.id.rl_toggleButton)
    RelativeLayout mRelativeLayout;
    @BindView(R.id.btn_out)
    Button mButton;
    @BindView(R.id.check_box)
    AppCompatCheckBox mCheckBox;

    public static final String TAG = SettingActivity.class.getSimpleName();
    private boolean isNull = true;

    @Override
    protected int getLayout() {
        return R.layout.activity_setting;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        isNull = savedInstanceState == null;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initEventAndData() {
        setToolBar("设置");
//        mRelativeLayout.setVisibility(App.bIsGuestLogin ? View.GONE : View.VISIBLE);

        mCheckBox.setOnCheckedChangeListener(this);
        mTvCacheSize.setText(UIUtils.calculateCacheSize(this));
        mToggle_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    enablePush();
                } else {
                    disablePush();
                }
            }
        });

        mButton.setText(SPUtil.getGuest() ? "登录" : "退出登录");
    }


    @OnClick({R.id.setting_pwd, R.id.setting_feedback, R.id.btn_out, R.id.ll_clean_cache, R.id.setting_about, R.id.setting_device_test})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.setting_pwd:
                if (SPUtil.getGuest()) {
                    UIUtils.startGuestLoginActivity(this, 0);
                } else {
                    startActivity(new Intent(this, ModifyPwActivity.class));
                    MobclickAgent.onEvent(this, "setting_modify_pw");
                }
                break;
            case R.id.setting_feedback:
                if (SPUtil.getGuest()) {
                    UIUtils.startGuestLoginActivity(this, 0);
                } else {
                    startActivity(new Intent(this, FeedBackActivity.class));
                }
                break;
            case R.id.btn_out:
                if (SPUtil.getGuest()) {
                    UIUtils.startGuestLoginActivity(this, 0);
                } else {
                    outApp();
                }
                break;
            case R.id.ll_clean_cache:
                cleanAppCache();
                break;
            case R.id.setting_about:
                startActivity(new Intent(this, AboutActivity.class));
                break;
            case R.id.setting_device_test:
                startActivity(new Intent(this, DeviceTestActivity.class));
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (isNull) {   //防止夜间模式MainActivity执行reCreate后重复调用
            SPUtil.setNightModeStatt(b);
            NightModeEvent event = new NightModeEvent(b);
            EventBus.getDefault().post(event);
        }
    }

    private void outApp() {
        DialogUtil.showOnlyAlert(this, "", "确定要退出登录?", "确定", "取消", true, true, new MyDialogListener() {
            @Override
            public void onPositive(DialogInterface dialog) {
//                App.bIsGuestLogin = true;
                SPUtil.setGuest(true);
                EventBus.getDefault().post(new MainActivityShowGuest(true));
                finish();
            }
        });
    }



    private void cleanAppCache() {
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

    //关闭 推送
    public void disablePush() {
        PushAgent.getInstance(this).disable(new IUmengCallback() {
            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SettingActivity.this, "关闭上课提醒了哦。", Toast.LENGTH_SHORT).show();
                        mToggle_btn.setChecked(false);
                        Log.d(TAG, "关闭 推送：  success");
                    }
                });
            }

            @Override
            public void onFailure(String s, String s1) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mToggle_btn.setChecked(true);
                        Log.d(TAG, "关闭 推送：  failure");
                    }
                });
            }
        });
    }

    //打开推送
    public void enablePush() {
        PushAgent.getInstance(this).enable(new IUmengCallback() {
            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SettingActivity.this, "上课提醒已经打开。", Toast.LENGTH_SHORT).show();
                        mToggle_btn.setChecked(true);
                        Log.d(TAG, "打开推送：success");
                    }
                });

            }

            @Override
            public void onFailure(String s, String s1) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mToggle_btn.setChecked(false);
                        Log.d(TAG, "打开推送：failure");
                    }
                });

            }
        });
    }


}
