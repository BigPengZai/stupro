package com.onlyhiedu.mobile.UI.Home.fragment;


import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.onlyhiedu.mobile.Base.BaseFragment;
import com.onlyhiedu.mobile.Base.BaseRecyclerAdapter;
import com.onlyhiedu.mobile.Model.bean.CourseList;
import com.onlyhiedu.mobile.Model.bean.RoomInfo;
import com.onlyhiedu.mobile.R;
import com.onlyhiedu.mobile.UI.Home.activity.HomeNewsWebViewActivity;
import com.onlyhiedu.mobile.UI.Home.activity.MainActivity;
import com.onlyhiedu.mobile.UI.Home.adapter.CourseFragmentAdapter;
import com.onlyhiedu.mobile.UI.Home.presenter.CoursePresenter;
import com.onlyhiedu.mobile.UI.Home.presenter.contract.CourseContract;
import com.onlyhiedu.mobile.Utils.UIUtils;
import com.onlyhiedu.mobile.Widget.ErrorLayout;
import com.onlyhiedu.mobile.Widget.RecyclerRefreshLayout;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

import butterknife.BindView;

import static com.onlyhiedu.mobile.Model.http.onlyApis.coursePlayback;

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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void initView() {
        mErrorLayout.setOnLayoutClickListener(this);
        mErrorLayout.setListenerPhone(phoneListener);

        mSwipeRefresh.setSuperRefreshLayoutListener(this);

        mAdapter = new CourseFragmentAdapter(mContext);
        mAdapter.setState(BaseRecyclerAdapter.STATE_HIDE, false);
        UIUtils.setRecycleAdapter(mContext, mRecyclerView, mAdapter);
        mAdapter.setOnItemClickListener(this);
    }

    @Override
    protected void initData() {
    }

    @Override
    public void onResume() {
        super.onResume();
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
    public void showCourseListSuccess(List<CourseList.ListBean> data, boolean isRefresh) {

        if (mErrorLayout != null && mErrorLayout.getErrorState() != ErrorLayout.HIDE_LAYOUT) {
            mErrorLayout.setState(ErrorLayout.HIDE_LAYOUT);
        }

        if (isRefresh) {  //刷新
            mSwipeRefresh.setRefreshing(false);
            if (data == null || data.size() == 0) { //没有数据
                mErrorLayout.isLlPhoneVisibility(View.VISIBLE);
                mErrorLayout.setState(ErrorLayout.NODATA);
            } else {
                mErrorLayout.isLlPhoneVisibility(View.GONE);
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
    public void showCourseListFailure() {
        if (mSwipeRefresh.isRefreshing()) mSwipeRefresh.setRefreshing(false);
        mErrorLayout.setState(ErrorLayout.NODATA);
    }

    @Override
    public void showNetWorkError() {
        mErrorLayout.setState(ErrorLayout.NETWORK_ERROR);
        mSwipeRefresh.setRefreshing(false);
    }

    @Override
    public void showRoomInfoSucess(RoomInfo roomInfo) {

    }

    View.OnClickListener phoneListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            MainActivity activity = (MainActivity) getActivity();
            activity.requestPermissions(activity, activity.CALL_REQUEST_CODE, new String[]{Manifest.permission.CALL_PHONE});
        }
    };

    @Override
    public void onClick(View view) {
        mErrorLayout.setState(ErrorLayout.NETWORK_LOADING);
        mPresenter.getEndCourseList(true);
    }

    @Override
    public void onItemClick(int position, long itemId) {
        MobclickAgent.onEvent(mContext, "item_finish_item");
        String url = coursePlayback + mAdapter.getItem(position).courseUuid + "&xp=1";
        startActivity(new Intent(mContext, HomeNewsWebViewActivity.class).putExtra(HomeNewsWebViewActivity.URL, url).putExtra(HomeNewsWebViewActivity.TITLE, "课程回放"));
    }

    @Override
    public void showError(String msg) {
        Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
    }
}
