package com.onlyhiedu.pro.UI.Home.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.onlyhiedu.pro.Base.BaseRecyclerAdapter;
import com.onlyhiedu.pro.Model.bean.CourseList;
import com.onlyhiedu.pro.R;
import com.onlyhiedu.pro.Utils.UserUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pengpeng on 2017/6/27.
 */

public class CourseRecordFragmentAdapter extends BaseRecyclerAdapter<CourseList.ListBean>  {
    public CourseRecordFragmentAdapter(Context context) {
        super(context, ONLY_FOOTER);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateDefaultViewHolder(ViewGroup parent, int type) {
        return new CourseRecordFragmentAdapter.ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_course, parent, false));
    }

    @Override
    protected void onBindDefaultViewHolder(RecyclerView.ViewHolder holder, CourseList.ListBean item, int position) {
        CourseRecordFragmentAdapter.ViewHolder h = (CourseRecordFragmentAdapter.ViewHolder) holder;
        h.mTvCourse.setText("科目: "+item.subject);
        h.mTvAuth.setText("讲师: "+item.teacherName);
        h.mTvTime.setText("课程时间: "+
                item.getCourseDate()+
                " "+
                item.startTime+
                "-"+
                item.getEndTime()
        );
        switch (item.subject) {
            case "地理":
                h.mIv_Class.setImageResource(R.mipmap.dili);
                break;
            case "化学":
                h.mIv_Class.setImageResource(R.mipmap.huaxue);
                break;
            case "历史":
                h.mIv_Class.setImageResource(R.mipmap.lishi);
                break;
            case "生物":
                h.mIv_Class.setImageResource(R.mipmap.shengwu);
                break;
            case "数学":
                h.mIv_Class.setImageResource(R.mipmap.shuxue);
                break;
            case "物理":
                h.mIv_Class.setImageResource(R.mipmap.wuli);
                break;
            case "英语":
                h.mIv_Class.setImageResource(R.mipmap.yinyu);
                break;
            case "语文":
                h.mIv_Class.setImageResource(R.mipmap.yuwen);
                break;
            case "政治":
                h.mIv_Class.setImageResource(R.mipmap.zhengzhi);
                break;
        }
        int state = UserUtil.isClassIn(item);
        //已经完成列表
        switch (state){
            case 0:
                h.mTv_Soon.setVisibility(View.GONE);
                h.mTv_In.setVisibility(View.GONE);
                item.setClickAble(false);
                break;
            case 1:
                h.mTv_In.setVisibility(View.GONE);
                h.mTv_Soon.setVisibility(View.GONE);
                item.setClickAble(true);
                break;
            case 2:
                h.mTv_Soon.setVisibility(View.GONE);
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
        @BindView(R.id.iv_class)
        ImageView mIv_Class;
        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
