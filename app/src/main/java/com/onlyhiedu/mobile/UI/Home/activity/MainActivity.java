package com.onlyhiedu.mobile.UI.Home.activity;

import android.support.v7.widget.Toolbar;
import android.widget.RadioGroup;

import com.onlyhiedu.mobile.App.Constants;
import com.onlyhiedu.mobile.Base.SimpleActivity;
import com.onlyhiedu.mobile.R;
import com.onlyhiedu.mobile.UI.Home.fragment.Fragment1;
import com.onlyhiedu.mobile.UI.Home.fragment.Fragment2;
import com.onlyhiedu.mobile.UI.Home.fragment.Fragment3;

import butterknife.BindView;
import me.yokeyword.fragmentation.SupportFragment;

public class MainActivity extends SimpleActivity implements RadioGroup.OnCheckedChangeListener {

    private Fragment1 m1Fragment;
    private Fragment2 m2Fragment;
    private Fragment3 m3Fragment;

    private int showFragment = Constants.TYPE_A;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.home_radio)
    RadioGroup mHomeRadio;

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initEventAndData() {
        setToolBar(mToolbar, "A");
        m1Fragment = new Fragment1();
        m2Fragment = new Fragment2();
        m3Fragment = new Fragment3();
        loadMultipleRootFragment(R.id.fl_main_content, 0, m1Fragment, m2Fragment, m3Fragment);
        mHomeRadio.setOnCheckedChangeListener(this);
        showHideFragment(getTargetFragment(showFragment));
        //peng 第一次提交
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_1:
                showFragment = Constants.TYPE_A;
                break;
            case R.id.rb_2:
                showFragment = Constants.TYPE_B;
                break;
            case R.id.rb_3:
                showFragment = Constants.TYPE_C;
                break;
        }
        showHideFragment(getTargetFragment(showFragment));
    }


    private SupportFragment getTargetFragment(int item) {
        switch (item) {
            case Constants.TYPE_A:
                return m1Fragment;
            case Constants.TYPE_B:
                return m2Fragment;
            case Constants.TYPE_C:
                return m3Fragment;
        }
        return m1Fragment;
    }
}
