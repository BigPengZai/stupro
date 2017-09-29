package com.onlyhiedu.mobile.Widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by Administrator on 2017/3/23.
 */

public class MyScrollView extends ScrollView {

    private boolean isIntercept;

    public MyScrollView(Context context) {
        super(context);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setIntercept(boolean intercept) {
        isIntercept = intercept;
    }

    public boolean isIntercept() {
        return isIntercept;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return !isIntercept;
    }

}
