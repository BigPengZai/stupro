package com.onlyhiedu.mobile.UI.Home.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.onlyhiedu.mobile.Base.BaseRecyclerAdapter;
import com.onlyhiedu.mobile.Model.bean.CourseList;
import com.onlyhiedu.mobile.R;
import com.onlyhiedu.mobile.Utils.UserUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/3/4.
 */

public class CourseFragmentAdapter extends BaseRecyclerAdapter<CourseList.ListBean> {


    public CourseFragmentAdapter(Context context) {
        super(context, ONLY_FOOTER);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateDefaultViewHolder(ViewGroup parent, int type) {

        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_course, parent, false));
    }

    @Override
    protected void onBindDefaultViewHolder(RecyclerView.ViewHolder holder, CourseList.ListBean item, int position) {
        ViewHolder h = (ViewHolder) holder;
        h.mTvCourse.setText("科目: "+item.subject);
        h.mTvAuth.setText("讲师: "+item.teacherName);
        h.mTvTime.setText("课程时间: "+
                item.getCourseDate()+
                " "+
                item.startTime+
                "-"+
                item.getEndTime()
        );

        int state = UserUtil.isClassIn(item);
        switch (state){
            case 0:
                h.mTv_Soon.setVisibility(View.GONE);
                h.mTv_In.setVisibility(View.GONE);
                item.setClickAble(false);
                break;
            case 1:
                h.mTv_In.setVisibility(View.VISIBLE);
                h.mTv_Soon.setVisibility(View.GONE);
                item.setClickAble(true);
                break;
            case 2:
                h.mTv_Soon.setVisibility(View.VISIBLE);
                h.mTv_In.setVisibility(View.GONE);
                item.setClickAble(true);
                break;
        }

//        {
//        View view = h.itemView;
//        view.setScaleY(0.7f);
//        view.setScaleX(0.7f);
//        ViewCompat.animate(view).scaleX(1.0f).scaleY(1.0f).setDuration(500).setInterpolator(new OvershootInterpolator()).start();
//        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_course)
        TextView mTvCourse;
        @BindView(R.id.tv_auth)
        TextView mTvAuth;
        @BindView(R.id.tv_time)
        TextView mTvTime;
        @BindView(R.id.tv_soon)
        TextView mTv_Soon;
        @BindView(R.id.tv_in)
        TextView mTv_In;
        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
