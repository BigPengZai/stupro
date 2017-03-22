package com.onlyhiedu.mobile.UI.Home.fragment;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.onlyhiedu.mobile.Base.BaseFragment;
import com.onlyhiedu.mobile.Base.BaseRecyclerAdapter;
import com.onlyhiedu.mobile.Model.bean.CourseList;
import com.onlyhiedu.mobile.R;
import com.onlyhiedu.mobile.UI.Home.adapter.CourseFragmentAdapter;
import com.onlyhiedu.mobile.UI.Home.presenter.CoursePresenter;
import com.onlyhiedu.mobile.UI.Home.presenter.contract.CourseContract;
import com.onlyhiedu.mobile.Utils.UIUtils;
import com.onlyhiedu.mobile.Widget.ErrorLayout;
import com.onlyhiedu.mobile.Widget.RecyclerRefreshLayout;

import java.util.List;

import butterknife.BindView;
import io.agore.openvcall.ui.RoomActivity;

/**
 * Created by xwc on 2017/3/1.
 */

public class CourseRecordFragment extends BaseFragment<CoursePresenter>
        implements
        View.OnClickListener,
        BaseRecyclerAdapter.OnItemClickListener,
        CourseContract.View,
        RecyclerRefreshLayout.SuperRefreshLayoutListener {

    private CourseFragmentAdapter mAdapter;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh)
    RecyclerRefreshLayout mSwipeRefresh;
    @BindView(R.id.error_layout)
    ErrorLayout mErrorLayout;

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
        mErrorLayout.setState(ErrorLayout.NETWORK_LOADING);
        mErrorLayout.setOnLayoutClickListener(this);

        mSwipeRefresh.setSuperRefreshLayoutListener(this);

        mAdapter = new CourseFragmentAdapter(mContext);
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
    public void onRefreshing() {
        mPresenter.getEndCourseList(true);
    }

    @Override
    public void onLoadMore() {
        mPresenter.getEndCourseList(false);
    }

    @Override
    public void showCourseListSuccess(List<CourseList.ListBean> data) {

        if (mSwipeRefresh.isRefreshing()) {
            mErrorLayout.setState(ErrorLayout.HIDE_LAYOUT);
            mAdapter.clear();
            mAdapter.addAll(data);
            mSwipeRefresh.setRefreshing(false);
        } else {
            mAdapter.addAll(data);
            mSwipeRefresh.setOnLoading(false);  //设置可加加载
        }
        if (data == null || data.size() < 10) {
            mAdapter.setState(BaseRecyclerAdapter.STATE_NO_MORE, true);
            mSwipeRefresh.setOnLoading(true);  //设置不可加更多
        } else {
            mAdapter.setState(BaseRecyclerAdapter.STATE_LOAD_MORE, true);
        }
    }

    @Override
    public void showCourseListFailure() {
        if (mSwipeRefresh.isRefreshing()) {
            mErrorLayout.setState(ErrorLayout.NETWORK_ERROR);
            mSwipeRefresh.setRefreshing(false);
        }else{
            mAdapter.setState(BaseRecyclerAdapter.STATE_LOAD_ERROR, true);
        }
    }

    @Override
    public void showNetWorkError() {
        mErrorLayout.setState(ErrorLayout.NETWORK_ERROR);
        mSwipeRefresh.setRefreshing(false);
    }

    @Override
    public void onClick(View view) {
        mErrorLayout.setState(ErrorLayout.NETWORK_LOADING);
        mPresenter.getEndCourseList(true);
    }

    @Override
    public void onItemClick(int position, long itemId) {
        mActivity.startActivity(new Intent(mActivity, RoomActivity.class));
    }

    @Override
    public void showError(String msg) {
        Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
    }
}
