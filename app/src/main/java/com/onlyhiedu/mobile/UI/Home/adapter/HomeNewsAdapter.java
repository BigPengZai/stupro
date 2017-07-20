package com.onlyhiedu.mobile.UI.Home.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.onlyhiedu.mobile.Base.BaseRecyclerAdapter;
import com.onlyhiedu.mobile.Model.bean.HomeBannerBean;
import com.onlyhiedu.mobile.R;
import com.onlyhiedu.mobile.Utils.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pengpeng on 2017/5/24.
 */

public class HomeNewsAdapter extends BaseRecyclerAdapter<HomeBannerBean.ListBean> {

    private RequestManager mRequestManager;

    public HomeNewsAdapter(Context context) {
        super(context, VIEW_TYPE_NORMAL);
        mRequestManager = Glide.with(context);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateDefaultViewHolder(ViewGroup parent, int type) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_home_news, parent, false));
    }

    @Override
    protected void onBindDefaultViewHolder(RecyclerView.ViewHolder holder, HomeBannerBean.ListBean item, int position) {
        ViewHolder h = (ViewHolder) holder;
        ImageLoader.loadImage(mRequestManager,h.mImage,item.image);
        h.mTv.setText(item.title);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image)
        ImageView mImage;
        @BindView(R.id.tv)
        TextView mTv;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
