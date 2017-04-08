package com.onlyhiedu.mobile.UI.Home.fragment;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.onlyhiedu.mobile.Base.BaseFragment;
import com.onlyhiedu.mobile.Base.BaseRecyclerAdapter;
import com.onlyhiedu.mobile.Model.bean.CourseList;
import com.onlyhiedu.mobile.Model.bean.RoomInfo;
import com.onlyhiedu.mobile.R;
import com.onlyhiedu.mobile.UI.Home.activity.MainActivity;
import com.onlyhiedu.mobile.UI.Home.adapter.CourseFragmentAdapter;
import com.onlyhiedu.mobile.UI.Home.presenter.CoursePresenter;
import com.onlyhiedu.mobile.UI.Home.presenter.contract.CourseContract;
import com.onlyhiedu.mobile.Utils.DialogUtil;
import com.onlyhiedu.mobile.Utils.UIUtils;
import com.onlyhiedu.mobile.Widget.ErrorLayout;
import com.onlyhiedu.mobile.Widget.RecyclerRefreshLayout;

import java.util.List;

import butterknife.BindView;
import io.agore.openvcall.ui.ChatActivity;

/**
 * Created by Administrator on 2017/3/1.
 */

public class CourseFragment extends BaseFragment<CoursePresenter>
        implements
        View.OnClickListener,
        BaseRecyclerAdapter.OnItemClickListener,
        CourseContract.View,
        RecyclerRefreshLayout.SuperRefreshLayoutListener {

    public static final String TAG = CourseFragment.class.getSimpleName();


    private CourseFragmentAdapter mAdapter;
    private Intent mIntent;
    private CourseList.ListBean mItem;
    private Dialog mDialog;


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
        mErrorLayout.setListenerPhone(phoneListener);

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
        mPresenter.getCourseList(true);
    }

    @Override
    public void onLoadMore() {
        mPresenter.getCourseList(false);
    }

    @Override
    public void showCourseListSuccess(List<CourseList.ListBean> data, boolean isRefresh) {

        if (mErrorLayout.getErrorState() != ErrorLayout.HIDE_LAYOUT) {
            mErrorLayout.setState(ErrorLayout.HIDE_LAYOUT);
        }

//        if (mSwipeRefresh.isRefreshing()) {
//            mAdapter.clear();
//            mAdapter.addAll(data);
//            mSwipeRefresh.setRefreshing(false);
//
//        } else {
//            mAdapter.addAll(data);
//            mSwipeRefresh.setOnLoading(false);  //设置可加加载
//        }
//        if (data == null || data.size() < 10) {
//            mAdapter.setState(BaseRecyclerAdapter.STATE_NO_MORE, true);
//            mSwipeRefresh.setOnLoading(true);  //设置不可加更多
//        } else {
//            mAdapter.setState(BaseRecyclerAdapter.STATE_LOAD_MORE, true);
//        }

        if (isRefresh) {  //刷新
            mSwipeRefresh.setRefreshing(false);
            if (data.size() == 0) { //没有数据
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
        mErrorLayout.setState(ErrorLayout.NETWORK_ERROR);
    }

    @Override
    public void onClick(View view) {
        mErrorLayout.setState(ErrorLayout.NETWORK_LOADING);
        mPresenter.getCourseList(true);
    }


    @Override
    public void showNetWorkError() {
        mErrorLayout.setState(ErrorLayout.NETWORK_ERROR);
        mSwipeRefresh.setRefreshing(false);
        DialogUtil.dismiss(mDialog);
    }

    @Override
    public void onItemClick(int position, long itemId) {
        mItem = mAdapter.getItem(position);
        Log.d(TAG, "uuid:" + mItem.getUuid());
        if (mItem != null) {
            mDialog = DialogUtil.showProgressDialog(mContext, "正在进入房间...", true, true);
            mPresenter.getRoomInfoList(mItem.getUuid());
        }
    }


    View.OnClickListener phoneListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            MainActivity activity = (MainActivity) getActivity();
            activity.requestPermissions(activity, activity.CALL_REQUEST_CODE, new String[]{Manifest.permission.CALL_PHONE});
        }
    };


    @Override
    public void showRoomInfoSucess(RoomInfo roomInfo) {
        if (roomInfo != null && mItem.getUuid() != null) {

            DialogUtil.dismiss(mDialog);

            mIntent = new Intent(mActivity, ChatActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("roomInfo", roomInfo);
            bundle.putString("uuid", mItem.getUuid());
            bundle.putString("uuid", mItem.getUuid());
            Log.d(TAG, "uuid:" + mItem.getUuid());
            mIntent.putExtras(bundle);
            mActivity.startActivity(mIntent);
        }
    }


    @Override
    public void showError(String msg) {
        DialogUtil.dismiss(mDialog);
        Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
    }


}
