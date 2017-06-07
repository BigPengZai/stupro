package com.onlyhiedu.mobile.UI.Home.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.onlyhiedu.mobile.R;
import com.onlyhiedu.mobile.Utils.ScreenUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/2.
 */

public class TeacherPageAdapter extends PagerAdapter {

    protected List<Integer> mData;
    private SparseArray<ImageView> mViews;
    private Context ctx;


    public TeacherPageAdapter(Context ctx) {
        mData = new ArrayList<>();
        mData.add(R.mipmap.teacher1);
        mData.add(R.mipmap.teacher2);
        mData.add(R.mipmap.teacher3);
        mViews = new SparseArray<ImageView>(mData.size());
        this.ctx = ctx;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView view = mViews.get(position);
        if (view == null) {
            view = new ImageView(ctx);
            mViews.put(position, view);
            view.setImageResource(getItem(position));
            view.setScaleType(ImageView.ScaleType.FIT_XY);
            view.setPadding(0, 0, ScreenUtil.dip2px(10), 0);
        }
        container.addView(view);
        return view;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mViews.get(position));
    }

    public int getItem(int position) {
        return mData.get(position);
    }
}
