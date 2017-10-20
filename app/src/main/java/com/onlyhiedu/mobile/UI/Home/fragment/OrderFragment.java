package com.onlyhiedu.mobile.UI.Home.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.onlyhiedu.mobile.Base.BaseFragment;
import com.onlyhiedu.mobile.Base.BaseRecyclerAdapter;
import com.onlyhiedu.mobile.Model.bean.OrderList;
import com.onlyhiedu.mobile.Model.event.MineOrdersActivityTabSelecPos;
import com.onlyhiedu.mobile.R;
import com.onlyhiedu.mobile.UI.Course.activity.CoursePayActivity;
import com.onlyhiedu.mobile.UI.Course.adapter.OrderAdapter;
import com.onlyhiedu.mobile.UI.Home.presenter.OrdersPresenter;
import com.onlyhiedu.mobile.UI.Home.presenter.contract.OrdersContract;
import com.onlyhiedu.mobile.Utils.UIUtils;
import com.onlyhiedu.mobile.Widget.ErrorLayout;
import com.onlyhiedu.mobile.Widget.RecyclerRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/8/1.
 */

public class OrderFragment extends BaseFragment<OrdersPresenter> implements OrdersContract.View, View.OnClickListener, RecyclerRefreshLayout.SuperRefreshLayoutListener, BaseRecyclerAdapter.OnItemClickListener, OrderAdapter.OnGoPayTextClickListener {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh)
    RecyclerRefreshLayout mSwipeRefresh;
    @BindView(R.id.error_layout)
    ErrorLayout mErrorLayout;


    private String mPayState;
    private boolean mPendingPay;
    private OrderAdapter mAdapter;

    public static final String TAG = OrderFragment.class.getSimpleName();
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
            mPendingPay = getArguments().getBoolean("mPendingPay");
        }

        mErrorLayout.setState(ErrorLayout.NETWORK_LOADING);
        mErrorLayout.setOnLayoutClickListener(this);

        mSwipeRefresh.setSuperRefreshLayoutListener(this);

        mAdapter = new OrderAdapter(mContext);
        mAdapter.setState(BaseRecyclerAdapter.STATE_HIDE, false);
        UIUtils.setRecycleAdapter(mContext, mRecyclerView, mAdapter);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnGoPayTextClickListener(this);
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
      /*  if (mAdapter.getItem(position).orderStatus == 1) {
            Intent mPayIntent = new Intent(getActivity(), CoursePayActivity.class);
            mPayIntent
                    .putExtra("originalPrice", (long) Double.parseDouble(mAdapter.getItem(position).originalPrice))
                    .putExtra("nowPrice", (long) Double.parseDouble(mAdapter.getItem(position).money))
                    .putExtra("specialPrice", (long) Double.parseDouble(mAdapter.getItem(position).discountPrice))
                    .putExtra("mPayFrom", "order")
                    .putExtra("coursePriceUuid", mAdapter.getItem(position).orderUuid)
                    .putExtra("coursePricePackageName", mAdapter.getItem(position).coursePricePackageName);
            startActivity(mPayIntent);
        }*/
    }

    //去支付按钮
    @Override
    public void onGoPayTextClick(String text, int position) {
        if ("去支付".equals(text)&&mPendingPay) {
            Intent intent = new Intent();
            intent
                    .putExtra("originalPrice", (long) Double.parseDouble(mAdapter.getItem(position).originalPrice))
                    .putExtra("nowPrice", (long) Double.parseDouble(mAdapter.getItem(position).money))
                    .putExtra("specialPrice", (long) Double.parseDouble(mAdapter.getItem(position).discountPrice))
                    .putExtra("mPayFrom", "order")
                    .putExtra("coursePriceUuid", mAdapter.getItem(position).orderUuid)
                    .putExtra("coursePricePackageName", mAdapter.getItem(position).coursePricePackageName);
            getActivity().setResult(10001, intent);
            getActivity().finish();
        } else {
            if (mAdapter.getItem(position).orderStatus == 1) {
                Intent mPayIntent = new Intent(mContext, CoursePayActivity.class);
                mPayIntent
                        .putExtra("originalPrice", (long) Double.parseDouble(mAdapter.getItem(position).originalPrice))
                        .putExtra("nowPrice", (long) Double.parseDouble(mAdapter.getItem(position).money))
                        .putExtra("specialPrice", (long) Double.parseDouble(mAdapter.getItem(position).discountPrice))
                        .putExtra("mPayFrom", "order")
                        .putExtra("mPayFromOrder", true)
                        .putExtra("coursePriceUuid", mAdapter.getItem(position).orderUuid)
                        .putExtra("coursePricePackageName", mAdapter.getItem(position).coursePricePackageName);

                Log.d(TAG, "coursePriceUuid:"+mAdapter.getItem(position).orderUuid);
                startActivity(mPayIntent);
                getActivity().finish();
            }
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEventMain(MineOrdersActivityTabSelecPos event) {
        if (mAdapter == null) {
            return;
        }
        mAdapter.clear();
        mPresenter.getOrderList(true, mPayState);
    }

}
