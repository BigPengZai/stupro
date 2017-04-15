package com.onlyhiedu.mobile.UI.Consumption.activity;

import android.widget.TextView;
import android.widget.Toast;

import com.onlyhiedu.mobile.Base.BaseActivity;
import com.onlyhiedu.mobile.Model.bean.ConsumptionData;
import com.onlyhiedu.mobile.R;
import com.onlyhiedu.mobile.UI.Consumption.presenter.ConsumptionPresenter;
import com.onlyhiedu.mobile.UI.Consumption.presenter.contract.ConsumptionContract;

import java.util.List;

import butterknife.BindView;

/**
 * Created by pengpeng on 2017/4/15.
 */

public class ConsumpActivity extends BaseActivity<ConsumptionPresenter> implements ConsumptionContract.View {
    @BindView(R.id.tv_account)
    TextView mTv_Account;
    //总课时
    @BindView(R.id.tv_total)
    TextView mTv_Total;
    //剩余时间
    @BindView(R.id.tv_remaing)
    TextView mTv_Remaing;
    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_consup;
    }

    @Override
    protected void initData() {
        setToolBar("课时消耗");
       mPresenter.getClassTimeInfo();
    }

    @Override
    public void showSuccess(List<ConsumptionData> data) {
        if (data != null&&data.size()!=0) {
            mTv_Account.setText(data.get(0).classPackageName);
            mTv_Total.setText(data.get(0).totalTime);
            mTv_Remaing.setText(data.get(0).surplusTime);
        }
    }

    @Override
    public void showError(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
