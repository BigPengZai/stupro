package com.onlyhiedu.mobile.UI.Home.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;
import android.widget.Toast;

import com.onlyhiedu.mobile.App.App;
import com.onlyhiedu.mobile.App.AppManager;
import com.onlyhiedu.mobile.Base.VersionUpdateActivity;
import com.onlyhiedu.mobile.R;
import com.onlyhiedu.mobile.UI.Home.fragment.ClassFragment;
import com.onlyhiedu.mobile.UI.Home.fragment.HomeFragment;
import com.onlyhiedu.mobile.UI.Home.fragment.MeFragment;
import com.onlyhiedu.mobile.UI.Home.fragment.MeFragment2;
import com.onlyhiedu.mobile.Utils.UIUtils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareAPI;

import butterknife.BindView;
import me.yokeyword.fragmentation.SupportFragment;


public class MainActivity extends VersionUpdateActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    public static final String TAG = MainActivity.class.getSimpleName();
    public static final int CALL_REQUEST_CODE = 110;

    private ClassFragment mClassFragment;
    private SupportFragment mMeFragment;

    private long mExitTime = 0;


    @BindView(R.id.navigation)
    BottomNavigationView mNavigation;

    private HomeFragment mHomeFragment;

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        mClassFragment = new ClassFragment();
        if (App.bIsGuestLogin) {
            mMeFragment = new MeFragment2();
        } else {
            mMeFragment = new MeFragment();
        }

        mHomeFragment = new HomeFragment();
        //隐藏首页
       /* if (App.getInstance().isTag) {
            loadMultipleRootFragment(R.id.fl_main_content, 0, mClassFragment, mMeFragment);
            App.getInstance().isTag = false;
        } else {
            loadMultipleRootFragment(R.id.fl_main_content, 0, mClassFragment, mMeFragment);
        }*/
        //不隐藏首页
        if (App.getInstance().isTag) {
            mNavigation.setSelectedItemId(R.id.tow);
            loadMultipleRootFragment(R.id.fl_main_content, 1, mHomeFragment, mClassFragment, mMeFragment);
            App.getInstance().isTag = false;
        } else {
            loadMultipleRootFragment(R.id.fl_main_content, 0, mHomeFragment, mClassFragment, mMeFragment);
        }
        mNavigation.setOnNavigationItemSelectedListener(this);
        mNavigation.setItemIconTintList(null);


    }

    @Override
    protected void initData() {
        super.initData();
        mPresenter.updateVersion(true);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        //隐藏首页
       /* if (item.getItemId() == R.id.one) {
            showHideFragment(mClassFragment);
        }
        if (item.getItemId() == R.id.tow) {
            showHideFragment(mMeFragment);
            MobclickAgent.onEvent(this, "me_me");
        }*/
        //不隐藏首页
        if (item.getItemId() == R.id.one) {
            showHideFragment(mHomeFragment);
        }
        if (item.getItemId() == R.id.tow) {
            showHideFragment(mClassFragment);
        }
        if (item.getItemId() == R.id.thr) {
          /*  if (App.bIsGuestLogin) startActivity(new Intent(this, BindActivity.class));
            else*/
            showHideFragment(mMeFragment);
            MobclickAgent.onEvent(this, "me_me");
        }
        return true;
    }

    @Override
    public void onBackPressedSupport() {
//        super.onBackPressedSupport();
        exit();
    }

    private void exit() {
        if (System.currentTimeMillis() - mExitTime > 2000) {
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
            AppManager.getAppManager().AppExit();
        }
    }


    public void requestPermissions(Activity context, int code, String[] permission) {
        if (UIUtils.requestPermission(context, code, permission)) {
            UIUtils.callLine(this, "400-876-3300");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CALL_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    UIUtils.callLine(this, "400-876-3300");
                } else {
                    Toast.makeText(mContext, "拨打电话权限未授权", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("SplashScreen11"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("SplashScreen11"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();
    }
}
