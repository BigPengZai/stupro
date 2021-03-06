package com.onlyhiedu.pro.Base;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.onlyhiedu.pro.App.App;
import com.onlyhiedu.pro.App.AppManager;
import com.onlyhiedu.pro.Dagger.Component.ActivityComponent;
import com.onlyhiedu.pro.Dagger.Component.DaggerActivityComponent;
import com.onlyhiedu.pro.Dagger.Modul.ActivityModule;
import com.onlyhiedu.pro.R;
import com.onlyhiedu.pro.Widget.Swipeback.app.SwipeBackActivity;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * MVP activity基类
 * Created by xuwc on 2016/11/24.
 */
public abstract class BaseActivity<T extends BasePresenter> extends SwipeBackActivity implements BaseView {

    @Inject
    protected T mPresenter;
    protected Activity mContext;

    private Unbinder mUnBinder;
    //String name = BaseActivity.class.getSimpleName();
    private String mPageName = this.getClass().getSimpleName();
    protected InputMethodManager inputMethodManager;
    public Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        //友盟  此方法与统计分析sdk中统计日活的方法无关！请务必调用此方法！
        PushAgent.getInstance(this).onAppStart();
        //竖屏锁定
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mUnBinder = ButterKnife.bind(this);
        mContext = this;
        initInject();
        if (mPresenter != null)
            mPresenter.attachView(this);
        AppManager.getAppManager().addActivity(this);

        initView();
        initData();
    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(mPageName);
        MobclickAgent.onResume(mContext);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(mPageName);
        MobclickAgent.onPause(mContext);
    }


    protected void setToolBar(String title) {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView tvTitle = (TextView) mToolbar.findViewById(R.id.title);
        tvTitle.setText(title);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressedSupport();
            }
        });
    }

    protected void setToolBar(String title, int iconId) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView tvTitle = (TextView) toolbar.findViewById(R.id.title);
        tvTitle.setText(title);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(iconId);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressedSupport();
            }
        });
    }

    protected ActivityComponent getActivityComponent() {
        return DaggerActivityComponent.builder()
                .appComponent(App.getAppComponent())
                .activityModule(getActivityModule())
                .build();
    }

    @Override
    public void useNightMode(boolean isNight) {
        if (isNight) {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_NO);
        }
        recreate();
    }

    protected ActivityModule getActivityModule() {
        return new ActivityModule(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
            mPresenter = null;
        }
        mUnBinder.unbind();

        AppManager.getAppManager().removeActivity(this);
    }


    protected abstract void initInject();

    protected abstract int getLayout();

    protected void initView() {
    }

    protected void initData() {
    }

    @Override
    public void onBackPressedSupport() {
        super.onBackPressedSupport();
    }


}
