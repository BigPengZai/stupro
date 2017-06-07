package com.onlyhiedu.mobile.UI.Home.fragment;

import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ScrollView;
import android.widget.Toast;

import com.onlyhiedu.mobile.Base.BaseRecyclerAdapter.OnItemClickListener;
import com.onlyhiedu.mobile.Base.SimpleFragment;
import com.onlyhiedu.mobile.Model.bean.HomeNews;
import com.onlyhiedu.mobile.R;
import com.onlyhiedu.mobile.UI.Home.adapter.HomeNewsAdapter;
import com.onlyhiedu.mobile.UI.Home.adapter.TeacherPageAdapter;
import com.onlyhiedu.mobile.Utils.UIUtils;
import com.onlyhiedu.mobile.Widget.GlideImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;

import butterknife.BindView;


/**
 * Created by pengpeng on 2017/5/24.
 */

public class HomeFragment extends SimpleFragment implements SwipeRefreshLayout.OnRefreshListener {


    private HomeNewsAdapter mNewsAdapter;
    private String urls[] = {"http://www.onlyhi.cn/", "http://www.onlyhi.cn/z/summer.html", "http://www.onlyhi.cn/z/StarCourse.html"};
    private String titles[] = {"首页", "暑期课程", "明星课程"};

    @BindView(R.id.banner)
    Banner mBanner;
    @BindView(R.id.swipe)
    SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.scroll_view)
    ScrollView mScrollView;
    @BindView(R.id.recycler_good)
    RecyclerView mRecyclerView_Good;
    @BindView(R.id.viewpager)
    ViewPager mViewPager;


    @Override
    protected int getLayoutId() {
        return R.layout.fr_home;
    }

    @Override
    protected void initEventAndData() {

        initBanner();
        initData();
        initListener();
    }

    private void initListener() {
        mRefreshLayout.setOnRefreshListener(this);
        mBanner.setOnBannerListener(bannerListener);
        mNewsAdapter.setOnItemClickListener(itemClickListener);
    }

    private void initBanner() {
        ArrayList<Integer> images = new ArrayList<>();
        images.add(R.mipmap.page1);
        images.add(R.mipmap.page2);
        images.add(R.mipmap.page3);
        mBanner.setImageLoader(new GlideImageLoader());
        mBanner.setImages(images);
        mBanner.setDelayTime(3000);
        mBanner.start();
    }

    private void initData() {
        mViewPager.setAdapter(new TeacherPageAdapter(mContext));
        mViewPager.setOffscreenPageLimit(5);
        mNewsAdapter = new HomeNewsAdapter(mContext);
        UIUtils.setHorizontalLayoutManager(mContext, mRecyclerView_Good, mNewsAdapter);
        new HomeNews();
        mNewsAdapter.addAll(HomeNews.datas);
    }

    OnItemClickListener itemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(int position, long itemId) {
            UIUtils.startHomeNewsWebViewAct(mContext, mNewsAdapter.getItem(position).url, " 精选好闻");
        }
    };

    OnBannerListener bannerListener = new OnBannerListener() {
        @Override
        public void OnBannerClick(int position) {
            UIUtils.startHomeNewsWebViewAct(mContext, urls[position], titles[position]);
        }
    };

    @Override
    public void onRefresh() {
        Log.d("Swipe", "Refreshing Number");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(false);
                Toast.makeText(mContext, "已经更新最新内容", Toast.LENGTH_SHORT).show();
            }
        }, 2000);
    }


    @Override
    public void onStart() {
        super.onStart();
        mBanner.startAutoPlay();
    }

    @Override
    public void onStop() {
        super.onStop();
        mBanner.stopAutoPlay();
    }


}
