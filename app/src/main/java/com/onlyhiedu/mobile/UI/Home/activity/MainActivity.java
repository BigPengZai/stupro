package com.onlyhiedu.mobile.UI.Home.activity;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;
import android.widget.Toast;

import com.onlyhiedu.mobile.App.AppManager;
import com.onlyhiedu.mobile.Base.SimpleActivity;
import com.onlyhiedu.mobile.R;
import com.onlyhiedu.mobile.UI.Home.fragment.ClassFragment;
import com.onlyhiedu.mobile.UI.Home.fragment.MeFragment;

import butterknife.BindView;

public class MainActivity extends SimpleActivity implements BottomNavigationView.OnNavigationItemSelectedListener {


    private ClassFragment mClassFragment;
    private MeFragment mMeFragment;
    private long mExitTime = 0;

    @BindView(R.id.navigation)
    BottomNavigationView mNavigation;

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initEventAndData() {
        mClassFragment = new ClassFragment();
        mMeFragment = new MeFragment();
        loadMultipleRootFragment(R.id.fl_main_content, 0, mClassFragment, mMeFragment);
        mNavigation.setOnNavigationItemSelectedListener(this);
        showHideFragment(mClassFragment);
        mNavigation.setItemIconTintList(null);
    }




    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.one) {
            showHideFragment(mClassFragment);
        }
        if (item.getItemId() == R.id.tow) {
            showHideFragment(mMeFragment);
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
}
