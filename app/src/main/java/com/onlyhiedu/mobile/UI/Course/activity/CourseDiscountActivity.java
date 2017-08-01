package com.onlyhiedu.mobile.UI.Course.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.onlyhiedu.mobile.Base.BaseActivity;
import com.onlyhiedu.mobile.Base.SimpleActivity;
import com.onlyhiedu.mobile.Base.ViewPagerAdapterFragment;
import com.onlyhiedu.mobile.Model.bean.CoursePriceTypeInfo;
import com.onlyhiedu.mobile.Model.bean.TypeListInfo;
import com.onlyhiedu.mobile.R;
import com.onlyhiedu.mobile.UI.Course.fragment.CourseDiscountFragment;
import com.onlyhiedu.mobile.UI.Course.persenter.CourseDiscountPresenter;
import com.onlyhiedu.mobile.UI.Course.persenter.contract.CourseDiscountContract;
import com.onlyhiedu.mobile.UI.Course.persenter.contract.CoursePayContract;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.onlyhiedu.mobile.Utils.UIUtils.setIndicator;

/**
 * Created by pengpeng on 2017/7/26.
 * 课程优惠
 */

public class CourseDiscountActivity extends SimpleActivity implements  TabLayout.OnTabSelectedListener {
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.viewpager)
    ViewPager mViewpager;
    private ViewPagerAdapterFragment mAdapter;
    private List<TypeListInfo> mTypeList;
    private Bundle mArgs;

    @BindView(R.id.iv_back)
    ImageView mIv_Back;
    @Override
    protected int getLayout() {
        return R.layout.activity_course;
    }

    @Override
    protected void initEventAndData() {
        mAdapter = new ViewPagerAdapterFragment(getSupportFragmentManager(), this);
        mTypeList = (List<TypeListInfo>) getIntent().getSerializableExtra("typeList");
        for (TypeListInfo typeListInfo : mTypeList) {
            mArgs = new Bundle();
            mArgs.putString("tag",typeListInfo.getValue());
            mAdapter.addTab(typeListInfo.getKey(), typeListInfo.getValue(), CourseDiscountFragment.class, mArgs);
        }
        mViewpager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewpager);
        setIndicator(mTabLayout, 40, 40);
        mTabLayout.addOnTabSelectedListener(this);
    }
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        switch (tab.getPosition()) {
            case 0:
                tab.setTag(mTypeList.get(0).getValue());
                MobclickAgent.onEvent(mContext, "tab_nostart");
                break;
            case 1:
                tab.setTag(mTypeList.get(1).getValue());
                MobclickAgent.onEvent(mContext, "tab_finish");
                break;
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @OnClick({R.id.iv_back})
    public void onClick(View view)  {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }


}