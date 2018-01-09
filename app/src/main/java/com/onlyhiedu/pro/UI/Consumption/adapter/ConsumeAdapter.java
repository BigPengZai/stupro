package com.onlyhiedu.pro.UI.Consumption.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.onlyhiedu.pro.Base.BaseRecyclerAdapter;
import com.onlyhiedu.pro.Model.bean.ConsumptionData;
import com.onlyhiedu.pro.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pengpeng on 2017/4/11.
 */

public class ConsumeAdapter extends BaseRecyclerAdapter<ConsumptionData> {


    public ConsumeAdapter(Context context) {
        super(context, NEITHER);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateDefaultViewHolder(ViewGroup parent, int type) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_consume, parent, false));
    }

    @Override
    protected void onBindDefaultViewHolder(RecyclerView.ViewHolder holder, ConsumptionData item, int position) {
        ViewHolder h = (ViewHolder) holder;
        h.mTvAccount.setText(item.classPackageName);
        h.mTvTotal.setText(item.totalTime);
        h.mTvRemaing.setText(item.surplusTime);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_account)
        TextView mTvAccount;
        @BindView(R.id.tv_total)
        TextView mTvTotal;
        @BindView(R.id.tv_remaing)
        TextView mTvRemaing;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


}
