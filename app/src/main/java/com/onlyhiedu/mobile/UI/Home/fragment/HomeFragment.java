package com.onlyhiedu.mobile.UI.Home.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.onlyhiedu.mobile.App.App;
import com.onlyhiedu.mobile.Base.BaseFragment;
import com.onlyhiedu.mobile.Base.BaseRecyclerAdapter.OnItemClickListener;
import com.onlyhiedu.mobile.Model.bean.HomeBannerBean;
import com.onlyhiedu.mobile.Model.bean.HomeTeacher;
import com.onlyhiedu.mobile.Model.bean.TypeListInfo;
import com.onlyhiedu.mobile.R;
import com.onlyhiedu.mobile.UI.Consumption.activity.ConsumeActivity;
import com.onlyhiedu.mobile.UI.Course.activity.CourseDiscountActivity;
import com.onlyhiedu.mobile.UI.Home.activity.MainActivity;
import com.onlyhiedu.mobile.UI.Home.adapter.HomeNewsAdapter;
import com.onlyhiedu.mobile.UI.Home.adapter.TeacherPageAdapter;
import com.onlyhiedu.mobile.UI.Home.presenter.HomePresenter;
import com.onlyhiedu.mobile.UI.Home.presenter.contract.HomeContract;
import com.onlyhiedu.mobile.Utils.SPUtil;
import com.onlyhiedu.mobile.Utils.UIUtils;
import com.onlyhiedu.mobile.Widget.GlideImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.onlyhiedu.mobile.UI.Home.activity.MainActivity.CALL_REQUEST_CODE;
import static com.onlyhiedu.mobile.UI.Setting.activity.AboutActivity.PHONE_NUM;


/**
 * Created by pengpeng on 2017/5/24.
 */

public class HomeFragment extends BaseFragment<HomePresenter> implements SwipeRefreshLayout.OnRefreshListener, HomeContract.View {


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
    private HomeBannerBean mData;
    private Intent mTypeIntent;


    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fr_home;
    }

    @Override
    protected void initView() {

        mPresenter.getBannerData();
        mPresenter.getTeacherData();
        mPresenter.getArticle();

    }

    @Override
    protected void initData() {
        initListener();
    }

    @Override
    public void showBannerData(HomeBannerBean data) {
        mData = data;
        ArrayList<String> images = new ArrayList<>();
        for (int i = 0; i < data.list.size(); i++) {
            images.add(data.list.get(i).image);
        }
        mBanner.setImageLoader(new GlideImageLoader());
        mBanner.setImages(images);
        mBanner.setDelayTime(5000);
        mBanner.start();
    }

    @Override
    public void showTeacherData(HomeTeacher data) {
        mViewPager.setAdapter(new TeacherPageAdapter(mContext, data));
        mViewPager.setOffscreenPageLimit(5);

    }

    @Override
    public void showArticle(HomeBannerBean data) {
        mNewsAdapter = new HomeNewsAdapter(mContext);
        UIUtils.setHorizontalLayoutManager(mContext, mRecyclerView_Good, mNewsAdapter);
        mNewsAdapter.addAll(data.list);
        mNewsAdapter.setOnItemClickListener(itemClickListener);
    }

    @Override
    public void showTypeListSucess(List<TypeListInfo> data) {
        mTypeIntent = new Intent(mContext, CourseDiscountActivity.class);
        Bundle extras = new Bundle();
        if (data != null && data.size() != 0) {
            extras.putSerializable("typeList", (Serializable) data);
            mTypeIntent.putExtras(extras);
            startActivity(mTypeIntent);
        }
    }


    private void initListener() {
        mRefreshLayout.setOnRefreshListener(this);
        mBanner.setOnBannerListener(bannerListener);

    }


    OnItemClickListener itemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(int position, long itemId) {
            UIUtils.startHomeNewsWebViewAct(mContext, mNewsAdapter.getItem(position).link, " 教育头条");
        }
    };

    OnBannerListener bannerListener = new OnBannerListener() {
        @Override
        public void OnBannerClick(int position) {
            UIUtils.startHomeNewsWebViewAct(mContext, mData.list.get(position).link, mData.list.get(position).title);
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
                if (!SPUtil.getGuest()) {
                    startActivity(new Intent(mContext, ConsumeActivity.class));
                } else {
                    UIUtils.startGuestLoginActivity(mContext,0);
                }
                break;
            case R.id.tv_service:
                if (UIUtils.requestPermission(mActivity, MainActivity.CALL_REQUEST_CODE, new String[]{Manifest.permission.CALL_PHONE})) {
                    UIUtils.callLine(mActivity, PHONE_NUM);
                }
                break;
            case R.id.tv_information:
                //支付模块暂时隐藏
                UIUtils.startHomeNewsWebViewAct(mContext, urls[0], titles[0]);
//                //课程优惠
//                if(App.bIsGuestLogin){
//                    UIUtils.startGuestLoginActivity(mContext,0);
//                }else{
//                    mPresenter.getActivityTypeList();
//                }
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


    @Override
    public void showError(String msg) {
        Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
    }

}
