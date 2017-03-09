package com.onlyhiedu.mobile.UI.Setting.activity;

import com.onlyhiedu.mobile.Base.SimpleActivity;
import com.onlyhiedu.mobile.R;

/**
 * Created by pengpeng on 2017/3/3.
 */

public class FeedBackActivity extends SimpleActivity   {

    @Override
    protected int getLayout() {
        return R.layout.activity_feed;
    }

    @Override
    protected void initEventAndData() {
        setToolBar("意见反馈");
    }


}
