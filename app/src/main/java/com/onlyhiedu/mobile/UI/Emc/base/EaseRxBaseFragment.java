package com.onlyhiedu.mobile.UI.Emc.base;

import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.hyphenate.easeui.R;
import com.hyphenate.easeui.widget.EaseTitleBar;
import com.onlyhiedu.mobile.Base.BaseFragment;
import com.onlyhiedu.mobile.Base.BasePresenter;

public abstract class EaseRxBaseFragment <T extends BasePresenter> extends BaseFragment<T> {


    protected EaseTitleBar titleBar;
    protected InputMethodManager inputMethodManager;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        //noinspection ConstantConditions
        titleBar = (EaseTitleBar) getView().findViewById(R.id.title_bar);

    }

    protected void hideSoftKeyboard() {
        if (getActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getActivity().getCurrentFocus() != null)
                inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}
