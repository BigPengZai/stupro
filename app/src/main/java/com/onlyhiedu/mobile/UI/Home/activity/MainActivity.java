package com.onlyhiedu.mobile.UI.Home.activity;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.onlyhiedu.mobile.Base.SimpleActivity;
import com.onlyhiedu.mobile.R;
import com.onlyhiedu.mobile.UI.Home.fragment.ClassFragment;
import com.onlyhiedu.mobile.UI.Home.fragment.MeFragment;

import butterknife.BindView;

public class MainActivity extends SimpleActivity implements BottomNavigationView.OnNavigationItemSelectedListener {




    private ClassFragment mClassFragment;
    private MeFragment mMeFragment;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.navigation)
    BottomNavigationView mNavigation;

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initEventAndData() {
        mToolbar.setTitle("");
        mTitle.setText("上课");
        setSupportActionBar(mToolbar);
        mClassFragment = new ClassFragment();
        mMeFragment = new MeFragment();
        loadMultipleRootFragment(R.id.fl_main_content, 0, mClassFragment, mMeFragment);
        mNavigation.setOnNavigationItemSelectedListener(this);
        showHideFragment(mClassFragment);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.one) {
            showHideFragment(mClassFragment);
            mTitle.setText("上课");
        }
        if (item.getItemId() == R.id.tow) {
            showHideFragment(mMeFragment);
            mTitle.setText("我的");
        }
        return true;
    }
}
