package com.onlyhiedu.mobile.UI.Course.activity;

import android.app.ProgressDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.onlyhiedu.mobile.App.Constants;
import com.onlyhiedu.mobile.Base.BaseActivity;
import com.onlyhiedu.mobile.Model.bean.PingPaySucessInfo;
import com.onlyhiedu.mobile.R;
import com.onlyhiedu.mobile.UI.Course.persenter.CoursePayPresenter;
import com.onlyhiedu.mobile.UI.Course.persenter.contract.CoursePayContract;
import com.onlyhiedu.mobile.Utils.UIUtils;

import butterknife.BindView;
import butterknife.OnClick;

import static com.onlyhiedu.mobile.R.id.alipayButton;
import static com.onlyhiedu.mobile.R.id.bfbButton;
import static com.onlyhiedu.mobile.R.id.confirm_pay;

/**
 * Created by pengpeng on 2017/7/27.
 */

public class CoursePayActivity extends BaseActivity<CoursePayPresenter> implements CoursePayContract.View {

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


    @BindView(R.id.coupon)
    EditText mCoupon;
    //小计
    @BindView(R.id.subtotal)
    TextView mSubtotal;
    private String mCoursePriceUuid;
    //确认支付
    @BindView(confirm_pay)
    Button mConfirm_pay;
    //支付宝
    @BindView(alipayButton)
    Button mAlipayButton;

    private ProgressDialog dialog;


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
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showGetPaySucess(double data) {
        mSubtotal.setText(data + "");
    }

    @Override
    public void showPingPaySucess(PingPaySucessInfo info) {
//        Pingpp.createPayment(CoursePayActivity.this, info);
    }

    @Override
    public void getBaiduPaySuccess(String url) {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        UIUtils.startHomeNewsWebViewAct(this, url, "百度分期");
    }

    @Override
    public void getBaiduPayFailure() {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        Toast.makeText(this, Constants.NET_ERROR, Toast.LENGTH_SHORT).show();
    }

    @OnClick({confirm_pay, alipayButton, bfbButton})
    public void onClick(View view) {
        switch (view.getId()) {
            case confirm_pay:
                mCoupon.clearFocus();
                break;
            case alipayButton:
                mPresenter.getPingppPaymentByJson(mCoursePriceUuid, CHANNEL_ALIPAY);
                break;
            case bfbButton: //百度分期
                if (dialog == null) {
                    dialog = ProgressDialog.show(this, null, "请稍后..");
                } else {
                    dialog.show();
                }
                mPresenter.getBaiduPay(mCoursePriceUuid);
                break;
        }
    }
}
