package com.onlyhiedu.mobile.UI.Home.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.onlyhiedu.mobile.Base.SimpleActivity;
import com.onlyhiedu.mobile.Base.ViewPagerAdapterFragment;
import com.onlyhiedu.mobile.R;
import com.onlyhiedu.mobile.UI.Home.fragment.OrderFragment;

import butterknife.BindView;

/**
 * Created by pengpeng on 2017/7/31.
 * 我的订单
 */

public class MineOrdersActivity extends SimpleActivity {


    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.viewpager)
    ViewPager mViewpager;

    private ViewPagerAdapterFragment mAdapter;


    @Override
    protected int getLayout() {
        return R.layout.activity_orders;
    }

    @Override
    protected void initEventAndData() {
        setToolBar("我的订单");

        mAdapter = new ViewPagerAdapterFragment(getSupportFragmentManager(), mContext);
        Bundle bundle = new Bundle();
        bundle.putString("payState", "1");
        Bundle bundle2 = new Bundle();
        bundle2.putString("payState", "2");
        Bundle bundle3 = new Bundle();
        bundle3.putString("payState", "0");

        mAdapter.addTab("全部", "全部", OrderFragment.class, null);
        mAdapter.addTab("待支付", "待支付", OrderFragment.class, bundle);
        mAdapter.addTab("已支付", "已支付", OrderFragment.class, bundle2);
        mAdapter.addTab("已关闭", "已关闭", OrderFragment.class, bundle3);
        mViewpager.setAdapter(mAdapter);
        mViewpager.setOffscreenPageLimit(5);
        mTabLayout.setupWithViewPager(mViewpager);
    }

}
