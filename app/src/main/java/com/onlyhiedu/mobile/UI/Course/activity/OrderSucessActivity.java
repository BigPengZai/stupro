package com.onlyhiedu.mobile.UI.Course.activity;

import android.content.Intent;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.onlyhiedu.mobile.App.AppManager;
import com.onlyhiedu.mobile.Base.SimpleActivity;
import com.onlyhiedu.mobile.R;
import com.onlyhiedu.mobile.UI.Home.activity.MineOrdersActivity;
import com.onlyhiedu.mobile.UI.Home.fragment.OrderFragment;
import com.onlyhiedu.mobile.Utils.AppUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by pengpeng on 2017/8/10.
 */

public class OrderSucessActivity extends SimpleActivity {
    @BindView(R.id.tv_view_order)
    TextView mTv_View_Order;
    private Intent mOrderSucessIntent;
    private String mPackageName;
    private String mOriginalPrice;
    private String mSpecialPrice;
    private String mPayMethod;
    @BindView(R.id.tv_package_name)
    TextView mTvPackageName;

    @BindView(R.id.tv_total)
    TextView mTvTotal;

    @BindView(R.id.tv_specialPrice)
    TextView mTvSpPrice;

    @BindView(R.id.tv_pay_method)
    TextView mTvPayMethod;
    private String pNameDate;
    private String mTotalDate;
    private String mSpecialDate;
    private String mPayDate;
    private String mMPayFrom;

    @Override
    protected int getLayout() {
        return R.layout.activity_ordersucess;
    }

    @Override
    protected void initEventAndData() {
        initIntentDate();
        initDate();
    }

    private void initDate() {
        pNameDate = "课时包： " + mPackageName;
        SpannableStringBuilder pNbuilder = new SpannableStringBuilder(pNameDate);
        ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.parseColor("#F42440"));
        pNbuilder.setSpan(redSpan, 4, pNameDate.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTvPackageName.setText(pNbuilder);

        mTotalDate = "支付金额： " + mOriginalPrice;
        SpannableStringBuilder totalBuilder = new SpannableStringBuilder(mTotalDate);
        totalBuilder.setSpan(redSpan, 5, mTotalDate.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTvTotal.setText(totalBuilder);

        mSpecialDate = "优惠金额： " + mSpecialPrice;
        SpannableStringBuilder sPBuilder = new SpannableStringBuilder(mSpecialDate);
        sPBuilder.setSpan(redSpan, 5, mSpecialDate.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTvSpPrice.setText(sPBuilder);

        mPayDate = "支付方式： " + mPayMethod;
        SpannableStringBuilder payBuilder = new SpannableStringBuilder(mPayDate);
        payBuilder.setSpan(redSpan, 5, mPayDate.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTvPayMethod.setText(payBuilder);
    }

    private void initIntentDate() {
        mMPayFrom = getIntent().getStringExtra("mPayFrom");
        mPackageName = getIntent().getStringExtra("packageName");
        mOriginalPrice = getIntent().getStringExtra("originalPrice");
        mSpecialPrice = getIntent().getStringExtra("specialPrice");
        mPayMethod = getIntent().getStringExtra("payMethod");
        switch (mPayMethod) {
            case "wx":
                mPayMethod = "微信支付";
                break;
            case "upacp":
                mPayMethod = "银联支付";
                break;
            case "alipay":
                mPayMethod = "支付宝支付";
                break;
        }
    }

    @OnClick({R.id.tv_view_order,R.id.tv_order_finsh})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_view_order:
                if ("order".equals(mMPayFrom)) {
                    AppManager.getAppManager().finishActivity(MineOrdersActivity.class);
                }
                mOrderSucessIntent = new Intent(this, MineOrdersActivity.class);
                mOrderSucessIntent.putExtra("mOrderSucessIntent", true);
                startActivity(mOrderSucessIntent);
                AppManager.getAppManager().finishActivity(this);
                AppManager.getAppManager().finishActivity(CoursePayActivity.class);
                AppManager.getAppManager().finishActivity(CourseDiscountActivity.class);
                break;
            case R.id.tv_order_finsh:
                if ("order".equals(mMPayFrom)) {
                    AppManager.getAppManager().finishActivity(MineOrdersActivity.class);
                }
                AppManager.getAppManager().finishActivity(this);
                AppManager.getAppManager().finishActivity(CoursePayActivity.class);
                break;
        }
    }

    @Override
    public void onBackPressedSupport() {
        if ("order".equals(mMPayFrom)) {
            AppManager.getAppManager().finishActivity(MineOrdersActivity.class);
        }
        AppManager.getAppManager().finishActivity(this);
        AppManager.getAppManager().finishActivity(CoursePayActivity.class);
    }
}
