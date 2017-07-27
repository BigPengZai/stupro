package com.onlyhiedu.mobile.UI.Course.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.onlyhiedu.mobile.Base.BaseFragment;
import com.onlyhiedu.mobile.Base.BaseRecyclerAdapter;
import com.onlyhiedu.mobile.Model.bean.CoursePriceList;
import com.onlyhiedu.mobile.Model.bean.CoursePriceTypeInfo;
import com.onlyhiedu.mobile.R;
import com.onlyhiedu.mobile.UI.Course.activity.CoursePayActivity;
import com.onlyhiedu.mobile.UI.Course.adapter.CourseDiscountFragmentAdapter;
import com.onlyhiedu.mobile.UI.Course.persenter.CourseDiscountPresenter;
import com.onlyhiedu.mobile.UI.Course.persenter.contract.CourseDiscountContract;
import com.onlyhiedu.mobile.UI.Home.presenter.CoursePresenter;
import com.onlyhiedu.mobile.Widget.LabelsView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by pengpeng on 2017/7/26.
 * 课程优惠
 */

public class CourseDiscountFragment extends BaseFragment<CourseDiscountPresenter> implements CourseDiscountContract.View,BaseRecyclerAdapter.OnItemClickListener {


    @BindView(R.id.labels)
    LabelsView labelsView;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyview;
    private CourseDiscountFragmentAdapter adapter;
    public static final String TAG = CourseDiscountFragment.class.getSimpleName();
    private String activityType;
    private List<CoursePriceList> mCurrentDataList;
    private Intent mPayIntent;

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_course;
    }

    @Override
    protected void initView() {
        Bundle arguments = getArguments();
        activityType = arguments.getString("tag");
        Log.d(TAG, "tag:" + activityType);
        mPresenter.getCoursePriceTypeListInfo(activityType);
        adapter = new CourseDiscountFragmentAdapter(mContext);
    }

    @Override
    protected void initData() {
        mCurrentDataList = new ArrayList<CoursePriceList>();

        adapter.addAll(mCurrentDataList);
        mRecyview.setLayoutManager(new GridLayoutManager(getActivity(),2));
        mRecyview.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
    }

    @Override
    public void showError(String msg) {

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void showPriceTypeListSuccess(List<CoursePriceTypeInfo> data) {
        ArrayList<String> label = new ArrayList<>();
        for (CoursePriceTypeInfo coursePriceTypeInfo : data) {
            label.add(coursePriceTypeInfo.getType());
        }
        labelsView.setLabels(label);
        mPresenter.getCoursePriceList(activityType,data.get(0).getType());
        //标签的点击监听
        labelsView.setOnLabelClickListener(new LabelsView.OnLabelClickListener() {
            @Override
            public void onLabelClick(View label, String labelText, int position) {
                //label是被点击的标签，labelText是标签的文字，position是标签的位置。
                mPresenter.getCoursePriceList(activityType,labelText);
            }
        });
    }

    @Override
    public void showCoursePriceList(List<CoursePriceList> data) {
        mCurrentDataList.clear();
        mCurrentDataList.addAll(data);
        adapter.clear();
        adapter.addAll(data);
    }

    @Override
    public void onItemClick(int position, long itemId) {
        mPayIntent = new Intent(getActivity(),CoursePayActivity.class);
        mPayIntent.putExtra("coursePriceUuid", mCurrentDataList.get(position).uuid);
        startActivity(mPayIntent);
    }
}
