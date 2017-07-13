package com.onlyhiedu.mobile.UI.Emc.base;

/**
 * Created by pengpeng on 2017/7/10.
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.hyphenate.easeui.EaseUI;
import com.onlyhiedu.mobile.Base.BaseActivity;
import com.onlyhiedu.mobile.Base.BasePresenter;

public abstract class EaseRxBaseActivity<T extends BasePresenter> extends BaseActivity<T> {

    /**
     * back
     *
     * @param view
     */
    public void back(View view) {
        finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        // cancel the notification
        EaseUI.getInstance().getNotifier().reset();
    }


    protected InputMethodManager inputMethodManager;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        //http://stackoverflow.com/questions/4341600/how-to-prevent-multiple-instances-of-an-activity-when-it-is-launched-with-differ/
        // should be in launcher activity, but all app use this can avoid the problem
        if(!isTaskRoot()){
            Intent intent = getIntent();
            String action = intent.getAction();
            if(intent.hasCategory(Intent.CATEGORY_LAUNCHER) && action.equals(Intent.ACTION_MAIN)){
                finish();
                return;
            }
        }
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    protected void hideSoftKeyboard() {
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
