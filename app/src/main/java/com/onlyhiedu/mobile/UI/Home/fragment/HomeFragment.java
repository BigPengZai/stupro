package com.onlyhiedu.mobile.UI.Home.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.onlyhiedu.mobile.Base.BaseRecyclerAdapter.OnItemClickListener;
import com.onlyhiedu.mobile.Base.SimpleFragment;
import com.onlyhiedu.mobile.Model.bean.HomeNews;
import com.onlyhiedu.mobile.R;
import com.onlyhiedu.mobile.UI.Consumption.activity.ConsumeActivity;
import com.onlyhiedu.mobile.UI.Home.activity.MainActivity;
import com.onlyhiedu.mobile.UI.Home.adapter.HomeNewsAdapter;
import com.onlyhiedu.mobile.UI.Home.adapter.TeacherPageAdapter;
import com.onlyhiedu.mobile.UI.Info.activity.MyInfoActivity;
import com.onlyhiedu.mobile.Utils.UIUtils;
import com.onlyhiedu.mobile.Widget.GlideImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

import static com.onlyhiedu.mobile.UI.Home.activity.MainActivity.CALL_REQUEST_CODE;
import static com.onlyhiedu.mobile.UI.Setting.activity.AboutActivity.PHONE_NUM;


/**
 * Created by pengpeng on 2017/5/24.
 */

public class HomeFragment extends SimpleFragment implements SwipeRefreshLayout.OnRefreshListener {


    private HomeNewsAdapter mNewsAdapter;
    private String urls[] = {"http://www.onlyhi.cn/", "http://www.onlyhi.cn/", "http://www.onlyhi.cn/"};
    private String titles[] = {"首页", "暑期课程", "明星课程"};

    @BindView(R.id.banner)
    Banner mBanner;
    @BindView(R.id.swipe)
    SwipeRefreshLayout mRefreshLayout;
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
        mBanner.setDelayTime(5000);
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
            UIUtils.startHomeNewsWebViewAct(mContext, mNewsAdapter.getItem(position).url, " 教育头条");
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


    @OnClick({R.id.tv_hear, R.id.tv_consume, R.id.tv_service, R.id.tv_information})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_hear:
                UIUtils.startHomeNewsWebViewAct(mContext, urls[0], titles[0]);
                break;
            case R.id.tv_consume:
                startActivity(new Intent(mContext, ConsumeActivity.class));
                break;
            case R.id.tv_service:
                if (UIUtils.requestPermission(mActivity, MainActivity.CALL_REQUEST_CODE, new String[]{Manifest.permission.CALL_PHONE})) {
                    UIUtils.callLine(mActivity, PHONE_NUM);
                }
                break;
            case R.id.tv_information:
                startActivity(new Intent(mContext, MyInfoActivity.class));
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CALL_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    UIUtils.callLine(mActivity, PHONE_NUM);
                } else {
                    Toast.makeText(mActivity, "拨打电话权限未授权", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


}
