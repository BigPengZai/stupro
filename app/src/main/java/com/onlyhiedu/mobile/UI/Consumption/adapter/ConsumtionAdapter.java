package com.onlyhiedu.mobile.UI.Consumption.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.onlyhiedu.mobile.Base.BaseRecyclerAdapter;
import com.onlyhiedu.mobile.Model.bean.ConsumptionList;
import com.onlyhiedu.mobile.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pengpeng on 2017/4/11.
 */

public class ConsumtionAdapter extends RecyclerView.Adapter<ConsumtionAdapter.ViewHolder> {
    private List<ConsumptionList.ListBean> data;
    private Context mContext;

    public ConsumtionAdapter(Context context, List<ConsumptionList.ListBean> data) {
        this.mContext = context;
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        ViewHolder mViewHolder=null;
        switch (type) {
            case 0:
                mViewHolder = new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_consumption_head, parent, false));
                break;
            case 1:
                mViewHolder = new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_consumption, parent, false));
                break;
        }
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case 0:
                break;
            case 1:
                TextView tv_account = (TextView) holder.getView(R.id.tv_account);
                TextView tv_total = (TextView) holder.getView(R.id.tv_total);
                TextView tv_remaing = (TextView) holder.getView(R.id.tv_remaing);
                ConsumptionList.ListBean consumptionList = data.get(position);
                if (consumptionList != null ) {
                    tv_account.setText(consumptionList.getTeacherName());
                    tv_total.setText(consumptionList.getTotal());
                    tv_remaing.setText(consumptionList.getRemaing());
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        return data == null ?  0:data.size();
    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position).getShow_type();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private Map<Integer, View> mCacheView;

        public ViewHolder(View view) {
            super(view);
            mCacheView = new HashMap<>();
            ButterKnife.bind(this, view);
        }

        public View getView(int resId) {
            View view;
            if (mCacheView.containsKey(resId)) {
                view = mCacheView.get(resId);
            } else {
                view = itemView.findViewById(resId);
                mCacheView.put(resId, view);
            }
            return view;
        }
    }
}
