package com.onlyhiedu.mobile.UI.Home.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.onlyhiedu.mobile.Base.SimpleFragment;
import com.onlyhiedu.mobile.R;
import com.onlyhiedu.mobile.UI.Home.activity.AboutActivity;
import com.onlyhiedu.mobile.UI.Home.activity.MyInfoActivity;
import com.onlyhiedu.mobile.UI.Home.activity.SettingActivity;

import butterknife.BindView;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by xuwc on 2017/2/18.
 */

public class MeFragment extends SimpleFragment implements View.OnClickListener {
    @BindView(R.id.ll_info)
    LinearLayout mLl_Info;
    @BindView(R.id.ll_setting)
    LinearLayout mLl_Setting;
    @BindView(R.id.ll_about)
    LinearLayout mLl_about;

    public static final String TAG = MeFragment.class.getSimpleName();

    @Override
    protected int getLayoutId() {
        return R.layout.fr_me;
    }

    @Override
    protected void initEventAndData() {
        initListener();
    }
    private void initListener() {
        mLl_Info.setOnClickListener(this);
        mLl_Setting.setOnClickListener(this);
        mLl_about.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_info:
                Log.d(TAG, "1");
                getContext().startActivity(new Intent(getContext(), MyInfoActivity.class));
                break;
            case R.id.ll_setting:
                getContext().startActivity(new Intent(getContext(), SettingActivity.class));
                Log.d(TAG, "2");
                break;
            case R.id.ll_about:
                getContext().startActivity(new Intent(getContext(), AboutActivity.class));
                Log.d(TAG, "3");
                break;
        }
    }
}
