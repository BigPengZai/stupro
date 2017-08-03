package com.onlyhiedu.mobile.UI.Course.adapter;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.onlyhiedu.mobile.Base.BaseRecyclerAdapter;
import com.onlyhiedu.mobile.Model.bean.CourseList;
import com.onlyhiedu.mobile.Model.bean.CoursePriceList;
import com.onlyhiedu.mobile.R;
import com.onlyhiedu.mobile.UI.Home.adapter.CourseFragmentAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pengpeng on 2017/7/26.
 */

public class CourseDiscountFragmentAdapter extends BaseRecyclerAdapter<CoursePriceList> {

    public CourseDiscountFragmentAdapter(Context context) {
        super(context, ONLY_FOOTER);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateDefaultViewHolder(ViewGroup parent, int type) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_course_discount, parent, false));
    }

    @Override
    protected void onBindDefaultViewHolder(RecyclerView.ViewHolder holder, CoursePriceList item, int position) {
        ViewHolder h = (ViewHolder) holder;

        h.mTv_Discount.setText(item.specialDiscount + "");
        h.mTv_Course_Name.setText(item.coursePricePackageName);
        h.mTv_Now_Price.setText(item.nowPrice + "");
        h.mTv_Original_Paice.setText(item.originalPrice + "");

        View view = h.itemView;
        view.setScaleY(0.7f);
        view.setScaleX(0.7f);
        ViewCompat.animate(view).scaleX(1.0f).scaleY(1.0f).setDuration(500).setInterpolator(new OvershootInterpolator()).start();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_discount)
        TextView mTv_Discount;
        @BindView(R.id.tv_course_name)
        TextView mTv_Course_Name;
        @BindView(R.id.tv_now_price)
        TextView mTv_Now_Price;
        @BindView(R.id.tv_original_price)
        TextView mTv_Original_Paice;


        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
