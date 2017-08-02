package com.onlyhiedu.mobile.UI.Course.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.onlyhiedu.mobile.Base.BaseRecyclerAdapter;
import com.onlyhiedu.mobile.Model.bean.OrderList;
import com.onlyhiedu.mobile.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/8/1.
 */

public class OrderAdapter extends BaseRecyclerAdapter<OrderList.ListBean> {


    public OrderAdapter(Context context) {
        super(context, ONLY_FOOTER);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateDefaultViewHolder(ViewGroup parent, int type) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_order, parent, false));
    }

    @Override
    protected void onBindDefaultViewHolder(RecyclerView.ViewHolder holder, OrderList.ListBean item, int position) {
        ViewHolder h = (ViewHolder) holder;
        h.mMoney.setText(item.money);
        h.mTvType.setText(item.coursePricePackageName);
        h.mTvOrderNo.setText("订单编号：" + item.orderNo);
        h.mTvTime.setText("创建时间：" + item.createDate);
        h.mGoPay.setBackgroundResource(item.orderStatus == 1 ? R.drawable.select_log_out : R.drawable.shape_login_ben_radius_gray);
        h.mGoPay.setClickable(item.orderStatus == 1 ? true : false);
        switch (item.orderStatus) {
            case 0:
                h.mGoPay.setText("已关闭");
                break;
            case 1:
                h.mGoPay.setText("去支付");
                break;
            case 2:
                h.mGoPay.setText("已支付");
                break;
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.tv_type)
        TextView mTvType;
        @BindView(R.id.money)
        TextView mMoney;
        @BindView(R.id.tv_orderNo)
        TextView mTvOrderNo;
        @BindView(R.id.tv_time)
        TextView mTvTime;
        @BindView(R.id.go_pay)
        TextView mGoPay;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
