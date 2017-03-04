package com.onlyhiedu.mobile.UI.Home.fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.onlyhiedu.mobile.Base.SimpleFragment;
import com.onlyhiedu.mobile.Base.ViewPagerAdapterFragment;
import com.onlyhiedu.mobile.R;

import butterknife.BindView;

/**
 * Created by xuwc on 2017/2/18.
 */

public class ClassFragment extends SimpleFragment {


    private ViewPagerAdapterFragment mAdapter;


    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.viewpager)
    ViewPager mViewpager;

    @Override
    protected int getLayoutId() {
        return R.layout.layout_viewpaget_tablayout;
    }

    @Override
    protected void initEventAndData() {
        mAdapter = new ViewPagerAdapterFragment(getChildFragmentManager(), mContext);
        mAdapter.addTab("待上课程", "CourseFragment", CourseFragment.class, null);
        mAdapter.addTab("完成课程", "CourseRecordFragment", CourseRecordFragment.class, null);
        mViewpager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewpager);
    }

}
