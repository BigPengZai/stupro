package com.onlyhiedu.mobile.Widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.onlyhiedu.mobile.R;

/**
 * Created by Administrator on 2017/8/2.
 */

public class PayItemView extends RelativeLayout implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {


//    private TextView mTitle, mTvDetail;
//    private ImageView mImage;
//    private View mDividerView;
//
//    //内容
//    private String mDetailText;
//    private final ImageView mLeft_icon;


    /**
     * 银联支付渠道
     */
    public static final String CHANNEL_UPACP = "upacp";
    /**
     * 微信支付渠道
     */
    public static final String CHANNEL_WECHAT = "wx";
    /**
     * 支付宝支付渠道
     */
    public static final String CHANNEL_ALIPAY = "alipay";
    /**
     * 百度分期
     */
    public static final String CHANNEL_BDF = "bdf";
    /**
     * 线下转账
     */
    public static final String CHANNEL_OFFLINE = "offline";

    CheckBox mCheckYl;
    CheckBox mCheckWx;
    CheckBox mCheckAlipay;
    CheckBox mCheckBdf;
    CheckBox mCheckOffline;

    RelativeLayout mRlBdf;
    RelativeLayout mRlWx;
    RelativeLayout mRlYl;
    RelativeLayout mRl_Offline;
    RelativeLayout mRlAlipay;
    LinearLayout mLl_Offline;
    private String payMethod;

    public String getPayMethod() {
        return payMethod;
    }

    public PayItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_pay_item, this, true);

        mRlBdf = (RelativeLayout) findViewById(R.id.rl_bdf);
        mRlWx = (RelativeLayout) findViewById(R.id.rl_wx);
        mRlYl = (RelativeLayout) findViewById(R.id.rl_yl);
        mRlAlipay = (RelativeLayout) findViewById(R.id.rl_alipay);


        mRlYl.setOnClickListener(this);
        mRlWx.setOnClickListener(this);
        mRlAlipay.setOnClickListener(this);
        mRlBdf.setOnClickListener(this);

        mCheckYl = (CheckBox) findViewById(R.id.check_yl);
        mCheckWx = (CheckBox) findViewById(R.id.check_wx);
        mCheckAlipay = (CheckBox) findViewById(R.id.check_alipay);
        mCheckBdf = (CheckBox) findViewById(R.id.check_bdf);
        mCheckOffline = (CheckBox) findViewById(R.id.check_offline);


        mCheckYl.setOnClickListener(this);
        mCheckWx.setOnClickListener(this);
        mCheckAlipay.setOnClickListener(this);
        mCheckBdf.setOnClickListener(this);

        //线下转账
        mRl_Offline = (RelativeLayout) findViewById(R.id.rl_offline);
        mLl_Offline = (LinearLayout) findViewById(R.id.ll_offline);
        mCheckOffline.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.rl_yl:
                payMethod = CHANNEL_UPACP;
                mCheckYl.setChecked(true);
                mCheckWx.setChecked(false);
                mCheckAlipay.setChecked(false);
                mCheckBdf.setChecked(false);
                mCheckOffline.setChecked(false);
                break;
            case R.id.rl_wx:
                payMethod = CHANNEL_WECHAT;
                mCheckYl.setChecked(false);
                mCheckWx.setChecked(true);
                mCheckAlipay.setChecked(false);
                mCheckBdf.setChecked(false);
                mCheckOffline.setChecked(false);
                break;
            case R.id.rl_alipay:
                payMethod = CHANNEL_ALIPAY;
                mCheckYl.setChecked(false);
                mCheckWx.setChecked(false);
                mCheckAlipay.setChecked(true);
                mCheckBdf.setChecked(false);
                mCheckOffline.setChecked(false);
                break;
            case R.id.rl_bdf:
                payMethod = CHANNEL_BDF;
                mCheckYl.setChecked(false);
                mCheckWx.setChecked(false);
                mCheckAlipay.setChecked(false);
                mCheckBdf.setChecked(true);
                mCheckOffline.setChecked(false);
                break;
            case R.id.check_yl:
                payMethod = CHANNEL_UPACP;
                mCheckYl.setChecked(true);
                mCheckWx.setChecked(false);
                mCheckAlipay.setChecked(false);
                mCheckBdf.setChecked(false);
                mCheckOffline.setChecked(false);
                break;
            case R.id.check_wx:
                payMethod = CHANNEL_WECHAT;
                mCheckYl.setChecked(false);
                mCheckWx.setChecked(true);
                mCheckAlipay.setChecked(false);
                mCheckBdf.setChecked(false);
                mCheckOffline.setChecked(false);
                break;
            case R.id.check_alipay:
                payMethod = CHANNEL_ALIPAY;
                mCheckYl.setChecked(false);
                mCheckWx.setChecked(false);
                mCheckAlipay.setChecked(true);
                mCheckBdf.setChecked(false);
                mCheckOffline.setChecked(false);
                break;
            case R.id.check_bdf:
                payMethod = CHANNEL_BDF;
                mCheckYl.setChecked(false);
                mCheckWx.setChecked(false);
                mCheckAlipay.setChecked(false);
                mCheckBdf.setChecked(true);
                mCheckOffline.setChecked(false);
                break;
            case R.id.check_offline:
                payMethod = CHANNEL_OFFLINE;
                mCheckYl.setChecked(false);
                mCheckWx.setChecked(false);
                mCheckAlipay.setChecked(false);
                mCheckBdf.setChecked(false);
                mCheckOffline.setChecked(true);
                break;
        }
        initOffline(mCheckOffline.isChecked());
    }

    private void initOffline(boolean ischecked) {
        if (ischecked) {
            mLl_Offline.setVisibility(VISIBLE);
        } else {
            mLl_Offline.setVisibility(GONE);
        }
            /*mLl_Offline.animate().translationY(mLl_Offline.getHeight()).setInterpolator(new AccelerateInterpolator(2));
            mLl_Offline.animate().setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    mLl_Offline.setVisibility(VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animator animation) {

                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });*/
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
    }
}
