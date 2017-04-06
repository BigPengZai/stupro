package com.onlyhiedu.mobile.Widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.onlyhiedu.mobile.R;

/**
 * Created by xwc on 2017/3/4.
 */

public class SettingItemView extends RelativeLayout {


    private TextView mTitle, mTvDetail;
    private ImageView mImage;
    private View mDividerView;

    //内容
    private String mDetailText;
    private final ImageView mLeft_icon;


    public SettingItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_setting_item, this, true);

        mTitle = (TextView) findViewById(R.id.title);
        mTvDetail = (TextView) findViewById(R.id.content);
        mImage = (ImageView) findViewById(R.id.right_icon);
        mDividerView = findViewById(R.id.item_bottom_divider);
        mLeft_icon = (ImageView) findViewById(R.id.iv_icon);

        TypedArray localTypedArray = context.obtainStyledAttributes(attrs, R.styleable.setting_view);
        setTitleText(localTypedArray.getString(R.styleable.setting_view_item_titleText));
        setDetailText(localTypedArray.getString(R.styleable.setting_view_item_detailText));
        setShowDivider(localTypedArray.getBoolean(R.styleable.setting_view_item_showDivider, true));
        setLiftImage(localTypedArray.getResourceId(R.styleable.setting_view_item_icon_left, 0));
    }

    public String getDetailText() {
        return mDetailText;
    }

    public void setTitleText(String titleText) {
        mTitle.setText(titleText);
    }

    public void setDetailText(String detailText) {
        if (detailText == null) {
            mTvDetail.setVisibility(GONE);
        } else {
            mTvDetail.setVisibility(VISIBLE);
            mDetailText = detailText;
            mTvDetail.setText(detailText);
        }

    }

    public void setShowDivider(boolean showDivider) {
        mDividerView.setVisibility(showDivider ? View.VISIBLE : View.GONE);
    }

    public void hintRightImage() {
        mImage.setVisibility(GONE);
    }

    public void setLiftImage(int resId) {
        if (resId == 0) {
            mLeft_icon.setVisibility(GONE);
            return;
        }
        mLeft_icon.setImageResource(resId);
    }

}
