package com.onlyhiedu.pro.UI.Course.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;

import com.onlyhiedu.pro.Model.bean.ViewPageInfo;

import java.util.ArrayList;


public class TabFragmentAdapter extends FragmentPagerAdapter {

    private SparseArray<Fragment> mArray = new SparseArray<>();
    private Fragment mFragment;
    private Context mContext;
    public ArrayList<ViewPageInfo> mTabs = new ArrayList<>();

    public TabFragmentAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    public void addTab(String title, String tag, Class<?> clss, Bundle args) {
        ViewPageInfo viewPageInfo = new ViewPageInfo(title, tag, clss, args);
        mTabs.add(viewPageInfo);
    }


    @Override
    public Fragment getItem(int position) {

        ViewPageInfo info = mTabs.get(position);
        mFragment = mArray.get(position);
        if (null == mFragment) {
            mFragment = Fragment.instantiate(mContext, info.clss.getName(), info.args);
            mArray.put(position, mFragment);
            return mFragment;
        } else {
            return mFragment;
        }

    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public int getCount() {
        return mTabs == null ? 0 : mTabs.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTabs.get(position).title;
    }
}