package com.onlyhiedu.mobile.Base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.onlyhiedu.mobile.App.AppManager;
import com.onlyhiedu.mobile.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.yokeyword.fragmentation.SupportActivity;

/**
 * 无MVP的activity
 * Created by xuwc on 2016/11/30.
 */
public abstract class SimpleActivity extends SupportActivity {


    protected Activity mContext;
    private Unbinder mUnBinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        mUnBinder = ButterKnife.bind(this);
        mContext = this;
        AppManager.getAppManager().addActivity(this);
        initEventAndData();

    }


    protected void setToolBar(String title) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView tvTitle = (TextView) toolbar.findViewById(R.id.title);
        tvTitle.setText(title);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
//        toolbar.setNavigationIcon(R.mipmap.back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressedSupport();
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().removeActivity(this);
        mUnBinder.unbind();
    }

    protected abstract int getLayout();

    protected abstract void initEventAndData();

}
