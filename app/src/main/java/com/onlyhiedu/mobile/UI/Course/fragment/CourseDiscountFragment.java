package com.onlyhiedu.mobile.UI.Course.fragment;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.onlyhiedu.mobile.R;
import com.onlyhiedu.mobile.UI.Course.adapter.CourseDiscountFragmentAdapter;
import com.onlyhiedu.mobile.UI.Home.presenter.CoursePresenter;
import com.onlyhiedu.mobile.Widget.LabelsView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by pengpeng on 2017/7/26.
 * 课程优惠
 */

public class CourseDiscountFragment extends BaseFragment<CoursePresenter> implements View.OnClickListener {


    @BindView(R.id.labels)
    LabelsView labelsView;

    @BindView(R.id.ll_course)
    GridLayout mLl_Course;

    @BindView(R.id.ll_first)
    LinearLayout ll_first;
    @BindView(R.id.ll_second)
    LinearLayout ll_second;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyview;
    private CourseDiscountFragmentAdapter adapter;
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
        adapter = new CourseDiscountFragmentAdapter(mContext);
        mLl_Course.removeAllViews();
        //测试的数据
        ArrayList<String> label = new ArrayList<>();
        label.add("小学");
        label.add("初一");
        label.add("初二");
        label.add("初三");
        label.add("高一");
        label.add("高二");
        label.add("高三");
        label.add("清北");
        label.add("初三精品");
        label.add("高三精品");
        for (int i = 0; i < label.size(); i++) {
            Button textView = new Button(getActivity());
            textView.setText(label.get(i));
            textView.setId(i);
            textView.setOnClickListener(this);
            mLl_Course.addView(textView);
        }
        labelsView.setLabels(label);

        //标签的点击监听
        labelsView.setOnLabelClickListener(new LabelsView.OnLabelClickListener() {
            @Override
            public void onLabelClick(View label, String labelText, int position) {
                //label是被点击的标签，labelText是标签的文字，position是标签的位置。
                Toast.makeText(getActivity(), "" + labelText, Toast.LENGTH_SHORT).show();
            }
        });
       List<String> arrayList = new ArrayList<String>();
        arrayList.add("");
        arrayList.add("");
        arrayList.add("");
        arrayList.add("");
        arrayList.add("");
        arrayList.add("");
        arrayList.add("");
        adapter.addAll(arrayList);
        mRecyview.setLayoutManager(new GridLayoutManager(getActivity(),2));
        mRecyview.setAdapter(adapter);
    }

    @Override
    protected void initData() {

        ArrayList<String> arr1 = new ArrayList<>();
        arr1.add("小学");
        arr1.add("初一");
        arr1.add("初二");
        arr1.add("初三");
        arr1.add("高一");
        for (int i = 0; i < arr1.size(); i++) {
            TextView textView = new TextView(getActivity());
            textView.setText(arr1.get(i));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
            params.gravity = Gravity.CENTER_HORIZONTAL;
            textView.setLayoutParams(params);
            ll_first.addView(textView);
        }
        ArrayList<String> arr2 = new ArrayList<>();
        arr2.add("高二");
        arr2.add("高三");
        arr2.add("清北");
        arr2.add("初三精品");
        arr2.add("高三精品");
        for (int i = 0; i < arr2.size(); i++) {
            TextView textView = new TextView(getActivity());
            textView.setText(arr2.get(i));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
            params.gravity = Gravity.CENTER_VERTICAL;
            textView.setLayoutParams(params);
            ll_second.addView(textView);
        }



    }

    @Override
    public void showError(String msg) {

    }

    @Override
    public void onClick(View v) {
        Toast.makeText(getActivity(), "" + v.getId(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mLl_Course != null) {
            mLl_Course.removeAllViews();
        }
    }
}
