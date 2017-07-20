package com.onlyhiedu.mobile.UI.Home.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.onlyhiedu.mobile.Model.bean.HomeTeacher;
import com.onlyhiedu.mobile.R;
import com.onlyhiedu.mobile.Utils.ImageLoader;
import com.onlyhiedu.mobile.Utils.ScreenUtil;

/**
 * Created by Administrator on 2017/6/2.
 */

public class TeacherPageAdapter extends PagerAdapter {

    private SparseArray<View> mViews;
    private Context ctx;
    private  HomeTeacher data;
    private RequestManager mRequestManager;
    public TeacherPageAdapter(Context ctx, HomeTeacher data) {

        this.data = data;
        mRequestManager = Glide.with(ctx);
        mViews = new SparseArray<>(data.list.size());
        this.ctx = ctx;
    }

    @Override
    public int getCount() {
        return data.list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = mViews.get(position);
        if (view == null) {
            view = View.inflate(ctx, R.layout.item_main_teacher, null);
            mViews.put(position, view);

            ImageView imageView = (ImageView) view.findViewById(R.id.image);
            TextView name = (TextView) view.findViewById(R.id.name);
            TextView age = (TextView) view.findViewById(R.id.age1);
            TextView age2 = (TextView) view.findViewById(R.id.age2);
            TextView grade = (TextView) view.findViewById(R.id.grade);
            TextView introduction = (TextView) view.findViewById(R.id.tv_introduction);

            HomeTeacher.ListBean item = getItem(position);
            ImageLoader.loadImage(mRequestManager,imageView,item.image);
            name.setText(item.name);
            age.setText(item.teachingAge);
            age2.setText(item.graduateClass);
            grade.setText(item.improveScore);
            introduction.setText(item.description);
            view.setPadding(0, 0, ScreenUtil.dip2px(10), 0);
        }
        container.addView(view, -2, -2);
        return view;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mViews.get(position));
    }

    public HomeTeacher.ListBean getItem(int position) {
        return data.list.get(position);
    }

}
