package com.onlyhiedu.mobile.UI.Home.activity;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.onlyhiedu.mobile.Base.SimpleActivity;
import com.onlyhiedu.mobile.R;
import com.onlyhiedu.mobile.UI.Home.fragment.Fragment1;
import com.onlyhiedu.mobile.UI.Home.fragment.Fragment2;
import com.onlyhiedu.mobile.UI.Home.fragment.Fragment3;

import butterknife.BindView;

public class MainActivity extends SimpleActivity implements BottomNavigationView.OnNavigationItemSelectedListener {


    private Fragment1 m1Fragment;
    private Fragment2 m2Fragment;
    private Fragment3 m3Fragment;


    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.navigation)
    BottomNavigationView mNavigation;

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initEventAndData() {
        setToolBar(mToolbar, "A");
        m1Fragment = new Fragment1();
        m2Fragment = new Fragment2();
        m3Fragment = new Fragment3();
        loadMultipleRootFragment(R.id.fl_main_content, 0, m1Fragment, m2Fragment, m3Fragment);
        mNavigation.setOnNavigationItemSelectedListener(this);
        showHideFragment(m1Fragment);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.one){
            showHideFragment(m1Fragment);
        }
        if(item.getItemId() == R.id.tow){
            showHideFragment(m2Fragment);
        }
        if(item.getItemId() == R.id.three){
            showHideFragment(m3Fragment);
        }
        return true;
    }
}
