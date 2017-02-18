package com.onlyhiedu.mobile.Widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.onlyhiedu.mobile.R;


/**
 * Created by Fon on 2016/5/15.
 */
public class SettingItem extends RelativeLayout {

    /**
     * 切换按钮
     */
    public static final int ACCESSORY_TYPE_CHECKBOX = 2;

    /**
     * Item内容区域
     */
    private LinearLayout mContent;
    /**
     * 标题View
     */
    private TextView mTitle;
    /**
     * 概要View
     */
    private TextView mSummary;
    /**
     * 切换View
     */
    private CheckedTextView mCheckedTextView;
    private TextView mNewUpdate;
    /**
     * 分割线
     */
    private View mDividerView;
    /**
     * 附加类型
     */
    private int mAccessoryType;
    /**
     * 是否显示分割线
     */
    private boolean mShowDivider;
    /**
     * 标题
     */
    private String mTitleText;

    /**
     * 图标
     */
    private int iconId;
    /**
     * 概要文字
     */
    private String mSummaryText;
    private int[] mInsetDrawableRect = {0, 0, 0, 0};

    /**
     * @param context
     * @param attrs
     */
    public SettingItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.detaillist_item, this, true);

        mContent = (LinearLayout) findViewById(R.id.content);
        mTitle = (TextView) findViewById(android.R.id.title);
        mSummary = (TextView) findViewById(android.R.id.summary);
        mNewUpdate = (TextView) findViewById(R.id.text_tv_one);
        mCheckedTextView = (CheckedTextView) findViewById(R.id.accessory_checked);
        mDividerView = findViewById(R.id.item_bottom_divider);

        TypedArray localTypedArray = context.obtainStyledAttributes(attrs, R.styleable.setting_info);
        setTitleText(localTypedArray.getString(R.styleable.setting_info_item_titleText));
        setDetailText(localTypedArray.getString(R.styleable.setting_info_item_detailText));
        setAccessoryType(localTypedArray.getInt(R.styleable.setting_info_item_accessoryType, 0));
        setShowDivider(localTypedArray.getBoolean(R.styleable.setting_info_item_showDivider, true));


        int indexCount = localTypedArray.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            int att = localTypedArray.getIndex(i);
            switch (att) {
                case R.styleable.setting_info_item_icon:
                    Drawable drawable = localTypedArray.getDrawable(att);
                    mTitle.setCompoundDrawablesWithIntrinsicBounds( drawable, null, null, null);
                    break;
            }
        }
        localTypedArray.recycle();
        mNewUpdate.setVisibility(View.GONE);
    }

    /**
     * @param showDivider
     */
    private void setShowDivider(boolean showDivider) {
        mShowDivider = showDivider;
        View dividerView = mDividerView;
        dividerView.setVisibility(mShowDivider ? View.VISIBLE : View.GONE);
    }

    /**
     * 设置标题信息
     *
     * @param text
     */
    public void setTitleText(String text) {
        mTitleText = text;
        if (text == null) {
            mTitle.setText("");
            return;
        }
        mTitle.setText(mTitleText);
    }


    public void setTitleIcon(int id) {
        iconId = id;
        if (iconId == 0) {
            return;
        }
        Drawable icon = getResources().getDrawable(id);
        icon.setBounds(0, 0, icon.getMinimumWidth(), icon.getMinimumHeight());
        mTitle.setCompoundDrawables(icon, null, null, null);
    }

    public void setCheckText(String text) {
        if (text == null) {
            mCheckedTextView.setText("");
            return;
        }
        mCheckedTextView.setText(text);
    }

    /**
     * @param text
     */
    public void setDetailText(String text) {
        mSummaryText = text;
        if (text == null) {
            mSummary.setText("");
            mSummary.setVisibility(View.GONE);
            return;
        }
        mSummary.setText(mSummaryText);
        mSummary.setVisibility(View.VISIBLE);
    }

    @SuppressWarnings("deprecation")
    private void setSettingBackground(int resid) {
        int[] rect = new int[4];
        rect[0] = getPaddingLeft();
        rect[1] = getPaddingTop();
        rect[2] = getPaddingRight();
        rect[3] = getPaddingBottom();
        if (isInsetDrawable()) {
            setBackgroundDrawable(new InsetDrawable(getContext().getResources()
                    .getDrawable(resid), mInsetDrawableRect[0],
                    mInsetDrawableRect[1], mInsetDrawableRect[2],
                    mInsetDrawableRect[3]));
        } else {
            setBackgroundResource(resid);
        }
        setPadding(rect[0], rect[1], rect[2], rect[3]);
    }

    /**
     * 是否显示版本更新
     *
     * @param visibility
     */
    public void setNewUpdateVisibility(boolean visibility) {
        if (mNewUpdate != null) {
            mNewUpdate.setVisibility(visibility ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * @return
     */
    private boolean isInsetDrawable() {
        for (int i = 0; i < mInsetDrawableRect.length; i++) {
            if (mInsetDrawableRect[i] <= 0) {
                continue;
            }
            return true;
        }
        return false;
    }

    /**
     * @param accessoryType
     */
    private void setAccessoryType(int accessoryType) {
        if (accessoryType == ACCESSORY_TYPE_CHECKBOX) {
            mAccessoryType = ACCESSORY_TYPE_CHECKBOX;

            //TODO
//            mCheckedTextView.setCheckMarkDrawable(getContext().getResources().getDrawable(R.mipmap.right));
            mCheckedTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            setSettingBackground(0);
            return;
        }
    }

    /**
     * 返回切换按钮
     *
     * @return
     */
    public CheckedTextView getCheckedTextView() {
        return mCheckedTextView;
    }

    /**
     * 是否处于选中状态
     *
     * @return
     */
    public boolean isChecked() {
        if (mAccessoryType == ACCESSORY_TYPE_CHECKBOX) {
            return mCheckedTextView.isChecked();
        }
        return true;
    }

    /**
     * 设置状态
     *
     * @param checked
     */
    public void setChecked(boolean checked) {
        if (mAccessoryType != ACCESSORY_TYPE_CHECKBOX) {
            return;
        }
        mCheckedTextView.setChecked(checked);
    }


    public void toggle() {
        if (mAccessoryType != ACCESSORY_TYPE_CHECKBOX) {
            return;
        }
        mCheckedTextView.toggle();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

}
