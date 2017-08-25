package com.onlyhiedu.mobile.Base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.onlyhiedu.mobile.App.App;
import com.onlyhiedu.mobile.Dagger.Component.DaggerFragmentComponent;
import com.onlyhiedu.mobile.Dagger.Component.FragmentComponent;
import com.onlyhiedu.mobile.Dagger.Modul.FragmentModule;
import com.umeng.analytics.MobclickAgent;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * MVP
 * Created by xuwc on 2016/11/24.
 */
public abstract class BaseFragment<T extends BasePresenter> extends SupportFragment implements BaseView {


    @Inject
    protected T mPresenter;
    protected View mView;
    protected Activity mActivity;
    protected Context mContext;


    private Unbinder mUnBinder;
    //String name = BaseActivity.class.getSimpleName();
    private String mPageName = this.getClass().getSimpleName();
    @Override
    public void onAttach(Context context) {
        mActivity = (Activity) context;
        mContext = context;
        super.onAttach(context);

    }

    protected FragmentComponent getFragmentComponent() {
        return DaggerFragmentComponent.builder()
                .appComponent(App.getAppComponent())
                .fragmentModule(getFragmentModule())
                .build();
    }

    protected FragmentModule getFragmentModule() {
        return new FragmentModule(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(getLayoutId(), null);
        initInject();
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.attachView(this);
        mUnBinder = ButterKnife.bind(this, view);
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
    //类库支持的懒加载
    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        if(savedInstanceState == null ){
            initView();
            initData();
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnBinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
            mPresenter = null;
        }
    }

    protected abstract void initInject();

    protected abstract int getLayoutId();

    protected abstract void initView();

    protected abstract void initData();
}
