package com.onlyhiedu.mobile.UI.Home.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.onlyhiedu.mobile.R;
import com.onlyhiedu.mobile.Utils.ScreenUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/2.
 */

public class TeacherPageAdapter extends PagerAdapter {

    private List<MyData> mData;
    private SparseArray<View> mViews;
    private Context ctx;


    public TeacherPageAdapter(Context ctx) {
        mData = new ArrayList<>();

        mData.add(new MyData(R.mipmap.teacher1, "孙莹老师", "7年", "6年", "36.8", "渤海大学硕士，英语八级特级名师，善于因材施教，擅长全年级英语辅导，所带学生平均成绩提升25%"));
        mData.add(new MyData(R.mipmap.teacher2, "许杨涛老师", "5年", "3年", "35.6", "四年的物理教学经验，嗨课堂高中物理专业讲师，嗨课堂全国优秀教师，拥有丰富的全国各省市考试教学经验"));
        mData.add(new MyData(R.mipmap.teacher3, "杨镇老师", "5年", "3年", "34.2", "有着自己独特的教学方法，教学体系完善，课堂氛围极佳，善于启发学生，部分考上了985大学，受到学生们的喜爱和家长们的信赖。"));
        mData.add(new MyData(R.mipmap.teacher4, "刘志扬老师", "7年", "五年", "36.5", "从事物理教学7年多，对不同版本的物理教材都比较熟悉，会根据学生的具体情况制定专业的教学计划，对物理教法学法有较深刻的研究"));
        mData.add(new MyData(R.mipmap.teacher5, "李娜英老师", "4年", "2年", "32.5", "嗨课堂高中物理专业讲师，嗨课堂全国优秀教师，拥有丰富的全国各省市考试教学经验"));
        mData.add(new MyData(R.mipmap.teacher6, "王亮老师", "5年", "三年", "35.2", "嗨课堂教研院高中数学老师，综合教学质量在院内始终名列前茅，对于高考数学有自己的一套押题题库，曾经多次押题高考数学大题"));
        mViews = new SparseArray<View>(mData.size());
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

            MyData data = getItem(position);
            imageView.setImageResource(data.imageResource);
            name.setText(data.name);
            age.setText(data.age1);
            age2.setText(data.age2);
            grade.setText(data.grade);
            introduction.setText(data.introduction);
            view.setPadding(0, 0, ScreenUtil.dip2px(10), 0);
        }
        container.addView(view, -2, -2);
        return view;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mViews.get(position));
    }

    public MyData getItem(int position) {
        return mData.get(position);
    }


    public static class MyData {
        public int imageResource;
        public String name;
        public String age1;
        public String age2;
        public String grade;
        public String introduction;

        public MyData(int imageResource, String name, String age1, String age2, String grade, String introduction) {
            this.imageResource = imageResource;
            this.name = name;
            this.age1 = age1;
            this.age2 = age2;
            this.grade = grade;
            this.introduction = introduction;
        }
    }
}
