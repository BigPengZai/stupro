package com.onlyhiedu.mobile.UI.Home.fragment;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.onlyhiedu.mobile.Base.BaseFragment;
import com.onlyhiedu.mobile.Base.BaseRecyclerAdapter;
import com.onlyhiedu.mobile.Model.bean.OrderList;
import com.onlyhiedu.mobile.R;
import com.onlyhiedu.mobile.UI.Course.activity.CoursePayActivity;
import com.onlyhiedu.mobile.UI.Course.adapter.OrderAdapter;
import com.onlyhiedu.mobile.UI.Home.presenter.OrdersPresenter;
import com.onlyhiedu.mobile.UI.Home.presenter.contract.OrdersContract;
import com.onlyhiedu.mobile.Utils.UIUtils;
import com.onlyhiedu.mobile.Widget.ErrorLayout;
import com.onlyhiedu.mobile.Widget.RecyclerRefreshLayout;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/8/1.
 */

public class OrderFragment extends BaseFragment<OrdersPresenter> implements OrdersContract.View, View.OnClickListener, RecyclerRefreshLayout.SuperRefreshLayoutListener, BaseRecyclerAdapter.OnItemClickListener {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh)
    RecyclerRefreshLayout mSwipeRefresh;
    @BindView(R.id.error_layout)
    ErrorLayout mErrorLayout;


    private String mPayState;
    private OrderAdapter mAdapter;

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_recycle_refresh;
    }

    @Override
    protected void initView() {
        if (getArguments() != null) {
            mPayState = getArguments().getString("payState");
        }

        mErrorLayout.setState(ErrorLayout.NETWORK_LOADING);
        mErrorLayout.setOnLayoutClickListener(this);

        mSwipeRefresh.setSuperRefreshLayoutListener(this);

        mAdapter = new OrderAdapter(mContext);
        mAdapter.setState(BaseRecyclerAdapter.STATE_HIDE, false);
        UIUtils.setRecycleAdapter(mContext, mRecyclerView, mAdapter);
        mAdapter.setOnItemClickListener(this);
    }


    @Override
    protected void initData() {
        mSwipeRefresh.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefresh.setRefreshing(true);
                onRefreshing();
            }
        });
    }


    @Override
    public void showError(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        mErrorLayout.setState(ErrorLayout.NETWORK_LOADING);
        mPresenter.getOrderList(true, mPayState);
    }

    @Override
    public void onRefreshing() {
        mPresenter.getOrderList(true, mPayState);
    }

    @Override
    public void onLoadMore() {
        mPresenter.getOrderList(false, mPayState);
    }

    @Override
    public void getOrderListSuccess(List<OrderList.ListBean> data, boolean isRefresh) {

        if (mErrorLayout.getErrorState() != ErrorLayout.HIDE_LAYOUT) {
            mErrorLayout.setState(ErrorLayout.HIDE_LAYOUT);
        }

        if (isRefresh) {  //刷新
            mSwipeRefresh.setRefreshing(false);
            if (data.size() == 0) { //没有数据
                mErrorLayout.setState(ErrorLayout.NODATA);
            } else {
                mAdapter.clear();
                mAdapter.addAll(data);
            }
        } else {//加载更多
            mAdapter.addAll(data);
            if (data.size() < 10) {
                mAdapter.setState(BaseRecyclerAdapter.STATE_NO_MORE, true);
                mSwipeRefresh.setOnLoading(true);  //设置不可加更多
            } else {
                mSwipeRefresh.setOnLoading(false);  //设置可加加载
            }
        }
    }

    @Override
    public void getOrderListFailure() {
        if (mSwipeRefresh.isRefreshing()) mSwipeRefresh.setRefreshing(false);
        mErrorLayout.setState(ErrorLayout.NETWORK_ERROR);
    }

    @Override
    public void showNetWorkError() {
        mErrorLayout.setState(ErrorLayout.NETWORK_ERROR);
        mSwipeRefresh.setRefreshing(false);
    }

    @Override
    public void onItemClick(int position, long itemId) {
        if (mPayState != null && mPayState.equals("1")) {
            Intent mPayIntent = new Intent(getActivity(), CoursePayActivity.class);
            mPayIntent.putExtra("coursePriceUuid", mAdapter.getItem(position).orderUuid);
            startActivity(mPayIntent);
        }
    }
}
