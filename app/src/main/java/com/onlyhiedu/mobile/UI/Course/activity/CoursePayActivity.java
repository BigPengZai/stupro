package com.onlyhiedu.mobile.UI.Course.activity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.onlyhiedu.mobile.Base.BaseActivity;
import com.onlyhiedu.mobile.Model.bean.PingPaySucessInfo;
import com.onlyhiedu.mobile.R;
import com.onlyhiedu.mobile.UI.Course.persenter.CoursePayPresenter;
import com.onlyhiedu.mobile.UI.Course.persenter.contract.CoursePayContract;
import com.pingplusplus.android.Pingpp;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by pengpeng on 2017/7/27.
 */

public class CoursePayActivity extends BaseActivity<CoursePayPresenter> implements CoursePayContract.View {
    @BindView(R.id.coupon)
    EditText mCoupon;
    //小计
    @BindView(R.id.subtotal)
    TextView mSubtotal;
    private String mCoursePriceUuid;
    //确认支付
    @BindView(R.id.confirm_pay)
    Button mConfirm_pay;
    //支付宝
    @BindView(R.id.alipayButton)
    Button mAlipayButton;
    /**
     * 银联支付渠道
     */
    private static final String CHANNEL_UPACP = "upacp";
    /**
     * 微信支付渠道
     */
    private static final String CHANNEL_WECHAT = "wx";
    /**
     * 支付宝支付渠道
     */
    private static final String CHANNEL_ALIPAY = "alipay";
    @Override
    protected void initInject() {

        getActivityComponent().inject(this);
    }

    @Override
    protected void initView() {
        super.initView();
        setToolBar("课程支付");
        mCoursePriceUuid = getIntent().getStringExtra("coursePriceUuid");
//        String s = mCoursePriceUuid.replaceAll("-", "");
//        mCoursePriceUuid = s;
    }

    @Override
    protected void initData() {
        super.initData();
        mCoupon.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                } else {
                    mPresenter.getPayMoney(mCoursePriceUuid, mCoupon.getText().toString());
                }
            }
        });
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_course_pay;
    }

    @Override
    public void showError(String msg) {

    }

    @Override
    public void showGetPaySucess(double data) {
        mSubtotal.setText(data+"");
    }

    @Override
    public void showPingPaySucess(PingPaySucessInfo info) {
//        Pingpp.createPayment(CoursePayActivity.this, info);
    }

    @OnClick({R.id.confirm_pay,R.id.alipayButton})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.confirm_pay:
                mCoupon.clearFocus();
                break;
            case R.id.alipayButton:
                mPresenter.getPingppPaymentByJson(mCoursePriceUuid,CHANNEL_ALIPAY);
                break;
        }
    }
}
