package com.onlyhiedu.mobile.UI.Home.fragment;

import android.content.Context;
import android.content.Intent;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.onlyhiedu.mobile.Base.SimpleFragment;
import com.onlyhiedu.mobile.R;
import com.onlyhiedu.mobile.UI.Home.activity.KnowActivity;
import com.onlyhiedu.mobile.UI.Home.activity.MainActivity;
import com.onlyhiedu.mobile.UI.User.activity.LoginActivity;
import com.onlyhiedu.mobile.Utils.ScreenUtil;
import com.onlyhiedu.mobile.Widget.SettingItemView;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xuwc on 2017/2/18.
 */

public class MeFragment2 extends SimpleFragment {
    public static final String TAG = MeFragment2.class.getSimpleName();
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.setting_me)
    SettingItemView mSettingInfo;
    @BindView(R.id.setting_consumption)
    SettingItemView mSettingConsumption;
    @BindView(R.id.setting_share)
    SettingItemView mSettingItemShare;
    @BindView(R.id.setting_know)
    SettingItemView mSettingItemKnow;


    @Override
    protected int getLayoutId() {
        return R.layout.fr_me;
    }

    @Override
    protected void initEventAndData() {
        mTvName.setText("登录/注册");
        mTvName.setTextColor(getResources().getColor(R.color.c_F42440));
        mTvName.setBackgroundResource(R.drawable.radius5);
        mTvName.setGravity(Gravity.CENTER);
        mTvName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ScreenUtil.dip2px(95), ScreenUtil.dip2px(28));
        params.topMargin = ScreenUtil.dip2px(10);
        mTvName.setLayoutParams(params);

    }

    @OnClick({R.id.iv_setting, R.id.setting_me, R.id.setting_consumption, R.id.setting_share, R.id.setting_know, R.id.tv_name})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_setting:
                showGuestLoginActivity(mContext);
                MobclickAgent.onEvent(mContext, "me_setting");
                break;
            case R.id.setting_me:
                showGuestLoginActivity(mContext);
                break;
            case R.id.setting_consumption:
                showGuestLoginActivity(mContext);
                break;
            case R.id.setting_share:
                showGuestLoginActivity(mContext);
                break;
            case R.id.setting_know:
                startActivity(new Intent(mContext, KnowActivity.class));
                break;
            case R.id.tv_name:
                showGuestLoginActivity(mContext);
                break;
        }
    }

    public static void showGuestLoginActivity(Context ctx) {
        ctx.startActivity(new Intent(ctx, LoginActivity.class).putExtra(LoginActivity.cancelShow, true).putExtra(MainActivity.showPagePosition,3));
    }

}
