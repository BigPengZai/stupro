package com.onlyhiedu.mobile.UI.Course.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.onlyhiedu.mobile.Base.BaseActivity;
import com.onlyhiedu.mobile.Base.ViewPagerAdapterFragment;
import com.onlyhiedu.mobile.R;
import com.onlyhiedu.mobile.UI.Course.fragment.CourseDiscountFragment;
import com.onlyhiedu.mobile.UI.Course.persenter.CourseDiscountPresenter;
import com.onlyhiedu.mobile.UI.Course.persenter.contract.CourseDiscountContract;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;

import static com.onlyhiedu.mobile.Utils.UIUtils.setIndicator;

/**
 * Created by pengpeng on 2017/7/26.
 * 课程优惠
 */

public class CourseDiscountActivity extends BaseActivity<CourseDiscountPresenter> implements CourseDiscountContract.View, TabLayout.OnTabSelectedListener {
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.viewpager)
    ViewPager mViewpager;
    private ViewPagerAdapterFragment mAdapter;
    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initView() {
        mAdapter = new ViewPagerAdapterFragment(getSupportFragmentManager(), this);
        mAdapter.addTab("课程优惠", "CourseDiscountFragment", CourseDiscountFragment.class, null);
        mAdapter.addTab("暑期优惠", "SummerDiscountFragment", CourseDiscountFragment.class, null);
        mViewpager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewpager);
        setIndicator(mTabLayout, 40, 40);
        mTabLayout.addOnTabSelectedListener(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_course;
    }

    @Override
    public void showInfoSuccess() {

    }

    @Override
    public void showError(String msg) {

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