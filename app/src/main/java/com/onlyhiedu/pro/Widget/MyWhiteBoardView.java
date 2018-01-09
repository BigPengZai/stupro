package com.onlyhiedu.pro.Widget;

import android.content.Context;
import android.util.AttributeSet;

import cn.robotpen.model.entity.note.TrailsEntity;
import cn.robotpen.views.widget.WhiteBoardView;

/**
 * Created by Administrator on 2017/9/23.
 */

public class MyWhiteBoardView extends WhiteBoardView {
    public MyWhiteBoardView(Context context) {
        super(context);
    }

    public MyWhiteBoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyWhiteBoardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setBg(String url) {
        this.setBgPhotoSelf(url);
    }

    public void drawTrailsPoint(TrailsEntity trails) {
        this.mPenDrawView.drawTrailsPoint(trails,true);
    }
}
