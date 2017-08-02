package com.onlyhiedu.mobile.UI.Course.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.onlyhiedu.mobile.App.Constants;
import com.onlyhiedu.mobile.Base.BaseActivity;
import com.onlyhiedu.mobile.Model.bean.PingPayStatus;
import com.onlyhiedu.mobile.Model.bean.PingPaySucessInfo;
import com.onlyhiedu.mobile.Model.bean.ProvinceBean;
import com.onlyhiedu.mobile.Model.bean.StudentInfo;
import com.onlyhiedu.mobile.R;
import com.onlyhiedu.mobile.UI.Course.persenter.CoursePayPresenter;
import com.onlyhiedu.mobile.UI.Course.persenter.contract.CoursePayContract;
import com.onlyhiedu.mobile.Utils.JsonUtil;
import com.onlyhiedu.mobile.Utils.UIUtils;
import com.onlyhiedu.mobile.Utils.WheelUtils;
import com.onlyhiedu.mobile.Widget.PayItemView;
import com.onlyhiedu.mobile.Widget.SettingItemView;
import com.pingplusplus.android.Pingpp;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

import static com.onlyhiedu.mobile.R.id.confirm_pay;
import static com.onlyhiedu.mobile.Widget.PayItemView.CHANNEL_ALIPAY;
import static com.onlyhiedu.mobile.Widget.PayItemView.CHANNEL_BDF;
import static com.onlyhiedu.mobile.Widget.PayItemView.CHANNEL_UPACP;
import static com.onlyhiedu.mobile.Widget.PayItemView.CHANNEL_WECHAT;


/**
 * Created by pengpeng on 2017/7/27.
 */

public class CoursePayActivity extends BaseActivity<CoursePayPresenter> implements CoursePayContract.View {


    public static final String TAG = CoursePayActivity.class.getSimpleName();

    @BindView(R.id.rl_edit)
    RelativeLayout mRelativeLayout;
    @BindView(R.id.gradeSubject)
    LinearLayout mGradeSubject;


    //支付View
    @BindView(R.id.pay_item_view)
    PayItemView mPayItemView;

    @BindView(R.id.money)
    TextView tvMoney;


    //优惠码
    @BindView(R.id.coupon)
    EditText mCoupon;

    //确认支付
    @BindView(confirm_pay)
    Button mConfirm_pay;

    private ProgressDialog dialog;
    //年级
    @BindView(R.id.setting_grade)
    SettingItemView mSettingGrade;
    private OptionsPickerView mGradeWheel;
    private ArrayList<ProvinceBean> mGradeData = WheelUtils.getGrade();
    //科目
    @BindView(R.id.setting_subject)
    SettingItemView mSettingSubject;
    private OptionsPickerView mSubject;
    private ArrayList<ProvinceBean> mSubjectData = WheelUtils.getSubject();


    String payMethod;
    @BindView(R.id.scroll_view)
    ScrollView mScrollView;
    @BindView(R.id.tv_offline)
    TextView mTextView;
    @BindView(R.id.tv_course_name)
    TextView mTvCourseName;

    private String mCoursePriceUuid;
    private String mChargeId;

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initView() {
        setToolBar("课程支付");

        mCoursePriceUuid = getIntent().getStringExtra("coursePriceUuid");
        mTvCourseName.setText(getIntent().getStringExtra("coursePricePackageName"));

        mRelativeLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();

                mRelativeLayout.getWindowVisibleDisplayFrame(rect);
                int rootInvisibleHeight = mRelativeLayout.getRootView().getHeight() - rect.bottom;
                Log.d(TAG, "lin.getRootView().getHeight()=" + mRelativeLayout.getRootView().getHeight() + ",rect.bottom=" + rect.bottom + ",rootInvisibleHeight=" + rootInvisibleHeight);
                if (rootInvisibleHeight <= 100) {
                    //软键盘隐藏啦
                    Log.d(TAG, "隐藏啦");
                    if (!TextUtils.isEmpty(mCoupon.getText().toString())) {
                        mPresenter.getPayMoney(mCoursePriceUuid, mCoupon.getText().toString());
                    }
                } else {
                    Log.d(TAG, "软键盘弹出啦");
                }
            }
        });

    }

    @Override
    protected void initData() {
        super.initData();
        mPresenter.getStudentInfo();
    }


    //年级
    OptionsPickerView.OnOptionsSelectListener gradeL = new OptionsPickerView.OnOptionsSelectListener() {
        @Override
        public void onOptionsSelect(int options1, int option2, int options3, View v) {
            String grade = mGradeData.get(options1).getPickerViewText();
            if (TextUtils.isEmpty(mSettingGrade.getDetailText()) || !grade.equals(mSettingGrade.getDetailText())) {
                mPresenter.updateGrade(grade);
                mSettingGrade.setDetailText(grade);
            }
        }
    };
    //科目
    OptionsPickerView.OnOptionsSelectListener subject = new OptionsPickerView.OnOptionsSelectListener() {
        @Override
        public void onOptionsSelect(int options1, int option2, int options3, View v) {
            String grade = mSubjectData.get(options1).getPickerViewText();
            if (TextUtils.isEmpty(mSettingSubject.getDetailText()) || !grade.equals(mSettingSubject.getDetailText())) {
                mPresenter.updateSubject(grade);
                mSettingSubject.setDetailText(grade);
            }
        }
    };

    @Override
    protected int getLayout() {
        return R.layout.activity_course_pay;
    }

    @Override
    public void showError(String msg) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showGetPaySucess(double data) {
        tvMoney.setText(data + "");
    }

    @Override
    public void showPingPaySucess(PingPaySucessInfo info) {
//        Toast.makeText(this, "支付成功", Toast.LENGTH_SHORT).show();
        mChargeId = info.getId();
        Pingpp.createPayment(CoursePayActivity.this, JsonUtil.toJson(info));
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
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        Toast.makeText(this, Constants.NET_ERROR, Toast.LENGTH_SHORT).show();
    }

    @OnClick({R.id.confirm_pay, R.id.setting_grade, R.id.setting_subject})
    public void onClick(View view) {
        switch (view.getId()) {
            case confirm_pay:
                //确认支付
                confirmPayment();
                break;
            case R.id.setting_grade:
                //年级
                if (mGradeWheel == null) {
                    mGradeWheel = WheelUtils.getWhellView(mContext, gradeL, mGradeData);
                }
                mGradeWheel.show();
                break;
            case R.id.setting_subject:
                //科目
                if (mSubject == null) {
                    mSubject = WheelUtils.getWhellView(mContext, subject, mSubjectData);
                }
                mSubject.show();
                break;
//            case R.id.offline:
//                //线下转账
//                checkOfflineVisable();
//                break;
        }
    }


    private void confirmPayment() {
        if (mSettingGrade.getDetailText() == null || TextUtils.isEmpty(mSettingGrade.getDetailText())) {
            Toast.makeText(this, "请填写 年级 信息", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mSettingSubject.getDetailText() == null || TextUtils.isEmpty(mSettingSubject.getDetailText())) {
            Toast.makeText(this, "请填写 科目 信息", Toast.LENGTH_SHORT).show();
            return;
        }
        payMethod = mPayItemView.getPayMethod();
        if (TextUtils.isEmpty(payMethod)) {
            Toast.makeText(this, "请选择支付方式", Toast.LENGTH_SHORT).show();
            return;
        }
        switch (payMethod) {
            case CHANNEL_ALIPAY:
                mPresenter.getPingppPaymentByJson(mCoursePriceUuid, payMethod);
                break;
            case CHANNEL_WECHAT:
                mPresenter.getPingppPaymentByJson(mCoursePriceUuid, payMethod);
                break;
            case CHANNEL_UPACP:
                mPresenter.getPingppPaymentByJson(mCoursePriceUuid, payMethod);
                break;
            case CHANNEL_BDF:
                if (dialog == null) {
                    dialog = ProgressDialog.show(this, null, "请稍后..");
                } else {
                    dialog.show();
                }
                mPresenter.getBaiduPay(mCoursePriceUuid);
                break;
        }
    }


    /**
     * onActivityResult 获得支付结果，如果支付成功，服务器会收到ping++ 服务器发送的异步通知。
     * 最终支付成功根据异步通知为准
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //支付页面返回处理
        if (requestCode == Pingpp.REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getExtras().getString("pay_result");
                /* 处理返回值
                 * "success" - payment succeed
                 * "fail"    - payment failed
                 * "cancel"  - user canceld
                 * "invalid" - payment plugin not installed
                 */
                String errorMsg = data.getExtras().getString("error_msg"); // 错误信息
                String extraMsg = data.getExtras().getString("extra_msg"); // 错误信息
                showMsg(result, errorMsg, extraMsg);
            }
        }
    }

    public void showMsg(String title, String msg1, String msg2) {
        String str = title;
        if (null != msg1 && msg1.length() != 0) {
            str += "\n" + msg1;
        }
        if (null != msg2 && msg2.length() != 0) {
            str += "\n" + msg2;
        }
        if ("success".equals(str) && mChargeId != null && !TextUtils.isEmpty(mChargeId)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    dialog.show();
                    mPresenter.getPingPayStatus(mChargeId);
                }
            }, 1000);
        } else {
            Toast.makeText(this, "支付失败", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void getPingPayStatusSucess(PingPayStatus data) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        Toast.makeText(this, "支付成功", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "" + data.getPayStatus());
    }

    @Override
    public void showStudentInfo(StudentInfo info) {
        if (info != null) {
            mSettingGrade.setDetailText(info.grade);
            mSettingSubject.setDetailText(info.subject);
        }
    }


}
