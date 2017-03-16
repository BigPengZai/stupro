package com.onlyhiedu.mobile.UI.User.activity;

import android.support.design.widget.TabLayout;

import com.onlyhiedu.mobile.Base.SimpleActivity;
import com.onlyhiedu.mobile.R;
import com.onlyhiedu.mobile.UI.User.fragment.LoginFragment;
import com.onlyhiedu.mobile.UI.User.fragment.RegFragment;

import butterknife.BindView;
import butterknife.OnClick;

import static com.onlyhiedu.mobile.Utils.UIUtils.setIndicator;


public class RegActivity extends SimpleActivity implements TabLayout.OnTabSelectedListener {


    private RegFragment mRegFragment;
    private LoginFragment mLoginFragment;


    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;


    @Override
    protected int getLayout() {
        return R.layout.activity_reg;
    }

    @Override
    protected void initEventAndData() {
        mRegFragment = new RegFragment();
        mLoginFragment = new LoginFragment();
        TabLayout.Tab tab1 = mTabLayout.newTab().setText("登录");
        mTabLayout.addTab(tab1);
        TabLayout.Tab tab2 = mTabLayout.newTab().setText("注册");
        mTabLayout.addTab(tab2, true);
        mTabLayout.addOnTabSelectedListener(this);
        setIndicator(mTabLayout,66,66);


        getSupportFragmentManager().beginTransaction().add(R.id.container, mRegFragment).show(mRegFragment)
                .add(R.id.container, mLoginFragment).hide(mLoginFragment).commit();
    }



    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        switch (tab.getPosition()) {
            case 0:
                getSupportFragmentManager().beginTransaction().show(mLoginFragment).hide(mRegFragment).commit();
                break;
            case 1:
                getSupportFragmentManager().beginTransaction().show(mRegFragment).hide(mLoginFragment).commit();
                break;
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
    }


    @OnClick(R.id.ic_delete)
    public void onClick() {
        finish();
    }
}