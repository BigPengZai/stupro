package com.onlyhiedu.mobile.Widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;

import com.onlyhiedu.mobile.R;

/**
 * Created by Administrator on 2017/8/2.
 */

public class PayItemView extends RelativeLayout implements View.OnClickListener {


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
    CheckBox mCheckYl;
    CheckBox mCheckWx;
    CheckBox mCheckAlipay;
    CheckBox mCheckBdf;

    RelativeLayout mRlBdf;
    RelativeLayout mRlWx;
    RelativeLayout mRlYl;
    RelativeLayout mRlAlipay;

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
                break;
            case R.id.rl_wx:
                payMethod = CHANNEL_WECHAT;
                mCheckYl.setChecked(false);
                mCheckWx.setChecked(true);
                mCheckAlipay.setChecked(false);
                mCheckBdf.setChecked(false);
                break;
            case R.id.rl_alipay:
                payMethod = CHANNEL_ALIPAY;
                mCheckYl.setChecked(false);
                mCheckWx.setChecked(false);
                mCheckAlipay.setChecked(true);
                mCheckBdf.setChecked(false);
                break;
            case R.id.rl_bdf:
                payMethod = CHANNEL_BDF;
                mCheckYl.setChecked(false);
                mCheckWx.setChecked(false);
                mCheckAlipay.setChecked(false);
                mCheckBdf.setChecked(true);
                break;
        }
    }
}
