package com.onlyhiedu.mobile.UI.Home.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.onlyhiedu.mobile.Base.SimpleFragment;
import com.onlyhiedu.mobile.R;
import com.onlyhiedu.mobile.UI.Home.adapter.CourseFragmentAdapter;
import com.onlyhiedu.mobile.Utils.UIUtils;
import com.onlyhiedu.mobile.Widget.ErrorLayout;
import com.onlyhiedu.mobile.Widget.RecyclerRefreshLayout;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/3/1.
 */

public class CourseFragment extends SimpleFragment implements View.OnClickListener {

    private CourseFragmentAdapter mAdapter;


    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh)
    RecyclerRefreshLayout mSwipeRefresh;
    @BindView(R.id.error_layout)
    ErrorLayout mErrorLayout;

    @Override
    protected int getLayoutId() {
        return R.layout.layout_recycle_refresh;
    }

    @Override
    protected void initEventAndData() {
        mErrorLayout.setState(ErrorLayout.HIDE_LAYOUT);
        mErrorLayout.setOnLayoutClickListener(this);
        mAdapter = new CourseFragmentAdapter(mContext);
        mAdapter.addItem("");
        mAdapter.addItem("");
        mAdapter.addItem("");
        for (int i = 0; i < 15; i++) {
            mAdapter.addItem("");
        }
        UIUtils.setRecycleAdapter(mContext,mRecyclerView,mAdapter);
    }

    @Override
    public void onClick(View view) {
        mErrorLayout.setState(ErrorLayout.NETWORK_LOADING);
    }


}
