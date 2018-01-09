package com.onlyhiedu.pro.UI.Home.fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.onlyhiedu.pro.Base.SimpleFragment;
import com.onlyhiedu.pro.Base.ViewPagerAdapterFragment;
import com.onlyhiedu.pro.R;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;

import static com.onlyhiedu.pro.Utils.UIUtils.setIndicator;

/**
 * Created by xuwc on 2017/2/18.
 */

public class ClassFragment extends SimpleFragment implements TabLayout.OnTabSelectedListener {


    private ViewPagerAdapterFragment mAdapter;


    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.viewpager)
    ViewPager mViewpager;

    public static final String TAG = ClassFragment.class.getSimpleName();
    @Override
    protected int getLayoutId() {
        return R.layout.layout_main_viewpaget_tablayout;
    }

    @Override
    protected void initEventAndData() {
        mAdapter = new ViewPagerAdapterFragment(getChildFragmentManager(), mContext);
        mAdapter.addTab("待上课程", "CourseFragment", CourseFragment.class, null);
        mAdapter.addTab("完成课程", "CourseRecordFragment", CourseRecordFragment.class, null);
        mViewpager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewpager);
        setIndicator(mTabLayout, 40, 40);
        mTabLayout.addOnTabSelectedListener(this);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        switch (tab.getPosition()) {
            case 0:
                MobclickAgent.onEvent(mContext,"tab_nostart");
                break;
            case 1:
                MobclickAgent.onEvent(mContext,"tab_finish");
                break;
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
    }
}
