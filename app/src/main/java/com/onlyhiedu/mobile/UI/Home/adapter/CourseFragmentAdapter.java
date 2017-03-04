package com.onlyhiedu.mobile.UI.Home.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.onlyhiedu.mobile.Base.BaseRecyclerAdapter;
import com.onlyhiedu.mobile.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/3/4.
 */

public class CourseFragmentAdapter extends BaseRecyclerAdapter<String> {


    public CourseFragmentAdapter(Context context) {
        super(context, NEITHER);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateDefaultViewHolder(ViewGroup parent, int type) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_course, parent, false));
    }

    @Override
    protected void onBindDefaultViewHolder(RecyclerView.ViewHolder holder, String item, int position) {
        ViewHolder h = (ViewHolder) holder;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_course)
        TextView mTvCourse;
        @BindView(R.id.tv_auth)
        TextView mTvAuth;
        @BindView(R.id.tv_time)
        TextView mTvTime;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
