package com.onlyhiedu.mobile.UI.Consumption.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.onlyhiedu.mobile.Base.SimpleActivity;
import com.onlyhiedu.mobile.Model.bean.ConsumptionList;
import com.onlyhiedu.mobile.R;
import com.onlyhiedu.mobile.UI.Consumption.adapter.ConsumtionAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by pengpeng on 2017/4/6.
 * 课时消耗
 */

public class ConsumptionActivity extends SimpleActivity {
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private List<ConsumptionList.ListBean> data;
    private ConsumptionList.ListBean mE;
    private ConsumptionList.ListBean mE1;
    private ConsumptionList.ListBean mE2;
    private ConsumptionList.ListBean mE3;

    public static final String TAG = ConsumptionActivity.class.getSimpleName();
    @Override
    protected int getLayout() {
        return R.layout.activity_consumption;
    }

    @Override
    protected void initEventAndData() {
        setToolBar("课时消耗");
        data = new ArrayList<>();
        mE = new ConsumptionList.ListBean();
        data.add(mE);
        mE1 = new ConsumptionList.ListBean();
        mE1.setTeacherName("张三");
        mE1.setTotal("180");
        mE1.setRemaing("80");
        data.add(mE1);
        mE2 = new ConsumptionList.ListBean();
        mE2.setTeacherName("张三");
        mE2.setTotal("180");
        mE2.setRemaing("80");
        data.add(mE2);
        mE3 = new ConsumptionList.ListBean();
        mE3.setTeacherName("张三");
        mE3.setTotal("180");
        mE3.setRemaing("80");
        data.add(mE3);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(new ConsumtionAdapter(this, data));

    }
}
