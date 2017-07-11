package com.onlyhiedu.mobile.UI.Emc.base;

/**
 * Created by pengpeng on 2017/7/10.
 */

import android.view.View;

import com.hyphenate.easeui.EaseUI;
import com.onlyhiedu.mobile.Base.BaseActivity;
import com.onlyhiedu.mobile.Base.BasePresenter;

public abstract class EaseRxBaseActivity<T extends BasePresenter> extends BaseActivity<T> {


    protected abstract void initInject();

    protected abstract int getLayout();
    /**
     * back
     *
     * @param view
     */
    public void back(View view) {
        finish();
    }
}
