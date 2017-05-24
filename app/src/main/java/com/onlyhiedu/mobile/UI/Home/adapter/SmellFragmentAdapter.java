package com.onlyhiedu.mobile.UI.Home.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.onlyhiedu.mobile.Base.BaseRecyclerAdapter;
import com.onlyhiedu.mobile.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pengpeng on 2017/5/24.
 */

public class SmellFragmentAdapter extends BaseRecyclerAdapter {
    public SmellFragmentAdapter(Context context) {
        super(context, ONLY_FOOTER);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateDefaultViewHolder(ViewGroup parent, int type) {
        return new SmellFragmentAdapter.ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_smell, parent, false));
    }

    @Override
    protected void onBindDefaultViewHolder(RecyclerView.ViewHolder holder, Object item, int position) {

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
