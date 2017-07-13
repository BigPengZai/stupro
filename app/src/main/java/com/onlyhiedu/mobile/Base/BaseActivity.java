package com.onlyhiedu.mobile.Base;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.onlyhiedu.mobile.App.App;
import com.onlyhiedu.mobile.App.AppManager;
import com.onlyhiedu.mobile.Dagger.Component.ActivityComponent;
import com.onlyhiedu.mobile.Dagger.Component.DaggerActivityComponent;
import com.onlyhiedu.mobile.Dagger.Modul.ActivityModule;
import com.onlyhiedu.mobile.R;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.yokeyword.fragmentation.SupportActivity;

/**
 * MVP activity基类
 * Created by xuwc on 2016/11/24.
 */
public abstract class BaseActivity<T extends BasePresenter> extends SupportActivity implements BaseView {

    @Inject
    protected T mPresenter;
    protected Activity mContext;

    private Unbinder mUnBinder;
    //String name = BaseActivity.class.getSimpleName();
    private String mPageName = this.getClass().getSimpleName();
    protected InputMethodManager inputMethodManager;
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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView tvTitle = (TextView) toolbar.findViewById(R.id.title);
        tvTitle.setText(title);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
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
