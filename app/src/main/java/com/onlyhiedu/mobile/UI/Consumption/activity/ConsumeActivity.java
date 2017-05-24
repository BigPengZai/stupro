package com.onlyhiedu.mobile.UI.Consumption.activity;

import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.onlyhiedu.mobile.Base.BaseActivity;
import com.onlyhiedu.mobile.Model.bean.ConsumptionData;
import com.onlyhiedu.mobile.R;
import com.onlyhiedu.mobile.UI.Consumption.adapter.ConsumeAdapter;
import com.onlyhiedu.mobile.UI.Consumption.presenter.ConsumptionPresenter;
import com.onlyhiedu.mobile.UI.Consumption.presenter.contract.ConsumptionContract;
import com.onlyhiedu.mobile.Utils.UIUtils;

import java.util.List;

import butterknife.BindView;

/**
 * Created by pengpeng on 2017/4/15.
 */

public class ConsumeActivity extends BaseActivity<ConsumptionPresenter> implements ConsumptionContract.View {


    private ConsumeAdapter mAdapter;


    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;


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
        if (data != null && data.size() != 0) {
            mAdapter = new ConsumeAdapter(this);
            UIUtils.setRcDecorationAndLayoutManager(mContext, mRecyclerView, mAdapter);
            mAdapter.addAll(data);
        }
    }

    @Override
    public void showError(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
