package com.onlyhiedu.pro.UI.Course.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.onlyhiedu.pro.Base.SimpleActivity;
import com.onlyhiedu.pro.Base.ViewPagerAdapterFragment;
import com.onlyhiedu.pro.Model.bean.LoginProtoDemo;
import com.onlyhiedu.pro.Model.bean.TypeListInfo;
import com.onlyhiedu.pro.R;
import com.onlyhiedu.pro.UI.Course.fragment.CourseDiscountFragment;
import com.onlyhiedu.pro.Utils.JsonUtil;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.onlyhiedu.pro.Utils.UIUtils.setIndicator;

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
    //竖线
    @BindView(R.id.view_vertical)
    View mView;
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
        if (mTypeList.size() == 1) {
            mView.setVisibility(View.GONE);
            mTabLayout.setTabTextColors(Color.parseColor("#3d3d3d"),Color.parseColor("#3d3d3d"));
            mTabLayout.setSelectedTabIndicatorColor(Color.parseColor("#FFFFFF"));
        }
        mViewpager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewpager);
        setIndicator(mTabLayout, 40, 40);
        mTabLayout.addOnTabSelectedListener(this);
        Log.d("TAG", "Json:"+JsonUtil.toJson(new LoginProtoDemo()).length());
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